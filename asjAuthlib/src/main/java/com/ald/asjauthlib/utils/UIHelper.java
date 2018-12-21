package com.ald.asjauthlib.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.widget.EditText;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.MainApi;
import com.ald.asjauthlib.auth.model.BannerModel;
import com.ald.asjauthlib.auth.model.BrandUrlModel;
import com.ald.asjauthlib.web.HTML5WebView;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.network.NetworkUtil;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.network.RequestCallBack;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.authframework.core.utils.UIUtils;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by yangfeng01 on 2017/9/4.
 */

public class UIHelper {

    private static final long MIN_CLICK_INTERVAL = 1000;    // 1秒内不能重复点击
    private static long lastClickTime;

    /**
     * dip转成对应分辨率的像素
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, double dpValue) {
        float density = AlaConfig.getResources().getDisplayMetrics().density;
        return (int) (dpValue * density + 0.5);
    }

    /**
     * 改变EditText hint的字体大小
     *
     * @param editText
     * @param hint
     * @param textSize 字体大小,ep:@dimen/x24
     */
    public static void editHintTextSize(EditText editText, String hint, int textSize) {
        if (editText == null) {
            return;
        }
        try {
            SpannableString spannableString = new SpannableString(hint);
            AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(textSize, false);
            spannableString.setSpan(absoluteSizeSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            editText.setHint(new SpannableString(spannableString));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 快速点击，即在1秒内重复点击
     *
     * @return
     */
    public static boolean isFastClick() {
        boolean result = false;
        long curClickTime = System.currentTimeMillis();
        if (curClickTime - lastClickTime < MIN_CLICK_INTERVAL) {
            result = true;
        }
        lastClickTime = curClickTime;
        return result;
    }

    /**
     * 删除一个字符串中的回车换行空格
     *
     * @param str
     * @return
     */
    public static String deleteSpace(String str) {
        str = str.replaceAll("\r", "");
        str = str.replaceAll("\n", "");
        str = str.replace(" ", "");
        return str;
    }

    /**
     * Banner数据类型的点击事件
     *
     * @param context
     * @param bannerModel
     */
    public static void clickBanner(Context context, BannerModel bannerModel) {
        if (bannerModel == null) {
            return;
        }
        Intent i = new Intent();
        if (ModelEnum.BRAND.getModel().equals(bannerModel.getType())) {    // 菠萝觅
            String shopId = bannerModel.getContent();
            String shopName = bannerModel.getTitleName();
            if (MiscUtils.isEmpty(shopId)) {
                UIUtils.showToast(AlaConfig.getResources().getString(R.string.toast_get_brand_url_err));
                return;
            }
            if (AlaConfig.isLand()) getBrandUrl(context, shopId, shopName);
            else {
                Utils.jumpToLoginNoResult();
            }
        } else if (ModelEnum.HOME_H5.getModel().equals(bannerModel.getType()) && MiscUtils.isNotEmpty(bannerModel.getContent())) {    // H5
            if (AlaConfig.isLand()) {
                i.putExtra(HTML5WebView.INTENT_BASE_URL, bannerModel.getContent());
                ActivityUtils.push(HTML5WebView.class, i);
            } else {
                Utils.jumpToLoginNoResult();
            }
        }
    }

    public static void jumpLink(Context context, String type, String content, String titleName) {
        Intent i = new Intent();
        if (ModelEnum.BRAND.getModel().equals(type)) {    // 菠萝觅
            if (MiscUtils.isEmpty(content)) {
                UIUtils.showToast(AlaConfig.getResources().getString(R.string.toast_get_brand_url_err));
                return;
            }
            if (AlaConfig.isLand()) getBrandUrl(context, content, titleName);
            else {
                Utils.jumpToLoginNoResult();
            }
        } else if (ModelEnum.HOME_H5.getModel().equals(type) && MiscUtils.isNotEmpty(content)) {    // H5
            Uri uri = Uri.parse(content);
            if (uri != null) {
                String isNeedLogin = uri.getQueryParameter("isNeedLogin");
                if ("true".equals(isNeedLogin) && !AlaConfig.isLand()) {
                    Utils.jumpToLoginNoResult();
                } else {
                    i.putExtra(HTML5WebView.INTENT_BASE_URL, content);
                    ActivityUtils.push(HTML5WebView.class, i);
                }
            }

        }
//		else if ("SELFSUPPORT".equals(model.getSource())) {	// 自营，注意区分自营商品，不能以淘宝商品的方式打开
//			UIHelper.goMallDetail(model.getGoodsId());
//		} else if ("TAOBAO".equals(model.getSource()) || "TMALL".equals(model.getSource())) { // 淘宝，天猫
//			intent.putExtra(BundleKeys.GOODS_DETAIL_GOODS_ID, content);
//			if ("1".equals(model.getIsWorm())) {
//				intent.putExtra(BundleKeys.GOODS_DETAIL_GOODS_WORM, model.getIsWorm());
//				ActivityUtils.push(ShoppingMallActivity.class, intent);
//			} else
//				ActivityUtils.push(TaoBaoActivity.class, intent);
//		}
    }

    /**
     * 跳转自营商品详情
     */
    public static void goMallDetail(String privateGoodsId) {
        Intent intent = new Intent();
        intent.putExtra(BundleKeys.GOODS_DETAIL_GOODS_ID, privateGoodsId);
        intent.putExtra(BundleKeys.REPLACE_MALLDETAILACTIVITY, true);
        intent.putExtra(BundleKeys.GOODS_DETAIL_GOODS_LC, "classification_result");
//		ActivityUtils.push(MallDetailActivity.class, intent);
//        ActivityUtils.push(ShoppingMallActivity.class, intent);
    }

    /**
     * 获取品牌URL
     *
     * @param shopId 品牌id
     * @param title  品牌name
     */
    private static void getBrandUrl(final Context context, final String shopId, final String title) {
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
                    intent.putExtra(HTML5WebView.INTENT_DEFAULT_TITLE, title);
                    ActivityUtils.push(HTML5WebView.class, intent);
                } else {
                    UIUtils.showToast(AlaConfig.getResources().getString(R.string.toast_get_brand_url_err));
                }
            }
        });
    }

    /**
     * 拨打客服电话
     */
    public static void telService(Context context, String phone) {
        Uri uri = Uri.parse("tel:" + phone);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }

//    public static void toastDialog(String msg, Context context) {
//        ToastDialog dialog = new ToastDialog(context);
//        dialog.setTip(msg);
//        dialog.show();
//    }
}
