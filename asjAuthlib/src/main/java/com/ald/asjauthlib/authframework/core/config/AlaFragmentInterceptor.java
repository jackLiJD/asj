package com.ald.asjauthlib.authframework.core.config;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;


/**
 * Created by Jacky Yu on 2015/11/26.
 */
public class AlaFragmentInterceptor {
    private Fragment fragment;
    //    private WeakReference<View> viewRef;
//    private boolean useAnnotation;
    private StatNameProvider statNameProvider;

    public AlaFragmentInterceptor(Fragment fragment, StatNameProvider statNameProvider) {
        this.fragment = fragment;
        this.statNameProvider = statNameProvider;
    }


    public void onViewCreated(View view) {

    }

    public void onActivityCreated(Bundle savedInstanceState) {
        Intent intent = fragment.getActivity().getIntent();
        Bundle bundle = null;
        if (intent != null) {
            bundle = intent.getExtras();
        }
    }

    public void onCreate(Bundle savedInstanceState) {

    }

    public void onResume() {
    }

    public void onPause() {

    }

    public void onDestroy() {
    }

    public void onViewStateRestored(Bundle savedInstanceState) {

    }

    public void onSaveInstanceState(Bundle outState) {

    }


}
