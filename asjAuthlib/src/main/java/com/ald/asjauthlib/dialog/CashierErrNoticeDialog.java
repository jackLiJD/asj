package com.ald.asjauthlib.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.model.LoginModel;
import com.ald.asjauthlib.auth.ui.PwdPayCaptchaActivity;
import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.utils.StageJumpEnum;
import com.ald.asjauthlib.authframework.core.info.SharedInfo;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.DataUtils;


/*
 * Created by liangchen on 2018/3/7.
 */

public class CashierErrNoticeDialog extends Dialog {


    public static class Builder {
        Context mContext;
        String msg = "";
        int mType = 0;//type=1 优惠券notice type=2 微信支付补偿
        String mTitle;
        SpannableString span;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setMsg(String msg) {
            this.msg = msg;
            return this;
        }

        public Builder setSpan(SpannableString span) {
            this.span = span;
            return this;
        }

        public Builder setType(int type) {
            mType = type;
            return this;
        }

        public String getmTitle() {
            return mTitle;
        }

        public Builder setmTitle(String mTitle) {
            this.mTitle = mTitle;
            return this;
        }

        public CashierErrNoticeDialog creater() {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final CashierErrNoticeDialog dialog = new CashierErrNoticeDialog(mContext, R.style.Translucent_NoTitle);
            View layout = inflater.inflate(mType == 1 || mType == 2 ? R.layout.dialog_trolley_notice : R.layout.dialog_cashier_notice, null);
            TextView txtMsg = layout.findViewById(R.id.txt_msg);
            if (mType == 2) {
                txtMsg.setText(span);
            } else {
                txtMsg.setText(msg);
            }
            if (mType == 1) {
                TextView ivClose = layout.findViewById(R.id.tv_close);
                ivClose.setOnClickListener(v -> dialog.dismiss());
                TextView tv_Title = layout.findViewById(R.id.txt_title);
                tv_Title.setText(mTitle);

            } else if (mType == 2) {
                TextView ivClose = layout.findViewById(R.id.iv_close);
                ivClose.setVisibility(View.GONE);
                TextView tvClose = layout.findViewById(R.id.tv_close);
                tvClose.setOnClickListener(v -> dialog.dismiss());
                TextView tv_Title = layout.findViewById(R.id.txt_title);
                tv_Title.setVisibility(View.GONE);
            } else {
                ImageView ivClose = layout.findViewById(R.id.iv_close);
                ivClose.setOnClickListener(v -> dialog.dismiss());
            }
            dialog.setContentView(layout);
            return dialog;
        }


        public CashierErrNoticeDialog twoBtnCreater() {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final CashierErrNoticeDialog dialog = new CashierErrNoticeDialog(mContext, R.style.Translucent_NoTitle);
            View layout = inflater.inflate(R.layout.dialog_cashier_two_btn, null);
            TextView txtMsg = layout.findViewById(R.id.txt_msg);
            txtMsg.setText(msg);
            TextView ivClose = layout.findViewById(R.id.iv_close);
            ivClose.setOnClickListener(v -> dialog.dismiss());
            TextView btnPwdForget = layout.findViewById(R.id.txt_pwd_forget);
            btnPwdForget.setOnClickListener(v -> {
                //忘记密码
                Intent intent = new Intent();
                LoginModel loginModel = SharedInfo.getInstance().getValue(LoginModel.class);
                if (loginModel != null && loginModel.getUser() != null) {
                    String mobile = loginModel.getUser().getMobile();
                    intent.putExtra(BundleKeys.SETTING_PAY_PHONE, mobile);
                }
                intent.putExtra(BundleKeys.STAGE_JUMP, StageJumpEnum.STAGE_CASHIER.getModel());
                ActivityUtils.push(PwdPayCaptchaActivity.class, intent);
                dialog.dismiss();

            });
            dialog.setCancelable(false);
            dialog.setContentView(layout);
            Window window = dialog.getWindow();
            if (window != null) {
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.gravity = Gravity.CENTER;
                lp.width = (int) (DataUtils.getCurrentDisplayMetrics().widthPixels * 0.8);
                window.setAttributes(lp);
            }
            return dialog;
        }

    }

    public CashierErrNoticeDialog(Context context, int theme) {
        super(context, theme);

    }
}
