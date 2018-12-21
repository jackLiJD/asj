package com.ald.asjauthlib.cashier.utils;

import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.network.entity.BaseModel;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;

import java.lang.reflect.Field;

/**
 * 支付参数基类
 * Created by sean yu on 2017/8/11.
 */

public abstract class PaymentParams extends BaseModel {

    /*
    *  -1.微信 0.代付  >0 银行卡id
    * */
    public String paType;

    /**
     * 银行卡支付方式---bankChannel	String	支付类型（2018-03-28）	代收：DAISHOU，快捷：KUAIJIE
     */
    public String bankChannel;
    /**
     * 银行卡预留手机号mobile	String	银行卡绑定手机号码
     */
    public String mobile;


    /**
     * 检查参数是否合法
     *
     * @param params 必要参数
     * @return 参数是否合法
     */
    protected boolean checkParams(String... params) {
        for (Field field : this.getClass().getFields()) {
            try {
                String name = field.getName();
                for (String param : params) {
                    if (param.equals(name)) {
                        String value = (String) field.get(this);
                        if (MiscUtils.isEmpty(value)) {
                            return false;
                        }
                    }
                }
                return true;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    /**
     * 封装请求参数
     *
     * @return JSONObject 请求参数
     */
    public JSONObject getParams() {
        JSONObject json = new JSONObject();
        for (Field field : this.getClass().getFields()) {
            try {
//                String value = (String) field.get(this);
                String value = String.valueOf(field.get(this));
                json.put(field.getName(), value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return json;
    }
}
