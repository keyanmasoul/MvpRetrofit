package corall.adscene;

import android.os.Message;

import androidx.annotation.StringRes;

import com.example.adscene.R;
import com.uber.android.mob.AMApplication;
import com.uber.android.mob.util.StringUtil;

import corall.base.app.CorApplication;
import corall.base.util.StringUtil;
import cr.s.a.ADGAConstant;
import cr.s.a.R;
import cr.s.a.bean.AdGARecord;

public class GARecordUtils {

    public static void reportFunc(CorApplication amApplication, String categoryId, String actionId, String labelId) {
//        Message msg = new Message();
//        msg.what = R.id.poster_msg_func_record;
//        AdGARecord record = new AdGARecord(categoryId, actionId, labelId);
//        msg.obj = record;
//        amApplication.handleMobMessage(msg);
    }

    public static void reportAd(CorApplication amApplication, String categoryId, String actionId, String labelId) {
//        Message msg = new Message();
//        msg.what = R.id.poster_msg_ad_record;
//        AdGARecord record = new AdGARecord(categoryId, actionId, labelId);
//        msg.obj = record;
//        amApplication.handleMobMessage(msg);
    }

    /**
     * 没有activity 在前台，需要activity 辅助 请求及展示 广告
     *
     * @param imContext
     * @param type
     * @param adSceneName
     */
    public static void onADLoadByActivity(CorApplication imContext, AdType type, String adSceneName) {
        reportAd(imContext, ADGAConstant.C_AD, ADGAConstant.A_REQUEST, StringUtil.decodeStringRes(imContext, R.string.poster_record_ad_on_load_activity, type.getName(), adSceneName));
    }

    /**
     * 应用有可见界面，主动 block 广告请求 （实际上没有发出请求）
     *
     * @param imContext
     * @param type
     * @param adSceneName
     */
    public static void onADLoadBlock(CorApplication imContext, AdType type, String adSceneName, int error) {
        reportAd(imContext, ADGAConstant.C_AD, ADGAConstant.A_FAIL, StringUtil.decodeStringRes(imContext, R.string.poster_record_ad_on_block, type.getName(), adSceneName) + "_(" + error + ")");
    }


    public static void onADFilledEffective(CorApplication imContext, String categoryId, String adSceneName) {
        reportAd(imContext, categoryId, ADGAConstant.A_FILL, StringUtil.decodeStringRes(imContext, R.string.poster_record_ad_on_filled_effective, ADGAConstant.PREFIX_AD, adSceneName));
    }
    /**
     * 不计算广告平台及类型的展示
     *
     * @param imContext
     * @param adSceneName
     */
    public static void onADShowEffective(CorApplication imContext, String categoryId, String adSceneName) {
        reportAd(imContext, categoryId, ADGAConstant.A_SHOW, StringUtil.decodeStringRes(imContext, R.string.poster_record_ad_on_show_effective, ADGAConstant.PREFIX_AD, adSceneName));
    }

    /**
     * 不计算广告平台及类型的请求
     *
     * @param imContext
     * @param adSceneName
     * @param groupIndex
     */
    public static void onADRequestEffective(CorApplication imContext, String categoryId, String adSceneName, int groupIndex) {
        if (groupIndex == ADConstant.INVALID_ID) {
            reportAd(imContext, categoryId, ADGAConstant.A_REQUEST, StringUtil.decodeStringRes(imContext, R.string.poster_record_ad_on_load_effective, ADGAConstant.PREFIX_AD, adSceneName));
        } else {
            reportAd(imContext, categoryId, ADGAConstant.A_REQUEST, StringUtil.decodeStringRes(imContext, R.string.poster_record_ad_on_load_effective, ADGAConstant.PREFIX_AD, adSceneName) + "_(" + groupIndex + ")");
        }
    }

    /**
     * 以下是 广告的 请求、填充、展示、点击、失败
     */
    public static void onADLoad(CorApplication imContext, String categoryId, AdType type, String adSceneName, int code) {
        reportAd(imContext, categoryId, ADGAConstant.A_REQUEST, R.string.poster_record_ad_on_load, ADGAConstant.PREFIX_AD, type, adSceneName, code);
    }

    public static void onADFilled(CorApplication imContext, String categoryId, AdType type, String adSceneName) {
        reportAd(imContext, categoryId, ADGAConstant.A_FILL, R.string.poster_record_ad_on_filled, ADGAConstant.PREFIX_AD, type, adSceneName);
    }

    public static void onADShow(CorApplication imContext, String categoryId, AdType type, String adSceneName) {
        reportAd(imContext, categoryId, ADGAConstant.A_SHOW, R.string.poster_record_ad_on_show, ADGAConstant.PREFIX_AD, type, adSceneName);
    }

    public static void onADClick(CorApplication imContext, String categoryId, AdType type, String adSceneName) {
        reportAd(imContext, categoryId, ADGAConstant.A_CLICK, R.string.poster_record_ad_on_click, ADGAConstant.PREFIX_AD, type, adSceneName);
    }

    public static void onADFail(CorApplication imContext, String categoryId, AdType type, String adSceneName, int code) {
        reportAd(imContext, categoryId, ADGAConstant.A_FAIL, R.string.poster_record_ad_on_fail, ADGAConstant.PREFIX_AD, type, adSceneName, code);
    }

    /**
     * 带 场景名字、广告类型
     *
     * @param imContext
     * @param category
     * @param action
     * @param label
     * @param adSceneName
     * @param type
     */
    private static void reportAd(CorApplication imContext, String category, String action, @StringRes int label, String prefix, AdType type, String adSceneName) {
        reportAd(imContext, category, action, StringUtil.decodeStringRes(imContext, label, prefix, type.getName(), adSceneName));
    }

    /**
     * 带 场景名字、广告类型、错误码或识别码
     *
     * @param imContext
     * @param category
     * @param action
     * @param label
     * @param adSceneName
     * @param type
     */
    private static void reportAd(CorApplication imContext, String category, String action, @StringRes int label, String prefix, AdType type, String adSceneName, int code) {
//        if (code == ADConstant.INVALID_ID) {
//            reportAd(imContext, category, action, StringUtil.decodeStringRes(imContext, label, prefix, type.getName(), adSceneName));
//        } else {
//            reportAd(imContext, category, action, StringUtil.decodeStringRes(imContext, label, prefix, type.getName(), adSceneName) + "_(" + code + ")");
//        }
    }
}
