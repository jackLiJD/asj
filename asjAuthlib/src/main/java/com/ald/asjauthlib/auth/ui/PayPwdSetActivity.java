package com.ald.asjauthlib.auth.ui;


import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.viewmodel.PayPwdSetVM;
import com.ald.asjauthlib.databinding.ActivityPayPwdSetBinding;
import com.ald.asjauthlib.authframework.core.AlaTopBarActivity;

public class PayPwdSetActivity extends AlaTopBarActivity<ActivityPayPwdSetBinding> {

    PayPwdSetVM pwdSetVM;

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_pay_pwd_set;
    }

    @Override
    protected void setViewModel() {
        pwdSetVM = new PayPwdSetVM(cvb, this);
        cvb.setViewModel(pwdSetVM);

    }

    @Override
    protected void afterOnCreate() {
        super.afterOnCreate();
        setTitle(getResources().getString(R.string.pay_pwd_set_title));
        setLeftImageListener(v -> pwdSetVM.onBackPressed());
    }

    @Override
    public String getStatName() {
        return "绑定银行卡设置密码";
    }

    @Override
    public void onBackPressed() {
        pwdSetVM.onBackPressed();
    }
}
