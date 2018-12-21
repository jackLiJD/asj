package com.ald.asjauthlib.cashier.viewmodel;

import android.app.Activity;
import android.databinding.ObservableBoolean;

import com.ald.asjauthlib.BR;
import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.BusinessApi;
import com.ald.asjauthlib.cashier.model.HistoryBillListModel;
import com.ald.asjauthlib.cashier.model.ItemDataPair;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.network.RequestCallBack;
import com.ald.asjauthlib.authframework.core.ui.DividerLine;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.authframework.core.vm.BaseRecyclerViewVM;

import java.util.List;

import com.ald.asjauthlib.tatarka.bindingcollectionadapter.ItemView;
import retrofit2.Call;
import retrofit2.Response;


/*
 * Created by luckyliang on 2017/12/6.
 */

public class HistoryBillListVM extends BaseRecyclerViewVM<ItemHistoryBillVM> {

    private Activity mContext;

    private List<HistoryBillListModel.BillItem> billItemList;

    public static final int TYPE_HEAD = 0;
    public static final int TYPE_ITEM_LIST = 1;

//    public final ObservableField<ViewBindingAdapter.PullToRefreshListener> refreshListener = new ObservableField<>();

    public final ObservableBoolean showNoData = new ObservableBoolean(false);


    @Override
    protected void selectView(ItemView itemView, int position, ItemHistoryBillVM item) {
        if (item.getmItemDataPair().getItemType() == TYPE_HEAD)
            itemView.set(BR.viewModel, R.layout.list_item_history_bill_title);
        else
            itemView.set(BR.viewModel, R.layout.list_item_history_bill_dtl);

    }

//    ViewBindingAdapter.PullToRefreshListener refreshListener
//            = new ViewBindingAdapter.PullToRefreshListener() {
//        @Override
//        public void pullForBottom() {
//            load(ptrFrame);
//        }
//    };

    public HistoryBillListVM(Activity context) {
        this.type = DividerLine.DEFAULT;
        mContext = context;
        load();
//        this.refreshListener.set(new HistoryBillListVM.RefreshListener());
//        this.listener.set(new PtrFrameListener() {
//            @Override
//            public void ptrFrameInit(final PtrClassicFrameLayout ptrFrame) {
//                //PtrFrameLayout页面UI初始化配置
//                // header
//                final CommonRefreshHeader header = new CommonRefreshHeader(AlaConfig.getContext());
//                ptrFrame.setHeaderView(header);
//                ptrFrame.addPtrUIHandler(header);
//                ptrFrame.disableWhenHorizontalMove(true);
//                setPtrFrame(ptrFrame);
//            }
//
//            @Override
//            public void ptrFrameRefresh() {
//
//            }
//
//            @Override
//            public void ptrFrameRequest(PtrFrameLayout ptrFrame) {
//                load(ptrFrame);
//            }
//        });

    }

    public void load() {
        items.clear();
        //拉取数据
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("page", 1);//页数
        jsonObject.put("pageSize", 8);//每页记录数
        Call<HistoryBillListModel> call = RDClient.getService(BusinessApi.class).getMyHistoryBorrowV1(jsonObject);
        call.enqueue(new RequestCallBack<HistoryBillListModel>() {

            @Override
            public void onSuccess(Call<HistoryBillListModel> call, Response<HistoryBillListModel> response) {
                if (response.body() == null || MiscUtils.isEmpty(response.body().getList())) {
                    showNoData.set(true);
                    return;
                }
                showNoData.set(false);
                for (int i = 0; i < response.body().getList().size(); i++) {
                    billItemList = response.body().getList();
                    ItemDataPair itemDataPairHeader = new ItemDataPair(billItemList.get(i).getYear(), TYPE_HEAD);
                    items.add(new ItemHistoryBillVM(mContext, itemDataPairHeader, billItemList.get(i).getYear()));
                    List<HistoryBillListModel.Bill> bills = billItemList.get(i).getBills();
                    for (int j = 0; j < bills.size(); j++) {
                        ItemDataPair itemDataPair = new ItemDataPair(bills.get(j), TYPE_ITEM_LIST);
                        ItemHistoryBillVM itemHistoryBillVM = new ItemHistoryBillVM(mContext, itemDataPair, billItemList.get(i).getYear());
                        items.add(itemHistoryBillVM);
//                        billItemList = response.body().getList();
                    }
                }


            }
        });
    }


//    private class RefreshListener implements ViewBindingAdapter.PullToRefreshListener {
//        @Override
//        public void pullForBottom() {
//        }
//    }

}
