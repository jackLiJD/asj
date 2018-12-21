package com.ald.asjauthlib.auth.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.view.View;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.UserApi;
import com.ald.asjauthlib.auth.model.WithholdBankCardModel;
import com.ald.asjauthlib.auth.model.WithholdSettingsModel;
import com.ald.asjauthlib.auth.model.WithholdSwitchModel;
import com.ald.asjauthlib.auth.model.WithholdUseBalanceModel;
import com.ald.asjauthlib.auth.ui.WithholdCardActivity;
import com.ald.asjauthlib.databinding.ActivityWithholdSettingsBinding;
import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.dialog.WithCancelDialog;
import com.ald.asjauthlib.dialog.WithholdTipDialog;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.network.NetworkUtil;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.network.RequestCallBack;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.authframework.core.vm.BaseVM;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 代扣设置VM
 * Created by ywd on 2017/11/13.
 */

public class WithholdSettingsVM extends BaseVM {
    private Context context;
    private ActivityWithholdSettingsBinding binding;
    public ObservableBoolean showSubItem = new ObservableBoolean(false);//是否显示子项设置

    public WithholdSettingsVM(final Context context, final ActivityWithholdSettingsBinding binding) {
        this.context = context;
        this.binding = binding;
        binding.tbOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.tbOpen.isChecked()) {
                    setWithholdSwitch("1");
                } else {
                    WithCancelDialog dialog = new WithCancelDialog(context, R.style.TelDialog, onBtnClickListener);
                    dialog.setCancelable(false);
                    dialog.setContent(AlaConfig.getResources().getString(R.string.withhold_settings_close_tip));
                    dialog.show();
                }
            }
        });
        binding.tbBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.tbBalance.isChecked()) {
                    setUseBalanceSwitch("1");
                } else {
                    setUseBalanceSwitch("0");
                }
            }
        });
        getWithholdInfo();
    }

    private WithCancelDialog.OnBtnClickListener onBtnClickListener = new WithCancelDialog.OnBtnClickListener() {
        @Override
        public void onCancelClick() {
            resetWithholdSwitch();
        }

        @Override
        public void onSureClick() {
            setWithholdSwitch("0");
        }
    };

    /**
     * 获取代扣设置
     */
    private void getWithholdInfo() {
        Call<WithholdSettingsModel> call = RDClient.getService(UserApi.class).getWithholdInfo();
        NetworkUtil.showCutscenes(context, call);
        call.enqueue(new RequestCallBack<WithholdSettingsModel>() {
            @Override
            public void onSuccess(Call<WithholdSettingsModel> call, Response<WithholdSettingsModel> response) {
                if (response != null) {
                    WithholdSettingsModel withholdSettingsModel = response.body();
                    if ("1".equals(withholdSettingsModel.getIsWithhold())) {
                        binding.tbOpen.setChecked(true);
                        showSubItem.set(true);
                    } else {
                        binding.tbOpen.setChecked(false);
                        showSubItem.set(false);
                    }

                    if ("1".equals(withholdSettingsModel.getUsebalance())) {
                        binding.tbBalance.setChecked(true);
                    } else {
                        binding.tbBalance.setChecked(false);
                    }
                }
            }
        });
    }

    /**
     * 代扣排序
     *
     * @param upLoadList
     */
    private void sortBankList(List<WithholdBankCardModel> upLoadList) {
        JSONObject reqObj = new JSONObject();
        for (int i = 0; i < upLoadList.size(); i++) {
            String cardNumber = upLoadList.get(i).getCardId();
            reqObj.put("card" + (i + 1), cardNumber);
        }
        Call<WithholdUseBalanceModel> call = RDClient.getService(UserApi.class).updateWithholdCard(reqObj);
        NetworkUtil.showCutscenes(context, call);
        call.enqueue(new RequestCallBack<WithholdUseBalanceModel>() {
            @Override
            public void onSuccess(Call<WithholdUseBalanceModel> call, Response<WithholdUseBalanceModel> response) {

            }
        });
    }

    /**
     * 开启/关闭代扣
     *
     * @param isSwitch 1:开启 0:关闭
     */
    private void setWithholdSwitch(final String isSwitch) {
        JSONObject reqObj = new JSONObject();
        reqObj.put("isSwitch", isSwitch);
        Call<WithholdSwitchModel> call = RDClient.getService(UserApi.class).updateWithholdSwitch(reqObj);
        NetworkUtil.showCutscenes(context, call);
        call.enqueue(new RequestCallBack<WithholdSwitchModel>() {
            @Override
            public void onSuccess(Call<WithholdSwitchModel> call, Response<WithholdSwitchModel> response) {
                if (response.body() != null) {
                    String status = response.body().getIsWithhold();
                    setWithholdStatus(status);
                } else {
                    resetWithholdSwitch();
                }
            }

            @Override
            public void onFailure(Call<WithholdSwitchModel> call, Throwable t) {
                super.onFailure(call, t);
                resetWithholdSwitch();
            }
        });
    }

    /**
     * 设置开关状态
     *
     * @param status
     */
    private void setWithholdStatus(String status) {
        if (MiscUtils.isNotEmpty(status)) {
            if (MiscUtils.isNotEmpty(status))
                if ("1".equals(status)) {
                    binding.tbOpen.setChecked(true);
                    showSubItem.set(true);
                } else {
                    binding.tbOpen.setChecked(false);
                    showSubItem.set(false);
                }
        } else {
            resetWithholdSwitch();
        }
    }

    /**
     * 重置代扣开关状态
     */
    private void resetWithholdSwitch() {
        if (binding.tbOpen.isChecked()) {
            binding.tbOpen.setChecked(false);
        } else {
            binding.tbOpen.setChecked(true);
        }
    }

    /**
     * 开启/关闭余额
     *
     * @param useBalance
     */
    private void setUseBalanceSwitch(String useBalance) {
        JSONObject reqObj = new JSONObject();
        reqObj.put("usebalance", useBalance);
        Call<WithholdUseBalanceModel> call = RDClient.getService(UserApi.class).updateWithholdCard(reqObj);
        NetworkUtil.showCutscenes(context, call);
        call.enqueue(new RequestCallBack<WithholdUseBalanceModel>() {
            @Override
            public void onSuccess(Call<WithholdUseBalanceModel> call, Response<WithholdUseBalanceModel> response) {
                if (response.body() != null) {
                    String status = response.body().getUsebalance();
                    setUseBalanceStatus(status);
                } else {
                    resetUseBalanceSwitch();
                }
            }

            @Override
            public void onFailure(Call<WithholdUseBalanceModel> call, Throwable t) {
                super.onFailure(call, t);
                resetUseBalanceSwitch();
            }
        });
    }

    /**
     * 设置余额开关状态
     *
     * @param status
     */
    private void setUseBalanceStatus(String status) {
        if (MiscUtils.isNotEmpty(status)) {
            if (MiscUtils.isNotEmpty(status))
                if ("1".equals(status)) {
                    binding.tbBalance.setChecked(true);
                } else {
                    binding.tbBalance.setChecked(false);
                }
        } else {
            resetUseBalanceSwitch();
        }
    }

    /**
     * 重置余额开关状态
     */
    private void resetUseBalanceSwitch() {
        if (binding.tbBalance.isChecked()) {
            binding.tbBalance.setChecked(false);
        } else {
            binding.tbBalance.setChecked(true);
        }
    }

    /**
     * 扣款顺序点击
     *
     * @param view
     */
    public void chargebackOrderClick(View view) {
        Intent intent = new Intent();
        ActivityUtils.push(WithholdCardActivity.class, intent, BundleKeys.REQUEST_CODE_WITHHOLD_SORT_BANK_CARD);
    }

    /**
     * 代扣说明点击
     *
     * @param view
     */
    public void onQuestionClick(View view) {
        final WithholdTipDialog.Builder builder = new WithholdTipDialog.Builder(context);
        builder
                .setContent(AlaConfig.getResources().getString(R.string.withhold_tip))
                .create()
                .show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BundleKeys.REQUEST_CODE_WITHHOLD_SORT_BANK_CARD) {
            if (data != null) {
                List<WithholdBankCardModel> list = (List<WithholdBankCardModel>) data.getSerializableExtra("sort_list");
                if (MiscUtils.isNotEmpty(list)) {
                    sortBankList(list);
                }
            }
        }
    }
}
