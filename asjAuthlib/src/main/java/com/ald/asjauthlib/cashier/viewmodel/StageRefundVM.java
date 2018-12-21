package com.ald.asjauthlib.cashier.viewmodel;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.utils.Constant;
import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.BusinessApi;
import com.ald.asjauthlib.auth.MainApi;
import com.ald.asjauthlib.bindingadapter.view.ViewBindingAdapter;
import com.ald.asjauthlib.cashier.BankChannel;
import com.ald.asjauthlib.cashier.LoanApi;
import com.ald.asjauthlib.cashier.impl.AccountInfoImpl;
import com.ald.asjauthlib.cashier.impl.AmountCalImpl;
import com.ald.asjauthlib.cashier.impl.JifengInfoImpl;
import com.ald.asjauthlib.cashier.impl.OfferInfoImpl;
import com.ald.asjauthlib.cashier.model.BillRefundModel;
import com.ald.asjauthlib.cashier.model.CashPageTypeModel;
import com.ald.asjauthlib.cashier.model.CouponListModel;
import com.ald.asjauthlib.cashier.model.MyTicketModel;
import com.ald.asjauthlib.cashier.model.PaymentModel;
import com.ald.asjauthlib.cashier.model.WxOrAlaPayModel;
import com.ald.asjauthlib.cashier.params.LoanPayParams;
import com.ald.asjauthlib.cashier.params.StagePayParams;
import com.ald.asjauthlib.cashier.ui.LimitDetailActivity;
import com.ald.asjauthlib.cashier.ui.LoanRepaymentDetailActivity;
import com.ald.asjauthlib.cashier.ui.PhoneTicketActivity;
import com.ald.asjauthlib.cashier.ui.StageRefundActivity;
import com.ald.asjauthlib.cashier.utils.IPaymentCallBack;
import com.ald.asjauthlib.cashier.utils.PaymentFactory;
import com.ald.asjauthlib.dialog.TipsDialog;
import com.ald.asjauthlib.dialog.ValidateDialog;
import com.ald.asjauthlib.utils.AppUtils;
import com.ald.asjauthlib.utils.ModelEnum;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.network.NetworkUtil;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.network.RequestCallBack;
import com.ald.asjauthlib.authframework.core.network.exception.ApiException;
import com.ald.asjauthlib.authframework.core.network.exception.ApiExceptionEnum;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.authframework.core.utils.UIUtils;
import com.ald.asjauthlib.authframework.core.utils.log.Logger;
import com.ald.asjauthlib.authframework.core.vm.BaseVM;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.ald.asjauthlib.utils.BundleKeys.REQUEST_STAGE_REPAYMENT;


/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/1/12 14:14
 * 描述：
 * 修订历史：
 */
public class StageRefundVM extends BaseVM {

    public final ObservableField<String> displayMaxAmount = new ObservableField<>();
    public final ObservableField<String> displayCouponValue = new ObservableField<>();
    public final ObservableField<String> displayRebate = new ObservableField<>();
    public final ObservableField<String> displayJfInfo = new ObservableField<>();
    public final ObservableField<Spannable> displayTips = new ObservableField<>();
    public final ObservableField<String> displayRemainTip = new ObservableField<>();

    public final ObservableField<String> displayEditAmount = new ObservableField<>();
    public final ObservableBoolean displayEditAmountView = new ObservableBoolean();
    public final ObservableBoolean showJfbSwitch = new ObservableBoolean(false);//是否显示集分宝开关(老版本显示，借钱合规不显示)

    public final ObservableField<ViewBindingAdapter.OnWatchListener> watchListener = new ObservableField<>();
    public final ObservableField<ViewBindingAdapter.SwitchWatch> rebateSwitchListener = new ObservableField<>();
    public final ObservableField<ViewBindingAdapter.SwitchWatch> jfSwitchListener = new ObservableField<>();
    //private IWXAPI api;
//    private WxOrAlaPayModel submitRepaymentResult;

    private Activity context;
    private String borrowId;
    private String billIds;    // 分期还款请求页面需要带的参数，用逗号分隔
    private String loanPeriodsIds;//白领贷借款期数ID
    /**
     * 还款金额
     */
    private BigDecimal repaymentAmount;
    private boolean isFromLoan = false;

    private AmountCalImpl calModule;    // 计算账户，集分宝，优惠券用的module
    private AccountInfoImpl accountModule;    // 账户余额
    private JifengInfoImpl jifengModule;    // 集分宝
    private OfferInfoImpl<MyTicketModel> offerModule;    // 优惠券module

    private BigDecimal maxAmount;
    private String pageType = ModelEnum.CASH_PAGE_TYPE_NEW_V2.getModel();//新老借钱页面跳转类型
    private String fromPage = ModelEnum.CASH_LOAN_REPAYMENT_FROM_PAGE_INDEX.getModel();//何页面进入还款
    private String orderId;//订单Id

    private int repaymentType;//还款类型(小贷、白领贷)
    private String repaymentTypeWhiteCollar;//白领贷还款类型(正常还款、提前结清)


    private PaymentFactory paymentFactory;
    private LoanPayParams loanPayParamsLast;
    private StagePayParams stagePayParamsLast;
    private boolean secretFree = false;//是否需要免密支付
    private String errMsg;
    private LoanPayParams loanParams = new LoanPayParams();

    public StageRefundVM(StageRefundActivity activity) {
        this.context = activity;
        fromPage = context.getIntent().getStringExtra(BundleKeys.CASH_LOAN_REPAYMENT_FROM_PAGE);
        orderId = context.getIntent().getStringExtra(BundleKeys.BORROW_ORDER_ID);
        repaymentType = context.getIntent().getIntExtra(BundleKeys.REPAYMENT_TYPE, Constant.LOAN_REPAYMENT_TYPE_PETTY);
        repaymentTypeWhiteCollar = context.getIntent().getStringExtra(BundleKeys.REPAYMENT_TYPE_WHITE_COLLAR);
        loanPeriodsIds = context.getIntent().getStringExtra(BundleKeys.LOAN_PERIODS_ID);
        getCashPageType();
    }

    /**
     * 获取新老借钱页面跳转类型
     * 判断使用新老借钱逻辑
     */
    private void getCashPageType() {
        Call<CashPageTypeModel> call = RDClient.getService(MainApi.class).getCashPageTypeV2();
        NetworkUtil.showCutscenes(context, call);
        call.enqueue(new RequestCallBack<CashPageTypeModel>() {
            @Override
            public void onSuccess(Call<CashPageTypeModel> call, Response<CashPageTypeModel> response) {
                if (response.body() != null) {
                    if (MiscUtils.isNotEmpty(response.body().getPageType())) {
                        pageType = response.body().getPageType();
                    } else {
                        pageType = ModelEnum.CASH_PAGE_TYPE_NEW_V2.getModel();
                    }
                } else {
                    pageType = ModelEnum.CASH_PAGE_TYPE_NEW_V2.getModel();
                }
                requestRepaymentInfo();
            }
        });
    }

    /**
     * 请求还款信息
     */
    private void requestRepaymentInfo() {
        Intent intent = context.getIntent();
        borrowId = intent.getStringExtra(BundleKeys.BORROW_ID);
        if (intent.hasExtra(BundleKeys.BILL_IDS)) {
            billIds = intent.getStringExtra(BundleKeys.BILL_IDS);
        }
        initPayFactory();

        if (borrowId != null) {
            //借钱还款
            isFromLoan = true;
            if (Constant.LOAN_REPAYMENT_TYPE_PETTY == repaymentType || (Constant.LOAN_REPAYMENT_TYPE_WHITE_COLLAR == repaymentType && Constant.LOAN_REPAYMENT_TYPE_WHITE_COLLAR_COMMON.equals(repaymentTypeWhiteCollar))) {
                displayEditAmountView.set(true);
            } else {
                displayEditAmountView.set(false);
            }
            if (Constant.LOAN_REPAYMENT_TYPE_PETTY == repaymentType || (Constant.LOAN_REPAYMENT_TYPE_WHITE_COLLAR == repaymentType && Constant.LOAN_REPAYMENT_TYPE_WHITE_COLLAR_COMMON.equals(repaymentTypeWhiteCollar))) {
                String repaymentStr = intent.getStringExtra(BundleKeys.MAX_REPAYMENT_AMOUNT);
                if (MiscUtils.isNotEmpty(repaymentStr)) {
                    repaymentAmount = new BigDecimal(repaymentStr);
                    displayEditAmount.set(repaymentAmount.toString());
                    maxAmount = new BigDecimal(repaymentStr);
                    if (maxAmount.compareTo(BigDecimal.ZERO) <= 0) {
                        maxAmount = BigDecimal.ZERO;
                    }
                }
            } else {
                //白领贷提前还款
                String repaymentAdvanceStr = intent.getStringExtra(BundleKeys.REPAYMENT_AMOUNT_BLD_ADVANCE);//白领贷提前结清金额
                if (MiscUtils.isNotEmpty(repaymentAdvanceStr)) {
                    repaymentAmount = new BigDecimal(repaymentAdvanceStr);
                    displayEditAmount.set(repaymentAmount.toString());
                    maxAmount = new BigDecimal(repaymentAdvanceStr);
                    if (maxAmount.compareTo(BigDecimal.ZERO) <= 0) {
                        maxAmount = BigDecimal.ZERO;
                    }
                }
            }

            String rebateAmountStr = intent.getStringExtra(BundleKeys.REBATE_AMOUNT);
            int jifCount = intent.getIntExtra(BundleKeys.JIFENG_COUNT, 0);
            if (rebateAmountStr != null) {
                initModule(new BigDecimal(rebateAmountStr), jifCount);
            }
            bindData();

            requestCouponInfo(generateCouponParams());
        } else {
            displayEditAmountView.set(false);
            requestStageRepayment(generateRefundParams());
        }
    }

    private JSONObject generateRefundParams() {
        //String year = context.getIntent().getStringExtra(BundleKeys.STAGE_BILL_YEAR);
        //String month = context.getIntent().getStringExtra(BundleKeys.STAGE_BILL_MONTH);
        //String billId = context.getIntent().getStringExtra(BundleKeys.STAGE_BILL_ID);
        //JSONObject jsonObject = new JSONObject();
        //jsonObject.put("billYear", year);
        //jsonObject.put("billMonth", month);
        //jsonObject.put("billId", billId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("billIds", billIds);
        return jsonObject;
    }

    /**
     * 分期还款界面，要传勾选的billIds用,隔开
     *
     * @param jsonObject billIds
     */
    private void requestStageRepayment(JSONObject jsonObject) {
        //Call<BillRefundModel> call = RDClient.getService(BusinessApi.class).getRepaymentConfirmInfo(jsonObject);
        Call<BillRefundModel> call = RDClient.getService(BusinessApi.class).getRepaymentConfirmInfoV1(jsonObject);
        NetworkUtil.showCutscenes(context, call);
        call.enqueue(new RequestCallBack<BillRefundModel>() {
            @Override
            public void onSuccess(Call<BillRefundModel> call, Response<BillRefundModel> response) {
                BillRefundModel billRefundModel = response.body();
                repaymentAmount = billRefundModel.getRepayAmount();    // 还款金额
                borrowId = billRefundModel.getBillId();
                initModule(billRefundModel.getRebateAmount(), billRefundModel.getJfbAmount());
                bindData();
                offerModule.setOfferInfo(billRefundModel.getCouponList());
                offerModule.calAvailableCoupon(repaymentAmount);
                calModule.calAmountInfo(repaymentAmount);
                refreshCouponView();
                refreshPayInfo();
            }
        });
    }

    /**
     * 生成请求参数
     */
    private JSONObject generateCouponParams() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "REPAYMENT");
        jsonObject.put("repaymentType", repaymentType == Constant.LOAN_REPAYMENT_TYPE_WHITE_COLLAR ? "LOAN" : "BORROWCASH");
        return jsonObject;
    }

    private void requestCouponInfo(JSONObject object) {
        Call<CouponListModel> call = RDClient.getService(LoanApi.class).getUserCounponListType(object);
        NetworkUtil.showCutscenes(context, call);
        call.enqueue(new RequestCallBack<CouponListModel>() {
            @Override
            public void onSuccess(Call<CouponListModel> call, Response<CouponListModel> response) {
                List<MyTicketModel> list = response.body().getCouponList();
                offerModule.setOfferInfo(list);
                offerModule.calAvailableCoupon(repaymentAmount);
                calModule.calAmountInfo(repaymentAmount);
                refreshCouponView();
                refreshPayInfo();
            }
        });
    }

    private void initModule(BigDecimal rebateAmount, int jfCount) {
        calModule = new AmountCalImpl();
        accountModule = new AccountInfoImpl(rebateAmount);
        jifengModule = new JifengInfoImpl(jfCount);
        offerModule = new OfferInfoImpl<>();

        calModule.setAccountInfo(jifengModule);
        calModule.setAccountInfo(accountModule);
        calModule.settOfferInfo(offerModule);
    }

    private void bindData() {
        displayMaxAmount.set(formatterAmount(String.valueOf(repaymentAmount.toString())));
        displayRebate.set(formatterAmount(accountModule.getAccountAmount().toString()));
        displayJfInfo.set(jifengModule.getJifengCount() + "");

        watchListener.set(new TextChangeListener());
        rebateSwitchListener.set(new RebateSwitch());
        if (ModelEnum.CASH_PAGE_TYPE_NEW_V2.getModel().equals(pageType) || ModelEnum.CASH_PAGE_TYPE_NEW_V1.getModel().equals(pageType) || 1 == repaymentType) {
            showJfbSwitch.set(false);
        } else {
            showJfbSwitch.set(true);
            jfSwitchListener.set(new JfSwitch());
        }
    }

    /**
     * 格式化金额
     *
     * @param amountStr 金额
     * @return 格式化金额
     */
    private String formatterAmount(String amountStr) {
        return String.format(AlaConfig.getResources().
                getString(R.string.loan_repayment_moeny_formatter), amountStr);
    }

    /**
     * 借钱还款的参数
     */
    private LoanPayParams createLoanPayParams() {
        LoanPayParams loanParams = new LoanPayParams();
        loanParams.borrowId = borrowId;
        loanParams.cardId = String.valueOf(-2);    // selectItem.getRid()
        if (offerModule.getOfferModel() != null) {
            loanParams.couponId = String.valueOf(offerModule.getOfferModel().getRid());
        }
        calModule.calAmountInfo(getCashAmountInfo().toString());
        if (calModule.getPayAmount() != null) {
            loanParams.repaymentAmount = calModule.getPayAmount().toString();
        }
        loanParams.rebateAmount = accountModule.getActualPayAmount();
        loanParams.jfbAmount = jifengModule.getActualPayCount() + "";
        loanParams.actualAmount = AppUtils.formatAmount(calModule.getExtraAmount());
        loanParams.pageType = pageType;
        loanParams.from = fromPage;
        loanParams.borrowOrderId = orderId;
        loanParams.loanId = borrowId;
        loanParams.loanPeriodsIds = loanPeriodsIds;
        loanParams.repaymentType = String.valueOf(repaymentType);
        loanParams.repaymentTypeWhiteCollar = repaymentTypeWhiteCollar;
        loanParams.secretFree = secretFree;
        loanParams.errMsg = errMsg;
        return loanParams;
    }

    /**
     * 分期还款的参数 账单二期
     */
    private StagePayParams createStagePayParams() {
        StagePayParams stagePayParams = new StagePayParams();
        stagePayParams.cardId = String.valueOf(-2);
        stagePayParams.billIds = billIds;
        if (offerModule.getOfferModel() != null) {
            stagePayParams.couponId = String.valueOf(offerModule.getOfferModel().getRid());
        }
        if (calModule.getPayAmount() != null) {
            stagePayParams.repaymentAmount = calModule.getPayAmount().toString();
        }
        stagePayParams.rebateAmount = accountModule.getActualPayAmount();
        stagePayParams.jfbAmount = jifengModule.getActualPayCount() + "";
        stagePayParams.actualAmount = AppUtils.formatAmount(calModule.getExtraAmount().toString());
        return stagePayParams;
    }

    /**
     * 创建支付工厂
     */
    private void initPayFactory() {
        paymentFactory = new PaymentFactory(context);
        paymentFactory.ObserverPayInfo(new IPaymentCallBack() {
            @Override
            public void onStart() {

            }

            @Override
            public void onHandle() {

            }

            @Override
            public <T> void onSuccess(T model) {
                WxOrAlaPayModel wxOrAlaPayModel = null;
                secretFree = false;
                errMsg = "";
                if (model instanceof WxOrAlaPayModel) {
                    wxOrAlaPayModel = (WxOrAlaPayModel) model;
                    if (isFromLoan) {
                        wxOrAlaPayModel.setMobile(loanPayParamsLast == null ? "" : loanPayParamsLast.mobile);
                        wxOrAlaPayModel.setBankChannel(loanPayParamsLast == null ? "" : loanPayParamsLast.bankChannel);
                        payLoanRefundSuccess(wxOrAlaPayModel);
                    } else {
                        wxOrAlaPayModel.setMobile(stagePayParamsLast == null ? "" : stagePayParamsLast.mobile);
                        wxOrAlaPayModel.setBankChannel(stagePayParamsLast == null ? "" : stagePayParamsLast.bankChannel);
                        payStageRefundSuccess(wxOrAlaPayModel);
                    }
                } else if (model instanceof PaymentModel) {
                    wxOrAlaPayModel = new WxOrAlaPayModel();
                    wxOrAlaPayModel.setType(((PaymentModel) model).getType());
                    wxOrAlaPayModel.setRefId(((PaymentModel) model).getRefId());
                    wxOrAlaPayModel.setBankChannel("");
                    if (isFromLoan) {
                        payLoanRefundSuccess(wxOrAlaPayModel);
                    } else {
                        payStageRefundSuccess(wxOrAlaPayModel);
                    }
                }
            }


            @Override
            public void onCancel(Throwable t) {
                if (isFromLoan) {
                    payLoanRefundFail(t);
                }
            }
        });
    }

    /**
     * app内密码支付：分为借钱还款和消费分期还款
     */
    private void payByPwd() {
        if (isFromLoan) {
            loanPayParamsLast = createLoanPayParams();
            paymentFactory.generatePayment(PaymentFactory.ALA_LOAN_PAYMENT_PWD);
            paymentFactory.initParams(loanPayParamsLast);
        } else {
            // 分期还款 pwd非微信方式
            stagePayParamsLast = createStagePayParams();
            paymentFactory.generatePayment(PaymentFactory.ALA_STAGE_PAYMENT_PWD);
            paymentFactory.initParams(stagePayParamsLast);
        }
    }

    /**
     * 其他支付方式：微信和银行卡密码支付
     */
    private void payByOther() {
        if (isFromLoan) {
            paymentFactory.generatePayment(PaymentFactory.ALA_LOAN_PAYMENT_OTHER);
            loanPayParamsLast = createLoanPayParams();
            paymentFactory.initParams(loanPayParamsLast);
        } else {
            paymentFactory.generatePayment(PaymentFactory.ALA_STAGE_PAYMENT_OTHER);
            stagePayParamsLast = createStagePayParams();
            paymentFactory.initParams(stagePayParamsLast);
        }
    }

    /**
     * 借钱还款成功:密码;微信,银行卡
     */
    private void payLoanRefundSuccess(final WxOrAlaPayModel model) {
        if (BankChannel.KUAIJIE.equals(model.getBankChannel()) || MiscUtils.isEqualsIgnoreCase(model.getNeedCode(), "10")) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("tradeNo", MiscUtils.isEqualsIgnoreCase("10", model.getNeedCode()) ?
                    model.getOutTradeNo() : model.getOrderNo());//这里不是bug，就是这么取得
            jsonObject.put("bankPayChannel", MiscUtils.isEqualsIgnoreCase("10", model.getNeedCode()) ?
                    BankChannel.XIEYI : BankChannel.KUAIJIE);

            new ValidateDialog.Builder(context)
                    .setOnFinishListener(apiResponse -> toLoanRepaymentDetail(apiResponse))
                    .setJsonObject(jsonObject)
                    .setPhone(model.getMobile())
                    .create()
                    .show();
        } else {
            toLoanRepaymentDetail(model);
        }
    }


//    private WxOrAlaPayModel dealApiResponse(String apiResponse) {
//        WxOrAlaPayModel wxOrAlaPayModel = new WxOrAlaPayModel();
//        JSONObject jsonObject = JSONObject.parseObject(apiResponse);
//        if (jsonObject != null && jsonObject.containsKey("result") && !TextUtils.isEmpty(jsonObject.getString("result"))) {
//            JSONObject jsonObject1 = JSONObject.parseObject(jsonObject.getString("result"));
//            if (jsonObject1 != null && jsonObject1.containsKey("data") && !TextUtils.isEmpty(jsonObject1.getString("data"))) {
//                wxOrAlaPayModel = JSONObject.parseObject(jsonObject1.getString("data"), WxOrAlaPayModel.class);
//            }
//        }
//        return wxOrAlaPayModel;
//    }

    private void toLoanRepaymentDetail(WxOrAlaPayModel model) {
        try {
            Intent intent = new Intent();
            intent.putExtra(BundleKeys.LOAN_REPAYMENT_TYPE, TextUtils.isEmpty(model.getType()) ? "" : model.getType());
            intent.putExtra(BundleKeys.LOAN_REPAYMENT_REPAY_ID, String.valueOf(model.getRefId()));
            intent.putExtra(BundleKeys.CASH_LOAN_PAGE_TYPE, pageType);
            intent.putExtra(BundleKeys.LOAN_REPAYMENT_MODEL, model);
            context.setResult(Activity.RESULT_OK);
            ActivityUtils.push(LoanRepaymentDetailActivity.class, intent);
//            ActivityUtils.finish(OrderDetailLegalActivity.class);
            ActivityUtils.pop(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 分期还款成功:密码;微信,银行卡
     */
    private void payStageRefundSuccess(WxOrAlaPayModel model) {
        if (BankChannel.KUAIJIE.equals(model.getBankChannel())) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("tradeNo", model.getOrderNo());
            ValidateDialog dialog = new ValidateDialog.Builder(context)
                    .setOnFinishListener(new ValidateDialog.OnFinishListener() {
                        @Override
                        public void onNext(WxOrAlaPayModel apiResponse) {
                            payLimitDetail(apiResponse);
                        }
                    })
                    .setJsonObject(jsonObject)
                    .setPhone(model.getMobile())
                    .create();
            dialog.show();
        } else {
            payLimitDetail(model);
        }
    }

    private void payLimitDetail(WxOrAlaPayModel model) {
        try {
            Intent intent = new Intent();
            intent.putExtra(BundleKeys.LIMIT_TYPE, model == null ? "" : model.getType());
            intent.putExtra(BundleKeys.LIMIT_REFILD_ID, String.valueOf(model == null ? "" : model.getRefId()));
            context.setResult(Activity.RESULT_OK);
            ActivityUtils.push(LimitDetailActivity.class, intent, REQUEST_STAGE_REPAYMENT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 借钱还款失败:密码;微信,银行卡
     */
    private void payLoanRefundFail(Throwable t) {
        if (t == null) {
            return;
        }
        if (t instanceof ApiException) {
            ApiException apiException = (ApiException) t;
            if (apiException.getCode() == ApiExceptionEnum.REPAYMENT_LOAN_ERROR.getErrorCode()) {
                showTipDialog();
            } else {
                if (apiException.getCode() != ApiExceptionEnum.USER_PWD_INPUT_ERROR.getErrorCode()) {
                    secretFree = true;
                    errMsg = apiException.getMsg();
                }
            }
        }
    }

    /**
     * 立即还款
     */
    public void onRefundClick(View view) {
        if (calModule == null)
            return;
        BigDecimal actualPayAmount = calModule.getExtraAmount();
        if (isFromLoan) {
            if (Constant.LOAN_REPAYMENT_TYPE_PETTY == repaymentType || (Constant.LOAN_REPAYMENT_TYPE_WHITE_COLLAR == repaymentType && "commonSettle".equals(repaymentTypeWhiteCollar))) {
                String amount = displayEditAmount.get();
                if (MiscUtils.isEmpty(amount)) {
                    UIUtils.showToast("还款金额不能为空");
                    return;
                }

                if (new BigDecimal(amount).doubleValue() <= 0) {
                    UIUtils.showToast("还款金额不能为0");
                    return;
                }
            }
        }
        if (actualPayAmount.doubleValue() <= 0) {    // 密码支付
            payByPwd();
        } else {    // 其他支付(微信 + 银行卡)
            payByOther();
        }
    }

    public void unObserverPayInfo() {
        if (paymentFactory != null) {
            paymentFactory.UnObserverPayInfo();
        }
    }

    //public void onEditAmount(View view) {
    //    if (!isFromLoan) {	// 不是现金贷，点击没反应
    //        return;
    //    }
    //    // 借钱还款的时候可以点击修改金额
    //    if (!displayEditAmountView.get()) {
    //        displayEditAmountView.set(true);
    //        displayEditAmount.set(repaymentAmount.toString());
    //    }
    //}

    /**
     * 还款使用优惠卷
     */
    public void couponClick(View view) {
        if (offerModule == null || MiscUtils.isEmpty(offerModule.getAvailableOfferList())) {
            UIUtils.showToast(R.string.coupon_list_no);
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(BundleKeys.SELECT_COUPON_IS_STAGE, true);
        intent.putExtra(BundleKeys.COUPON_LIST_DATA, (Serializable) offerModule.getAvailableOfferList());
        if (offerModule.getOfferModel() != null) {
            intent.putExtra(BundleKeys.COUPON_SELECT_DATA, offerModule.getOfferModel());
        }
        ActivityUtils.push(context, PhoneTicketActivity.class, intent, BundleKeys.REQUEST_CODE_COUPON_GET);
    }

    /**
     * 刷新界面
     */
    private void refreshCouponView() {
        if (offerModule.getValueAmount().compareTo(BigDecimal.ZERO) < 1) {
            displayCouponValue.set(AlaConfig.getResources().getString(R.string.coupon_list_no));
        } else {
            displayCouponValue.set("抵扣" + AppUtils.formatAmount(offerModule.getValueAmount()));
        }
    }


    /**
     * 刷新支付界面
     */
    private void refreshPayInfo() {
        String repaymentInfo = AlaConfig.getResources().getString(R.string.stage_refund_tips,
                AppUtils.formatAmount(calModule.getExtraAmount()));
        SpannableString sp = new SpannableString(repaymentInfo);
        sp.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.normal_color)),
                5, sp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        displayTips.set(sp);
        displayRemainTip.set(AlaConfig.getResources().getString(R.string.stage_refund_remain_tip));
    }

    /**
     * 获取借款信息
     */
    private BigDecimal getCashAmountInfo() {
        String info = repaymentAmount.toString();
        if (displayEditAmountView.get()) {
            info = displayEditAmount.get();
            if (MiscUtils.isEmpty(info)) {
                return BigDecimal.ZERO;
            }
        }
        return new BigDecimal(info);
    }

    /**
     * onActivityResult逻辑
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Logger.w("yf", "StageRefundVM, onActivityResult, requestCode == " + requestCode);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == BundleKeys.REQUEST_CODE_COUPON_GET) {
            if (data.getSerializableExtra(BundleKeys.COUPON_LIST_DATA_RESULT) instanceof MyTicketModel) {
                MyTicketModel couponListItemModel = (MyTicketModel) data.getSerializableExtra(BundleKeys.COUPON_LIST_DATA_RESULT);
                offerModule.setOfferModel(couponListItemModel);
            } else {
                offerModule.setOfferModel(null);
            }
            calModule.calAmountInfo();
            refreshCouponView();
            refreshPayInfo();
        } else if (requestCode == REQUEST_STAGE_REPAYMENT) {
            context.setResult(Activity.RESULT_OK);
            ActivityUtils.pop(context);
        }
    }

    private void showTipDialog() {
        TipsDialog dialog = new TipsDialog(context);
        dialog.setContent(AlaConfig.getResources().getString(R.string.loan_repayment_error_info));
        dialog.setListener(new TipsDialog.MakeSureListener() {
            @Override
            public void onSureClick(Dialog dialog, View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private class TextChangeListener implements ViewBindingAdapter.OnWatchListener {

        @Override
        public void onTextWatch(EditText editText, String inputInfo) {
            if (MiscUtils.isNotEmpty(inputInfo)) {
                if (inputInfo.startsWith(".")) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("0");
                    sb.append(inputInfo);
                    editText.setText(sb.toString());
                    editText.setSelection(sb.toString().length());
                    return;
                }
                String[] info = inputInfo.split("[.]");
                if (info.length > 1) {
                    if (info[1].length() > 2) {
                        int end = inputInfo.length() - (info[1].length() - 2);
                        inputInfo = inputInfo.substring(0, end);
                        editText.setText(inputInfo);
                        editText.setSelection(inputInfo.length());
                        return;
                    }
                }
                if (inputInfo.length() > 2) {
                    if (inputInfo.startsWith("0") && !inputInfo.substring(1, 2).equals(".")) {
                        displayEditAmount.set(inputInfo.substring(1, inputInfo.length()));
                    }
                }
                BigDecimal inputNumber = new BigDecimal(inputInfo);
                if (inputNumber.compareTo(maxAmount) > 0) {
                    inputInfo = maxAmount.toString();
                    editText.setText(inputInfo);
                    return;
                }

                offerModule.calAvailableCoupon(inputNumber);
                calModule.calAmountInfo(inputNumber);
            } else {
                offerModule.calAvailableCoupon(BigDecimal.ZERO);
                calModule.calAmountInfo(BigDecimal.ZERO);
            }

            refreshCouponView();
            refreshPayInfo();
        }
    }

    /**
     * 集分宝切换监听
     */
    private class JfSwitch implements ViewBindingAdapter.SwitchWatch {

        @Override
        public void onSwitch(ToggleButton buttonView) {
            jifengModule.setUse(buttonView.isChecked());
            calModule.calAmountInfo();
            refreshPayInfo();
        }
    }


    /**
     * 余额切换监听
     */
    private class RebateSwitch implements ViewBindingAdapter.SwitchWatch {

        @Override
        public void onSwitch(ToggleButton buttonView) {
            accountModule.setUse(buttonView.isChecked());
            calModule.calAmountInfo();
            refreshPayInfo();
        }
    }

}
