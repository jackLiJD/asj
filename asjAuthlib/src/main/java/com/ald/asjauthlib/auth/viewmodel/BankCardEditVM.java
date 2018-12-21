package com.ald.asjauthlib.auth.viewmodel;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.UserApi;
import com.ald.asjauthlib.dialog.PwdDialog;
import com.ald.asjauthlib.auth.model.BankCardModel;
import com.ald.asjauthlib.utils.AppUtils;
import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.utils.ModelEnum;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.network.NetworkUtil;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.network.RequestCallBack;
import com.ald.asjauthlib.authframework.core.network.entity.ApiResponse;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.authframework.core.utils.UIUtils;
import com.ald.asjauthlib.authframework.core.utils.encryption.MD5Util;
import com.ald.asjauthlib.authframework.core.vm.ViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 更换主副卡页面
 * Created by aladin on 2018/3/12.
 */

public class BankCardEditVM implements ViewModel {

    private Context context;
    private BankCardModel bankCardModel;
    private List<String> list0_2 = new ArrayList<>();
    private List<String> list3_5 = new ArrayList<>();
    private List<String> list6_7 = new ArrayList<>();
    private List<String> list8_9 = new ArrayList<>();

    public final ObservableField<String> displayImageUrl = new ObservableField<>();//银行卡图标
    public final ObservableField<String> displayTitle = new ObservableField<>();//银行名称
    public final ObservableField<String> displayCardType = new ObservableField<>();//银行卡类型
    public final ObservableField<String> displayCardNo = new ObservableField<>();//银行卡号
    public final ObservableField<Drawable> displayCardBg = new ObservableField<>();//银行卡背景色
    public final ObservableBoolean showSetMainBtn = new ObservableBoolean(false);//是否显示设置主卡按钮
    public final ObservableBoolean showMainTv = new ObservableBoolean(false);//是否显示主卡文案

    public BankCardEditVM(Context context) {
        this.context = context;
        Intent intent = ((Activity) context).getIntent();
        bankCardModel = intent.getParcelableExtra(BundleKeys.BANK_CARD_EDIT_MODEL);
        String[] arr0_2 = AlaConfig.getResources().getStringArray(R.array.bank_card_end_no_0_2);
        String[] arr3_5 = AlaConfig.getResources().getStringArray(R.array.bank_card_end_no_3_5);
        String[] arr6_7 = AlaConfig.getResources().getStringArray(R.array.bank_card_end_no_6_7);
        String[] arr8_9 = AlaConfig.getResources().getStringArray(R.array.bank_card_end_no_8_9);

        list0_2 = Arrays.asList(arr0_2);
        list3_5 = Arrays.asList(arr3_5);
        list6_7 = Arrays.asList(arr6_7);
        list8_9 = Arrays.asList(arr8_9);
        setBankBg(bankCardModel.getCardNumber());

        if (ModelEnum.Y.getModel().equals(bankCardModel.getIsMain())) {
            displayTitle.set(bankCardModel.getBankName());
            showMainTv.set(true);
            showSetMainBtn.set(false);
        } else {
            displayTitle.set(bankCardModel.getBankName());
            showMainTv.set(false);
            //信用卡不能设为主卡
            showSetMainBtn.set(ModelEnum.CREDIT.getValue() == bankCardModel.getCardType()?false:true);
        }

        displayCardType.set(ModelEnum.CREDIT.getValue() == bankCardModel.getCardType() ? AlaConfig.getResources().getString(R.string.bank_card_type2) : AlaConfig.getResources().getString(R.string.bank_card_type));
        displayImageUrl.set(bankCardModel.getBankIcon());
        displayCardNo.set(AppUtils.formatBankCardNo(bankCardModel.getCardNumber()));
    }

    /**
     * 设置银行卡背景
     *
     * @param cardNo 银行卡号
     */
    private void setBankBg(String cardNo) {
        if (MiscUtils.isEmpty(cardNo)) {
            displayCardBg.set(ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.bg_bank_car1));
            return;
        }

        String cardEnd = cardNo.substring(cardNo.length() - 1);
        if (list0_2.contains(cardEnd)) {
            displayCardBg.set(ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.ic_bank_bg_0_2));
            return;
        }
        if (list3_5.contains(cardEnd)) {
            displayCardBg.set(ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.ic_bank_bg_3_5));
            return;
        }
        if (list6_7.contains(cardEnd)) {
            displayCardBg.set(ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.ic_bank_bg_6_7));
            return;
        }
        if (list8_9.contains(cardEnd)) {
            displayCardBg.set(ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.ic_bank_bg_8_9));
            return;
        }
        displayCardBg.set(ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.bg_bank_car1));
    }

    /**
     * 设置主卡
     *
     * @param view view
     */
    public void setMainCardClick(View view) {
        if (bankCardModel == null) {
            UIUtils.showToast(AlaConfig.getResources().getString(R.string.toast_edit_main_card_bank_null));
            return;
        }
        showTipDialog(0);
    }

    /**
     * 解除绑定
     *
     * @param view view
     */
    public void removeCardClick(View view) {
        if (bankCardModel == null) {
            UIUtils.showToast(AlaConfig.getResources().getString(R.string.toast_edit_main_card_bank_null));
            return;
        }

        if (ModelEnum.Y.getModel().equals(bankCardModel.getIsMain())) {
            UIUtils.showToast(AlaConfig.getResources().getString(R.string.toast_edit_main_card_remove_main));
            return;
        }
        showTipDialog(1);

    }

    private void showTipDialog(final int type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("提示");
        if (0 == type) {
            builder.setMessage("是否将该卡设为主卡？");
        } else {
            builder.setMessage("是否解除绑定该卡？");
        }
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (0 == type) {
                    setMainCard();
                } else {
                    removeCard();
                }
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * 设置主卡
     */
    private void setMainCard() {
        PwdDialog.Builder builder = new PwdDialog.Builder(context);
        builder.setTitle("请输入支付密码");
        builder.setOnFinishListener(new PwdDialog.onFinishListener() {
            @Override
            public void onDone(String text) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("cardNumber", String.valueOf(bankCardModel.getCardNumber()));
                jsonObject.put("pwd", MD5Util.getMD5Str(text));
                Call<ApiResponse> call = RDClient.getService(UserApi.class).replaceMainCard(jsonObject);
                NetworkUtil.showCutscenes(context, call);
                call.enqueue(new RequestCallBack<ApiResponse>() {
                    @Override
                    public void onSuccess(Call<ApiResponse> call, final Response<ApiResponse> response) {
                        UIUtils.showToast(response.body().getMsg());
                        ActivityUtils.pop();
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        super.onFailure(call, t);
                    }
                });
            }
        });
        builder.create().show();
    }

    /**
     * 银行卡解绑
     */
    private void removeCard() {
        PwdDialog.Builder builder = new PwdDialog.Builder(context);
        builder.setTitle("请输入支付密码");
        builder.setOnFinishListener(new PwdDialog.onFinishListener() {
            @Override
            public void onDone(String text) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("bankId", String.valueOf(bankCardModel.getRid()));
                jsonObject.put("pwd", MD5Util.getMD5Str(text));
                Call<ApiResponse> call = RDClient.getService(UserApi.class).deleteBankCard(jsonObject);
                NetworkUtil.showCutscenes(context, call);
                call.enqueue(new RequestCallBack<ApiResponse>() {
                    @Override
                    public void onSuccess(Call<ApiResponse> call, final Response<ApiResponse> response) {
                        UIUtils.showToast(response.body().getMsg());
                        ActivityUtils.pop();
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        super.onFailure(call, t);
                    }
                });
            }
        });
        builder.create().show();
    }
}
