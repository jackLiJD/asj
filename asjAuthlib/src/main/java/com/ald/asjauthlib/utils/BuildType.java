package com.ald.asjauthlib.utils;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Yangyang on 2018/12/12.
 * desc:
 */

public class BuildType {
    public static final int DEBUG=1;
    public static final int PREVIEW=2;
    public static final int RELEASE=3;

    @IntDef({DEBUG, PREVIEW, RELEASE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {

    }
}
