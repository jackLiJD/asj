package com.ald.asjauthlib.cashier.viewmodel;

/*
 * Created by liangchen on 2018/3/2.
 * 收银台分期列表VM
 */

import android.content.Context;

import com.ald.asjauthlib.BR;
import com.ald.asjauthlib.R;
import com.ald.asjauthlib.cashier.model.CashierNperListModel;
import com.ald.asjauthlib.cashier.model.ItemDataPair;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.authframework.core.vm.BaseRecyclerViewVM;

import java.util.List;

import com.ald.asjauthlib.tatarka.bindingcollectionadapter.ItemView;

public class CashierPerListVM extends BaseRecyclerViewVM<ItemCashierPerVM> {

    private Context context;
    private List<CashierNperListModel.NperModel> nperList;
    private SelectPaymentVM selectPaymentVM;

    CashierPerListVM(Context context, SelectPaymentVM selectPaymentVM) {
        this.context = context;
        this.selectPaymentVM = selectPaymentVM;
    }

    public ItemCashierPerVM load(final List<CashierNperListModel.NperModel> nperList, final int selectedIndex, final onItemClickListener onItemClickListener) {
        if (this.nperList == null)
            this.nperList = nperList;
        items.clear();

        for (int i = 0; i < nperList.size(); i++) {
            final ItemDataPair itemDataPair = new ItemDataPair(nperList.get(i), 0);
            final ItemCashierPerVM itemCashierPerVM = new ItemCashierPerVM(selectPaymentVM, itemDataPair, context, i == selectedIndex, i, selectedItem -> {
                if (selectedItem != null) {
                    selectPaymentVM.displayNperDetail.set(selectedItem.getNperDetail());
                    selectedItem.isSelected = true;
                    selectedItem.itembg.set(AlaConfig.getResources().getDrawable(R.drawable.frame_cashier_per_item_select));
                    items.set(selectedItem.index, selectedItem);

                    onItemClickListener.onClick(selectedItem, selectedItem.getIndex());
                }
            });
            items.add(itemCashierPerVM);
        }
        if (items.size() > 0)
            return items.get(0);
        else return null;
    }


    boolean hasNperList() {
        return MiscUtils.isNotEmpty(nperList);
    }

    @Override
    protected void selectView(ItemView itemView, int position, ItemCashierPerVM item) {
        itemView.set(BR.viewModel, R.layout.item_cashier_nper_list);
    }

    interface onItemClickListener {
        void onClick(ItemCashierPerVM itemCashierPerVM, int index);

    }

    public ItemCashierPerVM getItem(int index) {
        return items.get(index);
    }

}
