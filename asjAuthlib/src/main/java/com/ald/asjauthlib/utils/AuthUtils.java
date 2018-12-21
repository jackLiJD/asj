package com.ald.asjauthlib.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.AuthApi;
import com.ald.asjauthlib.dialog.ActivityDialog;
import com.ald.asjauthlib.dialog.SteadBuyTipsDialog;
import com.ald.asjauthlib.auth.model.AuthModel;
import com.ald.asjauthlib.auth.model.CreditPromoteModel;
import com.ald.asjauthlib.auth.model.PhoneOperatorModel;
import com.ald.asjauthlib.auth.ui.BankCardAddIdActivity;
import com.ald.asjauthlib.auth.ui.CreditPromoteActivity;
import com.ald.asjauthlib.auth.ui.RRIdAuthActivity;
import com.ald.asjauthlib.web.HTML5WebView;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.network.NetworkUtil;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.network.RequestCallBack;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.BASE64Encoder;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.authframework.core.utils.UIUtils;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 认证工具类
 * Created by sean yu on 2017/6/12.
 */
public class AuthUtils {
    private Context context;
    private boolean isShowDialog = true;

    public AuthUtils setContext(Context context) {
        this.context = context;
        return this;
    }


    public AuthUtils setIsShow(boolean isShowDialog) {
        this.isShowDialog = isShowDialog;
        return this;
    }


    /**
     * 跳转到实名认证
     */
    public <T extends AuthModel> boolean isAuthAndJump(T model, boolean isLoan) {
        if (model == null) {
            return false;
        }
        if (ModelEnum.N.getModel().equals(model.getFaceStatus())) {
            realNameStatusError(isLoan);
            return false;
        }
        if (ModelEnum.N.getModel().equals(model.getIsBind())) {
            bindCardError(model, isLoan);
            return false;
        }
        //不是借钱
        if (!isLoan) {
            //风控审核不过而且没有完成补充认证
            if (RiskEnumStatus.N.getStatus().equals(model.getRiskStatus()) && ModelEnum.N.getModel().equals
                    (model.getIsSupplyCertify())) {
                SteadBuyTipsDialog dialog = new SteadBuyTipsDialog(context);
                dialog.setSureText("马上去认证");
                dialog.setContent("可用额度不足,\n完成补充认证，最多可享20000额度");
                dialog.setListener(new SteadBuyTipsDialog.ISureListener() {
                    @Override
                    public void onSureClick(Dialog dialog) {
                        Intent intent = new Intent();
                        intent.putExtra(BundleKeys.JUMP_AUTH_CODE, 1);
                        intent.putExtra(BundleKeys.STAGE_JUMP, StageJumpEnum.STAGE_ORAL_ACTIVITY.getModel());
                        intent.putExtra(BundleKeys.INTENT_KEY_CREDIT_PROMOTE_SCENE, BundleKeys.CREDIT_PROMOTE_SCENE_ONLINE);
                        ActivityUtils.push(CreditPromoteActivity.class, intent);
                    }
                });
                dialog.show();
                return false;
            }
        }
        if (!(RiskEnumStatus.Y.getStatus().equals(model.getRiskStatus()))) {
            riskError(isLoan);
            return false;
        }
        //运营商过期
        if(ModelEnum.Y.getModel().equals(model.getBasicStatus()) && ModelEnum.Y.getModel().equals(model.getRiskStatus()) && ModelEnum.N.getModel().equals(model.getMobileStatus())){
            ActivityDialog activityDialog = new ActivityDialog(context);
            activityDialog.setImageResource(model.getErrorUrl());
            activityDialog.setListener(new ActivityDialog.onImageClickListener() {
                @Override
                public void onImageClick(Dialog dialog) {
                    handleMobileOperator();
                    dialog.dismiss();
                }

                @Override
                public void onClose(Dialog dialog) {
                    dialog.dismiss();
                }
            });
            activityDialog.show();
            return false;
        }
        return true;
    }

    /**
     * 处理手机运营商逻辑
     */
    private void handleMobileOperator() {
        Call<PhoneOperatorModel> call = RDClient.getService(AuthApi.class).authMobile();
        NetworkUtil.showCutscenes(AlaConfig.getContext(), call);
        call.enqueue(new RequestCallBack<PhoneOperatorModel>() {
            @Override
            public void onSuccess(Call<PhoneOperatorModel> call, Response<PhoneOperatorModel> response) {
                if (response.body() != null) {
                    String url = response.body().getUrl();
                    if (MiscUtils.isNotEmpty(url)) {
                        Intent i = new Intent();
                        i.putExtra(HTML5WebView.INTENT_BASE_URL, url);
                        ActivityUtils.push(HTML5WebView.class, i);
                    }
                } else {
                    UIUtils.showToast(AlaConfig.getContext().getResources().getString(R.string.credit_promote_phone_err));
                }
            }
        });
    }

    /**
     * 未实名认证
     *
     * @param isLoan 是否是借钱 用于判断额度场景
     */
    private void realNameStatusError(final boolean isLoan) {
        if (isShowDialog) {
            showTipsDialog(new SteadBuyTipsDialog.ISureListener() {
                @Override
                public void onSureClick(Dialog dialog) {
                    jumpBindRealName(isLoan);
                }
            });
        } else {
            jumpBindRealName(isLoan);
        }

    }

    /**
     * 未通过银行卡绑定
     */
    private <T extends AuthModel> void bindCardError(final T model, final boolean isLoan) {
        if (isShowDialog) {
            showTipsDialog(new SteadBuyTipsDialog.ISureListener() {
                @Override
                public void onSureClick(Dialog dialog) {
                    jumpBindCard(model, isLoan);
                }
            });
        } else {
            jumpBindCard(model, isLoan);
        }

    }


    /**
     * 未通过强风控
     */
    private <T extends AuthModel> void riskError(final boolean isLoan) {
        if (isShowDialog) {
            showTipsDialog(new SteadBuyTipsDialog.ISureListener() {
                @Override
                public void onSureClick(Dialog dialog) {
                    jumpRisk(isLoan);
                }
            });
        } else {
            jumpRisk(isLoan);
        }

    }


    /**
     * 提示彈出信息
     */
    private void showTipsDialog(SteadBuyTipsDialog.ISureListener listener) {
        if (context != null) {
            SteadBuyTipsDialog dialog = new SteadBuyTipsDialog(context);
            dialog.setSureText("去认证");
            dialog.setContent("请先完成信用认证");
            dialog.setListener(listener);
            dialog.show();
        }
    }

    /**
     * 跳转到实名认证
     *
     * @param isLoan 用于额度场景判断
     */
    private <T extends AuthModel> void jumpBindRealName(boolean isLoan) {
        Intent intent = new Intent();
        intent.putExtra(BundleKeys.STAGE_JUMP, StageJumpEnum.STAGE_ORAL_ACTIVITY.getModel());
        intent.putExtra(BundleKeys.INTENT_KEY_CREDIT_PROMOTE_SCENE, isLoan ? BundleKeys.CREDIT_PROMOTE_SCENE_CASH : BundleKeys.CREDIT_PROMOTE_SCENE_ONLINE);
        ActivityUtils.push(RRIdAuthActivity.class, intent, BundleKeys.REQUEST_CODE_LOAN);

    }


    /**
     * 跳转到强风控
     */
    private void jumpRisk(boolean isLoan) {
        Intent intent = new Intent();
        intent.putExtra(BundleKeys.STAGE_JUMP, StageJumpEnum.STAGE_ORAL_ACTIVITY.getModel());
        intent.putExtra(BundleKeys.INTENT_KEY_CREDIT_PROMOTE_SCENE, isLoan ? BundleKeys.CREDIT_PROMOTE_SCENE_CASH : BundleKeys.CREDIT_PROMOTE_SCENE_ONLINE);
        ActivityUtils.push(CreditPromoteActivity.class, intent, BundleKeys.REQUEST_CODE_LOAN);

    }

    /**
     * 跳转到绑定银行卡
     */
    private <T extends AuthModel> void jumpBindCard(T model, boolean isLoan) {
        Intent intent = new Intent();
        intent.putExtra(BundleKeys.STAGE_JUMP, StageJumpEnum.STAGE_ORAL_ACTIVITY.getModel());
        intent.putExtra(BundleKeys.BANK_CARD_NAME, model.getRealName());
        if (MiscUtils.isNotEmpty(model.getIdNumber())) {
            intent.putExtra(BundleKeys.SETTING_PAY_ID_NUMBER, BASE64Encoder.decodeString(model.getIdNumber()));
        }
        intent.putExtra(BundleKeys.INTENT_KEY_CREDIT_PROMOTE_SCENE, isLoan ? BundleKeys.CREDIT_PROMOTE_SCENE_CASH : BundleKeys.CREDIT_PROMOTE_SCENE_ONLINE);
        ActivityUtils.push(BankCardAddIdActivity.class, intent, BundleKeys.REQUEST_CODE_LOAN);

    }

    public static boolean isAllowSubmit(Context context, CreditPromoteModel creditPromoteModel, boolean isShowToast) {
        if (creditPromoteModel == null) {
            return false;
        }
        String toastMessage = null;
        //是否实名认证
        if (MiscUtils.isEmpty(creditPromoteModel.getFaceStatus()) || !ModelEnum.Y.getModel()
                .equals(creditPromoteModel.getFaceStatus())) {
            toastMessage = AlaConfig.getResources().getString(R.string.credit_promote_submit_user_info_error);
        }
        //绑定银行卡状态
        if (MiscUtils.isEmpty(creditPromoteModel.getBankcardStatus()) || !ModelEnum.Y.getModel()
                .equals(creditPromoteModel.getBankcardStatus())) {
            toastMessage = AlaConfig.getResources().getString(R.string.credit_promote_submit_bind_card_error);
        }
        //芝麻信用认证
        if (creditPromoteModel.getZmModel() != null && MiscUtils.isNotEmpty(creditPromoteModel.getZmModel().getIsShow())
                && creditPromoteModel.getZmModel().getIsShow().equals("Y")
                && !ModelEnum.Y.getModel().equals(creditPromoteModel.getZmModel().getZmStatus())) {
            toastMessage = AlaConfig.getResources().getString(R.string.credit_promote_submit_zm_error);
        }
        //运营商认证
        if (ModelEnum.W.getModel().equals(creditPromoteModel.getMobileStatus())) {
            toastMessage = AlaConfig.getResources().getString(R.string.credit_promote_submit_operator_wait);
        } else if (!ModelEnum.Y.getModel().equals(creditPromoteModel.getMobileStatus())) {
            toastMessage = AlaConfig.getResources().getString(R.string.credit_promote_submit_operator_error);
        }

//        //联系人设置
//        if (MiscUtils.isEmpty(creditPromoteModel.getTeldirStatus()) || !ModelEnum.Y.getModel()
//                .equals(creditPromoteModel.getTeldirStatus())) {
//            toastMessage = AlaConfig.getResources().getString(R.string.credit_promote_submit_contact_error);
//        }
        if (toastMessage != null) {
            if (isShowToast) {
                UIUtils.showToast(toastMessage);
            }
            return false;
        }
        return true;
    }
}
