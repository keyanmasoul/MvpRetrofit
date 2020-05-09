package corall.ad.web;

import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.URISyntaxException;

public class AdWebViewClient extends WebViewClient {

    public IWebViewController webController;

    public AdWebViewClient(IWebViewController controller) {
        webController = controller;
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
        } catch (Exception se) {
            se.printStackTrace();
        }
        return false;
    }


}
