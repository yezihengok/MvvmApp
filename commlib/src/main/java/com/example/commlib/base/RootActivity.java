package com.example.commlib.base;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import com.example.commlib.R;
import com.example.commlib.utils.DensityUtil;
import com.example.commlib.utils.StatusNavUtils;
import com.example.commlib.utils.ToastUtils;
import com.example.commlib.utils.permission.PermissionsUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;

import java.util.List;

import static com.example.commlib.utils.ButtonUtils.isFastDoubleClick;
import static com.example.commlib.utils.CommUtils.isListNull;


/**
 * @Description: 最終的根acitvity
 * @Author: yzh
 * @CreateDate: 2019/11/6 12:01
 */
public class RootActivity extends RxAppCompatActivity {

    /**
     * 当statusbar颜色需要为白色的时候，需要设置，默认不是白色
     * true:白色 false:其他颜色
     */
    protected boolean isStatusBarWhite;

    protected RxAppCompatActivity mContext;
    private final int ACCESS_FINE_LOCATION_CODE = 2;//申请获得定位权限
    private final int ACTION_LOCATION_SOURCE_SETTINGS_CODE = 3; //打开GPS设置界面

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    //    translateStatueBar();
        Configuration mConfiguration = this.getResources().getConfiguration(); //获取设置的配置信息
        int orientation = mConfiguration.orientation; //获取屏幕方向
        if (orientation == mConfiguration.ORIENTATION_PORTRAIT) {
            DensityUtil.setDensity(getApplication(), this,600);
        }else {
            DensityUtil.setDensity(getApplication(), this,960);
        }
        mContext = this;


    }


    /**
     * 查看是否打开GPS  跳转设置GPS
     */
    public void isOpenGPS() {
        LocationManager locationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // 转到手机设置界面，用户设置GPS
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, ACTION_LOCATION_SOURCE_SETTINGS_CODE); // 设置完成后返回到原来的界面
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionsUtils.getInstance().onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        switch (requestCode) {
            case ACCESS_FINE_LOCATION_CODE:
                //定位权限   加设置GPS
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //申请权限可获得位置权限成功了
                    isOpenGPS();
                } else {
                    ToastUtils.showShort("申请失败,可能导致定位不准确");
                }
                break;
        }
    }




    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //设置相应的  设计图  dp  比率
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            //"横屏"
            DensityUtil.setDensity(getApplication(), this,960);
        }else{
            // "竖屏"
            DensityUtil.setDensity(getApplication(), this,600);
        }
    }


    /**
     * 沉浸式
     */

    protected void translateStatueBar() {
//        setTheme(R.style.AppTheme);
        Window window = getWindow();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        if (isStatusBarWhite){
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(Color.WHITE);
            }
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(Color.TRANSPARENT);
            }
        }
        StatusNavUtils.setNavigationBarColor(this,Color.TRANSPARENT);
    }



    //************************************** Activity跳转(兼容4.4) **************************************//

    /**
     * Activity跳转
     *
     * @param clz 要跳转的Activity的类名
     */
    public void startActivity(Class<?> clz) {
        if(isFastDoubleClick()){
            return;
        }
        startActivity(new Intent(this, clz));
    }


    /**
     * Activity携带数据的跳转
     *
     * @param clz    要跳转的Activity的类名
     * @param bundle bundle
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        if(isFastDoubleClick()){
            return;
        }
        Intent intent = new Intent(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * Activity跳转(带动画)
     *
     * @param clz 要跳转的Activity的类名
     */
    public void startActivityAnimation(Class<?> clz) {
        if(isFastDoubleClick()){
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(new Intent(this, clz), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        } else {
            startActivity(clz);
        }
    }

    /**
     * Activity跳转(共享元素动画)
     *
     * @param clz 要跳转的Activity的类名
     */
    public void startActivityAnimation(Class<?> clz, View view, String shareView) {
        if(isFastDoubleClick()){
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(new Intent(this, clz), ActivityOptions.makeSceneTransitionAnimation(this, view, shareView).toBundle());
        } else {
            startActivity(clz);
        }
    }

    /**
     * Activity跳转(共享元素动画,带Bundle数据)
     *
     * @param clz 要跳转的Activity的类名
     */
    public void startActivityAnimation(Class<?> clz, View view, String shareView, Bundle bundle) {
        if(isFastDoubleClick()){
            return;
        }
        Intent intent = new Intent(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this, view, shareView).toBundle());
        } else {
            startActivity(intent);
        }
    }

    /**
     * Activity跳转(带动画,带Bundle数据)
     *
     * @param clz 要跳转的Activity的类名
     */
    public void startActivityAnimation(Class<?> clz, Bundle bundle) {
        if(isFastDoubleClick()){
            return;
        }
        Intent intent = new Intent(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        } else {
            startActivity(intent);
        }
    }


    /**
     * 通过Class打开编辑界面
     *
     * @param cls
     * @param requestCode
     */
    public void startActivityForResult(Class<?> cls, int requestCode) {
        if(isFastDoubleClick()){
            return;
        }
        startActivityForResult(new Intent(this, cls), requestCode);
    }

    /**
     * 含有Bundle通过Class打开编辑界面
     *
     * @param cls
     * @param bundle
     * @param requestCode
     */
    public void startActivityForResult(Class<?> cls, Bundle bundle,int requestCode) {
        if(isFastDoubleClick()){
            return;
        }
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 有动画的Finish掉界面
     */
    public void AnimationFinish() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        } else {
            finish();
        }
    }

    //************************************** Activity跳转 **************************************//




    /**
     * 为textview 设值，避免空值情况
     *
     * @param tv
     * @param str
     */
    public void setTextValues(TextView tv, String str) {
        if (tv != null && !TextUtils.isEmpty(str)) {
            tv.setText(str);
        }
    }
    public void setTextValues(TextView tv, @StringRes int id) {
        String str = getString(id);
        if (tv != null && !TextUtils.isEmpty(str)) {
            tv.setText(str);
        }
    }

    /**
     * 获取
     * @param colorId
     * @return
     */
    public int getColors(int colorId) {
        return ContextCompat.getColor(this, colorId);
    }

    public View getView(@LayoutRes int layoutId){
        return LayoutInflater.from(mContext).inflate(layoutId,null);
    }


    /**
     *  为下拉刷新的RecyclerView 数据为空时添加空布局，并控制上拉加载状态
     * @param list
     * @param adapter
     * @param refreshLayout
     * @param <T>
     */
    public <T> void setFooterView(List<T> list, BaseRecyclerAdapters adapter, SmartRefreshLayout refreshLayout){
        adapter.recyclerView.setAdapter(adapter);//切换设置FooterView，需要重新setAdapter,不然会报错
        if(isListNull(list)){
            adapter.setFooterView(R.layout.empty_view);
            refreshLayout.setEnableLoadMore(false);
        }else{
            adapter.setFooterView(null);
            refreshLayout.setEnableLoadMore(true);
        }
    }




}
