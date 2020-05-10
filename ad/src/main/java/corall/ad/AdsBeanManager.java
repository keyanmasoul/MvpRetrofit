package corall.ad;

import corall.ad.cache.AdsConfigCache;
import corall.base.app.CorApplication;
import corall.base.app.MobBeanManager;

/**
 * Created by ChenLi on 2017/11/20.
 */

public class AdsBeanManager extends MobBeanManager {

    private static final boolean DEBUG = false;

    private AdsModule adsModule;

    public AdsBeanManager(CorApplication imContext, AdsModule adsModule) {
        super(imContext);
        this.adsModule = adsModule;
    }

    @Override
    protected <T> T createBean(Class<T> clazz, String mark) {
        Object obj = null;
        if (clazz == AdsConfigCache.class) {
            obj = new AdsConfigCache();

        } else {
            obj = super.createBean(clazz, mark);
        }
        return (T) obj;
    }



    /**
     * 获取广告配置缓存
     *
     * @return
     */
    public AdsConfigCache getAdsConfigCache() {
        return getBean(AdsConfigCache.class);
    }



}
