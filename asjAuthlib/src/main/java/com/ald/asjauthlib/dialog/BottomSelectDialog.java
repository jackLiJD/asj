package com.ald.asjauthlib.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.model.BankCardModel;
import com.ald.asjauthlib.auth.ui.BankCardAddIdActivity;
import com.ald.asjauthlib.cashier.model.RefundResponse;
import com.ald.asjauthlib.utils.ModelEnum;
import com.ald.asjauthlib.utils.StageJumpEnum;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.DataUtils;
import com.ald.asjauthlib.authframework.core.utils.DensityUtils;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.authframework.core.utils.UIUtils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import static com.ald.asjauthlib.utils.BundleKeys.REQUEST_CODE_BRAND_DIALOG_BANK_CARD;


/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/3/2 11:13
 * 描述：还款收银台
 * 修订历史：
 */
public class BottomSelectDialog extends Dialog {
    private final static int DIALOG_MAX_DP_HEIGHT = 438;

    protected BottomSelectDialog(Context context, int theme) {
        super(context, theme);
    }

    public interface OnSelectedListener {
        void onItemSelected(int position, BankCardModel selectItem);
    }

    public static class Builder {
        private Context context;
        private String title;
        private String payAmount;//支付金额
        private List<BankCardModel> data;
        private OnSelectedListener onSelectedListener;
        private List<RefundResponse.RefundType> refundTypeList;
        private String realName;
        private String payFailedTip;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public String getPayAmount() {
            return payAmount;
        }

        public Builder setPayAmount(String payAmount) {
            this.payAmount = payAmount;
            return this;
        }

        public Builder setData(List<BankCardModel> data) {
            this.data = data;
            return this;
        }

        public Builder setRefundType(List<RefundResponse.RefundType> refundType) {
            this.refundTypeList = refundType;
            return this;
        }

        public Builder setRealName(String realName) {
            this.realName = realName;
            return this;
        }

        public Builder setOnSelectedListener(OnSelectedListener onSelectedListener) {
            this.onSelectedListener = onSelectedListener;
            return this;
        }

        public Builder setPayFailedTip(String payFailedTip) {
            this.payFailedTip = payFailedTip;
            return this;
        }

        public BottomSelectDialog create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final BottomSelectDialog dialog = new BottomSelectDialog(context, R.style.BottomSelectDialog);
            View layout = inflater.inflate(R.layout.layout_bottom_select_dialog, null);
            TextView titleView = layout.findViewById(R.id.tv_title);
            if (MiscUtils.isNotEmpty(title)) {
                titleView.setText(title);
            }
            TextView tvPayAmount = layout.findViewById(R.id.tv_amount);
            if (MiscUtils.isNotEmpty(payAmount)) {
                tvPayAmount.setText(payAmount);
                tvPayAmount.setVisibility(View.VISIBLE);
            } else {
                tvPayAmount.setVisibility(View.GONE);
            }
            TextView tvTipFailed = layout.findViewById(R.id.tv_tip_failed);
            if (MiscUtils.isNotEmpty(payFailedTip)) {
                tvTipFailed.setText(payFailedTip);
                tvTipFailed.setVisibility(View.VISIBLE);
            } else {
                tvTipFailed.setText("");
                tvTipFailed.setVisibility(View.GONE);
            }
            refundWayInit();
            layout.findViewById(R.id.iv_back).setOnClickListener(v -> dialog.dismiss());

            layout.findViewById(R.id.rl_add).setOnClickListener(v -> {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.putExtra(BundleKeys.BANK_CARD_NAME, realName);
                intent.putExtra(BundleKeys.STAGE_JUMP, StageJumpEnum.STAGE_CASHIER.getModel());
                ActivityUtils.push(BankCardAddIdActivity.class, intent,
                        REQUEST_CODE_BRAND_DIALOG_BANK_CARD);
            });

            RecyclerView recyclerView = layout.findViewById(R.id.list_view_line);
            final SelectAdapter selectAdapter = new SelectAdapter(context, data, onSelectedListener, dialog);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(selectAdapter);
//            recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                }
//            });
            Window window = dialog.getWindow();
            window.setWindowAnimations(R.style.Animation_BottomSelect);
            dialog.setContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    DensityUtils.getPxByDip(DIALOG_MAX_DP_HEIGHT)));
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.gravity = Gravity.BOTTOM;
            lp.width = DataUtils.getCurrentDisplayMetrics().widthPixels;
            lp.dimAmount = 0.5f;
            return dialog;
        }

        //还款方式
        private void refundWayInit() {
            if (MiscUtils.isEmpty(data)) {
                data = new ArrayList<>();
            }
            List<BankCardModel> temList = new ArrayList<>();
            if (MiscUtils.isNotEmpty(refundTypeList)) {
                for (RefundResponse.RefundType refundType : refundTypeList) {
                    if (TextUtils.isEmpty(refundType.getPayType())) {
                        continue;
                    }
                    if ("WX".equals(refundType.getPayType().toUpperCase()) && "1".equals(refundType.getStatus())) {
                        BankCardModel selectItem = new BankCardModel();
                        selectItem.setRid(-1);
                        selectItem.setDrawableRes(R.drawable.ic_wechat);
                        selectItem.setBankName(AlaConfig.getResources().getString(R.string.pay_we_chat));
                        temList.add(selectItem);
                    }
                    if ("ZFB".equals(refundType.getPayType().toUpperCase()) && "1".equals(refundType.getStatus())) {
                        BankCardModel selectItem = new BankCardModel();
                        selectItem.setRid(-3);
                        selectItem.setDrawableRes(R.drawable.ic_ali_pay);
                        selectItem.setBankName(AlaConfig.getResources().getString(R.string.pay_ali_pay));
                        temList.add(selectItem);
                    }
                    if ("XXHK".equals(refundType.getPayType().toUpperCase()) && "1".equals(refundType.getStatus())) {
                        BankCardModel selectItem = new BankCardModel();
                        selectItem.setRid(-4);
                        selectItem.setDrawableRes(R.drawable.ic_outline_pay);
                        selectItem.setBankName(AlaConfig.getResources().getString(R.string.pay_outline));
                        temList.add(selectItem);
                    }
                }
                if (MiscUtils.isNotEmpty(temList)) {
                    data.addAll(0, temList);
                }
            }
        }
    }

    static class SelectAdapter extends RecyclerView.Adapter<SelectAdapter.ViewHolder> {

        private Context context;
        private List<BankCardModel> data;
        OnSelectedListener onSelectedListener;
        BottomSelectDialog dialog;

        SelectAdapter(Context context, List<BankCardModel> data, OnSelectedListener onSelectedListener, BottomSelectDialog bottomSelectDialog) {
            this.context = context;
            this.data = data;
            this.onSelectedListener = onSelectedListener;
            this.dialog = bottomSelectDialog;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.layout__bottom_select_dialog_list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final BankCardModel selectItem = data.get(position);
            String cardType = "";
            if (selectItem.getCardType() == ModelEnum.CREDIT.getValue()) {
                cardType = AlaConfig.getResources().getString(R.string.bank_card_type2);
                holder.tvCharge.setVisibility(View.VISIBLE);
            } else {
                holder.tvCharge.setVisibility(View.GONE);
            }
            if (MiscUtils.isNotEmpty(selectItem.getCardNumber())) {
                int startIndex = selectItem.getCardNumber().length() - 4;
                int endIndex = selectItem.getCardNumber().length();

                holder.tvName.setText(selectItem.getBankName() + cardType + "(" + selectItem.getCardNumber().substring(startIndex, endIndex) + ")");
            } else {
                holder.tvName.setText(selectItem.getBankName() + cardType);
            }
            if (MiscUtils.isNotEmpty(selectItem.getIsValid()) && "N".equals(selectItem.getIsValid())) {
                holder.tvTip.setText("银行维护中，请使用其他银行，谢谢！");
                holder.ivRight.setVisibility(View.GONE);
            } else if (selectItem.getBankStatus() != null) {
                String message = "";
                if (selectItem.getBankStatus().getLimitUp() != null)
                    message += "单笔限额:" + selectItem.getBankStatus().getLimitUp();
                if (selectItem.getBankStatus().getDailyLimit() != null)
                    message += " 单日限额:" + selectItem.getBankStatus().getDailyLimit();
                holder.tvTip.setText(message);
                holder.ivRight.setVisibility(View.VISIBLE);
            } else {
                holder.tvTip.setVisibility(View.GONE);
            }
            holder.cover.setVisibility((selectItem.getIsValid() == null || selectItem.getIsValid().equals("Y")) ? View.GONE :
                    View.VISIBLE);
            //为解决Glide Bug
            if (selectItem.getRid() < 0) {
                Glide.with(AlaConfig.getContext())
                        .load(selectItem.getDrawableRes())
                        .placeholder(R.drawable.ic_wechat)
                        .into(holder.ivLeft);
            } else {
                Glide.with(AlaConfig.getContext())
                        .load(TextUtils.isEmpty(selectItem.getBankIcon()) ? "" : selectItem.getBankIcon())
                        .placeholder(R.drawable.ic_wechat)
                        .error(R.drawable.ic_wechat)//加载失败应该使用默认图标，这里使用微信
                        .into(holder.ivLeft);
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            ImageView ivLeft;
            TextView tvName;
            ImageView ivRight;
            TextView tvTip;
            TextView tvCharge;
            View cover;

            public ViewHolder(final View itemView) {
                super(itemView);
                ivLeft = itemView.findViewById(R.id.iv_left);
                tvName = itemView.findViewById(R.id.tv_name);
                ivRight = itemView.findViewById(R.id.iv_right);
                tvTip = itemView.findViewById(R.id.tv_tip);
                tvCharge = itemView.findViewById(R.id.tv_charge);
                cover = itemView.findViewById(R.id.cover);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BankCardModel selectItem = data.get(getPosition());
                        if (MiscUtils.isNotEmpty(selectItem.getIsValid()) && "N".equals(selectItem.getIsValid())) {
                            String formatStr = String.format(AlaConfig.getResources().getString(R.string.dialog_pay_select_bank_toast_support_N), selectItem.getBankName());
                            UIUtils.showToast(formatStr);
                        } else {
                            onSelectedListener.onItemSelected(getPosition(), selectItem);
                            dialog.dismiss();
                        }
                    }
                });
            }
        }


    }
}
