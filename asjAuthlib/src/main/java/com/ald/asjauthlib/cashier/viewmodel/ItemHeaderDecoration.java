package com.ald.asjauthlib.cashier.viewmodel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.cashier.model.ItemStickyEntity;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;

import java.util.List;

/**
 * Created by wjy on 2017/12/26.
 */

public class ItemHeaderDecoration extends RecyclerView.ItemDecoration {
    private int mTitleHeight;
    private List<ItemStickyEntity> mDatas;
    private LayoutInflater mInflater;
    private int resource;
//    private CheckListener mCheckListener;
//    public static String currentTag = "0";//标记当前左侧选中的position，因为有可能选中的item，右侧不能置顶，所以强制替换掉当前的tag

//    void setCheckListener(CheckListener checkListener) {
//        mCheckListener = checkListener;
//    }

    public ItemHeaderDecoration(Context context, List<ItemStickyEntity> datas, int resource) {
        this.mDatas = datas;
        Paint paint = new Paint();
        mTitleHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 44, AlaConfig.getResources().getDisplayMetrics());
        int titleFontSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 13, AlaConfig.getResources().getDisplayMetrics());
        paint.setTextSize(titleFontSize);
        paint.setAntiAlias(true);
        mInflater = LayoutInflater.from(context);
        this.resource = resource;
    }


    public ItemHeaderDecoration setData(List<ItemStickyEntity> mDatas) {
        this.mDatas = mDatas;
        return this;
    }

//    public static void setCurrentTag(String currentTag) {
//        ItemHeaderDecoration.currentTag = currentTag;
//    }


    @Override
    public void onDrawOver(Canvas canvas, final RecyclerView parent, RecyclerView.State state) {
//        GridLayoutManager manager = (GridLayoutManager) parent.getLayoutManager();
//        GridLayoutManager.SpanSizeLookup spanSizeLookup = manager.getSpanSizeLookup();
//        int spanSize = spanSizeLookup.getSpanSize(pos);
        LinearLayoutManager manager = (LinearLayoutManager) parent.getLayoutManager();
        int pos = manager.findFirstVisibleItemPosition();
        if (pos > mDatas.size()-2)
            return;
        Log.d("wang pos--->", String.valueOf(pos));
//        Log.d("wang spanSize--->", String.valueOf(spanSize));
//        int stickyTag = mDatas.get(pos).getStickyTag();
        View child = parent.findViewHolderForLayoutPosition(pos).itemView;
        if (1 == pos) Log.d("top--->1", String.valueOf(child.getTop()));
        boolean isTranslate = false;//canvas是否平移的标志
        if (mDatas.get(pos).getTag() != mDatas.get(pos + 1).getTag()) {
            int i = child.getHeight() + child.getTop();
            Log.d("i---->", String.valueOf(i));
            if (!mDatas.get(pos).isTitle()) {
                //body 才平移
                if (child.getHeight() + child.getTop() < mTitleHeight) {
                    canvas.save();
                    isTranslate = true;
                    int height = child.getHeight() + child.getTop() - mTitleHeight;
                    canvas.translate(0, height);
                }
            }


        }
        drawHeader(parent, pos, canvas);
        if (isTranslate) {
            canvas.restore();
        }
//        Log.d("tag---> ", tag + " == " + currentTag);
//        if (!TextUtils.equals(tag, currentTag)) {
//            currentTag = tag;
//            Integer integer = Integer.valueOf(tag);
//            mCheckListener.check(integer, false);
//        }
    }

    /**
     * @param parent
     * @param pos
     */
    private void drawHeader(RecyclerView parent, int pos, Canvas canvas) {
        View topTitleView = mInflater.inflate(resource, parent, false);
        TextView tvTitle = (TextView) topTitleView.findViewById(R.id.head);
        tvTitle.setText(mDatas.get(pos).getTitleName());
        //绘制title开始
        int toDrawWidthSpec; // 用于测量的widthMeasureSpec
        int toDrawHeightSpec; // 用于测量的heightMeasureSpec
        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) topTitleView.getLayoutParams();
        if (lp == null) {
            lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);//这里是根据复杂布局layout的width height，new一个Lp
            topTitleView.setLayoutParams(lp);
        }
        topTitleView.setLayoutParams(lp);
        if (lp.width == ViewGroup.LayoutParams.MATCH_PARENT) {
            //如果是MATCH_PARENT，则用父控件能分配的最大宽度和EXACTLY构建MeasureSpec
            toDrawWidthSpec = View.MeasureSpec.makeMeasureSpec(parent.getWidth() - parent.getPaddingLeft() - parent.getPaddingRight(), View.MeasureSpec.EXACTLY);
        } else if (lp.width == ViewGroup.LayoutParams.WRAP_CONTENT) {
            //如果是WRAP_CONTENT，则用父控件能分配的最大宽度和AT_MOST构建MeasureSpec
            toDrawWidthSpec = View.MeasureSpec.makeMeasureSpec(parent.getWidth() - parent.getPaddingLeft() - parent.getPaddingRight(), View.MeasureSpec.AT_MOST);
        } else {
            //否则则是具体的宽度数值，则用这个宽度和EXACTLY构建MeasureSpec
            toDrawWidthSpec = View.MeasureSpec.makeMeasureSpec(lp.width, View.MeasureSpec.EXACTLY);
        }
        //高度同理
        if (lp.height == ViewGroup.LayoutParams.MATCH_PARENT) {
            toDrawHeightSpec = View.MeasureSpec.makeMeasureSpec(parent.getHeight() - parent.getPaddingTop() - parent.getPaddingBottom(), View.MeasureSpec.EXACTLY);
        } else if (lp.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            toDrawHeightSpec = View.MeasureSpec.makeMeasureSpec(parent.getHeight() - parent.getPaddingTop() - parent.getPaddingBottom(), View.MeasureSpec.AT_MOST);
        } else {
            toDrawHeightSpec = View.MeasureSpec.makeMeasureSpec(mTitleHeight, View.MeasureSpec.EXACTLY);
        }
        //依次调用 measure,layout,draw方法，将复杂头部显示在屏幕上
        topTitleView.measure(toDrawWidthSpec, toDrawHeightSpec);
        topTitleView.layout(parent.getPaddingLeft(), parent.getPaddingTop(), parent.getPaddingLeft() + topTitleView.getMeasuredWidth(), parent.getPaddingTop() + topTitleView.getMeasuredHeight());
        topTitleView.draw(canvas);//Canvas默认在视图顶部，无需平移，直接绘制
        //绘制title结束
    }

}
