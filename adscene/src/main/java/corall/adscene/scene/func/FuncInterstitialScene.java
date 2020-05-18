package corall.adscene.scene.func;

import android.os.Message;


import com.example.adscene.R;

import corall.adscene.ADGAConstant;
import corall.adscene.AdType;
import corall.adscene.EntranceType;
import corall.adscene.GARecordUtils;
import corall.adscene.scene.inner.CommonInterstitialScene;
import corall.base.app.CorApplication;
import corall.base.bean.AdEvent;


/**
 * 功能 插屏广告
 */
public class FuncInterstitialScene extends CommonInterstitialScene {

    public FuncInterstitialScene(CorApplication context, EntranceType entranceType) {
        super(context, entranceType);
    }

    @Override
    protected void onRequestAllFail() {
        super.onRequestAllFail();

        final AdEvent message = new AdEvent();
        message.setWhat(R.id.poster_msg_ad_interstitial_failed);
        message.setObject(mEntranceType.getName());
        imContext.sendMessage(message);
    }

    @Override
    protected void onADLoaded(AdType adType) {
        GARecordUtils.reportFunc(imContext, ADGAConstant.C_DATA, ADGAConstant.A_FILL, ":" + getOverTime());

        // 广告成功返回，通知界面，显示广告
        final AdEvent message = new AdEvent();
        message.setWhat(R.id.poster_msg_ad_interstitial_loaded);
        message.setObject(mEntranceType.getName());
        imContext.sendMessage(message);
    }


    @Override
    public void onADClick(AdType adType) {
        super.onADClick(adType);


    }
}
