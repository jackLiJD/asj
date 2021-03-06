package com.ald.asjauthlib.authframework.core.ui;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.authframework.core.info.ErrorInfo;
import com.ald.asjauthlib.authframework.core.utils.CanDoubleClick;
import com.ald.asjauthlib.authframework.core.utils.DensityUtils;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/1/3
 * 描述： appbar控件
 * 修订历史：
 */
public class AppBar extends LinearLayout {
    private RelativeLayout mLayout;
    // 左侧
    private RelativeLayout mLeftParent;
    private TextView mLeftText;
    private ImageView mLeftImage;
    // title
    private TextView mTitleText;
    // 右侧
    private FrameLayout mRightParent;
    private TextView mRightText;
    private ImageView mRightImage;
    // error
    private TextView mError;

    private View mTopLine;
    private Context context;

    public AppBar(Context context) {
        this(context, null);
    }

    public AppBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AppBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.appbar, this, true);

        mLayout = findViewById(R.id.appbar_bg);
        // 左侧
        mLeftParent = findViewById(R.id.left_parent);
        mLeftText = findViewById(R.id.appbar_left_text);
        mLeftImage = findViewById(R.id.appbar_left);

        // title
        mTitleText = findViewById(R.id.appbar_title);

        // 右侧
        mRightParent = findViewById(R.id.right_parent);
        mRightText = findViewById(R.id.appbar_right_text);
        mRightImage = findViewById(R.id.appbar_right);

        // error
        mError = findViewById(R.id.appbar_error);
        if (TextUtils.isEmpty(ErrorInfo.getInstance().error.get())) mError.setVisibility(View.GONE);
        else {
            mError.setText(ErrorInfo.getInstance().error.get());
            mError.setVisibility(View.VISIBLE);
        }
        setBackOption(true);

        mTopLine = findViewById(R.id.top_line);
    }


    /**
     * 设置title
     */
    public AppBar setTitle(CharSequence title) {
        mTitleText.setText(title);
        return this;
    }

    public AppBar showTopLine() {
        mTopLine.setVisibility(VISIBLE);
        return this;
    }

    /**
     * 设置title
     */
    public AppBar setTitle(@StringRes int resId) {
        mTitleText.setText(getResources().getString(resId));
        return this;
    }

    /**
     * 设置title颜色
     */
    public AppBar setTitleColor(@ColorInt int color) {
        mTitleText.setTextColor(color);
        return this;
    }

    /**
     * 设置title大小
     */
    public AppBar setTitleSize(int unit, float size) {
        mTitleText.setTextSize(unit, size);
        return this;
    }

    ///////////////////////////////////////////////////////////////////////////
    // left
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 设置back键显示
     */
    public void setBackOption(boolean option) {
        if (option) {
            mLeftImage.setVisibility(View.VISIBLE);
            mLeftParent.setOnClickListener(new OnClickListener() {
                @CanDoubleClick
                @Override
                public void onClick(View v) {
                    ((Activity) getContext()).onBackPressed();
                }
            });
        } else {
            mLeftParent.setOnClickListener(null);
            mLeftImage.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 设置左边icon的显示图片和点击事件
     */
    public void setLeftIconOption(@DrawableRes int imgResID, OnClickListener listener) {
        mLeftImage.setImageResource(imgResID);
        mLeftImage.setVisibility(View.VISIBLE);
        mLeftText.setVisibility(View.GONE);
        if (null != listener) {
            mLeftParent.setOnClickListener(listener);
        }
    }

    /**
     * 设置左边icon的点击事件
     */
    public void setLeftIconListener( OnClickListener listener) {
        mLeftImage.setVisibility(View.VISIBLE);
        mLeftText.setVisibility(View.GONE);
        if (null != listener) {
            mLeftParent.setOnClickListener(listener);
        }
    }

    /**
     * 设置左边icon的显示图片和点击事件,并给icon设置padding
     */
    public void setLeftIconNeedPadding(@DrawableRes int imgResID, OnClickListener listener) {
        mLeftImage.setImageResource(imgResID);
        mLeftImage.setPadding(DensityUtils.dp2px(context,10),0,0,0);
        mLeftImage.setVisibility(View.VISIBLE);
        mLeftText.setVisibility(View.GONE);
        if (null != listener) {
            mLeftParent.setOnClickListener(listener);
        }
    }

    /**
     * 设置左边文字的显示文字和点击事件
     */
    public void setLeftTextOption(CharSequence text, OnClickListener listener) {
        mLeftText.setText(text);
        mLeftText.setVisibility(View.VISIBLE);
        mLeftImage.setVisibility(View.GONE);
        if (null != listener) {
            mLeftParent.setOnClickListener(listener);
        }
    }

    /**
     * 设置左边文字字体颜色
     */
    public void setLeftTextColor(@ColorInt int color) {
        mLeftText.setTextColor(color);
    }

    ///////////////////////////////////////////////////////////////////////////
    // right
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 设置右边icon的显示图片和点击事件
     */
    public void setRightIconOption(@DrawableRes int imgResID, OnClickListener listener) {
        mRightImage.setImageResource(imgResID);
        mRightImage.setVisibility(View.VISIBLE);
        mRightText.setVisibility(View.GONE);
        if (null != listener) {
            mRightParent.setOnClickListener(listener);
        }
    }

    /**
     * 设置右边文字的显示文字和点击事件
     */
    public AppBar setRightTextOption(CharSequence text, OnClickListener listener) {
        mRightText.setText(text);
        mRightText.setVisibility(View.VISIBLE);
        mRightImage.setVisibility(View.GONE);
        if (null != listener) {
            mRightParent.setOnClickListener(listener);
        }
        return this;
    }

    public AppBar setRightTextSize(int unit, float size) {
        mRightText.setTextSize(unit, size);
        return this;
    }

    /**
     * 设置右边文字的显示文字加图片和点击事件
     */
    public void setRightTextANDImgOption(CharSequence text, Drawable drawable, OnClickListener listener) {
        mRightText.setText(text);
        mRightText.setTextSize(12);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());//必须设置图片大小，否则不显示
        mRightText.setCompoundDrawables(null, null, drawable, null);
        mRightText.setCompoundDrawablePadding(3);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(0, 0, 20, 0);//4个参数按顺序分别是左上右下
        mRightText.setLayoutParams(layoutParams);
        mRightText.setGravity(Gravity.CENTER);
        mRightText.setVisibility(View.VISIBLE);
        mRightImage.setVisibility(View.GONE);
        if (null != listener) {
            mRightParent.setOnClickListener(listener);
        }
    }

    /**
     * 隐藏右边文字和点击事件
     */
    public void removeRightText() {
        mRightText.setVisibility(View.GONE);
    }

    /**
     * 设置右边文字字体颜色
     */
    public void setRightTextColor(@ColorInt int color) {
        mRightText.setTextColor(color);
    }

    ///////////////////////////////////////////////////////////////////////////
    // appbar
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 设置appbar背景色
     */
    public void setBackgroundColor(@ColorInt int color) {
        mLayout.setBackgroundColor(color);
    }

    /**
     * 设置error提示信息
     */
    public void setError(String error) {
        mError.setText(error);
        if (TextUtils.isEmpty(error)) {
            mError.setVisibility(View.GONE);
        } else {
            mError.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 为back键设置selector
     *
     * @param color     normal
     * @param colorBurn press
     */
    public void setLeftParentSelector(@ColorInt int color, @ColorInt int colorBurn) {
        if (color != 0 && colorBurn != 0) {
            // 初始化一个空对象
            StateListDrawable listDrawable = new StateListDrawable();

            ColorDrawable drawable = new ColorDrawable(color);
            ColorDrawable drawableBurn = new ColorDrawable(colorBurn);

            // 获取对应的属性值 Android框架自带的属性 attr
            int pressed = android.R.attr.state_pressed;
            int window_focused = android.R.attr.state_window_focused;
            int focused = android.R.attr.state_focused;
            int selected = android.R.attr.state_selected;

            listDrawable.addState(new int[]{pressed, window_focused}, drawableBurn);
            listDrawable.addState(new int[]{pressed, -focused}, drawableBurn);
            listDrawable.addState(new int[]{selected}, drawableBurn);
            listDrawable.addState(new int[]{focused}, drawableBurn);
            // 没有任何状态时显示的图片，我们给它设置为空集合
            listDrawable.addState(new int[]{}, drawable);
            mLeftParent.setBackgroundDrawable(listDrawable);
        }
    }

    public View getAppBarLayout(){
        return mLayout;
    }
}
