package corall.ad.bean.listener.interstitialad;


import corall.ad.bean.interstitial.RawInterstitialAd;

/**
 * Created by ChenLi on 2017/12/5.
 */

public interface InterstitialAdLoadListener {
    public void onError(RawInterstitialAd ad, int code, String msg);

    public void onAdLoaded(RawInterstitialAd ad);

    public void onShow(RawInterstitialAd ad);

    public void onClick(RawInterstitialAd ad);

    public void onClose(RawInterstitialAd ad);


}
