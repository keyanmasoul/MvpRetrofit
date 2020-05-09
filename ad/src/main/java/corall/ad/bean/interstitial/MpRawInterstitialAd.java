package corall.ad.bean.interstitial;

import android.app.Activity;
import android.text.TextUtils;

import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;

import corall.ad.AdsContants;
import corall.ad.bean.listener.interstitialad.InterstitialAdLoadListener;
import corall.base.app.AMApplication;

import static corall.ad.AdsContants.RAW_AD_STATUS_DESTROYED;

public class MpRawInterstitialAd extends RawInterstitialAd {

    private String id;
    private String keyword;
    private String keywordField;
    private MoPubInterstitial realInterstitialAd;

    // 广告请求监听
    private InterstitialAdLoadListener mListener;

    public MpRawInterstitialAd(String id) {
        this.id = id;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setKeywordField(String keywordField) {
        this.keywordField = keywordField;
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
    public void setAdListener(InterstitialAdLoadListener listener) {
        mListener = listener;
    }

    @Override
    public void show()  {
        if (realInterstitialAd == null) {
            return;
        }
        realInterstitialAd.show();

    }

    @Override
    public void destroy()  {
        if (realInterstitialAd == null) {
            return;
        }
        realInterstitialAd.destroy();
        realInterstitialAd = null;
        mListener = null;
        adStatus = RAW_AD_STATUS_DESTROYED;
    }

    @Override
    public void load()  {
        super.load();
        try {
            initRealIntersititialAd(null);
            realInterstitialAd.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void load(Activity activity)  {
        super.load();
        try {
            initRealIntersititialAd(activity);
            realInterstitialAd.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isLoaded()  {
        return realInterstitialAd != null && adStatus == AdsContants.RAW_AD_STATUS_LOADED;
    }

    @Override
    public boolean isLoading()  {
        return false;
    }

    private void initRealIntersititialAd(Activity activity) throws Exception {
        if (realInterstitialAd == null) {
            if (activity == null) {
                realInterstitialAd = new MoPubInterstitial(AMApplication.getInstance(), id);
            } else {
                realInterstitialAd = new MoPubInterstitial(activity, id);
            }

            if (!TextUtils.isEmpty(keyword)) {
                realInterstitialAd.setKeywords(keyword);
            }

            if (!TextUtils.isEmpty(keywordField)) {
                realInterstitialAd.setUserDataKeywords(keywordField);
            }
            realInterstitialAd.setInterstitialAdListener(new MoPubInterstitial.InterstitialAdListener() {
                @Override
                public void onInterstitialLoaded(MoPubInterstitial moPubInterstitial) {
                    adStatus = AdsContants.RAW_AD_STATUS_LOADED;
                    if (mListener != null) {
                        mListener.onAdLoaded(MpRawInterstitialAd.this);
                    }
                }

                @Override
                public void onInterstitialFailed(MoPubInterstitial moPubInterstitial, MoPubErrorCode moPubErrorCode) {
                    adStatus = AdsContants.RAW_AD_STATUS_FAIL;

                    if (mListener != null) {
                        mListener.onError(MpRawInterstitialAd.this, moPubErrorCode.getIntCode(), moPubErrorCode.toString());
                    }
                }

                @Override
                public void onInterstitialShown(MoPubInterstitial moPubInterstitial) {
                    if (mListener != null) {
                        mListener.onShow(MpRawInterstitialAd.this);
                    }
                }

                @Override
                public void onInterstitialClicked(MoPubInterstitial moPubInterstitial) {
                    if (mListener != null) {
                        mListener.onClick(MpRawInterstitialAd.this);
                    }
                }

                @Override
                public void onInterstitialDismissed(MoPubInterstitial moPubInterstitial) {
                    if (mListener != null) {
                        mListener.onClose(MpRawInterstitialAd.this);
                    }
                }
            });
        }
    }

}
