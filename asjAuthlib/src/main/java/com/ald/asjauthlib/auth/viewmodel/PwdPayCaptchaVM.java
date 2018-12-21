package com.ald.asjauthlib.auth.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.ObservableField;
import android.view.View;
import android.widget.EditText;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.UserApi;
import com.ald.asjauthlib.auth.model.BankCardGetCaptchaodel;
import com.ald.asjauthlib.auth.ui.PwdPayIdfActivity;
import com.ald.asjauthlib.databinding.ActivityPwdPayCaptchaBinding;
import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.utils.InputCheck;
import com.ald.asjauthlib.widget.TimeCountButton;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.network.NetworkUtil;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.network.RequestCallBack;
import com.ald.asjauthlib.authframework.core.network.entity.ApiResponse;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.EditTextFormat;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.authframework.core.utils.UIUtils;
import com.ald.asjauthlib.authframework.core.vm.BaseVM;

import java.util.LinkedList;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/1/12 14:14
 * 描述：
 * 修订历史：
 */
public class PwdPayCaptchaVM extends BaseVM {
    public final ObservableField<String> displayTitle = new ObservableField<>();
    private final ActivityPwdPayCaptchaBinding binding;
    private final String phone;
    private final String name;
    private final TimeCountButton timeButton;
    private final String stageJump;
    /**
     * 需监听的editText list
     */
    public LinkedList<EditText> edList = new LinkedList<>();
    public ObservableField<Boolean> enable = new ObservableField<>(false);
    /**
     * 监听EditText 变化
     */
    public EditTextFormat.EditTextFormatWatcher watcher = new EditTextFormat.EditTextFormatWatcher() {
        @Override
        public void OnTextWatcher(String str) {
            enable.set(InputCheck.checkEtAndCbList(false, edList, null));
            enable.notifyChange();
        }
    };
    private Activity context;

    public PwdPayCaptchaVM(Activity activity, ActivityPwdPayCaptchaBinding cvb) {
        this.context = activity;
        this.binding = cvb;
        this.stageJump = context.getIntent().getStringExtra(BundleKeys.STAGE_JUMP);
        this.phone = context.getIntent().getStringExtra(BundleKeys.SETTING_PAY_PHONE);
        this.name = context.getIntent().getStringExtra(BundleKeys.BANK_CARD_NAME);
        String title = String
                .format(AlaConfig.getResources().getString(R.string.pwd_pay_captcha_title), MiscUtils.formatPhone(phone));
        displayTitle.set(title);
        timeButton = new TimeCountButton(60000, 1000);

    }


    /**
     * 验证验证码
     */
    public void nextClick(View view) {
        String captcha = binding.etCaptcha.getText().toString();
        if (MiscUtils.isEmpty(captcha)) {
            UIUtils.showToast(R.string.pwd_pay_captcha_toast);
            return;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("verifyCode", captcha);
        Call<BankCardGetCaptchaodel> call = RDClient.getService(UserApi.class).checkPayPwdVerifyCode(jsonObject);
        NetworkUtil.showCutscenes(context, call);
        call.enqueue(new RequestCallBack<BankCardGetCaptchaodel>() {
            @Override
            public void onSuccess(Call<BankCardGetCaptchaodel> call, Response<BankCardGetCaptchaodel> response) {
                Intent intent = new Intent();
                intent.putExtra(BundleKeys.STAGE_JUMP, stageJump);
                intent.putExtra(BundleKeys.BANK_CARD_NAME, response.body().getRealName());
                ActivityUtils.push(PwdPayIdfActivity.class, intent);
            }
        });
    }

    /**
     * 获取验证码
     */
    public void obtainCaptchaClick(View view) {
        Call<ApiResponse> call = RDClient.getService(UserApi.class).getPayPwdVerifyCode();
        NetworkUtil.showCutscenes(context, call);
        timeButton.setButton(binding.btnCaptchaRight);
        call.enqueue(new RequestCallBack<ApiResponse>() {
            @Override
            public void onSuccess(Call<ApiResponse> call, Response<ApiResponse> response) {
                UIUtils.showToast(AlaConfig.getResources().getString(R.string.register_get_captcha_tip));
                timeButton.start();
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                super.onFailure(call, t);
                timeButton.onFinish();
            }
        });
    }
}
