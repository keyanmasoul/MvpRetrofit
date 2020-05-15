package corall.ad;

import java.io.UnsupportedEncodingException;

import corall.ad.bean.RawAd;
import corall.base.util.Base64;

public class AdUtils {

    public static byte[] getDecodedConfigBase64(String config, int off) throws UnsupportedEncodingException {

        byte[] decode = Base64.decode(config.getBytes("utf-8"));
        for (int i = 0; i < decode.length; ++i) {
            decode[i] += off;
        }
        return decode;
    }

    public static byte[] getDecodedConfig(String config, int off) throws UnsupportedEncodingException {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < config.length(); i++) {
            sb.append((char) (config.charAt(i) + off));
        }
        String result = sb.toString();
        if (!result.startsWith("{")) {
            return getDecodedConfigBase64(config, off);
        }
        return result.getBytes("utf-8");

    }

    public static boolean isFaceBook(RawAd ad) {
        if (ad == null) {
            return false;
        }
        if (ad.getPlatform().equals(AdsContants.AD_PLATFORM_FACEBOOK)) {
            return true;
        }

        return false;
    }

    public static boolean isMopub(RawAd ad) {
        if (ad == null) {
            return false;
        }
        if (ad.getPlatform().equals(AdsContants.AD_PLATFORM_MOPUB)) {
            return true;
        }
        return false;
    }

    public static boolean isAdmob(RawAd ad) {
        if (ad == null) {
            return false;
        }
        if (ad.getPlatform().equals(AdsContants.AD_PLATFORM_ADMOB) /**|| ad.getPlatform().equals(AdsContants.AD_PLATFORM_ADMOB_NEW)**/) {
            return true;
        }


        return false;
    }
}
