package corall.ad.bean.nativead;

import android.content.Context;
import android.view.View;

import com.mopub.nativeads.AdapterHelper;
import com.mopub.nativeads.MediaViewBinder;
import com.mopub.nativeads.MoPubNative;
import com.mopub.nativeads.MoPubStaticNativeAdRenderer;
import com.mopub.nativeads.MoPubVideoNativeAdRenderer;
import com.mopub.nativeads.NativeAd;
import com.mopub.nativeads.NativeErrorCode;
import com.mopub.nativeads.ViewBinder;

import java.util.List;

import corall.ad.AdsContants;
import corall.ad.R;
import corall.ad.bean.listener.nativead.NativeAdLoadListener;
import corall.base.app.CorApplication;

import static corall.ad.AdsContants.RAW_AD_STATUS_DESTROYED;

public class MpRawNativeAd extends RawNativeAd {

    private String id;

    private MoPubNative realNativeAd;
    private NativeAd realLoadedNativeAd;
    private NativeAdLoadListener mListener;
    private int staticLayoutId = -1;
    private int videoLayoutId = -1;
    private int facebookLayoutId = -1;
    private int admobLayoutId = -1;

    public MpRawNativeAd(String id) {
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

    public void setLayoutId(int id) {
        staticLayoutId = id;
    }

    public void setVideoLayoutId(int id) {
        videoLayoutId = id;
    }

    public void setFacebookLayoutId(int id) {
        facebookLayoutId = id;
    }

    public void setAdmobLayoutId(int id) {
        admobLayoutId = id;
    }

    @Override
    public String getTitle()   {
        return "";
    }

    @Override
    public String getIconUrl()  {
        return "";
    }

    @Override
    public String getImageUrl() {
        return "";
    }

    @Override
    public String getDesc() {
        return "";
    }

    @Override
    public int getRawChannelType() {
        return 0;
    }

    @Override
    public String getCallToAction()  {
        return "";
    }

    @Override
    public float getRating()  {
        return 0;
    }

    public void clear(View v) {
        if (realLoadedNativeAd != null) {
            realLoadedNativeAd.clear(v);
        }
    }

    @Override
    public void destroy() {
        if (realNativeAd != null) {
            realNativeAd.destroy();
        }

        if (realLoadedNativeAd != null) {
            realLoadedNativeAd.destroy();
        }
        adStatus = RAW_AD_STATUS_DESTROYED;
    }

    @Override
    public String getSource()  {
        return null;
    }

    @Override
    public void load() {
        super.load();
        try {
            initRealNativeAd();

            if (videoLayoutId != -1) {

                MediaViewBinder.Builder builder = new MediaViewBinder.Builder(videoLayoutId);

                MediaViewBinder binder = builder.iconImageId(R.id.adv_sdk_install_icon).titleId(R.id.adv_sdk_title).textId(R.id.adv_sdk_desc)
                        .privacyInformationIconImageId(R.id.adv_sdk_corner_img).callToActionId(R.id.adv_sdk_install_dl).mediaLayoutId(R.id.adv_sdk_media_layout)
                        .build();
                MoPubVideoNativeAdRenderer renderer = new MoPubVideoNativeAdRenderer(binder);
                realNativeAd.registerAdRenderer(renderer);
            }

            if (facebookLayoutId != -1) {
//                Class mediaBinderClass = Class.forName(getString(R.string.mp_native_class_FacebookAdRenderer_FacebookViewBinder_Builder));
//                FacebookAdRenderer.FacebookViewBinder.Builder mediaBinderBuilder = new FacebookAdRenderer.FacebookViewBinder.Builder();
//                mediaBinderBuilder.adIconViewId(R.id.adv_sdk_install_icon).titlId(R.id.adv_sdk_title).textId(R.id.adv_sdk_title).mediaViewId(R.id.adv_fb_media_id)
//                        .advertiserNameId(R.id.adv_fb_social_text).callToActionId(R.id.adv_sdk_install_dl).adChoiceRelativeLayoutId(R.id.adv_fb_adchoice_id);
//                FacebookAdRenderer.FacebookViewBinder binder = mediaBinderBuilder.build();
//
//                FacebookAdRenderer render = new FacebookAdRenderer(binder);
//                realNativeAd.registerAdRenderer(render);
            }

            //static layout
            ViewBinder.Builder binderBuilder = new ViewBinder.Builder(staticLayoutId);
            binderBuilder.mainImageId(R.id.adv_sdk_install_image).titleId(R.id.adv_sdk_title).iconImageId(R.id.adv_sdk_install_icon).textId(R.id.adv_sdk_desc)
                    .callToActionId(R.id.adv_sdk_install_dl).privacyInformationIconImageId(R.id.adv_sdk_corner_img);
            MoPubStaticNativeAdRenderer staticRenderer = new MoPubStaticNativeAdRenderer(binderBuilder.build());
            realNativeAd.registerAdRenderer(staticRenderer);
            realNativeAd.makeRequest();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setAdListener(NativeAdLoadListener listener) {
        mListener = listener;
    }

    public View createView() {
        if (realLoadedNativeAd == null) {
            return null;
        }
        AdapterHelper helper = new AdapterHelper(CorApplication.getInstance(), 0, 3);
        View v = helper.getAdView(null, null, realLoadedNativeAd, null);
        return v;
    }

    @Override
    public void registerViewForInteraction(View view) {
    }

    @Override
    public void registerViewForInteraction(View view, List<View> viewList){
        return;
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

    private void initRealNativeAd() throws Exception {
        if (realNativeAd == null) {
            realNativeAd = new MoPubNative(CorApplication.getInstance(), id, new MoPubNative.MoPubNativeNetworkListener() {
                @Override
                public void onNativeLoad(NativeAd nativeAd) {
                    realLoadedNativeAd = nativeAd;
                    if (mListener != null) {
                        mListener.onAdLoaded(MpRawNativeAd.this);
                    }

                    realLoadedNativeAd.setMoPubNativeEventListener(new NativeAd.MoPubNativeEventListener() {
                        @Override
                        public void onImpression(View view) {
                            if (mListener != null) {
                                mListener.onLoggingImpression(MpRawNativeAd.this);
                            }
                        }

                        @Override
                        public void onClick(View view) {
                            if (mListener != null) {
                                mListener.onClick(MpRawNativeAd.this);
                            }
                        }
                    });
                }

                @Override
                public void onNativeFail(NativeErrorCode nativeErrorCode) {
                    if (mListener != null) {
                        mListener.onError(MpRawNativeAd.this, nativeErrorCode.getIntCode(), nativeErrorCode.toString());
                    }
                }
            });
        }
    }


}
