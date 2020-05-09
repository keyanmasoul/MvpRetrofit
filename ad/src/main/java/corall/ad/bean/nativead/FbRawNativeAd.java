package corall.ad.bean.nativead;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;

import java.util.List;

import corall.ad.AdsContants;
import corall.ad.bean.listener.nativead.NativeAdLoadListener;
import corall.base.app.AMApplication;

import static corall.ad.AdsContants.RAW_AD_STATUS_DESTROYED;

/**
 * fb广告代理类
 * Created by ChenLi on 2017/11/20.
 */

public class FbRawNativeAd extends RawNativeAd {

    String id;
    private NativeAd realNativeAd;
    private NativeAdLoadListener mListener;
    //    private FbReplaceInfo replaceInfo;
    private NativeAdLayout adLayout;

    public FbRawNativeAd(String id) {
        this.id = id;
    }


    @Override
    public String getId() {
        return id;
    }

//    public void setReplaceInfo(FbReplaceInfo info) {
//        replaceInfo = info;
//    }

    @Override
    public String getTitle() {
        if (realNativeAd == null) {
            return "";
        }
        return realNativeAd.getAdHeadline();
    }

    @Override
    public String getIconUrl() {
        return null;
    }

    @Override
    public String getImageUrl() {
        return null;
    }

    public void setNativeLayout(NativeAdLayout layout) {
        adLayout = layout;
    }

    @Override
    public String getDesc() {
        if (realNativeAd == null) {
            return "";
        }
        return realNativeAd.getAdBodyText();
    }

    @Override
    public int getRawChannelType() {
        return -1;
    }

    @Override
    public String getCallToAction() {
        if (realNativeAd == null) {
            return "";
        }
        return realNativeAd.getAdCallToAction();
    }

    @Override
    public float getRating() {
        return -1;
    }

    @Override
    public void destroy() {
        if (realNativeAd == null) {
            return;
        }
        realNativeAd.destroy();
        realNativeAd = null;
        mListener = null;
        adLayout = null;
        adStatus = RAW_AD_STATUS_DESTROYED;
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
//        try {
//            Method method = realNativeAd.getClass().getMethod(getString(R.string.fb_native_registerViewForInteraction), View.class);
//            method.invoke(realNativeAd, view);
//        } catch (Exception e) {

//        }
    }

    @Override
    public void registerViewForInteraction(View view, List<View> viewList) {
//        try {
//            Method method = realNativeAd.getClass().getMethod("registerViewForInteraction", View.class, List.class);
//            method.invoke(realNativeAd, view, viewList);
//        } catch (Exception e) {

//        }
    }

    @Override
    public void unregisterView() {
            realNativeAd.unregisterView();

    }

    public void registerViewForInteraction(View view, MediaView mediaView, ImageView iconView, List<View> viewList) {
        if (adLayout == null) {
            return;
        }
        try {
            realNativeAd.registerViewForInteraction(view, mediaView, iconView, viewList);
            adLayout.addView(view);
        } catch (Exception e) {

        }
    }

    public View getAdChoiceView(Context ctx) {
        if (adLayout == null) {
            return null;
        }
        try {
            AdOptionsView adOptionsView = new AdOptionsView(AMApplication.getInstance(), realNativeAd, adLayout);
            return adOptionsView;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void fill() {

    }

    @Override
    public void load() {
        super.load();
        try {
            initRealNativeAd();
            NativeAd.NativeAdLoadConfigBuilder builder = realNativeAd.buildLoadAdConfig();
            builder.withAdListener(new NativeAdListener() {
                @Override
                public void onMediaDownloaded(Ad ad) {

                }

                @Override
                public void onError(Ad ad, AdError adError) {
                    if (mListener != null) {
                        mListener.onError(FbRawNativeAd.this, adError.getErrorCode(), adError.getErrorMessage());
                    }
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    if (mListener != null) {
                        mListener.onAdLoaded(FbRawNativeAd.this);
                    }
                }

                @Override
                public void onAdClicked(Ad ad) {
                    if (mListener != null) {
                        mListener.onClick(FbRawNativeAd.this);
                    }
                }

                @Override
                public void onLoggingImpression(Ad ad) {
                    if (mListener != null) {
                        mListener.onLoggingImpression(FbRawNativeAd.this);
                    }
                }
            });
            realNativeAd.loadAd(builder.build());
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    public String getPlatform() {
        return AdsContants.AD_PLATFORM_FACEBOOK;
    }

    @Override
    public boolean isLoaded() {
        if (realNativeAd == null) {
            return false;
        }
        return realNativeAd.isAdLoaded();
    }

    @Override
    public RawNativeAd getCachedAd() {
        return null;
    }

    @Override
    public void clearCache() {

    }

    /**
     * 社交元素
     *
     * @return
     */
    public String getAdSocialContext() {
        if (realNativeAd == null) {
            return "";
        }
        return realNativeAd.getAdSocialContext();
    }

    /**
     * “Sponsored”的本地化文本
     *
     * @return
     */
    public String getSponsoredTranslation() {
        if (realNativeAd == null) {
            return "";
        }
        return realNativeAd.getSponsoredTranslation();
    }

    /**
     * 广告主名称
     *
     * @return
     */
    public String getAdvertiserName() {
        if (realNativeAd == null) {
            return "";
        }
        return realNativeAd.getAdvertiserName();
    }

    /**
     * 是否包含行动按钮
     *
     * @return
     */
    public boolean hasCallToAction() {
        if (realNativeAd == null) {
            return false;
        }
        return realNativeAd.hasCallToAction();
    }

    private void initRealNativeAd() throws Exception {
        if (realNativeAd == null) {
            realNativeAd = new NativeAd(AMApplication.getInstance(), id);
        }
    }

//    @Override
//    public FbReplaceInfo getReplaceInfo() {
//        return replaceInfo;
//    }

}
