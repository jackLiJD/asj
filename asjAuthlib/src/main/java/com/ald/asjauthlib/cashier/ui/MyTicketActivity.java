package com.ald.asjauthlib.cashier.ui;

import android.support.v4.content.ContextCompat;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.cashier.viewmodel.MyTicketsVM;
import com.ald.asjauthlib.databinding.ActivityMyTicketsBinding;
import com.ald.asjauthlib.authframework.core.AlaTopBarActivity;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;

/**
 * 版权：XXX公司 版权所有
 * 作者：TonyChen
 * 版本：1.0
 * 创建日期：2017/3/4
 * 描述：优惠券界面
 * 修订历史：
 */
public class MyTicketActivity extends AlaTopBarActivity<ActivityMyTicketsBinding> {
    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_my_tickets;
    }

    @Override
    protected void setViewModel() {
        //ViewModel模块
        cvb.setViewModel(new MyTicketsVM(this, this.getSupportFragmentManager(), cvb));
    }

    @Override
    public String getStatName() {
        return "我的优惠券";
    }

    @Override
    protected void afterOnCreate() {
        super.afterOnCreate();
        setTitle(getResources().getString(R.string.my_ticket_title));
        setTitleColor(ContextCompat.getColor(AlaConfig.getContext(),R.color.text_important_color));
    }
}
