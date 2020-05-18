package corall.adscene.scene;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.example.adscene.R;

import java.util.ArrayList;
import java.util.List;

import corall.ad.AdsModule;
import corall.ad.bean.CorAdPlace;
import corall.ad.bean.CorAdUnionPlace;
import corall.ad.bean.RawAd;
import corall.ad.bean.banner.AMRawBannerAd;
import corall.ad.bean.banner.DirectRawBannerAd;
import corall.ad.bean.banner.FbRawBannerAd;
import corall.ad.bean.banner.MpRawBannerAd;
import corall.ad.bean.banner.RawBannerAd;
import corall.ad.bean.interstitial.AMRawInterstitialAd;
import corall.ad.bean.interstitial.DirectInterstitialAd;
import corall.ad.bean.interstitial.FbRawInterstitialAd;
import corall.ad.bean.interstitial.MpRawInterstitialAd;
import corall.ad.bean.interstitial.RawInterstitialAd;
import corall.ad.bean.interstitial.WebInterstitialAd;
import corall.ad.bean.listener.banner.BannerLoadListener;
import corall.ad.bean.listener.interstitialad.InterstitialAdLoadListener;
import corall.ad.bean.listener.nativead.NativeAdLoadListener;
import corall.ad.bean.listener.reward.RewardAdListener;
import corall.ad.bean.nativead.AmRawNativeAd;
import corall.ad.bean.nativead.FbRawNativeAd;
import corall.ad.bean.nativead.MpRawNativeAd;
import corall.ad.bean.nativead.RawNativeAd;
import corall.ad.bean.reward.AMRawRewardAd;
import corall.ad.bean.reward.RawRewardAd;
import corall.ad.ui.card.AdCardEventListener;
import corall.ad.ui.card.CommonNativeAdCardView;
import corall.ad.ui.card.SmallNativeAdCardView;
import corall.adscene.AdType;
import corall.adscene.Category;
import corall.adscene.EntranceType;
import corall.adscene.strategy.ADStrategyManager;
import corall.adscene.strategy.base.AStrategyExecutor;
import corall.base.app.CorApplication;
import corall.base.bean.AdEvent;
import corall.base.util.AppInfoUtil;

//import cr.s.a.adscene.inner.StoreScene;

/**
 * desc: 广告加载核心类，目前只有 瀑布流广告加载策略
 * date: 2019/1/21 15:18
 * author: ancun
 */
public abstract class AdStrategyScene extends AdScene {

    /**
     * 当前加载好的广告
     */
    protected RawAd activeAd;

    protected int adStatus;

    public final static int AD_STATUS_WAIT = 0;
    public final static int AD_STATUS_LOADING = 1;
    public final static int AD_STATUS_LOADED = 2;
    public final static int AD_STATUS_SHOWED = 3;

    /**
     * admob banner 自刷新
     */
    protected boolean supportBannerRefresh = false;


    protected int failCount;
    /**
     * 瀑布流广告加载策略
     */
    private WaterFallStrategy mWaterFallStrategy;

    protected long showTime;
    protected long loadedTime;

    public AdStrategyScene(CorApplication context, EntranceType entranceType) {
        super(context, entranceType);
        adStatus = AD_STATUS_WAIT;
        showTime = -1;
        loadedTime = -1;
//        storeId = -1;
        failCount = 0;
    }

    @Override
    public long getLoadedTime() {
        return loadedTime;
    }

    @Override
    public long getShowTime() {
        return showTime;
    }

    /**
     * 开始请求瀑布流广告
     */
    protected void onRequestAll() {

    }

    private void onWaterFallRequestFail() {//当瀑布流请求失败时，判断是否有配置广告仓库，如果有配置则使用广告仓库
//        HmAdPlace adPlace = getHmAdUnionPlace();
//        if (!adPlace.isStore() && !TextUtils.isEmpty(adPlace.getStoreId())) {
//            int storeId = Integer.parseInt(adPlace.getStoreId());
//            StoreScene storeScene = (StoreScene) EntranceType.STORE.getAdScene(storeId);
//            if (storeScene.isActive() && !storeScene.isTaken()) {//判断广告仓库是否有广告，并且没有被别的广告位使用
//                storeScene.take(mEntranceType);
//                this.storeId = storeId;
//                onADFilledAndLoaded(AdType.STORE);
//                return;
//            }
//        }
        onRequestAllFail();
    }

    /**
     * 请求瀑布流广告结束，并全部失败
     */
    protected void onRequestAllFail() {
        adStatus = AD_STATUS_WAIT;
        failCount++;
        release();
//        if (isAlwaysPreload() && failCount <= 3) {//请求失败时，且失败次数小于等于3，间隔（15秒x连续失败次数）重新请求
//            Message msg = new Message();
//            msg.what = R.id.msg_ad_always_reload;
//            msg.obj = mEntranceType.getName();
//            msg.arg1 = placeId;
//            imContext.handleMobDelayMessage(msg, failCount > 0 ? 15000 * failCount : 1500);
//        }
    }


    /**
     * 自刷新广告回调，通常是 banner （RawBannerAd）类型
     * 注意：该广告回调 通常与 onADFilled() / onADLoaded() 互斥
     *
     * @param type
     */
    protected void onADRefresh(AdType type) {

    }

    /**
     * 广告加载成功，仅在本中类调用
     *
     * @param adType
     */
    private void onADFilledAndLoaded(AdType adType) {
        adStatus = AD_STATUS_LOADED;
        loadedTime = System.currentTimeMillis();
        onADFilled(adType);
        onADLoaded(adType);
        failCount = 0;//当广告有填充时，连续失败次数清零
    }

    private void onADFilledAndLoaded(RawAd rawAd) {
        adStatus = AD_STATUS_LOADED;
        loadedTime = System.currentTimeMillis();
        onADFilled(getAdType(rawAd));
        onADLoaded(getAdType(rawAd));
        failCount = 0;//当广告有填充时，连续失败次数清零
    }

    private AdType getAdType(RawAd rawAd) {
        if (rawAd instanceof FbRawInterstitialAd) {
            return AdType.FB_INTERSTITIAL;
        } else if (rawAd instanceof FbRawNativeAd) {
            return AdType.FB_NATIVE;
        } else if (rawAd instanceof FbRawBannerAd) {
            return AdType.FB_BANNER;
        } else if (rawAd instanceof AmRawNativeAd) {
            return AdType.AM_NATIVE;
        } else if (rawAd instanceof AMRawBannerAd) {
            return AdType.AM_BANNER;
        } else if (rawAd instanceof AMRawInterstitialAd) {
            return AdType.AM_INTERSTITIAL;
        } else if (rawAd instanceof MpRawBannerAd) {
            return AdType.MP_BANNER;
        } else if (rawAd instanceof MpRawInterstitialAd) {
            return AdType.MP_INTERSTITIAL;
        } else if (rawAd instanceof MpRawNativeAd) {
            return AdType.MP_NATIVE;
        } else if (rawAd instanceof AMRawRewardAd) {
            return AdType.AM_REWARD;

        }
        return AdType.UNKNOWN;
    }

    /**
     * 广告显示成功
     *
     * @param adType
     */
    private void onADShowed(AdType adType) {
        adStatus = AD_STATUS_SHOWED;
        if (showTime == -1) {
            showTime = System.currentTimeMillis();
        }
        onADShow(adType);

    }

    /**
     * 回调外部，当前加载的广告需要 Activity
     * 请在 Activity 存续期间 请求和展示广告
     *
     * @param adType
     */
    protected void onADLoadByActivity(AdType adType) {

    }

    /**
     * 初始化广告视图 For Native ad or Banner ad
     *
     * @return
     */
    protected abstract CommonNativeAdCardView initCardView();

    /**
     * 请求广告成功，通知外部可以展示广告（瀑布流中的某一个）
     *
     * @param adType
     */
    protected abstract void onADLoaded(AdType adType);

    /**
     * 请求广告成功，通常用来记录与打点（瀑布流中的某一个）
     *
     * @param adType
     */
    protected abstract void onADFilled(AdType adType);

    /**
     * 广告被点击回调（瀑布流中的某一个）
     *
     * @param adType
     */
    public abstract void onADClick(AdType adType);

    /**
     * 广告被展示回调（瀑布流中的某一个）
     *
     * @param adType
     */
    public abstract void onADShow(AdType adType);

    /**
     * 广告被关闭回调（瀑布流中的某一个）
     *
     * @param adType
     */
    public abstract void onAdClose(AdType adType);

    /**
     * 开始请求广告（瀑布流中的某一个）
     *
     * @param adType
     */
    protected abstract void onADLoad(AdType adType);

    /**
     * 请求广告失败（瀑布流中的某一个）
     *
     * @param adType
     * @param code
     */
    protected abstract void onADFail(AdType adType, int code);

    /**
     * 激励广告 - 通过激励
     */
    protected abstract void onRewarded();

    /**
     * 激励广告 - 激励视频开始播放
     */
    protected abstract void onRewardedVideoStarted();


    protected AdsModule getADModule() {
        return (AdsModule) imContext.getSubModule(AdsModule.MODULE_KEY);
    }

    public RawAd getActiveAd() {
        return activeAd;
    }

    @Override
    public void release() {

        loadedTime = -1;
        showTime = -1;


//        if (storeId > 0) {//如果是使用广告仓库，则释放广告仓库
//            EntranceType.STORE.release(storeId);
//            storeId = -1;
//            if (activeAd != null) {//如果有请求到广告，则保留广告，把状态设为已加载
//                adStatus = AD_STATUS_LOADED;
//                loadedTime = System.currentTimeMillis();
//            } else {//如果没有请求到广告，释放瀑布流，状态设为等待
//                adStatus = AD_STATUS_WAIT;
//                releaseWaterFallStrategy();
//                destroyUnionPlaceAd();
//            }
//        } else {
        adStatus = AD_STATUS_WAIT;
        activeAd = null;
        releaseWaterFallStrategy();
        destroyUnionPlaceAd();
//        }

        if (isAlwaysPreload() && activeAd == null && failCount < 3) {

            final AdEvent message = new AdEvent();
            message.setWhat(R.id.msg_ad_always_reload);
            message.setObject(mEntranceType.getName());
            imContext.sendMessageDelay(message, failCount > 0 ? failCount * 15000 : 1500);

        }
    }

    /**
     * @return 该广告场景对应的 策略检查者
     */
    @Override
    public AStrategyExecutor getStrategyExecutor() {
        return ADStrategyManager.getExecutorByEntranceType(mEntranceType);
    }

    /**
     * @return 该广告场景对应的 HmAdUnionPlace
     */
    @Override
    public CorAdUnionPlace getHmAdUnionPlace() {
        return getADModule().getUnionAd(mEntranceType.getPid());
    }

    /**
     * @return 该广告场景是否过了保护时间
     */
    public boolean isOutProtect() {
        CorAdUnionPlace hmAdUnionPlace = getHmAdUnionPlace();
        if (hmAdUnionPlace == null) {
            return false;
        }
        AStrategyExecutor strategyExecutor = getStrategyExecutor();
        if (strategyExecutor == null) {
            return false;
        }
        return strategyExecutor.isOutProtect(hmAdUnionPlace);
    }

    private void releaseWaterFallStrategy() {
        if (mWaterFallStrategy != null) {
            mWaterFallStrategy.release();
        }
    }

    private void destroyUnionPlaceAd() {
        CorAdUnionPlace unionPlace = getHmAdUnionPlace();
        if (unionPlace != null) {
            unionPlace.destory();
        }

    }

    /**
     * 尝试使用广告仓库
     *
     * @return
     */
//    public boolean tryTakeStore() {
//        HmAdUnionPlace hmAdUnionPlace = getHmAdUnionPlace();
//        if (TextUtils.isEmpty(hmAdUnionPlace.getStoreId())) {
//            return false;
//        }
//
//        int storeId = Integer.parseInt(hmAdUnionPlace.getStoreId());
//        StoreScene storeScene = (StoreScene) EntranceType.STORE.getAdScene(storeId);
//
//        if (storeScene.isActive() && !storeScene.isTaken()) {
//            storeScene.take(mEntranceType);
//            this.storeId = storeId;
//            GARecordUtils.reportFunc(imContext, ADGAConstant.C_FUNC, "take", mEntranceType.getName() + "_store" + storeId);
//            return true;
//        }
//
//        return false;
//    }

    /**
     * @return 装配广告视图 For Native ad or Banner ad
     */
    @Override
    public View getAdView() {
//        if (storeId > 0) {
//            return EntranceType.STORE.getAdView(storeId);
//        }
        CorAdUnionPlace unionPlace = getHmAdUnionPlace();
        if (unionPlace == null || activeAd == null) {
            return null;
        }

        if (activeAd instanceof RawBannerAd) {
            return getRawBannerAdView((RawBannerAd) activeAd);

        } else if (activeAd instanceof MpRawNativeAd) {
            return getMpNativeAdView(unionPlace, (MpRawNativeAd) activeAd);

        } else if (activeAd instanceof RawNativeAd) {
            return getRawNativeAdView(unionPlace, (RawNativeAd) activeAd);

        } else {
            return null;
        }

    }

    private View getRawBannerAdView(final RawBannerAd rawBannerAd) {
        final View bannerView = rawBannerAd.getBannerView();
        if (bannerView == null) {
            return null;
        }
        if (bannerView.getParent() != null) {
            ViewGroup viewGroup = (ViewGroup) bannerView.getParent();
            viewGroup.removeAllViews();
        }
        bannerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                bannerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                switch (rawBannerAd.getPlatform()) {
                    case "am":
                        onADShowed(AdType.AM_BANNER);
                        break;

                    case "am_new":
                        onADShowed(AdType.AM_NEW_BANNER);
                        break;

                    case "mp":
                        onADShowed(AdType.MP_BANNER);
                        break;

                    default:
                        break;
                }

            }
        });
        return bannerView;
    }

    private View getMpNativeAdView(CorAdPlace adPlace, MpRawNativeAd mpRawNativeAd) {
        SmallNativeAdCardView adCardView = new SmallNativeAdCardView(imContext);
        onADShowed(AdType.MP_NATIVE);
        adCardView.bindRawAd(adPlace, mpRawNativeAd);

        return adCardView;
    }

    private View getRawNativeAdView(CorAdPlace adPlace, RawNativeAd rawNativeAd) {
        try {
            CommonNativeAdCardView adCardView = null;
            adCardView = initCardView();
            if (adCardView == null) {
                return null;
            }
            adCardView.setEventListener(new AdCardEventListener() {
                @Override
                public void onAdCardShow(CorAdPlace hmNativeAdPlace) {
                    //原生广告，在部分界面，会奔溃。
                    if (activeAd == null) {
                        return;
                    }

                    switch (activeAd.getPlatform()) {
                        case "fb":
                            onADShowed(AdType.FB_NATIVE);
                            break;

                        case "am":
                            onADShowed(AdType.AM_NATIVE);
                            break;

                        case "am_new":
                            onADShowed(AdType.AM_NEW_NATIVE);
                            break;

                        case "mp":
                            onADShowed(AdType.MP_NATIVE);
                            break;

                        case "da":
                            onADShowed(AdType.DA_NATIVE);
                            break;

                        default:
                            break;
                    }
                }
            });
            adCardView.bindRawAd(adPlace, rawNativeAd);
            return adCardView;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected String getSimpleEntranceName() {
        String store = "";
//        if (storeId > 0) {
//            store = "_store" + storeId;
//        }

        return mEntranceType.getName() + store;
    }

    protected String getFormatEntranceNameByRawAd() {
        String adapterName = "";
        if (activeAd instanceof AMRawBannerAd) {
            try {
                adapterName = ((AMRawBannerAd) activeAd).getMediationAdapterClassName();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (activeAd instanceof AMRawInterstitialAd) {
            try {
                adapterName = ((AMRawInterstitialAd) activeAd).getMediationAdapterClassName();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (activeAd instanceof AmRawNativeAd) {
            try {
                adapterName = ((AmRawNativeAd) activeAd).getMediationAdapterClassName();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!TextUtils.isEmpty(adapterName) && !adapterName.toLowerCase().contains("admob") && !adapterName.toLowerCase().equals("error")) {
            adapterName = "_" + adapterName;
        } else {
            adapterName = "";
        }

        String store = "";
//        if (storeId > 0) {
//            store = "_store" + storeId;
//        }


        return mEntranceType.getName() + adapterName + store;
    }

    protected String getFormatAreaNameByRawAd() {
        String adapterName = "";
        if (activeAd instanceof AMRawBannerAd) {
            try {
                adapterName = ((AMRawBannerAd) activeAd).getMediationAdapterClassName();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (activeAd instanceof AMRawInterstitialAd) {
            try {
                adapterName = ((AMRawInterstitialAd) activeAd).getMediationAdapterClassName();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (activeAd instanceof AmRawNativeAd) {
            try {
                adapterName = ((AmRawNativeAd) activeAd).getMediationAdapterClassName();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!TextUtils.isEmpty(adapterName) && !adapterName.toLowerCase().contains("admob") && !adapterName.toLowerCase().equals("error")) {
            adapterName = "_" + adapterName;
        } else {
            adapterName = "";
        }

        return mEntranceType.getCategory().getName() + adapterName;
    }

    protected int getVideoLayoutId() {
        return R.layout.small_mp_video_ad_card_layout;
    }

    protected int getLayoutId() {
        return R.layout.small_mp_card_layout;
    }

    /**
     * 使用瀑布流策略 加载 配置中的 ads 广告流
     *
     * @param unionPlace
     * @return
     */
    protected boolean loadUnionPlace(CorAdUnionPlace unionPlace) {
        if (unionPlace == null) {
            return false;
        }

        List<RawAd> rawAdList = unionPlace.getRawAdList();
        if (rawAdList == null) {
            return false;
        }

        if (activeAd != null && adStatus == AD_STATUS_LOADED) {
            onADFilledAndLoaded(activeAd);
            return true;
        }

        if (mWaterFallStrategy == null) {
            mWaterFallStrategy = new WaterFallStrategy();
        }

        return mWaterFallStrategy.load(unionPlace);
    }

    /**
     * 使用瀑布流策略 加载 配置中的 ads 广告流
     *
     * @param unionPlace
     * @return
     */
    protected boolean reloadUnionPlace(CorAdUnionPlace unionPlace) {
        if (unionPlace == null) {
            return false;
        }
        List<RawAd> rawAdList = unionPlace.getRawAdList();
        if (rawAdList == null) {
            return false;
        }

        if (mWaterFallStrategy == null) {
            mWaterFallStrategy = new WaterFallStrategy();
        }

        return mWaterFallStrategy.reload(unionPlace);
    }

    /**
     * @return 检查策略是否通过
     */
    @Override
    public boolean check() {
        CorAdUnionPlace hmAdUnionPlace = getHmAdUnionPlace();
        if (hmAdUnionPlace == null) {
            return false;
        }
        AStrategyExecutor strategyExecutor = getStrategyExecutor();
        if (strategyExecutor == null) {
            return false;
        }
        return strategyExecutor.check(hmAdUnionPlace);
    }

    @Override
    public boolean isActive() {
        return activeAd != null;
    }

    @Override
    public boolean isShowed() {
        return adStatus == AD_STATUS_SHOWED;
    }

    @Override
    public boolean isWait() {
        return adStatus == AD_STATUS_WAIT;
    }

    public int getAdStatus() {
        return adStatus;
    }

    /**
     * desc: 瀑布流广告加载 策略
     * date: 2019/1/21 15:53
     * author: ancun
     */
    public class WaterFallStrategy {

        private int currentAdIndex;
        private List<RawAd> execAdList;

        private WaterFallStrategy() {
            currentAdIndex = 0;
            execAdList = new ArrayList<>();
        }

        public void release() {
            execAdList.clear();
            currentAdIndex = 0;
        }

        public boolean load(CorAdUnionPlace unionPlace) {
            if (adStatus == AD_STATUS_LOADING) {
                return true;
            }
            List<RawAd> adList = unionPlace.getRawAdList();
            //每次 load 清空 execAdList
            execAdList.clear();
            //瀑布流广告请求开始，index 置 0
            currentAdIndex = 0;
            //添加瀑布流广告
            execAdList.addAll(adList);
            onRequestAll();
            if (loadWaterFallRawAd()) {
                adStatus = AD_STATUS_LOADING;
                return true;
            } else {
                return false;
            }
        }

        public boolean reload(CorAdUnionPlace unionPlace) {
            return loadWaterFallRawAd();
        }

        public boolean loadWaterFallRawAd() {
            if (currentAdIndex >= execAdList.size()) {
                //瀑布流广告请求结束，index 置 0
                currentAdIndex = 0;
                return false;
            }

            RawAd ad = execAdList.get(currentAdIndex);
            if (!loadRawAd(ad)) {
                currentAdIndex++;
                return loadWaterFallRawAd();
            } else {
                return true;
            }
        }

        public boolean loadRawAd(RawAd ad) {
            if (ad instanceof AmRawNativeAd) {
                return loadAmNativeAd((AmRawNativeAd) ad);

            } else if (ad instanceof AMRawBannerAd) {
                return loadAmBannerAd((AMRawBannerAd) ad);

            } else if (ad instanceof AMRawInterstitialAd) {
                return loadAmInterstitialAd((AMRawInterstitialAd) ad);

            } else if (ad instanceof AMRawRewardAd) {
                return loadAmRewardAd((AMRawRewardAd) ad);

            } else if (ad instanceof MpRawNativeAd) {
                return loadMpRawNativeAd((MpRawNativeAd) ad);

            } else if (ad instanceof MpRawBannerAd) {
                return loadMpRawBannerAd((MpRawBannerAd) ad);

            } else if (ad instanceof MpRawInterstitialAd) {
                return loadMpRawInterstitialAd((MpRawInterstitialAd) ad);

//            } else if (ad instanceof FluctRawIntersitialAd) {
//                return loadFtInterstitialAd((FluctRawIntersitialAd) ad);
//
//            } else if (ad instanceof FluctRawRewardAd) {
//                return loadFtRawRewardAd((FluctRawRewardAd) ad);
//
//            } else if (ad instanceof AmUniRawNativeAd) {
//                return loadAmNRawNativeAd((AmUniRawNativeAd) ad);
//
//            } else if (ad instanceof AMNRawBannerAd) {
//                return loadAmNRawBannerAd((AMNRawBannerAd) ad);
//
//            } else if (ad instanceof AMNRawInterstitialAd) {
//                return loadAmNRawInterstitialAd((AMNRawInterstitialAd) ad);
//
//            } else if (ad instanceof AMNRawRewardAd) {
//                return loadAmNRawRewardAd((AMNRawRewardAd) ad);

            } else if (ad instanceof FbRawNativeAd) {
                return loadFbRawNativeAd((FbRawNativeAd) ad);

            } else if (ad instanceof WebInterstitialAd) {
                return loadWebInterstitialAd((WebInterstitialAd) ad);

            } else if (ad instanceof DirectInterstitialAd) {
                return loadDirectInterstitialAd((DirectInterstitialAd) ad);

            } else if (ad instanceof DirectRawBannerAd) {
                return loadDirectBannerAd((DirectRawBannerAd) ad);

            } else if (ad instanceof FbRawInterstitialAd) {
                return loadFbRawInterstitialAd((FbRawInterstitialAd) ad);

//            }
//            else if (ad instanceof StoreRawInterstitialAd) {
//                return loadStoreRawInterstitialAd((StoreRawInterstitialAd) ad);
//            } else if (ad instanceof StoreRawBannerAd) {
//                return loadStoreRawBannerAd((StoreRawBannerAd) ad);
            } else {
                return false;
            }
        }

//        //加载仓库广告
//        private boolean loadStoreRawInterstitialAd(final StoreRawInterstitialAd interstitialAd) {
//            int id = Integer.parseInt(interstitialAd.getId());
//            onADLoad(AdType.STORE);
//            if (EntranceType.STORE.isActive(id) && !((StoreScene) EntranceType.STORE.getAdScene(id)).isTaken()) {//仓库广告可用且未被占用
//                storeId = id;
//                activeAd = ((StoreScene) EntranceType.STORE.getAdScene(id)).take(mEntranceType);
//                onADFilledAndLoaded(AdType.STORE);
//                return true;
//            } else {
//                onADFail(AdType.STORE, -1);
//                return false;
//            }
//        }
//
//        private boolean loadStoreRawBannerAd(final StoreRawBannerAd bannerAd) {
//            int id = Integer.parseInt(bannerAd.getId());
//            onADLoad(AdType.STORE);
//            if (EntranceType.STORE.isActive(id) && !((StoreScene) EntranceType.STORE.getAdScene(id)).isTaken()) {//仓库广告可用且未被占用
//                storeId = id;
//                activeAd = ((StoreScene) EntranceType.STORE.getAdScene(id)).take(mEntranceType);
//                onADFilledAndLoaded(AdType.STORE);
//                return true;
//            } else {
//                onADFail(AdType.STORE, -1);
//                return false;
//            }
//        }

        private boolean loadFbRawInterstitialAd(final FbRawInterstitialAd interstitialAd) {
            Context windowsToken = CorApplication.getInstance().getMWindowToken();
            if (!Category.A.equals(mEntranceType.getCategory()) && !(windowsToken instanceof Activity)) {
                onADLoadByActivity(AdType.FB_INTERSTITIAL);
                return true;
            }

            if (interstitialAd == null) {
                return false;
            }

            if (interstitialAd.isLoading()) {
                return true;
            }


            if (interstitialAd.isLoaded() && !AppInfoUtil.isInSelfApp()) {
                activeAd = interstitialAd;
                onADFilledAndLoaded(AdType.FB_INTERSTITIAL);
                return true;
            }


            interstitialAd.setAdListener(new InterstitialAdLoadListener() {
                @Override
                public void onError(RawInterstitialAd rawInterstitialAd, int i, String s) {
                    onADFail(AdType.FB_INTERSTITIAL, i);
                    currentAdIndex++;
                    if (!loadWaterFallRawAd()) {
                        onWaterFallRequestFail();
                    }
                }

                @Override
                public void onAdLoaded(RawInterstitialAd rawInterstitialAd) {
                    activeAd = interstitialAd;
                    onADFilledAndLoaded(AdType.FB_INTERSTITIAL);
                }

                @Override
                public void onShow(RawInterstitialAd rawInterstitialAd) {
                    onADShowed(AdType.FB_INTERSTITIAL);
                }

                @Override
                public void onClick(RawInterstitialAd rawInterstitialAd) {
                    onADClick(AdType.FB_INTERSTITIAL);
                }

                @Override
                public void onClose(RawInterstitialAd rawInterstitialAd) {
                    onAdClose(AdType.FB_INTERSTITIAL);
                }
            });

            interstitialAd.load((Activity) windowsToken);
            onADLoad(AdType.FB_INTERSTITIAL);


            return true;
        }

        private boolean loadDirectBannerAd(final DirectRawBannerAd ad) {
            ad.setAdListener(new BannerLoadListener() {
                @Override
                public void onError(RawBannerAd rawBannerAd, int i, String s) {

                }

                @Override
                public void onAdLoaded(RawBannerAd rawBannerAd) {
                    activeAd = ad;
                    onADLoaded(AdType.DIRECT_BANNER);
                }

                @Override
                public void onClick(RawBannerAd rawBannerAd) {

                }

                @Override
                public void onInstall(RawBannerAd rawBannerAd, String s) {

                }

                @Override
                public void onLoggingImpression(RawBannerAd rawBannerAd) {

                }
            });

            ad.load();


            return true;
        }

        /**
         * direct interstitial
         *
         * @param ad
         * @return
         */
        private boolean loadDirectInterstitialAd(final DirectInterstitialAd ad) {
            ad.setAdListener(new InterstitialAdLoadListener() {
                @Override
                public void onError(RawInterstitialAd rawInterstitialAd, int i, String s) {

                }

                @Override
                public void onAdLoaded(RawInterstitialAd rawInterstitialAd) {
                    activeAd = ad;
                    onADLoaded(AdType.DIRECT_INTERSTITIAL);
                }

                @Override
                public void onShow(RawInterstitialAd rawInterstitialAd) {
                    onADShowed(AdType.DIRECT_INTERSTITIAL);
                }

                @Override
                public void onClick(RawInterstitialAd rawInterstitialAd) {
                    onADClick(AdType.DIRECT_INTERSTITIAL);
                }

                @Override
                public void onClose(RawInterstitialAd rawInterstitialAd) {
                    onAdClose(AdType.DIRECT_INTERSTITIAL);
                }
            });

            ad.load();

            return true;
        }

        /**
         * web interstitial
         *
         * @param ad
         * @return
         */
        private boolean loadWebInterstitialAd(final WebInterstitialAd ad) {
            ad.setAdListener(new InterstitialAdLoadListener() {
                @Override
                public void onError(RawInterstitialAd rawInterstitialAd, int i, String s) {

                }

                @Override
                public void onAdLoaded(RawInterstitialAd rawInterstitialAd) {
                    activeAd = ad;
                    onADLoaded(AdType.WEB_INTERSTITIAL);
                }

                @Override
                public void onShow(RawInterstitialAd rawInterstitialAd) {
                    onADShowed(AdType.WEB_INTERSTITIAL);
                }

                @Override
                public void onClick(RawInterstitialAd rawInterstitialAd) {
                    onADClick(AdType.WEB_INTERSTITIAL);
                }

                @Override
                public void onClose(RawInterstitialAd rawInterstitialAd) {
                    onAdClose(AdType.WEB_INTERSTITIAL);
                }
            });
            ad.load();

            return true;
        }


        /**
         * AdMob Native Ad
         */
        private boolean loadAmNativeAd(final AmRawNativeAd ad) {
            if (ad == null) {
                return false;
            }
            try {
//                ad.setAdmobAdListener(new AdListener() {
//                    @Override
//                    public void onAdFailedToLoad(int i) {
//                        super.onAdFailedToLoad(i);
//                        onADFail(AdType.AM_NATIVE, i);
//                        currentAdIndex++;
//                        if (!loadWaterFallRawAd()) {
//                            onWaterFallRequestFail();
//                        }
//                    }
//
//                    @Override
//                    public void onAdLeftApplication() {
//                        super.onAdLeftApplication();
//                        //应用外admob高级原生广告所有点击次数
//                        onADClick(AdType.AM_NATIVE);
//                    }
//                });
                ad.setAdListener(new NativeAdLoadListener() {
                    @Override
                    public void onError(RawNativeAd rawNativeAd, int i, String s) {
                        onADFail(AdType.AM_NATIVE, i);
                        currentAdIndex++;
                        if (!loadWaterFallRawAd()) {
                            onWaterFallRequestFail();
                        }
                    }

                    @Override
                    public void onAdLoaded(RawNativeAd rawNativeAd) {
                        activeAd = rawNativeAd;
                        onADFilledAndLoaded(AdType.AM_NATIVE);
                    }

                    @Override
                    public void onClick(RawNativeAd rawNativeAd) {
                        //应用外admob高级原生广告所有点击次数
                        onADClick(AdType.AM_NATIVE);
                    }

                    @Override
                    public void onInstall(RawNativeAd rawNativeAd, String s) {

                    }

                    @Override
                    public void onLoggingImpression(RawNativeAd rawNativeAd) {

                    }
                });
                ad.load();
                onADLoad(AdType.AM_NATIVE);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        /**
         * AdMob Banner Ad
         */
        private boolean loadAmBannerAd(final RawBannerAd bannerAd) {
            if (bannerAd == null) {
                return false;
            }

            if (bannerAd.isLoading()) {
                return false;
            }


            bannerAd.setAdListener(new BannerLoadListener() {
                @Override
                public void onError(RawBannerAd rawBannerAd, int i, String s) {
                    onADFail(AdType.AM_BANNER, i);
                    currentAdIndex++;
                    if (!loadWaterFallRawAd()) {
                        onWaterFallRequestFail();
                    }
                }

                @Override
                public void onAdLoaded(RawBannerAd rawBannerAd) {
                    if (supportBannerRefresh && activeAd instanceof RawBannerAd) {
                        onADRefresh(AdType.AM_BANNER);
                    } else {
                        activeAd = bannerAd;
                        onADFilledAndLoaded(AdType.AM_BANNER);
                    }
                }

                @Override
                public void onClick(RawBannerAd rawBannerAd) {
                    onADClick(AdType.AM_BANNER);
                }

                @Override
                public void onInstall(RawBannerAd rawBannerAd, String s) {

                }

                @Override
                public void onLoggingImpression(RawBannerAd rawBannerAd) {

                }
            });

//            bannerAd.setAdListener(new AdListener() {
//                @Override
//                public void onAdClosed() {
//                    onAdClose(AdType.AM_BANNER);
//                }
//
//                @Override
//                public void onAdFailedToLoad(int i) {
//                    super.onAdFailedToLoad(i);
//                    onADFail(AdType.AM_BANNER, i);
//                    currentAdIndex++;
//                    if (!loadWaterFallRawAd()) {
//                        onWaterFallRequestFail();
//                    }
//                }
//
//                @Override
//                public void onAdLeftApplication() {
//                    super.onAdLeftApplication();
//                    onADClick(AdType.AM_BANNER);
//                }
//
//                @Override
//                public void onAdOpened() {
//                    super.onAdOpened();
//                }
//
//                @Override
//                public void onAdLoaded() {
//                    super.onAdLoaded();
//                    if (supportBannerRefresh && activeAd instanceof RawBannerAd) {
//                        onADRefresh(AdType.AM_BANNER);
//                    } else {
//                        activeAd = bannerAd;
//                        onADFilledAndLoaded(AdType.AM_BANNER);
//                    }
//                }
//            });

            bannerAd.load();
            onADLoad(AdType.AM_BANNER);

            return true;
        }

        /**
         * AdMob Interstitial Ad
         */
        private boolean loadAmInterstitialAd(final AMRawInterstitialAd interstitialAd) {
            Context windowsToken = CorApplication.getInstance().getMWindowToken();
            if (!Category.A.equals(mEntranceType.getCategory()) && !(windowsToken instanceof Activity)) {
                onADLoadByActivity(AdType.AM_INTERSTITIAL);
                return true;
            }

            if (interstitialAd == null) {
                return false;
            }

            if (interstitialAd.isLoading()) {
                return false;
            }

            if (interstitialAd.isLoaded() && !AppInfoUtil.isInSelfApp()) {
                activeAd = interstitialAd;
                onADFilledAndLoaded(AdType.AM_INTERSTITIAL);
                return true;
            }

            interstitialAd.setAdListener(new InterstitialAdLoadListener() {
                @Override
                public void onError(RawInterstitialAd rawInterstitialAd, int i, String s) {
                    onADFail(AdType.AM_INTERSTITIAL, i);
                    currentAdIndex++;
                    if (!loadWaterFallRawAd()) {
                        onWaterFallRequestFail();
                    }
                }

                @Override
                public void onAdLoaded(RawInterstitialAd rawInterstitialAd) {
                    activeAd = interstitialAd;
                    onADFilledAndLoaded(AdType.AM_INTERSTITIAL);
                }

                @Override
                public void onShow(RawInterstitialAd rawInterstitialAd) {
                    onADShowed(AdType.AM_INTERSTITIAL);
                }

                @Override
                public void onClick(RawInterstitialAd rawInterstitialAd) {
                    onADClick(AdType.AM_INTERSTITIAL);
                }

                @Override
                public void onClose(RawInterstitialAd rawInterstitialAd) {
                    onAdClose(AdType.AM_INTERSTITIAL);
                }
            });

            interstitialAd.load();
            onADLoad(AdType.AM_INTERSTITIAL);


            return true;
        }

        /**
         * AdMob Reward Ad
         */
        private boolean loadAmRewardAd(final AMRawRewardAd rewardAd) {
            if (rewardAd == null) {
                return false;
            }

            if (rewardAd.isLoaded()) {
                activeAd = rewardAd;
                onADLoaded(AdType.AM_REWARD);
                return true;
            }

            if (rewardAd.isLoading()) {
                return false;
            }

            rewardAd.setAdListener(new RewardAdListener() {
                @Override
                public void onRewardedAdLoaded(RawRewardAd ad) {
                    activeAd = rewardAd;
                    onADLoaded(AdType.AM_REWARD);
                }

                @Override
                public void onRewardedAdOpened(RawRewardAd ad) {
                    onADShowed(AdType.AM_REWARD);
                }

                @Override
                public void onRewardedVideoStarted(RawRewardAd ad) {
                    AdStrategyScene.this.onRewardedVideoStarted();
                }

                @Override
                public void onRewardedAdClosed(RawRewardAd ad) {
                    onAdClose(AdType.AM_REWARD);
                }

                @Override
                public void onRewarded(RawRewardAd ad) {
                    AdStrategyScene.this.onRewarded();
                }

                @Override
                public void onRewardedAdLeftApplication(RawRewardAd ad) {
                    onADClick(AdType.AM_REWARD);
                }

                @Override
                public void onRewardedAdFailedToLoad(RawRewardAd ad, int code, String msg) {
                    onADFail(AdType.AM_REWARD, code);
                    currentAdIndex++;
                    if (!loadWaterFallRawAd()) {
                        onWaterFallRequestFail();
                    }

                }
            });

            rewardAd.load();
            onADLoad(AdType.AM_REWARD);


            return true;
        }

        /**
         * MoPub Native Ad
         */
        private boolean loadMpRawNativeAd(final MpRawNativeAd ad) {
            if (ad == null) {
                return false;
            }
            ad.setAdListener(new NativeAdLoadListener() {
                @Override
                public void onError(RawNativeAd ad, int code, String msg) {
                    onADFail(AdType.MP_NATIVE, code);
                    currentAdIndex++;
                    if (!loadWaterFallRawAd()) {
                        onWaterFallRequestFail();
                    }
                }

                @Override
                public void onAdLoaded(RawNativeAd ad) {
                    activeAd = ad;
                    onADFilledAndLoaded(AdType.MP_NATIVE);
                }

                @Override
                public void onClick(RawNativeAd ad) {
                    onADClick(AdType.MP_NATIVE);
                }

                @Override
                public void onInstall(RawNativeAd ad, String packageName) {
                }

                @Override
                public void onLoggingImpression(RawNativeAd ad) {
                }

            });
            ad.setLayoutId(getLayoutId());
            ad.setVideoLayoutId(getVideoLayoutId());
            ad.load();
            onADLoad(AdType.MP_NATIVE);

            return true;
        }

        /**
         * MoPub Banner Ad
         */
        private boolean loadMpRawBannerAd(final MpRawBannerAd ad) {
            if (ad == null) {
                return false;
            }
            ad.setAdListener(new BannerLoadListener() {

                @Override
                public void onError(RawBannerAd ad, int i, String msg) {
                    onADFail(AdType.MP_BANNER, i);
                    currentAdIndex++;
                    if (!loadWaterFallRawAd()) {
                        onWaterFallRequestFail();
                    }
                }

                @Override
                public void onAdLoaded(RawBannerAd ad) {
                    activeAd = ad;
                    onADFilledAndLoaded(AdType.MP_BANNER);
                }

                @Override
                public void onClick(RawBannerAd ad) {
                    onADClick(AdType.MP_BANNER);
                }

                @Override
                public void onInstall(RawBannerAd ad, String packageName) {

                }

                @Override
                public void onLoggingImpression(RawBannerAd ad) {

                }
            });

            ad.load();
            onADLoad(AdType.MP_BANNER);

            return true;
        }

        /**
         * MoPub Interstitial Ad
         */
        private boolean loadMpRawInterstitialAd(final MpRawInterstitialAd ad) {
            if (ad == null) {
                return false;
            }
            ad.setAdListener(new InterstitialAdLoadListener() {
                @Override
                public void onError(RawInterstitialAd ad, int code, String msg) {
                    onADFail(AdType.MP_INTERSTITIAL, code);
                    currentAdIndex++;
                    if (!loadWaterFallRawAd()) {
                        onWaterFallRequestFail();
                    }
                }

                @Override
                public void onAdLoaded(RawInterstitialAd ad) {
                    activeAd = ad;
                    onADFilledAndLoaded(AdType.MP_INTERSTITIAL);
                }

                @Override
                public void onShow(RawInterstitialAd ad) {
                    onADShowed(AdType.MP_INTERSTITIAL);
                }

                @Override
                public void onClick(RawInterstitialAd ad) {
                    onADClick(AdType.MP_INTERSTITIAL);
                }

                @Override
                public void onClose(RawInterstitialAd ad) {
                    onAdClose(AdType.MP_INTERSTITIAL);
                }
            });

            ad.load();
            onADLoad(AdType.MP_INTERSTITIAL);

            return true;
        }

        /**
         * FT Interstitial Ad
         */
//        private boolean loadFtInterstitialAd(final FluctRawIntersitialAd ad) {
//            if (ad == null) {
//                return false;
//            }
//
//            ad.setAdListener(new InterstitialAdLoadListener() {
//
//                @Override
//                public void onAdLoaded(RawInterstitialAd ad) {
//                    activeAd = ad;
//                    onADFilledAndLoaded(AdType.FT_INTERSTITIAL);
//                }
//
//                @Override
//                public void onError(RawInterstitialAd ad, int code, String msg) {
//                    onADFail(AdType.FT_INTERSTITIAL, code);
//                    currentAdIndex++;
//                    if (!loadWaterFallRawAd()) {
//                        onWaterFallRequestFail();
//                    }
//                }
//
//                @Override
//                public void onShow(RawInterstitialAd ad) {
//                    onADShowed(AdType.FT_INTERSTITIAL);
//                }
//
//                @Override
//                public void onClick(RawInterstitialAd ad) {
//                    onADClick(AdType.FT_INTERSTITIAL);
//                }
//
//                @Override
//                public void onClose(RawInterstitialAd ad) {
//                    onAdClose(AdType.FT_INTERSTITIAL);
//                }
//            });
//
//            try {
//                onADLoad(AdType.FT_INTERSTITIAL);
//                ad.load();
//            } catch (AdActionException e) {
//                e.printStackTrace();
//            }
//
//            return true;
//        }

        /**
         * FT Reward Ad
         */
//        private boolean loadFtRawRewardAd(FluctRawRewardAd ad) {
//            Context windowsToken = AMApplication.getInstance().getMWindowToken();
//            if (!Category.A.equals(mEntranceType.getCategory()) && !(windowsToken instanceof Activity)) {
//                onADLoadByActivity(AdType.AM_INTERSTITIAL);
//                return true;
//            }
//
//            ad.setAdListener(new RewardAdListener() {
//                @Override
//                public void onRewardedAdFailedToLoad(RawRewardAd ad, int code, String msg) {
//                    currentAdIndex++;
//                    onADFail(AdType.FT_REWARD, code);
//                }
//
//                @Override
//                public void onRewardedAdLoaded(RawRewardAd ad) {
//                    activeAd = ad;
//                    onADFilledAndLoaded(AdType.FT_REWARD);
//                }
//
//                @Override
//                public void onRewardedAdOpened(RawRewardAd ad) {
//                    onADShowed(AdType.FT_REWARD);
//                }
//
//                @Override
//                public void onRewardedVideoStarted(RawRewardAd ad) {
//                    AdStrategyScene.this.onRewardedVideoStarted();
//                }
//
//                @Override
//                public void onRewardedAdLeftApplication(RawRewardAd ad) {
//                }
//
//                @Override
//                public void onRewardedAdClosed(RawRewardAd ad) {
//                    onAdClose(AdType.FT_REWARD);
//                }
//
//                @Override
//                public void onRewarded(RawRewardAd ad) {
//                    AdStrategyScene.this.onRewarded();
//                }
//            });
//
//            try {
//                onADLoad(AdType.FT_REWARD);
//                ad.load((Activity) windowsToken);
//            } catch (AdActionException e) {
//                e.printStackTrace();
//            }
//
//            return true;
//        }

        /**
         * AdMob(Publisher) Native Ad
         */
//        private boolean loadAmNRawNativeAd(AmUniRawNativeAd ad) {
//            if (ad == null) {
//                return false;
//            }
//
//            try {
//                ad.setAdmobAdListener(new AdListener() {
//                    @Override
//                    public void onAdFailedToLoad(int i) {
//                        super.onAdFailedToLoad(i);
//                        onADFail(AdType.AM_NEW_NATIVE, i);
//                        currentAdIndex++;
//                        if (!loadWaterFallRawAd()) {
//                            onWaterFallRequestFail();
//                        }
//                    }
//
//                    @Override
//                    public void onAdLeftApplication() {
//                        super.onAdLeftApplication();
//                        //应用外admob高级原生广告所有点击次数
//                        onADClick(AdType.AM_NEW_NATIVE);
//                    }
//                });
//                ad.setAdListener(new NativeAdLoadListener() {
//                    @Override
//                    public void onError(RawNativeAd rawNativeAd, int i, String s) {
//                        //admob高级原生不会执行这个方法
//                    }
//
//                    @Override
//                    public void onAdLoaded(RawNativeAd rawNativeAd) {
//                        activeAd = rawNativeAd;
//                        onADFilledAndLoaded(AdType.AM_NEW_NATIVE);
//                    }
//
//                    @Override
//                    public void onClick(RawNativeAd rawNativeAd) {
//                        //admob高级原生不会执行这个方法
//                    }
//
//                    @Override
//                    public void onInstall(RawNativeAd rawNativeAd, String s) {
//                        //admob高级原生不会执行这个方法
//                    }
//
//                    @Override
//                    public void onLoggingImpression(RawNativeAd rawNativeAd) {
//                        //admob高级原生不会执行这个方法
//                    }
//                });
//                ad.load();
//                onADLoad(AdType.AM_NEW_NATIVE);
//            } catch (AdActionException e) {
//                e.printStackTrace();
//                return false;
//            }
//
//            return true;
//        }

        /**
         * AdMob(Publisher) Banner Ad
         */
//        private boolean loadAmNRawBannerAd(final AMNRawBannerAd bannerAd) {
//            if (bannerAd == null) {
//                return false;
//            }
//
//            try {
//                if (bannerAd.isLoading()) {
//                    return false;
//                }
//            } catch (AdActionException e) {
//                e.printStackTrace();
//            }
//
//            bannerAd.setAdListener(new AdListener() {
//                @Override
//                public void onAdClosed() {
//                    onAdClose(AdType.AM_NEW_BANNER);
//                }
//
//                @Override
//                public void onAdFailedToLoad(int i) {
//                    super.onAdFailedToLoad(i);
//                    onADFail(AdType.AM_NEW_BANNER, i);
//                    currentAdIndex++;
//                    if (!loadWaterFallRawAd()) {
//                        onWaterFallRequestFail();
//                    }
//                }
//
//                @Override
//                public void onAdLeftApplication() {
//                    super.onAdLeftApplication();
//                    onADClick(AdType.AM_NEW_BANNER);
//                }
//
//                @Override
//                public void onAdOpened() {
//                    super.onAdOpened();
//                }
//
//                @Override
//                public void onAdLoaded() {
//                    super.onAdLoaded();
//                    if (supportBannerRefresh && activeAd instanceof RawBannerAd) {
//                        onADRefresh(AdType.AM_NEW_BANNER);
//                    } else {
//                        activeAd = bannerAd;
//                        onADFilledAndLoaded(AdType.AM_NEW_BANNER);
//                    }
//                }
//            });
//            try {
//                bannerAd.load();
//                onADLoad(AdType.AM_NEW_BANNER);
//            } catch (AdActionException e) {
//                e.printStackTrace();
//                return false;
//            }
//            return true;
//        }

        /**
         * AdMob(Publisher) Interstitial Ad
         */
//        private boolean loadAmNRawInterstitialAd(final AMNRawInterstitialAd interstitialAd) {
//            Context windowsToken = AMApplication.getInstance().getMWindowToken();
//            if (!Category.A.equals(mEntranceType.getCategory()) && !(windowsToken instanceof Activity)) {
//                onADLoadByActivity(AdType.AM_INTERSTITIAL);
//                return true;
//            }
//
//            if (interstitialAd == null) {
//                return false;
//            }
//
//            try {
//                if (interstitialAd.isLoading()) {
//                    return false;
//                }
//            } catch (AdActionException e) {
//                e.printStackTrace();
//            }
//
//            try {
//                if (interstitialAd.isLoaded() && !AppInfoUtil.isInSelfApp()) {
//                    activeAd = interstitialAd;
//                    onADFilledAndLoaded(AdType.AM_NEW_INTERSTITIAL);
//                    return true;
//                }
//            } catch (AdActionException e) {
//                e.printStackTrace();
//            }
//            interstitialAd.setAmAdListener(new AdListener() {
//                @Override
//                public void onAdClosed() {
//                    super.onAdClosed();
//                    onAdClose(AdType.AM_NEW_INTERSTITIAL);
//                }
//
//                @Override
//                public void onAdFailedToLoad(int i) {
//                    super.onAdFailedToLoad(i);
//                    onADFail(AdType.AM_NEW_INTERSTITIAL, i);
//                    currentAdIndex++;
//                    if (!loadWaterFallRawAd()) {
//                        onWaterFallRequestFail();
//                    }
//                }
//
//                @Override
//                public void onAdLeftApplication() {
//                    super.onAdLeftApplication();
//                    onADClick(AdType.AM_NEW_INTERSTITIAL);
//                }
//
//                @Override
//                public void onAdOpened() {
//                    onADShowed(AdType.AM_NEW_INTERSTITIAL);
//                }
//
//                @Override
//                public void onAdLoaded() {
//                    activeAd = interstitialAd;
//                    onADFilledAndLoaded(AdType.AM_NEW_INTERSTITIAL);
//                }
//            });
//
//            try {
//                interstitialAd.load();
//                onADLoad(AdType.AM_NEW_INTERSTITIAL);
//            } catch (AdActionException e) {
//                e.printStackTrace();
//                return false;
//            }
//
//            return true;
//        }

        /**
         * AdMob(Publisher) Reward Ad
         */
//        private boolean loadAmNRawRewardAd(AMNRawRewardAd ad) {
//            if (ad == null) {
//                return false;
//            }
//
//            ad.setAdListener(new RewardAdListener() {
//                @Override
//                public void onRewardedAdLoaded(RawRewardAd ad) {
//                    activeAd = ad;
//                    onADFilledAndLoaded(AdType.AM_NEW_REWARD);
//                }
//
//                @Override
//                public void onRewardedAdOpened(RawRewardAd ad) {
//                    onADShowed(AdType.AM_NEW_REWARD);
//                }
//
//                @Override
//                public void onRewardedVideoStarted(RawRewardAd ad) {
//                    AdStrategyScene.this.onRewardedVideoStarted();
//                }
//
//                @Override
//                public void onRewardedAdClosed(RawRewardAd ad) {
//                    onAdClose(AdType.AM_NEW_REWARD);
//                }
//
//                @Override
//                public void onRewarded(RawRewardAd ad) {
//                    AdStrategyScene.this.onRewarded();
//                }
//
//                @Override
//                public void onRewardedAdLeftApplication(RawRewardAd ad) {
//                    onADClick(AdType.AM_NEW_REWARD);
//                }
//
//                @Override
//                public void onRewardedAdFailedToLoad(RawRewardAd ad, int code, String msg) {
//                    onADFail(AdType.AM_NEW_REWARD, code);
//                    currentAdIndex++;
//                    if (!loadWaterFallRawAd()) {
//                        onWaterFallRequestFail();
//                    }
//                }
//            });
//
//            try {
//                ad.load();
//                onADLoad(AdType.AM_NEW_REWARD);
//            } catch (AdActionException e) {
//                e.printStackTrace();
//                return false;
//            }
//
//            return true;
//        }

        /**
         * Facebook Native Ad
         */
        private boolean loadFbRawNativeAd(FbRawNativeAd ad) {
            if (ad == null) {
                return false;
            }

            ad.setAdListener(new NativeAdLoadListener() {

                @Override
                public void onError(RawNativeAd rawNativeAd, int i, String s) {
                    onADFail(AdType.FB_NATIVE, i);
                    currentAdIndex++;
                    if (!loadWaterFallRawAd()) {
                        onWaterFallRequestFail();
                    }

                    //如果 FB 广告加载失败，这里直接销毁
                    try {
                        rawNativeAd.destroy();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onAdLoaded(RawNativeAd rawNativeAd) {
                    activeAd = rawNativeAd;
                    onADFilledAndLoaded(AdType.FB_NATIVE);
                }

                @Override
                public void onClick(RawNativeAd rawNativeAd) {
                    onADClick(AdType.FB_NATIVE);
                }

                @Override
                public void onInstall(RawNativeAd rawNativeAd, String s) {

                }

                @Override
                public void onLoggingImpression(RawNativeAd rawNativeAd) {

                }
            });

            onADLoad(AdType.FB_NATIVE);
            ad.load();


            return true;
        }
    }
}
