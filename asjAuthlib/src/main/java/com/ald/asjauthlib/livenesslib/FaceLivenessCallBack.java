package com.ald.asjauthlib.livenesslib;

import android.app.Activity;

/**
 * Created by sean yu on 2017/7/24.
 */

public interface FaceLivenessCallBack extends LivenessCallBack {

    /**
     * face++验证成功回调
     */
    void onFaceSuccess(String delta, byte[] queryPackage, Activity context);
}
