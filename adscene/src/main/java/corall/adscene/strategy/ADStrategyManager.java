package corall.adscene.strategy;


import java.util.HashMap;

import corall.ad.AdsModule;
import corall.adscene.EntranceType;
import corall.adscene.strategy.base.AStrategyExecutor;
import corall.adscene.strategy.inner.FuncExecutor;
import corall.adscene.strategy.inner.InnerAdExecutor;
import corall.adscene.strategy.inner.RateExecutor;
import corall.adscene.strategy.inner.ShareExecutor;
import corall.adscene.strategy.outer.AppOuterDeferExecutor;
import corall.adscene.strategy.outer.AppOuterExecutor;
import corall.adscene.strategy.outer.ChargingExecutor;
import corall.adscene.strategy.outer.OuterAdExecutor;
import corall.base.app.CorApplication;

/**
 * desc: 广告位 管理器
 * date: 2017/12/19 15:34
 * author: ancun
 */
public class ADStrategyManager {

    private CorApplication imContext;
    private AdsModule mAdModule;

    private HashMap<String, Object> mExecutorMap;

    public ADStrategyManager(CorApplication context, AdsModule adModule) {
        imContext = context;
        mAdModule = adModule;
        mExecutorMap = new HashMap<>();
    }

    private <T> T getBean(Class<T> clazz, String mark) {
        String key = clazz.getName() + mark;
        Object instance = mExecutorMap.get(key);
        if (instance == null) {
            synchronized (clazz) {
                instance = mExecutorMap.get(key);
                if (instance == null) {
                    instance = this.createBean(clazz, mark);
                    mExecutorMap.put(key, instance);
                }
            }
        }
        return (T) instance;
    }

    private <T> T createBean(Class<T> clazz, String mark) {
        Object obj = null;

        if (clazz == InnerAdExecutor.class) {
            obj = new InnerAdExecutor(imContext, mAdModule, mark);

        } else if (clazz == OuterAdExecutor.class) {
            obj = new OuterAdExecutor(imContext, mAdModule, mark);

        } else if (clazz == AppOuterExecutor.class) {
            obj = new AppOuterExecutor(imContext, mAdModule, mark);

        } else if (clazz == FuncExecutor.class) {
            obj = new FuncExecutor(imContext, mAdModule, mark);

        } else if (clazz == AppOuterDeferExecutor.class) {
            obj = new AppOuterDeferExecutor(imContext, mAdModule, mark);

        }else if (clazz == ChargingExecutor.class) {
            obj = new ChargingExecutor(imContext, mAdModule, mark);

        } else if (clazz == RateExecutor.class) {
            obj = new RateExecutor(imContext, mAdModule, mark);

        } else if (clazz == ShareExecutor.class) {
            obj = new ShareExecutor(imContext, mAdModule, mark);
        }

        return (T) obj;
    }


    public AStrategyExecutor getExecutorByEntranceType(EntranceType entranceType) {
        switch (entranceType) {
            case SPLASH:
            case TRIGGER:
            case VIDEO_BROWSER_SITES:
            case VIDEO_BROWSER_SITES_2:
            case WEB_SITE_PAGE:
            case DOWNLOADING:
            case DOWNLOADED:
            case GAME_PLAY:
            case BACK_HOME:
            case PLAY_DETAIL:
            case SNIFF:
            case SNIFF_GUIDE:
            case DOWNLOAD_INFO:
            case UNLOCK_WEBSITE:
                return getBean(InnerAdExecutor.class, entranceType.getName());

            case APP_INSTALL:
            case APP_UNINSTALL:
                return getBean(OuterAdExecutor.class, entranceType.getName());

            case CHARGING_SCREEN:
                return getBean(ChargingExecutor.class, entranceType.getName());

            case WEATHER_CLOSE:
            case SWIPE_CLOSE:
            case APP_INSTALL_DEFER:
            case APP_UNINSTALL_DEFER:
                return getBean(AppOuterDeferExecutor.class, entranceType.getName());

            case APP_OUTER_AD:
            case BACK_GROUND:
                return getBean(AppOuterExecutor.class, entranceType.getName());

            case FUNC_CHANNEL_CHECK:
            case FUNC_SWIPE:
            case FUNC_WEATHER:
            case FUNC_OFFER:
            case FUNCTION_GIFT:
            case FUNCTION_GIFT_NOTIFY:
                return getBean(FuncExecutor.class, entranceType.getName());
            case FUNC_RATE:
                return getBean(RateExecutor.class, entranceType.getName());
            case FUNC_SHARE:
                return getBean(ShareExecutor.class, entranceType.getName());
            default:
                return null;
        }
    }

}