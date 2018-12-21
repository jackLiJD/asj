package com.ald.asjauthlib.cashier.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.utils.Constant;
import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.BusinessApi;
import com.ald.asjauthlib.auth.model.BannerModel;
import com.ald.asjauthlib.bindingadapter.view.ViewBindingAdapter;
import com.ald.asjauthlib.cashier.model.BillMonthModel;
import com.ald.asjauthlib.cashier.model.BillsModel;
import com.ald.asjauthlib.cashier.ui.RepaymentActivity;
import com.ald.asjauthlib.cashier.ui.SettleAdvancedActivity;
import com.ald.asjauthlib.databinding.ActivityBillDtlBinding;
import com.ald.asjauthlib.utils.AppUtils;
import com.ald.asjauthlib.web.HTML5WebView;
import com.alibaba.fastjson.JSONObject;
import com.android.databinding.library.baseAdapters.BR;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.network.RequestCallBack;
import com.ald.asjauthlib.authframework.core.ui.DividerLine;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.DensityUtils;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.authframework.core.utils.ScreenMatchUtils;
import com.ald.asjauthlib.authframework.core.vm.AlaObservableArrayList;
import com.ald.asjauthlib.authframework.core.vm.BaseRecyclerViewVM;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.footer.FalsifyFooter;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import java.util.List;

import com.ald.asjauthlib.tatarka.bindingcollectionadapter.ItemView;
import com.ald.asjauthlib.tatarka.bindingcollectionadapter.ItemViewSelector;
import retrofit2.Call;
import retrofit2.Response;

/*
 * Created by luckyliang on 2017/12/6.
 */

public class BillDtlVM extends BaseRecyclerViewVM<BillDtlItemVM> implements BillDtlMonthItemVM.OnMonthItemListener, OnRefreshLoadmoreListener, View.OnClickListener {

    public final ObservableField<String> billYear = new ObservableField<>();
    public final ObservableField<String> dateSpan = new ObservableField<>();
    public final ObservableField<String> billAmount = new ObservableField<>();
    public final ObservableField<String> deadline = new ObservableField<>();
    public final ObservableField<String> monthBtn = new ObservableField<>();
    public final ObservableField<String> imgText = new ObservableField<>();
    public final ObservableField<Drawable> imgSrc = new ObservableField<>();
    public final ObservableInt yearWidth = new ObservableInt();
    public final ObservableBoolean imgVis = new ObservableBoolean();
    public final ObservableBoolean qusVis = new ObservableBoolean();
    public final ObservableArrayList<BannerModel> bannerList = new ObservableArrayList<>();
    public final ObservableField<ViewBindingAdapter.BannerListener> bannerListener = new ObservableField<>();

    private boolean hasNotIn; // 有未入账订单
    //    private List<BillMonthModel.BorrowBean> borrowBeen;
    private BillDtlItemVM noInBillTip;
    private BillMonthModel billMonthModel;

    private PopupWindow popupWindow;
    private String selectMonth;
    private int xoff, yoff;

    @Override
    protected void selectView(ItemView itemView, int position, BillDtlItemVM item) {
        if (BillDtlItemVM.ITEM_HEAD == item.getItemType())
            itemView.set(BR.viewModel, R.layout.recyclerview_dtl_head);
        else if (BillDtlItemVM.ITEM_BODY_DTL == item.getItemType())
            itemView.set(BR.viewModel, R.layout.recyclerview_dtl_item);
        else if (BillDtlItemVM.ITEM_BODY_AD == item.getItemType())
            itemView.set(BR.viewModel, R.layout.recyclerview_banners_item);
    }

    public AlaObservableArrayList<BillDtlMonthItemVM> months = new AlaObservableArrayList<>();
    public final ItemViewSelector<BillDtlMonthItemVM> monthView = new ItemViewSelector<BillDtlMonthItemVM>() {
        @Override
        public void select(ItemView itemView, int position, BillDtlMonthItemVM item) {
            itemView.set(BR.viewModel, R.layout.recyclerview_month_item);
        }

        @Override
        public int viewTypeCount() {
            return getViewTypeCount();
        }
    };

    private Activity act;
    private ActivityBillDtlBinding binding;

    public BillDtlVM(final Activity act, final BillsModel.BillListBean.BillsBean billsBean, final ActivityBillDtlBinding binding) {
        this.act = act;
        this.binding = binding;
        type = DividerLine.HORIZONTAL_DEFAULT;
        billYear.set(billsBean.getBillYear() + "");
        yearWidth.set(ScreenMatchUtils.billYearWidth(act));
        fillMonths();
        final int selectMonth = billsBean.getBillMonth();
        this.selectMonth = selectMonth + "";
        binding.recyclerView.post(new Runnable() {
            @Override
            public void run() {
                binding.recyclerView.smoothScrollToPosition(selectMonth + 1);
                months.get(selectMonth - 1).setMonthColor(ContextCompat.getColor(AlaConfig.getContext(), R.color.color_ff7722));
            }
        });
        requestMonthData(billYear.get(), selectMonth + "");
        binding.refreshLayout.setOnRefreshLoadmoreListener(this);
    }

    private void fillMonths() {
        int monthWidth = ScreenMatchUtils.billMonthWidth(act);
        for (int i = 0; i < 12; i++) {
            BillDtlMonthItemVM monthItemModel = new BillDtlMonthItemVM(i + 1, monthWidth, this);
            monthItemModel.setMonthColor(ContextCompat.getColor(AlaConfig.getContext(), R.color.color_646464));
            months.add(monthItemModel);
        }
    }

    public void yearClick(View view) {
        showPopupWindow(view);
    }

    private void showPopupWindow(View view) {
        if (null != popupWindow) {
            if (!popupWindow.isShowing()) popupWindow.showAsDropDown(view, xoff, -yoff);
        } else {
            View popupView = LayoutInflater.from(act).inflate(R.layout.popup_bill_year, null);
            TextView year1 = popupView.findViewById(R.id.year1);
            year1.setOnClickListener(this);
            TextView year2 = popupView.findViewById(R.id.year2);
            year2.setOnClickListener(this);
            popupWindow = new PopupWindow(popupView, DensityUtils.getPxByDip(80), DensityUtils.getPxByDip(154));
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.transparent));
//            int[] location = new int[2];
//            view.getLocationOnScreen(location);
            xoff = DensityUtils.getPxByDip(10); // X偏移量
            yoff = DensityUtils.getPxByDip(10); // X偏移量
            //在控件的下方弹出窗口
            popupWindow.showAsDropDown(view, xoff, -yoff);
            //上边
            //popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0] - view.getWidth(), location[1]);
            //左边
            //popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0] - view.getWidth(), location[1]);
            //右边
            //popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0] + view.getWidth(), location[1]);
        }
    }

    @Override
    public void onMonthClickListener(BillDtlMonthItemVM selectItemVM, String billMonth) {
        initializeMonth();
        initializeRecyclerView();
        selectItemVM.setMonthColor(ContextCompat.getColor(AlaConfig.getContext(), R.color.color_ff7722));
        this.selectMonth = billMonth;
        requestMonthData(billYear.get(), billMonth);
    }

    private void initializeMonth() {
        for (BillDtlMonthItemVM itemVM : months) {
            itemVM.setMonthColor(ContextCompat.getColor(AlaConfig.getContext(), R.color.color_646464));
        }
    }

    private void initializeRecyclerView() {
        items.clear();
        hasNotIn = false;
        binding.refreshLayout.setRefreshFooter(new FalsifyFooter(act));
    }

    private void requestMonthData(String billYear, String billMonth) {
        JSONObject object = new JSONObject();
        object.put("billMonth", billMonth);
        object.put("billYear", billYear);
        Call<BillMonthModel> call = RDClient.getService(BusinessApi.class).getBillListByMonthAndYear(object);
        call.enqueue(new RequestCallBack<BillMonthModel>() {
            @Override
            public void onSuccess(Call<BillMonthModel> call, Response<BillMonthModel> response) {
                monthBuild(response.body());
            }
        });
    }

    private void monthBuild(BillMonthModel model) {
        String status = model.getStatus();
        switch (status) {
            case "out": // 账单已出未逾期
                imgVis.set(false);
                qusVis.set(false);
                widgetVisibility(View.VISIBLE);
                binding.txtDuration.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                binding.txtDuration.setTextColor(ContextCompat.getColor(AlaConfig.getContext(), R.color.color_666666));
                dateSpan.set(model.getStr() + "~" + model.getEnd());
                billAmount.set(AppUtils.formatPriceDot(model.getMoney().floatValue()));
                deadline.set("最后还款日" + model.getLastPayDay());
                monthBtn.set("立即还款");
                List<BillMonthModel.BillListBean> out = model.getBillList();
                items.add(new BillDtlItemVM(status, model,act));
                for (BillMonthModel.BillListBean bean : out) {
                    items.add(new BillDtlItemVM(bean,act));
                }
                break;
            case "overdue": // 账单逾期
                imgVis.set(false);
                qusVis.set(false);
                widgetVisibility(View.VISIBLE);
                binding.txtDuration.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                binding.txtDuration.setTextColor(ContextCompat.getColor(AlaConfig.getContext(), R.color.color_666666));
                dateSpan.set(model.getStr() + "~" + model.getEnd());
                billAmount.set(AppUtils.formatPriceDot(model.getMoney().floatValue()));
                deadline.set("包含逾期利息" + model.getOverdeuInterest().toString() + "元");
                monthBtn.set("立即还款");
                List<BillMonthModel.BillListBean> overdue = model.getBillList();
                items.add(new BillDtlItemVM(status, model,act));
                for (BillMonthModel.BillListBean bean : overdue) {
                    items.add(new BillDtlItemVM(bean,act));
                }
                break;
            case "finsh": // 所有账单已结清
                imgVis.set(true);
                widgetVisibility(View.INVISIBLE);
                qusVis.set(false);
                imgSrc.set(ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.pay_off));
                imgText.set("赞, 全部还清");
                requestADdata();
                break;
            case "noBill": // 没有账单
                imgVis.set(true);
                widgetVisibility(View.INVISIBLE);
                qusVis.set(false);
                imgSrc.set(ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.no_bills));
                imgText.set("无账单记录");
                requestADdata();
                break;
            case "notOut":   // 未出账
                imgVis.set(false);
                widgetVisibility(View.VISIBLE);
                qusVis.set(true);
                binding.txtDuration.setText("剩余待还(元)");
                binding.txtDuration.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                binding.txtDuration.setTextColor(ContextCompat.getColor(AlaConfig.getContext(), R.color.color_646464));
                billAmount.set(AppUtils.formatPriceDot(model.getMoney().floatValue()));
                deadline.set("最后还款日" + model.getLastPayDay());
                deadline.set("账单未出，将于" + model.getOutDay() + "出账");
                monthBtn.set("提前结清");
                List<BillMonthModel.BillListBean> notOut = model.getBillList();
                items.add(new BillDtlItemVM(status, model,act));
                for (BillMonthModel.BillListBean bean : notOut) {
                    items.add(new BillDtlItemVM(bean,act));
                }
                billMonthModel = model;
                List<BillMonthModel.BorrowBean> borrowBeen = model.getBorrowList();
                if (MiscUtils.isNotEmpty(borrowBeen)) {
                    hasNotIn = true;
                    binding.refreshLayout.setRefreshFooter(new ClassicsFooter(act));
                    items.add(noInBillTip = new BillDtlItemVM("noInBill_Tip", model,act));
                }
                break;
            case "noInBill": // 未入账
                imgVis.set(true);
                widgetVisibility(View.INVISIBLE);
                qusVis.set(false);
                imgSrc.set(ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.no_bills));
                imgText.set("无账单记录");

                List<BillMonthModel.BorrowBean> noInBill = model.getBorrowList();
                items.add(new BillDtlItemVM(status, model,act));
                for (BillMonthModel.BorrowBean borrowBean : noInBill) {
                    items.add(new BillDtlItemVM(borrowBean, act));
                }
                requestADdata();
                break;
        }
    }

    public void monthBtnClick(View view) {
        Intent intent = new Intent();
        intent.putExtra(BundleKeys.STAGE_JUMP, act.getIntent().getStringExtra(BundleKeys.STAGE_JUMP));
        if (monthBtn.get().equals("立即还款"))
            ActivityUtils.push(RepaymentActivity.class, intent);
        else if (monthBtn.get().equals("提前结清")) {
            ActivityUtils.push(SettleAdvancedActivity.class, intent);
        }

    }

    public void questionClick(View view) {
        String url = AlaConfig.getServerProvider().getAppServer() + Constant.H5_URL_AHEAD_RETURN;
        Intent intent = new Intent();
        intent.putExtra(HTML5WebView.INTENT_BASE_URL, url);
        intent.putExtra(HTML5WebView.INTENT_TITLE, view.getContext().getResources().getString(R.string.settle_advanced_title_what));
        ActivityUtils.push(HTML5WebView.class, intent);
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        if (hasNotIn) {
            if (null == billMonthModel) return;
            if (null != noInBillTip)
                noInBillTip.setHeadTxt("未确认订单共" + billMonthModel.getNotInCount() + "笔，确认后计入账单，共" + billMonthModel.getNotInMoney() + "元");
            List<BillMonthModel.BorrowBean> borrowBeen = billMonthModel.getBorrowList();
            for (BillMonthModel.BorrowBean bean : borrowBeen) {
                items.add(new BillDtlItemVM(bean, act));
            }
            refreshlayout.finishLoadmore(200);
            hasNotIn = false;
            binding.refreshLayout.setRefreshFooter(new FalsifyFooter(act));
        } else {
            refreshlayout.finishLoadmore(10);
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        refreshlayout.finishRefresh(10);
    }

    @Override
    public void onClick(View v) {
        initializeRecyclerView();
        int i = v.getId();
        if (i == R.id.year1) {
            billYear.set("2018");
            requestMonthData("2018", selectMonth);

        } else if (i == R.id.year2) {
            billYear.set("2017");
            requestMonthData("2017", selectMonth);

        }
        dismissPopupWindow();
    }

    private void dismissPopupWindow() {
        if (null != popupWindow && popupWindow.isShowing()) popupWindow.dismiss();
    }

    private void widgetVisibility(int visibility) {
        binding.txtDuration.setVisibility(visibility);
        binding.txtTotal.setVisibility(visibility);
        binding.txtDeadline.setVisibility(visibility);
        binding.btnRepay.setVisibility(visibility);
    }

    private void requestADdata() {
        items.add(new BillDtlItemVM(act));
    }

}
