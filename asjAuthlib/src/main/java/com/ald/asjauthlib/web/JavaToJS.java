package com.ald.asjauthlib.web;

/*
 * Created by sean yu on 2017/5/23.
 */

public class JavaToJS {

    public static String LOGIN_SUCCESS_WITH_USERNAME = "javascript:loginSuccess(%1$s)";
    static String LOGIN_SUCCESS = "javascript:loginSuccess()";
    public static String GET_SHARE_DATA = "javascript:alaShareData()";
    static String OTO_RECEIVE_LOCATION = "javascript:otosaas.receiveLocation('" + "%1$s" + "', '" + "%2$s" + "')";
    static String POST_CONTACTS = "javascript:phoneBook('" + "%1$s" + "')";
    public static String REFRESH_CONTACTS = "";//发送短信成功后刷新方法
    static String POST_MAIDIAN_JSON = "javascript:getMaidianInfoMsg('" + "%1$s" + "')";//回传埋点参数

    public static class KITKAT_JS {
        public static String LOGIN_SUCCESS_WITH_USERNAME = "javascript:alert(loginSuccess(%1$s))";
        public static String LOGIN_SUCCESS = "javascript:alert(loginSuccess())";
        public static String GET_SHARE_DATA = "javascript:alert(alaShareData())";
    }
}
