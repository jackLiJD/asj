package com.ald.asjauthlib.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.TextView;

import com.ald.asjauthlib.R;


/**
 * Created by sean on 2017/3/8.
 */
public class CreditPromoteDialog extends Dialog implements View.OnClickListener {

    private String title;
    private String content;
    private String sureText;
    private String cancelText;

    private MakeSureListener listener;

    private TextView tvTitle;
    private TextView tvContent;
    private TextView tvCancel;
    private TextView tvSure;

    public CreditPromoteDialog(@NonNull Context context) {
        this(context, R.style.creditPromoteDialog);
    }

    public CreditPromoteDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        initView();
    }

    private void initView() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_credit_promote, null);
        tvTitle = (TextView) dialogView.findViewById(R.id.tv_title);
        tvContent = (TextView) dialogView.findViewById(R.id.tv_content);
        tvCancel = (TextView) dialogView.findViewById(R.id.tv_cancel);
        tvSure = (TextView) dialogView.findViewById(R.id.tv_sure);


        tvCancel.setOnClickListener(this);
        tvSure.setOnClickListener(this);

        tvContent.setText(content);
        setContentView(dialogView);
    }

    public void setTitle(String title) {
        this.title = title;
        if (tvTitle != null) {
            tvTitle.setText(title);
        }
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

    public void setCancelText(String cancelText) {
        this.cancelText = cancelText;
        if (tvCancel != null) {
            tvCancel.setText(cancelText);
        }
    }


    /**
     * 确定按钮显示规则
     *
     * @param visible
     */
    public void setSureBtnVisible(int visible) {
        if (tvSure != null) {
            tvSure.setVisibility(visible);
        }
    }

    public void setListener(MakeSureListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.tv_cancel) {
            dismiss();

        } else if (i == R.id.tv_sure) {
            if (listener != null)
                listener.onSureClick(this, view);

        }
    }

    public interface MakeSureListener {
        void onSureClick(Dialog dialog, View view);
    }
}
