package com.ald.asjauthlib.cashier.viewmodel;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.annotation.ColorInt;
import android.view.View;

import com.ald.asjauthlib.authframework.core.vm.ViewModel;

/**
 * Created by wjy on 2017/12/18.
 */

public class BillDtlMonthItemVM implements ViewModel {

    public final ObservableField<String> monthText = new ObservableField<>();
    public final ObservableInt monthWidth = new ObservableInt();
    public final ObservableInt monthColor = new ObservableInt();

    private OnMonthItemListener listener;

    public BillDtlMonthItemVM(int month, float monthWidth, OnMonthItemListener listener) {
        monthText.set(month + "月");
        this.monthWidth.set((int) monthWidth);
        this.listener = listener;
    }

    public void setMonthColor(@ColorInt int color) {
        monthColor.set(color);
    }

    public void monthClick(View view) {
        if (null != listener) listener.onMonthClickListener(this, monthText.get().substring(0, monthText.get().length() - 1));
    }

    public interface OnMonthItemListener {

        void onMonthClickListener(BillDtlMonthItemVM selectItemVM, String billMonth);

    }
}
