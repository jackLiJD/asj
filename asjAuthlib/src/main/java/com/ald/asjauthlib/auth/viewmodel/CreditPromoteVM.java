package com.ald.asjauthlib.auth.viewmodel;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ScrollView;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.AuthApi;
import com.ald.asjauthlib.utils.AuthUtils;
import com.ald.asjauthlib.auth.BusinessApi;
import com.ald.asjauthlib.dialog.CreditPromoteDialog;
import com.ald.asjauthlib.dialog.RedPackageDialog;
import com.ald.asjauthlib.auth.model.AuthStrongRiskModel;
import com.ald.asjauthlib.auth.model.ContactsSycModel;
import com.ald.asjauthlib.auth.model.CreditPromoteModel;
import com.ald.asjauthlib.auth.ui.AuthStatusActivity;
import com.ald.asjauthlib.auth.ui.BasicAuthFragment;
import com.ald.asjauthlib.auth.ui.ExtraAuthFragment;
import com.ald.asjauthlib.databinding.ActivityCreditPromoteBinding;
import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.utils.ContactsUtils;
import com.ald.asjauthlib.utils.ModelEnum;
import com.ald.asjauthlib.utils.StageJumpEnum;
import com.alibaba.fastjson.JSONObject;
import com.bqs.risk.df.android.BqsDF;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.network.NetworkUtil;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.network.RequestCallBack;
import com.ald.asjauthlib.authframework.core.network.exception.ApiException;
import com.ald.asjauthlib.authframework.core.network.exception.ApiExceptionEnum;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.CanDoubleClick;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.authframework.core.utils.PermissionCheck;
import com.ald.asjauthlib.authframework.core.utils.UIUtils;
import com.ald.asjauthlib.authframework.core.vm.BaseVM;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import cn.tongdun.android.shell.FMAgent;
import retrofit2.Call;
import retrofit2.Response;

import static com.ald.asjauthlib.authframework.core.utils.PermissionCheck.REQUEST_CODE_CCONTACT;


/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/1/12 14:14
 * 描述：
 * 修订历史：
 */
public class CreditPromoteVM extends BaseVM {

    //进度条及下横线显示
    public final ObservableBoolean displayStepLayout = new ObservableBoolean();

    //fragment显示状态
    public final ObservableBoolean displayBasicFragment = new ObservableBoolean();

    //fragment显示状态
    public final ObservableBoolean displayExtraFragment = new ObservableBoolean();

    //banner额度提升范围
    public final ObservableField<String> fromLimit = new ObservableField<>("");
    public final ObservableField<String> toLimit = new ObservableField<>("");
    public final ObservableField<String> txtLimitNotice = new ObservableField<>("");


    //认证状态图标(打开或是关闭)
    public final ObservableField<Drawable> basicAuthStatusIc = new ObservableField<>();

    public final ObservableField<Drawable> extraAuthStatusIc = new ObservableField<>();

    //基础认证状态文案
    public final ObservableField<String> txtBasicStatus = new ObservableField<>();

    //普通banner显示状态，和提额banner互斥
    public final ObservableBoolean showBanner = new ObservableBoolean(false);

    //通知banner
    public final ObservableBoolean showNoticeBanner = new ObservableBoolean(false);

    //提额banner
    public final ObservableBoolean showLimitBanner = new ObservableBoolean(false);

    //基础认证title显示状态
    public final ObservableBoolean showBasicStatus = new ObservableBoolean(false);

    //补充认证title显示状态
    public final ObservableBoolean showExtraStatus = new ObservableBoolean(false);

    public final ObservableField<String> txtNoticeTitle1 = new ObservableField<>();
    public final ObservableField<String> txtNoticeTitle2 = new ObservableField<>();


    public final ObservableField<String> title1 = new ObservableField<>();
    public final ObservableField<String> title2 = new ObservableField<>();


    /**
     * fragmentTransaction 中tag名称，以变从中获取fragment
     */
    private final String[] TAGS = {"BASIC", "EXTRA"};

    private Activity context;
    private FragmentManager manager;
    private BasicAuthFragment basicFragment;
    private ExtraAuthFragment extraFragment;

    private CreditPromoteModel model;

    private ActivityCreditPromoteBinding mBinding;
    private String scene;//CASH 现金贷场景，ONLINE 线上分期，TRAIN线下培训 不传默认为现金贷

    /**
     * @param scene 场景
     */
    public CreditPromoteVM(Activity context, FragmentManager manger, ActivityCreditPromoteBinding binding, String scene) {
        this.context = context;
        this.manager = manger;
        mBinding = binding;
        this.scene = scene;
        requestAuthInfo(binding.refreshLayout);
    }

    /**
     * 请求认证信息
     */
    public void requestAuthInfo(RefreshLayout refreshLayout) {
        JSONObject jsonObject = new JSONObject();
        if (!MiscUtils.isEmpty(scene))//如果无法判断场景则不传
            jsonObject.put("scene", scene);
        Call<CreditPromoteModel> call = RDClient.getService(BusinessApi.class).getCreditPromoteInfoV1(jsonObject);
        NetworkUtil.showCutscenes(context, call);
        call.enqueue(new RequestCallBack<CreditPromoteModel>(refreshLayout) {
            @Override
            public void onSuccess(Call<CreditPromoteModel> call, Response<CreditPromoteModel> response) {
                try {
                    model = response.body();
                    displayTabInfo(model);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 是否可以进入补充认证阶段
     */
    private void displayTabInfo(CreditPromoteModel model) {
        if (model == null) {
            return;
        }
        //如果未进行基础认证，则显示 basicStatus 基础认证【A:未审核，N:未通过审核，P: 审核中，Y:已通过审核】
        String basic = model.getBasicStatus();
        String risk = model.getRiskStatus();
        String faceStatus = model.getFaceStatus();
        String bankcardStatus = model.getBankcardStatus();
        String mobileStatus = model.getMobileStatus();
        basicAuthStatusIc.set(ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.ic_auth_close));
        extraAuthStatusIc.set(ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.ic_auth_close));
        if (basic.equals("N") && risk.equals("N")) {//暂无信用额度，banner显示 基础认证打开,补充认证默认关闭
            showBasicStatus.set(true);
            showExtraStatus.set(true);
            showNoticeBanner.set(true);
            showLimitBanner.set(false);
            showBanner.set(false);
            txtBasicStatus.set("认证失败");
            mBinding.txtBasicStatus.setTextColor(ContextCompat.getColor(context, R.color.red));
            displayStepLayout.set(false);
            txtNoticeTitle1.set(model.getTitle1());
            txtNoticeTitle2.set(model.getTitle2());
            mBinding.btnSubmit.setVisibility(View.VISIBLE);
            mBinding.btnSubmit.setEnabled(false);
            mBinding.btnSubmitBottom.setVisibility(View.GONE);
            mBinding.btnSubmit.setText(model.getRiskRetrialRemind());
            showBasicFragment();
        } else if (basic.equals("P") && risk.equals("P"))//基础信息认证中，banner显示，基础认证（认证中） 默认关闭，补充认证打开
        {
            showBasicStatus.set(true);
            showExtraStatus.set(true);
            showBanner.set(true);
            showLimitBanner.set(false);
            showNoticeBanner.set(false);
            txtBasicStatus.set(context.getString(R.string.auth_banner_basic_ing));
            mBinding.txtBasicStatus.setTextColor(ContextCompat.getColor(context, R.color.color_auth_status_auth_ing));
            displayStepLayout.set(false);
            title1.set(MiscUtils.isEmpty(model.getTitle1()) ? "基础信息认证中" : model.getTitle1());
            title2.set(model.getTitle2());
            mBinding.btnSubmit.setVisibility(View.GONE);
            mBinding.btnSubmitBottom.setVisibility(View.GONE);
            mBinding.btnSubmitBottomTop.setVisibility(View.GONE);
            showExtraFragment();

        } else if (basic.equals("Y") && risk.equals("Y")) {//基础认证已认证，默认关闭，补充认证打开，提升额度显示
            showBasicStatus.set(true);
            showExtraStatus.set(true);
            showBanner.set(false);
            showLimitBanner.set(true);
            showNoticeBanner.set(false);
            txtBasicStatus.set(context.getString(R.string.auth_banner_basic_already));
            mBinding.txtBasicStatus.setTextColor(ContextCompat.getColor(context, R.color.color_auth_status_auth));
            displayStepLayout.set(false);
            fromLimit.set(Long.toString(model.getCurrentAmount()));
            toLimit.set(Long.toString(model.getHighestAmount()));
            txtLimitNotice.set(model.getTitle2());
            mBinding.btnSubmitBottom.setVisibility(View.GONE);
            mBinding.btnSubmit.setVisibility(View.GONE);
            mBinding.btnSubmitBottomTop.setVisibility(View.GONE);
            showExtraFragment();

        } else if (basic.equals("P") && risk.equals("Y")) {//基础认证认证中，关闭，提升额度banner 补充认证打开
            showBasicStatus.set(true);
            showExtraStatus.set(true);
            showBanner.set(false);
            showLimitBanner.set(true);
            showNoticeBanner.set(false);
            txtBasicStatus.set(context.getString(R.string.auth_banner_basic_ing));
            mBinding.txtBasicStatus.setTextColor(ContextCompat.getColor(context, R.color.color_auth_status_auth_ing));
            displayStepLayout.set(false);
            fromLimit.set(Long.toString(model.getCurrentAmount()));
            toLimit.set(Long.toString(model.getHighestAmount()));
            txtLimitNotice.set(model.getTitle2());
            mBinding.btnSubmitBottom.setVisibility(View.GONE);
            mBinding.btnSubmitBottomTop.setVisibility(View.GONE);
            showExtraFragment();

        } else if (basic.equals("N") && risk.equals("Y")) {//认证失败，提升额度banner显示，基础认证打开
            showBasicStatus.set(true);
            showExtraStatus.set(true);
            showBanner.set(false);
            showLimitBanner.set(true);
            showNoticeBanner.set(false);
            txtLimitNotice.set(model.getTitle2());
            fromLimit.set(Long.toString(model.getCurrentAmount()));
            toLimit.set(Long.toString(model.getHighestAmount()));
            txtBasicStatus.set("认证失败");
            mBinding.txtBasicStatus.setTextColor(ContextCompat.getColor(context, R.color.red));
            displayStepLayout.set(false);
            mBinding.btnSubmit.setEnabled(false);
            mBinding.btnSubmit.setVisibility(View.VISIBLE);
            mBinding.btnSubmitBottom.setVisibility(View.GONE);
            mBinding.btnSubmitBottomTop.setVisibility(View.GONE);
            showBasicFragment();
        } else if (basic.equals("A")) {
            if (model.getFlag().equals("N")) {

                if (MiscUtils.isNotEmpty(model.getBankcardStatus()) && ModelEnum.N.getModel()
                        .equals(model.getBankcardStatus())) {
                    mBinding.btnSubmitBottomTop.setVisibility(View.VISIBLE);
                } else {
                    mBinding.btnSubmitBottomTop.setVisibility(View.GONE);
                }
                //首次提交
                showBasicStatus.set(false);
                showExtraStatus.set(false);
                mBinding.btnSubmitBottom.setVisibility(View.VISIBLE);
                mBinding.btnSubmitBottom.setText(context.getString(R.string.auth_basic_submit_button_submit));
                mBinding.btnSubmit.setVisibility(View.GONE);

                showBanner.set(true);
                showLimitBanner.set(false);
                showNoticeBanner.set(false);
                title1.set(model.getTitle1());
                title2.set(model.getTitle2());
                displayStepLayout.set(true);
                showBasicFragment();

                if (!AuthUtils.isAllowSubmit(context, model, false))
                    mBinding.btnSubmitBottom.setEnabled(false);
                else
                    mBinding.btnSubmitBottom.setEnabled(true);

            } else {
                //AY
                displayStepLayout.set(false);
                showBasicStatus.set(true);
                showExtraStatus.set(true);
                showBanner.set(false);
                showLimitBanner.set(true);
                showNoticeBanner.set(false);
                toLimit.set(Long.toString(model.getHighestAmount()));
                fromLimit.set(Long.toString(model.getCurrentAmount()));
                txtLimitNotice.set(model.getTitle2());
                txtBasicStatus.set(AlaConfig.getResources().getString(R.string.credit_promote_basic_fail));
                mBinding.txtBasicStatus.setTextColor(ContextCompat.getColor(context, R.color.red));
                mBinding.btnSubmit.setVisibility(View.VISIBLE);
                mBinding.btnSubmit.setEnabled(true);
                mBinding.btnSubmit.setText(context.getString(R.string.auth_basic_submit_button_submit));
                mBinding.btnSubmitBottom.setVisibility(View.GONE);
                mBinding.btnSubmitBottomTop.setVisibility(View.GONE);
                showBasicFragment();
            }

        } else {
            showBasicStatus.set(false);
            showExtraStatus.set(false);
            showBanner.set(true);
            showLimitBanner.set(false);
            showNoticeBanner.set(false);
            title1.set(model.getTitle1());
            title2.set(model.getTitle2());
            displayStepLayout.set(true);
            showBasicFragment();
        }
        //补充认证隐藏
        if (model.getShowExtraTab() == 0) {
            showExtraStatus.set(false);
            hideFragment(extraFragment);
            showBasicFragment();
        }
        //基础认证只要有一项为N就展开，不能根据可隐藏基础认证项做判断
        if (ModelEnum.N.getModel().equals(faceStatus) || ModelEnum.N.getModel().equals(bankcardStatus) || ModelEnum.N.getModel().equals(mobileStatus)) {
            showBasicFragment();
        }
    }

    /**
     * 基础认证点击
     */
    @CanDoubleClick
    public void onBasicClick(View view) {
        if (basicFragment == null || !displayBasicFragment.get()) {
            //显示基础认证列表
            showBasicFragment();
            displayBasicFragment.set(true);
            basicAuthStatusIc.set(ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.ic_auth_open));
            boolean needShowSubmitButton = (model.getBasicStatus().equals("N") && model.getRiskStatus().equals("N"))
                    || (model.getBasicStatus().equals("N") && model.getRiskStatus().equals("Y")
                    || (model.getBasicStatus().equals("A") && model.getFlag().equals("Y")));
            if (needShowSubmitButton)
                mBinding.btnSubmit.setVisibility(View.VISIBLE);
        } else {
            displayBasicFragment.set(false);
            hideFragment(basicFragment);
            if (mBinding.btnSubmit.getVisibility() == View.VISIBLE)
                mBinding.btnSubmit.setVisibility(View.GONE);
            basicAuthStatusIc.set(ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.ic_auth_close));
        }
    }

    /**
     * 补充认证点击
     */
    @CanDoubleClick
    public void onExtraClick(View view) {
        if (extraFragment == null || !displayExtraFragment.get()) {
            showExtraFragment();
            displayExtraFragment.set(true);
            extraAuthStatusIc.set(ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.ic_auth_open));
            Handler handler = new Handler();
            handler.post(() -> mBinding.scrollView.fullScroll(ScrollView.FOCUS_DOWN));
        } else {
            hideFragment(extraFragment);
            displayExtraFragment.set(false);
            extraAuthStatusIc.set(ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.ic_auth_close));
        }
    }


    /**
     * 修改基础认证fragment状态
     */
    private void showBasicFragment() {
        displayBasicFragment.set(true);
        FragmentTransaction fragmentTs = manager.beginTransaction();
        if (basicFragment != null) {
            basicFragment.refresh(model);
        } else {
            basicFragment = (BasicAuthFragment) manager.findFragmentByTag(TAGS[0]);
        }
        basicFragment = BasicAuthFragment.newInstance(model, scene);

        fragmentTs.add(R.id.fl_auth_content, basicFragment, TAGS[0]);

        fragmentTs.show(basicFragment);
        basicFragment.setForceLoad(true);

        fragmentTs.commit();
        //展开向下箭头
        basicAuthStatusIc.set(ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.ic_auth_open));
    }

    private void hideFragment(android.support.v4.app.Fragment fragment) {
        if (null != fragment) {
            manager.beginTransaction().hide(fragment).commitAllowingStateLoss();
        }

    }


    /**
     * 跳转到补充认证
     */
    private void showExtraFragment() {
        displayExtraFragment.set(true);
        if (null != extraFragment && !extraFragment.isHidden()) {
            extraFragment.refresh(model);
            return;
        }
        FragmentTransaction fragmentTs = manager.beginTransaction();
        if (extraFragment == null) {
            extraFragment = (ExtraAuthFragment) manager.findFragmentByTag(TAGS[1]);
        }
        if (extraFragment == null) {
            extraFragment = new ExtraAuthFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("model", model);
            bundle.putString("scene", scene);
            extraFragment.setArguments(bundle);

            fragmentTs.add(R.id.fl_auth_content_extra, extraFragment, TAGS[1]);
        }
        fragmentTs.show(extraFragment);
        extraFragment.setForceLoad(true);
        fragmentTs.commitAllowingStateLoss();
        extraAuthStatusIc.set(ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.ic_auth_open));
    }


    /**
     * 提交强风控审核
     */
    public void onSubmitClick(View view) {
        if (AuthUtils.isAllowSubmit(context, model, true)) {
            getContactsList();
        }
    }

    /**
     * 请求参数
     */
    private void requestSubmit(JSONObject object) {
//        CreditCenterActivity.needRefresh = true;//刷新51信用首页数据
        final String stageJump = context.getIntent().getStringExtra(BundleKeys.STAGE_JUMP);
        final Intent intent = new Intent(context, AuthStatusActivity.class);
        Call<AuthStrongRiskModel> call = RDClient.getService(AuthApi.class).authStrongRiskV1(object);
        NetworkUtil.showCutscenes(context, call);
        call.enqueue(new RequestCallBack<AuthStrongRiskModel>() {
            @Override
            public void onSuccess(Call<AuthStrongRiskModel> call, Response<AuthStrongRiskModel> response) {
                if (response.body().getCreditRebateMsg() != null && MiscUtils.isNotEmpty(response.body().getCreditRebateMsg())) {
                    showRedPackageDialog(response.body().getCreditRebateMsg());
                } else {
                    if (stageJump == null || stageJump.equals(StageJumpEnum.STAGE_START_AUTH.getModel())) {
                        //来自注册认证引导页，提交成功后关闭该页面
                        ActivityUtils.push(AuthStatusActivity.class, intent, BundleKeys.REQUEST_CODE_CREDIT_PROMOTE_START_AUTH);
                    } else if (stageJump.equals(StageJumpEnum.STAGE_LIMIT_AUTH.getModel())) {
                        //来自51信用首页的，提交强风控成功后需要关闭提额页面就，并刷新51信用
                        ActivityUtils.push(AuthStatusActivity.class, intent, BundleKeys.REQUEST_CREDIT_PROMOT_STATUS);
                    } else if (scene != null && scene.equals(BundleKeys.CREDIT_PROMOTE_SCENE_TRAIN)) {
                        //线下培训场景或者线下商圈提交强风控 刷新h5页面
                        ActivityUtils.push(AuthStatusActivity.class, intent, BundleKeys.REQUEST_CODE_CREDIT_PROMOTE_H5);
                    } else if (stageJump.equals(StageJumpEnum.STAGE_TRADE_SCAN.getModel())) {
//                        来自商圈二维码 刷新h5页面
                        ActivityUtils.push(AuthStatusActivity.class, intent, BundleKeys.REQUEST_CODE_CREDIT_PROMOTE_H5);
                    } else if (stageJump.equals(StageJumpEnum.STAGE_CASHIER.getModel())) {
                        ActivityUtils.push(AuthStatusActivity.class, intent, BundleKeys.REQUEST_CODE_CASHIER);
                    } else {
                        ActivityUtils.push(AuthStatusActivity.class, intent, BundleKeys.REQUEST_CODE_CREDIT_PROMOTE_REFRESH);
                    }
                    UIUtils.showToast(AlaConfig.getResources().getString(R.string.credit_promote_success_toast));
                }
            }

            @Override
            public void onFailure(Call<AuthStrongRiskModel> call, Throwable t) {
                super.onFailure(call, t);
                if ((t instanceof ApiException) && ((ApiException) t).getCode() == ApiExceptionEnum.CREDIT_PROMOT_OVERDUE.getErrorCode()) {
                    requestAuthInfo(null);
                } else {
                    if ((t instanceof ApiException) && ((ApiException) t).getCode() == ApiExceptionEnum.EMPTY.getErrorCode()) {
                        if (stageJump == null || stageJump.equals(StageJumpEnum.STAGE_START_AUTH.getModel())) {
                            //来自注册认证引导页，提交成功后关闭该页面
                            ActivityUtils.push(AuthStatusActivity.class, intent, BundleKeys.REQUEST_CODE_CREDIT_PROMOTE_START_AUTH);
                        } else if (stageJump.equals(StageJumpEnum.STAGE_LIMIT_AUTH.getModel())) {
                            //来自51信用首页的，提交强风控成功后需要关闭提额页面就，并刷新51信用
                            ActivityUtils.push(AuthStatusActivity.class, intent, BundleKeys.REQUEST_CREDIT_PROMOT_STATUS);
                        } else if (scene != null && scene.equals(BundleKeys.CREDIT_PROMOTE_SCENE_TRAIN)) {
                            //线下培训场景或者线下商圈提交强风控 刷新h5页面
                            ActivityUtils.push(AuthStatusActivity.class, intent, BundleKeys.REQUEST_CODE_CREDIT_PROMOTE_H5);
                        } else if (stageJump.equals(StageJumpEnum.STAGE_TRADE_SCAN.getModel())) {
//                        来自商圈二维码 刷新h5页面
                            ActivityUtils.push(AuthStatusActivity.class, intent, BundleKeys.REQUEST_CODE_CREDIT_PROMOTE_H5);
                        } else if (stageJump.equals(StageJumpEnum.STAGE_CASHIER.getModel())) {
                            ActivityUtils.push(AuthStatusActivity.class, intent, BundleKeys.REQUEST_CODE_CASHIER);
                        } else {
                            ActivityUtils.push(AuthStatusActivity.class, intent, BundleKeys.REQUEST_CODE_CREDIT_PROMOTE_REFRESH);
                        }
                    } else {
                        requestAuthInfo(null);

                    }
                }
            }
        });
    }

    /**
     * 显示红包弹窗
     *
     * @param content 红包描述
     */
    private void showRedPackageDialog(final String content) {
        RedPackageDialog dialog = new RedPackageDialog(context, R.style.TelDialog, "");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnDismissListener(dialog1 -> load());
        dialog.setStrongRisk(true);
        dialog.setContentAfter(content);
        dialog.show();
    }


    public void load() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("scene", scene);
        Call<CreditPromoteModel> call = RDClient.getService(BusinessApi.class).getCreditPromoteInfoV1(jsonObject);
        NetworkUtil.showCutscenes(context, call);
        call.enqueue(new RequestCallBack<CreditPromoteModel>() {
            @Override
            public void onSuccess(Call<CreditPromoteModel> call, Response<CreditPromoteModel> response) {
                model = response.body();
                displayTabInfo(model);
            }
        });
    }

    /**
     * 生成提交参数
     */
    private JSONObject generateSubmitParams(String blackBox) {
        JSONObject object = new JSONObject();
        object.put("blackBox", blackBox);
        if (!MiscUtils.isEmpty(scene))//无法判断场景情况不传该参数
            object.put("scene", scene);
        //白骑士设备指纹
        String bqsBlackBox = BqsDF.getTokenKey();
        object.put("bqsBlackBox", bqsBlackBox);
        return object;
    }

    private void getContacts() {
        CreditPromoteDialog dialog = new CreditPromoteDialog(context);
        dialog.setContent(AlaConfig.getResources().getString(R.string.credit_promote_contact_update));
        dialog.setCancelText(AlaConfig.getResources().getString(R.string.credit_promote_mobile_give_up));
        dialog.setSureText(AlaConfig.getResources().getString(R.string.credit_promote_mobile_again));
        dialog.setSureBtnVisible(View.VISIBLE);
        dialog.setListener((dialog1, view) -> dialog1.dismiss());
        dialog.show();
    }

    /**
     * 获取通讯录信息
     */
    private void getContactsList() {
        //通讯录权限
        if (!PermissionCheck.getInstance().checkPermission(context, Manifest.permission.READ_CONTACTS)) {
            PermissionCheck.getInstance().askForPermissions(context, new String[]{Manifest.permission.READ_CONTACTS},
                    REQUEST_CODE_CCONTACT);
        } else {

            String contactString;
            try {
                contactString = ContactsUtils.getPostContacts(context);
            } catch (Exception ex) {
                //权限弹框
                PermissionCheck.getInstance().showContactAskDialog(context, R.string.permission_name_contacts);
                return;
            }

            JSONObject face = new JSONObject();
            face.put("contacts", contactString);
            face.put("scene", scene);
            Call<ContactsSycModel> call = RDClient.getService(AuthApi.class).authContacts(face);
            NetworkUtil.showCutscenes(context, call);
            call.enqueue(new RequestCallBack<ContactsSycModel>(context) {
                @Override
                public void onSuccess(Call<ContactsSycModel> call, Response<ContactsSycModel> response) {
                    String blackBox = FMAgent.onEvent(context);
                    JSONObject object = generateSubmitParams(blackBox);
                    requestSubmit(object);
                }

                @Override
                public void onFailure(Call<ContactsSycModel> call, Throwable t) {
                    super.onFailure(call, t);
                    if (t instanceof ApiException) {
                        ApiException apiException = (ApiException) t;
                        int code = apiException.getCode();
                        if (code == ApiExceptionEnum.EMPTY.getErrorCode()) {
                            UIUtils.showToast( "通讯录接口异常");
                        }
                    }
                }
            });
        }
    }

    public void onExtraActivityResult(int requestCode, int resultCode, Intent data) {
        extraFragment.onActivityResult(requestCode, resultCode, data);
    }


    public void onUIRequestPermissionsGrantedResult(int requestCode) {
        if (requestCode == REQUEST_CODE_CCONTACT) {
            getContactsList();
        }
    }

}
