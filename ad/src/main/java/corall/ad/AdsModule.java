package corall.ad;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Message;
import android.text.TextUtils;

import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.MobileAds;
import com.mopub.common.MoPub;
import com.mopub.common.SdkConfiguration;
import com.mopub.common.SdkInitializationListener;
import com.mopub.common.logging.MoPubLog;
import com.orhanobut.hawk.Hawk;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import corall.ad.bean.CorAdPlace;
import corall.ad.bean.CorAdUnionPlace;
import corall.ad.cache.AdsConfigCache;
import corall.base.task.CorTask;
import corall.base.app.CorApplication;
import corall.base.app.AModule;
import corall.base.util.AppInfoUtil;
import corall.base.util.Des3Util;
import corall.base.util.KeyUtil;
import corall.base.util.StringUtil;
import corall.base.util.Utils;

/**
 * 广告主模块
 * Created by ChenLi on 2017/11/20.
 */

public class AdsModule extends AModule {

    public static final String MODULE_KEY = "spring_md";

    private AdsBeanManager adsBeanManager;

    private boolean isTesting;

    private boolean isInstallFacebook;
    private boolean isInitComplete;
    private int decodeOff;

    //是否需要初始化mopub广告平台
    private boolean isNeedInitMopub;

    //mopub广告是否初始化完毕
    private boolean isMopubInit;

    private static boolean isTestAd = false;

    private static boolean isVpn = false;
    private static boolean isEmulator = false;
    private static boolean blockable = false;
    private boolean alreadyInitAd;
    private AdvInitListener mInitListener;

    public AdsModule(CorApplication context, String moduleMark) {
        super(context, moduleMark);
        isTesting = false;
        decodeOff = -3;
    }

    public void openTesting() {
        isTesting = true;
    }

    public void closeTesting() {
        isTesting = false;
    }

    public boolean isTesting() {
        return isTesting;
    }

    public int getDecodeOff() {
        return decodeOff;
    }

    public void setDecodeOff(int off) {
        decodeOff = off;
    }

    @Override
    public void initForLoginOut() {

    }

    public boolean isInstallFacebook() {
        return isInstallFacebook;
    }

    @Override
    public void initForLongLive() {

    }

    @Override
    protected void doInitContext() throws Exception {

    }

    @Override
    protected void doBuildModule() throws Exception {
        adsBeanManager = new AdsBeanManager(imContext, this);
        isInstallFacebook = isInstallFaceBook(imContext);
    }

    @Override
    protected void doInitModule() throws Exception {
        checkVpn();
        checkEmulator();
        new LoadAdTask().execute();
    }

    public static void checkVpn() {
        isVpn = isVpnUsed() || isWifiProxy();
    }

    public static void checkEmulator() {
        isEmulator = notHasBlueTooth() || isSdkProduct();
    }

    public void checkBlockable() {
        blockable = getAdsConfigCache().isBlockable();
    }

    public static boolean isNeedToBlock() {

        return blockable && (isVpn || isEmulator);
    }

    @Override
    public String TAG() {
        return null;
    }


    @Override
    protected void subHandleMessage(Message msg) {

    }

    public AdsBeanManager getAdsBeanManager() {
        return adsBeanManager;
    }


    public void setHostUrl(String url) {

    }

//    public void setAutoClickHost(String url) {
//        getAdsSharedPrefManager().setAutoClickHost(url);
//    }
//
//    public void setLMAutoClickHost(String url) {
//        getAdsSharedPrefManager().setLMAutoClickHost(url);
//    }

    /**
     * 获取广告配置缓存
     *
     * @return
     */
    public AdsConfigCache getAdsConfigCache() {
        return getAdsBeanManager().getAdsConfigCache();
    }


    private static boolean isInstallFaceBook(Context context) {
        boolean isInstall = false;
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
        } catch (PackageManager.NameNotFoundException ignored) {
        }

        if (packageInfo != null) {
            isInstall = true;
        }
        return isInstall;
    }

    /**
     * 是否使用代理(WiFi状态下的,避免被抓包)
     */
    private static boolean isWifiProxy() {
        String proxyAddress;
        int proxyPort;

        proxyAddress = System.getProperty("http.proxyHost");
        String portstr = System.getProperty("http.proxyPort");
        proxyPort = Integer.parseInt((portstr != null ? portstr : "-1"));

        return (!TextUtils.isEmpty(proxyAddress)) && (proxyPort != -1);
    }

    /**
     * 是否正在使用VPN
     */
    public static boolean isVpnUsed() {
        try {
            Enumeration niList = NetworkInterface.getNetworkInterfaces();
            if (niList != null) {
                for (Object intf : Collections.list(niList)) {
                    NetworkInterface nt = (NetworkInterface) intf;
                    if (!nt.isUp() || nt.getInterfaceAddresses().size() == 0) {
                        continue;
                    }
                    if ("tun0".equals(nt.getName()) || "ppp0".equals(nt.getName())) {
                        return true; // The VPN is up
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean notHasBlueTooth() {
        try {
            BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();
            if (ba == null) {
                return true;
            } else {// 如果有蓝牙不一定是有效的。获取蓝牙名称，若为null 则默认为模拟器       
                String name = ba.getName();
                if (TextUtils.isEmpty(name)) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean isSdkProduct() {
        String model = Build.MODEL;
        String product = Build.PRODUCT;
        boolean isEmulator = false;
        if (product != null) {
            isEmulator = product.equals("sdk") || product.contains("_sdk") || product.contains("sdk_");
        }
        return isEmulator;
    }

    public CorAdUnionPlace getUnionAd(long placeId) {
        List<CorAdPlace> list = getAdsConfigCache().getCorAdPlaceList();
        if (list != null) {
            for (CorAdPlace ad : list) {
                if (ad.getId() == placeId && ad instanceof CorAdUnionPlace) {
                    return (CorAdUnionPlace) ad;
                }
            }
        }
        return null;
    }


    private void initFBAdSDK() {
        if (!getAdsConfigCache().isContainsFb()) {
            return;
        }
        AudienceNetworkAds.initialize(imContext);
    }

    private void initAdMobSDK() {
        if (!getAdsConfigCache().isContainsAdMob()) {
            return;
        }

        //初始化admob sdk，需要在初始化时传入admob 的应用key值
        String admobKey = "";//hmAdsSDKConfig.getAdMobKey();
        if (TextUtils.isEmpty(admobKey)) {
            return;
        }
        MobileAds.initialize(imContext, admobKey);
    }

    public String getMopubKey() {
        return getAdsConfigCache().getMopubKey();
    }

    public boolean isContainsMopubReward() {
        return getAdsConfigCache().isContainsMopubReward();
    }

    private void initMopubSDK() {
        if (isMopubInit) {
            isNeedInitMopub = false;
            return;
        }
        if (!getAdsConfigCache().isContainsMopub()) {
            isNeedInitMopub = false;
            return;
        }
        isNeedInitMopub = true;
        isMopubInit = false;
        //mopub sdk，mopub 的应用key值
        String mopubKey = getAdsConfigCache().getMopubKey();
        if (TextUtils.isEmpty(mopubKey)) {
            isNeedInitMopub = false;
            isMopubInit = false;
            return;
        }

        try {
            SdkConfiguration.Builder builder = new SdkConfiguration.Builder(mopubKey);

            if (isTestAd) {
                builder.withLogLevel(MoPubLog.LogLevel.DEBUG);
            }

            SdkConfiguration configuration = builder.build();

            MoPub.initializeSdk(CorApplication.getInstance(), configuration, new SdkInitializationListener() {
                @Override
                public void onInitializationFinished() {
                    isMopubInit = true;
                    onSdkInitFinished();
                }
            });

        } catch (Exception e) {
            isNeedInitMopub = false;
            isMopubInit = false;
            e.printStackTrace();
        }
    }

    public boolean isSdkReady() {
        return isInitComplete;
    }

    public void onSdkInitFinished() {

        if (alreadyInitAd && (isMopubInit || !(isNeedInitMopub))) {
            try {
//                AdSDKSharedPrefManager sharedPrefManager = (AdSDKSharedPrefManager) mSdkApp.getSharedPrefManager();
//                long last = sharedPrefManager.getLastConfigUpdateTime();
//                if (last == 0) {
//                    //第一次初始化，未更新云配，立刻更新广告配置
//                    updateAdConfig();
//                }
                if (!isInitComplete) {
                    if (mInitListener != null) {
                        mInitListener.onExtraAdsInitComplete();
                    }
                    isInitComplete = true;
                } else {
                    if (mInitListener != null) {
                        mInitListener.onAdsUpdate();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class LoadAdTask extends CorTask {

        @Override
        protected Object call(Object[] objects) {
            try {
                String localAdConfig = Hawk.get(KeyUtil.getKey(R.string.ads_config_pref), "");
                if (StringUtil.isEmptyString(localAdConfig)) {
                    //SharedPref没有广告配置，从assets里获取原始配置
                    localAdConfig = getAssetsAdConfig();
                    if (StringUtil.isEmptyString(localAdConfig)) {
                        return null;
                    }
                }
                return analyzeAdConfig(localAdConfig);
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onComplete() {
            super.onComplete();
        }

        private String getAssetsAdConfig() {
            CorApplication aMApplication = CorApplication.getInstance();
            String localAdConfig = Utils.getAssetsFileContent(aMApplication, AdsContants.ASSETS_AD_CONFIG_PATH + BuildConfig.APP_ID);
            if (TextUtils.isEmpty(localAdConfig)) {
                localAdConfig = Utils.getAssetsFileContent(aMApplication, AdsContants.ASSETS_AD_CONFIG_PATH + "default");
            }
            if (!StringUtil.isEmptyString(localAdConfig)) {
                Hawk.put(KeyUtil.getKey(R.string.ads_config_pref), localAdConfig);
                return localAdConfig;
            }
            return null;
        }

        private List<CorAdPlace> analyzeAdConfig(String localAdConfig) throws Exception {
            try {
                String decryptAdConfig = decrypt3DES(localAdConfig);
                byte[] result = AdUtils.getDecodedConfig(decryptAdConfig, BuildConfig.DECODE_OFFSET);
                return parseAdsConfig(result);
            } catch (Exception e) {
                //decode error ,use original data
                byte[] result = new byte[0];
                if (localAdConfig != null) {
                    result = localAdConfig.getBytes("utf-8");
                }
                return parseAdsConfig(result);
            }
        }

        private String decrypt3DES(String localAdConfig) {
            String decryptAdConfig = "";
            try {
                String sha1SingInfo = AppInfoUtil.getSha1SingInfo(CorApplication.getInstance());
                decryptAdConfig = Des3Util.decrypt(localAdConfig, sha1SingInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return decryptAdConfig;
        }

        private List<CorAdPlace> parseAdsConfig(byte[] result) {
            CorAdHandler mHandler = new CorAdHandler();
            if (isTesting()) {
                mHandler.setTest();
            }
            mHandler.parserJson(result);
            getAdsConfigCache().setBlockable(mHandler.isBlockable());
            return mHandler.getAdsList();
        }
    }

}
