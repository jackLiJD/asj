package com.ald.asjauthlib.authframework.core.network;

import android.content.Context;

import com.scwang.smartrefresh.layout.api.RefreshLayout;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/*
 * Created by liangchen on 2018/5/28.
 */

public abstract class BaseObserver<T> implements Observer<T> ,HandleErrorInterface{

    private RefreshLayout refreshLayout = null;
    private Context context;//传了就表示需要加载框,必须传activity或者fragment的上下文,用于创建dialog

    public BaseObserver() {
    }

    public BaseObserver(RefreshLayout refreshLayout) {
        this.refreshLayout = refreshLayout;
    }

    public BaseObserver(Context context) {
        this.context = context;
    }

    public BaseObserver(RefreshLayout refreshLayout,Context context) {
        this.refreshLayout = refreshLayout;
        this.context = context;
    }

    @Override
    public void onSubscribe(Disposable d) {
        if (context != null) NetworkUtil.showCutscenesByRx(context, d);
    }

    @Override
    public void onNext(T t) {
        if (null != refreshLayout) {
            if (refreshLayout.isRefreshing()) refreshLayout.finishRefresh(500);
            if (refreshLayout.isLoading()) refreshLayout.finishLoadmore(500);
        }
        onSuccess(t);
    }

    public abstract void onSuccess(T t);

    @Override
    public void onError(Throwable t) {
        if (null != refreshLayout) {
            if (refreshLayout.isRefreshing()) refreshLayout.finishRefresh(500);
            if (refreshLayout.isLoading()) refreshLayout.finishLoadmore(500);
        }
        NetworkUtil.dismissCutscenes();
        commonHandleError(t);

    }



    @Override
    public void onComplete() {
        NetworkUtil.dismissForceCutscenes();
    }

}
