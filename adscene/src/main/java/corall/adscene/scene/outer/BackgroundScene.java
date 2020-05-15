package corall.adscene.scene.outer;

import android.os.Bundle;
import android.os.Message;

import com.example.adscene.R;
import com.rt.BannerAdsActivity;
import com.rt.NativeAdActivity;
import com.uber.adsbuisiness.bean.HmAdUnionPlace;

import corall.ad.bean.CorAdUnionPlace;
import corall.adscene.AdType;
import corall.adscene.EntranceType;
import corall.base.app.CorApplication;
import cr.s.a.ADGAConstant;
import cr.s.a.AdType;
import cr.s.a.R;
import cr.s.a.util.GARecordUtils;

/**
 * 重叠广告的场景
 */
public class BackgroundScene extends CommonInterstitialOutScene {

    private boolean isBlock;

    public BackgroundScene(CorApplication context, EntranceType entranceType) {
        super(context, entranceType);
        isBlock = false;
    }

    public boolean isLoading() {
        return adStatus == AD_STATUS_LOADING;
    }

    /**
     * block广告，广告请求成功后也不会展示
     */
    public void setBlock() {
        isBlock = true;
    }

    @Override
    protected void onADLoad(AdType adType) {
        isBlock = false;
        super.onADLoad(adType);
    }

    @Override
    protected void onADFilled(AdType adType) {
        super.onADFilled(adType);
    }

    @Override
    protected void onADFail(AdType adType, int code) {
        //广告场景打点
        GARecordUtils.onADFail(imContext, ADGAConstant.C_AD, adType, getFormatEntranceNameByRawAd(), code);
    }

    @Override
    public boolean showAd() {
        if (isBlock) {
            release();
            return false;
        }

        boolean result = super.showAd();
        if (activeAd == null) {
            release();
            return false;
        }

        //通知重叠广告已展示成功，正式广告可以展示
        Message msg = new Message();
        msg.what = R.id.poster_msg_ad_background_already_show;
        Bundle bundle = new Bundle();
        bundle.putInt("pid", mEntranceType.getPid());
        bundle.putString("pt", activeAd.getPlatform());
        msg.setData(bundle);
        imContext.handleMobDelayMessage(msg, 100);
        return result;
    }

    /**
     * 显示 BANNER 的全屏,衬底广告只能使用activity 展示
     */
    @Override
    protected void showBannerFullScreen() {
        BannerAdsActivity.showInterstitialAd(imContext, mEntranceType.getName());
    }

    /**
     * 显示原生的全屏,衬底广告只能使用activity 展示
     */
    @Override
    protected void showNativeFullScreen() {
        NativeAdActivity.showInterstitialAd(imContext, mEntranceType.getName());
    }

    @Override
    protected boolean loadBgPlaceAd(CorAdUnionPlace mHmAdPlace) {
        //重叠广告不再进行二次重叠
        return false;
    }
}
