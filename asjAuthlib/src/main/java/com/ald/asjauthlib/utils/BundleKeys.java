package com.ald.asjauthlib.utils;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/2/10 13:30
 * 描述：intent传值时的key
 * 修订历史：
 */
public class BundleKeys {
    //首页扫一扫
    public static final int REQUEST_CODE_MIAN_SCAN = 0x0101;

    public static final int REQUEST_CODE_BRAND = 0x0200;
    public static final int REQUEST_CODE_BRAND_PAY_INFO = 0x0201;
    public static final int REQUEST_CODE_BRAND_DIALOG_BANK_CARD = 0x0202;//选择银行卡
    public static final int REQUEST_CODE_LOAN = 0x0300;
    public static final int REQUEST_CODE_STAGE = 0x0400;
    public static final int REQUEST_CODE_STAGE_RR_IDF_FRONT = 0x0401;
    public static final int REQUEST_CODE_STAGE_RR_IDF_BACK = 0x0402;
    public static final int REQUEST_CODE_STAGE_APPEAL = 0x0404;

    public static final int REQUEST_CODE_MINE = 0x00500;
    public static final int REQUEST_CODE_LOGIN = 0x00600;
    public static final int REQUEST_CODE_MINE_ADD_BANK = 0x0501;
    public static final int REQUEST_CODE_COUPON_GET = 0x0502;
    public static final int REQUEST_CODE_MINE_UN_PAID = 0x0504;

    public static final int REQUEST_CODE_STEAD_PAY = 0x0701;
    public static final int REQUEST_CODE_STEAD_CANCEL = 0x0702;
    //获取通讯录手机号
    public static final int REQUEST_CODE_USER_LOGO = 0x0503;
    //请求
    public static final int REQUEST_IMAGE = 0x0601;
    public static final int REQUEST_CODE_CLIP_PHOTO = 0x0602;
    public static final int REQUEST_IMAGE_FRONT = 0x0621;
    public static final int REQUEST_IMAGE_BACK = 0x0622;
    //设置地址
    public static final int REQUEST_CODE_ADDRESS = 0x0603;
    //
    public static final int REQUEST_ADD_ADDRESS = 0x0604;
    //请求刷新地址code
    public static final int REQUEST_ADDRESS_REFRESH = 0x0604;
    //请求切换地址
    public static final int REQUEST_SELECT_ADDRESS = 0x0605;
    //请求修改地址
    public static final int REQUEST_MODIFY_ADDRESS = 0x0606;
    //请求切换地址
    public static final int REQUEST_SHOW_BIG_IMAGE = 0x0608;
    //订单关闭回调请求码
    public static final int REQUEST_ORDER_CLOSE = 0x0610;
    // 订单支付回调请求码
    public static final int REQUEST_ORDER_PAY = 0x0611;
    //订单完成回调请求码
    public static final int REQUEST_ORDER_FISH = 0x0612;
    //订单完成删除请求码
    public static final int REQUEST_ORDER_DELETE = 0x0613;
    //订单取消回调请求码
    public static final int REQUEST_ORDER_CANCEL = 0x0614;
    //订单详情请求码
    public static final int REQUEST_ORDER_DETAILS = 0x0615;
    //订单还款
    public static final int REQUEST_STAGE_REPAYMENT = 0x0616;
    //申请售后
    public static final int REQUEST_STATE_AFTER_SALE = 0x0617;
    //登录验证
    public static final int REQUEST_LOGIN_VERIFICATION = 0x0800;
    //借贷超市跳H5
    public static final int REQUEST_CODE_SUPERMARKET_H5 = 0x0801;
    //身份证跳转活体识别
    public static final int REQUEST_CODE_RRID = 0x0802;
    //代扣排序
    public static final int REQUEST_CODE_WITHHOLD_SORT_BANK_CARD = 0x0901;

    public static final int REQUEST_CODE_CALENDER = 0x1001;

    public static final int REQUEST_CODE_STAGE_LOGIN = 0x1002;

    //组合支付添加银行卡
    public static final int REQUEST_CODE_BANKCARD_ADD_CP = 0x1003;

    public static final int REQUEST_CODE_CASHIER = 0x1008;

    public static final int REQUEST_CODE_CP = 0x1009;//跳转组合支付
    //信用卡还款添加银行卡
    public static final int REQUEST_CODE_CREDIT_REFUND = 0X1010;
    //信用卡编辑还款日
    public static final int REQUEST_CODE_CREDIT_MANAGER = 0x1011;
    //信用卡未设置还款计划点还款
    public static final int REQUEST_CODE_CREDIT_N0_PLAN_MANAGER = 0x1012;

    //主页面跳转tab
    public static final String MAIN_DATA_TAB = "main_data_tab";
    //品牌orderId
    public static final String BRAND_FANBEI_ORDERID = "brand_fanbei_orderid";
    //品牌platform
    public static final String BRAND_PLATFORM = "brand_platform";

    //传验证码
    public static final String CAPTCHA_CODE = "captcha_code";
    //注册手机号
    public static final String EXTRA_REGISTER_PHONE = "extra_register_phone";
    /**
     * 搜呗页面
     */
    //搜呗搜索key
    public static final String SEARCH_KEY = "serach_key";
    //fragment传入参数
    public static final String FRAGMENT_DATA = "fragment_data";
    public static final String FRAGMENT_DATA1 = "fragment_data1";
    /**
     * 个人中心
     */
    //修改昵称
    public static final String SETTING_NICK = "setting_nick";
    //修改昵称返回值
    public static final String SETTING_NICK_RESULT = "setting_nick_result";
    //修改已经绑定过的手机号
    public static final String SETTING_BIND_IS_TEL = "setting_bind_is_tel";
    //已经绑定过的手机号或者邮箱
    public static final String SETTING_BIND_ACCOUNT = "setting_bind_account";
    //原手机号
    public static final String SETTING_ORIGINAL_PHONE = "setting_original_phone";
    //新手机号
    public static final String SETTING_NEW_PHONE = "setting_new_phone";
    //已实名认证没有支付密码
    public static final String SETTING_ID_AUTHED_NO_PWD = "setting_id_authed_no_pwd";
    //验证支付密码忘记支付密码
    public static final String SETTING_FORGOTTEN_PAY_PWD = "setting_forgotten_pay_pwd";
    //修改地址
    public static final String SETTING_ADDRESS = "setting_address";
    /**
     * 紧急联系人
     */
    public static final String CONTATCT_URGENT_TYPE = "contact_urgent_type";
    public static final String CONTATCT_URGENT_NAME = "contact_urgent_name";
    public static final String CONTATCT_URGENT_PHONE = "contact_urgent_phone";
    /**
     * 我的页面-余额提现
     */
    public static final String USER_LIMIT_GET = "user_limit_get";
    public static final String USER_LIMIT_GET_FLAG = "user_limit_get_flag";

    /**
     * 我的页面-订单
     */
    public static final String ORDER_ID = "orderId";
    /**
     * 商品详情页面
     */
    public static final String GOODS_DETAIL_DATA = "goods_detail_data";
    public static final String GOODS_DETAIL_GOODS_ID = "goods_detail_goods_id";
    public static final String GOODS_DETAIL_GOODS_NUM = "goods_detail_goods_num";
    public static final String GOODS_DETAIL_GOODS_AMOUNT = "goods_detail_goods_amount";
    public static final String GOODS_DETAIL_GOODS_NUMID = "goods_detail_goods_numid";
    public static final String GOODS_DETAIL_GOODS_WORM = "goods_detail_goods_worm";
    public static final String GOODS_DETAIL_GOODS_LC = "goods_detail_goods_lc";//商品来源
    public static final String MALL_ORDER_FROM = "mall_order_from";//确认订单来源
    public static final String GOODS_DETAIL_GOODS_AUTH = "goods_detail_goods_auth";//是否是权限包
    public static final String GOODS_DETAIL_SHOW_GOLD = "goods_detail_show_gold";//是否是权限包
    /**
     * 分呗
     */

    //实名认证逻辑、设置支付密码、添加银行卡结束时跳转
    public static final String STAGE_JUMP = "stage_jump";
    //人脸识别后绑定银行卡
    public static final String STAGE_BANK_CHECK = "stage_loan_bank_check";
    //借款额度认证-运营商回调
    public static final String STAGE_LOAN_PHONE_RESULT = "stage_loan_phone_result";
    //年月账单页面
    public static final String STAGE_BILL_YEAR = "stage_bill_year";
    public static final String STAGE_BILL_MONTH = "stage_bill_month";
    //账单id
    public static final String STAGE_BILL_ID = "stage_bill_id";
    //账单详情
    public static final String STAGE_BILL_DETAIL_TYPE = "stage_bill_detail_type";

    //refild
    public static final String LIMIT_REFILD_ID = "limit_refild_id";
    //type
    public static final String LIMIT_TYPE = "limit_type";
    /**
     * 还款模块-还款详情
     */
    //repayId
    public static final String LOAN_REPAYMENT_REPAY_ID = "loan_repayment_repay_id";
    //type
    public static final String LOAN_REPAYMENT_TYPE = "loan_repayment_type";
    //还款返回实体类
    public static final String LOAN_REPAYMENT_MODEL = "loan_repayment_model";
    /**
     * 优惠券
     */
    //优惠券列表
    public static final String COUPON_LIST_DATA = "coupon_list_data";
    public static final String COUPON_SELECT_DATA = "coupon_select_data";
    //优惠券列表回调
    public static final String COUPON_LIST_DATA_RESULT = "coupon_list_data_result";
    //取消使用优惠券
    public static final String COUPON_LIST_DATA_CANCEL = "coupon_list_data_cancel";
    //优惠券列表RequestCode
    public static final int REQUEST_CODE_COUPIN = 0x0001;
    //商品金额
    public static final String REQUEST_CODE_COUPIN_AMOUNT = "goods_amount";
    //商品ID
    public static final String REQUEST_CODE_COUPIN_GOODS_ID = "goods_id";
    //店铺ID
    public static final String REQUEST_CODE_COUPON_SHOP_ID = "shop_id";
    /**
     * 集分宝提取
     */
    public static final String USER_INTEGRAL = "user_integral";
    /**
     * 银行卡
     */
    //银行卡类型
    public static final String BANK_CARD_TYPE_SELETCT = "bank_card_type_select";
    //绑定银行卡时姓名
    public static final String BANK_CARD_NAME = "band_card_name";
    //绑定银行卡时校验银行页面
    public static final String BANK_CARD_CHECK = "band_card_check";
    //绑定银行卡时选择银行卡类型返回
    public static final String BANK_CARD_RESULT = "band_card_type_result";
    //更换主副卡银行卡实体类
    public static final String BANK_CARD_EDIT_MODEL = "bank_card_edit_model";
    //设置支付密码
    public static final String SETTING_PAY_PWD = "setting_pay_pwd";
    //修改支付密码
    public static final String SETTING_PAY_NEW_PWD = "setting_pay_new_pwd";
    //绑定的手机号
    public static final String SETTING_PAY_PHONE = "setting_pay_phone";
    //身份证号
    public static final String SETTING_PAY_ID_NUMBER = "setting_pay_id_number";
    //银行卡号
    public static final String SETTING_PAY_CARD_NUMBER = "setting_pay_card_number";
    //
    public static final String IS_UPLOAD_IMAGE = "is_upload_image";

    //是否为还款优惠券
    public static final String SELECT_COUPON_IS_STAGE = "is_stage";
    //我的页面是否绑定主卡
    public static final String SETTING_BIND_MAIN_CARD = "setting_bind_main_card";
    //我的頁面是否通過人臉識別
    public static final String SETTING_BIND_FACE = "setting_bind_face";

    //消息中心-是否系统通知
    public static final String MESSAGE_IS_SYSTEM = "message_is_system";
    //
    public static final String VERIFY_LOAN_INFO = "verify_pay_info";
    //小贷借款ID(白领贷的loanId)
    public static final String BORROW_ID = "borrow_id";
    //白领贷借款期数ID
    public static final String LOAN_PERIODS_ID = "loan_periods_id";
    //还款类型(小贷、白领贷)
    public static final String REPAYMENT_TYPE = "repayment_type";
    //白领贷还款类型(提前结清、正常还款)
    public static final String REPAYMENT_TYPE_WHITE_COLLAR = "repayment_type_white_collar";
    //借款附带的订单id
    public static final String BORROW_ORDER_ID = "borrow_order_id";
    //续借金额-合规
    public static final String RENEWAL_LEGAL_AMOUNT = "renewal_legal_amount";
    // 分期还款账单ids
    public static final String BILL_IDS = "bill_ids";
    //是否处于正在还款中Key
    public static final String CASH_LOAN_RENEWAL_STATUS = "casn_loan_renewal_status";
    //最大还款金额
    public static final String MAX_REPAYMENT_AMOUNT = "max_amount";

    //白领贷提前结清还款金额
    public static final String REPAYMENT_AMOUNT_BLD_ADVANCE = "repayment_amount_bld_advance";
    //
    public static final String REBATE_AMOUNT = "rebate_amount";
    //
    public static final String JIFENG_COUNT = "jifeng_count";
    //新老借钱页面跳转判断
    public static final String CASH_LOAN_PAGE_TYPE = "cash_loan_page_type";
    //续期天数
    public static final String RENEWAL_DAY = "renewal_day";

    //选择银行列表dialog
    public static final String DIALOG_SELECT_BANK_CARD_RESULT = "dialog_select_bank_card_result";
    //滞纳金页面borrowId
    public static final String CASH_OVERDUE_BORROW_ID = "cash_overdue_borrow_id";

    //我的优惠券页面(优惠券类型)
    public static final String VOUCHER_MENU_PLAYFORM = "voucher_menu_platform";

    //地址信息
    public static final String RECEIVING_ADDRESS = "receiving_address";

    //
    public static final String SUBMIT_TYPE = "submit_type";
    //
    public static final String SUBMIT_LOAN_SUCCESS = "loan_success";
    //
    public static final String SUBMIT_STEAD_BUY_SUCCESS = "stead_buy_success";
    //
    public static final String SELECT_ADDRESS = "select_address";
    //意图身份证信息
    public static final String RR_IDF_YITU_MODEL = "rr_idf_yitu_model";
    //意图身份证图片
    public static final String RR_IDF_YITU_PIC = "rr_idf_yitu_pic";

    //續期記錄ID
    public static final String DEFERRED_ID = "deferredId";

    public static final String DEFERRED_AMOUNT = "deferredAmount";

    public static final String DEFERRED_STATUS = "deferredStatus";

    public static final String BIG_IMAGE_PATH = "bigImagePath";


    //自营商城
    public static final String MALL_TYPE = "mall_type";
    public static final String MALL_GOODS_TROLLEY = "mall_goods_trolley";//购物车跳转到确认订单
    public static final String TROLLEY_DISCOUNT = "trolley_discount";//活动满减金额
    public static final String TROLLEY_DIFF_AMOUNT = "trolley_diff_amount";
    public static final String TROLLEY_ACTIVITY_AMOUNT = "trolley_activity_amount";//活动实际金额
    public static final String TROLLEY_ACTIVITY_THRESHOLD = "trolley_activity_threshold";
    public static final String SHOPPING_MALL_AMOUNT = "shopping_mall_amount";//接口计算的总金额(包含活动满减)

    //自营商品退款申请商品ID
    public static final String RETURN_ORDER_ID = "return_order_id";
    public static final String AFTER_SAIL_TYPE = "after_sail_type";
    //商品类型
    public static final String RETURN_GOODS_TYPE = "return_goods_type";
    //可使用的优惠券id
    public static final String USE_COUPON_ID = "use_coupon_id";

    //申请售后
    public static final String AFTER_SALE_GOODS_MODEL = "after_sale_goods_model";
    //跳转到指定提升信用页面
    public static final String JUMP_AUTH_CODE = "stead_buy_order_amount";
    //跳转到客服页面
    public static final String MINE_USER_INFO = "mine_user_info";

    // 我的页面在登录注册页面登录成功之后回调登录
    public static final String ON_ACTIVITY_RESULT = "call_back";
    // 独占控制待支付订单的提示
    public static final String ON_ACTIVITY_RESULT_2 = "call_back_2";
    // 更换手机号成功之后跳转登录页面
    public static final String ON_ACTIVITY_RESULT_CHANGE_MOBILE = "change_mobile";

    // 订单列表页面中类orderModel传递
    public static final String ORDER_MODEL = "order_model";

    // 订单列表页面中返利状态传递
    public static final String BUBBLE_OFFICIAL = "bubble_official";

    // 从主activity页面进入登录activity
    public static final String MAIN_ACTIVITY = "main_activity";
    // 从哪个activity页面来回到这个activity时的标记
    public static final String BACK_TO_ACTIVITY = "back_to_activity";

    public static final String REPLACE_MALLDETAILACTIVITY = "replace_malldetail";

    public static final String THIRDLEVELACTIVITY = "thirdlevel_activity";

    //借钱商品
    public static final String CASH_LOAN_GOODS_MODEL = "cash_loan_goods_model";
    //借钱收货人地址
    public static final String CASH_LOAN_RECEIVING_ADDRESS = "cash_loan_receiving_address";
    //借钱商品详情
    public static final String CASH_LOAN_GOODS_INFO = "cash_loan_goods_info";

    //订单详情页面订单ID
    public static final String ORDER_INFO_LEGAL_FRAG_ORDERID = "order_info_legal_frag_orderid";

    //何页面进入还款
    public static final String CASH_LOAN_REPAYMENT_FROM_PAGE = "cash_loan_repayment_from_page";

    // 灰度测试之区分哪个页面进入验证码验证页面
    public static final String FROM_ACTIVITY = "from_activity";

    //账号申诉旧账号
    public static final String APPEAL_NEWPHONE = "new_phone";

    //续期状态
    public static final String TXT_RENEW_STATUS = "txt_renew_status";


    //退还款记录选择的年份
    public static final String INTENT_KEY_REFUND_YEAR = "refund_record_selected_year";
    //退还款记录选择的月份
    public static final String INTENT_KEY_REFUND_MONTH = "refund_record_selected_month";

    public static final String INTENT_KEY_HISTORY_BILL_YEAR = "history_bill_year";

    public static final String INTENT_KEY_HISTORY_BILL_MONTH = "history_bill_month";

    public static final String INTENT_AMOUNT_ID = "amount_id";

    public static final String INTENT_SETTLE_BILL_ID = "settle_bill_id";

    public static final String STATUS_REFUND_RECORD = "refund";//退款

    public static final String STATUS_REPAYMENT_RECORD = "repayment";//还款

    public static final String INTENT_KEY_CREDIT_PROMOTE_SCENE = "credit_promote_scene";//认证场景

    public static final String CREDIT_PROMOTE_SCENE_CASH = "CASH";//CASH 现金贷场景
    public static final String CREDIT_PROMOTE_SCENE_ONLINE = "ONLINE";//ONLINE 线上分期
    public static final String CREDIT_PROMOTE_SCENE_TRAIN = "TRAIN";//TRAIN线下培训
    public static final String CREDIT_PROMOTE_SCENE_RENT = "RENT";//租房

    public static final String INTENT_KEY_STAGE_REAL_NAME = "stage_real_name";// 51信用首页跳转全部额度

    public static final String INTENT_KEY_CASHIER_BTN_TEXT = "cashier_btn_txt";//收银台跳转绑定银行卡按钮文案

    //收银台添加银行卡
    public static final String INTENT_KEY_CASHIER_ORDER_ID = "cashier_orderId";
    public static final String INTENT_KEY_CASHIER_NPER = "cashier_nper";//组合支付，期数
    public static final String INTENT_KEY_CASHIER_PAY_TYPE = "cashier_pay_type";

    //收货地址相关
    public static final String INTENT_KEY_ADDRESS_ID = "address_id";


    public static final int REQUEST_CREDIT_PROMOT_STATUS = 0x1003;//提交强风控跳转等待

    public static final int REQUEST_CODE_ALL_lIMITS = 0x1004;//提交强风控跳转等待

    public static final int REQUEST_CODE_CREDIT_PROMOTE_H5 = 0x1005;//线下培训场景跳转额度提升强风控跳转等待

    public static final int REQUEST_CODE_CREDIT_PROMOTE_START_AUTH = 0x1006;//来自首次注册认证引导

    public static final int REQUEST_CODE_CREDIT_PROMOTE_REFRESH = 0x1007;//来自首次注册认证引导

    public static final int REQUEST_CODE_BANKCARD_SCAN = 0x1020;//跳转银行卡扫描页

    public static final int REQUEST_CODE_SOCIAL_CONTACT = 100;//跳转冒泡

    public static final String MINE_TO_ORDERLIST_PAGE = "current_page";

    public static final String CASH_LOAN_SHOW_BLD_INDEX = "cash_loan_show_bld_index";//是否显示白领贷首页

    public static final String MAIN_TO_SORT_PAGE = "sort_page";//品牌H5跳品牌TAB

    public static final String MAIN_BOTTOM_H5_URL = "main_bottom_h5_url";

    public static final String CATEGORY_ID = "category_id";

    public static final String CONTENT = "content";

    public static final int REQUEST_CODE_CASHIER_BANK_CARD = 0x1021;//收银台跳转添加银行卡

    public static final int REQUEST_CODE_BANK_CARD_ADD_PWD = 0x1022;//添加銀行卡時設置密碼

    public static final int REQUEST_CODE_BANK_CARD_ADD = 0x1023;

    public static final int REQUEST_CODE_ALL_BILLS = 0x1024;  //全部待还

    public static final String BANK_CARD_INFO = "bank_card_info";

    public static final String MODIFY_MOBILE = "modify_mobile";

    public static final String CASHIER_FAIL_PARAMS_JSON = "cashier_fail_params_json";//跳转支付失败页面的json类型参数

    public static final String CASHIER_PERMISSION_ORDER_TO_PAY = "cashier_permission_order_to_pay";//权限包购买成功页面，待支付订单数量

    public static final String SEARCH_KEY_TO_H5 = "searchkey_h5";//跳转到搜索结果页携带的key

    public static final String SERVICE_INTENT_RAIN_ROUNDS_LIST = "rounds_list";//红包雨场次

    public static final String SERVICE_INTENT_SERVICE_TIME = "service_time";//服务器时间
}
