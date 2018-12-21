package com.ald.asjauthlib.auth;

import com.ald.asjauthlib.auth.model.BankCardGetCaptchaodel;
import com.ald.asjauthlib.auth.model.BankCardTypeListModel;
import com.ald.asjauthlib.auth.model.BankListModel;
import com.ald.asjauthlib.auth.model.ThirdLoginModel;
import com.ald.asjauthlib.auth.model.WithholdBankCardResponseModel;
import com.ald.asjauthlib.auth.model.WithholdSettingsCheckModel;
import com.ald.asjauthlib.auth.model.WithholdSettingsModel;
import com.ald.asjauthlib.auth.model.WithholdSwitchModel;
import com.ald.asjauthlib.auth.model.WithholdUseBalanceModel;
import com.ald.asjauthlib.cashier.model.MyTicketListModel;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.network.entity.ApiResponse;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/1/9
 * 描述： 账户接口api
 * 修订历史：
 */
public interface UserApi {


    /**
     * 验证码接口，验证码类型 R：regist注册验证码  F：forget忘记密码验证码 M:绑定手机 L：登录验证码 C:通用
     */
    @POST("/user/getVerifyCode")
    Call<Boolean> getVerifyCode(@Body JSONObject request);


    /**
     * 账号申诉验证码验证
     */
    @POST("/user/accountAppealCheckSms")
    Call<ApiResponse> accountAppealCheckSms(@Body JSONObject jsonObject);

    /**
     * 账号申诉
     */
    @POST("/user/accountAppealDo")
    Call<ApiResponse> accountAppealDo(@Body JSONObject jsonObject);

    /**
     * 获取银行卡类型列表
     */
    @POST("/auth/getBankList")
    Call<BankCardTypeListModel> getBankList();

    /**
     * 银行卡列表接口
     */
    @POST("/user/getBankCardList")
    Call<BankListModel> getBankCardList(@Body com.alibaba.fastjson.JSONObject request);

    /**
     * 删除银行表接口
     */
    @POST("/user/deleteBankCard")
    Call<ApiResponse> deleteBankCard(@Body com.alibaba.fastjson.JSONObject request);

    /**
     * 银行卡取消主副卡限制
     */
    @POST("/user/replaceMainCard")
    Call<ApiResponse> replaceMainCard(@Body JSONObject reqObj);

    /**
     * 修改支付密码
     */
    @POST("/user/checkPayPwdVerifyCode")
    Call<BankCardGetCaptchaodel> checkPayPwdVerifyCode(@Body com.alibaba.fastjson.JSONObject request);


    /**
     * 获取支付密码的验证码
     */
    @POST("/user/getPayPwdVerifyCode")
    Call<ApiResponse> getPayPwdVerifyCode();


    /**
     * 设置支付密码时身份验证
     */
    @POST("/user/checkIdNumber")
    Call<Boolean> checkIdNumber(@Body com.alibaba.fastjson.JSONObject request);


    /**
     * 修改支付密码
     */
    @POST("/user/setPayPwd")
    Call<ApiResponse> setPayPwd(@Body com.alibaba.fastjson.JSONObject request);

    /**
     * 检查是否可设置代扣
     */
    @POST("/Withhold/WithholdCheck")
    Call<WithholdSettingsCheckModel> withholdCheck();

    /**
     * 验证支付密码
     */
    @POST("/user/checkPayPwd")
    Call<ApiResponse> checkPayPwd(@Body com.alibaba.fastjson.JSONObject request);

    /**
     * 获取代扣信息
     */
    @POST("/Withhold/getWithholdInfo")
    Call<WithholdSettingsModel> getWithholdInfo();


    /**
     * 开启/关闭代扣
     */
    @POST("/Withhold/updateWithholdSwitch")
    Call<WithholdSwitchModel> updateWithholdSwitch(@Body JSONObject reqObj);

    /**
     * 开启/关闭余额(代扣排序)
     */
    @POST("/Withhold/updateWithholdCard")
    Call<WithholdUseBalanceModel> updateWithholdCard(@Body JSONObject reqObj);

    /**
     * 获取所有银行卡(代扣)
     */
    @POST("/Withhold/showUserBankCard")
    Call<WithholdBankCardResponseModel> showUserBankCard();

    /**
     * 获取优惠券列表
     */
    @POST("/user/getMineCouponList")
    Call<MyTicketListModel> getMineCouponList(@Body JSONObject request);

    /**
     * 钱包同步登录
     */
    @POST("/third/fenqi/doLogin")
    Observable<ThirdLoginModel> doLogin(@Body JSONObject request);
}
