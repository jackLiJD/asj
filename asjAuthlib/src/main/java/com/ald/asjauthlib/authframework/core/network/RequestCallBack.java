package com.ald.asjauthlib.authframework.core.network;

import android.content.Context;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/1/6
 * 描述：网络请求回调封装类
 * 修订历史：
 */
public abstract class RequestCallBack<T> implements Callback<T>,HandleErrorInterface {
//    private Context context;
    private RefreshLayout refreshLayout;
//    private PtrFrameLayout ptrFrame;
//    private boolean isShow = false;

    public RequestCallBack() {

    }

    public RequestCallBack(Context context) {
//        this.context = context;

    }

//    public RequestCallBack(boolean isShow) {
//        this.isShow = isShow;
//    }

//    public RequestCallBack(PtrFrameLayout ptrFrame) {
//        this.ptrFrame = ptrFrame;
//    }

    public RequestCallBack(RefreshLayout refreshLayout) {
        this.refreshLayout = refreshLayout;
    }

//    public RequestCallBack(PtrFrameLayout ptrFrame, boolean isShow) {
//        this.ptrFrame = ptrFrame;
//        this.isShow = isShow;
//    }

    public abstract void onSuccess(Call<T> call, Response<T> response);

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
//        if (ptrFrame != null && ptrFrame.isRefreshing()) {
//            ptrFrame.refreshComplete();
//        }
        if (null != refreshLayout) {
            if (refreshLayout.isRefreshing()) refreshLayout.finishRefresh(500);
            if (refreshLayout.isLoading()) refreshLayout.finishLoadmore(500);
        }
        NetworkUtil.dismissCutscenes();
        if (response != null && response.isSuccessful()) {
            onSuccess(call, response);
        }else if(response!=null&&response.code()>400){
            onFailure(call,new HttpException(response));
        }else {
            onFailure(call,new Exception(response.message()));
        }


    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
//        if (ptrFrame != null && ptrFrame.isRefreshing()) {
//            ptrFrame.refreshComplete();
//        }
        if (null != refreshLayout) {
            if (refreshLayout.isRefreshing()) refreshLayout.finishRefresh(500);
            if (refreshLayout.isLoading()) refreshLayout.finishLoadmore(500);
        }
        NetworkUtil.dismissCutscenes();
        commonHandleError(t);
    }

}
