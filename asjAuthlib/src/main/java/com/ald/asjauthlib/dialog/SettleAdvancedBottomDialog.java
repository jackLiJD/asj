package com.ald.asjauthlib.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.cashier.model.SettleAdvancedModel;
import com.ald.asjauthlib.cashier.utils.SettleBottomAdapter;
import com.ald.asjauthlib.authframework.core.utils.DataUtils;
import com.ald.asjauthlib.authframework.core.utils.DensityUtils;

import java.util.List;


/**
 * Created by luckyliang on 2017/12/17.
 */

public class SettleAdvancedBottomDialog extends Dialog {

    private final static int DIALOG_MAX_DP_HEIGHT = 438;

    public SettleAdvancedBottomDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder {

        Context context;
        RecyclerView recyclerView;
        SettleBottomAdapter adapter;
        TextView btnSubmit;
        int mTickPositionChecked = -1;
        TextView txtAmount;
        String strAmount;
        String strCharge;
        TextView txtCharge;
        OnSureClickListener onSureClickListener;


        public SettleAdvancedBottomDialog create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final SettleAdvancedBottomDialog dialog = new SettleAdvancedBottomDialog(context, R.style.BottomSelectDialog);
            View layout = inflater.inflate(R.layout.dialog_bottom_settle_advanced, null);
            recyclerView = layout.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(adapter);
            txtAmount = layout.findViewById(R.id.txt_real_pay);
            txtAmount.setText(strAmount);

            txtCharge = layout.findViewById(R.id.txt_charge);
            txtCharge.setText(strCharge);

            btnSubmit = layout.findViewById(R.id.btn_submit);
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //立即选中展开的item
                    onSureClickListener.onClick();
                    dialog.dismiss();
                }
            });


            Window window = dialog.getWindow();
            window.setWindowAnimations(R.style.Animation_BottomSelect);
            dialog.setContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    DensityUtils.getPxByDip(DIALOG_MAX_DP_HEIGHT)));
            dialog.setCancelable(true);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.gravity = Gravity.BOTTOM;
            lp.width = DataUtils.getCurrentDisplayMetrics().widthPixels;
            lp.dimAmount = 0.5f;
            return dialog;
        }

        public Builder setData(List<SettleAdvancedModel.Detail> details, final int ticketPositionChecked) {
            adapter = new SettleBottomAdapter(context, details);
            return this;

        }

        public Builder setAmount(String amount) {
            strAmount = amount;
            return this;
        }

        public Builder setCharge(String charge) {
            strCharge = charge;
            return this;
        }

        public Builder setSureClickListener(OnSureClickListener onSureClickListener) {
            this.onSureClickListener = onSureClickListener;
            return this;
        }


        public Builder(Context context) {
            this.context = context;
        }

        public interface OnSureClickListener {
            void onClick();
        }

    }
}
