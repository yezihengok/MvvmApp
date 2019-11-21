package com.example.mvvmapp.api;

import com.example.commlib.bean.ResultBean;
import com.example.commlib.bean.ResultBeans;
import com.example.mvvmapp.bean.HomeListBean;
import com.example.mvvmapp.bean.LoginBean;
import com.example.mvvmapp.bean.WanAndroidBannerBean;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * @Description: AppApi类作用描述
 * @Author: yzh
 * @CreateDate: 2019/11/16 14:17
 */
public interface AppApi {

    /**
     * 玩安卓轮播图
     */
    @GET("banner/json")
    Observable<ResultBeans<WanAndroidBannerBean>> getWanBanner();
    /**
     * 玩安卓，文章列表、知识体系下的文章
     *
     * @param page 页码，从0开始
     * @param cid  体系id
     */
    @GET("article/list/{page}/json")
    Observable<ResultBean<HomeListBean>> getHomeList(@Path("page") int page, @Query("cid") Integer cid);

    @GET("article/list/{page}/json")
    Observable<ResultBean<HomeListBean>> getHomeList(@Path("page") int page, @QueryMap Map<String,Integer> map);




    /**
     * 玩安卓登录
     *
     * @param username 用户名
     * @param password 密码
     */
    @FormUrlEncoded
    @POST("user/login")
    Observable<LoginBean> login(@Field("username") String username, @Field("password") String password);

    /**
     * 玩安卓注册
     */
    @FormUrlEncoded
    @POST("user/register")
    Observable<LoginBean> register(@Field("username") String username, @Field("password") String password, @Field("repassword") String repassword);
}
