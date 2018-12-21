package com.ald.asjauthlib.auth.listener;

import android.support.v7.widget.RecyclerView;

/**
 * Created by ywd on 2017/11/14 01:10
 */

public interface OnStartDragListener {
    /**
     * Called when a view is requesting a start of a drag.
     *
     * @param viewHolder The holder of the view to drag.
     */
    void onStartDrag(RecyclerView.ViewHolder viewHolder);
}
