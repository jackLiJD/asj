package com.ald.asjauthlib.authframework.core.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;

import com.ald.asjauthlib.authframework.core.config.AlaConfig;

/**
 * 带红点的图片
 * 链接：http://www.jianshu.com/p/e500204a41b2
 * 作者：浩码农
 * 來源：简书
 */

public class TipsDrawable extends Drawable {
    private Drawable mDrawable;
    private boolean mShowRedPoint = true;
    private Paint mPaint;
    private int mRadius;
    private int mGravity = Gravity.CENTER;
    private Context context;

    public TipsDrawable(Context context, Drawable origin) {
        this.context = context;
        mDrawable = origin;// 原来的drawable
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setColor(Color.RED);
        //小红点半径
        mRadius = (int) dp2px(3);
    }

    public void setColor(int color) {
        mPaint.setColor(color);
    }

    public void setShowRedPoint(boolean showRedPoint) {
        mShowRedPoint = showRedPoint;
        invalidateSelf();
    }

    public void setRadius(int radius) {
        this.mRadius = radius;
    }

    public void setGravity(int gravity) {
        this.mGravity = gravity;
        invalidateSelf();
    }


    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {
        mDrawable.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mDrawable.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return mDrawable.getOpacity();
    }

    @Override
    public int getIntrinsicHeight() {
        return mDrawable.getIntrinsicHeight();//它的高度使用原来的高度
    }

    @Override
    public int getIntrinsicWidth() {
        return mDrawable.getIntrinsicWidth();//它的宽度使用原来的宽度
    }

    @Override
    public void setBounds(@NonNull Rect bounds) {
        super.setBounds(bounds);
        mDrawable.setBounds(bounds);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        mDrawable.draw(canvas);
        if (mShowRedPoint) {
            // 获取原图标的右上角坐标
            int cx = getBounds().right;
            int cy = getBounds().top;
            // 计算我们的小红点的坐标
            if ((Gravity.START & mGravity) == Gravity.START) {
                cx += mRadius;
            } else if ((Gravity.END & mGravity) == Gravity.END) {
                cx -= mRadius;
            }
            if ((Gravity.TOP & mGravity) == Gravity.TOP) {
                cy -= mRadius;
            } else if ((Gravity.BOTTOM & mGravity) == Gravity.BOTTOM) {
                cy += mRadius;
            }
            canvas.drawCircle(cx, cy, mRadius, mPaint);//绘制小红点
        }
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        mDrawable.setBounds(left, top, right, bottom);
    }

    /**
     * 见dp转化为px
     *
     * @param dp dp
     */
    private float dp2px(int dp) {
        return dp * AlaConfig.getResources().getDisplayMetrics().density + 0.5f;
    }

}

