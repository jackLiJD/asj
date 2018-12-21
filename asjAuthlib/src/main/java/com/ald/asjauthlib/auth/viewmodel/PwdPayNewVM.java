package com.ald.asjauthlib.auth.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.UserApi;
import com.ald.asjauthlib.auth.model.BankCardCheckModel;
import com.ald.asjauthlib.auth.ui.BankCardAddIdActivity;
import com.ald.asjauthlib.auth.ui.BankCardListActivity;
import com.ald.asjauthlib.auth.ui.CreditPromoteActivity;
import com.ald.asjauthlib.auth.ui.PaySettingsActivity;
import com.ald.asjauthlib.auth.ui.RRIdAuthActivity;
import com.ald.asjauthlib.databinding.ActivityPwdPayNewBinding;
import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.utils.StageJumpEnum;
import com.ald.asjauthlib.widget.Keyboard;
import com.ald.asjauthlib.widget.PayEditText;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.network.NetworkUtil;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.network.RequestCallBack;
import com.ald.asjauthlib.authframework.core.network.entity.ApiResponse;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.BASE64Encoder;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.authframework.core.utils.UIUtils;
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
public class PwdPayNewVM extends BaseVM {
    private final ActivityPwdPayNewBinding binding;
    private final String oldPwd;
    private final String idNumber;
    private final BankCardCheckModel bankCardCheckModel;
    private final String stageJump;
    private Activity context;

    public PwdPayNewVM(Activity activity, ActivityPwdPayNewBinding cvb) {

        this.context = activity;
        this.binding = cvb;
        this.stageJump = context.getIntent().getStringExtra(BundleKeys.STAGE_JUMP);
        this.bankCardCheckModel = (BankCardCheckModel) context.getIntent().getSerializableExtra(BundleKeys.STAGE_BANK_CHECK);
        this.oldPwd = context.getIntent().getStringExtra(BundleKeys.SETTING_PAY_NEW_PWD);
        this.idNumber = context.getIntent().getStringExtra(BundleKeys.SETTING_PAY_ID_NUMBER);
        binding.icClose.setVisibility(View.GONE);
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

        /*
         * 当密码输入完成时的回调
         */
        binding.pwd.setOnInputFinishedListener(new PayEditText.OnInputFinishedListener() {
            @Override
            public void onInputFinished(String password) {
                handlePayPwd(password);
            }
        });
    }

    private void handlePayPwd(String pwd) {
        if (MiscUtils.isEmpty(pwd)) {
            UIUtils.showToast(AlaConfig.getResources().getString(R.string.pwd_pay_new_toast));
            return;
        }
        MiscUtils.hideKeyBoard(binding.pwd);
        JSONObject jsonObject = new JSONObject();
        if (MiscUtils.isNotEmpty(oldPwd)) {
            //修改密码
            jsonObject.put("oldPwd", MD5Util.getMD5Str(oldPwd));
            jsonObject.put("type", "C");
        } else {
            //设置密码
            jsonObject.put("type", "S");
            jsonObject.put("idNumber", BASE64Encoder.encodeString(idNumber));
        }
        jsonObject.put("newPwd", MD5Util.getMD5Str(pwd));
        Call<ApiResponse> call = RDClient.getService(UserApi.class).setPayPwd(jsonObject);
        NetworkUtil.showCutscenes(context, call);
        call.enqueue(new RequestCallBack<ApiResponse>() {
            @Override
            public void onSuccess(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response != null) {
                    //实名认证
                    if (StageJumpEnum.STAGE_AUTH.getModel().equals(stageJump)) {
                        if (bankCardCheckModel != null) {
                            Intent intent = new Intent();
                            intent.putExtra(BundleKeys.STAGE_JUMP, stageJump);
                            ActivityUtils.push(CreditPromoteActivity.class, intent);
                            ActivityUtils.pop();
                        }
                    } else if (StageJumpEnum.STAGE_BANK_CARD.getModel().equals(stageJump)) {
                        //来自添加银行卡
                        ActivityUtils.popUntil(BankCardListActivity.class);
                    } else if (StageJumpEnum.STAGE_SET_PAY_PWD.getModel().equals(stageJump)) {
                        //来自设置支付密码
//                        ActivityUtils.popUntil(SettingActivity.class);
                    } else if (StageJumpEnum.STAGE_CASHIER.getModel().equals(stageJump) || StageJumpEnum.STAGE_FORGET_PWD.getModel().equals(stageJump)) {
                        UIUtils.showToast("密码设置成功");
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                ActivityUtils.pop();
                                ActivityUtils.pop();
                                ActivityUtils.pop();
                            }
                        };
                        new android.os.Handler().postDelayed(runnable, 1000);

                    } else if (StageJumpEnum.STAGE_ORAL_ACTIVITY.getModel().equals(stageJump)) {
                        Intent intent = new Intent();
                        intent.putExtra(BundleKeys.STAGE_JUMP, stageJump);
                        ActivityUtils.finish(RRIdAuthActivity.class);
                        ActivityUtils.finish(BankCardAddIdActivity.class);
                        ActivityUtils.push(CreditPromoteActivity.class, intent);
                        ActivityUtils.pop(context);
                    } else if (StageJumpEnum.STAGE_TRADE_SCAN.getModel().equals(stageJump)) {
                        Intent intent = new Intent();
                        intent.putExtra(BundleKeys.STAGE_JUMP, stageJump);
                        ActivityUtils.finish(RRIdAuthActivity.class);
                        ActivityUtils.finish(BankCardAddIdActivity.class);
                        ActivityUtils.push(CreditPromoteActivity.class, intent);
                        ActivityUtils.pop(context);
                    } else if (StageJumpEnum.STAGE_BANK_CARD_ADD_PWD_SET.getModel().equals(stageJump)) {
                        //绑定银行卡密码设置

                    } else {
                        ActivityUtils.popUntil(PaySettingsActivity.class);
                        UIUtils.showToast("密码设置成功");
                    }
                }
            }
        });
    }

}
