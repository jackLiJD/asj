package com.ald.asjauthlib.cashier.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.cashier.ui.ReimburseRecordFragment;
import com.ald.asjauthlib.cashier.ui.RepaymentRecordFragment;
import com.ald.asjauthlib.databinding.ActivityRefundRecordBinding;
import com.ald.asjauthlib.utils.MyFragmentPagerAdapter;
import com.ald.asjauthlib.utils.UIHelper;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.vm.BaseVM;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by luckyliang on 2017/12/2.
 * <p>
 * 退还款记录
 */

public class RefundRecordVM extends BaseVM {

    private Context mContext;
    private ActivityRefundRecordBinding mBinding;
    private FragmentManager fragmentManager;
    private ReimburseRecordFragment reimburseRecordFragment;
    private RepaymentRecordFragment repaymentRecordFragment;
    private ArrayList<Fragment> fragments = new ArrayList<>();

    public RefundRecordVM(Context context, FragmentManager supportFragmentManager, ActivityRefundRecordBinding binding) {
        mContext = context;
        mBinding = binding;
        fragmentManager = supportFragmentManager;
        load();
    }

    public void load() {

        ArrayList<String> titles = new ArrayList<>();
        Collections.addAll(titles, AlaConfig.getResources().getStringArray(R.array.refundRecordTips));

        reimburseRecordFragment = new ReimburseRecordFragment();
        fragments.add(reimburseRecordFragment);//退款
        repaymentRecordFragment = new RepaymentRecordFragment();
        fragments.add(repaymentRecordFragment);//还款


        // 注意使用的是：getSupportFragmentManager
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(fragmentManager, fragments, titles);
        mBinding.pager.setAdapter(adapter);
        // 设置ViewPager最大缓存的页面个数(cpu消耗少)
//        mBinding.pager.setOffscreenPageLimit(2);
        mBinding.pager.setCurrentItem(0);
        mBinding.tabs.setViewPager(mBinding.pager);

    }

    /**
     * 选择年月后重新加载数据
     */
    public void reloadRecord(int year, int month) {
        if (year == 0 || month == 0)//不选择年月的话不刷新
            return;
        if (mBinding.pager.getCurrentItem() == 0) {
            reimburseRecordFragment.getCvb().getViewModel().load("", false, year, month);

        } else if (mBinding.pager.getCurrentItem() == 1) {
            repaymentRecordFragment.getCvb().getViewModel().load("", false, year, month);
        }

    }

    public void onBackClick(View view) {
        ((Activity) mContext).onBackPressed();
    }


    public void clickPhoneService(View view) {
        UIHelper.telService(mContext, AlaConfig.getResources().getString(R.string.service_phone));
    }
}
