package corall.base;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.provider.Settings;
import android.view.Gravity;
import android.view.KeyEvent;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import corall.base.bean.MessageEvent;
import corall.base.dialog.DialogPlus;
import corall.base.dialog.OverlayDialog;
import corall.base.dialog.ViewHolder;
import corall.base.util.StatusBarUtil;
import corall.base.util.StringUtil;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import zjj.network.R;

/**
 * BaseActivity
 * create by zhaojj 2018/6/28
 */
abstract public class BaseActivity extends AppCompatActivity {

    protected final static int REQUEST_CODE_PERMISSION_NO = -1;
    protected boolean lock = false;
    //是否处于可退出状态
    private boolean isExit;
    //双击退出提示文字
    private String exitTip;

    public void setUseAnimation(boolean useAnimation) {
        this.useAnimation = useAnimation;
    }

    private boolean useAnimation = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && isTranslucentOrFloating(this)) {
            fixOrientation(this);
        }
        super.onCreate(savedInstanceState);
        checkContext();
        exitTip = getStr(R.string.click_exit_tip);
        setContentView(setLayout());
        initViewAndData();
        StatusBarUtil.setColor(this, getResources().getColor(setStatusBarColor()));
    }

    protected int setStatusBarColor() {
        return R.color.base_primary_color;
    }

    abstract protected void checkContext();

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Glide.with(this).resumeRequests();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Glide.with(this).pauseRequests();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    protected abstract int setLayout();

    protected abstract void initViewAndData();

    /**
     * 获取解密字符串
     *
     * @param resId resId
     * @return title
     */
    protected String getStr(int resId) {
        return StringUtil.decodeStringRes(this, resId);
    }

    protected String getStrFormat(int resId, Object... formatArgs) {
        return StringUtil.decodeStringRes(this, resId, formatArgs);
    }

    protected String getStr(@StringRes int res, Object... formatArgs) {
        return StringUtil.decodeStringRes(this, res, formatArgs);
    }

    /**
     * 判断是否有notification权限
     *
     * @return
     */
    protected boolean checkNotifyPermission() {
        String packageName = getApplicationContext().getPackageName();
        String flat = Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners");
        boolean power = false;
        if (flat != null) {
            power = flat.contains(packageName);
        }
        return power;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (lock) {
            return false;
        }
        if (setExitBy2Click()) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {// 点击返回
                exitBy2Click();
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exitBy2Click() {
        if (preDoOnExit()) {
            return;
        }
        Timer tExit;
        if (!isExit) {
            isExit = true;
            ToastUtils.showShort(exitTip);
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                public void run() {
                    isExit = false;
                }
            }, 2000);

        } else {
            if (!doPreExit()) {
                doExitClient();
            }
        }
    }

    protected boolean doPreExit() {
        return false;
    }

    protected boolean preDoOnExit() {
        return false;
    }

    protected boolean setExitBy2Click() {
        return false;
    }

    private void clearAllCache() {
//        imContext.checkForLongLive(true);
    }

    public void doExitClient() {
        // 关闭所有试图
//        closeAllActivity();
        try {
            clearAllCache();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //开发者模式下可以关闭云配
//        AdvSDK.getInstance().updateAdConfig();
    }

    @Override
    public void finish() {
        super.finish();
        if (useAnimation) {
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }

    }

    /**
     * Request permissions.
     */
    public void requestPermission(int requestCode, String... permissions) {
        ActivityCompat.requestPermissions(this, permissions, requestCode);
    }

    public void requestPermission(String... permissions) {
        ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSION_CODE_DEFAULT);
    }


    //申请的权限被用户勾选不在提示,应用自身弹框提示用户
    //该方法为弹框提示 取消 按键回调, 具体业务给调用的activity子类实现
    protected void alwaysDeniedStillCancel(List<String> permissions) {

    }


    //该方法为弹框提示 设置 按键回调
    protected void alwaysDeniedStillSetting(List<String> permissions) {

    }

    //该方法为关闭系统设置页面后的回调
    protected void comeBackFromSystemSetting(List<String> permissions) {

    }

    /**
     * Alert view
     */
    protected void requestOverlay(int requestCode) {
        if (!checkOverlayPermission()) {
            OverlayDialog dialogPlusOverlay = new OverlayDialog(this);
            dialogPlusOverlay.show();
        } else {
            onOverlayPermissionGranted(requestCode);
        }
    }

    protected void onOverlayPermissionGranted(int requestCode) {

    }

    protected void onOverlayPermissionDenied(int requestCode) {
        ToastUtils.showShort(getStr(R.string.overlay_tip));
    }

    /**
     * 判断callservice是否开启
     */
    public void checkCallService() {
//        imContext.handleMobEmptyMessage(R.id.msg_check_caller_listener);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleMessage(MessageEvent messageEvent) {
        subHandleMessage(messageEvent);
    }

    abstract protected void subHandleMessage(MessageEvent messageEvent);

    protected void startActivityAnim(Class pointClass) {
        Intent intent = new Intent(this, pointClass);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, 0);
    }

    protected void startActivityAnim(Class pointClass, int requestCode) {
        Intent intent = new Intent(this, pointClass);
        startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.fade_in, 0);
    }

    /**
     * 针对 Android 27 的情况进行处理
     * 横竖屏设置了方向会崩溃的问题
     *
     * @return
     */
    public boolean isTranslucentOrFloating(Activity activity) {
        boolean isTranslucentOrFloating = false;
        try {
            int[] styleableRes = (int[]) Class.forName("com.android.internal.R$styleable").getField("Window").get(null);
            TypedArray ta = activity.obtainStyledAttributes(styleableRes);
            Method m = ActivityInfo.class.getMethod("isTranslucentOrFloating", TypedArray.class);
            m.setAccessible(true);
            isTranslucentOrFloating = (boolean) m.invoke(null, ta);
            m.setAccessible(false);
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return isTranslucentOrFloating;
    }

    /**
     * 修复横竖屏 crash 的问题
     *
     * @return
     */
    public void fixOrientation(Activity activity) {
        try {
            Field field = Activity.class.getDeclaredField("mActivityInfo");
            field.setAccessible(true);
            ActivityInfo o = (ActivityInfo) field.get(activity);
            o.screenOrientation = -1;
            field.setAccessible(false);
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    protected static final int REQUEST_PERMISSION_CODE_DEFAULT = 5001;

    protected void requestPermissionCompat(String[] permissions) {
        ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSION_CODE_DEFAULT);
    }

    protected void requestPermissionCompat(String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(this, permissions, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissions.length > 0) {
            List<String> deniedPermissions = new ArrayList<>();
            List<String> grantedPermissions = new ArrayList<>();
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    grantedPermissions.add(permissions[i]);
                } else {
                    deniedPermissions.add(permissions[i]);
                }
            }
            onCompatPermissionGranted(grantedPermissions, requestCode);
            onCompatPermissionDenied(deniedPermissions, requestCode);
        }
    }

    protected void onCompatPermissionGranted(List<String> grantedPermissions, int requestCode) {

    }

    protected void onCompatPermissionDenied(List<String> deniedPermissions, int requestCode) {

    }

    protected DialogPlus createDialog(int layoutId) {
        return createDialog(layoutId, false);
    }

    protected DialogPlus createDialog(int layoutId, boolean cancelable) {
        return DialogPlus.newDialog(this)
                .setCancelable(false)
                .setContentHolder(new ViewHolder(layoutId))
                .setGravity(Gravity.CENTER)
                .setContentBackgroundResource(R.color.trans)
                .create();
    }

    protected void sendEmptyMessage(int what, long delay) {
        MessageEvent event = new MessageEvent();
        event.setWhat(what);
        EventBus.getDefault().post(event);
    }

    protected void sendMessage(MessageEvent messageEvent) {
        EventBus.getDefault().post(messageEvent);
    }

    protected void sendMessageDelay(MessageEvent messageEvent, long delay) {
        Observable.timer(delay, TimeUnit.MILLISECONDS).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Long aLong) {
                EventBus.getDefault().post(messageEvent);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    protected void sendEmptyMessageDelay(int what, long delay) {
        Observable.timer(delay, TimeUnit.MILLISECONDS).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Long aLong) {
                MessageEvent event = new MessageEvent();
                event.setWhat(what);
                EventBus.getDefault().post(event);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
            }
        });
    }


    protected boolean checkOverlayPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            try {
                Class cls = Class.forName("android.content.Context");
                Field declaredField = cls.getDeclaredField("APP_OPS_SERVICE");
                declaredField.setAccessible(true);
                Object obj = declaredField.get(cls);
                if (!(obj instanceof String)) {
                    return false;
                }
                String str2 = (String) obj;
                obj = cls.getMethod("getSystemService", String.class).invoke(getApplicationContext(), str2);
                cls = Class.forName("android.app.AppOpsManager");
                Field declaredField2 = cls.getDeclaredField("MODE_ALLOWED");
                declaredField2.setAccessible(true);
                Method checkOp = cls.getMethod("checkOp", Integer.TYPE, Integer.TYPE, String.class);
                int result = (Integer) checkOp.invoke(obj, 24, Binder.getCallingUid(), getApplicationContext().getPackageName());
                return result == declaredField2.getInt(cls);
            } catch (Exception e) {
                return false;
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                AppOpsManager appOpsMgr = (AppOpsManager) getApplicationContext().getSystemService(Context.APP_OPS_SERVICE);
                if (appOpsMgr == null)
                    return false;
                int mode = appOpsMgr.checkOpNoThrow("android:system_alert_window", android.os.Process.myUid(), getApplicationContext()
                        .getPackageName());
                return Settings.canDrawOverlays(getApplicationContext()) || mode == AppOpsManager.MODE_ALLOWED || mode == AppOpsManager.MODE_IGNORED;
            } else {
                return Settings.canDrawOverlays(getApplicationContext());
            }
        }
    }

}
