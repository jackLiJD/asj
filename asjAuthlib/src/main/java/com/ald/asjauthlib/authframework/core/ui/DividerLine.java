package com.ald.asjauthlib.authframework.core.ui;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/1/4
 * 描述：分割线装饰
 * 修订历史：
 */
public class DividerLine extends RecyclerView.ItemDecoration {
    /**
     * 默认(不显示分割线)
     */
    public static final int DEFAULT = -1;
    /**
     * 水平方向
     */
    public static final int HORIZONTAL = LinearLayoutManager.HORIZONTAL;
    /**
     * 水平方向(设计统一默认分割线)
     */
    public static final int HORIZONTAL_DEFAULT = 3;
    /**
     * 垂直方向
     */
    public static final int VERTICAL = LinearLayoutManager.VERTICAL;
    /**
     * 网格布局:水平方向和垂直方向分割线
     */
    public static final int GRID = 2;
    // 画笔
    private Paint paint;
    // 布局方向
    private int orientation;
    // 分割线颜色
    private int color = ContextCompat.getColor(AlaConfig.getContext(), R.color.fw_default_divider_color);
    // 分割线尺寸
    private int size = 1;
    // 左间距
    private int marginLeft = 0;
    // 右间距
    private int marginRight = 0;

    public DividerLine() {
        this(HORIZONTAL);
    }

    public DividerLine(int orientation) {
        this.orientation = orientation;
        paint = new Paint();
        paint.setColor(color);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        if (orientation == VERTICAL) {
            drawVertical(c, parent);
        } else if (orientation == HORIZONTAL) {
            drawHorizontal(c, parent);
        } else if (orientation == GRID) {
            drawGrid(c, parent);
        }
    }

    /**
     * 设置分割线颜色
     *
     * @param color 颜色
     */
    public void setColor(int color) {
        this.color = color;
        paint.setColor(color);
    }

    /**
     * 设置分割线颜色和大小
     *
     * @param color 颜色
     */
    public void setColorAndSize(int color, float size) {
        this.color = color;
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(size);
    }

    /**
     * 设置分割线尺寸
     *
     * @param size 尺寸
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * 设置分割线的左间距
     *
     * @param marginLeft 尺寸
     */
    public void setMarginLeft(int marginLeft) {
        this.marginLeft = marginLeft;
    }

    /**
     * 设置分割线的右间距
     *
     * @param marginRight 尺寸
     */
    public void setMarginRight(int marginRight) {
        this.marginRight = marginRight;
    }

    // 绘制垂直分割线
    protected void drawVertical(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + size;
            c.drawRect(left + marginLeft, top, right - marginRight, bottom, paint);
        }
    }

    // 绘制水平分割线
    protected void drawHorizontal(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + size;

            c.drawRect(left + marginLeft, top, right - marginRight, bottom, paint);
        }
    }

    private void drawGrid(Canvas c, RecyclerView parent) {
//        int spanCount = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
        final int childCount = parent.getChildCount();
        for (int i = 0; i <= childCount - 1; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            if (0 == i % 2) {
                int verticalLeft = child.getRight() + params.rightMargin;
                int verticalRight = verticalLeft + size;
                int verticalTop = child.getTop() - params.topMargin;
                int verticalBottom = child.getBottom() + params.bottomMargin;
                c.drawRect(verticalLeft, verticalTop, verticalRight, verticalBottom, paint);
            }

            if (i >= 0 && i < childCount - 2) {
                int horizontalLeft = child.getLeft() - params.leftMargin;
                int horizontalRight = child.getRight() + params.rightMargin + size;
                int horizontalTop = child.getBottom() + params.bottomMargin;
                int horizontalBottom = horizontalTop + size;
                c.drawRect(horizontalLeft, horizontalTop, horizontalRight, horizontalBottom, paint);
            }
        }

    }
}
