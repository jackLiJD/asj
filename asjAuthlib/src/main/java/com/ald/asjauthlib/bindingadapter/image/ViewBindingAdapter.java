package com.ald.asjauthlib.bindingadapter.image;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.model.BannerModel;
import com.ald.asjauthlib.utils.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.ViewTarget;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.ui.GlideRoundTransform;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import java.util.List;


/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/1/20 10:29
 * 描述：业务层的BindingAdapter
 * 修订历史：
 */
public final class ViewBindingAdapter {

    @BindingAdapter(value = {"bannerImages", "defaultImage", "layout_width", "layout_height", "layout_marginTop", "arcHeight"}, requireAll = false)
    public static void setBannerImage(final Banner banner, List<BannerModel> bannerModels, Drawable defaultImage, int layout_width, int layout_height, int layout_marginTop, int arcHeight) {
        if (null == defaultImage) {
            defaultImage = ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.bg_img_load_rect);
        }
        if (null == bannerModels) return;
        if (bannerModels.isEmpty()) {
            bannerModels.add(new BannerModel("-1"));
        }
        banner.setImages(bannerModels);
        banner.setImageLoader(new BannerGlideImageLoader(defaultImage)).start();
        if (0 != layout_width) banner.getLayoutParams().width = layout_width;
        if (0 != layout_height) banner.getLayoutParams().height = layout_height;
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) banner.getLayoutParams();
        params.topMargin = layout_marginTop;
//        if (0 != arcHeight && banner instanceof ArcBanner)
//            ((ArcBanner) banner).setArcHeight(arcHeight);
    }





    @BindingAdapter(value = {"bannerImages", "defaultImage", "layout_height", "layout_marginTop", "arcHeight"}, requireAll = false)
    public static void setLocalBannerImage(final Banner banner, List<Integer> bannerModels, Drawable defaultImage, int layout_height, int layout_marginTop, int arcHeight
    ) {
        if (null == defaultImage) {
            defaultImage = ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.bg_img_load_rect);
        }
        if (null == bannerModels) return;
        if (bannerModels.isEmpty()) {
            bannerModels.add(-1);
        }
        banner.setBannerStyle(BannerConfig.NOT_INDICATOR);
        banner.setImages(bannerModels);
        banner.setImageLoader(new BannerGlideImageLoader(defaultImage)).start();
        if (0 != layout_height) banner.getLayoutParams().height = layout_height;
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) banner.getLayoutParams();
        params.topMargin = layout_marginTop;
//        if (0 != arcHeight && banner instanceof ArcBanner)
//            ((ArcBanner) banner).setArcHeight(arcHeight);
    }


    @BindingAdapter(value = {"layout_marginTop"}, requireAll = false)
    public static void setViewMarginTop(final View view, int layout_marginTop) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        params.topMargin = layout_marginTop;
    }





    public interface GalleryBannerClickListener {
        void onClick(int position);
    }

    @BindingAdapter(value = {"selectImage", "defaultSelect", "unSelectImage", "defaultUnSelect", "isSelect"}, requireAll = false)
    public static void setTabImage(final ImageView imgView, String selectImage, Drawable defaultSelect,
                                   String unSelectImage, Drawable defaultUnSelect, boolean isSelect) {
        if (null == defaultSelect) {
            defaultSelect = ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.bg_img_load_rect);
        }
        if (null == defaultUnSelect) {
            defaultUnSelect = ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.bg_img_load_rect);
        }

        if (isSelect) {
            if (TextUtils.isEmpty(selectImage)) {
                Glide.with(imgView.getContext()).load("").error(defaultSelect)
                        .placeholder(defaultSelect).dontAnimate().fallback(defaultSelect).into(imgView);
//                ObjectAnimator animatorX = ObjectAnimator.ofFloat(imgView, View.SCALE_X, 0.9f, 1.1f, 0.95f, 1.05f, 1);
//                ObjectAnimator animatorY = ObjectAnimator.ofFloat(imgView, View.SCALE_Y, 0.9f, 1.1f, 0.95f, 1.05f, 1);
//                AnimatorSet set = new AnimatorSet();
//                set.setDuration(600);
//                set.playTogether(animatorX, animatorY);
////                set.setInterpolator(new BounceInterpolator());
//                set.start();
            } else {
                Glide.with(imgView.getContext()).load(selectImage).error(defaultSelect)
                        .placeholder(defaultSelect).fallback(defaultSelect).dontAnimate().into(imgView);
            }
        } else {
            Glide.with(imgView.getContext()).load(unSelectImage).error(defaultUnSelect)
                    .placeholder(defaultUnSelect).fallback(defaultUnSelect).dontAnimate().into(imgView);
        }
    }

   /* @BindingAdapter(value = {"isOpen", "animatorDuration"}, requireAll = false)
    public static void setTabImage(ImageView imageView, boolean isOpen, long animatorDuration) {
        if (isOpen) {
            imageView.setVisibility(View.VISIBLE);
            ObjectAnimator rotate = null, animatorX = null, animatorY = null;
            if (imageView.getId() == R.id.cashLoan_iv2) {
                rotate = ObjectAnimator.ofFloat(imageView, View.ROTATION, 45, -45, 36, -36, 18, -18, 9, 0);
            }
            if (imageView.getId() == R.id.home_iv2) {
                animatorX = ObjectAnimator.ofFloat(imageView, View.SCALE_X, 1, 1.25f, 0.85f, 1.1f, 0.9f, 1);
                animatorY = ObjectAnimator.ofFloat(imageView, View.SCALE_Y, 1, 1.25f, 0.85f, 1.1f, 0.9f, 1);
            } else if (imageView.getId() == R.id.cashLoan_iv2) {
                animatorX = ObjectAnimator.ofFloat(imageView, View.SCALE_X, 1, 1.5f, 0.8f, 1.4f, 0.9f, 1.3f, 0.95f, 1.2f, 1);
                animatorY = ObjectAnimator.ofFloat(imageView, View.SCALE_Y, 1, 1.5f, 0.8f, 1.4f, 0.9f, 1.3f, 0.95f, 1.2f, 1);
            } else {
                animatorX = ObjectAnimator.ofFloat(imageView, View.SCALE_X, 1, 1.4f, 0.8f, 1.2f, 0.9f, 1);
                animatorY = ObjectAnimator.ofFloat(imageView, View.SCALE_Y, 1, 1.4f, 0.8f, 1.2f, 0.9f, 1);
            }
            AnimatorSet set = new AnimatorSet();
            if (null == rotate) set.playTogether(animatorX, animatorY);
            else set.playTogether(animatorX, animatorY, rotate);
            set.setDuration(animatorDuration);
//            set.setInterpolator(new BounceInterpolator());
            set.start();
        } else {
            imageView.setVisibility(View.GONE);
        }
    }*/

    @BindingAdapter(value = {"selectColor", "defaultColor", "unSelectColor", "defaultUnColor", "isSelect"}, requireAll = false)
    public static void setTabText(final TextView textView, String selectColor, int defaultColor,
                                  String unSelectColor, int defaultUnColor, boolean isSelect) {
       /* if (MiscUtils.isEmpty(selectColor) || MiscUtils.isEmpty(unSelectColor)) {
            return;
        }*/
        try {
            if (0 == defaultColor) {
                defaultColor = ContextCompat.getColor(textView.getContext(), R.color.text_color_black);
            }
            if (0 == defaultUnColor) {
                defaultUnColor = ContextCompat.getColor(textView.getContext(), R.color.text_color_normal_light);
            }

            if (isSelect) {
                if (MiscUtils.isEmpty(selectColor)) {
                    textView.setTextColor(defaultColor);
                } else {
                    selectColor = selectColor.trim();
                    int sColor = Color.parseColor(selectColor);
                    textView.setTextColor(sColor);
                }

            } else {
                if (MiscUtils.isEmpty(unSelectColor)) {
                    textView.setTextColor(defaultUnColor);
                } else {
                    unSelectColor = unSelectColor.trim();
                    int unColor = Color.parseColor(unSelectColor);
                    textView.setTextColor(unColor);
                }

            }
        } catch (Exception e) {
            if (isSelect) textView.setTextColor(defaultColor);
            else textView.setTextColor(defaultUnColor);
        }

    }



    /**
     * 配置首页搜索标题栏背景
     *
     * @param imageView
     * @param bgUrl
     */
    @BindingAdapter(value = {"homeTitleBarBg"}, requireAll = false)
    public static void setSearchBg(final ImageView imageView, final String bgUrl) {
        int bgResId = 0;
        try {
            if (MiscUtils.isEmpty(bgUrl)) {
                bgResId = ContextCompat.getColor(imageView.getContext(), R.color.fb_colorPrimary);    // resource id
                imageView.setBackgroundColor(bgResId);
            } else {
                Glide.with(imageView.getContext())
                        .load(bgUrl)
                        .placeholder(R.color.fb_colorPrimary)
                        .error(R.color.fb_colorPrimary)
                        .into(imageView);
            }
        } catch (Exception e) {
            e.printStackTrace();
            imageView.setBackgroundColor(ContextCompat.getColor(imageView.getContext(), R.color.white));
        }
    }

    /**
     * 为ImageView设置圆角图片
     */
    @BindingAdapter(value = {"roundImageUrl", "defaultImage", "roundImageRadius"}, requireAll = false)
    public static void setRoundImage(final ImageView imageView, String roundImageUrl, Drawable defaultImage, int roundImageRadius) {
        if (null == defaultImage) {
            defaultImage = ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.bg_img_load_rect);
        }

        GlideRoundTransform transform = new GlideRoundTransform(imageView.getContext(), roundImageRadius);
        Glide.with(imageView.getContext()).load(roundImageUrl)
                //.transform(new CenterCrop(imageView.getContext()), transform)
                .transform(transform)
//                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(defaultImage)
                .error(defaultImage)
                .into(imageView);
    }

    @BindingAdapter(value = {"roundImageUrl", "roundImageRadius"}, requireAll = false)
    public static void setRoundImage(final ImageView imageView, String roundImageUrl, int roundImageRadius) {
        GlideRoundTransform transform = new GlideRoundTransform(imageView.getContext(), roundImageRadius);
        Glide.with(imageView.getContext()).load(roundImageUrl)
                .transform(transform)
                .into(imageView);
    }

    @BindingAdapter(value = {"url", "glideListener", "defaultImage"})
    public static void glideListener(final View view, String url, final GlideListener listener, Drawable defaultImage) {
        Glide.with(view.getContext()).load(url).placeholder(defaultImage)
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        view.setBackground(resource.getCurrent());
                        if (null != listener) listener.onComplete();
                    }
                });
//                .into(new ViewTarget<View, Drawable>(view) {
//                    @Override
//                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                        view.setBackground(resource.getCurrent());
//                        if (null != listener) listener.onComplete();
//                    }
//                });
               /* .into(new ViewTarget<View, GlideDrawable>(view) {
                    //括号里为需要加载的控件
                    @Override
                    public void onResourceReady(GlideDrawable resource,
                                                GlideAnimation<? super GlideDrawable> glideAnimation) {
                        view.setBackground(resource.getCurrent());
                        if (null != listener) listener.onComplete();
                    }
                });*/
    }

    public interface GlideListener {

        void onComplete();

    }

    @BindingAdapter(value = {"isBannerPlay"}, requireAll = false)
    public static void setBannerPlay(final Banner banner, boolean isBannerPlay) {
        if (isBannerPlay) banner.startAutoPlay();
        else banner.stopAutoPlay();
    }


    public static class BannerGlideImageLoader extends ImageLoader {
        private final Drawable defaultImage;

        public BannerGlideImageLoader(Drawable defaultImage) {
            this.defaultImage = defaultImage;
        }

        @Override
        public void displayImage(Context context, Object bannerModel, ImageView imageView) {
            if (bannerModel instanceof BannerModel) {
                if ("-1".equals(((BannerModel) bannerModel).getImageUrl())) {
                    imageView.setImageResource(R.drawable.transparent);
                } else {
                    Glide.with(context).load(((BannerModel) bannerModel).getImageUrl())
                            .diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(defaultImage).error(defaultImage)
//                            .transition(DrawableTransitionOptions.withCrossFade(1000))
                            .into(imageView);
                }
            } else {
                Glide.with(context).load(bannerModel)
                        .diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(defaultImage).error(defaultImage)
//                        .transition(DrawableTransitionOptions.withCrossFade(1000))
                        .into(imageView);
            }
        }
    }

    /*
     *  加载资源文件图片
     * */
    public static class ResourceImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            if (path instanceof Integer) {
                imageView.setImageResource((Integer) path);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        }
    }

    @BindingAdapter(value = {"textColor", "defaultColor", "visibility"}, requireAll = false)
    public static void setText(final TextView textView, String textColor, int defaultColor, boolean visibility) {
        if (0 == defaultColor) {
            defaultColor = ContextCompat.getColor(textView.getContext(), R.color.text_color_black);
        }
        Utils.colorIntValue(textColor, defaultColor);
        textView.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

}

