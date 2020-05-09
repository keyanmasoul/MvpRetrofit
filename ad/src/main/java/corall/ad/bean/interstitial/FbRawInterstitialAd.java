package corall.ad.bean.interstitial;

import android.app.Activity;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;

import corall.ad.AdsContants;
import corall.ad.R;
import corall.ad.bean.listener.interstitialad.InterstitialAdLoadListener;
import corall.base.app.AMApplication;

import static corall.ad.AdsContants.RAW_AD_STATUS_DESTROYED;

public class FbRawInterstitialAd extends RawInterstitialAd {

    private String id;

    private InterstitialAd realInterstitialAd;

    // 广告请求监听
    private InterstitialAdLoadListener mListener;

    public FbRawInterstitialAd(String id) {
        this.id = id;
        realInterstitialAd = null;
    }

    @Override
    public String getPlatform() {
        return AdsContants.AD_PLATFORM_FACEBOOK;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void destroy()  {

        mListener = null;
        adStatus = RAW_AD_STATUS_DESTROYED;
        if (realInterstitialAd == null) {
            return;
        }
        realInterstitialAd.destroy();
        realInterstitialAd = null;
    }

    @Override
    public void setAdListener(InterstitialAdLoadListener listener) {
        mListener = listener;
    }

    private boolean isAdValid()  {
        if (realInterstitialAd == null) {
            return false;
        }
        if (id.contains(getString(R.string.fb_in_test))) {
            return true;
        }
        return realInterstitialAd.isAdInvalidated();
    }

    @Override
    public void show()  {
        if (!isAdValid()) {
            return;
        }
        realInterstitialAd.show();
    }

    @Override
    public boolean isLoaded()  {
        return isAdValid();
    }

    @Override
    public boolean isLoading()  {
        return false;
    }

    @Override
    public void load()  {
        load(null);
    }

    public void load(Activity activity)  {
        super.load(activity);
        try {
            initRealInterstitialAd(activity);
            InterstitialAd.InterstitialAdLoadConfigBuilder builder = realInterstitialAd.buildLoadAdConfig();
            builder.withAdListener(new InterstitialAdListener() {
                @Override
                public void onInterstitialDisplayed(Ad ad) {
                    if (mListener != null) {
                        mListener.onShow(FbRawInterstitialAd.this);
                    }
                }

                @Override
                public void onInterstitialDismissed(Ad ad) {
                    if (mListener != null) {
                        mListener.onClose(FbRawInterstitialAd.this);
                    }
                }

                @Override
                public void onError(Ad ad, AdError adError) {
                    if (mListener != null) {
                        mListener.onError(FbRawInterstitialAd.this, adError.getErrorCode(), adError.getErrorMessage());
                    }
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    if (mListener != null) {
                        mListener.onAdLoaded(FbRawInterstitialAd.this);
                    }
                }

                @Override
                public void onAdClicked(Ad ad) {
                    if (mListener != null) {
                        mListener.onClick(FbRawInterstitialAd.this);
                    }
                }

                @Override
                public void onLoggingImpression(Ad ad) {
                    if (mListener != null) {
                        mListener.onShow(FbRawInterstitialAd.this);
                    }
                }
            });
            realInterstitialAd.loadAd(builder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initRealInterstitialAd(Activity activity) throws Exception {
        if (realInterstitialAd == null) {
            if (activity != null) {
                realInterstitialAd = new InterstitialAd(activity, id);
            } else {
                realInterstitialAd = new InterstitialAd(AMApplication.getInstance(), id);
            }
        }
    }

}
