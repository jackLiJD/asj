package com.ald.asjauthlib.cashier.viewmodel;

import android.content.Intent;
import android.databinding.ObservableField;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;

import com.ald.asjauthlib.utils.Constant;
import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.model.LoginModel;
import com.ald.asjauthlib.cashier.model.ConsumeDtlModel;
import com.ald.asjauthlib.web.HTML5WebView;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.info.SharedInfo;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.log.DateFormatter;
import com.ald.asjauthlib.authframework.core.vm.BaseVM;

import java.text.SimpleDateFormat;
import java.util.Locale;

/*
 * Created by luckyliang on 2017/12/11.
 */

public class ConsumDtlFrgVM extends BaseVM {

    public ObservableField<String> displayBus = new ObservableField<>();
    public ObservableField<String> displayPayWay = new ObservableField<>();
    public ObservableField<String> displayNper = new ObservableField<>();
    public ObservableField<String> displayOrderNo = new ObservableField<>();
    public ObservableField<String> displayTime = new ObservableField<>();
    public ObservableField<String> displayTitle = new ObservableField<>();
    public ObservableField<String> displayPrice = new ObservableField<>();
    public ObservableField<String> displayRebare = new ObservableField<>();
    public ObservableField<String> displayGoodsImage = new ObservableField<>();
    public ObservableField<String> displayAgreement = new ObservableField<>();
    public ObservableField<String> displayBorrowTime = new ObservableField<>();

    private Fragment frag;
    private ConsumeDtlModel model;

    public ConsumDtlFrgVM(Fragment frag) {
        this.frag = frag;
    }

    public void fillData(ConsumeDtlModel model) {
        this.model = model;
        ConsumeDtlModel.Order order = model.getOrderDetail();
        displayBus.set(order.getShopName());
        String payType = order.getPayType();
        if ("AP".equals(payType)) // 代付
            displayPayWay.set("代付\n" + order.getSaleAmount().toString() + "元");
        else if ("CP".equals(payType)) // 组合支付
            displayPayWay.set("组合支付\n" + "信用额度 " + order.getBorrowAmount().toString() + "元\n" + "其他支付 " + order.getBankAmount().toString() + "元");
        displayNper.set("分" + order.getNper() + "期");
        displayOrderNo.set(order.getOrderNo());
        displayTime.set(new SimpleDateFormat(DateFormatter.SS.getValue(), Locale.CHINA).format(order.getGmtPay()));
        displayGoodsImage.set(order.getGoodsIcon());
        displayTitle.set(TextUtils.isEmpty(order.getGoodsName()) ? order.getShopName() : order.getGoodsName());
        displayPrice.set("售价" + String.format(frag.getResources().getString(R.string.f_space_rmb), order.getSaleAmount().toString()) + "元");
        displayRebare.set(order.getRebateAmount().toString());
        displayBorrowTime.set(new SimpleDateFormat(DateFormatter.SS.getValue(), Locale.CHINA).format(model.getGmtBorrow()));
        displayAgreement.set(model.getBorrowNo());
    }

    public void agreementClick(View view) {
        if (null != model) {
            Intent intent = new Intent();
            String url = AlaConfig.getServerProvider().getAppServer() + Constant.H5_URL_FENQI_SERVER_V2;
            LoginModel loginModel = SharedInfo.getInstance().getValue(LoginModel.class);
            if (loginModel != null && loginModel.getUser() != null) {
                String userName = loginModel.getUser().getUserName();
                url = String.format(url, userName, model.getOrderDetail().getNper(), model.getAmount().toString(), model.getInterest().toString(), model.getBorrowId());
            }
            intent.putExtra(HTML5WebView.INTENT_BASE_URL, url);
            ActivityUtils.push(HTML5WebView.class, intent);
        }
    }
}
