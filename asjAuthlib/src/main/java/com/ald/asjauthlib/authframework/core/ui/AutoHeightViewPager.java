package com.ald.asjauthlib.authframework.core.ui;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.scwang.smartrefresh.layout.util.DensityUtil;

/**
 * Created by wjy on 2018/7/13.
 *
 */

public class AutoHeightViewPager extends ViewPager {

    private int defaultHeight; // 左右滑动的情况下选择延迟加载数据时setUserVisibleHint能被调用的前提是高度不为0,所以设置为显示页面的高度,既让setUserVisibleHint被调用,又不会突兀
    private final int uiHeight = DensityUtil.dp2px(206.5f);
    private int defaultSelectIndex; // 默认取下标为0的页面的高度

    public AutoHeightViewPager(Context context) {
        super(context);
    }

    public AutoHeightViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        if (getChildCount() > getCurrentItem()) {
            View child = getChildAt(getCurrentItem());
            child.measure(widthMeasureSpec,
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if (h > height)
                height = h;
            if (defaultSelectIndex == getCurrentItem())
                defaultHeight = height;
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height < uiHeight ? defaultHeight : height,
                MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setDefaultSelectIndex(int defaultSelectIndex) {
        this.defaultSelectIndex = defaultSelectIndex;
    }
}
