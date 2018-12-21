package com.ald.asjauthlib.cashier.viewmodel;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.view.View;

/**
 * Created by wjy on 2018/3/7.
 */

public abstract class BillsVM {

    public final ObservableInt adWidth = new ObservableInt();
    public final ObservableInt adHeight = new ObservableInt();
    public final ObservableInt paddingRight = new ObservableInt();
    public final ObservableInt paddingBottom = new ObservableInt();
    public final ObservableInt paddingLeft = new ObservableInt();
    public final ObservableField<String> adImage = new ObservableField<>();

    public void adClick(View view) {
        adClickListener(view);
    }

    public abstract void adClickListener(View view);
}
