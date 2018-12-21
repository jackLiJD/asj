package com.ald.asjauthlib.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.authframework.core.ui.NoDoubleClickButton;

import java.lang.ref.SoftReference;

/**
 * 其他设备登录弹窗
 * Created by yaowenda on 2017/9/2.
 */

public class KickLoginDialog extends Dialog implements View.OnClickListener {
    private ImageView ivClose;
    private TextView tvContent;
    private NoDoubleClickButton btnSure;

    private MakeSureListener listener;

    private SoftReference<Context> ctxSoft;

    public KickLoginDialog(@NonNull Context context) {
        this(context, R.style.tipsDialog);
    }

    public KickLoginDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        ctxSoft = new SoftReference<>(context);
        initView();
    }

    private void initView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.dialog_kick_login, null);

        ivClose = (ImageView) v.findViewById(R.id.iv_close);
        tvContent = (TextView) v.findViewById(R.id.tv_content);
        btnSure = (NoDoubleClickButton) v.findViewById(R.id.btn_sure);
        setContentView(v);
        //
        //WindowManager manager = getWindow().getWindowManager();
        //WindowManager.LayoutParams lp = getWindow().getAttributes();
        //lp.gravity = Gravity.CENTER;
        //getWindow().setAttributes(lp);

        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);

        ivClose.setOnClickListener(this);
        btnSure.setOnClickListener(this);
    }

    public Context getMyContext() {
        return ctxSoft.get();
    }

    /*
     * 设置提示框内容
     */
    public void setContent(String tips) {
        if (tvContent != null) {
            tvContent.setText(tips);
        }
    }

    public void setContent(int resId) {
        if (0 != resId) {
            tvContent.setText(resId);
        }
    }

    public interface MakeSureListener {
        void onSureClick(Dialog dialog, View view);
    }

    public void setListener(MakeSureListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.iv_close) {
            if (listener != null) {
                listener.onSureClick(this, view);
            }
            dismiss();

        } else if (i == R.id.btn_sure) {
            if (listener != null) {
                listener.onSureClick(this, view);
            }
            dismiss();

        }

    }
}
