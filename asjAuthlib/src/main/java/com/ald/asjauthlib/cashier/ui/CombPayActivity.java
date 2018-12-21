package com.ald.asjauthlib.cashier.ui;

import android.app.Activity;
import android.content.Intent;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.cashier.viewmodel.CombPayVM;
import com.ald.asjauthlib.databinding.ActivityCombPayBinding;
import com.ald.asjauthlib.authframework.core.AlaTopBarActivity;

import static com.ald.asjauthlib.utils.BundleKeys.REQUEST_CODE_BANKCARD_ADD_CP;


public class CombPayActivity extends AlaTopBarActivity<ActivityCombPayBinding> {

    CombPayVM combPayVM;

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_comb_pay;
    }

    @Override
    protected void setViewModel() {
        if (combPayVM == null) {
            combPayVM = new CombPayVM(this, getIntent());
        }
        cvb.setViewModel(combPayVM);
    }

    @Override
    protected void afterOnCreate() {
        super.afterOnCreate();
        setTitle(getResources().getString(R.string.cashier_comb_pay));
        setTitleColor(getResources().getColor(R.color.text_important_color));

    }

    @Override
    public String getStatName() {
        return getResources().getString(R.string.cashier_comb_pay);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_BANKCARD_ADD_CP && resultCode == Activity.RESULT_OK) {
            setResult(-3);//添加银行卡成功，区别于组合支付成功
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        combPayVM.onDestroy();
    }
}
