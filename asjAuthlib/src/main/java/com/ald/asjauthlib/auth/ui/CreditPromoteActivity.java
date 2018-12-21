package com.ald.asjauthlib.auth.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.viewmodel.CreditPromoteVM;
import com.ald.asjauthlib.databinding.ActivityCreditPromoteBinding;
import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.utils.StageJumpEnum;
import com.ald.asjauthlib.web.HTML5WebView;
import com.ald.asjauthlib.authframework.core.AlaTopBarActivity;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/1/9
 * 描述：
 * 修订历史：
 */
public class CreditPromoteActivity extends AlaTopBarActivity<ActivityCreditPromoteBinding> {

    private String stageJump;
    private CreditPromoteVM viewModel;
    private String scene;

    @Override
    public String getStatName() {
        return "提升额度页面";
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_credit_promote;
    }

    @Override
    protected void setViewModel() {
        viewModel = new CreditPromoteVM(this, getSupportFragmentManager(), cvb, scene);
        cvb.setViewModel(viewModel);
        cvb.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                viewModel.requestAuthInfo(refreshlayout);
            }
        });
    }

    @Override
    protected void afterOnCreate() {
        super.afterOnCreate();
        scene = getIntent().getStringExtra(BundleKeys.INTENT_KEY_CREDIT_PROMOTE_SCENE);
        setTitle(getResources().getString(R.string.credit_promote_auth_title));
        setTitleColor(ContextCompat.getColor(AlaConfig.getContext(), R.color.text_important_color));
        stageJump = getIntent().getStringExtra(BundleKeys.STAGE_JUMP);

    }

    @Override
    public void onBackPressed() {
        if (StageJumpEnum.STAGE_BANK_CARD.getModel().equals(stageJump)) {
            ActivityUtils.popUntil(BankCardListActivity.class);
        } else if (StageJumpEnum.STAGE_SET_PAY_PWD.getModel().equals(stageJump)) {
//            ActivityUtils.popUntil(SettingActivity.class);
        } else if (StageJumpEnum.STAGE_ORAL_ACTIVITY.getModel().equals(stageJump)) {
            finish();
        } else if (StageJumpEnum.STAGE_TRADE_SCAN.getModel().equals(stageJump)) {
            //广播H5页面刷新数据
            refreshH5();
            finish();
        } else if (StageJumpEnum.STAGE_AUTH.getModel().equals(stageJump)) {
            finish();
        } else if (StageJumpEnum.STAGE_BASIC_AUTH_EXIT.getModel().equals(stageJump)) {
            finish();
        } else {
            ActivityUtils.pop(CreditPromoteActivity.this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != 1000) {
            //1000为补充认证魔蝎回调，此处不处理,注意其他地方不要使用1000
            if (requestCode == BundleKeys.REQUEST_CREDIT_PROMOT_STATUS) {
                setResult(Activity.RESULT_OK);//刷新全部额度页面
                finish();
            } else if (requestCode == BundleKeys.REQUEST_CODE_CREDIT_PROMOTE_H5) {
//            来自h5页面的提交审核后刷新h5
                refreshH5();
                finish();
            } else if (requestCode == BundleKeys.REQUEST_CODE_CREDIT_PROMOTE_START_AUTH) {
                //来自注册引导页
                finish();
            } else if (requestCode == BundleKeys.REQUEST_CODE_SOCIAL_CONTACT) {
                //来自冒泡认证
                if (viewModel != null) {
                    viewModel.onExtraActivityResult(requestCode, resultCode, data);
                }
            } else {
                reload();
            }
        }
    }

    public CreditPromoteVM getViewModel() {
        return viewModel;
    }

    /**
     * 重新加载数据
     */
    public void reload() {
        if (viewModel != null)
            viewModel.requestAuthInfo(null);
    }

    public void refreshH5() {
        Intent intent = new Intent(HTML5WebView.ACTION_REFRESH);
        sendBroadcast(intent);
    }


    @Override
    public void onUIRequestPermissionsGrantedResult(int requestCode) {
        super.onUIRequestPermissionsGrantedResult(requestCode);
        if (viewModel != null)
            viewModel.onUIRequestPermissionsGrantedResult(requestCode);
    }
}
