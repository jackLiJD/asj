package com.ald.asjauthlib.cashier.viewmodel;

import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.view.View;

import com.ald.asjauthlib.BR;
import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.ui.BankCardAddIdActivity;
import com.ald.asjauthlib.cashier.model.CashierModel;
import com.ald.asjauthlib.cashier.model.ItemDataPair;
import com.ald.asjauthlib.cashier.ui.CombPayActivity;
import com.ald.asjauthlib.cashier.utils.CashierPayUtils;
import com.ald.asjauthlib.dialog.PwdDialog;
import com.ald.asjauthlib.event.Event;
import com.ald.asjauthlib.event.EventBusUtil;
import com.ald.asjauthlib.event.EventCode;
import com.ald.asjauthlib.utils.AppUtils;
import com.ald.asjauthlib.utils.ModelEnum;
import com.ald.asjauthlib.utils.Permissions;
import com.ald.asjauthlib.utils.StageJumpEnum;
import com.ald.asjauthlib.utils.TimerCountDownUtils;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.location.LocationResult;
import com.ald.asjauthlib.authframework.core.location.LocationUtils;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.authframework.core.utils.PermissionCheck;
import com.ald.asjauthlib.authframework.core.utils.UIUtils;
import com.ald.asjauthlib.authframework.core.vm.BaseRecyclerViewVM;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import com.ald.asjauthlib.tatarka.bindingcollectionadapter.ItemView;

import static com.ald.asjauthlib.utils.BundleKeys.REQUEST_CODE_BANKCARD_ADD_CP;
import static com.ald.asjauthlib.cashier.viewmodel.ItemBankVM.BANK_ITEM_ADD;
import static com.ald.asjauthlib.cashier.viewmodel.ItemBankVM.BANK_ITEM_ALI;


/*
 * Created by luckyliang on 2018/3/5.
 */

public class CombPayVM extends BaseRecyclerViewVM<ItemBankVM> implements ItemBankVM.OnItemClickListener {

    public final ObservableField<String> usableAmount = new ObservableField<>();
    public final ObservableField<String> perDtl = new ObservableField<>();
    public final ObservableField<String> showBankPayAmount = new ObservableField<>();
    public final ObservableBoolean payBtnEnabled = new ObservableBoolean(true);
    public final ObservableField<String> payBtnTxt = new ObservableField<>();
    public final ObservableField<String> payFailedTip = new ObservableField<>();//支付失败提示
    public final ObservableBoolean showFailedTip = new ObservableBoolean(false);//是否显示支付失败提示
    public final ObservableField<String> displayCategoryName = new ObservableField<>();//额度名称默认花吧

    private CombPayActivity combPayActivity;
    private int indexChecked = -1;
    private String scene;
    private String nPer;
    private String payType;
    private boolean isOverTime = false;//订单是否超时
    private CashierModel.BankCardModel bankCardModelChecked;
    private CashierModel cashierModel;
    private String stage;
    private ItemBankVM selectedItem;
    private boolean secretFree = false;//是否需要免密支付
    private String pwdCache;
    private String categoryName = "花吧";

    public CombPayVM(CombPayActivity activity, Intent intent) {
        this.combPayActivity = activity;
        cashierModel = intent.getParcelableExtra("CashierModel");
        long startTime = intent.getLongExtra("startTime", 0);
        if (startTime != 0) {
            long time = startTime;
            if (time < 0) {
                time = 0;
            }
            //倒计时
            timerDown(time, 1000, true);
        }
        nPer = intent.getStringExtra("nPer");
        payType = intent.getStringExtra("payType");
        scene = intent.getStringExtra(BundleKeys.INTENT_KEY_CREDIT_PROMOTE_SCENE);
        stage = intent.getStringExtra(BundleKeys.STAGE_JUMP);

        categoryName = cashierModel.getAp() != null && MiscUtils.isNotEmpty(cashierModel.getAp().getCategoryName()) ? cashierModel.getAp().getCategoryName() : "花吧";
        displayCategoryName.set(categoryName);
        usableAmount.set("¥" + cashierModel.getAp().getUseableAmount());
        perDtl.set(nPer + "期");
        showBankPayAmount.set("¥" + cashierModel.getCp().getPayAmount());
        payBtnTxt.set(categoryName + "支付" + String.format(AlaConfig.getResources().getString(R.string.format_rmb), AppUtils.formatAmount(cashierModel.getAp().getUseableAmount())) + "银行卡支付¥" + cashierModel.getCp().getPayAmount());

        //银行卡数据
        initBankCardList();
        EventBusUtil.register(this);

    }

    private void initBankCardList() {
        CashierModel.BankCardModel bankCardModel;
        ItemDataPair itemDataPair;
        ItemBankVM itemBankVM;
        List<CashierModel.BankCardModel> bankCardModelList = cashierModel.getBankCardList();
        items.clear();
        for (int i = 0; bankCardModelList != null && i < bankCardModelList.size(); i++) {
            bankCardModel = bankCardModelList.get(i);
            if (indexChecked == -1 && bankCardModel.getIsValid().equals("Y")) {
                indexChecked = i;
                bankCardModelChecked = bankCardModel;
            }
            itemDataPair = new ItemDataPair(bankCardModel, ItemBankVM.BANK_ITEM_CARD);
            itemBankVM = new ItemBankVM(itemDataPair, combPayActivity, i, this);
            items.add(itemBankVM);
        }
        if (cashierModel.getCpali() != null && ModelEnum.Y.getModel().equals(cashierModel.getCpali().getStatus())) {
            bankCardModel = new CashierModel.BankCardModel();
            bankCardModel.setBankName("支付宝");
            itemDataPair = new ItemDataPair(bankCardModel, BANK_ITEM_ALI);
            int index = items.size();
            //如果第一个维护中银行卡index为0时（所有银行卡都在维护中），或者银行卡列表为空时，选中该项
            itemBankVM = new ItemBankVM(itemDataPair, combPayActivity, index, this);
            items.add(index, itemBankVM);

        }
        itemDataPair = new ItemDataPair(null, BANK_ITEM_ADD);
        itemBankVM = new ItemBankVM(itemDataPair, combPayActivity, items.size(), this);
        items.add(itemBankVM);
        if (indexChecked == -1)
            payBtnEnabled.set(false);
        else {
            ItemBankVM itemChecked = items.get(indexChecked);
            itemChecked.checkerBg.set(AlaConfig.getResources().getDrawable(R.drawable.ic_cashier_checked));
            itemChecked.isChecked.set(true);
            itemChecked.showCreditCharge.set((((CashierModel.BankCardModel) itemChecked.getItemDataPair().getData()).getCardType()) == ModelEnum.CREDIT.getValue());
            selectedItem = itemChecked;
            changeBottomTxt(itemChecked.getItemDataPair());
        }
    }

    /**
     * @author Yangyang
     * @desc 如果是信用卡需要更改文案
     */
    private void changeBottomTxt(ItemDataPair itemDataPair) {
        String bankPayAmount = cashierModel.getCp().getPayAmount();
        String creditAmount = AppUtils.decimalCalculation(bankPayAmount, 0.0045);
        //选择信用卡
        if (bankCardModelChecked.getCardType() == ModelEnum.CREDIT.getValue()) {
            double allAmount = Double.valueOf(cashierModel.getAmount()) + Double.valueOf(creditAmount);
            payBtnTxt.set(categoryName + "支付" + usableAmount.get() + "+银行卡支付" + showBankPayAmount.get());
            //选择储蓄卡
        } else {
            if (BANK_ITEM_ALI == itemDataPair.getItemType()) {
                payBtnTxt.set(categoryName + "支付" + usableAmount.get() + "+支付宝支付" + showBankPayAmount.get());
            } else {
                payBtnTxt.set(categoryName + "支付" + usableAmount.get() + "+银行卡支付" + showBankPayAmount.get());
            }
        }
    }


    public void payClick(View view) {
        if (isOverTime) {
            UIUtils.showToast(combPayActivity.getString(R.string.toast_pay_timeout));
            return;
        }
        if (secretFree && selectedItem.getItemDataPair().getItemType() == ItemBankVM.BANK_ITEM_CARD) {
            pay(pwdCache);
        } else {
            //弹出输入密码dialog
            new PwdDialog.Builder(combPayActivity)
                    .setStage(StageJumpEnum.STAGE_CASHIER.getModel())
                    .setOnFinishListener(text -> {
                        int type = selectedItem.getItemDataPair().getItemType();
                        doCashierPay(nPer, bankCardModelChecked, BANK_ITEM_ALI == type ? "-2" : "0", text, true, msg -> {
                            secretFree = true;
                            pwdCache = text;
                            payFailedTip.set(msg);
                            showFailedTip.set(true);
                        });
                    }).create().show();
        }
    }

    /**
     * 支付
     *
     * @param pwd
     */
    private void pay(String pwd) {
        doCashierPay(nPer, bankCardModelChecked, "0", pwd, true, msg -> {
            secretFree = true;
            payFailedTip.set(msg);
            showFailedTip.set(true);
        });
    }

    /**
     * 支付宝支付点击
     */
    private void AliPay() {
        doCashierPay(null, null, "-2", "", false, null);
    }

    private void doCashierPay(String nper, CashierModel.BankCardModel bankCardModel, String payId, String text, boolean isComPay, CashierPayUtils.PayBuilder.OnPayFailedListener onPayFailedListener) {
        LocationResult result = LocationUtils.getCurrentLocation();
        if (null != result) {
            handlePay(nper, bankCardModel, payId, text, result, isComPay, onPayFailedListener);
        } else {
            //定位权限
            PermissionCheck.getInstance().askForPermissions(combPayActivity, Permissions.locationPermissions,
                    PermissionCheck.REQUEST_CODE_ALL);
            AlaConfig.execute(() -> {
                LocationUtils.requestLocation(10000L);
                if (LocationUtils.isLocationFailure()) {
                    UIUtils.showToast(AlaConfig.getResources().getString(R.string.toast_get_location_fail));
                    return;
                }
                LocationResult result1 = LocationUtils.getCurrentLocation();
                handlePay(nper, bankCardModel, payId, text, result1, isComPay, onPayFailedListener);
            });
        }
    }

    private void handlePay(String nper, CashierModel.BankCardModel bankCardModel, String payId, String text, LocationResult result, boolean isComPay, CashierPayUtils.PayBuilder.OnPayFailedListener onPayFailedListener) {
        new CashierPayUtils.PayBuilder(combPayActivity)
                .setNper(nper)
                .setBankCardModel(bankCardModel)
                .setCashierModel(cashierModel)
                .setPayType(payType)
                .setStage(stage)
                .setOnPayFailedListener(onPayFailedListener)
                .handlePay(payId, text, result, isComPay);
    }

    @Override
    protected void selectView(ItemView itemView, int position, ItemBankVM item) {
        if (item.getItemDataPair().getItemType() == ItemBankVM.BANK_ITEM_CARD)
            itemView.set(BR.viewModel, R.layout.item_cashier_bank_list);
        else if (item.getItemDataPair().getItemType() == BANK_ITEM_ADD)
            itemView.set(BR.viewModel, R.layout.item_cashier_bank_list_add);
        else
            itemView.set(BR.viewModel, R.layout.item_cashier_bank_list_other);

    }

    @Override
    public void onClick(ItemBankVM itemBankVM) {
        if (itemBankVM.getItemDataPair().getItemType() == BANK_ITEM_ADD) {
            //添加银行卡
            Intent intent = new Intent();
            intent.putExtra(BundleKeys.BANK_CARD_NAME, cashierModel.getRealName());
            intent.putExtra(BundleKeys.STAGE_JUMP, StageJumpEnum.STAGE_CASHIER.getModel());
            intent.putExtra(BundleKeys.INTENT_KEY_CASHIER_NPER, nPer);
            intent.putExtra(BundleKeys.INTENT_KEY_CASHIER_PAY_TYPE, payType);
            intent.putExtra(BundleKeys.INTENT_KEY_CREDIT_PROMOTE_SCENE, scene);
            intent.putExtra(BundleKeys.INTENT_KEY_CASHIER_BTN_TEXT,
                    payBtnTxt.get());
            ActivityUtils.push(BankCardAddIdActivity.class, intent,
                    REQUEST_CODE_BANKCARD_ADD_CP);

        } else if (ItemBankVM.BANK_ITEM_CARD == itemBankVM.getItemDataPair().getItemType()) {
            if (((CashierModel.BankCardModel) itemBankVM.getItemDataPair().getData()).getIsValid().equals("Y")) {
                items.get(indexChecked).isChecked.set(false);
                items.get(indexChecked).checkerBg.set(AlaConfig.getResources().getDrawable(R.drawable.ic_cashier_unchecked));
                indexChecked = itemBankVM.getPosition();
                itemBankVM.isChecked.set(true);
                itemBankVM.checkerBg.set(AlaConfig.getResources().getDrawable(R.drawable.ic_cashier_checked));
                bankCardModelChecked = (CashierModel.BankCardModel) itemBankVM.getItemDataPair().getData();
                selectedItem = itemBankVM;
                changeBottomTxt(itemBankVM.getItemDataPair());
            }
        } else if (BANK_ITEM_ALI == itemBankVM.getItemDataPair().getItemType()) {
            items.get(indexChecked).isChecked.set(false);
            items.get(indexChecked).checkerBg.set(AlaConfig.getResources().getDrawable(R.drawable.ic_cashier_unchecked));
            indexChecked = itemBankVM.getPosition();
            itemBankVM.isChecked.set(true);
            itemBankVM.checkerBg.set(AlaConfig.getResources().getDrawable(R.drawable.ic_cashier_checked));
            bankCardModelChecked = (CashierModel.BankCardModel) itemBankVM.getItemDataPair().getData();
            selectedItem = itemBankVM;
            changeBottomTxt(itemBankVM.getItemDataPair());
        }

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
                combPayActivity.setRightText(toClock(millisUntilFinished),
                        null, AlaConfig.getResources().getColor(R.color.color_verifypay_countdown));
            }

            @Override
            public String toClock(long millisUntilFinished) {
                return super.toClock(millisUntilFinished, isBrand, "后未支付将自动关闭订单");
            }

            @Override
            public void onFinish() {
                combPayActivity.setRightText("订单超时", null, AlaConfig.getResources().getColor(R.color.color_verifypay_countdown));
                isOverTime = true;
                super.onFinish();
            }
        }.start();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusCome(Event event) {
        if (event != null && event.getCode() == EventCode.CASHIER_RELOAD) {
            combPayActivity.finish();
        }
    }

    public void onDestroy() {
        EventBusUtil.unregister(this);
    }
}
