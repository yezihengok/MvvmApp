package com.example.commlib.base.mvvmold;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * mvvm 模式下的BaseRecyclerViewAdapter (集成添加 HeaderView FooterView)功能
 * @Author: yzh
 * @CreateDate: 2019/10/25 15:03
 */
 //这个是自己封装实现的BaseRecyclerAdapter,当然也能实现功能,但没有BaseQuickAdapter功能强大，建议使用 基于BaseQuickAdapter封装的BaseMvvmRecyclerAdapter。

public abstract class BaseRecyclerAdapters<T> extends RecyclerView.Adapter<BaseMvvmRecyclerHolder> {
    protected Context mContext;
    private List<T> list;//数据源
    //item 布局文件 id
    protected int mLayoutId;
    protected LayoutInflater mInflater;
    // mvvm绑定的viewModel引用
    private int mVariableId;
    public RecyclerView recyclerView;


    //构造方法
    public BaseRecyclerAdapters(List<T> list, int variableId, Context context, @LayoutRes int layoutId) {
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

//    @Override
//    public BaseMvvmRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), mLayoutId, parent, false);
//        return BaseMvvmRecyclerHolder.getRecyclerHolder(binding,binding.getRoot());
//    }
//
//    @Override
//    public void onBindViewHolder(final BaseMvvmRecyclerHolder holder, int position) {
//        holder.binding.setVariable(mVariableId,list.get(position));
//        holder.binding.executePendingBindings();
//        convert(holder,list.get(position),position);
//    }

    //创建View，如果是HeaderView或者是FooterView，直接在Holder中返回
    @Override
    public BaseMvvmRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), mLayoutId, parent, false);

        if(mHeaderView != null && viewType == TYPE_HEADER) {
            return BaseMvvmRecyclerHolder.getRecyclerHolder(binding, mHeaderView);
        }
        if(mFooterView != null && viewType == TYPE_FOOTER){
            return BaseMvvmRecyclerHolder.getRecyclerHolder(binding, mFooterView);
        }
        // View view = inflater.inflate(layoutId, parent, false);
        return BaseMvvmRecyclerHolder.getRecyclerHolder(binding, binding.getRoot());
    }

    //绑定View，这里是根据返回的这个position的类型，从而进行绑定的，   HeaderView和FooterView, 就不同绑定了
    @Override
    public void onBindViewHolder(BaseMvvmRecyclerHolder holder, int position) {
        // JLog.v("onBindViewHolder--------"+getItemViewType(position)+"------position:"+position);

        if(getItemViewType(position) == TYPE_NORMAL){
            //如果设置了HeaderView position 需要-1 因为position==0已经被header占用了
            if(mHeaderView == null){
                convert(holder, list.get(position), position);
            }else{
                convert(holder, list.get(position-1), position-1);
            }
            holder.binding.setVariable(mVariableId,list.get(position));
            holder.binding.executePendingBindings();
        }else if(getItemViewType(position) == TYPE_HEADER){
            //这里加载数据的时候要注意，是从position -1开始，因为position==0已经被header占用了
           //  convert(holder, list.get(position-1), position-1);
        }else if(getItemViewType(position) == TYPE_FOOTER){
            //convert(holder, list.get(position), position);
        }

    }

    //返回View中Item的个数，这个时候，总的个数应该是ListView中Item的个数加上HeaderView和FooterView
    @Override
    public int getItemCount() {
        if(mHeaderView == null && mFooterView == null){
            return list==null?0: list.size();
        }else if(mHeaderView == null && mFooterView != null){
            return list==null?0: list.size() + 1;
        }else if (mHeaderView != null && mFooterView == null){
            return list==null?0: list.size() + 1;
        }else {
            return list==null?0: list.size() + 2;
        }
    }

    /**
     * 填充RecyclerView适配器的方法
     * @param holder      ViewHolder
     * @param item        子项
     * @param position    位置
     */
    public abstract void convert(BaseMvvmRecyclerHolder holder, T item, int position);

    public static final int TYPE_HEADER = 0;  //说明是带有Header的
    public static final int TYPE_FOOTER = 1;  //说明是带有Footer的
    public static final int TYPE_NORMAL = 2;  //说明是不带有header和footer的

    private View mHeaderView;
    private View mFooterView;
    //HeaderView和FooterView的get和set函数
    public View getHeaderView() {
        return mHeaderView;
    }

    public View getFooterView() {
        return mFooterView;
    }

    public void setHeaderView(int layoutId) {
        mHeaderView = mInflater.inflate(layoutId, recyclerView, false);
        notifyItemInserted(0);
    }
    public void setFooterView(@LayoutRes int layoutId) {
        mFooterView  = mInflater.inflate(layoutId, recyclerView, false);
        notifyItemInserted(getItemCount()-1);
    }
    public void setHeaderView(View v) {
        mHeaderView = v;
        notifyItemInserted(0);
    }
    public void setFooterView(View v) {
        mFooterView  = v;
        notifyItemInserted(getItemCount()-1);
    }

    /** 重写这个方法，很重要，是加入Header和Footer的关键，我们通过判断item的类型，从而绑定不同的view    * */
    @Override
    public int getItemViewType(int position) {
        if (mHeaderView == null && mFooterView == null){
            return TYPE_NORMAL;
        }
        if (mHeaderView != null &&position == 0){
            //第一个item应该加载Header
            return TYPE_HEADER;
        }
        if (mFooterView != null &&position == getItemCount()-1){
            //最后一个,应该加载Footer
            return TYPE_FOOTER;
        }
        return TYPE_NORMAL;
    }

}
