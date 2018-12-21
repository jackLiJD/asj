package com.ald.asjauthlib.cashier.ui;

import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.cashier.viewmodel.BrandPaySuccessVM;
import com.ald.asjauthlib.databinding.ActivityBrandPaySuccessBinding;
import com.ald.asjauthlib.authframework.core.AlaTopBarActivity;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;

/**
 * 品牌支付成功页面
 * Created by yaowenda on 17/4/6.
 */

public class BrandPaySuccessActivity extends AlaTopBarActivity<ActivityBrandPaySuccessBinding> {

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_brand_pay_success;
    }

    @Override
    protected void setViewModel() {
        cvb.setViewModel(new BrandPaySuccessVM(this));
    }

    @Override
    public String getStatName() {
        return "品牌支付成功页面";
    }

    @Override
    protected void afterOnCreate() {
        super.afterOnCreate();
        setTitle(getResources().getString(R.string.brand_pay_success_title));
        setTitleColor(ContextCompat.getColor(AlaConfig.getContext(), R.color.text_important_color));
        setBackOption(false);
//        setLeftImage(0, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode != KeyEvent.KEYCODE_BACK && super.onKeyDown(keyCode, event);
    }

}
