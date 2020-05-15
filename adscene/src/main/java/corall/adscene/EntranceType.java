package corall.adscene;

import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;

import corall.ad.bean.CorAdUnionPlace;
import corall.adscene.scene.AdScene;
import corall.adscene.scene.AdStrategyScene;
import corall.adscene.scene.func.FuncInterstitialScene;
import corall.adscene.scene.func.FunctionScene;
import corall.adscene.scene.inner.CommonInterstitialScene;
import corall.adscene.scene.inner.CommonScene;
import corall.adscene.scene.inner.UnlockVideoRewardScene;
import corall.adscene.scene.outer.AppOuterDeferScene;
import corall.adscene.scene.outer.AutoClickScene;
import corall.adscene.scene.outer.BackgroundScene;
import corall.adscene.scene.outer.CommonInterstitialOutScene;
import corall.base.app.CorApplication;

import static corall.adscene.ADConstant.HMPlaceId.APP_INSTALL_DEFER_UNION_ID;
import static corall.adscene.ADConstant.HMPlaceId.APP_INSTALL_UNION_ID;
import static corall.adscene.ADConstant.HMPlaceId.APP_OUTER_AD_UNION_ID_1;
import static corall.adscene.ADConstant.HMPlaceId.APP_UNINSTALL_DEFER_UNION_ID;
import static corall.adscene.ADConstant.HMPlaceId.APP_UNINSTALL_UNION_ID;
import static corall.adscene.ADConstant.HMPlaceId.AUTO_CLICK_ID;
import static corall.adscene.ADConstant.HMPlaceId.BACK_HOME_ID;
import static corall.adscene.ADConstant.HMPlaceId.CHARGING_SCREEN_UNION_ID;
import static corall.adscene.ADConstant.HMPlaceId.DOWNLOADED_UNION_ID;
import static corall.adscene.ADConstant.HMPlaceId.DOWNLOADING_UNION_ID;
import static corall.adscene.ADConstant.HMPlaceId.DOWNLOAD_INFO_ID;
import static corall.adscene.ADConstant.HMPlaceId.FUNC_CHANNEL_CHECK_ID;
import static corall.adscene.ADConstant.HMPlaceId.FUNC_FUNC_OFFER_ID;
import static corall.adscene.ADConstant.HMPlaceId.FUNC_GIFT_ID;
import static corall.adscene.ADConstant.HMPlaceId.FUNC_GIFT_NOTIFY_ID;
import static corall.adscene.ADConstant.HMPlaceId.FUNC_RATE_ID;
import static corall.adscene.ADConstant.HMPlaceId.FUNC_SHARE_ID;
import static corall.adscene.ADConstant.HMPlaceId.FUNC_SHORTCUT_ID;
import static corall.adscene.ADConstant.HMPlaceId.FUNC_SWIPE_ANDROID_0_ID;
import static corall.adscene.ADConstant.HMPlaceId.FUNC_SWIPE_ID;
import static corall.adscene.ADConstant.HMPlaceId.FUNC_WEATHER_ID;
import static corall.adscene.ADConstant.HMPlaceId.GAME_PLAY_UNION_ID;
import static corall.adscene.ADConstant.HMPlaceId.LEFT_SWIPE_WEB_UNION_ID;
import static corall.adscene.ADConstant.HMPlaceId.MOVIE_BOX_UNION_ID;
import static corall.adscene.ADConstant.HMPlaceId.PLAY_VIDEO_PAGE_ID;
import static corall.adscene.ADConstant.HMPlaceId.PLAY_VIDEO_UNION_ID;
import static corall.adscene.ADConstant.HMPlaceId.ROULETTE_UNION_ID;
import static corall.adscene.ADConstant.HMPlaceId.SITES_LIST_2_UNION_ID;
import static corall.adscene.ADConstant.HMPlaceId.SITES_LIST_UNION_ID;
import static corall.adscene.ADConstant.HMPlaceId.SNIFF_GUIDE_UNION_ID;
import static corall.adscene.ADConstant.HMPlaceId.SNIFF_UNION_ID;
import static corall.adscene.ADConstant.HMPlaceId.SPINNING_REWARD_ID;
import static corall.adscene.ADConstant.HMPlaceId.SPLASH_UNION_ID;
import static corall.adscene.ADConstant.HMPlaceId.SWIPE_CLOSE_UNION_ID;
import static corall.adscene.ADConstant.HMPlaceId.SWIPE_WEB_UNION_ID1;
import static corall.adscene.ADConstant.HMPlaceId.SWIPE_WEB_UNION_ID2;
import static corall.adscene.ADConstant.HMPlaceId.TRIGGER_UNION_ID;
import static corall.adscene.ADConstant.HMPlaceId.UNLOCK_WEBSITE_ID;
import static corall.adscene.ADConstant.HMPlaceId.WEATHER_CLOSE_UNION_ID;
import static corall.adscene.ADConstant.HMPlaceId.WEB_SITE_PAGE_UNION_ID;


/**
 * 广告(功能，入口)展示场景
 */
public enum EntranceType {

    /**
     * 未知或未定义
     */
    UNKNOWN("unknown", null, Category.F),
    /**
     * 获取渠道
     */
    FUNC_CHANNEL_CHECK("func_check", FunctionScene.class, FUNC_CHANNEL_CHECK_ID, Category.F),
    /**
     * 快滑
     */
    FUNC_SWIPE("func_swipe", FunctionScene.class, FUNC_SWIPE_ID, Category.F),

    /**
     * 天气
     */
    FUNC_WEATHER("func_weather", FunctionScene.class, FUNC_WEATHER_ID, Category.F),
    /**
     * 五星好评功能 配置
     */
    FUNC_RATE("function_rate", FunctionScene.class, FUNC_RATE_ID, Category.F),
    /**
     * 订阅向导 配置
     */
    FUNC_OFFER("function_offer", FunctionScene.class, FUNC_FUNC_OFFER_ID, Category.F),
    /**
     * 壁纸礼物功能 配置
     */
    FUNCTION_GIFT("function_gift", FunctionScene.class, FUNC_GIFT_ID, Category.F),
    /**
     * 壁纸礼物功能通知 配置
     */
    FUNCTION_GIFT_NOTIFY("function_gift_notify", FunctionScene.class, FUNC_GIFT_NOTIFY_ID, Category.F),
    /**
     * 分享功能 配置
     */
    FUNC_SHARE("function_share", FunctionScene.class, FUNC_SHARE_ID, Category.F),
    /////////////////////////////// 以上是功能配置 : 代号 F 区 ///////////////////////////////
    /**
     * 闪屏页
     */
    SPLASH("splash", FuncInterstitialScene.class, SPLASH_UNION_ID, Category.A),

    /**
     * Trigger广告
     */
    TRIGGER("trigger", FuncInterstitialScene.class, TRIGGER_UNION_ID, Category.A),
    /**
     * 视频网址列表广告
     */
    VIDEO_BROWSER_SITES("video_sites", CommonScene.class, SITES_LIST_UNION_ID, Category.A),
    /**
     * 视频网址列表广告
     */
    VIDEO_BROWSER_SITES_2("video_sites_2", CommonScene.class, SITES_LIST_2_UNION_ID, Category.A),
    /**
     * 视频网址详情
     */
    WEB_SITE_PAGE("web_site_page", FuncInterstitialScene.class, WEB_SITE_PAGE_UNION_ID, Category.A),
    /**
     * 正在下载广告
     */
    DOWNLOADING("downloading", CommonScene.class, DOWNLOADING_UNION_ID, Category.A),
    /**
     * 已下载广告(合并到600005)
     */
    DOWNLOADED("downloaded", CommonScene.class, DOWNLOADED_UNION_ID, Category.A),
    /**
     * 游戏广告
     */
    GAME_PLAY("game_play", FuncInterstitialScene.class, GAME_PLAY_UNION_ID, Category.A),
    /**
     * 解锁网站
     */
    UNLOCK_WEBSITE("unlock_website", UnlockVideoRewardScene.class, UNLOCK_WEBSITE_ID, Category.A),
    /**
     * 下载信息
     */
    DOWNLOAD_INFO("download_info", CommonScene.class, DOWNLOAD_INFO_ID, Category.A),
    /**
     * 返回主页 广告（目前只出现在：点击下载完成通知）
     */
    BACK_HOME("back_home", FuncInterstitialScene.class, BACK_HOME_ID, Category.A),
    /**
     * 视频播放banner
     */
    PLAY_DETAIL("play_detail", CommonScene.class, PLAY_VIDEO_PAGE_ID, Category.A),

    /**
     * 嗅控广告
     */
    SNIFF("sniff", CommonScene.class, SNIFF_UNION_ID, Category.A),

    /**
     * 嗅控教程
     */
    SNIFF_GUIDE("sniff_guide", CommonScene.class, SNIFF_GUIDE_UNION_ID, Category.A),

    /////////////////////////////// 以上是应用内场景的广告  : 代号 A 区  ///////////////////////////////

    /**
     * 安装应用
     */
    APP_INSTALL("app_install", CommonScene.class, APP_INSTALL_UNION_ID, Category.B),

    /**
     * 卸载应用
     */
    APP_UNINSTALL("app_uninstall", CommonScene.class, APP_UNINSTALL_UNION_ID, Category.B),

    /**
     * 充电锁屏
     */
    CHARGING_SCREEN("charging_screen", CommonScene.class, CHARGING_SCREEN_UNION_ID, Category.B),

    /**
     * 快划的web广告
     */
    SWIPE_WEB1("swipe_web1", CommonInterstitialScene.class, SWIPE_WEB_UNION_ID1, Category.B),
    SWIPE_WEB2("swipe_web2", CommonInterstitialScene.class, SWIPE_WEB_UNION_ID2, Category.B),

    LEFT_SWIPE_WEB("left_swipe_web", CommonInterstitialScene.class, LEFT_SWIPE_WEB_UNION_ID, Category.B),
    /////////////////////////////// 以上是应用外场景的广告，但有内容或功能载体  : 代号 B 区   ///////////////////////////////


    /**
     * 天气界面关闭
     */
    WEATHER_CLOSE("weather_close", CommonInterstitialOutScene.class, WEATHER_CLOSE_UNION_ID, Category.C),

    /**
     * 快划菜单关闭
     */
    SWIPE_CLOSE("swipe_close", CommonInterstitialOutScene.class, SWIPE_CLOSE_UNION_ID, Category.C),

    /**
     * 安装应用 (二次延时广告)
     */
    APP_INSTALL_DEFER("app_install_defer", AppOuterDeferScene.class, APP_INSTALL_DEFER_UNION_ID, Category.C),

    /**
     * 卸载应用 (二次延时广告)
     */
    APP_UNINSTALL_DEFER("app_uninstall_defer", AppOuterDeferScene.class, APP_UNINSTALL_DEFER_UNION_ID, Category.C),

    /**
     * 应用外广告
     */
    APP_OUTER_AD("app_outer", CommonInterstitialOutScene.class, APP_OUTER_AD_UNION_ID_1, Category.C),

    /////////////////////////////// 以上是应用外场景的广告，没有内容或功能载体，只为弹广告而弹广告  : 代号 C 区  ///////////////////////////////

    /**
     * 重叠广告，该广告place id可变
     */
    BACK_GROUND("back_ground", BackgroundScene.class, Category.D);

    /////////////////////////////// 特殊业务  : 代号 S 区  ///////////////////////////////
    private final String mName;
    private final Category mCategory;
    private String mSource;
    private int mPid;

    private Class<? extends AdScene> mAdSceneClass;
    private AdScene mAdSceneInstance;

    EntranceType(String name, Class<? extends AdScene> adSceneClass, Category category) {
        this.mName = name;
        this.mPid = 0;
        this.mAdSceneClass = adSceneClass;
        this.mCategory = category;
    }

    EntranceType(String name, Class<? extends AdScene> adSceneClass, int pid, Category category) {
        this.mName = name;
        this.mPid = pid;
        this.mAdSceneClass = adSceneClass;
        this.mCategory = category;
    }

    @Nullable
    public static EntranceType enumOf(String name) {
        if (TextUtils.isEmpty(name)) {
            return null;
        }

        EntranceType[] values = EntranceType.values();
        for (EntranceType value : values) {
            if (value.getName().equals(name)) {
                return value;
            }
        }

        return null;
    }

    public Category getCategory() {
        return mCategory;
    }

    public String getName() {
        return mName;
    }

    public int getPid() {
        return mPid;
    }

    public void setPid(int pid) {
        mPid = pid;
    }

    public String getSource() {
        return mSource;
    }

    public void setSource(String source) {
        mSource = source;
    }

    public AdScene getAdScene() {
        if (mAdSceneClass == null) {
            throw new NullPointerException("Scene Class in Entrance can not be null");
        }

        if (mAdSceneInstance != null) {
            return mAdSceneInstance;
        }

        mAdSceneInstance = createAdScene(mAdSceneClass);

        if (mAdSceneInstance == null) {
            throw new IllegalStateException("Unable to init Scene in Entrance : " + mAdSceneClass.toString());
        }

        if (mPid == 0) {
            mPid = mAdSceneInstance.getPlaceId();
        }

        return mAdSceneInstance;
    }

    /**
     * 为什么不用反射生成对象，因为反射需要 keep 构造方法
     *
     * @param adSceneClass
     * @return
     */
    private AdScene createAdScene(Class<? extends AdScene> adSceneClass) {
        CorApplication imContext = CorApplication.getInstance();
        AdScene adScene = null;

        if (adSceneClass == FunctionScene.class) {
            adScene = new FunctionScene(imContext, this);

        } else if (adSceneClass == CommonInterstitialScene.class) {
            adScene = new CommonInterstitialScene(imContext, this);

        } else if (adSceneClass == CommonScene.class) {
            adScene = new CommonScene(imContext, this);

        } else if (adSceneClass == FuncInterstitialScene.class) {
            adScene = new FuncInterstitialScene(imContext, this);

        } else if (adSceneClass == AppOuterDeferScene.class) {
            adScene = new AppOuterDeferScene(imContext, this);

        } else if (adSceneClass == BackgroundScene.class) {
            adScene = new BackgroundScene(imContext, this);

        } else if (adSceneClass == CommonInterstitialOutScene.class) {
            adScene = new CommonInterstitialOutScene(imContext, this);

        } else if (adSceneClass == UnlockVideoRewardScene.class) {
            adScene = new UnlockVideoRewardScene(imContext, this);
        }

        return adScene;
    }


    /**
     * 加载广告
     *
     * @return 是有有加载广告
     */
    public boolean load() {
        return load(false, true);
    }

    /**
     * 加载广告
     *
     * @param force 可选是否强制加载，跳过遵循策略判断
     * @return 是有有加载广告
     */
    public boolean load(boolean force) {
        return load(force, true);
    }

    /**
     * 加载广告
     *
     * @param forceLoad  可选是否强制加载，跳过遵循策略判断
     * @param isSubCheck 是否略过订阅检测,true 不掠过
     * @return 是有有加载广告
     */
    public boolean load(boolean forceLoad, boolean isSubCheck) {
//        BillsModule billsModule = (BillsModule) AMApplication.getInstance().getSubModule(BillsModule.MODULE_KEY);
//        if (billsModule == null) {
//            return false;
//        }
//
//        //已订阅
//        if (billsModule.isSubscription() && isSubCheck) {
//            return false;
//        }

        try {
            AdScene scene = getAdScene();
            if (scene != null) {
                return scene.load(forceLoad);
            } else {
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean show() {
        return getAdScene().show();
    }

    public void release() {
        getAdScene().release();
    }

    /**
     * 获取 EntranceType 加载的广告view
     *
     * @return
     */
    public View getAdView() {
        return getAdScene().getAdView();
    }

    public AdStrategyScene getStrategyScene() {
        return (AdStrategyScene) getAdScene();
    }

    public boolean isActive() {
        return getAdScene().isActive();
    }

    public boolean isShowed() {
        return getAdScene().isShowed();
    }

    public boolean getSwitch() {
        CorAdUnionPlace adPlace = getAdScene().getHmAdUnionPlace();
        if (adPlace == null) {
            return false;
        }

        return adPlace.getSwitch();
    }
}
