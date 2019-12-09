package com.example.commlib.base.mvvm;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.example.commlib.base.ContainerActivity;
import com.example.commlib.listener.Listener;
import com.example.commlib.utils.CommUtils;
import com.example.commlib.weight.LoadDialog;
import com.trello.rxlifecycle3.components.support.RxFragment;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import static android.app.Activity.RESULT_OK;


public abstract class BaseFragment<V extends ViewDataBinding, VM extends BaseViewModel> extends RxFragment {
    public V mBinding;
    public VM mViewModel;
    private CompositeDisposable mCompositeDisposable;
    protected LoadDialog dialog;
    /**
     * Fragment是否可见状态
     */
    private boolean isFragmentVisible = false;
    /**
     * 标志位，View是否已经初始化完成。
     */
    private boolean isPrepared = false;
    /**
     * 是否第一次加载
     */
    private boolean isFirstLoad = true;


    /**
     * <pre>
     * 忽略isFirstLoad的值，强制刷新数据，但仍要Visible & Prepared
     * 一般用于PagerAdapter需要刷新各个子Fragment的场景
     * 不要new 新的 PagerAdapter 而采取reset数据的方式
     * 所以要求Fragment重新走initData方法
     * 故使用 {@link BaseFragment#setForceLoad(boolean)}来让Fragment下次执行initData
     * </pre>
     */
    private boolean forceLoad = false;


    protected BaseActivity mActivity;
    private void initBundle(Bundle bundle) {
        if (mViewModel != null) {
            mViewModel.onBundle(bundle);
        }
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity= (BaseActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(getLayoutId(inflater, container), container, false);

        initViewDataBinding(view);
        //页面间传值
        if (savedInstanceState != null) {
            initBundle(savedInstanceState);
        } else if (getArguments() != null) {
            initBundle(getArguments());
        }

        // 若 viewpager 不设置 setOffscreenPageLimit 或设置数量不够
        // 销毁的Fragment onCreateView 每次都会执行(但实体类没有从内存销毁)
        isFirstLoad = true;
        loadData();
        initView();
        return view;

    }
    /**
     * 绑定mViewModel
     */
    private void initViewDataBinding(View view) {
        if(view==null){
            return;
        }
        mBinding= DataBindingUtil.bind(view);
        mViewModel = initViewModel();
        if (mViewModel == null) {
            Class modelClass;
            Type type = getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                modelClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[1];
            } else {
                //如果没有指定泛型参数，则默认使用BaseViewModel
                modelClass = BaseViewModel.class;
            }
            mViewModel = (VM) createViewModel(this, modelClass);
        }

        if (mViewModel != null) {
            mBinding.setVariable(initVariableId(), mViewModel);
        }

        //页面事件监听的方法 用于ViewModel层转到View层的事件注册
        initViewObservable();
        registorLiveDataCallBack();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //界面初始化完成
        isPrepared = true;
        loadData();
    }

    /**
     * 如果是与ViewPager一起使用，调用的是setUserVisibleHint
     * <p>
     * 这个方法执行的时候onCreateView并不一定执行(切记)
     *
     * @param isVisibleToUser 是否显示出来了
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            onVisible();
        } else {
            onInvisible();
        }
    }

    /**
     * 如果是通过FragmentTransaction的show和hide的方法来控制显示，调用的是onHiddenChanged.
     * 若是初始就show的Fragment 为了触发该事件 需要先hide再show
     *
     * @param hidden hidden True if the fragment is now hidden, false if it is not
     *               visible.
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            onVisible();
        } else {
            onInvisible();
        }
    }

    public void onInvisible() {
        isFragmentVisible = false;
    }

    /**
     * 当界面可见的时候执行
     */
    public void onVisible() {
        isFragmentVisible = true;
        loadData();

    }


    /**
     * 这里执行懒加载的逻辑
     * 只会执行一次(如果不想只执行一次此方法): {@link BaseFragment#setForceLoad(boolean)}
     */
    public void lazyLoad() {
        Log.d("BaseMVVMFragment","BaseMVVMFragment: lazyLoad");
    }

    private void loadData() {
        //判断View是否已经初始化完成 并且 fragment是可见 并且是第一次加载
        if (isPrepared && isFragmentVisible && isFirstLoad) {
            if (forceLoad || isFirstLoad) {
                forceLoad = false;
                isFirstLoad = false;
                lazyLoad();
            }
        }
    }

    /**
     * @param forceLoad 设置为true  lazyLoad()方法会执行多次 否则只会执行一次
     */
    public void setForceLoad(boolean forceLoad) {
        this.forceLoad = forceLoad;
    }


    @Override
    public void onDestroy() {
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
        isPrepared = false;
        unsubscribe();
    }

    /**
     * 初始化布局的id
     *
     * @return 布局的id
     */
    protected abstract int getLayoutId(LayoutInflater inflater, @Nullable ViewGroup container);

    /**
     * 初始化ViewModel
     *
     * @return 继承BaseViewModel的ViewModel
     */
    public  VM initViewModel(){
        return null;
    }
    /**
     * 初始化ViewModel的id
     *
     * @return BR的id
     */
  //  protected abstract int initVariableId();

    /**
     * 布局文件里的ViewModel默命名为viewModel（命名为其它重写方法返回自定义的命名）
     */
    public int initVariableId() {
        return com.example.commlib.BR.viewModel;
    }


    public abstract void initViewObservable();
    /**
     * 初始化view
     */
    protected abstract void initView();

    /**
     * 创建ViewModel
     */
    public <T extends ViewModel> T createViewModel(Fragment fragment, Class<T> cls) {
        return ViewModelProviders.of(fragment).get(cls);
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



    /**
     * 注册(初始化)ViewModel与View的UI回调事件
     */
    protected void registorLiveDataCallBack() {
        //加载对话框显示
        mViewModel.getUILiveData().getShowDialogEvent().observe(this,s -> showDialog(s));
        //加载对话框消失
        mViewModel.getUILiveData().getDismissDialogEvent().observe(this,aVoid ->dismissDialog());
        //跳入新页面
        mViewModel.getUILiveData().getStartActivityEvent().observe(this, new Observer<Map<String, Object>>() {
            @Override
            public void onChanged(@Nullable Map<String, Object> params) {
                Class<?> clz = (Class<?>) params.get(BaseViewModel.ParameterType.CLASS);
                Bundle bundle = (Bundle) params.get(BaseViewModel.ParameterType.BUNDLE);
                startActivity(clz, bundle);
            }
        });

        //finish界面
        mViewModel.getUILiveData().getFinishEvent().observe(this,aVoid ->mActivity.finish());
        //关闭上一层
        mViewModel.getUILiveData().getOnBackPressedEvent().observe(this, aVoid ->mActivity.onBackPressed());

        //跳转一个共用显示fragment页面
        mViewModel.getUILiveData().getStartContainerActivityEvent().observe(this,params -> {
            String canonicalName = (String) params.get(BaseViewModel.ParameterType.FARGMENT_NAME);
            Bundle bundle = (Bundle) params.get(BaseViewModel.ParameterType.BUNDLE);
            startContainerActivity(canonicalName, bundle);
        });
    }




    /**
     * 请求时的进度条
     * @param cancelAble 是否能取消，true_点击外部和返回时取消loading，false_点外部不能取消loading，
     */
    public void showDialog(String msg,final boolean... cancelAble) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            return;
        }
        if(TextUtils.isEmpty(msg)){
            msg="Loading...";
        }
        try {
            if (cancelAble == null || cancelAble.length == 0) {
                dialog = new LoadDialog(mActivity, msg, false);//默认不可关闭
            } else {
                dialog = new LoadDialog(mActivity,msg, cancelAble[0]);
            }
            dialog.show();
        } catch (Exception e) {
            // Log.e(TAG, e.toString());
        }
    }

    public void dismissDialog(){
        if (dialog != null) {
            try {
                dialog.dismiss();
                dialog = null;
            } catch (Exception ignored) {}
        }
    }




    //************************************** Activity跳转(兼容4.4) **************************************//

    /**
     * Activity跳转
     *
     * @param clz 要跳转的Activity的类名
     */
    public void startActivity(Class<?> clz) {
        startActivity(new Intent(getActivity(), clz));
    }


    /**
     * Activity携带数据的跳转
     *
     * @param clz    要跳转的Activity的类名
     * @param bundle bundle
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(getActivity(), clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 跳转公共的一个ContainerActivity 用来显示Fragment
     *
     * @param canonicalName 通过 Fragment.class.getCanonicalName()获取
     */
    public void startContainerActivity(String canonicalName) {
        startContainerActivity(canonicalName, null);
    }
    /**
     * 跳转容器页面
     *
     * @param canonicalName 通过 Fragment.class.getCanonicalName()获取
     * @param bundle
     */
    public void startContainerActivity(String canonicalName, Bundle bundle) {
        Intent intent = new Intent(mActivity, ContainerActivity.class);
        intent.putExtra(ContainerActivity.FRAGMENT, canonicalName);
        if (bundle != null) {
            intent.putExtra(ContainerActivity.BUNDLE, bundle);
        }
        startActivity(intent);
    }


    /**
     * Activity跳转(带动画)
     *
     * @param clz 要跳转的Activity的类名
     */
    public void startActivityAnimation(Class<?> clz) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(new Intent(getActivity(), clz), ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
        } else {
            startActivity(clz);
        }
    }

    /**
     * Activity跳转(共享元素动画)
     *
     * @param clz 要跳转的Activity的类名
     */
    public void startActivityAnimation(Class<?> clz, View view, String shareView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(new Intent(getActivity(), clz), ActivityOptions.makeSceneTransitionAnimation(getActivity(), view, shareView).toBundle());
        } else {
            startActivity(clz);
        }
    }

    /**
     * Activity跳转(共享元素动画,带Bundle数据)
     *
     * @param clz 要跳转的Activity的类名
     */
    public void startActivityAnimation(Class<?> clz, View view, String shareView, Bundle bundle) {
        Intent intent = new Intent(getActivity(), clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity(), view, shareView).toBundle());
        } else {
            startActivity(intent);
        }
    }

    /**
     * Activity跳转(带动画,带Bundle数据)
     *
     * @param clz 要跳转的Activity的类名
     */
    public void startActivityAnimation(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(getActivity(), clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
        } else {
            startActivity(intent);
        }
    }


    /**
     * 通过Class打开编辑界面
     *
     * @param cls
     * @param requestCode
     */
    public void startActivityForResult(Class<?> cls, int requestCode) {
        startActivityForResult(new Intent(getActivity(), cls), requestCode);
    }

    /**
     * 含有Bundle通过Class打开编辑界面
     *
     * @param cls
     * @param bundle
     * @param requestCode
     */
    public void startActivityForResult(Class<?> cls, Bundle bundle,
                                       int requestCode) {
        Intent intent = new Intent();

        intent.setClass(getActivity(), cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 有动画的Finish掉界面
     */
    public void AnimationFinish() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().finishAfterTransition();
        } else {
            getActivity().finish();
        }
    }

    //************************************** Activity跳转 **************************************//
    public int getColors(int colorId) {
        return ContextCompat.getColor(mActivity, colorId);
    }



    /**
     * 8.0需要校验安装未知源权限
     */
    public void canInstallAPK(Listener listener){
        boolean hasInstallPerssion = true;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            hasInstallPerssion = mActivity.getPackageManager().canRequestPackageInstalls();
        }
        if (hasInstallPerssion) {
            //去下载安装应用
            listener.onResult();
        } else {
            //跳转至“安装未知应用”权限界面，引导用户开启权限，可以在onActivityResult中接收权限的开启结果
            this.listener=listener;
            showDialogBysure("应用安装","更新app需要您开启安装权限",() -> {
                Uri packageURI = Uri.parse("package:"+mActivity.getPackageName());
                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,packageURI);
                startActivityForResult(intent, 0x33);
            }).setCancelable(true);
        }
    }

    Listener listener;
    //接收“安装未知应用”权限的开启结果
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0x33){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O&&resultCode == RESULT_OK) {
                listener.onResult();
                listener=null;
            }
        }

    }
    /**
     * 只有确定按钮的简化弹窗
     * @param title
     * @param msg
     * @param listener
     * @return
     */
    public Dialog showDialogBysure(String title, String msg, Listener listener){
        return CommUtils.showDialog(mActivity,title,msg,"确定"
                ,null, listener,null);
    }

}
