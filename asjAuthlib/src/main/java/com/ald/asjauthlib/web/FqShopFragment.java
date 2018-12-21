package com.ald.asjauthlib.web;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.databinding.FragmentOperationBinding;
import com.ald.asjauthlib.authframework.core.config.AlaBaseFragment;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.utils.immersionBar.NoImmersionBar;


/**
 * Created by Yangyang on 2018/4/18.
 * desc:运营位tab
 */

public class FqShopFragment extends AlaBaseFragment<FragmentOperationBinding> implements NoImmersionBar {
    private FqShopVM fqShopVM;
    private boolean loginStatus;

    @Override
    public int getLayoutInflate() {
        return R.layout.fragment_operation;
    }

    @Override
    public String getStatName() {
        return "分期商城tab" ;
    }


    @Override
    public void onResume() {
        super.onResume();
        loginStatusListener();
    }

    private void loginStatusListener() {
        if (null != fqShopVM && loginStatus != AlaConfig.isLand()){
            loginStatus = AlaConfig.isLand();
            fqShopVM.loadUrl();
        }
    }

    @Override
    protected void initData() {
        super.initData();
        if (fqShopVM == null) {
            loginStatus = AlaConfig.isLand();
            fqShopVM = new FqShopVM(cvb, this);
            cvb.setViewModel(fqShopVM);
        }
    }
}
