package com.ald.asjauthlib.auth.model;

import android.os.Parcel;

import com.ald.asjauthlib.authframework.core.network.entity.BaseModel;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/2/22 15:22
 * 描述：
 * 修订历史：
 */
public class CreditPromoteModel extends BaseModel {
    private CreditModel creditModel;
    private String rrCreditStatus;
    private String mobileStatus;//运营商
    private String teldirStatus;//通讯录认证
    private ZmModel zmModel;
    private LocationModel locationModel;
    private ContactorModel contactorModel;
    private String bankcardStatus;
    private String realnameStatus;

    //是否走过认证的强风控【A:未审核，N:未通过审核，P: 审核中，Y:已通过审核】
    private String riskStatus;

    private String realName;
    private String idNumber;
    private String bankCard;
    private String phoneNum;
    //是否上传了身份证照片
    private String isUploadImage;
    //人脸识别状态
    private String faceStatus;
    //
    private String riskRetrialRemind;
    //用户是否已发起过运营商认证
    private String gmtMobileExist;
    //是否显示运营活动图片
//    private String isShowImage;

    //审核通过与否的配图路径
    private String url;

    //是否跳转到H5
    private String isSkipH5;
    //H5页面
    private String h5Url;

    private String title;//沒有完成基础认证的提示语
    private String title1;//文案1
    private String title2;//文案2
    private long currentAmount;//当前额度
    private long highestAmount;//最高可提升额度
    private String fundStatus;//公积金认证状态
    private String socialSecurityStatus;//社保认证状态
    private String creditStatus;//信用卡
    private String alipayStatus;//支付寶
    private String zhengxinStatus;//人行征信
    private String chsiStatus;//学信网
    private String gmtFundExist;
    private String gmtSocialSecurityExist;
    private int showExtraTab;// 0不显示补充认证

    private String onlinebankStatus;//网银认证状态
    private String gmtCreditExist;
    private String basicStatus;//基础认证【A:未审核，N:未通过审核，P: 审核中，Y:已通过审核】
    private String flag;//Y ：已提交过，N：首次提交
    private String gmtAlipayExist;
    private String gmtChsiExist;
    private String gmtOnlinebankExist;//是否认证过

    //补充认证为F时有以下字段
    private String fundTitle;//	公积金重新提额提示信息（为空可以提交）
    private String socialSecurityTitle;//社保
    private String creditTitle;//信用卡
    private String alipayTitle;//支付宝
    private String zhengxinTitle;//人行征信
    private String chsiTitle;//学信网
    private String onlinebankTitle;//网银
    private String bubbleTitle;//冒泡
    private String onlinebankDialogShow;//是否显示白领贷弹框
    private String bldRiskStatus;//白领贷是否走过强风控【Y已认证 N未认证 C认证失败 P未认证且无过期 A认证中】

    //冒泡认证
    private String isOpenBubbleAuth;//如果 isOpenBubbleAuth为 Y 有以下字段 一下返回均是String 类型
    private String bubbleStatus;//冒泡认证状态【A:未认证，N:认证失败，W:认证中，Y:已通过认证,F:重新提额】
    private String gmtBubbleExist;//用户是否已发起过冒泡认证
//    已认证 并且是
//    现金贷场景 再显示网银


    public String getOnlinebankDialogShow() {
        return onlinebankDialogShow;
    }

    public void setOnlinebankDialogShow(String onlinebankDialogShow) {
        this.onlinebankDialogShow = onlinebankDialogShow;
    }

    public String getBldRiskStatus() {
        return bldRiskStatus;
    }

    public void setBldRiskStatus(String bldRiskStatus) {
        this.bldRiskStatus = bldRiskStatus;
    }

    public String getFundTitle() {
        return fundTitle;
    }

    public void setFundTitle(String fundTitle) {
        this.fundTitle = fundTitle;
    }

    public String getSocialSecurityTitle() {
        return socialSecurityTitle;
    }

    public void setSocialSecurityTitle(String socialSecurityTitle) {
        this.socialSecurityTitle = socialSecurityTitle;
    }

    public String getCreditTitle() {
        return creditTitle;
    }

    public void setCreditTitle(String creditTitle) {
        this.creditTitle = creditTitle;
    }

    public String getAlipayTitle() {
        return alipayTitle;
    }

    public void setAlipayTitle(String alipayTitle) {
        this.alipayTitle = alipayTitle;
    }

    public String getZhengxinTitle() {
        return zhengxinTitle;
    }

    public void setZhengxinTitle(String zhengxinTitle) {
        this.zhengxinTitle = zhengxinTitle;
    }

    public String getChsiTitle() {
        return chsiTitle;
    }

    public void setChsiTitle(String chsiTitle) {
        this.chsiTitle = chsiTitle;
    }

    public String getOnlinebankTitle() {
        return onlinebankTitle;
    }

    public void setOnlinebankTitle(String onlinebankTitle) {
        this.onlinebankTitle = onlinebankTitle;
    }

    public String getBubbleTitle() {
        return bubbleTitle;
    }

    public void setBubbleTitle(String bubbleTitle) {
        this.bubbleTitle = bubbleTitle;
    }

    public int getShowExtraTab() {
        return showExtraTab;
    }

    public void setShowExtraTab(int showExtraTab) {
        this.showExtraTab = showExtraTab;
    }


    public String getGmtOnlinebankExist() {
        return gmtOnlinebankExist;
    }

    public void setGmtOnlinebankExist(String gmtOnlinebankExist) {
        this.gmtOnlinebankExist = gmtOnlinebankExist;
    }

    public String getOnlinebankStatus() {
        return onlinebankStatus;
    }

    public void setOnlinebankStatus(String onlinebankStatus) {
        this.onlinebankStatus = onlinebankStatus;
    }

    public String getBasicStatus() {
        return basicStatus;
    }

    public void setBasicStatus(String basicStatus) {
        this.basicStatus = basicStatus;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFundStatus() {
        return fundStatus;
    }

    public void setFundStatus(String fundStatus) {
        this.fundStatus = fundStatus;
    }

    public String getSocialSecurityStatus() {
        return socialSecurityStatus;
    }

    public void setSocialSecurityStatus(String socialSecurityStatus) {
        this.socialSecurityStatus = socialSecurityStatus;
    }

    public String getCreditStatus() {
        return creditStatus;
    }

    public void setCreditStatus(String creditStatus) {
        this.creditStatus = creditStatus;
    }

    public String getAlipayStatus() {
        return alipayStatus;
    }

    public void setAlipayStatus(String alipayStatus) {
        this.alipayStatus = alipayStatus;
    }

    public String getZhengxinStatus() {
        return zhengxinStatus;
    }

    public void setZhengxinStatus(String zhengxinStatus) {
        this.zhengxinStatus = zhengxinStatus;
    }

    public String getChsiStatus() {
        return chsiStatus;
    }

    public void setChsiStatus(String chsiStatus) {
        this.chsiStatus = chsiStatus;
    }

    public String getGmtFundExist() {
        return gmtFundExist;
    }

    public void setGmtFundExist(String gmtFundExist) {
        this.gmtFundExist = gmtFundExist;
    }

    public String getGmtSocialSecurityExist() {
        return gmtSocialSecurityExist;
    }

    public void setGmtSocialSecurityExist(String gmtSocialSecurityExist) {
        this.gmtSocialSecurityExist = gmtSocialSecurityExist;
    }

    public String getGmtCreditExist() {
        return gmtCreditExist;
    }

    public void setGmtCreditExist(String gmtCreditExist) {
        this.gmtCreditExist = gmtCreditExist;
    }

    public String getGmtAlipayExist() {
        return gmtAlipayExist;
    }

    public void setGmtAlipayExist(String gmtAlipayExist) {
        this.gmtAlipayExist = gmtAlipayExist;
    }

    public String getGmtChsiExist() {
        return gmtChsiExist;
    }

    public void setGmtChsiExist(String gmtChsiExist) {
        this.gmtChsiExist = gmtChsiExist;
    }

    public String getGmtZhengxinExist() {
        return gmtZhengxinExist;
    }

    public void setGmtZhengxinExist(String gmtZhengxinExist) {
        this.gmtZhengxinExist = gmtZhengxinExist;
    }

    private String gmtZhengxinExist;


    public CreditPromoteModel() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public CreditModel getCreditModel() {
        return creditModel;
    }

    public void setCreditModel(CreditModel creditModel) {
        this.creditModel = creditModel;
    }

    public String getRrCreditStatus() {
        return rrCreditStatus;
    }

    public void setRrCreditStatus(String rrCreditStatus) {
        this.rrCreditStatus = rrCreditStatus;
    }

    public String getMobileStatus() {
        return mobileStatus;
    }

    public void setMobileStatus(String mobileStatus) {
        this.mobileStatus = mobileStatus;
    }

    public String getTeldirStatus() {
        return teldirStatus;
    }

    public void setTeldirStatus(String teldirStatus) {
        this.teldirStatus = teldirStatus;
    }

    public ZmModel getZmModel() {
        return zmModel;
    }

    public void setZmModel(ZmModel zmModel) {
        this.zmModel = zmModel;
    }

    public LocationModel getLocationModel() {
        return locationModel;
    }

    public void setLocationModel(LocationModel locationModel) {
        this.locationModel = locationModel;
    }

    public ContactorModel getContactorModel() {
        return contactorModel;
    }

    public void setContactorModel(ContactorModel contactorModel) {
        this.contactorModel = contactorModel;
    }

    public String getBankcardStatus() {
        return bankcardStatus;
    }

    public void setBankcardStatus(String bankcardStatus) {
        this.bankcardStatus = bankcardStatus;
    }

    public String getRealnameStatus() {
        return realnameStatus;
    }

    public void setRealnameStatus(String realnameStatus) {
        this.realnameStatus = realnameStatus;
    }

    public String getRiskStatus() {
        return riskStatus;
    }

    public void setRiskStatus(String riskStatus) {
        this.riskStatus = riskStatus;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getIsUploadImage() {
        return isUploadImage;
    }

    public void setIsUploadImage(String isUploadImage) {
        this.isUploadImage = isUploadImage;
    }

    public String getFaceStatus() {
        return faceStatus;
    }

    public String getTitle1() {
        return title1;
    }

    public void setTitle1(String title1) {
        this.title1 = title1;
    }

    public String getTitle2() {
        return title2;
    }

    public void setTitle2(String title2) {
        this.title2 = title2;
    }

    public long getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(long currentAmount) {
        this.currentAmount = currentAmount;
    }

    public long getHighestAmount() {
        return highestAmount;
    }

    public void setHighestAmount(long highestAmount) {
        this.highestAmount = highestAmount;
    }

    public void setFaceStatus(String faceStatus) {
        this.faceStatus = faceStatus;
    }

    public String getRiskRetrialRemind() {
        return riskRetrialRemind;
    }

    public void setRiskRetrialRemind(String riskRetrialRemind) {
        this.riskRetrialRemind = riskRetrialRemind;
    }

    public String getGmtMobileExist() {
        return gmtMobileExist;
    }

    public void setGmtMobileExist(String gmtMobileExist) {
        this.gmtMobileExist = gmtMobileExist;
    }

    public class CreditModel extends BaseModel {
        private String creditLevel;
        private long creditAssessTime;
        private String allowConsume;

        public CreditModel() {
        }

        public String getCreditLevel() {
            return creditLevel;
        }

        public void setCreditLevel(String creditLevel) {
            this.creditLevel = creditLevel;
        }

        public long getCreditAssessTime() {
            return creditAssessTime;
        }

        public void setCreditAssessTime(long creditAssessTime) {
            this.creditAssessTime = creditAssessTime;
        }

        public String getAllowConsume() {
            return allowConsume;
        }

        public void setAllowConsume(String allowConsume) {
            this.allowConsume = allowConsume;
        }
    }

    public class ZmModel extends BaseModel {
        private String zmStatus;
        private long zmScore;
        private String zmxyAuthUrl;
        private String zmDesc;//认证状态描述
        private String isShow;//认证项显示控制（Y：展示  N：不展示）

        public ZmModel() {
        }

        public String getZmDesc() {
            return zmDesc;
        }

        public void setZmDesc(String zmDesc) {
            this.zmDesc = zmDesc;
        }

        public String getIsShow() {
            return isShow;
        }

        public void setIsShow(String isShow) {
            this.isShow = isShow;
        }

        public String getZmxyAuthUrl() {
            return zmxyAuthUrl;
        }

        public void setZmxyAuthUrl(String zmxyAuthUrl) {
            this.zmxyAuthUrl = zmxyAuthUrl;
        }

        public long getZmScore() {
            return zmScore;
        }

        public void setZmScore(long zmScore) {
            this.zmScore = zmScore;
        }

        public String getZmStatus() {
            return zmStatus;
        }

        public void setZmStatus(String zmStatus) {
            this.zmStatus = zmStatus;
        }
    }

    public class LocationModel extends BaseModel {
        private String locationStatus;//	String	定位状态 Y:已设置，N:未设置
        private String locationAddress;//	SString	定位地址

        public LocationModel() {
        }

        public String getLocationStatus() {
            return locationStatus;
        }

        public void setLocationStatus(String locationStatus) {
            this.locationStatus = locationStatus;
        }

        public String getLocationAddress() {
            return locationAddress;
        }

        public void setLocationAddress(String locationAddress) {
            this.locationAddress = locationAddress;
        }
    }


    public static class ContactorModel extends BaseModel {
        private String contactorStatus;//String	紧急联系人状态 Y:已设置，N:未设置
        private String contactorName;//String	紧急联系人
        private String contactorMobile;//String	紧急联系人电话
        private String contactorType;//String	紧急联系人关系

        public ContactorModel() {
        }

        ContactorModel(Parcel in) {
            contactorStatus = in.readString();
            contactorName = in.readString();
            contactorMobile = in.readString();
            contactorType = in.readString();
        }

        public String getContactorStatus() {
            return contactorStatus;
        }

        public void setContactorStatus(String contactorStatus) {
            this.contactorStatus = contactorStatus;
        }

        public String getContactorName() {
            return contactorName;
        }

        public void setContactorName(String contactorName) {
            this.contactorName = contactorName;
        }

        public String getContactorMobile() {
            return contactorMobile;
        }

        public void setContactorMobile(String contactorMobile) {
            this.contactorMobile = contactorMobile;
        }

        public String getContactorType() {
            return contactorType;
        }

        public void setContactorType(String contactorType) {
            this.contactorType = contactorType;
        }
    }

    public String getIsSkipH5() {
        return isSkipH5;
    }

    public void setIsSkipH5(String isSkipH5) {
        this.isSkipH5 = isSkipH5;
    }

    public String getH5Url() {
        return h5Url;
    }

    public void setH5Url(String h5Url) {
        this.h5Url = h5Url;
    }

    public String getIsOpenBubbleAuth() {
        return isOpenBubbleAuth;
    }

    public void setIsOpenBubbleAuth(String isOpenBubbleAuth) {
        this.isOpenBubbleAuth = isOpenBubbleAuth;
    }

    public String getBubbleStatus() {
        return bubbleStatus;
    }

    public void setBubbleStatus(String bubbleStatus) {
        this.bubbleStatus = bubbleStatus;
    }

    public String getGmtBubbleExist() {
        return gmtBubbleExist;
    }

    public void setGmtBubbleExist(String gmtBubbleExist) {
        this.gmtBubbleExist = gmtBubbleExist;
    }
}
