package corall.ad.ui.card;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.formats.MediaView;

import corall.ad.bean.CorAdPlace;
import corall.ad.bean.nativead.RawNativeAd;
import corall.base.util.StringUtil;

/**
 * Created by ChenLi on 2017/11/21.
 */

public abstract class BaseNativeAdCardView extends LinearLayout {

    protected TextView title;
    protected TextView shortDesc;
    protected TextView freeBtn;
    protected ImageView iconImg;
    protected ImageView contentImg;
    protected FrameLayout mediaContainer;
    protected ImageView cornerImg;
    protected TextView cornerTxt;
    //admob 原生广告中的mediaview
    protected MediaView amMediaView;
    protected FrameLayout cornerContainer;

    protected View mView;
    protected Context mContext;

    protected FrameLayout adContainer;

    public BaseNativeAdCardView(@NonNull Context context) {
        super(context);
        mContext = context;

        adContainer = new FrameLayout(mContext);
        this.addView(adContainer, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

    }

    public abstract void initView();

    public abstract void bindRawAd(CorAdPlace adPlace, RawNativeAd ad);

    public View getTitleView() {
        return title;
    }

    public View getShortDesc() {
        return shortDesc;
    }

    public View getActionBtn() {
        return freeBtn;
    }

    public View getIconView() {
        return iconImg;
    }

    public View getContentImg() {
        return contentImg;
    }

    public View getAmMediaView() {
        return amMediaView;
    }

    public View getMainView() {
        return mView;
    }

    protected String getString(int res) {
        return StringUtil.decodeStringRes(mContext, res);
    }

}
