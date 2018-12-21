package com.ald.asjauthlib.auth.viewmodel;

import android.app.Activity;
import android.databinding.ObservableField;
import android.os.Handler;
import android.view.View;

import com.ald.asjauthlib.auth.ui.AuthStatusActivity;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.vm.BaseVM;

/**
 * Created by luckyliang on 2018/1/8.
 * <p>
 * 提交申请等待页
 */

public class AuthStatusVM extends BaseVM {

    private int restTime = 3;
    private AuthStatusActivity context;
    public ObservableField<String> displayTimerTxt = new ObservableField<>();
    private Handler handler;
    private Runnable runnableCount = new Runnable() {
        @Override
        public void run() {
            new Handler().postDelayed(this, 1000);
            displayTimerTxt.set(restTime + "s后自动返回");
            restTime--;
        }
    };
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            context.setResult(Activity.RESULT_OK);
            ActivityUtils.pop(context);
        }
    };

    public AuthStatusVM(final AuthStatusActivity context) {
        this.context = context;
        handler = new Handler();
        handler.postDelayed(runnable, 3000);
        runnableCount.run();

    }

    public void onCloseClick(View view) {
        handler.removeCallbacks(runnable);
        runnable = null;
        runnableCount = null;
        context.setResult(Activity.RESULT_OK);
        ActivityUtils.pop(context);
    }
}
