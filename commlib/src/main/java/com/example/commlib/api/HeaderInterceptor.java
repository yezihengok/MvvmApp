package com.example.commlib.api;

import android.util.Log;

import com.blankj.ALog;
import com.example.commlib.BuildConfig;
import com.example.commlib.utils.AppUtils;
import com.example.commlib.utils.CheckNetwork;
import com.example.commlib.utils.ToastUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

public class HeaderInterceptor implements Interceptor {

    /**
     * 用于转换 Request 的 body 字符串
     */
    private static final Buffer BUFFER = new Buffer();
    /**
     * 用于格式化输出网络请求
     */
    private static final String OUTPUT = "%1$s\n-\n%2$s\n-\n%3$s";

    private static final String TAG = "HeaderInterceptor";

    public HeaderInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
       // builder.addHeader("token", SpUtil.getInstance().getString(SpKey.KEY_TOKEN));
        builder.addHeader("App-Version", String.valueOf(AppUtils.getVersionCode()));
        builder.addHeader("Model", AppUtils.getDevice());
        builder.addHeader("Content-Type", "application/json;charset=UTF-8");
        builder.addHeader("Accept", "application/json;versions=1");
        if (CheckNetwork.isNetworkConnected()) {
            int maxAge = 60;
            builder.addHeader("Cache-Control", "public, max-age=" + maxAge);
        } else {
            int maxStale = 60 * 60 * 24 * 28;
            builder.addHeader("Cache-Control", "public, only-if-cached, max-stale=" + maxStale);
        }

        Request request = builder.build();
        long startTime = System.currentTimeMillis();
        Response response = chain.proceed(request);
        long endTime = System.currentTimeMillis();
        Response.Builder responseBuilder = response.newBuilder();
        response = responseBuilder.build();

        //外面已经添加了addInterceptor 日志打印
      //  response = printRequestAndResponse(request, response, endTime - startTime);

        if(!CheckNetwork.isNetworkConnected()){
            ToastUtils.showShortSafe("没有网络连接~",2500);
            ALog.e("没有网络连接~~~~~~~~" );
        }
        return response;
    }

    /**
     * 将 Request 和 Response 的大致内容输出
     *
     * @param request  Request
     * @param response Response
     * @param time     请求时间
     * @return 返回 Response，因为 Response 的 body 只能读取一次，所以读取打印之后需要再塞回去生成新的 Response
     */
    private Response printRequestAndResponse(Request request, Response response, long time) {
        if (!CheckNetwork.isNetworkConnected()) {
            ToastUtils.showShortSafe("没有网络连接~");
        }

        String requestBody = "";
        String responseBody = "";
        try {
            if (request.body() != null) {
                request.body().writeTo(BUFFER);
                requestBody = BUFFER.readUtf8();
            }
            ResponseBody body = response.body();
            if (body != null) {
                responseBody = unicodeToString(body.string());
                response = response.newBuilder().body(ResponseBody.create(body.contentType(), responseBody)).build();
            }
        } catch (IOException e) {
            ALog.e(TAG,"is in trycatch");
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
        }
        Log.w(TAG, ">>>>>>> Http Request Start >>>>>>>");
        ALog.w(TAG, String.format(
                OUTPUT,
                String.valueOf(time) + "ms",
                request.url() + "\n" + request.headers().toString() + "\n" + requestBody,
                response.headers().toString() + "\n" + responseBody
        ));
        Log.w(TAG, "<<<<<<<  Http Request End  <<<<<<<");
        return response;
    }

    /**
     * Unicode 转成字符串
     *
     * @param str 输入
     * @return 返回字符串
     */
    public  String unicodeToString(String str) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(str);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            str = str.replace(matcher.group(1), ch + "");
        }
        return str;
    }

    private void addHeaders(Request.Builder builder, HashMap<String, String> headers){
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            builder.addHeader(entry.getKey(), entry.getValue());
            ALog.w(TAG, "key="+entry.getKey()+"value="+entry.getValue());
        }
    }





}
