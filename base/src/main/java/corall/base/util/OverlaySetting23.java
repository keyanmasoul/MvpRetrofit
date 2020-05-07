package corall.base.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

public class OverlaySetting23 {
    private static final String MARK = Build.MANUFACTURER.toLowerCase();

    public static void start(Context context) {
        if (MARK.contains("meizu")) {
            if (!meiZuApi(context) && !defaultApi(context)) {
                appDetailsApi(context);
            }
        } else if (!defaultApi(context)) {
            appDetailsApi(context);
        }
    }

    private static boolean meiZuApi(Context context) {
        Intent overlayIntent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
        overlayIntent.putExtra("packageName", context.getPackageName());
        overlayIntent.setComponent(new ComponentName("com.meizu.safe", "com.meizu.safe.security.AppSecActivity"));
        try {
            context.startActivity(overlayIntent);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private static boolean defaultApi(Context context) {
        try {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            intent.setData(Uri.fromParts("package", context.getPackageName(), null));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private static void appDetailsApi(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        context.startActivity(intent);
    }
}
