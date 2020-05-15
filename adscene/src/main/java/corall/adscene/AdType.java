package corall.adscene;

/**
 * 广告类型
 */
public enum AdType {
    /**
     * 无需区分类型
     */
    ALL(-1, ""),
    /**
     * 未知
     */
    UNKNOWN(0, "unknown"),

    /**
     * DAP native
     */
    DA_NATIVE(1, "dap_native"),

    /**
     * Facebook native
     */
    FB_NATIVE(3, "facebook_native"),

    /**
     * Admob banner
     */
    AM_BANNER(2, "admob_banner"),

    /**
     * Admob native
     */
    AM_NATIVE(4, "admob_native"),

    /**
     * Admob Interstitial
     */
    AM_INTERSTITIAL(5, "admob_interstitial"),

    /**
     * batmobi native
     */
    BAT_NATIVE(6, "bat_native"),

    /**
     * mobpub banner
     */
    MP_BANNER(7, "mopub_banner"),

    /**
     * mobpub Interstitial
     */
    MP_INTERSTITIAL(8, "mopub_interstitial"),

    /**
     * mobpub native
     */
    MP_NATIVE(9, "mopub_native"),

    /**
     * fluct Interstitial
     */
    FT_INTERSTITIAL(10, "fluct_interstitial"),

    /**
     * fluct reward
     */
    FT_REWARD(11, "fluct_reward"),

    /**
     * Admob new banner
     */
    AM_NEW_BANNER(12, "adx_banner"),

    /**
     * Admob new native
     */
    AM_NEW_NATIVE(13, "adx_native"),

    /**
     * Admob new Interstitial
     */
    AM_NEW_INTERSTITIAL(14, "adx_interstitial"),

    /**
     * Admob new reward
     */
    AM_NEW_REWARD(15, "adx_reward"),

    /**
     * Admob reward
     */
    AM_REWARD(16, "admob_rewarded"),

    /**
     * direct Interstitial
     */
    DIRECT_INTERSTITIAL(17, "direct_interstitial"),

    /**
     * web Interstitial
     */
    WEB_INTERSTITIAL(18, "web_interstitial"),

    /**
     * direct banner
     */
    DIRECT_BANNER(19, "direct_banner"),

    /**
     * fb Interstitial
     */
    FB_INTERSTITIAL(20, "fb_interstitial"),

    /**
     * store
     */
    STORE(21, "store"),

    /**
     * Facebook banner
     */
    FB_BANNER(22, "facebook_banner"),

    /**
     * Admob reward
     */
    MP_REWARD(23, "mopub_reward");

    private final int mCode;
    private final String mName;

    AdType(int code, String name) {
        mCode = code;
        mName = name;
    }

    public static AdType enumOf(int value) {
        AdType[] values = AdType.values();
        for (AdType type : values) {
            if (type.getCode() == value) {
                return type;
            }
        }
        return UNKNOWN;
    }

    public int getCode() {
        return mCode;
    }

    public String getName() {
        return mName;
    }
}