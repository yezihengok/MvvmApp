package com.example.mvvmapp.api;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.blankj.ALog;
import com.didichuxing.doraemonkit.DoraemonKit;
import com.example.commlib.api.App;
import com.example.commlib.crash.CaocConfig;
import com.example.commlib.crash.MyDefaultErrorActivity;
import com.example.commlib.webview.WebViewActivity;
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
        registerActivityLifecycleCallbacks(this);
        initDoraemonKit();
    }



    public void initDoraemonKit(){
        // DoraemonKit.install(this);
        DoraemonKit.install(this,null,"780a59b23dde39e0527856bc30cd3056");
        DoraemonKit.setDebug(false);//DoraemonKit疯狂打印日志看了很烦

        // H5任意门功能需要，非必须（使用自己的H5容器打开这个链接）
        DoraemonKit.setWebDoorCallback((context, url) -> WebViewActivity.loadUrl(url,"DoraemonKit测试"));
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


    //注册activity生命周期， activity生命周期监听可以做一些事情
    private  void registerActivityLifecycleCallbacks(Application application) {
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, Bundle savedInstanceState) {
                ALog.v(activity.getClass().getSimpleName()+"-onActivityCreated");
                ActivityManager.getInstance().addActivity(activity);
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {
            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {
                ALog.v(activity.getClass().getSimpleName()+"-onActivityResumed");
            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {
            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
                ALog.v(activity.getClass().getSimpleName()+"-onActivityDestroyed");
                ActivityManager.getInstance().removeActivity(activity);
            }
        });
    }



}
