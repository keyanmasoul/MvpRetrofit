package corall.adscene.strategy.inner;


import corall.ad.AdsModule;
import corall.adscene.strategy.base.AStrategyExecutor;
import corall.base.app.CorApplication;

/**
 * 为某些功能指定的可配置的策略，但不是广告功能，而是利用广告功能上的配置
 */
public class FuncExecutor extends AStrategyExecutor {

    public FuncExecutor(CorApplication context, String adScene) {
        super(context, adScene);
    }

    @Override
    protected String getTag() {
        if (mAdScene != null) {
            return "FuncExecutor=>" + mAdScene;
        }
        return "FuncExecutor";
    }

    @Override
    protected boolean ignoreNetWork() {
        return true;
    }

    @Override
    protected boolean ignoreScreenOff() {
        return true;
    }
}
