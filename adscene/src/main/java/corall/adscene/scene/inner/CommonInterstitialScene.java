package corall.adscene.scene.inner;


import android.os.Message;
import android.text.format.DateUtils;

import com.example.adscene.R;

import org.apache.commons.lang3.ClassUtils;

import corall.activity.BannerAdsActivity;
import corall.activity.NativeAdActivity;
import corall.activity.WebAdActivity;
import corall.base.bean.AdEvent;
import corall.ad.bean.CorAdPlace;
import corall.ad.bean.CorAdUnionPlace;
import corall.ad.bean.banner.RawBannerAd;
import corall.ad.bean.interstitial.DirectInterstitialAd;
import corall.ad.bean.interstitial.RawInterstitialAd;
import corall.ad.bean.interstitial.WebInterstitialAd;
import corall.ad.bean.nativead.RawNativeAd;
import corall.ad.bean.reward.RawRewardAd;
import corall.ad.ui.card.CommonNativeAdCardView;
import corall.ad.ui.window.IFullScreenADListener;
import corall.ad.ui.window.RawBannerFullScreenAdWindow;
import corall.ad.ui.window.ViviRawNativeFullScreenAdWindow;
import corall.adscene.ADGAConstant;
import corall.adscene.AdType;
import corall.adscene.Category;
import corall.adscene.EntranceType;
import corall.adscene.GARecordUtils;
import corall.adscene.scene.AdReportScene;
import corall.adscene.strategy.IStrategyExecutor;
import corall.base.app.CorApplication;
import corall.base.util.PermissionUtil;

/**
 * desc: 普通的插屏广告场景。
 * <b>插件广告加载方式</b></br>
 * <p>
 * <p>从触发开始请求广告，这时弹出过渡动画，广告是否显示，和动画没有直接关系，广告生成广告视图时，直接显示广告</p>
 * <p>
 * <b>支持的广告类型</b></br>
 * <p>插屏(Admob,Mopub)，原生(Admob,Mopub,FB)，BANNER (Admob)</p>
 * date: 2018/4/18
 * author: ancun
 */

public class CommonInterstitialScene extends AdReportScene {

    protected RawBannerFullScreenAdWindow mBannerFullScreenWindow;
    protected ViviRawNativeFullScreenAdWindow mNativeFullScreenWindow;

    protected CorAdUnionPlace mHmAdPlace;
    protected IStrategyExecutor mExecutor;
    protected long startLoad = 0;

    public CommonInterstitialScene(CorApplication context, EntranceType entranceType) {
        super(context, entranceType);
    }

    @Override
    protected CommonNativeAdCardView initCardView() {
        return null;
    }

    @Override
    public void onAdClose(AdType adType) {
        release();

        final AdEvent message = new AdEvent();
        message.setWhat(R.id.poster_msg_ad_interstitial_closed);
        message.setObject(mEntranceType.getName());
        imContext.sendMessage(message);
    }

    @Override
    protected void onADLoaded(AdType adType) {
        GARecordUtils.reportFunc(imContext, ADGAConstant.C_DATA, ADGAConstant.A_FILL, ":" + getOverTime());
        showAd();
    }

    protected String getOverTime() {
        long spendTime = System.currentTimeMillis() - startLoad;
        spendTime = spendTime / DateUtils.SECOND_IN_MILLIS;
        if (spendTime > 20) {
            return ">20";
        } else if (spendTime > 15) {
            return "<20";
        } else if (spendTime > 10) {
            return "<15";
        } else if (spendTime > 6) {
            return "<10";
        }

        return "<6";
    }

    @Override
    public void onADShow(AdType adType) {
        super.onADShow(adType);
        if (mExecutor != null) {
            mExecutor.record();
        }

        final AdEvent message = new AdEvent();
        message.setWhat(R.id.poster_msg_ad_interstitial_show);
        message.setObject(mEntranceType.getName());
        imContext.sendMessage(message);

    }

    @Override
    public void onADClick(AdType adType) {
        super.onADClick(adType);

        // 非插屏的广告，一般不具有关闭的功能，所以需要在用户点击的时候关闭。
        switch (adType) {
            case DA_NATIVE:
            case FB_NATIVE:
            case AM_NATIVE:
            case MP_NATIVE:
            case AM_NEW_NATIVE:
                if (mNativeFullScreenWindow != null) {
                    mNativeFullScreenWindow.close();
                    mNativeFullScreenWindow = null;
                }
                break;
            case MP_BANNER:
            case AM_BANNER:
            case AM_NEW_BANNER:
                if (mBannerFullScreenWindow != null) {
                    mBannerFullScreenWindow.close();
                    mBannerFullScreenWindow = null;
                }
                break;
        }
    }

    @Override
    public void release() {
        super.release();

        mBannerFullScreenWindow = null;
        mNativeFullScreenWindow = null;
        mHmAdPlace = null;
    }

    /**
     * 加载广告
     *
     * @param forceLoad 是否忽略配置检查，强制加载广告
     * @return
     */
    @Override
    public boolean load(boolean forceLoad) {
        startLoad = System.currentTimeMillis();

        CorAdUnionPlace adUnionPlace = mEntranceType.getAdScene().getHmAdUnionPlace();
        if (adUnionPlace == null) {
            return false;
        }
        mHmAdPlace = adUnionPlace;
        mExecutor = mEntranceType.getAdScene().getStrategyExecutor();
        if (forceLoad || mExecutor.check(adUnionPlace)) {
            return loadUnionPlace(adUnionPlace);
        }

        return false;
    }

    @Override
    public boolean show() {
        return showAd();
    }

    /**
     * 显示广告
     */
    private boolean showAd() {
        boolean result = true;

        if (activeAd instanceof RawInterstitialAd) {
            showInterstitial();

        } else if (activeAd instanceof RawRewardAd) {
            showReward();

        } else if (activeAd instanceof RawBannerAd) {
            showBannerFullScreen();

        } else if (activeAd instanceof RawNativeAd) {
            showNativeFullScreen();

        } else {
            result = false;

        }
        return result;
    }

    @Override
    public void showInterstitial() {
        if (activeAd instanceof DirectInterstitialAd) {
            showDirectInterstitial();
            return;
        } else if (activeAd instanceof WebInterstitialAd) {
            showWebInterstitial();
            return;
        }
        try {
            if (activeAd != null) {
                ((RawInterstitialAd) activeAd).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDirectInterstitial() {
        try {
            ((DirectInterstitialAd) activeAd).showByAct(WebAdActivity.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showWebInterstitial() {
        try {
            ((WebInterstitialAd) activeAd).showByAct(WebAdActivity.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showReward() {
        super.showReward();
        try {
            ((RawRewardAd) activeAd).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示 BANNER 的全屏
     */
    protected void showBannerFullScreen() {
        if (PermissionUtil.checkDrawOverlaysPermission(imContext)) {
            showBannerOfWindow();
        } else {
            BannerAdsActivity.showInterstitialAd(imContext, mEntranceType.getName());
        }
    }

    /**
     * 显示原生的全屏
     */
    protected void showNativeFullScreen() {
        if (PermissionUtil.checkDrawOverlaysPermission(imContext) && mEntranceType.getCategory() == Category.C) {
            showNativeOfWindow();
        } else {
            NativeAdActivity.showInterstitialAd(imContext, mEntranceType.getName());
        }
    }

    /**
     * 显示 BANNER 的全屏
     */
    private void showBannerOfWindow() {
        if (mBannerFullScreenWindow != null) {
            mBannerFullScreenWindow.closeWithoutCallBack();
            mBannerFullScreenWindow = null;
        }

        try {
            mBannerFullScreenWindow = new RawBannerFullScreenAdWindow(imContext);
            mBannerFullScreenWindow.isTouchToClose(false);
            mBannerFullScreenWindow.setFullScreenListener(new IFullScreenADListener() {
                @Override
                public void onADShow(CorAdPlace hmAdPlace) {
                    onRawBannerShow();
                }

                @Override
                public void onADClose(CorAdPlace hmAdPlace) {
                    onRawBannerClose();
                }
            });
            mBannerFullScreenWindow.showFullScreenBannerAd(mHmAdPlace, (RawBannerAd) activeAd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onRequestAllFail() {
        super.onRequestAllFail();

        final AdEvent message = new AdEvent();
        message.setWhat(R.id.poster_msg_ad_interstitial_failed);
        message.setObject(mEntranceType.getName());
        imContext.sendMessage(message);
    }

    /**
     * 显示原生的全屏
     */
    private void showNativeOfWindow() {
        if (mNativeFullScreenWindow != null) {
            mNativeFullScreenWindow = null;
        }

        mNativeFullScreenWindow = new ViviRawNativeFullScreenAdWindow(imContext);
        mNativeFullScreenWindow.isTouchToClose(false);
        mNativeFullScreenWindow.setFullScreenListener(new IFullScreenADListener() {
            @Override
            public void onADShow(CorAdPlace hmAdPlace) {
                onRawNativeShow();
            }

            @Override
            public void onADClose(CorAdPlace hmAdPlace) {
                onRawNativeClose();
            }

        });
        mNativeFullScreenWindow.showFullScreenNativeAd(mHmAdPlace, (RawNativeAd) activeAd);
    }

    public void onRawBannerShow() {
        if (activeAd == null) {
            return;
        }
        switch (activeAd.getPlatform()) {
            case "am":
                onADShow(AdType.AM_BANNER);
                break;

            case "am_new":
                onADShow(AdType.AM_NEW_BANNER);
                break;

            case "mp":
                onADShow(AdType.MP_BANNER);
                break;

            default:
                break;
        }
    }

    public void onRawBannerClose() {
        if (activeAd == null) {
            return;
        }
        switch (activeAd.getPlatform()) {
            case "am":
                onAdClose(AdType.AM_BANNER);
                break;

            case "am_new":
                onAdClose(AdType.AM_NEW_BANNER);
                break;

            case "mp":
                onAdClose(AdType.MP_BANNER);
                break;

            default:
                break;
        }
    }

    public void onRawNativeShow() {
        if (activeAd == null) {
            return;
        }
        switch (activeAd.getPlatform()) {
            case "fb":
                onADShow(AdType.FB_NATIVE);
                break;

            case "am":
                onADShow(AdType.AM_NATIVE);
                break;

            case "am_new":
                onADShow(AdType.AM_NEW_NATIVE);
                break;

            case "mp":
                onADShow(AdType.MP_NATIVE);
                break;

            case "da":
                onADShow(AdType.DA_NATIVE);
                break;

            default:
                break;
        }
    }

    public void onRawNativeClose() {
        if (activeAd == null) {
            return;
        }
        switch (activeAd.getPlatform()) {
            case "fb":
                onAdClose(AdType.FB_NATIVE);
                break;

            case "am":
                onAdClose(AdType.AM_NATIVE);
                break;

            case "am_new":
                onAdClose(AdType.AM_NEW_NATIVE);
                break;

            case "mp":
                onAdClose(AdType.MP_NATIVE);
                break;

            case "da":
                onAdClose(AdType.DA_NATIVE);
                break;

            default:
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.full_screen_mp_card_layout;
    }


    @Override
    protected int getVideoLayoutId() {
        return R.layout.full_screen_mp_video_ad_card_layout;
    }
}
