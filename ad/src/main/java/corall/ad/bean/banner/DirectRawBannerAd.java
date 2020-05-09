package corall.ad.bean.banner;

import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import corall.ad.AdsContants;
import corall.ad.bean.listener.banner.BannerLoadListener;
import corall.ad.web.AdBannerWebViewClient;
import corall.ad.web.AdWebChromeClient;
import corall.ad.web.IWebViewController;
import corall.base.app.AMApplication;

public class DirectRawBannerAd extends RawBannerAd {
    private String id;
    private WebView realBannerAd;
    private BannerLoadListener mListener;
    private String url;
    private boolean isJumpOut;
    private String action;

    public DirectRawBannerAd(String id, String url) {
        this.id = id;
        this.url = url;
        this.isJumpOut = false;
        action = null;
    }

    @Override
    public String getPlatform() {
        return AdsContants.AD_PLATFORM_DIRECT;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getAction() {
        return action;
    }

    public boolean isJumpOut() {
        return isJumpOut;
    }

    @Override
    public void setAdListener(BannerLoadListener listener) {
        mListener = listener;
    }

    @Override
    public View getBannerView() {
        if (realBannerAd == null) {
            realBannerAd = new WebView(AMApplication.getInstance());
            initWebView(realBannerAd);
            realBannerAd.loadUrl(url);
        }
        return realBannerAd;
    }

    public void setJumpOut(boolean isJumpOut) {
        this.isJumpOut = isJumpOut;
    }

    public void setJumpAction(String action) {
        this.action = action;
    }

    @Override
    public boolean isLoading()  {
        return false;
    }

    @Override
    public void destroy()  {
        if (realBannerAd != null) {
            realBannerAd.destroy();
            realBannerAd = null;
        }
        mListener = null;
    }

    @Override
    public void load()  {
        super.load();
        if (mListener != null) {
            mListener.onAdLoaded(this);
        }

    }

    private void initWebView(WebView wv) {
        AdBannerWebViewClient webViewClient = new AdBannerWebViewClient(new IWebViewController() {

            @Override
            public void onWebJump() {
                if (mListener != null) {
                    mListener.onClick(DirectRawBannerAd.this);
                }
            }
        });
        webViewClient.setJumpOut(isJumpOut);
        webViewClient.setAction(action);
        wv.setWebViewClient(webViewClient);
        AdWebChromeClient chromeClient = new AdWebChromeClient(new IWebViewController() {

            @Override
            public void onWebJump() {
                if (mListener != null) {
                    mListener.onClick(DirectRawBannerAd.this);
                }
            }
        });

        chromeClient.setJumpOut(isJumpOut);
        chromeClient.setAction(action);
        wv.setWebChromeClient(chromeClient);

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

        if (!TextUtils.isEmpty(action) || isJumpOut) {
            webSettings.setSupportMultipleWindows(true);
        }
    }
}
