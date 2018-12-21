package com.ald.asjauthlib.auth.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.view.View;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.model.BankCardTypeModel;
import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.utils.ModelEnum;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.vm.ViewModel;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/1/19 18:01
 * 描述：
 * 修订历史：
 */

public class BankCardTypeItemVM implements ViewModel {
    //context
    private Activity context;
    //model
    public BankCardTypeModel itemModel;
    public final ObservableBoolean state = new ObservableBoolean(false);
    public final ObservableField<String> imgUrl=new ObservableField<>();
    public final ObservableField<String> displayTitle = new ObservableField<>();
    public final ObservableField<String> displayType = new ObservableField<>();

    public BankCardTypeItemVM(Activity context, BankCardTypeModel itemModel) {
        this.context = context;
        this.itemModel = itemModel;
        imgUrl.set(itemModel.getBankIcon());
        displayTitle.set(itemModel.getBankName());
        if(itemModel.isSelect()){
            state.set(true);
        }else {
            state.set(false);
        }
        if(itemModel.getCardType()== ModelEnum.CREDIT.getValue()){
            displayType.set(AlaConfig.getResources().getString(R.string.bank_card_type2));
        }else {
            displayType.set(AlaConfig.getResources().getString(R.string.bank_card_type));
        }
    }

    /**
     * 页面点击
     * @param view
     */
    public void itemClick(View view){
        itemModel.setSelect(true);
        if(itemModel.isSelect()){
            state.set(true);
        }else {
            state.set(false);
        }
        Intent data = new Intent();
        data.putExtra(BundleKeys.BANK_CARD_RESULT, itemModel);
        context.setResult(Activity.RESULT_OK,data);
        context.finish();
    }

}
