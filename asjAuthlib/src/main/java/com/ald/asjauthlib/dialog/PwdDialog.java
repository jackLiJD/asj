package com.ald.asjauthlib.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.model.LoginModel;
import com.ald.asjauthlib.auth.ui.PwdPayCaptchaActivity;
import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.utils.StageJumpEnum;
import com.ald.asjauthlib.widget.Keyboard;
import com.ald.asjauthlib.widget.PayEditText;
import com.ald.asjauthlib.authframework.core.info.SharedInfo;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.DataUtils;
import com.ald.asjauthlib.authframework.core.utils.DensityUtils;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/3/2 11:13
 * 描述：底部输入密码Dialog
 * 修订历史：
 */
public class PwdDialog extends Dialog {
    private final static int DIALOG_MAX_DP_HEIGHT = 438;

    protected PwdDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private String title;
        private onFinishListener onFinishListener;
        private String stage;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setStage(String stage) {
            this.stage = stage;
            return this;
        }

        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }


        public Builder setOnFinishListener(onFinishListener onFinishListener) {
            this.onFinishListener = onFinishListener;
            return this;
        }

        public PwdDialog create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final PwdDialog dialog = new PwdDialog(context, R.style.BottomSelectDialog);
            View layout = inflater.inflate(R.layout.layout_bottom_pwd_dialog, null);
            TextView titleView =  layout.findViewById(R.id.tv_title);

            if (MiscUtils.isNotEmpty(title)) {
                titleView.setText(title);
            }
            layout.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            layout.findViewById(R.id.tv_forget).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    LoginModel loginModel = SharedInfo.getInstance().getValue(LoginModel.class);
                    if (loginModel != null && loginModel.getUser() != null) {
                        String mobile = loginModel.getUser().getMobile();
                        intent.putExtra(BundleKeys.SETTING_PAY_PHONE, mobile);
                    }
                    if (MiscUtils.isEmpty(stage))
                        intent.putExtra(BundleKeys.STAGE_JUMP, StageJumpEnum.STAGE_FORGET_PWD.getModel());
                    else
                        intent.putExtra(BundleKeys.STAGE_JUMP, stage);

                    ActivityUtils.push(PwdPayCaptchaActivity.class, intent);
                    dialog.dismiss();
                }
            });
            final PayEditText pwd =  layout.findViewById(R.id.pwd);
            Keyboard keyboard =  layout.findViewById(R.id.keyboard);
            keyboard.setOnClickKeyboardListener(new Keyboard.OnClickKeyboardListener() {
                @Override
                public void onKeyClick(int position, String value) {
                    if (position < 11 && position != 9) {
                        pwd.add(value);
                    } else if (position == 9) {
//                        pwd.remove();
                    } else if (position == 11) {
                        pwd.remove();
                        //当点击完成的时候,pwd.getText()获取密码，此时不应该注册OnInputFinishedListener接口
                        /*String payPwd = pwd.getText().toString();
                        if (MiscUtils.isNotEmpty(payPwd) && payPwd.length() == 6) {
                            if (onFinishListener != null) {
                                onFinishListener.onDone(payPwd);
                                dialog.dismiss();
                            }
                        } else {
                            UIUtils.showToast("请输入正确的密码");
                        }*/
                    }
                }
            });

            /*
             * 当密码输入完成时的回调
             */
            pwd.setOnInputFinishedListener(new PayEditText.OnInputFinishedListener() {
                @Override
                public void onInputFinished(String password) {
                    if (onFinishListener != null) {
                        onFinishListener.onDone(pwd.getText().toString());
                        dialog.dismiss();
                    }
                }
            });
            Window window = dialog.getWindow();
            window.setWindowAnimations(R.style.Animation_BottomSelect);
            dialog.setContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    DensityUtils.getPxByDip(DIALOG_MAX_DP_HEIGHT)));
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.gravity = Gravity.BOTTOM;
            lp.width = DataUtils.getCurrentDisplayMetrics().widthPixels;
            lp.dimAmount = 0.5f;
            return dialog;
        }

    }

    @Override
    public void show() {
        super.show();
    }

    public interface onFinishListener {
        void onDone(String text);
    }


}
