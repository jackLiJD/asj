package com.ald.asjauthlib.cashier.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;

import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.BusinessApi;
import com.ald.asjauthlib.cashier.model.CalendarModel;
import com.ald.asjauthlib.cashier.utils.MonthRecyclerViewAdapter;
import com.ald.asjauthlib.cashier.utils.YearRecyclerViewAdapter;
import com.ald.asjauthlib.databinding.ActivityCalenderBinding;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.network.RequestCallBack;
import com.ald.asjauthlib.authframework.core.vm.BaseVM;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by luckyliang on 2017/12/3.
 */

public class CalenderVM extends BaseVM implements MonthRecyclerViewAdapter.MonthOnClickListener {

    private Activity context;
    private int mSelectedMonth;
    private int mSelectedYear = 0;
    private List<Integer> years;
    YearRecyclerViewAdapter yearRecyclerViewAdapter;
    private ActivityCalenderBinding mBinding;

    public CalenderVM(Activity context, ActivityCalenderBinding binding) {
        this.context = context;
        mBinding = binding;
        years = new ArrayList<>();

        for (int i = 0; i < AlaConfig.getResources().getStringArray(R.array.calenderTabs).length; i++) {
            int str = AlaConfig.getResources().getIntArray(R.array.calenderTabs)[i];
            years.add(str);
        }
        mSelectedYear = Calendar.getInstance().get(Calendar.YEAR);
        yearRecyclerViewAdapter = new YearRecyclerViewAdapter(context, years, mSelectedYear - 2017, new YearRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                getMonthList(years.get(position));
                mSelectedYear = position;
            }
        });
        mBinding.rvYear.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        mBinding.rvYear.setAdapter(yearRecyclerViewAdapter);

        mBinding.rvMonth.setLayoutManager(new GridLayoutManager(context, 4));

        getMonthList(mSelectedYear == 0 ? 2017 : mSelectedYear);
    }


    @Override
    public void onMonthClickListener(int currentMonth) {
        mSelectedMonth = currentMonth + 1;
    }


    public void setResult() {
        Intent intent = new Intent();
        intent.putExtra(BundleKeys.INTENT_KEY_REFUND_YEAR, mSelectedYear);
        intent.putExtra(BundleKeys.INTENT_KEY_REFUND_MONTH, mSelectedMonth);
        context.setResult(Activity.RESULT_OK, intent);
    }

    public void getMonthList(int year) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("year", year);
        jsonObject.put("status", context.getIntent().getStringExtra(BundleKeys.STAGE_JUMP));
        Call<CalendarModel> call = RDClient.getService(BusinessApi.class).getRepaymentCalendarV1(jsonObject);
        call.enqueue(new RequestCallBack<CalendarModel>() {
            @Override
            public void onSuccess(Call<CalendarModel> call, Response<CalendarModel> response) {
                CalendarModel calendarModel = response.body();
                mBinding.rvMonth.setAdapter(new MonthRecyclerViewAdapter(context, new MonthRecyclerViewAdapter.MonthOnClickListener() {
                    @Override
                    public void onMonthClickListener(int leftPosition) {
                        mSelectedMonth = leftPosition + 1;
                    }
                }, calendarModel));

            }
        });
    }
}
