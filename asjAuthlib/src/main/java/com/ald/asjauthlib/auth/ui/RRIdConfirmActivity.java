package com.ald.asjauthlib.auth.ui;

import android.os.Bundle;
import android.widget.TextView;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.utils.AppUtils;
import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.widget.TitleBar;
import com.ald.asjauthlib.authframework.core.config.AlaActivity;
import com.ald.asjauthlib.authframework.core.utils.BASE64Encoder;

public class RRIdConfirmActivity extends AlaActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rrid_confirm);
        TextView txtName =  findViewById(R.id.txt_name);
        TextView txtId = findViewById(R.id.txt_id);
        String name = getIntent().getStringExtra(BundleKeys.BANK_CARD_NAME);
        txtName.setText(name == null ? "" : AppUtils.formatRealName(name));
        String id = getIntent().getStringExtra(BundleKeys.SETTING_PAY_ID_NUMBER);
        if (id != null)
            id = AppUtils.formatStrWithStar(BASE64Encoder.decodeString(id),
                    getResources().getString(R.string.format_str_id_number), 0, 1, BASE64Encoder.decodeString(id).length() - 1);
        txtId.setText(id == null ? "" : id);
        TitleBar titleBar = findViewById(R.id.title);
        titleBar.setLeftImage(R.drawable.icon_titlebar_heise_fanhui);
        titleBar.setTitle("身份认证详情页");
    }

    @Override
    public String getStatName() {
        return "身份认证详情页";
    }

}
