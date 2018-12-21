package com.ald.asjauthlib.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.ald.asjauthlib.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;

/**
 * 首页弹出框
 * Created by sean yu on 2017/5/17.
 */
public class ActivityDialog extends AlertDialog implements View.OnClickListener {

    private ImageView imgContent;
    private String imageResource;

    private onImageClickListener listener;

    public ActivityDialog(@NonNull Context context) {
        this(context, R.style.baseDialog);
    }

    public ActivityDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_view);
        imgContent = findViewById(R.id.iv_activity_dialog_content);

        imgContent.setOnClickListener(this);
        findViewById(R.id.iv_activity_dialog_close).setOnClickListener(this);
        if (MiscUtils.isNotEmpty(imageResource)) {
            Glide.with(AlaConfig.getContext()).load(imageResource).diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .transition(DrawableTransitionOptions.withCrossFade(1000))
                    .into(imgContent);
        }
        setDialogLayout();
    }

    /**
     * 重新配置弹窗尺寸
     */
    private void setDialogLayout() {
        Window window = getWindow();
        if (window != null) {
            window.getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(lp);
        }
    }


    /**
     * 设置资源文件
     */
    public void setImageResource(String imageResource) {
        this.imageResource = imageResource;
        if (imgContent != null && MiscUtils.isNotEmpty(imageResource)) {
            Glide.with(AlaConfig.getContext()).load(imageResource).diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .transition(DrawableTransitionOptions.withCrossFade(1000))
                    .into(imgContent);
        }
    }

    public void setImageResource(@DrawableRes int resId) {
        if (null == imgContent) return;
        imgContent.setImageResource(resId);
    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        super.setCanceledOnTouchOutside(cancel);
        if (cancel)
            findViewById(R.id.rl).setOnClickListener(v -> dismiss());
    }

    /**
     * 设置图片点击监听
     *
     * @param listener
     */
    public void setListener(onImageClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_activity_dialog_close) {
            if (listener != null) {
                listener.onClose(this);
            }

        } else if (i == R.id.iv_activity_dialog_content) {
            if (listener != null) {
                listener.onImageClick(this);
            }

        }
    }


    public interface onImageClickListener {
        void onImageClick(Dialog dialog);

        void onClose(Dialog dialog);
    }
}
