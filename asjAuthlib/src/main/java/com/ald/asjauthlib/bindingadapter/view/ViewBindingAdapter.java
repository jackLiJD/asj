package com.ald.asjauthlib.bindingadapter.view;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.widget.AddSpaceTextWatcher;
import com.ald.asjauthlib.widget.TextSwitcher;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.ald.asjauthlib.authframework.core.ui.flowlayout.TagAdapter;
import com.ald.asjauthlib.authframework.core.ui.flowlayout.TagFlowLayout;
import com.ald.asjauthlib.authframework.core.utils.DensityUtils;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerClickListener;

import java.util.ArrayList;


/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/1/20 10:29
 * 描述：业务层的BindingAdapter
 * 修订历史：
 */
public final class ViewBindingAdapter {

    @BindingAdapter(value = {"textSize"}, requireAll = false)
    public static void setTextViewSize(final TextView textView, float textSize) {
        if (0 != textSize) {
            float size = DensityUtils.px2dp(textView.getContext(), textSize);
            textView.setTextSize(size);
        }
    }

    @BindingAdapter(value = {"view_width", "view_height"}, requireAll = false)
    public static void setViewWidthHeight(final View view, int viewWidth, int viewHeight) {
        if (0 != viewWidth) view.getLayoutParams().width = viewWidth;
        if (0 != viewHeight) view.getLayoutParams().height = viewHeight;
    }

    @BindingAdapter(value = {"paddingTop", "paddingBottom", "paddingLeft", "paddingRight"}, requireAll = false)
    public static void setViewPadding(final View view, int paddingTop, int paddingBottom, int paddingLeft, int paddingRight) {
        view.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
    }

    @BindingAdapter(value = {"leftMargin", "topMargin", "rightMargin", "bottomMargin"}, requireAll = false)
    public static void setViewMargin(final View view, int leftMargin, int topMargin, int rightMargin, int bottomMargin) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        params.leftMargin = leftMargin;
        params.topMargin = topMargin;
        params.rightMargin = rightMargin;
        params.bottomMargin = bottomMargin;
    }


    @BindingAdapter(value = {"bannerListener"}, requireAll = false)
    public static void bannerListener(final Banner banner, final BannerListener listener) {
        if (null != listener) {
            banner.setOnBannerClickListener(new OnBannerClickListener() {
                @Override
                public void OnBannerClick(int position) {
                    listener.onClick(position);
                }
            });
        }
    }

    @BindingAdapter(value = {"backgroundUrl", "backgroundColor"}, requireAll = false)
    public static void setViewGroupBackground(final View view, String backgroundUrl, String backgroundColor) {
        int bgResId = 0;
        try {
            if (MiscUtils.isEmpty(backgroundUrl) && MiscUtils.isEmpty(backgroundColor)) {
                bgResId = ContextCompat.getColor(view.getContext(), R.color.white);
                view.setBackgroundColor(bgResId);
            } else {
                if (MiscUtils.isNotEmpty(backgroundUrl)) {
//                    GlideApp.with(view.getContext()).load(backgroundUrl)
//                            .into(new ViewTarget<View, Drawable>(view) {
//
//                                @Override
//                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                                    view.setBackground(resource.getCurrent());
//                                }
//                            });
                           /* .into(new ViewTarget<View, GlideDrawable>(view) {
                                //括号里为需要加载的控件
                                @Override
                                public void onResourceReady(GlideDrawable resource,
                                                            GlideAnimation<? super GlideDrawable> glideAnimation) {
                                    view.setBackground(resource.getCurrent());
                                }
                            });*/

                    Glide.with(view.getContext())
                            .load(backgroundUrl)
                            .asBitmap()
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap>
                                        glideAnimation) {
                                    Drawable drawable = new BitmapDrawable(view.getResources(), resource);
                                    view.setBackground(drawable);
                                }
                            });
                } else if (MiscUtils.isNotEmpty(backgroundColor)) {
                    if (backgroundColor.charAt(0) == '#') {
                        view.setBackgroundColor(Color.parseColor(backgroundColor));
                    } else {
                        backgroundColor = "#" + backgroundColor;
                        view.setBackgroundColor(Color.parseColor(backgroundColor));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            view.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.white));
        }
    }

    /**
     * Banner切换动画
     */
    @BindingAdapter(value = {"bannerAnimation"}, requireAll = false)
    public static void bannerAnimation(final Banner banner, Class<? extends ViewPager.PageTransformer> animation) {
        banner.setBannerAnimation(animation);
    }

    /**
     * 设置RecyclerView Item宽高(布局使用RelativeLayout)
     */
    @BindingAdapter(value = {"itemLayoutParams"}, requireAll = false)
    public static void itemLayoutParams(final RelativeLayout itemView, ViewGroup.LayoutParams layoutParams) {
        itemView.getLayoutParams().width = layoutParams.width;
        itemView.getLayoutParams().height = layoutParams.height;
    }

    @BindingAdapter(value = {"onPullToRefreshListener"}, requireAll = false)
    public static void onPullToRefreshListener(final RecyclerView recyclerView, final PullToRefreshListener listener) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
//                    if (layoutManager instanceof LinearLayoutManager) {
//                        int lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
//                        int fistVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
//                        int totalCount = layoutManager.getItemCount();
//                        int totalVisibleCount = layoutManager.getChildCount();
//
//                        if ((totalCount > 0 && (lastVisibleItemPosition - fistVisibleItemPosition) <= totalVisibleCount &&
//                                lastVisibleItemPosition >= totalCount - 1)) {
//
//                        }
//                    }
                //               }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    if (!recyclerView.canScrollVertically(dy)) {
                        if (listener != null) {
                            listener.pullForBottom();
                        }
                    }
                }

            }
        });
    }

    @BindingAdapter({"seekProgress"})
    public static void SeekBarChangeListener(SeekBar seekBar, final OnProgressChangeListener changeListener) {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (!fromUser) {
                    return;
                }
                if (changeListener != null) {
                    changeListener.onProgressChanged(seekBar, progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (changeListener != null) {
                    changeListener.onStopChange(seekBar);
                }
            }
        });

    }

    @BindingAdapter("textWatch")
    public static void addTextWatch(final EditText editText, final OnWatchListener listener) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (listener != null) {
                    listener.onTextWatch(editText, editable.toString());
                }
            }
        });
    }

    @BindingAdapter("editActionDone")
    public static void addEditActionDone(final EditText editText, final OnActionDoneListener listener) {
        editText.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_DONE) {
                if (listener != null) {
                    listener.onActionDone(editText);
                }
                return true;
            }
            return false;
        });
    }

    @BindingAdapter("asjsetTextColor")
    public static void setTextColor(TextView textView, int textColor) {
        textView.setTextColor(textColor);
    }

    @BindingAdapter("switchWatch")
    public static void addSwitchWatch(ToggleButton button, final SwitchWatch watch) {
        button.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (watch != null)
                watch.onSwitch((ToggleButton) buttonView);
        });
    }

    @BindingAdapter("switchText")
    public static void addSwitchText(TextSwitcher switcher, ArrayList<String> textList) {
        switcher.addTextList(textList);
    }

    @BindingAdapter("checkWatch")
    public static void addSwitchWatch(CheckBox box, final CheckWatch watch) {
        box.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (watch != null) {
                watch.onCheck(buttonView);
            }
        });
    }

    @BindingAdapter("flowAdapter")
    public static void setAdapter(TagFlowLayout flowLayout, TagAdapter adapter) {
        flowLayout.setAdapter(adapter);
    }


    @Deprecated
    @BindingAdapter("mobilePhoneWatcher")
    public static void setPhoneWatcher(EditText editText, MobileWatcher watcher) {
        AddSpaceTextWatcher addSpaceTextWatcher = new AddSpaceTextWatcher(editText, 13);
    }

    //@BindingAdapter(value = {"hintText", "hintTextSize"}, requireAll = false)
    //public static void setHintTextSize(EditTextWithDelNew editTextWithDelNew, String hintText, int hintTextSize) {
    //	SpannableString spannableString = new SpannableString(hintText);
    //	AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(hintTextSize, false);
    //	spannableString.setSpan(absoluteSizeSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    //	editTextWithDelNew.setHint(new SpannableString(spannableString));
    //}

    @BindingAdapter("dynamicMarginBottom")
    public static void setMarginBottom(View view, int marginBottom) {
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        if (lp != null) {
            lp.setMargins(lp.leftMargin, lp.topMargin, lp.rightMargin, marginBottom);
            view.setLayoutParams(lp);
        }
    }


    @BindingAdapter(value = {"indicatorGravity"}, requireAll = false)
    public static void setBannerIndicatorGr(final Banner banner, int gravity) {
        banner.setIndicatorGravity(gravity);
    }


    public interface BannerListener {
        public void onClick(int position);
    }

    /**
     * 拉动加载监听
     */
    public interface PullToRefreshListener {

        //void pullForHeader();

        void pullForBottom();

    }

    public interface OnProgressChangeListener {
        void onProgressChanged(SeekBar seekBar, int progress);

        void onStopChange(SeekBar seekBar);
    }

    public interface OnWatchListener {
        void onTextWatch(EditText editText, String inputInfo);
    }

    public interface OnActionDoneListener {
        void onActionDone(EditText editText);
    }


    /**
     * 按钮开关事件监听
     */
    public interface SwitchWatch {
        void onSwitch(ToggleButton buttonView);
    }

    /**
     * 按钮开关事件监听
     */
    public interface CheckWatch {
        void onCheck(CompoundButton buttonView);
    }

    public interface MobileWatcher {

    }

    public interface BankCardWatcher {
    }


}

