package com.ald.asjauthlib.authframework.core.ui.animation;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/2/18 21:06
 * 描述：登录模块动画
 * 修订历史：
 */
public class ShrinkLogin {
    // 缩放图片动画
    private ScaleAnimation shrinkAnimation;
    // 属性位移动画
    private ObjectAnimator shrinkObjectAnimator;
    // 是否已经收缩
    private boolean isShrink = false;
    // 非logo的View
    private View layoutView;
    // 显示logo的View
    private View logoView;

    public ShrinkLogin(View layoutView, View logoView) {
        this.layoutView = layoutView;
        this.logoView = logoView;
    }

    /**
     * 焦点改变事件
     *
     * @param view
     *         用户名、密码输入框
     * @param hasFocus
     *         是否获得焦点
     */
    public void focusChange(View view, boolean hasFocus) {
        if ((view.isFocused() || hasFocus) && !isShrink) {
            isShrink = true;
            logoView.startAnimation(shrinkAnimation);
            shrinkObjectAnimator.start();
            openAnimation();
        }
    }

    /**
     * 界面碰触事件
     */
    public boolean layoutTouch(View view) {
        if (isShrink) {
            logoView.startAnimation(shrinkAnimation);
            shrinkObjectAnimator.start();
            shrinkAnimation();
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            isShrink = false;
        }
        return false;
    }

    /**
     * 设置收缩动画
     */
    public void shrinkAnimation() {
        shrinkAnimation = ShrinkOpen.getShrinkAnimation();
        shrinkObjectAnimator = ShrinkOpen.getUpMoveAnimator(layoutView);
    }

    /**
     * 设置展开动画
     */
    public void openAnimation() {
        shrinkAnimation = ShrinkOpen.getOpenAnimation();
        shrinkObjectAnimator = ShrinkOpen.getDownMoveAnimator(layoutView);
    }

    public void changeAnimationView(View view){
        if (isShrink()){
            openAnimation();
            logoView.startAnimation(getShrinkAnimation());
            getShrinkObjectAnimator().start();
            isShrink = false;
        }
        layoutView = view;
        shrinkAnimation();
    }

    public boolean isShrink() {
        return isShrink;
    }

    public void setShrink(boolean shrink) {
        isShrink = shrink;
    }

    public View getLayoutView() {
        return layoutView;
    }

    public void setLayoutView(View layoutView) {
        this.layoutView = layoutView;
    }

    public ObjectAnimator getShrinkObjectAnimator() {
        return shrinkObjectAnimator;
    }

    public void setShrinkObjectAnimator(ObjectAnimator shrinkObjectAnimator) {
        this.shrinkObjectAnimator = shrinkObjectAnimator;
    }

    public ScaleAnimation getShrinkAnimation() {
        return shrinkAnimation;
    }

    public void setShrinkAnimation(ScaleAnimation shrinkAnimation) {
        this.shrinkAnimation = shrinkAnimation;
    }

    public View getLogoView() {
        return logoView;
    }

    public void setLogoView(View logoView) {
        this.logoView = logoView;
    }
}
