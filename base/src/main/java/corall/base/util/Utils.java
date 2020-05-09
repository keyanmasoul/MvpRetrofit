package corall.base.util;

import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by ChenLi on 2017/11/20.
 */

public class Utils {

    /**
     * 读Assets资源文件
     * @param context
     * @param path
     * @return
     */
    public static String getAssetsFileContent(Context context, String path) {
        BufferedInputStream bis = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            bis = new BufferedInputStream(context.getAssets().open(path));
            byte[] buffer = new byte[4096];
            int readLen = -1;
            while ((readLen = bis.read(buffer)) > 0) {
                bos.write(buffer, 0, readLen);
            }
        } catch (IOException e) {
            Log.e("Utils", "IOException :" + e.getMessage());
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                }
            }
        }

        return bos.toString();
    }

}
