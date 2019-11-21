# MvvmApp
由于之前工作一直使用的MVP,今年开始项目需要用MVVM,  由于mvvm不像mvp那样基本就是那种固定的标准写法，百度Mvvm几乎大多数都是讲DataBing,完整的示例比较少。
现在把工作中自己封装的抽离出来  Mvvm+Retorfit+RxJava+RxLifecycle 架构独立出来，,基于此项目可以快速去开发一款Mvvm模式的APP


 >  tips:关于MVP  [(有兴趣可看看这个我写的Demo)](https://github.com/yezihengok/MVP-Rxjava2-Retrofit2-Dagger2)


### 关于MVVM示例
 * 接口调用示例 使用的是wanAndroid提供的 在此感谢。
 * 举例了2种将ViewModel 请求接口的数据通知给View （acitivty）的方式
 * 同时也例举了2种防止Rxjava内存泄漏的方式
 
### 包含一系列工具类 的使用示例
 * 权限管理工具类PermissionsUtils
 * 状态栏工具类 StatusBarUtil
 * RxBus工具类（代替evenbus的作用）
 * mvvm模式下的 BaseRecyclerAdapter
 * apk下载更新工具类
 * 等各种乱七八糟的工具类~ 



####简单截图：
![MvvmApp-master](https://github.com/yezihengok/MvvmApp/blob/master/screenshots/device-1.png)
![MvvmApp-master](https://github.com/yezihengok/MvvmApp/blob/master/screenshots/device-2.png)

