package com.ald.asjauthlib.bankcardscan;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.bankcardscan.viewmodel.BankCardScanResultVM;
import com.ald.asjauthlib.databinding.ActivityResutlBinding;

/*
 * Created by liangchen on 2018/4/4.
 * 银行卡扫描结果、修改页
 */
public class ResultActivity extends Activity {

    ActivityResutlBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_resutl);
        BankCardScanResultVM viewModel = new BankCardScanResultVM(this, binding);
        binding.setViewModel(viewModel);
    }


//    @Override
//    public void onClick(View v) {
//        if (v.getId() == R.id.result_layout_sureBtn) {
//            clickSureBtn();
//        } else if (v.getId() == R.id.btn_back) {
//            finish();
//        } else if (v.getId() == R.id.resutl_layout_rootRel) {
//            BankCardUtil.isGoneKeyBoard(this);
//        }
//    }


}