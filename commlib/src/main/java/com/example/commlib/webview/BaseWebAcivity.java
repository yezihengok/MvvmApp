package com.example.commlib.webview;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


/**
 * Created by yzh on 2019/12/11 16:06.
 */
public class BaseWebAcivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setDensity();
       // setWindow();
    }

    private void setDensity(){
        Configuration mConfiguration = this.getResources().getConfiguration(); //获取设置的配置信息
        int orientation = mConfiguration.orientation; //获取屏幕方向
        if (orientation == mConfiguration.ORIENTATION_PORTRAIT) {
          //  DensityUtil.setDensity(getApplication(), this,600);
        }else {
         //   DensityUtil.setDensity(getApplication(), this,960);
        }
    }

    private void setWindow(){
        Window window = getWindow();
        //如果是5.0  以上  全部状态栏 透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#00000000"));
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //设置相应的  设计图  dp  比率
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            //"横屏"
           // DensityUtil.setDensity(getApplication(), this,960);
        }else{
            // "竖屏"
          //  DensityUtil.setDensity(getApplication(), this,600);
        }
    }


}
