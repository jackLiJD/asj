package com.ald.asjauthlib.auth.liveness;

import android.app.Activity;
import android.content.Intent;

import com.ald.asjauthlib.auth.liveness.impl.FaceIDILiveness;
import com.ald.asjauthlib.auth.model.YiTuUploadCardResultModel;


/**
 * 人脸识别工
 * Created by sean yu on 2017/7/23.
 */

public class LivenessFactory {
    public static final String LIVENESS_TYPE = "liveness_type";
    public static final String LIVEN_TYPE_YI_TU = "liveness_yi_tu";
    public static final String LIVEN_TYPE_FACE = "liveness_face";

    private ILiveness liveness;
    private Activity context;

    public LivenessFactory(Activity context) {
        this.context = context;
        generateLiveness();
    }

    /**
     * 生成活体
     */
    private void generateLiveness() {
        String type = context.getIntent().getStringExtra(LIVENESS_TYPE);
        if (type.equals(LIVEN_TYPE_FACE)) {
            liveness = new FaceIDILiveness(context);
        }

    }

    /**
     * 启动活体认证
     */
    public void startLiveness(YiTuUploadCardResultModel model, String scene) {
        liveness.startLiveness(model, scene);
    }

    /**
     * 处理人脸识别结果
     */
    public void handleLivenessResult(int requestCode, int resultCode, Intent data) {
        liveness.handleLivenessResult(requestCode, resultCode, data);

    }
}
