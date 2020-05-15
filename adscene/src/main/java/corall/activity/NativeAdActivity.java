package corall.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.example.adscene.R;
import com.uber.adsbuisiness.bean.HmAdPlace;
import com.uber.adsbuisiness.bean.HmAdUnionPlace;
import com.uber.adsbuisiness.bean.RawAd;
import com.uber.adsbuisiness.bean.nativead.RawNativeAd;
import com.uber.adsbuisiness.service.AdActionException;
import com.uber.adsui.card.AdCardEventListener;
import com.uber.adsui.card.CommonNativeAdCardView;

import corall.ad.bean.CorAdPlace;
import corall.ad.bean.CorAdUnionPlace;
import corall.ad.bean.RawAd;
import corall.ad.bean.nativead.RawNativeAd;
import corall.ad.ui.card.AdCardEventListener;
import corall.ad.ui.card.CommonNativeAdCardView;
import corall.ad.ui.card.ViViFullScreenNativeCard;
import corall.adscene.EntranceType;
import corall.adscene.scene.AdStrategyScene;
import corall.adscene.scene.inner.CommonInterstitialScene;
import corall.adscene.scene.outer.CommonInterstitialOutScene;
import cr.s.a.ClassUtils;
import cr.s.a.EntranceType;
import cr.s.a.R;
import cr.s.a.adscene.AdStrategyScene;
import cr.s.a.adscene.inner.CommonInterstitialScene;
import cr.s.a.adscene.outer.CommonInterstitialOutScene;
import cr.s.a.ui.crad.view.ViViFullScreenNativeCard;

/**
 * desc: 模拟 插屏广告（将native广告以插屏的形式展示）
 * date: 2018/11/12
 * author: ancun
 */
public class NativeAdActivity extends InterstitialActivity {

    public static void showInterstitialAd(Context context, String entranceType) {
        Intent intent = new Intent(context, ClassUtils.getNativeAdActivity());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(ENTRANCE_TYPE, entranceType);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_activity_native_to_interstitial);

        initHomeKeyReceiver();
        initAdView(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initAdView(intent);
    }

    private void initAdView(Intent intent) {
        if (intent == null) {
            close();
            return;
        }

        mEntranceType = EntranceType.enumOf(intent.getStringExtra(ENTRANCE_TYPE));
        if (mEntranceType == null) {
            close();
            return;
        }

        CorAdUnionPlace adUnionPlace = mEntranceType.getAdScene().getHmAdUnionPlace();
        final RawAd rawAd = ((AdStrategyScene) mEntranceType.getAdScene()).getActiveAd();

        if (!(rawAd instanceof RawNativeAd)) {
            close();
            return;
        }

        final RawNativeAd activeAd = (RawNativeAd) rawAd;

        CommonNativeAdCardView cardView = new ViViFullScreenNativeCard(this);
            cardView.bindRawAd(adUnionPlace, activeAd);

        final AdStrategyScene adScene = (AdStrategyScene) mEntranceType.getAdScene();

        cardView.setEventListener(new AdCardEventListener() {
            @Override
            public void onAdClickClose(CorAdPlace adPlace) {
                close();
            }

            @Override
            public void onAdCardShow(CorAdPlace adPlace) {

                if (adScene instanceof CommonInterstitialScene) {
                    ((CommonInterstitialScene) adScene).onRawNativeShow();
                } else if (adScene instanceof CommonInterstitialOutScene) {
                    ((CommonInterstitialOutScene) adScene).onRawNativeShow();
                }

            }

            @Override
            public void onTimeCount(CorAdPlace adPlace, int timecount) {
                if (timecount <= 0) {
                    //close();
                }
            }
        });

        //cardView.setFocusableInTouchMode(true);
        cardView.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                NativeAdActivity.this.close();
                return true;
            }
        });

        try {
            ViewGroup adContainer = findViewById(R.id.ad_native_container);
            if (adContainer.getChildCount() > 0) {
                adContainer.removeAllViews();
            }

            ViewParent viewParent = cardView.getParent();
            if (viewParent instanceof ViewGroup) {
                ((ViewGroup) viewParent).removeView(cardView);
            }
            adContainer.addView(cardView);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            final AdStrategyScene adScene = (AdStrategyScene) mEntranceType.getAdScene();
            if (adScene instanceof CommonInterstitialScene) {
                ((CommonInterstitialScene) adScene).onRawNativeClose();
            } else if (adScene instanceof CommonInterstitialOutScene) {
                ((CommonInterstitialOutScene) adScene).onRawNativeClose();
            }
        } catch (Exception ignore) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
