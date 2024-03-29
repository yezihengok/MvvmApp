package com.example.commlib.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.blankj.ALog;
import com.example.commlib.R;
import com.example.commlib.api.App;
import com.example.commlib.listener.Listener;
import com.example.commlib.weight.dialog.CommAlertDialog;

import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    /**
     * 避免数组越界(判断list是否 不为空未越界)
     *
     * @param list
     * @param index
     * @param <T>
     * @return
     */
    public static <T> boolean isNotIndexOutOf(List<T> list, int index) {
        if(index<0){
            return false;
        }
        return list != null && !list.isEmpty() && index < list.size();
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
     * 只有确定按钮的简化弹窗
     * @param msg
     * @param
     * @return
     */
    @NonNull
    public static Dialog showDialogBySure(Context context, String msg){
        return showDialogBySure(context,msg,null);
    }
    @NonNull
    public static Dialog showDialogBySure(Context context, String msg, Listener rightListener){
        return showDialogBySure(context,null,msg,rightListener);
    }
    @NonNull
    public static Dialog showDialogBySure(Context context, String title, String msg, Listener rightListener){
        return CommAlertDialog.showDialog(context,title,msg,null,"确定", null,rightListener);
    }
    @NonNull
    public static Dialog showDialogByCancelSure(Context context, String msg, Listener leftListener, Listener rightListener){
        return CommAlertDialog.showDialog(context,null,msg,"取消","确定", leftListener,rightListener);
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
     * 正则表达式判断字符串是数字，可以为正数，可以为负数，可含有小数点，不能含有字符。
     */
    public static boolean isNumeric(String values) {
        if (isEmpty(values)) {
            return false;
        }
        //Pattern pattern = Pattern.compile("-?[0-9]*.?[0-9]*");
        Pattern pattern = Pattern.compile("^(\\-|\\+)?\\d+(\\.\\d+)?$");
        Matcher isNum = pattern.matcher(values);
        if (!isNum.matches()) {
            return false;
        }
        return true;
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

    /**
     * 正则表达式匹配两个指定字符串中间的内容 biru
     * @param str
     * @param rgex 例如  ： "abc(.*?)abc"  "(?<=\\{)(.+?)(?=\\})"
     * @return
     */
    public static List<String> getSubUtil(String str,String rgex){
        List<String> list = new ArrayList<String>();
        Pattern pattern = Pattern.compile(rgex);// 匹配的模式
        Matcher m = pattern.matcher(str);
        while (m.find()) {
            int i = 1;
            list.add(m.group(i));
            System.out.println(m.group(i));
           // i++;
        }
        return list;
    }

    /**
     * 正则表达式匹配两个指定字符串中间的内容 并去掉 可能包含的 "-" 符号 并且是数字
     * @param str
     * @param rgex 例如  ： "abc(.*?)abc"  "(?<=\\{)(.+?)(?=\\})"
     * @return
     */
    public static List<String> getSubUtil1(String str,String rgex){
        List<String> list = new ArrayList<String>();
        Pattern pattern = Pattern.compile(rgex);// 匹配的模式
        Matcher m = pattern.matcher(str);
        while (m.find()) {
            int i = 1;
            String s=m.group(i);
            if(isNoEmpty(s)){
                s=s.replace("-","");
                if(isNumeric(s)){
                    list.add(s);
                }
            }
            System.out.println(m.group(i));
            // i++;
        }
        return list;
    }

    /**
     * 正则表达式匹配两个指定字符串中间的内容 并且是中文数字
     * @param str
     * @param rgex 例如  ： "abc(.*?)abc"  "(?<=\\{)(.+?)(?=\\})"
     * @return
     */
    public static List<String> getSubUtil2(String str,String rgex){
        List<String> list = new ArrayList<String>();
        Pattern pattern = Pattern.compile(rgex);// 匹配的模式
        Matcher m = pattern.matcher(str);
        while (m.find()) {
            int i = 1;
            String s=m.group(i);
            if(isNoEmpty(s)){
                s=s.replace("-","");
                if(isChineseNumber(s)){
                    list.add(s);
                }
            }
            System.out.println(m.group(i));
            // i++;
        }
        return list;
    }

    /**
     * 正则表达式匹配两个指定字符串中间的内容 biru
     * @param str
     * @param rgex 例如  ： "abc(.*?)abc"  "(?<=\\{)(.+?)(?=\\})"
     * @return
     */
    public static List<String> getSubUtil3(String str,String rgex,String left,String right){
        List<String> list = new ArrayList<String>();
        Pattern pattern = Pattern.compile(rgex);// 匹配的模式
        Matcher m = pattern.matcher(str);
        while (m.find()) {
            int i = 1;
            list.add(left+m.group(i)+right);//在追加上 匹配的前后缀
            System.out.println(m.group(i));
            // i++;
        }
        return list;
    }

    /**
     * pan
     * @return
     */
    public static boolean isChineseNumber(String s){
        String regex="^[零一二三四五六七八九十百千万亿壹贰叁肆伍陆柒捌玖拾佰仟萬億]+$";
        Pattern p=Pattern.compile(regex);
        Matcher m=p.matcher(s);
        if(m.matches()){
            System.out.println(s+" 是汉字的数字");
        }
        return m.matches();
    }

    /**
     * 获取 String
     * @param id R.string.id
     * @return String
     */
    public static String getString(@StringRes final int id) {
        try {
            return App.getInstance().getResources().getString(id);
        } catch (Exception e) {
            ALog.eTag("TAG", e, "getString");
        }
        return "";
    }

    /**
     * 获取 String
     * @param id         R.string.id
     * @param formatArgs 格式化参数
     * @return String
     */
    public static String getString(@StringRes final int id, final Object... formatArgs) {
        try {
            return App.getInstance().getResources().getString(id, formatArgs);
        } catch (Exception e) {
            ALog.eTag("TAG", e, "getString");
        }
        return null;
    }

    /**
     * 获取 Color
     * @param colorId R.color.id
     * @return Color
     */
    public static int getColor(@ColorRes final int colorId) {
        try {
            return ContextCompat.getColor(App.getInstance(), colorId);
        } catch (Exception e) {
            ALog.eTag("TAG", e, "getColor");
        }
        return -1;
    }

    /**
     * 获取 Drawable
     * @param drawableId R.drawable.id
     * @return {@link Drawable}
     */
    public static Drawable getDrawable(@DrawableRes final int drawableId) {
        try {
            return ContextCompat.getDrawable(App.getInstance(), drawableId);
        } catch (Exception e) {
            ALog.eTag("TAG", e, "getDrawable");
        }
        return null;
    }
    public static String getStringFormat(@StringRes int id, Object... args){
        return String.format(getString(id),args);
    }
    public static void setForegroundPressBg(@NonNull View ...views){
        if (views!=null&&Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (View v:views){
                v.setForeground(ContextCompat.getDrawable(App.getInstance(),R.drawable.selector_ripple));
            }
        }
    }

}
