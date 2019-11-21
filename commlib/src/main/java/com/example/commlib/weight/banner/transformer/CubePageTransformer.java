package com.example.commlib.weight.banner.transformer;


import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.ViewPager;

/**
 * 创建时间:15/6/19 17:39
 * 描述:  //立方体
 */
public class CubePageTransformer implements ViewPager.PageTransformer {
    private float mMaxRotation = 90.0f;

    public CubePageTransformer() {
    }

    public CubePageTransformer(float maxRotation) {
        setMaxRotation(maxRotation);
    }


    @Override
    public void transformPage(@NonNull View view, float position) {
        if (position < -1.0f) {
            // [-Infinity,-1)
            // This page is way off-screen to the left.
            handleInvisiblePage(view, position);
        } else if (position <= 0.0f) {
            // [-1,0]
            // Use the default slide transition when moving to the left page
            handleLeftPage(view, position);
        } else if (position <= 1.0f) {
            // (0,1]
            handleRightPage(view, position);
        } else {
            // (1,+Infinity]
            // This page is way off-screen to the right.
            handleInvisiblePage(view, position);
        }
    }


    public void handleInvisiblePage(View view, float position) {
        view.setPivotX(view.getMeasuredWidth());
        view.setPivotY( view.getMeasuredHeight() * 0.5f);
        view.setRotationY(0);
    }


    public void handleLeftPage(View view, float position) {
        view.setPivotX(view.getMeasuredWidth());
        view.setPivotY(view.getMeasuredHeight() * 0.5f);
        view.setRotationY(mMaxRotation * position);
    }


    public void handleRightPage(View view, float position) {
        view.setPivotX(0);
        view.setPivotY(view.getMeasuredHeight() * 0.5f);
        view.setRotationY(mMaxRotation * position);
    }

    public void setMaxRotation(float maxRotation) {
        if (maxRotation >= 0.0f && maxRotation <= 90.0f) {
            mMaxRotation = maxRotation;
        }
    }

}
