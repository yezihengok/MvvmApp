package com.example.commlib.base.mvvmold;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.lifecycle.ViewModel;

import com.blankj.ALog;
import com.example.commlib.R;
import com.example.commlib.api.ConfigApi;
import com.example.commlib.base.mvvm.BaseMvvmRecyclerAdapter;
import com.example.commlib.utils.CommUtils;
import com.example.commlib.utils.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import static com.example.commlib.utils.ButtonUtils.isFastDoubleClick;
import static com.example.commlib.utils.CommUtils.isListNull;

//import androidx.lifecycle.ViewModel;


// 标准 ViewModel+liveData 使用 MainViewModel model = ViewModelProviders.of(this).get(MainViewModel.class);
//去创建ViewModel ,且model 里 理应不应持有 Context 对象（acivity/fragment），

// 我这里的BaseMvvmViewModel 持有了context,  手动添加了对context 内存泄漏的处理。也是一种方法。但使用liveData自动管理生命周期更优雅。


/**
 * 建议使用{@link com.example.commlib.base.mvvm.BaseViewModel}
 * Author： yzh
 * Date: 2019/11/11 17:09
 * Use： ViewModel基类
 */
public  class BaseMvvmViewModel extends ViewModel {

    public int mPage = 1;//列表分页使用默认1开始
    private CompositeDisposable mCompositeDisposable;

    protected Context mContext;
    protected BaseMvvmActivity mActivity;
    protected BaseMvvmFragment mFragment;
    protected Bundle bundle;
    private SmartRefreshLayout mRefreshLayout;

    public void setRefreshLayout(SmartRefreshLayout refreshLayout) {
        mRefreshLayout = refreshLayout;
    }

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


    /**
     *  请求数据后 为下拉刷新的RecyclerView 数据为空时添加空布局，并控制上拉加载状态、以及分页状态
     * @param list
     * @param adapter
     * @param isRefresh 是否是下拉刷新
     * @param <T>
     */
    public <T> void showEmptyView(List<T> list, BaseMvvmRecyclerAdapter adapter, boolean isRefresh, String contet){
        if(isListNull(list)){
            adapter.setEmptyView(getEmptyView(contet));
            if(mRefreshLayout!=null){
                mRefreshLayout.setEnableLoadMore(false);
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadMore();
            }
            ConfigApi.EMPTY_VIEW=true;
        }else{
            //请求有数据时分页才++
            if(isRefresh){
                mPage=2;
            }else{
                mPage++;
            }
            if(mRefreshLayout!=null) {
                mRefreshLayout.setEnableLoadMore(true);
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadMore();
            }
            ConfigApi.EMPTY_VIEW=false;
        }
        ALog.v(adapter.getEmptyViewCount()+"---adapter.getEmptyViewCount()---下一次请求的分页数："+mPage);
    }

    private View emptyView;
    private View getEmptyView(String contet){
        if(emptyView==null){
            emptyView=getView(R.layout.empty_view);
            emptyView.setOnClickListener(v -> ToastUtils.showShort("点击emptyView刷新不够优雅，直接下拉emptyView刷新吧"));
            ViewGroup.LayoutParams lp=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
            emptyView.setLayoutParams(lp);
        }
        CommUtils.setTextValues(emptyView.findViewById(R.id.tv_empty),contet);
        return emptyView;
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
    private View getView(@LayoutRes int layoutId){
        return LayoutInflater.from(mContext).inflate(layoutId,null);
    }

}
