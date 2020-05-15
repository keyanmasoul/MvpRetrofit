package corall.adscene.strategy.outer;


import corall.ad.AdsModule;
import corall.adscene.strategy.base.AdExecutor;
import corall.base.app.CorApplication;


/**
 * desc:
 * date: 2018/6/14
 * author: ancun
 */

public class OuterAdExecutor extends AdExecutor {

    public OuterAdExecutor(CorApplication context, AdsModule adModule, String adScene) {
        super(context, adModule, adScene);
    }
}
