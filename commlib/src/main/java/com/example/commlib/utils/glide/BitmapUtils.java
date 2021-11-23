package com.example.commlib.utils.glide;

import android.graphics.Bitmap;

import com.blankj.ALog;

/**
 * Created by yzh on 2020/7/7 14:31.
 */
public class BitmapUtils {
    /**
     * 对图片的高度的一半进行裁剪
     */
    public static Bitmap cutHalfBitmap(Bitmap bm){
        Bitmap bitmap = null;
        if(bm!=null){
            ALog.i("Height："+bm.getHeight()+"Width:"+bm.getWidth());
            //bitmap = Bitmap.createBitmap(bm,0,bm.getHeight()/2,bm.getWidth(),bm.getHeight()/2);//从图片一半开始截取一半的高度
            bitmap = Bitmap.createBitmap(bm,0,0,bm.getWidth(),bm.getHeight()/2);//从图片开始截取一半的高度
            ALog.v("Height："+bitmap.getHeight()+"Width:"+bitmap.getWidth());
        }
        return bitmap;
    }


    /**
     *以图片y坐标中轴线裁剪 (以图片高度度为基准裁剪正方形)
     * @param bm
     * @return
     */
    public static Bitmap cutSquareByMiddleY(Bitmap bm){
        Bitmap bitmap = null;
        if(bm!=null){
            int rectLength=bm.getHeight();//裁剪矩形的边长
            int topX=bm.getWidth()/2-rectLength/2;
            ALog.i("Height："+bm.getHeight()+"Width:"+bm.getWidth());
            bitmap = Bitmap.createBitmap(bm,topX,0,rectLength,rectLength);
            ALog.v("Height："+bitmap.getHeight()+"Width:"+bitmap.getWidth());
        }
        return bitmap;
    }


    /**
     *对高度过高的 图片的高度进行裁剪.让高度等于宽度(以图片宽度为基准裁剪正方形)
     * @param bm
     * @return
     */
    public static Bitmap cutSquareBitmap(Bitmap bm){
        Bitmap bitmap = null;
        if(bm!=null){
            ALog.i("Height："+bm.getHeight()+"Width:"+bm.getWidth());
            bitmap = Bitmap.createBitmap(bm,0,0,bm.getWidth(),bm.getWidth());
            ALog.v("Height："+bitmap.getHeight()+"Width:"+bitmap.getWidth());
        }
        return bitmap;
    }
}
