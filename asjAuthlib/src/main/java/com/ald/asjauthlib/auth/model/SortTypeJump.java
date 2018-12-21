package com.ald.asjauthlib.auth.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wjy on 2018/4/16.
 *
 */
public class SortTypeJump extends TypeJump {

    private int sort;

    public SortTypeJump() {
        super();
    }

    protected SortTypeJump(Parcel in) {
        // 调用父类的读取操作
        super(in);
        // 子类实现的读取操作
        sort = in.readInt();
    }

    public static final Parcelable.Creator<SortTypeJump> CREATOR = new Parcelable.Creator<SortTypeJump>() {
        public SortTypeJump createFromParcel(Parcel in) {
            // new自己
            return new SortTypeJump(in);
        }

        public SortTypeJump[] newArray(int size) {
            // new自己
            return new SortTypeJump[size];
        }
    };

    public void writeToParcel(Parcel out, int flags) {
        // 调用父类的写操作
        super.writeToParcel(out, flags);
        // 子类实现的写操作
        out.writeInt(sort);
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
