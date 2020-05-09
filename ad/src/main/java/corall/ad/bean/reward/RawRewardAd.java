package corall.ad.bean.reward;

import android.app.Activity;
import android.content.Context;

import corall.ad.AdsContants;
import corall.ad.AdsModule;
import corall.ad.bean.RawAd;

public abstract class RawRewardAd extends RawAd {

    public abstract String getId();

    public abstract void setAdListener(Object listener);

    public void load() {
        if (AdsModule.isNeedToBlock()) {
            return;
        }
        adStatus = AdsContants.RAW_AD_STATUS_LOADING;
    }

    public void load(Activity activity) {
        if (AdsModule.isNeedToBlock()) {
            return;
        }
        adStatus = AdsContants.RAW_AD_STATUS_LOADING;
    }

    public abstract void show();

    public abstract boolean isLoaded();

    public abstract boolean isLoading();

    public abstract void destroy(Context context);
}
