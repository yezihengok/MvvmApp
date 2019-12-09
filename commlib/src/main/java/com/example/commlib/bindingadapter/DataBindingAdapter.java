package com.example.commlib.bindingadapter;

import android.text.TextUtils;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.commlib.base.mvvm.BaseMvvmRecyclerAdapter;
import com.example.commlib.weight.recyclerview.DividerLine;

import static com.example.commlib.weight.recyclerview.DividerLine.LineDrawMode.BOTH;
import static com.example.commlib.weight.recyclerview.DividerLine.LineDrawMode.HORIZONTAL;
import static com.example.commlib.weight.recyclerview.DividerLine.LineDrawMode.VERTICAL;


public class DataBindingAdapter {
    private static final String TAG = DataBindingAdapter.class.getSimpleName();

    /**
     * 圆形图片
     *
     * @param img
     * @param path
     */
    @BindingAdapter("circleImg")
    public static void setCircleImg(ImageView img, String path) {
        if (path == null || path.isEmpty()) {
            return;
        }
        Glide
                .with(img)
                .load(TextUtils.isDigitsOnly(path) ? Integer.valueOf(path) : path)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(img);
    }

    /**
     * 加载网络或者本地资源
     *
     * @param img
     * @param url
     */
    @BindingAdapter("imgUrl")
    public static void setImgUrl(ImageView img, String url) {
        if (url == null || url.isEmpty()) {
            return;
        }
        //如果是-1不设置图片
        if (TextUtils.isDigitsOnly(url)) {
            int resId = Integer.valueOf(url);
            if (resId <= 0) {
                return;
            }
        }
        Glide
                .with(img)
                .load(TextUtils.isDigitsOnly(url) ? Integer.valueOf(url) : url)
                .into(img);
    }





    @BindingAdapter({"android:itemDecoration"})
    public static void addItemDecoration(RecyclerView mRecyclerView, int type) {
        switch (type){
            case 0:
                mRecyclerView.addItemDecoration(new DividerLine(mRecyclerView.getContext(),HORIZONTAL));
                break;
            case 1:
                mRecyclerView.addItemDecoration(new DividerLine(mRecyclerView.getContext(),VERTICAL));
                break;
            case 2:
                mRecyclerView.addItemDecoration(new DividerLine(mRecyclerView.getContext(),BOTH));
                break;
        }

        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(),DividerItemDecoration.VERTICAL));
    }


    @BindingAdapter(value ={"adapter", "bindAdapterAnimation"}, requireAll = false)
    public static void bindAdapter(RecyclerView recyclerView, BaseMvvmRecyclerAdapter adapter, int animation) {
        recyclerView.setAdapter(adapter);
        //设置动画
        if (animation != 0) {
            adapter.openLoadAnimation(animation);
        }
        //adapter.notifyDataSetChanged();
        // recyclerView.setPageFooter(R.layout.layout_loading_footer);
    }


}
