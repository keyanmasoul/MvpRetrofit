package corall.ad.bean;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import corall.ad.AdsContants;

/**
 * HM广告聚合类
 * Created by ChenLi on 2017/11/20.
 */
public abstract class CorAdPlace {

    protected long placeId;

    private int gray;

    private String actions;

    private int timeCount;

    // 广告位控制开关
    HashMap<String, Boolean> switchMap;
    // 第一次弹广告时长
    HashMap<String, Integer> proTimeMap;
    // 每天弹广告次数
    HashMap<String, Integer> limitMap;
    // 弹广告间隔时间
    HashMap<String, Float> intervalMap;

    // 每天请求次数
    HashMap<String, Integer> requestLimitMap;

    private boolean adSwitch;
    private int proTime;
    private int limit;
    private int requestLimit;
    private float interval;
    private int chance;
    private boolean isStore;

    private float frozenTime;

    private float blockTime;

    private long bgPlaceId;

    private String storeId;

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public boolean isStore() {
        return isStore;
    }

    public void setStore(boolean store) {
        this.isStore = store;
    }

    private String extra;


    private ArrayList<String> nationList;

    public void setNationList(ArrayList<String> nationList) {
        this.nationList = nationList;
    }


    public ArrayList<String> getNationList() {
        return nationList;
    }

    public void addNation(String nation) {
        if (nationList == null) {
            nationList = new ArrayList<>();
        }
        if (!nationList.contains(nation)) {
            this.nationList.add(nation);
        }
    }

    public boolean isNationDeploy(String na) {
        if (TextUtils.isEmpty(na) || nationList == null) {
            return false;
        }
        return nationList.contains(na.toLowerCase());
    }

    public CorAdPlace(long placeId) {
        this.placeId = placeId;

        switchMap = new HashMap<>();
        proTimeMap = new HashMap<>();
        limitMap = new HashMap<>();
        intervalMap = new HashMap<>();
        requestLimitMap = new HashMap<>();
        chance = 1;
        actions = "";
        gray = 100;
        timeCount = -1;
        frozenTime = 0f;
        blockTime = 0f;
        bgPlaceId = 0;
        isStore = false;
        nationList = new ArrayList<>();

    }

    public void dealWithChannel(String channel) {
        if (TextUtils.isEmpty(channel)) {
            channel = AdsContants.ORGANIC_CHANNEL;
        }

        if (switchMap.containsKey(channel)) {
            adSwitch = switchMap.get(channel);
        } else if (switchMap.containsKey(AdsContants.ORGANIC_CHANNEL)) {
            adSwitch = switchMap.get(AdsContants.ORGANIC_CHANNEL);
        } else {
            adSwitch = true;
        }

        if (proTimeMap.containsKey(channel)) {
            proTime = proTimeMap.get(channel);
        } else if (proTimeMap.containsKey(AdsContants.ORGANIC_CHANNEL)) {
            proTime = proTimeMap.get(AdsContants.ORGANIC_CHANNEL);
        } else {
            proTime = 24;
        }

        if (limitMap.containsKey(channel)) {
            limit = limitMap.get(channel);
        } else if (limitMap.containsKey(AdsContants.ORGANIC_CHANNEL)) {
            limit = limitMap.get(AdsContants.ORGANIC_CHANNEL);
        } else {
            limit = 1;
        }

        if (intervalMap.containsKey(channel)) {
            interval = intervalMap.get(channel);
        } else if (intervalMap.containsKey(AdsContants.ORGANIC_CHANNEL)) {
            interval = intervalMap.get(AdsContants.ORGANIC_CHANNEL);
        } else {
            interval = 6;
        }

        if (requestLimitMap.containsKey(channel)) {
            requestLimit = requestLimitMap.get(channel);
        } else if (requestLimitMap.containsKey(AdsContants.ORGANIC_CHANNEL)) {
            requestLimit = requestLimitMap.get(AdsContants.ORGANIC_CHANNEL);
        } else {
            requestLimit = 30;
        }
    }

    public RawAd findRawAdById(String id) {
        for (RawAd ad : getRawAdList()) {
            if (id.equals(ad.getId())) {
                return ad;
            }
        }
        return null;
    }

    /**
     * 获取原始广告位列表
     *
     * @return
     */
    public abstract List<RawAd> getRawAdList();

    public void setSwitch(String medium, boolean isOpen) {
        switchMap.put(medium, isOpen);
    }

    public void setProTime(String medium, int proTime) {
        proTimeMap.put(medium, proTime);
    }

    public void setlimit(String medium, int limit) {
        limitMap.put(medium, limit);
    }

    public void setRequestLimit(String medium, int limit) {
        requestLimitMap.put(medium, limit);
    }

    public void setInterval(String medium, float interval) {
        intervalMap.put(medium, interval);
    }

    public void setTimeCount(int count) {
        timeCount = count;
    }

    public boolean getSwitch() {
        return adSwitch;
    }

    public int getProTime() {
        return proTime;
    }

    public int getLimit() {
        return limit;
    }

    public int getRequestLimit() {
        return requestLimit;
    }

    public float getInterval() {
        return interval;
    }

    public int getChance() {
        return chance;
    }

    public void setChance(int chance) {
        this.chance = chance;
    }

    public int getTimeCount() {
        return timeCount;
    }

    public void setFrozenTime(float time) {
        frozenTime = time;
    }

    public float getFrozenTime() {
        return frozenTime;
    }

    public void setBlockTime(float time) {
        blockTime = time;
    }

    public float getBlockTime() {
        return blockTime;
    }

    public long getBgPlaceId() {
        return bgPlaceId;
    }

    public void setBgPlaceId(long bgPlaceId) {
        this.bgPlaceId = bgPlaceId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

//    public boolean getSwitch(String medium) {
//        if (!TextUtils.isEmpty(medium) && switchMap.containsKey(medium)) {
//            return switchMap.get(medium);
//        } else if (switchMap.containsKey(DEFAULT_KEY)) {
//            return switchMap.get(DEFAULT_KEY);
//        } else {
//            return true;
//        }
//    }
//
//    public int getProTime(String medium) {
//        if (!TextUtils.isEmpty(medium) && proTimeMap.containsKey(medium)) {
//            return proTimeMap.get(medium);
//        } else if (proTimeMap.containsKey(DEFAULT_KEY)) {
//            return proTimeMap.get(DEFAULT_KEY);
//        } else {
//            return 24;
//        }
//    }
//
//    public int getlimit(String medium) {
//        if (!TextUtils.isEmpty(medium) && limitMap.containsKey(medium)) {
//            return limitMap.get(medium);
//        } else if (limitMap.containsKey(DEFAULT_KEY)) {
//            return limitMap.get(DEFAULT_KEY);
//        } else {
//            return 1;
//        }
//    }
//
//    public int getInterval(String medium) {
//        if (!TextUtils.isEmpty(medium) && intervalMap.containsKey(medium)) {
//            return intervalMap.get(medium);
//        } else if (intervalMap.containsKey(DEFAULT_KEY)) {
//            return intervalMap.get(DEFAULT_KEY);
//        } else {
//            return 6;
//        }
//    }

    public void setGray(int gray) {
        this.gray = gray;
    }

    public int getGray() {
        return gray;
    }

    public String getActions() {
        return actions;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }


    public long getId() {
        return placeId;
    }


    public void setId(long id) {
        placeId = id;
    }
}
