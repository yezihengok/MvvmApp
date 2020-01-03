package com.example.commlib.webview;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.blankj.ALog;
import com.example.commlib.R;
import com.example.commlib.api.App;
import com.example.commlib.base.mvvm.BaseViewModel;
import com.example.commlib.databinding.ActivityWebviewBinding;
import com.example.commlib.utils.StatusBarUtil;


/**
 * 公用展示的WebView的Activity
 * Created by yzh on 2019/12/11.
 */

public class WebViewActivity extends BaseWebAcivity<ActivityWebviewBinding, BaseViewModel> {

    // 网页链接
    private String mUrl;
    // 可滚动的title 使用简单 没有渐变效果，文字两旁有阴影
    private Toolbar mTitleToolBar;
    private String mTitle;

    int type = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_webview);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_webview;
    }

    @Override
    public void initViewObservable() {
    }

    @Override
    protected void initView() {
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
        mProgressBar = mBinding.pbProgress;
        mProgressBar.setColor(ContextCompat.getColor(this, R.color.ui_blue));
        mProgressBar.show();
        webView =mBinding.webviewDetail;
         initToolBar();
//        webView.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) ->
//            scrollChangeHeader(scrollY,titleLayout)
//        );
    }



    private void initToolBar() {
        mTitleToolBar = mBinding.commonToolbar;
        setSupportActionBar(mTitleToolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //去除默认Title显示
            actionBar.setDisplayShowTitleEnabled(false);
        }
         mTitleToolBar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.actionbar_more));
        mBinding.commonTitle.postDelayed(() -> mBinding.commonTitle.setSelected(true), 1000);
        setTitle(mTitle);

        mTitleToolBar.setNavigationOnClickListener(v -> back());
    }







    /**
     * 使用singleTask启动模式的Activity在系统中只会存在一个实例。
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getDataFromBrowser(intent);
    }



    @Override
    public void setTitle(String mTitle) {
        if (mBinding.commonTitle != null)
            mBinding.commonTitle.setText(mTitle);
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


    @Override
    public void startProgress(int newProgress) {
        super.startProgress(newProgress);
        ALog.i("newProgress:"+newProgress);


    }
}
