package com.ald.asjauthlib.cashier.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.databinding.ObservableList;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.BusinessApi;
import com.ald.asjauthlib.auth.model.BannerModel;
import com.ald.asjauthlib.bindingadapter.view.ViewBindingAdapter;
import com.ald.asjauthlib.cashier.model.LimitDetailModel;
import com.ald.asjauthlib.cashier.ui.RepaymentActivity;
import com.ald.asjauthlib.cashier.ui.RepaymentStatusActivity;
import com.ald.asjauthlib.event.Event;
import com.ald.asjauthlib.event.EventBusUtil;
import com.ald.asjauthlib.event.EventCode;
import com.ald.asjauthlib.utils.AppUtils;
import com.ald.asjauthlib.utils.BannerClickUtils;
import com.ald.asjauthlib.utils.ModelEnum;
import com.ald.asjauthlib.utils.StageJumpEnum;
import com.ald.asjauthlib.utils.UIHelper;
import com.ald.asjauthlib.web.HTML5WebView;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.network.NetworkUtil;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.network.RequestCallBack;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.authframework.core.utils.ScreenMatchUtils;
import com.ald.asjauthlib.authframework.core.vm.BaseVM;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.ald.asjauthlib.utils.AppUtils.formatAmountKeepMinus;


/**
 * Created by luckyliang on 2017/12/24.
 * 还款结果页
 */

public class RepayStatusVM extends BaseVM {
    private final String refild;
    private final String type;
    private Activity context;

    public final ObservableField<String> displayCash = new ObservableField<>();//还款金额
    public final ObservableField<String> displayStatusTxt = new ObservableField<>();//还款状态（成功、失败、打款成功）
    public final ObservableField<String> displayNoTitle = new ObservableField<>();
    public final ObservableField<String> displayNo = new ObservableField<>();//还款编号
    public final ObservableField<String> displayTime = new ObservableField<>();//还款时间

    //还款
    public final ObservableField<String> displayDesc = new ObservableField<>();//还款说明
    public final ObservableField<String> displayPayWay = new ObservableField<>();//还款方式 账户尾号
    public final ObservableField<String> displayRebateAmount = new ObservableField<>();//余额抵扣
    public final ObservableField<String> displayCouponAmount = new ObservableField<>();//优惠券抵扣

    public final ObservableField<Drawable> statusIcon = new ObservableField<>();//还款结果图标
    public final ObservableInt lineSetpColor = new ObservableInt(0);//进度条颜色
    public final ObservableInt statusTxtColor = new ObservableInt(0);//还款结果颜色

    public final ObservableField<String> displayStatusHintTxt = new ObservableField<>();

    public final ObservableInt adWidth = new ObservableInt();
    public final ObservableInt adHeight = new ObservableInt();
    public final ObservableList<BannerModel> bannerList = new ObservableArrayList<>();
    public final ObservableField<ViewBindingAdapter.BannerListener> bannerListener = new ObservableField<>();

    public RepayStatusVM(RepaymentStatusActivity activity) {
        this.context = activity;
        this.refild = context.getIntent().getStringExtra(BundleKeys.LIMIT_REFILD_ID);
        this.type = context.getIntent().getStringExtra(BundleKeys.LIMIT_TYPE);
//        CreditCenterActivity.needRefresh = true;
        load();
    }

    private void load() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("refId", refild);
        jsonObject.put("type", type);
        Call<LimitDetailModel> call = RDClient.getService(BusinessApi.class).getLimitDetailInfo(jsonObject);
        NetworkUtil.showCutscenes(context, call);
        call.enqueue(new RequestCallBack<LimitDetailModel>() {
            @Override
            public void onSuccess(Call<LimitDetailModel> call, Response<LimitDetailModel> response) {
                LimitDetailModel model = response.body();

                //还款金额
                String amount = formatAmountKeepMinus(response.body().getAmount());
                displayCash.set(amount + "元");

                //还款页面
                if (ModelEnum.Y.getModel().equals(model.getStatus())) {
                    //还款成功
                    statusIcon.set(ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.ic_repay_status_success));
                    lineSetpColor.set(ContextCompat.getColor(AlaConfig.getContext(), R.color.color_step_line_blue));
                    statusTxtColor.set(ContextCompat.getColor(AlaConfig.getContext(), R.color.color_step_line_blue));
                    displayStatusTxt.set(AlaConfig.getResources().getString(R.string.repayment_status_success));
                    displayStatusHintTxt.set(AlaConfig.getResources().getString(R.string.repayment_status_success_hint));
                } else if (ModelEnum.F.getModel().equals(model.getStatus())) {
                    //还款失败
                    statusTxtColor.set(ContextCompat.getColor(AlaConfig.getContext(), R.color.color_history_bill_money));
                    statusIcon.set(ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.ic_repay_status_fail));
                    lineSetpColor.set(ContextCompat.getColor(AlaConfig.getContext(), R.color.color_step_line_gray));
                    displayStatusTxt.set(AlaConfig.getResources().getString(R.string.repayment_status_fail));
                    displayStatusHintTxt.set(AlaConfig.getResources().getString(R.string.repayment_status_fail_hint));
                } else if (ModelEnum.N.getModel().equals(model.getStatus()) || ModelEnum.P.getModel()
                        .equals(model.getStatus())) {
                    //还款处理中
                    statusTxtColor.set(ContextCompat.getColor(AlaConfig.getContext(), R.color.color_step_line_blue));
                    lineSetpColor.set(ContextCompat.getColor(AlaConfig.getContext(), R.color.color_step_line_gray));
                    statusIcon.set(ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.ic_repay_status_ing));
                    displayStatusHintTxt.set(AlaConfig.getResources().getString(R.string.repayment_status_success_hint));
                    displayStatusTxt.set(AlaConfig.getResources().getString(R.string.repayment_status_wait));
                }

                //还款账户
                String strPayway = MiscUtils.isEmpty(response.body().getCardNo()) ? ""
                        : (response.body().getCardName() + "尾号(" + response.body().getCardNo() + ")");
                displayPayWay.set(MiscUtils.isEmpty(strPayway) ? model.getCardName() : strPayway);

                //账户抵扣
                displayRebateAmount.set(AppUtils.formatAmount(model.getRebateAmount()) + "元");

                //优惠抵扣
                displayCouponAmount.set(AppUtils.formatAmount(model.getCouponAmount()) + "元");

                //还款编号
                displayNo.set(model.getNumber());

                //还款时间
                String time = MiscUtils.format(new Date(response.body().getGmtCreate()), "yyyy-MM-dd HH:mm");
                displayTime.set(time);

                context.setResult(Activity.RESULT_OK);
            }
        });
        adWidth.set(AlaConfig.getResources().getDisplayMetrics().widthPixels);
        adHeight.set(ScreenMatchUtils.repayStatusADHeight(adWidth.get(), 0));
        BannerClickUtils.setBannerSource(context, BannerClickUtils.TYPE_INSTALLMENT, bannerList, bannerListener, BannerClickUtils.TYPE_BANNER, new BannerClickUtils.ADdataListener() {
            @Override
            public void adDataCallBack(List<BannerModel> models, int position) {
            }
        });
    }

    /**
     * 拨打客服电话
     */
    public void telClick(View view) {
        UIHelper.telService(context, AlaConfig.getResources().getString(R.string.service_phone));

    }

    /**
     * 返回
     */
    public void onBackClick(View view) {
        String stage = context.getIntent().getStringExtra(BundleKeys.STAGE_JUMP);
        if (MiscUtils.isEmpty(stage) || stage.equals(StageJumpEnum.STAGE_TO_REPAY_H5.getModel())) {
            //如果来自h5页面需要刷新
            refreshH5();
            ActivityUtils.popUntil(HTML5WebView.class);
        } else if (stage.equals(StageJumpEnum.STAGE_CREDIT_CENTER.getModel())) {
//            ActivityUtils.popUntil(CreditCenterActivity.class);
        } else if (stage.equals(StageJumpEnum.STAGE_CASHIER.getModel())) {
            // 来自收银台 逾期账单跳转 返回到账单列表 并刷新收银台
            ActivityUtils.popUntilWithoutRefresh(RepaymentActivity.class);
            EventBusUtil.sendEvent(new Event(EventCode.REPAYMENT_RELOAD));
        }
    }

    private void refreshH5() {
        Intent intent = new Intent(HTML5WebView.ACTION_REFRESH);
        context.sendBroadcast(intent);
    }


}
