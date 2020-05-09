package corall.ad.bean.banner;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubView;

import corall.ad.AdsContants;
import corall.ad.R;
import corall.ad.bean.listener.banner.BannerLoadListener;
import corall.base.app.AMApplication;
import corall.base.util.ScreenUtil;

import static corall.ad.AdsContants.RAW_AD_STATUS_DESTROYED;

/**
 * mopub banner 广告代理类
 */
public class MpRawBannerAd extends RawBannerAd {

    private MoPubView realBannerAd;
    private String id;
    private String keyword;
    private String keywordField;
    // 广告请求监听
    private BannerLoadListener mListener;

    private View bannerView;

    private int widthDp;
    private int heightDp;

    //是否自动刷新
    private boolean isAutorefreshEnabled;

    public MpRawBannerAd(String id, String size) {
        this.id = id;
        isAutorefreshEnabled = false;
        if (!TextUtils.isEmpty(size)) {
            String[] sizes = size.split("x");
            if (sizes != null && sizes.length >= 2) {
                widthDp = Integer.parseInt(sizes[0]);
                heightDp = Integer.parseInt(sizes[1]);
            }
        }
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setKeywordField(String keywordField) {
        this.keywordField = keywordField;
    }

    @Override
    public String getPlatform() {
        return AdsContants.AD_PLATFORM_MOPUB;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setAdListener(BannerLoadListener listener) {

        mListener = listener;

    }

    @Override
    public View getBannerView() {
        return (View) bannerView;
    }

    @Override
    public void load() {
        super.load();
        try {
            initRealBannerAd();
            realBannerAd.loadAd();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean isLoading() {
        return false;
    }

    @Override
    public void destroy() {
        if (realBannerAd == null) {
            return;
        }

        realBannerAd.destroy();
        realBannerAd = null;
        bannerView = null;
        mListener = null;

        adStatus = RAW_AD_STATUS_DESTROYED;
    }

    public void setAutorefreshEnabled(boolean isAutorefreshEnabled) {
        this.isAutorefreshEnabled = isAutorefreshEnabled;
        if (realBannerAd == null) {
            return;
        }
        realBannerAd.setAutorefreshEnabled(isAutorefreshEnabled);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }


    private void initRealBannerAd() throws Exception {
        if (realBannerAd == null) {
            bannerView = LayoutInflater.from(AMApplication.getInstance()).inflate(R.layout.mp_banner_layout, null);

            realBannerAd = bannerView.findViewById(R.id.ad_mopub_banner_view);
            setAutorefreshEnabled(isAutorefreshEnabled);

            if (widthDp > 0 && heightDp > 0) {
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) ((View) realBannerAd).getLayoutParams();
                params.width = ScreenUtil.dp2px(AMApplication.getInstance(), widthDp);
                params.height = ScreenUtil.dp2px(AMApplication.getInstance(), heightDp);
            }

            realBannerAd.setAdUnitId(id);

            if (!TextUtils.isEmpty(keyword)) {
                realBannerAd.setKeywords(keyword);
            }

            if (!TextUtils.isEmpty(keywordField)) {
                realBannerAd.setUserDataKeywords(keywordField);
            }

            realBannerAd.setBannerAdListener(new MoPubView.BannerAdListener() {
                @Override
                public void onBannerLoaded(MoPubView moPubView) {
                    adStatus = AdsContants.RAW_AD_STATUS_LOADED;
                    if (mListener != null) {
                        mListener.onAdLoaded(MpRawBannerAd.this);
                    }
                }

                @Override
                public void onBannerFailed(MoPubView moPubView, MoPubErrorCode moPubErrorCode) {
                    if (mListener != null) {
                        mListener.onError(MpRawBannerAd.this, moPubErrorCode.getIntCode(), moPubErrorCode.toString());
                    }
                }

                @Override
                public void onBannerClicked(MoPubView moPubView) {
                    if (mListener != null) {
                        mListener.onClick(MpRawBannerAd.this);
                    }
                }

                @Override
                public void onBannerExpanded(MoPubView moPubView) {

                }

                @Override
                public void onBannerCollapsed(MoPubView moPubView) {

                }
            });
        }
    }

}
