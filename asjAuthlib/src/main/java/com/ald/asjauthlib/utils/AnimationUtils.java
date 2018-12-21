package com.ald.asjauthlib.utils;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

/**
 * Created by luckyliang on 2018/1/4.
 */

public class AnimationUtils {

    public enum AnimationState {
        STATE_SHOW,
        STATE_HIDDEN
    }

    public abstract static class AnimationListenerAdapter implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    /**
     * @param view
     * @param state    show/hide
     * @param duration
     */
    public static void showAndHiddenAnimation(final View view, AnimationState state, long duration) {
        float start = 0f;
        float end = 0f;
        if (state == AnimationState.STATE_SHOW) {
            end = 1f;
            view.setVisibility(View.VISIBLE);
        } else if (state == AnimationState.STATE_HIDDEN) {
            start = 1f;
            view.setVisibility(View.INVISIBLE);
        }
        AlphaAnimation animation = new AlphaAnimation(start, end);
        animation.setDuration(duration);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
            }
        });
        view.setAnimation(animation);
        animation.start();
    }

    public static void scaleYAnimation(final View view, AnimationState state, long duration, AnimationListenerAdapter listener) {
        float start = 0f;
        float end = 0f;
        if (state == AnimationState.STATE_SHOW) {
            end = 1f;
        } else if (state == AnimationState.STATE_HIDDEN) {
            start = 1f;
        }
        ScaleAnimation animation = new ScaleAnimation(1, 1, start, end, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
        animation.setDuration(duration);
        animation.setAnimationListener(listener);
        view.startAnimation(animation);
    }

    public static final int VIEW_HEIGHT = 0;
    public static final int VIEW_MARGIN_TOP = 1;

    /**
     * @param which 对view的哪一个属性做动画
     */
    public static ValueAnimator doValueAnimator(View view, int which, long duration, int... values) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(values);
        valueAnimator.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            switch (which) {
                case VIEW_HEIGHT:
                    ViewGroup.LayoutParams params = view.getLayoutParams();
                    params.height = value;
                    view.requestLayout();
                    break;
                case VIEW_MARGIN_TOP:
                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) view.getLayoutParams();
                    lp.topMargin = value;
                    view.requestLayout();
                    break;

            }
        });
        valueAnimator.setDuration(duration);
        return valueAnimator;
    }


    /**
     * 抖动动画
     */
    public static void startShakeAnim(View view, float scaleSmall, float scaleLarge, float shakeDegrees, long duration) {
        if (view == null) {
            return;
        }

        //先变小后变大
        PropertyValuesHolder scaleXValuesHolder = PropertyValuesHolder.ofKeyframe(View.SCALE_X,
                Keyframe.ofFloat(0f, 1.0f),
                Keyframe.ofFloat(0.25f, scaleSmall),
                Keyframe.ofFloat(0.5f, scaleLarge),
                Keyframe.ofFloat(0.75f, scaleLarge),
                Keyframe.ofFloat(1.0f, 1.0f)
        );
        PropertyValuesHolder scaleYValuesHolder = PropertyValuesHolder.ofKeyframe(View.SCALE_Y,
                Keyframe.ofFloat(0f, 1.0f),
                Keyframe.ofFloat(0.25f, scaleSmall),
                Keyframe.ofFloat(0.5f, scaleLarge),
                Keyframe.ofFloat(0.75f, scaleLarge),
                Keyframe.ofFloat(1.0f, 1.0f)
        );

        //先往左再往右
        PropertyValuesHolder rotateValuesHolder = PropertyValuesHolder.ofKeyframe(View.ROTATION,
                Keyframe.ofFloat(0f, 0f),
                Keyframe.ofFloat(0.1f, -shakeDegrees),
                Keyframe.ofFloat(0.2f, shakeDegrees),
                Keyframe.ofFloat(0.3f, -shakeDegrees),
                Keyframe.ofFloat(0.4f, shakeDegrees),
                Keyframe.ofFloat(0.5f, -shakeDegrees),
                Keyframe.ofFloat(0.6f, shakeDegrees),
                Keyframe.ofFloat(0.7f, -shakeDegrees),
                Keyframe.ofFloat(0.8f, shakeDegrees),
                Keyframe.ofFloat(0.9f, -shakeDegrees),
                Keyframe.ofFloat(1.0f, 0f)
        );

        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, scaleXValuesHolder, scaleYValuesHolder, rotateValuesHolder);
        objectAnimator.setDuration(duration);
        objectAnimator.start();
    }

}
