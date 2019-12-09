package com.example.commlib.base.mvvmold;

import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;


/**
 * @author yzh
 * @time 2019/10/25 15:03
 */
public class BaseMvvmRecyclerHolder extends RecyclerView.ViewHolder {
//public class BaseMvvmRecyclerHolder extends BaseViewHolder {
    public ViewDataBinding binding;
    private SparseArray<View> views;

    private BaseMvvmRecyclerHolder(ViewDataBinding binding , View itemView) {
        super(itemView);
        this.binding = binding;
        views = new SparseArray<>();
    }

    /**
     * 取得一个RecyclerHolder对象
     *
     * @param binding
     * @param itemView 子项
     * @return 返回一个RecyclerHolder对象
     */
    public static BaseMvvmRecyclerHolder getRecyclerHolder(ViewDataBinding binding, View itemView) {
        return new BaseMvvmRecyclerHolder(binding, itemView);
    }

    public SparseArray<View> getViews() {
        return this.views;
    }

    /**
     * 通过view的id获取对应的控件，如果没有则加入views中
     *
     * @param viewId 控件的id
     * @return 返回一个控件
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 设置字符串
     */
    public BaseMvvmRecyclerHolder setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    /**
     * 设置图片
     */
    public BaseMvvmRecyclerHolder setImageResource(int viewId, int drawableId) {
        ImageView iv = getView(viewId);
        iv.setImageResource(drawableId);
        return this;
    }
    /**
     * 设置图片
     */
    public BaseMvvmRecyclerHolder setBackgroundResource(int viewId, int drawableId) {
        ViewGroup iv = getView(viewId);
        iv.setBackgroundResource(drawableId);
        return this;
    }
    /**
     * 设置图片
     */
    public BaseMvvmRecyclerHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView iv = getView(viewId);
        iv.setImageBitmap(bitmap);
        return this;
    }

    /**
     * 设置图片
     */
    public BaseMvvmRecyclerHolder setImageByUrl(int viewId, String url) {
       // Picasso.with(context).load(url).into((ImageView) getView(viewId));
        //        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));
        //        ImageLoader.getInstance().displayImage(url, (ImageView) getView(viewId));
        return this;
    }
}
