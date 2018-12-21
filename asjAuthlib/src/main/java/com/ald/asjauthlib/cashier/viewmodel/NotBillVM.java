package com.ald.asjauthlib.cashier.viewmodel;

import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.v4.app.Fragment;
import android.view.View;

import com.ald.asjauthlib.BR;
import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.utils.Constant;
import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.model.BannerModel;
import com.ald.asjauthlib.cashier.model.BillsModel;
import com.ald.asjauthlib.cashier.ui.SettleAdvancedActivity;
import com.ald.asjauthlib.databinding.FragmentNotBillBinding;
import com.ald.asjauthlib.utils.BannerClickUtils;
import com.ald.asjauthlib.web.HTML5WebView;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.authframework.core.vm.BaseRecyclerViewVM;

import java.util.List;

import com.ald.asjauthlib.tatarka.bindingcollectionadapter.ItemView;

/*
 * Created by wjy on 2017/12/14.
 */

public class NotBillVM extends BaseRecyclerViewVM<NotBillItemVM> {

    public final ObservableBoolean isRebound = new ObservableBoolean(true);

    public final ObservableBoolean noStatus = new ObservableBoolean();
    public final ObservableBoolean billsVis = new ObservableBoolean();
    public final ObservableField<String> noBillsAmount = new ObservableField<>();
    private Fragment frag;
    public FragmentNotBillBinding binding;

    public NotBillVM(Fragment frag, FragmentNotBillBinding binding) {
        this.frag = frag;
        this.binding = binding;
    }

    @Override
    protected void selectView(ItemView itemView, int position, NotBillItemVM item) {
        if (NotBillItemVM.ITEM_HEAD_YEAR == item.getItemType())
            itemView.set(BR.viewModel, R.layout.recyclerview_not_bill_item_head);
        else if (NotBillItemVM.ITEM_BODY_Bill == item.getItemType())
            itemView.set(BR.viewModel, R.layout.recyclerview_not_bill_item);
        else if (NotBillItemVM.ITEM_HEAD_AD == item.getItemType())
            itemView.set(BR.viewModel, R.layout.textview_ad_title);
        else if (NotBillItemVM.ITEM_BODY_AD == item.getItemType())
            itemView.set(BR.viewModel, R.layout.recyclerview_ad_item);
    }

    public void fillData(BillsModel billsModel, int adItemWidth, int adItemHeight) {
        if (0 == billsModel.getMoney()) {
            noStatus.set(true);
            billsVis.set(false);
        } else {
            noStatus.set(false);
            billsVis.set(true);
            noBillsAmount.set(billsModel.getMoney() + "");
        }
        List<BillsModel.BillListBean> been = billsModel.getBillList();
        if (MiscUtils.isEmpty(been)) {
//            requestADdata(adItemWidth, adItemHeight);
        } else {
            for (BillsModel.BillListBean bean : been) {
                items.add(new NotBillItemVM(bean,frag.getActivity()));
                List<BillsModel.BillListBean.BillsBean> billsBeen = bean.getBills();
                for (BillsModel.BillListBean.BillsBean billsBean : billsBeen) {
                    items.add(new NotBillItemVM(billsBean,frag.getActivity()));
                }
            }
        }
    }

    public void noBillsClick(View view) {
        Intent intent = new Intent();
        intent.putExtra(BundleKeys.STAGE_JUMP, frag.getActivity().getIntent().getStringExtra(BundleKeys.STAGE_JUMP));
        ActivityUtils.push(SettleAdvancedActivity.class, intent);

    }

    public void questionClick(View view) {
        String url = AlaConfig.getServerProvider().getAppServer() + Constant.H5_URL_AHEAD_RETURN;
        Intent intent = new Intent();
        intent.putExtra(HTML5WebView.INTENT_BASE_URL, url);
        intent.putExtra(HTML5WebView.INTENT_TITLE, view.getContext().getResources().getString(R.string.settle_advanced_title_what));
        ActivityUtils.push(HTML5WebView.class, intent);
    }

    private void requestADdata(final int adItemWidth, final int adItemHeight) {
        BannerClickUtils.requestADdata(BannerClickUtils.TYPE_NO_PAY, new BannerClickUtils.ADdataListener() {
            @Override
            public void adDataCallBack(List<BannerModel> models, int position) {
                if (0 == models.size()) return;
                items.add(new NotBillItemVM(frag.getActivity()));
                for (int i = 0; i < models.size(); i++) {
                    items.add(new NotBillItemVM(frag.getActivity(), models.get(i), adItemWidth, adItemHeight, i + 1));
                }
            }
        }, BannerClickUtils.TYPE_SPECIAL);
    }

}
