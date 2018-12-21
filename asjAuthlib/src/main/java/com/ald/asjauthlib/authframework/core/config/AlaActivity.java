package com.ald.asjauthlib.authframework.core.config;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.authframework.core.utils.immersionBar.ImmersionBar;
import com.ald.asjauthlib.authframework.core.utils.PermissionCheck;
import com.ald.asjauthlib.authframework.core.utils.immersionBar.NoImmersionBar;

import java.util.List;


/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/1/6
 * 描述：activity都必须要继承自此类，这样就可以方便地进行一些
 * 统一的状态维护或者统一的处理。
 * 修订历史：
 */
public abstract class AlaActivity extends MyBaseActivity implements StatNameProvider, PermissionsResult {
    private static final String TAG = "AlaActivity";
    protected AlaActivityInterceptor interceptor;
    private Dialog showingDialog;
    protected ImmersionBar mImmersionBar;

    public AlaActivity() {
        interceptor = new AlaActivityInterceptor(this, this);
    }

    public AlaActivityInterceptor getInterceptor() {
        return interceptor;
    }

    public void setInterceptor(AlaActivityInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        interceptor.onNewIntent(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_in_to_left);
        super.onCreate(savedInstanceState);
        if (!this.isTaskRoot()) { //判断该Activity是不是任务空间的源Activity，“非”也就是说是被系统重新实例化出来
            //如果是就放在launcher Activity中话，这里可以直接return了
            Intent mainIntent = getIntent();
            String action = mainIntent.getAction();
            if (mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && action != null && action.equals(Intent.ACTION_MAIN)) {
                finish();
                return;
            }
        }
        interceptor.onCreate(savedInstanceState);
        if (this instanceof NoImmersionBar) return;
        setImmersionBar();
    }

    /**
     * @author Yangyang
     * @desc 带title, 把状态栏变白, 字体变黑
     */
    public void setImmersionBar() {
        if (mImmersionBar == null) mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.fitsSystemWindows(true, AlaConfig.getResources().getColor(R.color.white)).statusBarDarkFont(true, 0.3f).keyboardEnable(true).init();
    }

    /**
     * @author Yangyang
     * @desc 如果title不是白色, 需要把titleview的bgColor设置过来
     */
    public void setImmersionBar(@ColorInt int color) {
        if (mImmersionBar == null) mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.fitsSystemWindows(true, color).statusBarDarkFont(true, 0.3f).keyboardEnable(true).init();
    }

    /**
     * @param needWhite 是否需要白色状态栏字体,默认黑色
     * @author Yangyang
     * @desc 如果title不是白色, 需要把titleview的view设置过来, 会自动设置padding
     */
    public void setImmersionBar(View view, boolean needWhite) {
        if (mImmersionBar == null) mImmersionBar = ImmersionBar.with(this);
        try {
            if (needWhite)
                mImmersionBar.titleBar(view).fitsSystemWindows(false).statusBarDarkFont(false).flymeOSStatusBarFontColor(R.color.white).init();
            else
                mImmersionBar.titleBar(view).fitsSystemWindows(false).statusBarDarkFont(true, 0.3f).keyboardEnable(true).init();
        } catch (NullPointerException e) {
            setImmersionBar();
        }
    }

    /**
     * @author Yangyang
     * @desc 完全透明沉浸式
     */
    public void setTransImmersionBar() {
        if (mImmersionBar == null) mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.fitsSystemWindows(false).statusBarDarkFont(true, 0.3f).keyboardEnable(true).init();
    }


    public void setShowingDialog(Dialog showingDialog) {
        this.showingDialog = showingDialog;
    }

    private void dialogDestroy() {
        if (showingDialog != null) {
            if (showingDialog.isShowing() && !isFinishing()) {
                showingDialog.dismiss();
            }
        }
        showingDialog = null;
    }

    @Override
    protected void onStart() {
        super.onStart();
        interceptor.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        interceptor.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        interceptor.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        interceptor.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        interceptor.onStop();
        dialogDestroy();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        interceptor.onDestroy();
        dialogDestroy();
        if (mImmersionBar != null)
            mImmersionBar.destroy();
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.slide_out_from_left, R.anim.slide_out_to_right);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        interceptor.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        interceptor.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionCheck.getInstance().onRequestPermissionsResult(this, requestCode, permissions, grantResults, this);
    }

    /**
     * 向Fragment 分发onActivityResult
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        interceptor.onActivityResult(requestCode, resultCode, data);
        FragmentManager fm = getSupportFragmentManager();
        int index = requestCode >> 16;
        if (index != 0) {
            index--;
            if (fm.getFragments() == null || index < 0 || index >= fm.getFragments().size()) {
                Log.w(TAG, "Activity result fragment index out of range: 0x" + Integer.toHexString(requestCode));
                return;
            }
            Fragment frag = fm.getFragments().get(index);
            if (frag == null) {
                Log.w(TAG, "Activity result no fragment exists for index: 0x" + Integer.toHexString(requestCode));
            } else {
                handleResult(frag, requestCode, resultCode, data);
            }
        }
    }

    /**
     * 递归调用，对所有子Fragement生效
     */
    private void handleResult(Fragment frag, int requestCode, int resultCode, Intent data) {
        frag.onActivityResult(requestCode & 0xffff, resultCode, data);
        List<Fragment> frags = frag.getChildFragmentManager().getFragments();
        if (frags != null) {
            for (Fragment f : frags) {
                if (f != null) {
                    handleResult(f, requestCode, resultCode, data);
                }
            }
        }
    }



    @Override
    public void onUIRequestPermissionsGrantedResult(int requestCode) {

    }




}
