package com.ald.asjauthlib.cashier.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.view.View;

import com.ald.asjauthlib.BR;
import com.ald.asjauthlib.utils.Constant;
import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.ui.BankCardAddIdActivity;
import com.ald.asjauthlib.auth.ui.CreditPromoteActivity;
import com.ald.asjauthlib.auth.ui.RRIdAuthActivity;
import com.ald.asjauthlib.cashier.BrandApi;
import com.ald.asjauthlib.cashier.CashierApi;
import com.ald.asjauthlib.cashier.CashierConstant;
import com.ald.asjauthlib.cashier.model.APModel;
import com.ald.asjauthlib.cashier.model.BrandOrderDetailUrlModel;
import com.ald.asjauthlib.cashier.model.CPModel;
import com.ald.asjauthlib.cashier.model.CashLoanModel;
import com.ald.asjauthlib.cashier.model.CashierModel;
import com.ald.asjauthlib.cashier.model.CashierNperListModel;
import com.ald.asjauthlib.cashier.model.CreditModel;
import com.ald.asjauthlib.cashier.model.ItemDataPair;
import com.ald.asjauthlib.cashier.ui.CombPayActivity;
import com.ald.asjauthlib.cashier.ui.RepaymentActivity;
import com.ald.asjauthlib.cashier.ui.SelectPaymentActivity;
import com.ald.asjauthlib.cashier.ui.StageRefundActivity;
import com.ald.asjauthlib.cashier.utils.CashierPayUtils;
import com.ald.asjauthlib.databinding.ActivitySelectPaymentBinding;
import com.ald.asjauthlib.dialog.AgreementDialog;
import com.ald.asjauthlib.dialog.CashierPerDtlDialog;
import com.ald.asjauthlib.dialog.PwdDialog;
import com.ald.asjauthlib.dialog.TipsDialog;
import com.ald.asjauthlib.dialog.TwoButtonDialog;
import com.ald.asjauthlib.event.Event;
import com.ald.asjauthlib.event.EventBusUtil;
import com.ald.asjauthlib.event.EventCode;
import com.ald.asjauthlib.utils.AppUtils;
import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.utils.ModelEnum;
import com.ald.asjauthlib.utils.Permissions;
import com.ald.asjauthlib.utils.StageJumpEnum;
import com.ald.asjauthlib.utils.TimerCountDownUtils;
import com.ald.asjauthlib.utils.Utils;
import com.ald.asjauthlib.web.HTML5WebView;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.location.LocationResult;
import com.ald.asjauthlib.authframework.core.location.LocationUtils;
import com.ald.asjauthlib.authframework.core.network.BaseObserver;
import com.ald.asjauthlib.authframework.core.network.NetworkUtil;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.network.RequestCallBack;
import com.ald.asjauthlib.authframework.core.network.RxUtils;
import com.ald.asjauthlib.authframework.core.network.exception.ApiException;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.CanDoubleClick;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.authframework.core.utils.PermissionCheck;
import com.ald.asjauthlib.authframework.core.utils.UIUtils;
import com.ald.asjauthlib.authframework.core.vm.BaseRecyclerViewVM;


import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

import com.ald.asjauthlib.tatarka.bindingcollectionadapter.ItemView;

import retrofit2.Call;
import retrofit2.Response;

import static com.ald.asjauthlib.utils.BundleKeys.REQUEST_CODE_BRAND_DIALOG_BANK_CARD;
import static com.ald.asjauthlib.utils.BundleKeys.REQUEST_CODE_CASHIER;
import static com.ald.asjauthlib.utils.BundleKeys.REQUEST_CODE_CP;
import static com.ald.asjauthlib.cashier.viewmodel.ItemBankVM.BANK_ITEM_ADD;
import static com.ald.asjauthlib.cashier.viewmodel.ItemBankVM.BANK_ITEM_ALI;
import static com.ald.asjauthlib.cashier.viewmodel.ItemBankVM.BANK_ITEM_CARD;
import static com.ald.asjauthlib.authframework.core.utils.UIUtils.sendBroadcast;


/**
 * 选择支付方式
 * Created by ywd on 2017/10/30.
 */

public class SelectPaymentVM extends BaseRecyclerViewVM<ItemBankVM> {
    private SelectPaymentActivity context;
    private String payTime = "";//支付倒计时显示
    public final ObservableField<String> goodsName = new ObservableField<>();//商品名称
    public final ObservableField<String> displayTotalAmount = new ObservableField<>();//应付金额
    public final ObservableField<String> useableMoney = new ObservableField<>();//可用额度(isAuth为T时才有)

    public final ObservableField<String> categoryName = new ObservableField<>();//额度名称
    public final ObservableField<String> rightTxt = new ObservableField<>();//不支持分期，提升额度

    public final ObservableField<String> payBtnTxt = new ObservableField<>();//支付按钮文案
    public final ObservableField<SpannableString> agreementTxt = new ObservableField<>();
    // 纯额度支付或是组合支付时显示倒计时，其他支付方式不显示倒计时
    private boolean showCountDown;//是否显示倒计时
    private long startTime;
    //    private BrandPayOrderModel brandPayOrderModel;
    private boolean isOverTime = false;//订单是否超时
    private boolean isBrandOrder = false;//是否是菠萝觅订单(默认false)
    private String orderId;//订单编号
    private String orderType;//订单类型
    private String platform;//品牌第三方平台标识
    public final ObservableField<String> usableAmount = new ObservableField<>();//可用额度
    private CashierModel cashierModel;
    private APModel apModel;//代付实体类
    private CPModel cpModel;//组合支付实体类
    public ObservableBoolean showAPPay = new ObservableBoolean(true);//是否显示分期列表,协议，分期选择框
    public ObservableBoolean showCreditPay = new ObservableBoolean(false);//是否显示51信用支付
    //public ObservableBoolean showRebateMoney = new ObservableBoolean(false);//是否显示返利金额(商圈不显示)
    public ObservableBoolean needComb = new ObservableBoolean(false);//需要组合支付
    public ObservableBoolean showPayBtn = new ObservableBoolean(true);//所有支付方式都不可用时不显示按钮
    public ObservableBoolean apChecked = new ObservableBoolean(true);//分期选中状态
    public ObservableBoolean payBtnEnabled = new ObservableBoolean(true);
    public ObservableBoolean showOtherPayTitle = new ObservableBoolean(false);
    public ObservableInt showRightIc = new ObservableInt(View.VISIBLE);//是否显示右箭头
    public ObservableField<String> displayNperDetail = new ObservableField<>();//月供明细
    public CashierNperListModel nperListModel;
    private int perIndexChecked = 0;
    public final ObservableField<String> payFailedTip = new ObservableField<>();//支付失败提示
    public final ObservableBoolean showFailedTip = new ObservableBoolean(false);//是否显示支付失败提示

    private String payType = CashierConstant.CASHIER_PAY_TYPE_NORMAL;//付款类型 NORMAL:正常支付 ORDER:订单支付
    private String scene;
    private CashierPerListVM cashierPerListVM;
    private ItemBankVM.OnItemClickListener bankItemClickListener;
    private ItemBankVM formerCheckedItem;

    private List<CashierModel.BankCardModel> bankCardModelList;
    private ItemCashierPerVM perChecked;//选中的分期方式

    public ActivitySelectPaymentBinding binding;
    private TwoButtonDialog exitDialog;//退出对话框
    private String stage = "";


    private boolean needRefresh = false;//刷新标志
    private boolean secretFree = false;//是否需要免密支付
    private String pwdCache;

    private boolean isNeedDetail = false;//返回和成功是否需要拼团详情
    private boolean isGroup = false;//是否是拼团的返回,用type的额外字段判断需要请求
    private boolean isSuccessNeedDetail = false;//拼团成功返回是否需要拼团详情


    private String cardId = "";
    private String orderFrom;


    public SelectPaymentVM(SelectPaymentActivity context, ActivitySelectPaymentBinding binding) {
        this.context = context;
        EventBusUtil.register(this);
        cashierPerListVM = new CashierPerListVM(context, this);
        binding.setPerViewModel(cashierPerListVM);
        this.binding = binding;

        Intent intent = context.getIntent();
        orderFrom = intent.getStringExtra("orderFrom");
        payType = intent.getStringExtra(CashierConstant.CASHIER_PAY_TYPE);
        orderId = intent.getStringExtra("orderId");
        orderType = intent.getStringExtra("orderType");
        isBrandOrder = intent.getBooleanExtra("isBrandOrder", false);
        platform = intent.getStringExtra(BundleKeys.BRAND_PLATFORM);
        stage = intent.getStringExtra(BundleKeys.STAGE_JUMP);
        isGroup = intent.getBooleanExtra("isGroup", false);
        String needDetail = intent.getStringExtra("needDetail");
        isNeedDetail = "all".equals(needDetail);
        isSuccessNeedDetail = "success".equals(needDetail);
        scene = context.getIntent().getStringExtra(BundleKeys.INTENT_KEY_CREDIT_PROMOTE_SCENE);
        reloadPayInfo();

    }

    private void reloadPayInfo() {
        if (MiscUtils.isNotEmpty(orderId) && MiscUtils.isNotEmpty(orderType)) {
            JSONObject requestObj = new JSONObject();
            requestObj.put("orderId", orderId);
            requestObj.put("orderType", orderType);
            if (isBrandOrder) {
                requestObj.put("plantform", platform);
            }
            requestObj.put("scene", scene);
            Observable<CashierModel> observable = RDClient.getService(CashierApi.class).startRx(requestObj);
            observable.compose(RxUtils.io_main())
                    .subscribe(new BaseObserver<CashierModel>(context) {
                        @Override
                        public void onSuccess(CashierModel model) {
                            cashierModel = model;
                            cashierModel.setOrderFrom(orderFrom);
                            orderType = cashierModel.getOrderType();
                            initLayout();
                        }

                        @Override
                        public void onError(Throwable t) {
                            super.onError(t);
                            showPayBtn.set(false);
                        }
                    });
        }
    }

    private void initLayout() {
        setRightTxt("", false);
        if (cashierModel != null) {
            if (!MiscUtils.isEmpty(cashierModel.getScene()))
                scene = cashierModel.getScene();
            apModel = cashierModel.getAp();
            cpModel = cashierModel.getCp();
            setShowPayment();
            initBankItemClick();
            //倒计时
            showCountDown = cashierModel.isCountDown();
            if (showCountDown) {
                long time = cashierModel.getGmtPayEnd() - cashierModel.getCurrentTime();
                if (time < 0) {
                    time = 0;
                }
                //倒计时
                timerDown(time, 1000, true);
            }
            //应付金额
            String orderAmount = cashierModel.getAmount();
            if (MiscUtils.isNotEmpty(orderAmount)) {
                displayTotalAmount.set(String.format(AlaConfig.getResources().getString(R.string.select_payment_should_tip), orderAmount));
            } else {
                displayTotalAmount.set(String.format(AlaConfig.getResources().getString(R.string.select_payment_should_tip), "0"));
            }
        }

        SpannableString msp = new SpannableString(AlaConfig.getResources().getString(R.string.cashier_agreement));
        msp.setSpan(new UnderlineSpan(), 0, msp.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        agreementTxt.set(msp);


        if (apModel != null) {
            categoryName.set(apModel.getCategoryName() == null ? "花吧" : apModel.getCategoryName());
            usableAmount.set(String.format(AlaConfig.getResources().getString(R.string.cashier_ap_usableAmount),
                    apModel.getUseableAmount() == null ? "0.00" : AppUtils.formatAmount(apModel.getUseableAmount())));

            bankCardModelList = cashierModel.getBankCardList();
            //获取分期列表 没有额度时不调用
            requestNperList();
            String uAmount;
            if (ModelEnum.Y.getModel().equals(apModel.getStatus())) {
                //代付
                needComb.set(false);
                showAPPay.set(true);
                //虚拟商品额度
                if (ModelEnum.Y.getModel().equals(apModel.getIsVirtualGoods())) {
                    uAmount = String.format(AlaConfig.getResources().getString(R.string.cashier_ap_usableAmount), apModel.getVirtualGoodsUsableAmount());
                    if (ModelEnum.N.getModel().equals(cashierModel.getIsSupplyCertify())) {
                        uAmount = String.format(AlaConfig.getResources().getString(R.string.cashier_ap_usableAmount), apModel.getVirtualGoodsUsableAmount());
                    }
                    useableMoney.set(String.format(AlaConfig.getResources().getString(R.string.cashier_quota_normal), apModel.getVirtualGoodsUsableAmount()));
                    //虚拟可用额度小于资源配置限制金额
                    if (CashierConstant.NEEDUP_VIRTUAL.equals(apModel.getReasonType())) {
//                                    showAPPay.set(false);
                        setRightTxt(AlaConfig.getResources().getString(R.string.cashier_quota_normal_virtualgoods), false);
                    }
                } else {
                    //普通商品额度
                    useableMoney.set(String.format(AlaConfig.getResources().getString(R.string.cashier_quota_normal), apModel.getUseableAmount()));
                    uAmount = String.format(AlaConfig.getResources().getString(R.string.cashier_ap_usableAmount), apModel.getUseableAmount());
                    if (ModelEnum.N.getModel().equals(cashierModel.getIsSupplyCertify())) {
                        uAmount = String.format(AlaConfig.getResources().getString(R.string.cashier_ap_usableAmount), apModel.getUseableAmount());
                    }
                }
                usableAmount.set(uAmount);
                payBtnTxt.set(categoryName.get() + "支付¥" + cashierModel.getAmount() + "+其他支付 ¥0.0");
            } else {
                //以下情况不能分期，默认选中其他支付方式第一项
                showAPPay.set(false);
                if (apModel.getReasonType() != null) {
                    switch (apModel.getReasonType()) {
                        case CashierConstant.CASHIER:
                            setRightTxt("该业务不支持信用支付", false);
                            break;

                        case CashierConstant.USE_ABLED_LESS:
                            //可用额度小于分期额度 可以组合支付
                            //虚拟商品额度
                            uAmount = String.format(AlaConfig.getResources().getString(R.string.cashier_ap_cp_usableAmount),
                                    ModelEnum.Y.getModel().equals(apModel.getIsVirtualGoods()) ? apModel.getVirtualGoodsUsableAmount() : apModel.getPayAmount());
                            usableAmount.set(uAmount);
                            //组合支付
                            if (cpModel != null && ModelEnum.Y.getModel().equals(cpModel.getStatus())) {
                                showAPPay.set(true);
                            }
                            payBtnTxt.set("组合支付" + String.format(AlaConfig.getResources().getString(R.string.format_rmb), cashierModel.getAmount()));
                            break;

                        case CashierConstant.NEEDUP_VIRTUAL:
                            setRightTxt(AlaConfig.getResources().getString(R.string.cashier_quota_normal_virtualgoods), false);
                            break;

                        case CashierConstant.NEEDAUTH:
                            if (nperListModel != null && MiscUtils.isNotEmpty(nperListModel.getNperList())) {
                                //可分期
                                if (ModelEnum.Y.getModel().equals(cashierModel.getOnlineStatus()) && (ModelEnum.N.getModel().equals(cashierModel.getMobileStatus()) || ModelEnum.W.getModel().equals(cashierModel.getMobileStatus()))) {
                                    usableAmount.set("需重新完成运营商认证 立即完成 >");
                                } else {
                                    //提升额度
                                    setRightTxt(AlaConfig.getResources().getString(R.string.cashier_payment_auth_tip), true);
                                }
                            } else {
                                showCreditPay.set(false);
                            }

                            break;

                        case CashierConstant.NEEDUP:
                            //可用额度小于资源配置限制金额 比如额度只有1块钱，那么不能走分期
                            if (ModelEnum.Y.getModel().equals(cashierModel.getIsSupplyCertify())) {
                                setRightTxt(AlaConfig.getResources().getString(R.string.cashier_payment_need_up), false);//额度不足
                            } else {
                                setRightTxt("提升额度", true);
                            }
                            break;

                        case CashierConstant.CONSUME_MIN_AMOUNT:
                            //订单金额小于分期/组合支付最小额度限制
                            setRightTxt(AlaConfig.getResources().getString(R.string.cashier_payment_min_amount), false);
                            break;

                        case CashierConstant.OVERDUE_BORROW:
                        case CashierConstant.OVERDUE_BORROW_CASH:
                            //消费分期/现金借逾期
                            setRightTxt(AlaConfig.getResources().getString(R.string.cashier_payment_overdue), true);
                            break;
                    }
                }
            }
            if (MiscUtils.isEmpty(usableAmount.get())) {
                usableAmount.set(String.format(AlaConfig.getResources().getString(R.string.cashier_ap_usableAmount), BigDecimal.ZERO));
            }
        }
    }

    /**
     * 初始化其他支付方式列表
     */
    private void initOtherPayList() {
        //银行卡相关
        items.clear();
        showOtherPayTitle.set(!AlaConfig.isRevView());
        int firstInvalidIndex = -1;//首个维护中银行卡位置
        int firstValidIndex = -1;//首个可用银行卡
        ItemBankVM itemBankVM;
        ItemDataPair itemDataPair;
        CashierModel.BankCardModel model;
        if (bankCardModelList != null && cashierModel.getBank() != null && !ModelEnum.N.getModel().equals(cashierModel.getBank().getStatus())) {
            //加载银行卡
            List<CashierModel.BankCardModel> list = bankCardModelList;
            for (int i = 0; i < list.size(); i++) {
                model = list.get(i);
                //测试用
//                if (i == 2)
//                    model.setCardType(ModelEnum.CREDIT.getValue());
                itemDataPair = new ItemDataPair(model, BANK_ITEM_CARD);
                itemBankVM = new ItemBankVM(itemDataPair, context, i, bankItemClickListener);
                itemBankVM.showCreditCharge.set(model.getCardType() == ModelEnum.CREDIT.getValue());
                if (model.getIsValid() == null)
                    model.setIsValid("Y");//默认值为Y 有效
                if (!model.getIsValid().equals("Y") && firstInvalidIndex == -1) {
                    firstInvalidIndex = i;
                } else if (model.getIsValid().equals("Y") && firstValidIndex == -1) {
                    firstValidIndex = i;
                }
                items.add(itemBankVM);
            }
        }

        if (cashierModel.getAli() != null && !ModelEnum.N.getModel().equals(cashierModel.getAli().getStatus()) && !AlaConfig.isRevView()) {
            model = new CashierModel.BankCardModel();
            model.setBankName("支付宝");
            itemDataPair = new ItemDataPair(model, BANK_ITEM_ALI);
            int index = items.size();
            if (firstInvalidIndex > -1) {
                index = firstInvalidIndex;
            }
            //如果第一个维护中银行卡index为0时（所有银行卡都在维护中），或者银行卡列表为空时，选中该项
            itemBankVM = new ItemBankVM(itemDataPair, context, index, bankItemClickListener);
            items.add(index, itemBankVM);

            if (firstValidIndex == -1)
                firstValidIndex = index;

        }

        itemDataPair = new ItemDataPair(null, BANK_ITEM_ADD);
        itemBankVM = new ItemBankVM(itemDataPair, context, items.size(), bankItemClickListener);
        items.add(itemBankVM);
        if (firstValidIndex > -1 &&
                (!showAPPay.get())) {
            ItemBankVM itemChecked = items.get(firstValidIndex);
            itemChecked.isChecked.set(true);
            itemChecked.checkerBg.set(AlaConfig.getResources().getDrawable(R.drawable.ic_cashier_checked));
            formerCheckedItem = itemChecked;
            showOrtherPayStr(itemChecked.getItemDataPair());
        }
        //组合支付或者分期都不需要选中银行卡
        if (needComb.get() || (showCreditPay.get() && showAPPay.get())) {
            return;
        }
        for (int i = 0; i < items.size(); i++) {
            ItemBankVM itemBankVM1 = items.get(i);
            CashierModel.BankCardModel bankCardModel = (CashierModel.BankCardModel) itemBankVM1.getItemDataPair().getData();
            if (bankCardModel != null && (cardId.equals(bankCardModel.getCardNumber()) || MiscUtils.isEmpty(cardId))) {
                cardId = "";
                if (formerCheckedItem != null) {
                    formerCheckedItem.isChecked.set(false);
                    formerCheckedItem.checkerBg.set(AlaConfig.getResources().getDrawable(R.drawable.ic_cashier_unchecked));
                }
                apChecked.set(false);
                if (perChecked != null) {
                    perChecked.switchLayout(false);
                }
                showPayBtn.set(true);
                itemBankVM1.isChecked.set(true);
                formerCheckedItem = itemBankVM1;
                itemBankVM1.checkerBg.set(AlaConfig.getResources().getDrawable(R.drawable.ic_cashier_checked));
                showOrtherPayStr(formerCheckedItem.getItemDataPair());
                break;
            }
        }


        if (!apChecked.get() && firstValidIndex == -1) {
            payBtnEnabled.set(false);
            payBtnTxt.set("确认支付");
        } else {
            payBtnEnabled.set(true);
        }
    }

    /**
     * @author Yangyang
     * @desc 底部btn其他支付的文案
     */
    private void showOrtherPayStr(ItemDataPair itemDataPair1) {
        String orderAmount = cashierModel.getAmount();
        String creditAmount = AppUtils.decimalCalculation(orderAmount, 0.0045);
        int itemType = itemDataPair1.getItemType();
        CashierModel.BankCardModel bankCardModel = (CashierModel.BankCardModel) itemDataPair1.getData();
        if (itemType == BANK_ITEM_CARD) {
            //选择信用卡  4.1.8此处显示不包含手续费
//            if (bankCardModel.getCardType() == ModelEnum.CREDIT.getValue()) {
//                double allAmount = Double.valueOf(orderAmount) + Double.valueOf(creditAmount);
//                payBtnTxt.set(String.format(AlaConfig.getResources().getString(R.string.credit_bank_pay), String.valueOf(allAmount), creditAmount));
//            } else {
            //选择储蓄卡
            payBtnTxt.set(AlaConfig.getResources().getString(R.string.cashier_bank_pay) + orderAmount);
//            }
            //应付金额也要随之改动
//            if (MiscUtils.isNotEmpty(orderAmount)) {
//                displayTotalAmount.set(String.format(AlaConfig.getResources().getString(R.string.select_payment_should_tip), orderAmount));
//            } else {
//                displayTotalAmount.set(String.format(AlaConfig.getResources().getString(R.string.select_payment_should_tip), "0"));
//            }
        } else if (itemType == BANK_ITEM_ALI) {
            payBtnTxt.set(AlaConfig.getResources().getString(R.string.cashier_ali_pay) + orderAmount);
        }
    }

    /**
     * 设置51信用右侧文案及图标
     *
     * @param showIcon 是否显示展开按钮
     */
    private void setRightTxt(String txt, boolean showIcon) {
        rightTxt.set(txt);
        showRightIc.set(showIcon ? View.VISIBLE : View.INVISIBLE);//GONE 影响文字显示
        apChecked.set(MiscUtils.isEmpty(txt));
    }

    public void onRightTxtClick(View view) {
        if (apModel != null && apModel.getReasonType() != null && (apModel.getReasonType().equals(CashierConstant.OVERDUE_BORROW_CASH) || apModel.getReasonType().equals(CashierConstant.OVERDUE_BORROW))
                && cashierModel != null && cashierModel.getAp() != null && ("102".equals(cashierModel.getAp().getOverduedCode()) || "103".equals(cashierModel.getAp().getOverduedCode()))) {
            //逾期待还
            CashLoanModel model = new CashLoanModel();
            if (cashierModel.getAp().getJfbAmount() != null) {
//                    model.setJfbAmount(Integer.parseInt(cashierModel.getAp().getJfbAmount()));
                model.setJfbAmount(Double.valueOf(Double.parseDouble(cashierModel.getAp().getJfbAmount())).intValue());
            }
            if (cashierModel.getAp().getUserRebateAmount() != null) {
                model.setRebateAmount(new BigDecimal(cashierModel.getAp().getUserRebateAmount()));
            }
            if (cashierModel.getAp().getRepaymentAmount() != null) {
                model.setReturnAmount(new BigDecimal(cashierModel.getAp().getRepaymentAmount()));
            }
//                showTipsDialog(cashierModel.getAp().getOverduedCode(), cashierModel.getAp().getBillId(), model,cashierModel.getAp().getBorrowId());
            Intent intent = new Intent();
            if ("102".equals(cashierModel.getAp().getOverduedCode())) {    // 现金贷还款
                intent.putExtra(BundleKeys.BORROW_ID, cashierModel.getAp().getBorrowId());
                intent.putExtra(BundleKeys.MAX_REPAYMENT_AMOUNT, model.getReturnAmount().toString());
                intent.putExtra(BundleKeys.REBATE_AMOUNT, model.getRebateAmount().toString());
                intent.putExtra(BundleKeys.JIFENG_COUNT, model.getJfbAmount());
                intent.putExtra(BundleKeys.CASH_LOAN_REPAYMENT_FROM_PAGE, ModelEnum.CASH_LOAN_REPAYMENT_FROM_PAGE_INDEX.getModel());
                ActivityUtils.push(StageRefundActivity.class, intent, BundleKeys.REQUEST_CODE_BRAND_PAY_INFO);
                return;
            }
            if ("103".equals(cashierModel.getAp().getOverduedCode())) {
                if (MiscUtils.isNotEmpty(cashierModel.getAp().getBillId())) {
                    //跳转还款页面
                    intent.putExtra(BundleKeys.STAGE_JUMP, StageJumpEnum.STAGE_CASHIER.getModel());
                    ActivityUtils.push(RepaymentActivity.class, intent);
                } else {
                    UIUtils.showToast(AlaConfig.getResources().getString(R.string.toast_fanbeipay_billid_empty));
                }
            }
        } else if ((apModel == null || ModelEnum.N.getModel().equals(apModel.getStatus()))
                && (cpModel == null || ModelEnum.N.getModel().equals(cpModel.getStatus()))) {
            checkAndJump();
        }
    }

    /**
     * 运营商认证点击
     */
    public void onOperatorInfoClick(View view) {
        needRefresh = true;
        Intent intent = new Intent();
        intent.putExtra(BundleKeys.INTENT_KEY_CREDIT_PROMOTE_SCENE, scene);
        ActivityUtils.push(CreditPromoteActivity.class, intent, REQUEST_CODE_CASHIER);
    }


    /**
     * 处理支付方式显示
     */
    private void setShowPayment() {
        //收银台限制(是否显示51信用支付)
        if (cashierModel.getAp() == null
                || CashierConstant.RISK_CREDIT_PAYMENT.equals(cashierModel.getAp().getReasonType()) || AlaConfig.isRevView()) {
            showCreditPay.set(false);
        } else {
            showCreditPay.set(true);
        }
    }


    private void requestNperList() {
        JSONObject reqObj = new JSONObject();
        reqObj.put("orderId", String.valueOf(cashierModel.getOrderId()));
        reqObj.put("orderType", orderType);
        reqObj.put("amount", MiscUtils.isEqualsIgnoreCase(apModel.getStatus(), "N") ? (MiscUtils.isEqualsIgnoreCase(cpModel.getStatus(), "Y") ? apModel.getPayAmount() : cashierModel.getAmount()) : apModel.getPayAmount());
        RDClient.getService(CashierApi.class).getInstallmentInfo(reqObj)
                .compose(RxUtils.io_main())
                .subscribe(new BaseObserver<CashierNperListModel>() {
                    @Override
                    public void onSuccess(CashierNperListModel cashierNperListModel) {
                        nperListModel = cashierNperListModel;
                        if (cashierNperListModel != null && MiscUtils.isNotEmpty(cashierNperListModel.getNperList())) {
                            showAPPay.set(true);
                            List<CashierNperListModel.NperModel> nperList = cashierNperListModel.getNperList();
                            CreditModel creditModel = cashierModel.getCredit();
                            if (creditModel != null && ModelEnum.Y.getModel().equals(creditModel.getStatus())) {
                                CashierNperListModel.NperModel nperModel = new CashierNperListModel.NperModel();
                                nperModel.setNper(1);
                                CashierNperListModel.NperDetailModel detailModel = new CashierNperListModel.NperDetailModel();
                                detailModel.setAmount(BigDecimal.valueOf(Double.parseDouble(creditModel.getPayAmount())));
                                List<CashierNperListModel.NperDetailModel> nperDetailModelList = new ArrayList<>();
                                nperDetailModelList.add(detailModel);
                                nperModel.setNperDetailList(nperDetailModelList);
                                nperModel.setIsFree("1");
                                nperList.add(0, nperModel);
                            }
                            perChecked = cashierPerListVM.load(nperList, 0, (itemCashierPerVM, index) -> {
                                perIndexChecked = index;
                                if (perChecked != null) {
                                    perChecked.switchLayout(false);
                                }
                                perChecked = itemCashierPerVM;
                                perChecked.switchLayout(true);
                                if (!apChecked.get()) {
                                    apChecked.set(true);
                                    if (apModel != null && apModel.getStatus() != null && cashierModel != null) {
                                        if (apModel.getStatus().equals(ModelEnum.Y.getModel())) {
                                            payBtnTxt.set(apModel.getCategoryName() + "支付¥" + cashierModel.getAmount() + "+其他支付 ¥0.0");
                                        } else
                                            payBtnTxt.set(String.format(AlaConfig.getResources().getString(R.string.cashier_need_comb_pay), cashierModel.getAmount()));
                                    }
                                }
                                if (formerCheckedItem != null) {
                                    formerCheckedItem.checkerBg.set(AlaConfig.getResources().getDrawable(R.drawable.ic_cashier_unchecked));
                                    formerCheckedItem = null;
                                }
                            });
                            showCreditPay.set(true);
                            if (ModelEnum.N.getModel().equals(apModel.getStatus())) {
                                if (CashierConstant.NEEDAUTH.equals(cashierModel.getAp().getReasonType())) {
                                    //可分期
                                    if (ModelEnum.Y.getModel().equals(cashierModel.getOnlineStatus()) && (ModelEnum.N.getModel().equals(cashierModel.getMobileStatus()) || ModelEnum.W.getModel().equals(cashierModel.getMobileStatus()))) {
                                        usableAmount.set("需重新完成运营商认证 立即完成 >");
                                    } else {
                                        //提升额度
                                        setRightTxt(AlaConfig.getResources().getString(R.string.cashier_payment_auth_tip), true);
                                    }
                                    showAPPay.set(false);
                                } else if (CashierConstant.USE_ABLED_LESS.equals(cashierModel.getAp().getReasonType()) && ModelEnum.Y.getModel().equals(cpModel.getStatus())) {
                                    //额度不足,可以组合支付
                                } else if (CashierConstant.NEEDUP.equals(cashierModel.getAp().getReasonType())) {
                                    if (ModelEnum.Y.getModel().equals(cashierModel.getIsSupplyCertify())) {
                                        setRightTxt(AlaConfig.getResources().getString(R.string.cashier_payment_need_up), false);//额度不足
                                    } else {
                                        setRightTxt("提升额度", true);
                                    }
                                    showAPPay.set(false);
                                } else {
                                    showCreditPay.set(false);
                                }
                            }
                        } else {
                            showAPPay.set(false);
                            apChecked.set(false);
                            needComb.set(false);
                            showCreditPay.set(false);//不可分期商品不显示整个额度UI 和其他支付方式title
                        }
                        initOtherPayList();
                    }

                    @Override
                    public void onFailure(ApiException apiException) {
                        showAPPay.set(false);
                        apChecked.set(false);
                        initOtherPayList();
                    }
                });
    }

    @CanDoubleClick
    public void onApCheckerClick(View view) {
        if (!apChecked.get()) {
            if (formerCheckedItem != null) {
                formerCheckedItem.isChecked.set(false);
                formerCheckedItem.checkerBg.set(AlaConfig.getResources().getDrawable(R.drawable.ic_cashier_unchecked));
                formerCheckedItem = null;
            }
            apChecked.set(true);
            if (cashierPerListVM != null && cashierPerListVM.items.size() > 0) {
                perChecked = cashierPerListVM.getItem(0);
                perChecked.switchLayout(true);
            }
            if (apModel.getStatus().equals(ModelEnum.Y.getModel())) {
                payBtnTxt.set(apModel.getCategoryName() + "支付¥" + cashierModel.getAmount() + "+其他支付 ¥0.0");
            } else
                payBtnTxt.set(String.format(AlaConfig.getResources().getString(R.string.cashier_need_comb_pay), cashierModel.getAmount()));
            showPayBtn.set(true);
        } else {
            if (perChecked != null) {
                perChecked.switchLayout(false);
            }
//            perChecked = null;
            apChecked.set(false);
            showPayBtn.set(false);
        }
    }

    private void initBankItemClick() {

        bankItemClickListener = itemBankVM -> {
            if (itemBankVM.getItemDataPair().getItemType() == BANK_ITEM_ADD) {
                //添加银行卡,返回刷新列表
                //needRefresh = true;
                cancelSecretFree();
                Intent intent = new Intent();
                intent.putExtra(BundleKeys.BANK_CARD_NAME, cashierModel.getRealName());
                intent.putExtra(BundleKeys.SETTING_PAY_ID_NUMBER, cashierModel.getIdNumber());
                intent.putExtra(BundleKeys.STAGE_JUMP, StageJumpEnum.STAGE_CASHIER.getModel());
                intent.putExtra(BundleKeys.INTENT_KEY_CREDIT_PROMOTE_SCENE, scene);
                ActivityUtils.push(BankCardAddIdActivity.class, intent,
                        BundleKeys.REQUEST_CODE_BANK_CARD_ADD);
            } else {
                CashierModel.BankCardModel cur = ((CashierModel.BankCardModel) itemBankVM.getItemDataPair().getData());
                if (itemBankVM.getItemDataPair().getItemType() == BANK_ITEM_ALI
                        || cur.getIsValid().equals("Y")) {
                    showPayBtn.set(true);
                    if (formerCheckedItem != null) {
                        formerCheckedItem.isChecked.set(false);
                        formerCheckedItem.checkerBg.set(AlaConfig.getResources().getDrawable(R.drawable.ic_cashier_unchecked));
                    }
                    apChecked.set(false);
                    if (perChecked != null) {
                        perChecked.switchLayout(false);
                    }
                    showPayBtn.set(true);
                    formerCheckedItem = itemBankVM;
                    itemBankVM.checkerBg.set(AlaConfig.getResources().getDrawable(R.drawable.ic_cashier_checked));
                    showOrtherPayStr(formerCheckedItem.getItemDataPair());
                }
            }
        };
    }

    /**
     * 倒计时
     *
     * @param millisInFuture    总时间
     * @param countDownInterval 时间间隔
     */

    private void timerDown(long millisInFuture, long countDownInterval, final boolean isBrand) {
        new TimerCountDownUtils(millisInFuture, countDownInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                startTime = millisUntilFinished;
                payTime = toClock(millisUntilFinished);
                context.setRightText(payTime, null, AlaConfig.getResources().getColor(R.color.color_verifypay_countdown));
            }

            @Override
            public String toClock(long millisUntilFinished) {
                return super.toClock(millisUntilFinished, isBrand, "后未支付将自动关闭订单");
            }

            @Override
            public void onFinish() {
                payTime = AlaConfig.getResources().getString(R.string.toast_pay_timeout);
                context.setRightText("订单超时", null, AlaConfig.getResources().getColor(R.color.color_verifypay_countdown));
                isOverTime = true;
                cancelSecretFree();
                super.onFinish();
            }
        }.start();
    }


    /**
     * 检查认证状态
     *
     * @return 能否继续支付
     */
    private boolean checkAndJump() {
        if (cashierModel != null && !ModelEnum.Y.getModel().equals(cashierModel.getAp().getStatus())) {
            if (ModelEnum.N.getModel().equals(cashierModel.getFaceStatus())) {
                cancelSecretFree();
                goToRRIdf();
                return false;
            }
            //是否绑定主卡
            if (cashierModel.getMainBankCard() == null || ModelEnum.N.getModel().equals(cashierModel.getMainBankCard().getStatus())) {
                cancelSecretFree();
                Intent intent = new Intent();
                intent.putExtra(BundleKeys.BANK_CARD_NAME, cashierModel.getRealName());
                intent.putExtra(BundleKeys.SETTING_PAY_ID_NUMBER, cashierModel.getIdNumber());
                intent.putExtra(BundleKeys.INTENT_KEY_CREDIT_PROMOTE_SCENE, scene);
                // needRefresh = true;
                ActivityUtils.push(BankCardAddIdActivity.class, intent);
                ActivityUtils.pop();
                return false;
            }

            //需要认证
            if (CashierConstant.NEEDAUTH.equals(cashierModel.getAp().getReasonType())) {
                cancelSecretFree();
                showAPPay.set(false);
                //风控和补充认证
                Intent intent = new Intent();
                intent.putExtra(BundleKeys.INTENT_KEY_CREDIT_PROMOTE_SCENE, scene);
                if (ModelEnum.N.getModel().equals(cashierModel.getRiskStatus()) && ModelEnum.N.getModel()
                        .equals(cashierModel.getIsSupplyCertify())) {
                    intent.putExtra(BundleKeys.JUMP_AUTH_CODE, StageJumpEnum.STAGE_CASHIER);
                    needRefresh = true;
                    ActivityUtils.push(CreditPromoteActivity.class, intent, REQUEST_CODE_CASHIER);
                } else {
                    needRefresh = true;
                    ActivityUtils.push(CreditPromoteActivity.class, intent, REQUEST_CODE_CASHIER);
                }
                return false;
            }
            //可用额度小于资源配置限制金额 比如额度只有1块钱，那么不能走分期
            if (CashierConstant.NEEDUP.equals(cashierModel.getAp().getReasonType())) {
                if (ModelEnum.Y.getModel().equals(cashierModel.getIsSupplyCertify())) {
                    return true;
                }
                cancelSecretFree();
                UIUtils.showToast(AlaConfig.getResources().getString(R.string.cashier_payment_need_up));
                Intent intent = new Intent();
                intent.putExtra(BundleKeys.JUMP_AUTH_CODE, StageJumpEnum.STAGE_CASHIER);
                if (context != null) {
                    intent.putExtra(BundleKeys.INTENT_KEY_CREDIT_PROMOTE_SCENE, scene);
                }
                needRefresh = true;
                ActivityUtils.push(CreditPromoteActivity.class, intent);
                return false;
            }

            //虚拟商品
            if (ModelEnum.Y.getModel().equals(cashierModel.getAp().getIsVirtualGoods())) {
                //虚拟商品限额
                if (CashierConstant.VIRTUAL_GOODS_LIMIT.equals(cashierModel.getAp().getReasonType())) {
                    /*showTipsDialog(cashierModel.getAp().getOverduedCode(), cashierModel.getAp().getBillId(), null,
                            null);*/
                    //若可用额度<=0则不跳转
                    if (Double.parseDouble(cashierModel.getAp().getVirtualGoodsUsableAmount()) <= 0) {
                        cancelSecretFree();
                        UIUtils.showToast(AlaConfig.getResources()
                                .getString(R.string.toast_fanbeipay_quota_insufficient));
                        Intent intent = new Intent();
                        intent.putExtra(BundleKeys.JUMP_AUTH_CODE, 1);
                        if (context != null) {
                            intent.putExtra(BundleKeys.INTENT_KEY_CREDIT_PROMOTE_SCENE, scene);
                        }
                        needRefresh = true;
                        ActivityUtils.push(CreditPromoteActivity.class, intent);
                    }
                }
                return true;
            }
            //商品价格>可用额度
            /*if (Double.parseDouble(String.valueOf(brandPayOrderModel.getSaleAmount())) > Double
                    .parseDouble(String.valueOf(brandPayOrderModel.getUseableAmount()))) {
                if (brandPayOrderModel.getUseableAmount().doubleValue() <= 0) {
                    UIUtils.showToast(AlaConfig.getResources()
                            .getString(R.string.toast_fanbeipay_quota_insufficient));
                    Intent intent = new Intent();
                    intent.putExtra(BundleKeys.JUMP_AUTH_CODE, 1);
                    ActivityUtils.push(CreditPromoteActivity.class, intent);
                    return;
                }
                return;
            }*/
        }
        return true;

    }

    /**
     * 取消免密支付
     */
    private void cancelSecretFree() {
        secretFree = false;
        pwdCache = "";
        showFailedTip.set(false);
    }

    /**
     * 确认支付(代付)
     */
    public void payClick(View view) {
        if (isOverTime) {
            UIUtils.showToast(context.getString(R.string.toast_pay_timeout));
            return;
        }
        if (apModel == null || apModel.getStatus() == null)
            return;
        if (apChecked.get() && ModelEnum.Y.getModel().equals(apModel.getStatus())) {
            if (!checkAndJump())
                return;
            //代付
            new PwdDialog.Builder(context)
                    .setStage(StageJumpEnum.STAGE_CASHIER.getModel())
                    .setOnFinishListener(text -> {
                        doCashierPay(String.valueOf(perChecked == null ? 1 : perChecked.nPer.get()), null, "0", text, null);
                    }).create().show();
        } else if (showAPPay.get() && apChecked.get()) {
            //跳转组合支付页面
            if (!checkAndJump())
                return;
            Intent intent = new Intent(context, CombPayActivity.class);
            intent.putExtra("CashierModel", cashierModel);
            intent.putExtra("nPer", perChecked == null ? "1" : Integer.toString(perChecked.getModel().getNper()));
            intent.putExtra("payType", payType);
            if (showCountDown) {
                intent.putExtra("startTime", startTime);
            }
            intent.putExtra(BundleKeys.INTENT_KEY_CREDIT_PROMOTE_SCENE, scene);
            intent.putExtra(BundleKeys.STAGE_JUMP, stage);
            NetworkUtil.dismissCutscenes();
            ActivityUtils.push(CombPayActivity.class, intent, REQUEST_CODE_CP);

        } else {
            //其他方式支付
            if (formerCheckedItem == null || cashierModel == null)
                return;
            int type = formerCheckedItem.getItemDataPair().getItemType();
            switch (type) {
                case BANK_ITEM_CARD:
                    unionPay();
                    break;
                case BANK_ITEM_ALI:
                    AliPay();
                    break;
            }
        }
    }

    private void doCashierPay(String nper, CashierModel.BankCardModel bankCardModel, String payId, String text, CashierPayUtils.PayBuilder.OnPayFailedListener onPayFailedListener) {
        LocationResult result = LocationUtils.getCurrentLocation();
        if (null != result) {
            handlePay(nper, bankCardModel, payId, text, result, onPayFailedListener);
        } else {
            //定位权限
            PermissionCheck.getInstance().askForPermissions(context, Permissions.locationPermissions,
                    PermissionCheck.REQUEST_CODE_ALL);
            AlaConfig.execute(() -> {
                LocationUtils.requestLocation(10000L);
                if (LocationUtils.isLocationFailure()) {
                    UIUtils.showToast(AlaConfig.getResources().getString(R.string.toast_get_location_fail));
                    return;
                }
                LocationResult result1 = LocationUtils.getCurrentLocation();
                handlePay(nper, bankCardModel, payId, text, result1, onPayFailedListener);
            });
        }
    }

    private void handlePay(String nper, CashierModel.BankCardModel bankCardModel, String payId, String text, LocationResult result, CashierPayUtils.PayBuilder.OnPayFailedListener onPayFailedListener) {
        new CashierPayUtils.PayBuilder(context)
                .setNper(nper)
                .setBankCardModel(bankCardModel)
                .setCashierModel(cashierModel)
                .setSuccessNeedDetail(isSuccessNeedDetail || isNeedDetail)
                .setPayType(payType)
                .setStage(stage)
                .setOnPayFailedListener(onPayFailedListener)
                .handlePay(payId, text, result, false);
    }

    /**
     * 微信支付点击
     */
    private void weChatPay() {
        doCashierPay(null, null, "-1", "", null);

    }

    /**
     * 支付宝支付点击
     */
    private void AliPay() {
        doCashierPay(null, null, "-2", "", null);
    }


    /**
     * 销毁监听
     */
    public void unregisterReceiver() {
        EventBusUtil.unregister(this);
        if (exitDialog != null) {
            exitDialog.dismiss();
            exitDialog = null;
        }
        NetworkUtil.dismissCutscenes();
    }

    /**
     * 银行卡支付点击
     */
    private void unionPay() {
        final CashierModel.BankCardModel itemSelect = (CashierModel.BankCardModel) formerCheckedItem.getItemDataPair().getData();
        if (itemSelect == null) {
            UIUtils.showToast(AlaConfig.getResources().getString(R.string.verify_pay_info_bank_err));
            return;
        }
        if (secretFree) {
            doCashierPay(null, itemSelect, "", pwdCache, null);
        } else {
            new PwdDialog.Builder(context)
                    .setStage(StageJumpEnum.STAGE_CASHIER.getModel())
                    .setOnFinishListener(text -> {
                        doCashierPay(null, itemSelect, "", text, msg -> {
                            secretFree = true;
                            pwdCache = text;
                            payFailedTip.set(msg);
                            showFailedTip.set(true);
                        });
                    }).create().show();
        }
    }

    /**
     * 跳转实名认证
     */
    private void goToRRIdf() {
        Intent intent = new Intent();
        intent.putExtra(BundleKeys.INTENT_KEY_CREDIT_PROMOTE_SCENE, scene);
        needRefresh = true;
        ActivityUtils.push(RRIdAuthActivity.class, intent, BundleKeys.REQUEST_CODE_BRAND_PAY_INFO);
    }

    /**
     * onActivityResult逻辑
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BundleKeys.REQUEST_CODE_CASHIER_BANK_CARD && resultCode == Activity.RESULT_OK) {
            unionPay();
            return;
        }
        if (requestCode == REQUEST_CODE_BRAND_DIALOG_BANK_CARD && resultCode == Activity.RESULT_CANCELED) {
            return;
        }
        if (((requestCode == REQUEST_CODE_CASHIER || requestCode == REQUEST_CODE_BRAND_DIALOG_BANK_CARD || requestCode == BundleKeys.REQUEST_CODE_BANK_CARD_ADD) && resultCode == Activity.RESULT_OK)
                || (MiscUtils.isNotEmpty(orderId) && MiscUtils.isNotEmpty(platform)))   //提升信用后的刷新数据
        {
            reloadPayInfo();
        } else if (resultCode == -3) {
            reloadPayInfo();
        }
    }

    public void onResume() {
        if (needRefresh) {
            needRefresh = false;
            reloadPayInfo();
        }
    }

//    private void goToPaySuccess() {
//        if (CashierConstant.CASHIER_PAY_TYPE_NORMAL.equals(payType)) {
//            Intent intent = new Intent();
//            if (CashierConstant.ORDER_TYPE_BOLUOME.equals(orderType)) {
//                intent.putExtra(BundleKeys.BRAND_FANBEI_ORDERID, String.valueOf(cashierModel.getOrderId()));
//                ActivityUtils.push(BrandPaySuccessActivity.class, intent);
//            } else if (CashierConstant.ORDER_TYPE_TRADE.equals(orderType)) {
//                intent.putExtra(BundleKeys.ORDER_ID, orderId);
//                ActivityUtils.push(OrderTradeDetailActivity.class, intent);
//            }
//            ActivityUtils.finish(HTML5WebView.class);
//            ActivityUtils.finish(SelectPaymentActivity.class);
//        } else {
//            context.finish();
//            UIUtils.showToast("支付成功！");
//        }
//    }

    /**
     * 显示额度弹窗
     */
    public void quotaTipsClick(View view) {
        if (apModel != null) {
            TipsDialog dialog = new TipsDialog(context);
            dialog.setTitle(apModel.getCategoryName());
            String content = String.format(AlaConfig.getResources().getString(R.string.fanbei_pay_category_info),
                    AppUtils.formatAmount(apModel.getTotalVirtualAmount()), AppUtils.formatAmount(apModel.getVirtualGoodsUsableAmount()));
            dialog.setContent(content);
            dialog.show();
        }
    }

    public void onBackPressed() {
        TwoButtonDialog.Builder builder = new TwoButtonDialog.Builder(context);
        if (exitDialog != null)
            exitDialog.show();
        else {
            exitDialog = builder
                    .setTitleTxt(AlaConfig.getResources().getString(R.string.cashier_back_dialog_title))
                    .setContent(AlaConfig.getResources().getString(R.string.cashier_back_dialog_message))
                    .setSureTxt(AlaConfig.getResources().getString(R.string.cashier_back_dialog_sure))
                    .setCancelTxt(AlaConfig.getResources().getString(R.string.cashier_back_dialog_cancel))
                    .setSureListener(v -> {
                        if (CashierConstant.ORDER_TYPE_BOLUOME.equals(orderType)) {
                            getOrderDetailUrl();
                        } else if (CashierConstant.ORDER_TYPE_SELFSUPPORT.equals(orderType)) {
                            if (Constant.ORDER_FROM_SUREORDER.equals(orderFrom)) {
                                ActivityUtils.finish(HTML5WebView.class);
                                Utils.jumpH5(AlaConfig.getServerProvider().getH5Server() + Constant.H5_ORDER_LIST_ALL);
                            } else {
                                //订单列表不作处理
                            }

                        }
                        context.finish();
                    })
                    .create();
            exitDialog.show();
        }

    }

    /**
     * 获取菠萝觅订单详情接口
     */
    private void getOrderDetailUrl() {
        if (cashierModel != null) {
            JSONObject requestObj = new JSONObject();
            requestObj.put("orderId", String.valueOf(cashierModel.getOrderId()));
            Call<BrandOrderDetailUrlModel> call = RDClient.getService(BrandApi.class).getOrderDetailUrl(requestObj);
            NetworkUtil.showCutscenes(context, call);
            call.enqueue(new RequestCallBack<BrandOrderDetailUrlModel>() {
                @Override
                public void onSuccess(Call<BrandOrderDetailUrlModel> call,
                                      Response<BrandOrderDetailUrlModel> response) {
                    if (MiscUtils.isEmpty(response.body().getDetailUrl())) {
                        context.finish();
                        return;
                    }
                    Intent intent = new Intent();
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra(HTML5WebView.INTENT_BASE_URL, response.body().getDetailUrl());
                    ActivityUtils.push(HTML5WebView.class, intent);
                    context.finish();
                }

                @Override
                public void onFailure(Call<BrandOrderDetailUrlModel> call, Throwable t) {
                    super.onFailure(call, t);
                    context.finish();
                }
            });
        } else {
            context.finish();
        }
    }

    /**
     * 点击打开协议dialog
     */
    public void onOpenAgreementClick(View view) {
        AgreementDialog.Builder builder = new AgreementDialog.Builder(context)
                .setPayAmount(cashierModel.getAp().getPayAmount())
                .setPer(perChecked.nPer.get()).setPoundageAmount(perChecked.getModel().getPoundageAmount());
        builder.create().show();

    }


    /**
     * 打开分期信息
     */
    public void openNperDetail(View view) {
        if (nperListModel == null || MiscUtils.isEmpty(nperListModel.getNperList())) return;
        new CashierPerDtlDialog.Builder().setContext(context).setNperListModel(nperListModel.getNperList().get(perIndexChecked)).create()
                .show();
    }


    @Override
    protected void selectView(ItemView itemView, int position, ItemBankVM item) {
        if (item.getItemDataPair().getItemType() == BANK_ITEM_CARD)
            itemView.set(BR.viewModel, R.layout.item_cashier_bank_list);
        else if (item.getItemDataPair().getItemType() == BANK_ITEM_ADD)
            itemView.set(BR.viewModel, R.layout.item_cashier_bank_list_add);
        else
            itemView.set(BR.viewModel, R.layout.item_cashier_bank_list_other);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusCome(Event event) {
        if (event != null) {
            switch (event.getCode()) {
                case EventCode.BIND_CARD_TO_PAYMENT:
                    //绑卡成功后返回的卡号
                    cardId = (String) event.getData();
                    return;
                case EventCode.CASHIER_RELOAD:
                default:
                    //刷新页面
                    reloadPayInfo();
                    break;
            }

        }
    }

}
