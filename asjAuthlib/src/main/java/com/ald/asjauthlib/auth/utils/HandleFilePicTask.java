package com.ald.asjauthlib.auth.utils;

import android.os.AsyncTask;

import com.ald.asjauthlib.authframework.core.utils.BitmapUtil;

import java.io.File;

/**
 * 文件图片处理
 * Created by sean yu on 2017/8/11.
 */
public class HandleFilePicTask extends AsyncTask<String, Integer, String[]> {

    private HandlePicCallBack callBack;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (callBack != null) {
            callBack.onStart();
        }
    }

    /**
     * 图片处理回调
     *
     * @param callBack 处理返回监听
     */
    public HandleFilePicTask setCallBack(HandlePicCallBack callBack) {
        this.callBack = callBack;
        return this;
    }

    @Override
    protected String[] doInBackground(String... params) {
        if (callBack != null) {
            callBack.onHandle();
        }
        String[] pictureArray = new String[params.length];
        int len = params.length;
        for (int i = 0; i < len; i++) {
            File file = new File(params[0]);
            if (file.exists()) {
                String imageCompressPath = BitmapUtil.compressUploadFile(file.getPath());
                pictureArray[i] = imageCompressPath;
            }

        }
        return pictureArray;
    }

    @Override
    protected void onPostExecute(String[] strings) {
        super.onPostExecute(strings);
        if (callBack != null) {
            callBack.onSuccess(strings);
        }
    }


}
