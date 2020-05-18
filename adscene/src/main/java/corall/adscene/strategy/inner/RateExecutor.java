package corall.adscene.strategy.inner;

import corall.ad.AdsModule;
import corall.base.app.CorApplication;

/**
 * 为某些功能指定的可配置的策略，但不是广告功能，而是利用广告功能上的配置
 */
public class RateExecutor extends FuncExecutor {

    public RateExecutor(CorApplication context, String adScene) {
        super(context, adScene);
    }

    @Override
    protected String getTag() {
        return "RateExecutor";
    }
}
