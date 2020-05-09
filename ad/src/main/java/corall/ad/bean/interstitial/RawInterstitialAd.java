package corall.ad.bean.interstitial;

import android.app.Activity;

import corall.ad.AdsContants;
import corall.ad.AdsModule;
import corall.ad.bean.RawAd;
import corall.ad.bean.listener.interstitialad.InterstitialAdLoadListener;

/**
 * 插屏广告代理基类
 * Created by ChenLi on 2017/12/4.
 */

public abstract class RawInterstitialAd extends RawAd {

    public abstract String getId();

    public abstract void setAdListener(InterstitialAdLoadListener listener);

    public void load(Activity activity)  {
        if (AdsModule.isNeedToBlock()) {
            return;
        }
        adStatus = AdsContants.RAW_AD_STATUS_LOADING;
    }

    public void load()  {
        if (AdsModule.isNeedToBlock()) {
            return;
        }
        adStatus = AdsContants.RAW_AD_STATUS_LOADING;
    }

    public abstract void show() ;

    public abstract boolean isLoaded() ;

    public abstract boolean isLoading() ;

    public void destroy()  {

    }

}
