package com.ald.asjauthlib.authframework.core.network;

import com.ald.asjauthlib.BuildConfig;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.network.converter.FWFastJsonConverterFactory;
import com.ald.asjauthlib.authframework.core.network.interceptor.AppealInterceptor;
import com.ald.asjauthlib.authframework.core.network.interceptor.BasicParamsInterceptor;
import com.ald.asjauthlib.authframework.core.network.interceptor.HttpLoggingInterceptor;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.authframework.core.utils.log.Logger;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/1/6
 * 描述：网络请求client
 * 修订历史：
 */
public class RDClient {
    // 网络请求超时时间值(s)
    private static final int DEFAULT_TIMEOUT = 60;
    // retrofit实例
    private Retrofit retrofit;


    /**
     * 私有化构造方法
     */
    private RDClient(String user) {
        if (MiscUtils.isEmpty(user))
            updateRetrofit(new BasicParamsInterceptor.Builder().build());
        else {
            //替换header
            updateRetrofit(new AppealInterceptor.Builder(user).build());
        }
    }


    /**
     * 调用单例对象
     */
    private static RDClient instance;

    private static RDClient appealInstance;


    public static RDClient getInstance() {
        if (instance == null)
            instance = new RDClient("");

        return instance;
    }

    public static RDClient getInstance(String userName) {
        if (appealInstance == null) {
            appealInstance = new RDClient(userName);
        }
        return appealInstance;
    }

    public void updateRetrofit(Interceptor interceptor) {
        // 创建一个OkHttpClient
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // 设置网络请求超时时间
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
//        // 添加签名等基础参数
        builder.addInterceptor(interceptor);
        // 打印参数
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
        builder.addInterceptor(httpLoggingInterceptor);


        retrofit = new Retrofit.Builder()
                .baseUrl(AlaConfig.getServerProvider().getAppServer())
                .client(builder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(FWFastJsonConverterFactory.create())
                .build();
        getServiceMap().clear();
    }


    ///////////////////////////////////////////////////////////////////////////
    // service,用ConcurrentHashMap防止ConcurrentModificationException
    ///////////////////////////////////////////////////////////////////////////
    private static ConcurrentHashMap<String, Object> serviceMap;

    private static ConcurrentHashMap<String, Object> getServiceMap() {
        Logger.d("logger", "logger++1" + serviceMap);
        if (serviceMap == null)
            serviceMap = new ConcurrentHashMap<>();
        return serviceMap;
    }

    /**
     * @return 指定service实例
     */
    public static <T> T getService(Class<T> clazz) {
        Logger.d("logger", "logger++2" + getServiceMap().containsKey(clazz.getSimpleName()));
        if (getServiceMap().containsKey(clazz.getSimpleName())) {
            Object service = getServiceMap().get(clazz.getSimpleName());
            if(service !=null)
            return (T) service;
        }
        T service = RDClient.getInstance().retrofit.create(clazz);
        getServiceMap().put(clazz.getSimpleName(), service);
        return service;
    }


    /**
     * @return 指定service实例(免登)
     */
    public static <T> T getServiceLogout(Class<T> clazz, String userName) {
        Logger.d("logger", "logger++2" + getServiceMap().containsKey(clazz.getSimpleName()));
        T service = RDClient.getInstance(userName).retrofit.create(clazz);
        getServiceMap().put(clazz.getSimpleName(), service);
        return service;
    }


}
