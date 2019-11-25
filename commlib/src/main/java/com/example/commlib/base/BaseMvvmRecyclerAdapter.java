package com.example.commlib.base;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.databinding.ViewDataBinding;

import com.blankj.ALog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.commlib.R;
import com.example.commlib.api.ConfigApi;

import java.util.List;

/**
 * 基于BaseQuickAdapter 实现MVVM模式的BaseMvvmRecyclerAdapter
 * @anthor yzh
 * @time 2019/11/23 11:17
 */
public abstract class BaseMvvmRecyclerAdapter<T> extends BaseQuickAdapter<T, BaseMvvmRecyclerAdapter.BindingViewHolder> {
    private ObservableList<T> mTObservableList;//让list数据变更后自动notifyItemRangeChanged刷新

    protected BaseMvvmRecyclerAdapter(@LayoutRes int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
        this.mTObservableList = data == null ? new ObservableArrayList<T>() : (ObservableList<T>) data;
        if (layoutResId != 0) {
            this.mLayoutResId = layoutResId;
        }
        mTObservableList.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<T>>() {
            @Override
            public void onChanged(ObservableList<T> ts) {
                 notifyDataSetChanged();
                ALog.e("onChanged()");
            }
            @Override
            public void onItemRangeChanged(ObservableList<T> ts, int positionStart, int itemCount) {
                notifyItemRangeChanged(positionStart, itemCount);
                ALog.e("onItemRangeChanged()");
            }

            @Override
            public void onItemRangeInserted(ObservableList<T> ts, int positionStart, int itemCount) {
                ALog.e("onItemRangeInserted() "+getEmptyViewCount() +ConfigApi.EMPTY_VIEW);

                //TODO 踩坑提示：使用 quickadapter.setEmptyView 设置空布局后， 刷新又有了数据 必须调用mAdapter.setNewData(mList); 而不是调用notifyDataSetChanged()系列; 否则会报错

//                if(getEmptyViewCount()>0){ 不能用这个在这里判断,因为mTObservableList有值后getEmptyViewCount 会变成0
                if(ConfigApi.EMPTY_VIEW){
                    setNewData(ts);
                }else{
                    notifyItemRangeInserted(positionStart, itemCount);
                }

            }

            @Override
            public void onItemRangeMoved(ObservableList<T> ts, int fromPosition, int toPosition, int itemCount) {
                for (int i = 0; i < itemCount; i++) {
                    notifyItemMoved(fromPosition + i, toPosition + i);
                }
                ALog.e("onItemRangeMoved()");
            }
            @Override
            public void onItemRangeRemoved(ObservableList<T> ts, int positionStart, int itemCount) {
                notifyItemRangeRemoved(positionStart, itemCount);
                ALog.e("onItemRangeRemoved()");
            }
        });
    }

    //    @Override
//    public BaseMvvmRecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        super.onCreateViewHolder(parent,viewType);
//        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), layoutResId, parent, false);
//        return BaseMvvmRecyclerHolder.getRecyclerHolder(binding,binding.getRoot());
//    }
    @Override
    protected View getItemView(int layoutResId, ViewGroup parent) {
        ViewDataBinding binding = DataBindingUtil.inflate(mLayoutInflater, layoutResId, parent, false);
        if (binding == null) {
            return super.getItemView(layoutResId, parent);
        }
        View view = binding.getRoot();
        view.setTag(R.id.BaseQuickAdapter_databinding_support, binding);
        return view;
    }

    @Override
    protected void convert(@NonNull BaseMvvmRecyclerAdapter.BindingViewHolder helper, T item) {
        ViewDataBinding binding = helper.getBinding();
        //todo 建议item.xml里的bean的别名都取itemBean吧，自定义命名的话，构造函数又要增加一个别名参数（variableId）
        binding.setVariable(com.example.commlib.BR.itemBean, item);
        binding.executePendingBindings();
        convert(helper, binding, item);
    }

    /**
     * 填充RecyclerView适配器的方法
     */
    public abstract void convert(BaseMvvmRecyclerAdapter.BindingViewHolder holder,ViewDataBinding binding, T item);


    public static class BindingViewHolder extends BaseViewHolder {
        public BindingViewHolder(View view) {
            super(view);
        }
        public ViewDataBinding getBinding() {
            return (ViewDataBinding) itemView.getTag(R.id.BaseQuickAdapter_databinding_support);
        }
    }

}
