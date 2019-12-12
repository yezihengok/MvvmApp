package com.example.commlib.webview;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.example.commlib.api.App;


public class  WebTools {

    /**
     * 网页加载失败时显示的本地默认网页
     */
    public static String DEFAULT_ERROR= "file:///android_asset/html/404.html";
    /**
     * 判断网络是否连通
     */
    public static boolean isNetworkConnected(Context context) {
        try {
            if (context != null) {
                @SuppressWarnings("static-access")
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
                NetworkInfo info = cm.getActiveNetworkInfo();
                return info != null && info.isConnected();
            } else {
                /**如果context为空，就返回false，表示网络未连接*/
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();
            return info != null && (info.getType() == ConnectivityManager.TYPE_WIFI);
        } else {
            /**如果context为null就表示为未连接*/
            return false;
        }
    }

    /**
     * 将 Android5.0以下手机不能直接打开mp4后缀的链接
     *
     * @param url 视频链接
     */
    public static String getVideoHtmlBody(String url) {
        return "<html>" +
                "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width\">" +
                "<style type=\"text/css\" abt=\"234\"></style>" +
                "</head>" +
                "<body>" +
                "<video controls=\"\" autoplay=\"\" name=\"media\">" +
                "<source src=\"" + url + "\" type=\"video/mp4\">" +
                "</video>" +
                "</body>" +
                "</html>";
    }

    /**
     * 实现文本复制功能
     *
     * @param content 复制的文本
     */
    public static void copy(String content) {
        if (!TextUtils.isEmpty(content)) {
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                ClipboardManager clipboard = (ClipboardManager) App.getInstance().getSystemService(Context.CLIPBOARD_SERVICE);
                clipboard.setText(content);
            } else {
                ClipboardManager clipboard = (ClipboardManager) App.getInstance().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(content, content);
                clipboard.setPrimaryClip(clip);
            }
        }
    }

    /**
     * 使用浏览器打开链接
     */
    public static void openLink(Context context, String content) {
        if (!TextUtils.isEmpty(content) && content.startsWith("http")) {
            Uri issuesUrl = Uri.parse(content);
            Intent intent = new Intent(Intent.ACTION_VIEW, issuesUrl);
            context.startActivity(intent);
        }
    }

    /**
     * 分享
     */
    public static void share(Context context, String extraText) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT,"分享");
        intent.putExtra(Intent.EXTRA_TEXT, extraText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent,"分享"));
    }

    /**
     * 通过包名找应用,不需要权限
     */
    public static boolean hasPackage(Context context, String packageName) {
        if (null == context || TextUtils.isEmpty(packageName)) {
            return false;
        }
        try {
            context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_GIDS);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            // 抛出找不到的异常，说明该程序已经被卸载
            return false;
        }
    }

    /**
     * 处理三方链接
     * 网页里可能唤起其他的app
     */
    public static boolean handleThirdApp(Activity activity, String backUrl) {
        /**http开头直接跳过*/
        if (backUrl.startsWith("http")) {
            // 可能有提示下载Apk文件
            if (backUrl.contains(".apk")) {
                startActivity(activity, backUrl);
                return true;
            }
            return false;
        }

        boolean isJump = true;
        /**屏蔽以下应用唤起App，可根据需求 添加或取消*/
        if (
                backUrl.startsWith("tbopen:")// 淘宝
//                        || backUrl.startsWith("openapp.jdmobile:")// 京东
//                        || backUrl.startsWith("jdmobile:")//京东
//                        || backUrl.startsWith("alipay:")// 支付宝
//                        || backUrl.startsWith("alipays:")//支付宝
                        || backUrl.startsWith("zhihu:")// 知乎
                        || backUrl.startsWith("vipshop:")//
                        || backUrl.startsWith("youku:")//优酷
                        || backUrl.startsWith("uclink:")// UC
                        || backUrl.startsWith("ucbrowser:")// UC
                        || backUrl.startsWith("newsapp:")//
                        || backUrl.startsWith("sinaweibo:")// 新浪微博
                        || backUrl.startsWith("suning:")//
                        || backUrl.startsWith("pinduoduo:")// 拼多多
      //                  || backUrl.startsWith("baiduboxapp:")// 百度
                        || backUrl.startsWith("qtt:")//
        ) {
            isJump = false;
        }
        if (isJump) {
            startActivity(activity, backUrl);
        }
        return isJump;
    }

    private static void startActivity(Context context, String url) {
        try {

            // 用于DeepLink测试
            if (url.startsWith("will://")) {
                Uri uri = Uri.parse(url);
                Log.e("---------scheme", uri.getScheme() + "；host: " + uri.getHost() + "；Id: " + uri.getPathSegments().get(0));
            }

            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri uri = Uri.parse(url);
            intent.setData(uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
