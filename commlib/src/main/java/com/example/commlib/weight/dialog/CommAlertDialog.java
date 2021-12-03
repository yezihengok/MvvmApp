package com.example.commlib.weight.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.commlib.R;
import com.example.commlib.listener.Listener;
import com.example.commlib.utils.ScreenUtils;

import java.lang.ref.WeakReference;

import static com.example.commlib.utils.ScreenUtils.getScreenWidth;


/**
 * 公用样式 确认取消弹窗
 * Author：yzh
 * Date：2021/12/3
 */

public class CommAlertDialog {
    private final Context context;
    private final WeakReference<Context> contextWeakReference;
    private  Dialog showDialog;
    public CommAlertDialog(Context context) {
        contextWeakReference=new WeakReference<>(context);
        this.context = contextWeakReference.get();
    }

    @SuppressLint("StaticFieldLeak")
    private static volatile CommAlertDialog instance;
    public  static CommAlertDialog get(Context context){
        if(instance==null){
            synchronized (CommAlertDialog.class){
                if(instance==null){
                    instance=new CommAlertDialog(context);
                }
            }
        }
        return instance;
    }

    /**
     * 优化确认取消弹窗方法
     * @param title         标题
     * @param msg           弹框信息
     * @param leftName      左边按钮名称
     * @param rightName     右边按钮名称 (传null表示只显示一个按钮)
     * @param leftListener  左边按钮监听 (无需监听事件可传null)
     * @param rightListener 右边按钮监听 (无需监听事件可传null)
     * @param color         设置按钮文字颜色  #FFFFFF (可传null取默认)
     */
    public  Dialog showDialog(String title, String msg, String leftName, String rightName
            , final Listener leftListener, final Listener rightListener, String... color) {

        final Dialog showDialog = new Dialog(context);
        showDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        showDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        showDialog.getWindow().setGravity(Gravity.CENTER);
        showDialog.setContentView(R.layout.comm_show_dialogs);
        showDialog.setCancelable(false);

        if (!((Activity) context).isFinishing()&&isNoEmpty(msg)) {
            showDialog.show();
        }

        LinearLayout lyrame;
        Button btSure, btCancel;
        TextView tvContent;

        lyrame =showDialog.findViewById(R.id.lyShow_Frame);
        btSure =  showDialog.findViewById(R.id.btShow_sure);
        btCancel =showDialog.findViewById(R.id.btShow_cancle);
        tvContent = showDialog.findViewById(R.id.tvShow_content);
        lyrame.getLayoutParams().width = ScreenUtils.getScreenWidth()*4/5;

        tvContent.setText(msg);

        if(msg.length()>=20){
            tvContent.setGravity(Gravity.START);
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

        if(isNoEmpty(title)){
            TextView tvTitle =showDialog.findViewById(R.id.tvTitle);
            tvTitle.setText(title);
        }

        if (isEmpty(leftName)) {
            btSure.setVisibility(View.GONE);
            showDialog.findViewById(R.id.spit).setVisibility(View.GONE);
        } else {
            btSure.setText(leftName);
            btSure.setOnClickListener(v -> {
                dismissDialog();
                if (leftListener != null)
                    leftListener.onResult();
            });
        }

        if (isEmpty(rightName)) {
            btCancel.setVisibility(View.GONE);
            showDialog.findViewById(R.id.spit).setVisibility(View.GONE);
        } else {
            btCancel.setText(rightName);
            btCancel.setOnClickListener(v -> {
                dismissDialog();
                if (rightListener != null)
                    rightListener.onResult();
            });
        }

        if (isEmpty(rightName) || isEmpty(leftName)) {
            showDialog.findViewById(R.id.spit).setVisibility(View.GONE);
        }
        this.showDialog=showDialog;
        return showDialog;
    }


    /**
     * 优化确认取消弹窗方法
     * @param title         标题
     * @param msg           弹框信息
     * @param leftName      左边按钮名称
     * @param rightName     右边按钮名称 (传null表示只显示一个按钮)
     * @param leftListener  左边按钮监听 (无需监听事件可传null)
     * @param rightListener 右边按钮监听 (无需监听事件可传null)
     * @param color         设置按钮文字颜色  #FFFFFF (可传null取默认)
     */
    @Deprecated
    public Dialog showDialog(String title, String msg, String leftName, String rightName
            , final View.OnClickListener leftListener, final View.OnClickListener rightListener, String... color) {

        final Dialog showDialog = new Dialog(context);
        showDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        showDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        showDialog.getWindow().setGravity(Gravity.CENTER);
        showDialog.setContentView(R.layout.comm_show_dialogs);
        showDialog.setCancelable(false);
        if (!((Activity) context).isFinishing()&&isNoEmpty(msg)) {
            showDialog.show();
        }

        LinearLayout lyrame;
        Button btSure, btCancel;
        TextView tvContent;

        lyrame =showDialog.findViewById(R.id.lyShow_Frame);
        btSure =  showDialog.findViewById(R.id.btShow_sure);
        btCancel =showDialog.findViewById(R.id.btShow_cancle);
        tvContent = showDialog.findViewById(R.id.tvShow_content);
        lyrame.getLayoutParams().width = getScreenWidth()*4/5;

        tvContent.setText(msg);

        if(msg.length()>=20){
            tvContent.setGravity(Gravity.START);
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

        if(isNoEmpty(title)){
            TextView tvTitle =showDialog.findViewById(R.id.tvTitle);
            tvTitle.setText(title);
        }

        if (isEmpty(leftName)) {
            btSure.setVisibility(View.GONE);
            showDialog.findViewById(R.id.spit).setVisibility(View.GONE);
        } else {
            btSure.setText(leftName);
            btSure.setOnClickListener(v -> {
                dismissDialog();
                if (leftListener != null)
                    leftListener.onClick(v);
            });
        }

        if (isEmpty(rightName)) {
            btCancel.setVisibility(View.GONE);
            showDialog.findViewById(R.id.spit).setVisibility(View.GONE);
        } else {
            btCancel.setText(rightName);
            btCancel.setOnClickListener(v -> {
                dismissDialog();
                if (rightListener != null)
                    rightListener.onClick(v);
            });
        }

        if (isEmpty(rightName) || isEmpty(leftName)) {
            showDialog.findViewById(R.id.spit).setVisibility(View.GONE);
        }
        this.showDialog=showDialog;
        return showDialog;
    }


    public void dismissDialog(){
        if(showDialog!=null&&showDialog.isShowing()){
            showDialog.dismiss();
            contextWeakReference.clear();
            showDialog=null;
        }
    }


    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0 || s.equals("null");
    }

    public static boolean isNoEmpty(String s) {
        boolean bool=s == null || s.length() == 0 || s.equals("null");
        return !bool;
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
}
