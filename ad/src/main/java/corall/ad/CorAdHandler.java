package corall.ad;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import corall.ad.bean.AdsList;
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
import corall.ad.bean.nativead.AmRawNativeAd;
import corall.ad.bean.nativead.DirectRawNativeAd;
import corall.ad.bean.nativead.FbRawNativeAd;
import corall.ad.bean.nativead.MpRawNativeAd;
import corall.ad.bean.nativead.RawNativeAd;
import corall.ad.bean.reward.AMRawRewardAd;
import corall.ad.bean.reward.MpRawRewardAd;
import corall.ad.bean.reward.RawRewardAd;
import corall.base.app.AMApplication;
import corall.base.util.StringUtil;

/**
 * Created by ChenLi on 2017/11/20.
 */

public class CorAdHandler {
    private static final String TAG = CorAdHandler.class.getName();

    private List<CorAdPlace> mAds;

    private boolean isTesting = false;

    private boolean isBlockable = false;

    public CorAdHandler() {
        HandlerKeyConstant.init();
    }

    protected void parserContent(JSONObject content) throws Exception {
        JSONObject result = content.optJSONObject(HandlerKeyConstant.RESULT);
        JSONArray adsList = result.optJSONArray(HandlerKeyConstant.HMADS);
        isBlockable = result.optBoolean(HandlerKeyConstant.BLOCKABLE, false);
        mAds = new ArrayList<>();
        for (int i = 0; i < adsList.length(); i++) {
            JSONObject object = adsList.getJSONObject(i);
            try {
                CorAdPlace ad = parseHmAdJson(object);
                if (ad != null) {
                    mAds.add(ad);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setTest() {
        isTesting = true;
    }


    public CorAdPlace parseHmAdJson(JSONObject content) throws Exception {
        CorAdPlace hmAdPlace = null;
        String placeId = content.getString(HandlerKeyConstant.PLACEID);
        String type = content.getString(HandlerKeyConstant.TYPE);
        if (type.equals(HandlerKeyConstant.UNION)) {
            hmAdPlace = new CorAdUnionPlace(Long.valueOf(placeId));
            if (content.has(HandlerKeyConstant.ADS)) {
                JSONArray array = content.getJSONArray(HandlerKeyConstant.ADS);
                ArrayList<RawAd> rawAds = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    try {
                        RawAd ad = parseUnionRawAd(array.getJSONObject(i));
                        if (ad != null) {
                            ad.setPlaceId(placeId);
                            rawAds.add(ad);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                ((CorAdUnionPlace) hmAdPlace).setRawAdList(rawAds);
            } else if (content.has(HandlerKeyConstant.ADGROUP)) {
                JSONArray array = content.getJSONArray(HandlerKeyConstant.ADGROUP);
                ArrayList<AdsList> adsGroup = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    AdsList list = parseAdGroupList(array.getJSONObject(i), placeId);
                    adsGroup.add(list);
                }
                ((CorAdUnionPlace) hmAdPlace).setAdGroup(adsGroup);
            }
        }
        if (hmAdPlace == null) {
            return null;
        }


        String extra = content.optString(HandlerKeyConstant.EXTRA);
        hmAdPlace.setExtra(extra);

        JSONObject switch_info = content.optJSONObject(HandlerKeyConstant.SWITCH_INFO);
        if (switch_info != null) {
            Iterator<String> sIterator = switch_info.keys();
            while (sIterator.hasNext()) {
                String key = sIterator.next();
                boolean value = switch_info.getBoolean(key);
                hmAdPlace.setSwitch(key, value);
            }
        }

        JSONObject protime_info = content.optJSONObject(HandlerKeyConstant.PROTIME_INFO);
        if (protime_info != null) {
            Iterator<String> sIterator = protime_info.keys();
            while (sIterator.hasNext()) {
                String key = sIterator.next();
                int value = protime_info.getInt(key);
                hmAdPlace.setProTime(key, value);
            }
        }

        JSONObject limit_info = content.optJSONObject(HandlerKeyConstant.LIMIT_INFO);
        if (limit_info != null) {
            Iterator<String> sIterator = limit_info.keys();
            while (sIterator.hasNext()) {
                String key = sIterator.next();
                int value = limit_info.getInt(key);
                hmAdPlace.setlimit(key, value);
            }
        }

        JSONObject req_limit_info = content.optJSONObject(HandlerKeyConstant.REQ_LIMIT_INFO);
        if (req_limit_info != null) {
            Iterator<String> sIterator = req_limit_info.keys();
            while (sIterator.hasNext()) {
                String key = sIterator.next();
                int value = req_limit_info.getInt(key);
                hmAdPlace.setRequestLimit(key, value);
            }
        }

        JSONObject interval_info = content.optJSONObject(HandlerKeyConstant.INTERVAL_INFO);
        if (interval_info != null) {
            Iterator<String> sIterator = interval_info.keys();
            while (sIterator.hasNext()) {
                String key = sIterator.next();
                double value = interval_info.getDouble(key);
                hmAdPlace.setInterval(key, (float) value);
            }
        }

        if (content.has(HandlerKeyConstant.GRAY)) {
            hmAdPlace.setGray(content.getInt(HandlerKeyConstant.GRAY));
        }

        if (content.has(HandlerKeyConstant.TIMECOUNT)) {
            hmAdPlace.setTimeCount(content.getInt(HandlerKeyConstant.TIMECOUNT));
        }

        if (content.has(HandlerKeyConstant.FROZEN)) {
            hmAdPlace.setFrozenTime((float) content.getDouble(HandlerKeyConstant.FROZEN));
        }

        if (content.has(HandlerKeyConstant.IS_STORE)) {
            hmAdPlace.setStore(content.getBoolean(HandlerKeyConstant.IS_STORE));
        }

        if (content.has(HandlerKeyConstant.STOREID)) {
            hmAdPlace.setStoreId(content.getString(HandlerKeyConstant.STOREID));
        }

        if (content.has(HandlerKeyConstant.BLOCK)) {
            hmAdPlace.setBlockTime((float) content.getDouble(HandlerKeyConstant.BLOCK));
        }

        if (content.has(HandlerKeyConstant.CHANCE)) {
            hmAdPlace.setChance(content.getInt(HandlerKeyConstant.CHANCE));
        }

        if (content.has(HandlerKeyConstant.ACTIONS)) {
            hmAdPlace.setActions(content.getString(HandlerKeyConstant.ACTIONS));
        }

        if (content.has(HandlerKeyConstant.NATIONLIST)) {
            String nationStr = content.getString(HandlerKeyConstant.NATIONLIST);
            String[] nations = nationStr.split(",");
            if (nations.length > 0) {
                for (String na : nations) {
                    hmAdPlace.addNation(na.toLowerCase());
                }
            }
        }

        String bgid = content.optString(HandlerKeyConstant.BGPLACE, "0");
        hmAdPlace.setBgPlaceId(Long.valueOf(bgid));


        return hmAdPlace;
    }

    /**
     * 用于解析union中的单个广告
     *
     * @param content
     * @return
     * @throws Exception
     */
    private RawAd parseUnionRawAd(JSONObject content) throws Exception {

        String type = content.getString(HandlerKeyConstant.TYPE);
        RawAd rawAd = null;
        if (type.equals(HandlerKeyConstant.NATIVE)) {
            rawAd = parseNativeAdJson(content);
        } else if (type.equals(HandlerKeyConstant.BANNER)) {
            rawAd = parseBannerAdJson(content);
        } else if (type.equals(HandlerKeyConstant.INTERSTITIAL)) {
            rawAd = parseIntersitialAdJson(content);
        } else if (type.equals(HandlerKeyConstant.REWARD)) {
            rawAd = parseRewardAdJson(content);
        }

        return rawAd;
    }

    /**
     * 用于解析union中的多个广告列表
     *
     * @param content
     * @return
     * @throws Exception
     */
    private AdsList parseAdGroupList(JSONObject content, String placeId) throws Exception {

        int chance = content.optInt(HandlerKeyConstant.CHANCE, 10);
        int frozen = content.optInt(HandlerKeyConstant.FROZEN, 0);
        JSONArray array = content.getJSONArray(HandlerKeyConstant.ADS);
        ArrayList<RawAd> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            RawAd ad = parseUnionRawAd(array.getJSONObject(i));
            if (ad != null) {
                ad.setPlaceId(placeId);
                list.add(ad);
            }
        }
        AdsList adsList = new AdsList();
        adsList.setChance(chance);
        adsList.setFrozen(frozen);
        adsList.setRawAds(list);
        return adsList;
    }

    //    private RawAutoClickAd parseAutoClickAdJson(JSONObject content) throws Exception {
    //        String platform = content.getString("platform");
    //
    //        String id = content.getString("id");
    //        boolean isRecord = content.optBoolean("record", false);
    //        RawAutoClickAd rawAutoClickAd;
    //        switch (platform) {
    //            case AdsContants.AD_PLATFORM_TB_NETWORK:
    //                rawAutoClickAd = new TBNAutoClickAd(id);
    //                break;
    //            case AdsContants.AD_PLATFORM_LM_NETWORK:
    //                rawAutoClickAd = new LMAutoClickAd(id);
    //                break;
    //            default:
    //                rawAutoClickAd = null;
    //                break;
    //        }
    //        if (rawAutoClickAd != null) {
    //            rawAutoClickAd.setRecord(isRecord);
    //        }
    //        return rawAutoClickAd;
    //    }

    private RawBannerAd parseBannerAdJson(JSONObject content) throws Exception {
        String platform = content.getString(HandlerKeyConstant.PLATFORM);

        String id = content.getString(HandlerKeyConstant.ID);
        String size = content.optString(HandlerKeyConstant.SIZE);

        RawBannerAd rawBannerAd;
        switch (platform) {
            case AdsContants.AD_PLATFORM_ADMOB:
                if (!TextUtils.isEmpty(id) && isTesting) {
                    id = getString(R.string.am_banner_test);
                }
                rawBannerAd = new AMRawBannerAd(id, size);

                break;
            //            case AdsContants.AD_PLATFORM_ADMOB_NEW:
            //                if (!TextUtils.isEmpty(id) && isTesting) {
            //                    id = decodeConstant(AdsContants.ADMOB_NEW_TEST_BANNER);
            //                }
            //                rawBannerAd = new AMNRawBannerAd(id, size);
            //
            //                break;
            case AdsContants.AD_PLATFORM_MOPUB:
                if (!TextUtils.isEmpty(id) && isTesting) {
                    id = getString(R.string.mp_banner_test);
                }
                rawBannerAd = new MpRawBannerAd(id, size);
                break;
            //            case AdsContants.AD_PLATFORM_STORE:
            //                rawBannerAd = new StoreRawBannerAd(id);
            //                break;
            case AdsContants.AD_PLATFORM_FACEBOOK:
                if (!TextUtils.isEmpty(id) && isTesting) {
                    id = getString(R.string.fb_banner_test) + "#" + id;
                }
                rawBannerAd = new FbRawBannerAd(id, size);
                break;
            case AdsContants.AD_PLATFORM_DIRECT:
                String url = content.getString(HandlerKeyConstant.URL);
                boolean isJumpOut = content.optBoolean(HandlerKeyConstant.JUMPOUT, false);
                String action = content.optString(HandlerKeyConstant.ACTION);
                rawBannerAd = new DirectRawBannerAd(id, url);
                ((DirectRawBannerAd) rawBannerAd).setJumpAction(action);
                ((DirectRawBannerAd) rawBannerAd).setJumpOut(isJumpOut);
                break;
            default:
                rawBannerAd = null;
                break;
        }
        return rawBannerAd;
    }

    private RawRewardAd parseRewardAdJson(JSONObject content) throws Exception {
        String platform = content.getString(HandlerKeyConstant.PLATFORM);

        String id = content.getString(HandlerKeyConstant.ID);


        RawRewardAd rawRewardAd;
        switch (platform) {
            case AdsContants.AD_PLATFORM_ADMOB:
                if (!TextUtils.isEmpty(id) && isTesting) {
                    id = getString(R.string.am_reward_test);
                }
                rawRewardAd = new AMRawRewardAd(id);
                break;
            //            case AdsContants.AD_PLATFORM_ADMOB_NEW:
            //                if (!TextUtils.isEmpty(id) && isTesting) {
            //                    id = decodeConstant(AdsContants.ADMOB_NEW_TEST_REWARD);
            //                }
            //                rawRewardAd = new AMNRawRewardAd(id);
            //                break;
            //            case AdsContants.AD_PLATFORM_FACEBOOK:
            //                if (!TextUtils.isEmpty(id) && isTesting) {
            //                    id = decodeConstant(AdsContants.FB_TEST_REWARD);
            //                }
            //                rawRewardAd = new FbRawRewardAd(id);
            //                break;
            case AdsContants.AD_PLATFORM_MOPUB:
                if (!TextUtils.isEmpty(id) && isTesting) {
                    id = getString(R.string.mp_reward_test);
                }
                rawRewardAd = new MpRawRewardAd(id);
                break;
            //            case AdsContants.AD_PLATFORM_FLUCT:
            //                String[] ids = id.split(",");
            //                if (ids.length != 2) {
            //                    return null;
            //                }
            //                rawRewardAd = new FluctRawRewardAd(ids[0], ids[1]);
            //                if (!TextUtils.isEmpty(id) && isTesting) {
            //                    ((FluctRawRewardAd) rawRewardAd).setTest(true);
            //                }
            //
            //                break;
            default:
                rawRewardAd = null;
                break;
        }
        return rawRewardAd;
    }

    private RawInterstitialAd parseIntersitialAdJson(JSONObject content) throws Exception {
        String platform = content.getString(HandlerKeyConstant.PLATFORM);
        String id = content.getString(HandlerKeyConstant.ID);

        RawInterstitialAd rawInterstitialAd;
        switch (platform) {
            case AdsContants.AD_PLATFORM_ADMOB:
                if (!TextUtils.isEmpty(id) && isTesting) {
                    id = getString(R.string.am_in_test);
                }
                rawInterstitialAd = new AMRawInterstitialAd(id);
                break;
            //            case AdsContants.AD_PLATFORM_ADMOB_NEW:
            //                if (!TextUtils.isEmpty(id) && isTesting) {
            //                    id = decodeConstant(AdsContants.ADMOB_NEW_TEST_INTERSTITIAL);
            //                }
            //                rawInterstitialAd = new AMNRawInterstitialAd(id);
            //                break;
            case AdsContants.AD_PLATFORM_DIRECT:
                String url = content.getString(HandlerKeyConstant.URL);
                boolean isJumpOut = content.optBoolean(HandlerKeyConstant.JUMPOUT, false);
                rawInterstitialAd = new DirectInterstitialAd(id, url);
                ((DirectInterstitialAd) rawInterstitialAd).setOuter(isJumpOut);
                break;
            case AdsContants.AD_PLATFORM_WEB:
                url = content.getString(HandlerKeyConstant.URL);
                rawInterstitialAd = new WebInterstitialAd(id, url);
                break;
            case AdsContants.AD_PLATFORM_MOPUB:
                if (!TextUtils.isEmpty(id) && isTesting) {
                    id = getString(R.string.mp_in_test);
                }
                rawInterstitialAd = new MpRawInterstitialAd(id);
                break;
            //            case AdsContants.AD_PLATFORM_FLUCT:
            //                rawInterstitialAd = new FluctRawIntersitialAd(id);
            //                break;
            //            case AdsContants.AD_PLATFORM_STORE:
            //                rawInterstitialAd = new StoreRawInterstitialAd(id);
            //                break;
            case AdsContants.AD_PLATFORM_FACEBOOK:
                if (!TextUtils.isEmpty(id) && isTesting) {
                    id = getString(R.string.fb_in_test) + "#" + id;
                }
                rawInterstitialAd = new FbRawInterstitialAd(id);
                break;
            default:
                rawInterstitialAd = null;
                break;
        }
        return rawInterstitialAd;
    }

    private RawNativeAd parseNativeAdJson(JSONObject content) throws Exception {
        String platform = content.getString(HandlerKeyConstant.PLATFORM);
        String id = content.getString(HandlerKeyConstant.ID);
        RawNativeAd rawNativeAd = null;
        if (platform.equals(AdsContants.AD_PLATFORM_FACEBOOK)) {
            if (isTesting) {
                id = getString(R.string.fb_native_test) + "#" + id;
            }
            rawNativeAd = new FbRawNativeAd(id);
            //                if (content.has("fake")) {
            //                    FbReplaceInfo info = parseFbReplaceInfo(content.getJSONObject("fake"));
            //                    if (info != null) {
            //                        ((FbRawNativeAd) rawNativeAd).setReplaceInfo(info);
            //                    }
            //                }

        } else if (platform.equals(AdsContants.AD_PLATFORM_ADMOB)) {
            if (isTesting) {
                id = getString(R.string.am_native_adv_test);
            }

            rawNativeAd = new AmRawNativeAd(id);


        } else if (platform.equals(AdsContants.AD_PLATFORM_MOPUB)) {
            if (!TextUtils.isEmpty(id) && isTesting) {
                id = getString(R.string.mp_native_test);
            }
            rawNativeAd = new MpRawNativeAd(id);
        } else if (platform.equals(AdsContants.AD_PLATFORM_DIRECT)) {
            rawNativeAd = new DirectRawNativeAd(id);
            String title = content.getString(HandlerKeyConstant.TITLE);
            String iconUrl = content.getString(HandlerKeyConstant.ICON);
            String imageUrl = content.optString(HandlerKeyConstant.IMAGE);
            String desc = content.optString(HandlerKeyConstant.DESC);
            String callAction = content.optString(HandlerKeyConstant.CALL_ACTION);
            boolean jumpOut = content.optBoolean(HandlerKeyConstant.JUMPOUT);
            String url = content.optString(HandlerKeyConstant.URL);
            String pkgName = content.optString(HandlerKeyConstant.PKG);
            String action = content.optString(HandlerKeyConstant.ACTION);
            DirectRawNativeAd ad = (DirectRawNativeAd) rawNativeAd;
            ad.setTitle(title);
            ad.setIconUrl(iconUrl);
            ad.setAction(action);
            ad.setCallAction(callAction);
            ad.setImageUrl(imageUrl);
            ad.setUrl(url);
            ad.setPkgName(pkgName);
            ad.setDesc(desc);
            ad.setJumpOut(jumpOut);
        }
        return rawNativeAd;
    }

    /**
     * 解析json
     *
     * @param data
     */
    public void parserJson(byte[] data) {
        parserJson(data, "utf-8");
    }

    /**
     * 解析json
     *
     * @param data
     * @param charset
     */
    public void parserJson(byte[] data, String charset) {
        try {
            JSONObject result = new JSONObject(new String(data, charset));
            // 这里使用get获取值，因为我们希望捕获异常
            int code = result.getInt("code");
            if (code == 200) {
                JSONObject jsonPage = result.optJSONObject("result");
                parserContent(result);
            } else {

//                String exMsg = result.optString("msg");
//                ActionException actionException = new ActionException();
//                actionException.setExCode(code);
//                actionException.setExMessage(exMsg);
//
//                throw actionException;
            }

        } catch (Exception e) {
            e.printStackTrace();
//            if (e instanceof ActionException) {
//                throw (ActionException) e;
//            } else {
//                throw new RemoteViews.ActionException(RemoteViews.ActionException.OPERATE_ERROR, "");
//            }
        }
    }


    protected String getString(int res) {
        return StringUtil.decodeStringRes(AMApplication.getInstance(), res);
    }

    public List<CorAdPlace> getAdsList() {
        return mAds;
    }

    public boolean isBlockable() {
        return isBlockable;
    }
}
