package com.ald.asjauthlib.authframework.core.ui;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.authframework.core.vm.entity.ModelState;
import com.ald.asjauthlib.databinding.FwModelLayoutBinding;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/1/4
 * 描述：页面前置加载
 * 修订历史：
 */
public class ModelView extends LinearLayout {
    private FwModelLayoutBinding binding;

    public ModelView(Context context) {
        this(context, null, 0);
    }

    public ModelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ModelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.fw__model_layout, this, true);
    }

    /**
     * 设置空态值
     */
    public void setAsJemptyState(ModelState state) {
        binding.setModelState(state);
    }
}
