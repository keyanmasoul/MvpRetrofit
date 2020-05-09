package corall.ad.web;

import android.content.Intent;
import android.net.Uri;
import android.os.Message;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class AdWebChromeClient extends WebChromeClient {

    private long lastNewTab;
    private String lastUrl;
    public IWebViewController webController;
    private boolean isOut;
    private String action;

    public AdWebChromeClient() {

    }

    public AdWebChromeClient(IWebViewController controller) {
        webController = controller;
        isOut = false;
        lastNewTab = 0;
    }

    public void setJumpOut(boolean out) {
        isOut = out;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public boolean onCreateWindow(final WebView parentWebView, boolean isDialog, boolean isUserGesture, Message resultMsg) {
        WebView newWebView = new WebView(parentWebView.getContext());
        WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
        transport.setWebView(newWebView);
        resultMsg.sendToTarget();

        newWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                if (System.currentTimeMillis() - lastNewTab < 3000) {
                    return true;
                }
                if (System.currentTimeMillis() - lastNewTab < 5000 && url.equals(lastUrl)) {
                    return true;
                }
                lastNewTab = System.currentTimeMillis();
                lastUrl = url;
                if (isOut) {
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    try {
                        parentWebView.getContext().startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Intent intent = new Intent(action);
                    intent.putExtra("direct_url", url);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    parentWebView.getContext().startActivity(intent);
                }
                if (webController != null) {
                    webController.onWebJump();
                }
                webView.destroy();
                return true;
            }
        });

        return true;
    }
}
