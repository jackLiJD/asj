package com.ald.asjauthlib.auth.ui;


import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.viewmodel.AllLimitVM;
import com.ald.asjauthlib.databinding.ActivityAllLimitBinding;
import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.authframework.core.AlaTopBarActivity;

public class AllLimitActivity extends AlaTopBarActivity<ActivityAllLimitBinding> {


    AllLimitVM allLimitVM;
    public static boolean refreshFlag = false;


    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_all_limit;
    }

    @Override
    protected void setViewModel() {
        allLimitVM = new AllLimitVM(this, getIntent().getStringExtra(BundleKeys.INTENT_KEY_STAGE_REAL_NAME));
        cvb.setViewModel(allLimitVM);
    }

    @Override
    protected void afterOnCreate() {
        super.afterOnCreate();
        setTitle(getResources().getString(R.string.all_limit_title));
    }

    @Override
    public String getStatName() {
        return getResources().getString(R.string.all_limit_title);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (refreshFlag) {
            allLimitVM.load();
            refreshFlag = false;
        }
    }

}
