package com.ald.asjauthlib.authframework.core.config;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ald.asjauthlib.authframework.core.utils.PermissionCheck;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/1/6
 * 描述：
 * 修订历史：
 */
public abstract class AlaFragment extends Fragment implements StatNameProvider,PermissionsResult {
    // 标志位，标志已经初始化完成。
    protected boolean isPrepared;
    private AlaFragmentInterceptor interceptor;

    public AlaFragment() {
        interceptor = new AlaFragmentInterceptor(this, this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        interceptor.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        interceptor.onViewCreated(view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        interceptor.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        interceptor.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        interceptor.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        interceptor.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        interceptor.onDestroy();
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        interceptor.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionCheck.getInstance().onRequestPermissionsResult(getActivity(), requestCode, permissions,
                grantResults,AlaFragment.this);
    }

    @Override
    public void onUIRequestPermissionsGrantedResult(int requestCode) {
    }
}
