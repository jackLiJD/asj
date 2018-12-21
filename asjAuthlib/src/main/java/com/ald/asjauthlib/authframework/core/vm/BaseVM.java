package com.ald.asjauthlib.authframework.core.vm;

import android.databinding.ObservableBoolean;

import com.ald.asjauthlib.authframework.core.ui.DividerLine;
import com.ald.asjauthlib.authframework.core.vm.entity.EmptyState;
import com.ald.asjauthlib.authframework.core.vm.entity.ModelState;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/1/4
 * 描述：BaseViewModel
 * 修订历史：
 */
public abstract class BaseVM implements ViewModel {

    /**
     * 是否显示appbar
     */
    public final ObservableBoolean hidden = new ObservableBoolean(false);
    /**
     * 空态对象（appbar）
     */
    public final EmptyState emptyState = new EmptyState();
    /**
     * 网络请求数据
     */
    public final ModelState modelState = new ModelState();
    /**
     * tips栏目
     */
    public String[] tips = null;
    /**
     * 分割线类型
     * -1 - 没有分割线
     * 0 - 水平分割线
     * 1 - 垂直分割线
     */
    public int type = DividerLine.HORIZONTAL;


}
