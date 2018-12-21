package com.ald.asjauthlib.auth.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.UserApi;
import com.ald.asjauthlib.auth.ui.RRIdAuthActivity;
import com.ald.asjauthlib.bindingadapter.view.ViewBindingAdapter;
import com.ald.asjauthlib.databinding.ActivityAccountAppealBinding;
import com.ald.asjauthlib.utils.AppUtils;
import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.utils.Utils;
import com.alibaba.fastjson.JSONObject;
import com.bqs.risk.df.android.BqsDF;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.network.NetworkUtil;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.network.RequestCallBack;
import com.ald.asjauthlib.authframework.core.network.entity.ApiResponse;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.EditTextFormat;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.authframework.core.utils.UIUtils;
import com.ald.asjauthlib.authframework.core.utils.encryption.MD5Util;
import com.ald.asjauthlib.authframework.core.vm.BaseVM;

import java.util.LinkedList;

import cn.tongdun.android.shell.FMAgent;
import retrofit2.Call;
import retrofit2.Response;


/*
 * Created by luckyliang on 2017/11/10.
 */

public class AppealPhoneVM extends BaseVM {

    private Context mContext;

    //身份验证成功返回，切换流程字体颜色
    public final ObservableInt txt_color_step = new ObservableInt();

    public final ObservableField<Drawable> ic_step_2 = new ObservableField<>();
    public final ObservableField<Drawable> ic_setp_3 = new ObservableField<>();

    public final ObservableField<String> txt_btn = new ObservableField<>();

    public final ObservableField<String> txt_old_phone = new ObservableField<>();
    public final ObservableField<String> txt_new_phone = new ObservableField<>();

    //btn
    public final ObservableBoolean enabled = new ObservableBoolean();

    public final ObservableBoolean displayStep1 = new ObservableBoolean();
    public final ObservableBoolean displayStep3 = new ObservableBoolean();

    private CountDownTimer countDownTimer;

    public final ObservableField<ViewBindingAdapter.MobileWatcher> emptyMobileWatcher = new ObservableField<>();

    private ActivityAccountAppealBinding mBinding;

    public static String newPhone = "";
    public static String oldPhone = "";
    private boolean verifyCodeGet = false;

    public AppealPhoneVM(final Context context, Intent intent, ActivityAccountAppealBinding binding) {

        mContext = context;
        mBinding = binding;
        newPhone = intent.getStringExtra(BundleKeys.APPEAL_NEWPHONE);
        if (!MiscUtils.isEmpty(newPhone)) {
            showPwdSetLayout();
        } else {
            txt_color_step.set(ContextCompat.getColor(AlaConfig.getContext(), R.color.color_un_auth_step_txt));
            Resources resources = AlaConfig.getResources();
            ic_step_2.set(ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.ic_appeal_id_unauth));
            ic_setp_3.set(ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.ic_appeal_not_over));
            txt_btn.set(resources.getString(R.string.appeal_btn_next));

            displayStep1.set(true);
            displayStep3.set(false);

            countDownTimer = new CountDownTimer(62000L, 1000L) {
                @Override
                public void onTick(long l) {

                    mBinding.btnCaptchaRight.setEnabled(false);
                    long nextUntilFinished = l / 1000 - 1;
                    mBinding.btnCaptchaRight.setText(String.format(AlaConfig.getResources().getString(R.string.appeal_msg_time), nextUntilFinished));
                }

                @Override
                public void onFinish() {
                    mBinding.btnCaptchaRight.setEnabled(true);
                    mBinding.btnCaptchaRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, AlaConfig.getResources()
                            .getDimensionPixelSize(R.dimen.x28));
                    mBinding.btnCaptchaRight.setText(mContext.getString(R.string.appeal_msg_resend));
                }
            };
        }


    }


    public LinkedList<EditText> edList = new LinkedList<>();
    /**
     * 监听 app:asJwatcher="@{viewModel.watcher}"
     */
    public EditTextFormat.EditTextFormatWatcher watcherStep1 = new EditTextFormat.EditTextFormatWatcher() {
        @Override
        public void OnTextWatcher(String str) {
            enabled.set(inputCheck());
            enabled.notifyChange();
        }
    };

    private boolean inputCheck() {
        return mBinding.etPwd.getText().length() > 5 ||
                !(mBinding.etOldNo.getText().toString().replace(" ", "").length() < 11
                        || mBinding.etNo.getText().toString().replace(" ", "").length() < 11
                        || mBinding.etVerifyCode.getText().toString().replace(" ", "").length() < 6);
    }


    /**
     * 获取短信
     */
    public void getMsgClick(View view) {
        //调动获取短信接口
        if (!checkPhone()) {
            return;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("mobile", newPhone);
        jsonObject.put("type", "M");
        String blackBox = FMAgent.onEvent(mContext);
        jsonObject.put("blackBox", blackBox);
        //白骑士设备指纹
        String bqsBlackBox = BqsDF.getTokenKey();
        jsonObject.put("bqsBlackBox", bqsBlackBox);
        Call<Boolean> call = RDClient.getService(UserApi.class).getVerifyCode(jsonObject);
        NetworkUtil.showCutscenes(mContext, call);
        call.enqueue(new RequestCallBack<Boolean>() {
            @Override
            public void onSuccess(Call<Boolean> call, Response<Boolean> response) {
                UIUtils.showToast(AlaConfig.getResources().getString(R.string.appeal_msg_send_success));
                if (countDownTimer != null)
                    countDownTimer.start();
                verifyCodeGet = true;
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                super.onFailure(call, t);
                UIUtils.showToast(AlaConfig.getResources().getString(R.string.appeal_msg_send_fail));
            }
        });
    }

    /**
     * goto RRIdAuth/set pwd
     */
    public void nestStepClick(View view) {
        if (txt_btn.get().equals(mContext.getString(R.string.appeal_btn_next))) {
            if (checkPhone()) {
                if (!verifyCodeGet) {
                    UIUtils.showToast(mContext.getString(R.string.appeal_msg_not_get));
                    return;
                }
                if (MiscUtils.isEmpty(mBinding.etVerifyCode.getText().toString())) {
                    UIUtils.showToast(mContext.getString(R.string.appeal_msg_null));
                    return;
                }
                oldPhone = mBinding.etOldNo.getText().toString().trim().replace(" ", "");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("verifyCode", mBinding.etVerifyCode.getText().toString());
                jsonObject.put("newMobile", newPhone);
                jsonObject.put("oldMobile", oldPhone);
                Call<ApiResponse> call = RDClient.getService(UserApi.class).accountAppealCheckSms(jsonObject);
                NetworkUtil.showCutscenes(mContext, call);
                call.enqueue(new RequestCallBack<ApiResponse>() {
                    @Override
                    public void onSuccess(Call<ApiResponse> call, Response<ApiResponse> ApiResponse) {
                        gotoRRidAuth();
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        super.onFailure(call, t);
                        UIUtils.showToast(t.getMessage());
                    }
                });

            }
        } else {
            //verify pwd
            String loginPwd = mBinding.etPwd.getText().toString();
            if (MiscUtils.isEmpty(loginPwd)) {
                UIUtils.showToast(R.string.login_hit_password);
                return;
            }
            if (loginPwd.length() < 6) {
                UIUtils.showToast(R.string.register_password_toast_min);
                return;
            }
            if (loginPwd.length() > 18) {
                UIUtils.showToast(AlaConfig.getResources().getString(R.string.register_password_toast_max));
                return;
            }
            if (!AppUtils.isPassword(loginPwd)) {
                UIUtils.showToast(AlaConfig.getResources().getString(R.string.login_hit_password_toast));
                return;
            }
            //appeal and reset pwd
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("oldMobile", oldPhone);
            jsonObject.put("password", MD5Util.getMD5Str(loginPwd));
            Call<ApiResponse> call = RDClient.getService(UserApi.class).accountAppealDo(jsonObject);
            NetworkUtil.showCutscenes(mContext, call);
            call.enqueue(new RequestCallBack<ApiResponse>() {
                @Override
                public void onSuccess(Call<ApiResponse> call, Response<ApiResponse> ApiResponse) {
                    //goto login
                    Utils.jumpToLoginNoResult();
                    ActivityUtils.pop();
                    ActivityUtils.pop();
                    ActivityUtils.pop();
                    oldPhone = "";
                    newPhone = "";
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    super.onFailure(call, t);
                }
            });

        }
    }

    private void gotoRRidAuth() {
        Intent intent = new Intent();
        intent.putExtra(BundleKeys.APPEAL_NEWPHONE, newPhone);
        ActivityUtils.push(RRIdAuthActivity.class, intent, BundleKeys.REQUEST_CODE_STAGE_APPEAL);
    }

    private boolean checkPhone() {
        String mobileRegex = "^1[0-9]{10}$";
        oldPhone = mBinding.etOldNo.getText().toString().trim().replace(" ", "");
        newPhone = mBinding.etNo.getText().toString().trim().replace(" ", "");

        if (MiscUtils.isEmpty(oldPhone)) {
            UIUtils.showToast(AlaConfig.getResources().getString(R.string.appeal_toast_old_phone_null));
            return false;
        }

        if (oldPhone.length() < 11) {
            UIUtils.showToast(AlaConfig.getResources().getString(R.string.login_hit_user_toast));
            return false;
        }

        if (!oldPhone.matches(mobileRegex)) {
            UIUtils.showToast(AlaConfig.getResources().getString(R.string.login_hit_user_toast));
            return false;
        }

        if (MiscUtils.isEmpty(newPhone)) {
            UIUtils.showToast(AlaConfig.getResources().getString(R.string.appeal_toast_new_phone_null));
            return false;
        }
        if (newPhone.length() < 11) {
            UIUtils.showToast(AlaConfig.getResources().getString(R.string.login_hit_user_toast));
            return false;
        }

        if (!newPhone.matches(mobileRegex)) {
            UIUtils.showToast(AlaConfig.getResources().getString(R.string.login_hit_user_toast));
            return false;
        }
        if (oldPhone.equals(newPhone)) {
            UIUtils.showToast(mContext.getString(R.string.appeal_toast_same_phone));
            return false;
        }
        return true;
    }

    /**
     * back from liveness
     */
    private void showPwdSetLayout() {
        txt_color_step.set(ContextCompat.getColor(AlaConfig.getContext(), R.color.color_auth_step_txt));
        Resources resources = AlaConfig.getResources();
        ic_step_2.set(ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.ic_auth_step_id));
        ic_setp_3.set(ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.ic_appeal_over));
        txt_btn.set(resources.getString(R.string.appeal_submit));
        enabled.set(false);

        displayStep1.set(false);
        displayStep3.set(true);

        txt_old_phone.set(String.format("确认将原号码%s更换为", oldPhone));
        txt_new_phone.set(newPhone);
    }


}
