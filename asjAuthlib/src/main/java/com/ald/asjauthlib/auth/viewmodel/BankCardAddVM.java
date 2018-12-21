package com.ald.asjauthlib.auth.viewmodel;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.databinding.ObservableField;
import android.os.CountDownTimer;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.ald.asjauthlib.utils.Constant;
import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.AuthApi;
import com.ald.asjauthlib.dialog.NoticeImageDialog;
import com.ald.asjauthlib.dialog.TipsDialog;
import com.ald.asjauthlib.auth.model.BankCardModel;
import com.ald.asjauthlib.auth.model.BankCardTypeModel;
import com.ald.asjauthlib.auth.model.SendAddBindBankcardMsgModel;
import com.ald.asjauthlib.auth.ui.BankCardAddActivity;
import com.ald.asjauthlib.auth.ui.BankCardAddIdActivity;
import com.ald.asjauthlib.auth.ui.BankCardListActivity;
import com.ald.asjauthlib.auth.ui.BankCardTypeActivity;
import com.ald.asjauthlib.auth.ui.CreditPromoteActivity;
import com.ald.asjauthlib.auth.ui.RRIdAuthActivity;
import com.ald.asjauthlib.bindingadapter.view.ViewBindingAdapter;
import com.ald.asjauthlib.databinding.ActivityBankCardAddBinding;
import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.utils.InputCheck;
import com.ald.asjauthlib.utils.ModelEnum;
import com.ald.asjauthlib.utils.StageJumpEnum;
import com.ald.asjauthlib.web.HTML5WebView;
import com.ald.asjauthlib.widget.pickerView.builder.TimePickerBuilder;
import com.ald.asjauthlib.widget.pickerView.listener.OnTimeSelectListener;
import com.ald.asjauthlib.widget.pickerView.view.TimePickerView;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.network.NetworkUtil;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.network.RequestCallBack;
import com.ald.asjauthlib.authframework.core.network.entity.ApiResponse;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.EditTextFormat;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.authframework.core.utils.UIUtils;
import com.ald.asjauthlib.authframework.core.utils.log.Logger;
import com.ald.asjauthlib.authframework.core.vm.BaseVM;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

import retrofit2.Call;
import retrofit2.Response;


/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/1/12 14:14
 * 描述：
 * 修订历史：
 */
public class BankCardAddVM extends BaseVM {
    public final ObservableField<String> displayCardType = new ObservableField<>();
    //public final ObservableField<String> displayCardErr = new ObservableField<>();//卡号错误
    public final ObservableField<String> displayPhoneErr = new ObservableField<>();//手机错误
    public final ObservableField<String> displayAddBtnTxt = new ObservableField<>();//添加银行卡按钮文案
    private String cardNo = "";//银行卡号
    private final String stageJump;
    private String scene = "";
    private String orderId = "";
    private String realName = "";
    private String idNo = "";
    private String pwd = "";
    private BankCardModel bankCardModel;
    private final ActivityBankCardAddBinding binding;
    public final ObservableField<ViewBindingAdapter.MobileWatcher> emptyMobileWatcher = new ObservableField<>();
    private boolean isNewBindCard = true;//是否为新绑卡界面,默认是
    /**
     * 需监听的editText list
     */
    public LinkedList<EditText> edList = new LinkedList<>();
    /**
     * 需监听的CheckBox List
     */
    public LinkedList<CheckBox> cbList = new LinkedList<>();
    public ObservableField<Boolean> enable = new ObservableField<>(false);
    public ObservableField<Boolean> showNewLayout = new ObservableField<>(true);
    public ObservableField<Boolean> showCreditLayout = new ObservableField<>(false);
    /**
     * 监听EditText 变化
     */
    public EditTextFormat.EditTextFormatWatcher watcher = new EditTextFormat.EditTextFormatWatcher() {
        @Override
        public void OnTextWatcher(String str) {
            enable.set(InputCheck.checkEtAndCbList(true, edList, cbList));
            displayAddBtnTxt.set("绑  卡");
            enable.notifyChange();
        }
    };
    /**
     * 监听CheckBox变化
     */
    public EditTextFormat.CheckBoxCheckedWatcher cbWatcher = new EditTextFormat.CheckBoxCheckedWatcher() {
        @Override
        public void OnCheckedChange(boolean checked) {
            enable.set(InputCheck.checkEtAndCbList(true, edList, cbList));
            displayAddBtnTxt.set("绑  卡");
            enable.notifyChange();
        }
    };
    private Activity context;
    private BankCardTypeModel bankCardTypeModel;
    private long bankCardId = 0;
    private CountDownTimer countDownTimer;
    private boolean isCash = true;

    public BankCardAddVM(final Activity context, final ActivityBankCardAddBinding binding, View parent) {
        this.context = context;
        this.binding = binding;
        Intent intent = context.getIntent();
        this.stageJump = intent.getStringExtra(BundleKeys.STAGE_JUMP);
        this.cardNo = intent.getStringExtra(BundleKeys.SETTING_PAY_CARD_NUMBER);
        this.scene = intent.getStringExtra(BundleKeys.INTENT_KEY_CREDIT_PROMOTE_SCENE);
        this.orderId = intent.getStringExtra(BundleKeys.INTENT_KEY_CASHIER_ORDER_ID);
        this.realName = intent.getStringExtra(BundleKeys.BANK_CARD_NAME);
        this.idNo = intent.getStringExtra(BundleKeys.SETTING_PAY_ID_NUMBER);
        this.pwd = intent.getStringExtra(BundleKeys.SETTING_PAY_PWD);
        this.bankCardModel = intent.getParcelableExtra(BundleKeys.BANK_CARD_INFO);
        displayAddBtnTxt.set("绑  卡");

        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long l) {

                binding.btnCaptchaRight.setEnabled(false);
                binding.btnCaptchaRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, AlaConfig.getResources()
                        .getDimensionPixelSize(R.dimen.x24));
                binding.btnCaptchaRight.setText(l / 1000 + "s");
            }

            @Override
            public void onFinish() {
                binding.btnCaptchaRight.setEnabled(true);
                binding.btnCaptchaRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, AlaConfig.getResources()
                        .getDimensionPixelSize(R.dimen.x28));
                binding.btnCaptchaRight.setText("重新发送");
            }
        };

        binding.etPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    final String phone = binding.etPhone.getText().toString();
                    if (phone.length() < 13) {
                        displayPhoneErr.set(AlaConfig.getResources().getString(R.string.bank_card_add_card_phone_err_toast));
                        binding.txtPhoneErr.setVisibility(View.VISIBLE);
                    } else {
                        binding.txtPhoneErr.setVisibility(View.GONE);
                    }

                }
            }
        });

        //binding.txtBankNoErr.setVisibility(View.GONE);
        binding.txtPhoneErr.setVisibility(View.GONE);


        showOldOrNewLayout();
    }

    /**
     * @author Yangyang
     * @desc 通过来源显示隐藏新老界面
     */
    private void showOldOrNewLayout() {

        long cardType = bankCardModel.getCardType();
        if (cardType == ModelEnum.CASH.getValue()) {
            isCash = true;
            showNewLayout.set(true);
            showCreditLayout.set(false);
            binding.cardTvName.setText(bankCardModel.getBankName());
            binding.cardTvLast4num.setText(String.format(context.getString(R.string.last_card_num), cardNo.substring(cardNo.length() - 4, cardNo.length())));
        } else if (cardType == ModelEnum.CREDIT.getValue()) {
            isCash = false;
            showNewLayout.set(true);
            showCreditLayout.set(true);
            binding.cardTvName.setText(bankCardModel.getBankName() + AlaConfig.getResources().getString(R.string.bank_card_type2));
            binding.cardTvLast4num.setText(String.format(context.getString(R.string.last_card_num), cardNo.substring(cardNo.length() - 4, cardNo.length())));
        } else if (cardType == ModelEnum.FAILED.getValue()) {
            isNewBindCard = false;
            isCash = true;
            showNewLayout.set(false);
            //先默认选择储蓄卡
            showCreditLayout.set(false);
            //binding.tvCash.setSelected(isCash);
        }
        removeOrAddEdText();
    }

    /**
     * @author Yangyang
     * @desc 储蓄卡的时候不需要监听2个edittext
     */
    private void removeOrAddEdText() {
        if (isCash) {
            edList.remove(binding.etValidity);
            edList.remove(binding.etSafeCode);
        } else {
            edList.add(binding.etValidity);
            edList.add(binding.etSafeCode);
        }

    }


    /**
     * 点击底部绑定银行卡，校验验证码
     */
    public void saveClick(View view) {
        saveHandle();
    }

    /**
     * 校验验证码
     */
    private void saveHandle() {
        if (!isNewBindCard && bankCardTypeModel == null) {
            UIUtils.showToast(R.string.bank_card_add_card_card_type_toast);
            return;
        }
        final String phone = binding.etPhone.getText().toString();
        if (phone.length() < 13) {
            //含两个空格
            displayPhoneErr.set(AlaConfig.getResources().getString(R.string.bank_card_add_card_no_phone));
            binding.txtPhoneErr.setVisibility(View.VISIBLE);
            return;
        }
        final String captcha = binding.etCaptcha.getText().toString();
        if (MiscUtils.isEmpty(captcha)) {
            UIUtils.showToast(R.string.bank_card_add_card_no_toast);
            return;
        }
        if (!binding.cbAgreement.isChecked()) {
            UIUtils.showToast(R.string.bank_card_add_agreement_toast);
            return;
        }
//        bankCardId = 888888;//testl
        if (bankCardId == 0) {
            UIUtils.showToast(R.string.bank_card_add_card_captcha_toast);
            return;
        }

        bind(captcha);

    }

    /**
     * 绑卡
     */
    private void bind(String captcha) {
        //绑卡
        JSONObject face = new JSONObject();
        face.put("bankCardId", String.valueOf(bankCardId));
        face.put("smsCode", captcha);
        face.put("idNumber", idNo);
        face.put("realname", realName);
        face.put("newPassword", pwd);

        Call<ApiResponse> call = RDClient.getService(AuthApi.class).submitBindBankcard(face);

        NetworkUtil.showCutscenes(context, call);
        call.enqueue(new RequestCallBack<ApiResponse>() {
            @Override
            public void onSuccess(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.body() != null) {
                    Intent intent = new Intent();
                    intent.putExtra(BundleKeys.STAGE_JUMP, stageJump);
                    intent.putExtra(BundleKeys.INTENT_KEY_CREDIT_PROMOTE_SCENE, scene);
                    //来自添加银行卡
                    if (StageJumpEnum.STAGE_BANK_CARD.getModel().equals(stageJump)) {
                        ActivityUtils.popUntil(BankCardListActivity.class);
                    } else if (StageJumpEnum.STAGE_SET_PAY_PWD.getModel().equals(stageJump)) {
//                        ActivityUtils.popUntilWithoutRefresh(SettingActivity.class);
                    } else if (StageJumpEnum.STAGE_AUTH.getModel().equals(stageJump)
                            || StageJumpEnum.STAGE_LIMIT_AUTH.getModel().equals(stageJump) ||
                            StageJumpEnum.STAGE_ORAL_ACTIVITY.getModel().equals(stageJump)) {
                        finishAuthActivity();
                        ActivityUtils.push(CreditPromoteActivity.class, intent);
                    } else if (StageJumpEnum.STAGE_TRADE_SCAN.getModel().equals(stageJump)) {
                        finishAuthActivity();
                        ActivityUtils.push(CreditPromoteActivity.class, intent);
                    } else if (StageJumpEnum.STAGE_SELECT_BANK.getModel().equals(stageJump)) {
                        if (bankCardTypeModel != null) {
                            BankCardModel bankCardModel = new BankCardModel();
                            bankCardModel.setBankName(bankCardTypeModel.getBankName());
                            bankCardModel.setBankIcon(bankCardTypeModel.getBankIcon());
                            bankCardModel.setRid(Long.valueOf(bankCardTypeModel.getRid()));
                            bankCardModel.setCardNumber(cardNo);
                            intent.putExtra(BundleKeys.DIALOG_SELECT_BANK_CARD_RESULT, bankCardModel);
                        } else if (bankCardModel != null) {
                            BankCardModel bankCardModel = new BankCardModel();
                            bankCardModel.setBankName(bankCardModel.getBankName());
                            bankCardModel.setBankIcon(bankCardModel.getBankIcon());
                            bankCardModel.setRid(bankCardModel.getRid());
                            bankCardModel.setCardNumber(cardNo);
                            intent.putExtra(BundleKeys.DIALOG_SELECT_BANK_CARD_RESULT, bankCardModel);
                        }
                        ActivityUtils.pop(context, intent);
                    } else if (StageJumpEnum.STAGE_CASHIER.getModel().equals(stageJump)) {
                        //返回刷新银行卡列表
//                        EventBusUtil.sendEvent(new com.alfl.www.main.event.Event(EventCode.BIND_CARD_TO_PAYMENT, cardNo));
                        context.setResult(Activity.RESULT_OK);
                        ActivityUtils.pop(context);
                    } else if (StageJumpEnum.STAGE_H5_BANK_CARD.getModel().equals(stageJump)) {
                        ActivityUtils.popUntil(HTML5WebView.class);
                    } else if (StageJumpEnum.STAGE_CREDIT_REFUND_CARD.getModel().equals(stageJump) || StageJumpEnum.STAGE_CREDIT_REFUND_CENTER_CARD.getModel().equals(stageJump)) {
                        context.setResult(Activity.RESULT_OK);
                        context.finish();
                    } else {
                        finishAuthActivity();
                        ActivityUtils.push(CreditPromoteActivity.class, intent);
                    }

                }
            }
        });

    }

    private void finishAuthActivity() {
        ActivityUtils.finish(RRIdAuthActivity.class);
        ActivityUtils.finish(BankCardAddActivity.class);
        ActivityUtils.finish(BankCardAddIdActivity.class);
        if (ActivityUtils.peek() instanceof CreditPromoteActivity)
            ActivityUtils.pop();
    }


    /**
     * 银行卡类型选择
     */
    public void bankCardTypeClick(View view) {
        Intent intent = new Intent();
        if (bankCardTypeModel != null) {
            intent.putExtra(BundleKeys.BANK_CARD_TYPE_SELETCT, bankCardTypeModel);
        }
        ActivityUtils.push(BankCardTypeActivity.class, intent, BundleKeys.REQUEST_CODE_MINE_ADD_BANK);
    }

    /*
     * 收取短信
     */
    public void captchaClick(View view) {
        if (!isNewBindCard && bankCardTypeModel == null) {
            UIUtils.showToast(R.string.bank_card_add_card_card_type_toast);
            return;
        }
        if (MiscUtils.isEmpty(cardNo)) {
            UIUtils.showToast(R.string.bank_card_add_card_no_hit);
            return;
        }
        String phone = binding.etPhone.getText().toString();
        if (phone.length() < 13) {
            UIUtils.showToast(R.string.bank_card_add_card_phone_toast);
            return;
        }
        String validityCode = binding.etValidity.getText().toString();
        String safeCode = binding.etSafeCode.getText().toString();
        if (!isCash) {
            if (MiscUtils.isEmpty(validityCode)) {
                UIUtils.showToast(R.string.bank_card_add_card_validity_err_toast);
                return;
            }
            if (MiscUtils.isEmpty(safeCode)) {
                UIUtils.showToast(R.string.bank_card_add_card_safecode_err_toast);
                return;
            }
        }
        phone = phone.replace(" ", "");
        JSONObject face = new JSONObject();
        face.put("cardNumber", cardNo);
        face.put("mobile", phone);
        face.put("bankCode", isNewBindCard ? bankCardModel.getBankCode() : bankCardTypeModel.getBankCode());
        face.put("bankName", isNewBindCard ? bankCardModel.getBankName() : bankCardTypeModel.getBankName());
        face.put("realname", realName);
        face.put("idNumber", idNo);
        face.put("cardType", isCash ? ModelEnum.CASH.getValue() : ModelEnum.CREDIT.getValue());
        if (!isCash) {
            face.put("validDate", validityCode);
            face.put("safeCode", safeCode);
        }
        Call<SendAddBindBankcardMsgModel> call = RDClient.getService(AuthApi.class).applyBindBankcard(face);
        NetworkUtil.showCutscenes(context, call);
        call.enqueue(new RequestCallBack<SendAddBindBankcardMsgModel>() {
            @Override
            public void onSuccess(Call<SendAddBindBankcardMsgModel> call, Response<SendAddBindBankcardMsgModel> response) {
                bankCardId = response.body().getBankId();
                UIUtils.showToast(AlaConfig.getResources().getString(R.string.register_get_captcha_tip));
                countDownTimer.start();
            }

            @Override
            public void onFailure(Call<SendAddBindBankcardMsgModel> call, Throwable t) {
                super.onFailure(call, t);
                countDownTimer.onFinish();
            }
        });
    }

    /**
     * 电话tips
     */
    public void phoneClick(View view) {
        NoticeImageDialog.Builder builder = new NoticeImageDialog.Builder(context).setImage(R.drawable.bg_phone_hint);
        builder.creater().show();

    }

    /**
     * 安全码tips
     */
    public void safeCodeClick(View view) {
        NoticeImageDialog.Builder builder = new NoticeImageDialog.Builder(context).setImage(R.drawable.ic_safecode_des);
        builder.creater().show();
    }

    /**
     * @author Yangyang
     * @desc 信用卡有效期点击
     */
    public void validityClick(View view) {
        Calendar endDate = Calendar.getInstance();
        endDate.set(2100, 12, 30);
        TimePickerView pvTime = new TimePickerBuilder(context, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                String year = String.valueOf(date.getYear());
                String month = String.valueOf(date.getMonth() + 1);
                String year1 = year.substring(1);
                if (month.length() == 1) month = "0" + month;
                Logger.d("onTimeSelect", month + "/" + year1);
                binding.etValidity.setText(month + "/" + year1);
            }
        })
                .setType(new boolean[]{true, true, false, false, false, false})
                .isDialog(true)
                .setRangDate(Calendar.getInstance(), endDate)
                .build();

        Dialog mDialog = pvTime.getDialog();
        if (mDialog != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            pvTime.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
            }
        }
        pvTime.show();
    }

    /**
     * 服务协议
     */
    public void agreementClick(View view) {
        Intent i = new Intent();
        String url = AlaConfig.getServerProvider().getAppServer() + Constant.H5_URL_SERVER;
        i.putExtra(HTML5WebView.INTENT_BASE_URL, url);
        ActivityUtils.push(HTML5WebView.class, i);
    }

    /*
    * 提示dialog
    * */
    public void showTipsDialog(String title, String content) {
        TipsDialog dialog = new TipsDialog(context);
        dialog.setTitle(title);
        dialog.setContent(content);
        dialog.show();
    }

    /**
     * onActivityResult逻辑
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BundleKeys.REQUEST_CODE_MINE_ADD_BANK) {
            if (resultCode == Activity.RESULT_OK) {
                bankCardTypeModel = (BankCardTypeModel) data.getSerializableExtra(BundleKeys.BANK_CARD_RESULT);
                displayCardType.set(bankCardTypeModel.getBankName());
                isCash = bankCardTypeModel.getCardType() == ModelEnum.CASH.getValue();
                showCreditLayout.set(!isCash);
                removeOrAddEdText();
            }
        }
    }

    public void onDestroy() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}


