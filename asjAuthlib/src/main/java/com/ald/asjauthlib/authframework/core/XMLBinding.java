package com.ald.asjauthlib.authframework.core;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.databinding.ViewStubProxy;
import android.databinding.adapters.ListenerUtil;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.AnimRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.ui.DividerLine;
import com.ald.asjauthlib.authframework.core.ui.GlideRoundTransform;
import com.ald.asjauthlib.authframework.core.ui.GridSpaceItemDecoration;
import com.ald.asjauthlib.authframework.core.ui.InputMethodLinearLayout;
import com.ald.asjauthlib.authframework.core.ui.transformer.GlideCircleTransform;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.BitmapUtil;
import com.ald.asjauthlib.authframework.core.utils.DensityUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.util.Util;
import com.ald.asjauthlib.authframework.core.ui.flowlayout.FlowLayout;
import com.ald.asjauthlib.authframework.core.utils.DisplayFormat;
import com.ald.asjauthlib.authframework.core.utils.EditTextFormat;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/1/5
 * 描述：XML binding数据
 * 修订历史：
 */
public class XMLBinding {
    /**
     * 设置方向指示器
     */
    @BindingAdapter({"asJimageIndicator"})
    public static void imageIndicator(ImageView imageView, boolean isShow) {
        if (isShow) {
            imageView.setImageResource(R.drawable.fw__ic_arrow_bottom);
        } else {
            imageView.setImageResource(R.drawable.fw__ic_arrow_right);
        }
    }

    /**
     * View选择
     */
    @BindingAdapter(value = {"asJselected", "asJselectorRes"}, requireAll = false)
    public static void isSelect(View view, boolean isSelect, int res) {
        view.setBackgroundResource(res);
        view.setSelected(isSelect);
    }

    /**
     * 为ImageView设置图片
     */
    @BindingAdapter(value = {"asJsrc", "asJdefaultImage", "asJhiddenPlaceholder", "asJwidth", "asJheight"}, requireAll = false)
    public static void setImage(final ImageView imageView, String path, Drawable defaultImage, boolean hidden, int width, int height) {
        if (null == defaultImage && !hidden) {
            defaultImage = ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.fw__default_picture);
        }
        if (defaultImage != null) {
            imageView.setImageDrawable(defaultImage);
        }
        if (width > 0 || height > 0) {
            ViewGroup.LayoutParams params = imageView.getLayoutParams();
            if (0 != width) params.width = width;
            if (0 != height) params.height = height;
        }
        // .diskCacheStrategy(DiskCacheStrategy.SOURCE)防止Glide压缩图片后导致颜色不准
        if (Util.isOnMainThread()) {
            if (0 != width && 0 != height) {
                Glide.with(AlaConfig.getContext()).load(path).override(width, height).skipMemoryCache(hidden).diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .placeholder(defaultImage).error(defaultImage).into(imageView);
            } else {
                Glide.with(AlaConfig.getContext()).load(path).skipMemoryCache(hidden).diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .placeholder(defaultImage).error(defaultImage).into(imageView);
            }
        }
    }

    @BindingAdapter(value = {"asJbackground", "asJdefaultImage"}, requireAll = false)
    public static void setBackground(final View view, String path, Drawable defaultImage) {
        if (defaultImage != null) {
            view.setBackground(defaultImage);
        }
        if (Util.isOnMainThread()) {
            Glide.with(AlaConfig.getContext()).load(path).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(defaultImage).error(defaultImage)
                    .into(new SimpleTarget<GlideDrawable>() {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                            resource.setBounds(0, 0, view.getWidth(), view.getHeight());
                            view.setBackground(resource);
                        }
                    });
//                    into(new SimpleTarget<Drawable>() {
//                @Override
//                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                    resource.setBounds(0, 0, view.getWidth(), view.getHeight());
//                    view.setBackground(resource);
//                }
//            });
        }
    }


    /**
     * 为ImageView设置图片
     * 当后台不传宽高时取原图比例适配手机屏幕
     */
    @BindingAdapter(value = {"asJsource", "asJdefaultImage", "asJanimRes", "asJwidth", "asJheight", "asJappRect"}, requireAll = false)
    public static void setImage(final ImageView imageView, String path, Drawable defaultImage, Animation animRes, int width, int height, final Rect appRect) {
        if (null == defaultImage)
            defaultImage = ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.fw__default_picture);
        if (defaultImage != null) imageView.setImageDrawable(defaultImage);
        if (null == animRes)
            animRes = AnimationUtils.loadAnimation(AlaConfig.getContext(), R.anim.anim_alpha_in);
        if (width > 0 && height > 0) {
            ViewGroup.LayoutParams params = imageView.getLayoutParams();
            params.width = width;
            params.height = height;
//            Glide.with(AlaConfig.getContext()).load(path).animate(animRes).diskCacheStrategy(DiskCacheStrategy.RESULT).
//                    placeholder(defaultImage).error(defaultImage).into(imageView);
            Glide.with(AlaConfig.getContext()).load(path).skipMemoryCache(false)
                    .placeholder(defaultImage).error(defaultImage).into(imageView);
        } else {
            Glide.with(AlaConfig.getContext()).load(path).diskCacheStrategy(DiskCacheStrategy.NONE
            ).into(new SimpleTarget<GlideDrawable>() {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    ViewGroup.LayoutParams lp = imageView.getLayoutParams();
                    lp.width = appRect.width();
                    lp.height = appRect.width() * resource.getIntrinsicHeight() / resource.getIntrinsicWidth();
                }
            });
//                    .into(new DrawableImageViewTarget(imageView) {
//                @Override
//                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                    super.onResourceReady(resource, transition);
//                    ViewGroup.LayoutParams lp = imageView.getLayoutParams();
//                    lp.width = appRect.width();
//                    lp.height = appRect.width() * resource.getIntrinsicHeight() / resource.getIntrinsicWidth();
//                }
//            });

                   /* .into(new GlideDrawableImageViewTarget(imageView) {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                            super.onResourceReady(resource, animation);
                            ViewGroup.LayoutParams lp = imageView.getLayoutParams();
                            lp.width = appRect.width();
                            lp.height = appRect.width() * resource.getIntrinsicHeight() / resource.getIntrinsicWidth();
                        }
                    });*/
        }
    }


   /* *//**
     * 商品详情大图
     *//*
    @BindingAdapter(value = {"source", "defaultImage"}, requireAll = false)
    public static void setSubsamplingScaleImageView(final SubsamplingScaleImageView imageView, String path, Drawable defaultImage) {
        *//*AlaConfig.execute(() -> {
            FutureTarget<File> submit = GlideApp.with(AlaConfig.getContext())
                    .asFile()
                    .load(path)
                    .submit();
            try {
                File file = submit.get();
                AlaConfig.postOnUiThread(() -> {
                    imageView.setImage(ImageSource.uri(Uri.fromFile(file)), new ImageViewState(0.0F, new PointF(0, 0), 0));
                    imageView.setZoomEnabled(false);
                });
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }*//*
        imageView.setImage(ImageSource.resource(R.drawable.bg_img_load_square));
        GlideApp.with(AlaConfig.getContext())
                .asFile()
                .load(path).into(new SimpleTarget<File>() {
            @Override
            public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                imageView.setImage(ImageSource.uri(Uri.fromFile(resource)));
            }
        });

    }*/

    /**
     * 加载本地GIF
     */
    @BindingAdapter(value = {"asJlocalGif"}, requireAll = false)
    public static void setLocalGif(final ImageView imageView, int localPath) {
        Glide.with(AlaConfig.getContext()).load(localPath).
                diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
    }

    /**
     * 为ImageView设置图片
     */
    @BindingAdapter(value = {"asJbitmap"}, requireAll = false)
    public static void setImageBitmap(final ImageView imageView, Bitmap bitmap) {
//        Glide.with(AlaConfig.getContext()).load(bitmap).diskCacheStrategy(DiskCacheStrategy.RESULT).into(imageView);
        imageView.setImageBitmap(bitmap);
    }

    /**
     * 设置view是否显示
     *
     * @param visible true - 显示
     *                false - 不显示
     */
    @BindingAdapter({"asJvisibility"})
    public static void viewVisibility(View view, boolean visible) {
        if (view == null) {
            return;
        }
        if (visible) {
            if (view.getVisibility() != View.VISIBLE) {
                view.setVisibility(View.VISIBLE);
            }
        } else {
            if (view.getVisibility() != View.GONE) {
                view.setVisibility(View.GONE);
            }
        }
    }


    /**
     * 设置view是否显示
     *
     * @param visible true - 显示
     *                false - 不显示
     */
    @BindingAdapter({"asJviewStubVisibility"})
    public static void viewStubVisibility(ViewStubProxy stubProxy, boolean visible) {
        ViewStub viewStub = stubProxy.getViewStub();
        if (viewStub != null) {
            if (visible) {
                viewStub.inflate();
            }
        } else {
            viewVisibility(stubProxy.getRoot(), visible);
        }
    }

    /**
     * 设置显示内容值类型转换为string
     */
    @BindingAdapter({"asJtoString"})
    public static void valueToString(TextView view, Object object) {
        view.setText(String.valueOf(object));
    }

    /**
     * 为RecyclerView添加分割线
     *
     * @param type 水平 - HORIZONTAL = 0;
     *             垂直 - VERTICAL = 1;
     */
    @BindingAdapter({"asJaddItemDecoration"})
    public static void addItemDecoration(RecyclerView view, int type) {
        DividerLine dividerLine;
        switch (type) {
            case DividerLine.HORIZONTAL:
                dividerLine = new DividerLine(DividerLine.HORIZONTAL);
                dividerLine.setMarginLeft(20);
                view.addItemDecoration(dividerLine);
                break;
            case DividerLine.HORIZONTAL_DEFAULT:
                dividerLine = new DividerLine(DividerLine.HORIZONTAL);
                dividerLine.setColorAndSize(view.getContext().getResources().getColor(R.color.color_eeeeee), DensityUtils.getPxByDip(0.3f));
                view.addItemDecoration(dividerLine);
                break;
            case DividerLine.VERTICAL:
                dividerLine = new DividerLine(DividerLine.VERTICAL);
                dividerLine.setMarginLeft(20);
                view.addItemDecoration(dividerLine);
                break;
            case DividerLine.GRID:
                dividerLine = new DividerLine(DividerLine.GRID);
                dividerLine.setColorAndSize(view.getContext().getResources().getColor(R.color.color_eeeeee), DensityUtils.getPxByDip(0.3f));
                view.addItemDecoration(dividerLine);
                break;
            case DividerLine.DEFAULT:
            default:
                break;
        }
    }

    @BindingAdapter(value = {"asJrecScrollChildX", "asJrecScrollChildWidth"}, requireAll = false)
    public static void horizontalRecyclerViewScrollCenter(RecyclerView recyclerView, int recScrollChildX, int recScrollChildWidth) {
        int middle = (int) ((recyclerView.getX() + recyclerView.getWidth() / 2) - recScrollChildWidth);
        if (recScrollChildX != middle)
            recyclerView.smoothScrollBy(recScrollChildX - middle, 0);
    }

    @BindingAdapter(value = {"asJgridSpanCount", "asJgridSpacing", "asJgridIncludeEdge"}, requireAll = false)
    public static void nineGridItemDecoration(RecyclerView recyclerView, int spanCount, int spacing, boolean includeEdge) {
        GridSpaceItemDecoration gridSpaceItemDecoration = new GridSpaceItemDecoration(spanCount, spacing, includeEdge);
        recyclerView.addItemDecoration(gridSpaceItemDecoration);
    }

    /**
     * 设置TextView显示不同颜色的字体（仅限改变数字颜色）
     */
    @BindingAdapter(value = {"asJmiddleColor", "asJmiddleValue"}, requireAll = false)
    public static void middleColorShow(TextView view, Integer color, String value) {
        if (color == null) {
            throw new IllegalArgumentException("TextView color must not be null");
        }
        if (value == null) {
            //设置默认值
            value = "0";
            throw new IllegalArgumentException("TextView value must not be null");
        }
        view.setText(DisplayFormat.XLIFFNumFormat(value, color));
    }

    /**
     * 限制EditText只能输入两位小数
     *
     * @param toggle 开关
     */
    @BindingAdapter({"asJfilter"})
    public static void lengthfilter(EditText view, boolean toggle) {
        if (toggle) {
            InputFilter[] old = view.getFilters();
            InputFilter[] filters = new InputFilter[old.length + 1];
            int position = 0;
            for (; position < old.length; position++) {
                filters[position] = old[position];
            }
            filters[position] = EditTextFormat.getLengthFilter();
            view.setFilters(filters);
        }
    }

    /**
     * 设置Textview drawable ColorFilter
     */
    @BindingAdapter(value = {"asJdrawableLeft", "asJdrawableTop", "asJdrawableRight", "asJdrawableBottom",
            "asJcolorFilter"}, requireAll = false)
    public static void drawableText(TextView tv, Drawable drawableLeft, Drawable drawableTop, Drawable drawableRight,
                                    Drawable drawableBottom, int color) {
        if (0 == color) {
            color = ContextCompat.getColor(AlaConfig.getContext(), R.color.fw_app_color_principal);
        }
        if (null != drawableLeft) {
            drawableLeft.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        }
        if (null != drawableTop) {
            drawableTop.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        }
        if (null != drawableBottom) {
            drawableRight.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        }
        if (null != drawableBottom) {
            drawableRight.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        }
        tv.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, drawableTop, drawableRight, drawableBottom);
    }


    /**
     * 设置Textview drawable ColorFilter
     */
    @BindingAdapter(value = {"asJdrawable", "asJcolorFilter"}, requireAll = true)
    public static void drawableImageView(ImageView imageView, Drawable drawable, int color) {
        if (0 == color) {
            color = ContextCompat.getColor(AlaConfig.getContext(), R.color.fw_app_color_principal);
        }
        if (null != drawable) {
            drawable.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        }

        imageView.setImageDrawable(drawable);

    }

    /**
     * list 中 EditText 变化通知
     */
    @BindingAdapter(value = {"asJwatcher", "asJlist"}, requireAll = false)
    public static void setEditChangeListener(EditText ed, EditTextFormat.EditTextFormatWatcher watcher,
                                             LinkedList<EditText> edlist) {
        if (null != watcher) {
            EditTextFormat.editChange(ed, watcher);
        }
        if (null != edlist) {
            edlist.add(ed);
        }
    }

    /**
     * list中CheckBox变化通知
     */
    @BindingAdapter(value = {"asJcb_watcher", "asJcb_list"}, requireAll = false)
    public static void setCheckedChangeListener(CheckBox cb, EditTextFormat.CheckBoxCheckedWatcher watcher,
                                                LinkedList<CheckBox> cbList) {
        if (null != watcher) {
            EditTextFormat.checkedChange(cb, watcher);
        }
        if (null != cbList) {
            cbList.add(cb);
        }
    }


    /**
     * 为ImageView设置圆角图片
     */
    @BindingAdapter(value = {"asJroundsrc", "asJdefaultImage"}, requireAll = false)
    public static void setRoundImage(final ImageView imageView, String path, Drawable defaultImage) {
        if (null == defaultImage) {
            defaultImage = ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.fw__default_picture);
        }
        GlideRoundTransform transform = new GlideRoundTransform(ActivityUtils.peek());
        Glide.with(AlaConfig.getContext()).load(path).transform(transform).placeholder(defaultImage)
                .error(defaultImage).into(imageView);
    }

    /**
     * 为ImageView设置成圆形图片
     */
    @BindingAdapter(value = {"asJcirclesrc", "asJdefaultImage"}, requireAll = false)
    public static void setCircleImage(final ImageView imageView, String path, Drawable defaultImage) {
        if (null == defaultImage) {
            defaultImage = ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.fw__default_picture);
        }
        Bitmap bitmap = BitmapUtil.drawable2Bitmap(defaultImage);
        GlideCircleTransform transform = new GlideCircleTransform(ActivityUtils.peek());
        Glide.with(AlaConfig.getContext()).load(path).transform(transform)
                .error(BitmapUtil.bitmap2Drawable(BitmapUtil.toRoundCorner(bitmap, 90))).into(imageView);
    }

    @BindingAdapter(value = {"asJbeforeTextChange", "asJonTextChanged", "asJafterTextChanged"}, requireAll = false)
    public static void setTextWatcher(TextView textView, final XMLBindListener.TextWatcher.beforeTextChange before,
                                      final XMLBindListener.TextWatcher.onTextChanged changed,
                                      final XMLBindListener.TextWatcher.afterChanged afterChanged) {

        TextWatcher newValue = null;
        if (before == null && changed == null && afterChanged == null) {
            newValue = null;
        } else {
            newValue = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    if (before != null) {
                        before.onChange(s, start, count, after);
                    }
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (changed != null) {
                        changed.onChange(s, start, before, count);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (afterChanged != null) {
                        afterChanged.onChange(s);
                    }
                }
            };
        }

        TextWatcher oldValue = ListenerUtil.trackListener(textView, newValue, R.id.textWatcher);
        if (oldValue != null) {
            textView.removeTextChangedListener(oldValue);
        }
        if (newValue != null) {
            textView.addTextChangedListener(newValue);
        }
    }

    @BindingAdapter(value = {"asJswitchWatcher"})
    public static void setSwitchWatcher(final CompoundButton button,
                                        final XMLBindListener.SwitchWatcher switchWatcher) {
        CompoundButton.OnCheckedChangeListener newValue = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switchWatcher != null) {
                    switchWatcher.onBtnSwitch(buttonView);
                }
            }
        };
        button.setOnCheckedChangeListener(newValue);
    }

    @BindingAdapter(value = {"asJanimation", "asJanimationRes", "asJanimationStart", "asJanimationEnd", "asJanimationRepeat"}, requireAll = false)
    public static void setAnimation(final View view, Animation animation, @AnimRes int animationRes,
                                    final XMLBindListener.AnimationListener.AnimationStart start,
                                    final XMLBindListener.AnimationListener.AnimationEnd end,
                                    final XMLBindListener.AnimationListener.AnimationRepeat repeat) {
        if (animationRes == 0 && animation == null) {
            return;
        }
        if (animationRes != 0) {
            animation = AnimationUtils.loadAnimation(view.getContext(), animationRes);
        }
        Animation.AnimationListener listener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (start != null) {
                    start.onStart(animation, view);
                }
                view.setAnimation(animation);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (end != null) {
                    end.onEnd(animation, view);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                if (repeat != null) {
                    repeat.onRepeat(animation, view);
                }
            }
        };
        animation.setAnimationListener(listener);
    }


    @BindingAdapter("asJshowInputMethod")
    public static void isShowInputMethod(View view, boolean isShow) {
        if (isShow) {
            view.setFocusable(true);
            view.requestFocus();
            //打开软键盘
            InputMethodManager imm = (InputMethodManager) view.getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        } else {
            //隐藏软键盘
            InputMethodManager imm = (InputMethodManager) view.getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @BindingAdapter("asJchildViews")
    public static void addChildView(final ViewGroup viewGroup, ObservableArrayList<View> views) {
        if (views == null) {
            return;
        }
        views.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<View>>() {
            @Override
            public void onChanged(ObservableList<View> sender) {

            }

            @Override
            public void onItemRangeChanged(ObservableList<View> sender, int positionStart, int itemCount) {

            }

            @Override
            public void onItemRangeInserted(ObservableList<View> sender, int positionStart, int itemCount) {
                Iterator<View> iterator = sender.iterator();
                while (iterator.hasNext()) {
                    View childView = iterator.next();
                    if (childView.getParent() != null) {
                        ((ViewGroup) childView.getParent()).removeView(childView);
                    }
                    viewGroup.addView(childView);
                }
            }

            @Override
            public void onItemRangeMoved(ObservableList<View> sender, int fromPosition, int toPosition, int itemCount) {

            }

            @Override
            public void onItemRangeRemoved(ObservableList<View> sender, int positionStart, int itemCount) {
                Iterator<View> iterator = sender.iterator();
                while (iterator.hasNext()) {
                    View childView = iterator.next();
                    if (childView.getParent() != null) {
                        ((ViewGroup) childView.getParent()).removeView(childView);
                    }
                }
            }
        });
    }

    @BindingAdapter(value = {"asJInputMethodLinearLayoutListener"}, requireAll = false)
    public static void bannerListener(final InputMethodLinearLayout inputMethodLinearLayout, final
    XMLBindListener.InputMethodLinearLayoutListener listener) {
        if (null != listener) {
            inputMethodLinearLayout.setOnSizeChangeListener(new InputMethodLinearLayout.OnSizeChangeListener() {
                @Override
                public void onSizeLarger() {
                    listener.onSizeLarger(inputMethodLinearLayout);
                }

                @Override
                public void onSizeSmaller() {
                    listener.onSizeSmaller(inputMethodLinearLayout);
                }
            });
        }
    }

    @BindingAdapter(value = {"asJtypefaceSpan"}, requireAll = false)
    public static void typefaceSpan(TextView textView, String typefaceSpan) {
        if (TextUtils.isEmpty(typefaceSpan)) return;
        SpannableString spanRel = new SpannableString(typefaceSpan);
        RelativeSizeSpan sizeSpan = new RelativeSizeSpan(0.8f);
        spanRel.setSpan(sizeSpan, 0, 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        textView.setText(spanRel);
    }

    @BindingAdapter(value = {"asJlabels"})
    public static void setFlowLayout(FlowLayout flowLayout, List<String> labels) {
        flowLayout.removeAllViews();
        for (String label : labels) {
            FrameLayout fl = (FrameLayout) LayoutInflater.from(flowLayout.getContext()).inflate(R.layout.flow_tv, null);
            TextView tv = fl.findViewById(R.id.tv);
            tv.setText(label);
            flowLayout.addView(fl);
        }
    }

    @BindingAdapter(value = {"asJrvListener"})
    public static void setRvListener(RecyclerView recyclerView, RecyclerView.OnScrollListener listener) {
        if (listener == null) return;
        recyclerView.clearOnScrollListeners();
        recyclerView.addOnScrollListener(listener);
    }

    @BindingAdapter(value = {"asJnewlabels"})
    public static void setNewFlowLayout(FlowLayout flowLayout, List<String> labels) {
        flowLayout.removeAllViews();
        for (String label : labels) {
            FrameLayout fl = (FrameLayout) LayoutInflater.from(flowLayout.getContext()).inflate(R.layout.new_flow_tv, null);
            TextView tv = fl.findViewById(R.id.tv);
            tv.setText(label);
            flowLayout.addView(fl);
        }
    }

    @BindingAdapter(value = {"asJimageLabel"})
    public static void setImageLabels(LinearLayout layout, List<String> labels) {
        layout.removeAllViews();
        for (String label : labels) {
            ImageView child = new ImageView(layout.getContext());
//            child.setScaleType(ImageView.ScaleType.FIT_XY);
            layout.addView(child);
            child.setAdjustViewBounds(true);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) child.getLayoutParams();
            layoutParams.height=DensityUtils.getPxByDip(16);
            layoutParams.rightMargin = DensityUtil.dp2px(5);
            child.setLayoutParams(layoutParams);
            if (Util.isOnMainThread()) {
                Glide.with(AlaConfig.getContext())
                        .load(label)
                        .skipMemoryCache(false)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(new SimpleTarget<GlideDrawable>() {
                            @Override
                            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                child.setImageDrawable(resource);
                            }
                        });
            }
        }
    }

    @BindingAdapter(value = {"asJviewCheck"})
    public static void setviewCheck(View view,boolean isChecked) {
        view.setSelected(isChecked);
    }
}
