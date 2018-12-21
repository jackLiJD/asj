package com.ald.asjauthlib.auth.ui;

import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.viewmodel.WithholdCardVM;
import com.ald.asjauthlib.databinding.ActivityWithholdCardBinding;
import com.ald.asjauthlib.authframework.core.AlaTopBarActivity;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;

/**
 * 代扣排序页面
 * Created by ywd on 2017/11/13.
 */

public class WithholdCardActivity extends AlaTopBarActivity<ActivityWithholdCardBinding> {
    private WithholdCardVM withholdCardVM;

    @Override
    public String getStatName() {
        return "代扣顺序页面";
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_withhold_card;
    }

    @Override
    protected void setViewModel() {
        withholdCardVM = new WithholdCardVM(this, cvb);
        cvb.setViewModel(withholdCardVM);
    }

    @Override
    protected void afterOnCreate() {
        super.afterOnCreate();
        setTitle(getResources().getString(R.string.withhold_card_title));
        setTitleColor(ContextCompat.getColor(AlaConfig.getContext(),R.color.text_important_color));
        setLeftImageListener(v -> withholdCardVM.onBackPressed());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            withholdCardVM.onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
