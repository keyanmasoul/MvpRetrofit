package corall.ad.bean.banner;

import android.text.TextUtils;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import corall.ad.AdsContants;
import corall.ad.R;
import corall.ad.bean.listener.banner.BannerLoadListener;
import corall.base.app.CorApplication;

import static corall.ad.AdsContants.RAW_AD_STATUS_DESTROYED;

/**
 * Created by ChenLi on 2017/12/4.
 */

public class AMRawBannerAd extends RawBannerAd {

    private String id;
    private AdView realBannerAd;
    private BannerLoadListener mListener;
    private String type;

    private AdSize customAdSize;

    public AMRawBannerAd(String id, String type) {
        this.id = id;
        this.type = type;
        if (TextUtils.isEmpty(type)) {
            this.type = getString(R.string.am_banner_type_default);
        }
        realBannerAd = null;
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

    public Object getSize() {
        return customAdSize;
    }

    @Override
    public boolean isLoading() {
        if (realBannerAd == null) {
            return false;
        }
        return realBannerAd.isLoading();
    }


    @Override
    public void destroy()  {
        if (realBannerAd == null) {
            return;
        }
        realBannerAd.destroy();
        adStatus = RAW_AD_STATUS_DESTROYED;
    }

    @Override
    public void load()  {
        super.load();
        try {
            initRealBannerAd();
            AdRequest.Builder builder = new AdRequest.Builder();
            realBannerAd.loadAd(builder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getType() {
        return type;
    }

    public String getMediationAdapterClassName() throws Exception {
        if (realBannerAd == null) {
            return "";
        }
        return realBannerAd.getMediationAdapterClassName();
    }

    @Override
    public View getBannerView() {
        return (View) realBannerAd;
    }

    private AdSize getAdSize() throws Exception {
        return (AdSize) AdSize.class.getField(type).get(null);
    }

    @Override
    public void pause()  {
        if (realBannerAd == null) {
            return;
        }
        realBannerAd.pause();
    }

    @Override
    public void resume()  {
        if (realBannerAd == null) {
            return;
        }
        realBannerAd.resume();
    }

    private void initRealBannerAd() throws Exception {
        if (realBannerAd == null) {
            realBannerAd = new AdView(CorApplication.getInstance());
            realBannerAd.setAdUnitId(id);

            if (customAdSize == null) {
                AdSize adSize = getAdSize();
                realBannerAd.setAdSize(adSize);
            } else {
                realBannerAd.setAdSize(customAdSize);

            }
            realBannerAd.setAdListener(new AdListener() {
                @Override
                public void onAdClicked() {
                    if (mListener != null) {
                        mListener.onClick(AMRawBannerAd.this);
                    }
                }

                @Override
                public void onAdClosed() {
                }

                @Override
                public void onAdFailedToLoad(int i) {
                    if (mListener != null) {
                        mListener.onError(AMRawBannerAd.this, i, "fail");
                    }
                }

                @Override
                public void onAdImpression() {
                    if (mListener != null) {
                        mListener.onLoggingImpression(AMRawBannerAd.this);
                    }
                }

                @Override
                public void onAdLoaded() {
                    if (mListener != null) {
                        mListener.onAdLoaded(AMRawBannerAd.this);
                    }
                }

                @Override
                public void onAdOpened() {
//                    if(mListener!=null){
//                        mListener.onClick(AMRawBannerAd.this);
//                    }
                }
            });
        }
    }

    @Override
    public String getPlatform() {
        return AdsContants.AD_PLATFORM_ADMOB;
    }

}
