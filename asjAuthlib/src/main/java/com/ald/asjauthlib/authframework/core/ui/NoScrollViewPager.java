package com.ald.asjauthlib.authframework.core.ui;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/*
 * Created by wjy on 2017/9/2.
 */

public class NoScrollViewPager extends ViewPager {

    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private boolean unableSliding;

    public void isCanSlidingAround(boolean isCan) {
        unableSliding = !isCan;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return !unableSliding && super.onInterceptTouchEvent(ev);
    }
}
