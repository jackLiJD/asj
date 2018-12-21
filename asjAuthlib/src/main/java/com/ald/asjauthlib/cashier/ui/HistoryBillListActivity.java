package com.ald.asjauthlib.cashier.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.R;
import com.ald.asjauthlib.cashier.viewmodel.HistoryBillListVM;
import com.ald.asjauthlib.databinding.ActivityHistoryBillListBinding;
import com.ald.asjauthlib.authframework.core.AlaTopBarActivity;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;

public class HistoryBillListActivity extends AlaTopBarActivity<ActivityHistoryBillListBinding> {

    HistoryBillListVM historyBillListVM;

//    private PtrFrameLayout ptrFrameLayout;

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_history_bill_list;
    }


    @Override
    protected void setViewModel() {
        historyBillListVM = new HistoryBillListVM(this);
        cvb.setViewModel(historyBillListVM);
//        ptrFrameLayout = cvb.ptrFrame;
//        final CommonRefreshHeader header = new CommonRefreshHeader(AlaConfig.getContext());
//        ptrFrameLayout.setHeaderView(header);
//        ptrFrameLayout.addPtrUIHandler(header);
//        ptrFrameLayout.disableWhenHorizontalMove(true);
//        ptrFrameLayout.setPtrHandler(new PtrHandler() {
//            @Override
//            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
//                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
//            }
//
//            @Override
//            public void onRefreshBegin(PtrFrameLayout frame) {
//                if (historyBillListVM != null) {
//                    historyBillListVM.load(frame);
//                }
//            }
//        });
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.history_bill_title));
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
        return getString(R.string.history_bill_title);
    }
}
