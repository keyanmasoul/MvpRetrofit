package corall.adscene.scene;


import corall.ad.bean.CorAdUnionPlace;
import corall.adscene.ADGAConstant;
import corall.adscene.AdType;
import corall.adscene.EntranceType;
import corall.adscene.GARecordUtils;
import corall.base.app.CorApplication;


/**
 * desc: 主要用来上报广告信息的scene
 * date: 2018/4/24
 * author: ancun
 */

public abstract class AdReportScene extends AdStrategyScene {

    private boolean repeatRequestForActivity = false;
    private boolean isReportMuliFail = false;

    public AdReportScene(CorApplication context, EntranceType entranceType) {
        super(context, entranceType);
    }


    @Override
    public void onADClick(AdType adType) {
        //广告场景打点
        GARecordUtils.onADClick(imContext, ADGAConstant.C_AD, adType, getFormatEntranceNameByRawAd());
    }


    @Override
    protected void onADFilled(AdType adType) {
        GARecordUtils.onADFilledEffective(imContext, ADGAConstant.C_AD, mEntranceType.getName());
        //广告场景打点
        GARecordUtils.onADFilled(imContext, ADGAConstant.C_AD, adType, getFormatEntranceNameByRawAd());
    }

    @Override
    public void onADShow(AdType adType) {
        //广告场景打点
        GARecordUtils.onADShowEffective(imContext, ADGAConstant.C_AD, mEntranceType.getName());
        GARecordUtils.onADShow(imContext, ADGAConstant.C_AD, adType, getFormatEntranceNameByRawAd());

        //广告区域打点
        GARecordUtils.onADShow(imContext, ADGAConstant.C_AREA, adType, getFormatAreaNameByRawAd());
    }

    @Override
    protected void onADLoad(AdType adType) {
        if (isRepeatRequestForActivity()) {
            GARecordUtils.onADLoadByActivity(imContext, adType, getFormatEntranceNameByRawAd());
        } else {
            CorAdUnionPlace unionAd = mEntranceType.getAdScene().getHmAdUnionPlace();
            int groupIndex = unionAd == null ? -1 : unionAd.getCurrentListIndex();
            //广告场景打点
            GARecordUtils.onADLoad(imContext, ADGAConstant.C_AD, adType, mEntranceType.getName(), groupIndex);
        }
    }

    @Override
    protected void onADFail(AdType adType, int code) {
        //广告场景打点
        GARecordUtils.onADFail(imContext, ADGAConstant.C_AD, adType, getFormatEntranceNameByRawAd(), code);

    }

    @Override
    public void release() {
        super.release();

    }

    @Override
    protected void onRequestAllFail() {
        super.onRequestAllFail();
        if (failCount >= 3 && !isReportMuliFail) {
            GARecordUtils.reportFunc(imContext, ADGAConstant.C_FUNC, ADGAConstant.A_FAIL, mEntranceType.getName() + " request fail count >3");
            isReportMuliFail = true;
        }
    }

    @Override
    protected void onRequestAll() {
        super.onRequestAll();
        if (!isRepeatRequestForActivity()) {
            CorAdUnionPlace unionAd = mEntranceType.getAdScene().getHmAdUnionPlace();
            int groupIndex = unionAd == null ? -1 : unionAd.getCurrentListIndex();
            //广告场景打点
            GARecordUtils.onADRequestEffective(imContext, ADGAConstant.C_AD, mEntranceType.getName(), groupIndex);
            //一整个瀑布流的请求，计算做一次
            mEntranceType.getAdScene().getStrategyExecutor().recordOnRequest();

        }
    }

    @Override
    protected void onRewarded() {

    }

    @Override
    protected void onRewardedVideoStarted() {

    }

    public boolean isRepeatRequestForActivity() {
        return repeatRequestForActivity;
    }

    public void setRepeatRequestForActivity(boolean repeatRequestForActivity) {
        this.repeatRequestForActivity = repeatRequestForActivity;
    }


}
