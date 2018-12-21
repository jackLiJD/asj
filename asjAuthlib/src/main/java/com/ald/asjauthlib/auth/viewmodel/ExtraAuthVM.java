package com.ald.asjauthlib.auth.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import com.ald.asjauthlib.utils.Constant;
import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.AuthApi;
import com.ald.asjauthlib.utils.MXAuthUtils;
import com.ald.asjauthlib.utils.MxAuthCallBack;
import com.ald.asjauthlib.dialog.TwoButtonDialog;
import com.ald.asjauthlib.auth.model.CreditPromoteModel;
import com.ald.asjauthlib.auth.model.ExtraFundModel;
import com.ald.asjauthlib.auth.model.ExtraGxbModel;
import com.ald.asjauthlib.auth.model.ExtraUserIdModel;
import com.ald.asjauthlib.auth.model.FundModel;
import com.ald.asjauthlib.auth.ui.BankCardAddIdActivity;
import com.ald.asjauthlib.auth.ui.CreditPromoteActivity;
import com.ald.asjauthlib.utils.AppUtils;
import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.utils.ModelEnum;
import com.ald.asjauthlib.utils.StageJumpEnum;
import com.ald.asjauthlib.web.HTML5WebView;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.network.NetworkUtil;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.network.RequestCallBack;
import com.ald.asjauthlib.authframework.core.network.entity.ApiResponse;
import com.ald.asjauthlib.authframework.core.protocol.ProtocolUtils;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.authframework.core.utils.UIUtils;
import com.ald.asjauthlib.authframework.core.vm.ViewModel;

import retrofit2.Call;
import retrofit2.Response;

import static com.ald.asjauthlib.authframework.core.protocol.AlaProtocol.Protocol.GXB_BACK;
import static com.ald.asjauthlib.authframework.core.protocol.AlaProtocol.Protocol.WUYAO_FUND_STATUS;

/**
 * 额外认证
 * Created by sean yu on 2017/7/11.
 */

public class ExtraAuthVM implements ViewModel, ProtocolUtils.OpenNativeHandler {
    public final ObservableField<String> displayMaxAmount = new ObservableField<>();
    //公积金认证状态
    public final ObservableField<String> displayFundStatus = new ObservableField<>();
    //社保认证状态
    public final ObservableField<String> displaySecurityStatus = new ObservableField<>();
    //信用卡认证状态
    public final ObservableField<String> displayCreditCardStatus = new ObservableField<>();
    //支付宝认证状态
    public final ObservableField<String> displayAlipayStatus = new ObservableField<>();
    //人行征信认证状态
    public final ObservableField<String> displayZhengxinStatus = new ObservableField<>();
    //学信网认证状态
    public final ObservableField<String> displayChsiStatus = new ObservableField<>();
    //网银认证状态
    public final ObservableField<String> displayOnlineBankStatus = new ObservableField<>();
    //冒泡认证状态
    public final ObservableField<String> displayBubbleStatus = new ObservableField<>();
    //人行征信、信用卡邮箱显示状态，强风控时不显示，其他情况显示
    public final ObservableField<Integer> riskManagerStatus = new ObservableField<>(0);

    public final ObservableBoolean showOnlineBank = new ObservableBoolean(false);
    //是否显示冒泡认证
    public final ObservableBoolean showBubbleAuth = new ObservableBoolean(false);
    //分期购物场景补充认证只显示社保、公积金、支付宝、信用卡邮箱
    public final ObservableBoolean isOnlineScene = new ObservableBoolean(false);

    public final ObservableField<Integer> showZhengxin = new ObservableField<>(View.GONE);//是否显示人行征信

    //人行征信标识
    private static final String TYPE_ZHENGXIN = "zhengxin";
    //学信网认证标识
    private static final String TYPE_CHSI = "chsi";
    //网银认证标识
    private static final String TYPE_ONLINE_BANK = "ONLINEBANK";


    //认证状态字体颜色
    // 公积金
    public final ObservableInt displayFundStatusColor = new ObservableInt(0);
    public final ObservableInt displaySecurityStatusColor = new ObservableInt(0);
    public final ObservableInt displayCreditCardStatusColor = new ObservableInt(0);
    public final ObservableInt displayAlipayStatusColor = new ObservableInt(0);
    public final ObservableInt displayZhengxinStatusColor = new ObservableInt(0);
    public final ObservableInt displayChsiStatusColor = new ObservableInt(0);
    public final ObservableInt displayOnlineBankStatusColor = new ObservableInt(0);
    public final ObservableInt displayBubbleStatusColor = new ObservableInt(0);


    public final ObservableField<Drawable> statusIcSecurity = new ObservableField<>();
    public final ObservableField<Drawable> statusIcFund = new ObservableField<>();
    public final ObservableField<Drawable> statusIcCreditCard = new ObservableField<>();
    public final ObservableField<Drawable> statusIcAlipay = new ObservableField<>();
    public final ObservableField<Drawable> statusIcZhengxin = new ObservableField<>();
    public final ObservableField<Drawable> statusIcChsi = new ObservableField<>();
    public final ObservableField<Drawable> statusIcOnlineBank = new ObservableField<>();
    public final ObservableField<Drawable> statusIcBubble = new ObservableField<>();


    private MXAuthUtils authUtils;
    private Activity context;
    private CreditPromoteModel mModel;
    private String scene = BundleKeys.CREDIT_PROMOTE_SCENE_CASH;

    public ExtraAuthVM(final Activity activity, CreditPromoteModel model, String scene) {
        this.context = activity;
        this.scene = scene;
        isOnlineScene.set(scene != null && scene.equals(BundleKeys.CREDIT_PROMOTE_SCENE_ONLINE));
        mModel = model;
        authUtils = new MXAuthUtils(activity).setCallBack(new MxAuthCallBack() {
            @Override
            public void authSuccess(String authCode, String msg) {
                //公积金
                if (MXAuthUtils.PARAM_TASK_FUND.equals(authCode)) {
                    handleAuthInfo("FUND");
                } else if (MXAuthUtils.PARAM_TASK_EMAIL.equals(authCode)) {
                    handleAuthInfo("CREDIT");
                } else if (MXAuthUtils.PARAM_TASK_SECURITY.equals(authCode)) {
                    handleAuthInfo("SOCIAL_SECURITY");
                } else if (MXAuthUtils.PARAM_TASK_ALIPAY.equals(authCode)) {
                    handleAuthInfo("ALIPAY");
                } else if (MXAuthUtils.PARAM_TASK_ZHENGXIN.equals(authCode)) {
                    handleAuthInfo(TYPE_ZHENGXIN);
                } else if (MXAuthUtils.PARAM_TASK_CHSI.equals(authCode)) {
                    handleAuthInfo(TYPE_CHSI);
                } else if (MXAuthUtils.PARAM_TASK_ONLINEBANK.equals(authCode)) {
                    handleAuthInfo(TYPE_ONLINE_BANK);
                }
            }

            @Override
            public void authError(String authCode, String errorMsg) {
                UIUtils.showToast(errorMsg);
            }
        });

        //初始化状态图标为向右箭头
        statusIcFund.set(ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.ic_auth_right_arrow));
        statusIcSecurity.set(ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.ic_auth_right_arrow));
        statusIcCreditCard.set(ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.ic_auth_right_arrow));
        statusIcAlipay.set(ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.ic_auth_right_arrow));

        Integer color = ContextCompat.getColor(AlaConfig.getContext(), R.color.color_auth_status_txt);
        displayAlipayStatusColor.set(color);
        displaySecurityStatusColor.set(color);
        displayCreditCardStatusColor.set(color);
        displayAlipayStatusColor.set(color);
        displayChsiStatusColor.set(color);
        displayZhengxinStatusColor.set(color);
        displayAuthView(model);
    }

    public void setOpenNativeHandler(ProtocolUtils.OpenNativeHandler openNativeHandler) {
        ProtocolUtils.setOpenNativeHandler(openNativeHandler);
    }

    /**
     * 刷新认证信息
     */
    public void displayAuthView(CreditPromoteModel model) {
        if (model == null) {
            UIUtils.showToast(AlaConfig.getResources().getString(R.string.err_data_pull_refresh_hint));
            return;
        }
        mModel = model;
        showBubbleAuth.set(ModelEnum.Y.getModel().equals(mModel.getIsOpenBubbleAuth()));
        setRiskStatus();
        //model.getBldRiskStatus已认证（Y） 并且是 现金贷场景 显示网银
        boolean showOnlineStatus = model.getBldRiskStatus() != null && ModelEnum.Y.getModel().equals(model.getBldRiskStatus())
                && scene.equals(BundleKeys.CREDIT_PROMOTE_SCENE_CASH);
        showOnlineBank.set(showOnlineStatus);
        refreshAuthStatus(model.getFundStatus(), model.getGmtFundExist(), displayFundStatus, displayFundStatusColor, statusIcFund, model.getFundTitle());
        refreshAuthStatus(model.getSocialSecurityStatus(), model.getGmtSocialSecurityExist(), displaySecurityStatus, displaySecurityStatusColor, statusIcSecurity, model.getSocialSecurityTitle());
        refreshAuthStatus(model.getCreditStatus(), model.getGmtCreditExist(), displayCreditCardStatus, displayCreditCardStatusColor, statusIcCreditCard, model.getCreditTitle());
        refreshAuthStatus(model.getAlipayStatus(), model.getGmtAlipayExist(), displayAlipayStatus, displayAlipayStatusColor, statusIcAlipay, model.getAlipayTitle());
        refreshAuthStatus(model.getZhengxinStatus(), model.getGmtZhengxinExist(), displayZhengxinStatus, displayZhengxinStatusColor, statusIcZhengxin, model.getZhengxinTitle());
        refreshAuthStatus(model.getChsiStatus(), model.getGmtChsiExist(), displayChsiStatus, displayChsiStatusColor, statusIcChsi, model.getChsiTitle());
        refreshAuthStatus(model.getBubbleStatus(), model.getGmtBubbleExist(), displayBubbleStatus, displayBubbleStatusColor, statusIcBubble, model.getBubbleTitle());
        if (showOnlineStatus) {
            refreshAuthStatus(model.getOnlinebankStatus(), model.getGmtOnlinebankExist(), displayOnlineBankStatus, displayOnlineBankStatusColor, statusIcOnlineBank, model.getChsiTitle());
        }
    }

    /**
     * 根据认证状态刷新视图
     * 购物、线下培训场景
     *
     * @param msg 白领贷，状态为F（禁止期）时，显示的文案
     */
    private void refreshAuthStatus(String authStatus, String gmtStatus, ObservableField<String> displayStatus,
                                   ObservableInt color, ObservableField<Drawable> statusIc, String msg) {
        statusIc.set(ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.ic_auth_right_arrow));
        color.set(ContextCompat.getColor(AlaConfig.getContext(), R.color.color_auth_status_not_auth));
        //用户已经发起过认证
        if (ModelEnum.Y.getModel().equals(gmtStatus)) {
            statusIc.set(ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.ic_auth_right_arrow));
            if (ModelEnum.A.getModel().equals(authStatus)) {
                setStatusType(5, displayStatus, color, statusIc, "");
            } else if (ModelEnum.N.getModel().equals(authStatus)) {
                setStatusType(5, displayStatus, color, statusIc, "");
            } else if (ModelEnum.F.getModel().equals(authStatus)) {

                //禁止期
                setStatusType(3, displayStatus, color, statusIc, msg);

            }
        } else {
            if (ModelEnum.N.getModel().equals(authStatus)) {
                setStatusType(5, displayStatus, color, statusIc, "");
            } else if (ModelEnum.A.getModel().equals(authStatus) || MiscUtils.isEmpty(authStatus)) {
                setStatusType(0, displayStatus, color, statusIc, "");
            }
        }

        if (ModelEnum.Y.getModel().equals(authStatus)) {
            setStatusType(2, displayStatus, color, statusIc, "");
        } else if (ModelEnum.W.getModel().equals(authStatus)) {
            setStatusType(1, displayStatus, color, statusIc, "");
        } else if (ModelEnum.Q.getModel().equals(authStatus)) {
            setStatusType(6, displayStatus, color, statusIc, "");
        } else if (ModelEnum.E.getModel().equals(authStatus)) {
            setStatusType(7, displayStatus, color, statusIc, "");
        }
    }

    private void setStatusType(int statusType, ObservableField<String> displayStatus,
                               ObservableInt color, ObservableField<Drawable> statusIc, String msg) {
        String statusInfo = "";
        switch (statusType) {
            case 0://未认证
                statusIc.set(ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.ic_auth_right_arrow));
                statusInfo = AlaConfig.getResources().getString(R.string.extra_auth_apply_status);
                color.set(ContextCompat.getColor(AlaConfig.getContext(), R.color.color_auth_status_not_auth));
                break;
            case 1://认证中
                statusInfo = AlaConfig.getResources().getString(R.string.extra_auth_ing_status);
                color.set(ContextCompat.getColor(AlaConfig.getContext(), R.color.color_auth_status_auth_ing));
                statusIc.set(ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.ic_auth_right_arrow));
                break;
            case 2://已认证
                statusInfo = AlaConfig.getResources().getString(R.string.extra_auth_success_status);
                color.set(ContextCompat.getColor(AlaConfig.getContext(), R.color.color_auth_status_auth));
                statusIc.set(ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.ic_credit_auth_already));
                break;
            case 3://x天后重新认证
                statusIc.set(ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.ic_auth_right_arrow));
                statusInfo = MiscUtils.isEmpty(msg) ? AlaConfig.getResources().getString(R.string.extra_auth_resubmit_status) : msg;
                color.set(ContextCompat.getColor(AlaConfig.getContext(), R.color.red));
                break;
            case 4://重新提交
                color.set(ContextCompat.getColor(AlaConfig.getContext(), R.color.red));
                statusInfo = AlaConfig.getResources().getString(R.string.extra_auth_resubmit_status);
                statusIc.set(ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.ic_auth_right_arrow));
                break;
            case 5://重新认证
                color.set(ContextCompat.getColor(AlaConfig.getContext(), R.color.red));
                statusInfo = AlaConfig.getResources().getString(R.string.extra_auth_fail_status);
                statusIc.set(ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.ic_auth_right_arrow));
                break;
            case 6://等待查询
                statusInfo = AlaConfig.getResources().getString(R.string.extra_auth_query_ing_status);
                color.set(ContextCompat.getColor(AlaConfig.getContext(), R.color.color_auth_status_auth_ing));
                break;
            case 7://已过期
                statusInfo = AlaConfig.getResources().getString(R.string.extra_auth_overdue);
                statusIc.set(ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.ic_auth_right_arrow));
                color.set(ContextCompat.getColor(AlaConfig.getContext(), R.color.red));
                break;
        }

        displayStatus.set(MiscUtils.isEmpty(statusInfo) ?
                AlaConfig.getResources().getString(R.string.extra_auth_apply_status) :
                statusInfo);

    }


    /**
     * 提升额度
     */
    private void raiseQuota(String authItem) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("authItem", authItem);
        jsonObject.put("scene", scene);
        Call<ApiResponse> call = RDClient.getService(AuthApi.class).authRaiseQuota(jsonObject);
        NetworkUtil.showCutscenes(context, call);
        call.enqueue(new RequestCallBack<ApiResponse>() {
            @Override
            public void onSuccess(Call<ApiResponse> call, Response<ApiResponse> response) {
                //刷新页面
                if (context instanceof CreditPromoteActivity)
                    ((CreditPromoteActivity) context).reload();
            }
        });
    }

    /**
     * 公积金认证
     */
    public void onAuthFundClick(View view) {
        if (mModel != null) {
            if (ModelEnum.F.getModel().equals(mModel.getFundStatus())) {
                if (MiscUtils.isEmpty(mModel.getFundTitle())) {
                    raiseQuota("fund");
                }
            } else {
                judgeAuthFund();
            }
        }
    }

    /**
     * 社保认证
     */
    public void onAuthSecurityClick(View view) {
        if (mModel != null) {
            if (ModelEnum.F.getModel().equals(mModel.getSocialSecurityStatus())) {
                if (MiscUtils.isEmpty(mModel.getSocialSecurityTitle())) {
                    raiseQuota("insurance");
                }
            } else {
                handleAuthTips(mModel.getSocialSecurityStatus(), MXAuthUtils.PARAM_TASK_SECURITY);
            }
        }
    }

    /**
     * 支付宝认证
     */
    public void onAuthAlipayClick(View view) {
        if (mModel != null) {
            if (ModelEnum.F.getModel().equals(mModel.getAlipayStatus())) {
                if (MiscUtils.isEmpty(mModel.getAlipayTitle())) {
                    raiseQuota("alipay");
                }
            } else {
                handleAuthTips(mModel.getAlipayStatus(), MXAuthUtils.PARAM_TASK_ALIPAY);
            }
        }
    }

    /*
     * 信用卡认证
     */
    public void onAuthCreditCardClick(View view) {
        if (mModel != null) {
            if (ModelEnum.F.getModel().equals(mModel.getCreditStatus())) {
                if (MiscUtils.isEmpty(mModel.getCreditTitle())) {
                    raiseQuota("cardEmail");
                }
            } else {
                handleAuthTips(mModel.getCreditStatus(), MXAuthUtils.PARAM_TASK_EMAIL);
            }
        }
    }

    /**
     * 人行征信认证
     */
    public void onAuthZhengxinClick(View view) {
        if (mModel != null) {
            if (ModelEnum.F.getModel().equals(mModel.getZhengxinStatus())) {
                if (MiscUtils.isEmpty(mModel.getZhengxinTitle())) {
                    raiseQuota("zhengxin");
                }
            } else {
                handleAuthTips(mModel.getZhengxinStatus(), MXAuthUtils.PARAM_TASK_ZHENGXIN);
            }
        }

    }

    /**
     * 学信网认证
     */
    public void onAuthChsiClick(View view) {
        if (mModel != null) {
            if (ModelEnum.F.getModel().equals(mModel.getChsiStatus())) {
                if (MiscUtils.isEmpty(mModel.getChsiTitle())) {
                    raiseQuota("chsi");
                }
            } else {
                handleAuthTips(mModel.getChsiStatus(), MXAuthUtils.PARAM_TASK_CHSI);
            }
        }

    }

    // 51公积金入口
    private void handleFundInfo() {
        Call<ExtraFundModel> call = RDClient.getService(AuthApi.class).authFundNew();
        call.enqueue(new RequestCallBack<ExtraFundModel>() {
            @Override
            public void onSuccess(Call<ExtraFundModel> call, Response<ExtraFundModel> response) {
                String url = response.body().getUrl();
                Intent intent = new Intent();
                intent.putExtra(HTML5WebView.INTENT_BASE_URL, url);
                ActivityUtils.push(HTML5WebView.class, intent);
            }
        });
    }

    /**
     * 网银认证
     */
    public void onAuthOnlineBankClick(View view) {
        //弹框判断
        if (mModel != null) {
            if (mModel.getOnlinebankDialogShow() != null && mModel.getOnlinebankDialogShow().equals("Y")) {
                final TwoButtonDialog.Builder builder = new TwoButtonDialog.Builder(context);
                builder
                        .setContent(AlaConfig.getResources().getString(R.string.online_bank_tip_message))
                        .setCancelTxt(AlaConfig.getResources().getString(R.string.online_bank_tip_add_bankcard))
                        .setSureTxt(AlaConfig.getResources().getString(R.string.online_bank_tip_auth))
                        .setCancelable(true)
                        .setSureListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                handleAuthTips(mModel.getOnlinebankStatus(), MXAuthUtils.PARAM_TASK_ONLINEBANK);
                                builder.getDialog().dismiss();
                            }
                        })
                        .setCancelListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //去绑卡
                                Intent intent = new Intent();
                                intent.putExtra(BundleKeys.BANK_CARD_NAME, mModel.getRealName());
                                intent.putExtra(BundleKeys.SETTING_PAY_ID_NUMBER, mModel.getIdNumber());
                                intent.putExtra(BundleKeys.STAGE_JUMP, StageJumpEnum.STAGE_ONLINE_BANK.getModel());
                                intent.putExtra(BundleKeys.INTENT_KEY_CREDIT_PROMOTE_SCENE, BundleKeys.CREDIT_PROMOTE_SCENE_CASH);
                                ActivityUtils.push(BankCardAddIdActivity.class, intent);
                                builder.getDialog().dismiss();
                            }
                        }).create().show();
            } else {
                if (ModelEnum.F.getModel().equals(mModel.getOnlinebankStatus())) {
                    if (MiscUtils.isEmpty(mModel.getOnlinebankTitle())) {
                        raiseQuota("bank");
                    }
                } else {
                    handleAuthTips(mModel.getOnlinebankStatus(), MXAuthUtils.PARAM_TASK_ONLINEBANK);
                }
            }
        }

    }

    /**
     * 社交认证
     *
     * @param view
     */
    public void onSocialContactAuthClick(View view) {
        if (mModel != null) {
            if (ModelEnum.F.getModel().equals(mModel.getBubbleStatus())) {
                if (MiscUtils.isEmpty(mModel.getBubbleTitle())) {
                    raiseQuota("bubble");
                }
            } else {
                handleAuthTips(mModel.getBubbleStatus(), "bubble");
            }
        }
    }


    /**
     * 处理认证提示
     */
    private void handleAuthTips(String authStatus, String authType) {
        String authInfo = null;
        if (MXAuthUtils.PARAM_TASK_FUND.equals(authType)) {
            //公积金
            authInfo = "公积金";
        } else if (MXAuthUtils.PARAM_TASK_EMAIL.equals(authType)) {
            //信用卡
            authInfo = "信用卡";
        } else if (MXAuthUtils.PARAM_TASK_SECURITY.equals(authType)) {
            //社保
            authInfo = "社保";
        } else if (MXAuthUtils.PARAM_TASK_ALIPAY.equals(authType)) {
            //社保
            authInfo = "支付宝";
        } else if (MXAuthUtils.PARAM_TASK_ZHENGXIN.equals(authType)) {
            authInfo = "人行征信";
        } else if (MXAuthUtils.PARAM_TASK_CHSI.equals(authType))
            authInfo = "学信网";
        else if (MXAuthUtils.PARAM_TASK_ONLINEBANK.equals(authType)) {
            authInfo = "网银";
        } else if ("bubble".equals(authType)) {
            authInfo = "社交";
        }
        if (ModelEnum.A.getModel().equals(authStatus) || ModelEnum.N.getModel().equals(authStatus)
                || MiscUtils.isEmpty(authStatus) || ModelEnum.Q.getModel().equals(authStatus)) {
            submitAuthRequest(authType);
        } else if (ModelEnum.W.getModel().equals(authStatus)) {
            String tipsInfo = String.format(AlaConfig.getResources().getString(R.string.extra_auth_ing_text), authInfo);
            UIUtils.showToast(tipsInfo);
        } else if (ModelEnum.Y.getModel().equals(authStatus)) {
            String tipsInfo = String.format(AlaConfig.getResources().getString(R.string.extra_auth_success_text), authInfo);
            UIUtils.showToast(tipsInfo);
        }
    }

    /**
     * 提交认证
     *
     * @param authType 认证类型
     */

    private void submitAuthRequest(String authType) {
        if (MXAuthUtils.PARAM_TASK_FUND.equals(authType)) {
            authFund();
        } else if (MXAuthUtils.PARAM_TASK_EMAIL.equals(authType)) {
            authCreditCard();
        } else if (MXAuthUtils.PARAM_TASK_SECURITY.equals(authType)) {
            authSocialSecurity();
        } else if (MXAuthUtils.PARAM_TASK_ALIPAY.equals(authType)) {
            authAlipaySecurity();
        } else if (MXAuthUtils.PARAM_TASK_ZHENGXIN.equals(authType)) {
            authZhengxin();
        } else if (MXAuthUtils.PARAM_TASK_CHSI.equals(authType)) {
            authChsi();
        } else if (MXAuthUtils.PARAM_TASK_ONLINEBANK.equals(authType)) {
            authOnlineBank();
        } else if ("bubble".equals(authType)) {
            authSocialContact();
        }
    }

    /**
     * 社保认证
     */
    private void authSocialSecurity() {
        Call<ExtraUserIdModel> call = RDClient.getService(AuthApi.class).authSocialSecurity();
        call.enqueue(new RequestCallBack<ExtraUserIdModel>() {
            @Override
            public void onSuccess(Call<ExtraUserIdModel> call, Response<ExtraUserIdModel> response) {
                String authInfo = response.body().getTransPara();
                authUtils.configMxParams(authInfo);
                authUtils.handleSecurity();
            }
        });
    }

    /**
     * 支付宝认证
     */
    private void authAlipaySecurity() {
        Call<ExtraGxbModel> call = RDClient.getService(AuthApi.class).authGxb();
        call.enqueue(new RequestCallBack<ExtraGxbModel>() {
            @Override
            public void onSuccess(Call<ExtraGxbModel> call, Response<ExtraGxbModel> response) {
                String url = response.body().getUrl();
                Intent intent = new Intent();
                intent.putExtra(HTML5WebView.INTENT_TITLE, "支付宝认证");
                intent.putExtra(HTML5WebView.INTENT_BASE_URL, url);
                ActivityUtils.push(HTML5WebView.class, intent);
            }
        });
//        Call<ExtraUserIdModel> call = RDClient.getService(AuthApi.class).authAlipay();
//        call.enqueue(new RequestCallBack<ExtraUserIdModel>() {
//            @Override
//            public void onSuccess(Call<ExtraUserIdModel> call, Response<ExtraUserIdModel> response) {
//                String authInfo = response.body().getTransPara();
//                authUtils.configMxParams(authInfo);
//                authUtils.handleAlipay();
//            }
//        });
    }

    /**
     * 发起公积金认证
     */
    private void authFund() {
        Call<ExtraUserIdModel> call = RDClient.getService(AuthApi.class).authFund();
        call.enqueue(new RequestCallBack<ExtraUserIdModel>() {
            @Override
            public void onSuccess(Call<ExtraUserIdModel> call, Response<ExtraUserIdModel> response) {
                String authInfo = response.body().getTransPara();
                authUtils.configMxParams(authInfo);
                authUtils.handleFund();
            }
        });
    }

    /**
     * 发起信用卡认证
     */
    private void authCreditCard() {
        Call<ExtraUserIdModel> call = RDClient.getService(AuthApi.class).authCreditCard();
        call.enqueue(new RequestCallBack<ExtraUserIdModel>() {
            @Override
            public void onSuccess(Call<ExtraUserIdModel> call, Response<ExtraUserIdModel> response) {
                String authInfo = response.body().getTransPara();
                authUtils.configMxParams(authInfo);
                authUtils.handleOnlineBack();
            }
        });
    }

    /**
     * 发起征信认证
     */
    private void authZhengxin() {
        Call<ExtraUserIdModel> call = RDClient.getService(AuthApi.class).authZhengxin();
        call.enqueue(new RequestCallBack<ExtraUserIdModel>() {
            @Override
            public void onSuccess(Call<ExtraUserIdModel> call, Response<ExtraUserIdModel> response) {
                String authInfo = response.body().getTransPara();
                authUtils.configMxParams(authInfo);
                authUtils.handleZhengxin();
            }
        });

    }

    /**
     * 请求认证
     * statusInfo
     *
     * @param authType 认证类型
     */
    private void handleAuthInfo(final String authType) {
        JSONObject object = new JSONObject();
        object.put("authType", authType);
        Call<ApiResponse> call = RDClient.getService(AuthApi.class).authSupplyVerifying(object);
        call.enqueue(new RequestCallBack<ApiResponse>() {
            @Override
            public void onSuccess(Call<ApiResponse> call, Response<ApiResponse> response) {
                //默认写死认证中，再次刷新页面获取最新数据
                if ("FUND".equals(authType)) {
                    mModel.setFundStatus(ModelEnum.W.getModel());
                    setStatusType(1, displayFundStatus, displayFundStatusColor, statusIcFund, "");
                } else if ("CREDIT".equals(authType)) {
                    mModel.setCreditStatus(ModelEnum.W.getModel());
                    setStatusType(1, displayCreditCardStatus, displayCreditCardStatusColor, statusIcCreditCard, "");
                } else if ("SOCIAL_SECURITY".equals(authType)) {
                    mModel.setSocialSecurityStatus(ModelEnum.W.getModel());
                    setStatusType(1, displaySecurityStatus, displaySecurityStatusColor, statusIcSecurity, "");
                } else if ("ALIPAY".equals(authType)) {
                    mModel.setAlipayStatus(ModelEnum.W.getModel());
                    setStatusType(1, displayAlipayStatus, displayAlipayStatusColor, statusIcAlipay, "");
                } else if (TYPE_ZHENGXIN.equals(authType)) {
                    mModel.setZhengxinStatus(ModelEnum.W.getModel());
                    setStatusType(1, displayZhengxinStatus, displayZhengxinStatusColor, statusIcZhengxin, "");
                } else if (TYPE_CHSI.equals(authType)) {
                    mModel.setChsiStatus(ModelEnum.W.getModel());
                    setStatusType(1, displayChsiStatus, displayChsiStatusColor, statusIcChsi, "");
                } else if (TYPE_ONLINE_BANK.equals(authType)) {
                    mModel.setOnlinebankStatus(ModelEnum.W.getModel());
                    setStatusType(1, displayOnlineBankStatus, displayOnlineBankStatusColor, statusIcOnlineBank, "");
                } else if ("BUBBLE".equals(authType)) {
                    mModel.setBubbleStatus(ModelEnum.W.getModel());
                    setStatusType(1, displayBubbleStatus, displayBubbleStatusColor, statusIcBubble, "");
                }
            }
        });
    }


    /**
     * 发起学信网认证
     */
    private void authChsi() {
        Call<ExtraUserIdModel> call = RDClient.getService(AuthApi.class).authChsi();
        call.enqueue(new RequestCallBack<ExtraUserIdModel>() {
            @Override
            public void onSuccess(Call<ExtraUserIdModel> call, Response<ExtraUserIdModel> response) {
                String authInfo = response.body().getTransPara();
                authUtils.configMxParams(authInfo);
                authUtils.handleChsi();
            }
        });

    }


    /**
     * 发起网银认证
     */
    private void authOnlineBank() {
        Call<ExtraUserIdModel> call = RDClient.getService(AuthApi.class).authOnlineBank();
        call.enqueue(new RequestCallBack<ExtraUserIdModel>() {
            @Override
            public void onSuccess(Call<ExtraUserIdModel> call, Response<ExtraUserIdModel> response) {
                String authInfo = response.body().getTransPara();
                authUtils.configMxParams(authInfo);
                authUtils.handleOnlineBank();
            }
        });

    }

    /**
     * 发起冒泡认证
     */
    private void authSocialContact() {
        Intent intent = new Intent();
        if (AppUtils.isApplicationAvilible(context, "cn.citytag.mapgo")) {
            Uri uri = Uri.parse("maopp://mapgo.app?source=app&act=4");
            intent.setData(uri);
            context.startActivityForResult(intent, BundleKeys.REQUEST_CODE_SOCIAL_CONTACT);
        } else {
            intent.putExtra(HTML5WebView.INTENT_BASE_URL, Constant.H5_DOWNLOAN_URL_MAOPAO);
            ActivityUtils.push(HTML5WebView.class, intent);
        }
    }

    /**
     * 根据强风控状态控制人行征信和信用卡邮箱入口的显示
     */
    private void setRiskStatus() {
        //只有通过基础审核才显示
        if (mModel.getBasicStatus().equals("Y")) {
            riskManagerStatus.set(View.VISIBLE);
            showZhengxin.set((scene != null && scene.equals(BundleKeys.CREDIT_PROMOTE_SCENE_ONLINE)) ? View.GONE : View.VISIBLE);
        } else {
            riskManagerStatus.set(View.GONE);
            showZhengxin.set(View.GONE);
        }
    }

    @Override
    public void onHandelOpenNative(String name, String data, String otherData) {
        switch (name) {
            case GXB_BACK:
                ActivityUtils.popUntilWithoutRefresh(CreditPromoteActivity.class);
                handleAuthInfo("ALIPAY");
                break;
            case WUYAO_FUND_STATUS:
                ActivityUtils.popUntilWithoutRefresh(CreditPromoteActivity.class);
                wuYaoAuth(data);
                break;
        }
    }

    // 51公积金回调处理
    private void wuYaoAuth(String data) {
        JSONObject object = new JSONObject(1);
        object.put("orderSn", data);
        Call<ApiResponse> call = RDClient.getService(AuthApi.class).giveBack(object);
        call.enqueue(new RequestCallBack<ApiResponse>() {
            @Override
            public void onSuccess(Call<ApiResponse> call, Response<ApiResponse> response) {
                ApiResponse apiResponse = response.body();
                if (apiResponse.getCode() == 1000) {
                    //认证中
                    mModel.setFundStatus(ModelEnum.W.getModel());
                    setStatusType(1, displayFundStatus, displayFundStatusColor, statusIcFund, "");
                } else {
                    //重新认证
                    setStatusType(5, displayFundStatus, displayFundStatusColor, statusIcFund, "");
                    Toast.makeText(context, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void judgeAuthFund() {
        Call<FundModel> fundCall = RDClient.getService(AuthApi.class).authFundSwitch();
        fundCall.enqueue(new RequestCallBack<FundModel>() {
            @Override
            public void onSuccess(Call<FundModel> call, Response<FundModel> response) {
                FundModel model = response.body();
                if ("1".equals(model.getFundSwitch())) handleFundInfo(); // 1: 公积金认证切换为51公积金管家
                else
                    handleAuthTips(mModel.getFundStatus(), MXAuthUtils.PARAM_TASK_FUND); // 0 : 接的是魔蝎的公积金
            }
        });
    }

    public void onDestroy() {
        ProtocolUtils.setOpenNativeHandler(null);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BundleKeys.REQUEST_CODE_SOCIAL_CONTACT) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                String phone = data.getStringExtra("phone");
                if (MiscUtils.isNotEmpty(phone)) {
                    raiseQuota("bubble");
                } else {
                    UIUtils.showToast("授权失败，请重试");
                }
            }
        }
    }
}
