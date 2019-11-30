package com.example.commlib.utils.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.commlib.R;

import static com.example.commlib.utils.DensityUtil.dip2px;


/**
 * @author yzh
 * @date 2019/04/29
 */

public class GlideUtil {


//    DiskCacheStrategy.NONE： 表示不缓存任何内容。
//    DiskCacheStrategy.DATA： 表示只缓存原始图片。
//    DiskCacheStrategy.RESOURCE： 表示只缓存转换过后的图片。
//    DiskCacheStrategy.ALL ： 表示既缓存原始图片，也缓存转换过后的图片。
//    DiskCacheStrategy.AUTOMATIC： 表示让Glide根据图片资源智能地选择使用哪一种缓存策略（默认选项）。
    /**
     * 将gif图转换为静态图
     */
    public static void displayasBitmap(String url, ImageView imageView) {

        Glide.with(imageView.getContext())
                .asBitmap()
                .load(url)
                .placeholder(R.drawable.shape_bg_loading)
                .error(R.drawable.shape_bg_loading)
//                .skipMemoryCache(true) //跳过内存缓存
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(imageView);
    }


    /**
     *一般加载
     * @param url
     * @param type  加载失败默认图类型
     */
    @BindingAdapter({"displayImg","displayType"})
    public static void displayImg(ImageView imageView, String url,int type) {
        Glide.with(imageView.getContext())
                .load(url)
                .centerCrop()
                .placeholder(getDefaultPic(type))
                .error(getDefaultPic(type))
                .into(imageView);
    }

    /**
     * 加载圆角图片(实现加载的图片和默认加载失败（占位图）都处理成指定圆角效果)
     * @param url
     * @param type  加载失败默认图类型
     * @param radius 圆角度数
     */
    @BindingAdapter(value = {"displayRound","displayType","radius"},requireAll = true)
    public static void displayRoundImg(ImageView imageView, String url,int type,int radius) {
        //这种加载成功才会显示圆角
//        Glide.with(imageView.getContext())
//                .load(url)
//                .centerCrop()
//                .apply(RequestOptions.bitmapTransform(new RoundedCorners(radius)))
//                .transition(DrawableTransitionOptions.withCrossFade(500))
//                .placeholder(getDefaultPic(type))
//                .error(getDefaultPic(type))
//                .into(imageView);

        int placeholderId=getDefaultPic(type);
        int errorId=getDefaultPic(type);
        Transformation<Bitmap> transform=new GlideRoundTransform(imageView.getContext(),radius);
        //Transformation<Bitmap> transform=new RoundedCorners(radius);
        Glide.with(imageView.getContext()).load(url)
                .apply(new RequestOptions()
                        .placeholder(placeholderId)
                        .error(errorId)
                        .centerCrop()
                        .transform(transform))
            //    .transition(DrawableTransitionOptions.withCrossFade(500))
                .thumbnail(loadTransform(imageView.getContext(),placeholderId,transform))
                .thumbnail(loadTransform(imageView.getContext(),errorId,transform))
                .into(imageView);

    }
    private static RequestBuilder<Drawable> loadTransform(Context context, @DrawableRes int placeholderId
            , Transformation<Bitmap> transform ) {
        return Glide.with(context)
                .load(placeholderId)
                .apply(new RequestOptions().centerCrop()
                        .transform(transform));
    }


    /**
     * 加载圆形图,暂时用到显示头像(实现加载的图片和默认加载失败（占位图）都处理成指定圆形效果)
     */
    @BindingAdapter(value = {"displayCircle","displayType"},requireAll = true)
    public static void displayCircle(ImageView imageView, String url,int type) {

//        Glide.with(imageView.getContext())
//                .load(url)
//                .centerCrop()
//                .apply(RequestOptions.circleCropTransform())
//                .transition(DrawableTransitionOptions.withCrossFade(500))
//                .placeholder(getDefaultPic(type))
//                .error(getDefaultPic(type))
//                .into(imageView);

        int placeholderId=getDefaultPic(type);
        int errorId=getDefaultPic(type);

        Transformation<Bitmap> transform=new GlideCircleTransform();
        Glide.with(imageView.getContext()).load(url)
                .apply(new RequestOptions()
                        .placeholder(placeholderId)
                        .error(errorId)
                        .centerCrop()
                        .transform(transform))
 //               .transition(DrawableTransitionOptions.withCrossFade(500))
                .thumbnail(loadTransform(imageView.getContext(),placeholderId,transform))
                .thumbnail(loadTransform(imageView.getContext(),errorId,transform))
                .into(imageView);
    }



    private static int getDefaultPic(int type) {
        switch (type) {
            case 0:
                return R.drawable.shape_bg_loading;
            case 4:
                return R.drawable.no_banner;
            default:
                break;
        }
        return R.drawable.shape_bg_loading;
    }

    /**
     * 显示高斯模糊效果（电影详情页）
     */
//    @BindingAdapter("android:displayGaussian")
//    private static void displayGaussian(ImageView imageView,String url) {
        // "23":模糊度；"4":图片缩放4倍后再进行模糊
//        Glide.with(imageView.getContext())
//                .load(url)
//                .transition(DrawableTransitionOptions.withCrossFade())
//                .error(R.drawable.stackblur_default)
//                .placeholder(R.drawable.stackblur_default)
//                .transition(DrawableTransitionOptions.withCrossFade(500))
//                .transform(new BlurTransformation(50, 8))
//                .into(imageView);
 //   }








    /**
     * 加载固定宽高图片
     */
    @BindingAdapter({"android:imageUrl", "android:imageWidth", "android:imageHeight"})
    public static void imageUrl(ImageView imageView, String url, int imageWidthDp, int imageHeightDp) {
        Glide.with(imageView.getContext())
                .load(url)
                .override(dip2px(imageWidthDp),dip2px(imageHeightDp))
                .placeholder(getDefaultPic(4))
                .centerCrop()
                .error(getDefaultPic(0))
                .into(imageView);

        //         .apply(bitmapTransform(new CircleCrop()))
//                .transform(new GlideCircleTransform())
//                .transform(new RoundedCorners(20))
//                .transform(new CenterCrop(), new RoundedCorners(20))
    }
}
