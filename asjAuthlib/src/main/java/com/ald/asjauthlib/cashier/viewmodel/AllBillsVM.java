package com.ald.asjauthlib.cashier.viewmodel;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.auth.BusinessApi;
import com.ald.asjauthlib.cashier.model.BillsModel;
import com.ald.asjauthlib.cashier.ui.HasBillsFragment;
import com.ald.asjauthlib.cashier.ui.NotBillsFragment;
import com.ald.asjauthlib.cashier.ui.RefundRecordActivity;
import com.ald.asjauthlib.databinding.ActivityAllBillsBinding;
import com.ald.asjauthlib.utils.MyFragmentPagerAdapter;
import com.ald.asjauthlib.utils.StageJumpEnum;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.network.RequestCallBack;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.DensityUtils;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.authframework.core.vm.ViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/*
 * Created by wjy on 2017/12/13.
 */

public class AllBillsVM implements ViewModel {

    private FragmentActivity act;
    private ActivityAllBillsBinding binding;
    private List<Fragment> fragments;
    private HasBillsFragment hasBillsFragment;
    private NotBillsFragment notBillsFragment;

    public AllBillsVM(FragmentActivity act, ActivityAllBillsBinding binding, int pageNo) {
        this.act = act;
        this.binding = binding;
        fragments = new ArrayList<>(2);
        setRebound();
        layoutPage(pageNo);
        requestAllBiil();
    }

    private void setRebound() {
        binding.pager.setPagerCount(2);
        binding.pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                binding.pager.setCurrentIndex(position);
                if (0 == position) {
                    hasBillsFragment.recoverLayout();
                    notBillsFragment.translationLatout();
                } else {
                    hasBillsFragment.translationLatout();
                    notBillsFragment.recoverLayout();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void requestAllBiil() {
        JSONObject outBill = new JSONObject();
        outBill.put("status", "outBill"); // 所有已出
        Call<BillsModel> call1 = RDClient.getService(BusinessApi.class).getMyBorrowListV1(outBill);
        call1.enqueue(new RequestCallBack<BillsModel>() {
            @Override
            public void onSuccess(Call<BillsModel> call, Response<BillsModel> response) {
                BillsModel model = response.body();
                ((HasBillsFragment) fragments.get(0)).fillData(model);
            }
        });
        JSONObject notOutBill = new JSONObject();
        notOutBill.put("status", "notOutBill"); // 未出账单
        Call<BillsModel> call2 = RDClient.getService(BusinessApi.class).getMyBorrowListV1(notOutBill);
        call2.enqueue(new RequestCallBack<BillsModel>() {
            @Override
            public void onSuccess(Call<BillsModel> call, Response<BillsModel> response) {
                BillsModel model = response.body();
                ((NotBillsFragment) fragments.get(1)).fillData(model);
            }
        });
    }

    private void layoutPage(int pageNo) {
        fragments.add(hasBillsFragment = HasBillsFragment.newInstance(pageNo));
        fragments.add(notBillsFragment = NotBillsFragment.newInstance(pageNo));
        PagerAdapter adapter = new MyFragmentPagerAdapter(act.getSupportFragmentManager(), fragments);
        binding.pager.setAdapter(adapter);
        binding.pager.setOffscreenPageLimit(1);
        binding.pager.setPageMargin(DensityUtils.getPxByDip(8));
        binding.pager.setCurrentItem(pageNo);
    }

    public void topBarRightClick(View view) {
        Intent intent = new Intent();
        String stage = act.getIntent().getStringExtra(BundleKeys.STAGE_JUMP);
        intent.putExtra(BundleKeys.STAGE_JUMP, MiscUtils.isEmpty(stage) ? StageJumpEnum.STAGE_TO_REPAY_H5 : stage);
        ActivityUtils.push(RefundRecordActivity.class, intent);
    }

}
