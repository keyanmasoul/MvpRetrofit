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

public class SmallNativeAdCardView extends CommonNativeAdCardView {
    public SmallNativeAdCardView(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.small_ad_card_layout;
    }

    @Override
    protected int getMopubVideoLayoutId() {
        return R.layout.small_mp_video_ad_card_layout;
    }

    @Override
    protected BitmapTransformation getIconBitmapTransform() {
        return new RoundedCorners(ScreenUtil.dp2px(getContext(), 2));
    }

    @Override
    protected BitmapTransformation getCoverBitmapTransform() {
        /*RoundTransform transform = new RoundTransform(mContext);
        transform.setTopLeftRound(true);
        transform.setTopRightRound(true);
        transform.setBottomLeftRound(false);
        transform.setBottomRightRound(false);*/
        return new RoundedCorners(ScreenUtil.dp2px(getContext(), 4));
    }
}
