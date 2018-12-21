package com.ald.asjauthlib.auth.ui;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.viewmodel.AppealPhoneVM;
import com.ald.asjauthlib.databinding.ActivityAccountAppealBinding;
import com.ald.asjauthlib.authframework.core.config.AlaBaseActivity;


public class AccountAppealActivity extends AlaBaseActivity<ActivityAccountAppealBinding> {


    AppealPhoneVM viewModel;


    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_account_appeal;
    }

    @Override
    protected void setViewModel(ActivityAccountAppealBinding contentBinding) {
        viewModel = new AppealPhoneVM(this, getIntent(), contentBinding);
        cvb.setViewModel(viewModel);
        cvb.titleBar.setTitle(getResources().getString(R.string.appeal_title));
        cvb.titleBar.setLeftImage(R.drawable.icon_titlebar_heise_fanhui);
    }

    @Override
    public String getStatName() {
        return getResources().getString(R.string.appeal_title);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);

    }


    /**
     * 点击区域非Edit text则关闭虚拟键盘
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏虚拟键盘
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (im != null)
                im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
