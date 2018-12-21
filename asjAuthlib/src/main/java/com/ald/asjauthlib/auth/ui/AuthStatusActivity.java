package com.ald.asjauthlib.auth.ui;


import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.viewmodel.AuthStatusVM;
import com.ald.asjauthlib.databinding.ActivityAuthStatusBinding;
import com.ald.asjauthlib.authframework.core.config.AlaBaseActivity;

public class AuthStatusActivity extends AlaBaseActivity<ActivityAuthStatusBinding> {


    AuthStatusVM authStatusVM;

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_auth_status;
    }

    @Override
    protected void setViewModel(ActivityAuthStatusBinding contentBinding) {
        authStatusVM = new AuthStatusVM(this);
        cvb.setViewModel(authStatusVM);
    }


    @Override
    public String getStatName() {
        return "提交审核等待处理";
    }

    @Override
    public void onBackPressed() {
        authStatusVM.onCloseClick(null);
    }
}
