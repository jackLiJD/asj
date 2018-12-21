package com.ald.asjauthlib.authframework.core.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.ald.asjauthlib.R;



/*
 * Created by wangjy on 2017/4/20.
 */

public class DrawableText extends AppCompatTextView {

    public static final int NONE = -1;
    public static final int DISMISS = 0;

    private DrawableListener listener;

    public static final int LEFT = 1, TOP = 2, RIGHT = 3, BOTTOM = 4;

    private int mHeight, mWidth;

    private Drawable mDrawable;

    private int mLocation;

    public DrawableText(Context context) {
        this(context, null);
    }

    public DrawableText(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public DrawableText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DrawableText);

        mWidth = a.getDimensionPixelSize(R.styleable.DrawableText_drawable_width, 0);
        mHeight = a.getDimensionPixelSize(R.styleable.DrawableText_drawable_height, 0);
        mDrawable = a.getDrawable(R.styleable.DrawableText_drawable_src);
        mLocation = a.getInt(R.styleable.DrawableText_drawable_location, LEFT);

        int rightOnClickType = a.getInt(R.styleable.DrawableText_drTvRightClick, NONE);
        if (rightOnClickType == DISMISS) {
            listener = new DismissTextView();
        }

        a.recycle();

        //绘制Drawable宽高,位置
        drawDrawable(mDrawable);
    }

    public void setRightClickListener(DrawableListener listener) {
        this.listener = listener;
    }

    public void setLeftClickListener(DrawableListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean isFocused() {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (0.0 != getAlpha()) { // 全透明的时候要此控件不启作用
            if (null != listener) {
                if (null != getCompoundDrawables()[2]) { // 右边图片点击的情况
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        int remainRight = (int) (getWidth() - event.getX());
                        if (remainRight <= getTotalPaddingRight() && remainRight >= getPaddingRight())
                            listener.onClick(this);
                    }
                } else if (null != getCompoundDrawables()[0]) { // 左边图片点击的情况
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        int remainLeft = (int) event.getX();
                        if (remainLeft <= getTotalPaddingLeft())
                            listener.onClick(this);
                    }
                }
                return true;
            }
            return super.onTouchEvent(event);
        } else {
            return false;
        }
    }

    /**
     * 绘制Drawable宽高,位置
     */
    public void drawDrawable(Drawable mdrawable) {

        if (mdrawable != null) {
            Bitmap bitmap = ((BitmapDrawable) mdrawable).getBitmap();
            Drawable drawable;
            if (mWidth != 0 && mHeight != 0) {

                drawable = new BitmapDrawable(getResources(), getBitmap(bitmap,
                        mWidth, mHeight));

            } else {
                drawable = new BitmapDrawable(getResources(),
                        Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(),
                                bitmap.getHeight(), true));
            }

            switch (mLocation) {
                case LEFT:
                    this.setCompoundDrawablesWithIntrinsicBounds(drawable, null,
                            null, null);
                    break;
                case TOP:
                    this.setCompoundDrawablesWithIntrinsicBounds(null, drawable,
                            null, null);
                    break;
                case RIGHT:
                    this.setCompoundDrawablesWithIntrinsicBounds(null, null,
                            drawable, null);
                    break;
                case BOTTOM:
                    this.setCompoundDrawablesWithIntrinsicBounds(null, null, null,
                            drawable);
                    break;
            }
        }

    }

    /**
     * 缩放图片
     *
     */
    public Bitmap getBitmap(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = (float) newWidth / width;
        float scaleHeight = (float) newHeight / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
    }

    public void setLeftCompoundDrawablesWithIntrinsicBounds(@Nullable Drawable left) {
        if (null == left) {
            super.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            return;
        }
        Bitmap bitmap = ((BitmapDrawable) left).getBitmap();
        Drawable drawable;
        if (mWidth != 0 && mHeight != 0) {
            drawable = new BitmapDrawable(getResources(), getBitmap(bitmap, mWidth, mHeight));
        } else {
            drawable = new BitmapDrawable(getResources(),
                    Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(),
                            bitmap.getHeight(), true));
        }
        super.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
    }

    public void setTopCompoundDrawablesWithIntrinsicBounds(@Nullable Drawable top, int... attrs) {
        if (null == top) {
            super.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            return;
        }
        Bitmap bitmap = ((BitmapDrawable) top).getBitmap();
        Drawable drawable;
        if (null != attrs && 2 == attrs.length) {
            drawable = new BitmapDrawable(getResources(), getBitmap(bitmap, attrs[0], attrs[1]));
        } else if (mWidth != 0 && mHeight != 0) {
            drawable = new BitmapDrawable(getResources(), getBitmap(bitmap, mWidth, mHeight));
        } else {
            drawable = new BitmapDrawable(getResources(),
                    Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(),
                            bitmap.getHeight(), true));
        }
        super.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
    }

    public void setRightCompoundDrawablesWithIntrinsicBounds(@Nullable Drawable right) {
        if (null == right) {
            super.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            return;
        }
        Bitmap bitmap = ((BitmapDrawable) right).getBitmap();
        Drawable drawable;
        if (mWidth != 0 && mHeight != 0) {
            drawable = new BitmapDrawable(getResources(), getBitmap(bitmap, mWidth, mHeight));
        } else {
            drawable = new BitmapDrawable(getResources(),
                    Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(),
                            bitmap.getHeight(), true));
        }
        super.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
    }

    public void setBottomCompoundDrawablesWithIntrinsicBounds(@Nullable Drawable bottom) {
        if (null == bottom) {
            super.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            return;
        }
        Bitmap bitmap = ((BitmapDrawable) bottom).getBitmap();
        Drawable drawable;
        if (mWidth != 0 && mHeight != 0) {
            drawable = new BitmapDrawable(getResources(), getBitmap(bitmap, mWidth, mHeight));
        } else {
            drawable = new BitmapDrawable(getResources(),
                    Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(),
                            bitmap.getHeight(), true));
        }
        super.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);
    }

    public interface DrawableListener {
        void onClick(View v);
    }

    private class DismissTextView implements DrawableListener {

        @Override
        public void onClick(View v) {
            v.setVisibility(GONE);
        }
    }
}
