package corall.ad.ui.window;

import android.content.Context;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.Random;

import corall.ad.R;
import corall.ad.bean.CorAdPlace;
import corall.ad.bean.banner.RawBannerAd;
import corall.base.util.StringUtil;

/**
 * desc:
 * date: 2019/1/7
 * author: ancun
 */
public class RawBannerFullScreenAdWindow extends FullScreenWindow {

    private View mView;
    private FrameLayout container;
    private CorAdPlace mAdPlace;

    private IFullScreenADListener listener;

    public RawBannerFullScreenAdWindow(Context ctx) {
        super(ctx);
    }

    public void setFullScreenListener(IFullScreenADListener listener) {
        this.listener = listener;
    }

    public void showFullScreenBannerAd(CorAdPlace adPlace, RawBannerAd ad) {
        if (adPlace == null) {
            return;
        }

        this.mAdPlace = adPlace;
        mView = View.inflate(mCtx, R.layout.ad_activity_banner_to_interstitial, null);
        container = mView.findViewById(R.id.layout_ad_container);

        Random random = new Random();
        int recommendNum = 98 - random.nextInt(10);
        String title = recommendNum + StringUtil.decodeStringRes(mCtx, R.string.poster_ad_user_recommend);
        TextView tvTitle = mView.findViewById(R.id.tv_recommend_title);
        tvTitle.setText(title);

        try {
            if (ad != null) {
                container.addView(ad.getBannerView());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        TextView btn = mView.findViewById(R.id.tv_close);
        btn.setText(StringUtil.decodeStringRes(mCtx, R.string.poster_close));
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

    public void closeWithoutCallBack() {
        super.close();
        if (container != null) {
            container.removeAllViews();
            container = null;
        }
        mView = null;
    }
}