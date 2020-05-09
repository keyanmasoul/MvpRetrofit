package corall.ad.bean.listener.reward;

import corall.ad.bean.reward.RawRewardAd;

public interface RewardAdListener {
    void onRewardedAdLoaded(RawRewardAd ad);

    void onRewardedAdOpened(RawRewardAd ad);

    void onRewardedVideoStarted(RawRewardAd ad);

    void onRewardedAdClosed(RawRewardAd ad);

    void onRewarded(RawRewardAd ad);

    void onRewardedAdLeftApplication(RawRewardAd ad);

    void onRewardedAdFailedToLoad(RawRewardAd ad, int code, String msg);
}
