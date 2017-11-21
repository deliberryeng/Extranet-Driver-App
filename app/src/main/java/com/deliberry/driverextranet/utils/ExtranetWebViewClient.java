package com.deliberry.driverextranet.utils;

import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ExtranetWebViewClient extends WebViewClient {

    @Override
    public void onPageFinished(WebView view, String url) {
        // TODO Auto-generated method stub
        super.onPageFinished(view, url);
    }

    @Override
    public boolean shouldOverrideKeyEvent (WebView view, KeyEvent event) {
        // Do something with the event here
        return true;
    }

    @Override
    public boolean shouldOverrideUrlLoading (WebView view, String url) {
        view.loadUrl(url);
        return false;
    }
}