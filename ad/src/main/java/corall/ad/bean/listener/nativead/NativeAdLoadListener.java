package corall.ad.bean.listener.nativead;

import corall.ad.bean.nativead.RawNativeAd;

/**
 * Created by ChenLi on 2017/11/20.
 */

public interface NativeAdLoadListener {

    public void onError(RawNativeAd ad, int code, String msg);

    public void onAdLoaded(RawNativeAd ad);

    public void onClick(RawNativeAd ad);

    public void onInstall(RawNativeAd ad, String packageName);

    public void onLoggingImpression(RawNativeAd ad);

}
