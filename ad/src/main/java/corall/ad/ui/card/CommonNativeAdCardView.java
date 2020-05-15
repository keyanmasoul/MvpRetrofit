package corall.ad.ui.card;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.facebook.ads.NativeAdLayout;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import corall.ad.AdUtils;
import corall.ad.AdsContants;
import corall.ad.R;
import corall.ad.bean.CorAdPlace;
import corall.ad.bean.nativead.AmRawNativeAd;
import corall.ad.bean.nativead.FbRawNativeAd;
import corall.ad.bean.nativead.MpRawNativeAd;
import corall.ad.bean.nativead.RawNativeAd;
import corall.base.app.CorApplication;

import static android.view.View.inflate;

/**
 * Created by ChenLi on 2017/11/21.
 */

public abstract class CommonNativeAdCardView extends BaseNativeAdCardView {

    protected CorAdPlace mAdPlace;

    protected RawNativeAd nativeAd;

    protected AdCardEventListener mEventListener;

    protected View realAdView;

    protected View clickMask;

    protected boolean isShowed;

    protected boolean isAutoRecordShow;


    public CommonNativeAdCardView(@NonNull Context context) {
        super(context);
        isShowed = false;
        isAutoRecordShow = true;
    }

    public void setAutoRecordShow(boolean isAuto) {
        isAutoRecordShow = isAuto;
    }

    public void setEventListener(AdCardEventListener listener) {
        mEventListener = listener;
    }


    public CorAdPlace getAdPlace() {
        return mAdPlace;
    }

    @Override
    public void initView() {
        if (nativeAd == null || !nativeAd.getPlatform().equals(AdsContants.AD_PLATFORM_MOPUB)) {
            int layoutid = getLayoutId();
            if (layoutid == 0) {
                layoutid = R.layout.common_ad_card_layout;
            }
            mView = inflate(mContext, layoutid, null);
            title = (TextView) mView.findViewById(R.id.adv_sdk_title);
            shortDesc = (TextView) mView.findViewById(R.id.adv_sdk_desc);
            iconImg = (ImageView) mView.findViewById(R.id.adv_sdk_install_icon);
            freeBtn = (TextView) mView.findViewById(R.id.adv_sdk_install_dl);
            contentImg = (ImageView) mView.findViewById(R.id.adv_sdk_install_image);
            mediaContainer = (FrameLayout) mView.findViewById(R.id.adv_sdk_media_container);

            cornerImg = (ImageView) mView.findViewById(R.id.adv_sdk_corner_img);
            cornerTxt = (TextView) mView.findViewById(R.id.adv_sdk_corner_text);
            cornerContainer = (FrameLayout) mView.findViewById(R.id.adv_sdk_corner_container);
            amMediaView = mView.findViewById(R.id.adv_sdk_am_media);
        } else {
            MpRawNativeAd mpRawNativeAd = (MpRawNativeAd) nativeAd;
            mView = mpRawNativeAd.createView();
        }

        if (isAutoRecordShow) {
            mView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    mView.getViewTreeObserver().removeOnPreDrawListener(this);
                    onShow();
                    return true;
                }
            });
        }

    }

    public void notifyShowed() {
        onShow();
    }

    protected void onShow() {
        //只监听第一次展示
        if (!isShowed) {
            if (mEventListener != null) {
                mEventListener.onAdCardShow(mAdPlace);
            }
            if (mAdPlace != null && mAdPlace.getFrozenTime() > 0.1f) {
//            Log.i("bindAd", "click_wait");
                Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
//                    Log.i("bindAd", "click_actived");
                        unfreeze();
                    }
                };
                handler.sendEmptyMessageDelayed(0, (long) (mAdPlace.getFrozenTime() * 1000));
                freeze();

            }
            isShowed = true;

        }
    }

    protected void onTimeCount(int timecount) {
        if (mEventListener != null) {
            mEventListener.onTimeCount(mAdPlace, timecount);
        }
    }

    protected abstract int getLayoutId();

    protected int getMopubVideoLayoutId() {
        return R.layout.common_mp_video_ad_card_layout;
    }

    protected BitmapTransformation getIconBitmapTransform() {
        return null;
    }

    protected BitmapTransformation getCoverBitmapTransform() {
        return null;
    }

    protected ArrayList<View> getActionViews(String action) {
        ArrayList<View> list = new ArrayList<>();
        if (action.contains("image") || TextUtils.isEmpty(action)) {

            if (mediaContainer != null) {
                list.add(mediaContainer);
            } else if (contentImg != null) {
                list.add(contentImg);
            }
        }

        if (action.contains("icon") || TextUtils.isEmpty(action)) {
            if (iconImg != null) {
                list.add(iconImg);
            }
        }

        if (action.contains("button") || TextUtils.isEmpty(action)) {
            if (freeBtn != null) {
                list.add(freeBtn);
            }
        }

        if (action.contains("title") || TextUtils.isEmpty(action)) {
            if (title != null) {
                list.add(title);
            }
            if (shortDesc != null) {
                list.add(shortDesc);
            }
        }
        return list;
    }

    protected void registerViewForInteraction(CorAdPlace ad, RawNativeAd nativeAd) {


        try {
//            if (nativeAd.getPlatform().equals(AdsContants.AD_PLATFORM_DAP) && nativeAd.getRawChannelType() == 4) {
//                nativeAd.registerViewForInteraction(realAdView);
//            } else if (nativeAd.getPlatform().equals(AdsContants.AD_PLATFORM_DAP) && nativeAd.getRawChannelType() == 5) {
//                nativeAd.registerViewForInteraction(realAdView);
//            } else
            if (nativeAd.getPlatform().equals(AdsContants.AD_PLATFORM_ADMOB)) {
                AmRawNativeAd amRawNativeAd = (AmRawNativeAd) nativeAd;
                if (realAdView != null) {
                    amRawNativeAd.registerViewForInteraction(realAdView);
                }

//            } else if (nativeAd.getPlatform().equals(AdsContants.AD_PLATFORM_ADMOB_NEW)) {
//                if (nativeAd instanceof AmUniRawNativeAd) {
//                    AmUniRawNativeAd uniRawNativeAd = (AmUniRawNativeAd) nativeAd;
//                    if (realAdView != null) {
//                        uniRawNativeAd.registerViewForInteraction(realAdView);
//                    }
//                }
            } else {
                ArrayList<View> list = getActionViews(ad.getActions().toLowerCase());
                nativeAd.registerViewForInteraction(mView, list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void bindRawAd(CorAdPlace place, RawNativeAd ad) {
        mAdPlace = place;
        nativeAd = ad;
        initView();

        //mopub
        if (ad.getPlatform().equals(AdsContants.AD_PLATFORM_MOPUB)) {
            adContainer.addView(mView);
            View mopubContainer = mView.findViewById(R.id.mopub_container);
            MpRawNativeAd mpRawNativeAd = (MpRawNativeAd) ad;
            mpRawNativeAd.registerViewForInteraction(mopubContainer);
        } else if (ad.getPlatform().equals(AdsContants.AD_PLATFORM_FACEBOOK)) {
            if (title != null) {
                title.setText(ad.getTitle());
            }
            if (freeBtn != null) {
                freeBtn.setText(ad.getCallToAction());
            }
            if (shortDesc != null) {
                shortDesc.setText(ad.getDesc());
            }
            try {
                com.facebook.ads.MediaView mediaView = new com.facebook.ads.MediaView(mContext);
                mediaContainer.removeAllViews();
                mediaContainer.addView(mediaView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
                if (contentImg != null) {
                    contentImg.setVisibility(View.INVISIBLE);
                }

                ArrayList<View> clickViews = getActionViews(place.getActions().toLowerCase());

                NativeAdLayout adLayout = new NativeAdLayout(CorApplication.getInstance());
                ((FbRawNativeAd) ad).setNativeLayout(adLayout);
                ((FbRawNativeAd) ad).registerViewForInteraction(mView, mediaView, iconImg, clickViews);
                mView = adLayout;
                adContainer.addView(mView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (title != null) {
                title.setText(ad.getTitle());
            }
            if (freeBtn != null) {
                freeBtn.setText(ad.getCallToAction());
            }
            if (shortDesc != null) {
                shortDesc.setText(ad.getDesc());
            }
            BitmapTransformation iconTransform = getIconBitmapTransform();
            String iconUrl = ad.getIconUrl();
            String imageUrl = ad.getImageUrl();


            if (TextUtils.isEmpty(iconUrl)) {
                iconUrl = imageUrl;
            }

            if (!TextUtils.isEmpty(iconUrl)) {
                if (iconTransform != null) {
                    Glide.with(mContext).load(iconUrl).transform(getIconBitmapTransform()).placeholder(R.drawable.defualt_icon_fullscreen).error(R.drawable.defualt_icon_fullscreen)
                            .dontAnimate().diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(iconImg);
                } else {
                    Glide.with(mContext).load(iconUrl).placeholder(R.drawable.defualt_icon_fullscreen).error(R.drawable.defualt_icon_fullscreen)
                            .dontAnimate().diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(iconImg);
                }
            }
            if (!AdUtils.isAdmob(ad) && mediaContainer != null) {
                if (contentImg == null) {
                    contentImg = new ImageView(CorApplication.getInstance());
                    contentImg.setScaleType(ImageView.ScaleType.FIT_XY);
                    mediaContainer.addView(contentImg, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
                }
                BitmapTransformation coverTransform = getCoverBitmapTransform();

                if (TextUtils.isEmpty(imageUrl)) {
                    imageUrl = iconUrl;
                }
                if (!TextUtils.isEmpty(imageUrl)) {
                    if (coverTransform != null) {
                        Glide.with(mContext).load(imageUrl).transform(getCoverBitmapTransform()).dontAnimate().diskCacheStrategy(DiskCacheStrategy.RESOURCE).placeholder(R.drawable.notification_card_image_default_bg).into(contentImg);

                    } else {
                        Glide.with(mContext).load(imageUrl).dontAnimate().diskCacheStrategy(DiskCacheStrategy.RESOURCE).placeholder(R.drawable.notification_card_image_default_bg).into(contentImg);
                    }
                }

            }

            try {
//                if (ad.getPlatform().equals(AdsContants.AD_PLATFORM_DAP) && ad.getRawChannelType() == 4) {
//                    realAdView = initAdmobInstallView();
//                } else if (ad.getPlatform().equals(AdsContants.AD_PLATFORM_DAP) && ad.getRawChannelType() == 5) {
//                    realAdView = initAdmobContentView();
//                } else
                if (ad.getPlatform().equals(AdsContants.AD_PLATFORM_ADMOB)) {
                    realAdView = initUnifiedInstallView();
//                } else if (ad.getPlatform().equals(AdsContants.AD_PLATFORM_ADMOB_NEW)) {
//                    if (ad instanceof AmUniRawNativeAd) {
//                        realAdView = initUnifiedInstallView();
//                    }
                } else {
                    //不是admob广告，admob media控件不显示
                    if (amMediaView != null) {
                        amMediaView.setVisibility(View.INVISIBLE);
                    }
                    adContainer.addView(mView);
                }
//                if (place.getFrozenTime() > 0.1f) {
//                    Log.i("bindAd", "click_wait");
//                    Handler handler = new Handler() {
//                        @Override
//                        public void handleMessage(Message msg) {
//                            super.handleMessage(msg);
//                            try {
//                                Log.i("bindAd", "click_actived");
//                                registerViewForInteraction(mAdPlace, nativeAd);
//                            } catch (AdActionException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    };
//                    handler.sendEmptyMessageDelayed(0, (long) (place.getFrozenTime() * 1000));
//                } else {
                registerViewForInteraction(mAdPlace, nativeAd);
//                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if (AdUtils.isFaceBook(ad)) {
            View choiceView = ad.getAdChoiceView(mContext);
            cornerContainer.addView(choiceView);
            cornerContainer.setVisibility(View.VISIBLE);
            if (cornerImg != null) {
                cornerImg.setVisibility(View.INVISIBLE);
            }
            if (cornerTxt != null) {
                cornerTxt.setVisibility(View.VISIBLE);
            }
        } else if (AdUtils.isAdmob(ad)) {
            cornerContainer.setVisibility(View.INVISIBLE);
            if (cornerImg != null) {
                cornerImg.setVisibility(View.INVISIBLE);
            }
            if (cornerTxt != null) {
                cornerTxt.setVisibility(View.INVISIBLE);
            }
        } else if (ad.getPlatform().equals(AdsContants.AD_PLATFORM_APPNEXT)) {
            View choiceView = ad.getAdChoiceView(mContext);
            cornerContainer.addView(choiceView);
            cornerContainer.setVisibility(View.VISIBLE);
            if (cornerImg != null) {
                cornerImg.setVisibility(View.INVISIBLE);
            }
            if (cornerTxt != null) {
                cornerTxt.setVisibility(View.INVISIBLE);
            }
        } else {
            if (cornerContainer != null) {
                cornerContainer.setVisibility(View.INVISIBLE);
            }
            if (cornerTxt != null) {
                cornerTxt.setVisibility(View.INVISIBLE);
            }
            if (cornerImg != null) {
                cornerImg.setVisibility(View.VISIBLE);
            }
        }

//        if (place.getFrozenTime() > 0.1f) {
////            Log.i("bindAd", "click_wait");
//            Handler handler = new Handler() {
//                @Override
//                public void handleMessage(Message msg) {
//                    super.handleMessage(msg);
////                    Log.i("bindAd", "click_actived");
//                    unfreeze();
//                }
//            };
//            handler.sendEmptyMessageDelayed(0, (long) (place.getFrozenTime() * 1000));
//            freeze();

//        }

    }

    protected void freeze() {
        clickMask = mView.findViewById(R.id.adv_sdk_click_mask);
        if (clickMask == null) {
            clickMask = new View(mContext);
            adContainer.addView(clickMask, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 5));
            clickMask.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    if (clickMask != null && mView != null) {
                        ViewGroup.LayoutParams params = clickMask.getLayoutParams();
                        params.height = mView.getHeight();
                        clickMask.setLayoutParams(params);
                        clickMask.getViewTreeObserver().removeOnPreDrawListener(this);
                    }

                    return true;
                }
            });
        }
        clickMask.setVisibility(View.VISIBLE);
        clickMask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    protected void unfreeze() {
        if (clickMask != null) {
            clickMask.setVisibility(View.GONE);
        }
    }

//    @Override
//    public void bindAd(HmNativeAdPlace ad) throws AdActionException {
//        mAdPlace = ad;
//        nativeAd = null;
//        initView();
//        title.setText(ad.getTitle());
//        freeBtn.setText(ad.getCallToAction());
//        shortDesc.setText(ad.getDesc());
//        BitmapTransformation iconTransform = getIconBitmapTransform();
//
//        String iconUrl = ad.getIconUrl();
//        String imageUrl = ad.getImageUrl();
//
//
//        if (TextUtils.isEmpty(iconUrl)) {
//            iconUrl = imageUrl;
//        }
//
//        if (!TextUtils.isEmpty(iconUrl)) {
//            if (iconTransform != null) {
//                Glide.with(mContext).load(ad.getIconUrl()).transform(getIconBitmapTransform()).placeholder(R.drawable.defualt_icon_fullscreen).error(R.drawable.defualt_icon_fullscreen)
//                        .dontAnimate().diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(iconImg);
//            } else {
//                Glide.with(mContext).load(ad.getIconUrl()).placeholder(R.drawable.defualt_icon_fullscreen).error(R.drawable.defualt_icon_fullscreen)
//                        .dontAnimate().diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(iconImg);
//            }
//        }
//
//        if (contentImg != null) {
//            BitmapTransformation coverTransform = getCoverBitmapTransform();
//
//            if (TextUtils.isEmpty(imageUrl)) {
//                imageUrl = iconUrl;
//            }
//
//            if (!TextUtils.isEmpty(imageUrl)) {
//                if (coverTransform != null) {
//                    Glide.with(mContext).load(imageUrl).transform(getCoverBitmapTransform()).dontAnimate().diskCacheStrategy(DiskCacheStrategy.RESOURCE).placeholder(R.drawable.notification_card_image_default_bg).into(contentImg);
//
//                } else {
//                    Glide.with(mContext).load(imageUrl).dontAnimate().diskCacheStrategy(DiskCacheStrategy.RESOURCE).placeholder(R.drawable.notification_card_image_default_bg).into(contentImg);
//                }
//            }
//        }
//
//        if (ad.isFaceBook()) {
//            View choiceView = ad.getAdChoiceView(mContext);
//            cornerContainer.addView(choiceView);
//            cornerContainer.setVisibility(View.VISIBLE);
//            if (cornerImg != null) {
//                cornerImg.setVisibility(View.INVISIBLE);
//            }
//            if (cornerTxt != null) {
//                cornerTxt.setVisibility(View.VISIBLE);
//            }
//        } else if (ad.isAdmob()) {
//            cornerContainer.setVisibility(View.INVISIBLE);
//            if (cornerImg != null) {
//                cornerImg.setVisibility(View.INVISIBLE);
//            }
//            if (cornerTxt != null) {
//                cornerTxt.setVisibility(View.INVISIBLE);
//            }
//        } else if (ad.getPlatform().equals(AdsContants.AD_PLATFORM_APPNEXT)) {
//            View choiceView = ad.getAdChoiceView(mContext);
//            cornerContainer.addView(choiceView);
//            cornerContainer.setVisibility(View.VISIBLE);
//            if (cornerImg != null) {
//                cornerImg.setVisibility(View.INVISIBLE);
//            }
//            if (cornerTxt != null) {
//                cornerTxt.setVisibility(View.INVISIBLE);
//            }
//        } else {
//            cornerContainer.setVisibility(View.INVISIBLE);
//            if (cornerTxt != null) {
//                cornerTxt.setVisibility(View.INVISIBLE);
//            }
//            if (cornerImg != null) {
//                cornerImg.setVisibility(View.VISIBLE);
//            }
//        }
//        isShowed = false;
//        try {
////            if (ad.getPlatform().equals(AdsContants.AD_PLATFORM_DAP) && ad.getRawChannelType() == 4) {
////                View installView = initAdmobInstallView();
////                ad.registerViewForInteraction(installView);
////            } else if (ad.getPlatform().equals(AdsContants.AD_PLATFORM_DAP) && ad.getRawChannelType() == 5) {
////                View contentView = initAdmobContentView();
////                ad.registerViewForInteraction(contentView);
////            } else {
//            adContainer.addView(mView);
//            registerViewForInteraction(ad);
//
////            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private View initUnifiedInstallView() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        UnifiedNativeAdView unifiedView = new UnifiedNativeAdView(mContext);
        unifiedView.setHeadlineView(title);
        unifiedView.setIconView(iconImg);
        if (shortDesc != null) {
            unifiedView.setBodyView(shortDesc);
        }

        if (amMediaView != null) {
            unifiedView.setMediaView(amMediaView);
            if (contentImg != null) {
                contentImg.setVisibility(View.INVISIBLE);
            }
            amMediaView.setVisibility(View.VISIBLE);
        } else if (mediaContainer != null) {
            amMediaView = new MediaView(mContext);
            mediaContainer.addView(amMediaView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            unifiedView.setMediaView(amMediaView);
            if (contentImg != null) {
                contentImg.setVisibility(View.INVISIBLE);
            }
            amMediaView.setVisibility(View.VISIBLE);
        }

        unifiedView.setCallToActionView(freeBtn);

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);

        unifiedView.addView(mView, lp);
        adContainer.addView(unifiedView);
        return unifiedView;

    }

    public View findMediaView(Class cls) {
        for (int i = 0; i < mediaContainer.getChildCount(); i++) {
            View v = mediaContainer.getChildAt(i);
            if (cls.isInstance(v)) {
                return v;
            }
        }
        return null;
    }


//    private View initAdmobInstallView() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
//        Constructor con = Class.forName(decodeConstant(AdsContants.AM_AD_NATIVE_INSTALL_CLASS_NAME)).getConstructor(Context.class);
//        Object installView = con.newInstance(mContext);
//
//        Method setHeadLine = installView.getClass().getDeclaredMethod("setHeadlineView", View.class);
//        setHeadLine.invoke(installView, title);
//
//        Method setIconView = installView.getClass().getDeclaredMethod("setIconView", View.class);
//        setIconView.invoke(installView, iconImg);
//
//        if (shortDesc != null) {
//            Method setBodyView = installView.getClass().getDeclaredMethod("setBodyView", View.class);
//            setBodyView.invoke(installView, shortDesc);
//        }
//
//        if (amMediaView != null) {
//            Method setMediaView = installView.getClass().getDeclaredMethod("setMediaView", amMediaView.getClass());
//            setMediaView.invoke(installView, amMediaView);
//            if (contentImg != null) {
//                contentImg.setVisibility(View.INVISIBLE);
//            }
//            amMediaView.setVisibility(View.VISIBLE);
//        }
//        if (mediaContainer != null) {
//            Constructor media_con = Class.forName("com.google.android.gms.ads.formats.MediaView").getConstructor(Context.class);
//            amMediaView = (View) media_con.newInstance(AMApplication.getInstance());
//            mediaContainer.addView(amMediaView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
//
//            Method setMediaView = installView.getClass().getDeclaredMethod("setMediaView", amMediaView.getClass());
//            setMediaView.invoke(installView, amMediaView);
//            if (contentImg != null) {
//                contentImg.setVisibility(View.INVISIBLE);
//            }
//            amMediaView.setVisibility(View.VISIBLE);
//        }
//
//
//        Method setCallToActionView = installView.getClass().getDeclaredMethod("setCallToActionView", View.class);
//        setCallToActionView.invoke(installView, freeBtn);
//
//        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
//                FrameLayout.LayoutParams.WRAP_CONTENT);
//
//        Method addView = installView.getClass().getMethod("addView", View.class, ViewGroup.LayoutParams.class);
//        addView.invoke(installView, mView, lp);
//        adContainer.addView((View) installView);
//        return (View) installView;
//
//    }
//
//    private View initAdmobContentView() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
//        Constructor con = Class.forName(decodeConstant(AdsContants.AM_AD_NATIVE_CONTENT_CLASS_NAME)).getConstructor(Context.class);
//        Object contentView = con.newInstance(mContext);
//
//        Method setHeadLine = contentView.getClass().getDeclaredMethod("setHeadlineView", View.class);
//        setHeadLine.invoke(contentView, title);
//
//        Method setLogoView = contentView.getClass().getDeclaredMethod("setLogoView", View.class);
//        setLogoView.invoke(contentView, iconImg);
//
//        if (shortDesc != null) {
//            Method setBodyView = contentView.getClass().getDeclaredMethod("setBodyView", View.class);
//            setBodyView.invoke(contentView, shortDesc);
//        }
//
//        if (amMediaView != null) {
//            Method setMediaView = contentView.getClass().getDeclaredMethod("setMediaView", amMediaView.getClass());
//            setMediaView.invoke(contentView, amMediaView);
//            if (contentImg != null) {
//                contentImg.setVisibility(View.INVISIBLE);
//            }
//            amMediaView.setVisibility(View.VISIBLE);
//        }
//        if (mediaContainer != null) {
//            Constructor media_con = Class.forName("com.google.android.gms.ads.formats.MediaView").getConstructor(Context.class);
//            amMediaView = (View) media_con.newInstance(AMApplication.getInstance());
//            mediaContainer.addView(amMediaView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
//
//            Method setMediaView = contentView.getClass().getDeclaredMethod("setMediaView", amMediaView.getClass());
//            setMediaView.invoke(contentView, amMediaView);
//            if (contentImg != null) {
//                contentImg.setVisibility(View.INVISIBLE);
//            }
//            amMediaView.setVisibility(View.VISIBLE);
//        }
//
//
//        Method setCallToActionView = contentView.getClass().getDeclaredMethod("setCallToActionView", View.class);
//        setCallToActionView.invoke(contentView, freeBtn);
//
//        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
//                FrameLayout.LayoutParams.WRAP_CONTENT);
//
//        Method addView = contentView.getClass().getMethod("addView", View.class, ViewGroup.LayoutParams.class);
//        addView.invoke(contentView, mView, lp);
//
//        adContainer.addView((View) contentView);
//        return (View) contentView;
////        if (mView instanceof RelativeLayout) {
////            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
////            ((RelativeLayout) mView).addView((View) contentView, lp);
////        } else if (mView instanceof FrameLayout) {
////            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
////            ((FrameLayout) mView).addView((View) contentView, lp);
////        } else if (mView instanceof LinearLayout) {
////            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
////            ((LinearLayout) mView).addView((View) contentView, lp);
////        }
//    }
}
