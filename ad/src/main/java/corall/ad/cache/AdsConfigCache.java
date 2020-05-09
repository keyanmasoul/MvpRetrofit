package corall.ad.cache;

import android.text.TextUtils;

import java.util.List;

import corall.ad.AdsContants;
import corall.ad.bean.CorAdPlace;
import corall.ad.bean.CorAdUnionPlace;
import corall.ad.bean.RawAd;
import corall.ad.bean.reward.RawRewardAd;

/**
 * Created by ChenLi on 2017/11/21.
 */

public class AdsConfigCache {


    private boolean isContainsAdMob;

    private boolean isContainsFB;

    private boolean isContainsMopub;
    private boolean isContainsMopubReward;

    private String mopubKey;


    public boolean isBlockable() {
        return blockable;
    }

    public void setBlockable(boolean blockable) {
        this.blockable = blockable;
    }

    private boolean blockable;

    private List<CorAdPlace> corAdPlaceList;


    public AdsConfigCache() {

        isContainsAdMob = false;
        isContainsFB = false;
        isContainsMopub = false;
        isContainsMopubReward = false;
    }

    public void clearCache() throws Exception {
        if (corAdPlaceList != null) {
            corAdPlaceList.clear();
        }
    }

    public void setCorAdPlaceList(List<CorAdPlace> list) {
        corAdPlaceList = list;
        for (CorAdPlace adPlace : list) {
            if (adPlace != null && adPlace.getRawAdList() != null) {
                List<RawAd> rawAdList;
                if (adPlace instanceof CorAdUnionPlace) {
                    rawAdList = ((CorAdUnionPlace) adPlace).getAllRawAdList();
                } else {
                    rawAdList = adPlace.getRawAdList();
                }
                if (rawAdList == null) {
                    continue;
                }
                for (RawAd ad : rawAdList) {
                    if (ad.getPlatform().equals(AdsContants.AD_PLATFORM_ADMOB) /**|| ad.getPlatform().equals(AdsContants.AD_PLATFORM_ADMOB_NEW)**/) {
                        isContainsAdMob = true;
                    } else if (ad.getPlatform().equals(AdsContants.AD_PLATFORM_MOPUB)) {
                        isContainsMopub = true;
                        //mopub key使用任意一个mopub广告位的id
                        if (TextUtils.isEmpty(mopubKey)) {
                            mopubKey = ad.getId();
                        }

                        if (ad instanceof RawRewardAd) {
                            isContainsMopubReward = true;
                        }
                    } else if (ad.getPlatform().equals(AdsContants.AD_PLATFORM_FACEBOOK)) {
                        isContainsFB = true;
                    }
                }
            }
        }
    }

    public String getMopubKey() {
        return mopubKey;
    }

    public boolean isContainsAdMob() {
        return isContainsAdMob;
    }

    public boolean isContainsFb() {
        return isContainsFB;
    }

    public boolean isContainsMopub() {
        return isContainsMopub;
    }

    public boolean isContainsMopubReward() {
        return isContainsMopubReward;
    }

    public List<CorAdPlace> getCorAdPlaceList() {
        return corAdPlaceList;
    }


//    public void setHmAutoClickAdPlace(HmAutoClickAdPlace adPlace) {
//        mHmAutoClickAdPlace = adPlace;
//    }
//
//
//    public HmAutoClickAdPlace getHmAutoClickAdPlace() {
//        return mHmAutoClickAdPlace;
//    }
}
