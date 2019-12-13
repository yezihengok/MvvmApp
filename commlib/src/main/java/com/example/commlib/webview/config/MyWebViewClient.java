package com.example.commlib.webview.config;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.blankj.ALog;
import com.example.commlib.webview.WebTools;


/**
 * Created by jingbin on 2016/11/17.
 * 监听网页链接:
 * - 根据标识:打电话、发短信、发邮件
 * - 进度条的显示
 * - 添加javascript监听
 * - 唤起京东，支付宝，微信原生App
 */
public class MyWebViewClient extends WebViewClient {

    private IWebPageView mIWebPageView;

    public MyWebViewClient(IWebPageView mIWebPageView) {
        this.mIWebPageView = mIWebPageView;
    }


    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        mIWebPageView.onPageStarted(url);

        WebSettings webSettings = view.getSettings();
        Log.v("MyWebViewClient","onPageStarted()-返回 userAgent: "+webSettings.getUserAgentString());
    }


    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Log.e("jing", "----url:" + url);
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        return mIWebPageView.isOpenThirdApp(url);
    }


    @Override
    public void onPageFinished(WebView view, String url) {
        // html加载完成之后，添加监听图片的点击js函数
        mIWebPageView.onPageFinished(view, url);
        super.onPageFinished(view, url);
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        ALog.v(errorCode+"---onReceivedError---"+description);
        if (errorCode == 404) {
            //用javascript隐藏系统定义的404页面信息
//            String data = "Page NO FOUND！";
//            view.loadUrl("javascript:document.body.innerHTML=\"" + data + "\"");

            view.loadUrl(WebTools.DEFAULT_ERROR);//显示本地失败的html（注意会影响回退栈,失败了返回需要直接finish）
            mIWebPageView.onReceivedError(errorCode, description);
        }
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        //屏蔽系统默认的错误页面
        super.onReceivedError(view, request, error);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int errorCode=error.getErrorCode();
            ALog.v(errorCode+"---onReceivedError---"+error.getDescription());
            if (errorCode == 404) {
                view.loadUrl(WebTools.DEFAULT_ERROR);//显示本地失败的html（注意会影响回退栈,失败了返回需要直接finish）
                mIWebPageView.onReceivedError(errorCode, error.getDescription().toString());
            }
        }

    }


    // SSL Error. Failed to validate the certificate chain,error: java.security.cert.CertPathValidatorExcept
    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        ALog.e(error.toString());
//        if(isDbug){
//            //测试环境默认信任所有htpps的证书
            handler.proceed();
//        }else{
//            super.onReceivedSslError(view, handler, error);
//        }
    }

    // 视频全屏播放按返回页面被放大的问题
    @Override
    public void onScaleChanged(WebView view, float oldScale, float newScale) {
        super.onScaleChanged(view, oldScale, newScale);
        if (newScale - oldScale > 7) {
            view.setInitialScale((int) (oldScale / newScale * 100)); //异常放大，缩回去。
        }
    }

}
