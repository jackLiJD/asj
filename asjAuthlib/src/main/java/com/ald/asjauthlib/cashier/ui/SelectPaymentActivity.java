package com.ald.asjauthlib.cashier.ui;

import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.cashier.viewmodel.SelectPaymentVM;
import com.ald.asjauthlib.databinding.ActivitySelectPaymentBinding;
import com.ald.asjauthlib.authframework.core.AlaTopBarActivity;
import com.ald.asjauthlib.authframework.core.network.NetworkUtil;

import static com.ald.asjauthlib.utils.BundleKeys.REQUEST_CODE_CP;


/**
 * 选择支付方式页面
 * Created by ywd on 2017/10/30.
 */

public class SelectPaymentActivity extends AlaTopBarActivity<ActivitySelectPaymentBinding> {
    private SelectPaymentVM viewModel;

    @Override
    public String getStatName() {
        return "收银台页面";
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_select_payment;
    }

    @Override
    protected void setViewModel() {
        viewModel = new SelectPaymentVM(this, cvb);
        cvb.setViewModel(viewModel);

    }

    @Override
    protected void afterOnCreate() {
        super.afterOnCreate();
        setTitle(getResources().getString(R.string.cashier_title));
        setTitleColor(getResources().getColor(R.color.text_important_color));
        setLeftImageListener(v -> viewModel.onBackPressed());
    }

//    @Override
//    public void onBackPressed() {
//        viewModel.onBackPressed();
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CP && resultCode == Activity.RESULT_OK) {
            //组合支付成功
            setResult(Activity.RESULT_OK);
            finish();
        }
        viewModel.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.unregisterReceiver();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            viewModel.onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        NetworkUtil.dismissCutscenes();
    }
}
