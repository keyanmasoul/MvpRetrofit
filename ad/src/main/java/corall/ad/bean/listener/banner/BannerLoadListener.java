package corall.ad.bean.listener.banner;

import corall.ad.bean.banner.RawBannerAd;

public interface BannerLoadListener {

    public void onError(RawBannerAd ad, int code, String msg);

    public void onAdLoaded(RawBannerAd ad);

    public void onClick(RawBannerAd ad);

    public void onInstall(RawBannerAd ad, String packageName);

    public void onLoggingImpression(RawBannerAd ad);
}
