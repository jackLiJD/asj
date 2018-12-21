package com.ald.asjauthlib.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;

/**
 * Created by Yangyang on 2018/5/16.
 * desc:
 */

public class PayEditText extends View {

    private float width;
    private int measuredHeight;
    private int count;
    private Paint linePaint = new Paint();
    private Paint circlePaint = new Paint();
    private OnInputFinishedListener onInputFinishedListener;
    private StringBuilder mPassword;
    private Context context;
    private float radis;

    public PayEditText(Context context) {
        this(context, null);
    }

    public PayEditText(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PayEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        setBackgroundResource(R.drawable.shape_pay_pwd_input);
        linePaint.setColor(0xFF232323);
        linePaint.setStrokeWidth(dip2px(context,0.5f));
        circlePaint.setColor(Color.parseColor("#ffd500"));
        mPassword = new StringBuilder();
        radis=dip2px(context,6);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        int measuredWidth = getMeasuredWidth();
        measuredHeight = getMeasuredHeight();
        width = (float) (measuredWidth / 6.0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 1; i < 6; i++) {
            canvas.drawLine(width * i, 0, width * i, measuredHeight, linePaint);
        }
        for (int i = 0; i < count; i++) {
            canvas.drawCircle(width/2+width*i,measuredHeight/2,radis,circlePaint);
        }
    }

    public static int dip2px(Context context, float dpValue) {
        float scale = AlaConfig.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    /**
     * 输入密码
     *
     * @param value
     */
    public void add(String value) {
        if (mPassword != null && mPassword.length() < 6) {
            mPassword.append(value);
            count=mPassword.length();
            invalidate();
            //六个密码都输入完成时回调
            if (onInputFinishedListener != null&&count==6) {
                onInputFinishedListener.onInputFinished(mPassword.toString());
            }
        }
    }

    /**
     * 删除密码
     */
    public void remove() {
        if (mPassword != null && mPassword.length() > 0) {
            mPassword.deleteCharAt(mPassword.length() - 1);
            count=mPassword.length();
            invalidate();
        }
    }


    /**
     * 返回输入的内容
     *
     * @return 返回输入内容
     */
    public String getText() {
        return (mPassword == null) ? null : mPassword.toString();
    }

    /**
     * 当密码输入完成时的回调接口
     */
    public interface OnInputFinishedListener {
        void onInputFinished(String password);
    }

    /**
     * 对外开放的方法
     *
     * @param onInputFinishedListener
     */
    public void setOnInputFinishedListener(OnInputFinishedListener onInputFinishedListener) {
        this.onInputFinishedListener = onInputFinishedListener;
    }

    /**
     * 清空全部输入
     */
    public void clearAll() {
        count=0;
        mPassword = new StringBuilder();
        invalidate();
    }
}
