package com.ald.asjauthlib.authframework.core.ui;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;


/*
 * Created by liangchen on 2018/3/12.
 */

public class PermissionRequestDialog extends Dialog {

    PermissionRequestDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }


    public static class Builder {

        Context context;
        String message;
        View.OnClickListener listener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTxtId(int txtId) {
            this.message = AlaConfig.getResources().getString(txtId);
            return this;
        }

        public Builder setListener(View.OnClickListener listener) {
            this.listener = listener;
            return this;
        }


        public PermissionRequestDialog creater() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final PermissionRequestDialog dialog = new PermissionRequestDialog(context, R.style.CustomDialog);
            dialog.setCancelable(true);
            View layout = inflater.inflate(R.layout.dialog_permission_request, null);
            TextView btnRequest = (TextView) layout.findViewById(R.id.btn_request);
            TextView tvMessage = (TextView) layout.findViewById(R.id.txt_message);
            if (!MiscUtils.isEmpty(message)) {
                tvMessage.setText(message);
            }
            btnRequest.setOnClickListener(listener);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            return dialog;
        }
    }
}
