package com.ald.asjauthlib.authframework.core.utils;

/**
 * Created by Yangyang on 2018/7/19.
 * desc:通用的常量
 */

public interface CommonConstant {
    String SERVICE_URL = "service_url";
    String H5_URL = "h5_url";
    int LEFT = 1;
    int TOP = 2;
    int RIGHT = 3;
    int BOTTOM = 4;
    String NEED_KILL_APPLICATION="need_kill_application";//是否需要杀死进程,目前用于合并好补丁后冷启动
}
