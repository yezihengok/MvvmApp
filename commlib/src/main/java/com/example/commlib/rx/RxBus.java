package com.example.commlib.rx;


import com.example.commlib.utils.CommUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
/**
 * Created by yzh on 19/11/11.
 */
public class RxBus {
    /**
     * 参考:
     * http://www.loongwind.com/archives/264.html
     * https://blog.csdn.net/u013651026/article/details/79088442
     */

    //RxBus=用RxJava模拟实现的EventBus的功能,不需要再额外引入EventBus库增加app代码量
//        RxBus.getDefault().post(1,"呵呵呵哒");
//
//        RxBus.getDefault().toObservable(String.class).subscribe(new Consumer<String>() {
//            @Override
//            public void accept(String s) throws Exception {
//
//            }
//        });
//    }


//        RxBus.getDefault().postSticky(new EventSticky("aa"));
//        RxBus.getDefault().toObservableSticky(EventSticky.class).subscribe(new Consumer<EventSticky>() {
//        @Override
//        public void accept(EventSticky eventSticky) throws Exception {
//
//        }
//    });

//    public class EventSticky {
//        public String event;
//
//        public EventSticky(String event) {
//            this.event = event;
//        }
//
//        @Override
//        public String toString() {
//            return "EventSticky{" +
//                    "event='" + event + '\'' +
//                    '}';
//        }
//    }


    private static volatile RxBus mDefaultInstance;

    public static RxBus getDefault() {
        if (mDefaultInstance == null) {
            synchronized (RxBus.class) {
                if (mDefaultInstance == null) {
                    mDefaultInstance = new RxBus();
                }
            }
        }
        return mDefaultInstance;
    }

    public RxBus() {
        mStickyEventMap = new ConcurrentHashMap<>();
    }


    private final Subject<Object> _bus = PublishSubject.create().toSerialized();

    public void send(Object o) {
        _bus.onNext(o);
    }

    public Observable<Object> toObservable() {
        return _bus;
    }

    /**
     * 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
     *
     * @param eventType 事件类型
     * @param <T>
     * @return
     */
    public <T> Observable<T> toObservable(Class<T> eventType) {
        return _bus.ofType(eventType);
    }

    /**
     * 提供了一个新的事件,根据code进行分发
     *
     * @param code 事件code
     * @param o
     */
    public void post(int code, Object o) {
        _bus.onNext(new RxBusMessage(code, o));

    }

    /**
     * 根据传递的code和 eventType 类型返回特定类型(eventType)的 被观察者
     * 对于注册了code为0，class为voidMessage的观察者，那么就接收不到code为0之外的voidMessage。
     *
     * @param code      事件code
     * @param eventType 事件类型
     * @param <T>
     * @return
     */
    public <T> Observable<T> toObservable(final int code, final Class<T> eventType) {
        CommUtils.isMainThread();
        return _bus.ofType(RxBusMessage.class)
                .filter(new Predicate<RxBusMessage>() {
                    @Override
                    public boolean test(RxBusMessage rxBusMessage) throws Exception {
                        //过滤code和eventType都相同的事件
                        return rxBusMessage.getCode() == code && eventType.isInstance(rxBusMessage.getObject());
                    }
                }).map(new Function<RxBusMessage, Object>() {
                    @Override
                    public Object apply(RxBusMessage rxBusMessage) throws Exception {
                        return rxBusMessage.getObject();
                    }
                }).cast(eventType);
    }

    /**
     * 判断是否有订阅者
     */
    public boolean hasObservers() {
        return _bus.hasObservers();
    }




    /**
     * Stciky 相关
     */
    private final Map<Class<?>, Object> mStickyEventMap;
    /**
     * 发送一个新Sticky事件
     */
    public void postSticky(Object event) {
        synchronized (mStickyEventMap) {
            mStickyEventMap.put(event.getClass(), event);
        }
        post(event);
    }

    public void post(Object event) {
        _bus.onNext(event);
    }
    /**
     * 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
     */
    public <T> Observable<T> toObservableSticky(final Class<T> eventType) {
        synchronized (mStickyEventMap) {
            Observable<T> observable = _bus.ofType(eventType);
            final Object event = mStickyEventMap.get(eventType);

            if (event != null) {
                return observable.mergeWith(Observable.create(emitter -> {
                    emitter.onNext(eventType.cast(event));
                }));
            } else {
                return observable;
            }
        }
    }

    /**
     * 根据eventType获取Sticky事件
     */
    public <T> T getStickyEvent(Class<T> eventType) {
        synchronized (mStickyEventMap) {
            return eventType.cast(mStickyEventMap.get(eventType));
        }
    }

    /**
     * 移除指定eventType的Sticky事件
     */
    public <T> T removeStickyEvent(Class<T> eventType) {
        synchronized (mStickyEventMap) {
            return eventType.cast(mStickyEventMap.remove(eventType));
        }
    }



//    在使用Sticky特性时，在不需要某Sticky事件时， 通过removeStickyEvent(Class<T> eventType)移除它，最保险的做法是：在主Activity的onDestroy里removeAllStickyEvents()。
//    因为我们的RxBus是个单例静态对象，再正常退出app时，该对象依然会存在于JVM，除非进程被杀死，这样的话导致StickyMap里的数据依然存在，为了避免该问题，需要在app退出时，清理StickyMap。
    /**
     * 移除所有的Sticky事件
     */
    public void removeAllStickyEvents() {
        synchronized (mStickyEventMap) {
            mStickyEventMap.clear();
        }
    }

}
