package com.ald.asjauthlib.cashier.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.ald.asjauthlib.BR;
import com.ald.asjauthlib.utils.Constant;
import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.UserApi;
import com.ald.asjauthlib.bindingadapter.view.ViewBindingAdapter;
import com.ald.asjauthlib.cashier.BrandApi;
import com.ald.asjauthlib.cashier.model.MyTicketListModel;
import com.ald.asjauthlib.cashier.model.MyTicketModel;
import com.ald.asjauthlib.cashier.ui.MyTicketFragment;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.network.RequestCallBack;
import com.ald.asjauthlib.authframework.core.ui.DividerLine;
import com.ald.asjauthlib.authframework.core.vm.BaseRecyclerViewVM;

import com.ald.asjauthlib.tatarka.bindingcollectionadapter.ItemView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 版权：XXX公司 版权所有
 * 作者：TonyChen
 * 版本：1.0
 * 创建日期：2017/3/4
 * 描述：我的优惠券子类控制器
 * 修订历史：
 */
public class MyTicketsChildVM extends BaseRecyclerViewVM<CouponListItemVM> {

    //状态 【 EXPIRE:过期 ; NOUSE:未使用 ， USED:已使】
    private int ticketType;

    private String status;
    private String brandVoucherType;//品牌抵用券类型(1:可使用优惠券 2:已使用优惠券 3:过期优惠券 4:所有优惠券)

    private MyTicketFragment fragment;


    private int page = 0;

    private int menuType = Constant.VOUCHER_MENU_TYPE_PLATFORM;//优惠券类型,0:品台优惠券 1:品牌优惠券

    public ObservableBoolean showNoData = new ObservableBoolean(true);//是否显示无数据页面
    public ObservableBoolean showList = new ObservableBoolean(false);//是否显示无数据页面
    public final ObservableField<String> tipMsg = new ObservableField<>("暂无数据");
    public final ObservableField<ViewBindingAdapter.PullToRefreshListener> pullToRefresh = new ObservableField<>();

    public MyTicketsChildVM(MyTicketFragment fragment, int ticketType) {
        this.ticketType = ticketType;
        this.fragment = fragment;
        type = DividerLine.DEFAULT;
        switch (ticketType) {
            case 0:
                status = "NOUSE";
                brandVoucherType = "1";
                break;
            case 1:
                status = "USED";
                brandVoucherType = "2";
                break;
            case 2:
                status = "EXPIRE";
                brandVoucherType = "3";
                break;
        }
        pullToRefresh.set(new RefreshListener());

    }

    @Override
    protected void selectView(ItemView itemView, int position, CouponListItemVM item) {
        itemView.set(BR.viewModel, R.layout.list_item_coupon);
    }

    public void reLoad() {
        page = 1;
        JSONObject jsonObject = new JSONObject();
        if (menuType == Constant.VOUCHER_MENU_TYPE_PLATFORM) {
            jsonObject.put("status", status);
        } else {
            jsonObject.put("type", brandVoucherType);
        }
        jsonObject.put("pageNo", String.valueOf(page));
        requestData(jsonObject);
    }

    //请求数据
    private void loadMore() {
        JSONObject jsonObject = new JSONObject();
        if (menuType == Constant.VOUCHER_MENU_TYPE_PLATFORM) {
            jsonObject.put("status", status);
            page++;
        } else {
            jsonObject.put("type", brandVoucherType);
        }
        jsonObject.put("pageNo", String.valueOf(page));
        requestData(jsonObject);

    }

    /**
     * 请求数据
     */
    private void requestData(JSONObject jsonObject) {
        Call<MyTicketListModel> call;
        if (menuType == Constant.VOUCHER_MENU_TYPE_PLATFORM) {
            call = RDClient.getService(UserApi.class).getMineCouponList(jsonObject);
        } else {
            call = RDClient.getService(BrandApi.class).getBrandCouponList(jsonObject);
        }
        call.enqueue(new RequestCallBack<MyTicketListModel>() {
            @Override
            public void onSuccess(Call<MyTicketListModel> call, final Response<MyTicketListModel> response) {
                if (response.body() == null) {
                    showNoData.set(true);
                    showList.set(false);
                    tipMsg.set("加载失败");
                    return;
                }

                if (response.body().getCouponList() == null) {
                    showNoData.set(true);
                    showList.set(false);
                    tipMsg.set(getEmptyTip());
                    return;
                }
                if (emptyState.isLoading()) {
                    emptyState.setLoading(false);
                }
                // 刷新时重新加载
                if (page == 1) {
                    items.clear();
                }

                //若领券中心URL不为空则显示领券中心按钮
                /*if (MiscUtils.isNotEmpty(response.body().getCouponCenterUrl())) {
                    ((MyTicketActivity) fragment.getActivity()).setRightText(fragment.getActivity().getResources().getString(R.string.my_ticket_operation_right), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.putExtra(HTML5WebView.INTENT_BASE_URL, response.body().getCouponCenterUrl());
                            ActivityUtils.push(HTML5WebView.class, intent);
                        }
                    }, fragment.getActivity().getResources().getColor(R.color.color_coupon_title_right_text));
                }*/

                for (MyTicketModel ticketModel : response.body().getCouponList()) {
                    items.add(new CouponListItemVM(fragment.getActivity(), ticketModel, menuType, false));
                }
                showNoData.set(false);
                showList.set(true);
                if (menuType == Constant.VOUCHER_MENU_TYPE_BRAND) {
                    page = response.body().getNextPageNo();
                }

                if (items.size() <= 0) {
                    showNoData.set(true);
                    showList.set(false);
                    tipMsg.set(getEmptyTip());
                }
            }
        });
    }

    private String getEmptyTip() {
        switch (ticketType) {
            case 0:
                return "暂无未使用优惠券";
            case 1:
                return "暂无已使用优惠券";
            case 2:
                return "暂无已过期优惠券";
        }
        return "暂无数据";
    }

    public class RefreshListener implements ViewBindingAdapter.PullToRefreshListener {

        @Override
        public void pullForBottom() {
            if (menuType == Constant.VOUCHER_MENU_TYPE_PLATFORM) {
                loadMore();
            } else {
                if (page != 0) {
                    loadMore();
                }
            }
        }
    }
}
