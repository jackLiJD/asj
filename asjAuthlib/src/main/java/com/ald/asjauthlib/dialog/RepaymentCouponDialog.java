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

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.cashier.model.MyTicketModel;
import com.ald.asjauthlib.cashier.utils.RepayCouponAdapter;
import com.ald.asjauthlib.authframework.core.utils.DataUtils;
import com.ald.asjauthlib.authframework.core.utils.DensityUtils;

import java.util.List;

/**
 * Created by luckyliang on 2017/12/15.
 * <p>
 * 还款选择优惠券弹窗
 */

public class RepaymentCouponDialog extends Dialog {

    private final static int DIALOG_MAX_DP_HEIGHT = 438;


    public RepaymentCouponDialog(Context context, int themeResId) {
        super(context, themeResId);
    }


    public static class Builder {

        private Context context;
        private RecyclerView recyclerView;
        private RepayCouponAdapter adapter;
        private OnSureClickListener onSureClickListener;
        private RepaymentCouponDialog dialog;

        public RepaymentCouponDialog create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (dialog == null) {
                dialog = new RepaymentCouponDialog(context, R.style.BottomSelectDialog);
            }
            View layout = inflater.inflate(R.layout.dialog_bottom_coupon, null);
            recyclerView = layout.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(adapter);
            Window window = dialog.getWindow();
            if (window != null) {
                window.setWindowAnimations(R.style.Animation_BottomSelect);
                dialog.setContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        DensityUtils.getPxByDip(DIALOG_MAX_DP_HEIGHT)));
                dialog.setCancelable(true);
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.gravity = Gravity.BOTTOM;
                lp.width = DataUtils.getCurrentDisplayMetrics().widthPixels;
                lp.dimAmount = 0.5f;
            }
            return dialog;
        }

        public Builder setData(List<MyTicketModel> ticketModels, final int ticketPositionChecked) {

            adapter = new RepayCouponAdapter(context, ticketModels, new RepayCouponAdapter.onCouponClickListener() {
                @Override
                public void onClick(int position) {
                    onSureClickListener.onClick(position);
                    dialog.dismiss();
                }
            }, ticketPositionChecked);
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
            void onClick(int position);

        }

    }


}
