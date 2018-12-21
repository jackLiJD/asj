package com.ald.asjauthlib.cashier.viewmodel;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.cashier.model.CashierModel;
import com.ald.asjauthlib.cashier.model.ItemDataPair;
import com.ald.asjauthlib.utils.ModelEnum;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.utils.CanDoubleClick;
import com.ald.asjauthlib.authframework.core.vm.BaseVM;

/*
 * Created by liangchen on 2018/3/1.
 * 收银台银行卡列表项
 */

public class ItemBankVM extends BaseVM {

    static final int BANK_ITEM_CARD = 0;   // 普通银行卡
    static final int BANK_ITEM_ALI = 1;  //支付宝
    static final int BANK_ITEM_ADD = 3;   // 添加银行卡

    private ItemDataPair itemDataPair;
    private OnItemClickListener listener;

    public final ObservableField<String> displayBankName = new ObservableField<>();//银行名称
    public final ObservableField<String> displayTip = new ObservableField<>();
    public final ObservableBoolean showDisplayTip = new ObservableBoolean(false);
    public final ObservableField<String> displayIc = new ObservableField<>();
    public final ObservableField<Drawable> displayLocalIc = new ObservableField<>();
    public final ObservableBoolean isMaintain = new ObservableBoolean(false);//是否维护中
    public final ObservableBoolean isChecked = new ObservableBoolean(false);//是否选中
    public final ObservableField<Drawable> checkerBg = new ObservableField<>();//选择框背景图
    public final ObservableBoolean showCreditCharge = new ObservableBoolean(false);//是否显示信用卡手续费
    public final ObservableField<String> displayCreditCharge = new ObservableField<>();//信用卡手续费

    private int position;

    /**
     */
    ItemBankVM(ItemDataPair itemDataPair, Context context, int position, OnItemClickListener listener) {
        this.listener = listener;
        this.itemDataPair = itemDataPair;
        this.position = position;
        this.isChecked.set(false);
        checkerBg.set(AlaConfig.getResources().getDrawable(R.drawable.ic_cashier_unchecked));
        CashierModel.BankCardModel bankCardModel = (CashierModel.BankCardModel) itemDataPair.getData();
        if (itemDataPair.getItemType() == BANK_ITEM_CARD) {
            String cardNumber = bankCardModel.getCardNumber();
            int startIndex = cardNumber.length() - 4;
            int endIndex = cardNumber.length();
            String cardType = "";
            if (bankCardModel.getCardType() == ModelEnum.CREDIT.getValue()) {
                cardType = AlaConfig.getResources().getString(R.string.bank_card_type2);
                if (bankCardModel.getCreditRate() > 0) {
                    showCreditCharge.set(true);
                    displayCreditCharge.set("手续费:" + (double) bankCardModel.getCreditRate() / 100 + "%");
                } else {
                    showCreditCharge.set(false);
                }
            } else
                showCreditCharge.set(false);
            displayBankName.set(bankCardModel.getBankName() + cardType + "(" + cardNumber.substring(startIndex, endIndex) + ")");
            if (bankCardModel.getIsValid() != null
                    && bankCardModel.getIsValid().equals("Y")) {

                showDisplayTip.set(false);

                displayTip.set("单笔限额:" + bankCardModel.getBankStatus().getLimitUp().intValue()
                        + " 单日限额:" + bankCardModel.getBankStatus().getDailyLimit().intValue());
                isMaintain.set(false);
            } else {

                showDisplayTip.set(false);

                displayTip.set("银行维护中，请使用其他银行，谢谢！");
                isMaintain.set(true);
            }
            displayIc.set(bankCardModel.getBankIcon());
        } else if (itemDataPair.getItemType() != BANK_ITEM_ADD) {
            displayBankName.set(bankCardModel.getBankName());
            if (bankCardModel.getBankName().equals("支付宝")) {
                displayLocalIc.set(AlaConfig.getResources().getDrawable(R.drawable.ic_ali_pay));
            }
        }
    }

    interface OnItemClickListener {
        void onClick(ItemBankVM itemBankVM);
    }

    @CanDoubleClick
    public void onItemClick(View view) {
        if (type != BANK_ITEM_CARD || !isMaintain.get()) {
            isChecked.set(true);
            listener.onClick(this);
        }

    }

    public int getPosition() {
        return position;
    }

    public ItemDataPair getItemDataPair() {
        return itemDataPair;
    }
}
