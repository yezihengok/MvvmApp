package com.example.mvvmapp.api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.commlib.listener.Listener;
import com.example.mvvmapp.main.MainActivity;

import java.util.Stack;


public class ActivityManager {

    private static ActivityManager manager;
    public static ActivityManager getInstance(){
        if(manager==null){
            synchronized (ActivityManager.class){
                if(manager==null){
                    manager= new ActivityManager();
                }
            }
        }
        return manager;
    }

    private ActivityManager() {
        activities = new Stack<>();
    }

    private  Stack<Activity> activities;

    public void addActivity(Activity activity){
        activities.add(activity);
    }

    public void removeActivity(Activity act) {
        if (activities != null) {
            activities.remove(act);
        }
    }

    /**
     *
     *  @description 结束给定的Activity实例
     */
    public void removeFinishActivity(Activity activity){
        if (activity !=null){
            activities.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     *
     *  @description 结束当前的Activity实例
     *  @date: 2019/10/25 10:12
     *  @return activity的实例
     */
    public void finishCurrentActivity(){
        Activity activity = activities.lastElement();
        removeFinishActivity(activity);
    }

    /**
     *
     *  @description 获取当前的Activity实例
     *  @date: 2019/10/25 10:12
     *  @return activity的实例
     */
    public Activity getCurrentActivity(){
        if (!activities.empty()){
            return activities.lastElement();
        }
        return null;
    }

    /**
     *
     *  @description 结束指定类名的Activity
     *  @date: 2019/10/25 10:12
     *  @param cls
     */
    public void finishTargetActivity(Class cls){
        for (Activity activity: activities) {
            if (activity.getClass().getName().equals(cls.getName())){
                removeFinishActivity(activity);
            }
        }
    }

    public void finishAllActivity(){
        for (Activity activity: activities) {
            if (null != activity){
                activity.finish();
                activity = null;
            }
        }
        activities.clear();
    }

    public void exitApp(Context context){
        try {
            finishAllActivity();
            System.exit(0);
        }catch (Exception e){
            Log.e("error","退出程序失败");
        }
    }


    /**
     * 结束指定的activity
     * @param activityName
     */
    public void finishActivity(Listener listener, Class... activityName) {
        if(activityName==null||activities==null||activities.isEmpty()){
            listener.onResult();
            return;
        }

        for (int j = 0; j <activities.size() ; j++) {
            Activity activity=activities.get(j);
            for (int i=0;i<activityName.length;i++){
                if(activityName[i].getSimpleName().equals(activity.getClass().getSimpleName())) {
                    if (!activity.isFinishing())
                    {
                        Log.d("-",activity.getClass().getSimpleName()+" finish!!!");
                        activity.finish();
                        activities.remove(activity);
                        j--;
                    }
                }

            }
            if(j==activities.size()-1){
                listener.onResult();
                return;
            }
        }

    }

    /**
     * 结束指定的activity
     * @param activityName
     */
    public void finishActivity(Class... activityName) {
        if(activityName==null||activities==null||activities.isEmpty()){
            return;
        }
        for (int j = 0; j <activities.size() ; j++) {
            Activity activity=activities.get(j);
            for (int i=0;i<activityName.length;i++){
                if(activityName[i].getSimpleName().equals(activity.getClass().getSimpleName())) {
                    if (!activity.isFinishing())
                    {
                        Log.d("-",activity.getClass().getSimpleName()+" finish!!!");

                        activity.finish();
                        activities.remove(activity);
                        j--;
                    }
                }
            }
        }
    }
    
    
    /**
     * 重启app
     * @param context
     */
    public static void restartApp(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        Intent restartIntent = Intent.makeRestartActivityTask(intent.getComponent());
        context.startActivity(restartIntent);
        System.exit(0);
    }


}
