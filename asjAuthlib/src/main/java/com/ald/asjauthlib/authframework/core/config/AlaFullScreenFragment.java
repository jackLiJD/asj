package com.ald.asjauthlib.authframework.core.config;

import android.databinding.ViewDataBinding;
import android.support.v4.app.FragmentActivity;

public abstract class AlaFullScreenFragment<CVB extends ViewDataBinding> extends AlaBaseFragment<CVB> implements BackFragment{
    @Override
    public boolean onBackPressed() {
        return false;
    }

    public boolean exit() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.getSupportFragmentManager().popBackStack();
        }
        return true;
    }
}
