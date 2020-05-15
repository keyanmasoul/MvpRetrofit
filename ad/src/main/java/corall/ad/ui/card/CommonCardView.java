package corall.ad.ui.card;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.FitCenter;

import corall.ad.R;

/**
 * desc:
 * date: 2018/1/5
 * author: ancun
 */

public class CommonCardView extends SmallNativeAdCardView {

    public CommonCardView(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ad_common_card_layout;
    }

    @Override
    protected BitmapTransformation getCoverBitmapTransform() {
        return new FitCenter();
    }

    @Override
    protected BitmapTransformation getIconBitmapTransform() {
        return new CenterCrop();
    }
}
