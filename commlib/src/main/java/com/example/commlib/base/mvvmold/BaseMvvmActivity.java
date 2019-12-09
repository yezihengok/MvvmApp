package com.example.commlib.base.mvvmold;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.example.commlib.base.RootActivity;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @Author： yzh
 * @Date: 2019/11/11 17:09
 * @Use： Mvvm模式Activity基类
 */
public abstract class BaseMvvmActivity<V extends ViewDataBinding, VM extends BaseMvvmViewModel> extends RootActivity {
    public V mBinding;
    public VM mViewModel;
    private CompositeDisposable mCompositeDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //页面间传值
        if (savedInstanceState != null) {
            initParam(savedInstanceState);
        } else if (getIntent() != null && getIntent().getExtras() != null) {
            initParam(getIntent().getExtras());
        }

        //防止空指针异常
        if (mViewModel != null) {
            if (savedInstanceState != null) {
                mViewModel.initParam(savedInstanceState);
            } else if (getIntent() != null && getIntent().getExtras() != null) {
                mViewModel.initParam(getIntent().getExtras());
            }
//            mViewModel.onCreate();
//            mViewModel.registerRxBus();
        }
        initViewDataBinding();
        initView();
    }

    //绑定mViewModel
    private void initViewDataBinding() {
        //DataBindingUtil类需要在project的build中配置 dataBinding {enabled true }, 同步后会自动关联android.databinding包
        mBinding = DataBindingUtil.setContentView(this, getLayoutId());
        mViewModel = initMVVMViewModel();
        if (mViewModel != null) {
            mBinding.setVariable(initVariableId(), mViewModel);
        }
    }

    private void initParam(Bundle bundle) {
        if (mViewModel != null) {
            mViewModel.initParam(bundle);
        }
    }

    /**
     * 初始化布局的id
     *
     * @return 布局的id
     */
    protected abstract int getLayoutId();


    /**
     * 初始化ViewModel
     *
     * @return 继承BaseViewModel的ViewModel
     */
    protected abstract VM initMVVMViewModel();


    /**
     * 布局文件里的ViewModel默命名为viewModel（命名为其它重写方法返回自定义的命名）
     */
    public int initVariableId() {
        //因为commlib 是无法引用 主app 里的BR（com.example.mvvmapp.BR.viewModel）.所以我这里创建activity_meg.xml
        // 里命名了一个占位的viewModel以便通过编译期，实际运行时会被替换主app里的BR
        return com.example.commlib.BR.viewModel;
    }



    /**
     * 初始化view
     */
    protected abstract void initView();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //防止空指针异常
        if (mViewModel != null) {
//            mViewModel.removeRxBus();
//            mViewModel.onDestroy();
            mViewModel = null;
        }
        if (mBinding != null) {
            mBinding.unbind();
        }
        unsubscribe();

    }


    /**
     * 添加activity里的订阅者 对订阅者统一管理,避免内存泄漏
     *
     * @param disposable
     */
    public void addRxDisposable(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    /**
     * 解绑
     */
    public void unsubscribe() {
        if (this.mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
            this.mCompositeDisposable.clear();
            mCompositeDisposable = null;
        }
    }



}
