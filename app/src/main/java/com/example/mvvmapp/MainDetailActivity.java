package com.example.mvvmapp;

import androidx.databinding.DataBindingUtil;

import com.blankj.ALog;
import com.example.commlib.base.BaseMvvmActivity;
import com.example.commlib.base.BaseMvvmViewModel;
import com.example.commlib.downloadapk.DownloadAPk;
import com.example.commlib.rx.RxBus;
import com.example.commlib.rx.RxBusCode;
import com.example.commlib.utils.CommUtils;
import com.example.commlib.utils.StatusBarUtil;
import com.example.commlib.utils.ToastUtils;
import com.example.commlib.utils.permission.PermissionsUtil;
import com.example.commlib.utils.permission.PermissionsUtils;
import com.example.mvvmapp.bean.WanAndroidBannerBean;
import com.example.mvvmapp.databinding.ActivityMainDetailBinding;
import com.example.mvvmapp.databinding.TitleLayoutBinding;

/**
 * @Description: MainDetailActivity类作用描述
 * @Author: yzh
 * @CreateDate: 2019/11/19 16:01
 */
public class MainDetailActivity extends BaseMvvmActivity<ActivityMainDetailBinding, BaseMvvmViewModel> {
    TitleLayoutBinding mTitleLayoutBinding;
    WanAndroidBannerBean mBannerBean;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_detail;
    }

    @Override
    protected BaseMvvmViewModel initMVVMViewModel() {
        return null;//这个界面比较简单 不需要ViewModel
    }

    @Override
    protected void initView() {
        StatusBarUtil.setColorNoTranslucent(mContext, getColors(R.color.colorPrimary));
        //示例如何动态的添加一个BindingView
        mTitleLayoutBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.title_layout,mBinding.topLayout, false);
        mBinding.topLayout.addView(mTitleLayoutBinding.getRoot());
        mBannerBean=(WanAndroidBannerBean) getIntent().getSerializableExtra("bannerBean");
        mBinding.setBannerBean(mBannerBean);

        mTitleLayoutBinding.titleBack.setOnClickListener(v -> finish());
        mBinding.mUpdateButton.setOnClickListener(v -> download());
    }


    public void download(){
        CommUtils.showDialog(mContext,"提示","测试更新apk"
                ,"确定","取消",() -> {
                    PermissionsUtils.getInstance().chekPermissions(this, PermissionsUtil.PERMISSION_FILE, new PermissionsUtils.IPermissionsResult() {
                        @Override
                        public void passPermissons() {
                            DownloadAPk.getInstance().downApk(mContext, "https://github.com/yezihengok/MvvmApp/blob/master/testapk/ESFileExplorer-V4.2.1.7.apk"
                                    , new DownloadAPk.DownLoadListener() {
                                        @Override
                                        public void onProgressUpdate(int progress) {
                                            mBannerBean.progressValue.set(progress);
                                        }

                                        @Override
                                        public void finish(String filePath) {
                                            ALog.d(filePath);
                                        }
                                    });
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
