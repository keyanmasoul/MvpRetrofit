package corall.ad.ui.card;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import corall.ad.R;
import corall.ad.bean.CorAdPlace;
import corall.ad.bean.nativead.FbRawNativeAd;
import corall.ad.bean.nativead.RawNativeAd;
import corall.base.util.StringUtil;

public class ViViFullScreenNativeCard extends FullScreenNativeAdCardView {

    public ViViFullScreenNativeCard(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ad_common_native_fullscreen_card;
    }

    @Override
    public void bindRawAd(CorAdPlace place, RawNativeAd ad) {
        super.bindRawAd(place, ad);
        View closeBtn = mView.findViewById(R.id.adv_sdk_close_btn2);
        if (closeBtn != null) {
            ((TextView) closeBtn).setText(StringUtil.decodeStringRes(mContext, R.string.poster_close));
            closeBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ViViFullScreenNativeCard.this.mEventListener != null) {
                        ViViFullScreenNativeCard.this.mEventListener.onAdClickClose(ViViFullScreenNativeCard.this.mAdPlace);
                    }
                }
            });
        }

        TextView socialTxt = mView.findViewById(R.id.adv_fb_social_text);
        if (socialTxt != null) {
            if (ad instanceof FbRawNativeAd) {
                socialTxt.setText(((FbRawNativeAd) ad).getAdSocialContext());
                socialTxt.setVisibility(View.VISIBLE);
            } else {
                socialTxt.setVisibility(View.GONE);
            }
        }
    }
}
