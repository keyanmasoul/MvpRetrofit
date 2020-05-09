package corall.ad.bean.interstitial;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import corall.ad.AdsContants;
import corall.ad.R;
import corall.ad.bean.listener.interstitialad.InterstitialAdLoadListener;
import corall.ad.web.AdWebChromeClient;
import corall.ad.web.AdWebViewClient;
import corall.ad.web.IWebViewController;
import corall.base.PermissionUtil;
import corall.base.app.AMApplication;

public class DirectInterstitialAd extends RawInterstitialAd {
    private String id;
    private String url;
    protected WindowManager mWindowManager;
    protected BroadcastReceiver mReceiver;

    private boolean isOuter = false;

    final String SYSTEM_DIALOG_REASON_KEY = "reason";

    final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

    public static final String URL_KEY = "direct_url";
    public static final String AD_PLACE_ID = "place_id";
    private View showView;

    // 广告请求监听
    private InterstitialAdLoadListener mListener;

    public DirectInterstitialAd(String id, String url) {
        this.id = id;
        this.url = url;
    }

    public void setOuter(boolean isOuter) {
        this.isOuter = isOuter;
    }

    public boolean getOuter() {
        return isOuter;
    }

    @Override
    public String getPlatform() {
        return AdsContants.AD_PLATFORM_DIRECT;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void load() {
        super.load();
        if (mListener != null) {
            mListener.onAdLoaded(this);
        }

    }

    @Override
    public void load(Activity activity) {
        super.load(activity);
        if (mListener != null) {
            mListener.onAdLoaded(this);
        }

    }

    @Override
    public void setAdListener(InterstitialAdLoadListener listener) {
        mListener = listener;
    }

    public void showByAct(Class clazz) {
        Context ctx = AMApplication.getInstance();
        if (isOuter) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            try {
                ctx.startActivity(intent);
                if (mListener != null) {
                    mListener.onShow(this);
                }
            } catch (Exception ex) {
            }
            return;
        }
        try {
            Intent intent = new Intent(ctx, clazz);
            intent.putExtra(URL_KEY, url);
            intent.putExtra(AD_PLACE_ID, getPlaceId());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                ctx.startActivity(intent);
                if (mListener != null) {
                    mListener.onShow(this);
                }
            } catch (Exception ex) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void show() {
        Context ctx = AMApplication.getInstance();
        if (isOuter) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            try {
                ctx.startActivity(intent);
                if (mListener != null) {
                    mListener.onShow(this);
                }
            } catch (Exception ex) {

            }
            return;
        }


        WindowManager.LayoutParams windowViewParams = new WindowManager.LayoutParams();
        windowViewParams.format = PixelFormat.RGBA_8888;
        windowViewParams.gravity = Gravity.TOP | Gravity.LEFT;
        //先判断是否有悬浮窗权限
        if (PermissionUtil.checkDrawOverlaysPermission(ctx)) {
            //8.0以上使用 TYPE_APPLICATION_OVERLAY
            if (Build.VERSION.SDK_INT > 25) {
                windowViewParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                windowViewParams.type = 2010;
            }
        } else {
            //如果没有悬浮窗权限 7.0及以下使用 toast
            if (Build.VERSION.SDK_INT <= 24) {
                windowViewParams.type = WindowManager.LayoutParams.TYPE_TOAST;
            } else {
                //7.1及以上,不显示
                close();
                return;
            }
        }

        Point size = new Point();
        mWindowManager = (WindowManager) AMApplication.getInstance().getSystemService(Context.WINDOW_SERVICE);
        mWindowManager.getDefaultDisplay().getSize(size);
        windowViewParams.width = size.x;
        windowViewParams.height = size.y - getStatusBarHeight(ctx);

        showView = LayoutInflater.from(ctx).inflate(R.layout.direct_interstitial_layout, null);
        WebView wb = showView.findViewById(R.id.ad_web);
        initWebView(wb);
        wb.loadUrl(url);

        View btn = showView.findViewById(R.id.adv_sdk_close_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });


        try {
            mWindowManager.addView(showView, windowViewParams);

            showView.setFocusableInTouchMode(true);
            showView.setOnKeyListener(new View.OnKeyListener() {
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
            ctx.registerReceiver(mReceiver, homeFilter);
            wb.loadUrl(url);
        } catch (Exception e) {

        }

        if (mListener != null) {
            mListener.onShow(this);
        }

    }

    private void initWebView(WebView wv) {
        wv.setWebViewClient(new AdWebViewClient(new IWebViewController() {

            @Override
            public void onWebJump() {
                if (mListener != null) {
                    mListener.onClick(DirectInterstitialAd.this);
                }
                close();
            }
        }));
        wv.setWebChromeClient(new AdWebChromeClient());

        WebSettings webSettings = wv.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAppCacheMaxSize(Long.MAX_VALUE);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        String appCacheDir = AMApplication.getInstance().getCacheDir().getAbsolutePath();
        webSettings.setDatabaseEnabled(true);
        //下面设置需配套使用，否则会导致加载大型页面时，页面异常
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        webSettings.setDatabasePath(appCacheDir);
        webSettings.setAppCachePath(appCacheDir);//h5缓存
        webSettings.setAppCacheEnabled(true);//h5缓存是否开启
        webSettings.setAllowFileAccess(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setSaveFormData(false);
        webSettings.setJavaScriptEnabled(true); //允许JavaScript执行
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);//启用内置的缩放功能
        webSettings.setDisplayZoomControls(false);//去掉缩放按钮
        webSettings.setBlockNetworkImage(false);
    }

    public void close() {

        try {
            if (mWindowManager != null && showView != null) {
                WebView wb = showView.findViewById(R.id.ad_web);
                if (wb != null) {
                    wb.destroy();
                }
                mWindowManager.removeView(showView);
                showView = null;

            }
            if (mReceiver != null) {
                AMApplication.getInstance().unregisterReceiver(mReceiver);
                mReceiver = null;
            }
        } catch (Exception e) {

        }
        if (mListener != null) {
            mListener.onClose(this);
            mListener = null;
        }
    }

    @Override
    public boolean isLoaded() {
        return true;
    }

    @Override
    public boolean isLoading() {
        return false;
    }

    @Override
    public void destroy() {

    }

    /* 判断是否有系统提示框权限
     *
     * @param context
     * @return
     */
    private static boolean checkAlertWindowPermisson(Context context) {
        String packname = context.getPackageName();
        return (PackageManager.PERMISSION_GRANTED == context.getPackageManager().checkPermission("android.permission.SYSTEM_ALERT_WINDOW", packname));
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


}
