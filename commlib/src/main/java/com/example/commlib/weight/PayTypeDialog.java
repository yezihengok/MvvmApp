package com.example.commlib.weight;

import android.app.Activity;
import android.widget.ImageView;


import com.example.commlib.R;
import com.example.commlib.base.BaseRecyclerAdapter;
import com.example.commlib.bean.PayTypeBean;
import com.example.commlib.listener.ResultCallback;
import com.example.commlib.utils.CommUtils;
import com.example.commlib.weight.dialog.BottomListDialog;

import java.util.List;

/**
 * 选择支付方式dialog
 * Author：yzh
 * Date：2021/10/29
 */

public class PayTypeDialog extends BottomListDialog<PayTypeBean> {

    private final List<PayTypeBean> beanList;
    public PayTypeDialog(Activity context, List<PayTypeBean> beanList) {
        super(context,beanList);
        this.beanList = beanList;
    }

    @Override
    protected BaseRecyclerAdapter<PayTypeBean> initAdapter() {
        return new BaseRecyclerAdapter<PayTypeBean>(R.layout.item_paytype_item, beanList) {
            @Override
            public void convert(BindingViewHolder holder, PayTypeBean item, int position) {
                String code = item.getPayWayCode();
                setIcon(holder.getView(R.id.iconIv), code);
                if (CommUtils.isNoEmpty(item.getPayWayName())) {
                    holder.setText(R.id.payTypeTv, item.getPayWayName());
                }
                holder.setVisible(R.id.selectIv, item.isChoose());
            }
        };
    }

    @Override
    public PayTypeDialog setListener(ResultCallback<PayTypeBean> callback) {
        return (PayTypeDialog) super.setListener(callback);
    }

    @Override
    public PayTypeDialog loadNewData(List<PayTypeBean> list) {
        return (PayTypeDialog) super.loadNewData(list);
    }

    @Override
    public PayTypeDialog setChoose(int i) {
        return (PayTypeDialog) super.setChoose(i);
    }

    @Override
    public PayTypeDialog setTitle(String title) {
        return (PayTypeDialog) super.setTitle(title);
    }

    /**
     * 根据code设置图标
     *
     * @param view
     * @param code
     */
    public static void setIcon(ImageView view, String code) {
        if ("100".equals(code)) {
            view.setImageResource(R.drawable.aty_paypurse_paytype_alipay);
        } else if ("101".equals(code)) {
            view.setImageResource(R.drawable.aty_paypurse_paytype_weichat);
        } else if ("102".equals(code)) {
            view.setImageResource(R.drawable.aty_paypurse_paytype_union);
        }
    }
}
