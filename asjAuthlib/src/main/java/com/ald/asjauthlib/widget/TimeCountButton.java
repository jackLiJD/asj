package com.ald.asjauthlib.widget;

import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * 按钮倒计时
 * Created by yaowenda on 17/3/8.
 */

public class TimeCountButton extends CountDownTimer {
    private TextView btnGetCode;

    public TimeCountButton(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    public void setButton(TextView btnGetCode){
        this.btnGetCode=btnGetCode;
    }

    @Override
    public void onTick(long l) {
        btnGetCode.setClickable(false);
        btnGetCode.setText("重新获取("+l/1000+")");
    }

    @Override
    public void onFinish() {
        if(btnGetCode != null){
            btnGetCode.setClickable(true);
            btnGetCode.setText("重新获取");
        }
    }
}
