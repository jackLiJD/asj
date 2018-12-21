package com.ald.asjauthlib.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.utils.ModelEnum;
import com.ald.asjauthlib.authframework.core.utils.DataUtils;

/**
 * Created by Yangyang on 2018/5/10.
 * desc:无法识别的卡时弹框
 */

public class NoSupportBankDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private TextView title;
    private TextView middle;
    private TextView back;
    private TextView sure;
    private TextView toBanklist;
    private ItemClickListener itemClickListener;

    public NoSupportBankDialog(@NonNull Context context) {
        this(context, R.style.Translucent_NoTitle);
    }

    public NoSupportBankDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_no_support_bank, null);
        title = view.findViewById(R.id.bank_tv_title);
        middle = view.findViewById(R.id.bank_tv_middle);
        back = view.findViewById(R.id.bank_tv_back);
        sure = view.findViewById(R.id.bank_tv_sure);
        toBanklist = view.findViewById(R.id.bank_tv_tobanklist);
        view.findViewById(R.id.bank_iv_close).setOnClickListener(this);
        back.setOnClickListener(this);
        sure.setOnClickListener(this);
        toBanklist.setOnClickListener(this);
        setContentView(view);
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.gravity = Gravity.CENTER;
            lp.width = (int) (DataUtils.getCurrentDisplayMetrics().widthPixels * 0.8);
            window.setAttributes(lp);
        }
    }

    public NoSupportBankDialog setTitle(String tv) {
        title.setText(tv);
        return this;
    }

    public NoSupportBankDialog setMiddle(String tv) {
        middle.setText(tv);
        return this;
    }

    public void setType(int type){
        if(type== ModelEnum.OTHER.getValue()){
            back.setVisibility(View.GONE);
            sure.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if(itemClickListener==null)return;
        int i = v.getId();
        if (i == R.id.bank_iv_close || i == R.id.bank_tv_back) {
            dismiss();

        } else if (i == R.id.bank_tv_sure) {
            itemClickListener.moveToOldActivity();

        } else if (i == R.id.bank_tv_tobanklist) {
            itemClickListener.showSupportBanks();

        }
    }

    public interface ItemClickListener {
        void moveToOldActivity();
        void showSupportBanks();
    }



    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
