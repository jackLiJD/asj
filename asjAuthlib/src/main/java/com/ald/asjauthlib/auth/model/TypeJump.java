package com.ald.asjauthlib.auth.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wjy on 2018/4/16.
 * 基础实体类(很多地方用到,抽取出来)
 */

public class TypeJump implements Parcelable{

    private String content;
    public String imageUrl;
    private String type;

    public TypeJump() {
    }

    protected TypeJump(Parcel in) {
        content = in.readString();
        imageUrl = in.readString();
        type = in.readString();
    }

    public static final Creator<TypeJump> CREATOR = new Creator<TypeJump>() {
        @Override
        public TypeJump createFromParcel(Parcel in) {
            return new TypeJump(in);
        }

        @Override
        public TypeJump[] newArray(int size) {
            return new TypeJump[size];
        }
    };

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(content);
        dest.writeString(imageUrl);
        dest.writeString(type);
    }
}
