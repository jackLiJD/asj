package com.ald.asjauthlib.cashier.viewmodel;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.view.View;
import android.widget.CheckBox;

import com.ald.asjauthlib.cashier.model.ItemDataPair;
import com.ald.asjauthlib.cashier.model.SettleAdvancedModel;
import com.ald.asjauthlib.dialog.SettleAdvancedBottomDialog;
import com.ald.asjauthlib.utils.AppUtils;
import com.ald.asjauthlib.authframework.core.utils.CanDoubleClick;
import com.ald.asjauthlib.authframework.core.vm.BaseVM;

/*
 * Created by luckyliang on 2017/12/15.
 */

public class ItemSettleAdvancedVM extends BaseVM {

    public final ObservableField<String> displayName = new ObservableField<>();
    public final ObservableField<String> displayStageInfo = new ObservableField<>();
    public final ObservableField<String> displayAmount = new ObservableField<>();
    public final ObservableBoolean isChecked = new ObservableBoolean();

    public ItemDataPair itemDataPair;
    private SettleAdvancedModel.Bill bill;
    Context context;
    String amount;
    private String charge;
    private OnClickLister onClickLister;
    int position;

    /**
     * @param amount   选定总金额
     * @param charge   可减免手续费
     * @param position 位置
     */
    ItemSettleAdvancedVM(Context context, ItemDataPair itemDataPair, boolean checked, String amount, String charge,
                         int position, OnClickLister onClickLister) {
        this.context = context;
        this.itemDataPair = itemDataPair;
        bill = (SettleAdvancedModel.Bill) itemDataPair.getData();
        displayName.set(bill.getTitle());
        displayStageInfo.set(bill.getStartNper() + "/" + bill.getNper() + "期~" + bill.getEndNper() + "/" + bill.getNper() + "期");
        displayAmount.set(bill.getAmount().toString() + "元");
        isChecked.set(checked);
        this.amount = amount;
        this.charge = charge;
        this.onClickLister = onClickLister;
        this.position = position;
    }

    public void itemOnClick(View view) {
        //打开底部弹框
        SettleAdvancedBottomDialog.Builder builder = new SettleAdvancedBottomDialog.Builder(context);
        builder.setData(bill.getDetailList(), 0)
                .setAmount(AppUtils.formatAmount(bill.getAmount()) + "元")
                .setCharge(charge + "元")
                .setSureClickListener(new SettleAdvancedBottomDialog.Builder.OnSureClickListener() {
                    @Override
                    public void onClick() {
                        onClickLister.onCheckClick(ItemSettleAdvancedVM.this, true);

                    }
                }).create().show();
    }

    /**
     * checker点击 单选
     */
    @CanDoubleClick
    public void onCheckerClick(View view) {

//        if (((CheckBox) view).isChecked()) {
        onClickLister.onCheckClick(this, ((CheckBox) view).isChecked());
//        }

    }


    public interface OnClickLister {

        void onCheckClick(ItemSettleAdvancedVM itemChecked, boolean isChecked);
    }
}
