package com.example.mvvmapp.main;

import android.os.Bundle;
import android.view.KeyEvent;

import androidx.lifecycle.Observer;

import com.blankj.ALog;
import com.example.commlib.base.mvvmold.BaseMvvmActivity;
import com.example.commlib.rx.RxBus;
import com.example.commlib.rx.RxBusCode;
import com.example.commlib.utils.CommUtils;
import com.example.commlib.utils.GlideImageLoader;
import com.example.commlib.utils.StatusBarUtil;
import com.example.commlib.utils.ToastUtils;
import com.example.commlib.weight.banner.BannerConfig;
import com.example.commlib.weight.banner.Transformer;
import com.example.mvvmapp.BuildConfig;
import com.example.mvvmapp.MainDetailActivity;
import com.example.mvvmapp.R;
import com.example.mvvmapp.TestWeightActivity;
import com.example.mvvmapp.bean.WanAndroidBannerBean;
import com.example.mvvmapp.databinding.ActivityMainBinding;
import com.example.mvvmapp.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * 手动创建ViewModel，ViewModel持有context，并手动添加RxLifecycle、CompositeDisposable 对context内存泄漏管理  方式 实现的MVVM
 */
public class MainActivity extends BaseMvvmActivity<ActivityMainBinding, MainViewModel> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setTransparentForImageView(mContext,null);

        //示例 RxBus使用
        Disposable subscribe = RxBus.getDefault().toObservable(RxBusCode.TYPE_0,String.class).subscribe(s -> ALog.i("返回值:"+s));
        addRxDisposable(subscribe);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected MainViewModel initMVVMViewModel() {
        //常规的binding的写法
        //MessageViewModel mViewModel=new MessageViewModel(this);
        //mBinding.setViewModel(mViewModel);

        //MainViewModel model = ViewModelProviders.of(this).get(MainViewModel.class);
        return new MainViewModel(this);
    }

    @Override
    protected void initView() {

        initBanner();
        mBinding.mRefreshLayout.setOnRefreshListener(refreshLayout ->getHomeList(true));
        mBinding.mRefreshLayout.setOnLoadMoreListener(refreshLayout ->getHomeList(false));
        mViewModel.setRefreshLayout(mBinding.mRefreshLayout);

        mViewModel.toastEvent.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                ToastUtils.showShort(s);
            }
        });
    }

    private void initBanner(){

        //设置banner样式
        mBinding.include.mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE)//设置banner样式
        .setImageLoader(new GlideImageLoader())//设置图片加载器
      //.setImages(images)
        .setBannerAnimation(Transformer.ZoomOutSlide)
        //.setBannerTitles(titles);//设置标题集合（当banner样式有显示title时）
        .isAutoPlay(true)//设置自动轮播，默认为true
        .setDelayTime(4000)//设置轮播时间
        .setIndicatorGravity(BannerConfig.LEFT)  //设置指示器位置（当banner模式中有指示器时）
        .start();//banner设置方法全部调用完毕时最后调用

        getWanBanner();

    }

    private void getHomeList(boolean isRefresh){
        mViewModel.getHomeList(-1,isRefresh,result -> {
            ALog.i("请求到的数据回调给Activity："+result.size());
//                if (isRefresh){
//                    mBinding.mRefreshLayout.finishRefresh();
//                }else{
//                    mBinding.mRefreshLayout.finishLoadMore();
//                }

        });
    }

    private void getWanBanner() {

        mViewModel.getWanAndroidBanner().observe(this, new Observer<List<WanAndroidBannerBean>>() {
            @Override
            public void onChanged(List<WanAndroidBannerBean> dataBeans) {
                mBinding.mRefreshLayout.autoRefresh(500);
                if(CommUtils.isListNotNull(dataBeans)){
                    List<String> images=new ArrayList<>();
                    List<String> titles=new ArrayList<>();
                    for (WanAndroidBannerBean articlesBean:dataBeans){
                        images.add(articlesBean.getImagePath());
                        titles.add(articlesBean.getTitle());
                    }
                    mBinding.include.mBanner.update(images,titles)
                            .setOnBannerListener(position -> {
                                ToastUtils.showShort(dataBeans.get(position).getUrl());
                                Bundle bundle=new Bundle();
                                bundle.putSerializable("bannerBean",dataBeans.get(position));
                                startActivity(MainDetailActivity.class,bundle);
                            });

                } else {
                    ToastUtils.showShort("获取banner失败~~");
                }

            }
        });

    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (BuildConfig.DEBUG) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_VOLUME_DOWN:
                        startActivity(TestWeightActivity.class);
                    return super.onKeyDown(keyCode, event);
                case KeyEvent.KEYCODE_VOLUME_UP:


                    return super.onKeyDown(keyCode, event);
                case KeyEvent.KEYCODE_MENU:

                    return true;
                case KeyEvent.KEYCODE_BACK:
                    return true;
            }
        }

        return true;
    }


}
