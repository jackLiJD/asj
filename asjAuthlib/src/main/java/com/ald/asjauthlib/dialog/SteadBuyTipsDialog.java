package com.ald.asjauthlib.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ald.asjauthlib.R;


/**
 * Created by sean yu on 2017/6/1.
 */

public class SteadBuyTipsDialog extends Dialog implements View.OnClickListener {

    private TextView tvContent;
    private Button tvSure;

    private String content;
    private String sureText;

    private ISureListener listener;

    public SteadBuyTipsDialog(@NonNull Context context) {
        this(context, R.style.tipsDialog);
    }

    public SteadBuyTipsDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        initView();
    }

    private void initView() {
        View view = getLayoutInflater().inflate(R.layout.dialog_stead_buy_tips, null);
        tvContent = (TextView) view.findViewById(R.id.tv_stead_buy_dialog_content);
        tvSure = (Button) view.findViewById(R.id.btn_stead_buy_dialog_sure);

        tvContent.setOnClickListener(this);
        tvSure.setOnClickListener(this);
        view.findViewById(R.id.img_stead_buy_dialog_close).setOnClickListener(this);
        setContentView(view);
    }

    public void setContent(String content) {
        this.content = content;
        if (tvContent != null) {
            tvContent.setText(content);
        }
    }

    public void setSureText(String sureText) {
        this.sureText = sureText;
        if (tvSure != null) {
            tvSure.setText(sureText);
        }
    }

    public void setListener(ISureListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_stead_buy_dialog_sure) {
            if (listener != null) {
                listener.onSureClick(this);
            }
            dismiss();

        } else if (i == R.id.img_stead_buy_dialog_close) {
            dismiss();

        }
    }

    public interface ISureListener {
        void onSureClick(Dialog dialog);
    }
}
