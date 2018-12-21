package com.ald.asjauthlib.auth.ui;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.viewmodel.BankCardAddIDVM;
import com.ald.asjauthlib.databinding.ActivityBankCardAddIdBinding;
import com.ald.asjauthlib.authframework.core.AlaTopBarActivity;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.utils.PermissionCheck;

public class BankCardAddIdActivity extends AlaTopBarActivity<ActivityBankCardAddIdBinding> {


    BankCardAddIDVM viewModel;

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_bank_card_add_id;
    }

    @Override
    protected void setViewModel() {
        viewModel = new BankCardAddIDVM(this, cvb);
        cvb.setViewModel(viewModel);
    }


    @Override
    public String getStatName() {
        return "添加银行卡/添加身份证信息页";
    }

    @Override
    protected void afterOnCreate() {
        super.afterOnCreate();
        setTitle("添加身份信息");
        setTitleColor(ContextCompat.getColor(AlaConfig.getContext(), R.color.text_important_color));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (viewModel == null)
            viewModel = new BankCardAddIDVM(this, cvb);
        viewModel.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            viewModel.doScan();
        } else {
            PermissionCheck.getInstance().showAskDialog(this,
                    R.string.permission_name_camera, (dialog, which) -> {

                    });
        }
    }

    @Override
    public void onBackPressed() {
        viewModel.onBackPressed(false);
    }
}
