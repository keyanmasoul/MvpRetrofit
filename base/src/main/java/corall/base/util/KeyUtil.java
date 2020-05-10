package corall.base.util;

import corall.base.app.CorApplication;

public class KeyUtil {

    public static String getKey(int res){
        String title = StringUtil.decodeStringRes(CorApplication.getInstance(), res);
        String[] name = CorApplication.getInstance().getPackageName().split("\\.");
        return title + "_" + name[1] + "_" + name[2].substring(0,1);
    }
}
