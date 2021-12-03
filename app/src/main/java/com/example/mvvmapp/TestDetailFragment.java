package com.example.mvvmapp;

import android.app.ProgressDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.blankj.ALog;
import com.example.commlib.base.mvvm.BaseFragment;
import com.example.commlib.download.DownLoadManager;
import com.example.commlib.download.ProgressCallBack;
import com.example.commlib.utils.CommUtils;
import com.example.commlib.utils.StatusBarUtil;
import com.example.commlib.utils.ToastUtils;
import com.example.commlib.utils.permission.PermissionsUtil;
import com.example.commlib.utils.permission.PermissionsUtils;
import com.example.mvvmapp.databinding.ActivityMainDetailBinding;
import com.example.mvvmapp.databinding.TitleLayoutBinding;
import com.example.mvvmapp.viewmodel.MainDetialViewModel;

import java.io.File;

import okhttp3.ResponseBody;

import static com.example.commlib.utils.CommUtils.getInstallAppIntent;

/**
 *  除了下载功能与MainDetailActivity一样  --只是为了 示例Fragment 使用
 * Anthor yzh Date 2019/12/9 13:57
 */
public class TestDetailFragment extends BaseFragment<ActivityMainDetailBinding,MainDetialViewModel> {
    TitleLayoutBinding mTitleLayoutBinding;

    @Override
    protected int getLayoutId(LayoutInflater inflater, @Nullable ViewGroup container) {
        return  R.layout.activity_main_detail;
    }

    @Override
    public void initViewObservable() {
        mViewModel.downLoadEvent.observe(this, this::downloadApk);

        mViewModel.permissionsEvent.observe(this,o -> {
            ToastUtils.showShort("权限申请");
            PermissionsUtils.getInstance().chekPermissions(mActivity, PermissionsUtil.PERMISSION_CAMERA, new PermissionsUtils.IPermissionsResult() {
                @Override
                public void passPermissons() {
                    ToastUtils.showShort("获取相机成功");
                }
                @Override
                public void forbitPermissons() {
                    ToastUtils.showShort("您拒绝了相机权限");
                }
            });
        });

        mViewModel.errorEvent.observe(this,o ->{
            throw new NullPointerException(o);
        });
    }

    @Override
    protected void initView() {
        StatusBarUtil.setColorNoTranslucent(mActivity, getColors(R.color.colorPrimary));
        //示例如何动态的添加一个BindingView
        mTitleLayoutBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.title_layout,mBinding.topLayout, false);
        mBinding.topLayout.addView(mTitleLayoutBinding.getRoot());

        mTitleLayoutBinding.titleBack.setOnClickListener(v -> mActivity.finish());
        // mBinding.mUpdateButton.setOnClickListener(v -> download());

        // WanAndroidBannerBean mBannerBean=(WanAndroidBannerBean) getIntent().getSerializableExtra("bannerBean");
        //  mBinding.setBannerBean(mBannerBean);
        mTitleLayoutBinding.titleText.setText("示例Fragment使用");

        mViewModel.downBtnName.set("===使用Retrofit方式下载apk===");
    }


    public void downloadApk(String url){
        CommUtils.showDialogByCancelSure(mActivity, "测试更新apk", null, () ->
                PermissionsUtils.getInstance().chekPermissions(mActivity, PermissionsUtil.PERMISSION_FILE, new PermissionsUtils.IPermissionsResult() {
            @Override
            public void passPermissons() {
                canInstallAPK(() -> downLoad(url));

            }
            @Override
            public void forbitPermissons() {
                ToastUtils.showShort("您拒绝了存储权限，将无法下载更新");
            }
        }));

    }
    public void downLoad(String url){
        String destFileDir = mActivity.getCacheDir().getPath();
        String destFileName ="downlaod.apk";
        final ProgressDialog progressDialog = new ProgressDialog(mActivity);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("正在下载...");
        progressDialog.setCancelable(true);
        progressDialog.show();
        DownLoadManager.getInstance().load(url, new ProgressCallBack<ResponseBody>(destFileDir, destFileName) {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onCompleted() {
                progressDialog.dismiss();
                mViewModel.mBannerBean.progressValue.set(100);
            }

            @Override
            public void onSuccess(ResponseBody responseBody) {
                ToastUtils.showShort("文件下载完成！");
                String fileStr=destFileDir+ File.separator +destFileName;
                ALog.v(fileStr);

                startActivity(getInstallAppIntent(fileStr));
            }

            @Override
            public void progress(final long progress, final long total) {
                progressDialog.setMax((int) total);
                progressDialog.setProgress((int) progress);


                int mProgress = (int) (+progress * 100 / total);
                //进度显示2位小数：
                // double mProgress= ArithUtils.round((progress * 100 / (double) total),2);

                Log.v("DownloadAPk",mProgress + "%    总大小：" + total+"已下载大小："+progress);
                mViewModel.mBannerBean.progressValue.set(mProgress);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                ToastUtils.showShort("文件下载失败！");
                progressDialog.dismiss();
            }
        });
    }

}
