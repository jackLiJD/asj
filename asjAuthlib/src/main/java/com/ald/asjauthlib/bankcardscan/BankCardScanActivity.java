package com.ald.asjauthlib.bankcardscan;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.bankcardscan.viewmodel.BankcardScanVM;
import com.ald.asjauthlib.databinding.BankcardscanLayoutBinding;

public class BankCardScanActivity extends Activity {


    BankcardscanLayoutBinding databinding;
    BankcardScanVM viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databinding = DataBindingUtil.setContentView(this, R.layout.bankcardscan_layout);
        viewModel = new BankcardScanVM(this, databinding);

        databinding.setViewModel(viewModel);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
        viewModel.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.onDestroy();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        viewModel.onActivityResult(requestCode, resultCode, data);
    }

}