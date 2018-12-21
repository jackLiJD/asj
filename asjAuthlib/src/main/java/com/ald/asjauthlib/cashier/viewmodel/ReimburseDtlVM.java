package com.ald.asjauthlib.cashier.viewmodel;

import android.app.Activity;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.view.View;

import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.auth.BusinessApi;
import com.ald.asjauthlib.cashier.model.RefundDtlModel;
import com.ald.asjauthlib.utils.AppUtils;
import com.ald.asjauthlib.utils.StageJumpEnum;
import com.ald.asjauthlib.web.HTML5WebView;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.network.RequestCallBack;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.authframework.core.utils.UIUtils;
import com.ald.asjauthlib.authframework.core.vm.BaseVM;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/*
 * Created by luckyliang on 2017/12/11.
 */

public class ReimburseDtlVM extends BaseVM {

    public final ObservableField<String> displayAmount = new ObservableField<>();//退款总金额
    public final ObservableField<String> displayBankAmount = new ObservableField<>();//直接支付
    public final ObservableField<String> displayPrinciple = new ObservableField<>();//本金
    public final ObservableField<String> displayStageServiceCharge = new ObservableField<>();//分期手续费
    public final ObservableField<String> displayOverdueInterest = new ObservableField<>();//逾期利息
    public final ObservableField<String> displayUseDiscount = new ObservableField<>();//优惠券抵扣
    public final ObservableField<String> displayUseRebate = new ObservableField<>();//优惠券抵扣
    public final ObservableField<String> displayDtlBankAmount = new ObservableField<>();//直接支付(详情中)
    public final ObservableField<String> displayName = new ObservableField<>();//商品名称
    public final ObservableField<String> displayPriceAmount = new ObservableField<>();//商品总价
    public final ObservableField<String> displayStageDtl = new ObservableField<>();//分期详情
    public final ObservableField<String> displayTime = new ObservableField<>();//退款时间
    public final ObservableField<String> displayRefundId = new ObservableField<>();//退款编号
    public final ObservableField<String> displaySystemDiscont = new ObservableField<>();//系统减免
    public final ObservableBoolean showSystemDiscontLayout = new ObservableBoolean(false);//系统减免展示状态

    private long amountId = 0;
    private Activity activity;

    public ReimburseDtlVM(long amountId, Activity activity) {
        this.amountId = amountId;
        this.activity = activity;
        load();
    }

    public void load() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("amountId", amountId);//退款id
        Call<RefundDtlModel> call = RDClient.getService(BusinessApi.class).getRepaymentDetailV1(jsonObject);
        call.enqueue(new RequestCallBack<RefundDtlModel>() {
                         @Override
                         public void onSuccess(Call<RefundDtlModel> call, Response<RefundDtlModel> response) {
                             if (response.body() == null) {
                                 UIUtils.showToast("无数据");
                                 return;
                             }
                             RefundDtlModel refundDtlModel = response.body();
                             displayDtlBankAmount.set(AppUtils.formatAmount(refundDtlModel.getBankAmount()) + "元");
//                             displayAmount.set(Double.toString(refundDtlModel.getAmount()) + "元");
                             displayBankAmount.set(AppUtils.formatAmount(refundDtlModel.getBankAmount()) + "元");
                             List<RefundDtlModel.Detail> detailList = refundDtlModel.getDetailList();
                             for (RefundDtlModel.Detail detail : detailList) {
                                 setDetail(detail);
                             }
                             displayName.set(refundDtlModel.getName());
                             displayPriceAmount.set(AppUtils.formatAmount(refundDtlModel.getPriceAmount()) + "元");
                             displayStageDtl.set(AppUtils.formatAmount(refundDtlModel.getNperAmount()) + "X" +
                                     Integer.toString(refundDtlModel.getNper()) + "期(已支付" +
                                     Integer.toString(refundDtlModel.getNperRepayment()) + "期)");
                             displayTime.set(refundDtlModel.getDate());
                             displayRefundId.set(refundDtlModel.getNumber());
                         }

                     }

        );


    }

    private void setDetail(RefundDtlModel.Detail detail) {
        String amount = "0.00";
        if (detail.getAmount() != null) {
            amount = AppUtils.formatAmount(detail.getAmount());
        }

        switch (detail.getType()) {
            case 0://本金
                displayPrinciple.set(amount + "元");
                break;
            case 1://分期手续费
                displayStageServiceCharge.set(amount + "元");
                break;
            case 2://逾期利息
                displayOverdueInterest.set(amount + "元");
                break;
            case 3://账户余额抵扣
                displayUseRebate.set(amount + "元");
                break;
            case 4://优惠券抵扣
                displayUseDiscount.set(amount + "元");
                break;
            case 6://退款总金额
                displayAmount.set(amount + "元");
//                displayDtlBankAmount.set(amount + "元");
                break;
            case 7://系统减免,没有的话不返回
                displaySystemDiscont.set(amount + "元");
                showSystemDiscontLayout.set(true);
                break;

        }
    }

    public void onBackHome(View view) {
        String stage = activity.getIntent().getStringExtra(BundleKeys.STAGE_JUMP);
        if (MiscUtils.isEmpty(stage) || stage.equals(StageJumpEnum.STAGE_TO_REPAY_H5.getModel())) {
            ActivityUtils.popUntilWithoutRefresh(HTML5WebView.class);
        } else {
//            ActivityUtils.popUntilWithoutRefresh(CreditCenterActivity.class);
            ActivityUtils.pop();
        }

    }
}
