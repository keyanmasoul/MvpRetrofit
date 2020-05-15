package corall.activity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.Nullable;

import com.example.adscene.R;

import corall.ad.bean.interstitial.DirectInterstitialAd;
import corall.ad.bean.interstitial.WebInterstitialAd;
import corall.ad.web.AdWebChromeClient;
import corall.ad.web.AdWebViewClient;
import corall.ad.web.IWebViewController;
import corall.adscene.AdType;
import corall.adscene.EntranceType;
import corall.adscene.scene.AdStrategyScene;
import corall.adscene.scene.inner.CommonInterstitialScene;
import corall.adscene.scene.outer.CommonInterstitialOutScene;
import corall.base.app.CorApplication;
import corall.base.util.StatusBarUtil;

public class WebAdActivity extends InterstitialActivity {

    private static final int TYPE_DIRECT = 0;
    private static final int TYPE_WEBGAME = 1;
    private String placeId;
    private int type;

    private WebView mWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StatusBarUtil.setColor(this, 0xb3000000);
        setContentView(R.layout.adv_web_to_interstitial);

        String url = getIntent().getStringExtra(WebInterstitialAd.URL_KEY);
        if (TextUtils.isEmpty(url)) {
            url = getIntent().getStringExtra(DirectInterstitialAd.URL_KEY);
            type = TYPE_DIRECT;
        } else {
            type = TYPE_WEBGAME;
        }
        placeId = getIntent().getStringExtra(WebInterstitialAd.AD_PLACE_ID);
        EntranceType[] entranceTypes = EntranceType.values();
        for (EntranceType entranceType : entranceTypes) {
            if (entranceType.getPid() == Integer.parseInt(placeId)) {
                mEntranceType = entranceType;
                break;
            }
        }
        mWebView = findViewById(R.id.ad_web);
        initWebView(mWebView);
        mWebView.loadUrl(url);

        View btn = findViewById(R.id.adv_web_close_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.adv_web_back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.goBack();
            }
        });

        findViewById(R.id.adv_web_forward_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.goForward();
            }
        });

        findViewById(R.id.adv_web_refresh_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.reload();
            }
        });
    }

    @Override
    public void close() {
        finish();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mEntranceType != null) {
            final AdStrategyScene adScene = (AdStrategyScene) mEntranceType.getAdScene();
            if (adScene instanceof CommonInterstitialScene) {
                adScene.onAdClose(AdType.DIRECT_INTERSTITIAL);
            } else if (adScene instanceof CommonInterstitialOutScene) {
                adScene.onAdClose(AdType.DIRECT_INTERSTITIAL);
            }
        }
    }

    private void initWebView(WebView wv) {
        wv.setWebViewClient(new AdWebViewClient(new IWebViewController() {

            @Override
            public void onWebJump() {
//                if (mListener != null) {
//                    mListe            android:taskAffinity="com.a.b.c.Ad" />ner.onClick(DirectInterstitialAd.this);
//                }
//                close();
            }
        }));
        wv.setWebChromeClient(new AdWebChromeClient());

        WebSettings webSettings = wv.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAppCacheMaxSize(Long.MAX_VALUE);
        webSettings.setAllowFileAccessFromFileURLs(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webSettings.setAllowUniversalAccessFromFileURLs(true);
        }
        String appCacheDir = CorApplication.getInstance().getCacheDir().getAbsolutePath();
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
}
