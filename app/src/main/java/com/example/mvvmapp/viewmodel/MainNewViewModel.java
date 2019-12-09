package com.example.mvvmapp.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.MutableLiveData;

import com.example.commlib.api.CommonObserver;
import com.example.commlib.base.mvvm.BaseMvvmRecyclerAdapter;
import com.example.commlib.base.mvvm.BaseViewModel;
import com.example.commlib.bean.ResultBeans;
import com.example.commlib.event.SingleLiveEvent;
import com.example.commlib.utils.ToastUtils;
import com.example.mvvmapp.R;
import com.example.mvvmapp.api.HttpReq;
import com.example.mvvmapp.bean.ArticlesBean;
import com.example.mvvmapp.bean.WanAndroidBannerBean;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

import static com.example.commlib.api.ConfigApi.ERROR_CODE;
import static com.example.commlib.utils.CommUtils.isListNotNull;

/**
 *
 * @Author: yzh
 * @CreateDate: 2019/11/16 11:58
 */
@SuppressLint("CheckResult")
public class MainNewViewModel extends BaseViewModel {

    //使用LiveData 可通知Activity去toast
    public SingleLiveEvent<String> toastEvent = new SingleLiveEvent<>();

    @Override
    public void onBundle(Bundle bundle) {
    }

    public MainNewViewModel(@NonNull Application application) {
        super(application);
    }

    private ObservableArrayList<ArticlesBean> mList=new ObservableArrayList<>();

    public BaseMvvmRecyclerAdapter<ArticlesBean> mAdapter=new BaseMvvmRecyclerAdapter<ArticlesBean>(R.layout.item_message, mList) {
        @Override
        public void convert(BindingViewHolder holder, ArticlesBean item,int position) {
            holder.itemView.setOnClickListener(v -> {
                //当然可以直接在这toast,这里示例：回调给activity去处理
                //ToastUtils.showShort(item.getLink());
                toastEvent.setValue(item.getLink());
            });
        }
    };


//    在LiveData出现之前，一般状态分发我们使用EventBus或者RxJava，这些都很容易出现内存泄漏问题，而且需要我们手动管理生命周期。而LiveData则规避了这些问题，
//    LiveData是一个持有Activity、Fragment生命周期的数据容器。当数据源发生变化的时候，通知它的观察者(相应的界面)更新UI界面。同时它只会通知处于Active状态的观察者更新界面，
//    如果某个观察者的状态处于Paused或Destroyed时那么它将不会收到通知。所以不用担心内存泄漏问题。


    public MutableLiveData<List<WanAndroidBannerBean>> getWanAndroidBanner() {

        final MutableLiveData<List<WanAndroidBannerBean>> data = new MutableLiveData<>();
        HttpReq.getInstance().getWanBanner()
                .subscribe(new CommonObserver<ResultBeans<WanAndroidBannerBean>>(this,true) {
                    @Override
                    public void success(ResultBeans<WanAndroidBannerBean>  bannerBean) {
                        //ResultBean<WanAndroidBannerBean> 不会为空不需要做为空判断，因为前面调用失败new一个空对象
 //                       if (bannerBean != null){
//                           data.setValue(bannerBean);
//                        } else {
//                            data.setValue(null);
//                        }
                        data.setValue(bannerBean.getData());
                    }

                    @Override
                    public void error(Throwable e) {
                        data.setValue(null);
                    }
                });
        return data;
    }



    @SuppressLint("CheckResult")
    public MutableLiveData<List<ArticlesBean>> getHomeList(int cid,boolean isRefresh){
        if(isRefresh){
           // mPage = 1;
            mPage.set(1);
        }
        final MutableLiveData<List<ArticlesBean>> data = new MutableLiveData<>();
        Disposable subscribe= HttpReq.getInstance().getHomeList(mPage.get(),cid)
                .subscribe(homeListBean -> { //可以使用CommonObserver弹窗
                    if(homeListBean.getErrorCode()==ERROR_CODE){
                        ToastUtils.showShort("接口请求失败了~~");
                    }
                    List<ArticlesBean> articlesBeans = new ArrayList<>();
                    if(homeListBean.getData()!=null){
                        articlesBeans=homeListBean.getData().getDatas();
                        if(isListNotNull(articlesBeans)){
                            if(isRefresh){
                                mList.clear();
                            }
                            mList.addAll(articlesBeans);
                        }
                      //  mAdapter.setNewData(mList);  list改变 无需调用刷新，已封装在BaseMvvmRecyclerAdapter
                      //  mAdapter.notifyDataSetChanged();
                    }

                    setPage(mList,isRefresh);
                    data.setValue(mList);
                });

        addDisposable(subscribe);
        return data;
    }


}
