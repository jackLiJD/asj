package com.ald.asjauthlib.cashier.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.view.View;

import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.R;
import com.ald.asjauthlib.cashier.CashierConstant;
import com.ald.asjauthlib.cashier.ui.CashierPayFailActivity;
import com.ald.asjauthlib.cashier.ui.SelectPaymentActivity;
import com.ald.asjauthlib.event.Event;
import com.ald.asjauthlib.event.EventBusUtil;
import com.ald.asjauthlib.event.EventCode;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.authframework.core.utils.UIUtils;
import com.ald.asjauthlib.authframework.core.vm.ViewModel;


/*
 * Created by liangchen on 2018/5/24.
 * 普通商品(COMMON)支付失败页面
 */

public class CashierPayFailVM implements ViewModel {


    public final ObservableBoolean showPermissionLayout = new ObservableBoolean(false);//是否显示购买权限包布局
    public final ObservableBoolean showBuyButton = new ObservableBoolean(false);
    public final ObservableField<String> displayCardImage = new ObservableField<>();
    public final ObservableField<String> displayVipHint = new ObservableField<>();
    private CashierPayFailActivity mActivity;
    private String permissionPackageId;
    private String permissionOrderId;//生成的权限包orderID
    private String stage;

    public CashierPayFailVM(CashierPayFailActivity activity) {
        this.mActivity = activity;
        String params = activity.getIntent().getStringExtra(BundleKeys.CASHIER_FAIL_PARAMS_JSON);
        stage = activity.getIntent().getStringExtra(BundleKeys.STAGE_JUMP);
        if (MiscUtils.isNotEmpty(params)) {
            JSONObject parseObject = JSONObject.parseObject(params);
            String isRecomend = parseObject.getString("isRecomend");
            if (MiscUtils.isEqualsIgnoreCase(isRecomend, "Y")) {
                showPermissionLayout.set(true);
                String cardUrl = parseObject.getString("goodsBanner");
                permissionPackageId = parseObject.getString("goodsId");
                permissionOrderId = parseObject.getString("vipGoodsOrderId");
                displayCardImage.set(cardUrl);
                String vipGoodsOrderPayStatus = parseObject.getString("vipGoodsOrderPayStatus");
                if (MiscUtils.isEqualsIgnoreCase(vipGoodsOrderPayStatus, "DEALING")) {
                    displayVipHint.set(AlaConfig.getResources().getString(R.string.permission_package_cashing_hint));//支付中
                    showBuyButton.set(false);
                } else {
                    displayVipHint.set(AlaConfig.getResources().getString(R.string.permission_package_buy_hint));//待支付（NEW）或支付失敗(PAYFAIL)
                    showBuyButton.set(true);
                }

            } else {
                //isRecomend=N  即普通商品支付失败，且不推荐用户购买其它商品
                showPermissionLayout.set(false);
                showBuyButton.set(false);
            }
        }

    }

    /**
     * 查看权限包详情
     */
    public void onShowPermissionDetailsClick(View view) {
        if (MiscUtils.isEmpty(permissionPackageId)) {
            UIUtils.showToast("商品id获取失败");
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(BundleKeys.GOODS_DETAIL_GOODS_AUTH, true);
        intent.putExtra(BundleKeys.GOODS_DETAIL_GOODS_ID, permissionPackageId + "");
//        ActivityUtils.push(ShoppingMallActivity.class, intent);
        UIUtils.showToast("查看权限包详情");
    }

    /**
     * 权限包购买，直接跳转收银台
     */
    public void onPermissionBuyClick(View view) {
        if (MiscUtils.isEmpty(permissionOrderId)) {
            UIUtils.showToast("orderid获取失败");
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("orderId", permissionOrderId);
        intent.putExtra("orderType", "SELFSUPPORT");
        intent.putExtra(CashierConstant.CASHIER_PAY_TYPE, CashierConstant.CASHIER_PAY_TYPE_NORMAL);
        intent.putExtra(BundleKeys.INTENT_KEY_CREDIT_PROMOTE_SCENE, BundleKeys.CREDIT_PROMOTE_SCENE_ONLINE);
        ActivityUtils.push(SelectPaymentActivity.class, intent);

    }

    /**
     * 重新支付点击
     */
    public void onRepayClick(View view) {
        //返回并刷新
        EventBusUtil.sendEvent(new Event(EventCode.CASHIER_RELOAD));
//        ActivityUtils.finish(OrderDetailActivity.class);
        mActivity.setResult(Activity.RESULT_OK);
        mActivity.finish();
    }

    public void onBackPressed() {
        ActivityUtils.pop();
//        if (MiscUtils.isEqualsIgnoreCase(stage, StageJumpEnum.STAGE_ORDER_LIST.getModel())) {
//            //关闭至订单列表
//            ActivityUtils.popUntil(OrderListActivity.class);
//        } else {
//            ActivityUtils.popUntilWithoutRefresh(ShoppingMallActivity.class);
//        }
    }
}
