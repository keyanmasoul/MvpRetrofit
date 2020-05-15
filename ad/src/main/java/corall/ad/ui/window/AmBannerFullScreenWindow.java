package corall.ad.ui.window;

import android.content.Context;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import corall.ad.R;
import corall.ad.bean.CorAdPlace;
import corall.ad.bean.banner.AMRawBannerAd;

/**
 * Created by ChenLi on 2018/1/5.
 */

public class AmBannerFullScreenWindow extends FullScreenWindow {

    private View mView;
    private FrameLayout container;

    private IFullScreenADListener listener;

    private CorAdPlace mAdPlace;


    public AmBannerFullScreenWindow(Context ctx) {
        super(ctx);

    }

    public void setFullScreenListener(IFullScreenADListener listener) {
        this.listener = listener;
    }

    public void showAmBanner(CorAdPlace adPlace, AMRawBannerAd ad) {
        if (adPlace == null) {
            return;
        }
        this.mAdPlace = adPlace;
        mView = View.inflate(mCtx, R.layout.admob_fullscreen_ad_container, null);
        container = (FrameLayout) mView.findViewById(R.id.am_ad_container);
        try {
            if (ad != null) {
                container.addView(ad.getBannerView());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        View btn = mView.findViewById(R.id.adv_sdk_close_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });

        mView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mView.getViewTreeObserver().removeOnPreDrawListener(this);
                if (listener != null) {
                    listener.onADShow(mAdPlace);

                }
                return false;
            }
        });
        showWindowManager(mView);
    }

    @Override
    public void close() {
        super.close();
        if (listener != null) {
            listener.onADClose(mAdPlace);
        }
        if (container != null) {
            container.removeAllViews();
            container = null;
        }
        mView = null;
    }
}
