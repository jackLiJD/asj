package com.ald.asjauthlib.utils;

import com.ald.asjauthlib.authframework.core.utils.MiscUtils;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/2/20 19:59
 * 描述：
 * 修订历史：
 */
public enum ModelEnum {

    Y("Y", 1, "true"), //已认证
    N("N", 0, "false"),//未认证
    P("P", 2, "default"),//认证场景：认证中

    T("T", 1, "true"),
    F("F", 0, "false"),
    A("A", 3, "normal"),//认证场景：未认证

    MOBILE("MOBILE", 2, "移动"),
    TELECOM("TELECOM", 1, "电信"),
    UNICOM("UNICOM", 0, "联通"),

    HOME_H5("H5_URL", 1, "本地H5"),
    HOME_GOODS("GOODS_ID", 0, "商品详情页面"),
    CATEGORY_ID("CATEGORY_ID", 5, "分类商品结果页"),
    SEARCH_TAG("SEARCH_TAG", 6, "搜索tag页"),
    COUPON_LIST("COUPON_LIST", 7, "优惠券列表"),
    BRAND("BRAND", 3, "菠萝觅"),
    UN_URL("UN_URL", 2, "无处理"),

    BILL_STATUS_C("C", 3, "已关闭"),
    BILL_STATUS_O("O", 2, "已逾期"),
    BILL_STATUS_Y("Y", 1, "已还款"),
    BILL_STATUS_D("D", 4, "还款中"),
    BILL_STATUS_N("N", 0, "未还款"),
    BILL_STATUS_F("F", -1, "冻结"),

    WITH_DRAW_REBATE_CASH("CASH", 0, "现金提现金额"),
    WITH_DRAW_REBATE_JFB("JIFENBAO", 1, "集分宝提现"),

    LOAN_STATE_APPLY("APPLY", 0, "审核中"),
    LOAN_STATE_REFUSE("REFUSE", 1, "审核拒绝"),
    LOAN_STATE_CLOSED("CLOSED", 2, "借款关闭"),
    LOAN_STATE_TRANSED("TRANSED", 3, "借款成功"),
    LOAN_STATE_Y("Y", 5, "还款成功"),
    LOAN_STATE_AGREE("AGREE", 6, "审核通过"),

    BILL_TYPE_CASH("CASH", 2, "现金借款"),
    BILL_TYPE_REPAYMENT("REPAYMENT", 1, "还款"),
    BILL_TYPE_CONSUME("CONSUME", 0, "消费分期"),

    RECOMMEND_IMG("RECOMMEND_IMG", 1, "活动banner"),
    APP_MINE_COUPON("APP_MINE_COUPON", 1, "我的优惠券"),

    BOLUOME_TYPE_DIDI("YONGCHE", 0, "滴滴"),

    H5_URL("H5_URL", 0, "H5_URL跳转类型"),

    CASH_PAGE_TYPE_NEW("new", 0, "新借钱页面跳转类型"),
    CASH_PAGE_TYPE_OLD("V0", 1, "老借钱页面跳转类型"),
    CASH_PAGE_TYPE_NEW_V1("V1", 2, "新借钱页面跳转类型"),
    CASH_PAGE_TYPE_NEW_V2("V2", 3, "新借钱页面跳转类型"),

//    CASH_LOAN_TYPE_SEVEN("SEVEN", 0, "借款类型-7天"),
//    CASH_LOAN_TYPE_FOURTEEN("FOURTEEN", 1, "借款类型-14天"),

    CASH_LOAN_REPAYMENT_FROM_PAGE_INDEX("INDEX", 0, "借钱主页进入还款"),
    CASH_LOAN_REPAYMENT_FROM_PAGE_BORROW("BORROW", 1, "借钱记录进入还款"),
    CASH_LOAN_REPAYMENT_FROM_PAGE_ORDER("ORDER", 2, "订单记录进入还款"),

    AGREEMENTS_TYPE_CASH_SERVICE("AGREEMENT_CASH_SERVICE", 0, "现金服务合同"),
    AGREEMENTS_TYPE_DIGITAL("AGREEMENT_DIGITAL", 1, "数字证书服务协议"),
    AGREEMENTS_TYPE_RISK("AGREEMENT_RISK", 2, "风险提示函"),
    AGREEMENTS_TYPE_STAGE("AGREEMENT_STAGE", 3, "分期协议"),
    AGREEMENTS_TYPE_RENEWAL("AGREEMENT_RENEWAL", 4, "续期协议"),

    W("W", 1, "认证中"),

    Q("Q", 2, "等待查询"),

    E("E", 3, "已过期"),

    R("R", 4, "重新提交"),//cash场景补充认证

    S("S", 5, "禁止期"),//cash场景补充认证，X天后重新认证

    SOURCE_TYPE_RENEWAL("RENEWAL", 0, "续期"),//续期支付

    OTHER(0), CASH(1), CREDIT(2), FAILED(7), //卡类型: 0 其它; 1 借记卡; 2 信用卡;7 识别失败

    REGISTER_USER_SMS_USER_ACCOUNT("验证码已发送,通过验证后,将为您直接注册账号", 1146);


    ModelEnum(int value) {
        this.value = value;
    }


    private String model;
    private int value;
    private String desc;

    ModelEnum(String desc, int value) {
        this.desc = desc;
        this.value = value;
    }

    ModelEnum(String model, int value, String desc) {
        this.model = model;
        this.value = value;
        this.desc = desc;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * 通过Desc获取到model
     *
     * @param desc 描述
     */
    public static String DescToModel(String desc) {
        for (ModelEnum modelEnum : ModelEnum.values()) {
            if (modelEnum != null && MiscUtils.isEquals(modelEnum.getDesc(), desc)) {
                return modelEnum.getModel();
            }
        }
        return ModelEnum.Y.getModel();
    }
}
