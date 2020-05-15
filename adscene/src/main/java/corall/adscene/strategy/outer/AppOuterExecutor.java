package corall.adscene.strategy.outer;


import corall.ad.AdsModule;
import corall.base.app.CorApplication;

/**
 * desc:
 * date: 2018/4/19
 * author: ancun
 */

public class AppOuterExecutor extends OuterAdExecutor {

    public AppOuterExecutor(CorApplication context, AdsModule adModule, String adScene) {
        super(context, adModule, adScene);
    }

    @Override
    protected String getTag() {
        return AppOuterExecutor.class.getName();
    }

    @Override
    protected void onCountLimited() {
        super.onCountLimited();
    }

}
