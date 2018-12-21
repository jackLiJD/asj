package com.ald.asjauthlib.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.UserApi;
import com.ald.asjauthlib.auth.model.BankCardModel;
import com.ald.asjauthlib.auth.model.BankListModel;
import com.ald.asjauthlib.utils.ModelEnum;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.network.RequestCallBack;
import com.ald.asjauthlib.authframework.core.utils.DataUtils;
import com.ald.asjauthlib.authframework.core.utils.DensityUtils;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.authframework.core.utils.UIUtils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/3/2 11:13
 * 描述：底部选择Dialog
 * 修订历史：
 */
public class PayBankSelectDialog extends Dialog {
    private final static int DIALOG_MAX_DP_HEIGHT = 438;
    public final static long DIALOG_ADD_RID_ICON = -1;

    protected PayBankSelectDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private String title;
        private List<BankCardModel> data;
        private BankCardModel defaultBankCardModel;
        private OnSelectedListener onSelectedListener;

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

        public Builder setDefaultBankCardModel(BankCardModel defaultBankCardModel) {
            this.defaultBankCardModel = defaultBankCardModel;
            return this;
        }

        public Builder setData(List<BankCardModel> data) {
            this.data = data;
            return this;
        }

        public Builder setOnSelectedListener(OnSelectedListener onSelectedListener) {
            this.onSelectedListener = onSelectedListener;
            return this;
        }

        public PayBankSelectDialog create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final PayBankSelectDialog dialog = new PayBankSelectDialog(context, R.style.BottomSelectDialog);
            View layout = inflater.inflate(R.layout.layout_bottom_select_bank_dialog, null);
            TextView titleView = (TextView) layout.findViewById(R.id.tv_title);
            final ListView listView = ((ListView) layout.findViewById(R.id.list_view));
            final View loading = (View) layout.findViewById(R.id.loading);
            if (MiscUtils.isNotEmpty(title)) {
                titleView.setText(title);
            }
            layout.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            //获取银行卡列表
            JSONObject jsonObject=new JSONObject();
            //jsonObject.put("cardType","3");
            Call<BankListModel> call = RDClient.getService(UserApi.class).getBankCardList(jsonObject);
            call.enqueue(new RequestCallBack<BankListModel>() {
                @Override
                public void onSuccess(Call<BankListModel> call, Response<BankListModel> response) {
                    loading.setVisibility(View.GONE);
                    if (MiscUtils.isEmpty(response.body().getBankCardList())) {
                        data = new ArrayList<>();
                    } else {
                        data = response.body().getBankCardList();
                    }

                    BankCardModel selectItem = new BankCardModel();
                    selectItem.setRid(DIALOG_ADD_RID_ICON);
                    selectItem.setBankName(AlaConfig.getResources().getString(R.string.dialog_pay_select_bank));
                    data.add(selectItem);
                    final SelectAdapter selectAdapter = new SelectAdapter(context, data, defaultBankCardModel);
                    listView.setAdapter(selectAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (onSelectedListener != null) {
                                BankCardModel selectItem = selectAdapter.getItem(position);
                                if (MiscUtils.isNotEmpty(selectItem.getIsValid()) && "N".equals(selectItem.getIsValid())) {
                                    String formatStr = String.format(AlaConfig.getResources().getString(R.string.dialog_pay_select_bank_toast_support_N), selectItem.getBankName());
                                    UIUtils.showToast(formatStr);
                                    return;
                                }
                                onSelectedListener.onItemSelected(position, selectItem);
                                selectAdapter.setDefaultBankCardModel(selectItem);
                                dialog.dismiss();
                            }
                        }
                    });
                }

                @Override
                public void onFailure(Call<BankListModel> call, Throwable t) {
                    super.onFailure(call, t);
                    loading.setVisibility(View.GONE);
                }
            });

            Window window = dialog.getWindow();
            window.setWindowAnimations(R.style.Animation_LeftRight);
            dialog.setContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    DensityUtils.getPxByDip(DIALOG_MAX_DP_HEIGHT)));
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.gravity = Gravity.BOTTOM;
            lp.width = DataUtils.getCurrentDisplayMetrics().widthPixels;
            lp.dimAmount = 0.5f;
            window.setAttributes(lp);
            return dialog;
        }

    }

    public interface OnSelectedListener {
        void onItemSelected(int position, BankCardModel selectItem);
    }

    static class SelectAdapter extends BaseAdapter {

        class ViewHolder {
            ImageView ivLeft;
            TextView tvName;
            ImageView ivRight;
            TextView tvTip;
        }

        private Context context;
        private List<BankCardModel> data;
        private ViewHolder holder;
        private BankCardModel defaultBankCardModel;

        public SelectAdapter(Context context, List<BankCardModel> data, BankCardModel defaultBankCardModel) {
            this.context = context;
            this.data = data;
            this.defaultBankCardModel = defaultBankCardModel;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public BankCardModel getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void setDefaultBankCardModel(BankCardModel defaultBankCardModel) {
            this.defaultBankCardModel = defaultBankCardModel;
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.layout__dialog_select_bank_list_item, null);
                holder = new ViewHolder();
                holder.ivLeft = (ImageView) convertView.findViewById(R.id.iv_left);
                holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                holder.ivRight = (ImageView) convertView.findViewById(R.id.iv_right);
                holder.tvTip = (TextView) convertView.findViewById(R.id.tv_tip);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            BankCardModel selectItem = data.get(position);
            String cardType="";
            if(selectItem.getCardType()== ModelEnum.CREDIT.getValue()) cardType= AlaConfig.getResources().getString(R.string.bank_card_type2);
            if (MiscUtils.isNotEmpty(selectItem.getCardNumber())) {
                int startIndex = selectItem.getCardNumber().length() - 4;
                int endIndex = selectItem.getCardNumber().length();
                holder.tvName.setText(selectItem.getBankName()+cardType + "(" + selectItem.getCardNumber().substring(startIndex, endIndex) + ")");
            } else {
                holder.tvName.setText(selectItem.getBankName()+cardType);
            }

            if (MiscUtils.isNotEmpty(selectItem.getIsValid()) && "N".equals(selectItem.getIsValid())) {
                String formatStr = String.format(AlaConfig.getResources().getString(R.string.dialog_pay_select_bank_tip_support_N), selectItem.getBankName());
                holder.tvTip.setText(formatStr);
            } else {
                holder.tvTip.setText("");
            }

            if (defaultBankCardModel.getRid() == selectItem.getRid()) {
                holder.ivRight.setVisibility(View.VISIBLE);
            } else {
                holder.ivRight.setVisibility(View.GONE);
            }
            if (selectItem.getRid() == DIALOG_ADD_RID_ICON) {
                holder.ivLeft.setImageResource(R.drawable.ic_add_select_bank);
            } else {
                holder.ivLeft.setImageDrawable(null);
                Glide.with(AlaConfig.getContext()).load(selectItem.getBankIcon())
                        .crossFade(1000).into(holder.ivLeft);
            }
            return convertView;
        }
    }


}
