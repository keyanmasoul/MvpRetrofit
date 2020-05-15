package corall.adscene.scene.func;

import android.os.Message;


import com.example.adscene.R;

import corall.adscene.ADGAConstant;
import corall.adscene.AdType;
import corall.adscene.EntranceType;
import corall.adscene.GARecordUtils;
import corall.adscene.scene.inner.CommonInterstitialScene;
import corall.base.app.CorApplication;


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
        Message message = Message.obtain();
        message.what = R.id.poster_msg_ad_interstitial_failed;
        message.obj = mEntranceType.getName();
        imContext.handleMobMessage(message);
    }

    @Override
    protected void onADLoaded(AdType adType) {
        GARecordUtils.reportFunc(imContext, ADGAConstant.C_DATA, ADGAConstant.A_FILL, ":" + getOverTime());

        // 广告成功返回，通知界面，显示广告
        Message message = Message.obtain();
        message.what = R.id.poster_msg_ad_interstitial_loaded;
        message.obj = mEntranceType.getName();
        imContext.handleMobMessage(message);
    }


    @Override
    public void onADClick(AdType adType) {
        super.onADClick(adType);


    }
}
