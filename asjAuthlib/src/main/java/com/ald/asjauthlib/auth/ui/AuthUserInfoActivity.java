package com.ald.asjauthlib.auth.ui;


import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.viewmodel.AuthUserInfoVM;
import com.ald.asjauthlib.databinding.ActivityUserAuthInfoBinding;
import com.ald.asjauthlib.authframework.core.AlaTopBarActivity;

/*
 * Created by sean yu on 2017/6/12.
 */

public class AuthUserInfoActivity extends AlaTopBarActivity<ActivityUserAuthInfoBinding> {

    @Override
    public String getStatName() {
        return "认证信息";
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_user_auth_info;
    }

    @Override
    protected void setViewModel() {
        cvb.setViewModel(new AuthUserInfoVM(this));
    }

}
