package com.example.commlib.bindingadapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.blankj.ALog;
import com.example.commlib.event.SingleLiveEvent;
import com.example.commlib.listener.ClickListener;
import com.example.commlib.utils.ButtonUtils;
import com.example.commlib.utils.CommUtils;

/**
 * @Description: ViewAdapter类作用描述
 * @Author: yzh
 * @CreateDate: 2019/11/15 14:37
 */
public class ViewAdapters {

    /**
     * requireAll 是意思是是否需要绑定全部参数, false为否
     */
    @BindingAdapter(value = {"textIsNullGone"}, requireAll = false)
    public static void setTexts(TextView view, String string) {
        //如果是空的话就设置为Gone
        if (TextUtils.isEmpty(string)) {
            if (view.getVisibility() != View.GONE) {
                view.setVisibility(View.GONE);
            }

        } else {
            if (view.getVisibility() != View.VISIBLE) {
                view.setVisibility(View.VISIBLE);
            }
            view.setText(string);
        }
    }


    @BindingAdapter(value = {"onBindingClick"}, requireAll = false)
    public static void onClicks(View view, ClickListener listener) {
        if(listener!=null){
            //view.setOnClickListener(listener::onResult);
            view.setOnClickListener(v -> {
                if(!ButtonUtils.isFastDoubleClick()){
                    listener.onResult(v);
                }
            });
        }
    }

    @BindingAdapter(value = {"onBindingClick"}, requireAll = false)
    public static void onClick(View view, SingleLiveEvent event) {
        if(event!=null){
            view.setOnClickListener(v -> {
                if(!ButtonUtils.isFastDoubleClick()){
                    event.call();
                }
            });

        }
    }

    /**
     * 设置宽高
     * @param imageView
     * @param width
     * @param height
     */
    @BindingAdapter(value = {"setWidth","setHeight"}, requireAll = false)
    public static void setHW(View imageView, int width,int height) {
        if(width>0){
            ALog.w(width+"---------"+ CommUtils.dip2px(width));
            imageView.getLayoutParams().width= CommUtils.dip2px(width);
        }
        if(height>0){
            imageView.getLayoutParams().height=CommUtils.dip2px(height);
        }
    }


}
