package com.example.mvvmapp;

import com.example.commlib.base.BaseMvvmActivity;
import com.example.commlib.base.BaseMvvmViewModel;
import com.example.commlib.rx.RxTimerUtil;
import com.example.commlib.weight.QMUIProgressBar;
import com.example.mvvmapp.databinding.ActivityTestWeightBinding;

import java.util.concurrent.TimeUnit;

/**
 * @anthor yzh
 * @time 2019/11/30 15:07
 */
public class TestWeightActivity extends BaseMvvmActivity<ActivityTestWeightBinding, BaseMvvmViewModel> {

    int num=0;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_test_weight;
    }

    @Override
    protected BaseMvvmViewModel initMVVMViewModel() {
        return null;
    }

    @Override
    protected void initView() {
        mBinding.mCircleProgressBar.setQMUIProgressBarTextGenerator(new QMUIProgressBar.QMUIProgressBarTextGenerator() {
            @Override
            public String generateText(QMUIProgressBar progressBar, int value, int maxValue) {
                if(value==maxValue){
                    mBinding.mCircleProgressBar.setProgress(0);
                    RxTimerUtil.cancel("TestWeightActivity");
                }
                return 100 * value / maxValue + "%";
            }
        });

        RxTimerUtil.interval(100, TimeUnit.MILLISECONDS,"TestWeightActivity",(number, timerName) -> {
            mBinding.mCircleProgressBar.setProgress(num++);

        });

        mBinding.mCountDownView.startCountDown();
        mBinding.mCountDownView.setAddCountDownListener(() -> mBinding.mCountDownView.startCountDown());
    }
}
