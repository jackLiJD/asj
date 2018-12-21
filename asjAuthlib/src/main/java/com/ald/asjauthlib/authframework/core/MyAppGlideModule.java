//package com.ald.asjauthlib.authframework.core;
//
//import android.content.Context;
//import android.support.annotation.NonNull;
//
//import com.bumptech.glide.GlideBuilder;
//import com.bumptech.glide.annotation.GlideModule;
//import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
//import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
//import com.bumptech.glide.load.engine.cache.LruResourceCache;
//import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
//import com.bumptech.glide.module.AppGlideModule;
//
///**
// * Created by Yangyang on 2018/7/17.
// * desc:
// */
//@GlideModule
//public class MyAppGlideModule extends AppGlideModule {
//    @Override
//    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
//        int cacheSize100MegaBytes = 104857600;
//        builder.setDiskCache(
//                new InternalCacheDiskCacheFactory(context, cacheSize100MegaBytes)
//        );
//        //builder.setDiskCache(
//        //new ExternalCacheDiskCacheFactory(context, cacheSize100MegaBytes));
//        // 20%大的内存缓存作为 Glide 的默认值
//        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context).build();
//        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
//        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();
//
//        int customMemoryCacheSize = (int) (1.2 * defaultMemoryCacheSize);
//        int customBitmapPoolSize = (int) (1.2 * defaultBitmapPoolSize);
//
//        builder.setMemoryCache( new LruResourceCache(customMemoryCacheSize));
//        builder.setBitmapPool( new LruBitmapPool(customBitmapPoolSize));
////        builder.setDefaultRequestOptions(new RequestOptions().format(DecodeFormat.PREFER_RGB_565));
//    }
//
//    @Override
//    public boolean isManifestParsingEnabled() {
//        //不使用清单配置的方式,减少初始化时间
//        return false;
//    }
//}
