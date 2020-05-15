package corall.ad.ui.card;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;

import corall.ad.R;
import corall.ad.bean.CorAdPlace;
import corall.ad.bean.nativead.FbRawNativeAd;
import corall.ad.bean.nativead.RawNativeAd;

public class FbInnerCardView extends CommonCardView {

    public FbInnerCardView(@NonNull Context context) {
        super(context);
    }

    @Override
    public void bindRawAd(CorAdPlace place, RawNativeAd ad) {
        super.bindRawAd(place, ad);
        TextView socialTxt = mView.findViewById(R.id.adv_fb_social_text);
        if (socialTxt != null) {
            if (ad instanceof FbRawNativeAd) {
                socialTxt.setText(((FbRawNativeAd) ad).getAdSocialContext());
            }
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ad_fb_card_layout;
    }
}
