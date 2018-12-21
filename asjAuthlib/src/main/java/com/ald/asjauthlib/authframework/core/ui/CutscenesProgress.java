package com.ald.asjauthlib.authframework.core.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ald.asjauthlib.R;
//import com.ald.asjauthlib.authframework.core.GlideApp;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/1/6
 * 描述：网络请求等待动画
 * 修订历史：
 */
public class CutscenesProgress extends Dialog {

    public CutscenesProgress(Context context) {
        this(context, R.style.dialog, false);
    }

    public CutscenesProgress(Context context, boolean isTrans) {
        this(context, R.style.dialog, isTrans);
    }

    private CutscenesProgress(Context context, int theme, boolean isTrans) {
        super(context, theme);
        init(isTrans);
    }

    private void init(boolean isTrans) {
        setContentView(R.layout.cutscenes_progress_layout);
        final ImageView imageView = findViewById(R.id.loadingImageView);
        FrameLayout fl = findViewById(R.id.fl);
        Window window = getWindow();
        if (null != window) {
            if (!isTrans) {
                fl.setBackgroundColor(fl.getContext().getResources().getColor(R.color.white)); // 如有背景需要颜色的可在这自由配置颜色
                WindowManager.LayoutParams params = window.getAttributes();
                params.height = MiscUtils.getAppRect(window).height();
                params.width = ViewGroup.MarginLayoutParams.MATCH_PARENT;
                params.gravity = Gravity.CENTER;
//            setOnDismissListener(new OnDismissListener() {
//                @Override
//                public void onDismiss(DialogInterface dialog) {
//                    if (imageView != null) {
//                        Glide.clear(imageView);
//                    }
//                }
//            });
            }
        } else {
            fl.setBackgroundColor(fl.getContext().getResources().getColor(R.color.transparent));
        }
        setCanceledOnTouchOutside(false);
        //如下设置不会造成一直gc
        Glide.with(AlaConfig.getContext())
                .load(R.drawable.fw__ic_loading)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .into(imageView);
//                .into(new GlideDrawableImageViewTarget(imageView, GlideDrawable.LOOP_INTRINSIC));
    }

    /**
     * 标题
     */
    public CutscenesProgress setTitle(String strTitle) {
        super.setTitle(strTitle);
        return this;
    }

    /**
     * 提示内容
     */
    public CutscenesProgress setMessage(String strMessage) {
        TextView tvMsg = findViewById(R.id.loadingMsg);
        if (tvMsg != null) {
            tvMsg.setVisibility(View.VISIBLE);
            tvMsg.setText(strMessage);
        }
        return this;
    }

    @Override
    public void show() {
        try {
            if (getContext() instanceof Activity) {
                if (!((Activity) getContext()).isFinishing()) {
                    super.show();
                }
            } else {
                super.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dialogDismiss() {
        callDismiss();
//        if (Looper.getMainLooper() == Looper.myLooper()) {
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    callDismiss();
//                }
//            }, 400);
//        } else {
//            try {
//                Thread.sleep(400);
//                callDismiss();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    }

    private void callDismiss() {
        try {
            if (getContext() instanceof Activity) {
                if (!((Activity) getContext()).isFinishing()) {
                    dismiss();
                }
            } else {
                dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
