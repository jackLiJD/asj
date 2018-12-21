package com.ald.asjauthlib.authframework.core.ui.animation;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/2/18 21:06
 * 描述：伸缩展开动画
 * 修订历史：
 */
public class ShrinkOpen {
    /**
     * 获取上移动画
     */
    public static ObjectAnimator getUpMoveAnimator(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", 0F, -260F);
        // 设置动画持续时间
        animator.setDuration(1000);
        return animator;
    }

    /**
     * 获取下移动画
     */
    public static ObjectAnimator getDownMoveAnimator(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", -260F, 0F);
        // 设置动画持续时间
        animator.setDuration(1000);
        return animator;
    }

    /**
     * 获取展开动画
     */
    public static ScaleAnimation getOpenAnimation() {
        ScaleAnimation animator = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.0f);
        // 设置动画持续时间
        animator.setDuration(1000);
        animator.setFillAfter(true);
        return animator;
    }

    /**
     * 获取收缩动画
     */
    public static ScaleAnimation getShrinkAnimation() {
        ScaleAnimation animator = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.0f);
        // 设置动画持续时间
        animator.setDuration(1000);
        animator.setFillAfter(true);
        return animator;
    }
}
