package com.example.commlib.base;

import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 基于BaseQuickAdapter
 * @anthor yzh
 * @time 2019/11/23 11:17
 */
public abstract class BaseRecyclerAdapter<T> extends BaseQuickAdapter<T, BaseRecyclerAdapter.BindingViewHolder> {
    private List<T> list;//数据源
    public RecyclerView recyclerView;

    //在RecyclerView提供数据的时候调用
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.recyclerView = null;
    }


    protected BaseRecyclerAdapter(@LayoutRes int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
        this.list = data;
        if (layoutResId != 0) {
            this.mLayoutResId = layoutResId;
        }

    }

    //    @Override
//    public BaseMvvmRecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        super.onCreateViewHolder(parent,viewType);
//        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), layoutResId, parent, false);
//        return BaseMvvmRecyclerHolder.getRecyclerHolder(binding,binding.getRoot());
//    }


    @Override
    protected void convert(@NonNull BindingViewHolder helper, T item) {

        //BaseQuickAdapter position获取
        int position = helper.getAdapterPosition();
        if (position == RecyclerView.NO_POSITION) {
            return;
        }
        position -= this.getHeaderLayoutCount();

        convert(helper,item,position);
    }

    /**
     * 填充RecyclerView适配器的方法
     */
    public abstract void convert(BindingViewHolder holder, T item, int position);


    public static class BindingViewHolder extends BaseViewHolder {
        public BindingViewHolder(View view) {
            super(view);
        }
    }

}
