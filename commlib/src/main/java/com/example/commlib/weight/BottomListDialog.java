package com.example.commlib.weight;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.ALog;
import com.example.commlib.R;
import com.example.commlib.base.BaseRecyclerAdapter;
import com.example.commlib.base.MyBaseRecyclerAdapter;
import com.example.commlib.bean.BottomItem;
import com.example.commlib.listener.ResultCallback;
import com.example.commlib.utils.CommUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static com.example.commlib.utils.ScreenUtils.getScreenWidth;


/**
 * 可公用的 底部列表选择的样式弹窗
 * Author：yzh
 * Date：2020/10/29
 */

public abstract class BottomListDialog<T extends BottomItem> {
    private final Activity activity;
    private Dialog dialog;
    private List<T> mList;
    private ResultCallback<T> callback;
    private BaseRecyclerAdapter<T> mAdapter;
    private WeakReference<Activity> contextWeakReference;
    private TextView dialogTitle;
    protected RecyclerView mRecyclerView;


    public BottomListDialog(Activity context,List<T> mList) {
        contextWeakReference=new WeakReference<>(context);
        this.activity = contextWeakReference.get();
        this.mList=mList;
        builder();
    }

    private void builder() {
        //dialog = new Dialog(activity, R.style.BottomListDialog);
        dialog  = new BottomSheetDialog(activity, R.style.PayTypeDialog);
        dialog.setContentView(R.layout.bottomlist_dialog);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        if(window!=null){
            window.setGravity(Gravity.BOTTOM);
        }
        ConstraintLayout dialogLayout=dialog.findViewById(R.id.dialogLayout);
        dialogLayout.getLayoutParams().width = getScreenWidth();
        dialogTitle = dialog.findViewById(R.id.dialogTitle);
        ImageView cancels = dialog.findViewById(R.id.cancels);
        mRecyclerView = dialog.findViewById(R.id.mRecyclerView);
        cancels.setOnClickListener(v -> dialog.dismiss());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(activity,RecyclerView.VERTICAL,false));
//        mAdapter=new MyBaseRecyclerAdapter<BottomItem>(R.layout.item_card_type,mList) {
//            @Override
//            public void convert(BindingViewHolder holder,BottomItem item, int position) {
//                if(CommUtils.isNoEmpty(item.getCardTypeName())){
//                    holder.setText(R.id.tv_card_type,item.getCardTypeName());
//                }
//                holder.setVisible(R.id.iv_check,item.isChoose());
//            }
//        };
        mAdapter=initAdapter();
        mAdapter.setOnItemClickListener((baseQuickAdapter, view, i) -> setChoose(i));
        mRecyclerView.setAdapter(mAdapter);

        dialog.setOnDismissListener(dialog1 -> contextWeakReference = null);
    }


    /**
     * 填充列表的adapter
     */
    protected abstract BaseRecyclerAdapter<T> initAdapter();

    /**
     * 初始化dialog时 需要默认选中第一条时设置
     * @param list
     * @return
     */
    public BottomListDialog<T> loadNewData(List<T> list){
        this.mList=list;
        if (CommUtils.isListNotNull(mList)){
            //设置默认选择
            //setChoose(0);
        }else{
            ALog.e("数据为空！");
        }
        return this;
    }


    /**
     * 设置选中项
     * @param i
     */
    public BottomListDialog<T> setChoose(int i){
        if(CommUtils.isNotIndexOutOf(mList,i)){
            for (int j = 0; j <mList.size() ; j++) {
                mList.get(j).setChoose(j==i);
            }
            mAdapter.setNewData(mList);
            BottomItem item=mList.get(i);
            if(callback!=null&&item!=null){
                callback.onResult(mList.get(i));
                if(dialog!=null&&dialog.isShowing())
                    dialog.dismiss();
            }
        }else{
            ALog.e("数据为空或越界了！");
        }
        return this;
    }

    /**
     * 设置item监听事件
     * @param callback
     * @return
     */
    public BottomListDialog<T> setListener(ResultCallback<T> callback) {
        this.callback = callback;
        return this;
    }
    /**
     * 是否可以取消弹窗
     * @param cancel
     * @return
     */
    public BottomListDialog<T> setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    /**
     * 设置点击外部是否可关闭
     * @param cancel
     * @return
     */
    public BottomListDialog<T> setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    /**
     * 设置标题
     * @param title
     * @return
     */
    public BottomListDialog<T> setTitle(String title) {
        if(dialogTitle!=null&&title!=null){
            dialogTitle.setText(title);
        }
        return this;
    }
    public BottomListDialog<T> show() {
        dialog.show();
        return this;
    }


}
