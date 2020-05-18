package corall.adscene.scene.inner;


import android.app.Activity;

import com.example.adscene.R;

import corall.ad.bean.CorAdUnionPlace;
import corall.ad.bean.reward.AMRawRewardAd;
import corall.ad.ui.card.CommonNativeAdCardView;
import corall.ad.ui.window.RawNativeFullScreenAdWindow;
import corall.adscene.AdType;
import corall.adscene.EntranceType;
import corall.adscene.scene.AdReportScene;
import corall.adscene.strategy.ADStrategyManager;
import corall.adscene.strategy.IStrategyExecutor;
import corall.base.app.CorApplication;
import corall.base.bean.AdEvent;


/**
 * desc: 支持admob 视频广告的scenen
 * date: 2018/4/18
 * author: ancun
 */

public class UnlockVideoRewardScene extends AdReportScene {

    private RawNativeFullScreenAdWindow mWindow;

    private CorAdUnionPlace mHmAdPlace;

    private IStrategyExecutor mExecutor;

    public UnlockVideoRewardScene(CorApplication context, EntranceType entranceType) {
        super(context, entranceType);
    }

    /**
     * 在 Activity 中的 onDestroy() 中调用
     */
    @Override
    public void release() {
        super.release();
        mWindow = null;
        mHmAdPlace = null;
    }

    /**
     * 在 Activity 中的 onDestroy() 中调用 , 且在 release() 之前调用有效
     */
    public void destroyAMRawRewardAd(Activity activity) {
        if (activeAd instanceof AMRawRewardAd) {
            ((AMRawRewardAd) activeAd).destroy(activity);
        }
    }

    /**
     * 在 Activity 中的 onResume() 中调用
     */
    public void resumeAMRawRewardAd(Activity activity) {
        if (activeAd instanceof AMRawRewardAd) {
            ((AMRawRewardAd) activeAd).resume(activity);
        }
    }

    /**
     * 在 Activity 中的 onPause() 中调用
     */
    public void pauseAMRawRewardAd(Activity activity) {
        if (activeAd instanceof AMRawRewardAd) {
            ((AMRawRewardAd) activeAd).pause(activity);
        }
    }

    @Override
    public boolean load(boolean forceLoad) {
        CorAdUnionPlace adUnionPlace = getADModule().getUnionAd(mEntranceType.getPid());
        if (adUnionPlace == null) {
            return false;
        }
        mHmAdPlace = adUnionPlace;
        mExecutor = ADStrategyManager.getExecutorByEntranceType(mEntranceType);
        return (forceLoad || mExecutor.check(adUnionPlace)) && loadUnionPlace(adUnionPlace);

    }

    @Override
    protected CommonNativeAdCardView initCardView() {
        return null;
    }

    @Override
    protected void onADLoaded(AdType type) {
        switch (type) {
            case AM_REWARD:
            case AM_NEW_REWARD:

                final AdEvent message = new AdEvent();
                message.setWhat(R.id.msg_ad_unlock_video_show_loaded);
                imContext.sendMessage(message);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean show() {
        boolean result = true;
        if (activeAd instanceof AMRawRewardAd) {
            ((AMRawRewardAd) activeAd).show();
//            } else if (activeAd instanceof AMNRawRewardAd) {
//                ((AMNRawRewardAd) activeAd).show();
        } else {
            result = false;
        }
        return result;
    }

    @Override
    protected void onRewarded() {
        super.onRewarded();
        final AdEvent message = new AdEvent();
        message.setWhat(R.id.msg_ad_unlock_video_show_reward);
        imContext.sendMessage(message);
    }

    @Override
    protected void onRewardedVideoStarted() {
        super.onRewardedVideoStarted();
    }

    @Override
    public void onAdClose(AdType type) {
        final AdEvent message = new AdEvent();
        message.setWhat(R.id.msg_ad_unlock_video_show_close);
        message.setObject(mEntranceType.getName());
        imContext.sendMessage(message);
    }

    @Override
    public void onADClick(AdType type) {
        super.onADClick(type);
    }

    @Override
    public void onADShow(AdType type) {
        super.onADShow(type);
        if (mExecutor != null) {
            mExecutor.record();
        }
    }

    @Override
    protected void onADLoad(AdType type) {
        super.onADLoad(type);
    }

    @Override
    protected void onADFail(AdType type, int code) {
        super.onADFail(type, code);
    }
}
