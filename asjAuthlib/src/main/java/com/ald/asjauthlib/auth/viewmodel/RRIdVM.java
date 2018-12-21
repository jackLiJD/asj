package com.ald.asjauthlib.auth.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.hardware.Camera;
import android.view.View;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.idcard.IDCardScanFactory;
import com.ald.asjauthlib.auth.model.IDCardInfoModel;
import com.ald.asjauthlib.auth.ui.RRIdAuthActivity;
import com.ald.asjauthlib.auth.utils.HandlePicCallBack;
import com.ald.asjauthlib.databinding.ActivityRridAuthBinding;
import com.ald.asjauthlib.utils.Permissions;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.authframework.core.utils.PermissionCheck;
import com.ald.asjauthlib.authframework.core.utils.UIUtils;
import com.ald.asjauthlib.authframework.core.vm.BaseVM;
import com.bumptech.glide.Glide;


/*
 * Created by luckyliang on 2017/10/30.
 */

public class RRIdVM extends BaseVM {

    private IDCardScanFactory cardScanFactory;
    private Activity mActivity;
    private String uploadPath[] = new String[]{"", ""};
    public final ObservableField<String> frontSource = new ObservableField<>();
    public final ObservableField<String> backSource = new ObservableField<>();


    public final ObservableBoolean isAppealLayout = new ObservableBoolean(false);

    private ActivityRridAuthBinding mBinding;

    public RRIdVM(final RRIdAuthActivity activity, ActivityRridAuthBinding binding) {
        mActivity = activity;
        Boolean isAppeal = !MiscUtils.isEmpty(AppealPhoneVM.oldPhone);
        isAppealLayout.set(isAppeal);
        mBinding = binding;
        cardScanFactory = new IDCardScanFactory(mActivity).setCallBack(new HandlePicCallBack() {
            @Override
            public void onStart() {

            }

            @Override
            public void onHandle() {

            }

            @Override
            public void onSuccess(String[] picPath) {
                if(mActivity.isDestroyed()) return;
                if (picPath.length > 1 && !MiscUtils.isEmpty(picPath[1])) {
                    uploadPath[1] = picPath[1];
                    backSource.set(picPath[1]);
                    Glide.with(AlaConfig.getContext()).load(picPath[1]).into(mBinding.icIdback);
                    mBinding.icIdback.setVisibility(View.VISIBLE);
                    mBinding.btnModifyBack.setVisibility(View.VISIBLE);
                } else {
                    uploadPath[0] = picPath[0];
                    frontSource.set(picPath[0]);
                    Glide.with(AlaConfig.getContext()).load(picPath[0]).into(mBinding.icIdfront);
                    mBinding.icIdfront.setVisibility(View.VISIBLE);
                    mBinding.btnModifyFront.setVisibility(View.VISIBLE);
                }
                if (!MiscUtils.isEmpty(uploadPath[0]) && !MiscUtils.isEmpty(uploadPath[1])) {
                    mBinding.btnStart.setEnabled(true);
                }
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onIDInfoRecognized(IDCardInfoModel.CardInfo cardInfo) {

            }

        });
        mBinding.icIdback.setVisibility(View.GONE);
        mBinding.btnStart.setEnabled(false);
    }


    //点击识别身份证正面
    public void idfrontClick(View view) {
        if (checkPermission()) {
            cardScanFactory.scanFront();
        }


    }

    //扫描身份证背面
    public void idbackClick(View view) {
        if (checkPermission())
            cardScanFactory.scanBack();
    }


    //相机权限检查
    public boolean checkPermission() {
        if (!PermissionCheck.getInstance().checkPermission(mActivity, Permissions.facePermissions)) {
            PermissionCheck.getInstance().askForPermissions(mActivity, Permissions.facePermissions, PermissionCheck.REQUEST_CODE_CAMERA);
            return false;
        } else {
            Camera camera;
            try {
                camera = Camera.open();
                camera.startPreview();
            } catch (Exception ex) {
                PermissionCheck.getInstance().showAskDialog(mActivity,
                        R.string.permission_name_camera, (dialog, which) -> {

                        });
                return false;
            } finally {
                camera = null;
            }
            return true;
        }
    }

    //身份信息确认页面
    public void confirm(View view) {
        if (MiscUtils.isEmpty(uploadPath[0]) || MiscUtils.isEmpty(uploadPath[1])) {
            UIUtils.showToast("资料采集不全");
        } else {
            cardScanFactory.submitIDCard(uploadPath);

        }

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        cardScanFactory.handleScanResult(requestCode, resultCode, data);

    }
}
