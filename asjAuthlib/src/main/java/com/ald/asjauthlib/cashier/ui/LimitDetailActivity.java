package com.ald.asjauthlib.cashier.ui;

import android.support.v4.content.ContextCompat;

import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.R;
import com.ald.asjauthlib.cashier.viewmodel.LimitDetailVM;
import com.ald.asjauthlib.databinding.ActivityLimitDetailBinding;
import com.ald.asjauthlib.utils.ModelEnum;
import com.ald.asjauthlib.authframework.core.AlaTopBarActivity;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/1/9
 * 描述：
 * 修订历史：
 */
public class LimitDetailActivity extends AlaTopBarActivity<ActivityLimitDetailBinding> {

    @Override
    public String getStatName() {
        return "借还款详情页面";
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_limit_detail;
    }

    @Override
    protected void setViewModel() {
        cvb.setViewModel(new LimitDetailVM(this));
    }

    @Override
    protected void afterOnCreate() {
        super.afterOnCreate();
        setTitleColor(ContextCompat.getColor(AlaConfig.getContext(), R.color.text_important_color));
        String limitType = getIntent().getStringExtra(BundleKeys.LIMIT_TYPE);
        if (ModelEnum.BILL_TYPE_REPAYMENT.getModel().equals(limitType)) {
            setTitle("还款详情");
        } else {
            setTitle("借款详情");
        }
    }


}
