package com.ald.asjauthlib.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.constraint.Group;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.utils.DataUtils;

/*
 * Created by yy .
 * 底部有1个或者2个按钮,中间纯文本或者含有title的dialog
 */

public class OneOrTwoButtonDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private TwoItemClickListener twoItemClickListener;
    private OneItemClickListener oneItemClickListener;
    private TextView leftTv;
    private TextView rightTv;
    private TextView txtMsg;
    private TextView titleTv;
    private TextView middleTv;
    private Group twoItemGroup;//有2个按钮则为true,显示左右和中间竖线
    private Group haveTitleGroup;//有title则为true,中部文案默认距离title4dp,距离底部横线19dp  ,若为false,怎title消失,中部文案默认距上下52dp
    private View bottomLine;
    private View middleLine;

    public TextView getTxtMsg() {
        return txtMsg;
    }

    public OneOrTwoButtonDialog(@NonNull Context context) {
        this(context, R.style.Translucent_NoTitle);
    }

    public OneOrTwoButtonDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_new_cashier_two_btn, null);
        leftTv = view.findViewById(R.id.tv_left);
        rightTv = view.findViewById(R.id.tv_right);
        txtMsg = view.findViewById(R.id.txt_msg);
        titleTv = view.findViewById(R.id.tv_title);
        middleTv = view.findViewById(R.id.tv_middle);
        twoItemGroup = view.findViewById(R.id.group_two_item);
        haveTitleGroup = view.findViewById(R.id.group_title);
        bottomLine = view.findViewById(R.id.bottom_line);
        middleLine = view.findViewById(R.id.middle_line);
        leftTv.setOnClickListener(this);
        rightTv.setOnClickListener(this);
        middleTv.setOnClickListener(this);
        setContentView(view);
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.gravity = Gravity.CENTER;
            lp.width = (int) (DataUtils.getCurrentDisplayMetrics().widthPixels * 0.8);
            lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
        }
    }

    /**
     * @author Yangyang
     * @desc 底部左右按钮+背景
     */
    public OneOrTwoButtonDialog setleftAndRightTv(String left, @ColorRes int leftColor, @ColorRes int leftBg, String right, @ColorRes int rightColor, @ColorRes int rightBg) {
        middleTv.setVisibility(View.GONE);
        leftTv.setText(left);
        leftTv.setTextColor(ContextCompat.getColor(AlaConfig.getContext(), leftColor));
        leftTv.setBackgroundResource(leftBg);
        rightTv.setText(right);
        rightTv.setTextColor(ContextCompat.getColor(AlaConfig.getContext(), rightColor));
        rightTv.setBackgroundResource(rightBg);
        bottomLine.setBackgroundResource(R.color.transparent);
        middleLine.setBackgroundResource(R.color.transparent);
        return this;
    }

    /**
     * @author Yangyang
     * @desc 底部左右按钮+背景
     */
    public OneOrTwoButtonDialog setleftAndRightTv(String left, @ColorRes int leftColor, Drawable leftBg, String right, @ColorRes int rightColor, Drawable rightBg,@ColorRes int middleLineColor) {
        middleTv.setVisibility(View.GONE);
        leftTv.setText(left);
        leftTv.setTextColor(ContextCompat.getColor(AlaConfig.getContext(), leftColor));
        leftTv.setBackground(leftBg);
        rightTv.setText(right);
        rightTv.setTextColor(ContextCompat.getColor(AlaConfig.getContext(), rightColor));
        rightTv.setBackground(rightBg);
        bottomLine.setBackgroundResource(middleLineColor);
        middleLine.setBackgroundResource(R.color.transparent);
        return this;
    }

    /**
     * @author Yangyang
     * @desc 底部左右按钮
     */
    public OneOrTwoButtonDialog setleftAndRightTv(String left, @ColorRes int leftColor, String right, @ColorRes int rightColor) {
        middleTv.setVisibility(View.GONE);
        leftTv.setText(left);
        leftTv.setTextColor(ContextCompat.getColor(AlaConfig.getContext(), leftColor));
        rightTv.setText(right);
        rightTv.setTextColor(ContextCompat.getColor(AlaConfig.getContext(), rightColor));
        return this;
    }

    /**
     * @author Yangyang
     * @desc 底部只有一个按钮, 且居中
     */
    public OneOrTwoButtonDialog setMiddleTv(String middle, @ColorRes int color) {
        twoItemGroup.setVisibility(View.GONE);
        middleTv.setText(middle);
        middleTv.setTextColor(ContextCompat.getColor(AlaConfig.getContext(), color));
        return this;
    }

    /**
     * @author Yangyang
     * @desc 默认13sp, 字体居中
     */
    public OneOrTwoButtonDialog setContentMsg(String msg) {
        return setContentMsg(msg, 13);
    }

    public OneOrTwoButtonDialog setContentMsg(String msg, int textSize) {
        return setContentMsg(msg, textSize, Gravity.CENTER);
    }

    public OneOrTwoButtonDialog setContentMsg(String msg, int textSize, int graviaty) {
        txtMsg.setText(msg);
        txtMsg.setTextSize(textSize);
        txtMsg.setGravity(graviaty);
        return this;
    }
    public OneOrTwoButtonDialog setContentMsg(SpannableString msg, int textSize, int graviaty) {
        txtMsg.setText(msg);
        txtMsg.setTextSize(textSize);
        txtMsg.setGravity(graviaty);
        return this;
    }

    public OneOrTwoButtonDialog setContentColor(@ColorRes int txtColor) {
        txtMsg.setTextColor(ContextCompat.getColor(AlaConfig.getContext(), txtColor));
        return this;
    }


    public OneOrTwoButtonDialog setTitle(String title) {
        haveTitleGroup.setVisibility(View.VISIBLE);
        titleTv.setText(title);
        return this;
    }

    /**
     * @author Yangyang
     * @desc 不需要title, 则msg居中
     */
    public OneOrTwoButtonDialog setNoTitle() {
        haveTitleGroup.setVisibility(View.GONE);
        return this;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_left) {
            dismiss();
            if (twoItemClickListener != null) twoItemClickListener.onLeftClick();

        } else if (i == R.id.tv_right) {
            dismiss();
            if (twoItemClickListener != null) twoItemClickListener.onRightClick();

        } else if (i == R.id.tv_middle) {
            dismiss();
            if (oneItemClickListener != null) oneItemClickListener.onMiddleClick();
        }
    }

    public interface TwoItemClickListener {
        void onLeftClick();

        void onRightClick();
    }

    public interface OneItemClickListener {
        void onMiddleClick();
    }


    /**
     * @author Yangyang
     * @desc 单按钮监听, 点击自动dismiss
     */
    public OneOrTwoButtonDialog setOneItemClickListener(OneItemClickListener itemClickListener) {
        this.oneItemClickListener = itemClickListener;
        return this;
    }

    /**
     * @author Yangyang
     * @desc 双按钮监听, 点击自动dismiss
     */
    public OneOrTwoButtonDialog setTwoItemClickListener(TwoItemClickListener itemClickListener) {
        this.twoItemClickListener = itemClickListener;
        return this;
    }
}
