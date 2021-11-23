package com.example.mvvmapp;

import android.content.Context;


import androidx.annotation.NonNull;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruArrayPool;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.example.commlib.api.App;

/**
 * 存放位置一定是在程序包名下面
 * Author：yzh
 * Date：2021/11/16
 */
@GlideModule
public class MyAppGlideModule extends AppGlideModule {
    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(App.getInstance()).build();
        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();
        int defaultArrayPoolSize = calculator.getArrayPoolSizeInBytes();
        builder.setDefaultRequestOptions(new RequestOptions().format(DecodeFormat.PREFER_RGB_565));
        builder.setMemoryCache(new LruResourceCache(defaultMemoryCacheSize/2));
        builder.setBitmapPool(new LruBitmapPool(defaultBitmapPoolSize/2));
        builder.setArrayPool(new LruArrayPool(defaultArrayPoolSize/2));
        //在全局设置中将图片质量设置为565，如果遇到显示gif文件的时候，可能会出现gif图片周边出现黑框的问题
        //需要在加载图片时候，单独针对gif结尾的url将图片质量改回8888：

/*        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_refresh_loading)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .error(R.drawable.ic_refresh_bad_net);
        if(!TextUtils.isEmpty(url) && url.endsWith(".gif")) {
            options.format(DecodeFormat.PREFER_ARGB_8888);
        }
        GlideApp.with(context)
                .load(url)
                .apply(options)
                .into(imageView);*/
    }

    //此方法isManifestParsingEnabled（）,返回false ：不再解析AndroidMenifest文件。返回true，这是Glide4为了兼容Glide3
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}
