package com.ald.asjauthlib.cashier.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.ObservableInt;
import android.view.View;

import com.ald.asjauthlib.BR;
import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.R;
import com.ald.asjauthlib.cashier.model.MyTicketModel;
import com.ald.asjauthlib.cashier.ui.PhoneTicketActivity;
import com.ald.asjauthlib.databinding.ActivityPhoneTicketBinding;
import com.ald.asjauthlib.authframework.core.network.entity.BaseModel;
import com.ald.asjauthlib.authframework.core.ui.DividerLine;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.authframework.core.vm.BaseRecyclerViewVM;

import java.util.List;

import com.ald.asjauthlib.tatarka.bindingcollectionadapter.ItemView;

/**
 * 手机充值优惠券viewModel
 * Created by sean on 2017/3/8.
 */
public class PhoneTicketVM extends BaseRecyclerViewVM<BaseModel> {

    public ObservableInt visible = new ObservableInt();
    private Activity context;
    private List<MyTicketModel> couponList;
    private MyTicketModel selectModel;//已选择优惠券

    public PhoneTicketVM(PhoneTicketActivity activity, ActivityPhoneTicketBinding binding) {
        this.context = activity;
        this.type = DividerLine.DEFAULT;
        this.couponList = (List<MyTicketModel>) context.getIntent().getSerializableExtra(BundleKeys.COUPON_LIST_DATA);
        this.selectModel = (MyTicketModel) context.getIntent().getSerializableExtra(BundleKeys.COUPON_SELECT_DATA);
        load();
    }

    @Override
    public void selectView(ItemView itemView, int position, BaseModel itemVM) {
        if (itemVM instanceof CouponListItemVM) {
            itemView.set(BR.viewModel, R.layout.list_item_coupon);
        } else if (itemVM instanceof ButtonItemVM) {
            itemView.set(BR.viewModel, R.layout.item_btn);
        }
    }

    private void load() {
        //还款页面选择优惠券
        if (MiscUtils.isNotEmpty(couponList)) {
            for (MyTicketModel itemMode : couponList) {
                if (selectModel != null && selectModel.getRid() == itemMode.getRid()) {
                    itemMode.setSelect(true);
                } else {
                    itemMode.setSelect(false);
                }
                items.add(new CouponListItemVM(context, itemMode, 0, true));
            }
            //添加btn
            items.add(new ButtonItemVM(context, selectModel));
            visible.set(View.GONE);
        } else {
            visible.set(View.VISIBLE);
        }
    }

    /**
     * 取消使用优惠券点击
     *
     * @param view
     */
    public void cancelSelectCouponClick(View view) {
        Intent intent = new Intent();
        intent.putExtra(BundleKeys.COUPON_LIST_DATA_RESULT, "");
        ActivityUtils.pop(context, intent);
    }
}
