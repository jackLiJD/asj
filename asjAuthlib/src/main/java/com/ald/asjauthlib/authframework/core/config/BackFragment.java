package com.ald.asjauthlib.authframework.core.config;

/**
 * @author lisiqi
 * @date 2018-11-6 14:31:37
 */
public interface BackFragment {
    /**
     * 所有需要响应回退按钮的fragment需要实现这个方法
     *
     * @return true:fragment自己消化掉回退事件
     */
    boolean onBackPressed();
}
