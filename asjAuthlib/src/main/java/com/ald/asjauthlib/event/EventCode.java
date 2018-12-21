package com.ald.asjauthlib.event;

/**
 * Created by Yangyang on 2018/4/16.
 * desc:
 */

public class EventCode {
    //测试用
    public static final int TEST_CODE = 0;
    //品牌H5跳分类的品牌页
    public static final int SORT_TO_BRAND = 1;
    //完成绑卡传递卡信息到收银台
    public static final int BIND_CARD_TO_PAYMENT = 2;
    //收银台刷新
    public static final int CASHIER_RELOAD = 3;
    //订单列表刷新
    public static final int ORDER_LIST_RELOAD = 4;
    //还款页面刷新
    public static final int REPAYMENT_RELOAD = 5;
    // 代表用户当前登录状态的改变,登录or退出登录 都会发送该消息事件
    public static final int USER_STATUS_CHANGE = 6;
    //下载借贷超人刷新文案
    public static final int CASH_TEXT_REFRESH = 7;
    //刷新首页购物车数量
    public static final int REFRESH_TROLLEY_COUNT = 8;
    //刷新购物车
    public static final int REFRESH_TROLLEY = 9;
    // 极光推送首页弹窗
    public static final int JPUSH_DIALOG_HOME = 10;
    // show首页titles
    public static final int SHOW_TITLES = 11;
    // hide首页titles
    public static final int HIDE_TITLES = 12;
    //刷新商品详情页购物车数量
    public static final int REFRESH_SHOPPING_MALL_TROLLEY_COUNT = 13;
    //首页数据提前加载传递
    public static final int HOME_DATA_BEFORE=14;
    //关闭订单详情页
    public static final int CLOSE_ORDER_DETAIL = 16;
    // 极光推送我的页面弹窗
    public static final int JPUSH_DIALOG_MINE = 17;
    //Scheme协议接收跳转
    public static final int SCHEME_GOTO=18;
}
