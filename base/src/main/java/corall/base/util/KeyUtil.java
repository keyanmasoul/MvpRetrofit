package corall.base.util;

import corall.base.app.AMApplication;

public class KeyUtil {

    public final static String getKey(int res){
        String title = StringUtil.decodeStringRes(AMApplication.getInstance(), res);
        String[] name = AMApplication.getInstance().getPackageName().split("\\.");
        String key = title + "_" + name[1] + "_" + name[2].substring(0,1);
        return key;
    }
}
