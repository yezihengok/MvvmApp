package com.example.mvvmapp.viewmodel;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.blankj.ALog;
import com.example.commlib.base.mvvm.BaseViewModel;
import com.example.commlib.event.SingleLiveEvent;
import com.example.commlib.listener.ClickListener;
import com.example.mvvmapp.TestDetailFragment;
import com.example.mvvmapp.bean.WanAndroidBannerBean;

/**
 * Anthor yzh Date 2019/12/6 11:32
 */
public class MainDetialViewModel extends BaseViewModel {
    private Bundle bundle;
    public WanAndroidBannerBean mBannerBean;

    //权限点击事件 在onBindingClick里调用了call，
    public SingleLiveEvent<Void> permissionsEvent = new SingleLiveEvent<>();

    public SingleLiveEvent<String> downLoadEvent = new SingleLiveEvent<>();
    public SingleLiveEvent<String> errorEvent = new SingleLiveEvent<>();


    public ObservableField<String> downBtnName = new ObservableField<>("点击体验下载的乐趣");
    public MainDetialViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public void onBundle(Bundle bundle) {
        this.bundle=bundle;
        if(bundle!=null){
            mBannerBean= (WanAndroidBannerBean) bundle.getSerializable("bannerBean");
            ALog.w("示例ViewModel获取acitivty的传值:"+mBannerBean.toString());
        }


    }

    //闪退点击事件
    public ClickListener errorClick=(v) -> errorEvent.setValue("哟哟哟，项目又报空指针错误了呢");

    //下载
    public ClickListener downloadClick=(v) -> downLoadEvent.setValue("http://s.duapps.com/apks/own/ESFileExplorer-V4.2.1.7.apk");

    //跳转测试Fragment
    public ClickListener fragmentClick=(v) -> startContainerActivity(TestDetailFragment.class.getCanonicalName(), bundle);

}
