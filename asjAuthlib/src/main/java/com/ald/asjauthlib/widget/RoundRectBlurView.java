package com.ald.asjauthlib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.ald.asjauthlib.R;

/**
 * 带阴影的圆角矩形
 * Created by ywd on 2018/1/3.
 */

public class RoundRectBlurView extends View {
    private Paint paint;
    private float rxHornRadius;//x方向的圆角半径
    private float ryHornRadius;//y方向的圆角半径
    private int shadowColor;//控件阴影颜色
    private final float DEFAULT_HORN_RADIUS_RX = 12;//x方向的默认圆角半径
    private final float DEFAULT_HORN_RADIUS_RY = 12;//y方向的默认圆角半径
    private final int DEFAULT_SHADOW_COLOR = Color.rgb(235, 235, 235);//默认阴影颜色


    public RoundRectBlurView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public RoundRectBlurView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public RoundRectBlurView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    /**
     * 初始化
     */
    private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundRectBlurView, defStyleAttr, 0);
        rxHornRadius = a.getDimension(R.styleable.RoundRectBlurView_hornRadiusRx, DEFAULT_HORN_RADIUS_RX);
        ryHornRadius = a.getDimension(R.styleable.RoundRectBlurView_hornRadiusRy, DEFAULT_HORN_RADIUS_RY);
        shadowColor = a.getColor(R.styleable.RoundRectBlurView_shadowColor, DEFAULT_SHADOW_COLOR);
        a.recycle();
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        paint = new Paint();
        paint.setColor(shadowColor);
        paint.setMaskFilter(new BlurMaskFilter(15, BlurMaskFilter.Blur.OUTER));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawRoundRect(15, 15, getMeasuredWidth() - 15, getMeasuredHeight() - 15, rxHornRadius, ryHornRadius, paint);
        }
    }
}
