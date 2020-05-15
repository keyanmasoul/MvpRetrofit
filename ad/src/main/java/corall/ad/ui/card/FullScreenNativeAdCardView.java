package corall.ad.ui.card;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;


import corall.ad.R;
import corall.ad.bean.CorAdPlace;
import corall.ad.bean.nativead.RawNativeAd;
import corall.base.util.ScreenUtil;

/**
 * Created by ChenLi on 2017/11/21.
 */

public class FullScreenNativeAdCardView extends CommonNativeAdCardView {

    private int mAdCardHeight;

    private int mAdCardWidth;

    private ImageView closeBtn;

    public FullScreenNativeAdCardView(@NonNull Context context) {
        super(context);

    }

    @Override
    public void initView() {

        super.initView();

        closeBtn = (ImageView) (mView.findViewById(R.id.adv_sdk_close_btn));

        if (closeBtn != null) {
            closeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mEventListener != null) {
                        mEventListener.onAdClickClose(mAdPlace);
                    }
                }
            });
        }


        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        int windowWidth = dm.widthPixels;

        mAdCardWidth = windowWidth;
        mAdCardHeight = (int) (mAdCardWidth / 1.9f);

        if (contentImg != null) {
            android.view.ViewGroup.LayoutParams lp = contentImg.getLayoutParams();
            lp.width = mAdCardWidth;
            lp.height = mAdCardHeight;
            contentImg.setLayoutParams(lp);
        }

        if (amMediaView != null) {
            android.view.ViewGroup.LayoutParams mediaLp = amMediaView.getLayoutParams();
            mediaLp.width = mAdCardWidth;
            mediaLp.height = mAdCardHeight;
            amMediaView.setLayoutParams(mediaLp);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.full_screen_ad_card_layout;
    }

    @Override
    protected int getMopubVideoLayoutId() {
        return R.layout.full_screen_mp_video_ad_card_layout;
    }


    @Override
    public void bindRawAd(CorAdPlace place, RawNativeAd ad){
        super.bindRawAd(place, ad);
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
        transform.setBottomRightRound(false);
        transform.setRadius(ScreenUtil.dp2px(mContext, 8));
        transform.setHeight(ScreenUtil.getScreenWidth(mContext) / 1.9f);
        transform.setWidth(ScreenUtil.getScreenWidth(mContext));*/
        return new RoundedCorners(ScreenUtil.dp2px(getContext(), 4));
    }
}
