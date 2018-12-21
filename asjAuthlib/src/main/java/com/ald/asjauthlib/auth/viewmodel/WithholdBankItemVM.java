package com.ald.asjauthlib.auth.viewmodel;

import android.content.Context;
import android.databinding.ObservableField;

import com.ald.asjauthlib.auth.model.WithholdBankCardModel;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.authframework.core.vm.ViewModel;

/**
 * 代扣银行卡列表itemVM
 * Created by ywd on 2017/11/13.
 */

public class WithholdBankItemVM implements ViewModel {
    private Context context;
    private WithholdBankCardModel bankCardModel;

    public ObservableField<String> bankName=new ObservableField<>();

    public WithholdBankItemVM(Context context, WithholdBankCardModel bankCardModel) {
        this.context = context;
        this.bankCardModel = bankCardModel;
        if (bankCardModel!=null && MiscUtils.isNotEmpty(bankCardModel.getCard())) {
            bankName.set(bankCardModel.getCard());
        }
    }
}
