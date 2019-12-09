package com.example.commlib.api;

import android.content.Context;

import com.blankj.ALog;
import com.example.commlib.bean.ResultBean;
import com.example.commlib.bean.ResultBeans;
import com.example.commlib.weight.LoadDialog;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.example.commlib.api.ConfigApi.ERROR_CODE;

/**
 * date : 2019/10/21
 * desc : 请求是否加载等待框
 */
public abstract class CommObserver<T> implements Observer<T> {

    protected LoadDialog dialog;
    private boolean isShowDialog;
    private Context context;

    public CommObserver(Context context, boolean isShowDialog) {
        this.context = context;
        this.isShowDialog = isShowDialog;
        if (this.isShowDialog && dialog == null) {
            createLoading();
        }
    }



    public abstract void success(T data);

    public abstract void error(Throwable e);

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        error(e);
        dismiss();
        ALog.e("onError==="+e.toString());
    }

    @Override
    public void onNext(T t) {
        success(t);
        dismiss();

        if(t instanceof ResultBean){
            ResultBean<T> bean= (ResultBean<T>) t;
            if(bean.getErrorCode()==ERROR_CODE){
                ALog.e("请求失败========"+ERROR_CODE+" "+bean.getErrorMsg());
            }
        }else if(t instanceof ResultBeans){
            ResultBeans<T> bean= (ResultBeans<T>) t;
            if(bean.getErrorCode()==ERROR_CODE){
                ALog.e("请求失败~~~~~~~~"+ERROR_CODE+" "+bean.getErrorMsg());
            }
        }

    }

    @Override
    public void onSubscribe(Disposable d) {
        if (isShowDialog && dialog != null) {
            dialog.show();
        }


    }

    @Override
    public void onComplete() {
        dismiss();
    }

    /**
     * 请求时的进度条
     * @param cancelAble 是否能取消，true_点击外部和返回时取消loading，false_点外部不能取消loading，
     */
    public void createLoading(final boolean... cancelAble) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            return;
        }
        try {

            if (cancelAble == null || cancelAble.length == 0) {
                dialog = new LoadDialog(context, "Loading...", false);//默认不可关闭
            } else {
                dialog = new LoadDialog(context, "Loading...", cancelAble[0]);
            }
            dialog.show();

        } catch (Exception e) {
            // handle exception
            // Log.e(TAG, e.toString());
        }

    }

    public void dismiss(){
        if (dialog != null) {
            try {
                dialog.dismiss();
                dialog = null;
            } catch (Exception e) {
            }
        }
    }
    
}
