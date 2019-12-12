package com.example.commlib.api;

import com.blankj.ALog;
import com.example.commlib.base.mvvm.BaseViewModel;
import com.example.commlib.bean.ResultBean;
import com.example.commlib.bean.ResultBeans;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.example.commlib.api.ConfigApi.ERROR_CODE;

/**
 * ViewModel里 使用的CommonObserver
 * date : 2019/10/21
 * desc : 请求是否加载等待框
 */
public abstract class CommonObserver<T> implements Observer<T> {

    private boolean isShowDialog;
    private BaseViewModel mViewModel;

    public CommonObserver(BaseViewModel mViewModel, boolean isShowDialog) {
        this.mViewModel = mViewModel;
        this.isShowDialog = isShowDialog;
    }

    public abstract void success(T data);

    public abstract void error(Throwable e);

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        error(e);
        mViewModel.dismissDialog();
        ALog.e("onError==="+e.toString());
    }

    @Override
    public void onNext(T t) {
        success(t);
        mViewModel.dismissDialog();

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
        if (isShowDialog ) {
            mViewModel.showDialog();
        }
        //调用addSubscribe()添加Disposable，请求与View周期同步
        mViewModel.addDisposable(d);
    }

    @Override
    public void onComplete() { mViewModel.dismissDialog();
    }


    
}
