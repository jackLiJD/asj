package com.ald.asjauthlib.authframework.core.network.converter;

import com.ald.asjauthlib.authframework.core.network.NetworkUtil;
import com.ald.asjauthlib.authframework.core.network.exception.ApiException;
import com.ald.asjauthlib.authframework.core.network.exception.ApiExceptionEnum;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.ald.asjauthlib.authframework.core.network.entity.ApiResponse;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

final class FWFastJsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private static final Feature[] EMPTY_SERIALIZER_FEATURES = new Feature[0];

    private Type mType;

    private ParserConfig config;
    private int featureValues;
    private Feature[] features;

    FWFastJsonResponseBodyConverter(Type type, ParserConfig config, int featureValues,
                                    Feature... features) {
        mType = type;
        this.config = config;
        this.featureValues = featureValues;
        this.features = features;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        /**
         * 网络加载动画close
         */
        NetworkUtil.dismissCutscenes();
        /***
         * 此处的value只能使用一次，否则会出现错误
         */
        String response = value.string().trim();
//        if (AlaConfig.isDebug()) {
//            Logger.d("RDClient", "response >> \n" + response);
//        }
        try {
            /**
             * ApiResponse结构体解析
             */
            JSONObject jsonObjectResp = JSON.parseObject(response);
            ApiResponse apiResponse = new ApiResponse(jsonObjectResp);
            //钱包的方的接口特殊处理
            String code = jsonObjectResp.getString("code");
            if(MiscUtils.isNotEmpty(code)&&Integer.valueOf(code)>0){
                if(Integer.valueOf(code)==200){
                    return JSON.parseObject(jsonObjectResp.getString("data"), mType);
                }else {
                    throw new ApiException(Integer.valueOf(code), jsonObjectResp.getString("message"));
                }
            }
            /**
             * 返回非成功的直接抛异常
             */
            T model = apiResponse.getData(mType);
            if (apiResponse.getCode() != ApiExceptionEnum.SUCCESS.getErrorCode()) {
                if (model != null) {
                    //支付失败情况单独处理：返回异常状态码，正常数据
                    throw new ApiException(apiResponse.getCode(), apiResponse.getMsg(), apiResponse.getDataJsonString());
                } else {
                    //上传异常日志用的已存在接口，特殊处理
                    if (apiResponse.getJsonObject() != null && apiResponse.getJsonObject().get("url") != null && MiscUtils.isNotEmpty(apiResponse.getJsonObject().get("url").toString())) {
                        ApiResponse apiResponseRst = new ApiResponse();
                        apiResponseRst.setCode(1000);
                        apiResponseRst.setMsg(apiResponse.getJsonObject().get("url").toString());
                        return (T) apiResponseRst;
                    } else {
                        throw new ApiException(apiResponse.getCode(), apiResponse.getMsg());
                    }
                }
            }
            if (model == null) {
                String className = mType.toString();
                String cls = Boolean.class.getSimpleName();
                String clsApiResponse = ApiResponse.class.getSimpleName();
                if (className.contains(cls)) {
                    return (T) Boolean.class.cast(true);
                }
                if (className.contains(clsApiResponse)) {
                    return (T) apiResponse;
                }
                throw new ApiException(ApiExceptionEnum.EMPTY.getErrorCode(), ApiExceptionEnum.EMPTY.getErrorMsg());
            }
            return model;
        } finally {
            value.close();
        }
    }
}
