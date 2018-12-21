package com.ald.asjauthlib.authframework.core;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.databinding.FwTopBarActivityBinding;
import com.ald.asjauthlib.authframework.core.config.AlaActivity;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.ui.AppBar;
import com.ald.asjauthlib.authframework.core.vm.TopBarVM;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/1/5
 * 描述：
 * 修订历史：
 */
public abstract class AlaTopBarActivity<CVB extends ViewDataBinding> extends AlaActivity {
    protected FwTopBarActivityBinding topBarActivityBinding;
    protected CVB cvb;
    protected FrameLayout mainContent;
    protected ViewGroup rootView;
    protected TopBarVM topBarVM = new TopBarVM();
    // title bar
    protected AppBar mAppBar = null;

    /**
     * 设置除去topBar外的布局
     */
    protected abstract int getLayoutInflate();

    /**
     * 设置ViewModel
     */
    protected abstract void setViewModel();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        topBarActivityBinding = DataBindingUtil.setContentView(this, R.layout.fw__top_bar_activity);
        topBarActivityBinding.setTopBarVM(topBarVM);
        rootView = findViewById(R.id.root_view);
        mainContent = findViewById(R.id.main_content);
        int layoutId = getLayoutInflate();
        if (layoutId > 0) {
            cvb = DataBindingUtil.inflate(LayoutInflater.from(this), layoutId, mainContent, true);
            mainContent.removeAllViews();
            mainContent.addView(cvb.getRoot());
            if (null == mAppBar) {
                mAppBar = findViewById(R.id.appbar);
            }
            mAppBar.setLeftParentSelector(getResources().getColor(R.color.fw_btn_white_color),
                    getResources().getColor(R.color.fw_btn_white_color));
            afterOnCreate();
            setViewModel();
        } else {
            throw new IllegalArgumentException("layout is not a inflate");
        }

    }

    /**
     * UI相关操作
     */
    protected void afterOnCreate() {

    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public AppBar getAppTopBar() {
        return mAppBar;
    }

    /**
     * 为appbar设置标题 hao
     */
    @Override
    public void setTitle(CharSequence title) {
        if (null != mAppBar && !TextUtils.isEmpty(title)) {
            mAppBar.setTitle(title);
        }
    }

    public void setTitle(CharSequence title, @ColorInt int color) {
        setTitle(title);
        setTitleColor(color);
    }

    /**
     * 为appbar设置标题
     */
    @Override
    public void setTitle(@StringRes int resId) {
        if (null != mAppBar && resId != 0) {
            mAppBar.setTitle(getString(resId));
        }
    }

    public void setTitle(@StringRes int resId, @ColorInt int color) {
        setTitle(resId);
        setTitleColor(color);
    }

    public void showTopLine() {
        mAppBar.showTopLine();
    }

    /**
     * 为appbar设置标题的颜色
     */
    @Override
    public void setTitleColor(@ColorInt int color) {
        if (null != mAppBar && color != 0) {
            mAppBar.setTitleColor(color);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // left
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 设置是否显示back键
     */
    public void setBackOption(boolean option) {
        if (null != mAppBar) {
            mAppBar.setBackOption(option);
        }
    }

    /**
     * 设置左侧文字
     */
    public void setLeftText(CharSequence text, View.OnClickListener listener, @ColorInt int color) {
        if (null != mAppBar) {
            mAppBar.setLeftTextOption(text, listener);
            if (color != 0) {
                mAppBar.setLeftTextColor(color);
            }
        }
    }

    public void setLeftText(CharSequence text, View.OnClickListener listener) {
        setLeftText(text, listener, 0);
    }

    /**
     * 设置左侧文字
     */
    public void setLeftText(int resId, View.OnClickListener listener, @ColorInt int color) {
        if (null != mAppBar) {
            mAppBar.setLeftTextOption(getString(resId), listener);
            if (color != 0) {
                mAppBar.setLeftTextColor(color);
            }
        }
    }

    public void setLeftText(int resId, View.OnClickListener listener) {
        setLeftText(resId, listener, 0);
    }

    /**
     * 设置左侧返回健的图片和监听事件
     */
    public void setLeftImage(@DrawableRes int resId, View.OnClickListener listener) {
        mAppBar.setLeftIconOption(resId, listener);
    }

    /**
     * 设置左侧返回健的监听事件
     */
    public void setLeftImageListener( View.OnClickListener listener) {
        mAppBar.setLeftIconListener(listener);
    }

    /**
     * 设置左侧返回健的图片和监听事件,图片增加10的左padding
     */
    public void setLeftImageNeedPadding(@DrawableRes int resId, View.OnClickListener listener) {
        mAppBar.setLeftIconNeedPadding(resId, listener);
    }

    ///////////////////////////////////////////////////////////////////////////
    // right
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 设置右侧文字
     */
    public void setRightText(CharSequence text, View.OnClickListener listener, @ColorInt int color) {
        if (null != mAppBar) {
            mAppBar.setRightTextOption(text, listener);
            if (color != 0) {
                mAppBar.setRightTextColor(color);
            }
        }
    }

    public void removeRightText() {
        if (null != mAppBar) {
            mAppBar.removeRightText();
        }
    }

    public void setRightText(CharSequence text, View.OnClickListener listener) {
        setRightText(text, listener, 0);
    }

    /**
     * 设置右侧文字
     */
    public void setRightText(int resId, View.OnClickListener listener, @ColorInt int color) {
        if (null != mAppBar) {
            mAppBar.setRightTextOption(getString(resId), listener);
            if (color != 0) {
                mAppBar.setRightTextColor(color);
            }
        }
    }

    public void setRightText(int resId, View.OnClickListener listener) {
        setRightText(resId, listener, 0);
    }

    public void setReightImage(int resId, View.OnClickListener listener) {
        if (null != mAppBar) {
            mAppBar.setRightIconOption(resId, listener);
        }
    }

    /**
     * 设置右侧文字加图片
     */
    public void setRightTextANDImg(int resId, int imgId, View.OnClickListener listener, @ColorInt int color) {
        if (null != mAppBar) {
            Drawable drawable = ContextCompat.getDrawable(AlaConfig.getContext(), imgId);
            mAppBar.setRightTextANDImgOption(getString(resId), drawable, listener);
            if (color != 0) {
                mAppBar.setRightTextColor(color);
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // appbar
    ///////////////////////////////////////////////////////////////////////////


    /**
     * 设置appbar的颜色
     */
    public void setAppBarColor(@ColorInt int color) {
        setParentSelector(color);
    }

    /**
     * 设置appbar的背景图片
     */
    public void setAppBarDrawble(@DrawableRes int drawble) {
        if (null != mAppBar && drawble != 0) {
            mAppBar.setBackgroundResource(drawble);
        }
    }

    /**
     * 为LeftImage设置Selector
     */
    private void setParentSelector(@ColorInt int color) {
        //int colorBurn = MiscUtils.colorBurn(color);
        if (null != mAppBar && color != 0) {
            mAppBar.setBackgroundColor(color);
            mAppBar.setLeftParentSelector(color, color);
        }

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.setStatusBarColor(color);
            window.setNavigationBarColor(color);
        }
    }

    public ViewGroup getRootView() {
        return rootView;
    }

    public View getAppBarLayout(){
        return mAppBar.getAppBarLayout();
    }
}
