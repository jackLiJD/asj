package com.ald.asjauthlib.authframework.core.info;

import android.databinding.ObservableField;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/1/3
 * 描述：错误消息显示处理类
 * 修订历史：
 */
public class ErrorInfo {
    public final ObservableField<String> error = new ObservableField<>();
    // 错误消息存储的有序MAP，迭代输出时先进先出
    private LinkedHashMap<String, String> msgMap = null;

    private ErrorInfo() {
    }

    public static ErrorInfo getInstance() {
        return ErrorInfoInstance.instance;
    }

    private static class ErrorInfoInstance {
        static ErrorInfo instance = new ErrorInfo();
    }

    /**
     * 添加错误信息
     *
     * @param key   键
     * @param value 值
     */
    public void addError(String key, String value) {
        if (msgMap == null) {
            msgMap = new LinkedHashMap<>();
        }
        msgMap.put(key, value);
        refreshError();
    }

    /**
     * 删除错误信息
     *
     * @param key 键
     */
    public void removeError(String key) {
        if (msgMap != null && !msgMap.isEmpty()) {
            msgMap.remove(key);
            refreshError();
        }
    }

    /**
     * 刷新错误提示框
     */
    private void refreshError() {
        Iterator i = msgMap.entrySet().iterator();
        // 只遍历一次,速度快
        if (i.hasNext()) {
            Map.Entry e = (Map.Entry) i.next();
            error.set(e.getValue().toString());
        }
    }
}
