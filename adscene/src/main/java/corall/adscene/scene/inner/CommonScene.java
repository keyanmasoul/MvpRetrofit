package corall.adscene.scene.inner;

import android.os.Message;
import android.view.View;


import com.example.adscene.R;

import corall.ad.AdsContants;
import corall.ad.bean.CorAdUnionPlace;
import corall.ad.bean.banner.DirectRawBannerAd;
import corall.ad.ui.card.CommonCardView;
import corall.ad.ui.card.CommonInnerCardView;
import corall.ad.ui.card.CommonNativeAdCardView;
import corall.ad.ui.card.FbInnerCardView;
import corall.adscene.AdType;
import corall.adscene.Category;
import corall.adscene.EntranceType;
import corall.adscene.scene.AdReportScene;
import corall.adscene.strategy.IStrategyExecutor;
import corall.base.app.CorApplication;
import corall.base.bean.AdEvent;

/**
 * desc: 普通的原生广告（banner）场景
 * date: 2018/4/18
 * author: ancun
 */

public class CommonScene extends AdReportScene {

    private IStrategyExecutor mExecutor;

    public CommonScene(CorApplication context, EntranceType entranceType) {
        super(context, entranceType);
    }

    @Override
    protected CommonNativeAdCardView initCardView() {
        if (Category.A.equals(mEntranceType.getCategory())) {
            if (activeAd.getPlatform().equals(AdsContants.AD_PLATFORM_FACEBOOK)) {
                return new FbInnerCardView(imContext);
            } else {
                return new CommonInnerCardView(imContext);
            }
        } else {
            return new CommonCardView(imContext);
        }
    }

    @Override
    public boolean load(boolean forceLoad) {
        CorAdUnionPlace adUnionPlace = mEntranceType.getAdScene().getHmAdUnionPlace();
        if (adUnionPlace == null) {
            return false;
        }

        mExecutor = mEntranceType.getAdScene().getStrategyExecutor();
        if (forceLoad || mExecutor.check(adUnionPlace)) {
            return loadUnionPlace(adUnionPlace);
        }

        return false;
    }

    @Override
    public View getAdView() {
        View adView = super.getAdView();
        if (adView instanceof CommonCardView) {
//            if (AdUtils.isFaceBook(activeAd)) {
//                View connerLayout = adView.findViewById(R.id.conner_layout);
//                if (connerLayout != null) {
//                    connerLayout.setVisibility(View.GONE);
//                }
//            }
        }
        return adView;
    }


    @Override
    protected void onADLoaded(AdType adType) {

        final AdEvent message = new AdEvent();
        message.setWhat(R.id.poster_msg_ad_common_scene_loaded);
        message.setObject(mEntranceType.getName());
        imContext.sendMessage(message);
    }

    @Override
    public void onAdClose(AdType adType) {
        //release();
    }

    @Override
    public void onADShow(AdType adType) {
        super.onADShow(adType);
        if (mExecutor != null) {
            mExecutor.record();
        }
    }

    public void onShowedDirectBanner() {
        if (activeAd instanceof DirectRawBannerAd) {
            adStatus = AD_STATUS_SHOWED;
            if (showTime == -1) {
                showTime = System.currentTimeMillis();
            }
            onADShow(AdType.DIRECT_BANNER);
        }
    }


    @Override
    public void onADClick(AdType adType) {
        super.onADClick(adType);

        final AdEvent message = new AdEvent();
        message.setWhat(R.id.poster_msg_ad_common_scene_click);
        message.setObject(mEntranceType.getName());
        imContext.sendMessage(message);
    }
}
