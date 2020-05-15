package corall.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.example.adscene.R;

import java.util.Random;

import corall.ad.bean.RawAd;
import corall.ad.bean.banner.RawBannerAd;
import corall.adscene.EntranceType;
import corall.adscene.scene.AdScene;
import corall.adscene.scene.AdStrategyScene;
import corall.adscene.scene.inner.CommonInterstitialScene;
import corall.adscene.scene.outer.CommonInterstitialOutScene;
import corall.base.util.StringUtil;


/**
 * desc: 模拟 插屏广告（将banner广告以插屏的形式展示）
 * date: 2018/11/12
 * author: ancun
 */
public class BannerAdsActivity extends InterstitialActivity {

    public static void showInterstitialAd(Context context, String entranceType) {
        Intent intent = new Intent(context, ClassUtils.getBannerAdsActivity());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(ENTRANCE_TYPE, entranceType);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_activity_banner_to_interstitial);
        initHomeKeyReceiver();

        initView();
        initAdView(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        initView();
        initAdView(intent);
    }

    private void initView() {
        TextView tvTitle = findViewById(R.id.tv_recommend_title);

        Random random = new Random();
        int recommendNum = 98 - random.nextInt(10);
        String title = recommendNum + StringUtil.decodeStringRes(BannerAdsActivity.this, R.string.poster_ad_user_recommend);
        tvTitle.setText(title);
    }

    private void initAdView(Intent intent) {
        if (intent == null) {
            close();
            return;
        }

        mEntranceType = EntranceType.enumOf(intent.getStringExtra(ENTRANCE_TYPE));
        if (mEntranceType == null) {
            return;
        }

        final RawAd rawAd = ((AdStrategyScene) mEntranceType.getAdScene()).getActiveAd();
        if (!(rawAd instanceof RawBannerAd)) {
            close();
            return;
        }

        final RawBannerAd activeAd = (RawBannerAd) rawAd;

        TextView btn = findViewById(R.id.tv_close);
        btn.setText(StringUtil.decodeStringRes(this, R.string.poster_close));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                close();
            }
        });

        final View root = findViewById(R.id.root);
        root.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                root.getViewTreeObserver().removeOnPreDrawListener(this);

                AdScene adScene = mEntranceType.getAdScene();
                if (adScene instanceof CommonInterstitialScene) {
                    ((CommonInterstitialScene) adScene).onRawBannerShow();
                } else if (adScene instanceof CommonInterstitialOutScene) {
                    ((CommonInterstitialOutScene) adScene).onRawBannerShow();
                }

                return false;
            }
        });

        try {
            ViewGroup adContainer = findViewById(R.id.layout_ad_container);
            if (adContainer.getChildCount() > 0) {
                adContainer.removeAllViews();
            }

            View adView = activeAd.getBannerView();
            if (adView != null) {
                ViewParent viewParent = adView.getParent();
                if (viewParent instanceof ViewGroup) {
                    ((ViewGroup) viewParent).removeView(adView);
                }
                adContainer.addView(adView);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            final AdStrategyScene adScene = (AdStrategyScene) mEntranceType.getAdScene();
            if (adScene instanceof CommonInterstitialScene) {
                ((CommonInterstitialScene) adScene).onRawBannerClose();
            } else if (adScene instanceof CommonInterstitialOutScene) {
                ((CommonInterstitialOutScene) adScene).onRawBannerClose();
            }
        } catch (Exception ignore) {
        }
    }

    @Override
    public void close() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
