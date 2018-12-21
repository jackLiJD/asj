package com.ald.asjauthlib.auth;

import com.ald.asjauthlib.auth.model.AllLimitModel;
import com.ald.asjauthlib.auth.model.AuthResultModel;
import com.ald.asjauthlib.auth.model.AuthStrongRiskModel;
import com.ald.asjauthlib.auth.model.AuthTdModel;
import com.ald.asjauthlib.auth.model.AuthUserBasicInfoModel;
import com.ald.asjauthlib.auth.model.AuthYdInfoModel;
import com.ald.asjauthlib.auth.model.BankCardCheckModel;
import com.ald.asjauthlib.auth.model.BankCardModel;
import com.ald.asjauthlib.auth.model.CodeConfirmModel;
import com.ald.asjauthlib.auth.model.ContactorUrgentModel;
import com.ald.asjauthlib.auth.model.ContactsSycModel;
import com.ald.asjauthlib.auth.model.ExtraAuthModel;
import com.ald.asjauthlib.auth.model.ExtraFundModel;
import com.ald.asjauthlib.auth.model.ExtraGxbModel;
import com.ald.asjauthlib.auth.model.ExtraUserIdModel;
import com.ald.asjauthlib.auth.model.FaceTypeModel;
import com.ald.asjauthlib.auth.model.FundModel;
import com.ald.asjauthlib.auth.model.PhoneOperatorModel;
import com.ald.asjauthlib.auth.model.RealNameModel;
import com.ald.asjauthlib.auth.model.SendAddBindBankcardMsgModel;
import com.ald.asjauthlib.auth.model.YiTuModel;
import com.ald.asjauthlib.auth.model.ZMXYModel;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.network.entity.ApiResponse;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 认证api
 * Created by sean yu on 2017/7/11.
 */
public interface AuthApi {

    /**
     * 公积金认证
     */
    @POST("/auth/authFund")
    Call<ExtraUserIdModel> authFund();


    /**
     * 社保认证
     */
    @POST("/auth/authSocialSecurity")
    Call<ExtraUserIdModel> authSocialSecurity();

    /**
     * 支付宝认证
     */
    @POST("/auth/authAlipay")
    Call<ExtraUserIdModel> authAlipay();


    /**
     * 信用卡认证
     */
    @POST("/auth/authCreditCard")
    Call<ExtraUserIdModel> authCreditCard();

    /**
     * 人行征信
     */
    @POST("/auth/authZhengxin")
    Call<ExtraUserIdModel> authZhengxin();

    /**
     * 学信网
     */
    @POST("/auth/authChsi")
    Call<ExtraUserIdModel> authChsi();


    /**
     * 网银
     */
    @POST("/auth/authOnlinebank")
    Call<ExtraUserIdModel> authOnlineBank();

    /**
     * 公信宝
     */
    @POST("/auth/authGxb")
    Call<ExtraGxbModel> authGxb();


    /**
     * 获取补充认证信息
     */
    @POST("/auth/authSupplyCertify")
    Call<ExtraAuthModel> authSupplyCertify();

    /**
     * 同步认证信息
     */
    @POST("/auth/authSupplyVerifying")
    Call<ApiResponse> authSupplyVerifying(@Body JSONObject object);

    /**
     * 手动修改证件名(申诉免登陆)
     */
    @POST("/auth/updateRealnameManualFree")
    Call<ApiResponse> updateRealnameManualFree(@Body JSONObject object);

    /**
     * 手动修改证件名
     */
    @POST("/auth/updateRealnameManual")
    Call<ApiResponse> updateRealnameManual(@Body JSONObject object);

    /**
     * 绑定银行卡前的短信校验397
     */
    @POST("/auth/checkMessages")
    Call<ApiResponse> checkMessages(@Body JSONObject object);

    /**
     * 绑定银行卡397
     */
    @POST("/auth/bindingBankcard")
    Call<BankCardCheckModel> bindingBankcard(@Body JSONObject object);

    /**
     * 获取人脸识别类型
     */
    @POST("/auth/getFaceType")
    Call<FaceTypeModel> getFaceType();

    /**
     * 获取人脸识别类型(免登陆)
     */
    @POST("/auth/getFaceTypeFree")
    Call<FaceTypeModel> getFaceTypeFree();

    /**
     * 查看全部额度
     */
    @POST("/auth/lookAllQuota")
    Call<AllLimitModel> lookAllQuota();

    @POST("/auth/authFundNew")
    Call<ExtraFundModel> authFundNew();

    @POST("/auth/authFundNew/GiveBack")
    Call<ApiResponse> giveBack(@Body JSONObject object);

    /**
     * 获取支付时银行卡列表
     */
    @POST("/auth/authRealnameV1")
    Call<AuthTdModel> authRealname();

    /**
     * 提交运营商认证
     */
    @POST("/auth/authMobileBack")
    Call<ApiResponse> authMobileBack();

    /**
     * 定位地址提交
     */
    @POST("/auth/authLocation")
    Call<ContactsSycModel> authLocation(@Body JSONObject request);


    /**
     * 依图提交正反面身份证信息
     */
    @POST("/auth/checkIdCardApi")
    Call<YiTuModel> checkIdCardApi(@Body JSONObject request);

    /**
     * 依图确认身份
     */
//    @POST("/auth/saveIdNumber")
    @POST("/auth/submitIdNumberInfoV1")
    Call<ApiResponse> submitIdNumberInfo(@Body JSONObject request);

    /**
     * face++ 確認身份
     */
    @POST("/auth/submitIdNumberInfoForFacePlus")
    Call<ApiResponse> submitIdNumberInfoForFacePlus(@Body JSONObject requestObj);

    /**
     * 依图人脸识别
     */
    @POST("/auth/checkFaceApi")
    Call<ApiResponse> checkFaceApi(@Body JSONObject request);

    @POST("/auth/authStrongRisk")
    Call<AuthStrongRiskModel> authStrongRisk(@Body JSONObject request);


    @POST("/auth/authStrongRiskV1")
    Call<AuthStrongRiskModel> authStrongRiskV1(@Body JSONObject request);

    /**
     * 实名认证
     */
    @POST("/auth/authRealnameV1")
    Call<RealNameModel> authRealName(@Body JSONObject request);


    /**
     * 芝麻信用认证
     */
    @POST("/auth/authCreditV1")
    Observable<ZMXYModel> authCredit(@Body JSONObject request);


    /**
     * 人脸识别
     */
    @POST("/auth/authFaceV1")
    Call<AuthResultModel> authFace(@Body JSONObject request);


    /**
     * 人脸识别
     */
    @POST("/auth/authYdInfo")
    Call<AuthYdInfoModel> authYdInfo();


    /**
     * 通讯录授权
     */
    @POST("/auth/authContactsV1")
    Call<ContactsSycModel> authContacts(@Body JSONObject request);

    /**
     * 获取手机运营商url
     */
    @POST("/auth/authMobile")
    Call<PhoneOperatorModel> authMobile();

    /**
     * 紧急联系人提交
     */
    @POST("/auth/authContactorV1")
    Call<ContactorUrgentModel> authContactor(@Body JSONObject request);

    /**
     * 补充认证提额
     */
    @POST("/auth/authRaiseQuota")
    Call<ApiResponse> authRaiseQuota(@Body JSONObject request);

    @POST("/auth/authFundSwitch")
    Call<FundModel> authFundSwitch();

    /**
     * 绑定银行卡含必要支付V4.1.2
     */
    @POST("/auth/submitBindBankcard")
    Call<ApiResponse> submitBindBankcard(@Body JSONObject object);

    /**
     * 获取用户基础认证信息V4.1.2
     */
    @POST("/auth/checkUserBasicInfo")
    Call<AuthUserBasicInfoModel> checkUserBasicInfo(@Body JSONObject object);

    /**
     * 判断身份证号是否已经绑定其他账号
     */
    @POST("/auth/checkUserIdCardInfo")
    Call<ApiResponse> checkUserIdCardInfo(@Body JSONObject object);

    @POST("/auth/applyBindBankcard")
    Call<SendAddBindBankcardMsgModel> applyBindBankcard(@Body JSONObject object);

    /**
     * 核对银行卡信息
     */
    @POST("/auth/bankcardInfo")
    Call<BankCardModel> searchCardInfo(@Body JSONObject object);

    /**
     * 修改银行卡时校验手机号
     * @param object
     * @return
     */
    @POST("/auth/applyEditBankCardInfo")
    Call<CodeConfirmModel> applyEditBankCardInfo(@Body JSONObject object);

    /**
     * 提交修改银行卡
     * @param object
     * @return
     */
    @POST("/auth/submitEditBankCardInfo")
    Call<String> submitEditBankCardInfo(@Body JSONObject object);

}
