package com.ald.asjauthlib.auth.idcard;

import android.app.Activity;
import android.content.Intent;

import com.ald.asjauthlib.auth.AuthApi;
import com.ald.asjauthlib.auth.idcard.impl.FaceIDCard;
import com.ald.asjauthlib.auth.idcard.impl.FaceIDCardPhoto;
import com.ald.asjauthlib.auth.model.FaceTypeModel;
import com.ald.asjauthlib.auth.utils.HandlePicCallBack;
import com.ald.asjauthlib.auth.viewmodel.AppealPhoneVM;
import com.ald.asjauthlib.authframework.core.network.NetworkUtil;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.network.RequestCallBack;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 身份证认证工厂方法
 * Created by sean yu on 2017/7/21.
 */

public class IDCardScanFactory {
    //
    private static final String SCAN_ID_CARD_YITU = "YITU";
    private static final String SCAN_ID_CARD_FACE = "FACE_PLUS";
    public static String mSwitchType = "N";
    private boolean isAuthPhoto = false;

    private IDCardScan idCardScan;
    private HandlePicCallBack mCallBack;
    private Activity mContext;


    public IDCardScanFactory(Activity context) {
        this.mContext = context;

        if (!MiscUtils.isEmpty(AppealPhoneVM.oldPhone)) {
            requestFaceTypeFree();
        } else
            requestFaceType();
    }

    /**
     * 请求人脸识别类型
     */
    private void requestFaceType() {
        Call<FaceTypeModel> call = RDClient.getService(AuthApi.class).getFaceType();
        NetworkUtil.showCutscenes(mContext, call);
        call.enqueue(new RequestCallBack<FaceTypeModel>() {
            @Override
            public void onSuccess(Call<FaceTypeModel> call, Response<FaceTypeModel> response) {
                FaceTypeModel model = response.body();
                mSwitchType = model.getSwitchType();
                generateIDCardScan(model.getType());
            }
        });
        //generateIDCardScan(SCAN_ID_CARD_YITU);
    }

    /**
     * 请求人脸识别类型，免登陆
     */
    private void requestFaceTypeFree() {
        Call<FaceTypeModel> call = RDClient.getService(AuthApi.class).getFaceTypeFree();
        call.enqueue(new RequestCallBack<FaceTypeModel>() {
            @Override
            public void onSuccess(Call<FaceTypeModel> call, Response<FaceTypeModel> response) {
                FaceTypeModel model = response.body();
                mSwitchType = model.getSwitchType();
                generateIDCardScan(model.getType());
            }
        });
    }

    public IDCardScanFactory setAuthPhoto(boolean authPhoto) {
        isAuthPhoto = authPhoto;
        return this;
    }

    private void generateIDCardScan(String type) {
        if (MiscUtils.isEmpty(type)) {
            return;
        }

//        if (type.equals(SCAN_ID_CARD_YITU)) {
//            if (!isAuthPhoto) {
//                idCardScan = new YiTuIDCard(mContext);
//                idCardScan.setCallBack(mCallBack);
//            } else {
//                idCardScan = new YiTuIDCardPhoto(mContext);
//            }
//        } else
            if (type.equals(SCAN_ID_CARD_FACE)) {
            if (!isAuthPhoto) {
                idCardScan = new FaceIDCard(mContext);
                idCardScan.setCallBack(mCallBack);
            } else {
                idCardScan = new FaceIDCardPhoto(mContext);
            }
        }
    }

    public IDCardScanFactory setCallBack(HandlePicCallBack callBack) {
        this.mCallBack = callBack;
        return this;
    }

    public void scanFront() {
        if (idCardScan != null) {
            NetworkUtil.showCutscenes(mContext, null);
            idCardScan.scanFront();
        }
    }

    public void scanBack() {
        if (idCardScan != null) {
            NetworkUtil.showCutscenes(mContext, null);
            idCardScan.scanBack();
        }
    }

    /**
     * 提交身份证照片
     */
    public void submitIDCard(String[] picPath) {
        idCardScan.submitIDCard(picPath);
    }

    /**
     * @param picPath 正面路径
     */
    public void submitIDCard(String picPath) {
        idCardScan.submitIDCard(picPath);
    }

    /**
     */
    public void handleScanResult(int requestCode, int resultCode, Intent data) {
        if (idCardScan != null) {
            idCardScan.handleScanResult(requestCode, resultCode, data);
        }
    }
}
