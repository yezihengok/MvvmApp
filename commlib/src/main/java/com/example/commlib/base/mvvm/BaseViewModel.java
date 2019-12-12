package com.example.commlib.base.mvvm;

import android.app.Application;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.blankj.ALog;
import com.example.commlib.base.mvvmold.BaseMvvmViewModel;
import com.example.commlib.event.SingleLiveEvent;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import static com.example.commlib.utils.CommUtils.isListNotNull;

/**
 * BaseViewModel只使用 LiveData 方式去刷新数据
 *  也有其实的实现方式（但不太建议） 参见{@link BaseMvvmViewModel}
 *
 * anthor yzh time 2019/11/27 10:07
 */
public abstract class BaseViewModel extends AndroidViewModel {
   // public int mPage = 1;//列表分页使用默认1开始
    public ObservableInt mPage= new ObservableInt(1);
    private CompositeDisposable mCompositeDisposable;
    private UILiveData mUILiveData;

//    void initBundle(Bundle bundle) {
//        onCreate(bundle);
//    }

    public abstract void onBundle(Bundle bundle);

    public BaseViewModel(@NonNull Application application) {
        super(application);
    }

    //避免Rxjava内存泄漏,
    //1、可以将Rxjava 订阅的时间添至CompositeDisposable进来，Activity销毁时切断订阅
    //2、也可以用 RxLifeCycle 将Rxjava绑定Acitivty/Fragment,销毁时自动取消订阅

    public void addDisposable(Disposable disposable) {
        if (this.mCompositeDisposable == null) {
            this.mCompositeDisposable = new CompositeDisposable();
        }
        this.mCompositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {//此方法会在Activity/Fragment销毁时调用，可以在这里做一些额外释放资源的操作。
        super.onCleared();
        if (this.mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) {
            this.mCompositeDisposable.clear();
        }
    }


    public void showDialog() {
        getUILiveData().getShowDialogEvent().postValue(null);
    }
    public void showDialog(String title) {
        getUILiveData().getShowDialogEvent().postValue(title);
    }

    public void dismissDialog() {
        getUILiveData().getDismissDialogEvent().call();
    }

    /**
     * 跳转页面
     * @param clz 所跳转的目的Activity类
     */
    public void startActivity(Class<?> clz) {
        startActivity(clz, null);
    }

    /**
     * 跳转页面
     * @param clz    所跳转的目的Activity类
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Map<String, Object> params = new HashMap<>();
        params.put(ParameterType.CLASS, clz);
        if (bundle != null) {
            params.put(ParameterType.BUNDLE, bundle);
        }
        getUILiveData().startActivityEvent.postValue(params);
    }

    /**
     * Activity跳转(共享元素动画)
     * @param clz 要跳转的Activity的类名
     */
    public void startActivityAnimation(Class<?> clz, View view, String shareView) {
        startActivityAnimation(clz,view,shareView,null);
    }

    /**
     * Activity跳转(共享元素动画,带Bundle数据)
     * @param clz 要跳转的Activity的类名
     */
    public void startActivityAnimation(Class<?> clz, View view, String shareName, Bundle bundle) {

        Map<String, Object> params = new HashMap<>();
        params.put(ParameterType.CLASS, clz);
        params.put(ParameterType.VIEW, view);
        params.put(ParameterType.VIEW_NAME, shareName);
        if (bundle != null) {
            params.put(ParameterType.BUNDLE, bundle);
        }
        getUILiveData().startActivityAnimationEvent.postValue(params);
    }


    /**
     * 跳转显示一个fragment的公共页面
     * @param canonicalName 规范名 : Fragment.class.getCanonicalName()
     */
    public void startContainerActivity(String canonicalName) {
        startContainerActivity(canonicalName, null);
    }

    /**
     *跳转显示一个fragment的公共页面
     * @param canonicalName 规范名 : Fragment.class.getCanonicalName()
     */
    protected void startContainerActivity(String canonicalName, Bundle bundle) {
        ALog.i("canonicalName---"+canonicalName);
        Map<String, Object> params = new HashMap<>();
        params.put(ParameterType.FARGMENT_NAME, canonicalName);
        if (bundle != null) {
            params.put(ParameterType.BUNDLE, bundle);
        }
        getUILiveData().startContainerActivityEvent.postValue(params);
    }


    public UILiveData getUILiveData() {
        if (mUILiveData == null) {
            mUILiveData = new UILiveData();
        }
        return mUILiveData;
    }

    /**
     * UILiveData 的作用 放一些常用的事件，减少去new 重复的SingleLiveEvent()
     */
    public final class UILiveData extends SingleLiveEvent {
        private SingleLiveEvent<String> showDialogEvent;
        private SingleLiveEvent<Void> dismissDialogEvent;
        private SingleLiveEvent<Map<String, Object>> startActivityEvent;
        private SingleLiveEvent<Map<String, Object>> startActivityAnimationEvent;
        private SingleLiveEvent<Void> finishEvent;
        private SingleLiveEvent<Void> onBackPressedEvent;
        private SingleLiveEvent<Map<String, Object>> startContainerActivityEvent;
        private SingleLiveEvent<?> commEvent;


        //普通通用的一般回调事件
        public SingleLiveEvent<?> getCommEvent() {
            return commEvent = createLiveData(commEvent);
        }


        public SingleLiveEvent<String> getShowDialogEvent() {
            return showDialogEvent = createLiveData(showDialogEvent);
        }

        public SingleLiveEvent<Void> getDismissDialogEvent() {
            return dismissDialogEvent = createLiveData(dismissDialogEvent);
        }

        //Activity跳转事件
        public SingleLiveEvent<Map<String, Object>> getStartActivityEvent() {
            return startActivityEvent = createLiveData(startActivityEvent);
        }

        //Activity跳转(共享元素动画,带Bundle数据)事件
        public SingleLiveEvent<Map<String, Object>> getStartActivityAnimationEvent() {
            return startActivityAnimationEvent = createLiveData(startActivityAnimationEvent);
        }

        public SingleLiveEvent<Void> getFinishEvent() {
            return finishEvent = createLiveData(finishEvent);
        }

        public SingleLiveEvent<Void> getOnBackPressedEvent() {
            return onBackPressedEvent = createLiveData(onBackPressedEvent);
        }
        public SingleLiveEvent<Map<String, Object>> getStartContainerActivityEvent() {
            return startContainerActivityEvent = createLiveData(startContainerActivityEvent);
        }
        private SingleLiveEvent createLiveData(SingleLiveEvent liveData) {
            if (liveData == null) {
                liveData = new SingleLiveEvent();
            }
            return liveData;
        }

        @Override
        public void observe(LifecycleOwner owner, Observer observer) {
            super.observe(owner, observer);
        }
    }

    public static final class ParameterType {
        public static String CLASS = "CLASS";
        public static String BUNDLE = "BUNDLE";
        public static String FARGMENT_NAME = "FARGMENT_NAME";
        //Activity跳转共享元素动画
        public static String VIEW = "VIEW";
        public static String VIEW_NAME = "VIEW_NAME";
    }


    /**
     * 请求成功后，设置下一次请求的分页
     * @param isRefresh 是否是下拉刷新
     */
    public void setPage(ObservableArrayList mList,boolean isRefresh){
        if(isListNotNull(mList)){
            if(isRefresh){
                //mPage=2;
                mPage.set(2);
            }else{
                mPage.set(mPage.get()+1);
               // mPage++;
            }
        }
        ALog.i("下一次请求的分页数："+mPage.get());
    }
}
