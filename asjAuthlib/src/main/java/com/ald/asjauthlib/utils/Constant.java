package com.ald.asjauthlib.utils;

import android.os.Environment;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/2/28 19:33
 * 描述：
 * 修订历史：
 */
public interface Constant {
    /*==================================常量String=========================================*/
    String WX_KEY = "wx7b2b0aa8b3f0459e";
    String USER_ID_ENCRYPTION_STR = "asj";
    String APP_GUIDE_SHOW_TAG = "__app_guide_show_tag";
    String APP_STEAD_BUY_TAG = "__app_guide_stead_buy_tag";
    String APP_PREFERENCE_NAME = "ala_51fanbei";
    String APP_GUIDE_STAGE_UN_AUTH = "__app_guide_stage_un_auth";
    String APP_GUIDE_STAGE_AUTH = "__app_guide_stage_auth";
    String APP_GUIDE_MINE = "__app_guide_mine";
    String GUIDE_PAGE_BRAND = "__app_guide_brand";
    String GUIDE_PAGE_LOAN = "__app_guide_loan";
    String GUIDE_PAGE_STEAD_BUY = "__app_guide_stead_buy";
    String GUIDE_PAGE_HOME = "__app_guide_home";
    //红包雨action
    String ACTION_RED_PACKAGE_RAIN = "red_rain";
    //电子签章相关参数
    String AGREEMENT_BILL_TYPE = "agreement_bill_type";
    String AGREEMENT_BILL_ID = "agreement_bill_id";
    String USER_PHONE = "user_phone";
    String USER_ID = "user_id";
    String PHONE = "phone";
    String PHONE_TIP = "历史记录";
    String PHONE_HISTORY = "phone_history";
    String TASKBROWSETIME = "taskBrowseTime";
    String FIRST_TIME = "FirstTime";
    String LOAN_REPAYMENT_TYPE_WHITE_COLLAR_COMMON = "commonSettle";//白领贷正常还款
    String LOAN_REPAYMENT_TYPE_WHITE_COLLAR_FORWARD = "forwardSettle";//白领贷提前还款
    String CASH_LOAN_SHOW_LOAN_INDEX = "0";//显示极速贷页面
    String CASH_LOAN_SHOW_BLD_INDEX = "1";//显示白领贷页面
    String DownloadUrl = "http://sftp.51fanbei.com/jiekuanchaoren_v3.9.1_app.apk";
    String SuperMan = "com.alfl.www.borrowSuperman";
    String SuperBannerTitle_1 = "借款超人-即刻申请借款";
    String SuperBannerTitle_2 = "借款超人-即刻下载申请";
    String SuperBannerAction_1 = "立即打开";
    String SuperBannerAction_2 = "立即下载";
    String SuperBannerAction_3 = "立即安装";
    String SuperBannerAction_4 = "下载中";
    String CloseSuperLoan = "CloseSuperLoan";//banner点击事件
    String OpenSuperLoan = "OpenSuperLoan";//banner点击事件
    String DownloadKey = "DownloadSuperLoanId";
    String ShowDownloadSuperLoan = "showDownloadSuperLoan";//弹窗弹出
    String ClickDownloadSuperLoan = "clickDownloadSuperLoan";//点击下载或打开事件
    String ClickCancelDownloadSuperLoan = "clickCancelDownloadSuperLoan";//关闭事件
    String CURRENT_TIME = "current_time"; // 年月日yyyyMMdd形式的int值
    String SYS_MESS_COUNT = "sys_mess_count"; // 应用通知关闭情况下弹窗提示用户次数
    String DETAIL_COUNT_GOLD = "count_gold"; //详情页计时
    String SHARE_MINIPROGREAM_DETAIL = "/pages/goods_detail?id=%1$s&userName=%2$s";//详情页小程序分享
    String HOME_DATA_CACHE = "home_data_cache";//首页数据缓存
    String HAVE_USED = "二手";//审核用,有这两个字的要隐藏
    String SHOW_DETAIL_LOADVIEW = "show_detail_loadview";//是否显示详情页加载view
    String VERSION_CODE_CACHE = "version_code_cache";//当前版本号缓存
    String FROM_TASK = "drawGold=true";//来自任务列表
    String FILELOGSWITCH = "fileLogSwitch";//是否需要开启埋点文件及上传
    String CRASHEXCEPTION = "crash_exception";
    String FIRST_INTO_MINE = "first_into_mine";//第一次进入我的页面
    String COLLECT_PAGE = "firstIndex";//收藏夹页码
    String cacheText = "livenessDemo_text";
    String cacheImage = "livenessDemo_image";
    String cacheVideo = "livenessDemo_video";
    String cacheCampareImage = "livenessDemo_campareimage";
    String dirName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/faceapp";
    String ORDER_FROM_SUREORDER="0";
    String ORDER_FROM_ORDERLIST="1";


    /*==================================常量int=========================================*/
    //我的优惠券类型(0:平台抵用券 1:品牌抵用券)
    int VOUCHER_MENU_TYPE_PLATFORM = 0;
    int VOUCHER_MENU_TYPE_BRAND = 1;
    //贷款还款类型
    int LOAN_REPAYMENT_TYPE_PETTY = 0;//小贷
    int LOAN_REPAYMENT_TYPE_WHITE_COLLAR = 1;//白领贷


    /*==================================常量H5=========================================*/
    String H5_URL_99 = "app/goods/goodsListModel?modelId=2";//9.9包邮
    String H5_URL_SERVER = "fanbei-web/app/protocolService";//服务协议
    String H5_URL_REGISTER = "fanbei-web/app/protocolRegister";//注册协议
    String H5_URL_LOAN = "fanbei-web/app/protocolBorrow";//借款协议
    String H5_URL_LOAN_GOODS = "fanbei-web/app/protocolFenqiBorrow";//消费借款协议
    String H5_URL_STAGE_GUIDE = "fanbei-web/app/fenbeiTutorial";//分呗新手教程协议
    String H5_URL_DESC = "fanbei-web/app/protocolPersonalInfo";//个人使用说明
    String H5_URL_REBATE_GUIDE = "fanbei-web/app/rebateTutorial";//返利教程
    String H5_URL_VERIFY_LOAN_WITH_NO = "fanbei-web/app/protocolCashLoan?userName=%1$s&borrowAmount=%2$s&borrowId=%3$s";//个人现金服务合同
    String H5_URL_VERIFY_LOAN_WITH_NO_LEGAL = "/fanbei-web/app/protocolLegalCashLoan?userName=%1$s&borrowId=%2$s&borrowAmount=%3$s&type=%4$s";//个人现金服务合同(借款协议)-合规
    String H5_URL_VERIFY_LOAN_WITH_NO_LEGAL_V2 = "/fanbei-web/app/protocolLegalCashLoanV2?userName=%1$s&borrowId=%2$s&borrowAmount=%3$s&type=%4$s";//个人现金服务合同(借款协议)-合规
    String H5_URL_STEP_BUY = "fanbei-web/app/protocolDaimai";//代买协议
    String H5_URL_FENQI_SERVER = "fanbei-web/app/protocolFenqiService?userName=%1$s&nper=%2$s&amount=%3$s&poundage=%4$s&borrowId=%5$s";//分期服务协议
    String H5_URL_FENQI_SERVER_V2 = "/fanbei-web/app/protocolLegalInstalmentV2?userName=%1$s&nper=%2$s&amount=%3$s&poundage=%4$s&borrowId=%5$s";//分期服务协议V2
    String H5_URL_FENQI_SERVER_LEGAL = "/fanbei-web/app/protocolLegalInstalment?userName=%1$s&nper=%2$s&amount=%3$s&type=%4$s&orderId=%5$s";//分期服务协议-合规
    String H5_URL_RENEWAL_AGREEMENT = "fanbei-web/app/protocolRenewal?userName=%1$s&renewalId=%2$s&renewalAmount=%3$s&renewalDay=%4$s&borrowId=%5$s&appVersion=%6$s";//续期协议
    String H5_URL_RENEWAL_AGREEMENT_LEGAL = "/fanbei-web/app/protocolLegalRenewal?userName=%1$s&renewalId=%2$s&borrowId=%3$s&renewalDay=%4$s&renewalAmount=%5$s&type=%6$s";//续期协议-合规
    String H5_URL_HELP = "fanbei-web/app/help";//帮助中心
    String H5_URL_WIN_RANK = "fanbei-web/app/inviteLastWinRank";//邀请有礼上月中奖排行
    String H5_URL_LOGISTICS = "fanbei-web/app/delivery?orderId=%1$s&traces=%2$s"; // 物流详情
    String H5_URL_LOGISTICS_NEW = "fanbei-web/app/deliveryNew?orderId=%1$s&traces=%2$s"; // 物流详情-合规
    String H5_URL_SUPERMARKET_ACTIVITY = "/fanbei-web/activity/borrowDetails";    // 借贷超市活动
    String H5_URL_NEWINVITE = "fanbei-web/app/newinvite";    // 邀请有礼
    String H5_URL_RED_PACKAGE_RAIN = "/fanbei-web/activity/redRainActivity?showTitle=false";    // 红包雨活动
    String H5_URL_DIGITAL_CERTIFICATE = "/fanbei-web/app/numProtocol?userName=%s";    // 数字证书服务协议
    String H5_URL_RISK_PROMPT = "/app/sys/riskWarning";    // 风险提示函
    String H5_URL_BORROW_SUPERMARKET = "https://app.51fanbei.com/fanbei-web/activity/otherBorrow?source=BORROWMARKETMAIDIAN";  // 借贷超市
    String H5_URL_AHEAD_RETURN = "fanbei-web/app/help?tab=third&lis=whatisahead";
    String H5_URL_PLATFORM_AGREEMENT = "fanbei-web/app/platformServiceProtocolInstalment?userName=%1s&nper=%2s&borrowAmount=%3s&poundage=%4s";//平台服务协议
    String H5_URL_BILL_DATE_MODIFY = "/fanbei-web/activity/cdynamicBill";//修改账单日
    String H5_LOAN_INDEX_BDL = "/h5/whiteCollar/borrowIndex.html";//白领贷借钱首页h5
    String H5_LOAN_DETAIL_BLD = "/h5/whiteCollar/borrowDetail.html?id=%1$s";//白领贷借款详情
    String H5_RENT_SUCCESS = "/h5/hire/success.html?showTitle=false&orderId=%1$s";//租赁成功
    String H5_RENT_DETAIL = "/h5/hire/orderdetail.html?showTitle=false&id=%1$s";//租赁详情
    String H5_MYTICKET = "h5/tickets/index.html";
    String H5_DOWNLOAN_URL_MAOPAO = "https://www.maopp.cn/download.html";//冒泡下载地址
    String H5_APPLY_CREDIT_CARD = "fanbei-web/activity/card?type=icon";//老项目申请信用卡
    String H5_RECYCLE = "h5/lease/midpage1.html";//闲置回收
    String H5_GET_GOLD = "h5/invite3/myCoin.html?spread=app&refreshUrl=true&showTitle=false";//金币h5
    String H5_HAITAOGUAN = "https://h5.51fanbei.com/h5/activity/201804/visualization.html?addRightUiName=SEARCH&id=446&spread=app";//审核版海淘馆
    String H5_TEJIA = "http://t.cn/RdwxrLJ";//审核特价h5
    String H5_XINPIN = "http://t.cn/RduOeih";//审核新品h5
    String H5_PINPAITEMAI = "https://h5.51fanbei.com/h5/h5brands/h5brands.html?tag=2";//审核品牌特卖h5
    String H5_TIMELIMITSHOP = "https://h5.51fanbei.com/h5/asj/scekill/scekill.html?spread=app";//审核限时抢购h5
    String H5_PINTUAN_GOODS_DETAIL = "asj/collage/goodDetail?goodsId=%1$s&showTitle=false";//拼团商品详情h5
    String H5_PINTUAN_SUCCESS = "asj/collage/payResult?orderId=%1$s&refreshUrl=false";//拼团支付成功h5
    String H5_PINTUAN_DETAIL = "asj/collage/orderDetail?orderId=%1$s&refreshUrl=false";//拼团详情h5
    String H5_FQ_SHOP="asj/fqshop/fqshop?showTitle=false";//分期商城
    String H5_ORDER_LIST_ALL="asj/fqshop/orders?fromClient=true&showTitle=false";//订单列表(全部)
    String H5_ORDER_LIST_DAIZHIFU="asj/fqshop/orders?fromClient=true&type=NEW&showTitle=false";//订单列表(待支付)
    String H5_ORDER_LIST_DAIFAHUO="asj/fqshop/orders?fromClient=true&type=TODELIVER&showTitle=false";//订单列表(待发货)
    String H5_ORDER_LIST_DAISHOUHUO="asj/fqshop/orders?fromClient=true&type=DELIVERED&showTitle=false";//订单列表(待收货)


    //好友代付
    String H5_FRIEND_PAY = "asj/friendPay?orderId=%1$s&amount=%2$s";
    String H5_PAY_HELP = "asj/friendPay/payment?showTitle=false&spread=app";


    /*==================================H5地址和后端地址分离(即url内含javaurl字段的)=========================================*/
    String H5_URL_SEARCH_GOODS = "h5/searchRoute/searchRoute.html?showTitle=false&javaurl=%1$s&httptype=%2$s&searchGoodName=%3$s&params={\"lc\":\"search_result\"}"; // 商品搜索列表
    String H5_OTHER_REPAYMENT = "h5/otherRepay/otherRepay.html?javaurl=%1$s&httptype=%2$s";//其他还款方式
    String H5_CASHBACK = "/h5/lease/wallet.html?";
    String H5_BRAND_LIST = "h5/asj/asjBrand/asjBrand.html?showTitle=false&javaurl=%1$s&httptype=%2$s&brandId=%3$s";//品牌列表
    String H5_ALL_BILLS = "h5/asj/asjBrand/allBills.html?javaurl=%1$s&httptype=%2$s";//全部待还
    String H5_LIFE = "h5/asj/asjLife/asjLife.html?javaurl=%1$s&httptype=%2$s&showTitle=false";//生活
    String H5_LIFE_NEW = "h5/asj/asjLife/asjLife.html?javaurl=%1$s&httptype=%2$s&showTitle=false&isNewTitle=1";//生活,区别老版本的头部
    String H5_PAY_SUCCESS = "/h5/asj/asjBrand/payResult.html?javaurl=%1$s&httptype=%2$s&orderId=%3$s&showTitle=false";//自营支付成功,使用h5title
    String H5_CREDIT_REFUND_BILL = "h5/creditCardUp/billList.html?javaurl=%1$s&httptype=%2$s&cardId=%3$s";//信用卡还款账单
    String H5_CREDIT_REFUND_RESULT = "h5/creditCardUp/refundStatus.html?javaurl=%1$s&httptype=%2$s&refstu=%3$s";//信用卡还款结果  0 成功 1失败


    //测试环境
    String appServerTest = "https://btestapp.51fanbei.com:443/";
//    String appServerTest = "http://172.20.17.6:4444/";
    String imageServerTest = "http://testfile.51fanbei.com/";
    String h5ServerTest = "https://testh5.51fanbei.com/";
//    String h5ServerTest = "http://172.20.47.32:8080/";


    // 预发环境
    String appServerPreview = "https://yapp.51fanbei.com/";
    String imageServerPreview = "https://yfile.51fanbei.com/";
    String h5ServerPreview = "https://yh5.51fanbei.com/";

    // 正式环境
    String appServer = "https://app.51fanbei.com/";
    String imageServer = "https://file.51fanbei.com/";
    String h5Server = "https://h5.51fanbei.com/";
}
