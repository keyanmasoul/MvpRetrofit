package corall.ad.bean.interstitial;

import android.app.Activity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import corall.ad.AdsContants;
import corall.ad.bean.listener.interstitialad.InterstitialAdLoadListener;
import corall.base.app.CorApplication;

import static corall.ad.AdsContants.RAW_AD_STATUS_DESTROYED;

/**
 * admob插屏广告代理类
 * Created by ChenLi on 2017/12/4.
 */

public class AMRawInterstitialAd extends RawInterstitialAd {

    private String id;
    private InterstitialAd realInterstitialAd;
    private InterstitialAdLoadListener mListener;

    public AMRawInterstitialAd(String id) {
        this.id = id;
        realInterstitialAd = null;
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
    public boolean isLoaded() {
        if (realInterstitialAd == null) {
            return false;
        }
        return realInterstitialAd.isLoaded();
    }

    @Override
    public boolean isLoading() {
        if (realInterstitialAd == null) {
            return false;
        }
        return realInterstitialAd.isLoading();
    }

    @Override
    public void destroy() {
        //admob插屏没有销毁方法
        realInterstitialAd = null;
        mListener = null;
        adStatus = RAW_AD_STATUS_DESTROYED;
    }

    @Override
    public void load() {
        super.load();

        initRealIntersititialAd(null);
        AdRequest.Builder builder = new AdRequest.Builder();
        realInterstitialAd.loadAd(builder.build());

    }

    @Override
    public void load(Activity activity) {
        super.load();
        initRealIntersititialAd(activity);
        AdRequest.Builder builder = new AdRequest.Builder();
        realInterstitialAd.loadAd(builder.build());
    }

    @Override
    public void show() {
        if (realInterstitialAd == null) {
            return;
        }
        realInterstitialAd.show();
    }

    private void initRealIntersititialAd(Activity activity) {
        if (realInterstitialAd == null) {
            if (activity == null) {
                realInterstitialAd = new InterstitialAd(CorApplication.getInstance());
            } else {
                realInterstitialAd = new InterstitialAd(activity);
            }

            realInterstitialAd.setAdUnitId(id);

            realInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClicked() {
                    if (mListener != null) {
                        mListener.onClick(AMRawInterstitialAd.this);
                    }
                }

                @Override
                public void onAdClosed() {
                    if (mListener != null) {
                        mListener.onClose(AMRawInterstitialAd.this);
                    }
                }

                @Override
                public void onAdFailedToLoad(int i) {
                    if (mListener != null) {
                        mListener.onError(AMRawInterstitialAd.this, i, "fail");
                    }
                }

                @Override
                public void onAdImpression() {
//                    if (mListener != null) {
//                        mListener.onShow(AMRawInterstitialAd.this);
//                    }
                }

                @Override
                public void onAdLoaded() {
                    if (mListener != null) {
                        mListener.onAdLoaded(AMRawInterstitialAd.this);
                    }
                }

                @Override
                public void onAdOpened() {
                    if (mListener != null) {
                        mListener.onShow(AMRawInterstitialAd.this);
                    }
                }
            });
        }
    }

    @Override
    public String getPlatform() {
        return AdsContants.AD_PLATFORM_ADMOB;
    }

    public String getMediationAdapterClassName() throws Exception {
        if (realInterstitialAd == null) {
            return "";
        }
        return realInterstitialAd.getMediationAdapterClassName();

    }

//    private class AmAdListenerHandler implements MethodHandler {
//
//
//        @Override
//        public Object invoke(Object o, Method method, Method method1, Object[] objects) throws Throwable {
//            if (MLog.debug) {
//                Log.i("AMRawNativeExpressAd", method.getName() + "," + method1.getName());
//            }
//            return null;
//        }
//    }
}
