package com.ald.asjauthlib.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;

/**
 * Created by Yangyang on 2018/4/12.
 * desc:
 */

public class CoverScrollView extends NestedScrollView {
    public CoverScrollView(@NonNull Context context) {
        super(context);
    }

    public CoverScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private ScrollViewListener scrollViewListener;

    public void setonScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }
    public interface ScrollViewListener {

        void onScrollChanged(int l, int t, int oldl, int oldt);

    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (scrollViewListener != null)
            scrollViewListener.onScrollChanged(l, t, oldl, oldt);
    }

   /* private final static int INVALID_ID = -1;
    private int mActivePointerId = INVALID_ID;
    private float mPrimaryLastY = -1;
    private boolean isAtTop = false;//scrollview是否在顶部
    private boolean canScroll = true;//动画状态中能否滑动
    private boolean isMoving = false;//是否在下拉中
//    private NestedScrollingParentHelper mParentHelper;
//    private RecyclerView rv;
//    private int boundary;

    public CoverScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        mParentHelper = new NestedScrollingParentHelper(this);
    }

    public CoverScrollView(Context context) {
        super(context);
    }

    private ScrollViewListener scrollViewListener;

    public void setonScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    public interface ScrollViewListener {

        void onPullDown(float moveY);

        void onHandUp();

        void onScrollChanged(int l, int t, int oldl, int oldt);

    }

//    @Override
//    protected void onFinishInflate() {
//        super.onFinishInflate();
//        rv=findViewById(R.id.guessBody);
//    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPrimaryLastY = event.getY();
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!canScroll)
            return true;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPrimaryLastY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //如果到顶部了重新记录一次down事件
                if (isAtTop) {
                    mPrimaryLastY = event.getY();
                    isAtTop = false;
                }
                float y = event.getY() - mPrimaryLastY;
                if (y > 0 && scrollViewListener != null) {
                    //到顶部开始下拉
                    if (getScrollY() == 0) {
                        scrollViewListener.onPullDown(y);
                        isMoving = true;
                        //滑动中如果回拉,scrollview不滑动
                    } else if (isMoving) {
                        scrollViewListener.onPullDown(y);
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (scrollViewListener != null) {
                    scrollViewListener.onHandUp();
                    isMoving = false;
                }

                break;
        }

        return super.onTouchEvent(event);
    }


    public void setCantScroll(boolean canScroll) {
        this.canScroll = canScroll;
    }

    private boolean canNestedScroll = true;

    public void setNestedParentScroll(boolean canNestedScroll) {
        this.canNestedScroll = canNestedScroll;
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed,
                                        int dyUnconsumed, int[] offsetInWindow, int type) {
        return super.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, canNestedScroll || (0 != getScrollY() && 0 == getChildAt(0).getHeight() - getScrollY() - getHeight()) ? dyUnconsumed : 0,
                offsetInWindow, type);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        isAtTop = t == 0;
        if (scrollViewListener != null)
            scrollViewListener.onScrollChanged(l, t, oldl, oldt);
    }*/

/*
    // ================================  滑动事件交互接口实现  =====================================
    //在此可以判断参数target是哪一个子view以及滚动的方向，然后决定是否要配合其进行嵌套滚动
    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return R.id.guessBody == target.getId();
    }


    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        mParentHelper.onNestedScrollAccepted(child, target, nestedScrollAxes);
    }

    @Override
    public void onStopNestedScroll(View target) {
        mParentHelper.onStopNestedScroll(target);
    }

    private boolean isSelfScroll;

    // 先于child滚动
    // 前3个为输入参数，最后一个是输出参数
    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        if ((pullDown(dy) || pullUp(dy))) { // 自己(parent)滚动条件
            isSelfScroll = true;
            scrollBy(0, dy); // 滚动
            consumed[1] = dy; // 告诉child我消费了多少
        } else {
            isSelfScroll = false;
        }
    }

    //后于child滚动
    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {

    }

    //返回值：是否消费了fling
    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        if (isSelfScroll) {
            fling((int) velocityY);
            return true;
        } else {
            return false;
        }
    }

    //    返回值：是否消费了fling
    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    @Override
    public int getNestedScrollAxes() {
        return mParentHelper.getNestedScrollAxes();
    }

    public boolean pullDown(int dy) {
        if (dy < 0) {
            if (getScrollY() > 0 && !rv.canScrollVertically(-1)) {
                return true;
            }
        }
        return false;
    }

    public void setBoundary(int boundary) {
        this.boundary = boundary;
    }

    public boolean pullUp(int dy) {
        if (dy > 0) {
            if (getScrollY() < boundary) {
                return true;
            }
        }
        return false;
    }*/
}
