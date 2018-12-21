package com.ald.asjauthlib.cashier.viewmodel;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.cashier.model.CashierNperListModel;
import com.ald.asjauthlib.cashier.model.ItemDataPair;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.utils.CanDoubleClick;
import com.ald.asjauthlib.authframework.core.vm.BaseVM;

/*
 * Created by liangchen on 2018/3/2.
 */

public class ItemCashierPerVM extends BaseVM {

    public ObservableBoolean showFreeSign = new ObservableBoolean(false);

    public ObservableBoolean showDtl = new ObservableBoolean(true);//如果全部免息则不显示 否则显示
    public ObservableField<String> includeFee = new ObservableField<>();//含利息手续费
    public ObservableField<Drawable> itembg = new ObservableField<>();

    public ObservableField<String> per = new ObservableField<>();//期数
    public ObservableField<String> displayPerAmount = new ObservableField<>();//月供 ¥183.73/ 前3月供(免息)
    public ObservableField<String> displayOtherPerAmount = new ObservableField<>();//其他月供
    public ObservableField<String> displayFreeNper = new ObservableField<>();

    public final ObservableInt nperColor = new ObservableInt(0);
    public final ObservableBoolean showOtherPerAmount = new ObservableBoolean(false);//是否显示其他月供
    ObservableInt nPer = new ObservableInt(1);
    boolean isSelected = false;
    private OnItemSelectedListener onItemSelectedListener;
    public int index;
    private CashierNperListModel.NperModel nperModel;
    private Context context;
    private String nperDetail;


    public CashierNperListModel.NperModel getModel() {
        return nperModel;
    }

    /**
     * @param isSelected 是否默认选中
     */
    ItemCashierPerVM(SelectPaymentVM selectPaymentVM, ItemDataPair itemDataPair, Context context, boolean isSelected, int index, OnItemSelectedListener onItemSelectedListener) {
        this.index = index;
        this.context = context;
        this.isSelected = isSelected;
        this.onItemSelectedListener = onItemSelectedListener;

        nperModel = (CashierNperListModel.NperModel) itemDataPair.getData();
        nPer.set(nperModel.getNper());//期数
        per.set(nPer.get() + "期");
        switch (nperModel.getIsFree()) {

            case "1"://全免
                showFreeSign.set(true);
                displayFreeNper.set("免息");
                displayPerAmount.set("月供¥" + nperModel.getNperDetailList().get(0).getAmount());//月供 前3月供
                showOtherPerAmount.set(false);
                nperDetail = "¥" + nperModel.getNperDetailList().get(0).getAmount();
                break;
            case "2": //部分免息
                displayFreeNper.set("前" + nperModel.getFreeNper() + "期免息");
                displayPerAmount.set("前" + nperModel.getFreeNper() + "月供¥" + nperModel.getNperDetailList().get(0).getAmount());// 前3月供
                displayOtherPerAmount.set("其他月供¥" + nperModel.getNperDetailList().get(Integer.parseInt(nperModel.getFreeNper())).getMonthAmount());//其他月供
                showOtherPerAmount.set(true);
                showFreeSign.set(true);
                nperDetail = "前" + nperModel.getFreeNper() + "月¥" + nperModel.getNperDetailList().get(0).getAmount()
                        + "其他月¥" + nperModel.getNperDetailList().get(Integer.parseInt(nperModel.getFreeNper())).getMonthAmount();
                break;
            case "0":
                //非免息
                showFreeSign.set(false);
                displayPerAmount.set("月供¥" + nperModel.getNperDetailList().get(0).getMonthAmount());
                nperDetail = "¥" + nperModel.getNperDetailList().get(0).getMonthAmount();
                showOtherPerAmount.set(false);
                break;
        }
        if (index == 0)
            selectPaymentVM.displayNperDetail.set(nperDetail);
        switchLayout(isSelected);
    }

    @CanDoubleClick
    public void onItemClick(View view) {
        if (!isSelected) {
            onItemSelectedListener.onClick(this);
        }

    }

    public int getIndex() {
        return index;
    }

    interface OnItemSelectedListener {
        void onClick(ItemCashierPerVM itemCashierPerVM);
    }

    public void switchLayout(boolean isSelected) {
        this.isSelected = isSelected;
        nperColor.set(AlaConfig.getResources().getColor(isSelected ? R.color.colorPrimaryNew : R.color.color_2e2e2e));
        itembg.set(AlaConfig.getResources().getDrawable(isSelected ? R.drawable.frame_cashier_per_item_select : R.drawable.frame_cashier_per_item_defult));

    }

    public String getNperDetail() {
        return nperDetail;
    }

}

