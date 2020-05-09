package corall.ad.bean.banner;

import android.view.View;

import corall.ad.AdsContants;
import corall.ad.AdsModule;
import corall.ad.bean.RawAd;
import corall.ad.bean.listener.banner.BannerLoadListener;

/**
 * Created by ChenLi on 2017/12/4.
 */

public abstract class RawBannerAd extends RawAd {

    public abstract String getId();

    public abstract void setAdListener(BannerLoadListener listener);

    public void load() {
        if (AdsModule.isNeedToBlock()) {
            return;
        }
        adStatus = AdsContants.RAW_AD_STATUS_LOADING;
    }

    public abstract View getBannerView();

    public abstract boolean isLoading() ;

    public abstract void destroy() ;

    public void pause()  {

    }

    public void resume() {

    }
}
