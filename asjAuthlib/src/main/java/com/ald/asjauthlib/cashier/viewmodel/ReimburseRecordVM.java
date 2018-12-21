package com.ald.asjauthlib.cashier.viewmodel;


import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.view.View;

import com.ald.asjauthlib.BR;
import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.BusinessApi;
import com.ald.asjauthlib.cashier.model.ItemDataPair;
import com.ald.asjauthlib.cashier.model.ItemStickyEntity;
import com.ald.asjauthlib.cashier.model.RefundModel;
import com.ald.asjauthlib.cashier.ui.CalenderActivity;
import com.ald.asjauthlib.cashier.ui.ReimburseRecordFragment;
import com.ald.asjauthlib.databinding.FragmentReimburseRecordBinding;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.network.RequestCallBack;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.authframework.core.vm.BaseRecyclerViewVM;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import java.util.ArrayList;
import java.util.List;

import com.ald.asjauthlib.tatarka.bindingcollectionadapter.ItemView;
import retrofit2.Call;
import retrofit2.Response;


/**
 * Created by luckyliang on 2017/12/3.
 * 退款记录Fragment VM
 */

public class ReimburseRecordVM extends BaseRecyclerViewVM<ItemReimburseDtlVM> implements OnRefreshLoadmoreListener {

    static final int TYPE_MONTH_TITLE = 0;
    static final int TYPE_MONTH_DETAIL = 1;

    private final String OPTION_TOP = "top";
    private final String OPTION_BOTTOM = "bottom";

    public final ObservableBoolean showNoData = new ObservableBoolean(false);

    ReimburseRecordFragment fragment;

    FragmentReimburseRecordBinding binding;
    private int topMonth = 12;
    private int topYear = 2017;

    public ReimburseRecordVM(ReimburseRecordFragment fragment, FragmentReimburseRecordBinding binding) {
        this.fragment = fragment;
        this.binding = binding;
        binding.refreshLayout.setOnRefreshLoadmoreListener(this);
        load("", true, 0, 0);
    }

    @Override
    protected void selectView(ItemView itemView, int position, ItemReimburseDtlVM item) {
        final int itemType = item.itemDataPair.getItemType();

        if (itemType == TYPE_MONTH_TITLE) {
            itemView.set(BR.viewModel, R.layout.list_item_refund_record_title);

        } else if (itemType == TYPE_MONTH_DETAIL)
            itemView.set(BR.viewModel, R.layout.list_item_reimburse_detail);
    }


    /**
     * 加载数据
     *
     * @param operation 滑动方向
     */
    public void load(final String operation, boolean isFirst, int year, int month) {
        JSONObject jsonObject = new JSONObject();
        if (!isFirst) {
            if (!MiscUtils.isEmpty(operation)) {
                jsonObject.put("month", month == 0 ? "" : month);
                jsonObject.put("year", (year == 0 || month == 0) ? "" : year);
                jsonObject.put("operation", operation);//top 上翻 bottom:下翻
            }
        }
        //首次加载数据只传status
        jsonObject.put("status", "refund");//refund为退款 repayment为还款
        Call<RefundModel> call = RDClient.getService(BusinessApi.class).getMyRepaymentHistoryV1(jsonObject);
        call.enqueue(new RequestCallBack<RefundModel>() {

            @Override
            public void onSuccess(Call<RefundModel> call, Response<RefundModel> response) {
                items.clear();
                topYear = 0;
                topMonth = 0;
                if (response.body() == null || response.body().getStatus() == null) {
                    showNoData.set(true);
                    return;
                }
                showNoData.set(false);
                String status = response.body().getStatus();//类型
                if (status.equals("refund")) {
                    List<ItemStickyEntity> itemEntities = new ArrayList<>();
                    List<RefundModel.Month> monthList = response.body().getList(); // 月份列表
//                    List<RefundModel.Month> monthList = JSON.parseObject(FileUtils.getAssetsData(fragment.getContext(), "json"), RefundModel.class).getList(); // 假数据看悬浮效果
                    for (int i = 0; i < monthList.size(); i++) {
                        RefundModel.Month month = monthList.get(i);
                        ItemDataPair monthDataPair = new ItemDataPair(month, TYPE_MONTH_TITLE);
                        if (topYear == 0)
                            topYear = month.getYear();
                        if (topMonth == 0)
                            topMonth = month.getMonth();
                        items.add(new ItemReimburseDtlVM(monthDataPair, fragment.getActivity()));//添加头部
                        String titleName = month.getYear() + "年" + month.getMonth() + "月份退款记录(共" + month.getAmountList().size() + "条)";
                        itemEntities.add(new ItemStickyEntity(i, true, titleName.replace("共0条", "无")));
                        for (RefundModel.Amount amount : month.getAmountList()) {
                            ItemDataPair amountDataPair = new ItemDataPair(amount, TYPE_MONTH_DETAIL);
                            items.add(new ItemReimburseDtlVM(amountDataPair, fragment.getActivity()));
                            itemEntities.add(new ItemStickyEntity(i, false, titleName));
                        }
                    }
                    binding.recyclerView.addItemDecoration(new ItemHeaderDecoration(fragment.getContext(), itemEntities, R.layout.list_item_refund_record_title));
                }

                if (MiscUtils.isEquals(operation,OPTION_BOTTOM)) {
                    binding.refreshLayout.finishLoadmore(200);
                    binding.refreshLayout.setRefreshFooter(new ClassicsFooter(fragment.getContext()));
                } else if (MiscUtils.isEquals(operation,OPTION_TOP)) {
                    binding.refreshLayout.finishRefresh(200);
                    binding.refreshLayout.getRefreshHeader();
                }
            }
        });
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        load(OPTION_BOTTOM, false, topYear, topMonth);


    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        load(OPTION_TOP, false, topYear, topMonth);
    }

    public void onCalendarClick(View view) {
        Intent intent = new Intent();
        intent.putExtra(BundleKeys.STAGE_JUMP, BundleKeys.STATUS_REFUND_RECORD);
        ActivityUtils.push(CalenderActivity.class, intent, BundleKeys.REQUEST_CODE_CALENDER);
    }
}
