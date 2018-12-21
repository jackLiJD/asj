package com.ald.asjauthlib.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.MainApi;
import com.ald.asjauthlib.auth.utils.RedPackageEnum;
import com.ald.asjauthlib.auth.model.RedPacketModel;
import com.ald.asjauthlib.utils.BundleKeys;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.network.RequestCallBack;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.moxie.client.MainActivity;

import retrofit2.Call;
import retrofit2.Response;

/*
 * Created by yangfeng01 on 2017/8/2.
 */

public class RedPackageDialog extends Dialog implements View.OnClickListener, DialogInterface.OnKeyListener {

	/** 借钱红包 */
	public static final String TYPE_LOAN_PACKET = "LOAN_PACKET";
	/** 风控拒绝红包 */
	public static final String TYPE_RISK_PACKET = "RISK_PACKET";

	private final Context context;
	/** 点击拆红包的红包类型 */
	private String type = TYPE_LOAN_PACKET;

	private TextView tvTitle;
	/**
	 * 拆红包背景
	 */
	private ImageView ivMain;
	private ImageView ivMainAfter;
	private TextView tvContent;
	private TextView tvContentAfter;
	private ImageView ivClose;
	private RelativeLayout rlContentAfter;

	/** 打开红包后: 获得星巴克5元代金券title */
	private TextView tvGetTitle;

	/** 打开红包后: 奖励已发放至我的-抵用券desc */
	private TextView tvGetDesc;


	/**
	 * 强风控:确定	弱风控:立即使用
	 */
	private RedPacketModel model;
	private IPackageOpenListener listener;

	public RedPackageDialog(Context context, int theme, String type) {
		super(context, theme);
		Window window = getWindow();
		if (window != null) {
			window.getDecorView().setPadding(0, 0, 0, 0);
			WindowManager.LayoutParams lp = window.getAttributes();
			lp.width = WindowManager.LayoutParams.MATCH_PARENT;
			lp.height = WindowManager.LayoutParams.MATCH_PARENT;
			window.setAttributes(lp);
		}

		this.context = context;
		this.type = type;
		View v = LayoutInflater.from(context).inflate(R.layout.dialog_red_package, null);

		ivClose =  v.findViewById(R.id.iv_close);
		ivClose.setOnClickListener(this);
		ivMain = v.findViewById(R.id.iv_main);
		ivMainAfter =  v.findViewById(R.id.iv_main_after);
		tvTitle = v.findViewById(R.id.tv_title);
		tvContent =  v.findViewById(R.id.tv_content);
		tvContentAfter =  v.findViewById(R.id.tv_content_after);
		rlContentAfter =  v.findViewById(R.id.rl_red_package_after);
		setContentView(v);
		setOnClick();

		tvGetTitle = v.findViewById(R.id.tv_get_title);
		tvGetDesc = v.findViewById(R.id.tv_get_desc);

		TextView tvSure =  v.findViewById(R.id.tv_sure);
		tvSure.setOnClickListener(this);
	}

	private void setOnClick() {
		ivMain.setOnClickListener(this);
		ivClose.setOnClickListener(this);
	}

	public void setTitle(String title) {
		if (MiscUtils.isNotEmpty(title)) {
			tvTitle.setText(title);
		}
	}

	public void setTitle(int resourceId) {
		if (resourceId != 0) {
			tvTitle.setText(resourceId);
		}
	}

	public void setContent(String content) {
		tvContent.setText(content);
	}

	public void setContentAfter(String contentAfter) {
		if (MiscUtils.isNotEmpty(contentAfter)) {
			tvContentAfter.setText(contentAfter);
		}
	}

	/**
	 * 强风控:用于认证红包，
	 */
	public void setStrongRisk(boolean strongRisk) {
		//是否在强风控页面
		if (strongRisk) {
			rlContentAfter.setVisibility(View.VISIBLE);
			ivMain.setVisibility(View.GONE);
			ivMainAfter.setVisibility(View.VISIBLE);
			tvContent.setVisibility(View.VISIBLE);
			tvGetTitle.setVisibility(View.GONE);
			tvGetDesc.setVisibility(View.GONE);
		} else {
			rlContentAfter.setVisibility(View.GONE);
			ivMain.setVisibility(View.VISIBLE);
			ivMainAfter.setVisibility(View.GONE);
			tvContent.setVisibility(View.VISIBLE);
			tvGetTitle.setVisibility(View.GONE);
			tvGetDesc.setVisibility(View.GONE);
		}

		//rlContentAfter.setVisibility(View.VISIBLE);
		//ivMain.setVisibility(View.GONE);
		//ivMainAfter.setVisibility(View.VISIBLE);
		//tvGetTitle.setVisibility(View.VISIBLE);
		//tvGetTitle.setText("50元满减券");
		//tvGetDesc.setVisibility(View.VISIBLE);
		//tvGetDesc.setText("奖励已发放至-抵用券");


	}

	/**
	 * 設置監聽
	 *
	 */
	public void setListener(IPackageOpenListener listener) {
		this.listener = listener;
	}

	@Override
	public void onClick(View v) {
		int i = v.getId();
		if (i == R.id.iv_main) {
			requestPacketInfo();

		} else if (i == R.id.tv_sure) {//if (model != null) {
			//	handleRedPackage();
			//} else {
			//	dismiss();
			//}
			//dismiss();
			dismiss();

		} else if (i == R.id.iv_close) {
			dismiss();

		} else {
		}
	}

	/**
	 * 拆红包
	 * 请求红包信息
	 */
	private void requestPacketInfo() {
		JSONObject object = new JSONObject();
		object.put("type", type);
		Call<RedPacketModel> call = RDClient.getService(MainApi.class).tearPacket(object);
		call.enqueue(new RequestCallBack<RedPacketModel>() {
			@Override
			public void onSuccess(Call<RedPacketModel> call, Response<RedPacketModel> response) {
				model = response.body();

				rlContentAfter.setVisibility(View.VISIBLE);
				ivMain.setVisibility(View.GONE);
				ivMainAfter.setVisibility(View.VISIBLE);
				tvContent.setVisibility(View.GONE);
				tvGetTitle.setVisibility(View.VISIBLE);
				tvGetDesc.setVisibility(View.VISIBLE);
				if (model != null) {
					// 有多个红包name截取换行符只展示第一个
					tvGetTitle.setText(model.getPrizeName());
					String desc;
					if (RedPackageEnum.CASH.getValue().equals(model.getPrizeType())) {
						desc = "返现金额";
					} else {
						desc = "抵用券";
					}
					tvGetDesc.setText(String.format(AlaConfig.getResources().getString(R.string
							.cash_loan_package_reward_to), desc));
				}

			}
		});
	}

	/*
	 * 处理红包跳转逻辑
	 */
	//private void handleRedPackage() {
	//	if (model == null) {
	//		return;
	//	}
	//	if (RedPackageEnum.MOBILE.getValue().equals(model.getPrizeType()) ||
	//			RedPackageEnum.FULLVOUCHER.getValue().equals(model.getPrizeType()) ||
	//			RedPackageEnum.CASH.getValue().equals(model.getPrizeType()) ||
	//			RedPackageEnum.ACTIVITY.getValue().equals(model.getPrizeType())) {
	//		jumpToHome();
	//	}
	//	if (RedPackageEnum.REPAYMENT.getValue().equals(model.getPrizeType())) {
	//		jumpToStageBill();
	//	}
	//	if (RedPackageEnum.FREEINTEREST.getValue().equals(model.getPrizeType())) {
	//		jumpToLoan();
	//	}
	//	if (RedPackageEnum.STROLL.getValue().equals(model.getPrizeType())) {
	//		jumpTobBrand();
	//	}
	//	if (listener != null) {
	//		listener.packageOpen(model);
	//	}
	//	dismiss();
	//}

	/**
	 * 跳转到首页
	 */
	private void jumpToHome() {
		/*Intent intent = new Intent();
		intent.putExtra(BundleKeys.MAIN_DATA_TAB, 0);
		ActivityUtils.push(MainActivity.class, intent);*/
	}

	/**
	 * 跳转到借钱
	 */
	private void jumpToLoan() {
		//跳转到借钱
		//ActivityUtils.push(CashLoanActivity.class);
	}


	/**
	 * 跳转到逛逛
	 */
	private void jumpTobBrand() {
		//跳转到借钱
		Intent intent = new Intent();
		intent.putExtra(BundleKeys.MAIN_DATA_TAB, 1);
		ActivityUtils.push(MainActivity.class, intent);
	}

	@Override
	public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
		return keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0;
	}

	/**
	 * 打开红包监听
	 */
	public interface IPackageOpenListener {
		/**
		 * 打开红包
		 */
		void packageOpen(RedPacketModel model);
	}
}
