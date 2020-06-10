package com.example.mvvmapp.api;



import com.example.commlib.api.RetrofitFactory;
import com.example.commlib.bean.ResultBean;
import com.example.commlib.bean.ResultBeans;
import com.example.mvvmapp.bean.HomeListBean;
import com.example.mvvmapp.bean.WanAndroidBannerBean;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * @Description: Http请求类
 * 基于原有的 RetrofitFactory.getInstance()请求类 在封装的一层。
 * @Author: yzh
 * @CreateDate: 2019/10/28 9:57
 */
public class HttpReq {

    private static volatile HttpReq mInstance;
    public static HttpReq getInstance() {
        if (mInstance == null) {
            synchronized (HttpReq.class) {
                if (mInstance == null) {
                    mInstance = new HttpReq();
                }
            }
        }
        return mInstance;
    }

    /**
     *  返回 Observable<ResultBean<T>> 类型
     */
    private  <T> Observable<ResultBean<T>> requests(Observable<ResultBean<T>> beanObservable) {
        return  beanObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
                //onErrorReturn 的作用：请求失败了返回一个错误信息的对象,即使请求失败CommonObserver 里也会回调成功并返回一个错误码：ERROR_CODE的空对象
               // .onErrorReturn(t -> new ResultBean<>(ERROR_CODE, t.getMessage()));
    }
    private  <T> Observable<ResultBeans<T>> requestss(Observable<ResultBeans<T>> beanObservable) {
        return  beanObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
              //  .onErrorReturn(t -> new ResultBeans<>(ERROR_CODE, t.getMessage()));
    }

    private <T> Observable<T> requestT(Observable<T> beanObservable) {
        return  beanObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     *  返回 Observable<ResultBean> 类型
     */
    private  Observable<ResultBean> request(Observable<ResultBean> beanObservable) {
        return  beanObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
             //   .onErrorReturn(t -> new ResultBean<>(ERROR_CODE, t.getMessage()));
    }



//    /**
//     * 获取消息列表
//     */
//    public Observable<ResultBean<MessageBean>> getMessageList(String pageNo, String pageSize) {
//        return RetrofitFactory.getInstance().create(SeniorApi.class)
//                .getMessageList(pageNo,pageSize)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .onErrorReturn(t -> new ResultBean<>(ERROR_CODE, t.getMessage()));
//    }


    /**
     * 获取消息列表
     */
//    public Observable<ResultBean<MessageBean>> getMessageList(String pageNo, String pageSize) {
//        return requests(RetrofitFactory.getInstance().create(SeniorApi.class)
//                .getMessageList(pageNo,pageSize)
//        );
//    }


    public Observable<ResultBeans<WanAndroidBannerBean>> getWanBanner() {
        return requestss(RetrofitFactory.getInstance().create(AppApi.class)
                .getWanBanner());
    }

    public Observable<ResultBean<HomeListBean>> getHomeList(int page, int cid ) {
        HashMap<String, Integer> map=new HashMap<>();
        if(cid!=-1){
            map.put("cid",cid);
        }
        return requests(RetrofitFactory.getInstance().create(AppApi.class)
                .getHomeList(page,map));
    }

}
