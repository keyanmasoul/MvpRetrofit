package corall.ad.ui.card;

import android.content.Context;

import androidx.annotation.NonNull;

import corall.ad.R;

public class CommonInnerCardView extends CommonCardView {

    public CommonInnerCardView(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ad_common_inner_card_layout;
    }
}
