## leakCanary 原理解析

核心原理，使用weakReference + referenceQueue来记录需要回收的对象，在主动触发GC后，发现依然存在的对象，dump它的堆栈信息，弹窗提示。

前置需要知道的知识![垃圾回收机制](../../gc/GcRoot.md)

### Activity 泄漏过程代码追踪

1. provider 时机初始化
2. 绑定Application后，注册生命周期回掉，在Activity 执行到onDestroy的时候，将activity实例的弱引用加入到引用队列中，默认延时5s后
进行手动GC，然后判断引用队列中是否还有引用，如果有则发生了内存泄漏，然后dump堆栈信息使用shark库解析，从而使用弹窗展示出来。