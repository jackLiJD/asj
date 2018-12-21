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
import android.widget.RelativeLayout;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.authframework.core.config.AlaFragment;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.databinding.FwTopBarFragmentBinding;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.ui.AppBar;
import com.ald.asjauthlib.authframework.core.vm.TopBarVM;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/1/18 11:16
 * 描述：
 * 修订历史：
 */
public abstract  class AlaTopBarFragment<CVB extends ViewDataBinding> extends AlaFragment {
    // 布局view
    protected CVB cvb;
    protected TopBarVM topBarVM;
    // title bar
    protected AppBar mAppBar = null;
    // fragment是否显示了
    protected boolean isVisible = false;
    // 内容布局
    protected RelativeLayout container;
    private FwTopBarFragmentBinding topBarFragmentBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        topBarFragmentBinding = DataBindingUtil.setContentView(this.getActivity(), R.layout.fw__top_bar_fragment);
        topBarFragmentBinding.setTopBarVM(topBarVM);
        int layoutId = getLayoutInflate();
        if(layoutId > 0) {
            cvb = DataBindingUtil.inflate(getActivity().getLayoutInflater(), layoutId, null, false);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            cvb.getRoot().setLayoutParams(params);
            this.container = (RelativeLayout) topBarFragmentBinding.getRoot().findViewById(R.id.container);
            this.container.removeAllViews();
            this.container.addView(cvb.getRoot());
            if (null == mAppBar) {
                mAppBar = (AppBar)topBarFragmentBinding.getRoot().findViewById(R.id.appbar);
            }
            afterOnCreateView();
            isPrepared = true;
            return topBarFragmentBinding.getRoot();
        }else {
            throw new IllegalArgumentException("layout is not a inflate");
        }

    }

    /**
     * 在这里实现Fragment数据的缓加载.
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    protected void onInvisible() {
    }

    protected void onVisible() {
        setViewModel();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    protected <T extends View> T getView(int id) {
        return (T) getView().findViewById(id);
    }

    /**
     * 布局
     */
    public abstract int getLayoutInflate();

    /**
     * 设置ViewModel
     * @param
     */
    protected abstract void setViewModel();

    /**
     * UI相关初始化
     * @param
     */
    protected void afterOnCreateView(){};


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    /**
     * 为appbar设置标题 hao
     */
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
    public void setTitle(@StringRes int resId) {
        if (null != mAppBar && resId != 0) {
            mAppBar.setTitle(getString(resId));
        }
    }

    public void setTitle(@StringRes int resId, @ColorInt int color) {
        setTitle(resId);
        setTitleColor(color);
    }

    /**
     * 为appbar设置标题的颜色
     */
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
     *
     * @param resId
     * @param listener
     */
    public void setLeftImage(@DrawableRes int resId, View.OnClickListener listener) {
        mAppBar.setLeftIconOption(resId, listener);
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
        int colorBurn = MiscUtils.colorBurn(color);
        if (null != mAppBar && color != 0) {
            mAppBar.setBackgroundColor(color);
            mAppBar.setLeftParentSelector(color, colorBurn);
        }

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = getActivity().getWindow();
            window.setStatusBarColor(colorBurn);
            window.setNavigationBarColor(colorBurn);
        }
    }
}
