package com.ald.asjauthlib.cashier.viewmodel;

import android.app.Activity;
import android.databinding.ObservableField;

import com.ald.asjauthlib.BR;
import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.BusinessApi;
import com.ald.asjauthlib.cashier.model.HistoryBillMonthDtlModel;
import com.ald.asjauthlib.utils.AppUtils;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.network.RequestCallBack;
import com.ald.asjauthlib.authframework.core.vm.BaseRecyclerViewVM;

import java.math.BigDecimal;
import java.util.List;

import com.ald.asjauthlib.tatarka.bindingcollectionadapter.ItemView;
import retrofit2.Call;
import retrofit2.Response;

/*
 * Created by luckyliang on 2017/12/6.
 */

public class HistoryBillMonthDtlVM extends BaseRecyclerViewVM<ItemHistoryBillMonthVM> {
    public final ObservableField<String> displayDuration = new ObservableField<>();
    public final ObservableField<String> displayAmountInfo = new ObservableField<>();//账单综述

    BigDecimal amount;//本月消费总金额
    private Activity mContext;

    public HistoryBillMonthDtlVM(Activity context, String year, String month) {
        mContext = context;
        load(year, month);
    }

    @Override
    protected void selectView(ItemView itemView, int position, ItemHistoryBillMonthVM item) {
        itemView.set(BR.viewModel, R.layout.item_history_bill_month);
    }

    public void load(String years, String month) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("billMonth", month);//账月
        jsonObject.put("billYear", years);//帐年
        Call<HistoryBillMonthDtlModel> call = RDClient.getService(BusinessApi.class).getMyHistoryBorrowDetailV1(jsonObject);
        call.enqueue(new RequestCallBack<HistoryBillMonthDtlModel>() {
                         @Override
                         public void onSuccess(Call<HistoryBillMonthDtlModel> call, Response<HistoryBillMonthDtlModel> response) {
                             displayDuration.set(response.body().getStr() + "-" + response.body().getEnd());
                             List<HistoryBillMonthDtlModel.SubBill> subBills = response.body().getBillList();
                             amount = new BigDecimal(0);
                             for (int i = 0; i < subBills.size(); i++) {
                                 ItemHistoryBillMonthVM itemHistoryBillMonthVM =
                                         new ItemHistoryBillMonthVM(subBills.get(i), mContext.getIntent().getStringExtra(BundleKeys.STAGE_JUMP));
                                 items.add(itemHistoryBillMonthVM);
                                 amount = amount.add(subBills.get(i).getBillAmount());
                             }
                             displayAmountInfo.set(String.format("本月共消费%s笔，实际支出%s元"
                                     , Integer.toString(response.body().getBillList().size()), AppUtils.formatAmount(amount)))
                             ;

                         }

                     }

        );
    }
}
