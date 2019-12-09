package com.example.commlib.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.example.commlib.base.mvvmold.BaseMvvmRecyclerHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * mvvm 模式下的BaseRecyclerViewAdapter
 * @Author: yzh
 * @CreateDate: 2019/10/25 15:03
 */
@Deprecated //这个是自己封装实现的BaseRecyclerAdapter,当然也能实现功能,但没有BaseQuickAdapter功能强大，建议使用 基于BaseQuickAdapter封装的BaseMvvmRecyclerAdapter。
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseMvvmRecyclerHolder> {
    protected Context mContext;
    private List<T> list;//数据源
    //item 布局文件 id
    protected int mLayoutId;
    protected LayoutInflater mInflater;
    // mvvm绑定的viewModel引用
    private int mVariableId;
    private RecyclerView recyclerView;



    //构造方法
    public BaseRecyclerAdapter(List<T> list, int variableId, Context context, @LayoutRes int layoutId) {
        mContext = context;
        this.list = list;
        mLayoutId = layoutId;
        mInflater = LayoutInflater.from(mContext);
        mVariableId = variableId;
        //        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
        //            @Override
        //            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        //                super.onScrollStateChanged(recyclerView, newState);
        //                isScrolling = !(newState == RecyclerView.SCROLL_STATE_IDLE);
        //                if (!isScrolling) {
        //                    notifyDataSetChanged();
        //                }
        //            }
        //        });
    }


    public void setLists(List<T> lists)
    {
        if(lists==null){
            lists=new ArrayList<>();
        }
        this.list = lists;
        notifyDataSetChanged();
    }
    /**
     * 插入一项
     *
     * @param item
     * @param position
     */
    public void insert(T item, int position) {
        list.add(position, item);
        notifyItemInserted(position);
        //刷新下标，不然下标就重复
        notifyItemRangeChanged(position, list.size());
    }

    /**
     * 删除一项
     *
     * @param position 删除位置
     */
    public void delete(int position) {
        list.remove(position);
        notifyItemRemoved(position);
        //刷新下标，不然下标就重复
        notifyItemRangeChanged(position, list.size());
    }


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

    @Override
    public BaseMvvmRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), mLayoutId, parent, false);
        return BaseMvvmRecyclerHolder.getRecyclerHolder(binding,binding.getRoot());
    }

    @Override
    public void onBindViewHolder(final BaseMvvmRecyclerHolder holder, int position) {
        holder.binding.setVariable(mVariableId,list.get(position));
        holder.binding.executePendingBindings();
        convert(holder,list.get(position),position);
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }

    /**
     * 填充RecyclerView适配器的方法
     * @param holder      ViewHolder
     * @param item        子项
     * @param position    位置
     */
    public abstract void convert(BaseMvvmRecyclerHolder holder, T item, int position);
}
