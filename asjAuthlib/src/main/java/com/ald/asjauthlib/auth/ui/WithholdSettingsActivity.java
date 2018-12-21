package com.ald.asjauthlib.auth.ui;

import android.content.Intent;
import android.support.v4.content.ContextCompat;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.viewmodel.WithholdSettingsVM;
import com.ald.asjauthlib.databinding.ActivityWithholdSettingsBinding;
import com.ald.asjauthlib.authframework.core.AlaTopBarActivity;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;

/**
 * 代扣设置页面
 * Created by ywd on 2017/11/13.
 */

public class WithholdSettingsActivity extends AlaTopBarActivity<ActivityWithholdSettingsBinding> {
    private WithholdSettingsVM withholdSettingsVM;

    @Override
    public String getStatName() {
        return "代扣设置页面";
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_withhold_settings;
    }

    @Override
    protected void setViewModel() {
        withholdSettingsVM = new WithholdSettingsVM(this, cvb);
        cvb.setViewModel(withholdSettingsVM);
    }

    @Override
    protected void afterOnCreate() {
        super.afterOnCreate();
        setTitle(getResources().getString(R.string.withhold_settings_title));
        setTitleColor(ContextCompat.getColor(AlaConfig.getContext(),R.color.text_important_color));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        withholdSettingsVM.onActivityResult(requestCode,resultCode,data);
    }
}
