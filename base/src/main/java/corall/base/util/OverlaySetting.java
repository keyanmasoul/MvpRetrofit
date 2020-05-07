package corall.base.util;

import android.content.Context;
import android.os.Build;

public class OverlaySetting {

    public static void start(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            OverlaySetting23.start(context);
        } else {
            OverlaySetting22.start(context);
        }
    }
}
