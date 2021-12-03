package com.example.mvvmapp.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.blankj.ALog;
import com.example.commlib.base.BaseRecyclerAdapter;
import com.example.commlib.base.mvvm.BaseActivity;
import com.example.commlib.base.mvvm.BaseViewModel;
import com.example.commlib.bean.BottomItem;
import com.example.commlib.bean.PayTypeBean;
import com.example.commlib.rx.RxTimerUtil;
import com.example.commlib.utils.CommUtils;
import com.example.commlib.utils.animations.Other;
import com.example.commlib.utils.animations.RxAnimation;
import com.example.commlib.weight.dialog.BottomListDialog;
import com.example.commlib.weight.PayTypeDialog;
import com.example.commlib.weight.SuperTextView;
import com.example.mvvmapp.R;
import com.example.mvvmapp.databinding.ActivityTestWeightBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @anthor yzh
 * @time 2019/11/30 15:07
 */
public class TestWeightActivity extends BaseActivity<ActivityTestWeightBinding, BaseViewModel> {

    int num = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test_weight;
    }

//    @Override
//    protected BaseMvvmViewModel initMVVMViewModel() {
//        return null;
//    }

    @Override
    public void initViewObservable() {

    }

    @Override
    protected void initView() {
        mBinding.mCircleProgressBar.setIProgressBarTextGenerator((progressBar, value, maxValue) -> {
            if (value == maxValue) {
                mBinding.mCircleProgressBar.setProgress(0);
                RxTimerUtil.cancel("TestWeightActivity");
            }
            return 100 * value / maxValue + "%";
        });

        //倒计时工具类
        RxTimerUtil.interval(100, TimeUnit.MILLISECONDS, "TestWeightActivity", (number, timerName) -> {
            mBinding.mCircleProgressBar.setProgress(num++);

        });

        mBinding.mCountDownView.startCountDown();
        mBinding.mCountDownView.setAddCountDownListener(() -> mBinding.mCountDownView.startCountDown());
        mBinding.mLineWaveVoiceView.startRecord();

        //示例动画工具类使用
        RxAnimation.get().setAnimation(Other.pulseAnimator(mBinding.mCountDownView, 2))
                .setDuration(1000)
                .start();


        String content = "Android仿酷狗动感歌词（支持翻译和音译歌词）显示效果\nhttps://www.jianshu.com/p/9e7111db7b41";
        mBinding.mSuperTextView.setDynamicText(content);
        mBinding.mSuperTextView.setDynamicStyle(SuperTextView.DynamicStyle.CHANGE_COLOR);
        mBinding.mSuperTextView.setDurationByToalTime(8 * 1000);
        mBinding.mSuperTextView.start();
        mBinding.mSuperTextView.setOnDynamicListener(new SuperTextView.OnDynamicListener() {
            @Override
            public void onChange(int position, int total) {
                int lineCount = mBinding.mSuperTextView.getLineCount();
                int lineHeight = mBinding.mSuperTextView.getLineHeight();
                ALog.i(String.format("总行数：%s,行高度：%s", lineCount, lineHeight));
            }

            @Override
            public void onCompile() {
                mBinding.mSuperTextView.setDynamicStyle(SuperTextView.DynamicStyle.TYPEWRITING);
                mBinding.mSuperTextView.start();
            }
        });

        List<PayTypeBean> itemList = new ArrayList<>();
        itemList.add(new PayTypeBean("支付宝支付", "100"));
        itemList.add(new PayTypeBean("微信支付", "101"));
        itemList.add(new PayTypeBean("银联支付", "102"));
        initPayType(itemList);

        List<BottomItem> itemList2 = new ArrayList<>();
        itemList2.add(new BottomItem("支付宝支付", "100"));
        itemList2.add(new BottomItem("微信支付", "101"));
        itemList2.add(new BottomItem("银联支付", "102"));
        initPayType2(itemList2);
    }

    private void initPayType(List<PayTypeBean> itemList) {
        TextView payTypeTv = findViewById(R.id.payTypeTv);
        ImageView selectIv = findViewById(R.id.selectIv);
        ImageView iconIv = findViewById(R.id.iconIv);
        ConstraintLayout payTypeLayout = findViewById(R.id.payTypeLayout);
        selectIv.setImageResource(R.drawable.right_arrow);
        selectIv.setVisibility(View.VISIBLE);
        PayTypeDialog payTypeDialog = new PayTypeDialog(this, itemList)
                .setListener(result -> {
                    String payTypeCode = result.getPayWayCode();
                    setTextValues(payTypeTv, result.getPayWayName());
                    PayTypeDialog.setIcon(iconIv, payTypeCode);
                }).setChoose(0);

        payTypeLayout.setOnClickListener(v -> payTypeDialog.show());
    }

    /**
     * 也可以直接使用 BottomListDialog弹窗
     * @param itemList
     */
    private void initPayType2(List<BottomItem> itemList) {

        ConstraintLayout payTypeLayout = findViewById(R.id.payTypeLayout2);
        TextView payTypeTv = payTypeLayout.findViewById(R.id.payTypeTv);
        ImageView selectIv = payTypeLayout.findViewById(R.id.selectIv);
        ImageView iconIv = payTypeLayout.findViewById(R.id.iconIv);

        selectIv.setImageResource(R.drawable.right_arrow);
        selectIv.setVisibility(View.VISIBLE);
        BottomListDialog<BottomItem> payTypeDialog = new BottomListDialog<BottomItem>(this,itemList) {
            @Override
            protected BaseRecyclerAdapter<BottomItem> initAdapter() {
                return new BaseRecyclerAdapter<BottomItem>(com.example.commlib.R.layout.item_paytype_item, itemList) {
                    @Override
                    public void convert(BindingViewHolder holder, BottomItem item, int position) {
                        String code = item.getCode();
                        PayTypeDialog.setIcon(holder.getView(com.example.commlib.R.id.iconIv), code);
                        if (CommUtils.isNoEmpty(item.getName())) {
                            holder.setText(com.example.commlib.R.id.payTypeTv, item.getName());
                        }
                        holder.setVisible(com.example.commlib.R.id.selectIv, item.isChoose());
                    }
                };
            }
        }.setTitle("我是弹窗2号")
        .setListener(result -> {
            String payTypeCode = result.getCode();
            setTextValues(payTypeTv, result.getName());
            PayTypeDialog.setIcon(iconIv, payTypeCode);
        }).setChoose(2);

        payTypeLayout.setOnClickListener(v -> payTypeDialog.show());
    }
}
