package corall.ad;



/**
 * 广告模块常量
 * 自定议常量数值区间9000-10000
 * Created by ChenLi on 2017/11/20.
 */
public class AdsContants {

    // fan原生广告类名
    public static final String FB_AD_NATIVE_CLASS_NAME = "com.facebook.ads.NativeAd";
    // fan插屏广告类名
    public static final String FB_AD_INTERSTITIAL_CLASS_NAME = "com.facebook.ads.InterstitialAd";
    // fan插屏监听类名
    public static final String FB_INTERSTITIAL_AD_LISTENER_CLASS_NAME = "com.facebook.ads.InterstitialAdListener";

    // fan广告监听类名
    public static final String FB_NATIVE_AD_LISTENER_CLASS_NAME = "com.facebook.ads.NativeAdListener";
    // fan广告角标类名
    public static final String FB_AD_CHOICE_VIEW_CLASS_NAME = "com.facebook.ads.AdOptionsView";

    //admob interstitial广告
    public static final String AM_AD_INTERSTITIALAD_CLASS_NAME = "com.google.android.gms.ads.InterstitialAd";


    //admob adquest builder
    public static final String AM_AD_REQUEST_BUILDER_CLASS_NAME = "com.google.android.gms.ads.AdRequest$Builder";

    //admob native unified listener
    public static final String AM_NATIVE_UNIFIED_AD_LISTENER = "com.google.android.gms.ads.formats.UnifiedNativeAd$OnUnifiedNativeAdLoadedListener";


    //admob ad listener
    public static final String AM_AD_LISTENER_CLASS_NAME = "com.google.android.gms.ads.AdListener";

    //admob 高级原生广告loader builder
    public static final String AM_NATIVE_LOADER_BUILDER_CLASS_NAME = "com.google.android.gms.ads.AdLoader$Builder";

    //admob 高级原生广告adOption builder
    public static final String AM_NATIVE_AD_OPTION_BUILDER_CLASS_NAME = "com.google.android.gms.ads.formats.NativeAdOptions$Builder";


    //admob banner广告
    public static final String AM_AD_BANNER_CLASS_NAME = "com.google.android.gms.ads.AdView";

    //facebook banner广告
    public static final String FB_AD_BANNER_CLASS_NAME = "com.facebook.ads.AdView";

    //admob new banner广告
    public static final String AMN_AD_BANNER_CLASS_NAME = "com.google.android.gms.ads.doubleclick.PublisherAdView";

    //admob native unified view
    public static final String AM_AD_NATIVE_UNIFIED_VIEW_CLASS_NAME = "com.google.android.gms.ads.formats.UnifiedNativeAdView";

    //admob native unified
    public static final String AM_AD_NATIVE_UNIFIED_CLASS_NAME = "com.google.android.gms.ads.formats.UnifiedNativeAd";

    //admob banner广告
    public static final String AM_AD_NATIVE_CONTENT_CLASS_NAME = "com.google.android.gms.ads.formats.NativeContentAdView";

    public static final String AM_AD_MOBILE_CLASS_NAME = "com.google.android.gms.ads.MobileAds";

    public static final String AM_AD_REWARD_LISTENER_CLASS_NAME = "com.google.android.gms.ads.reward.RewardedVideoAdListener";

    //mopub banner广告
    public static final String MOPUB_AD_BANNER_CLASS_NAME = "com.mopub.mobileads.MoPubView";
    //mopub banner广告监听类名
    public static final String MOPUB_AD_BANNER_LISTENER_CLASS_NAME = "com.mopub.mobileads.MoPubView$BannerAdListener";

    //mopub interstitial广告
    public static final String MOPUB_AD_INTERSTITIALAD_CLASS_NAME = "com.mopub.mobileads.MoPubInterstitial";
    //mopub interstitial广告监听类名
    public static final String MOPUB_AD_INTERSTITIALAD_LISTENER_CLASS_NAME = "com.mopub.mobileads.MoPubInterstitial$InterstitialAdListener";

    // mopub native
    public static final String MOPUB_AD_NATIVE_CLASS_NAME = "com.mopub.nativeads.MoPubNative";
    public static final String MOPUB_AD_NET_LISTENER_CLASS_NAME = "com.mopub.nativeads.MoPubNative$MoPubNativeNetworkListener";
    public static final String MOPUB_AD_RENDER_LISTENER_CLASS_NAME = "com.mopub.nativeads.NativeAd$MoPubNativeEventListener";

    // 广告云配置SP
    public static final String SHARE_PREF_DU_AD_CONFIG = "a_business_config_";

    // 是否第一次启动
    public static final String SHARE_PREF_AD_FIRST = "a_business_first";

    // 广告云配置灰度结果sp key前缀
    public static final String SHARE_PREF_AD_GRAY = "ad_gr_test_";

    // 本地默认广告云配置地址
    public static final String ASSETS_AD_CONFIG_PATH = "deploy/";

    // 平台类型
//    public static final String AD_PLATFORM_DAP = "da";
    public static final String AD_PLATFORM_FACEBOOK = "fb";
    //    public static final String AD_PLATFORM_MOBJOY = "mj";
    public static final String AD_PLATFORM_APPNEXT = "an";
    public static final String AD_PLATFORM_ADMOB = "am";
//    public static final String AD_PLATFORM_ADMOB_NEW = "am_new";
//    public static final String AD_PLATFORM_FLUCT = "flt";
    public static final String AD_PLATFORM_DIRECT = "direct";
    public static final String AD_PLATFORM_WEB = "web";
    public static final String AD_PLATFORM_MOPUB = "mp";
    //    public static final String AD_PLATFORM_CRYSTALEXPRESS = "ce";
//    public static final String AD_PLATFORM_TB_NETWORK = "tb_network";
//    public static final String AD_PLATFORM_LM_NETWORK = "latent";
//    public static final String AD_PLATFORM_STORE = "store";

    //admob高级原生广告类型
    public static final int ADMOB_NATIVE_INSTALL = 1;
    public static final int ADMOB_NATIVE_CONTENT = 2;
    public static final int ADMOB_NATIVE_UNIFIED = 3;
    public static final int ADMOB_NATIVE_CUSTOM = 4;

    //渠道定义
    public static final String ORGANIC_CHANNEL = "organic";
    public static final String OTHER_CHANNEL = "other";

    //等待
    public static final int RAW_AD_STATUS_WAITING = 6600;
    //加载中
    public static final int RAW_AD_STATUS_LOADING = 6601;
    //填充中（可能已有数据）
    public static final int RAW_AD_STATUS_FILLING = 6602;
    //加载失败
    public static final int RAW_AD_STATUS_FAIL = 6603;
    //已加载
    public static final int RAW_AD_STATUS_LOADED = 9004;

    public static final int RAW_AD_STATUS_DESTROYED = 9006;

    // 读取本地广告配置的注解
    public static final long SERVICE_MARK_LOADADSCONFIG = 5*3600*1000;
    // 获取广告云配置的注解
    public static final long SERVICE_MARK_FETCHCLOUDSADSCONFIG = -3*3600*1000;

    public static final String ADMOB_TEST_BANNER = "ca-app-pub-3940256099942544/6300978111";
    public static final String ADMOB_TEST_INTERSTITIAL = "ca-app-pub-3940256099942544/1033173712";
    public static final String ADMOB_TEST_ADVANCED = "ca-app-pub-3940256099942544/2247696110";
    public static final String ADMOB_TEST_REWARD = "ca-app-pub-3940256099942544/5224354917";

//    public static final String ADMOB_NEW_TEST_BANNER = "/6499/example/banner";
//    public static final String ADMOB_NEW_TEST_INTERSTITIAL = "/6499/example/interstitial";
//    public static final String ADMOB_NEW_TEST_ADVANCED = "/6499/example/native";
//    public static final String ADMOB_NEW_TEST_REWARD = "/6499/example/rewarded-video";

    public static final String MOPUB_TEST_BANNER = "252412d5e9364a05ab77d9396346d73d";
    public static final String MOPUB_TEST_INTERSTITIAL = "24534e1901884e398f1253216226017e";
    public static final String MOPUB_TEST_NATIVE = "11a17b188668469fb0412708c3d16813";

    public static final String FB_TEST_NATIVE = "VID_HD_16_9_15S_APP_INSTALL";
    public static final String FB_TEST_INTERSTITIAL = "VID_HD_16_9_15S_APP_INSTALL";
    public static final String FB_TEST_BANNER = "IMG_16_9_APP_INSTALL";
    public static final String FB_TEST_REWARD = "YOUR_PLACEMENT_ID";
}
