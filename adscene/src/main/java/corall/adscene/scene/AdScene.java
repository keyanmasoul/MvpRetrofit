package corall.adscene.scene;

import android.view.View;


import corall.ad.bean.CorAdUnionPlace;
import corall.adscene.EntranceType;
import corall.adscene.strategy.IStrategyExecutor;
import corall.base.app.CorApplication;

/**
 * desc:
 * date: 2018/1/4
 * author: ancun
 */

public abstract class AdScene {

    protected CorApplication imContext;
    protected EntranceType mEntranceType;
    protected boolean isAlwaysPreload;

    public AdScene(CorApplication context, EntranceType entranceType) {
        imContext = context;
        mEntranceType = entranceType;
        isAlwaysPreload = false;
    }

    /**
     * 加载广告
     *
     * @param forceLoad 是否忽略配置检查，强制加载广告
     * @return
     */
    public abstract boolean load(boolean forceLoad);

    /**
     * 释放广告
     */
    public abstract void release();

    /**
     * 获取广告视图（native 、banner）
     *
     * @return
     */
    public abstract View getAdView();

    /**
     * 获取该广告场景的 HmAdUnionPlace
     *
     * @return
     */
    public abstract CorAdUnionPlace getHmAdUnionPlace();

    /**
     * 获取该广告场景的 策略管理者
     *
     * @return
     */
    public abstract IStrategyExecutor getStrategyExecutor();

    /**
     * 检查策略是否通过
     *
     * @return
     */
    public abstract boolean check();

    /**
     * 显示广告 (包含该场景下 一切可以展示的广告)
     */
    public boolean show() {
        return false;
    }

    /**
     * 显示插屏广告
     */
    public void showInterstitial() {

    }

    /**
     * 显示激励视频广告
     */
    public void showReward() {

    }

    public int getPlaceId() {
        return 0;
    }

    public boolean isActive() {
        return false;
    }

    public boolean isWait() {
        return false;
    }

    public abstract boolean isShowed();

    public void startAlwaysPreload() {
        isAlwaysPreload = true;
    }

    public void stopAlwaysPreload() {
        isAlwaysPreload = false;
    }

    public boolean isAlwaysPreload() {
        return isAlwaysPreload;
    }

    public abstract long getLoadedTime();

    public abstract long getShowTime();

}
