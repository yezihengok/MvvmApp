package com.example.mvvmapp.activity;

import android.graphics.Color;
import android.text.TextUtils;
import android.widget.ScrollView;

import com.example.commlib.base.mvvm.BaseActivity;
import com.example.commlib.utils.EllipsizeUtils;
import com.example.mvvmapp.R;
import com.example.mvvmapp.databinding.ActivityGreendaoBinding;
import com.example.mvvmapp.viewmodel.GreenDaoViewModel;

/**
 * Created by yzh on 2020/6/9 15:01.
 */
public class GreenDaoAvtivity extends BaseActivity<ActivityGreendaoBinding, GreenDaoViewModel> {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_greendao;
    }

    String content="contentType";
    @Override
    public void initViewObservable() {
        mViewModel.deleteEvent.observe(this, aVoid -> {
            String txt=mBinding.editText.getText().toString();
            int _id=TextUtils.isEmpty(txt)?0:Integer.parseInt(txt);

            mViewModel.delete(_id);
        });

        mViewModel.updateEvent.observe(this,aVoid ->{
            String txt=mBinding.editText.getText().toString();
            int _id=TextUtils.isEmpty(txt)?0:Integer.parseInt(txt);

            content=String.format("【我是修改后的内容%s】",_id);
            mViewModel.update(_id,content);

        });

        mViewModel.addEvent.observe(this,aVoid ->
                mBinding.mScrollView.postDelayed(() ->
                        mBinding.mScrollView.fullScroll(ScrollView.FOCUS_DOWN),300));


        mViewModel.contentChangeEvent.observe(this,aVoid ->{
            mBinding.tvContent.postDelayed(() ->{
                //文字高亮工具类
                EllipsizeUtils.ellipsizeAndHighlight(mBinding.tvContent, mBinding.tvContent.getText().toString(),content,
                        Color.RED, true, false);
            },400);
         });

    }


    @Override
    protected void initView() {

    }


}
