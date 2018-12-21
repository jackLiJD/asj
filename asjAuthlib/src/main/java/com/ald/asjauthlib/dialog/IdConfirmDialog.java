package com.ald.asjauthlib.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.AuthApi;
import com.ald.asjauthlib.auth.idcard.IDCardScanFactory;
import com.ald.asjauthlib.auth.model.YiTuUploadCardResultModel;
import com.ald.asjauthlib.auth.ui.StartFaceActivity;
import com.ald.asjauthlib.auth.viewmodel.AppealPhoneVM;
import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.utils.InputCheck;
import com.ald.asjauthlib.utils.KeyboardUtil;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.network.NetworkUtil;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.network.RequestCallBack;
import com.ald.asjauthlib.authframework.core.network.entity.ApiResponse;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.authframework.core.utils.UIUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Response;

/*
 * Created by luckyliang on 2017/11/8.
 */

public class IdConfirmDialog extends Dialog implements View.OnClickListener {

    private EditText editTextName;
    TextView tvSure;
    private YiTuUploadCardResultModel mYiTuReultModel;
    private Intent mIntent;

    public IdConfirmDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public IdConfirmDialog(Context context, Intent intent, boolean isAppeal) {
        super(context, 0);
        mIntent = intent;
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_fragment_id_confirm, null);
        editTextName = v.findViewById(R.id.txt_name);
        ImageView imageViewEdit = v.findViewById(R.id.btn_edit);
        imageViewEdit.setVisibility(IDCardScanFactory.mSwitchType.equals("Y") ? View.VISIBLE : View.GONE);
        tvSure = v.findViewById(R.id.tv_sure);
        TextView tvCancel = v.findViewById(R.id.tv_cancel);
        TextView tvId = v.findViewById(R.id.txt_id);
//        mYiTuReultModel = yiTuReultModel;
        mYiTuReultModel = (YiTuUploadCardResultModel) mIntent.getSerializableExtra(BundleKeys.RR_IDF_YITU_MODEL);
        editTextName.setText(mYiTuReultModel.getCardInfo().getName());
        setEditTextChinese(editTextName);
        tvId.setText(mYiTuReultModel.getCardInfo().getCitizen_id());
        editTextName.setEnabled(false);
        setContentView(v);
        tvSure.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        editTextName.setOnClickListener(this);
        imageViewEdit.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_edit) {
            editTextName.setEnabled(true);
            editTextName.requestFocus();
            editTextName.setSelection(editTextName.getText().length());
            KeyboardUtil.showKeyBoard(getContext());


        } else if (i == R.id.tv_sure) {//跳转
            if (editTextName.length() < 2 || editTextName.getText().length() > 15 || !InputCheck.checkChinese(editTextName.getText().toString())) {
                UIUtils.showToast("请输入正确的姓名");
                return;
            }
            if (!mYiTuReultModel.getCardInfo().getName().equals(editTextName.getText().toString().trim()))
                updateRealName();
            //从RRIdAuthActivity启动
            ActivityUtils.push(StartFaceActivity.class, mIntent, BundleKeys.REQUEST_CODE_STAGE_APPEAL);
            dismiss();

        } else if (i == R.id.tv_cancel) {
            dismiss();

        }
    }

    private void updateRealName() {
        JSONObject face = new JSONObject();
        face.put("realname", editTextName.getText().toString().trim());
        if (!MiscUtils.isEmpty(AppealPhoneVM.oldPhone)) {
            face.put("oldMobile", AppealPhoneVM.oldPhone);
        }
        Call<ApiResponse> call = MiscUtils.isEmpty(AppealPhoneVM.oldPhone) ? RDClient.getService(AuthApi.class).updateRealnameManual(face) :
                RDClient.getService(AuthApi.class).updateRealnameManualFree(face);
        NetworkUtil.showCutscenes(getContext(), call);
        call.enqueue(new RequestCallBack<ApiResponse>() {
            @Override
            public void onSuccess(Call<ApiResponse> call, Response<ApiResponse> response) {
                UIUtils.showToast("修改成功");
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                super.onFailure(call, t);
            }
        });

    }

    private void setEditTextChinese(final EditText editText) {

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String txt = s.toString();
                //注意返回值是char数组
                char[] stringArr = txt.toCharArray();
                for (char aStringArr : stringArr) {
                    //转化为string
                    String value = String.valueOf(aStringArr);
                    Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
                    Matcher m = p.matcher(value);
                    if (!m.matches()) {
                        editText.setText(editText.getText().toString().substring(0, editText.getText().toString().length() - 1));
                        editText.setSelection(editText.getText().toString().length());
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        editText.addTextChangedListener(textWatcher);
    }

}
