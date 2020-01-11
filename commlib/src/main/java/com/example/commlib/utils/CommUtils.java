package com.example.commlib.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ArrayRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.content.FileProvider;

import com.blankj.ALog;
import com.example.commlib.R;
import com.example.commlib.api.App;
import com.example.commlib.listener.Listener;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

import static com.example.commlib.utils.DensityUtil.getScreenWidth;

/**
 * @Description: 公用的一些方法
 * @Author: yzh
 * @CreateDate: 2019/10/23 14:37
 */
public class CommUtils {
    private static final String TAG="CommUtils";
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     * @param dpValue dp 值
     * @return int 转换后的值
     */
    public static int dip2px(float dpValue) {
        final float scale = App.getInstance().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     * @param pxValue dp 值
     * @return int 转换后的值
     */
    public static int px2dip( float pxValue) {
        final float scale = App.getInstance().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * 直接获取控件的宽、高
     * @param view
     * @return int[]
     */
    public static int getViewHeight(final View view){
        ViewTreeObserver vto2 = view.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        return view.getHeight();
    }

    /**
     * 直接获取控件的宽、高
     * @param view
     * @return int[]
     */
    public static int getViewWidth(final View view){
        ViewTreeObserver vto2 = view.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        return view.getWidth();
    }

    public static void isMainThread() {
        Log.e(TAG, "---是否在主线程：" + (Thread.currentThread() == Looper.getMainLooper().getThread()));
    }

    /**
     * 判断list 是否为空
     * @param list
     * @param <T>
     * @return
     */
    public static  <T> boolean isListNotNull(List<T> list){
        if(list!=null&&!list.isEmpty()){
            return true;
        }
        return false;
    }
    /**
     * 判断list 是否为空
     * @param list
     * @param <T>
     * @return
     */
    public static  <T> boolean isListNull(List<T> list){
        if(list!=null&&!list.isEmpty()){
            return false;
        }
        return true;
    }

    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0 || s.equals("null");
    }

    public static boolean isNoEmpty(String s) {
        boolean bool=s == null || s.length() == 0 || s.equals("null");
        return !bool;
    }

    public static boolean  isJson(String json){
        try{
            JSONObject jsonObject = new JSONObject(json);
        }catch(Exception e){
            //Log.e(TAG,"该字符串不是json!\n"+json);
            return false;
        }
        return true;
    }



    /**
     * 设置某个View的margin
     *
     * @param view   需要设置的view
     * @param isDp   需要设置的数值是否为DP
     * @param left   左边距
     * @param right  右边距
     * @param top    上边距
     * @param bottom 下边距
     * @return
     */
    public static ViewGroup.LayoutParams setViewMargin(View view, boolean isDp, int left, int right, int top, int bottom) {
        if (view == null) {
            return null;
        }

        int leftPx = left;
        int rightPx = right;
        int topPx = top;
        int bottomPx = bottom;
        ViewGroup.LayoutParams params = view.getLayoutParams();
        ViewGroup.MarginLayoutParams marginParams = null;
        //获取view的margin设置参数
        if (params instanceof ViewGroup.MarginLayoutParams) {
            marginParams = (ViewGroup.MarginLayoutParams) params;
        } else {
            //不存在时创建一个新的参数
            marginParams = new ViewGroup.MarginLayoutParams(params);
        }

        //根据DP与PX转换计算值
        if (isDp) {
            leftPx = dip2px(left);
            rightPx = dip2px(right);
            topPx = dip2px(top);
            bottomPx = dip2px(bottom);
        }
        //设置margin
        marginParams.setMargins(leftPx, topPx, rightPx, bottomPx);
        view.setLayoutParams(marginParams);
        view.requestLayout();
        return marginParams;
    }

    /**
     * 在代码中为TextView 设置color资源
     *
     * @param tv
     * @param color 例如 R.color.oranges
     */
    public static void setTextColor(TextView tv, int color) {
        Resources resource = (Resources) tv.getContext().getResources();
//        ColorStateList csl = (ColorStateList) resource.getColorStateList(color);
//        if(csl!=null){
//        }
        tv.setTextColor(resource.getColor(color));
    }
    /**editText设置文字后 光标设置为文字末尾**/
    public static void setTextSelectEnd(@NonNull EditText editText, @NonNull String str){
        if (editText!=null)
        {
            if(str!=null){
                editText.setText(str);
                editText.setSelection(editText.getText().length());
            }

        }
    }
    /**
     * 为textview 设值，避免空值情况
     *
     * @param tv
     * @param str
     */
    public static void setTextValues(TextView tv, String str) {
        if (tv != null && !TextUtils.isEmpty(str)) {
            tv.setText(str);
        }
    }
    public static void setTextValues(TextView tv, @StringRes int id) {
        String str = tv.getContext().getString(id);
        if (tv != null && !TextUtils.isEmpty(str)) {
            tv.setText(str);
        }
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height"
                , "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        if (result == 0) {
            final float scale = context.getResources().getDisplayMetrics().density;
            result = (int) (26 * scale + 0.5f);
        }
        return result;
    }


    /**
     * 优化确认取消弹窗方法 2016-3-1 yzh update
     *
     * @param msg           弹框信息
     * @param leftName      左边按钮名称
     * @param rightName     右边按钮名称 (传null表示只显示一个按钮)
     * @param leftlistener  左边按钮监听 (无需监听事件可传null)
     * @param rightlistener 右边按钮监听 (无需监听事件可传null)
     * @param color         设置按钮文字颜色  #FFFFFF (可传null取默认)
     * @return
     */
    public static Dialog showDialog(Context context, String title, String msg, String leftName, String rightName
            , final Listener leftlistener, final Listener rightlistener, String... color) {

        final Dialog showDialog = new Dialog(context);
        showDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        showDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        showDialog.getWindow().setGravity(Gravity.CENTER);
        showDialog.setContentView(R.layout.comm_show_dialogs);
        showDialog.setCancelable(false);
        if (!((Activity) context).isFinishing()) {
            showDialog.show();
        }

        LinearLayout lyrame;
        Button btSure, btCancel;
        TextView tvContent;

        lyrame =showDialog.findViewById(R.id.lyShow_Frame);
        btSure =  showDialog.findViewById(R.id.btShow_sure);
        btCancel =showDialog.findViewById(R.id.btShow_cancle);
        tvContent = showDialog.findViewById(R.id.tvShow_content);
        lyrame.getLayoutParams().width = getScreenWidth() - dip2px(100);

        tvContent.setText(msg);

        if(msg.length()>=20){
            tvContent.setGravity(Gravity.LEFT);
        }else{
            tvContent.setGravity(Gravity.CENTER);
        }
        if (color != null && color.length > 0) {
            btSure.setTextColor(Color.parseColor(color[0]));
            if (color.length >1) {
                btCancel.setTextColor(Color.parseColor(color[1]));
            }

        } else {
            //默认颜色
            setTextColor(btSure, R.color.ui_blue);
            setTextColor(btCancel,R.color.ui_blue);
        }

        if(StringUtil.isNoEmpty(title)){
            TextView tvTitle =showDialog.findViewById(R.id.tvTitle);
            tvTitle.setText(title);
        }

        if (StringUtil.isEmpty(leftName)) {
            btSure.setVisibility(View.GONE);
            showDialog.findViewById(R.id.spit).setVisibility(View.GONE);
        } else {
            btSure.setText(leftName);
            btSure.setOnClickListener(v -> {
                showDialog.dismiss();
                if (leftlistener != null)
                    leftlistener.onResult();
            });
        }

        if (StringUtil.isEmpty(rightName)) {
            btCancel.setVisibility(View.GONE);
            showDialog.findViewById(R.id.spit).setVisibility(View.GONE);
        } else {
            btCancel.setText(rightName);
            btCancel.setOnClickListener(v -> {
                showDialog.dismiss();
                if (rightlistener != null)
                    rightlistener.onResult();
            });
        }

        if (StringUtils.isEmpty(rightName) || StringUtils.isEmpty(leftName)) {
            showDialog.findViewById(R.id.spit).setVisibility(View.GONE);
        }
        return showDialog;
    }

    /**
     * 点击按钮后需要手动关闭的弹窗
     *
     * @param msg           弹框信息
     * @param leftName      左边按钮名称
     * @param rightName     右边按钮名称 (传null表示只显示一个按钮)
     * @param leftlistener  左边按钮监听 (无需监听事件可传null)
     * @param rightlistener 右边按钮监听 (无需监听事件可传null)
     * @param color         设置按钮文字颜色  #FFFFFF (可传null取默认)
     * @return
     */
    public static TextView showDialogs(Context context, String title, String msg, String leftName, String rightName
            , final Listener leftlistener, final Listener rightlistener, String... color) {

        final Dialog showDialog = new Dialog(context);
        showDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        showDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        showDialog.getWindow().setGravity(Gravity.CENTER);
        showDialog.setContentView(R.layout.comm_show_dialogs);
        showDialog.setCancelable(false);
        if (!((Activity) context).isFinishing()) {
            showDialog.show();
        }

        LinearLayout lyrame;
        Button btSure, btCancel;
        TextView tvContent;

        lyrame =showDialog.findViewById(R.id.lyShow_Frame);
        btSure =  showDialog.findViewById(R.id.btShow_sure);
        btCancel =showDialog.findViewById(R.id.btShow_cancle);
        tvContent = showDialog.findViewById(R.id.tvShow_content);
        lyrame.getLayoutParams().width = getScreenWidth() - dip2px(100);
        tvContent.setText(msg);

        if(msg.length()>=20){
            tvContent.setGravity(Gravity.LEFT);
        }else{
            tvContent.setGravity(Gravity.CENTER);
        }
        if (color != null && color.length > 0) {
            btSure.setTextColor(Color.parseColor(color[0]));
            if (color.length >1) {
                btCancel.setTextColor(Color.parseColor(color[1]));
            }

        } else {
            //默认颜色
            setTextColor(btSure, R.color.ui_blue);
            setTextColor(btCancel,R.color.ui_blue);
        }

        if(StringUtil.isNoEmpty(title)){
            TextView tvTitle =showDialog.findViewById(R.id.tvTitle);
            tvTitle.setText(title);
        }

        if (StringUtil.isEmpty(leftName)) {
            btSure.setVisibility(View.GONE);
            showDialog.findViewById(R.id.spit).setVisibility(View.GONE);
        } else {
            btSure.setText(leftName);
            btSure.setOnClickListener(v -> {
               // showDialog.dismiss();
                if (leftlistener != null)
                    leftlistener.onResult();
            });
        }

        if (StringUtil.isEmpty(rightName)) {
            btCancel.setVisibility(View.GONE);
            showDialog.findViewById(R.id.spit).setVisibility(View.GONE);
        } else {
            btCancel.setText(rightName);
            btCancel.setOnClickListener(v -> {
                //showDialog.dismiss();
                if (rightlistener != null)
                    rightlistener.onResult();
            });
        }

        if (StringUtils.isEmpty(rightName) || StringUtils.isEmpty(leftName)) {
            showDialog.findViewById(R.id.spit).setVisibility(View.GONE);
        }
        return tvContent;
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
        Uri fileUri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            fileUri = FileProvider.getUriForFile(App.getInstance(), App.getInstance().getPackageName() + ".fileprovider", file);
        } else {
            fileUri = Uri.fromFile(file);
        }
        return fileUri;
    }


    /**
     * 根据资源名称获取 资源id
     * @param name
     * @param type 资源类型  drawable、 raw
     * @return
     */

    public static int getResId(String name,String type){
        Resources r=App.getInstance().getResources();
        int id = 0;
        try {
            id = r.getIdentifier(name,type, App.getInstance().getPackageName());
            //踩坑提示  如果是在插件化环境里 context.getPackageName() 获取的包名会变成宿主的包名，建议写死包名或者反射获取
            ALog.v("BaseApplication.getInstance().getPackageName()=="+App.getInstance().getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }



    /**
     * 根据资源名称获取 资源id （通过反射）
     * @param imageName
     * @param mipmap   资源类型  例如  R.mipmap.class
     * @return
     */
    public  static int  getResId(String imageName,Class mipmap){
        //Class mipmap = R.raw.class;
        //Class mipmap = R.mipmap.class;
        try {
            Field field = mipmap.getField(imageName);
            int resId = field.getInt(imageName);
            return resId;
        } catch (NoSuchFieldException | IllegalAccessException e) {//如果没有在"mipmap"下找到imageName,将会返回0
            return 0;
        }

    }

    /**
     * 从arrays.xml中读取数组
     * @param resId
     * @return
     */
    public static int[] getArrays(@ArrayRes int resId){
        TypedArray array = App.getInstance().getResources().obtainTypedArray(resId);
        int len = array.length();
        int[] intArray = new int[array.length()];

        for(int i = 0; i < len; i++){
            intArray[i] = array.getResourceId(i, 0);
        }
        array.recycle();
        return intArray;
    }


    /**
     * 将秒 格式化显示（例：4000秒 =  01:06:40）
     * @return
     */
    // 将秒转化成小时分钟秒
    public static String showTime(long miss){
        String hh=miss/3600>9?miss/3600+"":"0"+miss/3600;
        String  mm=(miss % 3600)/60>9?(miss % 3600)/60+"":"0"+(miss % 3600)/60;
        String ss=(miss % 3600) % 60>9?(miss % 3600) % 60+"":"0"+(miss % 3600) % 60;
        if(!"00".equals(hh)){
            return hh+":"+mm+":"+ss;
        }else{
            return mm+":"+ss;
        }
    }


    /**
     * 将秒 格式化显示（例：4000秒 =  1:06:40）1小时6分钟40秒
     * @return
     */
    public String showTimes(long miss){
        String hh=miss/3600>9?miss/3600+"":"0"+miss/3600;
        String  mm=(miss % 3600)/60>9?(miss % 3600)/60+"":"0"+(miss % 3600)/60;
        String ss=(miss % 3600) % 60>9?(miss % 3600) % 60+"":"0"+(miss % 3600) % 60;
        return hh+"小时"+mm+"分钟"+ss+"秒";
    }

}
