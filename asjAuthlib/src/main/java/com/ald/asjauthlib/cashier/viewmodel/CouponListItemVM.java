package com.ald.asjauthlib.cashier.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;

import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.utils.Constant;
import com.ald.asjauthlib.R;
import com.ald.asjauthlib.cashier.model.MyTicketModel;
import com.ald.asjauthlib.utils.AppUtils;
import com.ald.asjauthlib.web.HTML5WebView;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.network.entity.BaseModel;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.DensityUtils;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.moxie.client.MainActivity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/1/19 18:01
 * 描述：
 * 修订历史：
 */

public class CouponListItemVM extends BaseModel {
    //context
    private Context context;
    //model
    public MyTicketModel itemModel;
    private int listType = 0;//列表类型 0:优惠券列表 1:菠萝觅优惠券列表
    private boolean isUse = false;//是否是从使用优惠券场景进入列表

    //field to presenter
    public final ObservableBoolean state = new ObservableBoolean();
    public final ObservableField<String> showCouponTypeName = new ObservableField<>();
    public final ObservableField<String> showUseRange = new ObservableField<>();//使用范围
    public final ObservableBoolean displayCouponStatement = new ObservableBoolean();
    public final ObservableField<String> couponStatement = new ObservableField<>();
    public final ObservableField<String> amount = new ObservableField<>();
    public final ObservableBoolean isValidate = new ObservableBoolean();//是否有效
    public final ObservableBoolean isUsing = new ObservableBoolean();//使用时


    public final ObservableBoolean isSelected = new ObservableBoolean();//是否选中
    public final ObservableField<Integer> inValidateRes = new ObservableField<>();//无效时图标

    /**
     * 券类型
     */
    //优惠券类型【MOBILE：话费充值， REPAYMENT：还款, FULLVOUCHER:满减卷,CASH:现金奖励,ACTIVITY:会场券,FREEINTEREST:借钱免息券
    //LOAN:白领贷还款券，BORROWCASH:现金贷还款券，BORROWBILL:分期账单还款券，DISCOUNT:折扣券】
    public interface CouponType {
        String MOBILE = "MOBILE";//充值券
        String REPAYMENT = "REPAYMENT";//还款券
        String FULLVOUCHER = "FULLVOUCHER";//满减券
        String CASH = "CASH";//现金券
        String ACTIVITY = "ACTIVITY";//会场券
        String FREEINTEREST = "FREEINTEREST";//借钱免息券
        String DISCOUNT = "DISCOUNT";
    }


    public CouponListItemVM(Context context, MyTicketModel itemModel, int listType, boolean isUse) {
        this.context = context;
        this.itemModel = itemModel;
        this.listType = listType;
        this.isUse = isUse;
        isUsing.set(isUse);
        if (this.context == null) {
            this.context = AlaConfig.getContext();
        }
        isSelected.set(itemModel.isSelect());
        isValidate();
        handleAmount();
        handleCouponTypeName();
        handleUseRange();
        setStatement();
    }

    //使用说明
    private void setStatement() {
        StringBuilder stringBuilder = new StringBuilder();
        if (itemModel.getGmtStart() != 0) {
            stringBuilder.append(String.format(AlaConfig.getResources().getString(R.string.repay_coupon_use_expiod), AppUtils.coverTimeYMD(itemModel.getGmtStart()),
                    AppUtils.coverTimeYMD(itemModel.getGmtEnd())));
            stringBuilder.append("\n");
        }
        stringBuilder.append(String.format(AlaConfig.getResources().getString(R.string.repay_coupon_use_statement), itemModel.getUseRange()));
        couponStatement.set(stringBuilder.toString());
    }

    public Spannable handleAmount() {
        Spannable span;
        if (CouponType.DISCOUNT.equals(itemModel.getType())) {
            String content = AppUtils.formatCouponAmount(new BigDecimal(itemModel.getDiscount().doubleValue() * 10)) + "折";
            span = new SpannableString(content);
            // 设置字体大小
            span.setSpan(new AbsoluteSizeSpan(DensityUtils.sp2px(ActivityUtils.peek(), 30)), 0, content.indexOf("折"),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            span = new SpannableString("￥" + AppUtils.formatAmount(itemModel.getAmount()));
            if (span.length() > 6) {
                span.setSpan(new AbsoluteSizeSpan(DensityUtils.sp2px(ActivityUtils.peek(), 18, true)), 1, span.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                span.setSpan(new AbsoluteSizeSpan(DensityUtils.sp2px(ActivityUtils.peek(), 30, true)), 1, span.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return span;
    }


    /**
     * 使用范围说明
     */
    //(品牌【1：金额满减,2：金额满折,3:数量满减,4:数量满折】)
    //MOBILE:充值券 REPAYMENT:还款券 FULLVOUCHER:满减券 CASH:现金券 ACTIVITY:会场券 FREEINTEREST:借钱免息券
    public void handleUseRange() {
        if (listType == Constant.VOUCHER_MENU_TYPE_PLATFORM) {
            if (CouponType.DISCOUNT.equals(itemModel.getType())) {
                StringBuilder limit = new StringBuilder();

                if (new BigDecimal(0).compareTo(itemModel.getLimitAmount()) == 0) {
                    limit.append(AlaConfig.getResources().getString(R.string.my_ticket_unlimit_format));
                } else {
                    limit.append(String.format(AlaConfig.getResources().getString(R.string.my_ticket_limit_format), AppUtils.formatCouponAmount(itemModel.getLimitAmount())));
                }
//                if (new BigDecimal(0).compareTo(itemModel.getAmount()) != 0) {
//                    limit.append(String.format(AlaConfig.getResources().getString(R.string.my_ticket_limit_max), AppUtils.formatCouponAmount(itemModel.getAmount())));
//                }
                showUseRange.set(limit.toString());
            } else {
                if (new BigDecimal(0).compareTo(itemModel.getLimitAmount()) == 0) {
                    showUseRange.set(AlaConfig.getResources().getString(R.string.my_ticket_unlimit_format));
                } else {
                    showUseRange.set(String.format(AlaConfig.getResources().getString(R.string.my_ticket_limit_format), AppUtils.formatCouponAmount(itemModel.getLimitAmount())));
                }
            }
        } else {
            if ("2".equals(itemModel.getType()) || "4".equals(itemModel.getType())) {
                BigDecimal limitAmount = itemModel.getLimitAmount();
                BigDecimal maxAmount = itemModel.getMaxAmount();
                if (MiscUtils.isEmpty(String.valueOf(limitAmount)) || "0".equals(String.valueOf(limitAmount))) {
                    showUseRange.set(AlaConfig.getResources().getString(R.string.my_ticket_limit_unlimited));
                }
                if (limitAmount.doubleValue() <= 0) {
                    if (MiscUtils.isNotEmpty(String.valueOf(maxAmount))) {
                        showUseRange.set(String.format(AlaConfig.getResources().getString(R.string.my_ticket_limit_format_max_unlimited), AppUtils.formatAmount(maxAmount)));
                        return;
                    }
                    showUseRange.set(AlaConfig.getResources().getString(R.string.my_ticket_limit_unlimited));
                }

                if (MiscUtils.isNotEmpty(String.valueOf(maxAmount))) {
                    showUseRange.set(String.format(AlaConfig.getResources().getString(R.string.my_ticket_limit_format_max), AppUtils.formatAmount(limitAmount), AppUtils.formatAmount(maxAmount)));
                }
                showUseRange.set(String.format(AlaConfig.getResources().getString(R.string.my_ticket_limit_format), AppUtils.formatAmount(limitAmount)));
            }
        }
    }

    /**
     * 处理优惠券类型显示
     */
    private void handleCouponTypeName() {
//        优惠券类型【MOBILE：话费充值， REPAYMENT：还款, FULLVOUCHER:满减卷,CASH:现金奖励,ACTIVITY:会场券,FREEINTEREST:借钱免息券
//        LOAN:白领贷还款券，BORROWCASH:现金贷还款券，BORROWBILL:分期账单还款券，DISCOUNT:折扣券】
        switch (itemModel.getType()) {
            case "CASH":
                showCouponTypeName.set(AlaConfig.getResources().getString(R.string.my_ticket_type_cash));
                break;
            case "MOBILE":
                showCouponTypeName.set(AlaConfig.getResources().getString(R.string.my_ticket_type_mobile));
                break;
            case "ACTIVITY":
                showCouponTypeName.set(AlaConfig.getResources().getString(R.string.my_ticket_type_activity));
                break;
            case "REPAYMENT":
                showCouponTypeName.set(AlaConfig.getResources().getString(R.string.my_ticket_type_repayment));
                break;
            case "2":
            case "DISCOUNT":
                showCouponTypeName.set(AlaConfig.getResources().getString(R.string.my_ticket_type_full_discount));
                break;
            //菠萝觅券类型
            default:
            case "1":
            case "FULLVOUCHER":
                showCouponTypeName.set(AlaConfig.getResources().getString(R.string.my_ticket_type_full));
                break;
        }
    }

    /**
     * 处理优惠券名称(因名称前有类型标识，优惠券名称首行需要空格)
     *
     * @return 优惠券名称
     */
    public String handleCouponName() {
        return itemModel.getName();
    }

    /**
     * 判断是否显示印章
     */
    public void isValidate() {
        if (MiscUtils.isEmpty(itemModel.getStatus()) || "NOUSE".equals(itemModel.getStatus())) {
            isValidate.set(true);
        } else if ("EXPIRE".equals(itemModel.getStatus())) {
            isValidate.set(false);
            inValidateRes.set(R.drawable.ic_coupon_invalidate);
        } else if ("USED".equals(itemModel.getStatus())) {
            isValidate.set(false);
            inValidateRes.set(R.drawable.ic_coupon_used);
        }
    }

    /**
     * 券的有效期
     */
    public String getValidityTime() {
//        if ("Y".equals(itemModel.getWillExpireStatus())) {
//            return AppUtils.returnDay(itemModel.getGmtEnd(), new Date().getTime());
//        } else {
//            return AppUtils.coverTimeYYMMDD(itemModel.getGmtEnd()) + "到期";
//        }
        return AppUtils.returnDay(itemModel.getGmtEnd(), new Date().getTime());
    }


    /**
     * item点击事件
     */
    public void itemClick(View view) {
        //若从使用优惠券场景进入
        if (isUse) {
            Intent intent = new Intent();
            intent.putExtra(BundleKeys.COUPON_LIST_DATA_RESULT, itemModel);
            ActivityUtils.pop((Activity) context, intent);
        } else {
            String url = itemModel.getShopUrl();
            Intent intent = new Intent();
            //平台优惠券列表
            if (listType == Constant.VOUCHER_MENU_TYPE_PLATFORM) {
                //URL为空则关闭当前及上一个页面,返回APP首页
                if (MiscUtils.isEmpty(url)) {
                    intent.putExtra(BundleKeys.MAIN_DATA_TAB, 0);
                    ActivityUtils.push(MainActivity.class, intent);
                    ActivityUtils.pop((Activity) context);
                } else {
                    //跳转URL
                    intent.putExtra(HTML5WebView.INTENT_BASE_URL, url);
                    ActivityUtils.push(HTML5WebView.class, intent);
                }
            } else if (listType == Constant.VOUCHER_MENU_TYPE_BRAND) {//品牌优惠券
                //URL为空则关闭当前及上一个页面,返回逛逛首页
                if (MiscUtils.isEmpty(url)) {
                    intent.putExtra(BundleKeys.MAIN_DATA_TAB, 1);
                    ActivityUtils.push(MainActivity.class, intent);
                    ActivityUtils.pop((Activity) context);
                } else {
                    //跳转URL
                    intent.putExtra(HTML5WebView.INTENT_BASE_URL, url);
                    ActivityUtils.push(HTML5WebView.class, intent);
                }
            }
        }
    }

    //展开说明
    public void toExpandView(View view) {
        displayCouponStatement.set(!displayCouponStatement.get());
    }
}
