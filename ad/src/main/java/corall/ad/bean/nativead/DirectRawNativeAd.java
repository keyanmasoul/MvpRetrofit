package corall.ad.bean.nativead;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;

import java.util.List;

import corall.ad.AdsContants;
import corall.ad.bean.listener.nativead.NativeAdLoadListener;
import corall.base.app.CorApplication;

public class DirectRawNativeAd extends RawNativeAd implements View.OnClickListener {

    String id;
    String title;
    String iconUrl;
    String imageUrl;
    String desc;
    String callAction;
    String url;
    String pkgName;
    boolean isJumpOut;
    String action;

    private NativeAdLoadListener mListener;

    public DirectRawNativeAd(String id) {
        this.id = id;
        isJumpOut = false;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setCallAction(String callAction) {
        this.callAction = callAction;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public void setJumpOut(boolean jumpOut) {
        this.isJumpOut = jumpOut;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public String getPlatform() {
        return AdsContants.AD_PLATFORM_DIRECT;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getIconUrl() {
        return iconUrl;
    }

    @Override
    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    @Override
    public int getRawChannelType() {
        return 0;
    }

    @Override
    public String getCallToAction() {
        return callAction;
    }

    @Override
    public float getRating() {
        return 0;
    }

    @Override
    public void destroy() {

    }

    @Override
    public String getSource() {
        return null;
    }

    @Override
    public void setAdListener(NativeAdLoadListener listener) {
        mListener = listener;
    }

    @Override
    public void registerViewForInteraction(View view) {
        view.setOnClickListener(this);
    }

    @Override
    public void registerViewForInteraction(View view, List<View> viewList) {
        for (View v : viewList) {
            v.setOnClickListener(this);
        }
    }

    @Override
    public void unregisterView() {

    }

    @Override
    public boolean isLoaded() {
        return false;
    }

    @Override
    public RawNativeAd getCachedAd() {
        return null;
    }

    @Override
    public void clearCache() {

    }

    @Override
    public View getAdChoiceView(Context ctx) {
        return null;
    }

    public void logImpression() {
        if (mListener != null) {
            mListener.onLoggingImpression(this);
        }
    }

    @Override
    public void load() {
        super.load();
        if (mListener != null) {
            mListener.onAdLoaded(this);
        }
    }

    public void open() {
        Context ctx = CorApplication.getInstance();
        //如果有包名，判断该包名是否安装，如果已安装打开应用，如果没安装跳转gp
        if (!TextUtils.isEmpty(pkgName)) {
            PackageInfo packageInfo = null;
            try {
                packageInfo = ctx.getPackageManager().getPackageInfo(pkgName, 0);
            } catch (PackageManager.NameNotFoundException ignored) {
            }

            if (packageInfo != null) {//已安装
                Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
                resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                resolveIntent.setPackage(pkgName);
                List<ResolveInfo> apps = ctx.getPackageManager().queryIntentActivities(resolveIntent, 0);
                ResolveInfo ri = apps.iterator().next();
                if (ri != null) {
                    String packageName = ri.activityInfo.packageName;
                    String className = ri.activityInfo.name;
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ComponentName cn = new ComponentName(packageName, className);
                    intent.setComponent(cn);
                    ctx.startActivity(intent);
                }
            } else {
                try {
                    String str = "market://details?id=" + pkgName;
                    Intent localIntent = new Intent(Intent.ACTION_VIEW);
                    localIntent.setData(Uri.parse(str));
                    localIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ctx.startActivity(localIntent);
                } catch (Exception e) { // 打开应用商店失败 可能是没有手机没有安装应用市场

                }
            }
            if (mListener != null) {
                mListener.onClick(this);
            }
            return;
        }

        if (!TextUtils.isEmpty(url)) {
            if (isJumpOut) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                try {
                    ctx.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (!TextUtils.isEmpty(action)) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(action);
                intent.putExtra("direct_url", url);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    ctx.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (mListener != null) {
                mListener.onClick(this);
            }
        }


    }

    @Override
    public void onClick(View view) {
        open();
    }
}
