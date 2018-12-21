package com.ald.asjauthlib.auth;

import com.ald.asjauthlib.auth.model.FaceLivenessModel;
import com.ald.asjauthlib.auth.model.IDCardInfoModel;
import com.ald.asjauthlib.auth.model.UrlModel;
import com.ald.asjauthlib.auth.model.YiTuLivenessModel;
import com.ald.asjauthlib.auth.model.YiTuUploadCardResultModel;
import com.ald.asjauthlib.authframework.core.network.entity.ApiResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/1/9
 * 描述： app公共接口api
 * 修订历史：
 */
public interface AppApi {





    @Multipart
    @POST
    Call<UrlModel> uploadImage(@Url String url, @Part List<MultipartBody.Part> partList);


    /**
     * 上传头像
     */
    @Multipart
    @POST
    Call<YiTuUploadCardResultModel> uploadFile(@Url String url, @Part List<MultipartBody.Part> partList);


    /**
     * 上传埋点文件
     */
    @Multipart
    @POST
    Call<ApiResponse> uploadMaidianFile(@Url String url, @Part List<MultipartBody.Part> partList);

    /**
     * 上传头像
     */
    @Multipart
    @POST
    Call<IDCardInfoModel> uploadIDFile(@Url String url, @Part List<MultipartBody.Part> partList);


    @Multipart
    @POST
    Call<YiTuLivenessModel> uploadLivenessFile(@Url String url, @Part List<MultipartBody.Part> partList);


    @Multipart
    @POST
    Call<FaceLivenessModel> uploadFaceLivenessFile(@Url String url, @Part List<MultipartBody.Part> partList);


    /**
     * 经纬度转换城市
     */
    @POST("regeo")
    Call<ResponseBody> conversionLatLon(@Query("location") String location,
                                        @Query("output") String output,
                                        @Query("key") String key,
                                        @Query("radius") String radius,
                                        @Query("extensions") String extensions);



}
