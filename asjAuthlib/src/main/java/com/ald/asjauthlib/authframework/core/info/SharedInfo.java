package com.ald.asjauthlib.authframework.core.info;


import com.ald.asjauthlib.authframework.core.utils.SPUtil;
import com.ald.asjauthlib.authframework.core.utils.SoftMap;

/**
 * Author: chenming
 * E-mail: cm1@erongdu.com
 * Date: 16/3/18 下午3:09
 * <p/>
 * Description:
 * 需要持久化的数据:

 */

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/1/4
 * 描述：SharedPreferences管理类
 * 1.User 信息 (Token)
 * 2.本地的公告时间戳
 * 3.本地的手势密码
 * 4.重新打开的时间
 * <p/>
 * 临时的数据:
 * 1.服务器的公告时间戳
 * 修订历史：
 */
public class SharedInfo {
    private SoftMap<String, Object> moMap;

    private SharedInfo() {
    }

    public static SharedInfo getInstance() {
        return SharedInfoInstance.instance;
    }

    private static class SharedInfoInstance {
        static SharedInfo instance = new SharedInfo();
    }

    /**
     * 获取Sp 实体类
     */
    public <T> T getValue(Class<T> clazz) {
        return getValue(clazz, null);
    }

    public <T> T getValue(Class<T> clazz, String make) {
        if (null == clazz) {
            return null;
        }
        String key = getKey(clazz, make);
        // moMap 中是否存在，存在则从 moMap 中取出
        if (getMoMap().containsKey(key)) {
            return (T) moMap.get(key);
        }
        // 不存在则从 SP 中取出
        T mo = SPUtil.getEntity(clazz, null, null, true);
        // 不为空，则 put 进 moMap
        if (null != mo) {
            moMap.put(key, mo);
        }
        return mo;
    }

    /**
     * 将mo存储至SoftMap 中，防止每次使用重新创建
     */
    public void setValue(Class<?> clazz, Object obj) {
        setValue(clazz, obj, null);
    }

    public void setValue(Class<?> clazz, Object obj, String make) {
        if (null == clazz && null == obj) {
            return;
        }
        // 每次setValue 替换最新的Object
        String key = getKey(clazz, make);
        if (getMoMap().containsKey(key)) {
            getMoMap().remove(key);
        }
        getMoMap().put(key, obj);
        SPUtil.setEntity(obj, true);
    }

    /**
     * 从mo中删除此键值对
     */
    public void remove(Class<?> clazz) {
        remove(clazz, null);
    }

    public void remove(Class<?> clazz, String make) {
        if (null == clazz) {
            return;
        }
        String key = getKey(clazz, make);
        getMoMap().remove(key);
        SPUtil.remove(key);
    }

    private String getKey(Class<?> clazz, String make) {
        if (make != null) {
            return clazz.getName() + "_" + make;
        }
        return clazz.getName();
    }

    private SoftMap<String, Object> getMoMap() {
        if (moMap == null)
            moMap = new SoftMap<>();
        return moMap;
    }

    private void setMoMap(SoftMap<String, Object> moMap) {
        this.moMap = moMap;
    }
}
