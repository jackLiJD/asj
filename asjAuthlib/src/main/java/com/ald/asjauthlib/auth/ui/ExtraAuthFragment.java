package com.ald.asjauthlib.auth.ui;

import android.content.Intent;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.model.CreditPromoteModel;
import com.ald.asjauthlib.auth.viewmodel.ExtraAuthVM;
import com.ald.asjauthlib.databinding.FragmentExtraAtuhBinding;
import com.ald.asjauthlib.authframework.core.config.AlaBaseFragment;

/**
 * 补充认证界面
 * Created by sean yu on 2017/7/11.
 */
public class ExtraAuthFragment extends AlaBaseFragment<FragmentExtraAtuhBinding> {

    private ExtraAuthVM viewModel;
    private String scene;

    @Override
    public String getStatName() {
        return "补充认证";
    }

    @Override
    public int getLayoutInflate() {
        return R.layout.fragment_extra_atuh;
    }

    @Override
    protected void initViews() {
        super.initViews();
        if (viewModel == null) {
            CreditPromoteModel creditModel = (CreditPromoteModel) getArguments().getSerializable("model");
            scene = getArguments().getString("scene");
            viewModel = new ExtraAuthVM(getActivity(), creditModel, scene);
            cvb.setViewModel(viewModel);
        }
        viewModel.setOpenNativeHandler(viewModel);
//        viewModel.requestExtraAuthInfo();
    }

    /**
     * 下拉刷新
     */
    public void refresh(CreditPromoteModel model) {
        if (viewModel == null) {
            initViews();
        }
        viewModel.displayAuthView(model);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (viewModel != null) {
            viewModel.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModel.onDestroy();
    }
}
