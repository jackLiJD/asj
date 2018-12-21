package com.ald.asjauthlib.cashier.model;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/1/19 19:31
 * 描述：
 * 修订历史：
 */
public class WxOrAlaPayModel implements Parcelable {

    private String sign;        //  "E5DD3B09A352536F92C35ED0D85CA92E"
    private String timestamp;    // "1488349841"
    private String noncestr;    // "r2L0XE3r3ta9DCEb"
    private String partnerid;    // "1441757802"
    private String prepayid;    //  "wx2017030114304147a55d455b0033964317"
    private String refId;        // 15
    private String appid;        // "wx7b2b0aa8b3f0459e"
    private String type;        // "REPAYMENT"
    private String wxpackage;    // "Sign=WXPay"
    //private String package;	// "Sign=WXPay"

    private String merchantNo;    // 易宝商户编号
    private String code;        // "OPR00000",该参数为微信内部参数，用于后续调用微信API获取用户OPENID,code
    private String token;        // "E3AF76F92919081591974D3C520B762432FC474B79F5601D45C8120D72C19733",易宝订单token,state
    private String getopenusrl;    // redirect_uri
    private String urlscheme;    // 拼接的url，H5调用易宝收银台

    //还款合规返回字段
    private String amount;//还款金额
    private String status;//还款状态
    private String couponAmount;//优惠金额
    private String userAmount;//账户余额使用金额
    private String actualAmount;//实际支付金额
    private String cardNumber;//卡号|微信号
    private String cardName;//银行名称|微信
    private String repayNo;//还款编号
    private String gmtCreate;//还款时间
    private String jfbAmount;//集分宝个数


    private String bankChannel; //快捷支付
    private String mobile;//预留手机号
    private String tradeNo;
    private String orderNo;

    private String resp;

    private String goodsType;//common：普通商品 auth：权限包商品
    private int toPayOrderNums;//用户当前待支付订单个数，默认为0
    private String isRecomend;// 是否推荐用户去购买其它商品 Y:是 N：否
    private long goodsId;//推荐商品id，即当前的权限包商品id
    private String goodsBanner;// 推荐商品对应的banner描述url
    private int vipGoodsOrderId;//权限包商品对应的订单id
    private String vipGoodsOrderPayStatus;// 权限包商品的订单状态
    private String orderInfo;//支付宝订单信息
    private String needCode;
    private String outTradeNo;//协议支付用


    protected WxOrAlaPayModel(Parcel in) {
        sign = in.readString();
        timestamp = in.readString();
        noncestr = in.readString();
        partnerid = in.readString();
        prepayid = in.readString();
        refId = in.readString();
        appid = in.readString();
        type = in.readString();
        wxpackage = in.readString();
        merchantNo = in.readString();
        code = in.readString();
        token = in.readString();
        getopenusrl = in.readString();
        urlscheme = in.readString();
        amount = in.readString();
        status = in.readString();
        couponAmount = in.readString();
        userAmount = in.readString();
        actualAmount = in.readString();
        cardNumber = in.readString();
        cardName = in.readString();
        repayNo = in.readString();
        gmtCreate = in.readString();
        jfbAmount = in.readString();
        bankChannel = in.readString();
        mobile = in.readString();
        tradeNo = in.readString();
        outTradeNo = in.readString();
        orderNo = in.readString();
        resp = in.readString();
        goodsType = in.readString();
        toPayOrderNums = in.readInt();
        isRecomend = in.readString();
        goodsId = in.readLong();
        goodsBanner = in.readString();
        vipGoodsOrderId = in.readInt();
        vipGoodsOrderPayStatus = in.readString();
        needCode = in.readString();
        outTradeNo = in.readString();
    }

    public static final Creator<WxOrAlaPayModel> CREATOR = new Creator<WxOrAlaPayModel>() {
        @Override
        public WxOrAlaPayModel createFromParcel(Parcel in) {
            return new WxOrAlaPayModel(in);
        }

        @Override
        public WxOrAlaPayModel[] newArray(int size) {
            return new WxOrAlaPayModel[size];
        }
    };

    public String getNeedCode() {
        return needCode;
    }

    public void setNeedCode(String needCode) {
        this.needCode = needCode;
    }

    public int getVipGoodsOrderId() {
        return vipGoodsOrderId;
    }

    public void setVipGoodsOrderId(int vipGoodsOrderId) {
        this.vipGoodsOrderId = vipGoodsOrderId;
    }

    public String getVipGoodsOrderPayStatus() {
        return vipGoodsOrderPayStatus;
    }

    public void setVipGoodsOrderPayStatus(String vipGoodsOrderPayStatus) {
        this.vipGoodsOrderPayStatus = vipGoodsOrderPayStatus;
    }

    public String getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(String orderInfo) {
        this.orderInfo = orderInfo;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public int getToPayOrderNums() {
        return toPayOrderNums;
    }

    public void setToPayOrderNums(int toPayOrderNums) {
        this.toPayOrderNums = toPayOrderNums;
    }

    public String getIsRecomend() {
        return isRecomend;
    }

    public void setIsRecomend(String isRecomend) {
        this.isRecomend = isRecomend;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsBanner() {
        return goodsBanner;
    }

    public void setGoodsBanner(String goodsBanner) {
        this.goodsBanner = goodsBanner;
    }

    public String getResp() {
        return resp;
    }

    public void setResp(String resp) {
        this.resp = resp;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public WxOrAlaPayModel() {
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWxpackage() {
        return wxpackage;
    }

    public void setWxpackage(String wxpackage) {
        this.wxpackage = wxpackage;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getGetopenusrl() {
        return getopenusrl;
    }

    public void setGetopenusrl(String getopenusrl) {
        this.getopenusrl = getopenusrl;
    }

    public String getUrlscheme() {
        return urlscheme;
    }

    public void setUrlscheme(String urlscheme) {
        this.urlscheme = urlscheme;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(String couponAmount) {
        this.couponAmount = couponAmount;
    }

    public String getUserAmount() {
        return userAmount;
    }

    public void setUserAmount(String userAmount) {
        this.userAmount = userAmount;
    }

    public String getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(String actualAmount) {
        this.actualAmount = actualAmount;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getRepayNo() {
        return repayNo;
    }

    public void setRepayNo(String repayNo) {
        this.repayNo = repayNo;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getJfbAmount() {
        return jfbAmount;
    }

    public void setJfbAmount(String jfbAmount) {
        this.jfbAmount = jfbAmount;
    }

    public String getBankChannel() {
        return bankChannel;
    }

    public void setBankChannel(String bankChannel) {
        this.bankChannel = bankChannel;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sign);
        dest.writeString(timestamp);
        dest.writeString(noncestr);
        dest.writeString(partnerid);
        dest.writeString(prepayid);
        dest.writeString(refId);
        dest.writeString(appid);
        dest.writeString(type);
        dest.writeString(wxpackage);
        dest.writeString(merchantNo);
        dest.writeString(code);
        dest.writeString(token);
        dest.writeString(getopenusrl);
        dest.writeString(urlscheme);
        dest.writeString(amount);
        dest.writeString(status);
        dest.writeString(couponAmount);
        dest.writeString(userAmount);
        dest.writeString(actualAmount);
        dest.writeString(cardNumber);
        dest.writeString(cardName);
        dest.writeString(repayNo);
        dest.writeString(gmtCreate);
        dest.writeString(jfbAmount);
        dest.writeString(bankChannel);
        dest.writeString(mobile);
        dest.writeString(tradeNo);
        dest.writeString(outTradeNo);
        dest.writeString(orderNo);
        dest.writeString(resp);
        dest.writeString(goodsType);
        dest.writeInt(toPayOrderNums);
        dest.writeString(isRecomend);
        dest.writeLong(goodsId);
        dest.writeString(goodsBanner);
        dest.writeInt(vipGoodsOrderId);
        dest.writeString(vipGoodsOrderPayStatus);
        dest.writeString(needCode);
        dest.writeString(outTradeNo);

    }
}
