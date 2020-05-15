package corall.adscene.scene.outer;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.uber.android.mob.AMApplication;

import corall.ad.bean.CorAdUnionPlace;
import corall.adscene.AdType;
import corall.adscene.EntranceType;
import corall.base.app.CorApplication;
import cr.s.a.AdType;
import cr.s.a.EntranceType;

/**
 * desc: 应用外广告 管理
 * date: 2018/4/20
 * author: ancun
 */

public class AppOuterDeferScene extends CommonInterstitialOutScene {

    private static final int SHOW_AD_DEFER = 5000;
    private long loadAdTimeAt;

    public AppOuterDeferScene(CorApplication context, EntranceType entranceType) {
        super(context, entranceType);
    }

    @Override
    protected void onADLoaded(AdType type) {
        //加载广告时长
        long duration = System.currentTimeMillis() - loadAdTimeAt;

        if (duration >= SHOW_AD_DEFER) {
            showAd();
        } else {
            Handler handler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    showAd();
                }
            };
            handler.sendEmptyMessageDelayed(49, SHOW_AD_DEFER - duration);
        }
        loadAdTimeAt = 0;
    }

    @Override
    public boolean load(boolean forceLoad) {
        loadAdTimeAt = System.currentTimeMillis();
        return super.load(forceLoad);
    }

}
