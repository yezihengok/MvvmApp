package com.example.commlib.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 拦截子View不响应事件的FrameLayout
 * Created by yzh on 2020/6/6 9:57.
 */
public class InterceptLayout extends FrameLayout {

    /**
     * 是否拦截子View
     */
    private boolean intercept;

    public boolean isIntercept() {
        return intercept;
    }

    public void setIntercept(boolean intercept) {
        this.intercept = intercept;
    }

    public InterceptLayout(@NonNull Context context) {
        super(context);
    }

    public InterceptLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public InterceptLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (intercept) {
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

}
