package com.ald.asjauthlib.cashier.viewmodel;

import android.app.Activity;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.BusinessApi;
import com.ald.asjauthlib.cashier.model.RefundDtlModel;
import com.ald.asjauthlib.utils.AppUtils;
import com.ald.asjauthlib.utils.StageJumpEnum;
import com.ald.asjauthlib.utils.TimeUtils;
import com.ald.asjauthlib.web.HTML5WebView;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.network.RequestCallBack;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.authframework.core.utils.UIUtils;
import com.ald.asjauthlib.authframework.core.vm.BaseVM;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by luckyliang on 2017/12/10.
 * 退还款记录 还款详情
 */

public class RepaymentDtlVM extends BaseVM {

    public final ObservableField<String> displayRealRepayment = new ObservableField<>();//实际还款金额
    public final ObservableField<String> displaPyrincipal = new ObservableField<>();//本金
    public final ObservableField<String> displayServiceCharge = new ObservableField<>();//手续费
    public final ObservableField<String> displayDemurrage = new ObservableField<>();//逾期费
    public final ObservableField<String> displayBalanceDeduction = new ObservableField<>();//账户余额抵扣
    public final ObservableField<String> displayDiscountDeduction = new ObservableField<>();//优惠券抵扣
    public final ObservableField<String> displaySelfRepayment = new ObservableField<>();//直接支付
    public final ObservableField<String> displaySystemDiscont = new ObservableField<>();//系统减免
    public final ObservableBoolean showSystemDiscount = new ObservableBoolean(false);//系统减免显示状态

    public final ObservableField<String> displayTimeSubmit = new ObservableField<>();//提交还款时间
    public final ObservableField<String> displayTimeBankHandle = new ObservableField<>();//银行处理中
    public final ObservableField<String> displayTimeReceive = new ObservableField<>();//到账成功

    public final ObservableField<String> displayTimeRepay = new ObservableField<>();//还款时间
    public final ObservableField<String> displayTimeRepaymentNo = new ObservableField<>();//还款编号
    public final ObservableField<String> displayTimeRepaymentResult = new ObservableField<>();//还款结果

    public final ObservableInt displayStatusColor = new ObservableInt(0);
    public final ObservableInt displaylineColor = new ObservableInt(0);//下段进度线条颜色
    public final ObservableField<Drawable> displayStatusIcon = new ObservableField<>();

    private long amountId;
    Activity activity;


    public RepaymentDtlVM(long amountId, Activity activity) {
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
                             displayRealRepayment.set(AppUtils.formatAmount(refundDtlModel.getAmount()) + "元");
                             displayTimeRepay.set(refundDtlModel.getDate());
                             displayTimeRepaymentNo.set(refundDtlModel.getNumber());


                             List<RefundDtlModel.Detail> detailList = refundDtlModel.getDetailList();
                             for (RefundDtlModel.Detail detail : detailList) {
                                 setDetail(detail);
                             }
                             for (RefundDtlModel.Log log : refundDtlModel.getLogList()) {
                                 if (log.getStatus() == 0) {
                                     //提交还款
                                     displayTimeSubmit.set(TimeUtils.longToBeijingTimeString(log.getGmtCreate()));
                                 } else if (log.getStatus() == 2) {
                                     //成功時間
                                     displayTimeReceive.set(TimeUtils.longToBeijingTimeString(log.getGmtCreate()));
                                     displayTimeRepaymentResult.set("到账成功");
                                     displaylineColor.set(ContextCompat.getColor(AlaConfig.getContext(), R.color.color_blue_renewal_all));
                                     displayStatusColor.set(ContextCompat.getColor(AlaConfig.getContext(), R.color.color_blue_renewal_all));
                                     displayStatusIcon.set(ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.ic_hook_circle_hollow));

                                 } else if (log.getStatus() == 3) {
                                     displayTimeReceive.set(TimeUtils.longToBeijingTimeString(log.getGmtCreate()));
                                     displayTimeRepaymentResult.set("还款失败");
                                     displaylineColor.set(ContextCompat.getColor(AlaConfig.getContext(), R.color.color_blue_renewal_all));
                                     displayStatusColor.set(ContextCompat.getColor(AlaConfig.getContext(), R.color.color_ff5546));
                                     displayStatusIcon.set(ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.ic_circle_fork));
                                 } else if (log.getStatus() == 1) {
                                     //处理中
                                     displayTimeRepaymentResult.set("到账成功");
                                     displaylineColor.set(ContextCompat.getColor(AlaConfig.getContext(), R.color.color_step_line_gray));
                                     displayStatusColor.set(ContextCompat.getColor(AlaConfig.getContext(), R.color.color_step_line_gray));
                                     displayTimeBankHandle.set(TimeUtils.longToBeijingTimeString(log.getGmtCreate()));
                                     displayStatusIcon.set(ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.ic_repay_stauts_ing));
                                 }
                             }

                         }

                     }

        );


    }

    private void setDetail(RefundDtlModel.Detail detail) {
        String amount = "0.00元";
        if (detail.getAmount() != null) {
            amount = AppUtils.formatAmountKeepMinus(detail.getAmount()) + "元";
        }
        switch (detail.getType()) {
            case 0://本金
                displaPyrincipal.set(amount);
                break;
            case 1://分期手续费
                displayServiceCharge.set(amount);
                break;
            case 2://逾期利息
                displayDemurrage.set(amount);
                break;
            case 3://账户余额抵扣
                displayBalanceDeduction.set(amount);
                break;
            case 4://优惠券抵扣
                displayDiscountDeduction.set(amount);
                break;
            case 5:
                displaySelfRepayment.set(amount);
                break;
            case 6://退款总金额；实际还款金额
//                displayRealRepayment.set(amount);
                displaySelfRepayment.set(amount);
                break;
            case 7://系统减免，没有不返回
                displaySystemDiscont.set(amount);
                showSystemDiscount.set(true);
                break;
            case 8:
                displayRealRepayment.set(amount);
                break;


        }
    }

    /**
     * 底部回到首页点击
     */
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
