package com.ald.asjauthlib.auth.viewmodel;

import android.content.Context;
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
import com.ald.asjauthlib.auth.ui.BankCardEditActivity;
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
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/1/19 18:01
 * 描述：
 * 修订历史：
 */

public class BankCardItemVM implements ViewModel {
    public final ObservableField<String> displayTitle = new ObservableField<>();
    public final ObservableField<String> displayCardType = new ObservableField<>();
    public final ObservableField<String> displayCardNo = new ObservableField<>();
    public final ObservableField<String> displayImageUrl = new ObservableField<>();
    public final ObservableBoolean displayDelete = new ObservableBoolean(false);
    public final ObservableField<Drawable> displayCardBg = new ObservableField<>();//银行卡背景色
    public final ObservableBoolean isMain = new ObservableBoolean(false);
    //model
    public BankCardModel itemModel;
    //context
    private Context context;
    private DeleteListener listener;
    private List<String> list1 = new ArrayList<>();
    private List<String> list2 = new ArrayList<>();
//    private List<String> list6_7 = new ArrayList<>();
//    private List<String> list8_9 = new ArrayList<>();

    public BankCardItemVM(Context context, BankCardModel itemModel) {
        this.context = context;
        this.itemModel = itemModel;
        if (ModelEnum.Y.getModel().equals(itemModel.getIsMain())) {
            isMain.set(true);
            displayTitle.set(itemModel.getBankName());
            displayDelete.set(false);
        } else {
            displayDelete.set(true);
            displayTitle.set(itemModel.getBankName());
        }
        displayCardType.set(ModelEnum.CREDIT.getValue()==itemModel.getCardType()? AlaConfig.getResources().getString(R.string.bank_card_type2): AlaConfig.getResources().getString(R.string.bank_card_type));
        displayImageUrl.set(itemModel.getBankIcon());
        displayCardNo.set(AppUtils.formatBankCardNo(itemModel.getCardNumber()));

        String[] arr1 = context.getResources().getStringArray(R.array.bank_card_code_1);
        String[] arr2 = context.getResources().getStringArray(R.array.bank_card_code_2);



        list1 = Arrays.asList(arr1);
        list2 = Arrays.asList(arr2);

        setBankBg(itemModel.getCardNumber(),itemModel.getBankCode());
    }

    /**
     * 设置银行卡背景
     *
     * @param cardNo 银行卡号
     *    根据银行名称，显示不同卡片背景色
     *    红   工商银行、中国银行、招商银行、平安银行、中信银行、光大银行、广发银行  other
     *    蓝   建设银行、交通银行、兴业银行、上发浦发银行  list2
     *    绿   农业银行、邮储银行、民生银行、杭州银行   list1
     *    version 4.2.8
     */
    private void setBankBg(String cardNo,String bankCode) {
        if (MiscUtils.isEmpty(cardNo)) {
            displayCardBg.set(ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.bg_bank_car1));
            return;
        }

        if (list1.contains(bankCode)) {
            displayCardBg.set(ContextCompat.getDrawable(context, R.drawable.card_green));
            return;
        }
        if (list2.contains(bankCode)) {
            displayCardBg.set(ContextCompat.getDrawable(context, R.drawable.card_blue));
            return;
        }

        displayCardBg.set(ContextCompat.getDrawable(context, R.drawable.card_red));
    }

    public void setListener(DeleteListener listener) {
        this.listener = listener;
    }

    /**
     * 银行卡点击
     *
     * @param view view
     */
    public void itemClick(View view) {
        if (itemModel == null) {
            UIUtils.showToast(AlaConfig.getResources().getString(R.string.toast_edit_main_card_bank_null));
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(BundleKeys.BANK_CARD_EDIT_MODEL, itemModel);
        ActivityUtils.push(BankCardEditActivity.class, intent);
    }

    /**
     * 页面点击
     *
     * @param view view
     */
    public void deleteClick(final View view) {
        PwdDialog.Builder builder = new PwdDialog.Builder(context);
        builder.setTitle("请输入支付密码");
        builder.setOnFinishListener(new PwdDialog.onFinishListener() {
            @Override
            public void onDone(String text) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("bankId", String.valueOf(itemModel.getRid()));
                jsonObject.put("pwd", MD5Util.getMD5Str(text));
                Call<ApiResponse> call = RDClient.getService(UserApi.class).deleteBankCard(jsonObject);
                NetworkUtil.showCutscenes(context, call);
                call.enqueue(new RequestCallBack<ApiResponse>() {
                    @Override
                    public void onSuccess(Call<ApiResponse> call, final Response<ApiResponse> response) {
                        UIUtils.showToast(response.body().getMsg());
                        if (listener != null) {
                            listener.delete(BankCardItemVM.this);
                        }
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


    interface DeleteListener {
        void delete(BankCardItemVM itemVM);
    }
}
