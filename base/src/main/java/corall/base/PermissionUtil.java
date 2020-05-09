package corall.base;

import android.app.AppOpsManager;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import zjj.network.R;

public class PermissionUtil {

    private static int mTargetSdkVersion;

    public static boolean checkPermission(Context ctx, String permission) {
        if (Build.VERSION.SDK_INT >= 23) {
            return (ctx.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
        } else {
            return true;
        }


        //Android 6.0：默认情况下将授予应用程序的运行时权限（targetSdkVersion < 23）
        //但用户可以在Android设置中更改运行时权限,以下会是更严谨的检查方法

        /*int targetSdkVersion = 26;
        try {
            final PackageInfo info = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
            targetSdkVersion = info.applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= 23) {
            if (targetSdkVersion >= Build.VERSION_CODES.M) {
                // targetSdkVersion >= Android M, we can use Context#checkSelfPermission
                return ctx.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
            } else {
                // targetSdkVersion < Android M, we have to use PermissionChecker
                return PermissionChecker.checkSelfPermission(ctx, permission) == PermissionChecker.PERMISSION_GRANTED;
            }
        } else {
            return true;
        }*/

    }

    /* 判断是否有系统提示框权限
     *
     * @param context
     * @return
     */
    public static boolean checkDrawOverlaysPermission(Context context) {
        if (Build.VERSION.SDK_INT < 23) {
            return tryDisplayDialog(context);
        } else {
            return canDrawOverlays(context) && tryDisplayDialog(context);
        }
    }

    private static boolean canDrawOverlays(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            return getTargetSdkVersion(context) >= 23 ? Settings.canDrawOverlays(context) : reflectionOps(context, "OP_SYSTEM_ALERT_WINDOW");
        } else {
            return true;
        }
    }

    private static boolean tryDisplayDialog(Context context) {
        Dialog dialog = new Dialog(context, R.style.OverlayDialog_Theme_Transparent);
        Window window = dialog.getWindow();
        if (Build.VERSION.SDK_INT >= 26) {
            window.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        } else {
            window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }

        boolean var4;
        try {
            dialog.show();
            return true;
        } catch (Exception var8) {
            var4 = false;
        } finally {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

        }

        return var4;
    }

    private static int getTargetSdkVersion(Context context) {
        if (mTargetSdkVersion < 14) {
            mTargetSdkVersion = context.getApplicationInfo().targetSdkVersion;
        }

        return mTargetSdkVersion;
    }

    @RequiresApi(
            api = 19
    )
    private static boolean reflectionOps(Context context, String opFieldName) {
        int uid = context.getApplicationInfo().uid;

        try {
            Class<AppOpsManager> appOpsClass = AppOpsManager.class;
            Method method = appOpsClass.getMethod("checkOpNoThrow", Integer.TYPE, Integer.TYPE, String.class);
            Field opField = appOpsClass.getDeclaredField(opFieldName);
            int opValue = (Integer) opField.get(Integer.class);
            AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            String packageName = context.getApplicationContext().getPackageName();
            int result = (Integer) method.invoke(manager, opValue, uid, packageName);
            return result == 0 || result == 4 || result == 5;
        } catch (Throwable var10) {
            return true;
        }
    }
}
