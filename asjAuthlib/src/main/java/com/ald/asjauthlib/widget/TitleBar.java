package com.ald.asjauthlib.widget;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ald.asjauthlib.R;


/**
 * Created by yangfeng01 on 2017/9/6.
 *
 * 高度44的TitleBar
 */

public class TitleBar extends FrameLayout implements View.OnClickListener {

	private Context context;
	private String title;
	private ImageView ivClose;
	private TextView tvTitle;

	private OnTitleBarListener listener;

	public TitleBar(@NonNull Context context) {
		this(context, null);
	}

	public TitleBar(@NonNull Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TitleBar(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		this.context = context;
		inflate(context, R.layout.layout_title_bar, this);
		ivClose = (ImageView) findViewById(R.id.iv_close);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		ivClose.setOnClickListener(this);
	}

	public void setTitle(String title) {
		this.title = title;
		tvTitle.setVisibility(VISIBLE);
		tvTitle.setText(title);
	}

	public void setLeftImage(@DrawableRes int drawableResId) {
		if (drawableResId > 0) {
			ivClose.setImageResource(drawableResId);
		}
	}

	@Override
	public void onClick(View view) {

		int i = view.getId();
		if (i == R.id.iv_close) {
			((Activity) context).onBackPressed();

		}
	}

	public void setOnTitleBarListener(OnTitleBarListener listener) {
		this.listener = listener;
	}

	public interface OnTitleBarListener {
		void onCloseClick(View view);
	}
}
