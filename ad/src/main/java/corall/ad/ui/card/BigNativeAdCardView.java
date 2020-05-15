package corall.ad.ui.card;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.ArrayList;

import corall.ad.R;
import corall.base.util.ScreenUtil;

/**
 * Created by ChenLi on 2017/11/28.
 */

public class BigNativeAdCardView extends CommonNativeAdCardView {

    private LinearLayout starLayout;

    private ImageView star_1;
    private ImageView star_2;
    private ImageView star_3;
    private ImageView star_4;
    private ImageView star_5;

    public BigNativeAdCardView(@NonNull Context context) {
        super(context);
    }

    @Override
    public void initView() {
        super.initView();

//        starLayout = (LinearLayout) mView.findViewById(R.id.adv_sdk_star_layout);
//        star_1 = (ImageView) mView.findViewById(R.id.adv_sdk_star_1);
//        star_2 = (ImageView) mView.findViewById(R.id.adv_sdk_star_2);
//        star_3 = (ImageView) mView.findViewById(R.id.adv_sdk_star_3);
//        star_4 = (ImageView) mView.findViewById(R.id.adv_sdk_star_4);
//        star_5 = (ImageView) mView.findViewById(R.id.adv_sdk_star_5);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.common_ad_card_layout;
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

    @Override
    protected ArrayList<View> getActionViews(String action) {
        ArrayList<View> list = super.getActionViews(action);
        if (action.contains("title")) {
            if (starLayout != null) {
                list.add(starLayout);
            }
        }
        return list;
    }

}
