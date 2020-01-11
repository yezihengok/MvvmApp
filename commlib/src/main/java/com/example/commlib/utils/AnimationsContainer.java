package com.example.commlib.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Handler;
import android.widget.ImageView;

import androidx.annotation.ArrayRes;

import java.io.InputStream;
import java.lang.ref.SoftReference;


/**
 * 帧动画加载工具类
 * Android帧动画很多图片时一次性加载会造成ui卡顿，所以就有了这份代码。通过handle队列和Bitmap复用
 * 每次加载一张并显示，可以解决帧动画卡顿问题。
 * Created by yzh on 2020/1/8 14:21.
 */
public class AnimationsContainer {

    private static AnimationsContainer mInstance;

    public static AnimationsContainer getInstance() {
       if(mInstance==null){
           synchronized (AnimationsContainer.class){
               if(mInstance==null){
                   mInstance = new AnimationsContainer();
               }
           }
       }
       return mInstance;
    }

    /**
     * 创建一个 Imageview 的播放动画类
     * @param imageView 要播放动画的ImageView
     * @param resId     资源id数组 R.array.dj_end_anim  <item>@drawable/dj_end_sh00</item>
     */
    public FramesSequenceAnimation create(ImageView imageView, @ArrayRes int resId) {
        return new FramesSequenceAnimation(imageView, CommUtils.getArrays(resId));
    }


    /**
     * 循环读取帧---循环播放帧
     */
    public class FramesSequenceAnimation {
        private int[] mFrames; // 帧数组
        private int mIndex; // 当前帧
        private boolean mShouldRun; // 开始/停止播放用
        private boolean mIsRunning; // 动画是否正在播放，防止重复播放
        private SoftReference<ImageView> mSoftReferenceImageView; // 软引用ImageView，以便及时释放掉
        private Handler mHandler;
        private long mDelayMillis = 58;
        private boolean isLoop = false;
        private boolean isGoBack = true;
        private OnAnimationListener mOnAnimationListener; //播放停止监听

        private Bitmap mBitmap = null;
        private BitmapFactory.Options mBitmapOptions;//Bitmap管理类，可有效减少Bitmap的OOM问题

        public FramesSequenceAnimation(ImageView imageView, int[] frames) {
            mHandler = new Handler();
            mFrames = frames;
            mIndex = -1;
            mSoftReferenceImageView = new SoftReference<ImageView>(imageView);
            mShouldRun = false;
            mIsRunning = false;
            setResource(imageView);

            // 当图片大小类型相同时进行复用，避免频繁GC
            if (Build.VERSION.SDK_INT >= 11) {
                Bitmap bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                int width = bmp.getWidth();
                int height = bmp.getHeight();
                Bitmap.Config config = bmp.getConfig();
                mBitmap = Bitmap.createBitmap(width, height, config);
                mBitmapOptions = new BitmapFactory.Options();
                //设置Bitmap内存复用
                mBitmapOptions.inBitmap = mBitmap;//Bitmap复用内存块，类似对象池，避免不必要的内存分配和回收
                mBitmapOptions.inMutable = true;//解码时返回可变Bitmap
                mBitmapOptions.inSampleSize = 1;//缩放比例
            }
        }

        private void setResource(ImageView imageView) {
            //  imageView.setImageResource(mFrames[0]);
            //这里不能使用image.setImageResource(会不流畅) 因为源码中也是创建了bitmap 所以这里自己创建
            Bitmap bitmap = readBitMap(imageView.getContext(),mFrames[0]);
            imageView.setImageBitmap(bitmap);
        }

        /**
         * 循环读取下一帧
         */
        private int getNext() {
            mIndex++;
            if (mIndex < mFrames.length) {
                return mFrames[mIndex];
            } else if (mIndex >= mFrames.length && isLoop) {
                return mFrames[mIndex = 0];
            } else if (isGoBack) {
                end();
                return mFrames[mIndex = 0];
            } else {
                end();
                return mIndex = -1;
            }
        }

        /**
         * How long this animation should last. The duration cannot be negative.
         * 与<code>setDelayMillis(long)</code>方法二选一
         *
         * @param duration Duration in milliseconds
         * @throws IllegalArgumentException if the duration is < 0
         * @attr ref android.R.styleable#Animation_duration
         * @see #setDelayMillis(long)
         */
        public FramesSequenceAnimation setDuration(long duration) {
            if (mFrames.length == 0) {
                throw new IllegalArgumentException("Animation frame length == 0");
            }
            if (duration < 0) {
                throw new IllegalArgumentException("Animators cannot have negative duration: " +
                        duration);
            }
            mDelayMillis = duration / mFrames.length;
            return this;
        }

        /**
         * 直接设置每帧间隔时间，与 <code>setDuration(long)</code> 方法二选一
         *
         * @param delayMillis 每帧间隔时间，数值推荐：58，单位:毫秒
         * @throws IllegalArgumentException if the duration is < 0
         * @attr ref android.R.styleable#Animation_duration
         * @see #setDuration(long)
         */
        public FramesSequenceAnimation setDelayMillis(long delayMillis) {
            if (delayMillis < 0) {
                throw new IllegalArgumentException("Animators cannot have negative duration: " +
                        delayMillis);
            }
            mDelayMillis = delayMillis;
            return this;
        }

        /**
         * 是否循环播放
         *
         * @param isLoop
         */
        public synchronized FramesSequenceAnimation setLoop(boolean isLoop) {
            this.isLoop = isLoop;
            return this;
        }

        /**
         * 是否返回第一帧
         *
         * @param isGoBack
         */
        public synchronized FramesSequenceAnimation setGoBack(boolean isGoBack) {
            this.isGoBack = isGoBack;
            return this;
        }

        /**
         * 播放动画，同步锁防止多线程读帧时，数据安全问题
         */
        public synchronized void start() {
            mShouldRun = true;
            if (mIsRunning)
                return;

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    ImageView imageView = mSoftReferenceImageView.get();
                    if (!mShouldRun || imageView == null) {
                        mIsRunning = false;
                        return;
                    }

                    mIsRunning = true;
                    //新开任务到队尾去读下一帧
                    mHandler.postDelayed(this, mDelayMillis);

                    if (imageView.isShown()) {
                        int imageRes = getNext();
                        if (imageRes == -1) {
                            return;
                        }
                        if (mBitmap != null) { // so Build.VERSION.SDK_INT >= 11
                            Bitmap bitmap = null;
                            try {
                                bitmap = BitmapFactory.decodeResource(imageView.getResources(), imageRes, mBitmapOptions);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (bitmap != null) {
                                imageView.setImageBitmap(bitmap);
                            } else {
                                imageView.setImageResource(imageRes);
                                mBitmap.recycle();
                                mBitmap = null;
                            }
                        } else {
                            imageView.setImageResource(imageRes);
                        }
                    }

                }
            };

            mHandler.post(runnable);

            if (mOnAnimationListener != null) {
                mOnAnimationListener.onAnimationStart(FramesSequenceAnimation.this);
            }
        }

        /**
         * 暂停播放，下次start会继续播放
         */
        public synchronized void stop() {
            if (!mShouldRun) {
                return;
            }
            mShouldRun = false;
            if (mOnAnimationListener != null) {
                mOnAnimationListener.onAnimationStopOrCancel(FramesSequenceAnimation.this);
            }
        }

        /**
         * 取消播放，下次start会从头播放
         */
        public synchronized void cancel() {
            if (!mShouldRun) {
                return;
            }
            mShouldRun = false;
            mIndex = 0;
            if (mOnAnimationListener != null) {
                mOnAnimationListener.onAnimationStopOrCancel(FramesSequenceAnimation.this);
            }
        }

        /**
         * 播放结束
         */
        private synchronized void end() {
            if (!mShouldRun) {
                return;
            }
            mShouldRun = false;
            if (mOnAnimationListener != null) {
                mOnAnimationListener.onAnimationEnd(FramesSequenceAnimation.this);
            }
        }

        /**
         * 直接返回第一帧
         */
        public synchronized void goBackStart() {
            if (mFrames.length == 0) {
                throw new IllegalArgumentException("Animation frame length == 0");
            }
            mIndex = 0;
            ImageView imageView = mSoftReferenceImageView.get();
          //  imageView.setImageResource(mFrames[mIndex]);
            setResource(imageView);
        }

        /**
         * 设置停止播放监听
         *
         * @param listener
         */
        public FramesSequenceAnimation setOnAnimStopListener(OnAnimationListener listener) {
            this.mOnAnimationListener = listener;
            return this;
        }
    }


    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }


    /**
     * 停止播放监听
     */
    public interface OnAnimationListener {
        void onAnimationStart(FramesSequenceAnimation animation);

        void onAnimationEnd(FramesSequenceAnimation animation);

        void onAnimationStopOrCancel(FramesSequenceAnimation animation);
    }
}

