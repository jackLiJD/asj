package com.ald.asjauthlib.auth.viewmodel;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.View;

import com.ald.asjauthlib.BR;
import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.UserApi;
import com.ald.asjauthlib.dialog.CreditPromoteDialog;
import com.ald.asjauthlib.auth.model.BankCardModel;
import com.ald.asjauthlib.auth.model.BankListModel;
import com.ald.asjauthlib.auth.ui.BankCardAddIdActivity;
import com.ald.asjauthlib.auth.ui.RRIdAuthActivity;
import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.utils.ModelEnum;
import com.ald.asjauthlib.utils.StageJumpEnum;
import com.ald.asjauthlib.web.HTML5WebView;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.network.NetworkUtil;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.network.RequestCallBack;
import com.ald.asjauthlib.authframework.core.ui.DividerLine;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.BASE64Encoder;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.authframework.core.utils.SPUtil;
import com.ald.asjauthlib.authframework.core.vm.BaseRecyclerViewVM;

import com.ald.asjauthlib.tatarka.bindingcollectionadapter.ItemView;
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
public class BankCardListVM extends BaseRecyclerViewVM<BankCardItemVM> {
    private Activity context;
    private BankListModel bankListModel;

    public BankCardListVM(Activity context) {
        this.context = context;
        this.type = DividerLine.DEFAULT;

    }

    @Override
    public void selectView(ItemView itemView, int position, BankCardItemVM itemVM) {
        itemView.set(BR.viewModel, R.layout.list_item_bank_card_list);
    }

    public void load() {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("cardType","3");
        Call<BankListModel> call = RDClient.getService(UserApi.class).getBankCardList(jsonObject);
        NetworkUtil.showCutscenes(context, call);
        call.enqueue(new RequestCallBack<BankListModel>() {
            @Override
            public void onSuccess(Call<BankListModel> call, final Response<BankListModel> response) {
                items.clear();
                bankListModel = response.body();
                if (bankListModel == null || MiscUtils.isEmpty(bankListModel.getBankCardList())) {
                    modelState.setNoData();
                } else {
                    modelState.setNoData(false);
                    for (BankCardModel bankCardModel : bankListModel.getBankCardList()) {
                        BankCardItemVM bankCardItemVm = new BankCardItemVM(context, bankCardModel);
                        bankCardItemVm.setListener(new BankCardItemVM.DeleteListener() {
                            @Override
                            public void delete(BankCardItemVM itemVM) {
                                load();
                            }
                        });
                        items.add(bankCardItemVm);
                    }
                }
            }
        });

    }


    /**
     * 添加银行卡
     */
    public void toAddBankcardClick(View view) {
        if (bankListModel != null) {
            Intent intent = new Intent();
            intent.putExtra(BundleKeys.BANK_CARD_NAME, bankListModel.getRealName());
            if (MiscUtils.isNotEmpty(bankListModel.getIdNumber())) {
                intent.putExtra(BundleKeys.SETTING_PAY_ID_NUMBER, BASE64Encoder.
                        decodeString(bankListModel.getIdNumber()));
            }
            intent.putExtra(BundleKeys.STAGE_JUMP, StageJumpEnum.STAGE_BANK_CARD.getModel());
            ActivityUtils.push(BankCardAddIdActivity.class, intent);
        }
    }


    /**
     * 申请信用卡
     */
    public void toApplyCreditCard(View view) {
        if (bankListModel != null) {
            Intent intent = new Intent();
            intent.putExtra(HTML5WebView.INTENT_BASE_URL, SPUtil.getString("APPLY_CREDIT_CARD",""));
            ActivityUtils.push(HTML5WebView.class, intent);
        }
    }

    /**
     * 提示用户绑卡
     */
    private void showTipDialog() {
        CreditPromoteDialog dialog = new CreditPromoteDialog(context);
        dialog.setContent(AlaConfig.getResources().getString(R.string.card_list_credit_promote));
        dialog.setListener(new CreditPromoteDialog.MakeSureListener() {
            @Override
            public void onSureClick(Dialog dialog, View view) {
                Intent intent = new Intent();
                if (ModelEnum.N.getModel().equals(bankListModel.getFaceStatus())) {
                    intent.putExtra(BundleKeys.STAGE_JUMP, StageJumpEnum.STAGE_BANK_CARD.getModel());
                    ActivityUtils.push(RRIdAuthActivity.class, intent);
                    return;
                }
                if (ModelEnum.N.getModel().equals(bankListModel.getBankcardStatus())) {
                    intent.putExtra(BundleKeys.BANK_CARD_NAME, bankListModel.getRealName());
                    if (MiscUtils.isNotEmpty(bankListModel.getIdNumber())) {
                        intent.putExtra(BundleKeys.SETTING_PAY_ID_NUMBER, BASE64Encoder.decodeString(bankListModel.getIdNumber()));
                    }
                    intent.putExtra(BundleKeys.STAGE_JUMP, StageJumpEnum.STAGE_BANK_CARD.getModel());
                    ActivityUtils.push(BankCardAddIdActivity.class, intent);
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}


