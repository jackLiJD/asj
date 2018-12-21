package com.ald.asjauthlib.cashier.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.model.BannerModel;
import com.ald.asjauthlib.bindingadapter.view.ViewBindingAdapter;
import com.ald.asjauthlib.cashier.LoanApi;
import com.ald.asjauthlib.cashier.model.LoanRepaymentDetailModel;
import com.ald.asjauthlib.cashier.model.WxOrAlaPayModel;
import com.ald.asjauthlib.utils.AppUtils;
import com.ald.asjauthlib.utils.BannerClickUtils;
import com.ald.asjauthlib.utils.ModelEnum;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.network.NetworkUtil;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.network.RequestCallBack;
import com.ald.asjauthlib.authframework.core.utils.DensityUtils;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.authframework.core.utils.ScreenMatchUtils;
import com.ald.asjauthlib.authframework.core.vm.BaseVM;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
public class LoanRepaymentDetailVM extends BaseVM {
    private final String repayId;
    private final String limitType;
    private Activity context;

    //还款、借款都有的公共字段
    public final ObservableField<String> displayCash = new ObservableField<>();
    public final ObservableField<String> displayTitle = new ObservableField<>();
    public final ObservableField<String> displayNo = new ObservableField<>();
    public final ObservableField<String> displayTime = new ObservableField<>();
    //还款
    public final ObservableField<String> displayDesc = new ObservableField<>();//还款说明
    public final ObservableField<String> displayOffer = new ObservableField<>();//优惠抵扣
    public final ObservableField<String> displayRebate = new ObservableField<>();//还款账户余额
    public final ObservableField<String> displayActualPay = new ObservableField<>();//还款现金
    public final ObservableField<String> displayPayWay = new ObservableField<>();//还款方式

    public final ObservableBoolean displayOfferInfo = new ObservableBoolean();
    public final ObservableBoolean displayRebateInfo = new ObservableBoolean();
    public final ObservableBoolean displayActualPayInfo = new ObservableBoolean();

    public final ObservableArrayList<BannerModel> bannerList = new ObservableArrayList<>();
    public final ObservableField<ViewBindingAdapter.BannerListener> bannerListener = new ObservableField<>();
    public final ObservableInt adWidth = new ObservableInt();
    public final ObservableInt adHeight = new ObservableInt();

    private String pageType = ModelEnum.CASH_PAGE_TYPE_NEW_V2.getModel();//新老借钱页面跳转类型
    private WxOrAlaPayModel wxOrAlaPayModel;


    public LoanRepaymentDetailVM(Activity activity) {
        this.context = activity;
        Intent intent = context.getIntent();
        this.repayId = intent.getStringExtra(BundleKeys.LOAN_REPAYMENT_REPAY_ID);
        this.limitType = intent.getStringExtra(BundleKeys.LOAN_REPAYMENT_TYPE);
        pageType = intent.getStringExtra(BundleKeys.CASH_LOAN_PAGE_TYPE);
        wxOrAlaPayModel = intent.getParcelableExtra(BundleKeys.LOAN_REPAYMENT_MODEL);
        if (ModelEnum.CASH_PAGE_TYPE_NEW_V2.getModel().equals(pageType) || ModelEnum.CASH_PAGE_TYPE_NEW_V1.getModel().equals(pageType)) {
            handleData(wxOrAlaPayModel);
            return;
        }
        load();
    }

    private void load() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("repayId", repayId);
        Call<LoanRepaymentDetailModel> call = RDClient.getService(LoanApi.class).getRepayCashInfo(jsonObject);
        NetworkUtil.showCutscenes(context, call);
        call.enqueue(new RequestCallBack<LoanRepaymentDetailModel>() {
            @Override
            public void onSuccess(Call<LoanRepaymentDetailModel> call, Response<LoanRepaymentDetailModel> response) {
                LoanRepaymentDetailModel model = response.body();
                String amount = formatAmount(model.getAmount());
                displayCash.set("￥" + amount);
                if (ModelEnum.Y.getModel().equals(model.getStatus())) {
                    displayTitle.set(AlaConfig.getResources().getString(R.string.limit_detail_success_state));
                } else if (ModelEnum.N.getModel().equals(model.getStatus())) {
                    displayTitle.set(AlaConfig.getResources().getString(R.string.limit_detail_failed_state));
                } else {
                    displayTitle.set(AlaConfig.getResources().getString(R.string.limit_detail_default_state));
                }
                if (limitType.equals(ModelEnum.BILL_TYPE_CASH.getModel())) {
                    displayDesc.set(ModelEnum.BILL_TYPE_CASH.getDesc() + model.getCardNumber());
                } else if (limitType.equals(ModelEnum.BILL_TYPE_CONSUME.getModel())) {
                    displayDesc.set(ModelEnum.BILL_TYPE_CONSUME.getDesc());
                } else if (limitType.equals(ModelEnum.BILL_TYPE_REPAYMENT.getModel())) {
                    displayDesc.set(ModelEnum.BILL_TYPE_REPAYMENT.getDesc());
                }
                if (model.getCouponAmount().compareTo(BigDecimal.ZERO) > 0) {
                    displayOfferInfo.set(true);
                    displayOffer.set("￥" + formatAmount(model.getCouponAmount()));
                }
                if (model.getUserAmount().compareTo(BigDecimal.ZERO) > 0) {
                    displayRebateInfo.set(true);
                    displayRebate.set("￥" + formatAmount(model.getUserAmount()));
                }

                if (model.getActualAmount().compareTo(BigDecimal.ZERO) > 0) {
                    displayActualPayInfo.set(true);
                    displayActualPay.set("￥" + formatAmount(model.getActualAmount()));
                }
                displayPayWay.set(model.getCardName());
                displayNo.set(model.getRepayNo());
                String time = MiscUtils.format(new Date(response.body().getGmtCreate()), "yyyy-MM-dd HH:mm");
                displayTime.set(time);
            }
        });
    }

    //处理页面数据-合规
    private void handleData(WxOrAlaPayModel model) {
        if (model != null) {
            displayCash.set(String.format(AlaConfig.getResources().getString(R.string.price_formatter_only_symbol), model.getAmount()));
            if (ModelEnum.Y.getModel().equals(model.getStatus())) {
                displayTitle.set(AlaConfig.getResources().getString(R.string.limit_detail_success_state));
            } else if (ModelEnum.N.getModel().equals(model.getStatus())) {
                displayTitle.set(AlaConfig.getResources().getString(R.string.limit_detail_failed_state));
            } else {
                displayTitle.set(AlaConfig.getResources().getString(R.string.limit_detail_default_state));
            }

            /*if (limitType.equals(ModelEnum.BILL_TYPE_CASH.getModel())) {
                displayDesc.set(ModelEnum.BILL_TYPE_CASH.getDesc() + model.getCardNumber());
            } else if (limitType.equals(ModelEnum.BILL_TYPE_CONSUME.getModel())) {
                displayDesc.set(ModelEnum.BILL_TYPE_CONSUME.getDesc());
            } else if (limitType.equals(ModelEnum.BILL_TYPE_REPAYMENT.getModel())) {
                displayDesc.set(ModelEnum.BILL_TYPE_REPAYMENT.getDesc());
            }*/

            if (model.getCouponAmount() != null && new BigDecimal(model.getCouponAmount()).compareTo(BigDecimal.ZERO) > 0) {
                displayOfferInfo.set(true);
                displayOffer.set("￥" + formatAmount(model.getCouponAmount()));
            }
            if (model.getUserAmount() != null && new BigDecimal(model.getUserAmount()).compareTo(BigDecimal.ZERO) > 0) {
                displayRebateInfo.set(true);
                displayRebate.set("￥" + formatAmount(model.getUserAmount()));
            }

            if (model.getActualAmount() != null && new BigDecimal(model.getActualAmount()).compareTo(BigDecimal.ZERO) > 0) {
                displayActualPayInfo.set(true);
                displayActualPay.set("￥" + formatAmount(model.getActualAmount()));
            }
            displayPayWay.set(model.getCardName());
            displayNo.set(model.getRepayNo());
//            String time = MiscUtils.format(new Date(model.getGmtCreate()), "yyyy-MM-dd HH:mm");
            String time = AppUtils.coverTimeYMDHMS(model.getGmtCreate());
            displayTime.set(time);
        }

        adWidth.set((int) (AlaConfig.getResources().getDisplayMetrics().widthPixels - AlaConfig.getResources().getDimension(R.dimen.x24) * 2));
        adHeight.set(ScreenMatchUtils.repayStatusADHeight(adWidth.get(), DensityUtils.dp2px(context, 15)));
        BannerClickUtils.setBannerSource(context, BannerClickUtils.TYPE_BORROW_MONEY, bannerList, bannerListener, BannerClickUtils.TYPE_BANNER, new BannerClickUtils.ADdataListener() {
            @Override
            public void adDataCallBack(List<BannerModel> models, int position) {
//                MobclickAgent.onEvent(context, "position_jqhk_" + position);
            }
        });
    }

}
