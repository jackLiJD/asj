package com.ald.asjauthlib.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.cashier.model.CashierNperListModel;
import com.ald.asjauthlib.utils.AppUtils;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.utils.DataUtils;
import com.ald.asjauthlib.authframework.core.utils.DensityUtils;

import java.util.List;



/*
 * Created by liangchen on 2018/9/4.
 */

public class CashierPerDtlDialog extends Dialog {

    private final static int DIALOG_MAX_DP_HEIGHT = 500;

    CashierPerDtlDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder {
        Context context;
        CashierNperListModel.NperModel nperModel;

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder setNperListModel(CashierNperListModel.NperModel nperModel) {
            this.nperModel = nperModel;
            return this;
        }

        public CashierPerDtlDialog create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final CashierPerDtlDialog dialog = new CashierPerDtlDialog(context, R.style.BottomSelectDialog);
            View layout = inflater.inflate(R.layout.layout_bottom_cashier_nper_detail, null);
            TextView txtAmount = layout.findViewById(R.id.txt_amount);
            TextView txtFee = layout.findViewById(R.id.txt_fee);

            SpannableString sp = new SpannableString(String.format(AlaConfig.getResources().getString(R.string.format_rmb), AppUtils.formatAmount(nperModel.getTotalAmount())));
            sp.setSpan(new RelativeSizeSpan(0.8f), 0, 1, 0);
            txtAmount.setText(sp);
            txtFee.setText(String.format(AlaConfig.getResources().getString(R.string.format_nper_detail_fee), AppUtils.formatAmount(nperModel.getPoundageAmount())));

            ImageView ivClose = layout.findViewById(R.id.iv_close);
            ivClose.setOnClickListener(v -> dialog.dismiss());

            RecyclerView recyclerView = layout.findViewById(R.id.list_view_line);
            final CashierNperAdapter adapter = new CashierNperAdapter(nperModel.getNperDetailList(), context);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(adapter);
            Window window = dialog.getWindow();
            if (window != null) {
                window.setWindowAnimations(R.style.Animation_BottomSelect);
                dialog.setContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        DensityUtils.getPxByDip(DIALOG_MAX_DP_HEIGHT)));
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.gravity = Gravity.BOTTOM;
                lp.width = DataUtils.getCurrentDisplayMetrics().widthPixels;
                lp.dimAmount = 0.5f;
            }
            return dialog;
        }
    }

    static class CashierNperAdapter extends RecyclerView.Adapter<CashierNperAdapter.CashiernPerVH> {

        List<CashierNperListModel.NperDetailModel> cashierNperListModel;
        Context context;

        CashierNperAdapter(List<CashierNperListModel.NperDetailModel> cashierNperListModel, Context context) {
            this.cashierNperListModel = cashierNperListModel;
            this.context = context;
        }

        @Override
        public CashiernPerVH onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_cashier_nper_detail_list, parent, false);
            return new CashiernPerVH(view);
        }

        @Override
        public void onBindViewHolder(CashiernPerVH holder, int position) {
            CashierNperListModel.NperDetailModel nperDetailModel = cashierNperListModel.get(position);
            holder.txtNper.setText(String.format(AlaConfig.getResources().getString(R.string.format_nper_detail_per_num), Integer.toString(nperDetailModel.getNperNum())));
            holder.txtNperAmount.setText(String.format(AlaConfig.getResources().getString(R.string.format_yuan), AppUtils.formatAmount(nperDetailModel.getMonthAmount())));
            holder.txtFee.setText(String.format(AlaConfig.getResources().getString(R.string.format_yuan), AppUtils.formatAmount(nperDetailModel.getPoundageAmount2())));
            holder.txtAmount.setText(String.format(AlaConfig.getResources().getString(R.string.format_yuan), AppUtils.formatAmount(nperDetailModel.getAmount())));
            holder.txtFistHint.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
        }

        @Override
        public int getItemCount() {
            return cashierNperListModel.size();
        }

        static class CashiernPerVH extends RecyclerView.ViewHolder {

            TextView txtNper;
            TextView txtNperAmount;
            TextView txtAmount;
            TextView txtFee;
            TextView txtFistHint;//第一期提示

            CashiernPerVH(View itemView) {
                super(itemView);
                txtNper = itemView.findViewById(R.id.txt_nper);
                txtNperAmount = itemView.findViewById(R.id.txt_nper_amount);
                txtAmount = itemView.findViewById(R.id.txt_amount);
                txtFee = itemView.findViewById(R.id.txt_fee);
                txtFistHint = itemView.findViewById(R.id.txt_first_hint);
            }
        }

    }
}
