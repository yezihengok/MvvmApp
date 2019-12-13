package com.example.commlib.webview;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.blankj.ALog;
import com.example.commlib.R;
import com.example.commlib.api.App;
import com.example.commlib.utils.StatusBarUtil;
import com.example.commlib.webview.config.FullscreenHolder;
import com.example.commlib.webview.config.IWebPageView;
import com.example.commlib.webview.config.MyJavascriptInterface;
import com.example.commlib.webview.config.MyWebChromeClient;
import com.example.commlib.webview.config.MyWebViewClient;
import com.example.commlib.webview.config.WebProgress;


/**
 * 公用展示的WebView的Activity
 * Created by yzh on 2019/12/11.
 */

public class WebViewActivity extends BaseWebAcivity implements IWebPageView {

    // 进度条
    private WebProgress mProgressBar;
    // 全屏时视频加载view
    private FrameLayout videoFullView;
    // 加载视频相关
    private MyWebChromeClient mWebChromeClient;
    // 网页链接
    private String mUrl;
    // 可滚动的title 使用简单 没有渐变效果，文字两旁有阴影
    private Toolbar mTitleToolBar;
    private WebView webView;
    private TextView tvGunTitle;
    private String mTitle;

    int type = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        mUrl = getIntent().getStringExtra("mUrl");
        mTitle = getIntent().getStringExtra("mTitle");
        type = getIntent().getIntExtra("type", 0);
        initTitle();
        initWebView();
        handleLoadUrl();
        getDataFromBrowser(getIntent());
    }

    private void handleLoadUrl() {
        if (!TextUtils.isEmpty(mUrl) && mUrl.endsWith("mp4") && Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            webView.loadData(WebTools.getVideoHtmlBody(mUrl), "text/html", "UTF-8");
        } else {
            webView.loadUrl(mUrl);
        }
    }

    private void initTitle() {
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.colorAccent), 0);
        mProgressBar = findViewById(R.id.pb_progress);
        mProgressBar.setColor(ContextCompat.getColor(this, R.color.ui_blue));
        mProgressBar.show();
        webView = findViewById(R.id.webview_detail);

         initToolBar();
//        webView.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) ->
//            scrollChangeHeader(scrollY,titleLayout)
//        );
    }



    private void back() {
        //返回网页上一页
        if (webView.canGoBack() && !onPageError) {
            webView.goBack();
            //退出网页
        } else {
            handleFinish();
        }
    }


    private void scrollChangeHeader(int scrolledY, View titleLayout) {

        if (scrolledY < 0) {
            scrolledY = 0;
        }
        // 滑动多少距离后标题透明
        int slidingDistance = 500;
        float alpha = Math.abs(scrolledY) * 1.0f / slidingDistance;
        //ALog.i("scrolledY "+scrolledY+"  "+alpha);
        if (alpha <= 1f) {
            titleLayout.setAlpha(1f - alpha);
        }


    }

    private void initToolBar() {
        mTitleToolBar = findViewById(R.id.common_toolbar);
        tvGunTitle = findViewById(R.id.common_title);
        setSupportActionBar(mTitleToolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //去除默认Title显示
            actionBar.setDisplayShowTitleEnabled(false);
        }
         mTitleToolBar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.actionbar_more));
        tvGunTitle.postDelayed(() -> tvGunTitle.setSelected(true), 1000);
        setTitle(mTitle);

        mTitleToolBar.setNavigationOnClickListener(v -> back());
    }


    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface", "JavascriptInterface"})
    private void initWebView() {
        WebSettings ws = webView.getSettings();
        // 网页内容的宽度是否可大于WebView控件的宽度
        ws.setLoadWithOverviewMode(false);
        // 保存表单数据
        ws.setSaveFormData(true);
        // 是否应该支持使用其屏幕缩放控件和手势缩放
        ws.setSupportZoom(true);
        ws.setBuiltInZoomControls(false);
        ws.setDisplayZoomControls(false);
        // 启动应用缓存
        ws.setAppCacheEnabled(true);
        // 设置缓存模式
        ws.setCacheMode(WebSettings.LOAD_DEFAULT);
        // setDefaultZoom  api19被弃用
        // 设置此属性，可任意比例缩放。
        ws.setUseWideViewPort(true);
        // 不缩放
        webView.setInitialScale(100);
        // 告诉WebView启用JavaScript执行。默认的是false。
        ws.setJavaScriptEnabled(true);
        //  页面加载好以后，再放开图片
        ws.setBlockNetworkImage(false);
        // 使用localStorage则必须打开
        ws.setDomStorageEnabled(true);
        // 排版适应屏幕
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        // WebView是否新窗口打开(加了后可能打不开网页)
//        ws.setSupportMultipleWindows(true);

        // webview从5.0开始默认不允许混合模式,https中不能加载http资源,需要设置开启。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ws.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        /** 设置字体默认缩放大小(改变网页字体大小,setTextSize  api14被弃用)*/
        ws.setTextZoom(100);

        mWebChromeClient = new MyWebChromeClient(this);
        webView.setWebChromeClient(mWebChromeClient);
        webView.setWebViewClient(new MyWebViewClient(this));
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return handleLongImage();
            }
        });

        // 与js交互
        webView.addJavascriptInterface(new MyJavascriptInterface(this), "injectedObject");
        webView.addJavascriptInterface(this, "androidInjected");
    }


    int time=0;
    @JavascriptInterface
    public void reload(String s){
        ALog.v("reload==="+s);
        if(time<3){
            runOnUiThread(() ->webView.reload());
        }else{
            ALog.w("java传递参数去调用 js方法");
            loadJs("javascript:callJsWithArgs('" + "请稍后再试哦~" + "')");
        }

        time++;
    }

    @Override
    public void showWebView() {
        webView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hindWebView() {
        webView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void fullViewAddView(View view) {
        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
        videoFullView = new FullscreenHolder(WebViewActivity.this);
        videoFullView.addView(view);
        decor.addView(videoFullView);
    }

    @Override
    public void showVideoFullView() {
        videoFullView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hindVideoFullView() {
        videoFullView.setVisibility(View.GONE);
    }

    @Override
    public void startProgress(int newProgress) {
        mProgressBar.setWebProgress(newProgress);
    }

    public void setTitle(String mTitle) {
        if (tvGunTitle != null)
            tvGunTitle.setText(mTitle);
    }

    /**
     * android与js交互：
     * 前端注入js代码：不能加重复的节点，不然会覆盖
     * 前端调用js代码
     */
    @Override
    public void onPageFinished(WebView view, String url) {
        if (!WebTools.isNetworkConnected(this)) {
            mProgressBar.hide();
        }
        loadImageClickJS();
        loadTextClickJS();
        loadCallJS();
    }

    /**
     * 处理是否唤起三方app
     */
    @Override
    public boolean isOpenThirdApp(String url) {
        return WebTools.handleThirdApp(this, url);
    }

    /**
     * 网页是否加载失败了
     */
    private boolean onPageError;

    @Override
    public void onReceivedError(int errorCode, String description) {
        onPageError = true;
        ALog.v("onReceivedError---"+onPageError);
    }

    @Override
    public void onPageStarted(String url) {
        if(!WebTools.DEFAULT_ERROR.equals(url)){
            onPageError = false;//每次加载时初始化错误状态
        }
        ALog.v("url: "+url);
    }

    /**
     * 前端注入JS：
     * 这段js函数的功能就是，遍历所有的img节点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
     */
    private void loadImageClickJS() {
        loadJs("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\");" +
                "for(var i=0;i<objs.length;i++)" +
                "{" +
                "objs[i].onclick=function(){window.injectedObject.imageClick(this.getAttribute(\"src\"));}" +
                "}" +
                "})()");
    }

    /**
     * 前端注入JS：
     * 遍历所有的<li>节点,将节点里的属性传递过去(属性自定义,用于页面跳转)
     */
    private void loadTextClickJS() {
        loadJs("javascript:(function(){" +
                "var objs =document.getElementsByTagName(\"li\");" +
                "for(var i=0;i<objs.length;i++)" +
                "{" +
                "objs[i].onclick=function(){" +
                "window.injectedObject.textClick(this.getAttribute(\"type\"),this.getAttribute(\"item_pk\"));}" +
                "}" +
                "})()");
    }

    /**
     * 传应用内的数据给html，方便html处理
     */
    private void loadCallJS() {
        // 无参数调用
        //   loadJs("javascript:javacalljs()");
        // 传递参数调用
        // loadJs("javascript:javacalljswithargs('" + "android传入到网页里的数据，有参" + "')");
    }

    /**
     * 全屏时按返加键执行退出全屏方法
     */
    public void hideCustomView() {
        mWebChromeClient.onHideCustomView();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public FrameLayout getVideoFullView() {
        return videoFullView;
    }

    @Override
    public View getVideoLoadingProgressView() {
        return LayoutInflater.from(this).inflate(R.layout.video_loading_progress, null);
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        setTitle(title);
    }

    @Override
    public void startFileChooserForResult(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    /**
     * 上传图片之后的回调
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == MyWebChromeClient.FILECHOOSER_RESULTCODE) {
            mWebChromeClient.mUploadMessage(intent, resultCode);
        } else if (requestCode == MyWebChromeClient.FILECHOOSER_RESULTCODE_FOR_ANDROID_5) {
            mWebChromeClient.mUploadMessageForAndroid5(intent, resultCode);
        }
    }

    /**
     * 使用singleTask启动模式的Activity在系统中只会存在一个实例。
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getDataFromBrowser(intent);
    }

    /**
     * 作为三方浏览器打开传过来的值
     * Scheme: https
     * host: www.jianshu.com
     * path: /p/1cbaf784c29c
     * url = scheme + "://" + host + path;
     */
    private void getDataFromBrowser(Intent intent) {
        Uri data = intent.getData();
        if (data != null) {
            try {
                String scheme = data.getScheme();
                String host = data.getHost();
                String path = data.getPath();
                String text = "Scheme: " + scheme + "\n" + "host: " + host + "\n" + "path: " + path;
                Log.e("data", text);
                String url = scheme + "://" + host + path;
                webView.loadUrl(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void handleFinish() {
        supportFinishAfterTransition();
//        if (!MainActivity.isLaunch) {
//            MainActivity.start(this);
//        }
    }

    /**
     * 4.4以上可用 evaluateJavascript 效率高
     */
    private void loadJs(String jsString) {

        runOnUiThread(() -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                webView.evaluateJavascript(jsString, null);
            } else {
                webView.loadUrl(jsString);
            }
        });

    }

    /**
     * 长按事件处理
     */
    private boolean handleLongImage() {
        final WebView.HitTestResult hitTestResult = webView.getHitTestResult();
        // 如果是图片类型或者是带有图片链接的类型
        if (hitTestResult.getType() == WebView.HitTestResult.IMAGE_TYPE ||
                hitTestResult.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
            // 弹出保存图片的对话框
//            new AlertDialog.Builder(WebViewActivity.this)
//                    .setItems(new String[]{"查看大图", "保存图片到相册"}, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            String picUrl = hitTestResult.getExtra();
//                            //获取图片
//                            Log.e("picUrl","picUrl: "+ picUrl);
//                            switch (which) {
//                                case 0:
//                                    break;
//                                case 1:
//                                    break;
//                                default:
//                                    break;
//                            }
//                        }
//                    })
//                    .show();
            return true;
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //全屏播放退出全屏
            if (mWebChromeClient.inCustomView()) {
                hideCustomView();
                return true;
            }  else {
                finish();
            }
        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
        // 支付宝网页版在打开文章详情之后,无法点击按钮下一步
        webView.resumeTimers();
        // 设置为横屏
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    protected void onDestroy() {
        if (videoFullView != null) {
            videoFullView.removeAllViews();
            videoFullView = null;
        }
        if (webView != null) {
            ViewGroup parent = (ViewGroup) webView.getParent();
            if (parent != null) {
                parent.removeView(webView);
            }
            webView.removeAllViews();
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.stopLoading();
            webView.setWebChromeClient(null);
            webView.setWebViewClient(null);
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }


    /**
     * 打开网页:
     *
     * @param mUrl   要加载的网页url
     * @param mTitle 标题
     */
    public static void loadUrl(String mUrl, String mTitle) {
        Intent intent = new Intent(App.getInstance(), WebViewActivity.class);
        intent.putExtra("mUrl", mUrl);
        intent.putExtra("mTitle", mTitle == null ? "加载中..." : mTitle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        App.getInstance().startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_webview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {// 返回键
            handleFinish();

        } else if (itemId == R.id.actionbar_share) {// 分享到
            String shareText = webView.getTitle() + webView.getUrl();
            WebTools.share(WebViewActivity.this, shareText);

        } else if (itemId == R.id.actionbar_cope) {// 复制链接
            WebTools.copy(webView.getUrl());
            Toast.makeText(this, "复制成功", Toast.LENGTH_LONG).show();

        } else if (itemId == R.id.actionbar_open) {// 打开链接
            WebTools.openLink(WebViewActivity.this, webView.getUrl());

        } else if (itemId == R.id.actionbar_webview_refresh) {// 刷新页面
            webView.reload();
        }
        return super.onOptionsItemSelected(item);
    }

}
