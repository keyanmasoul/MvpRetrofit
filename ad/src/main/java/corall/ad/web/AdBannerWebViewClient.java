package corall.ad.web;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.URISyntaxException;

public class AdBannerWebViewClient extends WebViewClient {

    public IWebViewController webController;
    private boolean isOut;
    private String action;
    private long lastNewTab;
    private String lastUrl;

    public AdBannerWebViewClient(IWebViewController controller) {
        webController = controller;
//        isOut = false;
//        lastNewTab = 0;
    }

    public void setJumpOut(boolean out) {
        isOut = out;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        if (!TextUtils.isEmpty(action) || isOut) {
//            view.loadUrl("javascript: var allLinks = document.getElementsByTagName('a'); if (allLinks) {var i;for (i=0; i<allLinks.length; i++) {var link = allLinks[i];var target = link.getAttribute('target'); if (target && target == '_blank') {link.href = 'newtab:'+link.href;link.setAttribute('target','_self');}}}");
        }
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {

        if (url.startsWith("http")) {
            return false;
        }
        Intent intent = null;
        try {
            if (url.startsWith("android-app://")) {
                intent = Intent.parseUri(url, Intent.URI_ANDROID_APP_SCHEME);
            } else if (url.startsWith("intent://")) {
                intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
            } else {
                Uri uri = Uri.parse(url);
                intent = new Intent(Intent.ACTION_VIEW, uri);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (URISyntaxException ex) {
            return false;
        }
        if (view.getContext().getPackageManager().resolveActivity(intent, 0) == null) {
            return true;
        }
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        if (!url.startsWith("android-app://")) {
            intent.setComponent(null);
        }
        try {
            view.getContext().startActivity(intent);
            if (webController != null) {
                webController.onWebJump();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


}
