package com.ald.asjauthlib.authframework.core.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;

/**
 * Created by wjy on 2017/8/14.
 */

public class MyLinearLayout extends LinearLayout {

    private Paint p;
    private Path mPath;
    private Point startPoint, endPoint, assistPoint;
    private float gap;
    private RectF rectF;

    public MyLinearLayout(Context context) {
        this(context, null);
    }

    public MyLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        p = new Paint();
        p.setColor(0xffff6e43);
        p.setAntiAlias(true);
        p.setDither(true);

        mPath = new Path();
        startPoint = new Point();
        endPoint = new Point();
        assistPoint = new Point();

        rectF = new RectF();

        gap = AlaConfig.getResources().getDimension(R.dimen.y30);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        rectF.set(0, 0, w, h - gap);

        startPoint.set(0, (int) (h - gap));
        endPoint.set(w, (int) (h - gap));
        assistPoint.set(w / 2, (int) (h + gap));
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        mPath.moveTo(startPoint.x, startPoint.y);
        mPath.quadTo(assistPoint.x, assistPoint.y, endPoint.x, endPoint.y);
        canvas.drawPath(mPath, p);
        canvas.drawPoint(assistPoint.x, assistPoint.y, p);
        canvas.drawRect(rectF, p);
        super.dispatchDraw(canvas);
    }
}
