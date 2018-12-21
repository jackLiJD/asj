package com.ald.asjauthlib.auth.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.AuthApi;
import com.ald.asjauthlib.auth.model.AllLimitModel;
import com.ald.asjauthlib.auth.model.BannerModel;
import com.ald.asjauthlib.auth.model.BorrowHomeModel;
import com.ald.asjauthlib.auth.ui.AllLimitActivity;
import com.ald.asjauthlib.auth.ui.BankCardAddIdActivity;
import com.ald.asjauthlib.auth.ui.CreditPromoteActivity;
import com.ald.asjauthlib.auth.ui.RRIdAuthActivity;
import com.ald.asjauthlib.bindingadapter.view.ViewBindingAdapter;
import com.ald.asjauthlib.utils.AppUtils;
import com.ald.asjauthlib.utils.BannerClickUtils;
import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.utils.StageJumpEnum;
import com.ald.asjauthlib.web.HTML5WebView;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.network.RequestCallBack;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.authframework.core.vm.BaseVM;

import retrofit2.Call;
import retrofit2.Response;

import static com.ald.asjauthlib.utils.BannerClickUtils.TYPE_RENT;

/**
 * Created by luckyliang on 2018/1/8.
 * <p>
 * 全部额度(额度拆分)
 */
public class AllLimitVM extends BaseVM {

    public final ObservableField<String> displayLoanAmount = new ObservableField<>();
    public final ObservableField<String> displayLoanHint = new ObservableField<>();
    public final ObservableField<String> displayLoanAuthBtnTxt = new ObservableField<>();
    public final ObservableBoolean showLoanAuth = new ObservableBoolean(false);//借钱认证按钮显示
    public final ObservableField<Drawable> displayCashBg = new ObservableField<>();


    public final ObservableField<String> displayBuyAmount = new ObservableField<>();
    public final ObservableField<String> displayBuyHint = new ObservableField<>();
    public final ObservableField<String> displayBuyAuthBtnTxt = new ObservableField<>();
    public final ObservableBoolean showBuyAuth = new ObservableBoolean(false);//购物认证按钮显示
    public final ObservableField<Drawable> displayOnlineBg = new ObservableField<>();


    public final ObservableField<String> displayOffLineTitle = new ObservableField<>();
    public final ObservableField<String> displayOffLineDesc = new ObservableField<>();//线下培训描述

    public final ObservableField<String> displayRentTitle = new ObservableField<>();
    public final ObservableField<String> displayRentDesc = new ObservableField<>();
    public final ObservableBoolean showRentLayout = new ObservableBoolean();

    private BorrowHomeModel borrowHomeModel;
    private AllLimitModel allLimitModel;
    private AllLimitModel.Limit loanLimit;
    private AllLimitModel.Limit onLineLimit;
    private AllLimitModel.Limit trainLimit;
    private AllLimitModel.Limit rentLimit;
    private Context context;
    private String realName;

    public final ObservableList<BannerModel> bannerList = new ObservableArrayList<>();
    public final ObservableField<ViewBindingAdapter.BannerListener> bannerListener = new ObservableField<>();

    /**
     * 全部额度
     *
     * @param realName 真实姓名
     */
    public AllLimitVM(Context context, String realName) {
        this.context = context;
        this.realName = realName;
        this.realName = realName == null ? "" : realName;
        getAllLimitInfo();
        BannerClickUtils.setBannerSource(context, TYPE_RENT, bannerList, bannerListener);
    }

    /**
     * 51信用
     */
    public AllLimitVM(Context context, BorrowHomeModel borrowHomeModel) {
        this.context = context;
        this.borrowHomeModel = borrowHomeModel;
        this.realName = borrowHomeModel.getRealName() == null ? "" : borrowHomeModel.getRealName();
        getAllLimitInfo();
    }

    private void getStageResolutionInfo() {
        //1:尚未认证状态 2:认证一半退出 3:强风控失败 4:认证成功 5.通过人脸识别未绑定银行卡
        //借钱认证状态
        if (loanLimit != null) {
            displayLoanHint.set(loanLimit.getDesc() == null ? "借钱描述" : loanLimit.getDesc());
            if (loanLimit.getStatus() != null) {
                displayCashBg.set(AlaConfig.getResources().getDrawable(loanLimit.getStatus().equals("1")
                        ? R.drawable.bg_stage_loan_limit_icon : R.drawable.bg_stage_loan_limit));
                switch (loanLimit.getStatus()) {
                    case "1":
                        showLoanAuth.set(false);
                        displayLoanAmount.set(loanLimit.getShowAmount() + "元");
                        break;
                    case "2":
                        //认证一半退出
                        showLoanAuth.set(true);
                        displayLoanAmount.set(loanLimit.getShowAmount() + "元");
                        displayLoanAuthBtnTxt.set(AlaConfig.getResources().getString(R.string.stage_resolution_auth_continue));
                        break;

                    case "3":
                        //未通过强风控
                        showLoanAuth.set(true);
                        displayLoanAmount.set(loanLimit.getShowAmount() + "元");
                        displayLoanAuthBtnTxt.set(AlaConfig.getResources().getString(R.string.stage_resolution_credit_promote));
                        break;

                    case "4":
                        //通过强风控审核
                        showLoanAuth.set(true);
                        displayLoanAuthBtnTxt.set(AlaConfig.getResources().getString(R.string.stage_resolution_credit_promote));
                        displayLoanAmount.set(AppUtils.formatAmount(loanLimit.getAmount()) + "元");
                        break;
                    case "5":
                        //“继续认证”
                        showLoanAuth.set(true);
                        displayLoanAmount.set(loanLimit.getShowAmount() + "元");
                        displayLoanAuthBtnTxt.set(AlaConfig.getResources().getString(R.string.stage_resolution_auth_continue));
                        break;
                }
            } else {
                showLoanAuth.set(false);
                displayCashBg.set(AlaConfig.getResources().getDrawable(R.drawable.bg_stage_loan_limit_icon));
            }

        }

        if (onLineLimit != null) {
            //购物认证状态
            displayBuyHint.set(onLineLimit.getDesc());
            if (onLineLimit.getStatus() != null) {
                displayOnlineBg.set(AlaConfig.getResources().getDrawable(onLineLimit.getStatus().equals("1") ?
                        R.drawable.bg_stage_buy_limit_icon : R.drawable.bg_stage_buy_limit));
                switch (onLineLimit.getStatus()) {
                    case "1":
                        displayBuyAmount.set(onLineLimit.getShowAmount() + "元");
                        showBuyAuth.set(false);
                        break;
                    case "2":
                        displayBuyAuthBtnTxt.set(AlaConfig.getResources().getString(R.string.stage_resolution_auth_continue));
                        displayBuyAmount.set(onLineLimit.getShowAmount() + "元");
                        showBuyAuth.set(true);
                        break;

                    case "3":
                        displayBuyAuthBtnTxt.set(AlaConfig.getResources().getString(R.string.stage_resolution_credit_promote));
                        displayBuyAmount.set(AppUtils.formatAmount(onLineLimit.getShowAmount()) + "元");
                        showBuyAuth.set(true);
                        break;
                    case "4":
                        //通过强风控审核
                        displayBuyAmount.set(AppUtils.formatAmount(onLineLimit.getAmount()) + "元");
                        displayBuyAuthBtnTxt.set(AlaConfig.getResources().getString(R.string.stage_resolution_credit_promote));
                        showBuyAuth.set(true);
                        break;
                    case "5":
                        displayBuyAuthBtnTxt.set(AlaConfig.getResources().getString(R.string.stage_resolution_auth_continue));
                        displayBuyAmount.set(onLineLimit.getShowAmount() + "元");
                        showBuyAuth.set(true);
                        break;
                }
            } else {
                showBuyAuth.set(false);
                displayOnlineBg.set(AlaConfig.getResources().getDrawable(R.drawable.bg_stage_buy_limit_icon));
            }
        }

        if (trainLimit != null) {
            //培训状态
            displayOffLineTitle.set(trainLimit.getTitle());
            displayOffLineDesc.set(trainLimit.getDesc());
        }
        if (rentLimit != null) {
            //租房
            showRentLayout.set(true);
            displayRentTitle.set(rentLimit.getTitle());
            displayRentDesc.set(rentLimit.getDesc());
        } else {
            showRentLayout.set(false);
            displayRentTitle.set("租房分期");
        }
    }

    /**
     * 获取全部额度
     */
    private void getAllLimitInfo() {
        if (borrowHomeModel == null) {
            load();
        } else {
            loanLimit = new AllLimitModel.Limit();
            loanLimit.setAmount(borrowHomeModel.getAmount());
            loanLimit.setStatus(borrowHomeModel.getBorrowStatus());
            loanLimit.setAuAmount(borrowHomeModel.getAuAmount() == null ? "" : borrowHomeModel.getAuAmount().toString());
            loanLimit.setDesc(borrowHomeModel.getDesc());
            loanLimit.setShowAmount(borrowHomeModel.getShowAmount());

            onLineLimit = new AllLimitModel.Limit();
            onLineLimit.setAmount(borrowHomeModel.getOnlineAmount());
            onLineLimit.setStatus(borrowHomeModel.getOnlineStatus());
            onLineLimit.setAuAmount(borrowHomeModel.getOnlineAuAmount() == null ? "" : borrowHomeModel.getOnlineAuAmount().toString());
            onLineLimit.setDesc(borrowHomeModel.getOnlineDesc());
            onLineLimit.setShowAmount(borrowHomeModel.getOnlineShowAmount());
            getStageResolutionInfo();
        }
    }

    public void load() {
        Call<AllLimitModel> call = RDClient.getService(AuthApi.class).lookAllQuota();
        call.enqueue(new RequestCallBack<AllLimitModel>() {
            @Override
            public void onSuccess(Call<AllLimitModel> call, Response<AllLimitModel> response) {
                if (response != null) {
                    allLimitModel = response.body();
                    for (AllLimitModel.Limit limit : allLimitModel.getData()) {
                        switch (limit.getScene()) {
                            case BundleKeys.CREDIT_PROMOTE_SCENE_CASH:
                                loanLimit = limit;
                                break;
                            case BundleKeys.CREDIT_PROMOTE_SCENE_ONLINE:
                                onLineLimit = limit;
                                break;
                            case BundleKeys.CREDIT_PROMOTE_SCENE_TRAIN:
                                trainLimit = limit;
                                break;
                            case BundleKeys.CREDIT_PROMOTE_SCENE_RENT:
                                rentLimit = limit;
                                break;
                        }
                    }
                    getStageResolutionInfo();
                }
            }
        });
    }


    /**
     * 继续认证点击
     *
     * @param scene 场景
     */
    private void onAuthClick(final String scene) {
//        CreditCenterActivity.needRefresh = true;
        AllLimitActivity.refreshFlag = true;
        Intent intent = new Intent();
        intent.putExtra(BundleKeys.INTENT_KEY_CREDIT_PROMOTE_SCENE, scene);
        intent.putExtra(BundleKeys.STAGE_JUMP, StageJumpEnum.STAGE_LIMIT_AUTH.getModel());
        String sceneStatus = scene.equals(BundleKeys.CREDIT_PROMOTE_SCENE_CASH) ? loanLimit.getStatus() : onLineLimit.getStatus();
        if (MiscUtils.isEmpty(realName)) {
            realName = loanLimit.getRealName();
        }
        switch (sceneStatus) {
            case "1":
                ActivityUtils.push(RRIdAuthActivity.class, intent, BundleKeys.REQUEST_CODE_ALL_lIMITS);
                break;
            case "5":
//                状态为5 已完成人脸识别 未绑定银行卡 跳转银行卡页面
                intent.putExtra(BundleKeys.BANK_CARD_NAME, realName);
                ActivityUtils.push(BankCardAddIdActivity.class, intent, BundleKeys.REQUEST_CODE_ALL_lIMITS);
                break;
            default:
                //跳转信用页面
                ActivityUtils.push(CreditPromoteActivity.class, intent, BundleKeys.REQUEST_CODE_ALL_lIMITS);
                break;
        }
    }

    /**
     * 借钱场景认证
     */
    public void onLoanAuthClick(View view) {
        onAuthClick(BundleKeys.CREDIT_PROMOTE_SCENE_CASH);
    }

    /**
     * 购物场景认证
     */
    public void onOnlineAuthClick(View view) {
        onAuthClick(BundleKeys.CREDIT_PROMOTE_SCENE_ONLINE);
    }

    /**
     * 线下培训,跳转H5
     */
    public void onTrainClick(View view) {
        if (trainLimit == null)
            return;
        AllLimitActivity.refreshFlag = true;
        Intent intent = new Intent();
        intent.putExtra(HTML5WebView.INTENT_BASE_URL, trainLimit.getJumpUrl());
        intent.putExtra(HTML5WebView.INTENT_TITLE, trainLimit.getTitle());
        ActivityUtils.push(HTML5WebView.class, intent);
    }

    /**
     * 线下租房，跳转h5
     */
    public void onRentClick(View view) {
        if (rentLimit == null)
            return;
        AllLimitActivity.refreshFlag = true;
        Intent intent = new Intent();
        intent.putExtra(HTML5WebView.INTENT_BASE_URL, rentLimit.getJumpUrl());
        intent.putExtra(HTML5WebView.INTENT_TITLE, rentLimit.getTitle());
        ActivityUtils.push(HTML5WebView.class, intent);
    }

}
