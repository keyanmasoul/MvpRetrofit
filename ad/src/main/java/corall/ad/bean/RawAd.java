package corall.ad.bean;

import corall.ad.AdsContants;
import corall.base.app.AMApplication;
import corall.base.util.StringUtil;

/**
 * 原始广告父类
 * Created by ChenLi on 2017/11/21.
 */

public abstract class RawAd {

    private String placeId;

    // 广告请求状态
    protected int adStatus = AdsContants.RAW_AD_STATUS_WAITING;

    public void setADStatus(int status) {
        adStatus = status;
    }

    public int getAdStatus() {
        return adStatus;
    }

    public void setPlaceId(String id) {
        placeId = id;
    }

    public String getPlaceId() {
        return placeId;
    }

    // 看广告平台，如：FAN,du,MJ
    public abstract String getPlatform();

    public abstract String getId();

    protected String getString(int res) {
        return decodeMethod(res);
    }


    protected String decodeMethod(int res) {
        return StringUtil.decodeStringRes(AMApplication.getInstance(), res);
    }


}
