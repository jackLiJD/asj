package com.ald.asjauthlib.auth.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.ald.asjauthlib.auth.UserApi;
import com.ald.asjauthlib.auth.model.LoginModel;
import com.ald.asjauthlib.auth.ui.PwdPayCaptchaActivity;
import com.ald.asjauthlib.auth.ui.PwdPayNewActivity;
import com.ald.asjauthlib.databinding.ActivityPwdPayOriginalBinding;
import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.widget.Keyboard;
import com.ald.asjauthlib.widget.PayEditText;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.info.SharedInfo;
import com.ald.asjauthlib.authframework.core.network.NetworkUtil;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.network.RequestCallBack;
import com.ald.asjauthlib.authframework.core.network.entity.ApiResponse;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.encryption.MD5Util;
import com.ald.asjauthlib.authframework.core.vm.BaseVM;

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
public class PwdPayOriginalVM extends BaseVM {
    private final ActivityPwdPayOriginalBinding binding;
    private final String stageJump;
    private Activity context;

    public PwdPayOriginalVM(Activity activity, ActivityPwdPayOriginalBinding cvb) {
        this.context = activity;
        this.binding = cvb;
        this.stageJump = context.getIntent().getStringExtra(BundleKeys.STAGE_JUMP);
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
                    /*if (MiscUtils.isEmpty(binding.pwd.getText().toString()) || binding.pwd.getText().toString().length() != 6) {
                        UIUtils.showToast(R.string.pwd_pay_id_input_pwd_err_toast);
                        return;
                    }
                    //当点击完成的时候,pwd.getText()获取密码，此时不应该注册OnInputFinishedListener接口

                    checkPwdPay(binding.pwd.getText().toString());*/
                }
            }
        });

        /*
         * 当密码输入完成时的回调
         */
        binding.pwd.setOnInputFinishedListener(new PayEditText.OnInputFinishedListener() {
            @Override
            public void onInputFinished(String password) {
                checkPwdPay(password);
            }
        });
    }

    /**
     * 忘记支付密码
     */
    public void forgetPayPwdClick(View view) {
        Intent intent = new Intent();
        LoginModel loginModel = SharedInfo.getInstance().getValue(LoginModel.class);
        if (loginModel != null) {
            String mobile = loginModel.getUser().getMobile();
            intent.putExtra(BundleKeys.STAGE_JUMP, stageJump);
            intent.putExtra(BundleKeys.SETTING_PAY_PHONE, mobile);
        }
        ActivityUtils.push(PwdPayCaptchaActivity.class, intent);
    }

    /**
     * 检验原支付密码
     */
    private void checkPwdPay(final String PwdPay) {
        JSONObject object = new JSONObject();
        object.put("payPwd", MD5Util.getMD5Str(PwdPay));

        Call<ApiResponse> call = RDClient.getService(UserApi.class).checkPayPwd(object);
        NetworkUtil.showCutscenes(context, call);
        call.enqueue(new RequestCallBack<ApiResponse>() {
            @Override
            public void onSuccess(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response != null) {
                    Intent intent = new Intent();
                    intent.putExtra(BundleKeys.SETTING_PAY_NEW_PWD, PwdPay);
                    intent.putExtra(BundleKeys.STAGE_JUMP, stageJump);
                    ActivityUtils.push(PwdPayNewActivity.class, intent);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                super.onFailure(call, t);
                binding.pwd.clearAll();
            }
        });
    }

}
