package com.example.commlib.weight;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import com.example.commlib.R;


/**
 *  逐渐变色的TextView
 *  Created by yzh. 2019-11-28
 *  基于 @author Jenly 修改 <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */

@SuppressLint("AppCompatCustomView")
public class SuperTextView extends TextView{

    private int mDuration = 200;

    private boolean mIsStart;

    public boolean isStart() {
        return mIsStart;
    }

    private CharSequence mText;

    private int mPosition;

    private int mSelectedColor = 0xffff00ff;

    private OnDynamicListener mOnDynamicListener;

    private DynamicStyle mDynamicStyle = DynamicStyle.NORMAL;

    public enum DynamicStyle{
        NORMAL(0),TYPEWRITING(1),CHANGE_COLOR(2);

        private int mValue;
        DynamicStyle(int value){
            this.mValue = value;
        }

        private static DynamicStyle getFromInt(int value){

            for(DynamicStyle style : DynamicStyle.values()){
                if(style.mValue == value){
                    return style;
                }
            }

            return DynamicStyle.NORMAL;
        }
    }

    public SuperTextView(Context context) {
        this(context,null);
    }

    public SuperTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SuperTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SuperTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context,attrs);
    }

    private void init(Context context,AttributeSet attrs){
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SuperTextView);
        mText = a.getText(R.styleable.SuperTextView_dynamicText);
        mDuration = a.getInt(R.styleable.SuperTextView_duration,mDuration);
        mSelectedColor = a.getColor(R.styleable.SuperTextView_selectedColor,mSelectedColor);
        mDynamicStyle = DynamicStyle.getFromInt(a.getInt(R.styleable.SuperTextView_dynamicStyle,0));
        a.recycle();
    }


    public void start(){
        if(mIsStart){
            return;
        }
        if(TextUtils.isEmpty(mText)){//如果动态文本为空、则取getText()的文本内容
            mText = getText();
        }
        mPosition = 0;
        if(!TextUtils.isEmpty(mText)){
            mIsStart = true;
            post(mRunnable);
        }else{
            mIsStart = false;
            if(mOnDynamicListener!=null){
                mOnDynamicListener.onCompile();
            }
        }
    }

    /**
     * 停止
     */
    public void stop(){
        mIsStart = false;
        mPosition = 0;
        removeCallbacks(mRunnable);
    }
    /**
     * 暂停
     */
    public void push(){
        mIsStart = false;
        removeCallbacks(mRunnable);
    }

    public void reStart(){
        mIsStart = true;
        postDelayed(mRunnable,mDuration);
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if(DynamicStyle.TYPEWRITING == mDynamicStyle){
                setText(mText.subSequence(0, mPosition));
            }else if(DynamicStyle.CHANGE_COLOR == mDynamicStyle){
                setChangeColorText(mPosition);
            }else{
                setText(mText);
                mIsStart = false;
                if(mOnDynamicListener!=null){
                    mOnDynamicListener.onCompile();
                }
                return;
            }

            if (mPosition < mText.length()) {
                if(mOnDynamicListener!=null){
                    mOnDynamicListener.onChange(mPosition,mText.length());
                }
                mPosition++;
                postDelayed(mRunnable,mDuration);
            }else{
                if(mOnDynamicListener!=null){
                    mOnDynamicListener.onChange(mPosition,mText.length());
                }
                mIsStart = false;
                if(mOnDynamicListener!=null){
                    mOnDynamicListener.onCompile();
                }
            }
        }
    };

    private void setChangeColorText(int position){
        SpannableString spannableString = new SpannableString(mText);
        spannableString.setSpan(new ForegroundColorSpan(mSelectedColor), 0, position, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        setText(spannableString);
    }

    public void setDynamicText(@StringRes int resId){
        this.mText = getResources().getText(resId);
    }

    public void setDynamicText(CharSequence text){
        this.mText = text;
    }

    public CharSequence getDynamicText(){
        return mText;
    }

    public void setDuration(int duration){
        this.mDuration = duration;
    }

    /***
     * 根据指定播放完的总时间，计算出mDuration
     * @param time 毫秒
     */
    public void setDurationByToalTime(long time){
       if(mText==null){
           return;
       }

        Log.i("SuperTextView","总时长==="+time);
        this.mDuration = (int) (time/mText.length());
        Log.d("SuperTextView","mDuration==="+mDuration);

    }

    public int getDuration(){
        return mDuration;
    }

    public DynamicStyle getDynamicStyle(){
        return mDynamicStyle;
    }

    public void setDynamicStyle(DynamicStyle dynamicStyle){
        this.mDynamicStyle = dynamicStyle;
    }

    public void setOnDynamicListener(OnDynamicListener onDynamicListener){
        this.mOnDynamicListener = onDynamicListener;
    }

    public void setSelectedColor(int selectedColor){
        this.mSelectedColor = selectedColor;
    }

    public void setSelectedColorResource(@ColorRes int resId){
        this.mSelectedColor = ContextCompat.getColor(getContext(),resId);
    }


    public interface OnDynamicListener{
        void onChange(int position, int total);
        void onCompile();
    }

}
