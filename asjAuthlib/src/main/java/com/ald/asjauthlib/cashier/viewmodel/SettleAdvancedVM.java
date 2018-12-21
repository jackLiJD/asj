package com.ald.asjauthlib.cashier.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.ald.asjauthlib.BR;
import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.BusinessApi;
import com.ald.asjauthlib.cashier.BankChannel;
import com.ald.asjauthlib.cashier.model.BillRefundModel;
import com.ald.asjauthlib.cashier.model.ItemDataPair;
import com.ald.asjauthlib.cashier.model.MyTicketModel;
import com.ald.asjauthlib.cashier.model.PaymentModel;
import com.ald.asjauthlib.cashier.model.SettleAdvancedModel;
import com.ald.asjauthlib.cashier.model.WxOrAlaPayModel;
import com.ald.asjauthlib.cashier.params.SettlePayParams;
import com.ald.asjauthlib.cashier.ui.RepaymentStatusActivity;
import com.ald.asjauthlib.cashier.utils.IPaymentCallBack;
import com.ald.asjauthlib.cashier.utils.PaymentFactory;
import com.ald.asjauthlib.databinding.ActivitySettleAdvancedBinding;
import com.ald.asjauthlib.dialog.RepaymentCouponDialog;
import com.ald.asjauthlib.dialog.ValidateDialog;
import com.ald.asjauthlib.utils.StageJumpEnum;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.network.NetworkUtil;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.network.RequestCallBack;
import com.ald.asjauthlib.authframework.core.network.exception.ApiException;
import com.ald.asjauthlib.authframework.core.network.exception.ApiExceptionEnum;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.CanDoubleClick;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.authframework.core.utils.UIUtils;
import com.ald.asjauthlib.authframework.core.vm.BaseRecyclerViewVM;

import java.math.BigDecimal;
import java.util.List;

import com.ald.asjauthlib.tatarka.bindingcollectionadapter.ItemView;
import retrofit2.Call;
import retrofit2.Response;

import static com.ald.asjauthlib.utils.BundleKeys.REQUEST_CODE_BRAND_DIALOG_BANK_CARD;
import static com.ald.asjauthlib.utils.BundleKeys.REQUEST_STAGE_REPAYMENT;


/*
 * Created by luckyliang on 2017/12/15.
 */

public class SettleAdvancedVM extends BaseRecyclerViewVM<ItemSettleAdvancedVM> {


    public ObservableField<String> displayFreeCharge = new ObservableField<>();//减免手续费
    public ObservableField<String> displayRealRepay = new ObservableField<>();//实际支付
    public final ObservableInt displayCouponTxtColor = new ObservableInt(ContextCompat.getColor(AlaConfig.getContext(), R.color.color_969696));
    public final ObservableField<String> displayCouponCount = new ObservableField<>();//可用优惠券数量显示、已选用优惠券金额
    public final ObservableField<String> displayRebateAmount = new ObservableField<>();//余额
    public ObservableBoolean displayUseRabteAmount = new ObservableBoolean(false);//余额开关
    public ObservableBoolean displayUseCouponChecked = new ObservableBoolean(false);//优惠券开关
    public ObservableBoolean displayShowCharge = new ObservableBoolean(true);//手续费
    public ObservableBoolean showDataView = new ObservableBoolean(true);
    public ObservableBoolean tgRebteEnable = new ObservableBoolean(true);
    public ObservableBoolean tgCouponEnable = new ObservableBoolean(true);


    private Activity context;
    private List<SettleAdvancedModel.Bill> bills;//待选list
    private SettleAdvancedModel.Bill billChecked;
    private List<MyTicketModel> ticketModels;//可用优惠券列表

    private PaymentFactory paymentFactory;
    private SettlePayParams settlePayParams = new SettlePayParams();

    private int couponCheckIndex = -1;//使用的优惠券Index
    private BigDecimal rabteAmount;//账户余额

    private int positionChecked = 0;
    private boolean secretFree = false;//是否需要免密支付
    private String errMsg;


    private ActivitySettleAdvancedBinding binding;

    public SettleAdvancedVM(Activity context, ActivitySettleAdvancedBinding binding) {

        this.context = context;
        this.binding = binding;
        initPaymentFactory();
        int rid = context.getIntent().getIntExtra(BundleKeys.INTENT_SETTLE_BILL_ID, 0);
        load(rid);

    }


    /**
     * 获取提前结清数据
     *
     * @param rid ""获取所有提前结清数据  "xxxx"指定bill 来自消费明细页
     */
    public void load(int rid) {
        billChecked = new SettleAdvancedModel.Bill();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("billId", rid == 0 ? "" : Integer.toString(rid));//退款id
        Call<SettleAdvancedModel> call = RDClient.getService(BusinessApi.class).getDataView(jsonObject);
        call.enqueue(new RequestCallBack<SettleAdvancedModel>() {
            @Override
            public void onSuccess(Call<SettleAdvancedModel> call, Response<SettleAdvancedModel> response) {

                if (response.body() == null || response.body().getResult() == null || response.body().getResult().size() == 0) {
                    showDataView.set(false);
                    modelState.setNoData(true, null);
                    modelState.setPrompt(AlaConfig.getResources().getString(R.string.empty_no_data));
                    return;
                }
                bills = response.body().getResult();
                billChecked = bills.get(0);
                refreshLayout(0);//默认选中
            }
        });
    }

    /**
     * 结清点击
     */
    public void onSettleClick(View view) {
        BigDecimal actualPayAmount = getActualPayAmount();
        if (actualPayAmount.doubleValue() <= 0) {
            payByPwd();
        } else {
            payByOther();
        }

    }

    /**
     * 获取实际还款金额
     */
    private BigDecimal getActualPayAmount() {
        if (billChecked == null)
            return BigDecimal.ZERO;
        BigDecimal repayamount = billChecked.getAmount();//原选中金额
        if (displayUseRabteAmount.get()) {
            repayamount = repayamount.subtract(rabteAmount);
        }
        if (displayUseCouponChecked.get()) {
            repayamount = repayamount.subtract(ticketModels.get(couponCheckIndex).getAmount());
        }
        if (repayamount.compareTo(BigDecimal.ZERO) < 0)
            repayamount = BigDecimal.ZERO;
        return repayamount;
    }


    /**
     * 刷新优惠券布局
     */
    private void refreshCouponLayout(final String billId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("billIds", billId);
        Call<BillRefundModel> call = RDClient.getService(BusinessApi.class).getRepaymentConfirmInfoV1(jsonObject);
        NetworkUtil.showCutscenes(context, call);
        call.enqueue(new RequestCallBack<BillRefundModel>() {
            @Override
            public void onSuccess(Call<BillRefundModel> call, Response<BillRefundModel> response) {
                BillRefundModel billRefundModel = response.body();
                ticketModels = billRefundModel.getCouponList();
                rabteAmount = billRefundModel.getRebateAmount();
                couponCheckIndex = -1;
                if (MiscUtils.isEmpty(ticketModels)) {
                    displayCouponCount.set("暂无可用优惠券");
                    displayCouponTxtColor.set(ContextCompat.getColor(AlaConfig.getContext(), R.color.color_969696));
                    displayUseCouponChecked.set(false);
                    tgCouponEnable.set(false);

                } else {
                    displayUseCouponChecked.set(false);
                    displayCouponCount.set(ticketModels.size() + "张可用,点击查看");
                    displayCouponTxtColor.set(ContextCompat.getColor(AlaConfig.getContext(), R.color.color_969696));
                }

                displayRebateAmount.set(billRefundModel.getRebateAmount().toString() + "元");
                if (billRefundModel.getRebateAmount().equals(BigDecimal.ZERO)) {
                    tgRebteEnable.set(false);
                }
            }
        });
    }


    /**
     * 打开优惠券列表，点击
     */
    public void onOpenCouponList(View view) {
        if (displayUseCouponChecked.get()) {
            displayUseCouponChecked.set(false);
            displayCouponCount.set(ticketModels.size() + "张可用,点击查看");
            displayCouponTxtColor.set(ContextCompat.getColor(AlaConfig.getContext(), R.color.color_969696));
            couponCheckIndex = -1;
            setActualAmount();
        } else {
            if (ticketModels == null || ticketModels.size() == 0) {
                UIUtils.showToast("暂无可用优惠券");
                displayCouponTxtColor.set(ContextCompat.getColor(AlaConfig.getContext(), R.color.color_969696));
                displayUseCouponChecked.set(false);
                return;
            }
            RepaymentCouponDialog.Builder builder = new RepaymentCouponDialog.Builder(context);
            builder.setData(ticketModels, couponCheckIndex)
                    .setSureClickListener(position -> {
                        if (position > -1) {
                            displayCouponCount.set("¥" + ticketModels.get(position).getAmount() + "元");
                            displayCouponTxtColor.set(ContextCompat.getColor(AlaConfig.getContext(), R.color.color_ff5546));
                            couponCheckIndex = position;
                            displayUseCouponChecked.set(true);
                            setActualAmount();
                        } else {
                            displayUseCouponChecked.set(false);
                        }
                    })
                    .create()
                    .show();
        }
    }

    /**
     * 使用余额
     */
    @CanDoubleClick
    public void onUserRabte(View view) {
        displayUseRabteAmount.set(!displayUseRabteAmount.get());
        setActualAmount();
    }


    /**
     * 其他支付方式：微信和银行卡密码支付
     */

    private void payByOther() {
        paymentFactory.generatePayment(PaymentFactory.ALA_STAGE_PAYMENT_OTHER);
        settlePayParams = createStagePayParams(true);
        paymentFactory.initParams(settlePayParams);

    }

    /**
     * 余额不小于结清总额
     */
    private void payByPwd() {
        settlePayParams = createStagePayParams(false);
        paymentFactory.generatePayment(PaymentFactory.ALA_STAGE_PAYMENT_PWD);
        paymentFactory.initParams(settlePayParams);
    }


    /**
     * 提前结清参数
     */
    private SettlePayParams createStagePayParams(boolean payByOther) {
        settlePayParams.setCardId(String.valueOf(-2));
        settlePayParams.setBillId(Long.toString(billChecked.getDetailList().get(0).getBillId()));

        settlePayParams.setCouponId(couponCheckIndex == -1 ? "" : Long.toString(ticketModels.get(couponCheckIndex).getRid()));
        settlePayParams.setRepayAmount(billChecked.getAmount().toString());

        settlePayParams.setRabteAmount(displayUseRabteAmount.get() ? rabteAmount.toString() : "0");//账户余额
        settlePayParams.setUserRabte(displayUseRabteAmount.get());
        if (payByOther) {
            settlePayParams.secretFree = secretFree;
            settlePayParams.errMsg = errMsg;
        }
        return settlePayParams;
    }

    /**
     * 初始化支付工厂
     */
    private void initPaymentFactory() {

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
                secretFree = false;
                errMsg = "";
                WxOrAlaPayModel wxModel = null;
                if (model instanceof WxOrAlaPayModel) {
                    wxModel = (WxOrAlaPayModel) model;
                    wxModel.setMobile(settlePayParams.mobile);
                    wxModel.setBankChannel(settlePayParams.bankChannel);
                } else if (model instanceof PaymentModel) {
                    wxModel = new WxOrAlaPayModel();
                    wxModel.setType(((PaymentModel) model).getType());
                    wxModel.setRefId(((PaymentModel) model).getRefId());
                    wxModel.setBankChannel(settlePayParams.bankChannel);
                    wxModel.setMobile(settlePayParams.mobile);
                }
                payStageRefundSuccess(wxModel);
            }


            @Override
            public void onCancel(Throwable t) {
                if (t instanceof ApiException) {
                    ApiException apiException = (ApiException) t;
                    if (apiException.getCode() != ApiExceptionEnum.USER_PWD_INPUT_ERROR.getErrorCode()) {
                        secretFree = true;
                        errMsg = apiException.getMsg();
                    }
                }
            }
        });

    }


    /**
     * 分期还款成功:密码;微信,银行卡
     */
    private void payStageRefundSuccess(final WxOrAlaPayModel model) {
        if (model != null && (settlePayParams != null && BankChannel.KUAIJIE.equals(settlePayParams.bankChannel)
                || MiscUtils.isEqualsIgnoreCase("10", model.getNeedCode()))) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("tradeNo", MiscUtils.isEqualsIgnoreCase("10", model.getNeedCode()) ? model.getOutTradeNo() : model.getOrderNo());
            jsonObject.put("bankPayChannel", MiscUtils.isEqualsIgnoreCase("10", model.getNeedCode()) ? BankChannel.XIEYI : BankChannel.KUAIJIE);
            new ValidateDialog.Builder(context)
                    .setPhone(model.getMobile())
                    .setJsonObject(jsonObject)
                    .setOnFinishListener(this::paySuccess)
                    .create()
                    .show();
        } else {
            paySuccess(model);
        }
    }

    private void paySuccess(WxOrAlaPayModel model) {
        if (model == null) return;
        Intent intent = new Intent();
        intent.putExtra(BundleKeys.LIMIT_TYPE, model.getType());
        intent.putExtra(BundleKeys.LIMIT_REFILD_ID, String.valueOf(model.getRefId()));
        String stage = context.getIntent().getStringExtra(BundleKeys.STAGE_JUMP);
        intent.putExtra(BundleKeys.STAGE_JUMP, MiscUtils.isEmpty(stage) ? StageJumpEnum.STAGE_TO_REPAY_H5 : stage);
        context.setResult(Activity.RESULT_OK);
        ActivityUtils.push(RepaymentStatusActivity.class, intent, REQUEST_STAGE_REPAYMENT);
    }


    /**
     * 刷新页面
     *
     * @param position 选中位置
     */
    public void refreshLayout(int position) {
        //刷新列表
        items.clear();
        for (int i = 0; i < bills.size(); i++) {
            final SettleAdvancedModel.Bill bill = bills.get(i);
            ItemDataPair itemDataPair = new ItemDataPair(bill, 0);
            //                @Override
//                public void onSubmitClick(int position) {
//                    //选中查看的item
//                    billChecked = bills.get(position);
//                    refreshLayout(position);
//                }
            ItemSettleAdvancedVM itemSettleAdvancedVM = new ItemSettleAdvancedVM(context, itemDataPair,
                    i == position, getActualPayAmount().toString(), getFreeCharge(bill).toString()
                    , i, (itemChecked, isChecked) -> {
                //切换checker 关闭优惠券开关、打开时重新获取可用
                displayUseCouponChecked.set(false);
                couponCheckIndex = -1;
                if (isChecked)
                    billChecked = bills.get(itemChecked.position);
                else
                    billChecked = null;
                //                    refreshLayout(position);

                //取消选中之前item
                ItemSettleAdvancedVM itemFormer = items.get(positionChecked);
                itemFormer.isChecked.set(false);
                items.set(positionChecked, itemFormer);

                //选中当前item
                itemChecked.isChecked.set(isChecked);
                items.set(itemChecked.position, itemChecked);

                //设置当前位置
                if (isChecked)
                    positionChecked = itemChecked.position;

                setActualAmount();

            });
            items.add(itemSettleAdvancedVM);
        }

        refreshCouponLayout(Long.toString(billChecked.getDetailList().get(0).getBillId()));//请求优惠券等信息
        setActualAmount();

    }

    /**
     * 显示实际还款金额和手续费
     */
    private void setActualAmount() {
        BigDecimal actual = getActualPayAmount();
        if (billChecked == null) {
            //提交置灰
            binding.btnSubmit.setEnabled(false);
        } else {
            binding.btnSubmit.setEnabled(true);
        }
        displayRealRepay.set(Double.toString(actual.doubleValue()) + "元");
        if (billChecked == null)
            displayFreeCharge.set("0.00元");
        else
            displayFreeCharge.set(getFreeCharge(billChecked) + "元");

    }

    /**
     * 计算减免手续费
     *
     * @return 选中bill可减免的手续费
     */
    private BigDecimal getFreeCharge(SettleAdvancedModel.Bill bill) {
        BigDecimal charge = new BigDecimal(0);
        List<SettleAdvancedModel.Detail> detailList = bill.getDetailList();
        for (int i = 0; i < detailList.size(); i++) {
            if (detailList.get(i).getFree())
                charge = charge.add(detailList.get(i).getPoundAmount());
        }
        return charge;
    }


    @Override
    protected void selectView(ItemView itemView, int position, ItemSettleAdvancedVM item) {
        itemView.set(BR.viewModel, R.layout.item_settle_advanced_list);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_BRAND_DIALOG_BANK_CARD) {
            secretFree = false;
            errMsg = "";
        }
    }

}
