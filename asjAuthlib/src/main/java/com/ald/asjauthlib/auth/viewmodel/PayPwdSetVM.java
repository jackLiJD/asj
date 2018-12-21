package com.ald.asjauthlib.auth.viewmodel;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.dialog.TwoButtonDialog;
import com.ald.asjauthlib.databinding.ActivityPayPwdSetBinding;
import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.widget.Keyboard;
import com.ald.asjauthlib.widget.PayEditText;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.authframework.core.utils.UIUtils;
import com.ald.asjauthlib.authframework.core.vm.BaseVM;


/*
 * Created by liangchen on 2018/4/8.
 */

public class PayPwdSetVM extends BaseVM {

    public final ObservableField<String> hint = new ObservableField<>();//错误提示
    public final ObservableInt hintColor = new ObservableInt(R.color.color_646464);//#f81400
    private String fomerPwd = "";//首次输入的密码
    private ActivityPayPwdSetBinding binding;
    private Activity activity;
    private Intent intent;

    public PayPwdSetVM(ActivityPayPwdSetBinding binding, Activity activity) {
        this.binding = binding;
        this.activity = activity;
        this.intent = activity.getIntent();
        hintColor.set(AlaConfig.getResources().getColor(R.color.color_646464));
        hint.set("为保障您的资金安全，请设置密码");
        initEvent();
    }


    private void initEvent() {
        binding.keyboard.setOnClickKeyboardListener(new Keyboard.OnClickKeyboardListener() {
            @Override
            public void onKeyClick(int position, String value) {
                if (position < 11 && position != 9) {
                    binding.pwd.add(value);
                } else if (position == 9) {
//                    binding.pwd.remove();
                } else if (position == 11) {
                    binding.pwd.remove();
                    //当点击完成的时候，也可以通过payEditText.getText()获取密码，此时不应该注册OnInputFinishedListener接口
                    /*String payPwd = binding.pwd.getText().toString();
                    if (MiscUtils.isNotEmpty(payPwd) && payPwd.length() == 6) {
                        handlePayPwd(binding.pwd.getText());
                    } else {
                        UIUtils.showToast("请输入正确的密码");
                    }*/
                }
            }
        });

        binding.pwd.setOnInputFinishedListener(new PayEditText.OnInputFinishedListener() {
            @Override
            public void onInputFinished(String password) {
                handlePayPwd(password, binding.pwd);
            }
        });
    }

    private void handlePayPwd(String pwd, final PayEditText payEditText) {
        if (MiscUtils.isEmpty(pwd)) {
            UIUtils.showToast("请输入支付密码");
            return;
        }
        MiscUtils.hideKeyBoard(payEditText);
        if (MiscUtils.isEmpty(fomerPwd)) {
            //首次输入密码
            fomerPwd = pwd;
            payEditText.clearAll();
            this.activity.setTitle("再次输入支付密码");

//            textViewTitle.setText("再次确认支付密码");
            return;
        }
        if (!MiscUtils.isEquals(fomerPwd, pwd)) {
            UIUtils.showToast("两次输入不一致");
            hint.set("与上次输入不一致，请重新输入");
            hintColor.set(AlaConfig.getResources().getColor(R.color.red));
            activity.setTitle(AlaConfig.getResources().getString(R.string.pay_pwd_set_title));
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(binding.tvHint, "translationX", 0, 100, -100, 20, -20, 0);
            objectAnimator.setInterpolator(new DecelerateInterpolator());
            objectAnimator.setDuration(1000);
            objectAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    fomerPwd = "";
                    payEditText.clearAll();
                    hintColor.set(AlaConfig.getResources().getColor(R.color.color_646464));
                    hint.set("为保障您的资金安全，请设置密码");
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            objectAnimator.start();
//            new Handler().postDelayed(new Runnable() {
//                public void run() {
////                    textViewTitle.setText("请输入支付密码");
//                    fomerPwd = "";
//                    hint.set("");
//                    payEditText.clearAll();
//                }
//            }, 1200);
            return;
        }
        //调用设置密码接口后跳转
        intent.putExtra(BundleKeys.SETTING_PAY_PWD, fomerPwd);
        activity.setResult(Activity.RESULT_OK, intent);
        ActivityUtils.pop(activity);

    }

    public void onBackPressed() {
        //确认弹框
        final TwoButtonDialog.Builder builder = new TwoButtonDialog.Builder(activity);
        builder.setContent(AlaConfig.getResources().getString(R.string.bankcard_add_dialog_message))
                .setSureListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.setResult(Activity.RESULT_CANCELED);
                        activity.finish();
                    }
                }).create()
                .show();

    }
}
