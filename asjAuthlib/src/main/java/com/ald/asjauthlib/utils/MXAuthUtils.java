package com.ald.asjauthlib.utils;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.view.View;

import com.ald.asjauthlib.dialog.CreditPromoteDialog;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.utils.UIUtils;
import com.moxie.client.manager.MoxieCallBack;
import com.moxie.client.manager.MoxieCallBackData;
import com.moxie.client.manager.MoxieContext;
import com.moxie.client.manager.MoxieSDK;
import com.moxie.client.model.MxLoginCustom;
import com.moxie.client.model.MxParam;
import com.moxie.client.model.TitleParams;

/**
 * 魔蝎认证工具类
 * Created by sean yu on 2017/7/10.
 */

public class MXAuthUtils {
    public static final String PARAM_TASK_FUND = MxParam.PARAM_TASK_FUND;
    public static final String PARAM_TASK_ONLINEBANK = MxParam.PARAM_TASK_ONLINEBANK;
    public static final String PARAM_TASK_SECURITY = MxParam.PARAM_TASK_SECURITY;
    public static final String PARAM_TASK_EMAIL = MxParam.PARAM_TASK_EMAIL;
    public static final String PARAM_TASK_ALIPAY = MxParam.PARAM_TASK_ALIPAY;
    public static final String PARAM_TASK_ZHENGXIN = MxParam.PARAM_TASK_ZHENGXIN;
    public static final String PARAM_TASK_CHSI = MxParam.PARAM_TASK_CHSI;

    private static final String DEBUG_MX_API_KEY = "a84f8aa629f0464d86b3729db9bb7adf";
    private static final String RELEASE_MX_API_KEY = "3e5e6cc9af494ccfb0b84f7e1f37fb2e";

    private MxParam mxParam;
    private Activity activity;
    private MxAuthCallBack callBack;

    //判断用户是否在认证中
    private boolean isAuth = false;

    public MXAuthUtils(Activity activity) {
        this.activity = activity;
        mxParam = new MxParam();
    }


    /*
      初始化摩蝎SDK
    * */
    public static void initSDK(Application application) {
        MoxieSDK.init(application);
    }

    /**
     * 谁知认证回调监听
     *
     * @param callBack callBack
     * @return MXAuthUtils
     */
    public MXAuthUtils setCallBack(MxAuthCallBack callBack) {
        this.callBack = callBack;
        return this;
    }

    /**
     * 配置魔蝎api_key
     */
    private String getMXApiKey() {
        if (AlaConfig.isDebug()) {
            return DEBUG_MX_API_KEY;
        }
        return RELEASE_MX_API_KEY;
    }


    /**
     * 配置魔蝎参数
     */
    public void configMxParams(String userId) {
        mxParam.setUserId(userId);
        mxParam.setApiKey(getMXApiKey());
        mxParam.setCacheDisable(MxParam.PARAM_COMMON_YES);//不使用缓存（非必传）
        mxParam.setQuitDisable(true); //设置导入过程中，触发返回键或者点击actionbar的返回按钮的时候，不执行魔蝎的默认行为
        configTitleParams();
    }


    /**
     * 配置魔蝎界面样式
     */
    private void configTitleParams() {
        TitleParams titleParams = new TitleParams.Builder()
                //不设置此方法会默认使用魔蝎的icon
//                .leftNormalImgResId(R.drawable.back)
//                //用于设置selector，表示按下的效果，不设置默认使用leftNormalImgResId()设置的图片
//                .leftPressedImgResId(R.drawable.moxie_client_banner_back_black)
//                .titleColor(c.getResources().getColor(R.color.colorWhite))
//                .backgroundColor(getContext().getResources().getColor(R.color.colorAccent))
//                .backgroundDrawable(R.drawable.bg_actionbar)
//                .rightNormalImgResId(R.drawable.refresh)
//                .immersedEnable(true)
                .build();
        mxParam.setTitleParams(titleParams);

    }


    /**
     * 网银信用卡认证
     */
    public void handleOnlineBack() {
        if (mxParam == null) {
            return;
        }
        //1.打开信用卡列表页面
        // MxParam.PARAM_ITEM_TYPE_CREDITCARD（信用卡列表）
        // MxParam.PARAM_ITEM_TYPE_DEBITCARD（储蓄卡列表）
        mxParam.setTaskType(MxParam.PARAM_TASK_EMAIL);
        handleAuth();
        showCardAuthToast();
    }


    /**
     * 网银认证(借记卡)
     */
    public void handleOnlineBank() {
        if (mxParam == null) {
            return;
        }
        mxParam.setTaskType(MxParam.PARAM_TASK_ONLINEBANK);
        MxLoginCustom loginCustom = new MxLoginCustom();
        loginCustom.setLoginType(MxLoginCustom.LOGIN_TYPE_V_DEBITCARD);
        mxParam.setLoginCustom(loginCustom);
        handleAuth();
    }


    /**
     * 公积金认证
     */
    public void handleFund() {
        if (mxParam == null) {
            return;
        }
        mxParam.setTaskType(MxParam.PARAM_TASK_FUND);
        handleAuth();
    }

    /**
     * 社保 认证
     */
    public void handleSecurity() {
        if (mxParam == null) {
            return;
        }
        mxParam.setTaskType(MxParam.PARAM_TASK_SECURITY);
        handleAuth();
    }

    /**
     * 支付宝 认证
     */
    public void handleAlipay() {
        if (mxParam == null) {
            return;
        }
        mxParam.setTaskType(MxParam.PARAM_TASK_ALIPAY);
        handleAuth();
    }

    /**
     * 学信网认证
     */
    public void handleChsi() {
        if (mxParam == null) {
            return;
        }
        mxParam.setTaskType(MxParam.PARAM_TASK_CHSI);
        handleAuth();
    }

    /**
     * 人行征信认证
     */
    public void handleZhengxin() {
        if (mxParam == null) {
            return;
        }
        mxParam.setTaskType(MxParam.PARAM_TASK_ZHENGXIN);
        handleAuth();
    }


    /**
     * 处理认证信息
     */
    private void handleAuth() {
        if (mxParam == null || activity == null || isAuth) {
            return;
        }
        //防止魔蝎重复认证
        isAuth = true;
        MoxieSDK.getInstance().start(activity, mxParam, new MoxieCallBack() {
            /**
             *  物理返回键和左上角返回按钮的back事件以及任务的状态都通过这个函数来回调
             *
             * @param moxieContext       可以用这个来实现在魔蝎的页面弹框或者关闭魔蝎的界面
             * @param moxieCallBackData  我们可以根据 MoxieCallBackData 的code来判断目前处于哪个状态，以此来实现自定义的行为
             * @return 返回true表示这个事件由自己全权处理，返回false会接着执行魔蝎的默认行为
             */
            @Override
            public boolean callback(MoxieContext moxieContext, MoxieCallBackData moxieCallBackData) {
                if (moxieCallBackData != null) {
                    switch (moxieCallBackData.getCode()) {
                        /*
                         * 如果用户正在导入魔蝎SDK会出现这个情况，如需获取最终状态请轮询贵方后台接口
                         * 魔蝎后台会向贵方后台推送Task通知和Bill通知
                         * Task通知：登录成功/登录失败
                         * Bill通知：账单通知
                         */
                        case MxParam.ResultCode.IMPORTING:
                        case MxParam.ResultCode.IMPORT_UNSTART:
                            showTipDialog(moxieContext);
                            return true;
                        case MxParam.ResultCode.THIRD_PARTY_SERVER_ERROR:
                        case MxParam.ResultCode.MOXIE_SERVER_ERROR:
                        case MxParam.ResultCode.USER_INPUT_ERROR:
                        case MxParam.ResultCode.IMPORT_FAIL:
                            //防止魔蝎重复认证
                            isAuth = false;
                            //根据taskType进行对应的处理
                            switch (moxieCallBackData.getTaskType()) {
                                case MxParam.PARAM_TASK_FUND:
                                    setErrorInfo(PARAM_TASK_FUND, moxieCallBackData.getMessage());
                                    break;

                                case MxParam.PARAM_TASK_ONLINEBANK:
                                    setErrorInfo(PARAM_TASK_ONLINEBANK, moxieCallBackData.getMessage());
                                    break;

                                case MxParam.PARAM_TASK_EMAIL:
                                    setErrorInfo(PARAM_TASK_EMAIL, moxieCallBackData.getMessage());
                                    break;

                                case MxParam.PARAM_TASK_SECURITY:
                                    setErrorInfo(PARAM_TASK_SECURITY, moxieCallBackData.getMessage());
                                    break;

                                case MxParam.PARAM_TASK_ALIPAY:
                                    setErrorInfo(PARAM_TASK_ALIPAY, moxieCallBackData.getMessage());
                                    break;

                                case MxParam.PARAM_TASK_CHSI:
                                    setErrorInfo(PARAM_TASK_CHSI, moxieCallBackData.getMessage());
                                    break;
                                case MxParam.PARAM_TASK_ZHENGXIN:
                                    setErrorInfo(PARAM_TASK_ZHENGXIN, moxieCallBackData.getMessage());
                                    break;
                            }
                            moxieContext.finish();
                            return true;

                        case MxParam.ResultCode.IMPORT_SUCCESS:
                            //防止魔蝎重复认证
                            isAuth = false;
                            //根据taskType进行对应的处理
                            switch (moxieCallBackData.getTaskType()) {
                                case MxParam.PARAM_TASK_FUND:
                                    setCallBakInfo(PARAM_TASK_FUND, "公积金导入成功");
                                    break;

                                case MxParam.PARAM_TASK_ONLINEBANK:
                                    setCallBakInfo(PARAM_TASK_ONLINEBANK, "网银导入成功");
                                    break;

                                case MxParam.PARAM_TASK_EMAIL:
                                    setCallBakInfo(PARAM_TASK_EMAIL, "邮箱导入成功");
                                    break;

                                case MxParam.PARAM_TASK_SECURITY:
                                    setCallBakInfo(PARAM_TASK_SECURITY, "社保导入成功");
                                    break;

                                case MxParam.PARAM_TASK_ALIPAY:
                                    setCallBakInfo(PARAM_TASK_ALIPAY, "支付宝导入成功");
                                    break;
                                case MxParam.PARAM_TASK_CHSI:
                                    setCallBakInfo(PARAM_TASK_CHSI, "学信网导入成功");
                                    break;
                                case MxParam.PARAM_TASK_ZHENGXIN:
                                    setCallBakInfo(PARAM_TASK_ZHENGXIN, "人行征信导入成功");
                                    break;

                            }
                            moxieContext.finish();
                            return true;
                    }
                }
                return false;
            }
        });
    }

    /**
     *
     */
    private void showCardAuthToast() {
        AlaConfig.postDelayOnUiThread(new Runnable() {
            @Override
            public void run() {
                UIUtils.showToast("请您选择信用卡绑定的邮箱");
            }
        }, 1000);
    }


    /**
     * 显示认证提示框
     */
    private void showTipDialog(final MoxieContext context) {
        CreditPromoteDialog dialog = new CreditPromoteDialog(context.getContext());
        dialog.setContent("退出后需重新进行认证，确定返回？");
        dialog.setCancelText("取消");
        dialog.setSureText("确定");
        dialog.setSureBtnVisible(View.VISIBLE);
        dialog.setListener(new CreditPromoteDialog.MakeSureListener() {
            @Override
            public void onSureClick(Dialog dialog, View view) {
                dialog.dismiss();
                context.finish();
                //防止魔蝎重复认证
                isAuth = false;
            }
        });
        dialog.show();
    }

    /**
     * 设置回调信息
     *
     * @param authCode 回调码
     * @param authMsg  回调信息
     */
    private void setCallBakInfo(String authCode, String authMsg) {
        if (MXAuthUtils.this.callBack != null) {
            callBack.authSuccess(authCode, authMsg);
        }
    }

    /**
     * 设置回调信息
     *
     * @param authCode 回调码
     * @param authMsg  回调信息
     */
    private void setErrorInfo(String authCode, String authMsg) {
        if (MXAuthUtils.this.callBack != null) {
            callBack.authError(authCode, authMsg);
        }
    }

}
