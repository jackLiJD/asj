package com.ald.asjauthlib.authframework.core.ui;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.authframework.core.vm.entity.ModelState;
import com.ald.asjauthlib.databinding.LayoutEmptyBankcardListModelBinding;


/**
 * @version 4.2.8
 * @desc 银行卡列表预加载
 * @author fanyubin
 */
public class EmptyBankcardListModelView extends LinearLayout {

    private LayoutEmptyBankcardListModelBinding binding;

    public EmptyBankcardListModelView(Context context) {
        this(context, null, 0);
    }

    public EmptyBankcardListModelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmptyBankcardListModelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.layout_empty_bankcard_list_model, this, true);
    }

    /**
     * 设置空态值
     */
    public void setAsJemptyState(ModelState state) {
        binding.setModelState(state);
    }
}
