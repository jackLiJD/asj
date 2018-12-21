package com.ald.asjauthlib.authframework.core.config;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.authframework.core.utils.immersionBar.ImmersionBar;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/1/18 11:16
 * 描述：
 * 修订历史：
 */
public abstract class AlaBaseFragment<CVB extends ViewDataBinding> extends AlaFragment {
    // 布局view
    protected CVB cvb;
    // 内容布局
    protected RelativeLayout container;
    /**
     * Fragment title
     */
    public String fragmentTitle;
    /**
     * 是否可见状态 为了避免和{@link Fragment#isVisible()}冲突 换个名字
     */
    private boolean isFragmentVisible;
    /**
     * 标志位，View已经初始化完成。
     * 2016/04/29
     * 用isAdded()属性代替
     * 2016/05/03
     * isPrepared还是准一些,isAdded有可能出现onCreateView没走完但是isAdded了
     */
    private boolean isPrepared;
    /**
     * 是否第一次加载
     */
    private boolean isFirstLoad = true;
    /**
     * <pre>
     * 忽略isFirstLoad的值，强制刷新数据，但仍要Visible & Prepared
     * 一般用于PagerAdapter需要刷新各个子Fragment的场景
     * 不要new 新的 PagerAdapter 而采取reset数据的方式
     * 所以要求Fragment重新走initData方法
     * 故使用 {@link AlaBaseFragment#setForceLoad(boolean)}来让Fragment下次执行initData
     * </pre>
     */
    private boolean forceLoad = false;

    protected ImmersionBar mImmersionBar;
    private Activity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.size() > 0) {
            initVariables(bundle);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // 若 viewpager 不设置 setOffscreenPageLimit 或设置数量不够
        // 销毁的Fragment onCreateView 每次都会执行(但实体类没有从内存销毁)
        // 导致initData反复执行,所以这里注释掉
        // isFirstLoad = true;

        // 2016/04/29
        // 取消 isFirstLoad = true的注释 , 因为上述的initData本身就是应该执行的
        // onCreateView执行 证明被移出过FragmentManager initData确实要执行.
        // 如果这里有数据累加的Bug 请在initViews方法里初始化您的数据 比如 list.clear();
        isFirstLoad = true;
        View ll = inflater.inflate(R.layout.fw__fragment_base, container, false);
        int layoutId = getLayoutInflate();
        if (layoutId > 0) {
            cvb = DataBindingUtil.inflate(getLayoutInflater(), layoutId, null, false);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            cvb.getRoot().setLayoutParams(params);
            this.container = (RelativeLayout) ll.findViewById(R.id.container);
            this.container.removeAllViews();
            this.container.addView(cvb.getRoot());
            initViews();
            isPrepared = true;
            lazyLoad();
            return ll;
        } else {
            throw new IllegalArgumentException("layout is not a inflate");
        }
    }

    public CVB getCvb() {
        return cvb;
    }

    /**
     * @author Yangyang
     * @desc 带title, 把状态栏变白, 字体变黑
     */
    protected void setImmersionBar() {
        if (mActivity == null) return;
        if (mImmersionBar == null) mImmersionBar = ImmersionBar.with(mActivity);
        mImmersionBar.fitsSystemWindows(true, AlaConfig.getResources().getColor(R.color.white)).statusBarDarkFont(true, 0.3f).flymeOSStatusBarFontColor(R.color.color_232323).init();
    }

    /**
     * @author Yangyang
     * @desc 如果title不是白色, 需要把titleview的bgColor设置过来
     */
    protected void setImmersionBar(@ColorInt int color) {
        if (mActivity == null) return;
        if (mImmersionBar == null) mImmersionBar = ImmersionBar.with(mActivity);
        mImmersionBar.fitsSystemWindows(true, color).statusBarDarkFont(true, 0.3f).flymeOSStatusBarFontColor(R.color.color_232323).init();
    }

    /**
     * @param needWhite 是否需要白色状态栏字体,默认黑色
     * @author Yangyang
     * @desc 如果title不是白色, 需要把titleview的view设置过来, 会自动设置padding
     */
    protected void setImmersionBar(View view, boolean needWhite) {
        if (mActivity == null) return;
        try {
            if (mImmersionBar == null) mImmersionBar = ImmersionBar.with(mActivity);
            if (needWhite)
                mImmersionBar.titleBar(view).fitsSystemWindows(false).statusBarDarkFont(false).flymeOSStatusBarFontColor(R.color.white).init();
            else
                mImmersionBar.titleBar(view).fitsSystemWindows(false).statusBarDarkFont(true, 0.3f).flymeOSStatusBarFontColor(R.color.color_232323).init();
        } catch (NullPointerException e) {
            if (mImmersionBar != null)
                mImmersionBar.destroy();
            mImmersionBar = null;
            setImmersionBar();
        }

    }

    /**
     * @author Yangyang
     * @desc 完全透明沉浸式
     */
    protected void setTransImmersionBar() {
        if (mActivity == null) return;
        if (mImmersionBar == null) mImmersionBar = ImmersionBar.with(mActivity);
        mImmersionBar.fitsSystemWindows(false).statusBarDarkFont(true, 0.3f).flymeOSStatusBarFontColor(R.color.color_232323).init();
    }

    protected void setTransImmersionBar( boolean needWhite) {
        if (mActivity == null) return;
        if (mImmersionBar == null) mImmersionBar = ImmersionBar.with(mActivity);
        if (needWhite)
            mImmersionBar.fitsSystemWindows(false).statusBarDarkFont(false).flymeOSStatusBarFontColor(R.color.white).init();
        else
            mImmersionBar.fitsSystemWindows(false).statusBarDarkFont(true, 0.3f).flymeOSStatusBarFontColor(R.color.color_232323).init();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isPrepared = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null)
            mImmersionBar.destroy();
    }

    /**
     * 如果是与ViewPager一起使用，调用的是setUserVisibleHint
     *
     * @param isVisibleToUser 是否显示出来了
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            onVisible();
        } else {
            onInvisible();
        }
        currentFragVisibility(isVisibleToUser);
    }

    public void currentFragVisibility(boolean isVisibleToUser) {
    }

    /**
     * /**
     * 注2:
     * 如果是通过FragmentTransaction的show和hide的方法来控制显示，调用的是onHiddenChanged.
     * 针对初始就show的Fragment 为了触发onHiddenChanged事件 达到lazy效果
     * 需要先hide再show
     * 需要先hide再show
     * 需要先hide再show
     * eg:
     * transaction.hide(aFragment);
     * transaction.show(aFragment);
     *
     * @param hidden hidden True if the fragment is now hidden, false if it is not
     *               visible.
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            onVisible();
        } else {
            onInvisible();
        }
        currentFragVisibility(!hidden);
    }

    protected void onVisible() {
        isFragmentVisible = true;
        lazyLoad();
    }

    protected void onInvisible() {
        isFragmentVisible = false;
    }

    /**
     * 要实现延迟加载Fragment内容,需要在 onCreateView
     * isPrepared = true;
     */
    protected void lazyLoad() {
        if (isPrepared() && isFragmentVisible()) {
            if (forceLoad || isFirstLoad()) {
                forceLoad = false;
                isFirstLoad = false;
                initData();
            }
        }
    }

    /**
     * 被ViewPager移除的Fragment 下次显示时会从getArguments()中重新获取数据
     * 所以若需要刷新被移除Fragment内的数据需要重新put数据 eg:
     * Bundle args = getArguments();
     * if (args != null) {
     * args.putParcelable(KEY, info);
     * }
     */
    public void initVariables(Bundle bundle) {
    }


    protected void initData() {
    }

    public boolean isPrepared() {
        return isPrepared;
    }

    /**
     * 忽略isFirstLoad的值，强制刷新数据，但仍要Visible & Prepared
     */
    public void setForceLoad(boolean forceLoad) {
        this.forceLoad = forceLoad;
    }

    public boolean isFirstLoad() {
        return isFirstLoad;
    }

    public boolean isFragmentVisible() {
        return isFragmentVisible;
    }

    public String getTitle() {
        if (null == fragmentTitle) {
            setDefaultFragmentTitle(null);
        }
        return TextUtils.isEmpty(fragmentTitle) ? "" : fragmentTitle;
    }

    public void setTitle(String title) {
        fragmentTitle = title;
    }

    /**
     * 设置fragment的Title直接调用 {@link AlaBaseFragment#setTitle(String)},若不显示该title 可以不做处理
     *
     * @param title 一般用于显示在TabLayout的标题
     */
    protected void setDefaultFragmentTitle(String title) {

    }

    /**
     * 布局
     */
    public abstract int getLayoutInflate();


    /**
     * 根据id获取View
     */
    protected <T extends View> T getView(int id) {
        return (T) getView().findViewById(id);
    }

    /**
     * UI相关初始化
     */
    protected void initViews() {
        if (cvb == null) return;
    }

}
