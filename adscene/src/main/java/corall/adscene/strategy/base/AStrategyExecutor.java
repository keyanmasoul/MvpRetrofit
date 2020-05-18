package corall.adscene.strategy.base;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.PowerManager;
import android.text.format.DateUtils;

import com.cpu.time.AppRunTimeManager;
import com.orhanobut.hawk.Hawk;

import corall.ad.AdsModule;
import corall.ad.bean.CorAdPlace;
import corall.adscene.ADConstant;
import corall.adscene.strategy.IStrategyExecutor;
import corall.base.app.CorApplication;
import corall.base.util.NetWorkInfoParser;


/**
 * desc: 广告策略的执行者
 * date: 2017/12/20
 * author: ancun
 */

public abstract class AStrategyExecutor implements IStrategyExecutor {

    protected CorApplication imContext;
    protected String mAdScene;

    public AStrategyExecutor(CorApplication context, String adScene) {
        imContext = context;
        mAdScene = adScene;
    }

    protected abstract String getTag();

    protected String getLastShowTimeKey() {
        return mAdScene + ADConstant.AdSharedPrefKey.SHOW_TIME;
    }

    protected String getRequestCountKey() {
        return mAdScene + ADConstant.AdSharedPrefKey.REQUEST_COUNT;
    }

    protected String getShowCountKey() {
        return mAdScene + ADConstant.AdSharedPrefKey.SHOW_COUNT;
    }

    protected String getBeginTimeOfDayKey() {
        return mAdScene + ADConstant.AdSharedPrefKey.BEGIN_TIME;
    }

    protected long getLastShowTime() {
        return Hawk.get(getLastShowTimeKey());
    }

    protected void setLastShowTime(long lastShowTime) {
        Hawk.put(getLastShowTimeKey(), lastShowTime);
    }

    protected int getRequestCounts() {
        return Hawk.get(getRequestCountKey());
    }

    protected void setRequestCounts(int count) {
        Hawk.put(getRequestCountKey(), count);
    }

    protected int getShowCounts() {
        return Hawk.get(getShowCountKey());
    }

    protected void setShowCounts(int count) {
        Hawk.put(getShowCountKey(), count);
    }

    protected long getBeginTimeOfDay() {
        return Hawk.get(getBeginTimeOfDayKey());
    }

    protected void setBeginTimeOfDay(long beginTime) {
        Hawk.put(getBeginTimeOfDayKey(), beginTime);
    }

    @Override
    public void reset() {
        //第一次或超过24小时，重置 开始时间和广告计数
        if (System.currentTimeMillis() - getBeginTimeOfDay() > DateUtils.DAY_IN_MILLIS) {
            setBeginTimeOfDay(System.currentTimeMillis());
            setRequestCounts(0);
            setShowCounts(0);
        }
    }

    @Override
    public void record() {
        if (System.currentTimeMillis() - getBeginTimeOfDay() > DateUtils.DAY_IN_MILLIS) {
            //第一次或超过24小时，重置 开始时间和广告计数
            setBeginTimeOfDay(System.currentTimeMillis());
            setShowCounts(1);
        } else {
            //记录24小时内的展示次数
            setShowCounts(getShowCounts() + 1);
        }
        //记录当前最后一次展示的时间
        setLastShowTime(System.currentTimeMillis());
    }

    @Override
    public void recordOnRequest() {
        if (System.currentTimeMillis() - getBeginTimeOfDay() > DateUtils.DAY_IN_MILLIS) {
            // 由于该方法有广告展示后调用，所以是 1
            setRequestCounts(1);
        } else {
            //记录24小时内的请求次数
            setRequestCounts(getRequestCounts() + 1);
        }
    }

    @Override
    public boolean check(CorAdPlace hmNativeAd) {
        //每次检查前 调用一次 reset();
        reset();

        if (hmNativeAd == null) {

            return false;
        }

        // 判断是否有网
        if (!ignoreNetWork()) {
            if (!NetWorkInfoParser.isNetConnect(imContext)) {
                return false;
            }
        }

        // 判断是否亮屏
        if (!ignoreScreenOff()) {
            PowerManager pm = (PowerManager) imContext.getSystemService(Context.POWER_SERVICE);
            KeyguardManager manager = (KeyguardManager) imContext.getSystemService(Context.KEYGUARD_SERVICE);
            if (!pm.isScreenOn() || manager.isKeyguardLocked()) {
                return false;
            }
        }

        if (!hmNativeAd.getSwitch()) {
            return false;
        }

        int requestCount = hmNativeAd.getRequestLimit();
        if (getRequestCounts() >= requestCount) {

            onRequestLimited();
            return false;
        }

        //检查配置
        if (!ignoreOutTimeProtect()) {
            int protect = hmNativeAd.getProTime();
            if (protect * DateUtils.HOUR_IN_MILLIS > AppRunTimeManager.getInstance().getAppUsedTime()) {
                return false;
            }
        }

        if (getLastShowTime() == 0) {

            return true;
        }

        float interval = hmNativeAd.getInterval();
        if ((System.currentTimeMillis() - getLastShowTime() < (long) (interval * DateUtils.HOUR_IN_MILLIS))) {

            return false;
        }

        int count = hmNativeAd.getLimit();
        if (getShowCounts() >= count) {

            onCountLimited();
            return false;
        }

        return true;
    }

    public boolean isOutProtect(CorAdPlace hmAdPlace) {
        int protect = hmAdPlace.getProTime();
        if (protect * DateUtils.HOUR_IN_MILLIS > AppRunTimeManager.getInstance().getAppUsedTime()) {

            return false;
        }
        return true;
    }

    protected void onCountLimited() {

    }

    protected void onRequestLimited() {
//        GARecordUtils.reportAd(imContext, ADGAConstant.C_AD, ADGAConstant.A_REQUEST, mAdScene + "_reached_request_limit");
    }

    public void forwardBeginTime() {
        setBeginTimeOfDay(System.currentTimeMillis() - 24 * DateUtils.DAY_IN_MILLIS);
    }

    protected boolean ignoreOutTimeProtect() {
        return false;
    }

    protected boolean ignoreNetWork() {
        return false;
    }

    protected boolean ignoreScreenOff() {
        return false;
    }
}
