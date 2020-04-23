package com.example.mvvmapp;

import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.blankj.ALog;
import com.example.commlib.base.mvvm.BaseActivity;
import com.example.commlib.downloadapk.DownloadAPk;
import com.example.commlib.rx.RxBus;
import com.example.commlib.rx.RxBusCode;
import com.example.commlib.utils.BarUtils;
import com.example.commlib.utils.ClickUtils;
import com.example.commlib.utils.CommUtils;
import com.example.commlib.utils.StatusBarUtil;
import com.example.commlib.utils.ToastUtils;
import com.example.commlib.utils.permission.PermissionsUtil;
import com.example.commlib.utils.permission.PermissionsUtils;
import com.example.mvvmapp.databinding.ActivityMainDetailBinding;
import com.example.mvvmapp.databinding.TitleLayoutBinding;
import com.example.mvvmapp.viewmodel.MainDetialViewModel;

/**
 * @Description: banner 详情
 * @Author: yzh
 * @CreateDate: 2019/11/19 16:01
 */
public class MainDetailActivity extends BaseActivity<ActivityMainDetailBinding, MainDetialViewModel> {
    TitleLayoutBinding mTitleLayoutBinding;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_detail;
    }

    @Override
    public void initViewObservable() {
       // mViewModel.downLoadEvent.observe(this,o -> download(o));

        mViewModel.downLoadEvent.observe(this, this::download);
        mViewModel.permissionsEvent.observe(this,o -> {
            ToastUtils.showShort("权限申请");
            PermissionsUtils.getInstance().chekPermissions(this, PermissionsUtil.PERMISSION_CAMERA, new PermissionsUtils.IPermissionsResult() {
                @Override
                public void passPermissons() {
                    ToastUtils.showShort("获取相机成功");
                }
                @Override
                public void forbitPermissons() {
                    ToastUtils.showShort("您拒绝了存储权限");
                }
            });
        });

        mViewModel.errorEvent.observe(this,o ->{
            throw new NullPointerException(o);
        });
    }


    @Override
    protected void initView() {
        StatusBarUtil.setColorNoTranslucent(mContext, getColors(R.color.colorAccent));
       // BarUtils.addMarginTopEqualStatusBarHeight(mBinding.rootLayout);
        BarUtils.setStatusBarLightMode(mContext,true);
        //示例如何动态的添加一个BindingView
        mTitleLayoutBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.title_layout,mBinding.topLayout, false);
        mBinding.topLayout.addView(mTitleLayoutBinding.getRoot());

        mTitleLayoutBinding.titleBack.setOnClickListener(v -> finish());
       // mBinding.mUpdateButton.setOnClickListener(v -> download());

      // WanAndroidBannerBean mBannerBean=(WanAndroidBannerBean) getIntent().getSerializableExtra("bannerBean");
       // mBinding.setBannerBean(mBannerBean);

        if(mViewModel.mBannerBean!=null){
            setTextValues(mTitleLayoutBinding.titleText,mViewModel.mBannerBean.getTitle());
        }

        //点击工具类使用
        ClickUtils.applyPressedViewScale(mBinding.btn1,mBinding.btn2);//给view 添加缩放点击效果
        ClickUtils.applyPressedViewAlpha(mBinding.primaryMessageDetailsImg,0.6f);//给view 透明度点击效果
        ClickUtils.applyPressedBgAlpha(mBinding.btn3,0.5f);//给view 透明度背景点击效果
        ClickUtils.applyPressedBgDark(mBinding.btn4);//给view 背景变暗点击效果

        mBinding.primaryMessageDetailsImg.setOnClickListener(new ClickUtils.OnMultiClickListener(5) {
            @Override
            public void onTriggerClick(View v) {
                ToastUtils.showShort("指定时间内连续点击5次触发事件");
            }

            @Override
            public void onBeforeTriggerClick(View v, int count) {
                ToastUtils.showShort(String.valueOf(count));
            }
        });

    }


    public void download(String url){
        CommUtils.showDialog(mContext,"提示","测试更新apk"
                ,"确定","取消",() -> {
                    PermissionsUtils.getInstance().chekPermissions(this, PermissionsUtil.PERMISSION_FILE, new PermissionsUtils.IPermissionsResult() {
                        @Override
                        public void passPermissons() {

                            canInstallAPK(() ->
                                DownloadAPk.getInstance().downApk(mContext, url
                                        , new DownloadAPk.DownLoadListener() {
                                            @Override
                                            public void onProgressUpdate(int progress) {
                                                mViewModel.mBannerBean.progressValue.set(progress);
                                            }

                                            @Override
                                            public void finish(String filePath) {
                                                ALog.d(filePath);
                                            }
                                        })
                            );

                        }
                        @Override
                        public void forbitPermissons() {
                            ToastUtils.showShort("您拒绝了存储权限，将无法下载更新");
                        }
                    });
        },null);
    }

    @Override
    protected void onDestroy() {

        RxBus.getDefault().post(RxBusCode.TYPE_0,"MainDetailActivity");
        super.onDestroy();
    }
}
