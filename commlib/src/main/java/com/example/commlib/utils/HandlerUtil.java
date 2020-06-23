package com.example.commlib.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yeziheng
 * @date 2016-02-14
 */
public class HandlerUtil {
    public static List<Runnable> runnables=new ArrayList<>();
    public static final Handler HANDLER = new Handler(Looper.getMainLooper());

    public static void runOnUiThread(Runnable runnable){
        HANDLER.post(runnable);
        runnables.add(runnable);
    }

    public static void runOnUiThreadDelay(Runnable runnable, long delayMillis){
        HANDLER.postDelayed(runnable,delayMillis);
        runnables.add(runnable);
    }

    public static void removeRunable(Runnable runnable){
        HANDLER.removeCallbacks(runnable);
    }

    public static void removeAllRunable(){
        if(runnables!=null&&runnables.size()>0){
            for(Runnable runnable:runnables){
                HANDLER.removeCallbacks(runnable);
            }
        }

    }
}
