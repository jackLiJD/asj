package com.ald.asjauthlib.authframework.core.network.exception;

import android.util.Log;

import com.ald.asjauthlib.authframework.core.utils.HtmlRegexpUtil;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/1/6
 * 描述：HTTP请求异常类
 * 修订历史：
 */
public class ApiException extends RuntimeException {
    private int code;
    private String msg;
    private String dataJsonStr;//正常返回数据的json字符串

    public ApiException(int resultCode, String msg) {
        this(msg);
        this.code = resultCode;
        this.msg = HtmlRegexpUtil.filterHtml(msg);
        Log.d("msg", msg);
        Log.d("resultCode", resultCode + "");
    }

    public ApiException(int resultCode, String msg,String dataJsonStr) {
        this(msg);
        this.code = resultCode;
        this.msg = HtmlRegexpUtil.filterHtml(msg);
        this.dataJsonStr=dataJsonStr;
        Log.d("msg", msg);
        Log.d("resultCode", resultCode + "");
    }


    public ApiException(String detailMessage) {
        super(detailMessage);
    }

    public String getDataJsonStr() {
        return dataJsonStr;
    }

    public void setDataJsonStr(String dataJsonStr) {
        this.dataJsonStr = dataJsonStr;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = HtmlRegexpUtil.filterHtml(msg);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


}
