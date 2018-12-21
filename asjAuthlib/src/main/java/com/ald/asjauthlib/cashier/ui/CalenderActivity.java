package com.ald.asjauthlib.cashier.ui;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.cashier.viewmodel.CalenderVM;
import com.ald.asjauthlib.databinding.ActivityCalenderBinding;
import com.ald.asjauthlib.authframework.core.AlaTopBarActivity;

public class CalenderActivity extends AlaTopBarActivity<ActivityCalenderBinding> {

    CalenderVM calenderVM;

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_calender;
    }

    @Override
    protected void setViewModel() {
        calenderVM = new CalenderVM(this, cvb);
        setTitle(getResources().getString(R.string.calender_title));

    }


    @Override
    public String getStatName() {
        return getResources().getString(R.string.calender_title);
    }

    @Override
    public void onBackPressed() {
        calenderVM.setResult();
        super.onBackPressed();
    }
}
