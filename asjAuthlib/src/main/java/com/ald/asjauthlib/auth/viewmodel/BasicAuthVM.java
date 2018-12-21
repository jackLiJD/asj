package com.ald.asjauthlib.auth.viewmodel;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.AuthApi;
import com.ald.asjauthlib.auth.BusinessApi;
import com.ald.asjauthlib.dialog.CreditPromoteDialog;
import com.ald.asjauthlib.dialog.ZmLowVSystemHintDialog;
import com.ald.asjauthlib.auth.model.ContactsSycModel;
import com.ald.asjauthlib.auth.model.CreditPromoteModel;
import com.ald.asjauthlib.auth.model.PhoneOperatorModel;
import com.ald.asjauthlib.auth.model.ZMXYModel;
import com.ald.asjauthlib.auth.ui.AuthUserInfoActivity;
import com.ald.asjauthlib.auth.ui.BankCardAddIdActivity;
import com.ald.asjauthlib.auth.ui.CreditPromoteActivity;
import com.ald.asjauthlib.auth.ui.RRIdConfirmActivity;
import com.ald.asjauthlib.databinding.FragmentBasicAuthBinding;
import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.utils.ContactsUtils;
import com.ald.asjauthlib.utils.ModelEnum;
import com.ald.asjauthlib.utils.StageJumpEnum;
import com.ald.asjauthlib.web.HTML5WebView;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.config.AlaActivity;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.network.BaseObserver;
import com.ald.asjauthlib.authframework.core.network.NetworkUtil;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.network.RequestCallBack;
import com.ald.asjauthlib.authframework.core.network.RxUtils;
import com.ald.asjauthlib.authframework.core.network.exception.ApiException;
import com.ald.asjauthlib.authframework.core.protocol.AlaProtocol;
import com.ald.asjauthlib.authframework.core.protocol.ProtocolUtils;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.authframework.core.utils.PermissionCheck;
import com.ald.asjauthlib.authframework.core.utils.UIUtils;
import com.ald.asjauthlib.authframework.core.vm.ViewModel;

import retrofit2.Call;
import retrofit2.Response;


/**
 * 基础认证viewModel
 * Created by sean yu on 2017/7/11.
 */
public class BasicAuthVM implements ViewModel, ProtocolUtils.OpenNativeHandler {
    //广播接收器
    public static final String BROAD_CAST_AUTH_RESULT = "_auth_result";
    public final ObservableField<String> displayUserInfo = new ObservableField<>();
    //实名认证已认证字体颜色
    public final ObservableField<Integer> bindStatusUser = new ObservableField<>();
    //实名认证已认证图片
    public final ObservableField<Drawable> bindStatusImgUser = new ObservableField<>();

    //银行卡绑定状态
    public final ObservableField<String> displayBindStatus = new ObservableField<>();
    //银行卡已绑定字体颜色
    public final ObservableField<Integer> bindStatusBank = new ObservableField<>();
    //银行卡已绑定图片
    public final ObservableField<Drawable> bindStatusImgBank = new ObservableField<>();

    //芝麻信用
    public final ObservableField<String> displayZmStatus = new ObservableField<>();
    //芝麻信用已认证字体颜色
    public final ObservableField<Integer> bindStatusZM = new ObservableField<>();
    //芝麻信用已认证图片
    public final ObservableField<Drawable> bindStatusImgZM = new ObservableField<>();
    //是否显示芝麻认证布局
    public final ObservableBoolean showZmLayout = new ObservableBoolean(true);

    //运营商认证
    public final ObservableField<String> displayOperatorStatus = new ObservableField<>();
    //运营商已认证字体颜色
    public final ObservableField<Integer> bindStatusOperator = new ObservableField<>();
    //运营商已认证图片
    public final ObservableField<Drawable> bindStatusImgOperator = new ObservableField<>();


    //联系人
    public final ObservableField<String> displayContactsStatus = new ObservableField<>();
    //联系人已认证字体颜色
    public final ObservableField<Integer> bindStatusMail = new ObservableField<>();
    //联系人已认证图片
    public final ObservableField<Drawable> bindStatusImgMail = new ObservableField<>();

    private AlaActivity mActivity;
    private CreditPromoteModel creditPromoteModel;
    private FragmentBasicAuthBinding mBinding;

    private String scene = "";
    private boolean zmRequest = false;//防止芝麻分认证同步接口重复调用
    private boolean mLowSystemVersionCheck;
    private ZmLowVSystemHintDialog mZmLowVSystemHintDialog;


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (MiscUtils.isEqualsIgnoreCase(action, BROAD_CAST_AUTH_RESULT)) {
                load();
            }
        }
    };

    public BasicAuthVM(AlaActivity activity, FragmentBasicAuthBinding binding, String scene) {
        this.mActivity = activity;
        mBinding = binding;
        this.scene = scene;
    }

    public void setOpenNativeHandler() {
        ProtocolUtils.setOpenNativeHandler(this);
    }

    public void onDestroy() {
        ProtocolUtils.setOpenNativeHandler(null);
    }


    /**
     * 传入导入的CreditPromoteModel
     */
    public void setCreditPromoteModel(CreditPromoteModel creditPromoteModel) {
        this.creditPromoteModel = creditPromoteModel;
        String basic = creditPromoteModel.getBasicStatus();

        if (ModelEnum.Y.getModel().equals(basic) || ModelEnum.P.getModel().equals(basic)) {
            mBinding.llTdIdentify.setVisibility(View.VISIBLE);
        } else if (ModelEnum.A.getModel().equals(basic)) {
            //未审核，显示底部btn
            if (ModelEnum.N.getModel().equals(creditPromoteModel.getFlag()))
                mBinding.llTdIdentify.setVisibility(View.GONE);
            else
                mBinding.llTdIdentify.setVisibility(View.VISIBLE);
        }
        displayView(creditPromoteModel);
    }

    /**
     * 注册微信广播
     */
    public void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BROAD_CAST_AUTH_RESULT);
        AlaConfig.getLocalBroadcastManager().registerReceiver(receiver, intentFilter);
    }

    /**
     * 销毁监听
     */
    public void unregisterReceiver() {
        AlaConfig.getLocalBroadcastManager().unregisterReceiver(receiver);
    }

    /**
     * 获取提升信用信息
     */
    public void load() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("scene", scene);
        Call<CreditPromoteModel> call = RDClient.getService(BusinessApi.class).getCreditPromoteInfoV1(jsonObject);
        NetworkUtil.showCutscenes(AlaConfig.getContext(), call);
        call.enqueue(new RequestCallBack<CreditPromoteModel>() {
            @Override
            public void onSuccess(Call<CreditPromoteModel> call, Response<CreditPromoteModel> response) {
                creditPromoteModel = response.body();
                displayView(creditPromoteModel);
            }

        });
    }


    /*
     *  视图绑定
     * */
    public void displayView(CreditPromoteModel creditPromoteModel) {
        if (creditPromoteModel == null) {
            UIUtils.showToast((AlaConfig.getContext()).getResources().getString(R.string.err_data_pull_refresh_hint));
            return;
        }
        this.creditPromoteModel = creditPromoteModel;

        //是否实名认证
        if (MiscUtils.isEqualsIgnoreCase(ModelEnum.Y.getModel(), creditPromoteModel.getFaceStatus())) {
            setStatusStyle(3, displayUserInfo, bindStatusUser, bindStatusImgUser);
        } else if (MiscUtils.isEqualsIgnoreCase(ModelEnum.E.getModel()
                , creditPromoteModel.getFaceStatus())) {
            displayUserInfo.set(AlaConfig.getContext().getResources().getString(R.string.credit_promote_do_auth));
            setStatusStyle(4, displayUserInfo, bindStatusUser, bindStatusImgUser);
        } else {
            displayUserInfo.set(AlaConfig.getContext().getString(R.string.credit_promote_do_auth));
            setStatusStyle(0, displayUserInfo, bindStatusUser, bindStatusImgUser);
        }
        //绑定银行卡状态

        if (MiscUtils.isEqualsIgnoreCase(creditPromoteModel.getBankcardStatus(), ModelEnum.Y.getModel())) {
            setStatusStyle(3, displayBindStatus, bindStatusBank, bindStatusImgBank);
        } else if (MiscUtils.isEqualsIgnoreCase(creditPromoteModel.getBankcardStatus(), ModelEnum.E.getModel())) {
            setStatusStyle(4, displayBindStatus, bindStatusBank, bindStatusImgBank);
        } else {
            setStatusStyle(0, displayBindStatus, bindStatusBank, bindStatusImgBank);
        }
        //芝麻信用认证
        displayZmView(creditPromoteModel.getZmModel());

        //运营商认证
        if (MiscUtils.isEqualsIgnoreCase(ModelEnum.Y.getModel(), creditPromoteModel.getMobileStatus())) {
            setStatusStyle(3, displayOperatorStatus, bindStatusOperator, bindStatusImgOperator);
        } else if (MiscUtils.isEqualsIgnoreCase(ModelEnum.W.getModel(), creditPromoteModel.getMobileStatus())) {
            setStatusStyle(1, displayOperatorStatus, bindStatusOperator, bindStatusImgOperator);
        } else if (MiscUtils.isEqualsIgnoreCase(ModelEnum.E.getModel(), creditPromoteModel.getMobileStatus())) {
            setStatusStyle(4, displayOperatorStatus, bindStatusOperator, bindStatusImgOperator);
        } else {
            if (MiscUtils.isEqualsIgnoreCase(ModelEnum.Y.getModel(), creditPromoteModel.getGmtMobileExist())) {
                setStatusStyle(2, displayOperatorStatus, bindStatusOperator, bindStatusImgOperator);

            } else {
                setStatusStyle(0, displayOperatorStatus, bindStatusOperator, bindStatusImgOperator);

            }
        }

        //通讯录设置
        if (MiscUtils.isEqualsIgnoreCase(creditPromoteModel.getTeldirStatus(), ModelEnum.Y.getModel())) {
            setStatusStyle(3, displayContactsStatus, bindStatusMail, bindStatusImgMail);
        } else if (MiscUtils.isEqualsIgnoreCase(creditPromoteModel.getTeldirStatus(), ModelEnum.E.getModel())) {
            setStatusStyle(4, displayContactsStatus, bindStatusMail, bindStatusImgMail);
        } else {
            setStatusStyle(0, displayContactsStatus, bindStatusMail, bindStatusImgMail);

        }
    }

    private void displayZmView(CreditPromoteModel.ZmModel zmModel) {
        if (zmModel == null)
            return;
        showZmLayout.set(zmModel.getIsShow() != null && MiscUtils.isEqualsIgnoreCase(zmModel.getIsShow(), ModelEnum.Y.getModel()));
        displayZmStatus.set(zmModel.getZmDesc());
        bindStatusImgZM.set(ContextCompat.getDrawable(AlaConfig.getContext(), MiscUtils.isEmpty(zmModel.getZmxyAuthUrl())
                ? R.drawable.ic_credit_auth_already : R.drawable.ic_auth_right_arrow));
        int txtColor;
        switch (zmModel.getZmDesc() == null ? "未认证" : zmModel.getZmDesc()) {
            case "已认证":
                txtColor = R.color.color_auth_status_auth;
                break;
            case "重新认证":
            case "重新提交":
                txtColor = R.color.red;
                break;
            case "未认证":
                txtColor = R.color.color_auth_status_not_auth;
                break;
            default:
                txtColor = R.color.color_auth_status_auth;
                break;
        }
        bindStatusZM.set(AlaConfig.getContext().getResources().getColor(txtColor));
    }

    /**
     * 跳转实名认证信息页
     */
    public void showAuthUserClick(View view) {
        if (creditPromoteModel == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(BundleKeys.BANK_CARD_NAME, creditPromoteModel.getRealName());
        intent.putExtra(BundleKeys.SETTING_PAY_ID_NUMBER, creditPromoteModel.getIdNumber());
        ActivityUtils.push(RRIdConfirmActivity.class, intent);
    }

    /**
     * 展示綁定銀行卡
     */
    public void showAuthCardClick(View view) {
        if (creditPromoteModel == null) {
            return;
        }
        if (MiscUtils.isEqualsIgnoreCase(creditPromoteModel.getBankcardStatus(), ModelEnum.N.getModel())) {
            //跳转绑定银行卡页面
            Intent intent = new Intent();
            intent.putExtra(BundleKeys.BANK_CARD_NAME, creditPromoteModel.getRealName());
            intent.putExtra(BundleKeys.SETTING_PAY_ID_NUMBER, creditPromoteModel.getIdNumber());
            intent.putExtra(BundleKeys.STAGE_JUMP, StageJumpEnum.STAGE_AUTH.getModel());
            intent.putExtra(BundleKeys.INTENT_KEY_CREDIT_PROMOTE_SCENE, mActivity.getIntent().getStringExtra(BundleKeys.INTENT_KEY_CREDIT_PROMOTE_SCENE));
            ActivityUtils.push(BankCardAddIdActivity.class, intent);

        } else {
            Intent intent = new Intent();
            intent.putExtra(BundleKeys.BANK_CARD_NAME, creditPromoteModel.getRealName());
            intent.putExtra(BundleKeys.SETTING_PAY_CARD_NUMBER, creditPromoteModel.getBankCard());
            intent.putExtra(BundleKeys.SETTING_PAY_PHONE, creditPromoteModel.getPhoneNum());
            intent.putExtra(BundleKeys.IS_UPLOAD_IMAGE, creditPromoteModel.getIsUploadImage());
            ActivityUtils.push(AuthUserInfoActivity.class, intent);
        }
    }

    /**
     * 芝麻授权
     */
    public void bindZmClick(View view) {
        //安卓版本低于5.0的偶尔会出现无法唤醒支付宝的情况，所以弹窗提示用户手动开启支付宝
        if (!mLowSystemVersionCheck && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            if (mZmLowVSystemHintDialog == null) {
                mZmLowVSystemHintDialog = new ZmLowVSystemHintDialog();
                mZmLowVSystemHintDialog.setConfirmListener(new ZmLowVSystemHintDialog.ConfirmListener() {
                    @Override
                    public void confirm() {
                        mLowSystemVersionCheck = true;
                        bindZmClick(view);
                    }
                });
            }
            mZmLowVSystemHintDialog.show(mActivity.getSupportFragmentManager(), "ZmLowVSystemHintDialog");
            return;
        }
        mLowSystemVersionCheck = false;
        if (creditPromoteModel == null || creditPromoteModel.getZmModel() == null) {
            return;
        }
        if (MiscUtils.isEqualsIgnoreCase(creditPromoteModel.getBankcardStatus(), ModelEnum.N.getModel())) {
            UIUtils.showToast(AlaConfig.getContext().getResources().getString(R.string.auth_bank_bard_unbind));
            return;
        }
        if (MiscUtils.isEmpty(creditPromoteModel.getZmModel().getZmxyAuthUrl())) {
            UIUtils.showToast(AlaConfig.getContext().getResources().getString(R.string.credit_idf_toast));
        } else {
            handZmAuth();
        }
    }

    /**
     * 运营商认证
     */
    public void bindPhoneClick(View view) {
        if (creditPromoteModel == null) {
            return;
        }
        if (MiscUtils.isEqualsIgnoreCase(ModelEnum.N.getModel(), creditPromoteModel.getBankcardStatus())) {
            UIUtils.showToast(AlaConfig.getContext().getResources().getString(R.string.auth_bank_bard_unbind));
            return;
        }
        String mobileStatus = creditPromoteModel.getMobileStatus();
        if (MiscUtils.isEqualsIgnoreCase(ModelEnum.Y.getModel(), mobileStatus)) {
            //显示运营商认证成功页面
            if (MiscUtils.isEqualsIgnoreCase(ModelEnum.A.getModel(), mobileStatus)) {
                showAuthInfoDialog(AlaConfig.getContext().getResources().getString(R.string.credit_promote_mobile_auth_success));
            } else {
                showAuthInfoDialog(AlaConfig.getContext().getResources().getString(R.string.credit_promote_mobile_success));
            }
        } else if (MiscUtils.isEqualsIgnoreCase(ModelEnum.W.getModel(), mobileStatus)) {
            displayOperatorStatus.set(ModelEnum.W.getDesc());
            showAuthInfoDialog(AlaConfig.getContext().getResources().getString(R.string.credit_promote_mobile_ing));
        } else if (MiscUtils.isEqualsIgnoreCase(ModelEnum.N.getModel(), mobileStatus)
                || MiscUtils.isEqualsIgnoreCase(ModelEnum.E.getModel(), mobileStatus)) {
            if (MiscUtils.isEqualsIgnoreCase(ModelEnum.N.getModel(), (mobileStatus))) {
                handleMobileOperator();
            } else {
                showAuthAgainDialog(AlaConfig.getContext().getResources().getString(R.string.credit_promote_mobile_auth_again));
            }
        }
    }

    /**
     * 展示运营商认证对话框
     */
    private void showAuthInfoDialog(String content) {
        showAuthPhoneDialog(content, AlaConfig.getContext().getResources().getString(R.string.credit_promote_mobile_sure), "",
                View.GONE);
    }

    /**
     * 展示运营商重新认证对话框
     */
    private void showAuthAgainDialog(String content) {
        showAuthPhoneDialog(content, AlaConfig.getContext().getResources().getString(R.string.credit_promote_mobile_give_up),
                AlaConfig.getContext().getResources().getString(R.string.credit_promote_mobile_again), View.VISIBLE);
    }

    /**
     * 展示运营商认证对话框
     */
    private void showAuthPhoneDialog(String content, String cancelText, String sureText, int visible) {
        CreditPromoteDialog dialog = new CreditPromoteDialog(mActivity);
        dialog.setContent(content);
        dialog.setCancelText(cancelText);
        dialog.setSureText(sureText);
        dialog.setSureBtnVisible(visible);
        dialog.setListener(new CreditPromoteDialog.MakeSureListener() {
            @Override
            public void onSureClick(Dialog dialog, View view) {
                handleMobileOperator();
                dialog.dismiss();
            }
        });
        dialog.show();

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
     * 紧急联系人(通讯录)
     */
    public void bindContactsClick(View view) {

        if (creditPromoteModel != null && MiscUtils.isEqualsIgnoreCase(ModelEnum.Y.getModel(), creditPromoteModel.getTeldirStatus())) {
            CreditPromoteDialog dialog = new CreditPromoteDialog(mActivity);
            dialog.setContent(AlaConfig.getContext().getResources().getString(R.string.credit_promote_contact_update));
            dialog.setCancelText(AlaConfig.getContext().getResources().getString(R.string.credit_promote_mobile_give_up));
            dialog.setSureText(AlaConfig.getContext().getResources().getString(R.string.credit_promote_mobile_again));
            dialog.setSureBtnVisible(View.VISIBLE);
            dialog.setListener(new CreditPromoteDialog.MakeSureListener() {
                @Override
                public void onSureClick(Dialog dialog, View view) {
                    getContactsList();
                    dialog.dismiss();
                }
            });
            dialog.show();
            return;
        } else if (creditPromoteModel != null && MiscUtils.isEqualsIgnoreCase(creditPromoteModel.getBankcardStatus(), ModelEnum.N.getModel())) {
            UIUtils.showToast(AlaConfig.getContext().getResources().getString(R.string.auth_bank_bard_unbind));
            return;
        }
        getContactsList();

    }

    /**
     * 芝麻授权
     */
    private void handZmAuth() {
        if (creditPromoteModel != null && MiscUtils.isNotEmpty(creditPromoteModel.getZmModel().getZmxyAuthUrl())) {
            zmRequest = false;
            Intent i = new Intent();
            i.putExtra(HTML5WebView.INTENT_BASE_URL, creditPromoteModel.getZmModel().getZmxyAuthUrl());
            ActivityUtils.push(HTML5WebView.class, i);
        }
    }


    @Override
    public void onHandelOpenNative(String name, String data, String otherData) {
        //芝麻信用
        if (MiscUtils.isEqualsIgnoreCase(AlaProtocol.Protocol.ZMXY_KEY, name)) {
            if (!zmRequest) {
                zmRequest = true;
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("respBody", data);
                jsonObject.put("sign", otherData);
                RDClient.getService(AuthApi.class).authCredit(jsonObject).compose(RxUtils.io_main())
                        .subscribe(new BaseObserver<ZMXYModel>(mActivity) {
                            @Override
                            public void onSuccess(ZMXYModel zmxyModel) {
                                ((CreditPromoteActivity) mActivity).reload();
                                ActivityUtils.popUntilWithoutRefresh(CreditPromoteActivity.class);
                            }

                            @Override
                            public void onFailure(ApiException apiException) {
                                ActivityUtils.popUntilWithoutRefresh(CreditPromoteActivity.class);
                            }
                        });
            }
        } else if (MiscUtils.isEqualsIgnoreCase(AlaProtocol.GROUP_MOBILE_OPERATOR, name)) {
            if (AlaProtocol.Protocol.MOBILE_BACK_KEY.equals(otherData)) {
                ActivityUtils.popUntilWithoutRefresh(CreditPromoteActivity.class);
                JSONObject jsonObject = JSONObject.parseObject(data);
                String status = jsonObject.getString("refreshStatus");
                if (MiscUtils.isEquals(status, "1")) {
                    //置为认证中，刷新重新请求状态
                    setStatusStyle(1, displayOperatorStatus, bindStatusOperator, bindStatusImgOperator);
//                    ((CreditPromoteActivity) context).reload();
                }
            }
        }

    }


    /**
     * 获取通讯录信息
     */
    private void getContactsList() {
        //通讯录权限
        if (!PermissionCheck.getInstance().checkPermission(AlaConfig.getContext(), Manifest.permission.READ_CONTACTS)) {
            PermissionCheck.getInstance().askForPermissions(mActivity == null ? AlaConfig.getCurrentActivity() : mActivity, new String[]{Manifest.permission.READ_CONTACTS},
                    PermissionCheck.REQUEST_CODE_ALL);
        } else {
            String contactString = ContactsUtils.getPostContacts(AlaConfig.getContext());
            JSONObject face = new JSONObject();
            face.put("contacts", contactString);
            face.put("scene", scene);
            Call<ContactsSycModel> call = RDClient.getService(AuthApi.class).authContacts(face);
            NetworkUtil.showCutscenes(AlaConfig.getContext(), call);
            call.enqueue(new RequestCallBack<ContactsSycModel>() {
                @Override
                public void onSuccess(Call<ContactsSycModel> call, Response<ContactsSycModel> response) {
                    creditPromoteModel.setTeldirStatus(ModelEnum.Y.getModel());
                    ((CreditPromoteActivity) mActivity).reload();
                }
            });
        }
    }


    /**
     * 设置状态文字及背景
     *
     * @param status 0.为认证 1.认证中 2.重新提交 3.已认证,4.已过期
     * @param txt    状态文字
     * @param color  状态文字颜色
     * @param ic     状态图标
     */
    private void setStatusStyle(int status, ObservableField<String> txt, ObservableField<Integer> color, ObservableField<Drawable> ic) {
        Context context = AlaConfig.getContext();
        Resources resources = AlaConfig.getResources();
        switch (status) {
            case 0:
                //银行卡：未绑定 其他：未认证
                txt.set(txt == displayBindStatus ? resources.getString(R.string.credit_promote_bind_card_unbind) :
                        resources.getString(R.string.credit_promote_do_auth));
                color.set(ContextCompat.getColor(AlaConfig.getContext(), R.color.color_auth_status_not_auth));
                ic.set(ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.ic_auth_right_arrow));
                break;
            case 1:
                //认证中
                txt.set(ModelEnum.W.getDesc());
                ic.set(ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.ic_auth_right_arrow));
                color.set(ContextCompat.getColor(AlaConfig.getContext(), R.color.color_auth_status_auth_ing));
                break;
            case 2:
                //重新认证（认证失败）
                txt.set(AlaConfig.getResources().getString(R.string.basic_auth_recommit));
                color.set(ContextCompat.getColor(AlaConfig.getContext(), R.color.red));//需确认
                ic.set(ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.ic_auth_right_arrow));
                break;
            case 3:
                //已认证
                txt.set(txt == displayBindStatus ? resources.getString(R.string.credit_promote_bind_card_bind) :
                        resources.getString(R.string.credit_promote_auth));//绑定银行卡
                ic.set(ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.ic_credit_auth_already));
                color.set(ContextCompat.getColor(AlaConfig.getContext(), R.color.color_auth_status_auth));
                resources.getString(R.string.credit_promote_auth);
                break;
            case 4:
                //已过期
                txt.set(resources.getString(R.string.basic_auth_overdue));
                color.set(ContextCompat.getColor(AlaConfig.getContext(), R.color.red));
                ic.set(ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.ic_auth_right_arrow));
                break;
        }
    }


}
