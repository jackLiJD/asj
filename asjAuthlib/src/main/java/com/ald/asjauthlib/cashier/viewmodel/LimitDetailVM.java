package com.ald.asjauthlib.cashier.viewmodel;

import android.app.Activity;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.BusinessApi;
import com.ald.asjauthlib.cashier.model.LimitDetailModel;
import com.ald.asjauthlib.cashier.ui.LimitDetailActivity;
import com.ald.asjauthlib.utils.ModelEnum;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.network.NetworkUtil;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.network.RequestCallBack;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.authframework.core.vm.BaseVM;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Response;

import static com.ald.asjauthlib.utils.AppUtils.formatAmount;


/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/1/12 14:14
 * 描述：
 * 修订历史：
 */
public class LimitDetailVM extends BaseVM {
    private final String refild;
    private final String limitType;
    private Activity context;

    //还款、借款都有的公共字段
    public final ObservableField<String> displayCash = new ObservableField<>();
    public final ObservableField<String> displayTitle = new ObservableField<>();
    public final ObservableField<String> displayNoTitle = new ObservableField<>();
    public final ObservableField<String> displayNo = new ObservableField<>();
    public final ObservableField<String> displayTime = new ObservableField<>();
    //借款或者还款
    public final ObservableBoolean isLoan = new ObservableBoolean(true);
    //现金借款或者消费借款
    public final ObservableBoolean cash = new ObservableBoolean(false);
    //消费借款成功
    public final ObservableBoolean consumeSuccess = new ObservableBoolean(false);
    //现金借款拒绝
    public final ObservableBoolean cashRefuse = new ObservableBoolean(true);

    //还款
    public final ObservableField<String> displayDesc = new ObservableField<>();//还款说明
    public final ObservableField<String> displayOffer = new ObservableField<>();//优惠抵扣
    public final ObservableField<String> displayRebate = new ObservableField<>();//还款账户余额
    public final ObservableField<String> displayActualPay = new ObservableField<>();//还款现金
    public final ObservableField<String> displayPayWay = new ObservableField<>();//还款方式

    public final ViewLoan viewLoan = new ViewLoan();

    //消费借款或者分期借款
    public class ViewLoan {
        public final ObservableField<String> displayAccount = new ObservableField<>();//收款账户
        public final ObservableField<String> displayType = new ObservableField<>();
        //消费分期
        public final ObservableField<String> displayDesc = new ObservableField<>();//消费借款明细
        public final ObservableField<String> displayNper = new ObservableField<>();//消费借款分期数
        public final ObservableField<String> displayHasNper = new ObservableField<>();//消费借款已还期数
        public final ObservableField<String> displayNperAmount = new ObservableField<>();//消费借款每期还款金额
        public final ObservableField<String> displayHasAmount = new ObservableField<>();//消费借款已还本金
        //现金借款
        public final ObservableField<String> displayCashDay = new ObservableField<>();//现金借款天数
        public final ObservableField<String> displayCashRefundAmount = new ObservableField<>();


    }

    public LimitDetailVM(LimitDetailActivity activity) {
        this.context = activity;
        this.refild = context.getIntent().getStringExtra(BundleKeys.LIMIT_REFILD_ID);
        this.limitType = context.getIntent().getStringExtra(BundleKeys.LIMIT_TYPE);
        load();
    }

    private void load() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("refId", refild);
        jsonObject.put("type", limitType);
        Call<LimitDetailModel> call = RDClient.getService(BusinessApi.class).getLimitDetailInfo(jsonObject);
        NetworkUtil.showCutscenes(context, call);
        call.enqueue(new RequestCallBack<LimitDetailModel>() {
            @Override
            public void onSuccess(Call<LimitDetailModel> call, Response<LimitDetailModel> response) {
                LimitDetailModel model = response.body();
                String amount = formatAmount(response.body().getAmount());
                displayCash.set("￥" + amount);
                //还款页面
                if (ModelEnum.BILL_TYPE_REPAYMENT.getModel().equals(limitType)) {
                    isLoan.set(false);
                    if (ModelEnum.Y.getModel().equals(model.getStatus())) {
                        displayTitle.set(AlaConfig.getResources().getString(R.string.limit_detail_success_state));
                    } else if (ModelEnum.F.getModel().equals(model.getStatus())) {
                        displayTitle.set(AlaConfig.getResources().getString(R.string.limit_detail_failed_state));
                    }else if (ModelEnum.N.getModel().equals(model.getStatus()) || ModelEnum.P.getModel()
                            .equals(model.getStatus())) {
                        displayTitle.set(AlaConfig.getResources().getString(R.string.limit_detail_default_state));
                    }
                    if (limitType.equals(ModelEnum.BILL_TYPE_CASH.getModel())) {
                        displayDesc.set(ModelEnum.BILL_TYPE_CASH.getDesc() + model.getCardNo());
                    } else if (limitType.equals(ModelEnum.BILL_TYPE_CONSUME.getModel())) {
                        displayDesc.set(ModelEnum.BILL_TYPE_CONSUME.getDesc());
                    } else if (limitType.equals(ModelEnum.BILL_TYPE_REPAYMENT.getModel())) {
                        displayDesc.set(ModelEnum.BILL_TYPE_REPAYMENT.getDesc());
                    }
                    displayOffer.set("￥" + model.getCouponAmount().toString());
                    displayRebate.set("￥" + model.getRebateAmount().toString());
                    displayActualPay.set("￥" + model.getActualAmount());
                    displayPayWay.set(model.getCardName());
                    displayNoTitle.set(AlaConfig.getResources().getString(R.string.limit_detail_loan_refund_no_title));
                    displayNo.set(model.getNumber());
                } else {
                    isLoan.set(true);
                    viewLoan.displayAccount
                            .set(response.body().getCardName() + "(尾号" + response.body().getCardNo() + ")");
                    if (ModelEnum.LOAN_STATE_AGREE.getModel().equals(model.getStatus())) {
                        displayTitle.set(ModelEnum.LOAN_STATE_AGREE.getDesc());
                    } else if (ModelEnum.LOAN_STATE_APPLY.getModel().equals(model.getStatus())) {
                        displayTitle.set(ModelEnum.LOAN_STATE_APPLY.getDesc());
                    } else if (ModelEnum.LOAN_STATE_REFUSE.getModel().equals(model.getStatus())) {
                        displayTitle.set(ModelEnum.LOAN_STATE_REFUSE.getDesc());
                    } else if (ModelEnum.LOAN_STATE_TRANSED.getModel().equals(model.getStatus())) {
                        displayTitle.set(ModelEnum.LOAN_STATE_TRANSED.getDesc());
                    } else if (ModelEnum.LOAN_STATE_CLOSED.getModel().equals(model.getStatus())) {
                        displayTitle.set(ModelEnum.LOAN_STATE_CLOSED.getDesc());
                    }
                    //现金借款
                    if (ModelEnum.BILL_TYPE_CASH.getModel().equals(limitType)) {
                        cash.set(true);
                        viewLoan.displayType.set(ModelEnum.BILL_TYPE_CASH.getDesc());
                        viewLoan.displayCashDay.set(model.getBorrowDay() + "天");
                        viewLoan.displayCashRefundAmount.set("￥" + formatAmount(model.getPayAmount()));
                        if (ModelEnum.LOAN_STATE_APPLY.getModel().equals(model.getStatus())
                                || ModelEnum.LOAN_STATE_CLOSED.getModel().equals(model.getStatus())
                                || ModelEnum.LOAN_STATE_REFUSE.getModel().equals(model.getStatus())) {
                            cashRefuse.set(false);
                        } else {
                            cashRefuse.set(true);
                        }
                    } else {
                        cash.set(false);
                        if (ModelEnum.LOAN_STATE_TRANSED.getModel().equals(model.getStatus())) {
                            consumeSuccess.set(true);
                        } else {
                            consumeSuccess.set(false);
                        }
                        viewLoan.displayType.set(ModelEnum.BILL_TYPE_CONSUME.getDesc());
                        viewLoan.displayDesc.set(model.getBorrowDetail());
                        viewLoan.displayNper.set(response.body().getNper() + "");
                        viewLoan.displayHasNper.set(response.body().getNperRepayment() + "");
                        String perAmount = "￥" + formatAmount(response.body().getPerAmount());
                        viewLoan.displayNperAmount.set(perAmount);
                        String repayPrinAmount = "￥" + formatAmount(response.body().getRepayPrinAmount());
                        viewLoan.displayHasAmount.set(repayPrinAmount);
                    }
                    displayNoTitle.set(AlaConfig.getResources().getString(R.string.limit_detail_loan_no_title));
                    displayNo.set(model.getNumber());
                }

                String time = MiscUtils.format(new Date(response.body().getGmtCreate()), "yyyy-MM-dd HH:mm");
                displayTime.set(time);
				context.setResult(Activity.RESULT_OK);
            }
        });
    }


}
