package com.example.mvvmapp.api;

import com.example.commlib.api.App;
import com.example.commlib.crash.CaocConfig;
import com.example.commlib.crash.MyDefaultErrorActivity;
import com.example.mvvmapp.R;
import com.example.mvvmapp.main.MainNewActivity;

/**
 * Anthor yzh Date 2019/11/6 10:36
 */
public class AppApplication extends App {

    @Override
    public void onCreate() {
        super.onCreate();
        initCrash();
    }

    private void initCrash() {
        CaocConfig.Builder.create()
                .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //背景模式,开启沉浸式
                .enabled(true) //是否启动全局异常捕获
                .showErrorDetails(true) //是否显示错误详细信息
                .showRestartButton(true) //是否显示重启按钮
                .trackActivities(true) //是否跟踪Activity
                .minTimeBetweenCrashesMs(2000) //崩溃的间隔时间(毫秒)
                .errorDrawable(R.drawable.customactivityoncrash_error_image) //错误图标
                .restartActivity(MainNewActivity.class) //重新启动后的activity
                .errorActivity(MyDefaultErrorActivity.class) //崩溃后的错误activity(不设置使用默认)
//                .eventListener(new YourCustomEventListener()) //崩溃后的错误监听
                .apply();
    }
}
