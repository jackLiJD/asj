package com.ald.asjauthlib.auth.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.text.TextUtils;
import android.view.View;

import com.ald.asjauthlib.auth.UserApi;
import com.ald.asjauthlib.auth.model.WithholdSettingsCheckModel;
import com.ald.asjauthlib.auth.ui.PwdPayCaptchaActivity;
import com.ald.asjauthlib.auth.ui.PwdPayOriginalActivity;
import com.ald.asjauthlib.auth.ui.WithholdSettingsActivity;
import com.ald.asjauthlib.databinding.ActivityPaySettingsBinding;
import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.utils.ModelEnum;
import com.ald.asjauthlib.authframework.core.network.NetworkUtil;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.network.RequestCallBack;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.UIUtils;
import com.ald.asjauthlib.authframework.core.vm.BaseVM;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 支付设置VM
 * Created by ywd on 2017/11/13.
 */

public class PaySettingsVM extends BaseVM {
    private Context context;
    private ActivityPaySettingsBinding binding;
    private final String isPayPwd;
    private String mobile;
    public ObservableBoolean showWithholdEntrance=new ObservableBoolean(false);//是否显示代扣设置入口

    public PaySettingsVM(Context context, ActivityPaySettingsBinding binding) {
        this.context = context;
        this.binding = binding;
        Intent intent = ((Activity) context).getIntent();
        this.isPayPwd = intent.getStringExtra(BundleKeys.SETTING_PAY_PWD);
        this.mobile = intent.getStringExtra(BundleKeys.SETTING_PAY_PHONE);
        getWithholdInfo();
    }

    /**
     * 获取是否显示代扣入口信息
     */
    private void getWithholdInfo(){
        Call<WithholdSettingsCheckModel> call = RDClient.getService(UserApi.class).withholdCheck();
        NetworkUtil.showCutscenes(context, call);
        call.enqueue(new RequestCallBack<WithholdSettingsCheckModel>() {
            @Override
            public void onSuccess(Call<WithholdSettingsCheckModel> call, Response<WithholdSettingsCheckModel> response) {
                if (response != null) {
                    //白名单+主卡显示代扣入口
                    WithholdSettingsCheckModel model = response.body();
                    if(ModelEnum.Y.getModel().equals(model.getIsUserSeed())){
                        //若有主卡，则显示代扣入口
                        if(ModelEnum.Y.getModel().equals(model.getIsMain())){
                            showWithholdEntrance.set(true);
                        }else {
                            showWithholdEntrance.set(false);
                        }
                    }else if (TextUtils.isEmpty(model.getIsUserSeed())) {
                        //非白名单 若有主卡，则显示代扣入口
                        if(ModelEnum.Y.getModel().equals(response.body().getIsMain())){
                            showWithholdEntrance.set(true);
                        }else {
                            showWithholdEntrance.set(false);
                        }
                    } else {
                        showWithholdEntrance.set(false);
                    }
                }
            }
        });
    }

    /**
     * 支付密码设置点击
     *
     * @param view
     */
    public void payPwdSetClick(View view) {
        Intent intent = new Intent();
        if (ModelEnum.Y.getModel().equals(isPayPwd)) {
            ActivityUtils.push(PwdPayOriginalActivity.class, intent);
        } else {
            intent.putExtra(BundleKeys.SETTING_PAY_PHONE, mobile);
            ActivityUtils.push(PwdPayCaptchaActivity.class, intent);
        }
    }

    /**
     * 代扣设置点击
     *
     * @param view
     */
    public void withholdClick(View view) {
        getDeal();
    }

    /**
     * 获取当前是否在代扣
     */
    private void getDeal(){
        Call<WithholdSettingsCheckModel> call = RDClient.getService(UserApi.class).withholdCheck();
        NetworkUtil.showCutscenes(context, call);
        call.enqueue(new RequestCallBack<WithholdSettingsCheckModel>() {
            @Override
            public void onSuccess(Call<WithholdSettingsCheckModel> call, Response<WithholdSettingsCheckModel> response) {
                if (response != null) {
                    if(ModelEnum.N.getModel().equals(response.body().getIsDeal())){
                        ActivityUtils.push(WithholdSettingsActivity.class);
                    }else {
                        UIUtils.showToast(response.body().getMessage());
                    }
                }
            }
        });
    }

}
