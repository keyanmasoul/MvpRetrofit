package corall.ad.bean.banner;

import android.text.TextUtils;
import android.view.View;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;

import corall.ad.AdsContants;
import corall.ad.R;
import corall.ad.bean.listener.banner.BannerLoadListener;
import corall.base.app.AMApplication;

import static corall.ad.AdsContants.RAW_AD_STATUS_DESTROYED;


public class FbRawBannerAd extends RawBannerAd {

    String id;
    private AdView realBannerAd;
    // 广告请求监听
    private BannerLoadListener mListener;
    private String type;
    private AdSize customAdSize;

    public FbRawBannerAd(String id, String type) {
        this.id = id;
        this.type = type;
        if (TextUtils.isEmpty(type)) {
            this.type = getString(R.string.fb_banner_type_default);
        }
        realBannerAd = null;
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
    public void setAdListener(BannerLoadListener listener) {

        mListener = listener;

    }

    public void setSize(AdSize adSize) {
        customAdSize = adSize;
    }

    @Override
    public void load()  {
        super.load();
        try {
            initRealBannerAd();
            AdView.AdViewLoadConfigBuilder builder = realBannerAd.buildLoadAdConfig();
            builder.withAdListener(new AdListener() {
                @Override
                public void onError(Ad ad, AdError adError) {
                    if (mListener != null) {
                        mListener.onError(FbRawBannerAd.this, adError.getErrorCode(), adError.getErrorMessage());
                    }
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    if (mListener != null) {
                        mListener.onAdLoaded(FbRawBannerAd.this);
                    }
                }

                @Override
                public void onAdClicked(Ad ad) {
                    if (mListener != null) {
                        mListener.onClick(FbRawBannerAd.this);
                    }
                }

                @Override
                public void onLoggingImpression(Ad ad) {
                    if (mListener != null) {
                        mListener.onLoggingImpression(FbRawBannerAd.this);
                    }
                }
            });
            realBannerAd.loadAd(builder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private AdSize getAdSize() throws Exception {
        if (customAdSize == null) {
            return (AdSize) AdSize.class.getField(type).get(null);
        } else {
            return customAdSize;
        }
    }

    private void initRealBannerAd() throws Exception {
        if (realBannerAd == null) {
            AdSize adSize = getAdSize();
            realBannerAd = new AdView(AMApplication.getInstance(), id, adSize);
        }
    }

    @Override
    public View getBannerView() {
            if (!isAdValid()) {
                return null;
            }

        return (View) realBannerAd;
    }

    @Override
    public boolean isLoading()  {
        return false;
    }

    public boolean isLoaded()  {
        return isAdValid();
    }

    private boolean isAdValid()  {
        if (realBannerAd == null) {
            return false;
        }
        if (id.contains(getString(R.string.fb_in_test))) {
            return true;
        }
        return realBannerAd.isAdInvalidated();//.getClass().getMethod(getString(R.string.fb_banner_isAdInvalidated));

    }

    @Override
    public void destroy()  {
        if (realBannerAd == null) {
            return;
        }
        realBannerAd.destroy();
        realBannerAd = null;
        mListener = null;
        adStatus = RAW_AD_STATUS_DESTROYED;
    }

}
