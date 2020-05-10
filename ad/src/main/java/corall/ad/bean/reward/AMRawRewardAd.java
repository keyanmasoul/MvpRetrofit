package corall.ad.bean.reward;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import corall.ad.AdsContants;
import corall.ad.bean.listener.reward.RewardAdListener;
import corall.base.app.CorApplication;

import static corall.ad.AdsContants.RAW_AD_STATUS_DESTROYED;

public class AMRawRewardAd extends RawRewardAd {

    private String id;
    private RewardedVideoAd realRewardAd;

    public AMRawRewardAd(String id) {
        this.id = id;
    }

    // 广告请求监听
    private RewardAdListener mListener;

    @Override
    public String getPlatform() {
        return AdsContants.AD_PLATFORM_ADMOB;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setAdListener(Object listener) {
        this.mListener = (RewardAdListener) listener;
    }

    @Override
    public void show() {
        if (realRewardAd == null) {
            return;
        }
        realRewardAd.show();
    }

    @Override
    public void load() {
        super.load();
        try {
            initRealRewardAd(null);
            AdRequest.Builder builder = new AdRequest.Builder();
            realRewardAd.loadAd(id, builder.build());
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public void load(Activity activity) {
        super.load();
        try {
            initRealRewardAd(activity);
            AdRequest.Builder builder = new AdRequest.Builder();
            realRewardAd.loadAd(id, builder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void destroy() {
        destroy(null);
    }

    public void destroy(Context context) {
        if (realRewardAd != null) {
            realRewardAd.destroy(context);
        }
        realRewardAd = null;
        adStatus = RAW_AD_STATUS_DESTROYED;
    }

    public void resume(Activity activity) {
        if (realRewardAd != null) {
            realRewardAd.resume(activity);
        }
    }

    public void pause(Activity activity) {
        if (realRewardAd != null) {
            realRewardAd.pause(activity);
        }
    }

    @Override
    public boolean isLoaded() {
        if (realRewardAd == null) {
            return false;
        }
        return realRewardAd.isLoaded();
    }

    @Override
    public boolean isLoading() {
        return false;
    }

    private void initRealRewardAd(Activity activity) {
        if (realRewardAd == null) {
            if (activity == null) {
                realRewardAd = MobileAds.getRewardedVideoAdInstance(CorApplication.getInstance());
            } else {
                realRewardAd = MobileAds.getRewardedVideoAdInstance(activity);
            }
            realRewardAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
                @Override
                public void onRewardedVideoAdLoaded() {
                    adStatus = AdsContants.RAW_AD_STATUS_LOADED;
                    if (mListener != null) {
                        mListener.onRewardedAdLoaded(AMRawRewardAd.this);
                    }
                }

                @Override
                public void onRewardedVideoAdOpened() {
                    if (mListener != null) {
                        mListener.onRewardedAdOpened(AMRawRewardAd.this);
                    }
                }

                @Override
                public void onRewardedVideoStarted() {
                    if (mListener != null) {
                        mListener.onRewardedVideoStarted(AMRawRewardAd.this);
                    }
                }

                @Override
                public void onRewardedVideoAdClosed() {
                    if (mListener != null) {
                        mListener.onRewardedAdClosed(AMRawRewardAd.this);
                    }
                }

                @Override
                public void onRewarded(RewardItem rewardItem) {
                    if (mListener != null) {
                        mListener.onRewarded(AMRawRewardAd.this);
                    }
                }

                @Override
                public void onRewardedVideoAdLeftApplication() {
                    if (mListener != null) {
                        mListener.onRewardedAdLeftApplication(AMRawRewardAd.this);
                    }
                }

                @Override
                public void onRewardedVideoAdFailedToLoad(int i) {
                    adStatus = AdsContants.RAW_AD_STATUS_FAIL;
                    if (mListener != null) {
                        mListener.onRewardedAdFailedToLoad(AMRawRewardAd.this, i, "");
                    }
                }

                @Override
                public void onRewardedVideoCompleted() {

                }
            });

        }
    }

}
