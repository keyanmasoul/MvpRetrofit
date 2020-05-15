package corall.adscene;

/**
 * desc:
 * date: 2017/12/19 15:37
 * author: ancun
 */
public class ADConstant {

    /**
     * 无效的ID
     */
    public static final int INVALID_ID = -49;


    /**
     * 用于记录广告配置的 sp key
     */
    public interface AdSharedPrefKey {

        //应用外广告开关（该广告与实时保护弹窗广告互斥）
        String KEY_APP_OUTER_AD = "key_app_outer_ad";

        //快滑（功能）广告
        String KEY_SWIPE_AD = "key_swipe_ad";

        //快滑（引导）
        String KEY_SWIPE_GUIDE = "key_swipe_guide";

        //天气（引导）
        String KEY_WEATHER_GUIDE = "key_weather_guide";

        //各个策略执行者 SharedPref key 的后缀 （由广告场景名称 + 后缀 拼接而成）
        String SHOW_TIME = "_show_time";
        String REQUEST_COUNT = "_request_count";
        String SHOW_COUNT = "_show_count";
        String BEGIN_TIME = "_begin_time";

        /**
         * 收到的 referrer 值
         **/
        String KEY_REFERRER_VALUE = "key_referrer_value";
        String KEY_REFERRER_VALUE_GA = "key_referrer_value_ga";

        /**
         * 收到的 referrer 的time
         **/
        String KEY_REFERRER_TIME = "key_referrer_time";
        String KEY_CHARGING_AD = "key_charging_ad";

        String KEY_WEB_SHORTCUT = "key_web_shortcut";
        String KEY_CAMPAIGN_INSTALL = "key_campaign_install";
//        String KEY_APP_SHORTCUT_SUCCESS = "key_a_s_s";
    }

    /**
     * HM SDK place ID
     */
    public interface HMPlaceId {
        /**
         * 验证渠道的ID
         */
        int FUNC_CHANNEL_CHECK_ID = 400001;
        /**
         * 快捷方式
         */
        int FUNC_SHORTCUT_ID = 400002;
        /**
         * 快滑功能配置
         */
        int FUNC_SWIPE_ID = 400003;
        /**
         * 天气功能配置
         */
        int FUNC_WEATHER_ID = 400004;
        /**
         * 五星好评
         */
        int FUNC_RATE_ID = 400005;
        /**
         * 快滑功能配置 Android 0 配置
         */
        int FUNC_SWIPE_ANDROID_0_ID = 400006;
        /**
         * 订阅向导
         */
        int FUNC_FUNC_OFFER_ID = 400007;
        /**
         * 解锁视频配置
         */
        int FUNC_GIFT_ID = 400008;
        /**
         * 解锁视频通知配置
         */
        int FUNC_GIFT_NOTIFY_ID = 400009;
        /**
         * 分享
         */
        int FUNC_SHARE_ID = 400010;

        /////////////////////////////// 功能和广告做个区分 4打头是功能的配置  ///////////////////////////////

        /**
         * 闪屏页
         */
        int SPLASH_UNION_ID = 600001;
        /**
         * trigger
         */
        int TRIGGER_UNION_ID = 600002;
        /**
         * 视频网址列表广告
         */
        int SITES_LIST_UNION_ID = 600003;
        /**
         * 视频网址详情
         */
        int WEB_SITE_PAGE_UNION_ID = 600004;
        /**
         * 正在下载广告
         */
        int DOWNLOADING_UNION_ID = 600005;
        /**
         * 已下载广告
         */
        int DOWNLOADED_UNION_ID = 600006;
        /**
         * 播放视频广告
         */
        int PLAY_VIDEO_UNION_ID = 600007;
        /**
         * 游戏广告
         */
        int GAME_PLAY_UNION_ID = 600009;
        /**
         * 解锁网站激励适配
         */
        int UNLOCK_WEBSITE_ID = 600011;
        /**
         * 下载弹出插屏广告
         */
        int DOWNLOAD_INFO_ID = 600012;
        /**
         * 列表页返回插屏
         */
        int BACK_HOME_ID = 600013;
        /**
         * 视频播放banner
         */
        int PLAY_VIDEO_PAGE_ID = 600014;
        /**
         * 转盘抽奖
         */
        int SPINNING_REWARD_ID = 600015;
        /**
         * 转盘网页
         */
        int ROULETTE_UNION_ID = 600016;
        /**
         * 电影盒子
         */
        int MOVIE_BOX_UNION_ID = 600017;
        /**
         * 视频网址列表广告2
         */
        int SITES_LIST_2_UNION_ID = 600018;
        /**
         * 嗅控广告
         */
        int SNIFF_UNION_ID = 600019;

        /**
         * 嗅控教程
         */
        int SNIFF_GUIDE_UNION_ID = 600020;

        /////////////////////////////// 应用内外做个区分 6打头是应用内的  ///////////////////////////////

        /**
         * 新装应用广告
         */
        int APP_INSTALL_UNION_ID = 700004;

        /**
         * 卸载应用广告
         */
        int APP_UNINSTALL_UNION_ID = 700005;
        /**
         * 充电广告
         */
        int CHARGING_SCREEN_UNION_ID = 700006;
        /**
         * Swipe web game广告1
         */
        int SWIPE_WEB_UNION_ID1 = 700008;

        /**
         * Swipe web game广告2
         */
        int SWIPE_WEB_UNION_ID2 = 700009;

        /**
         * 左Swipe web game广告
         */
        int LEFT_SWIPE_WEB_UNION_ID = 700011;

        /////////////////////////////// 应用内外做个区分 7 打头是应用外的广告，但是带有场景  ///////////////////////////////

        /**
         * 应用外全屏广告_1
         */
        int APP_OUTER_AD_UNION_ID_1 = 800001;

        /**
         * 新装应用广告 (二次延时广告)
         */
        int APP_INSTALL_DEFER_UNION_ID = 800004;

        /**
         * 卸载应用广告 (二次延时广告)
         */
        int APP_UNINSTALL_DEFER_UNION_ID = 800005;

        /**
         * 快划菜单关闭广告
         */
        int SWIPE_CLOSE_UNION_ID = 800006;

        /**
         * 天气界面关闭广告
         */
        int WEATHER_CLOSE_UNION_ID = 800007;

        /////////////////////////////// 应用内外做个区分 6打头是应用内的，8打头是应用外的，无场景  ///////////////////////////////

        /**
         * 订阅广告
         */
        int AUTO_CLICK_ID = 900001;

    }

    public interface ErrorCode {
        int SELF_APP_RUNNING = 10000;
        int NO_PERMISSION = 10001;
    }

    public interface TaskMark {
        /**
         * 上报referrer
         */
        long SERVICE_MARK_SUBMIT_REFERRER = 1511860031;
    }

}
