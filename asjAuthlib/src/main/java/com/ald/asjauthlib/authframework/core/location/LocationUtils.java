package com.ald.asjauthlib.authframework.core.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;

import com.ald.asjauthlib.authframework.core.data.CityNameCodeMapping;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.authframework.core.utils.UIUtils;
import com.ald.asjauthlib.authframework.core.utils.log.Logger;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;


/**
 * 此类负责定位
 *
 * @author Jacky
 */
public class LocationUtils {

    public static final String TAG = LocationUtils.class.getSimpleName();

    public static final String ACTION_TRY_TO_LOCATE = "android.com.authframework.core.location.ACTION_TRY_TO_LOCATE";

    private static final int MAX_RETRY_COUNT = 5;
    private static final long MIN_INTERVAL = 1100L;// 两次定位请求之间的最小间隔，系统是1秒，我们再加长一点
    private static final String LOCATION_PREFS = "_location_prefs";

    private static volatile LocationResult result;
    private static final ReentrantLock resultLock = new ReentrantLock();

    private static volatile LocationResult debugResult;//调试模式下，由人工可以更改的地址，这样方便在这个城市测试那个城市的结果
    private static volatile boolean locationFailure = true;

    private static volatile String ipCityCode;
    private static final ReentrantLock ipCityCodeLock = new ReentrantLock();

    private static BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            AlaConfig.execute(new Runnable() {
                @Override
                public void run() {
                    tryGaoDeLocate();
                }
            });
        }
    };

    private static boolean initialized;//是否已经初始化了
    private static final ReentrantLock initializedLock = new ReentrantLock();

    public static void setDebugResult(LocationResult debugResult) {
        LocationUtils.debugResult = debugResult;
    }

    public static boolean isLocationFailure() {
        return locationFailure;
    }

    private static void saveIpCityCode(String cityCode) {
        if (MiscUtils.isNotEmpty(cityCode)) {
            SharedPreferences prefs = AlaConfig.getContext().getSharedPreferences(LOCATION_PREFS, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("ipCityCode", cityCode);
            // 应为在setAndMaybeSaveLocationResult中要保证set和save是只能被一个线程独占操作的，所以这里不能用
            // apply来延迟保存。
            editor.commit();
        }
    }

    private static String readIpCityCode() {
        SharedPreferences prefs = AlaConfig.getContext().getSharedPreferences(LOCATION_PREFS, Context.MODE_PRIVATE);
        String cityCode = prefs.getString("ipCityCode", "");
        if (MiscUtils.isNotEmpty(cityCode)) {
            Logger.i("Jacky", "有缓存的ipCityCode");
            return cityCode;
        }
        return null;
    }

    public static String getIpCityCode() {
        return ipCityCode;
    }

    private static void saveLocationResult(LocationResult result) {
        if (result != null) {
            SharedPreferences prefs = AlaConfig.getContext().getSharedPreferences(LOCATION_PREFS, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("hasGaoDeLocation", true);
            editor.putString("address", result.getAddress());
            editor.putString("cityCode", result.getCityCode());
            editor.putString("cityName", result.getCityName());
            editor.putString("resultTime", result.getResultTime());
            editor.putString("longitude", String.valueOf(result.getLongitude()));
            editor.putString("latitude", String.valueOf(result.getLatitude()));
            editor.putString("radius", String.valueOf(result.getRadius()));
            editor.putString("province", result.getProvince());
            editor.putString("district", result.getDistrict());
            // 应为在setAndMaybeSaveLocationResult中要保证set和save是只能被一个线程独占操作的，所以这里不能用
            // apply来延迟保存。
            editor.commit();
        }
    }

    private static LocationResult readLocationResult() {
        // 这里进行读锁保护是因为要读取大批preference
        if (!resultLock.tryLock()) {
            return null;
        }
        try {
            SharedPreferences prefs = AlaConfig.getContext().getSharedPreferences(LOCATION_PREFS, Context.MODE_PRIVATE);
            boolean cachedGaoDeLocation = prefs.getBoolean("hasGaoDeLocation", false);
            if (cachedGaoDeLocation) {
                Logger.i("Jacky", "有缓存的位置");
                LocationResult result = new LocationResult();
                result.setResultTime(prefs.getString("resultTime", ""));
                result.setLongitude(MiscUtils.parseDouble(prefs.getString("longitude", "0"), 0));
                result.setLatitude(MiscUtils.parseDouble(prefs.getString("latitude", "0"), 0));
                result.setRadius(MiscUtils.parseDouble(prefs.getString("radius", "0"), 0));
                result.setAddress(prefs.getString("address", ""));
                result.setProvince(prefs.getString("province", ""));
                result.setCityName(prefs.getString("cityName", ""));
                result.setCityCode(prefs.getString("cityCode", ""));
                result.setDistrict(prefs.getString("district", ""));
                return result;
            } else {
                Logger.i("Jacky", "没有缓存位置");
            }
            return null;
        } finally {
            resultLock.unlock();
        }
    }

    /**
     * 请求一个同步的请求，此方法会阻塞，直到定位成功或者超时的时间到了
     *
     * @param maxWait 最多等多长时间，单位为毫秒
     */
    public static LocationResult requestLocation(long maxWait) {
        if (UIUtils.onUiThread()) {
            throw new RuntimeException("LocationUtils.requestLocation 必须在非UI线程上调用");
        }
        if (maxWait < 0 || maxWait > 120000L) {
            throw new RuntimeException("等待时长必须在0-120秒之间");
        }
        long from = System.currentTimeMillis();
        final CountDownLatch async2syncLatch = new CountDownLatch(1);
        final AMapLocationClient[] lc = new AMapLocationClient[1];
        final LocationResult[] lr = new LocationResult[1];
        AlaConfig.postOnUiThread(new Runnable() {
            @Override
            public void run() {
                final AMapLocationClient client = new AMapLocationClient(AlaConfig.getContext());
                lc[0] = client;
                client.setLocationListener(new AMapLocationListener() {
                    long retryCount = 0L;
                    long lastRequestTime = 0L;

                    @Override
                    public void onLocationChanged(AMapLocation location) {
                        Logger.i("Jacky", "onReceiveLocation:" + location.getLocationType());
                        if (isSuccess(location)) {
                            locationFailure = false;
                            Logger.i("Jacky", "高德定位-定位成功");
                            LocationResult result = from(location);
                            setAndMaybeSaveLocationResult(result, /* 保存结果? */ true);
                            lr[0] = result;
                            cleanUp();
                        } else {
                            if (location.getErrorCode() == AMapLocation.ERROR_CODE_FAILURE_LOCATION_PERMISSION) {
                                locationFailure = true;
                                Logger.i("Jacky", "高德定位-用户禁止");
                                cleanUp();
                            } else {
                                Logger.i("Jacky", "retry............" + retryCount);
                                if (retryCount < MAX_RETRY_COUNT) {
                                    retryCount++;
                                    long now = System.currentTimeMillis();
                                    long wait = MIN_INTERVAL - (now - lastRequestTime);
                                    if (wait > 0) {
                                        MiscUtils.sleep(wait);
                                    }
                                    lastRequestTime = now;
                                    client.startLocation();
                                } else {
                                    cleanUp();
                                }
                            }
                        }
                    }

                    private void cleanUp() {
                        lc[0] = null;
                        client.stopLocation();
                        async2syncLatch.countDown();
                    }
                });
                //声明mLocationOption对象
                AMapLocationClientOption mLocationOption = null;
                //初始化定位参数
                mLocationOption = new AMapLocationClientOption();
                //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
                mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
                //设置是否返回地址信息（默认返回地址信息）
                mLocationOption.setNeedAddress(true);
                //设置是否只定位一次,默认为false
                mLocationOption.setOnceLocation(false);
                //设置是否强制刷新WIFI，默认为强制刷新
                mLocationOption.setWifiActiveScan(true);
                //设置是否允许模拟位置,默认为false，不允许模拟位置
                mLocationOption.setMockEnable(false);
                //设置定位间隔,单位毫秒,默认为2000ms
                mLocationOption.setInterval(2000);
                //给定位客户端对象设置定位参数
                client.setLocationOption(mLocationOption);
                //启动定位
                client.startLocation();
            }
        });
        try {
            async2syncLatch.await(maxWait, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } finally {
            if (lc[0] != null) {
                lc[0].stopLocation();
            }
        }
        if (lr[0] == null) {
            locationFailure = true;
            Logger.i("Jacky", "定位失败");
        }
        String eventName = getElapsedTime("定位", from);
        return lr[0];
    }

    private static String getElapsedTime(String what, long from) {
        long usedTime = System.currentTimeMillis() - from;
        String eventName;
        if (usedTime < 500L) {
            eventName = "获取" + what + "定位小于0.5秒";
        } else if (usedTime < 1000L) {
            eventName = "获取" + what + "定位小于1秒";
        } else if (usedTime < 2000L) {
            eventName = "获取" + what + "定位小于2秒";
        } else if (usedTime < 3000L) {
            eventName = "获取" + what + "定位小于3秒";
        } else if (usedTime < 4000L) {
            eventName = "获取" + what + "定位小于4秒";
        } else if (usedTime < 5000L) {
            eventName = "获取" + what + "定位小于5秒";
        } else if (usedTime < 10000L) {
            eventName = "获取" + what + "定位小于10秒";
        } else {
            eventName = "获取" + what + "定位大于10秒";
        }
        return eventName;
    }


    public static LocationResult getCurrentLocation() {
        if (AlaConfig.isDebug() && debugResult != null) {
            return debugResult;
        }
        return result;
    }

    private static void tryGaoDeLocate() {
        long from = System.currentTimeMillis();
        LocationResult newResult = requestLocation(60000L);
        Logger.i("Jacky", "定位耗时:" + (System.currentTimeMillis() - from) + "ms,结果：" + newResult);
        if (newResult != null) {
            setAndMaybeSaveLocationResult(result, /* 保存结果? */ true);
        } else {
            Logger.i("Jacky", "定位耗时:" + (System.currentTimeMillis() - from) + "ms,失败");
        }
    }

    /**
     * 此方法只应该在AlaApplication中调用。
     */
    public static void doInit() {
        if (!initializedLock.tryLock()) {
            return;
        }
        try {
            if (initialized) {
                Logger.i(TAG, "already initialized");
                return;
            }
            initialized = true;
        } finally {
            initializedLock.unlock();
        }

        AlaConfig.getLocalBroadcastManager().registerReceiver(receiver, new IntentFilter(ACTION_TRY_TO_LOCATE));
        setAndMaybeSaveLocationResult(readLocationResult(), /* 保存结果? */  false);
        setAndMaybeSaveIpCityCode(readIpCityCode(), /* 保存结果? */  false);
        AlaConfig.execute(new Runnable() {
            @Override
            public void run() {
                tryGaoDeLocate();
            }
        });
//        //只在主进程中去做IP定位,没有必要去其他进程做IP定位
//        AlaConfig.execute(new Runnable() {
//            @Override
//            public void run() {
//                tryIpLocate();
//            }
//        });
    }


    /**
     * 设置并且根据开关决定是否保存新的位置信息。这个是线程安全的。
     */
    private static void setAndMaybeSaveLocationResult(LocationResult newResult, boolean save) {
        if (!resultLock.tryLock()) {
            return;
        }
        try {
            result = newResult;
            if (save) {
                saveLocationResult(newResult);
            }
        } finally {
            resultLock.unlock();
        }
    }

    private static boolean isSuccess(AMapLocation location) {
        if (location == null) {
            return false;
        }
        return location.getLongitude() > 1.0 && location.getLatitude() > 1.0;

    }

    private static LocationResult from(AMapLocation location) {
        LocationResult lr = new LocationResult();
        lr.setAddress(location.getAddress());
        lr.setCityName(location.getCity());
        lr.setCityCode(CityNameCodeMapping.getCityCode(location.getCity()));
        lr.setDistrict(location.getDistrict());
        lr.setLatitude(location.getLatitude());
        lr.setLongitude(location.getLongitude());
        lr.setProvince(location.getProvince());
        lr.setRadius(location.getAltitude());
        lr.setResultTime(location.getTime() + "");
        return lr;
    }


    /**
     * 设置并且根据开关决定是否保存新的位置信息。这个是线程安全的。
     */
    public static void setAndMaybeSaveIpCityCode(String ipCityCode, boolean save) {
        if (!ipCityCodeLock.tryLock()) {
            return;
        }
        try {
            LocationUtils.ipCityCode = ipCityCode;
            if (save) {
                saveIpCityCode(ipCityCode);
            }
        } finally {
            ipCityCodeLock.unlock();
        }
    }

    public static class AlaLocation {
        private double longitude;
        private double latitude;

        public AlaLocation(double longitude, double latitude) {
            this.longitude = longitude;
            this.latitude = latitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }
    }

}
