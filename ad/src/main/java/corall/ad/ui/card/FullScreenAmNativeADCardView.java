package corall.ad.ui.card;

import android.content.Context;

import androidx.annotation.NonNull;

import corall.ad.R;

/**
 * Created by ChenLi on 2018/3/19.
 */

public class FullScreenAmNativeADCardView extends FullScreenNativeAdCardView {
    public FullScreenAmNativeADCardView(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.full_screen_am_ad_card_layout;
    }
}
