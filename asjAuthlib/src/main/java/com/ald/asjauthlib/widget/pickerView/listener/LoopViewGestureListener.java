package com.ald.asjauthlib.widget.pickerView.listener;

import android.view.MotionEvent;

import com.ald.asjauthlib.widget.pickerView.view.WheelView;


/**
 * 手势监听
 */
public final class LoopViewGestureListener extends android.view.GestureDetector.SimpleOnGestureListener {

    private final WheelView wheelView;


    public LoopViewGestureListener(WheelView wheelView) {
        this.wheelView = wheelView;
    }

    @Override
    public final boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        wheelView.scrollBy(velocityY);
        return true;
    }
}
