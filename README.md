# MvvmApp
由于之前工作一直使用的MVP,今年开始项目需要用MVVM,  由于mvvm不像mvp那样基本就是那种固定的标准写法，百度Mvvm几乎大多数都是讲DataBing,完整的示例比较少。
现在把工作中自己封装的抽离出来  Mvvm+Retorfit+RxJava+RxLifecycle 架构独立出来,基于此项目你可以快速去开发一款Mvvm模式的APP


 >  tips:自己使用的是平板 手机运行布局整体偏小，是因为项目里使用的是  [(今日头条的适配方式)](https://mp.weixin.qq.com/s/d9QCoBP6kV9VSWvVldVVwA)
 自己实际使用请根据设计图UI的 dp 去修改RootActivity里的DensityUtil.setDensity 。不喜欢这种适配 可以屏蔽 自己改用其它 如 smallestWidth 限定符适配方案； 

 
 
### 关于MVVM示例
 * 接口调用示例 使用的是wanAndroid提供的 在此感谢。
 * 举例了2种将ViewModel 请求接口的数据通知给View （acitivty）的方式
 * 同时也例举了2种防止Rxjava内存泄漏的方式
 * 同时也列举了2种ViewModel创建方式（Mvvm的实现方式）
 
  > Mvvm的实现方式示例界面 MainActivity---MainNewActivity(推荐使用MainNewActivity的示例)：
  
  
 | MainActivity			|MainNewActivity			 |
 | ------------- |  :-------------|
 | 手动new创建的ViewModel，ViewModel持有context，并手动添加RxLifecycle、CompositeDisposable 对context内存泄漏管理的方式实现的MVVM|ViewModelProviders.of 方式 初始化的ViewModel，ViewModel不持有context，LiveDada通知回调 不用担心管理内存泄漏问题  方式实现的MVVM（推荐）|



### 包含一系列工具类 的使用示例
 * 权限管理工具类PermissionsUtils
 * 状态栏工具类 StatusBarUtil
 * RxBus工具类（代替evenbus的作用）
 * mvvm模式下的 BaseRecyclerAdapter
 * apk下载更新工具类
 * RxAnimation 属性动画工具类
 * 公用的webView页面展示、js与Java交互示例
 * 播放本地音频工具类、帧动画播放工具类
 * 等各种乱七八糟的工具类~ 



####简单截图：
![MvvmApp-master](https://github.com/yezihengok/MvvmApp/blob/master/screenshots/device-1.png)
![MvvmApp-master](https://github.com/yezihengok/MvvmApp/blob/master/screenshots/device-2.png)
![MvvmApp-master](https://github.com/yezihengok/MvvmApp/blob/master/screenshots/device-3.png)

