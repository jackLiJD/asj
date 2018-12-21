package com.ald.asjauthlib.cashier.ui;

import android.content.Intent;
import android.view.View;

import com.ald.asjauthlib.utils.Constant;
import com.ald.asjauthlib.R;
import com.ald.asjauthlib.cashier.viewmodel.SettleAdvancedVM;
import com.ald.asjauthlib.databinding.ActivitySettleAdvancedBinding;
import com.ald.asjauthlib.web.HTML5WebView;
import com.ald.asjauthlib.authframework.core.AlaTopBarActivity;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;

public class SettleAdvancedActivity extends AlaTopBarActivity<ActivitySettleAdvancedBinding> {

    SettleAdvancedVM settleAdvancedVM;

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_settle_advanced;
    }

    @Override
    protected void setViewModel() {
        settleAdvancedVM = new SettleAdvancedVM(this, cvb);
        cvb.setViewModel(settleAdvancedVM);

    }

    @Override
    protected void afterOnCreate() {
        super.afterOnCreate();
        setTitle(getResources().getString(R.string.settle_advanced_title));
        setRightText(getResources().getString(R.string.settle_advanced_title_what) + "？", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(HTML5WebView.INTENT_BASE_URL, AlaConfig.getServerProvider().getAppServer() + Constant.H5_URL_AHEAD_RETURN);
                intent.putExtra(HTML5WebView.INTENT_TITLE, getResources().getString(R.string.settle_advanced_title_what));
                ActivityUtils.push(HTML5WebView.class, intent);
            }
        });
    }

    @Override
    public String getStatName() {
        return getResources().getString(R.string.settle_advanced_title);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (settleAdvancedVM != null) {
            settleAdvancedVM.onActivityResult(requestCode, resultCode, data);
        }
    }
}
