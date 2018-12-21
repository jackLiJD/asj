package com.ald.asjauthlib.auth;

import com.ald.asjauthlib.auth.model.BorrowHomeModel;
import com.ald.asjauthlib.auth.model.CreditPromoteModel;
import com.ald.asjauthlib.cashier.model.BillMonthModel;
import com.ald.asjauthlib.cashier.model.BillRefundModel;
import com.ald.asjauthlib.cashier.model.BillsModel;
import com.ald.asjauthlib.cashier.model.CalendarModel;
import com.ald.asjauthlib.cashier.model.ConsumeDtlModel;
import com.ald.asjauthlib.cashier.model.HistoryBillListModel;
import com.ald.asjauthlib.cashier.model.HistoryBillMonthDtlModel;
import com.ald.asjauthlib.cashier.model.LimitDetailModel;
import com.ald.asjauthlib.cashier.model.MyRepaymentDtlModel;
import com.ald.asjauthlib.cashier.model.MyRepaymentModel;
import com.ald.asjauthlib.cashier.model.RefundDtlModel;
import com.ald.asjauthlib.cashier.model.RefundModel;
import com.ald.asjauthlib.cashier.model.SettleAdvancedModel;
import com.ald.asjauthlib.cashier.model.WxOrAlaPayModel;
import com.alibaba.fastjson.JSONObject;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/1/10
 * 描述：
 * 修订历史：
 */
public interface BusinessApi {


    /**
     * 分期账单还款页面(新)
     */
    @POST("/repay/getRepaymentConfirmInfoV1")
    Call<BillRefundModel> getRepaymentConfirmInfoV1(@Body com.alibaba.fastjson.JSONObject request);



    /**
     * 获取提升信用页面
     */
    @POST("/borrow/getCreditPromoteInfoV1")
    Call<CreditPromoteModel> getCreditPromoteInfoV1(@Body com.alibaba.fastjson.JSONObject request);


    /**
     * 分期还款支付(余额，银行卡，微信支付)
     */
    @POST("/borrow/submitRepaymentByYiBao")
    Call<WxOrAlaPayModel> submitRepaymentByYiBao(@Body JSONObject jsonObject);


    /**
     * 提交提前结清(4.0.4)
     */
    @POST("/borrow/submitClear")
    Call<WxOrAlaPayModel> submitClear(@Body JSONObject jsonObject);
    /**
     * 额度明细页面
     */
    @POST("/bill/getLimitDetailInfo")
    Call<LimitDetailModel> getLimitDetailInfo(@Body com.alibaba.fastjson.JSONObject request);

    /**
     * 还款页面(4.0.4)
     */
    @POST("/borrow/getMyRepaymentV1")
    Call<MyRepaymentModel> getMyRepaymentV1();

    /**
     * 还款详情弹窗(4.0.4)
     */
    @POST("/borrow/getMyRepaymentDetailV1")
    Call<MyRepaymentDtlModel> getMyRepaymentDetailV1(@Body JSONObject jsonObject);

    @POST("/borrow/getMyBorrowListV1")
    Call<BillsModel> getMyBorrowListV1(@Body JSONObject jsonObject);

    /**
     * 提前结清查询(4.0.4)
     */
    @POST("/borrow/getDataView")
    Call<SettleAdvancedModel> getDataView(@Body JSONObject jsonObject);

    /**
     * 消费详情(4.0.4)
     */
    @POST("/borrow/getBorrowDetailV1")
    Call<ConsumeDtlModel> getBorrowDetailV1(@Body JSONObject jsonObject);


    /**
     * 历史账单列表(4.0.4)
     */
    @POST("/borrow/getMyHistoryBorrowV1")
    Call<HistoryBillListModel> getMyHistoryBorrowV1(@Body JSONObject jsonObject);

    /**
     * 历史账单详情（4.0.4）
     */
    @POST("/borrow/getMyHistoryBorrowDetailV1")
    Call<HistoryBillMonthDtlModel> getMyHistoryBorrowDetailV1(@Body JSONObject jsonObject);


    /**
     * 退还款记录页面(账单二期)
     */
    @POST("/borrow/getMyRepaymentHistoryV1")
    Call<RefundModel> getMyRepaymentHistoryV1(@Body JSONObject jsonObject);

    /**
     * 退还款详情(4.0.4)
     */
    @POST("/borrow/getRepaymentDetailV1")
    Call<RefundDtlModel> getRepaymentDetailV1(@Body JSONObject jsonObject);

    /**
     * 根据用户选择年份返回月份列表(账单二期)
     */
    @POST("/borrow/getRepaymentCalendarV1")
    Call<CalendarModel> getRepaymentCalendarV1(@Body JSONObject jsonObject);

    @POST("/borrow/getBillListByMonthAndYear")
    Call<BillMonthModel> getBillListByMonthAndYear(@Body JSONObject jsonObject);

    /**
     * 信用中心主页页面
     */
    @POST("/borrow/getMyBorrowV1")
    Observable<BorrowHomeModel> getMyBorrowV1();
}



