package com.ald.asjauthlib.authframework.core.ui;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ald.asjauthlib.R;


/**
 * Created by yangfeng01 on 2017/10/24.
 */

public class GridSpaceItemDecoration extends RecyclerView.ItemDecoration {

	private int spanCount;
	private int spacing;
	private boolean includeEdge;

	public GridSpaceItemDecoration(int spanCount, int spacing, boolean includeEdge) {
		this.spanCount = spanCount;
		this.spacing = spacing;
		this.includeEdge = includeEdge;
	}

	@Override
	public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
		int position = parent.getChildAdapterPosition(view); // item position
		int childCount = parent.getAdapter().getItemCount();
		int itemPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
		int column = position % spanCount; // item column

		if (includeEdge) {
			outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
			outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

			if (position < spanCount) { // top edge
				outRect.top = 0;    //spacing;
			}
			//boolean isLastRaw = isLastRaw(parent, itemPosition, spanCount, childCount);
			boolean isLastRaw = isLastRaw(parent, position, spanCount, childCount);
			if (isLastRaw) {
				try {
					outRect.bottom = parent.getContext().getResources().getDimensionPixelOffset(R.dimen.y10);
				} catch (Exception e) {
					e.printStackTrace();
					outRect.bottom = 20;
				}
			} else {
				outRect.bottom = 0;        // spacing; // item bottom
			}
		} else {
			outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
			outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /
			// spanCount) * spacing)
			if (position >= spanCount) {
				outRect.top = spacing; // item top
			}
		}
	}

	private boolean isLastRaw(RecyclerView parent, int pos, int spanCount,
							  int childCount) {
		RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
		if (layoutManager instanceof GridLayoutManager) {
			if (pos + spanCount >= childCount)
				return true;
		}
		return false;
	}

}
