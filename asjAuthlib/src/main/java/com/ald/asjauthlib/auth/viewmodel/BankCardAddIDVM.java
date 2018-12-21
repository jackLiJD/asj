package com.ald.asjauthlib.auth.viewmodel;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.hardware.Camera;
import android.view.Gravity;
import android.view.View;

import com.ald.asjauthlib.utils.Constant;
import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.AuthApi;
import com.ald.asjauthlib.auth.UserApi;
import com.ald.asjauthlib.bankcardscan.BankCardScanActivity;
import com.ald.asjauthlib.bankcardscan.viewmodel.BankcardScanVM;
import com.ald.asjauthlib.dialog.NoSupportBankDialog;
import com.ald.asjauthlib.dialog.NoticeImageDialog;
import com.ald.asjauthlib.dialog.SupportBanksDialog;
import com.ald.asjauthlib.auth.idcard.IDCardScanFactory;
import com.ald.asjauthlib.auth.model.AuthUserBasicInfoModel;
import com.ald.asjauthlib.auth.model.BankCardModel;
import com.ald.asjauthlib.auth.model.BankCardTypeListModel;
import com.ald.asjauthlib.auth.model.IDCardInfoModel;
import com.ald.asjauthlib.dialog.OneOrTwoButtonDialog;
import com.ald.asjauthlib.auth.ui.BankCardAddActivity;
import com.ald.asjauthlib.auth.ui.BankCardAddIdActivity;
import com.ald.asjauthlib.auth.ui.CreditPromoteActivity;
import com.ald.asjauthlib.auth.ui.PayPwdSetActivity;
import com.ald.asjauthlib.auth.utils.HandlePicCallBack;
import com.ald.asjauthlib.databinding.ActivityBankCardAddIdBinding;
import com.ald.asjauthlib.utils.AppUtils;
import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.utils.ModelEnum;
import com.ald.asjauthlib.utils.Permissions;
import com.ald.asjauthlib.utils.StageJumpEnum;
import com.ald.asjauthlib.web.HTML5WebView;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.network.NetworkUtil;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.network.RequestCallBack;
import com.ald.asjauthlib.authframework.core.network.entity.ApiResponse;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.EditTextFormat;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.authframework.core.utils.PermissionCheck;
import com.ald.asjauthlib.authframework.core.utils.SPUtil;
import com.ald.asjauthlib.authframework.core.utils.UIUtils;
import com.ald.asjauthlib.authframework.core.utils.encryption.MD5Util;
import com.ald.asjauthlib.authframework.core.vm.BaseVM;

import retrofit2.Call;
import retrofit2.Response;



/*
 * Created by liangchen on 2018/4/3.
 */

public class BankCardAddIDVM extends BaseVM {

    public ObservableField<String> lblName = new ObservableField<>();
    public ObservableField<String> lblNo = new ObservableField<>();
    public ObservableField<String> displayHint = new ObservableField<>();
    public ObservableBoolean showNoticeIc = new ObservableBoolean(false);
    public ObservableBoolean isIDLayout = new ObservableBoolean(true);
    public ObservableBoolean submitEnable = new ObservableBoolean(false);
    public ObservableBoolean etNameEnable = new ObservableBoolean(true);
    public ObservableBoolean showSupportBank = new ObservableBoolean();

    private Activity activity;
    private Intent intent;
    private IDCardScanFactory cardScanFactory;
    private String txtIDNo = "";
    private String txtName = "";
    private AuthUserBasicInfoModel authUserBasicInfoModel;
    private ActivityBankCardAddIdBinding binding;
    private BankCardTypeListModel bankCardTypeListModel;
    private String bankNO;
    private BankCardModel bankCardModel;
    private OneOrTwoButtonDialog newTwoButtonDialog;


    public BankCardAddIDVM(Activity activity, ActivityBankCardAddIdBinding bankCardAddIdBinding) {
        this.activity = activity;
        this.binding = bankCardAddIdBinding;
        intent = activity.getIntent();
        txtName = intent.getStringExtra(BundleKeys.BANK_CARD_NAME);
        txtIDNo = intent.getStringExtra(BundleKeys.SETTING_PAY_ID_NUMBER);
//        binding.etBankcard.setEditViewStyle(AlaConfig.getResources().getColor(R.color.black), "请输入");
        switchLayout(false);
        showPwdLayout();
        requestSupportBanks();
        cardScanFactory = new IDCardScanFactory(activity).setCallBack(new HandlePicCallBack() {
            @Override
            public void onStart() {

            }

            @Override
            public void onHandle() {

            }

            @Override
            public void onSuccess(String[] picPath) {
                //直接上传
                cardScanFactory.submitIDCard(picPath[0]);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onIDInfoRecognized(IDCardInfoModel.CardInfo cardInfo) {
                binding.etId.setText(cardInfo.getCitizen_id());
                binding.etName.setText(cardInfo.getName());
            }

        });


    }

    /**
     * @author Yangyang
     * @desc 请求银行卡列表
     */
    private void requestSupportBanks() {
        Call<BankCardTypeListModel> call = RDClient.getService(UserApi.class).getBankList();
        NetworkUtil.showCutscenes(activity, call);
        call.enqueue(new RequestCallBack<BankCardTypeListModel>() {
            @Override
            public void onSuccess(Call<BankCardTypeListModel> call, Response<BankCardTypeListModel> response) {
                if (response.body() != null) {
                    bankCardTypeListModel = response.body();
                }
            }
        });
    }

    public EditTextFormat.EditTextFormatWatcher idWatcher = new EditTextFormat.EditTextFormatWatcher() {
        @Override
        public void OnTextWatcher(String str) {
            if (isIDLayout.get()) {
                submitEnable.set(MiscUtils.isValidID(binding.etId.getText().toString()) && binding.etName.getText().toString().trim().length() > 1);
                submitEnable.notifyChange();
            }
        }
    };

    public EditTextFormat.EditTextFormatWatcher bankCardWatcher = new EditTextFormat.EditTextFormatWatcher() {
        @Override
        public void OnTextWatcher(String str) {
            if (!isIDLayout.get()) {
                submitEnable.set(binding.etBankcard.getText().toString().length() > 15);
                submitEnable.notifyChange();
            }
        }
    };

    /**
     * 切换布局
     *
     * @param isIDLayout 是否是身份证布局
     */
    private void switchLayout(boolean isIDLayout) {
        showSupportBank.set(!isIDLayout);
        this.isIDLayout.set(isIDLayout);
        if (isIDLayout) {
            etNameEnable.set(true);
            lblName.set("姓名");
            lblNo.set("身份证");
            displayHint.set("请添加本人的身份证信息");
            showNoticeIc.set(false);
            binding.etId.setText(txtIDNo);
            binding.etName.setText(txtName);
            binding.ivBottomImg.setVisibility(View.GONE);
        } else {
            activity.setTitle("添加银行卡");
            etNameEnable.set(false);
            submitEnable.set(false);
            if (MiscUtils.isNotEmpty(binding.etName.getText().toString().trim()) && (authUserBasicInfoModel == null || !authUserBasicInfoModel.isIdCard())) {
                txtIDNo = binding.etId.getText().toString().trim();
                txtName = binding.etName.getText().toString().trim();
            }

            intent.putExtra(BundleKeys.SETTING_PAY_ID_NUMBER, txtIDNo);
            intent.putExtra(BundleKeys.BANK_CARD_NAME, txtName);
            lblName.set("持卡人");
            lblNo.set("银行卡号");
            displayHint.set("请添加本人的银行卡账号");
            showNoticeIc.set(true);
            binding.etName.setText(AppUtils.formatRealName(txtName));
            binding.etId.setText("");
        }

    }

    /**
     * 判断是否需要设置支付密码
     */
    private void showPwdLayout() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", SPUtil.getValue(Constant.USER_PHONE));
        Call<AuthUserBasicInfoModel> call = RDClient.getService(AuthApi.class).checkUserBasicInfo(jsonObject);
        call.enqueue(new RequestCallBack<AuthUserBasicInfoModel>() {
            @Override
            public void onSuccess(Call<AuthUserBasicInfoModel> call, Response<AuthUserBasicInfoModel> response) {
                authUserBasicInfoModel = response.body();
                txtIDNo = intent.getStringExtra(BundleKeys.SETTING_PAY_ID_NUMBER);
                txtName = intent.getStringExtra(BundleKeys.BANK_CARD_NAME);
                if (authUserBasicInfoModel.isIdCard()) {
                    switchLayout(false);
                } else {
                    switchLayout(true);
                }
                if (!authUserBasicInfoModel.isPayPwd()) {
                    ActivityUtils.push(PayPwdSetActivity.class, activity.getIntent(), BundleKeys.REQUEST_CODE_BANK_CARD_ADD_PWD);
                }

            }
        });
    }

    /**
     * 扫描身份证、银行卡
     */
    public void onScanClick(View view) {
        if (!PermissionCheck.getInstance().checkPermission(activity, Permissions.facePermissions)) {
            PermissionCheck.getInstance().askForPermissions(activity, Permissions.facePermissions, PermissionCheck.REQUEST_CODE_CAMERA);
        } else {
            if (checkPermission()) {
                doScan();
            }
        }
    }

    public void doScan() {
        if (isIDLayout.get()) {
            //扫描身份证
            cardScanFactory.scanFront();
        } else {
            //扫描银行卡
            Intent intent = new Intent(activity,
                    BankCardScanActivity.class);
            intent.putExtra("name", AppUtils.formatRealName(txtName));
            activity.startActivityForResult(intent, BundleKeys.REQUEST_CODE_BANKCARD_SCAN);
        }
    }

    /**
     * 持卡人tips
     */
    public void nameClick(View view) {
        NoticeImageDialog.Builder builder = new NoticeImageDialog.Builder(activity).setImage(R.drawable.bg_user_hint);
        builder.creater().show();
    }

    /**
     * 提交身份证信息跳转绑卡页面
     */
    public void onSubmitClick(View view) {
        if (isIDLayout.get()) {
            if (binding.etId.getText().toString().trim().length() < 18
                    || MiscUtils.isEmpty(binding.etName.getText().toString())) {
                UIUtils.showToast("请填写姓名和身份证号或者扫描身份证");
                return;
            }
            if (!MiscUtils.isValidID(binding.etId.getText().toString())) {
                UIUtils.showToast("身份证号不合法！");
                return;
            }

            //检查该身份证号是否已经绑定其他账号
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("idNumber", binding.etId.getText().toString().trim());
            jsonObject.put("userId", SPUtil.getValue(Constant.USER_PHONE));
            Call<ApiResponse> call = RDClient.getService(AuthApi.class).checkUserIdCardInfo(jsonObject);
            call.enqueue(new RequestCallBack<ApiResponse>() {
                @Override
                public void onSuccess(Call<ApiResponse> call, Response<ApiResponse> response) {
                    //扫描身份证页面跳转输入银行卡页
                    switchLayout(false);

                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    super.onFailure(call, t);
                    //身份证号已绑定别的账号
                    txtName = "";
                    txtIDNo = "";
                    switchLayout(true);
                }
            });


        } else {
            bankNO = binding.etBankcard.getText().toString().trim();
            if (bankNO.length() < 16 || bankNO.length() > 19) {
                UIUtils.showToast(AlaConfig.getResources().getString(R.string.bank_card_add_card_no_err_hit));
                return;
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("cardNo", bankNO);
            Call<BankCardModel> call = RDClient.getService(AuthApi.class).searchCardInfo(jsonObject);
            NetworkUtil.showCutscenes(activity, call);
            call.enqueue(new RequestCallBack<BankCardModel>() {
                @Override
                public void onSuccess(Call<BankCardModel> call, Response<BankCardModel> response) {
                    if (response.body() != null) {
                        bankCardModel = response.body();
                        if (bankCardModel != null) {
                            int cardType = (int) bankCardModel.getCardType();
                            //弹框,能识别不支持
                            if (cardType == ModelEnum.OTHER.getValue()) {
                                showNoSupportDialog(cardType, "换张卡吧，暂不支持", "抱歉，暂时不支持该银行信用卡，换张卡绑定吧");
                                //跳新绑卡页面
                            } else if (cardType == ModelEnum.CASH.getValue() || cardType == ModelEnum.CREDIT.getValue()) {
                                moToBankCardAddActivity();
                                //弹框,查看支持的信用卡列表或者跳老绑卡页面
                            } else {
                                showNoSupportDialog(cardType, "抱歉，无法获取类型", "可能您银行卡号录入错误\n" +
                                        "核对卡号：" + bankNO);
                            }
                        }
                    }
                }
            });
        }

    }


    private void showNoSupportDialog(int cardType, String title, String middle) {
        final NoSupportBankDialog noSupportBankDialog = new NoSupportBankDialog(activity);
        noSupportBankDialog.setType(cardType);
        noSupportBankDialog.setTitle(title);
        noSupportBankDialog.setMiddle(middle);
        noSupportBankDialog.setItemClickListener(new NoSupportBankDialog.ItemClickListener() {
            @Override
            public void moveToOldActivity() {
                noSupportBankDialog.dismiss();
                moToBankCardAddActivity();
            }

            @Override
            public void showSupportBanks() {
                noSupportBankDialog.dismiss();
                supportBankClick(null);
            }
        });
        noSupportBankDialog.show();
    }

    private void moToBankCardAddActivity() {
        //输入银行卡页跳转绑卡页面
        intent.putExtra(BundleKeys.SETTING_PAY_CARD_NUMBER, bankNO);
        intent.putExtra(BundleKeys.BANK_CARD_NAME, txtName);
        intent.putExtra(BundleKeys.SETTING_PAY_ID_NUMBER, txtIDNo);
        intent.putExtra(BundleKeys.BANK_CARD_INFO, bankCardModel);
        ActivityUtils.push(BankCardAddActivity.class, intent, BundleKeys.REQUEST_CODE_BANK_CARD_ADD);
    }

    /**
     * @author Yangyang
     * @desc 支持银行卡点击
     */
    public void supportBankClick(View view) {
        if (bankCardTypeListModel != null && MiscUtils.isNotEmpty(bankCardTypeListModel.getBankList())) {
            SupportBanksDialog supportBanksDialog = new SupportBanksDialog(activity, bankCardTypeListModel.getBankList());
            supportBanksDialog.show();
        }
    }

    /**
     * @author Yangyang
     * @desc 信用卡图片点击
     */
    public void onCreditImgClick(View view) {
        Intent i = new Intent();
        String url = AlaConfig.getServerProvider().getAppServer() + Constant.H5_APPLY_CREDIT_CARD;
        i.putExtra(HTML5WebView.INTENT_BASE_URL, url);
        ActivityUtils.push(HTML5WebView.class, i);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BundleKeys.REQUEST_CODE_BANK_CARD_ADD_PWD) {
            if (resultCode == Activity.RESULT_CANCELED) {
                onBackPressed(true);
            } else if (resultCode == Activity.RESULT_OK) {
                //保存密码 加密
                intent.putExtra(BundleKeys.SETTING_PAY_PWD, MD5Util.getMD5Str(data.getStringExtra(BundleKeys.SETTING_PAY_PWD)));
            }

        } else if (requestCode == BundleKeys.REQUEST_CODE_BANKCARD_SCAN) {
            if (resultCode == Activity.RESULT_OK) {
                String filePath = data.getStringExtra("filePath");
                String bankNum = data.getStringExtra("bankNum");
                String confidence = data.getStringExtra("confidence");
                binding.etBankcard.setText(bankNum);
            } else if (resultCode == Activity.RESULT_FIRST_USER) {
                //重新扫描
                onScanClick(null);
            } else if (resultCode == BankcardScanVM.RESULT_CODE_FAIL) {
                UIUtils.showToast("相机初始化失败，请尝试手动输入!");
            }
        } else if (requestCode == BundleKeys.REQUEST_CODE_STAGE_RR_IDF_FRONT)
            cardScanFactory.handleScanResult(requestCode, resultCode, data);
        else if (requestCode == BundleKeys.REQUEST_CODE_BANK_CARD_ADD && resultCode == Activity.RESULT_OK) {
            activity.setResult(Activity.RESULT_OK);
            activity.finish();
        }


    }

    /**
     * @param fromPwd 来自设置密码回退 不用再次弹窗
     */
    public void onBackPressed(boolean fromPwd) {
        if (isIDLayout.get() || (authUserBasicInfoModel != null && authUserBasicInfoModel.isIdCard())) {

            final String fromStage = intent.getStringExtra(BundleKeys.STAGE_JUMP);
            if (fromPwd) {
                gotoAuth(fromStage);
                activity.finish();
                return;
            }
            if (newTwoButtonDialog == null) {
                newTwoButtonDialog = new OneOrTwoButtonDialog(activity);
                newTwoButtonDialog.setContentMsg(AlaConfig.getResources().getString(R.string.bankcard_add_dialog_message), 20, Gravity.LEFT | Gravity.CENTER)
                        .setleftAndRightTv("确认退出", R.color.color_blue_renewal_all, "我再想想", R.color.text_color_normal)
                        .setNoTitle()
                        .setTwoItemClickListener(new OneOrTwoButtonDialog.TwoItemClickListener() {
                            @Override
                            public void onLeftClick() {
                                gotoAuth(fromStage);
                                ActivityUtils.finish(BankCardAddActivity.class);
                                ActivityUtils.finish(BankCardAddIdActivity.class);
                            }

                            @Override
                            public void onRightClick() {
                            }
                        }).show();
            } else if (!newTwoButtonDialog.isShowing()) {
                newTwoButtonDialog.show();
            }

        } else {
            switchLayout(true);
        }
    }


    /**
     * 跳转认证页面
     */
    private void gotoAuth(String fromStage) {
        if (fromStage == null || (!fromStage.equals(StageJumpEnum.STAGE_CASHIER.getModel())
                && !fromStage.equals(StageJumpEnum.STAGE_BANK_CARD.getModel()) &&
                !fromStage.equals(StageJumpEnum.STAGE_H5_BANK_CARD.getModel()) &&
                !fromStage.equals(StageJumpEnum.STAGE_SET_PAY_PWD.getModel()) &&
                !fromStage.equals(StageJumpEnum.STAGE_CREDIT_REFUND_CARD.getModel()) &&
                !fromStage.equals(StageJumpEnum.STAGE_CREDIT_REFUND_CENTER_CARD.getModel()))) {
            Intent i = new Intent();
            i.putExtra(BundleKeys.STAGE_JUMP, fromStage);
            i.putExtra(BundleKeys.INTENT_KEY_CREDIT_PROMOTE_SCENE, intent.getStringExtra(BundleKeys.INTENT_KEY_CREDIT_PROMOTE_SCENE));
            //跳转认证页面
            ActivityUtils.push(CreditPromoteActivity.class, i);
        }
    }

    //相机权限检查

    public boolean checkPermission() {

        Camera camera;
        try {
            camera = Camera.open();
            camera.startPreview();
        } catch (Exception ex) {
            PermissionCheck.getInstance().showAskDialog(activity,
                    R.string.permission_name_camera, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            return false;
        } finally {
            camera = null;
        }
        return true;
    }

}
