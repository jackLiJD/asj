package com.ald.asjauthlib.auth.viewmodel;

import android.app.Activity;
import android.databinding.ObservableField;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.utils.AppUtils;
import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.vm.ViewModel;

/**
 * 认证用户信息
 * Created by sean yu on 2017/6/12.
 */
public class AuthUserInfoVM implements ViewModel {

    public final CardInfoView cardInfoView = new CardInfoView();

    public AuthUserInfoVM(Activity context) {
        context.setTitle(AlaConfig.getResources().getString(R.string.auth_user_bind_card_info));
        String userInfo = context.getIntent().getStringExtra(BundleKeys.BANK_CARD_NAME);
        cardInfoView.displayName.set(AppUtils.formatRealName(userInfo));
        String cardNumber = context.getIntent().getStringExtra(BundleKeys.SETTING_PAY_CARD_NUMBER);
        cardInfoView.displayCardNumber.set(AppUtils.formatBankCardNo(cardNumber));
        String phoneNumber = context.getIntent().getStringExtra(BundleKeys.SETTING_PAY_PHONE);
        cardInfoView.displayPhone.set(AppUtils.formatPhoneFourStar(phoneNumber));

    }

    public static class CardInfoView {
        public final ObservableField<String> displayName = new ObservableField<>();
        public final ObservableField<String> displayPhone = new ObservableField<>();
        public final ObservableField<String> displayCardNumber = new ObservableField<>();
    }

}
