package corall.ad.task;

import android.text.TextUtils;

import com.orhanobut.hawk.Hawk;

import java.util.List;

import corall.ad.AdUtils;
import corall.ad.AdsContants;
import corall.ad.AdsModule;
import corall.ad.BuildConfig;
import corall.ad.CorAdHandler;
import corall.ad.R;
import corall.ad.bean.CorAdPlace;
import corall.base.app.CorApplication;
import corall.base.task.CorTask;
import corall.base.task.CorTaskSign;
import corall.base.task.ICorTaskResult;
import corall.base.util.AppInfoUtil;
import corall.base.util.Des3Util;
import corall.base.util.KeyUtil;
import corall.base.util.StringUtil;
import corall.base.util.Utils;

public class LoadAdTask extends CorTask {
    public static final String TASKSIGN = "load_ad";

    private AdsModule adsModule;

    public LoadAdTask(AdsModule adsModule, ICorTaskResult iCorTaskResult) {
        super(new CorTaskSign(TASKSIGN), iCorTaskResult);
        this.adsModule = adsModule;
    }


    @Override
    protected Object call(Object[] objects) {
        try {
            String localAdConfig = Hawk.get(KeyUtil.getKey(R.string.ads_config_pref), "");
            if (StringUtil.isEmptyString(localAdConfig)) {
                //SharedPref没有广告配置，从assets里获取原始配置
                localAdConfig = getAssetsAdConfig();
                if (StringUtil.isEmptyString(localAdConfig)) {
                    return null;
                }
            }
            return analyzeAdConfig(localAdConfig);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onComplete() {
        super.onComplete();
    }

    private String getAssetsAdConfig() {
        CorApplication aMApplication = CorApplication.getInstance();
        String localAdConfig = Utils.getAssetsFileContent(aMApplication, AdsContants.ASSETS_AD_CONFIG_PATH + BuildConfig.APP_ID);
        if (TextUtils.isEmpty(localAdConfig)) {
            localAdConfig = Utils.getAssetsFileContent(aMApplication, AdsContants.ASSETS_AD_CONFIG_PATH + "default");
        }
        if (!StringUtil.isEmptyString(localAdConfig)) {
            Hawk.put(KeyUtil.getKey(R.string.ads_config_pref), localAdConfig);
            return localAdConfig;
        }
        return null;
    }

    private List<CorAdPlace> analyzeAdConfig(String localAdConfig) throws Exception {
        try {
            String decryptAdConfig = decrypt3DES(localAdConfig);
            byte[] result = AdUtils.getDecodedConfig(decryptAdConfig, BuildConfig.DECODE_OFFSET);
            return parseAdsConfig(result);
        } catch (Exception e) {
            //decode error ,use original data
            byte[] result = new byte[0];
            if (localAdConfig != null) {
                result = localAdConfig.getBytes("utf-8");
            }
            return parseAdsConfig(result);
        }
    }

    private String decrypt3DES(String localAdConfig) {
        String decryptAdConfig = "";
        try {
            String sha1SingInfo = AppInfoUtil.getSha1SingInfo(CorApplication.getInstance());
            decryptAdConfig = Des3Util.decrypt(localAdConfig, sha1SingInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptAdConfig;
    }

    private List<CorAdPlace> parseAdsConfig(byte[] result) {
        CorAdHandler mHandler = new CorAdHandler();
        if (adsModule.isTesting()) {
            mHandler.setTest();
        }
        mHandler.parserJson(result);
        adsModule.getAdsConfigCache().setBlockable(mHandler.isBlockable());
        adsModule.getAdsConfigCache().setCorAdPlaceList(mHandler.getAdsList());
        return mHandler.getAdsList();
    }
}