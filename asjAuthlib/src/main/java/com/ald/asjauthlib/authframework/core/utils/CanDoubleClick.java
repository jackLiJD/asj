package com.ald.asjauthlib.authframework.core.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Yangyang on 2018/5/4.
 * desc:onclick方法加上此注解,可实现响应快速点击
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD})
public @interface CanDoubleClick {

}
