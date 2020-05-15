package corall.adscene.strategy.inner;


import corall.ad.AdsModule;
import corall.base.app.CorApplication;


public class ShareExecutor extends FuncExecutor {

    public ShareExecutor(CorApplication context, AdsModule adModule, String adScene) {
        super(context, adModule, adScene);
    }

    @Override
    protected String getTag() {
        return "ShareExecutor";
    }
}
