## 消息循环机制

我们都知道，Android应用程序是通过消息来驱动的，整个机制是围绕着消息的产生以及处理而展开的。消息机制的三大要点：消息队列、消息循环（分发）、消息发送与处理。

### 1. 消息队列

Android应用程序线程的消息队列是使用一个MessageQueue对象来描述的，它可以通过调用Looper类的静态成员函数`prepareMainLooper`或者`prepare`
来创建，其中，前者用来为应用程序的主线程创建消息队列；后者用来为应用程序的其它子线程创建消息队列。

- 创建消息队列

`prepareMainLooper`和`prepare`的实现：

```
public static void prepare() {
        prepare(true);
    }
    
public static void prepareMainLooper() {
        prepare(false);
        synchronized (Looper.class) {
            if (sMainLooper != null) {
                throw new IllegalStateException("The main Looper has already been prepared.");
            }
            sMainLooper = myLooper();
        }
    }
```

不管是在主线程中prepare还是在其它线程中，最终调用的方法都是`prepare(boolean quitAllowed)`方法，进一步来看下具体实现。

```
private static void prepare(boolean quitAllowed) {
        if (sThreadLocal.get() != null) {
            throw new RuntimeException("Only one Looper may be created per thread");
        }
        //当前线程创建唯一的loop对象
        sThreadLocal.set(new Looper(quitAllowed));
    }
```

程序最后一行中，为当前的线程创建唯一的loop对象。

loop的构造方法如下：

```
private Looper(boolean quitAllowed) {
        //创建消息队列
        mQueue = new MessageQueue(quitAllowed);
        mThread = Thread.currentThread();
    }
```

程序到了上面后开始创建消息队列，MessageQueue的构造方法如下：

```
MessageQueue(boolean quitAllowed) {
        mQuitAllowed = quitAllowed;
        //调用JNI方法创建消息队列
        mPtr = nativeInit();
    }
```

可以发现java层的MessageQueue是由JNI层的`nativeInit`方法实现的。
> 到了JNI层IDE上面就看不到具体实现了，这里推荐一个在线的源码阅读地址：[点击查看](http://androidxref.com/)

在`/frameworks/base/core/jni/android_os_MessageQueue.cpp`找到`android_os_MessageQueue.cpp`
文件，这里我们先看`nativeInit`方法的实现

```

sp<MessageQueue> android_os_MessageQueue_getMessageQueue(JNIEnv* env, jobject messageQueueObj) {
    //java 层messageQueue与当前JNI的MessageQueue关联
   jint intPtr = env->GetIntField(messageQueueObj, gMessageQueueClassInfo.mPtr);
    return reinterpret_cast<NativeMessageQueue*>(intPtr);
}

static jlong android_os_MessageQueue_nativeInit(JNIEnv* env, jclass clazz) {
   NativeMessageQueue* nativeMessageQueue = new NativeMessageQueue();
   if (!nativeMessageQueue) {
        jniThrowRuntimeException(env, "Unable to allocate native queue");
       return 0;
    }
    //强引用计数加1
    nativeMessageQueue->incStrong(env);
    //将指针强转化为java long类型
    return reinterpret_cast<jlong>(nativeMessageQueue);
}


```

在C++层，实现Java层的MessageQueue新建了NativeMessageQueue与java层的相关联，并将生成的nativeMessageQueue的内存地址返回到java层。

在NativeMessageQueue的构造方法中，新建了JNI层的loop对象：

```
NativeMessageQueue::NativeMessageQueue() :
       mPollEnv(NULL), mPollObj(NULL), mExceptionObj(NULL) {
    mLooper = Looper::getForThread();
    if (mLooper == NULL) {
          //创建JNI层的Looper对象
        mLooper = new Looper(false);
        Looper::setForThread(mLooper);
   }
}
```

找到路径`/system/core/libutils/Looper.cpp`，我们来看Looper的具体实现。

```
Looper::Looper(bool allowNonCallbacks) :
        mAllowNonCallbacks(allowNonCallbacks), mSendingMessage(false),
        mResponseIndex(0), mNextMessageUptime(LLONG_MAX) {
    int wakeFds[2];//准备两个文件描述符
    int result = pipe(wakeFds);//创建一个管道
    LOG_ALWAYS_FATAL_IF(result != 0, "Could not create wake pipe.  errno=%d", errno);

    mWakeReadPipeFd = wakeFds[0];//管道读端文件描述符
    mWakeWritePipeFd = wakeFds[1];//管道写端文件描述符

    result = fcntl(mWakeReadPipeFd, F_SETFL, O_NONBLOCK);//将管道读端设为非阻塞模式
    LOG_ALWAYS_FATAL_IF(result != 0, "Could not make wake read pipe non-blocking.  errno=%d",
            errno);

    result = fcntl(mWakeWritePipeFd, F_SETFL, O_NONBLOCK);//管道写端同样设为非阻塞
    LOG_ALWAYS_FATAL_IF(result != 0, "Could not make wake write pipe non-blocking.  errno=%d",
            errno);

    mIdling = false;

    // Allocate the epoll instance and register the wake pipe.
    mEpollFd = epoll_create(EPOLL_SIZE_HINT);//创建一个epoll专用的文件描述符
    LOG_ALWAYS_FATAL_IF(mEpollFd < 0, "Could not create epoll instance.  errno=%d", errno);
    
	//epoll其中一个专用结构体
    struct epoll_event eventItem;
    //把结构体清零
    memset(& eventItem, 0, sizeof(epoll_event)); // zero out unused members of data field union
    //重新赋值
    eventItem.events = EPOLLIN;//EPOLLIN ：表示对应的文件描述符可以读；
    eventItem.data.fd = mWakeReadPipeFd;//fd：关联的文件描述符；
    //epoll_ctl函数用于控制某个epoll文件描述符上的事件，可以注册事件，修改事件，删除事件。这里是添加事件 
    result = epoll_ctl(mEpollFd, EPOLL_CTL_ADD, mWakeReadPipeFd, & eventItem);
    LOG_ALWAYS_FATAL_IF(result != 0, "Could not add wake read pipe to epoll instance.  errno=%d",
            errno);
}
```

上述代码中创建的管道非常的重要，它有两个文件描述符：`mWakeReadPipeFd`（管道读端文件描述符）和`mWakeWritePipeFd`
（管道写端描述符）。首先，当一个线程没有新的消息处理时，它就会睡眠在这个管道的读端文件描述符上，直到有新的消息需要处理为止；其次，当其它线程向这个线程的消息队列发送一个消息之后，其它线程就会通过这个管道的写端文件描述符往这个管道写入数据，，从而将这个线程唤醒，以便它可以对刚才发送到它的消息队列中的消息进行处理。
> epoll是Linux内核为处理大批量文件描述符而作了改进的poll，是Linux下多路复用IO接口select/poll的增强版本，它能显著提高程序在大量并发连接中只有少量活跃的情况下的系统CPU利用率。另一点原因就是获取事件的时候，它无须遍历整个被侦听的描述符集，只要遍历那些被内核IO事件异步唤醒而加入Ready队列的描述符集合就行了。epoll除了提供select/poll那种IO事件的水平触发（Level Triggered）外，还提供了边缘触发（Edge Triggered），这就使得用户空间程序有可能缓存IO状态，减少epoll_wait/epoll_pwait的调用，提高应用程序效率。后面在回答为什么死循环不会导致app卡死也是利用了这机制的这个特点。

### 2. 消息循环过程

在looper中建立完毕消息队列后，就会进入循环了，我们这里来看下looper的静态方法`Looper.loop()`，为了不影响阅读我把理解都加到代码的注释中，之外就不做过多解释。

```
public static void loop() {
        final Looper me = myLooper();
        if (me == null) {
            throw new RuntimeException("No Looper; Looper.prepare() wasn't called on this thread.");
        }
        final MessageQueue queue = me.mQueue;

       ················
       //进入死循环，获取message并处理它
        for (;;) {
            //从队列中获取下一个消息
            Message msg = queue.next(); // might block
            if (msg == null) {
                // No message indicates that the message queue is quitting.
                如果没有消息循环中止
                return;
            }

           ················
           
            msg.target.dispatchMessage(msg);
           
           ················
            //回收消息即便还在使用
            msg.recycleUnchecked();
        }
    }
```

我们都知道消息队列是遵循先进先出的，那么为什么会这样呢？我们开看下Message的结构：

```
    /*package*/ int flags;

    /*package*/ long when;
    
    /*package*/ Bundle data;
    
    /*package*/ Handler target;
    
    /*package*/ Runnable callback;
    
    // sometimes we store linked lists of these things
    /*package*/ Message next;//链表结构
    private static final Object sPoolSync = new Object();
    private static Message sPool;//本地静态变量，可避免创建多个Messager
    private static int sPoolSize = 0;

    private static final int MAX_POOL_SIZE = 50;
    
    public static Message obtain() {
        synchronized (sPoolSync) {
            if (sPool != null) {
                Message m = sPool;
                sPool = m.next;
                m.next = null;
                m.flags = 0; // clear in-use flag
                sPoolSize--;
                return m;
            }
        }
        return new Message();
    }
```

不难发现，Message是链表结构。回归正题，我们接着看MessageQueue如何取Message的

```
Message next() {
        // Return here if the message loop has already quit and been disposed.
        // This can happen if the application tries to restart a looper after quit
        // which is not supported.
        final long ptr = mPtr;
        //如果队列已经退出，则返回空
        if (ptr == 0) {
            return null;
        }
        //等待的闲置Handdler的数目
        int pendingIdleHandlerCount = -1; // -1 only during first iteration
        //下一个消息执行需要的时间
        int nextPollTimeoutMillis = 0;
        for (;;) {
            if (nextPollTimeoutMillis != 0) {
                //进入睡眠状态时，将当前线程中挂起的所有Binder命令刷新到内核驱动程序
                Binder.flushPendingCommands();
            }
            //检查当前线程是否有新的消息需要处理，具体实现下面会讲
            nativePollOnce(ptr, nextPollTimeoutMillis);

            synchronized (this) {
                // Try to retrieve the next message.  Return if found.
                final long now = SystemClock.uptimeMillis();
                Message prevMsg = null;
                Message msg = mMessages;//取出表头
                if (msg != null && msg.target == null) {
                    // Stalled by a barrier.  Find the next asynchronous message in the queue.
                    //当前消息的Handler处理器为空，证明这是个barrier，会拦截当前的队列，直到不是异步消息为止。
                    do {
                        prevMsg = msg;
                        msg = msg.next;
                    } while (msg != null && !msg.isAsynchronous());
                }
                if (msg != null) {
                    if (now < msg.when) {
                        // Next message is not ready.  Set a timeout to wake up when it is ready.
                        nextPollTimeoutMillis = (int) Math.min(msg.when - now, Integer.MAX_VALUE);
                    } else {
                        // Got a message.
                        mBlocked = false;
                        if (prevMsg != null) {
                            prevMsg.next = msg.next;
                        } else {
                            mMessages = msg.next;
                        }
                        msg.next = null;
                        if (DEBUG) Log.v(TAG, "Returning message: " + msg);
                        msg.markInUse();
                        return msg;
                    }
                } else {
                    // No more messages.
                    nextPollTimeoutMillis = -1;
                }

                // Process the quit message now that all pending messages have been handled.
                if (mQuitting) {
                    dispose();
                    return null;
                }

                // If first time idle, then get the number of idlers to run.
                // Idle handles only run if the queue is empty or if the first message
                // in the queue (possibly a barrier) is due to be handled in the future.
                //当线程发现它的消息队列没有新的消息需要处理时，不是马上就进入睡眠等待状态，而是先调用注册到它的消息队列中的IdleHandler对象的成员函数queueIdle，一遍它们有机会在线程空闲时执行一些操作。
                if (pendingIdleHandlerCount < 0
                        && (mMessages == null || now < mMessages.when)) {
                    pendingIdleHandlerCount = mIdleHandlers.size();
                }
                if (pendingIdleHandlerCount <= 0) {
                    // No idle handlers to run.  Loop and wait some more.
                    mBlocked = true;
                    continue;
                }

                if (mPendingIdleHandlers == null) {
                    mPendingIdleHandlers = new IdleHandler[Math.max(pendingIdleHandlerCount, 4)];
                }
                mPendingIdleHandlers = mIdleHandlers.toArray(mPendingIdleHandlers);
            }

            // Run the idle handlers.
            // We only ever reach this code block during the first iteration.
            for (int i = 0; i < pendingIdleHandlerCount; i++) {
                final IdleHandler idler = mPendingIdleHandlers[i];
                mPendingIdleHandlers[i] = null; // release the reference to the handler

                boolean keep = false;
                try {
                    keep = idler.queueIdle();
                } catch (Throwable t) {
                    Log.wtf(TAG, "IdleHandler threw exception", t);
                }

                if (!keep) {
                    synchronized (this) {
                        mIdleHandlers.remove(idler);
                    }
                }
            }

            // Reset the idle handler count to 0 so we do not run them again.
            pendingIdleHandlerCount = 0;

            // While calling an idle handler, a new message could have been delivered
            // so go back and look again for a pending message without waiting.
            nextPollTimeoutMillis = 0;
        }
```

JNI层是如何检测是否有新的消息的呢？我们来看下`nativePollOnce(ptr, nextPollTimeoutMillis);`
还是在路径`/frameworks/base/core/jni/android_os_MessageQueue.cpp`

```
static void android_os_MessageQueue_nativePollOnce(JNIEnv* env, jobject obj,
       jlong ptr, jint timeoutMillis) {
    //obj参数指向了一个Java层的MessageQueue对象，参数ptr指向了这个MessageQueue对象的成员变量mPtr,而mPtr保存的是C++层NativeMessageQueue的地址，所以这里类型转换是安全的
    NativeMessageQueue* nativeMessageQueue = reinterpret_cast<NativeMessageQueue*>(ptr);
    //调用方法检查是否有新的消息出现
    nativeMessageQueue->pollOnce(env, obj, timeoutMillis);
}

···········
void NativeMessageQueue::pollOnce(JNIEnv* env, jobject pollObj, int timeoutMillis) {
    ··········
    //mLooper指向了一个C++层的Looper对象，这里调用其成员函数pollOnce来检查当前线程是否有新的消息需要处理
    mLooper->pollOnce(timeoutMillis);
    ··········
    }
```

继续找到路径`/system/core/libutils/Looper.cpp`：

```
int Looper::pollOnce(int timeoutMillis, int* outFd, int* outEvents, void** outData) {
    int result = 0;
    for (;;) {
    
        ······
        
        if (result != 0) {
        ······
            return result;
        }

        result = pollInner(timeoutMillis);
    }
}

······
int Looper::pollInner(int timeoutMillis) {
     ······
    // Poll.
    int result = POLL_WAKE;
    
    ······
    //监听在Looper构造方法中创建的epoll实例的文件描述符的IO读写事件
    struct epoll_event eventItems[EPOLL_MAX_EVENTS];
    //如果这些文件描述符都没有发生IO读写事件，那么当前线程就会进入等待状态，等待时间由timeoutMillis来指定
    int eventCount = epoll_wait(mEpollFd, eventItems, EPOLL_MAX_EVENTS, timeoutMillis);

    ······
    //循环检查哪一个文件描述符发生了IO读写事件
    for (int i = 0; i < eventCount; i++) {
        int fd = eventItems[i].data.fd;
        uint32_t epollEvents = eventItems[i].events;
        //检查是否是当前线程管道的读端文件描述符
        if (fd == mWakeEventFd) {
            //是否写入了新的数据
            if (epollEvents & EPOLLIN) {
                //读取当前线程关联的管道的数据
                awoken();
            }
            ······
        }
        ······
    }

    ······
    return result;
}
······
void Looper::awoken() {

······

    uint64_t counter;
    //将与当前线程所关联的管道数据读出来，以便可以清理这个管道的就数据。
    TEMP_FAILURE_RETRY(read(mWakeEventFd, &counter, sizeof(uint64_t)));
}

```

### 3. 消息发送与处理

在前面插播了Message的结构体介绍，那么作为链表的表头它是在什么时候给赋值的呢？下面我们来一起看下Handler是如何做到消息发送以及处理的。

首先是Handler的构造函数：

```
public class Handler {

    ······
    final Looper mLooper;
    final MessageQueue mQueue;
    final Callback mCallback;
    final boolean mAsynchronous;
    ······

   public Handler(Callback callback, boolean async) {
        if (FIND_POTENTIAL_LEAKS) {
            final Class<? extends Handler> klass = getClass();
            if ((klass.isAnonymousClass() || klass.isMemberClass() || klass.isLocalClass()) &&
                    (klass.getModifiers() & Modifier.STATIC) == 0) {
                Log.w(TAG, "The following Handler class should be static or leaks might occur: " +
                    klass.getCanonicalName());
            }
        }

        mLooper = Looper.myLooper();
        if (mLooper == null) {
            throw new RuntimeException(
                "Can't create handler inside thread that has not called Looper.prepare()");
        }
        mQueue = mLooper.mQueue;
        mCallback = callback;
        mAsynchronous = async;
    }

}
```

一个Handler对应一个消息队列和一个消息循环。Handler发送消息最终都会走到`sendMessageAtTime`方法

```
public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
        MessageQueue queue = mQueue;
        if (queue == null) {
            RuntimeException e = new RuntimeException(
                    this + " sendMessageAtTime() called with no mQueue");
            Log.w("Looper", e.getMessage(), e);
            return false;
        }
        return enqueueMessage(queue, msg, uptimeMillis);
    }
    
private boolean enqueueMessage(MessageQueue queue, Message msg, long uptimeMillis) {
        msg.target = this;
        if (mAsynchronous) {
            msg.setAsynchronous(true);
        }
        //调用messagequeue的enqueueMessage方法
        return queue.enqueueMessage(msg, uptimeMillis);
    }    
```

一波三折，最后走到的是MessageQueue的`enqueueMessage(Message msg, long when)`方法

```
boolean enqueueMessage(Message msg, long when) {

       ······

        synchronized (this) {
        
            ······

            msg.markInUse();
            msg.when = when;
            Message p = mMessages;
            boolean needWake;
            if (p == null || when == 0 || when < p.when) {
                // New head, wake up the event queue if blocked.
                msg.next = p;
                mMessages = msg;//给表头赋值
                needWake = mBlocked;
            } else {
                //通常中途插入队列的消息是不处理的，除非是barrier或者是早先插入的异步消息
                needWake = mBlocked && p.target == null && msg.isAsynchronous();
                Message prev;
                for (;;) {
                    prev = p;
                    p = p.next;
                    if (p == null || when < p.when) {
                        break;
                    }
                    if (needWake && p.isAsynchronous()) {
                        needWake = false;
                    }
                }
                msg.next = p; // invariant: p == prev.next
                prev.next = msg;
            }
            //调用native方法唤醒线程处理消息，这里可以推断mPtr！=0，因为mQuitting=false
            if (needWake) {
                nativeWake(mPtr);
            }
        }
        return true;
    }
```

下面我们来看如何唤醒线程的，还是找到MessagerQueue.cpp：

```
static void android_os_MessageQueue_nativeWake(JNIEnv* env, jclass clazz, jlong ptr) {
    NativeMessageQueue* nativeMessageQueue = reinterpret_cast<NativeMessageQueue*>(ptr);
    nativeMessageQueue->wake();
}

void NativeMessageQueue::wake() {
//又到loop里面啦
    mLooper->wake();
}
......

//Looper.cpp
void Looper::wake() {

······

    uint64_t inc = 1;
    //通过向管道的写端文件描述符中写入数据，从而来唤起线程
    ssize_t nWrite = TEMP_FAILURE_RETRY(write(mWakeEventFd, &inc, sizeof(uint64_t)));
    if (nWrite != sizeof(uint64_t)) {
        if (errno != EAGAIN) {
            LOG_ALWAYS_FATAL("Could not write wake signal to fd %d: %s",
                    mWakeEventFd, strerror(errno));
        }
    }
}

```

上面通过向管道写入数据来唤醒线程，那么之后又是怎么处理消息的呢？ 再来看Looper的`loop()`方法:

```
public static void loop() {
        final Looper me = myLooper();
        ······
        final MessageQueue queue = me.mQueue;

        ······
        //处理消息
        msg.target.dispatchMessage(msg);

        ......

        msg.recycleUnchecked();
        }
    }
```

ok，闭上眼睛，深呼吸。Handler的消息循环机制基本上跟着代码撸完了一遍，下面通过一些问题来深化对其的理解。

## 深化理解

1. q：什么是消息循环机制？

Android应用程序是通过消息来驱动的，整个机制是围绕着消息的产生以及处理而展开的。消息机制的三大要点：消息队列、消息循环（分发）、消息发送与处理。

2. q: loop死循环为什么不会导致应用卡死？

消息循环数据通信采用的是epoll机制，它能显著的提高CPU的利用率，另外Android应用程序的主线程在进入消息循环前，会在内部创建一个Linux管道（Pipe），这个管道的作用是使得ANdroid应用主线程在消息队列唯恐时可以进入空闲等待状态，并且使得当应用程序的消息队列有消息需要处理是唤醒应用程序的主线程。也就是说在无消息时，循环处于睡眠状态，并不会出现卡死情况。

3. q：Handler为什么能够处理不同线程的消息？

可以看Handler的构造方法，传入不同的looper就会处理不同的线程。这里一个线程只有一个消息队列和一个消息循环，而Handler与线程是一对多的关系。

4. 为什么直接在子线程中初始化Handler会报错？

我们在主线程直接初始化的Handler是不会报错的，因为创建主线程的时候，已经先初始化了主线程的loop对象，而子线程中我们如果不初始化loop对象就会报错。可以看下面ActivityThread的Main方法：

```
public static void main(String[] args) {

        ······
        //先要创建loop对象
        Looper.prepareMainLooper();

        ActivityThread thread = new ActivityThread();
        thread.attach(false);

        if (sMainThreadHandler == null) {
            sMainThreadHandler = thread.getHandler();
        }

        if (false) {
            Looper.myLooper().setMessageLogging(new
                    LogPrinter(Log.DEBUG, "ActivityThread"));
        }

        // End of event ActivityThreadMain.
        Trace.traceEnd(Trace.TRACE_TAG_ACTIVITY_MANAGER);
        Looper.loop();

        throw new RuntimeException("Main thread loop unexpectedly exited");
    }
}

```

5. Thread是怎么做到线程隔离的，thread拥有各自的loop

Loop中有个ThreadLocal成员变量，他是每个Thread各自的一个私有的Map变量，在Loop prepare的时候将新建的looper存储在Thread副本中，保证了每个线程都有了独立的Loop。

本文会持续更新，欢迎各位同学拍砖。

参考文章：

1. 《Andorid系统源代码情景分析》
2. [Android消息处理零散分析](http://windrunnerlihuan.com/2016/07/31/Android%E6%B6%88%E6%81%AF%E6%9C%BA%E5%88%B6%E9%9B%B6%E6%95%A3%E5%88%86%E6%9E%90/)






