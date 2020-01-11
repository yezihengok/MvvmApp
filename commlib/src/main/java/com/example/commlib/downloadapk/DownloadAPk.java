package com.example.commlib.downloadapk;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import com.blankj.ALog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;


/**
 * 下载工具类（开发中一般用于APK应用升级）
 *  需要断点续传的可使用这个库 ：https://github.com/ssseasonnn/RxDownload
 */
public class DownloadAPk {
    private  static int   FILE_LEN    = 0;
    public   static String  APK_UPGRADE = Environment.getExternalStorageDirectory() + "/DownLoad/apk/downloadApp.apk";
    private static Context  mContext;
    public static volatile DownloadAPk downloadAPk;
    private static DownLoadListener mListener;
    public static  DownloadAPk getInstance(){
        if(downloadAPk==null){
            synchronized (DownloadAPk.class){
                if(downloadAPk==null){
                    downloadAPk=new DownloadAPk();
                }
            }
        }
        return downloadAPk;
    }
    /**
     * 判断8.0 安装权限
     */
    public  void downApk(Context context, String url,DownLoadListener mListener) {
        mContext = context;
        this.mListener=mListener;
        if (Build.VERSION.SDK_INT >= 26) {
            boolean b = context.getPackageManager().canRequestPackageInstalls();
            if (b) {
                downloadAPK(url, null);
            } else {
                //请求安装未知应用来源的权限
                startInstallPermissionSettingActivity();
            }
        } else {
            downloadAPK(url, null);
        }
    }


    /**
     * 开启安装APK权限(适配8.0)
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startInstallPermissionSettingActivity() {
        Uri packageURI = Uri.parse("package:" + mContext.getPackageName());
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
        mContext.startActivity(intent);
    }

    /**
     * 下载APK文件
     */
    private  void downloadAPK(String url, String localAddress) {
        // 下载
        if (localAddress != null) {
            APK_UPGRADE = localAddress;
        }
        new UpgradeTask().execute(url);

    }

    static class UpgradeTask extends AsyncTask<String, Integer, Void> {

        @Override
        protected void onPreExecute() { }

        @Override
        protected Void doInBackground(String... params) {

            String apkUrl = params[0];
            InputStream is = null;
            FileOutputStream fos = null;
            try {
                URL url = new URL(apkUrl);
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                // 设置连接超时时间
                conn.setConnectTimeout(20000);
                // 设置下载数据超时时间
                conn.setReadTimeout(25000);
                if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return null;// 服务端错误响应
                }
                is = conn.getInputStream();
                FILE_LEN = conn.getContentLength();
                File apkFile = new File(APK_UPGRADE);
                // 如果文件夹不存在则创建
                if (!apkFile.getParentFile().exists()) {
                    apkFile.getParentFile().mkdirs();
                }
                fos = new FileOutputStream(apkFile);
                byte[] buffer = new byte[8024];
                int len = 0;
                int loadedLen = 0;// 当前已下载文件大小
                // 更新100次onProgressUpdate 回調次數
                int updateSize = FILE_LEN / 100;
                int num = 0;
                while (-1 != (len = is.read(buffer))) {
                    loadedLen += len;
                    fos.write(buffer, 0, len);
                    if (loadedLen > updateSize * num) {
                        num++;
                        publishProgress(loadedLen);
                    }
                }
                fos.flush();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (SocketTimeoutException e) {
                // 处理超时异常，提示用户在网络良好情况下重试
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            int progress;
            if(values[0] ==FILE_LEN){
                progress=100;
            }else{
                progress = values[0] * 100 / FILE_LEN;
                //进度显示2位小数：
              // double progress= ArithUtils.round((values[0] * 100 / (double) FILE_LEN),2);
            }

            Log.w("DownloadAPk",progress + "%    总大小：" + FILE_LEN+"已下载大小："+values[0]);
            mListener.onProgressUpdate(progress);
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.v("DownloadAPk","下载完成");
            mListener.finish(APK_UPGRADE);

            mContext.startActivity(getInstallAppIntent(APK_UPGRADE));
        }


    }


    /**
     * 调往系统APK安装界面（适配7.0）
     *
     * @return
     */
    public static Intent getInstallAppIntent(String filePath) {
        //apk文件的本地路径
        File apkfile = new File(filePath);
        if (!apkfile.exists()) {
            return null;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri contentUri = getUriForFile(apkfile);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        return intent;
    }

    /**
     * 将文件转换成uri
     *
     * @return
     */
    public static Uri getUriForFile(File file) {
        ALog.v(file.getPath());
        Uri fileUri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            fileUri = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".fileprovider", file);
        } else {
            fileUri = Uri.fromFile(file);
        }
        ALog.v(fileUri.toString());
        return fileUri;
    }

   public interface DownLoadListener{
        void onProgressUpdate(int progress);
        void finish(String filePath);
    }
}
