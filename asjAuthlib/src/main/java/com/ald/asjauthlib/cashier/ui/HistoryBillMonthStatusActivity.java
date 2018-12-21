package com.ald.asjauthlib.cashier.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.R;
import com.ald.asjauthlib.cashier.viewmodel.HistoryBillMonthDtlVM;
import com.ald.asjauthlib.databinding.ActivityHistoryBillMonthStatusBinding;
import com.ald.asjauthlib.authframework.core.AlaTopBarActivity;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;

public class HistoryBillMonthStatusActivity extends AlaTopBarActivity<ActivityHistoryBillMonthStatusBinding> {


    HistoryBillMonthDtlVM historyBillMonthDtlVM;

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_history_bill_month_status;
    }

    @Override
    protected void setViewModel() {
        String year = getIntent().getStringExtra(BundleKeys.INTENT_KEY_HISTORY_BILL_YEAR);
        String month = getIntent().getStringExtra(BundleKeys.INTENT_KEY_HISTORY_BILL_MONTH);
        historyBillMonthDtlVM = new HistoryBillMonthDtlVM(this, year, month);
        cvb.setViewModel(historyBillMonthDtlVM);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.history_bill_month_title));
        setRightText(getString(R.string.history_bill_title_right_txt), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(BundleKeys.STAGE_JUMP, getIntent().getStringExtra(BundleKeys.STAGE_JUMP));
                ActivityUtils.push(RefundRecordActivity.class, intent);
            }
        });
    }

    @Override
    public String getStatName() {
        return getString(R.string.history_bill_month_title);
    }
}
