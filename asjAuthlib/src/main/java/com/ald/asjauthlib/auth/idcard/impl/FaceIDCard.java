package com.ald.asjauthlib.auth.idcard.impl;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;

import com.ald.asjauthlib.utils.Constant;
import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.AppApi;
import com.ald.asjauthlib.dialog.IdConfirmDialog;
import com.ald.asjauthlib.auth.idcard.IDCardScan;
import com.ald.asjauthlib.auth.liveness.LivenessFactory;
import com.ald.asjauthlib.auth.model.IDCardInfoModel;
import com.ald.asjauthlib.auth.model.YiTuUploadCardResultModel;
import com.ald.asjauthlib.auth.ui.RRIdAuthActivity;
import com.ald.asjauthlib.auth.utils.HandleAuthCallBack;
import com.ald.asjauthlib.auth.utils.HandleFaceAuthTask;
import com.ald.asjauthlib.auth.utils.HandlePicCallBack;
import com.ald.asjauthlib.auth.utils.HandlePicTask;
import com.ald.asjauthlib.auth.viewmodel.AppealPhoneVM;
import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.idcardlib.IDCardScanActivity;
import com.ald.asjauthlib.utils.Permissions;
import com.ald.asjauthlib.web.HTML5WebView;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.network.NetworkUtil;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.network.RequestCallBack;
import com.ald.asjauthlib.authframework.core.network.exception.ApiException;
import com.ald.asjauthlib.authframework.core.ui.CustomDialog;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.authframework.core.utils.PermissionCheck;
import com.ald.asjauthlib.authframework.core.utils.UIUtils;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;


/**
 * face++ 身份证
 * Created by sean yu on 2017/7/21.
 */

public class FaceIDCard implements IDCardScan {

    private static final String CARD_TYPE_SIDE = "side";
    //依图身份证认证常量
    private static final int CARD_TYPE_FRONT = 0;
    private static final int CARD_TYPE_BACK = 1;
    //

    //faceId返回图片
    private static final String ID_CARD_IMAGE = "idcardImg";
    private boolean isAllowAuth;

    private HandlePicCallBack callBack;
    private Activity mContext;
    private String stageJump;

    public FaceIDCard(Activity context) {
        this.mContext = context;
        this.stageJump = context.getIntent().getStringExtra(BundleKeys.STAGE_JUMP);
    }

    /**
     * 设置回调监听
     *
     * @param callBack 处理返回监听
     */
    public FaceIDCard setCallBack(HandlePicCallBack callBack) {
        this.callBack = callBack;
        return this;
    }

    /**
     * 检查faceSDK授权
     */
    private void checkFaceAuth(final int side) {
        new HandleFaceAuthTask(mContext).setCallBack(new HandleAuthCallBack() {
            @Override
            public void authStart() {

            }

            @Override
            public void authSuccess() {
                isAllowAuth = true;
                goToFaceID(side);
            }

            @Override
            public void authError() {
                isAllowAuth = false;
            }
        }).execute();
    }

    @Override
    public void scanFront() {
        if (isAllowAuth) {
            goToFaceID(CARD_TYPE_FRONT);
        } else {
            checkFaceAuth(CARD_TYPE_FRONT);
        }
    }

    @Override
    public void scanBack() {
        if (isAllowAuth) {
            goToFaceID(CARD_TYPE_BACK);
        } else {
            checkFaceAuth(CARD_TYPE_BACK);
        }

    }


    /**
     * face++ 身份证识别
     */
    private void goToFaceID(int side) {
        if (!PermissionCheck.getInstance().checkPermission(mContext, Permissions.facePermissions)) {
            PermissionCheck.getInstance().askForPermissions(mContext, Permissions.facePermissions, PermissionCheck.REQUEST_CODE_CAMERA);
        } else {
            Intent intent = new Intent(mContext, IDCardScanActivity.class);
            intent.putExtra(CARD_TYPE_SIDE, side);
            intent.putExtra("isvertical", false);
            if (side == CARD_TYPE_FRONT) {
                mContext.startActivityForResult(intent, BundleKeys.REQUEST_CODE_STAGE_RR_IDF_FRONT);
            } else if (side == CARD_TYPE_BACK) {
//                ActivityUtils.push(IDCardScanActivity.class, intent, REQUEST_CODE_STAGE_RR_IDF_BLACK);
                mContext.startActivityForResult(intent, BundleKeys.REQUEST_CODE_STAGE_RR_IDF_BACK);
//                context.startActivity(intent);
            }
        }
        NetworkUtil.dismissCutscenes();
    }

    /**
     * 认证结果处理
     */
    public void handleScanResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            switch (requestCode) {
                case BundleKeys.REQUEST_CODE_STAGE_RR_IDF_FRONT:
                    byte[] frontByte = data.getByteArrayExtra(ID_CARD_IMAGE);
                    if (frontByte != null && frontByte.length > 0) {
                        new HandlePicTask().setCallBack(callBack).execute(frontByte, null);
                    } else {
                        UIUtils.showToast(R.string.rr_identification_err);
                    }
                    break;

                case BundleKeys.REQUEST_CODE_STAGE_RR_IDF_BACK:
                    byte[] blackByte = data.getByteArrayExtra(ID_CARD_IMAGE);
                    if (blackByte != null && blackByte.length > 0) {
                        new HandlePicTask().setCallBack(callBack).execute(null, blackByte);
                    } else {
                        UIUtils.showToast(R.string.rr_identification_err);
                    }
                    break;
            }
        }
    }

    @Override
    public void submitIDCard(String[] pictureArray) {
        if (pictureArray != null && pictureArray.length == 2) {
            String subPath = MiscUtils.isEmpty(AppealPhoneVM.oldPhone) ? "/file/uploadIdNumberCardForFacePlus.htm" : "/file/plusUploadIdCardFree.htm";
            String path = AlaConfig.getServerProvider().getImageServer() + subPath;
            RequestBody frontRequestBody = RequestBody
                    .create(MediaType.parse("multipart/form-data"), new File(pictureArray[0]));
            RequestBody blackRequestBody = RequestBody
                    .create(MediaType.parse("multipart/form-data"), new File(pictureArray[1]));
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)//表单类型
                    .addFormDataPart("reqData", new JSONObject().toJSONString());
            builder.addFormDataPart("file", "front.jpg", frontRequestBody);
            builder.addFormDataPart("file", "back.jpg", blackRequestBody);
            List<MultipartBody.Part> parts = builder.build().parts();
            Call<YiTuUploadCardResultModel> call = MiscUtils.isEmpty(AppealPhoneVM.oldPhone) ? RDClient.getService(AppApi.class).uploadFile(path, parts)
                    : RDClient.getServiceLogout(AppApi.class, AppealPhoneVM.oldPhone).uploadFile(path, parts);
            NetworkUtil.showCutscenes(mContext, call);
            call.enqueue(new RequestCallBack<YiTuUploadCardResultModel>() {
                @Override
                public void onSuccess(Call<YiTuUploadCardResultModel> call, Response<YiTuUploadCardResultModel> response) {
                    NetworkUtil.dismissCutscenes();
                    if (response.body() != null && MiscUtils.isNotEmpty(response.body().getList()) && response.body().getList().size() == 2) {
                        final YiTuUploadCardResultModel yiTuUploadCardResultModel = response.body();
                        Intent intent = new Intent();
                        intent.putExtra(BundleKeys.STAGE_JUMP, stageJump);
                        intent.putExtra(LivenessFactory.LIVENESS_TYPE, LivenessFactory.LIVEN_TYPE_FACE);
                        intent.putExtra(BundleKeys.RR_IDF_YITU_MODEL, yiTuUploadCardResultModel);
                        intent.putExtra(BundleKeys.INTENT_KEY_CREDIT_PROMOTE_SCENE,
                                mContext.getIntent().getStringExtra(BundleKeys.INTENT_KEY_CREDIT_PROMOTE_SCENE));

                        //防止RRIdAuthActivity已经被销毁 引发 leaked window
                        Activity currentActivity = AlaConfig.getCurrentActivity();
                        if (currentActivity instanceof RRIdAuthActivity) {
                            IdConfirmDialog dialog = new IdConfirmDialog(AlaConfig.getCurrentActivity(), intent, !MiscUtils.isEmpty(AppealPhoneVM.oldPhone));
                            dialog.show();
                        }
                    } else {
                        UIUtils.showToast(R.string.rr_identification_err);
                    }
                }

                @Override
                public boolean errorHandle( ApiException apiException) {
                    if (apiException.getCode() == 1307) {
                        new CustomDialog.Builder(mContext).setMessage(apiException.getMessage())
                                .setNegativeButton(R.string.cancel, null)
                                .setNegativeButtonTextColor(ContextCompat.getColor(AlaConfig.getContext(), R.color.text_important_color))
                                .setPositiveButton(R.string.dialog_btn_customer, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent();
                                        String url = AlaConfig.getServerProvider().getAppServer() + Constant.H5_URL_HELP;
                                        intent.putExtra(HTML5WebView.INTENT_BASE_URL, url);
                                        ActivityUtils.push(HTML5WebView.class, intent);
                                    }
                                }).create().show();
                    } else {
                        UIUtils.showToast(apiException.getMessage());
                    }
                    return true;
                }
            });
        } else {
            UIUtils.showToast(R.string.rr_identification_err);
        }
    }

    /**
     * @param picture 银行卡正面路径
     */
    @Override
    public void submitIDCard(String picture) {
        if (MiscUtils.isNotEmpty(picture)) {
            String path = AlaConfig.getServerProvider().getImageServer() + "/file/plusUpIdCardFront.htm";
            RequestBody frontRequestBody = RequestBody
                    .create(MediaType.parse("multipart/form-data"), new File(picture));
//            RequestBody blackRequestBody = RequestBody
//                    .create(MediaType.parse("multipart/form-data"), new File(pictureArray[1]));
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)//表单类型
                    .addFormDataPart("reqData", new JSONObject().toJSONString());
            builder.addFormDataPart("file", "front.jpg", frontRequestBody);
            List<MultipartBody.Part> parts = builder.build().parts();
            Call<IDCardInfoModel> call = RDClient.getService(AppApi.class).uploadIDFile(path, parts);
            NetworkUtil.showCutscenes(mContext, call);
            call.enqueue(new RequestCallBack<IDCardInfoModel>() {
                @Override
                public void onSuccess(Call<IDCardInfoModel> call, Response<IDCardInfoModel> response) {
                    NetworkUtil.dismissCutscenes();
                    if (response.body() != null && response.body() != null) {
                        IDCardInfoModel model = response.body();
                        callBack.onIDInfoRecognized(model.getCardInfo());
                    } else {
                        UIUtils.showToast(R.string.rr_identification_err);
                    }
                }

                @Override
                public boolean errorHandle( ApiException apiException) {
                    if (apiException.getCode() == 1307) {
                        new CustomDialog.Builder(mContext).setMessage(apiException.getMessage())
                                .setNegativeButton(R.string.cancel, null)
                                .setNegativeButtonTextColor(ContextCompat.getColor(AlaConfig.getContext(), R.color.text_important_color))
                                .setPositiveButton(R.string.dialog_btn_customer, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent();
                                        String url = AlaConfig.getServerProvider().getAppServer() + Constant.H5_URL_HELP;
                                        intent.putExtra(HTML5WebView.INTENT_BASE_URL, url);
                                        ActivityUtils.push(HTML5WebView.class, intent);
                                    }
                                }).create().show();
                    } else {
                        UIUtils.showToast(apiException.getMessage());
                    }
                    return true;
                }
            });
        } else {
            UIUtils.showToast(R.string.rr_identification_err);
        }
    }


}
