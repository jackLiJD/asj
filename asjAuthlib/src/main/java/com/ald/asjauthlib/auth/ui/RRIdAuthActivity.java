package com.ald.asjauthlib.auth.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.content.ContextCompat;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.dialog.ImageDialog;
import com.ald.asjauthlib.dialog.TwoButtonDialog;
import com.ald.asjauthlib.auth.viewmodel.AppealPhoneVM;
import com.ald.asjauthlib.auth.viewmodel.RRIdVM;
import com.ald.asjauthlib.databinding.ActivityRridAuthBinding;
import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.authframework.core.AlaTopBarActivity;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;


public class RRIdAuthActivity extends AlaTopBarActivity<ActivityRridAuthBinding> {

    RRIdVM rrIdVM;
    Context mContext;
    Boolean mIsAppeal;

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_rrid_auth;
    }

    @Override
    protected void setViewModel() {
        mContext = this;
        setTitle("身份认证");
        setTitleColor(ContextCompat.getColor(AlaConfig.getContext(),R.color.text_important_color));
        setReightImage(R.drawable.ic_id_q, v -> {
            ImageDialog imageDialog = new ImageDialog(mContext);
            imageDialog.show();

        });


        mIsAppeal = !MiscUtils.isEmpty(AppealPhoneVM.oldPhone);
        rrIdVM = new RRIdVM(this, cvb);
        cvb.setViewModel(rrIdVM);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public String getStatName() {
        return "身份认证";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BundleKeys.REQUEST_CODE_STAGE_RR_IDF_FRONT || requestCode == BundleKeys.REQUEST_CODE_STAGE_RR_IDF_BACK) {
            rrIdVM.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        if (!mIsAppeal) {
            TwoButtonDialog.Builder builder = new TwoButtonDialog.Builder(mContext);
            builder
                    .setContent(getResources().getString(R.string.auth_exit_dialog_message))
                    .setSureListener(v -> finish()).create()
                    .show();
        } else {
            super.onBackPressed();
        }
    }
}
