package corall.ad.bean.nativead;

import android.content.Context;
import android.view.View;

import java.util.List;

import corall.ad.AdsContants;
import corall.ad.AdsModule;
import corall.ad.bean.RawAd;
import corall.ad.bean.listener.nativead.NativeAdLoadListener;

/**
 * 原生平台广告代理基类
 * Created by ChenLi on 2017/11/20.
 */

public abstract class RawNativeAd extends RawAd {

    protected boolean isLoadFail;

    public boolean isLoadFail() {
        return isLoadFail;
    }

    public abstract String getId();

    public abstract String getTitle();

    public abstract String getIconUrl();

    public abstract String getImageUrl();

    public abstract String getDesc();

    public abstract int getRawChannelType();

    public abstract String getCallToAction();

    public abstract float getRating();

    public abstract void destroy();

    public abstract String getSource();

    public abstract void setAdListener(NativeAdLoadListener listener);

    public abstract void registerViewForInteraction(View view);

    public abstract void registerViewForInteraction(View view, List<View> viewList);

    public abstract void unregisterView();

    public void fill() {
        if (AdsModule.isNeedToBlock()) {
            return;
        }
        adStatus = AdsContants.RAW_AD_STATUS_FILLING;
    }

    public void load() {
        if (AdsModule.isNeedToBlock()) {
            return;
        }
        adStatus = AdsContants.RAW_AD_STATUS_LOADING;
    }


    public abstract boolean isLoaded();

    public abstract RawNativeAd getCachedAd();

    public abstract void clearCache();

    public abstract View getAdChoiceView(Context ctx);

}
