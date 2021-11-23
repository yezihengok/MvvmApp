package com.example.commlib.utils.glide;

import android.graphics.Bitmap;


import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

/**
 * 图片处理后显示
 *  Created by yzh on 2020/8/7 14:31.
 */
public class HalfTransform extends BitmapTransformation {

//    @Override
//    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
//        Canvas canvas = new Canvas(toTransform);
//        BitmapShader bitmapShader = new BitmapShader(toTransform, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
//        int min = Math.min(toTransform.getWidth(), toTransform.getHeight());
//        int radius = min / 2;
//        RadialGradient radialGradient = new RadialGradient(toTransform.getWidth() / 2 , toTransform.getHeight() / 2, radius, Color.TRANSPARENT, Color.WHITE, Shader.TileMode.CLAMP);
//        ComposeShader composeShader = new ComposeShader(bitmapShader, radialGradient, PorterDuff.Mode.SRC_OVER);
//        Paint paint = new Paint();
//        paint.setShader(composeShader);
//        canvas.drawRect(0, 0, toTransform.getWidth(), toTransform.getHeight(), paint);
//        return toTransform;
//    }


    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        int width=toTransform.getWidth();
        int height=toTransform.getHeight();
        if(width==height){
            return toTransform;
            //图片是横着的矩形,以图片y坐标中轴线裁剪一个矩形
        }else if(width>height){
            return BitmapUtils.cutSquareByMiddleY(toTransform);
            //图片是竖着矩形，按上半部分一半裁剪
        }else{
            return BitmapUtils.cutHalfBitmap(toTransform);
        }
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

    }

}
