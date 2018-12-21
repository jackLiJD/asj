package com.ald.asjauthlib.web;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.model.LoginModel;
import com.ald.asjauthlib.auth.model.UserModel;
import com.ald.asjauthlib.dialog.CashierErrNoticeDialog;
import com.ald.asjauthlib.dialog.KickLoginDialog;
import com.ald.asjauthlib.utils.Utils;
import com.ald.asjauthlib.authframework.core.config.AccountProvider;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.info.SharedInfo;
import com.ald.asjauthlib.authframework.core.network.exception.ApiExceptionEnum;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.InfoUtils;
import com.ald.asjauthlib.authframework.core.utils.log.Logger;


/**
 * 自定义接收器
 */
public class ApiReceiver extends BroadcastReceiver {
    public static final IntentFilter INTENT_FILTER_OPEN = new IntentFilter(AlaConfig.ACTION_API_OPEN);
    private static final String TAG = ApiReceiver.class.getSimpleName();
    private KickLoginDialog kickLoginDialog;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!intent.getAction().equals(AlaConfig.ACTION_API_OPEN)) {
            Logger.d(TAG, "收到action不一致的广播");
            return;
        }
        final int errCode = intent.getIntExtra(AlaConfig.EXTRA_ERR_CODE, 0);
        if (errCode == ApiExceptionEnum.REQUEST_INVALID_SIGN_ERROR.getErrorCode()
                || (errCode == ApiExceptionEnum.REQUEST_PARAM_TOKEN_ERROR.getErrorCode() && AlaConfig.isLand())) {    // 账号在其他设备登录，但是code 1005后台使用的地方很多，未必是在其他设备登录
            Logger.w(TAG, "onReceive, errorCode == " + errCode);
            LoginModel loginData = new LoginModel();
            loginData.setToken("");
            UserModel userModel = new UserModel();
            String userName = InfoUtils.getDeviceId();
            userModel.setUserName(userName);
            loginData.setUser(userModel);
            SharedInfo.getInstance().setValue(LoginModel.class, loginData);
            AlaConfig.setAccountProvider(new AccountProvider() {
                @Override
                public String getUserName() {
                    return InfoUtils.getDeviceId();
                }

                @Override
                public String getUserToken() {
                    return "";
                }
            });
            AlaConfig.updateLand(false);
            if (kickLoginDialog == null) {
                buildDialog(ActivityUtils.peek());
                kickLoginDialog.show();
            } else {
                if (ActivityUtils.peek() != kickLoginDialog.getMyContext()) {
                    buildDialog(ActivityUtils.peek());
                    kickLoginDialog.show();
                } else if (!kickLoginDialog.isShowing()) {
                    kickLoginDialog.show();
                }
            }
        }
        else if (errCode == ApiExceptionEnum.USER_PWD_INPUT_ERROR.getErrorCode()
                || errCode == ApiExceptionEnum.USER_PWD_FORBID.getErrorCode()) {
            String msg = intent.getStringExtra(AlaConfig.EXTRA_ERR_MSG);
            new CashierErrNoticeDialog.Builder(AlaConfig.getCurrentActivity()).setMsg(msg).twoBtnCreater().show();
        }
    }

    private void buildDialog(Context context) {
        if (context == null) {
            context = AlaConfig.getContext();
        }
        kickLoginDialog = new KickLoginDialog(context);
        kickLoginDialog.setContent(AlaConfig.getResources().getString(R.string.dialog_kick_login_content_default));
        kickLoginDialog.setListener((dialog, view) -> Utils.jumpToLoginNoResult());
    }

}
