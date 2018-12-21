package com.ald.asjauthlib.utils;

import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.text.TextUtils;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.MainApi;
import com.ald.asjauthlib.auth.model.BannerListModel;
import com.ald.asjauthlib.auth.model.BannerModel;
import com.ald.asjauthlib.auth.model.BrandUrlModel;
import com.ald.asjauthlib.bindingadapter.view.ViewBindingAdapter;
import com.ald.asjauthlib.web.HTML5WebView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.network.NetworkUtil;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.network.RequestCallBack;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.authframework.core.utils.UIUtils;
import com.moxie.client.MainActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by luckyliang on 2017/12/2.
 * 引流页Banner跳转类
 */

public class BannerClickUtils {

    //借款申请成功页底部
    public static String TYPE_BORROW = "BORROW_APPLY_SUCCESS_PAGE_BOTTOM";
    //续借申请成功页底部
    public static String TYPE_RENEWAL = "RENEWAL_APPLY_SUCCESS_PAGE_BOTTOM";
    //吃玩住行支付页底部
    public static String TYPE_BRAND = "STROLL_PAYMENT_PAGE_BOTTOM";
    //代卖申请头部
    public static String TYPE_STEAD = "AGENCY_PURCHASE_PAGE_TOP";
    // 已出账单列表页
    public static String TYPE_YES_PAY = "YES_PAYMENT_BANNER";
    // 未出账单列表页
    public static String TYPE_NO_PAY = "NO_PAYMENT_BANNER";
    // 分期账单还款结果页
    public static String TYPE_INSTALLMENT = "INSTALLMENT_PAYMENT_BANNER";
    // 借钱还款结果页
    public static String TYPE_BORROW_MONEY = "BORROW_MONEY_BANNER";
    // 账单详情页
    public static String TYPE_BILLING_DETAIL = "BILLING_DETAIL_BANNER";
    public static String TYPE_RENT = "IDENTIFY_STUTAS_TOP_BANNER";
    /**
     * 爱花主页轮播
     */
    public static String LOVE_SPEND_MONEY_HOME_BANNER = "LOVE_SPEND_MONEY_HOME_BANNER";

    /**
     * 首页大促氛围图
     */
    public static String HOME_PROMOTION_BANNER = "HOME_PROMOTION_BANNER";

    // 专场
    public static String TYPE_SPECIAL = "SPECIAL";
    // 轮播
    public static String TYPE_BANNER = "BANNER";

    public static void setBannerSource(Context context, String type,
                                       ObservableList<BannerModel> bannerList,
                                       ObservableField<ViewBindingAdapter.BannerListener> bannerListener) {
        setBannerSource(context, type, bannerList, bannerListener, null, null);
    }

    /**
     * 设置引流页数据源
     *
     * @param type    获取数据源参数
     * @param channel 统一跳转
     */
    public static void setBannerSource(Context context, String type,
                                       final ObservableList<BannerModel> bannerList,
                                       ObservableField<ViewBindingAdapter.BannerListener> bannerListener, String channel, ADdataListener listener) {
        JSONObject object = new JSONObject();
        object.put("type", type);
        if (!TextUtils.isEmpty(channel)) object.put("from", channel);
        Call<BannerListModel> call = RDClient.getService(MainApi.class).getBannerList(object);
        call.enqueue(new RequestCallBack<BannerListModel>() {
            @Override
            public void onSuccess(Call<BannerListModel> call, Response<BannerListModel> response) {
                List<BannerModel> bannerModels = response.body().getBannerList();
                bannerList.clear();
                bannerList.addAll(bannerModels);
            }
        });
        BannerClickUtils.setBannerClick(context, bannerListener, bannerList, listener, channel);
    }

    private static void setBannerClick(final Context context, ObservableField<ViewBindingAdapter.BannerListener> bannerListener, final ObservableList<BannerModel> bannerList, final ADdataListener listener, final String channel) {
        bannerListener.set(new ViewBindingAdapter.BannerListener() {
            @Override
            public void onClick(int position) {
                BannerModel item = bannerList.get(position - 1);
                if (item != null) {
                    if (null != listener) listener.adDataCallBack(bannerList, position);
                    if (!TextUtils.isEmpty(channel)) unifySkip(item);
                    else if (ModelEnum.BRAND.getModel().equals(item.getType())) {
                        //跳转菠萝觅
                        String shopId = item.getContent();
                        String shopName = item.getTitleName();
                        if (MiscUtils.isEmpty(shopId)) {
                            UIUtils.showToast(AlaConfig.getResources().getString(R.string.toast_get_brand_url_err));
                            return;
                        }
                        goToShopPage(context, shopId, shopName);

                    } else if (ModelEnum.H5_URL.getModel().equals(item.getType())) {
                        Intent intent = new Intent();
                        //跳转H5
                        String shopUrl = item.getContent();
                        if (MiscUtils.isEmpty(shopUrl) || shopUrl.replace(" ", "").length() <= 0) {
                            return;
                        }
                        intent.putExtra(HTML5WebView.INTENT_BASE_URL, shopUrl);
                        ActivityUtils.push(HTML5WebView.class, intent);
                    }
                }
            }
        });
    }


    /**
     * 根据shopId,获取url,跳转到shop页面
     *
     * @param shopId 获取url时传入shop标识
     * @param title  url获取失败时，h5页面title
     */
    private static void goToShopPage(final Context context, final String shopId, final String title) {
        JSONObject requestObj = new JSONObject();
        requestObj.put("shopId", shopId);
        Call<BrandUrlModel> call = RDClient.getService(MainApi.class).getBrandUrl(requestObj);
        NetworkUtil.showCutscenes(context, call);
        call.enqueue(new RequestCallBack<BrandUrlModel>() {
            @Override
            public void onSuccess(Call<BrandUrlModel> call, Response<BrandUrlModel> response) {
                String shopUrl = response.body().getShopUrl();
                if (MiscUtils.isNotEmpty(shopUrl)) {
                    Intent intent = new Intent();
                    intent.putExtra(HTML5WebView.INTENT_BASE_URL, shopUrl);
                    ActivityUtils.push(HTML5WebView.class, intent, BundleKeys.REQUEST_CODE_BRAND);
                } else {
                    UIUtils.showToast(AlaConfig.getResources().getString(R.string.toast_get_brand_url_err));
                }
            }

            @Override
            public void onFailure(Call<BrandUrlModel> call, Throwable t) {
                super.onFailure(call, t);
                Intent intent = new Intent();
                intent.putExtra(HTML5WebView.INTENT_TITLE, title);
                intent.putExtra(HTML5WebView.INTENT_BASE_URL, "");
                ActivityUtils.push(HTML5WebView.class, intent, BundleKeys.REQUEST_CODE_BRAND);
            }
        });
    }

    public static void requestADdata(String type, final ADdataListener listener, String channel) {
        JSONObject object = new JSONObject();
        object.put("type", type);
        if (!TextUtils.isEmpty(channel)) object.put("from", channel);
        Call<BannerListModel> bannersCall = RDClient.getService(MainApi.class).getBannerList(object);
        bannersCall.enqueue(new RequestCallBack<BannerListModel>() {
            @Override
            public void onSuccess(Call<BannerListModel> call, Response<BannerListModel> response) {
                BannerListModel ads = response.body();
                if (null != listener) listener.adDataCallBack(ads.getBannerList(), -1);
            }
        });
    }

    public interface ADdataListener {
        void adDataCallBack(List<BannerModel> models, int position);
    }

    public static void unifySkip(BannerModel item) {
        String className = item.getClassName();
        if (null == className) return;
        if (1 == item.getNeedLogin() && !AlaConfig.isLand()) { // 需要登录
            Utils.jumpToLoginNoResult();
        } else {
            String[] pageID = className.split("_");
            try {
                if (MainActivity.class.getName().equals(pageID[0])) {
                    int mainTab = Integer.valueOf(pageID[1]);
                    Intent i = new Intent();
                    i.putExtra(BundleKeys.MAIN_DATA_TAB, mainTab);
                    ActivityUtils.push(MainActivity.class, i);
                } else {
                    Class clazz = Class.forName(pageID[0]);
                    JSONObject object = JSON.parseObject(item.getParamDic());
                    if (object.isEmpty())
                        ActivityUtils.push(clazz);
                    else {
                        Intent intent = new Intent();
                        for (String s : object.keySet()) {
//                            Boolean obj = object.getBoolean(s);
                            Object obj = object.get(s);
                            // 只支持boolean, int, long, String类型
                            if (obj instanceof Boolean)
                                intent.putExtra(s, (boolean) obj);
                            else if (obj instanceof Integer)
                                intent.putExtra(s, (int) obj);
                            else if (obj instanceof Long)
                                intent.putExtra(s, (long) obj);
                            else if (obj instanceof String)
                                intent.putExtra(s, (String) obj);
                        }
                        ActivityUtils.push(clazz, intent);
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}
