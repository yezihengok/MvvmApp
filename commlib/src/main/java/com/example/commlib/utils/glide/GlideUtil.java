package com.example.commlib.utils.glide;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.commlib.R;
import com.example.commlib.api.App;
import com.example.commlib.listener.ResultCallback;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.example.commlib.utils.CommUtils.dip2px;


//    DiskCacheStrategy.NONE： 表示不缓存任何内容。
//    DiskCacheStrategy.DATA： 表示只缓存原始图片。
//    DiskCacheStrategy.RESOURCE： 表示只缓存转换过后的图片。
//    DiskCacheStrategy.ALL ： 表示既缓存原始图片，也缓存转换过后的图片。
//    DiskCacheStrategy.AUTOMATIC： 表示让Glide根据图片资源智能地选择使用哪一种缓存策略（默认选项）。
//清空内存缓存，要求在主线程中执行
//Glide.get(mContext).clearMemory();
//
////清空磁盘缓存，要求在后台线程中执行
//        Glide.get(mContext).clearDiskCache();

/**
 *  Created by yzh on 2019/3/23 14:03.
 */
public class GlideUtil {

    /**
     * 加载图片   资源是绝对路径的
     * path 是       本地文件路径        /storage/emulated/0/cache/17f48c4494e047d1bce8b0ee38ec7057.jpg
     * path 也可以是  网络地址  https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1571114483606&di=a48c2bcfd9b5c40e03d522ccc717d4a3&imgtype=0&src=http%3A%2F%2Fec4.images-amazon.com%2Fimages%2FI%2F81CQOXWTbKL._SL1500_.jpg
     *
     * @param context
     * @param path
     * @param imageView
     */
    public static void load(Context context, String path, ImageView imageView) {
        Glide.with(context).load(path).into(imageView);
    }

    /**
     * 加载uri 的
     *
     * @param context
     * @param uri
     * @param imageView
     */
    public static void load(Context context, Uri uri, ImageView imageView) {
        Glide.with(context).load(uri).into(imageView);
    }

    /**
     * 加载图片（无缓存）
     *
     * @param context   context
     * @param object    任意类型的图片资源路径
     * @param imageView 显示图片的view
     */
    public static void loadObject(Activity context, Object object, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                //不开启内存缓存
                .skipMemoryCache(true)
                //不使用磁盘缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                //图片加载错误占位图
                .error(R.drawable.shape_bg_loading)
                //图片未加载完占位图
                .placeholder(R.drawable.shape_bg_loading);
        if (!context.isDestroyed()) {
            Glide.with(context).load(object).apply(options).into(imageView);
        }
    }

    /**
     * 加载图片 （有缓存）
     *
     * @param context   context
     * @param object    任意类型的图片资源路径
     * @param imageView 显示图片的view
     */
    public static void loadObjectCache(Activity context, Object object, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                //图片加载错误占位图
                .error(R.drawable.shape_bg_loading)
                //图片未加载完占位图
                .placeholder(R.drawable.shape_bg_loading);
        if (!context.isDestroyed()) {
            Glide.with(context).load(object).apply(options).into(imageView);
        }
    }

    /**
     * 加载图片 （有缓存）
     *
     * @param context   context
     * @param object    任意类型的图片资源路径
     * @param imageView 显示图片的view
     * @param drawable  占位图片和错误图片
     */
    public static void loadObject(Activity context, Object object, ImageView imageView, @Nullable Drawable drawable) {
        RequestOptions options = new RequestOptions()
                //图片加载错误占位图
                .error(drawable)
                //图片未加载完占位图
                .placeholder(drawable);
        if (!context.isDestroyed()) {
            Glide.with(context).load(object).apply(options).into(imageView);
        }
    }

    /**
     * 加载图片（有缓存）
     *
     * @param context    context
     * @param object     任意类型的图片资源路径
     * @param imageView  显示图片的view
     * @param resourceId 占位图片和错误图片
     */
    public static void loadObject(Activity context, Object object, ImageView imageView, @DrawableRes int resourceId) {
        RequestOptions options = new RequestOptions()
                //图片加载错误占位图
                .error(resourceId)
                //图片未加载完占位图
                .placeholder(resourceId);
        if (!context.isDestroyed()) {
            Glide.with(context).load(object).apply(options).into(imageView);
        }
    }

    /**
     * 加载图片 （无缓存）
     *
     * @param context   context
     * @param object    任意类型的图片资源路径
     * @param imageView 显示图片的view
     * @param drawable  占位图片和错误图片
     */
    public static void loadObjectNoCache(Activity context, Object object, ImageView imageView, @Nullable Drawable drawable) {
        RequestOptions options = new RequestOptions()
                //不开启内存缓存
                .skipMemoryCache(true)
                //不使用磁盘缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                //图片加载错误占位图
                .error(drawable)
                //图片未加载完占位图
                .placeholder(drawable);
        if (!context.isDestroyed()) {
            Glide.with(context).load(object).apply(options).into(imageView);
        }
    }

    /**
     * 加载图片（无缓存）
     *
     * @param context    context
     * @param object     任意类型的图片资源路径
     * @param imageView  显示图片的view
     * @param resourceId 占位图片和错误图片
     */
    public static void loadObjectNoCache(Activity context, Object object, ImageView imageView, @DrawableRes int resourceId) {
        RequestOptions options = new RequestOptions()
                //不开启内存缓存
                .skipMemoryCache(true)
                //不使用磁盘缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                //图片加载错误占位图
                .error(resourceId)
                //图片未加载完占位图
                .placeholder(resourceId);
        if (!context.isDestroyed()) {
            Glide.with(context).load(object).apply(options).into(imageView);
        }
    }

    /**
     * 加载  圆角  30
     *
     * @param url
     * @param imageView
     */
    public static void loadRcImage30(String url, ImageView imageView, String type, boolean isVague) {
        int placeholder=R.drawable.shape_bg_loading;

        RequestBuilder<Drawable> builder = Glide.with(imageView.getContext())
                .load(url)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(10)))
//                .placeholder(R.drawable.bg_primary_read_rv_image_loading)
                .placeholder(placeholder)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
        
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .skipMemoryCache(false);

        if (!isVague) {
            builder.into(imageView);
        } else {
            builder.apply(RequestOptions.bitmapTransform(new GaussFuzzy(App.getInstance())))
                    .into(imageView);
        }
    }
 

    /**
     * 加载  圆角 30  模糊
     *
     * @param url
     * @param imageView
     */
    public static void loadRcVagueImage30(String url, ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(url)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                .placeholder(R.drawable.shape_bg_loading)
                //只缓存转换过后的图片
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                //跳过内存缓存
                .skipMemoryCache(false)
                .apply(RequestOptions.bitmapTransform(new GaussFuzzy(App.getInstance())))
                .into(imageView);
    }

    /**
     * 清空  content  的缓存
     *
     * @param context
     */
    public static void clearMemory(Context context) {
        Glide.get(context).clearMemory();
    }

    /**
     * 清除在content  下  imageView 的 缓存
     *
     * @param context
     * @param imageView
     */
    public static void clearImageView(Context context, ImageView imageView) {
        Glide.with(context).clear(imageView);
        Glide.get(context).clearMemory();
    }


    public static void displayImg(ImageView imageView, String url, int type, ResultCallback<Integer> callback) {
        Glide.with(imageView.getContext())
                .load(url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        imageView.setVisibility(View.GONE);
                        callback.onResult(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .centerCrop()
//                .placeholder(getDefaultPic(type))
                .error(R.drawable.shape_bg_loading)
                .into(imageView);
    }


//--------------------------------------------------------------------------------------------------


    public static void display(ImageView imageView, int resId) {
        Glide.with(imageView.getContext())
                .load(resId)
                .centerCrop()
                //.placeholder(getDefaultPic(type))
                //.error(getDefaultPic(type))
                .into(imageView);
    }

    /**
     * 一般加载
     *
     * @param url
     */
    @BindingAdapter(value = {"display","overrideH","overrideW"},requireAll = false)
    public static void display(ImageView imageView, String url,int overrideH,int overrideW) {
        if(overrideH>0&&overrideW>0){
            Glide.with(imageView.getContext())
                    .load(url)
                    .override(dip2px(overrideW),dip2px(overrideH))
                    .centerCrop()
                    .into(imageView);
        }else{
            Glide.with(imageView.getContext())
                    .load(url)
                    .centerCrop()
                    //.placeholder(getDefaultPic(type))
                    //.error(getDefaultPic(type))
                    .into(imageView);
        }

    }

    /**
     * 一般加载
     *
     * @param url
     * @param type 加载失败默认图类型
     */
    @BindingAdapter({"displayImg", "displayType"})
    public static void displayImg(ImageView imageView, String url, int type) {
        Glide.with(imageView.getContext())
                .load(url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        //imageView.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .centerCrop()
                .placeholder(getDefaultPic(type))
                .error(getDefaultPic(type))
                .into(imageView);
    }

    /**
     * 将网络图片裁剪只显示上半部分
     * @param url 图片地址
     * @return ImageView
     */
    @SuppressLint("CheckResult")
    @BindingAdapter({"displayHalfImg", "displayHalfType"})
    public static void displayHalfImg(ImageView imageView, String url,int type){
//         url="https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1594118951666&di=f9f8f92ccf354ddd257a16db8071e4c4&imgtype=0&src=http%3A%2F%2Fd.paper.i4.cn%2Fmax%2F2016%2F12%2F01%2F10%2F1480558471363_838559.jpg";
//        Glide.with(imageView.getContext())
//                .asBitmap()
//                .load(url)
//                .placeholder(getDefaultPic(type))
//                .error(getDefaultPic(type))
//                .into(new SimpleTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
//                        if(resource!=null){
//                            Bitmap bitmap = BitmapUtils.cutHalfBitmap(resource); //调用裁剪图片工具类进行裁剪
//                            if(bitmap!=null)
//                                imageView.setImageBitmap(bitmap); //设置Bitmap到图片上
//                        }
//                    }
//                });
       // url="https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2838338311,2142837060&fm=26&gp=0.jpg";
        Glide.with(imageView.getContext())
                .load(url)
                .placeholder(getDefaultPic(type))
                .error(getDefaultPic(type))
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .transform(new HalfTransform())
                // .apply(RequestOptions.bitmapTransform(new HalfTransform(imageView.getContext())))
                .into(imageView);

    }

    /**
     * 加载固定宽高图片
     */
  //  @BindingAdapter({"android:imageUrl", "android:imageWidth", "android:imageHeight"})
    public static void displayImg(ImageView imageView, String url, int imageWidthDp, int imageHeightDp) {
        Glide.with(imageView.getContext())
                .load(url)
                .override(dip2px(imageWidthDp), dip2px(imageHeightDp))
                .placeholder(getDefaultPic(4))
                .centerCrop()
                .error(getDefaultPic(0))
                .into(imageView);

        //         .apply(bitmapTransform(new CircleCrop()))
//                .transform(new GlideCircleTransform())
//                .transform(new RoundedCorners(20))
//                .transform(new CenterCrop(), new RoundedCorners(20))
    }


    private static int getDefaultPic(int type) {
        switch (type) {
            case 0:
                return R.drawable.shape_bg_loading;
//            case 1:
//                return com.blue.commonlib.R.drawable.translate_default;
//            case 2:
//                return R.mipmap.literature_tiem_default;
            default:
                return R.drawable.shape_bg_loading;
        }
    }


        public static void loadOneTimeGif(Context context, Object model, final ImageView imageView, final GifListener gifListener) {
            Glide.with(context).asGif()
                    .load(model)
                    .listener(new RequestListener<GifDrawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                    if (gifListener != null) {
                        gifListener.onLoadFailed();
                    }
                    return false;
                }

                @SuppressWarnings("rawtypes")
                @Override
                public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {

                    //        但是在Glide4.0中，没法再直接获取GifDecoder对象了，原因是因为GlideDrawable不再提供这个方法了。
                    //        这里是采用反射的方法获取到GifDecoder变量：
                    try {
                        Field gifStateField = GifDrawable.class.getDeclaredField("state");
                        gifStateField.setAccessible(true);
                        Class gifStateClass = Class.forName("com.bumptech.glide.load.resource.gif.GifDrawable$GifState");
                        Field gifFrameLoaderField = gifStateClass.getDeclaredField("frameLoader");
                        gifFrameLoaderField.setAccessible(true);
                        Class gifFrameLoaderClass = Class.forName("com.bumptech.glide.load.resource.gif.GifFrameLoader");
                        Field gifDecoderField = gifFrameLoaderClass.getDeclaredField("gifDecoder");
                        gifDecoderField.setAccessible(true);
                        Class gifDecoderClass = Class.forName("com.bumptech.glide.gifdecoder.GifDecoder");
                        Object gifDecoder = gifDecoderField.get(gifFrameLoaderField.get(gifStateField.get(resource)));
                        Method getDelayMethod = gifDecoderClass.getDeclaredMethod("getDelay", int.class);
                        getDelayMethod.setAccessible(true);
                        //设置只播放一次
                        resource.setLoopCount(1);
                        //获得总帧数
                        int count = resource.getFrameCount();
                        int delay = 0;
                        for (int i = 0; i < count; i++) {
                            //计算每一帧所需要的时间进行累加
                            delay += (int) getDelayMethod.invoke(gifDecoder, i);
                        }
                        if (gifListener != null) {
                            gifListener.onLoadSuccess(resource,delay);
                        }
                    } catch (NoSuchFieldException | ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            }).into(imageView);
        }

    /**
     * 加载gif
     * @param context
     * @param model
     * @param imageView
     * @param loopCount
     * @param overrideH
     * @param overrideW
     * @param gifListener
     */
    public static void loadLoopTimeGif(Context context, Object model, final ImageView imageView,int loopCount
            ,int overrideH,int overrideW,final GifListener gifListener) {
        RequestOptions options = new RequestOptions();
//                .centerCrop()
//                .placeholder(R.drawable.ic_refresh_loading)
//                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
//                .error(R.drawable.ic_refresh_bad_net);
//        if(!TextUtils.isEmpty(url) && url.endsWith(".gif")) {
            options.format(DecodeFormat.PREFER_ARGB_8888);
//        }
        Glide.with(context).asGif()
                .load(model)
                .override(overrideH,overrideW)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .listener(new RequestListener<GifDrawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                        if (gifListener != null) {
                            gifListener.onLoadFailed();
                        }
                        return false;
                    }

                    @SuppressWarnings("rawtypes")
                    @Override
                    public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {

                        try {
                            Field gifStateField = GifDrawable.class.getDeclaredField("state");
                            gifStateField.setAccessible(true);
                            Class gifStateClass = Class.forName("com.bumptech.glide.load.resource.gif.GifDrawable$GifState");
                            Field gifFrameLoaderField = gifStateClass.getDeclaredField("frameLoader");
                            gifFrameLoaderField.setAccessible(true);
                            Class gifFrameLoaderClass = Class.forName("com.bumptech.glide.load.resource.gif.GifFrameLoader");
                            Field gifDecoderField = gifFrameLoaderClass.getDeclaredField("gifDecoder");
                            gifDecoderField.setAccessible(true);
                            Class gifDecoderClass = Class.forName("com.bumptech.glide.gifdecoder.GifDecoder");
                            Object gifDecoder = gifDecoderField.get(gifFrameLoaderField.get(gifStateField.get(resource)));
                            Method getDelayMethod = gifDecoderClass.getDeclaredMethod("getDelay", int.class);
                            getDelayMethod.setAccessible(true);
                            //设置只播放一次
                            resource.setLoopCount(loopCount);

                            //获得总帧数
//                            int count = resource.getFrameCount();
//                            ALog.a("count==="+count);
//                            int delay = 0;
//                            for (int i = 0; i < count; i++) {
//                                //计算每一帧所需要的时间进行累加
//                                delay += (int) getDelayMethod.invoke(gifDecoder, i);
//                            }
//                            if(delay==0){
//                                delay=80*count;
//                            }
                            //每帧的间隔时长
                            int delay = (int) getDelayMethod.invoke(gifDecoder, 0);
                            if (gifListener != null) {
                                gifListener.onLoadSuccess(resource,delay);
                            }
//                            imageView.postDelayed(() -> {
//                                if (gifListener != null) {
//                                    gifListener.onPlayComplete();
//                                }
//                            }, delay);
                        } catch (NoSuchFieldException | ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        return false;
                    }
                })
                .apply(options)
                .into(imageView);
    }

    /**
     * Gif播放完毕回调
     */
    public interface GifListener {
        void onLoadFailed();
        void onLoadSuccess(GifDrawable drawable,long delay);
    }

    /**
     * 根据gif 当前帧数 计算出播完所需要时间
     * @param frameIndex 当前从第几帧开始 1开始计数
     * @param totalCount 总帧数
     * @param delay 每帧 间隔时间
     * @return
     */
    public static long  getDuration(int frameIndex,int totalCount,long delay){
        long totalDelay=0;
        if(totalCount>0){
            if(frameIndex<1||frameIndex==totalCount){
                frameIndex=1;
            }
            if(frameIndex<totalCount){
                for (int i = frameIndex; i < totalCount; i++) {
                    totalDelay+=delay;
                }
            }
        }
        return totalDelay;
    }
}
