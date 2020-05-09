package corall.ad;


import java.io.UnsupportedEncodingException;

import corall.base.app.AMApplication;
import corall.base.util.StringUtil;

public class HandlerKeyConstant {

    private static final String DEFKEY = "data";

    public static void init() {
        String[] STR_DATA = new String[0];
        try {
            STR_DATA = decode(StringUtil.decodeStringRes(AMApplication.getInstance(), R.string.arc_mob_json_keys));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (STR_DATA.length == 0) {
            return;
        }
        RESULT = STR_DATA[0];
        HMADS = STR_DATA[1];
        BLOCKABLE = STR_DATA[2];
        PLACEID = STR_DATA[3];
        TYPE = STR_DATA[4];
        ADS = STR_DATA[5];
        ADGROUP = STR_DATA[6];
        EXTRA = STR_DATA[7];
        SWITCH_INFO = STR_DATA[8];
        PROTIME_INFO = STR_DATA[9];
        LIMIT_INFO = STR_DATA[10];
        REQ_LIMIT_INFO = STR_DATA[11];
        INTERVAL_INFO = STR_DATA[12];
        GRAY = STR_DATA[13];
        TIMECOUNT = STR_DATA[14];
        FROZEN = STR_DATA[15];
        IS_STORE = STR_DATA[16];
        STOREID = STR_DATA[17];
        BLOCK = STR_DATA[18];
        CHANCE = STR_DATA[19];
        ACTIONS = STR_DATA[20];
        NATIONLIST = STR_DATA[21];
        BGPLACE = STR_DATA[22];
        NATIVE = STR_DATA[23];
        BANNER = STR_DATA[24];
        INTERSTITIAL = STR_DATA[25];
        REWARD = STR_DATA[26];
        ID = STR_DATA[27];
        SIZE = STR_DATA[28];
        PLATFORM = STR_DATA[29];
        URL = STR_DATA[30];
        TITLE = STR_DATA[31];
        ICON = STR_DATA[32];
        IMAGE = STR_DATA[33];
        CALL_ACTION = STR_DATA[34];
        DESC = STR_DATA[35];
        PKG = STR_DATA[36];
        ACTION = STR_DATA[37];
        UNION = STR_DATA[38];
        JUMPOUT = STR_DATA[39];
    }

    private static String[] decode(String data) throws UnsupportedEncodingException {
        return data.split("22");
    }

    public static String RESULT = DEFKEY;
    public static String HMADS = DEFKEY;
    public static String BLOCKABLE = DEFKEY;
    public static String PLACEID = DEFKEY;
    public static String TYPE = DEFKEY;
    public static String ADS = DEFKEY;
    public static String ADGROUP = DEFKEY;
    public static String EXTRA = DEFKEY;
    public static String SWITCH_INFO = DEFKEY;
    public static String PROTIME_INFO = DEFKEY;
    public static String LIMIT_INFO = DEFKEY;
    public static String REQ_LIMIT_INFO = DEFKEY;
    public static String INTERVAL_INFO = DEFKEY;
    public static String GRAY = DEFKEY;
    public static String TIMECOUNT = DEFKEY;
    public static String FROZEN = DEFKEY;
    public static String IS_STORE = DEFKEY;
    public static String STOREID = DEFKEY;
    public static String BLOCK = DEFKEY;
    public static String CHANCE = DEFKEY;
    public static String ACTIONS = DEFKEY;
    public static String NATIONLIST = DEFKEY;
    public static String BGPLACE = DEFKEY;
    public static String NATIVE = DEFKEY;
    public static String BANNER = DEFKEY;
    public static String INTERSTITIAL = DEFKEY;
    public static String REWARD = DEFKEY;
    public static String ID = DEFKEY;
    public static String SIZE = DEFKEY;
    public static String PLATFORM = DEFKEY;
    public static String URL = DEFKEY;
    public static String TITLE = DEFKEY;
    public static String ICON = DEFKEY;
    public static String IMAGE = DEFKEY;
    public static String CALL_ACTION = DEFKEY;
    public static String DESC = DEFKEY;
    public static String PKG = DEFKEY;
    public static String ACTION = DEFKEY;
    public static String UNION = DEFKEY;
    public static String JUMPOUT = DEFKEY;
}
