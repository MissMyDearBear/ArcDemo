## Tinker 原理
核心原理： classLoader 双亲委派原理。每个classloader都委托自己的父classloader去家在class对象，直到找不到可以家在的父classloader时，自己加载。这样做的目的是：
- 避免类的重复加载，确保一个类的全局唯一性
- 安全，核心api不会被篡改

### tinker热修复


#### 修复class对象

DexClassLoader --> BaseDexClassLoader --> DexPathList-->native方法向DexPathList中dexElements数组中添加element

findClass时，会遍历dexElements


在拿到补丁包后，通过反射将补丁包dex放入到DexPathList的DexElements数组最前面，实现修复



#### 资源文件修复

1、首先将activityThread中所有LoadApk中的resDir的值替换成新合成的资源文件路径（获取Resources时，会以LoadApk中的resDir作为key去ResourcesManager中获取）
2、创建一个新的AssetManager，并把资源补丁apk加载进新的 AssetManager 中
3、将ResourcesManager中所有Resources对象中AssetManager替换成我们新建的AssetManager，那么所有的Resources对象获取到的都是新合成的资源文件。
