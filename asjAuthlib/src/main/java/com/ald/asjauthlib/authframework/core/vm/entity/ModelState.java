package com.ald.asjauthlib.authframework.core.vm.entity;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.annotation.StringRes;
import android.view.View;

import com.ald.asjauthlib.BR;
import com.ald.asjauthlib.R;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.ui.ModelView;
import com.ald.asjauthlib.authframework.core.ui.PageLoadingView;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/1/4
 * 描述：空态对象({@link PageLoadingView})
 * 修订历史：
 */

public class ModelState extends BaseObservable {
    /**
     * 是否加载中
     */
    private boolean noData = false;
    /**
     * 空态图的 resId；
     */
    private Drawable image;
    /**
     * 提示文字
     */
    private String prompt;

    /**
     * 点击事件
     */
    private ModelViewClick click;

    public ModelState() {
    }

    @Bindable
    public boolean isNoData() {
        return noData;
    }

    public void setNoData(boolean noData) {
        this.noData = noData;
        notifyPropertyChanged(BR.noData);
        setPrompt();
    }


    public void setNoData(boolean noData,ModelViewClick click) {
        this.noData = noData;
        if(click != null){
            this.click = click;
        }
        setNoData(true);
    }

    public void setNoData() {
        setNoData(true);
    }

    public void setNoData(ModelViewClick click) {
        if(click != null){
            this.click = click;
        }
        setNoData(true);
    }


    @Bindable
    public Drawable getImage() {
        return image;
    }


    @Bindable
    public String getPrompt() {
        return prompt;
    }

    public void setPrompt() {
        this.prompt = AlaConfig.getContext().getString(R.string.empty_no_data);
        setPrompt(prompt);
    }

    @Bindable
    public void setPrompt(String prompt) {
        this.prompt = prompt;
        notifyPropertyChanged(BR.prompt);
    }


    /**
     * @param resId 提示内容
     */
    public void setPrompt(@StringRes int resId) {
        Context context = ActivityUtils.peek();
        if (null != context && 0 != resId) {
            setNoData(true);
            setPrompt(context.getString(resId));
        }
    }


    /**
     * PageLoadingView 点击事件
     */
    public void onClick(View view) {
        if (null != click) {
            click.onClick(view);
        }
    }

    /**
     * PageLoadingView 点击事件接口
     */
    public interface ModelViewClick {
        void onClick(View view);
    }


    @BindingAdapter("asJmodelState")
    public static void modelState(View view, ModelState state) {
        ((ModelView) view).setAsJemptyState(state);
    }
}
