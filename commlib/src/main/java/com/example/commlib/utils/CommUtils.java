package com.example.commlib.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Looper;
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

import androidx.annotation.NonNull;

import com.example.commlib.R;
import com.example.commlib.api.App;
import com.example.commlib.listener.Listener;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

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

}
