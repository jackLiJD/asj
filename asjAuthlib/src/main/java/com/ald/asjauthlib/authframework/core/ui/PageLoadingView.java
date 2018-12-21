package com.ald.asjauthlib.authframework.core.ui;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.authframework.core.vm.entity.EmptyState;
import com.ald.asjauthlib.databinding.PageLoadingLayoutBinding;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/1/4
 * 描述：页面前置加载
 * 修订历史：
 */
public class PageLoadingView extends LinearLayout {
    private PageLoadingLayoutBinding binding;

    public PageLoadingView(Context context) {
        this(context, null, 0);
    }

    public PageLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageLoadingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.page_loading_layout, this, true);
    }

    /**
     * 设置空态值
     */
    public void setEmptyState(EmptyState state) {
        binding.setState(state);
    }
}
