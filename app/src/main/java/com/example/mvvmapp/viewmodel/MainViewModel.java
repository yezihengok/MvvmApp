package com.example.mvvmapp.viewmodel;

import android.annotation.SuppressLint;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.MutableLiveData;

import com.example.commlib.api.CommonObserver;
import com.example.commlib.base.BaseMvvmActivity;
import com.example.commlib.base.BaseMvvmRecyclerAdapter;
import com.example.commlib.base.BaseMvvmViewModel;
import com.example.commlib.bean.ResultBeans;
import com.example.commlib.event.SingleLiveEvent;
import com.example.commlib.listener.ResultCallback;
import com.example.commlib.utils.ToastUtils;
import com.example.mvvmapp.R;
import com.example.mvvmapp.api.HttpReq;
import com.example.mvvmapp.bean.ArticlesBean;
import com.example.mvvmapp.bean.WanAndroidBannerBean;
import com.trello.rxlifecycle3.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

import static com.example.commlib.api.ConfigApi.ERROR_CODE;
import static com.example.commlib.utils.CommUtils.isListNotNull;

/**
 * @Description: 举例ViewModel的使用
 * @Author: yzh
 * @CreateDate: 2019/11/16 11:58
 */
@SuppressLint("CheckResult")
public class MainViewModel extends BaseMvvmViewModel {

    //使用LiveData 通知Activity去toast
    public SingleLiveEvent<String> toastEvent = new SingleLiveEvent<>();

    public MainViewModel(BaseMvvmActivity activity) {
        super(activity);
    }

    private ObservableArrayList<ArticlesBean> mList=new ObservableArrayList<>();

    public BaseMvvmRecyclerAdapter<ArticlesBean> mAdapter=new BaseMvvmRecyclerAdapter<ArticlesBean>(R.layout.item_message, mList) {
        @Override
        public void convert(BindingViewHolder holder, ViewDataBinding binding, ArticlesBean item,int position) {
            holder.itemView.setOnClickListener(v -> {
                //ToastUtils.showShort(item.getLink());
                click(item.getLink());
            });
        }
    };


//    在LiveData出现之前，一般状态分发我们使用EventBus或者RxJava，这些都很容易出现内存泄漏问题，而且需要我们手动管理生命周期。而LiveData则规避了这些问题，
//    LiveData是一个持有Activity、Fragment生命周期的数据容器。当数据源发生变化的时候，通知它的观察者(相应的界面)更新UI界面。同时它只会通知处于Active状态的观察者更新界面，
//    如果某个观察者的状态处于Paused或Destroyed时那么它将不会收到通知。所以不用担心内存泄漏问题。





    //这里我举例了2种将ViewModel 请求接口的数据通知给View的方式 （acitivty）
    //同时也例举了2种防止Rxjava内存泄漏的方式 （LiveData方式则无需考虑）


    public MutableLiveData<List<WanAndroidBannerBean>> getWanAndroidBanner() {
        //通知给View的方式1：使用LiveData 将数据通知给View(推荐使用这种方式~~)
        //todo LiveData的优点：不用手动控制生命周期，不用担心内存泄露，数据变化时会收到通知。
        final MutableLiveData<List<WanAndroidBannerBean>> data = new MutableLiveData<>();
        HttpReq.getInstance().getWanBanner()
                .subscribe(new CommonObserver<ResultBeans<WanAndroidBannerBean>>(mContext,true) {
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
    public void getHomeList(int cid,boolean isRefresh, ResultCallback<List<ArticlesBean>> callback){
        if(isRefresh){
            mPage = 1;
        }
        //通知给View的方式2：直接简单粗暴将ViewModel 请求的数据 通过接口回调给View
        Disposable subscribe= HttpReq.getInstance().getHomeList(mPage,cid)
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))//防止Rxjava内存泄漏方式1：用 RxLifecycle 将Rxjava绑定Acitivty/Fragment,销毁时自动取消订阅
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
                    showEmptyView(mList,mAdapter,isRefresh,"数据空空如也~");
                    callback.onResult(articlesBeans);

                });

        //防止Rxjava内存泄漏方式2：将订阅的事件加入CompositeDisposable。Activity销毁时切断订阅
     //   addDisposable(subscribe);
    }


    public void click(String str){
        toastEvent.setValue(str);
    }
}
