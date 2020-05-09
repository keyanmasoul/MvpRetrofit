package corall.ad.bean;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import corall.ad.bean.banner.RawBannerAd;
import corall.ad.bean.interstitial.RawInterstitialAd;
import corall.ad.bean.nativead.RawNativeAd;
import corall.ad.bean.reward.RawRewardAd;

/**
 * 聚合广告位
 * Created by ChenLi on 2018/1/12.
 */

public class CorAdUnionPlace extends CorAdPlace {

    private ArrayList<RawAd> mAdList;

    private ArrayList<AdsList> mAdGroup;

    private int currentIndex;

    public CorAdUnionPlace(long placeId) {
        super(placeId);
        currentIndex = 0;
    }

    public List<RawAd> getAllRawAdList() {
        return mAdList;
    }

    @Override
    public List<RawAd> getRawAdList() {
        if (mAdGroup != null && mAdGroup.size() > currentIndex) {
            return mAdGroup.get(currentIndex).getRawAds();
        }
        return mAdList;
    }

    public void setRawAdList(ArrayList<RawAd> list) {
        mAdList = list;
    }

    public RawNativeAd findNativeAdByPlatform(String platform) {
        for (RawAd ad : getRawAdList()) {
            if (ad.getPlatform().equals(platform) && ad instanceof RawNativeAd) {
                return (RawNativeAd) ad;
            }
        }
        return null;
    }

    public RawInterstitialAd findInterstitialAdByPlatform(String platform) {
        for (RawAd ad : getRawAdList()) {
            if (ad.getPlatform().equals(platform) && ad instanceof RawInterstitialAd) {
                return (RawInterstitialAd) ad;
            }
        }
        return null;
    }

    public RawBannerAd findBannerAdByPlatform(String platform) {
        for (RawAd ad : getRawAdList()) {
            if (ad.getPlatform().equals(platform) && ad instanceof RawBannerAd) {
                return (RawBannerAd) ad;
            }
        }
        return null;
    }

    public RawRewardAd findRewardAdByPlatform(String platform) {
        for (RawAd ad : getRawAdList()) {
            if (ad.getPlatform().equals(platform) && ad instanceof RawRewardAd) {
                return (RawRewardAd) ad;
            }
        }
        return null;
    }

    public void destory() {
        destory(null);
    }

    public void destory(Context context) {
        for (RawAd ad : getRawAdList()) {
            if (ad instanceof RawNativeAd) {
                ((RawNativeAd) ad).destroy();
            } else if (ad instanceof RawBannerAd) {
                ((RawBannerAd) ad).destroy();
            } else if (ad instanceof RawInterstitialAd) {
                ((RawInterstitialAd) ad).destroy();
            } else if (ad instanceof RawRewardAd) {
                ((RawRewardAd) ad).destroy(context);
            }
        }
        resetAdList();
    }

    public void setAdGroup(ArrayList<AdsList> list) {
        mAdGroup = list;
        resetAdList();
    }

    @Override
    public float getFrozenTime() {
        if (mAdGroup != null && mAdGroup.size() > currentIndex) {
            return mAdGroup.get(currentIndex).getFrozen();
        }
        return super.getFrozenTime();
    }

    public int getCurrentListIndex() {
        return currentIndex;
    }

    private void resetAdList() {
        if (mAdGroup == null || mAdGroup.size() == 0) {
            return;
        }
        if (mAdList != null) {
            mAdList.clear();
        }
        mAdList = new ArrayList<>();
        int[] scores = new int[mAdGroup.size()];
        int total = 0;
        for (int i = 0; i < mAdGroup.size(); i++) {
            AdsList list = mAdGroup.get(i);
            mAdList.addAll(mAdGroup.get(i).getRawAds());
            total += list.getChance();
            scores[i] = total;
        }
        Random random = new Random(System.currentTimeMillis());
        int r = random.nextInt(6 * 3600) % total;
        int length = scores.length;
        for (int i = 0; i < length; i++) {
            if (scores[i] > r) {
                currentIndex = i;
                return;
            }
        }
    }


}
