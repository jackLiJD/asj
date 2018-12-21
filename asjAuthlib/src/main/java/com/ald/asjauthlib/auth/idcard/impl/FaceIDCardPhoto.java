package com.ald.asjauthlib.auth.idcard.impl;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;

import com.ald.asjauthlib.utils.Constant;
import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.AppApi;
import com.ald.asjauthlib.auth.AuthApi;
import com.ald.asjauthlib.auth.idcard.IDCardScan;
import com.ald.asjauthlib.auth.model.YiTuUploadCardResultModel;
import com.ald.asjauthlib.auth.utils.HandleAuthCallBack;
import com.ald.asjauthlib.auth.utils.HandleFaceAuthTask;
import com.ald.asjauthlib.auth.utils.HandlePicCallBack;
import com.ald.asjauthlib.auth.utils.HandlePicTask;
import com.ald.asjauthlib.auth.viewmodel.AppealPhoneVM;
import com.ald.asjauthlib.idcardlib.IDCardScanActivity;
import com.ald.asjauthlib.utils.Permissions;
import com.ald.asjauthlib.web.HTML5WebView;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.network.NetworkUtil;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.network.RequestCallBack;
import com.ald.asjauthlib.authframework.core.network.entity.ApiResponse;
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
 * 单纯身份证验证(face++)
 * Created by sean yu on 2017/7/24.
 */

public class FaceIDCardPhoto implements IDCardScan {

    private static final String CARD_TYPE_SIDE = "side";
    //依图身份证认证常量
    private static final int CARD_TYPE_FRONT = 0;
    private static final int CARD_TYPE_BACK = 1;
    //
    private static final int REQUEST_CODE_STAGE_RR_IDF_FRONT = 0x112;
    private static final int REQUEST_CODE_STAGE_RR_IDF_BLACK = 0x111;
    private static final int PERMISSIONS_ASK_CODE = 0x110;
    //faceId返回图片
    private static final String ID_CARD_IMAGE = "idcardImg";
    private boolean isAllowAuth;
    //图片字节流

    private HandlePicCallBack callBack;
    private Activity context;

    public FaceIDCardPhoto(Activity context) {
        this.context = context;
    }

    /**
     * 设置回调监听
     *
     * @param callBack 处理返回监听
     */
    public FaceIDCardPhoto setCallBack(HandlePicCallBack callBack) {
        this.callBack = callBack;
        return this;
    }

    /**
     * 检查faceSDK授权
     */
    private void checkFaceAuth(final int side) {
        new HandleFaceAuthTask(context).setCallBack(new HandleAuthCallBack() {
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
        if (!PermissionCheck.getInstance().checkPermission(context, Permissions.facePermissions)) {
            PermissionCheck.getInstance().askForPermissions(context, Permissions.facePermissions, PERMISSIONS_ASK_CODE);
        } else {
            Intent intent = new Intent(context, IDCardScanActivity.class);
            intent.putExtra(CARD_TYPE_SIDE, side);
            intent.putExtra("isvertical", false);
            if (side == CARD_TYPE_FRONT) {
                context.startActivityForResult(intent, REQUEST_CODE_STAGE_RR_IDF_FRONT);
            } else if (side == CARD_TYPE_BACK) {
                context.startActivityForResult(intent, REQUEST_CODE_STAGE_RR_IDF_BLACK);
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
                case REQUEST_CODE_STAGE_RR_IDF_FRONT:
                    byte[] frontByte = data.getByteArrayExtra(ID_CARD_IMAGE);
                    if (frontByte != null && frontByte.length > 0) {
                        new HandlePicTask().setCallBack(callBack).execute(frontByte);
                    } else {
                        UIUtils.showToast(R.string.rr_identification_err);
                    }
                    break;

                case REQUEST_CODE_STAGE_RR_IDF_BLACK:
                    byte[] blackByte = data.getByteArrayExtra(ID_CARD_IMAGE);
                    if (blackByte != null && blackByte.length > 0) {
                        new HandlePicTask().setCallBack(callBack).execute(blackByte);
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
            boolean isAppeal = !MiscUtils.isEmpty(AppealPhoneVM.oldPhone);
            String subPath = isAppeal ? "/file/plusUploadIdCardFree.htm" : "/file/uploadIdNumberCardForFacePlus.htm";
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
            Call<YiTuUploadCardResultModel> call = isAppeal ? RDClient.getServiceLogout(AppApi.class, AppealPhoneVM.oldPhone).uploadFile(path, parts)
                    : RDClient.getService(AppApi.class).uploadFile(path, parts);
            call.enqueue(new RequestCallBack<YiTuUploadCardResultModel>() {
                @Override
                public void onSuccess(Call<YiTuUploadCardResultModel> call, Response<YiTuUploadCardResultModel> response) {
                    NetworkUtil.dismissCutscenes();
                    if (response.body() != null && MiscUtils.isNotEmpty(response.body().getList()) && response.body().getList().size() == 2) {
                        submitFaceCard(response.body());
                    } else {
                        UIUtils.showToast(R.string.rr_identification_success);
                    }
                }

                @Override
                public boolean errorHandle( ApiException apiException) {
                    if (apiException.getCode() == 1307) {
                        new CustomDialog.Builder(context).setMessage(apiException.getMessage())
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

    @Override
    public void submitIDCard(String picture) {

    }

    /**
     * 提交身份证信息
     */
    private void submitFaceCard(YiTuUploadCardResultModel model) {
        JSONObject requestObj = new JSONObject();
        requestObj.put("address", model.getCardInfo().getAddress());
        requestObj.put("citizenId", model.getCardInfo().getCitizen_id() + "");
        requestObj.put("gender", model.getCardInfo().getGender());
        requestObj.put("nation", model.getCardInfo().getNation());
        requestObj.put("name", model.getCardInfo().getName());
        requestObj.put("validDateBegin", model.getCardInfo().getValid_date_begin());
        requestObj.put("validDateEnd", model.getCardInfo().getValid_date_end());
        requestObj.put("birthday", model.getCardInfo().getBirthday());
        requestObj.put("agency", model.getCardInfo().getAgency());
        requestObj.put("idFrontUrl", model.getList().get(0).getUrl());
        requestObj.put("idBehindUrl", model.getList().get(1).getUrl());
        requestObj.put("type", "FACE_PLUS_CARD");
        Call<ApiResponse> submitCall = RDClient.getService(AuthApi.class).submitIdNumberInfoForFacePlus(requestObj);
        NetworkUtil.showCutscenes(context, submitCall);
        submitCall.enqueue(new RequestCallBack<ApiResponse>() {
            @Override
            public void onSuccess(Call<ApiResponse> call, Response<ApiResponse> response) {
                UIUtils.showToast(R.string.rr_identification_success);
                context.setResult(Activity.RESULT_OK);
                context.finish();
            }
        });
    }
}
