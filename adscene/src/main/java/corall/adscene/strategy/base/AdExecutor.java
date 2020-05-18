package corall.adscene.strategy.base;

import corall.ad.AdsModule;
import corall.base.app.CorApplication;

public abstract class AdExecutor extends AStrategyExecutor {

    public AdExecutor(CorApplication context, String adScene) {
        super(context, adScene);
    }

    @Override
    protected String getTag() {
        return AdExecutor.class.getName();
    }

    //for extra tactics implement
    protected boolean extraExecutor() {
        return true;
    }
}
