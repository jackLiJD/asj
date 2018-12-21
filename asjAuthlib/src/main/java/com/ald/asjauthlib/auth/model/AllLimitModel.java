package com.ald.asjauthlib.auth.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.List;

/*
 * Created by liangchen on 2018/1/17.
 */

public class AllLimitModel implements Parcelable {

    private List<Limit> data;

    public List<Limit> getData() {
        return data;
    }

    public void setData(List<Limit> data) {
        this.data = data;
    }

    public AllLimitModel() {

    }

    protected AllLimitModel(Parcel in) {
        data = in.createTypedArrayList(Limit.CREATOR);
    }

    public static final Creator<AllLimitModel> CREATOR = new Creator<AllLimitModel>() {
        @Override
        public AllLimitModel createFromParcel(Parcel in) {
            return new AllLimitModel(in);
        }

        @Override
        public AllLimitModel[] newArray(int size) {
            return new AllLimitModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(data);
    }


    public static class Limit implements Parcelable {
        private BigDecimal amount;
        private String auAmount;
        private String desc;
        private String jumpUrl;
        private String picUrl;
        private String scene;
        private String showAmount;
        private String status;
        private String title;
        private String realName;


        protected Limit(Parcel in) {
            amount = new BigDecimal(in.readString());
            auAmount = in.readString();
            desc = in.readString();
            jumpUrl = in.readString();
            picUrl = in.readString();
            scene = in.readString();
            showAmount = in.readString();
            status = in.readString();
            title = in.readString();
            realName = in.readString();

        }

        public Limit() {
        }

        public static final Creator<Limit> CREATOR = new Creator<Limit>() {
            @Override
            public Limit createFromParcel(Parcel in) {
                return new Limit(in);
            }

            @Override
            public Limit[] newArray(int size) {
                return new Limit[size];
            }
        };

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getShowAmount() {
            return showAmount;
        }

        public void setShowAmount(String showAmount) {
            this.showAmount = showAmount;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public String getAuAmount() {
            return auAmount;
        }

        public void setAuAmount(String auAmount) {
            this.auAmount = auAmount;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getJumpUrl() {
            return jumpUrl;
        }

        public void setJumpUrl(String jumpUrl) {
            this.jumpUrl = jumpUrl;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getScene() {
            return scene;
        }

        public void setScene(String scene) {
            this.scene = scene;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(amount == null ? "0" : amount.toString());
            dest.writeString(auAmount);
            dest.writeString(desc);
            dest.writeString(jumpUrl);
            dest.writeString(picUrl);
            dest.writeString(scene);
            dest.writeString(showAmount);
            dest.writeString(status);
            dest.writeString(title);
            dest.writeString(realName);
        }
    }


}
