package com.ald.asjauthlib.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.authframework.core.utils.DataUtils;

/**
 * Created by ywd on 2017/11/21.
 */

public class WithholdTipDialog extends Dialog {
    public WithholdTipDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder {

        Context mContext;
        WithholdTipDialog dialog;
        String mContent;
        View.OnClickListener mSureOnclickListener;
        TextView tvContent;
        TextView tvSure;
        TextView tvCancel;

        public WithholdTipDialog.Builder setContent(String content) {
            mContent = content;
            return this;
        }

        public WithholdTipDialog.Builder setSureListener(View.OnClickListener onClickListener) {
            mSureOnclickListener = onClickListener;
            return this;
        }

        public Builder(Context context) {
            mContext = context;
        }

        public WithholdTipDialog create() {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            dialog = new WithholdTipDialog(mContext, R.style.CommonDialog);
            View layout = inflater.inflate(R.layout.dialog_withhold_tip, null);
            tvContent = (TextView) layout.findViewById(R.id.tv_content);
            tvContent.setText(mContent);
            tvSure = (TextView) layout.findViewById(R.id.tv_sure);
            tvSure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            Window window = dialog.getWindow();
            dialog.setCancelable(false);
            dialog.setContentView(layout);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.gravity = Gravity.CENTER;
            lp.width = DataUtils.getCurrentDisplayMetrics().widthPixels;
            return dialog;
        }

    }
}
