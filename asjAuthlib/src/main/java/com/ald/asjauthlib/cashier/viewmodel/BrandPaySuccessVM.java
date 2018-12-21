package com.ald.asjauthlib.cashier.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.view.View;

import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.auth.model.BannerModel;
import com.ald.asjauthlib.bindingadapter.view.ViewBindingAdapter;
import com.ald.asjauthlib.cashier.BrandApi;
import com.ald.asjauthlib.cashier.model.BrandOrderDetailUrlModel;
import com.ald.asjauthlib.utils.BannerClickUtils;
import com.ald.asjauthlib.web.HTML5WebView;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.network.NetworkUtil;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.network.RequestCallBack;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.authframework.core.utils.UIUtils;
import com.ald.asjauthlib.authframework.core.vm.BaseVM;

import retrofit2.Call;
import retrofit2.Response;

import static com.ald.asjauthlib.utils.BannerClickUtils.TYPE_BRAND;


/**
 * 品牌订单支付成功vm
 * Created by yaowenda on 17/4/6.
 */

public class BrandPaySuccessVM extends BaseVM {
    private Context context;
    private String fanbeiOrderId = "0";

    public final ObservableList<BannerModel> bannerList = new ObservableArrayList<>();//底部引流banner
    public final ObservableField<ViewBindingAdapter.BannerListener> bannerListener = new ObservableField<>();

    public BrandPaySuccessVM(Context context) {
        this.context = context;
        fanbeiOrderId = ((Activity) context).getIntent().getStringExtra(BundleKeys.BRAND_FANBEI_ORDERID);
        BannerClickUtils.setBannerSource(context, TYPE_BRAND, bannerList, bannerListener);
    }

    /**
     * 返回首页点击事件
     *
     */
    public void returnToBrandList(View view) {
        ((Activity) context).finish();
    }

    /**
     * 查看订单点击事件
     *
     */
    public void goToBrandList(View view) {
        JSONObject requestObj = new JSONObject();
        requestObj.put("orderId", fanbeiOrderId);
        Call<BrandOrderDetailUrlModel> call = RDClient.getService(BrandApi.class).getOrderDetailUrl(requestObj);
        NetworkUtil.showCutscenes(context, call);
        call.enqueue(new RequestCallBack<BrandOrderDetailUrlModel>() {
            @Override
            public void onSuccess(Call<BrandOrderDetailUrlModel> call, Response<BrandOrderDetailUrlModel> response) {
                if (MiscUtils.isEmpty(response.body().getDetailUrl())) {
                    UIUtils.showToast("获取订单信息失败,请重试");
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra(HTML5WebView.INTENT_BASE_URL, response.body().getDetailUrl());
                ActivityUtils.push(HTML5WebView.class, intent);
                ((Activity) context).finish();
            }
        });
    }

}
