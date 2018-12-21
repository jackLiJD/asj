package com.ald.asjauthlib.cashier.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.cashier.model.MyTicketModel;
import com.ald.asjauthlib.cashier.viewmodel.CouponListItemVM;
import com.ald.asjauthlib.utils.AppUtils;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.DensityUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/*
 * Created by luckyliang on 2017/12/16.
 */

public class RepayCouponAdapter extends RecyclerView.Adapter<RepayCouponAdapter.ViewHolder> {
    private Context context;
    private List<MyTicketModel> myTicketModels;
    private onCouponClickListener listener;
    private int checkedPosition = -1;


    public RepayCouponAdapter(Context context, List<MyTicketModel> myTicketModels, onCouponClickListener listener, int checkedPosition) {
        this.context = context;
        this.myTicketModels = myTicketModels;
        this.listener = listener;
        this.checkedPosition = checkedPosition;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(context).inflate(R.layout.item_dialog_bottom_repayment_ticket, parent, false);
        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        MyTicketModel myTicketModel = myTicketModels.get(position);
        setPrice(holder.txtAmount, myTicketModel);
        setType(holder.txtType, myTicketModel);
        holder.txtName.setText(myTicketModel.getName());
        setLimit(holder.txtLimit, myTicketModel);
        setDate(holder.txtDate, myTicketModel);
        setStatemnt(holder.txtStatement, myTicketModel);
        holder.txtStatement.setVisibility(View.GONE);
        holder.checked.setOnCheckedChangeListener((buttonView, isChecked) -> listener.onClick(position));

        holder.coupon_price_parent.setOnClickListener(v -> {
            holder.coupon_date_img.setRotation(holder.txtStatement.getVisibility() == View.GONE ? 0 : 180);
            holder.txtStatement.setVisibility(holder.txtStatement.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        });
        holder.coupon_info_parent.setOnClickListener(v -> {
            holder.coupon_date_img.setRotation(holder.txtStatement.getVisibility() == View.GONE ? 0 : 180);
            holder.txtStatement.setVisibility(holder.txtStatement.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        });
    }

    @Override
    public int getItemCount() {
        return myTicketModels == null ? 0 : myTicketModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtAmount;//优惠券金额
        TextView txtType;//优惠券类型
        TextView txtLimit;//使用规则
        TextView txtName;//优惠券名称
        TextView txtDate;//期限
        TextView txtStatement;
        CheckBox checked;
        LinearLayout coupon_price_parent;
        LinearLayout coupon_info_parent;
        ImageView coupon_date_img;

        public ViewHolder(View itemView) {
            super(itemView);
            txtAmount = itemView.findViewById(R.id.coupon_price);
            txtType = itemView.findViewById(R.id.coupon_type);
            txtLimit = itemView.findViewById(R.id.coupon_limit);
            txtName = itemView.findViewById(R.id.coupon_name);
            txtDate = itemView.findViewById(R.id.coupon_date);
            txtStatement = itemView.findViewById(R.id.coupon_detail);
            checked = itemView.findViewById(R.id.coupon_select);
            coupon_price_parent = itemView.findViewById(R.id.coupon_price_parent);
            coupon_info_parent = itemView.findViewById(R.id.coupon_info_parent);
            coupon_date_img = itemView.findViewById(R.id.coupon_date_img);
        }
    }

    public interface onCouponClickListener {
        void onClick(int position);
    }

    //金额
    private void setPrice(TextView textView, MyTicketModel myTicketModel) {
        Spannable span;
        if (CouponListItemVM.CouponType.DISCOUNT.equals(myTicketModel.getType())) {
            String content = AppUtils.formatCouponAmount(new BigDecimal(myTicketModel.getDiscount().doubleValue() * 10)) + "折";
            span = new SpannableString(content);
            // 设置字体大小
            span.setSpan(new AbsoluteSizeSpan(DensityUtils.sp2px(ActivityUtils.peek(), 30, true)), 0, content.indexOf("折"),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            span = new SpannableString("￥" + AppUtils.formatCouponAmount(myTicketModel.getAmount()));
            span.setSpan(new AbsoluteSizeSpan(DensityUtils.sp2px(ActivityUtils.peek(), 30, true)), 1, span.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        textView.setText(span);
    }

    //类型
    private void setType(TextView textView, MyTicketModel myTicketModel) {
        String typeName;
        if ("CASH".equals(myTicketModel.getType())) {
            typeName = AlaConfig.getResources().getString(R.string.my_ticket_type_cash);
        } else if ("MOBILE".equals(myTicketModel.getType())) {
            typeName = AlaConfig.getResources().getString(R.string.my_ticket_type_mobile);
        } else if ("ACTIVITY".equals(myTicketModel.getType())) {
            typeName = AlaConfig.getResources().getString(R.string.my_ticket_type_activity);
        } else if ("REPAYMENT".equals(myTicketModel.getType())) {
            typeName = AlaConfig.getResources().getString(R.string.my_ticket_type_repayment);
        } else if ("2".equals(myTicketModel.getType()) || "DISCOUNT".equals(myTicketModel.getType())) {
            typeName = AlaConfig.getResources().getString(R.string.my_ticket_type_full_discount);
        } else {
            typeName = AlaConfig.getResources().getString(R.string.my_ticket_type_full);
        }
        textView.setText(typeName);
    }

    //使用限制
    private void setLimit(TextView textView, MyTicketModel myTicketModel) {
        String useLimit;
        if (CouponListItemVM.CouponType.DISCOUNT.equals(myTicketModel.getType())) {
            StringBuilder limit = new StringBuilder();
            if (new BigDecimal(0).compareTo(myTicketModel.getLimitAmount()) == 0) {
                limit.append(AlaConfig.getResources().getString(R.string.my_ticket_unlimit_format));
            } else {
                limit.append(String.format(AlaConfig.getResources().getString(R.string.my_ticket_limit_format), AppUtils.formatCouponAmount(myTicketModel.getLimitAmount())));
            }
//            if (new BigDecimal(0).compareTo(myTicketModel.getAmount()) != 0) {
//                limit.append(String.format(AlaConfig.getResources().getString(R.string.my_ticket_limit_max), AppUtils.formatCouponAmount(myTicketModel.getAmount())));
//            }
            useLimit = limit.toString();
        } else {
            if (new BigDecimal(0).compareTo(myTicketModel.getLimitAmount()) == 0) {
                useLimit = AlaConfig.getResources().getString(R.string.my_ticket_unlimit_format);
            } else {
                useLimit = String.format(AlaConfig.getResources().getString(R.string.my_ticket_limit_format), AppUtils.formatCouponAmount(myTicketModel.getLimitAmount()));
            }
        }
        textView.setText(useLimit);
    }

    //有效期
    private void setDate(TextView textView, MyTicketModel myTicketModel) {
//        if ("Y".equals(myTicketModel.getWillExpireStatus())) {
//            textView.setText(AppUtils.returnDay(myTicketModel.getGmtEnd(), new Date().getTime()));
//        } else {
//            textView.setText(AppUtils.coverTimeYYMMDD(myTicketModel.getGmtEnd()) + "到期");
//        }
        textView.setText(AppUtils.returnDay(myTicketModel.getGmtEnd(), new Date().getTime()));
    }

    //使用说明
    private void setStatemnt(TextView textView, MyTicketModel myTicketModel) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format(AlaConfig.getResources().getString(R.string.repay_coupon_use_expiod), AppUtils.coverTimeYMD(myTicketModel.getGmtStart()),
                AppUtils.coverTimeYMD(myTicketModel.getGmtEnd())));
        if (!TextUtils.isEmpty(myTicketModel.getUseRange())) {
            stringBuilder.append("\n");
            stringBuilder.append(String.format(AlaConfig.getResources().getString(R.string.repay_coupon_use_statement), myTicketModel.getUseRange()));
        }
        textView.setText(stringBuilder);
    }
}
