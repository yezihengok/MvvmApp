package com.example.mvvmapp.viewmodel;

import android.annotation.SuppressLint;

import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.MutableLiveData;

import com.example.commlib.api.CommonObserver;
import com.example.commlib.base.BaseMvvmActivity;
import com.example.commlib.base.BaseMvvmRecyclerAdapters;
import com.example.commlib.base.BaseMvvmRecyclerHolder;
import com.example.commlib.base.BaseMvvmViewModel;
import com.example.commlib.bean.ResultBeans;
import com.example.commlib.listener.ResultCallback;
import com.example.commlib.utils.ToastUtils;
import com.example.mvvmapp.BR;
import com.example.mvvmapp.R;
import com.example.mvvmapp.api.HttpReq;
import com.example.mvvmapp.bean.ArticlesBean;
import com.example.mvvmapp.bean.WanAndroidBannerBean;
import com.trello.rxlifecycle3.android.ActivityEvent;

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

    public MainViewModel(BaseMvvmActivity activity) {
        super(activity);
    }

    private ObservableArrayList<ArticlesBean> mList=new ObservableArrayList<>();
    public BaseMvvmRecyclerAdapters<ArticlesBean> mAdapter=new BaseMvvmRecyclerAdapters<ArticlesBean>(mList, BR.itemBean,mContext,R.layout.item_message) {
        @Override
        public void convert(BaseMvvmRecyclerHolder holder,ArticlesBean item, int position) {
            holder.itemView.setOnClickListener(v -> ToastUtils.showShort(item.getLink()));
        }
    };



    //这里我举例了2种将ViewModel 请求接口的数据通知给View的方式 （acitivty）
    //同时也例举了2种防止Rxjava内存泄漏的方式
    public MutableLiveData<List<WanAndroidBannerBean>> getWanAndroidBanner() {
        //通知给View的方式1：使用LiveData 将数据通知给View
        final MutableLiveData<List<WanAndroidBannerBean>> data = new MutableLiveData<>();
        HttpReq.getInstance().getWanBanner()
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))//防止Rxjava内存泄漏方式1：用 RxLifecycle 将Rxjava绑定Acitivty/Fragment,销毁时自动取消订阅
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
        //通知给View的方式2：直接简单粗暴将ViewModel 请求的数据 通过接口回调给View
        Disposable subscribe= HttpReq.getInstance().getHomeList(mPage,cid)
                .subscribe(homeListBean -> { //可以使用CommonObserver弹窗
                    if(homeListBean.getErrorCode()==ERROR_CODE){
                        ToastUtils.showShort("接口请求失败了~~");
                    }
                    List<ArticlesBean> articlesBeans = null;
                    if(homeListBean.getData()!=null){
                        articlesBeans=homeListBean.getData().getDatas();
                        if(isListNotNull(articlesBeans)){
                            if(isRefresh){
                                mList.clear();
                                mPage=1;
                            }else{
                                mPage++;
                            }
                            mList.addAll(articlesBeans);
                        }
                        mAdapter.notifyDataSetChanged();
                      //  mAdapter.setLists(mList);
                    }
                    callback.onResult(articlesBeans);
                });

        //防止Rxjava内存泄漏方式2：将订阅的事件加入CompositeDisposable。Activity销毁时切断订阅
        addDisposable(subscribe);
    }
}
