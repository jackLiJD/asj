package com.ald.asjauthlib.bankcardscan.viewmodel;

/*
 * Created by liangchen on 2018/4/4.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.View;

import com.ald.asjauthlib.databinding.ActivityResutlBinding;


public class BankCardScanResultVM {

    private String filePath, num, confidence;
    private Activity activity;
    private ActivityResutlBinding binding;

    public BankCardScanResultVM(Activity activity, ActivityResutlBinding binding) {
        this.activity = activity;
        this.binding = binding;
        Intent intent = activity.getIntent();
        filePath = intent.getStringExtra("filePath");
        num = intent.getStringExtra("bankNum");
        confidence = intent.getStringExtra("confidence");

        init();

    }

    private void init() {
        binding.resultBankcardEditText.setStr(num);
        binding.resultBankcardImage.setImageBitmap(BitmapFactory.decodeFile(filePath));
    }

    public void onBackClick(View view) {
        //回退到输入页面
        activity.setResult(Activity.RESULT_CANCELED);
        activity.finish();
    }

    public void onSubmitClick(View view) {
        String num = binding.resultBankcardEditText.getNumText();
        Intent intent = new Intent();
        intent.putExtra("filePath", filePath);
        intent.putExtra("bankNum", num);
        intent.putExtra("confidence", confidence);
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }

    /**
     * 重新识别
     */
    public void onRecognizeClick(View view) {
        activity.setResult(Activity.RESULT_FIRST_USER, new Intent());
        activity.finish();
    }
}
