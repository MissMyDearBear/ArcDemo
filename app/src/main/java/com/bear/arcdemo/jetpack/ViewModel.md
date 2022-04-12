viewModel职责是为Activity或者Fragment准备并且管理数据。负责业务逻辑代码。ViewModel的创建需要关联Scope如Activity or
Fragment，并且生命周期与其保持一致。除此之外，当屏幕配置相关的改动（语言，旋转屏）等导致Activity的销毁和重建不会影响ViewModel，将在Activity重建后，重新绑定新的Scope。

### ViewModel的优点

1. ViewModel接管了业务逻辑处理，和LiveData的配合可以实现响应式布局，数据源改变将会自动通知view层去更新UI
2. ViewModel在Activity的配置变化导致销毁重建的时候，保存了页面状态的数据，待重建的时候能够快速恢复页面的数据。
3. ViewModel的实例化依赖ViewModelProvider中的Factory，可以有效规范ViewModel的创建。
4. ViewModel作用在Activity or
   Fragment中，可以随着对应宿主的生命周期如onDestroy的时候主动释放。异步的网络请求也可以通过重写viewModel的onClear方法去做请求取消等。

### ViewModel内部实现

1. 内部包含一个HashMap，用来缓存ViewModel对应Scope宿主的状态。
2. 内部提供onCleared方法以供ViewModel销毁时，开发者去释放一些异步操作等，防止内存泄漏

### ViewModel +livedata + dataBinding

#### 什么是数据双向绑定

数据双向绑定说的是dataBinding
使得在xml中数据可以直接被对应的控件使用，同时用户的操作也可以直接改变数据本身。当数据发生变化，因数据是LiveData类型，Activity订阅了数据源，所以数据源变化时又会同时更新UI界面。

#### 什么是单向数据流

个人认为，dataBinding的引入在频繁更改UI情况下的表现并不是很良好，这里可以仅用dataBinding预编译后的产物即可（findViewById操作预编译一遍查找就将所以的控件的id找到了）当向数据流即用户的行为将不直接影响数据改变，而是通过调用viewMode的方法使得数据发生改变，使得数据源自单一viewmodel。

#### livedata原理

livedata 内部维护观察者map，当数据发生改变后会通知各个观察者，以便做出对应相应

#### dataBinding原理

APT+ grdle
预编译生成对应的ActivityBindingImpl，然后再onCreate方法中将xml布局文件与生成的类绑定，内部通过一遍遍历将Id与控件绑定。动态更新UI的实现原理是传入的数据是Observe类型，实现类种对数据进行了订阅，当数据发生改变时会自行去改变页面。在给text赋值的时候会判断是否为null，这也是为啥其能防止空指针异常。

### ViewModel如何做到关联Activity or Fragment

ViewModel的实例都是通过ViewModelProvider来提供的，构造方法中填入Activity或者Fragment。自定义的Activity
必须是ComponentActivity，ViewModelStore是在ComponentActivity生命周期变化过程中被赋值的。ViewModelStore被创建后则保存在Activity的nonConfigrucationInstance中。当监测到Activity
执行ondestory的时候执行viewmodel.clear

### 当Activity的config改变时导致Activity重建的时候ViewModel如何留数据的。

Activity -> Activity(onRetainConfig方法种将viewmodel 保存再Actvity的lastNonConfigrucationIntance中)Activity(
onDestory) -> ActivityClientRecord保存lastxx, Activity RelaunchActivity
会从ActivityClient中取出数据，在Activity Attch中将数据重新装载到Actvity中，从而实现了将旧的数据转接给了新的Activity

