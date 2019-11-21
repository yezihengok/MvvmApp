package com.example.commlib.base;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModel;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import static com.example.commlib.utils.ButtonUtils.isFastDoubleClick;


/**
 * @Author： yzh
 * @Date: 2019/11/11 17:09
 * @Use： ViewModel基类
 */
public  class BaseMvvmViewModel extends ViewModel {

    public int mPage = 0;//列表分页使用
    private CompositeDisposable mCompositeDisposable;

    protected Context mContext;
    protected BaseMvvmActivity mActivity;
    protected BaseMvvmFragment mFragment;
    protected Bundle bundle;

    public BaseMvvmViewModel(BaseMvvmActivity activity) {
        this.mActivity = activity;
        this.mContext = activity;


    }

    public BaseMvvmViewModel(BaseMvvmFragment fragment) {
        mFragment = fragment;
        mContext = fragment.getActivity();

    }

    void initParam(Bundle bundle) {
        this.bundle=bundle;
    }



    //避免Rxjava内存泄漏,
    //1、可以将Rxjava 订阅的时间添至CompositeDisposable进来，Activity销毁时切断订阅
    //2、也可以用 RxLifeCycle 将Rxjava绑定Acitivty/Fragment,销毁时自动取消订阅

    protected void addDisposable(Disposable disposable) {
        if (this.mCompositeDisposable == null) {
            this.mCompositeDisposable = new CompositeDisposable();
        }
        this.mCompositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {//此方法会在Activity/Fragment销毁时调用，可以在这里做一些额外释放资源的操作。
        super.onCleared();
        if (this.mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) {
            this.mCompositeDisposable.clear();
        }
    }


    //************************************** Activity跳转(兼容4.4) **************************************//

    /**
     * Activity跳转
     *
     * @param clz 要跳转的Activity的类名
     */
    public void startActivity(Class<?> clz)
    {
        if(isFastDoubleClick()){
            return;
        }
        mContext.startActivity(new Intent(mContext, clz));
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
        Intent intent = new Intent(mContext, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        mContext.startActivity(intent);
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
            mContext.startActivity(new Intent(mContext, clz), ActivityOptions.makeSceneTransitionAnimation((Activity) mContext).toBundle());
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
            mContext.startActivity(new Intent(mContext, clz), ActivityOptions.makeSceneTransitionAnimation((Activity) mContext, view, shareView).toBundle());
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
        Intent intent = new Intent(mContext, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mContext.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) mContext, view, shareView).toBundle());
        } else {
            mContext.startActivity(intent);
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
        Intent intent = new Intent(mContext, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mContext.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) mContext).toBundle());
        } else {
            mContext.startActivity(intent);
        }
    }

    /**
     * 有动画的Finish掉界面
     */
    public void AnimationFinish() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ((Activity) mContext).finishAfterTransition();
        } else {
            ((Activity) mContext).finish();
        }
    }


    //************************************** Activity跳转 **************************************//


}
