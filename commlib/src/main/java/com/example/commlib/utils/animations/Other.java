package com.example.commlib.utils.animations;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;

/**
 * anthor yzh
 * time 2019/12/5 14:37
 */
public class Other {




    public static AnimatorSet pulseAnimator(View view,int...repeatCount) {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator object1 =ObjectAnimator.ofFloat(view, "scaleY", 1, 1.1f, 1);
        ObjectAnimator object2 = ObjectAnimator.ofFloat(view, "scaleX", 1, 1.1f, 1);

        if(repeatCount!=null&&repeatCount.length>0){
            object1.setRepeatCount(repeatCount[0]);
            object2.setRepeatCount(repeatCount[0]);
        }
        animatorSet.playTogether(object1, object2);
        return animatorSet;
    }


    public static AnimatorSet Rotation(View view) {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view,"rotation",0f,360f);
        //objectAnimator.setDuration(2000);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        animatorSet.play(objectAnimator);
        return animatorSet;
    }

}
