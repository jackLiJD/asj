package com.ald.asjauthlib.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.ald.asjauthlib.utils.Constant;
import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.model.LoginModel;
import com.ald.asjauthlib.utils.AppUtils;
import com.ald.asjauthlib.web.HTML5WebView;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.info.SharedInfo;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.DataUtils;

import java.math.BigDecimal;

/*
 * Created by liangchen on 2018/3/1.
 */

public class AgreementDialog extends Dialog {
    AgreementDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder {

        AgreementDialog dialog;
        Context context;
        int nPer;
        String payAmount;
        BigDecimal poundageAmount;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setPer(int nper) {
            this.nPer = nper;
            return this;
        }

        public Builder setPayAmount(String payAmount) {
            this.payAmount = payAmount;
            return this;
        }

        public Builder setPoundageAmount(BigDecimal poundageAmount) {
            this.poundageAmount = poundageAmount;
            return this;
        }

        public AgreementDialog create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            dialog = new AgreementDialog(context, R.style.CommonDialog);
            View layout = inflater.inflate(R.layout.dialog_cashier_agreement, null);

            layout.findViewById(R.id.txt_agreement_first).setOnClickListener(v -> {
                //支付协议
                Intent i = new Intent();
                String url = AlaConfig.getServerProvider().getAppServer() + Constant.H5_URL_STEP_BUY;
                i.putExtra(HTML5WebView.INTENT_BASE_URL, url);
                ActivityUtils.push(HTML5WebView.class, i);
                dialog.dismiss();
            });

            layout.findViewById(R.id.txt_agreement_second).setOnClickListener(v -> {
                //分期服务协议
                Intent i = new Intent();
                String url = AlaConfig.getServerProvider().getAppServer() + Constant.H5_URL_FENQI_SERVER_V2;
                LoginModel loginModel = SharedInfo.getInstance().getValue(LoginModel.class);
                if (loginModel != null && loginModel.getUser() != null) {
                    String userName = loginModel.getUser().getUserName();
                    url = String.format(url, userName, nPer, payAmount, AppUtils.formatAmount(poundageAmount), "");
                }
                i.putExtra(HTML5WebView.INTENT_BASE_URL, url);
                ActivityUtils.push(HTML5WebView.class, i);
                dialog.dismiss();

            });

            layout.findViewById(R.id.txt_agreement_third).setOnClickListener(v -> {
                Intent intent = new Intent();
                LoginModel loginModel = SharedInfo.getInstance().getValue(LoginModel.class);
                String url = AlaConfig.getServerProvider().getAppServer() + Constant.H5_URL_DIGITAL_CERTIFICATE;
                if (loginModel != null && loginModel.getUser() != null) {
                    String userName = loginModel.getUser().getUserName();
                    url = String.format(url, userName);
                }
                intent.putExtra(HTML5WebView.INTENT_BASE_URL, url);
                ActivityUtils.push(HTML5WebView.class, intent);
                dialog.dismiss();
            });

            layout.findViewById(R.id.txt_agreement_forth).setOnClickListener(v -> {
                Intent intent = new Intent();
                String url = AlaConfig.getServerProvider().getAppServer() + Constant.H5_URL_RISK_PROMPT;
                intent.putExtra(HTML5WebView.INTENT_BASE_URL, url);
                ActivityUtils.push(HTML5WebView.class, intent);
                dialog.dismiss();
            });

            layout.findViewById(R.id.btn_close).setOnClickListener(v -> dialog.dismiss());
            layout.findViewById(R.id.txt_agreement_fifth).setOnClickListener(v -> {
                Intent intent = new Intent();

                LoginModel loginModel = SharedInfo.getInstance().getValue(LoginModel.class);
                String url = "";
                if (loginModel != null && loginModel.getUser() != null) {
                    String userName = loginModel.getUser().getUserName();
                    url = String.format(Constant.H5_URL_PLATFORM_AGREEMENT, userName, nPer, payAmount, poundageAmount).trim();
                }
                url = AlaConfig.getServerProvider().getAppServer() + url;
                intent.putExtra(HTML5WebView.INTENT_BASE_URL, url);
                ActivityUtils.push(HTML5WebView.class, intent);
                dialog.dismiss();
            });

            Window window = dialog.getWindow();
            dialog.setCancelable(true);
            dialog.setContentView(layout);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.gravity = Gravity.CENTER;
            lp.width = DataUtils.getCurrentDisplayMetrics().widthPixels * 3 / 4;
            return dialog;
        }
    }
}
