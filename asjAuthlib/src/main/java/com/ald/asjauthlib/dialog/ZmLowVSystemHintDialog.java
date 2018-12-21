package com.ald.asjauthlib.dialog;

import android.app.Dialog;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ald.asjauthlib.R;


/**
 * @author Pro47x
 * @date 2018/9/10 14:11
 */
public class ZmLowVSystemHintDialog extends DialogFragment implements View.OnClickListener {
    private ConfirmListener mConfirmListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_zm_lowv_check, container, false);
        View confirm = rootView.findViewById(R.id.btn_confirm);
        GradientDrawable background = (GradientDrawable) confirm.getBackground();
//        background.setColor(getContext().getResources().getColor(R.color.color_ffd500));
//        float c = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getContext().getResources().getDisplayMetrics());
//        background.setCornerRadii(new float[]{c, c, c, c, c, c, c, c});
        confirm.setOnClickListener(this);
        return rootView;
    }

    public void setConfirmListener(ConfirmListener confirmListener) {
        mConfirmListener = confirmListener;
    }

    @Override
    public void onClick(View v) {
        if (mConfirmListener != null) {
            mConfirmListener.confirm();
        }
        dismiss();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getActivity(), R.style.zmLowVCheck);
    }

    public interface ConfirmListener {
        void confirm();
    }
}
