package com.ald.asjauthlib.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ald.asjauthlib.R;


/**
 * Created by luckyliang on 2017/11/9.
 */

public class ImageDialog extends Dialog {
    
    TextView textView;

    public ImageDialog(Context context, int themeResId) {
        super(context, themeResId);

    }

    public ImageDialog(Context mContext) {
        super(mContext, 0);
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_image, null);
        textView = (TextView) v.findViewById(R.id.btn);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setContentView(v);
    }

}
