package corall.adscene.strategy.outer;

import android.text.format.DateUtils;

import com.cpu.time.AppRunTimeManager;

import corall.ad.AdsModule;
import corall.ad.bean.CorAdPlace;
import corall.base.app.CorApplication;

/**
 * desc:
 * date: 2018/4/19
 * author: ancun
 */

public class ChargingExecutor extends OuterAdExecutor {

    public ChargingExecutor(CorApplication context, String adScene) {
        super(context, adScene);
    }

    @Override
    protected String getTag() {
        return ChargingExecutor.class.getName();
    }

    public boolean isOutProtect(CorAdPlace hmAdPlace) {
        int protect = hmAdPlace.getProTime();
        if (protect * DateUtils.HOUR_IN_MILLIS > AppRunTimeManager.getInstance().getAppUsedTime()) {
            return false;
        }
        return true;
    }

}
