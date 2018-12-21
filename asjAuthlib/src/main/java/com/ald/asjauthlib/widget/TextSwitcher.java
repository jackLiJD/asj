package com.ald.asjauthlib.widget;

import android.content.Context;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;

import java.util.ArrayList;

/**
 * Created by yushumin on 2017/4/28.
 */

public class TextSwitcher extends android.widget.TextSwitcher implements ViewSwitcher.ViewFactory {

    private int delayTime = 3 * 1000;
    private ArrayList<String> textList = new ArrayList<>();
    private int nowPosition;

    private static Handler handler = new Handler();
    private SwitcherRunnable runnable;

    public TextSwitcher(Context context) {
        super(context);
        initSwitcher();
    }

    public TextSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSwitcher();
    }

    private void initSwitcher() {
        setFactory(this);
        Animation in = AnimationUtils.loadAnimation(getContext(), R.anim.switcher_slide_in_bottom);
        Animation out = AnimationUtils.loadAnimation(getContext(), R.anim.switcher_slide_out_top);
        setInAnimation(in);
        setOutAnimation(out);

        runnable = new SwitcherRunnable();

    }

    private void switcherText() {
        handler.removeCallbacks(runnable);
        handler.post(runnable);
    }

    public void addTextList(ArrayList<String> textList) {
        if (textList == null) {
            return;
        }
        this.textList.addAll(textList);
        switcherText();
    }


    @Override
    public View makeView() {
        TextView textView = new TextView(getContext());
        textView.setTextSize(12);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(ContextCompat.getColor(AlaConfig.getContext(),R.color.text_normal_color));
        //textView.setSingleLine();
        textView.setEllipsize(TextUtils.TruncateAt.END);
        //      textView.setMaxEms( 20 );
        //        textView.setFilters( new InputFilter[]{new InputFilter.LengthFilter( 20)} );
        return textView;
    }

    private class SwitcherRunnable implements Runnable {

        @Override
        public void run() {
            nowPosition %= textList.size();
            setText(textList.get(nowPosition));
            nowPosition++;
            handler.postDelayed(this, delayTime);
        }
    }
}
