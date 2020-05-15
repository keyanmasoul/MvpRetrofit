package corall.adscene.scene.func;


import corall.ad.bean.CorAdUnionPlace;
import corall.ad.ui.card.CommonNativeAdCardView;
import corall.adscene.AdType;
import corall.adscene.EntranceType;
import corall.adscene.scene.AdStrategyScene;
import corall.adscene.strategy.IStrategyExecutor;
import corall.base.app.CorApplication;

public class FunctionScene extends AdStrategyScene {

    public FunctionScene(CorApplication context, EntranceType entranceType) {
        super(context, entranceType);
    }

    @Override
    protected CommonNativeAdCardView initCardView() {
        return null;
    }

    @Override
    protected void onADLoaded(AdType adType) {

    }

    @Override
    public void onADClick(AdType adType) {

    }

    @Override
    public void onADShow(AdType adType) {

    }

    @Override
    public void onAdClose(AdType adType) {

    }

    @Override
    protected void onADLoad(AdType adType) {

    }

    @Override
    protected void onADFail(AdType adType, int code) {

    }

    @Override
    protected void onRewarded() {

    }

    @Override
    protected void onRewardedVideoStarted() {

    }

    @Override
    protected void onADFilled(AdType adType) {

    }

    @Override
    public boolean load(boolean forceLoad) {
        CorAdUnionPlace adUnionPlace = mEntranceType.getAdScene().getHmAdUnionPlace();
        if (adUnionPlace == null) {
            return false;
        }

        IStrategyExecutor executor = mEntranceType.getAdScene().getStrategyExecutor();
        if (executor == null) {
            return false;
        }

        return forceLoad || executor.check(adUnionPlace);

    }

    @Override
    public boolean isAlwaysPreload() {
        return false;
    }
}
