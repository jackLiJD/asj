package com.ald.asjauthlib.authframework.core.network;


import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Yangyang on 2018/6/25.
 * desc:线程调度工具
 */

public class RxUtils {

    /**
     * @author Yangyang
     * @desc 单纯线程调度
     */
    public static <T> ObservableTransformer<T, T> io_main() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return  upstream
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
       /* return upstream -> upstream
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());*/
    }

    /**
     * @author Yangyang
     * @desc 线程耗时操作并之后回到主线程
     */
    public static void doIOtoMain(Consumer<Object> io, Consumer<Object> main) {
        Observable.just(1).doOnSubscribe(io).compose(io_main()).subscribe(main);
    }


}
