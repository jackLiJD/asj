package com.ald.asjauthlib.authframework.core.network;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/1/6
 * 描述：网络请求参数
 * 修订历史：
 */
public class RequestParams {


    // 登录参数
    public static final String TOKEN = "oauthToken";
    public static final String USER_ID = "userId";
    public static final String REFRESH_TOKEN = "refresh_token";
    ///////////////////////////////////////////////////////////////////////////
    // 登录请求参数
    ///////////////////////////////////////////////////////////////////////////
    public static final String ID = "id";
    public static final String PWD = "pwd";
    public static final String PHONE = "phone";
    public static final String CODE = "code";

    /**
     * IMEI相关
     */
    public static final String LANREN = "ackToken";
    public static final String LANREN_IMEI = "ackAppkey";
    public static final String LANREN_IMEI_NUM = "291DE5F14A880DBC78A401856EE5771C";

    //分页参数
    public static final String PAGE = "page";
    public static final String PAGE_SIZE = "pagesize";
    //产品相关
    public static final String UUID = "uuid";
    public static final String MONEY = "money";
    public static final String DIRPWD = "pwd";
    public static final String PAYPASSWORD = "paypwd";
    public static final String REDIDS = "red_ids";
    public static final String EXPIDS = "experience_ids";
    public static final String UPIDS = "up_rate_id";
    public static final String SESSION_ID = "session_id";
    public static final String NEW_PWD = "new_pwd";
    public static final String OLD_PWD = "old_pwd";
    public static final String NEW_PAYPWD = "new_paypwd";
    public static final String OLD_PAYPWD = "old_paypwd";
    //实名认证
    public static final String REALNAME = "realname";
    public static final String CARDID = "card_id";
    //消息
    public static final String TYPE = "type";
    //记录相关
    public static final String STATUS = "status";
    public static final String TENDER_ID = "tender_id";
    //双乾授权
    public static final String AUTHORIZE_TYPE = "type";
    public static final String AUTHORIZE_ONOFF = "on_off";
    //重置密码
    public static final String RESETPAYPWD_NEWPWD = "new_paypwd";
    public static final String RESETPAYPWD_IDCARD = "id_card";
    public static final String SETPAYPWD_PWD = "payPwd";
    public static final String PHONE_OR_EMAIL = "phone_or_email";




}
