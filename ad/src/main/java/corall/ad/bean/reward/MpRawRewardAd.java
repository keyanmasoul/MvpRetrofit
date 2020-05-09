package corall.ad.bean.reward;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.mopub.common.MoPubReward;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubRewardedVideoListener;
import com.mopub.mobileads.MoPubRewardedVideos;

import corall.ad.AdsContants;
import corall.ad.bean.listener.reward.RewardAdListener;

import java.util.Set;

public class MpRawRewardAd extends RawRewardAd {

    private Object realRewardAd;
    // 广告请求监听
    private RewardAdListener mListener;
    private String id;

    public MpRawRewardAd(String id) {
        this.id = id;
    }

    @Override
    public String getPlatform() {
        return AdsContants.AD_PLATFORM_MOPUB;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setAdListener(Object listener) {
        mListener = (RewardAdListener) listener;
    }

    @Override
    public void show() {
        MoPubRewardedVideos.showRewardedVideo(id);
    }

    @Override
    public boolean isLoaded() {
        return false;
    }

    @Override
    public boolean isLoading() {
        return false;
    }

    @Override
    public void destroy(Context context) {

    }

    private void initRealRewardAd(Activity activity) throws Exception {
//        if (realRewardAd == null) {
//            Class mobileClass = Class.forName(decodeConstant(AdsContants.AM_AD_MOBILE_CLASS_NAME));
//            if (activity == null) {
//                realRewardAd = mobileClass.getMethod("getRewardedVideoAdInstance", Context.class).invoke(null, AMApplication.getInstance());
//            } else {
//                realRewardAd = mobileClass.getMethod("getRewardedVideoAdInstance", Context.class).invoke(null, activity);
//            }
//
//            Class AmRewardAdListener = Class.forName(decodeConstant(AdsContants.AM_AD_REWARD_LISTENER_CLASS_NAME));
//            Method setListenerMethod = realRewardAd.getClass().getDeclaredMethod("setRewardedVideoAdListener", AmRewardAdListener);
//            Object listener = Proxy.newProxyInstance(AmRewardAdListener.getClassLoader(), new Class[]{AmRewardAdListener}, new AMRawRewardAd.AmRewardAdListenerImpl());
//            setListenerMethod.invoke(realRewardAd, listener);
//        }
    }

    @Override
    public void load() {
        super.load();
        try {
            MoPubRewardedVideos.setRewardedVideoListener(new MoPubRewardedVideoListener() {
                @Override
                public void onRewardedVideoLoadSuccess(@NonNull String s) {
                    adStatus = AdsContants.RAW_AD_STATUS_LOADED;

                    if (mListener != null) {
                        mListener.onRewardedAdLoaded(MpRawRewardAd.this);
                    }
                }

                @Override
                public void onRewardedVideoLoadFailure(@NonNull String s, @NonNull MoPubErrorCode moPubErrorCode) {
                    if (mListener != null) {
                        mListener.onRewardedAdFailedToLoad(MpRawRewardAd.this, moPubErrorCode.getIntCode(), moPubErrorCode.toString());
                    }
                }

                @Override
                public void onRewardedVideoStarted(@NonNull String s) {
                    if (mListener != null) {
                        mListener.onRewardedAdOpened(MpRawRewardAd.this);
                    }
                }

                @Override
                public void onRewardedVideoPlaybackError(@NonNull String s, @NonNull MoPubErrorCode moPubErrorCode) {

                }

                @Override
                public void onRewardedVideoClicked(@NonNull String s) {
                    if (mListener != null) {
                        mListener.onRewardedAdLeftApplication(MpRawRewardAd.this);
                    }
                }

                @Override
                public void onRewardedVideoClosed(@NonNull String s) {
                    if (mListener != null) {
                        mListener.onRewardedAdClosed(MpRawRewardAd.this);
                    }
                }

                @Override
                public void onRewardedVideoCompleted(@NonNull Set<String> set, @NonNull MoPubReward moPubReward) {
                    if (mListener != null) {
                        mListener.onRewarded(MpRawRewardAd.this);
                    }
                }
            });

            MoPubRewardedVideos.loadRewardedVideo(id);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void load(Activity activity) {
        load();
    }

}
