

package com.example.commlib.utils.animations;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.blankj.ALog;

/**
 * 属性动画工具类
 * @author  yzh 2019/10/30 17:37
 */
public class RxAnimation {
    private static RxAnimation mAnimation;
    public static RxAnimation get(){
        if(mAnimation==null){
            synchronized (RxAnimation.class){
                if(mAnimation==null){
                    mAnimation=new RxAnimation();
                }
            }
        }
        return mAnimation;
    }

    public AnimatorSet getAnimator() {
        return animatorSet;
    }

    private AnimatorSet animatorSet;
    // setAnimation(Bounce.In(textView))
    public RxAnimation setAnimation (AnimatorSet animationSet) {
        this.animatorSet = animationSet;
      //  animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.setInterpolator(new LinearInterpolator());//匀速
        return mAnimation;
    }

    public void start() {
        if(animatorSet!=null) {
            if (animatorSet.isPaused()) {
                animatorSet.resume();
                ALog.w("resume----------");
            } else {
                animatorSet.start();
            }
        }
    }

    public void pause() {
        if(animatorSet!=null){
            animatorSet.pause();
        }
    }

    public void cancel(View v) {
        if(animatorSet!=null){
            animatorSet.cancel();
            reset(v);
        }
    }

    /**
     * 恢复view 默认状态
     *
     * @param target
     */
    public static void reset(View target) {
        target.setAlpha( 1);
        target.setScaleX( 1);
        target.setScaleY( 1);
        target.setTranslationX( 0);
        target.setTranslationY( 0);
        target.setRotation( 0);
        target.setRotationY( 0);
        target.setRotationX( 0);
    }


    /**
     * 需要 执行动画 并控制隐藏 显示view时设置
     * @param v
     * @param type  0执行前显示view   1执行前显示view且执行完后隐藏view
     */
    public RxAnimation visableOrGone(View v,int type){

        if(animatorSet!=null){
            animatorSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    v.setVisibility(View.VISIBLE);
                }
                @Override
                public void onAnimationEnd(Animator animation) {
                    if(type==1){
                        v.setVisibility(View.GONE);
                    }

                }
                @Override
                public void onAnimationCancel(Animator animation) { }
                @Override
                public void onAnimationRepeat(Animator animation) { }
            });
        }else{
            throw new NullPointerException("请先设置animatorSet！");
        }
        return mAnimation;
    }



    public RxAnimation setDuration(long duration){
        if(animatorSet!=null){
            animatorSet.setDuration(duration);
        }

        return mAnimation;
    }


}
