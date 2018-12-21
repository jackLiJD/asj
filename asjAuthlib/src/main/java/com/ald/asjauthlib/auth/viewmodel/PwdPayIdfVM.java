package com.ald.asjauthlib.auth.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.ObservableField;
import android.view.View;
import android.widget.EditText;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.UserApi;
import com.ald.asjauthlib.auth.ui.PwdPayIdfActivity;
import com.ald.asjauthlib.auth.ui.PwdPayNewActivity;
import com.ald.asjauthlib.databinding.ActivityPwdPayIdfBinding;
import com.ald.asjauthlib.utils.AppUtils;
import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.utils.InputCheck;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.network.NetworkUtil;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.network.RequestCallBack;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.BASE64Encoder;
import com.ald.asjauthlib.authframework.core.utils.EditTextFormat;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.authframework.core.utils.UIUtils;
import com.ald.asjauthlib.authframework.core.vm.BaseVM;

import java.util.LinkedList;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/1/12 14:14
 * 描述：
 * 修订历史：
 */
public class PwdPayIdfVM extends BaseVM {
    private final ActivityPwdPayIdfBinding binding;
    private final String name;
    private final String stageJump;
    private Activity context;
    public final ObservableField<String> displayTitle = new ObservableField<>();

    /**
     * 需监听的editText list
     */
    public LinkedList<EditText> edList = new LinkedList<>();
    public ObservableField<Boolean> enable = new ObservableField<>(false);

    /**
     * 监听EditText 变化
     */
    public EditTextFormat.EditTextFormatWatcher watcher = new EditTextFormat.EditTextFormatWatcher() {
        @Override
        public void OnTextWatcher(String str) {
            enable.set(InputCheck.checkEtAndCbList(false,edList,null));
            enable.notifyChange();
        }
    };


    public PwdPayIdfVM(PwdPayIdfActivity activity, ActivityPwdPayIdfBinding cvb) {
        this.context = activity;
        this.binding = cvb;
        this.stageJump = context.getIntent().getStringExtra(BundleKeys.STAGE_JUMP);
        this.name = context.getIntent().getStringExtra(BundleKeys.BANK_CARD_NAME);
        String formatName = String
                .format(AlaConfig.getResources().getString(R.string.pwd_pay_id_no_title), AppUtils.formatRealName(name));
        displayTitle.set(formatName);
    }

    /**
     * 验证身份证
     */
    public void nextClick(View view) {
        final String idNumber = binding.etNo.getText().toString();
        if(MiscUtils.isEmpty(idNumber)){
            UIUtils.showToast(R.string.pwd_pay_id_no_hit);
            return;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("idNumber", BASE64Encoder.encodeString(idNumber));
        Call<Boolean> call = RDClient.getService(UserApi.class).checkIdNumber(jsonObject);
        NetworkUtil.showCutscenes(context,call);
        call.enqueue(new RequestCallBack<Boolean>() {
            @Override
            public void onSuccess(Call<Boolean> call, Response<Boolean> response) {
                if (response.body()) {
                    Intent intent = new Intent();
                    intent.putExtra(BundleKeys.STAGE_JUMP,stageJump);
                    intent.putExtra(BundleKeys.SETTING_PAY_ID_NUMBER, idNumber);
                    ActivityUtils.push(PwdPayNewActivity.class, intent);
                }
            }
        });

    }
}
