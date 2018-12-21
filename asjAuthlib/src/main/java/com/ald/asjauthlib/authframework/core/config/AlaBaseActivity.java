package com.ald.asjauthlib.authframework.core.config;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.databinding.FwActivityBaseBinding;
import com.ald.asjauthlib.authframework.core.vm.BaseVM;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/1/5
 * 描述：
 * 修订历史：
 */
public abstract class AlaBaseActivity<CVB extends ViewDataBinding> extends AlaActivity {
    protected FwActivityBaseBinding fwActivityBaseBinding;
    protected CVB cvb;
    protected FrameLayout mainContent;
    protected ViewGroup rootView;
    protected BaseVM baseVM;

    /**
     * 设置除去topBar外的布局
     */
    protected abstract int getLayoutInflate();

    /**
     * 设置ViewModel
     *
     * @param contentBinding
     */
    protected abstract void setViewModel(CVB contentBinding);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fwActivityBaseBinding = DataBindingUtil.setContentView(this, R.layout.fw__activity_base);
        fwActivityBaseBinding.setBaseVM(baseVM);

        rootView = (ViewGroup) findViewById(R.id.root_view);
        mainContent = (FrameLayout) findViewById(R.id.main_content);
        int layoutId = getLayoutInflate();
        if (layoutId > 0) {
            cvb = DataBindingUtil.inflate(LayoutInflater.from(this), layoutId, mainContent, true);
            mainContent.removeAllViews();
            mainContent.addView(cvb.getRoot());
            afterOnCreate();
            setViewModel(cvb);
        } else {
            throw new IllegalArgumentException("layout is not a inflate");
        }

    }

    /**
     * UI相关初始化
     *
     * @param
     */
    protected void afterOnCreate() {
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public CVB getCvb() {
        return cvb;
    }

}
