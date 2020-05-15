package corall.adscene.scene.outer;

import android.view.View;

import com.uber.adsbuisiness.bean.HmAdUnionPlace;
import com.uber.android.mob.AMApplication;

import cr.s.a.EntranceType;
import cr.s.a.adscene.AdScene;
import cr.s.a.strategy.IStrategyExecutor;

/**
 * desc:
 * date: 2018/12/7
 * author: ancun
 */
public class AutoClickScene extends AdScene {

    public AutoClickScene(AMApplication context, EntranceType entranceType) {
        super(context, entranceType);
    }

    @Override
    public boolean load(boolean forceLoad) {
        return false;
    }

    @Override
    public void release() {

    }

    @Override
    public View getAdView() {
        return null;
    }

    @Override
    public HmAdUnionPlace getHmAdUnionPlace() {
        return null;
    }

    @Override
    public IStrategyExecutor getStrategyExecutor() {
        return null;
    }

    @Override
    public boolean check() {
        return false;
    }

    @Override
    public boolean isShowed() {
        return false;
    }

    @Override
    public long getLoadedTime() {
        return 0;
    }

    @Override
    public long getShowTime() {
        return 0;
    }
}
