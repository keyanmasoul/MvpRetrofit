package corall.ad.ui.window;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import corall.ad.ui.card.CommonNativeAdCardView;
import corall.base.util.PermissionUtil;

/**
 * Created by ChenLi on 2018/1/5.
 */

public class FullScreenWindow {

    protected Context mCtx;
    protected WindowManager mWindowManager;
    protected BroadcastReceiver mReceiver;
    private boolean touchToClose;
    private View showView;

    final String SYSTEM_DIALOG_REASON_KEY = "reason";

    final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

    public FullScreenWindow(Context ctx) {
        this.mCtx = ctx;
        touchToClose = true;
    }

    public void isTouchToClose(boolean isTouchToClose) {
        touchToClose = isTouchToClose;
    }

    public boolean showWindowManager(View v) {
        return showWindowManager(v, true);
    }

    public boolean showWindowManager(View v, boolean bottomPadding) {
        showView = v;
        if (v instanceof CommonNativeAdCardView && bottomPadding) {
            View mainView = ((CommonNativeAdCardView) v).getMainView();
            mainView.setPadding(mainView.getPaddingLeft(), mainView.getPaddingTop(), mainView.getPaddingRight(), getNavigationBarHeight());
        }
        mWindowManager = (WindowManager) mCtx.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams windowViewParams = new WindowManager.LayoutParams();
        windowViewParams.gravity = 53;
        windowViewParams.flags = 524584;

        windowViewParams.flags |= 21497344;

        if (Build.VERSION.SDK_INT > 25) {
            windowViewParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            windowViewParams.systemUiVisibility = 2;
            windowViewParams.systemUiVisibility |= 4096;
            windowViewParams.systemUiVisibility |= 4;

        } else {
            if (PermissionUtil.checkDrawOverlaysPermission(mCtx)) {
                windowViewParams.type = 2010;
            } else {
                windowViewParams.type = WindowManager.LayoutParams.TYPE_TOAST;
            }
        }

        windowViewParams.flags |= 134217728;
        windowViewParams.flags |= 67108864;

        Display display = mWindowManager.getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);
        windowViewParams.x = 0;
        windowViewParams.y = getStatusBarHeight(mCtx);
        //高始终大于宽,保证竖屏绘制
        windowViewParams.width = size.x > size.y ? size.y : size.x;
        windowViewParams.height = size.x > size.y ? size.x : size.y;
        windowViewParams.height = windowViewParams.height - getStatusBarHeight(mCtx);

        windowViewParams.format = -2;
        windowViewParams.screenOrientation = 1;
        try {
            mWindowManager.addView(v, windowViewParams);

            if (touchToClose) {
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        close();
                    }
                });
            }

            v.setFocusableInTouchMode(true);
            v.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    close();
                    return true;
                }
            });

            mReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                        String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                        if (reason != null && reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
                            close();
                        }
                    } else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
                        close();
                    }
                }
            };

            //注册系统广播，home键和锁屏事件，关闭全屏广告（只限native广告类型）
            IntentFilter homeFilter = new IntentFilter();
            homeFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            homeFilter.addAction(Intent.ACTION_SCREEN_OFF);
            mCtx.registerReceiver(mReceiver, homeFilter);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void close() {

        try {
            if (mWindowManager != null && showView != null) {
                mWindowManager.removeView(showView);
                showView = null;

            }
            if (mCtx != null && mReceiver != null) {
                mCtx.unregisterReceiver(mReceiver);
                mReceiver = null;
            }
        } catch (Exception e) {

        }
    }


    private int getStatusBarHeight(Context ctx) {
        int statusBarHeight1 = -1;
        //获取status_bar_height资源的ID
        int resourceId = ctx.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight1 = ctx.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight1;
    }

    public int getNavigationBarHeight() {
        int resourceId = mCtx.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        try {
            return mCtx.getResources().getDimensionPixelSize(resourceId);
        } catch (Resources.NotFoundException e) {
            return 168;
        }
    }

}
