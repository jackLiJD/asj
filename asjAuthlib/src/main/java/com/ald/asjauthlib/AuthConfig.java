package com.ald.asjauthlib;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import com.ald.asjauthlib.auth.BusinessApi;
import com.ald.asjauthlib.auth.UserApi;
import com.ald.asjauthlib.auth.model.BorrowHomeModel;
import com.ald.asjauthlib.auth.model.LoginModel;
import com.ald.asjauthlib.auth.model.ThirdLoginModel;
import com.ald.asjauthlib.auth.model.UserModel;
import com.ald.asjauthlib.cashier.ui.AllBillsActivity;
import com.ald.asjauthlib.utils.BqsUtils;
import com.ald.asjauthlib.utils.BuildType;
import com.ald.asjauthlib.utils.Constant;
import com.ald.asjauthlib.utils.MXAuthUtils;
import com.ald.asjauthlib.utils.Utils;
import com.ald.asjauthlib.web.ApiReceiver;
import com.ald.asjauthlib.web.FqShopFragment;
import com.ald.asjauthlib.web.HTML5WebView;
import com.ald.asjauthlib.web.NativeReceiver;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.config.AccountProvider;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.config.ServerProvider;
import com.ald.asjauthlib.authframework.core.info.SharedInfo;
import com.ald.asjauthlib.authframework.core.location.LocationUtils;
import com.ald.asjauthlib.authframework.core.network.BaseObserver;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.network.RxUtils;
import com.ald.asjauthlib.authframework.core.network.exception.ApiException;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.InfoUtils;
import com.ald.asjauthlib.authframework.core.utils.SPUtil;
import com.ald.asjauthlib.authframework.core.utils.UIUtils;
import com.ald.asjauthlib.authframework.core.utils.log.Logger;

import cn.tongdun.android.shell.FMAgent;
import cn.tongdun.android.shell.exception.FMException;

import static com.ald.asjauthlib.utils.Utils.doAuth;
import static com.ald.asjauthlib.authframework.core.utils.UIUtils.sendBroadcast;

/**
 * Created by Yangyang on 2018/12/10.
 * desc:
 */

public class AuthConfig {
    //构建环境
    private static int buildType;
    private static GoHomeListener goHomeListener;


    public static void setBuildType(int buildType) {
        AuthConfig.buildType = buildType;
    }

    /**
     * 初始化sdk,在application中进行
     * loginClasses:登陆类
     */
    public static void init(Application application, Class<? extends Activity> loginClasses) {
        if (!UIUtils.onUiThread()) {
            throw new RuntimeException("AuthSDK 必须在UI线程上调用");
        }
        if (application == null) {
            Logger.d("AuthConfig", "初始化失败,application 不能为null");
            return;
        }
        if (loginClasses == null) {
            Logger.d("AuthConfig", "初始化失败,loginClasses 不能为null");
            return;
        }
        Utils.loginClass = loginClasses;
        AlaConfig.setDebug(BuildConfig.DEBUG);
        AlaConfig.init(application);
        initLocalData();
        setServerProvider();
        clearAuthLogin();
        LocationUtils.doInit();
        //白骑士初始化
        BqsUtils.initBqsSdk(application);
        //魔蝎SDK
        MXAuthUtils.initSDK(application);
        //同盾风控
        try {
            String env = FMAgent.ENV_PRODUCTION;
            if (AlaConfig.isDebug()) {
                env = FMAgent.ENV_SANDBOX;
            }
            FMAgent.init(application, env);
        } catch (FMException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据本地数据，进行初始化
     */
    private static void initLocalData() {
        AlaConfig.getLocalBroadcastManager().registerReceiver(new ApiReceiver(),
                ApiReceiver.INTENT_FILTER_OPEN);
        AlaConfig.getLocalBroadcastManager().registerReceiver(new NativeReceiver(),
                NativeReceiver.INTENT_FILTER_OPEN);
    }

    private static void setServerProvider() {
        AlaConfig.setServerProvider(new ServerProvider() {
            @Override
            public String getAppServer() {
                if (buildType == BuildType.DEBUG) {
                    return Constant.appServerTest;
                } else if (buildType == BuildType.PREVIEW) {
                    return Constant.appServerPreview;
                } else {
                    return Constant.appServer;
                }

            }

            @Override
            public String getImageServer() {
                if (buildType == BuildType.DEBUG) {
                    return Constant.imageServerTest;
                } else if (buildType == BuildType.PREVIEW) {
                    return Constant.imageServerPreview;
                } else {
                    return Constant.imageServer;
                }
            }

            @Override
            public String getH5Server() {
                if (buildType == BuildType.DEBUG) {
                    return Constant.h5ServerTest;
                } else if (buildType == BuildType.PREVIEW) {
                    return Constant.h5ServerPreview;
                } else {
                    return Constant.h5Server;
                }
            }
        });
    }


    /**
     * 同步登陆
     */
    public static void doAuthLogin(String phoneNum) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("mobilePhone", phoneNum);
        RDClient.getService(UserApi.class).doLogin(jsonObject).compose(RxUtils.io_main())
                .subscribe(new BaseObserver<ThirdLoginModel>() {
                    @Override
                    public void onSuccess(ThirdLoginModel thirdLoginModel) {
                        if (AlaConfig.isDebug()) UIUtils.showToast("登陆成功");
                        if (thirdLoginModel != null) {
                            LoginModel loginModel = new LoginModel();
                            UserModel userModel = new UserModel();
                            userModel.setMobile(thirdLoginModel.getMobilePhone());
                            userModel.setUserName(thirdLoginModel.getMobilePhone());
                            loginModel.setToken(thirdLoginModel.getToken());
                            loginModel.setUser(userModel);
                            AlaConfig.setAccountProvider(new AccountProvider() {
                                @Override
                                public String getUserName() {
                                    return thirdLoginModel.getMobilePhone();
                                }

                                @Override
                                public String getUserToken() {
                                    return thirdLoginModel.getToken();
                                }
                            });
                            AlaConfig.updateLand(true);
                            //本地存储登录信息
                            SPUtil.setValue(Constant.USER_PHONE, loginModel.getUser().getUserName());
                            SharedInfo.getInstance().setValue(LoginModel.class, loginModel);
                            sendBroadcast(HTML5WebView.ACTION_REFRESH);
                        }

                    }

                    @Override
                    public void onFailure(ApiException apiException) {
                        clearAuthLogin();
                    }
                });
    }

    /**
     * 清除用户信息
     */
    public static void clearAuthLogin() {
        AlaConfig.setAccountProvider(new AccountProvider() {
            @Override
            public String getUserName() {
                return InfoUtils.getDeviceId();
            }

            @Override
            public String getUserToken() {
                return "";
            }
        });
        SPUtil.remove(Constant.USER_PHONE);
        SharedInfo.getInstance().remove(LoginModel.class);
        AlaConfig.updateLand(false);
    }

    /**
     * 获取分期商城首页fragment
     */
    public static Fragment getFqShopFragment() {
        return new FqShopFragment();
    }

    /**
     * 传递首页activity
     */
    public static void setMainActivity(Activity activity) {
        ActivityUtils.push(activity);
        AlaConfig.setCurrentActivity(activity);
    }

    /**
     * 跳认证页面
    * */
    public static void startAuthActivity(){
        if(AlaConfig.isLand()){
            RDClient.getService(BusinessApi.class)
                    .getMyBorrowV1()
                    .compose(RxUtils.io_main())
                    .subscribe(new BaseObserver<BorrowHomeModel>() {
                        @Override
                        public void onSuccess(BorrowHomeModel borrowHomeModel) {
                            if(borrowHomeModel!=null){
                                String realName = borrowHomeModel.getRealName() == null ? "" : borrowHomeModel.getRealName();
                                doAuth(borrowHomeModel.getOnlineStatus(),realName);
                            }
                        }
                    });
        }else {
            Utils.jumpToLoginNoResult();
        }
    }

    /**
    * 跳转订单列表
     * index:0全部,1待支付,2待发货,3待收货
    * */
    public static void startOrderListActivity(int index){
        if(AlaConfig.isLand()){
            String url;
            switch (index){
                case 1:
                    url=AlaConfig.getServerProvider().getH5Server()+Constant.H5_ORDER_LIST_DAIZHIFU;
                    break;
                case 2:
                    url=AlaConfig.getServerProvider().getH5Server()+Constant.H5_ORDER_LIST_DAIFAHUO;
                    break;
                case 3:
                    url=AlaConfig.getServerProvider().getH5Server()+Constant.H5_ORDER_LIST_DAISHOUHUO;
                    break;
                default:
                    url=AlaConfig.getServerProvider().getH5Server()+Constant.H5_ORDER_LIST_ALL;
            }
            Utils.jumpH5(url);
        }else {
            Utils.jumpToLoginNoResult();
        }
    }

    /**
     * 跳转全部账单
     * */
    public static void startAllBillActivity(){
        if(AlaConfig.isLand()){
            ActivityUtils.push(AllBillsActivity.class);
        }else {
            Utils.jumpToLoginNoResult();
        }
    }

    /**
     * 回到首页监听
     * */
    public static void setGoHomeListener(GoHomeListener goHomeListener) {
        AuthConfig.goHomeListener = goHomeListener;
    }

    public static void goHomeAction(){
        if(goHomeListener!=null) goHomeListener.goHome();
    }

    public interface GoHomeListener{
       void goHome();
    }


}
