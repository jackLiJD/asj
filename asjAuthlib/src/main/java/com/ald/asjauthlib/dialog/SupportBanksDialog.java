package com.ald.asjauthlib.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;


import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.model.BankCardTypeModel;
import com.ald.asjauthlib.utils.BaseViewHolder;
import com.ald.asjauthlib.utils.CommonRecyclerAdapter;

import java.util.List;

/**
 * Created by Yangyang on 2018/5/10.
 * desc:银行卡列表dialog
 */

public class SupportBanksDialog extends Dialog {
    private List<BankCardTypeModel> list;
    private Context context;

    public SupportBanksDialog(@NonNull Context context, List<BankCardTypeModel> bankCardTypeListModel) {
        this(context, R.style.Translucent_NoTitle, bankCardTypeListModel);
    }

    public SupportBanksDialog(@NonNull Context context, int themeResId, List<BankCardTypeModel> bankCardTypeListModel) {
        super(context, themeResId);
        this.list = bankCardTypeListModel;
        this.context = context;
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_support_bank_list, null);
        view.findViewById(R.id.banklist_iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        RecyclerView rvCashcard = view.findViewById(R.id.banklist_rv_cashcard);
        RecyclerView rvCreditcard = view.findViewById(R.id.banklist_rv_creditcard);
        setadapter(rvCashcard);
        setadapter(rvCreditcard);
        setContentView(view);
    }

    private void setadapter(RecyclerView rvCashcard) {
        rvCashcard.setLayoutManager(new GridLayoutManager(context, 3));
        SupportBanksAdapter supportBanksAdapter = new SupportBanksAdapter(rvCashcard, R.layout.banklist_item);
        supportBanksAdapter.setDataList(list);
        rvCashcard.setAdapter(supportBanksAdapter);
    }


    class SupportBanksAdapter extends CommonRecyclerAdapter<BankCardTypeModel> {

        public SupportBanksAdapter(RecyclerView recyclerView, int itemLayoutId) {
            super(recyclerView, itemLayoutId);
        }

        @Override
        public void bindViewData(BaseViewHolder baseViewHolder, BankCardTypeModel dataItem, int position) {
            baseViewHolder.setText(R.id.banklist_item_tv, dataItem.getBankName());
        }
    }

}
