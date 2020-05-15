package corall.adscene.scene.outer;


import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Message;


import com.blankj.utilcode.util.AppUtils;
import com.example.adscene.R;

import org.apache.commons.lang3.ClassUtils;

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
import corall.adscene.ADConstant;
import corall.adscene.AdType;
import corall.adscene.EntranceType;
import corall.adscene.scene.AdReportScene;
import corall.adscene.strategy.IStrategyExecutor;
import corall.base.app.CorApplication;
import corall.base.util.PermissionUtil;


/**
 * desc: 普通的插屏广告场景，可以将非插屏广告类型（如：Native/Banner）以插屏广告形式展示
 * 2019年1月7日 周一 逻辑调整：
 * 有悬浮窗权限使用 window , 没有则使用 Activity
 * <p>
 * date: 2018/11/12
 * author: ancun
 */
public class CommonInterstitialOutScene extends AdReportScene {

    protected CorAdUnionPlace mHmAdPlace;
    protected IStrategyExecutor mExecutor;
    private RawBannerFullScreenAdWindow mBannerFullScreenWindow;
    private ViviRawNativeFullScreenAdWindow mNativeFullScreenWindow;
    private boolean isShowed = false;
    private boolean isLoaded = false;
    private boolean isWaitBg = false;

    private MarkableHandler msgHandler;

    @SuppressLint("HandlerLeak")
    public CommonInterstitialOutScene(CorApplication context, EntranceType entranceType) {
        super(context, entranceType);
        msgHandler = new MarkableHandler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == R.id.poster_msg_ad_background_already_show) {
                    int bgPlaceId = msg.getData().getInt("pid");
                    String bgPt = msg.getData().getString("pt");
                    //重叠广告展示成功，如果广告已加载且未展示，则展示
                    isWaitBg = false;
                    if (isLoaded && !isShowed && activeAd != null && bgPlaceId == mHmAdPlace.getBgPlaceId()) {
                        if (activeAd.getPlatform().equals(bgPt)) {
                            //如果重叠广告与本广告是同一平台，则不展示本广告
                            release();
                        } else {
                            isShowed = showAd();
                        }
                    }

                } else if (msg.what == R.id.poster_msg_ad_background_wait_out_time) {
                    //5s超时，不再等待重叠广告，直接展示，并且block重叠广告，以防重叠广告覆盖本广告
                    if (!isShowed && isWaitBg && isLoaded) {
                        isWaitBg = false;
                        isShowed = showAd();
                        getADModule().getAdSceneManager().blockBgPlace((int) mHmAdPlace.getBgPlaceId());
                    }
                }
            }
        };
    }

    @Override
    protected CommonNativeAdCardView initCardView() {
        return null;
    }

    @Override
    protected void onADLoaded(AdType adType) {
        isLoaded = true;
        if (isWaitBg) {
            //如果此时重叠广告还没有展示，本广告先不展示，等待5s
            imContext.handleMobEmptyDelayMessage(R.id.poster_msg_ad_background_wait_out_time, 5000);
            return;
        }
        isShowed = showAd();
    }

    @Override
    public void onADShow(AdType adType) {
        super.onADShow(adType);
        if (mExecutor != null) {
            mExecutor.record();
        }

    }

    @Override
    public void release() {
        super.release();
        imContext.unregisterSubHandler(msgHandler);
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
        boolean inSelfApp = AppUtils.isInSelfApp();
        if (!forceLoad && inSelfApp) {
            GARecordUtils.onADLoadBlock(imContext, AdType.ALL, mEntranceType.getName(), ADConstant.ErrorCode.SELF_APP_RUNNING);
            return false;
        }

        CorAdUnionPlace adUnionPlace = mEntranceType.getAdScene().getHmAdUnionPlace();
        if (adUnionPlace == null) {
            return false;
        }

        isWaitBg = false;
        isShowed = false;
        isLoaded = false;
        mHmAdPlace = adUnionPlace;
        mExecutor = getADModule().getADStrategyManager().getExecutorByEntranceType(mEntranceType);
        if (forceLoad || mExecutor.check(adUnionPlace)) {
            loadBgPlaceAd(adUnionPlace);

            return loadUnionPlace(adUnionPlace);
        }

        return false;
    }

    //加载重叠广告
    protected boolean loadBgPlaceAd(CorAdUnionPlace mHmAdPlace) {
        boolean bg_result = false;
        if (mHmAdPlace.getBgPlaceId() != 0) {
            if (!getADModule().getAdSceneManager().isLoadingBgPlace()) {
                bg_result = getADModule().getAdSceneManager().loadBgPlace((int) mHmAdPlace.getBgPlaceId());
                if (bg_result) {
                    //重叠广告开始加载，注册消息监听用于接收重叠广告展示的消息
                    isWaitBg = true;
                    imContext.registerSubHandler(msgHandler);
                }
            }
        }
        return bg_result;
    }

    public boolean reload(boolean forceLoad) {
        setRepeatRequestForActivity(true);
        boolean result = false;
        //isWaitBg = false;
        isShowed = false;
        isLoaded = false;
        mExecutor = getADModule().getADStrategyManager().getExecutorByEntranceType(mEntranceType);
        if (forceLoad || mExecutor.check(mHmAdPlace)) {
            //loadBgPlaceAd(mHmAdPlace);
            result = reloadUnionPlace(mHmAdPlace);
            setRepeatRequestForActivity(false);
        }

        return result;
    }

    @Override
    protected void onADLoadByActivity(AdType adType) {
        super.onADLoadByActivity(adType);

        Message message = Message.obtain();
        message.what = R.id.poster_msg_ad_show_app_outer_activity;
        message.obj = mEntranceType.getName();
        imContext.handleMobMessage(message);
    }

    @Override
    public void onAdClose(AdType adType) {
        release();

        Message message = Message.obtain();
        message.what = R.id.poster_msg_ad_close_app_outer_activity;
        message.obj = mEntranceType.getName();
        imContext.handleMobMessage(message);
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

        Message message = Message.obtain();
        message.what = R.id.poster_msg_ad_close_app_outer_activity;
        message.obj = mEntranceType.getName();
        imContext.handleMobMessage(message);
    }

    @Override
    protected void onADFail(AdType adType, int code) {
        super.onADFail(adType, code);

        Message message = Message.obtain();
        message.what = R.id.poster_msg_ad_close_app_outer_activity;
        message.obj = mEntranceType.getName();
        imContext.handleMobMessage(message);
    }

    @Override
    public boolean show() {
        return showAd();
    }

    /**
     * 显示广告
     */
    protected boolean showAd() {
        //横屏不展示广告
        if (imContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            release();
            return false;
        }

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
        imContext.unregisterSubHandler(msgHandler);
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
            ((RawInterstitialAd) activeAd).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDirectInterstitial() {
        try {
            ((DirectInterstitialAd) activeAd).showByAct(ClassUtils.getWebAdActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showWebInterstitial() {
        try {
            ((WebInterstitialAd) activeAd).showByAct(ClassUtils.getWebAdActivity());
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
        if (PermissionUtil.checkDrawOverlaysPermission(imContext)) {
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
