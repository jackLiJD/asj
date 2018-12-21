package com.ald.asjauthlib.auth.liveness.impl;

import android.app.Activity;
import android.content.Intent;

import com.ald.asjauthlib.utils.Constant;
import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.AppApi;
import com.ald.asjauthlib.auth.AuthApi;
import com.ald.asjauthlib.dialog.TipsDialog;
import com.ald.asjauthlib.auth.liveness.ILiveness;
import com.ald.asjauthlib.auth.model.AuthUserBasicInfoModel;
import com.ald.asjauthlib.auth.model.FaceLivenessModel;
import com.ald.asjauthlib.auth.model.YiTuUploadCardResultModel;
import com.ald.asjauthlib.auth.ui.AccountAppealActivity;
import com.ald.asjauthlib.auth.ui.BankCardAddIdActivity;
import com.ald.asjauthlib.auth.ui.CreditPromoteActivity;
import com.ald.asjauthlib.auth.utils.HandleAuthCallBack;
import com.ald.asjauthlib.auth.utils.HandleFaceAuthTask;
import com.ald.asjauthlib.auth.viewmodel.AppealPhoneVM;
import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.livenesslib.LivenessActivity;
import com.ald.asjauthlib.livenesslib.LivenessData;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.network.NetworkUtil;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.network.RequestCallBack;
import com.ald.asjauthlib.authframework.core.network.entity.ApiResponse;
import com.ald.asjauthlib.authframework.core.network.exception.ApiException;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.DataUtils;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.authframework.core.utils.SPUtil;
import com.ald.asjauthlib.authframework.core.utils.UIUtils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;


/**
 * face++人脸识别
 * Created by sean yu on 2017/7/23.
 */

public class FaceIDILiveness implements ILiveness, Serializable {
    private YiTuUploadCardResultModel model;
    private Activity context;

    private String stageJump;
    private boolean isAllowAuth;
    private String scene;

    public static int FACE_ID_RESULT = 0x111;

    public FaceIDILiveness(Activity context) {
        this.context = context;
        stageJump = context.getIntent().getStringExtra(BundleKeys.STAGE_JUMP);
    }

    /**
     *
     */
    @Override
    public void startLiveness(YiTuUploadCardResultModel model, String scene) {
        this.model = model;
        this.scene = scene;
        if (MiscUtils.isEmpty(AppealPhoneVM.oldPhone)) {
            if (model != null)
                submitFaceCard();
        } else {
            if (isAllowAuth) {
                goToLiveness();
            } else {
                checkFaceAuth();
            }
        }

    }

    /**
     * 提交数据到后台服务器
     * Face++人脸
     */
    private void submitFace(String delta, Map<String, byte[]> data, final Activity context) throws IOException {
        NetworkUtil.showCutscenes(context, "处理中", "处理中...");
        String subPath = MiscUtils.isEmpty(AppealPhoneVM.oldPhone) ? "/file/uploadFaceForFacePlus.htm" : "/file/plusUploadFaceFree.htm";
        String path = AlaConfig.getServerProvider().getImageServer() + subPath;

        File file = DataUtils.createIfNotExists("face");
        DataUtils.saveToFile(data.get("image_best"), file);
        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        File fileBackgroud = DataUtils.createIfNotExists("backgroud");
        DataUtils.saveToFile(data.get("image_env"), fileBackgroud);
        RequestBody bgBody = RequestBody.create(MediaType.parse("multipart/form-data"), fileBackgroud);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("delta", delta);
        if (model != null && model.getCardInfo() != null) {
            jsonObject.put("idCardNumber", model.getCardInfo().getCitizen_id());
            jsonObject.put("idCardName", model.getCardInfo().getName());
        }

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)//表单类型
                .addFormDataPart("reqData", jsonObject.toJSONString());

        builder.addFormDataPart("file", "filename=\"front.png", imageBody);
        builder.addFormDataPart("image_env", "filename=\"image_env.png", bgBody);

        List<MultipartBody.Part> parts = builder.build().parts();
        Call<FaceLivenessModel> call = MiscUtils.isEmpty(AppealPhoneVM.oldPhone) ? RDClient.getService(AppApi.class).uploadFaceLivenessFile(path, parts)
                : RDClient.getServiceLogout(AppApi.class, AppealPhoneVM.oldPhone).uploadFaceLivenessFile(path, parts);
        NetworkUtil.showCutscenes(context, call);
        call.enqueue(new RequestCallBack<FaceLivenessModel>() {
            @Override
            public void onSuccess(Call<FaceLivenessModel> call, Response<FaceLivenessModel> response) {

                FaceLivenessModel livenessModel = response.body();

                //账号申诉不进行提交，直接跳转
                if (livenessModel != null) {
                    if (!MiscUtils.isEmpty(AppealPhoneVM.oldPhone)) {
                        Intent intent = new Intent();
                        intent.putExtra(BundleKeys.APPEAL_NEWPHONE, AppealPhoneVM.newPhone);
                        ActivityUtils.push(AccountAppealActivity.class, intent);
                        return;
                    }

                    JSONObject requestObj = new JSONObject();
                    requestObj.put("imageBestUrl", livenessModel.getImageBestUrl());
                    requestObj.put("confidence", livenessModel.getConfidence() + "");
                    requestObj.put("thresholds", livenessModel.getThresholds());
                    requestObj.put("type", "FACE_PLUS_FACE");
                    Call<ApiResponse> submitCall = RDClient.getService(AuthApi.class).submitIdNumberInfoForFacePlus(requestObj);
                    NetworkUtil.showCutscenes(context, submitCall);
                    submitCall.enqueue(new RequestCallBack<ApiResponse>() {
                        @Override
                        public void onSuccess(Call<ApiResponse> call, Response<ApiResponse> response) {
                            NetworkUtil.dismissForceCutscenes();
                            //from auth,goto bank card
                            checkBankStatus();

                        }

                        @Override
                        public void onFailure(Call<ApiResponse> call, Throwable t) {
                            super.onFailure(call, t);
                            NetworkUtil.dismissForceCutscenes();
                        }
                    });
                } else {
                    NetworkUtil.dismissForceCutscenes();
                    UIUtils.showToast(R.string.rr_identification_err);
                }
            }

            @Override
            public void onFailure(Call<FaceLivenessModel> call, Throwable t) {
                super.onFailure(call, t);
                NetworkUtil.dismissForceCutscenes();
            }
        });
    }

    private void checkBankStatus() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", SPUtil.getValue(Constant.USER_PHONE));
        Call<AuthUserBasicInfoModel> call = RDClient.getService(AuthApi.class).checkUserBasicInfo(jsonObject);
        NetworkUtil.showCutscenes(context, call);
        call.enqueue(new RequestCallBack<AuthUserBasicInfoModel>() {
            @Override
            public void onSuccess(Call<AuthUserBasicInfoModel> call, Response<AuthUserBasicInfoModel> response) {
                if (response != null && response.body() != null) {
                    if (response.body().isBindIcCard()) {
                        Intent intent = new Intent();
                        intent.putExtra(BundleKeys.STAGE_JUMP, stageJump);
                        intent.putExtra(BundleKeys.INTENT_KEY_CREDIT_PROMOTE_SCENE, scene);
                        context.setResult(Activity.RESULT_OK);
                        ActivityUtils.pop(context);//finish StartFaceActivity
                        ActivityUtils.pop();//finish RRIdAuthActivity
                        ActivityUtils.push(CreditPromoteActivity.class, intent);
                    } else {
                        Intent intent = new Intent();
                        intent.putExtra(BundleKeys.BANK_CARD_NAME, model.getCardInfo().getName());
                        intent.putExtra(BundleKeys.SETTING_PAY_ID_NUMBER, model.getCardInfo().getCitizen_id());
                        intent.putExtra(BundleKeys.STAGE_JUMP, stageJump);
                        intent.putExtra(BundleKeys.INTENT_KEY_CREDIT_PROMOTE_SCENE, scene);
                        ActivityUtils.push(BankCardAddIdActivity.class, intent, BundleKeys.REQUEST_CODE_RRID);
                        context.setResult(Activity.RESULT_OK);//顺利跳转到银行卡，销毁人脸识别引导页和身份证扫描页
                        ActivityUtils.pop(context);//finish StartFaceActivity
                        ActivityUtils.pop();//finish RRIdAuthActivity
                    }
                }
            }
        });
    }

    /**
     * 提交身份证信息
     */
    private void submitFaceCard() {
        JSONObject requestObj = new JSONObject();
        requestObj.put("address", model.getCardInfo() != null ? model.getCardInfo().getAddress() : "");
        requestObj.put("citizenId", model.getCardInfo() != null ? model.getCardInfo().getCitizen_id() + "" : "");
        requestObj.put("gender", model.getCardInfo() != null ? model.getCardInfo().getGender() : "");
        requestObj.put("nation", model.getCardInfo() != null ? model.getCardInfo().getNation() : "");
        requestObj.put("name", model.getCardInfo() != null ? model.getCardInfo().getName() : "");
        requestObj.put("validDateBegin", model.getCardInfo() != null ? model.getCardInfo().getValid_date_begin() : "");
        requestObj.put("validDateEnd", model.getCardInfo() != null ? model.getCardInfo().getValid_date_end() : "");
        requestObj.put("birthday", model.getCardInfo() != null ? model.getCardInfo().getBirthday() : "");
        requestObj.put("agency", model.getCardInfo() != null ? model.getCardInfo().getAgency() : "");
        requestObj.put("idFrontUrl", MiscUtils.isNotEmpty(model.getList()) ? model.getList().get(0).getUrl() : "");
        requestObj.put("idBehindUrl", model.getList() != null && model.getList().size() > 1 ? model.getList().get(1).getUrl() : "");
        requestObj.put("type", "FACE_PLUS_CARD");
        Call<ApiResponse> submitCall = RDClient.getService(AuthApi.class).submitIdNumberInfoForFacePlus(requestObj);
        NetworkUtil.showCutscenes(context, submitCall);
        submitCall.enqueue(new RequestCallBack<ApiResponse>() {
            @Override
            public void onSuccess(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (isAllowAuth) {
                    goToLiveness();
                } else {
                    checkFaceAuth();
                }
            }

            @Override
            public boolean errorHandle(ApiException apiException) {
                if (apiException.getCode() == 1310) {
                    TipsDialog dialog = new TipsDialog(context);
                    dialog.setTitle("提示");
                    dialog.setContent(apiException.getMessage());
                    dialog.show();
                } else {
                    UIUtils.showToast(apiException.getMessage());
                }
                return true;
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }

    /**
     * 打开face++人脸识别
     */
    private void goToLiveness() {
        Intent intent = new Intent(context, LivenessActivity.class);
        context.startActivityForResult(intent, FACE_ID_RESULT);
//        ActivityUtils.push(LivenessActivity.class, intent, FACE_ID_RESULT);
    }

    /**
     * 检查faceSDK授权
     */
    private void checkFaceAuth() {
        new HandleFaceAuthTask(context).setCallBack(new HandleAuthCallBack() {
            @Override
            public void authStart() {

            }

            @Override
            public void authSuccess() {
                isAllowAuth = true;
                goToLiveness();
            }

            @Override
            public void authError() {
                isAllowAuth = false;
            }
        }).setAuthType(HandleFaceAuthTask.AUTH_FACE).execute();
    }

    @Override
    public void handleLivenessResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == FACE_ID_RESULT) {
                LivenessData resultData = (LivenessData) data.getSerializableExtra("result");
                if (MiscUtils.isNotEmpty(resultData.getDelta()) && resultData.getImages() != null) {
                    if (resultData.getImages().containsKey("image_best")) {
                        try {
                            submitFace(resultData.getDelta(), resultData.getImages(), context);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (resultData.getResID() != 0) {
                    String errorMsg = AlaConfig.getResources().getString(resultData.getResID());
                    UIUtils.showToast(errorMsg);
                } else {
                    String errorMsg = AlaConfig.getResources().getString(R.string.liven_app_error);
                    UIUtils.showToast(errorMsg);
                }


            }
        }

    }
}
