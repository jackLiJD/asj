package com.ald.asjauthlib.auth.model;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by wjy on 2018/4/20.
 */

public class BannerModel extends SortTypeJump {

    private String titleName;
    private String jumpType;
    private int createType;
    private int needLogin;
    private int needParam;
    private String className;
    private String paramDic;
    private String resourceId;

    public BannerModel() {
        super();
    }

    public BannerModel(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    protected BannerModel(Parcel in) {
        // 调用父类的读取操作
        super(in);
        // 子类实现的读取操作
        titleName = in.readString();
        jumpType = in.readString();
        createType = in.readInt();
        needLogin = in.readInt();
        needParam = in.readInt();
        className = in.readString();
        paramDic = in.readString();
        resourceId = in.readString();
    }

    public static final Parcelable.Creator<BannerModel> CREATOR = new Parcelable.Creator<BannerModel>() {
        public BannerModel createFromParcel(Parcel in) {
            // new自己
            return new BannerModel(in);
        }

        public BannerModel[] newArray(int size) {
            // new自己
            return new BannerModel[size];
        }
    };

    public void writeToParcel(Parcel out, int flags) {
        // 调用父类的写操作
        super.writeToParcel(out, flags);
        // 子类实现的写操作
        out.writeString(titleName);
        out.writeString(jumpType);
        out.writeInt(createType);
        out.writeInt(needLogin);
        out.writeInt(needParam);
        out.writeString(className);
        out.writeString(paramDic);
        out.writeString(resourceId);
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getJumpType() {
        return jumpType;
    }

    public void setJumpType(String jumpType) {
        this.jumpType = jumpType;
    }

    public int getCreateType() {
        return createType;
    }

    public void setCreateType(int createType) {
        this.createType = createType;
    }

    public int getNeedLogin() {
        return needLogin;
    }

    public void setNeedLogin(int needLogin) {
        this.needLogin = needLogin;
    }

    public int getNeedParam() {
        return needParam;
    }

    public void setNeedParam(int needParam) {
        this.needParam = needParam;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getParamDic() {
        return paramDic;
    }

    public void setParamDic(String paramDic) {
        this.paramDic = paramDic;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }
}
