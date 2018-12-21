package com.ald.asjauthlib.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ald.asjauthlib.R;


/**
 * 带有取消按钮的Dialog
 * Created by ywd on 2017/11/17.
 */

public class WithCancelDialog extends Dialog implements View.OnClickListener {
    private final Context context;
    private TextView tvTitle;
    private TextView tvCancel;
    private TextView tvSure;
    private TextView tv_content;
    private OnBtnClickListener onBtnClickListener;

    public interface OnBtnClickListener{
        void onCancelClick();
        void onSureClick();
    }

    public WithCancelDialog(Context context, int theme, OnBtnClickListener onBtnClickListener) {
        super(context, theme);
        this.context = context;
        this.onBtnClickListener=onBtnClickListener;
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_tips_with_cancel, null);
        tvCancel = (TextView) v.findViewById(R.id.tv_cancel);
        tvSure = (TextView) v.findViewById(R.id.tv_sure);
        tv_content = (TextView) v.findViewById(R.id.tv_content);
        tvTitle = (TextView) v.findViewById(R.id.tv_title);
        setContentView(v);
        setOnClick();
    }

    public void setOnClick() {
        tvSure.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
    }

    public void setContent(String content) {
        tv_content.setText(content);
    }

    public void setTvTitle(String name) {
        tvTitle.setText(name);
    }

    @Override
    public void onClick(View v) {
        if(onBtnClickListener!=null){
            if(v == tvSure){
                onBtnClickListener.onSureClick();
            }else {
                onBtnClickListener.onCancelClick();
            }
            dismiss();
        }
    }
}
