package com.example.commlib.base.mvvm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @anthor yzh
 * @time 2019/11/27 10:07
 */
public class BaseViewModel extends AndroidViewModel {
    public int mPage = 1;//列表分页使用默认1开始
    private CompositeDisposable mCompositeDisposable;
    private SmartRefreshLayout mRefreshLayout;

    public void setRefreshLayout(SmartRefreshLayout refreshLayout) {
        mRefreshLayout = refreshLayout;
    }

    public BaseViewModel(@NonNull Application application) {
        super(application);
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
//    public <T> void showEmptyView(List<T> list, BaseMvvmRecyclerAdapter adapter, boolean isRefresh, String contet){
//        if(isListNull(list)){
//            adapter.setEmptyView(getEmptyView(contet));
//            if(mRefreshLayout!=null){
//                mRefreshLayout.setEnableLoadMore(false);
//                mRefreshLayout.finishRefresh();
//                mRefreshLayout.finishLoadMore();
//            }
//            ConfigApi.EMPTY_VIEW=true;
//        }else{
//            //请求有数据时分页才++
//            if(isRefresh){
//                mPage=2;
//            }else{
//                mPage++;
//            }
//            if(mRefreshLayout!=null) {
//                mRefreshLayout.setEnableLoadMore(true);
//                mRefreshLayout.finishRefresh();
//                mRefreshLayout.finishLoadMore();
//            }
//            ConfigApi.EMPTY_VIEW=false;
//        }
//        ALog.v(adapter.getEmptyViewCount()+"---adapter.getEmptyViewCount()---下一次请求的分页数："+mPage);
//    }
//
//    private View emptyView;
//    private View getEmptyView(String contet){
//        if(emptyView==null){
//            emptyView=getView(R.layout.empty_view);
//            emptyView.setOnClickListener(v -> ToastUtils.showShort("点击emptyView刷新不够优雅，直接下拉emptyView刷新吧"));
//            ViewGroup.LayoutParams lp=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
//            emptyView.setLayoutParams(lp);
//        }
//        CommUtils.setTextValues(emptyView.findViewById(R.id.tv_empty),contet);
//        return emptyView;
//    }
//
//
//    private View getView(@LayoutRes int layoutId){
//        return LayoutInflater.from(mContext).inflate(layoutId,null);
//    }



}
