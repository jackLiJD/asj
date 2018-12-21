package com.ald.asjauthlib.cashier.ui;

import android.support.v4.app.Fragment;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.cashier.viewmodel.MyBrandVoucherChildVM;
import com.ald.asjauthlib.databinding.FragmentMyTicketsBrandListBinding;
import com.ald.asjauthlib.authframework.core.config.AlaBaseFragment;


/**
 * 版权：XXX公司 版权所有
 * 作者：TonyChen
 * 版本：1.0
 * 创建日期：2017/3/4
 * 描述：
 * 修订历史：
 */
public class MyBrandVoucherFragment extends AlaBaseFragment<FragmentMyTicketsBrandListBinding> {

//    private PtrFrameLayout ptrFrameLayout;
    private int ticketType;
    private MyBrandVoucherChildVM vm;

    @Override
    public String getStatName() {
        return "优惠券";
    }

    /**
     * type
     * 0-未使用
     * 1-已使用
     * 2-已过期
     */
    public Fragment create(int ticketType) {
        this.ticketType = ticketType;
        return this;
    }


    @Override
    public int getLayoutInflate() {
        return R.layout.fragment_my_tickets_brand_list;
    }


    @Override
    protected void initViews() {
        vm = new MyBrandVoucherChildVM(this, ticketType, cvb);
        cvb.setViewModel(vm);
        //PtrFrameLayout页面UI初始化配置
//        ptrFrameLayout = (PtrFrameLayout) cvb.getRoot().findViewById(R.id.ptr_frame);
        // header
//        final CommonRefreshHeader header = new CommonRefreshHeader(getContext());
//        ptrFrameLayout.setHeaderView(header);
//        ptrFrameLayout.addPtrUIHandler(header);
//        ptrFrameLayout.setPtrHandler(new PtrHandler() {
//            @Override
//            public void onRefreshBegin(PtrFrameLayout frame) {
//                frame.refreshComplete();
//            }
//
//            @Override
//            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
//                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
//            }
//        });
        // the following are default settings
//        ptrFrameLayout.setResistance(1.7f);
//        ptrFrameLayout.setRatioOfHeaderHeightToRefresh(1.2f);
//        ptrFrameLayout.setDurationToClose(200);
//        ptrFrameLayout.setDurationToCloseHeader(1000);
        // default is false
//        ptrFrameLayout.setPullToRefresh(false);
        // default is true
//        ptrFrameLayout.setKeepHeaderWhenRefresh(true);
//        ptrFrameLayout.disableWhenHorizontalMove(true);
//        ptrFrameLayout.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                ptrFrameLayout.autoRefresh();
//            }
//        }, 100);
    }

    @Override
    protected void initData() {
        vm.reLoad();
    }


}
