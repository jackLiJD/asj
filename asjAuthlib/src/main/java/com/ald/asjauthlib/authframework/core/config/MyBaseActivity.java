package com.ald.asjauthlib.authframework.core.config;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 基础Activity
 */
public class MyBaseActivity extends AppCompatActivity {

    protected int width;
    protected int height;


    //截图路径
    private String screenUir;

    //截图工具
    private ScreenShotListenUtils shotUtils;

    //截图延迟
    @SuppressLint("HandlerLeak")
    private Handler shotHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                Class c = Class.forName("com.rd.chux.utils.LibUtils");
                Method payMethod = c.getMethod("libSetShot", Activity.class, String.class);
                payMethod.invoke(null, MyBaseActivity.this, screenUir);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getDisplay();
        super.onCreate(savedInstanceState);
        //监听截屏
        shotUtils = ScreenShotListenUtils.newInstance(this);
        shotUtils.setListener(
                new ScreenShotListenUtils.OnScreenShotListener() {
                    public void onShot(String imagePath) {
                        // do something
                        screenUir = imagePath;
                        shotHandler.sendEmptyMessageDelayed(0x123, 500);
                    }
                }
        );
        //截图开启
        shotUtils.startListen();
    }

    private void getDisplay() {
        DisplayMetrics metrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        width = metrics.widthPixels;
        height = metrics.heightPixels;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onStop() {
        super.onStop();
        //截图关闭
        shotUtils.stopListen();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //截图关闭
        shotUtils.stopListen();
    }
}