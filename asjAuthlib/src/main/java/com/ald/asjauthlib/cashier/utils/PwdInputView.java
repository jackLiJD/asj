package com.ald.asjauthlib.cashier.utils;

import android.content.Context;

import com.ald.asjauthlib.dialog.PwdDialog;


/**
 * 密码输入视图
 * Created by sean yu on 2017/8/1.
 */
public class PwdInputView implements IPaymentView<String, String> {

    private PwdDialog.Builder builder;
    private PwdDialog pwdDialog;
    private Context context;
    private IViewResultCallBack<String> viewCallBack;
    private boolean secretFree = false;
    private String cachePwd = "";

    public PwdInputView(Context context) {
        this.context = context;
    }

    @Override
    public void createView(String viewParams) {
        if (!secretFree) {
            builder = new PwdDialog.Builder(context)
                    .setOnFinishListener(new PwdDialog.onFinishListener() {
                        @Override
                        public void onDone(String text) {
                            if (viewCallBack != null) {
                                viewCallBack.onViewResult(text);
                            }
                        }
                    });
            pwdDialog = builder.create();
            pwdDialog.show();
        }
    }

    @Override
    public void notifyView(String viewParams) {
        builder.setTitle(viewParams);
    }

    @Override
    public void destroyView() {
        if (pwdDialog != null && pwdDialog.isShowing()) {
            pwdDialog.dismiss();
        }
        pwdDialog = null;
    }

    @Override
    public void ObserverView(IViewResultCallBack<String> callBack) {
        this.viewCallBack = callBack;
    }

    @Override
    public void UnObserverView(IViewResultCallBack<String> callBack) {
        if (viewCallBack != null) {
            viewCallBack = null;
        }
    }

    public void setSecretFree(boolean secretFree) {
        this.secretFree = secretFree;
    }

    public void setSecretPwd(String secPwd) {
        cachePwd = secPwd;
    }

    public void paySecretFree(){
        if (viewCallBack != null) {
            viewCallBack.onViewResult(cachePwd);
        }
    }
}
