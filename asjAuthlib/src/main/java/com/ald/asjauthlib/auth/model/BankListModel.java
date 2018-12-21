package com.ald.asjauthlib.auth.model;

import com.ald.asjauthlib.authframework.core.network.entity.BaseModel;

import java.util.List;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/1/20 10:50
 * 描述：
 * 修订历史：
 */
public class BankListModel extends BaseModel {
    private List<BankCardModel> bankCardList;
    private String faceStatus;
    private String bankcardStatus;
    private String realName;
    private String idNumber;
    private String isMain;
    private String feeRatio;//手续费比例
    private String feeQuota;//手续费定额

    public BankListModel() {
    }

    public List<BankCardModel> getBankCardList() {
        return bankCardList;
    }

    public void setBankCardList(List<BankCardModel> bankCardList) {
        this.bankCardList = bankCardList;
    }

    public String getBankcardStatus() {
        return bankcardStatus;
    }

    public void setBankcardStatus(String bankcardStatus) {
        this.bankcardStatus = bankcardStatus;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getFaceStatus() {
        return faceStatus;
    }

    public void setFaceStatus(String faceStatus) {
        this.faceStatus = faceStatus;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getIsMain() {
        return isMain;
    }

    public void setIsMain(String isMain) {
        this.isMain = isMain;
    }

    public String getFeeRatio() {
        return feeRatio;
    }

    public void setFeeRatio(String feeRatio) {
        this.feeRatio = feeRatio;
    }

    public String getFeeQuota() {
        return feeQuota;
    }

    public void setFeeQuota(String feeQuota) {
        this.feeQuota = feeQuota;
    }
}
