package com.ald.asjauthlib.cashier.viewmodel;

import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ald.asjauthlib.BR;
import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.BusinessApi;
import com.ald.asjauthlib.auth.model.BannerModel;
import com.ald.asjauthlib.cashier.model.BillsModel;
import com.ald.asjauthlib.cashier.ui.RepaymentActivity;
import com.ald.asjauthlib.databinding.FragmentHasBillBinding;
import com.ald.asjauthlib.utils.BannerClickUtils;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.network.RequestCallBack;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.DensityUtils;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.authframework.core.utils.ScreenMatchUtils;
import com.ald.asjauthlib.authframework.core.vm.BaseRecyclerViewVM;

import java.util.List;

import com.ald.asjauthlib.tatarka.bindingcollectionadapter.ItemView;
import com.ald.asjauthlib.tatarka.bindingcollectionadapter.LayoutManagers;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by wjy on 2017/12/14.
 */

public class HasBillsVM extends BaseRecyclerViewVM<HasBillItemVM> implements View.OnClickListener {
    public final ObservableBoolean isRebound = new ObservableBoolean(true);
    public final ObservableBoolean showAD = new ObservableBoolean();
    public final ObservableBoolean noBillsImage = new ObservableBoolean();
    public final ObservableBoolean noBillsText = new ObservableBoolean();
    public final ObservableBoolean hasTextVis = new ObservableBoolean();
    public final ObservableBoolean hasBtnVis = new ObservableBoolean();
    public final ObservableField<String> hasBillsText = new ObservableField<>();
    public final ObservableField<String> billStatus = new ObservableField<>();

    private Fragment frag;
    private FragmentHasBillBinding binding;
    private boolean isOpen;
    private PopupWindow popupWindow;
    private int xoff;
    private int adWidth, adHeight, paddingBottom, paddingRight, paddingLeft;

    @Override
    protected void selectView(ItemView itemView, int position, HasBillItemVM item) {
        if (HasBillItemVM.ITEM_HEAD_YEAR == item.getItemType())
            itemView.set(BR.viewModel, R.layout.recyclerview_has_bill_item_head);
        else if (HasBillItemVM.ITEM_BODY_BILL == item.getItemType())
            itemView.set(BR.viewModel, R.layout.recyclerview_has_bill_item);
        else if (HasBillItemVM.ITEM_FOOT == item.getItemType())
            itemView.set(BR.viewModel, R.layout.recyclerview_has_bill_foot);
        else if (HasBillItemVM.ITEM_BODY_AD == item.getItemType())
            itemView.set(BR.viewModel, R.layout.recyclerview_ad_item);
    }

    public HasBillsVM(Fragment frag, FragmentHasBillBinding binding) {
        this.frag = frag;
        adWidth = ScreenMatchUtils.hasBillsItemADWidth(frag.getContext());
        adHeight = ScreenMatchUtils.hasBillsItemADHeight(adWidth, frag.getContext());
        paddingBottom = DensityUtils.getPxByDip(10);
        paddingRight = DensityUtils.getPxByDip(0);
        paddingLeft = DensityUtils.getPxByDip(5);
        this.binding = binding;
        billStatus.set(frag.getResources().getString(R.string.bills_paid));
    }

    public void fillData(BillsModel billsModel) {
        items.clear();
        double money = billsModel.getMoney();
        if (0 == money) {
            noBillsImage.set(true);
            noBillsText.set(true);
            hasTextVis.set(false);
            hasBtnVis.set(false);
        } else {
            noBillsText.set(false);
            noBillsImage.set(false);
            hasTextVis.set(true);
            hasBtnVis.set(true);
            hasBillsText.set(money + "");
        }

        List<BillsModel.BillListBean> been = billsModel.getBillList();
        showAD.set(false);
        if (MiscUtils.isEmpty(been) && 0 == money) {
//            binding.recyclerView.setLayoutManager(LayoutManagers.grid(2).create(binding.recyclerView));
//            requestADdata();
        } else {
            binding.recyclerView.setLayoutManager(LayoutManagers.linear().create(binding.recyclerView));
            for (BillsModel.BillListBean bean : been) {
                items.add(new HasBillItemVM(bean, HasBillItemVM.ITEM_HEAD_YEAR,frag.getActivity()));
                List<BillsModel.BillListBean.BillsBean> billsBeen = bean.getBills();
                for (BillsModel.BillListBean.BillsBean billsBean : billsBeen) {
                    items.add(new HasBillItemVM(billsBean, HasBillItemVM.ITEM_BODY_BILL,frag.getActivity()));
                }
            }
            if (0 != money) items.add(new HasBillItemVM(HasBillItemVM.ITEM_FOOT,frag.getActivity()));
        }
    }

    public void hasBillClick(View view) {
        isOpen = !isOpen;
        int drawable;
        drawable = isOpen ? R.drawable.bill_open : R.drawable.bill_close;
        binding.billStatus.setCompoundDrawablesWithIntrinsicBounds(null, null, frag.getResources().getDrawable(drawable), null);
        showPopupWindow(view);
    }

    private void showPopupWindow(View view) {
        if (null != popupWindow) {
            if (!popupWindow.isShowing()) popupWindow.showAsDropDown(view, -xoff, 0);
        } else {
            View popupView = LayoutInflater.from(frag.getContext()).inflate(R.layout.popup_has_bills, null);
            TextView billsPaid = popupView.findViewById(R.id.billsPaid);
            billsPaid.setOnClickListener(this);
            TextView onlyOverdue = popupView.findViewById(R.id.onlyOverdue);
            onlyOverdue.setOnClickListener(this);
            TextView onlyMonth = popupView.findViewById(R.id.onlyMonth);
            onlyMonth.setOnClickListener(this);
            popupWindow = new PopupWindow(popupView, DensityUtils.getPxByDip(180), DensityUtils.getPxByDip(179));
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    isOpen = false;
                    binding.billStatus.setCompoundDrawablesWithIntrinsicBounds(null, null, frag.getResources().getDrawable(R.drawable.bill_close), null);
                }
            });
            popupWindow.setBackgroundDrawable(frag.getResources().getDrawable(R.drawable.bubble_has_bill));
//            int[] location = new int[2];
//            view.getLocationOnScreen(location);
            xoff = DensityUtils.getPxByDip(31); // X偏移量
            //在控件的下方弹出窗口
            popupWindow.showAsDropDown(view, -xoff, 0);
            //上边
            //popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0] - view.getWidth(), location[1]);
            //左边
            //popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0] - view.getWidth(), location[1]);
            //右边
            //popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0] + view.getWidth(), location[1]);
        }
    }

    private void dismissPopupWindow() {
        if (null != popupWindow && popupWindow.isShowing()) popupWindow.dismiss();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.billsPaid) {
            requestBillsData("outBill");

        } else if (i == R.id.onlyOverdue) {
            requestBillsData("overdueBill");

        } else if (i == R.id.onlyMonth) {
            requestBillsData("nowBill");

        }
        billStatus.set(((TextView) v).getText().toString());
        dismissPopupWindow();
    }

    public void hasBtnClick(View view) {
        Intent intent = new Intent();
        intent.putExtra(BundleKeys.STAGE_JUMP, frag.getActivity().getIntent().getStringExtra(BundleKeys.STAGE_JUMP));
        ActivityUtils.push(RepaymentActivity.class, intent);
    }

    private void requestBillsData(String status) {
        JSONObject outBill = new JSONObject();
        outBill.put("status", status);
        Call<BillsModel> call1 = RDClient.getService(BusinessApi.class).getMyBorrowListV1(outBill);
        call1.enqueue(new RequestCallBack<BillsModel>() {
            @Override
            public void onSuccess(Call<BillsModel> call, Response<BillsModel> response) {
                BillsModel model = response.body();
                fillData(model);
            }
        });
    }

    private void requestADdata() {
        BannerClickUtils.requestADdata(BannerClickUtils.TYPE_YES_PAY, new BannerClickUtils.ADdataListener() {
            @Override
            public void adDataCallBack(List<BannerModel> models, int position) {
                if (0 == models.size()) return;
                List<BannerModel> children = null;
                if (models.size() >= 4)
                    children = models.subList(0, 4);
                else if (models.size() >= 2)
                    children = models.subList(0, 2);
                if (null == children) return;
                showAD.set(true);
                for (int i = 0; i < children.size(); i++) {
                    items.add(new HasBillItemVM(frag.getActivity(), i + 1, children.get(i), HasBillItemVM.ITEM_BODY_AD, adWidth, adHeight, paddingBottom, paddingRight, paddingLeft));
                }
            }
        }, BannerClickUtils.TYPE_SPECIAL);
    }

}
