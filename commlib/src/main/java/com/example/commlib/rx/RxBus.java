package com.example.commlib.rx;


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
     * 参考: http://hanhailong.com/2015/10/09/RxBus%E2%80%94%E9%80%9A%E8%BF%87RxJava%E6%9D%A5%E6%9B%BF%E6%8D%A2EventBus/
     * http://www.loongwind.com/archives/264.html
     * https://theseyears.gitbooks.io/android-architecture-journey/content/rxbus.html
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


    private static volatile RxBus mDefaultInstance;

    private RxBus() {
    }

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

}
