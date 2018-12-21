package com.ald.asjauthlib.auth.liveness;

import android.content.Intent;

import com.ald.asjauthlib.auth.model.YiTuUploadCardResultModel;


/**
 * Created by sean yu on 2017/7/23.
 */

public interface ILiveness {

    /**
     * 启动人脸识别
     */
    void startLiveness(YiTuUploadCardResultModel model, String scene);

    /**
     * 活体识别返回值处理
     *
     */
    void handleLivenessResult(int requestCode, int resultCode, Intent data);
}
