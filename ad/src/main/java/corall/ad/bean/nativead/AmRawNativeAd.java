package corall.ad.bean.nativead;

import android.content.Context;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;

import java.util.List;

import corall.ad.AdsContants;
import corall.ad.bean.listener.nativead.NativeAdLoadListener;
import corall.base.app.CorApplication;

import static corall.ad.AdsContants.RAW_AD_STATUS_DESTROYED;

/**
 * Created by ChenLi on 2018/3/22.
 */

public class AmRawNativeAd extends RawNativeAd {

    private String id;
    private UnifiedNativeAd realNativeAd;
    private NativeAdLoadListener mListener;

    public AmRawNativeAd(String id) {
        this.id = id;
    }

    public int getAdmobType() {
        return AdsContants.ADMOB_NATIVE_UNIFIED;
    }

    @Override
    public String getPlatform() {
        return AdsContants.AD_PLATFORM_ADMOB;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getTitle() {
        if (realNativeAd == null) {
            return "";
        }
        return realNativeAd.getHeadline();
    }

    @Override
    public String getIconUrl() {
        if (realNativeAd == null || realNativeAd.getIcon() == null) {
            return "";
        }
        return realNativeAd.getIcon().getUri().toString();

    }

    public boolean isContentVideo() {
        if (realNativeAd == null) {
            return false;
        }
        return realNativeAd.getVideoController().hasVideoContent();

    }

    @Override
    public String getImageUrl() {
        if (realNativeAd == null) {
            return "";
        }

        List<NativeAd.Image> imageList = realNativeAd.getImages();
        if (imageList != null && imageList.size() > 0) {
            NativeAd.Image image = imageList.get(0);
            return image.getUri().toString();
        }

        return "";
    }

    @Override
    public String getDesc() {
        return realNativeAd.getBody();
    }

    @Override
    public int getRawChannelType() {
        return 0;
    }

    @Override
    public String getCallToAction() {
        return realNativeAd.getCallToAction();
    }

    @Override
    public float getRating() {
        return realNativeAd.getStarRating().floatValue();
    }

    @Override
    public void destroy() {
        if (realNativeAd == null) {
            return;
        }
        realNativeAd.destroy();

        realNativeAd = null;
        mListener = null;

        adStatus = RAW_AD_STATUS_DESTROYED;
    }

    public String getMediationAdapterClassName() throws Exception {
        if (realNativeAd == null) {
            return "error";
        }
        return realNativeAd.getMediationAdapterClassName();
    }

    @Override
    public String getSource() {
        return null;
    }

    @Override
    public void setAdListener(NativeAdLoadListener listener) {
        mListener = listener;
    }


    @Override
    public void registerViewForInteraction(View view) {
        if (realNativeAd == null) {
            return;
        }
        if (view instanceof UnifiedNativeAdView) {
            ((UnifiedNativeAdView) view).setNativeAd(realNativeAd);
        }
//        try {
//            Class unifiedAdView = Class.forName(decodeConstant(AdsContants.AM_AD_NATIVE_UNIFIED_CLASS_NAME);
//            if (view.getClass() == unifiedAdView) {
//            Method setNativeAd = view.getClass().getMethod(getString(R.string.am_native_setNativeAd), Class.forName(getString(R.string.am_native_uni_ad)));
//            setNativeAd.invoke(view, realNativeAd);
//            realNativeAd.
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            AdActionException adActionException = new AdActionException(id, getPlatform(), e.getMessage());
//            throw adActionException;
//        }
    }

    @Override
    public void registerViewForInteraction(View view, List<View> viewList) {
        registerViewForInteraction(view);
    }

    @Override
    public void unregisterView() {

    }

    @Override
    public boolean isLoaded() {
        return false;
    }

    @Override
    public RawNativeAd getCachedAd() {
        return null;
    }

    @Override
    public void clearCache() {

    }

    @Override
    public View getAdChoiceView(Context ctx) {
        return null;
    }

    public void load() {
        super.load();
        try {
            //初始化AdRequest

            AdRequest.Builder builder = new AdRequest.Builder();


            AdLoader.Builder loaderBuilder = new AdLoader.Builder(CorApplication.getInstance(), id);


            NativeAdOptions.Builder optionBuilder = new NativeAdOptions.Builder();
            NativeAdOptions options = optionBuilder.build();
            loaderBuilder.withNativeAdOptions(options);


            loaderBuilder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                @Override
                public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                    realNativeAd = unifiedNativeAd;
                    if (mListener != null) {
                        mListener.onAdLoaded(AmRawNativeAd.this);
                    }
                }
            });
            loaderBuilder.withAdListener(new AdListener() {
                @Override
                public void onAdClicked() {
                    if (mListener != null) {
                        mListener.onClick(AmRawNativeAd.this);
                    }
                }

                @Override
                public void onAdClosed() {
//                    if (mListener != null) {
//                        mListener.onClose(AMRawInterstitialAd.this);
//                    }
                }

                @Override
                public void onAdFailedToLoad(int i) {
                    if (mListener != null) {
                        mListener.onError(AmRawNativeAd.this, i, "fail");
                    }
                }

                @Override
                public void onAdImpression() {
//                    if (mListener != null) {
//                        mListener.onShow(AmRawNativeAd.this);
//                    }
                }

                @Override
                public void onAdLoaded() {
//                    if (mListener != null) {
//                        mListener.onAdLoaded(AMRawInterstitialAd.this);
//                    }
                }

                @Override
                public void onAdOpened() {
//                    if(mListener!=null){
//                        mListener.onClick(AMRawBannerAd.this);
//                    }
                }
            });

            AdLoader loader = loaderBuilder.build();
            loader.loadAd(builder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
