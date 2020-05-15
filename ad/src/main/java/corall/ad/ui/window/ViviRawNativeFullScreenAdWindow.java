//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package corall.ad.ui.window;

import android.content.Context;
import android.view.View;

import corall.ad.bean.CorAdPlace;
import corall.ad.bean.nativead.RawNativeAd;
import corall.ad.ui.card.AdCardEventListener;
import corall.ad.ui.card.CommonNativeAdCardView;
import corall.ad.ui.card.ViViFullScreenNativeCard;

public class ViviRawNativeFullScreenAdWindow extends FullScreenWindow {
    private CorAdPlace adPlace;
    private IFullScreenADListener listener;
    private View fullScreenView;
    private int style = 0;

    public ViviRawNativeFullScreenAdWindow(Context ctx) {
        super(ctx);
    }

    public ViviRawNativeFullScreenAdWindow(Context ctx, int style) {
        super(ctx);
        this.style = style;
    }

    public void setFullScreenListener(IFullScreenADListener listener) {
        this.listener = listener;
    }

    public void showFullScreenNativeAd(CorAdPlace adPlace, RawNativeAd ad) {
        this.adPlace = adPlace;
        CommonNativeAdCardView mCardView = null;
        mCardView = new ViViFullScreenNativeCard(this.mCtx);

            mCardView.bindRawAd(adPlace, ad);


        mCardView.setEventListener(new AdCardEventListener() {
            public void onAdClickClose(CorAdPlace adPlace) {
                ViviRawNativeFullScreenAdWindow.this.close();
            }

            public void onAdCardShow(CorAdPlace adPlace) {
                if (ViviRawNativeFullScreenAdWindow.this.listener != null && adPlace != null) {
                    ViviRawNativeFullScreenAdWindow.this.listener.onADShow(adPlace);
                }

            }

            public void onTimeCount(CorAdPlace adPlace, int timecount) {
                if (timecount <= 0) {
                }

            }
        });
        this.fullScreenView = mCardView;
        this.showWindowManager(mCardView);
    }


    public void close() {
        super.close();
        if (this.listener != null && this.adPlace != null) {
            this.listener.onADClose(this.adPlace);
        }

//        try {
//            if (this.adPlace instanceof HmNativeAdPlace) {
//                ((HmNativeAdPlace) this.adPlace).destroy();
//            } else if (this.adPlace instanceof HmBannerAdPlace) {
//                ((HmBannerAdPlace) this.adPlace).destroy();
//            }
//        } catch (Exception var2) {
//            var2.printStackTrace();
//        }

    }
}
