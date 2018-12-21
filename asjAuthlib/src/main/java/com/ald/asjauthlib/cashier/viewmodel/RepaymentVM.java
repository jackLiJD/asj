package com.ald.asjauthlib.cashier.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.BusinessApi;
import com.ald.asjauthlib.cashier.BankChannel;
import com.ald.asjauthlib.cashier.model.BillRefundModel;
import com.ald.asjauthlib.cashier.model.MyRepaymentDtlModel;
import com.ald.asjauthlib.cashier.model.MyRepaymentModel;
import com.ald.asjauthlib.cashier.model.MyTicketModel;
import com.ald.asjauthlib.cashier.model.PaymentModel;
import com.ald.asjauthlib.cashier.model.WxOrAlaPayModel;
import com.ald.asjauthlib.cashier.params.StagePayParams;
import com.ald.asjauthlib.cashier.ui.RepaymentStatusActivity;
import com.ald.asjauthlib.cashier.utils.IPaymentCallBack;
import com.ald.asjauthlib.cashier.utils.PaymentFactory;
import com.ald.asjauthlib.databinding.ActivityRepaymentBinding;
import com.ald.asjauthlib.dialog.RepaymentBottomDialog;
import com.ald.asjauthlib.dialog.RepaymentCouponDialog;
import com.ald.asjauthlib.dialog.ValidateDialog;
import com.ald.asjauthlib.event.Event;
import com.ald.asjauthlib.event.EventBusUtil;
import com.ald.asjauthlib.event.EventCode;
import com.ald.asjauthlib.utils.AnimationUtils;
import com.ald.asjauthlib.utils.AppUtils;
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
import com.ald.asjauthlib.authframework.core.vm.BaseVM;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.ald.asjauthlib.utils.BundleKeys.REQUEST_CODE_BRAND_DIALOG_BANK_CARD;
import static com.ald.asjauthlib.utils.BundleKeys.REQUEST_STAGE_REPAYMENT;


/**
 * Created by luckyliang on 2017/12/8.
 * 还款页面(4.0.4)
 */

public class RepaymentVM extends BaseVM {

    public final ObservableField<String> displayOverdueAmount = new ObservableField<>();//逾期总额
    public final ObservableField<String> displayOverdueCheckStatusDtl = new ObservableField<>();//逾期选择统计
    public final ObservableBoolean displayOverdueCheckStatus = new ObservableBoolean(true);//逾期选择状态

    public final ObservableField<String> displayCurrentAmount = new ObservableField<>();//当月总额
    public final ObservableField<String> displayCurrentCheckStatusDtl = new ObservableField<>();//当月账单选择状态
    public final ObservableBoolean displayCurrentCheckStatus = new ObservableBoolean(true);//已出选择状态
    public final ObservableField<String> displayOutMonth = new ObservableField<>();//已出月份

    public final ObservableField<String> displayNextAmount = new ObservableField<>();//下月总额
    public final ObservableField<String> displayNextCheckStatusDtl = new ObservableField<>();//下月账单选择状态
    public final ObservableBoolean displayNextCheckStatus = new ObservableBoolean(true);//未出选择状态
    public final ObservableField<String> displayNotOutMonth = new ObservableField<>();//下月份


    public final ObservableField<String> displayRebateAmount = new ObservableField<>();
    public final ObservableBoolean displayUseCouponChecked = new ObservableBoolean(false);
    public final ObservableBoolean displayUseRebate = new ObservableBoolean(false);

    public final ObservableField<String> displayCheckedAmount = new ObservableField<>();//实际还款，已选择还款

    public final ObservableInt displayCouponTxtColor = new ObservableInt(0);
    public final ObservableField<String> displayCouponCount = new ObservableField<>();


    private ActivityRepaymentBinding binding;
    private Activity context;
    private MyRepaymentModel myRepaymentModel;
    private String billIds = "";


    private List<Long> overdueBillsChecked;
    private List<Long> outBillsChecked;
    private List<Long> notOutBillChecked;
    private BigDecimal overdueCheckedAmount;
    private BigDecimal outCheckedAmount;
    private BigDecimal notoutCheckedAmout;

    private BigDecimal rabteAmount;//账户余额
    private List<MyTicketModel> myTicketModels;//优惠券列表
    private int useTicketIndex;//使用的优惠券index

    private PaymentFactory paymentFactory;
    private StagePayParams stagePayParams = new StagePayParams();
    private boolean secretFree = false;//是否需要免密支付
    private String errMsg;


    public RepaymentVM(Activity context, ActivityRepaymentBinding binding) {
        this.binding = binding;
        this.context = context;
        EventBusUtil.register(this);
        load();
    }

    /**
     * 计算设置实际还款金额
     */
    private void updateExtraAmout() {
        displayCheckedAmount.set(Double.toString(getActualPayAmount().doubleValue()) + "元");
    }

    /**
     * 获取实际还款金额
     */
    private BigDecimal getActualPayAmount() {
        BigDecimal repaymentAmount = getAmountChecked();
        if (binding.toggleRabte.isChecked()) {
            repaymentAmount = repaymentAmount.subtract(rabteAmount == null ? BigDecimal.ZERO : rabteAmount);
        }
        if (displayUseCouponChecked.get() && useTicketIndex > -1) {
            repaymentAmount = repaymentAmount.subtract(myTicketModels.get(useTicketIndex).getAmount());
        }
        if (repaymentAmount.compareTo(BigDecimal.ZERO) < 0)
            repaymentAmount = BigDecimal.ZERO;
        return repaymentAmount;
    }

    public void load() {
        overdueBillsChecked = new ArrayList<>();
        outBillsChecked = new ArrayList<>();
        notOutBillChecked = new ArrayList<>();
        overdueCheckedAmount = BigDecimal.ZERO;
        outCheckedAmount = BigDecimal.ZERO;
        notoutCheckedAmout = BigDecimal.ZERO;
        displayCouponTxtColor.set(ContextCompat.getColor(context, R.color.color_969696));
        Call<MyRepaymentModel> call = RDClient.getService(BusinessApi.class).getMyRepaymentV1();
        call.enqueue(new RequestCallBack<MyRepaymentModel>() {
                         @Override
                         public void onSuccess(Call<MyRepaymentModel> call, Response<MyRepaymentModel> response) {
                             if (response.body() == null || (MiscUtils.isEmpty(response.body().getOutBills())
                                     && MiscUtils.isEmpty(response.body().getNotOutBills())
                                     && MiscUtils.isEmpty(response.body().getOverdueBills()))) {
                                 modelState.setNoData(true, null);
                                 modelState.setPrompt("没有数据啦");
                                 return;
                             }
                             myRepaymentModel = response.body();
                             initPayFactory();
                             displayBillsData(myRepaymentModel);
                         }
                     }
        );


    }

    /**
     * 拼接bills
     */
    private String getBills(List<Long> sBillIds, String ids) {
        String strBill;
        if (ids.length() > 0)
            ids += ",";
        StringBuilder builder = new StringBuilder(ids);
        if (MiscUtils.isNotEmpty(sBillIds)) {
            for (Long id : sBillIds) {
                strBill = Long.toString(id);
                if (!"0".equals(strBill)) {
                    builder.append(strBill);
                    builder.append(",");
                }
            }
            ids = builder.toString();
            if (MiscUtils.isNotEmpty(ids)) {
                ids = ids.substring(0, ids.length() - 1);
            }
        }
        return ids;
    }


    /**
     * 获取/
     * 展示可用优惠券数量
     */
    private void getCouponInfo(String billIds) {
        displayUseCouponChecked.set(false);
        displayCouponTxtColor.set(ContextCompat.getColor(context, R.color.color_969696));
        if (MiscUtils.isEmpty(billIds)) {
            displayCouponCount.set("无可用优惠券");
            binding.toggleCoupon.setEnabled(false);
            binding.toggleCoupon.setChecked(false);
            updateExtraAmout();
            return;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("billIds", billIds);
        Call<BillRefundModel> callCoupon = RDClient.getService(BusinessApi.class).getRepaymentConfirmInfoV1(jsonObject);
        NetworkUtil.showCutscenes(context, callCoupon);
        callCoupon.enqueue(new RequestCallBack<BillRefundModel>() {
            @Override
            public void onSuccess(Call<BillRefundModel> call, Response<BillRefundModel> response) {
                BillRefundModel billRefundModel = response.body();
                myTicketModels = billRefundModel.getCouponList();
                useTicketIndex = -1;
                if (myTicketModels == null || myTicketModels.size() == 0) {

                    displayCouponCount.set("无可用优惠券");
                    binding.toggleCoupon.setEnabled(false);
                    binding.toggleCoupon.setChecked(false);
                    displayCouponTxtColor.set(ContextCompat.getColor(context, R.color.color_969696));
                } else {
                    displayCouponCount.set(myTicketModels.size() + "张可用，点击查看");
                    binding.toggleCoupon.setEnabled(true);
                }
                rabteAmount = billRefundModel.getRebateAmount();
                updateExtraAmout();

            }
        });
    }


    /**
     * 显示账单信息
     */
    private void displayBillsData(MyRepaymentModel myRepaymentModel) {
//12月30日新需求 有逾期勾选逾期 没逾期勾选当月 逾期当月都没有勾选下月

        displayRebateAmount.set(Double.toString(myRepaymentModel.getRebateAmount().doubleValue()) + "元");//账户余额
        if (myRepaymentModel.getRebateAmount().compareTo(BigDecimal.ZERO) == 0) {
            binding.toggleRabte.setChecked(false);
            binding.toggleRabte.setEnabled(false);
        }
        String bills = "";
        if (myRepaymentModel.getOverdueMoney().compareTo(BigDecimal.ZERO) > 0) {
            AnimationUtils.showAndHiddenAnimation(binding.rlOverdue, AnimationUtils.AnimationState.STATE_SHOW, 800);
            overdueCheckedAmount = myRepaymentModel.getOverdueMoney();
            displayOverdueAmount.set(AppUtils.formatAmount(overdueCheckedAmount) + "元");
            bills = getBills(overdueBillsChecked, bills);
            overdueBillsChecked = strToLong(myRepaymentModel.getOverdueBills());
            displayOverdueCheckStatusDtl.set("已全选");
        }
        if (myRepaymentModel.getOutMoney().compareTo(BigDecimal.ZERO) > 0) {
            AnimationUtils.showAndHiddenAnimation(binding.rlCurrentMonth, AnimationUtils.AnimationState.STATE_SHOW, 800);
            outCheckedAmount = myRepaymentModel.getOutMoney();
            displayCurrentAmount.set(AppUtils.formatAmount(outCheckedAmount) + "元");
            displayOutMonth.set(Integer.toString(myRepaymentModel.getMonth()) + "月账单");
            bills = getBills(outBillsChecked, bills);
            outBillsChecked = strToLong(myRepaymentModel.getOutBills());
            displayCurrentCheckStatusDtl.set("已全选");
        }
        if (myRepaymentModel.getNotOutMoney().compareTo(BigDecimal.ZERO) > 0) {
            //如果逾期本月都没有才勾选下月的，否则不选中
            AnimationUtils.showAndHiddenAnimation(binding.rlNextMonth, AnimationUtils.AnimationState.STATE_SHOW, 800);
            if (binding.rlOverdue.getVisibility() == View.GONE && binding.rlCurrentMonth.getVisibility() == View.GONE) {
                notoutCheckedAmout = myRepaymentModel.getNotOutMoney();
                getBills(notOutBillChecked, bills);
                notOutBillChecked = strToLong(myRepaymentModel.getNotOutBills());
                displayNextCheckStatusDtl.set("已全选");
                displayNextCheckStatus.set(true);
            } else {
                displayNextCheckStatusDtl.set("已选0个");
                displayNextCheckStatus.set(false);
            }
            displayNextAmount.set(AppUtils.formatAmount(notoutCheckedAmout) + "元");
            displayNotOutMonth.set(Integer.toString(myRepaymentModel.getNextMonth()) + "月账单");
        }

        setBillIds();
        getCouponInfo(billIds);


    }

    /**
     * 打开优惠券列表
     */
    @CanDoubleClick
    public void onOpenCouponList(View view) {
        if (myTicketModels == null || myTicketModels.size() == 0) {
            UIUtils.showToast("暂无可用优惠券");
            displayCouponTxtColor.set(ContextCompat.getColor(context, R.color.color_969696));
            return;
        }
        if (displayUseCouponChecked.get()) {
            displayUseCouponChecked.set(false);
            displayCouponCount.set(myTicketModels.size() + "张可用，点击查看");
            displayCouponTxtColor.set(ContextCompat.getColor(context, R.color.color_969696));
            useTicketIndex = -1;
            updateExtraAmout();

        } else {
            openCouponDialog();
        }

    }

    /**
     * 打开优惠券Dialog
     */
    private void openCouponDialog() {
        RepaymentCouponDialog.Builder builder = new RepaymentCouponDialog.Builder(context);
        builder.setData(myTicketModels, -1).setSureClickListener(new RepaymentCouponDialog.Builder.OnSureClickListener() {
            @Override
            public void onClick(int position) {
                if (position == -1 || myTicketModels == null) {
                    if (myTicketModels == null || myTicketModels.size() == 0) {
                        displayCouponCount.set("无可用优惠券");
                    } else {
                        displayCouponCount.set(myTicketModels.size() + "张可用，点击查看");
                    }
                    displayCouponTxtColor.set(ContextCompat.getColor(context, R.color.color_969696));
                    binding.toggleCoupon.setChecked(false);
                    displayUseCouponChecked.set(false);
                    useTicketIndex = -1;
                } else {
                    useTicketIndex = position;
                    displayUseCouponChecked.set(true);
                    displayCouponCount.set("¥" + myTicketModels.get(useTicketIndex).getAmount() + "元");
                    displayCouponTxtColor.set(ContextCompat.getColor(context, R.color.color_ff5546));
                }
                updateExtraAmout();
            }
        }).create().show();
    }

    /**
     * 优惠券开关点击
     */
    @CanDoubleClick
    public void onCouponClick(View view) {
        if (displayUseCouponChecked.get()) {
            displayUseCouponChecked.set(false);
            if (myTicketModels == null || myTicketModels.size() == 0) {
                displayCouponCount.set("无可用优惠券");
            } else
                displayCouponCount.set(myTicketModels.size() + "张可用，点击查看");
            displayCouponTxtColor.set(ContextCompat.getColor(view.getContext(), R.color.color_969696));
            updateExtraAmout();
        } else {
            binding.toggleCoupon.setChecked(false);
            displayUseCouponChecked.set(false);
            openCouponDialog();
        }
    }


    /**
     * 使用余额开关点击
     */
    @CanDoubleClick
    public void onRabteClick(View view) {
        if (displayUseRebate.get()) {
            displayUseRebate.set(false);
            binding.toggleRabte.setChecked(false);
        } else {
            displayUseRebate.set(true);
            binding.toggleRabte.setChecked(true);
        }
        updateExtraAmout();
    }

    /**
     * 查看逾期账单
     */
    public void onOverDueClick(View view) {
        showDtlDialog("overdue", overdueBillsChecked);
    }

    /**
     * 查看已出账单
     */
    public void onOutClick(View view) {
        if (!displayOverdueCheckStatus.get()) {
            UIUtils.showToast(AlaConfig.getResources().getString(R.string.repayment_overdue_first));
            return;
        }
        showDtlDialog("out", outBillsChecked);

    }

    /**
     * 未出账单
     */
    public void onNotOutClick(View view) {
        if (!displayOverdueCheckStatus.get()) {
            UIUtils.showToast(AlaConfig.getResources().getString(R.string.repayment_overdue_first));
            return;
        }
        showDtlDialog("notOut", notOutBillChecked);

    }

    /**
     * @param type 逾期、已出 或是未出
     */
    private void showDtlDialog(String type, final List<Long> billsChecked) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", type);
        Call<MyRepaymentDtlModel> call = RDClient.getService(BusinessApi.class).getMyRepaymentDetailV1(jsonObject);
        call.enqueue(new RequestCallBack<MyRepaymentDtlModel>() {
            @Override
            public void onSuccess(Call<MyRepaymentDtlModel> call, Response<MyRepaymentDtlModel> response) {
                if (response.body() == null || response.body().getBillList().size() == 0) {
                    UIUtils.showToast("暂无账单详情");
                    return;
                }
                MyRepaymentDtlModel model = response.body();
                RepaymentBottomDialog.Builder builder = new RepaymentBottomDialog.Builder(context);
                builder.setCheckedItems(billsChecked)
                        .setAdapter(model)
                        .setListener(new RepaymentBottomDialog.Builder.OnCheckedCompleteListener() {
                            @Override
                            public void onCheckedComplete(String status, List<Long> checkedItems, BigDecimal checkedAmout) {
                                //底部弹框选择完毕
                                //账单类型 overdue 逾期 out 某月已出 notOut 未出账单
                                switch (status) {
                                    case "overdue":
                                        if (checkedItems.size() == myRepaymentModel.getOverdueBills().size()) {
                                            displayOverdueCheckStatus.set(true);
                                            displayOverdueCheckStatusDtl.set("已全选");
                                        } else {
                                            displayOverdueCheckStatus.set(checkedItems.size() != 0);
                                            displayOverdueCheckStatusDtl.set("已选" + checkedItems.size() + "个");
                                        }
                                        overdueBillsChecked = checkedItems;
                                        overdueCheckedAmount = checkedAmout;
                                        displayOverdueAmount.set(AppUtils.formatAmount(checkedAmout) + "元");
                                        break;
                                    case "out":
                                        if (checkedItems.size() == myRepaymentModel.getOutBills().size()) {
                                            displayCurrentCheckStatus.set(true);
                                            displayCurrentCheckStatusDtl.set("已全选");
                                        } else {

                                            displayCurrentCheckStatus.set(checkedItems.size() != 0);
                                            displayCurrentCheckStatusDtl.set("已选" + checkedItems.size() + "个");

                                        }
                                        outCheckedAmount = checkedAmout;
                                        outBillsChecked = checkedItems;
                                        displayCurrentAmount.set(AppUtils.formatAmount(checkedAmout) + "元");
                                        break;
                                    case "notOut":
                                        if (checkedItems.size() == myRepaymentModel.getNotOutBills().size()) {
                                            displayNextCheckStatus.set(true);
                                            displayNextCheckStatusDtl.set("已全选");

                                        } else {
                                            displayNextCheckStatus.set(checkedItems.size() != 0);
                                            displayNextCheckStatusDtl.set("已选" + checkedItems.size() + "个");
                                        }
                                        notoutCheckedAmout = checkedAmout;
                                        notOutBillChecked = checkedItems;
                                        displayNextAmount.set(AppUtils.formatAmount(checkedAmout.toString()) + "元");
                                        break;
                                }
                                //重新获取优惠券,计算总金额
                                setBillIds();
                                getCouponInfo(billIds);

                            }
                        }).create().show();
            }
        });

    }

    /**
     * checker单击事件
     */
    @CanDoubleClick
    public void onCheckListener(View view) {

        int i = view.getId();
        if (i == R.id.checker) {
            displayOverdueCheckStatus.set(!displayOverdueCheckStatus.get());
            if (displayOverdueCheckStatus.get()) {
                overdueCheckedAmount = myRepaymentModel.getOverdueMoney();
                overdueBillsChecked = strToLong(myRepaymentModel.getOverdueBills());
                displayOverdueCheckStatusDtl.set("已全选");
            } else {
                overdueCheckedAmount = BigDecimal.ZERO;
                overdueBillsChecked = new ArrayList<>();
                displayOverdueCheckStatusDtl.set("已选0个");
            }
            displayOverdueAmount.set(AppUtils.formatAmount(overdueCheckedAmount) + "元");

        } else if (i == R.id.checker_current_month) {
            if (!displayCurrentCheckStatus.get() && myRepaymentModel.getOverdueBills() != null && overdueBillsChecked != null && (overdueBillsChecked.size() < myRepaymentModel.getOverdueBills().size())) {
                binding.checkerCurrentMonth.setChecked(false);
                UIUtils.showToast(AlaConfig.getResources().getString(R.string.repayment_overdue_first));
                return;
            }
            displayCurrentCheckStatus.set(!displayCurrentCheckStatus.get());
            if (displayCurrentCheckStatus.get()) {
                outCheckedAmount = myRepaymentModel.getOutMoney();
                outBillsChecked = (strToLong(myRepaymentModel.getOutBills()));
                displayCurrentCheckStatusDtl.set("已全选");
            } else {
                outCheckedAmount = BigDecimal.ZERO;
                outBillsChecked = new ArrayList<>();
                displayCurrentCheckStatusDtl.set("已选0个");
            }
            displayCurrentAmount.set(AppUtils.formatAmount(outCheckedAmount) + "元");

        } else if (i == R.id.checker_next_month) {
            if (!displayNextCheckStatus.get() && myRepaymentModel.getOverdueBills() != null && overdueBillsChecked != null && overdueBillsChecked.size() < myRepaymentModel.getOverdueBills().size()) {
                binding.checkerNextMonth.setChecked(false);
                UIUtils.showToast(AlaConfig.getResources().getString(R.string.repayment_overdue_first));
                return;
            }
            displayNextCheckStatus.set(!displayNextCheckStatus.get());
            if (displayNextCheckStatus.get()) {
                notoutCheckedAmout = myRepaymentModel.getNotOutMoney();
                notOutBillChecked = (strToLong(myRepaymentModel.getNotOutBills()));
                displayNextCheckStatusDtl.set("已全选");
            } else {
                notoutCheckedAmout = BigDecimal.ZERO;
                notOutBillChecked = new ArrayList<>();
                displayNextCheckStatusDtl.set("已选0个");
            }
            displayNextAmount.set(AppUtils.formatAmount(notoutCheckedAmout) + "元");

        }

        //拼接billid字符串，请求可用优惠券
        setBillIds();
        getCouponInfo(billIds);
    }


    private void setBillIds() {
        billIds = "";
        if (overdueBillsChecked.size() > 0) {//逾期
            billIds = getBills(overdueBillsChecked, billIds);
//            addCheckedBills(myRepaymentModel.getOverdueBills());
        }
        if (outBillsChecked.size() > 0) {
            billIds = getBills(outBillsChecked, billIds);

        }
        if (notOutBillChecked.size() > 0) {
            billIds = getBills(notOutBillChecked, billIds);
        }

    }


    public void onSubmit(View view) {
        if (overdueBillsChecked.size() + outBillsChecked.size() + notOutBillChecked.size() == 0) {
            UIUtils.showToast("请先选择还款账单");
            return;
        }
        if (myRepaymentModel.getOverdueBills() != null && ((outBillsChecked.size() + notOutBillChecked.size()) > 0) && overdueBillsChecked.size() < myRepaymentModel.getOverdueBills().size()) {
            UIUtils.showToast("请优先选择逾期账单~");
            return;
        }

        setBillIds();
        updateExtraAmout();
        BigDecimal actualPayAmount = getActualPayAmount();
        if (actualPayAmount.doubleValue() <= 0) {    // 密码支付
            payByPwd();
        } else {    // 其他支付(微信 + 银行卡)
            payByOther();
        }
    }

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
                secretFree = false;
                errMsg = "";
                WxOrAlaPayModel wxModel = null;
                if (model instanceof WxOrAlaPayModel) {
                    wxModel = (WxOrAlaPayModel) model;
                    wxModel.setBankChannel(stagePayParams.bankChannel);
                    wxModel.setMobile(stagePayParams.mobile);
                } else if (model instanceof PaymentModel) {
                    wxModel = new WxOrAlaPayModel();
                    wxModel.setType(((PaymentModel) model).getType());
                    wxModel.setRefId(((PaymentModel) model).getRefId());
                    wxModel.setBankChannel("");
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
     * app内密码支付：分为借钱还款和消费分期还款
     */
    private void payByPwd() {
        // 分期还款 pwd非微信方式
        stagePayParams = createStagePayParams(false);
        paymentFactory.generatePayment(PaymentFactory.ALA_STAGE_PAYMENT_PWD);
        paymentFactory.initParams(stagePayParams);
    }

    /**
     * 其他支付方式：微信和银行卡密码支付
     */
    private void payByOther() {
        stagePayParams = createStagePayParams(true);
        paymentFactory.generatePayment(PaymentFactory.ALA_STAGE_PAYMENT_OTHER);
        paymentFactory.initParams(stagePayParams);

    }

    private StagePayParams createStagePayParams(boolean payOther) {
        stagePayParams.cardId = String.valueOf(-2);
        stagePayParams.billIds = billIds;
        if (useTicketIndex != -1 && myTicketModels != null) {
            stagePayParams.couponId = String.valueOf(myTicketModels.get(useTicketIndex).getRid());
        }
        stagePayParams.repaymentAmount = getAmountChecked().toString();
        stagePayParams.rebateAmount = (displayUseRebate.get() && rabteAmount != null) ? rabteAmount.toString() : "0";
        stagePayParams.actualAmount = getActualPayAmount().toString();
        if (payOther) {
            stagePayParams.secretFree = secretFree;
            stagePayParams.errMsg = errMsg;
        }
        return stagePayParams;
    }


    /**
     * 分期还款成功:密码;微信,银行卡
     */
    private void payStageRefundSuccess(WxOrAlaPayModel model) {
        if (model == null) return;
        if (stagePayParams != null && (BankChannel.KUAIJIE.equals(model.getBankChannel()) || MiscUtils.isEqualsIgnoreCase("10", model.getNeedCode()))) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("tradeNo", MiscUtils.isEqualsIgnoreCase("10", model.getNeedCode()) ? model.getOutTradeNo() : model.getOrderNo());
            jsonObject.put("bankPayChannel", MiscUtils.isEqualsIgnoreCase("10", model.getNeedCode()) ? BankChannel.XIEYI : BankChannel.KUAIJIE);
            new ValidateDialog.Builder(context)
                    .setPhone(stagePayParams.mobile)
                    .setJsonObject(jsonObject)
                    .setOnFinishListener(apiResponse -> paySuccess(apiResponse))
                    .create()
                    .show();
        } else {
            paySuccess(model);
        }
    }

    private void paySuccess(WxOrAlaPayModel model) {
        Intent intent = new Intent();
        intent.putExtra(BundleKeys.LIMIT_REFILD_ID, String.valueOf(model.getRefId()));
        intent.putExtra(BundleKeys.LIMIT_TYPE, model.getType());
        String stage = context.getIntent().getStringExtra(BundleKeys.STAGE_JUMP);
        intent.putExtra(BundleKeys.STAGE_JUMP, MiscUtils.isEmpty(stage) ? StageJumpEnum.STAGE_TO_REPAY_H5 : stage);
        context.setResult(Activity.RESULT_OK);
        ActivityUtils.push(RepaymentStatusActivity.class, intent, REQUEST_STAGE_REPAYMENT);
    }

    /**
     * 返回为List<String> 需要转换为List<Long>
     */
    private List<Long> strToLong(List<String> stringList) {
        List<Long> longList = new ArrayList<>();
        if (MiscUtils.isEmpty(stringList))
            return longList;
        for (String str : stringList) {
            longList.add(Long.parseLong(str));
        }
        return longList;
    }

    /**
     * 计算还款总金额
     */
    private BigDecimal getAmountChecked() {
        BigDecimal amountChecked = BigDecimal.ZERO;
        amountChecked = amountChecked.add(overdueCheckedAmount);
        amountChecked = amountChecked.add(outCheckedAmount == null ? BigDecimal.ZERO : outCheckedAmount);
        amountChecked = amountChecked.add(notoutCheckedAmout == null ? BigDecimal.ZERO : notoutCheckedAmout);
        return amountChecked;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusCome(Event event) {
        if (event != null && event.getCode() == EventCode.REPAYMENT_RELOAD) {
            load();
        }
    }

    public void onBackPressed() {
        String stage = context.getIntent().getStringExtra(BundleKeys.STAGE_JUMP);
        if (MiscUtils.isEqualsIgnoreCase(stage, StageJumpEnum.STAGE_CASHIER.getModel())) {
            //来自收银台逾期跳转，刷新收银台
            EventBusUtil.sendEvent(new Event(EventCode.CASHIER_RELOAD));
        }
    }

    public void onDestroy() {
        EventBusUtil.unregister(this);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_BRAND_DIALOG_BANK_CARD) {
            secretFree = false;
            errMsg = "";
        }
    }
}
