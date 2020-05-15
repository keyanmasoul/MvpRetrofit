package corall.ad.ui.window;

import android.content.Context;
import android.view.View;

import corall.ad.AdUtils;
import corall.ad.bean.CorAdPlace;
import corall.ad.bean.nativead.RawNativeAd;
import corall.ad.ui.card.AdCardEventListener;
import corall.ad.ui.card.CommonNativeAdCardView;
import corall.ad.ui.card.FullScreenAmNativeADCardView;
import corall.ad.ui.card.FullScreenNativeAdCardView;
import corall.ad.ui.card.FullScreenNativeAdTimeCardView;

/**
 * Created by ChenLi on 2017/12/14.
 */

/**
 * 应用外全屏广告类，目前只支持native类型广告和非admob插屏类型广告
 * (native类型)使用windowsmanager，点击home键，回退键，锁屏时关闭
 * (非admob插屏类型)，直接调用其插屏广告类型
 */
public class RawNativeFullScreenAdWindow extends FullScreenWindow {

    private CorAdPlace adPlace;
    private IFullScreenADListener listener;

    private View fullScreenView;
    private int style = 0;


    public RawNativeFullScreenAdWindow(Context ctx) {
        super(ctx);
    }


    public RawNativeFullScreenAdWindow(Context ctx, int style) {
        super(ctx);
        this.style = style;
    }


    public void setFullScreenListener(IFullScreenADListener listener) {
        this.listener = listener;
    }


    public void showFullScreenNativeAd(CorAdPlace adPlace, RawNativeAd ad) {
        this.adPlace = adPlace;
        CommonNativeAdCardView mCardView = null;
        if (AdUtils.isAdmob(ad)) {
            mCardView = new FullScreenAmNativeADCardView(mCtx);
        } else {
            if (style == 1) {
                mCardView = new FullScreenNativeAdTimeCardView(mCtx);
            } else {
                mCardView = new FullScreenNativeAdCardView(mCtx);
            }
        }


            mCardView.bindRawAd(adPlace, ad);


        mCardView.setEventListener(new AdCardEventListener() {
            @Override
            public void onAdClickClose(CorAdPlace adPlace) {
                close();
            }

            @Override
            public void onAdCardShow(CorAdPlace adPlace) {
                if (listener != null && adPlace != null) {
                    listener.onADShow(adPlace);
                }
            }

            @Override
            public void onTimeCount(CorAdPlace adPlace, int timecount) {
                if (timecount <= 0) {
//                    close();
                }
            }
        });


        fullScreenView = mCardView;
        showWindowManager(mCardView);

    }

    @Override
    public void close() {
        super.close();
        if (listener != null && adPlace != null) {
            listener.onADClose(adPlace);
        }
    }


}
