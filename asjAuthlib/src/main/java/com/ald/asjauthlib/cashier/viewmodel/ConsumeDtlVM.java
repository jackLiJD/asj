package com.ald.asjauthlib.cashier.viewmodel;

import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.BusinessApi;
import com.ald.asjauthlib.cashier.model.ConsumeDtlModel;
import com.ald.asjauthlib.cashier.ui.ConsumeDtlFragment;
import com.ald.asjauthlib.cashier.ui.SettleAdvancedActivity;
import com.ald.asjauthlib.cashier.ui.StageDtlFragment;
import com.ald.asjauthlib.databinding.ActivityConsumeDtlBinding;
import com.ald.asjauthlib.utils.MyFragmentPagerAdapter;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.network.RequestCallBack;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.vm.BaseVM;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by luckyliang on 2017/12/11.
 */

public class ConsumeDtlVM extends BaseVM implements ViewPager.OnPageChangeListener {

    public final ObservableField<String> displayPrinciple = new ObservableField<>();//分期本金
    public final ObservableField<String> displayServiceCharge = new ObservableField<>();//合计手续费
    public final ObservableField<String> displayOverdueInterest = new ObservableField<>();//逾期利息
    public final ObservableBoolean aheadVisibility = new ObservableBoolean(false);//提前结清按钮显示状态

    private FragmentActivity act;
    private StageDtlFragment stageDtlFragment;
    private int rid;

    /**
     * @param canAhead 是否可以提前结清
     */
    public ConsumeDtlVM(FragmentActivity act, ActivityConsumeDtlBinding cvb, int rid, boolean canAhead) {
        this.act = act;
//        binding = cvb;
        this.rid = rid;
        aheadVisibility.set(canAhead);
        loadFrag(cvb, rid);
    }

    private void loadFrag(ActivityConsumeDtlBinding binding, int rid) {
        ArrayList<String> titles = new ArrayList<>();
        String[] consumeDtlTips = AlaConfig.getResources().getStringArray(R.array.consumeDtlTips);
        for (String dtlTip : consumeDtlTips) {
            titles.add(dtlTip);
        }
        ArrayList<Fragment> fragments = new ArrayList<>();
        stageDtlFragment = StageDtlFragment.newInstance();
        fragments.add(stageDtlFragment);
        ConsumeDtlFragment consumeDtlFragment = ConsumeDtlFragment.newInstance();
        fragments.add(consumeDtlFragment);

        // 注意使用的是：getSupportFragmentManager
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(act.getSupportFragmentManager(), fragments, titles);
        binding.pager.setAdapter(adapter);
        // 设置ViewPager最大缓存的页面个数(cpu消耗少)
        binding.pager.setOffscreenPageLimit(1);
        binding.pager.setCurrentItem(0);
        binding.pager.addOnPageChangeListener(this);
        binding.tabs.setViewPager(binding.pager);
        reqConsumeDtl(rid, stageDtlFragment, consumeDtlFragment);
    }

    private void reqConsumeDtl(int rid, final StageDtlFragment stageDtlFragment, final ConsumeDtlFragment consumeDtlFragment) {
        JSONObject object = new JSONObject();
        object.put("billId", rid);
        Call<ConsumeDtlModel> call = RDClient.getService(BusinessApi.class).getBorrowDetailV1(object);
        call.enqueue(new RequestCallBack<ConsumeDtlModel>() {
            @Override
            public void onSuccess(Call<ConsumeDtlModel> call, Response<ConsumeDtlModel> response) {
                ConsumeDtlModel model = response.body();
                displayPrinciple.set(model.getAmount().toString());
                displayServiceCharge.set(model.getInterest().toString());
                displayOverdueInterest.set(model.getOverdueInterest().toString());
                stageDtlFragment.fillData(model);
                consumeDtlFragment.fillData(model);
            }
        });
    }

    public void backClick(View view) {
        act.finish();
    }

    public void aheadClick(View view) {
        Intent intent = new Intent();
        intent.putExtra(BundleKeys.INTENT_SETTLE_BILL_ID, rid);
        intent.putExtra(BundleKeys.STAGE_JUMP, act.getIntent().getStringExtra(BundleKeys.STAGE_JUMP));
        ActivityUtils.push(SettleAdvancedActivity.class, intent);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (0 == position && null != stageDtlFragment) {
            stageDtlFragment.playAnimation();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
