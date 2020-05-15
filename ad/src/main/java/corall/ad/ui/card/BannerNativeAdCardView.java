package corall.ad.ui.card;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import corall.ad.R;
import corall.base.util.ScreenUtil;

/**
 * Created by ChenLi on 2017/11/28.
 */

public class BannerNativeAdCardView extends CommonNativeAdCardView {

    public BannerNativeAdCardView(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.banner_ad_card_layout;
    }

    @Override
    protected BitmapTransformation getIconBitmapTransform() {
        return new RoundedCorners(ScreenUtil.dp2px(getContext(), 2));
    }
}
