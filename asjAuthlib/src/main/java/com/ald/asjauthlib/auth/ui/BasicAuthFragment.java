package com.ald.asjauthlib.auth.ui;

import android.os.Bundle;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.model.CreditPromoteModel;
import com.ald.asjauthlib.auth.viewmodel.BasicAuthVM;
import com.ald.asjauthlib.databinding.FragmentBasicAuthBinding;
import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.authframework.core.config.AlaActivity;
import com.ald.asjauthlib.authframework.core.config.AlaBaseFragment;

/**
 * 基础认证界面
 * Created by sean yu on 2017/7/11.
 */

public class BasicAuthFragment extends AlaBaseFragment<FragmentBasicAuthBinding> {

    private BasicAuthVM viewModel;
    private CreditPromoteModel model;
    private String scene;

    public static BasicAuthFragment newInstance(CreditPromoteModel model, String scene) {
        BasicAuthFragment fragment = new BasicAuthFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(BundleKeys.FRAGMENT_DATA, model);
        bundle.putString(BundleKeys.INTENT_KEY_CREDIT_PROMOTE_SCENE, scene);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void initVariables(Bundle bundle) {
        super.initVariables(bundle);
        model = (CreditPromoteModel) bundle.getSerializable(BundleKeys.FRAGMENT_DATA);
        scene = bundle.getString(BundleKeys.INTENT_KEY_CREDIT_PROMOTE_SCENE);
    }

    @Override
    protected void initViews() {
        super.initViews();
        //if (viewModel == null) {
            viewModel = new BasicAuthVM((AlaActivity) getActivity(), cvb, scene);
            cvb.setViewModel(viewModel);
       // }
        viewModel.setCreditPromoteModel(model);
        viewModel.registerReceiver();
        viewModel.setOpenNativeHandler();
    }

    public void refresh(CreditPromoteModel model) {
        if (viewModel == null)
            viewModel = new BasicAuthVM((AlaActivity) getActivity(), cvb, scene);
        viewModel.displayView(model);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModel.unregisterReceiver();
        viewModel.onDestroy();
    }

    @Override
    public String getStatName() {
        return "基础认证";
    }

    @Override
    public int getLayoutInflate() {
        return R.layout.fragment_basic_auth;
    }
}
