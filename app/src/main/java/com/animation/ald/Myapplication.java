package com.animation.ald;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * 版权：XXX公司 版权所有
 * 作者：lijinduo
 * 版本：2.0
 * 创建日期：2018/12/21
 * 描述：(重构)
 * 修订历史：
 * 参考链接：
 */
public class Myapplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
