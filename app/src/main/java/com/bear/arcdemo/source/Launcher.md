## Launcher页面点击应用图标启动过程

### 简易流程图

                                       +---------------------------------+
                                       |                                 |                    +----------------------------+
+------------------------+             |     ActivityMangerService       |                    |        MainActivity        |
|                        |             |                                 |                    |                            |
|  LauncherActivity      |             |                                 |                    |   执行ActivityThread的main  |
|   --statrtActivity     |             |                                 |                    |   函数，构建主线程消息循环。    |
|                        |             |                                 |                    |   --IPC调用AMS attach方法   |
|                        |             |                                 |                    |   --创建Application并与     |
| mInstrumention         |   IPC(IBind)|                                 |     IPC(IBind)     |   contextImpl绑定           |
| --通过MainThread拿到   +------------^+通过IPC找到AMSProxy                  +-------------------->                           |
| ApplicationThread      |             +->AMS.startActivity              |                    |                            |
|                        |             |                                 |                    |    performLaunchActivity  |
| 导入ActivityThread      |             |创建Activity栈，启动栈顶Actvity。   |                     |                           |
|                        |             |新启动的Activity进程信息为空，所以|   |                     |
+------------------------+             |foke zygote创建新的进程。           |                    |                            |
                                       |                                 |                    |                            |
                                       |                                 |                    |                            |
                                       |                                 |                    |                            |
                                       |                                 |                    |                            |
                                       |                                 |                    |                            |
                                       |                                 |                    |                            |
                                       |                                 |                    +----------------------------+
                                       +---------------------------------+

### Activity启动流程中，关键问题

#### 主线程的创建是在什么时候？

通过zygote孵化出新的进程后，初始化进程相关信息后，会导入ActivityThread类，然后会执行ActivityThread的main方法，在Main方法中创建主线程的消息队列以及消息循环，并开启。

#### contentProvider的初始化时机

contentProvider初始化的时机鉴于Application的attach与onCreate之间。鉴于此，一些常见的第三方库会在这个地方进行初始化，例如bugly，leakCanary等。屏幕适配方案中在
计算个屏幕与设计图的比例时可以在此进行初始化

#### onWindowFocusChanged

当执行一些与UI界面没有直接关系的耗时操作，可以将这部分代码由`onCreate`方法中，移动至`onWindowFocusChanged`方法中。此方法回调是在`onResume`
之后页面获取到焦点时，也就是第一帧绘制完回调。


