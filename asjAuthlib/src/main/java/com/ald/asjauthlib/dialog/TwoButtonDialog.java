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
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;

/*
 * Created by luckyliang on 2017/11/3.
 */

public class TwoButtonDialog extends Dialog {
    TwoButtonDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder {

        Context mContext;
        TwoButtonDialog dialog;
        String titleTxt;
        String mContent;
        String sureTxt;
        String cancelTxt;
        String mCancelTxt;
        String mSureTxt;
        View.OnClickListener mSureOnclickListener;
        TextView tvTitle;
        View.OnClickListener mCancelOnclickListener;
        TextView tvContent;
        TextView tvSure;
        TextView tvCancel;
        boolean cancelable = false;

        public Builder setTitleTxt(String txt) {
            titleTxt = txt;
            return this;
        }

        public Builder setContent(String content) {
            mContent = content;
            return this;
        }

        public Builder setSureTxt(String txt) {
            mSureTxt = txt;
            return this;
        }

        public Builder setCancelTxt(String txt) {
            mCancelTxt = txt;
            return this;
        }

        public Builder setSureListener(View.OnClickListener onClickListener) {
            mSureOnclickListener = onClickListener;
            return this;
        }

        public Builder setCancelListener(View.OnClickListener onClickListener) {
            mCancelOnclickListener = onClickListener;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;

        }


        public Builder(Context context) {
            mContext = context;
        }

        public TwoButtonDialog getDialog() {
            return dialog;
        }

        public TwoButtonDialog create() {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            dialog = new TwoButtonDialog(mContext, R.style.CommonDialog);
            View layout = inflater.inflate(R.layout.dialog_two_button, null);

            tvTitle = layout.findViewById(R.id.tv_title);
            if (!MiscUtils.isEmpty(titleTxt)) {
                tvTitle.setText(titleTxt);
            }

            tvContent = layout.findViewById(R.id.tv_content);
            tvContent = layout.findViewById(R.id.tv_content);
            tvContent.setText(mContent);
            tvSure = layout.findViewById(R.id.tv_sure);
            if (!MiscUtils.isEmpty(mSureTxt))
                tvSure.setText(mSureTxt);
            tvSure.setOnClickListener(mSureOnclickListener);
            if (!MiscUtils.isEmpty(sureTxt)) {
                tvSure.setText(sureTxt);
            }

            tvCancel = layout.findViewById(R.id.tv_cancel);
            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            if (!MiscUtils.isEmpty(cancelTxt)) {
                tvCancel.setText(cancelTxt);
            }
            if (!MiscUtils.isEmpty(mCancelTxt))
                tvCancel.setText(mCancelTxt);
            if (mCancelOnclickListener == null)
                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            else {
                tvCancel.setOnClickListener(mCancelOnclickListener);
            }

            Window window = dialog.getWindow();
            if (window != null) {
                dialog.setCancelable(cancelable);
                dialog.setContentView(layout);
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.gravity = Gravity.CENTER;
                lp.width = (int) (DataUtils.getCurrentDisplayMetrics().widthPixels * 0.8);
            }
            return dialog;
        }

    }
}
