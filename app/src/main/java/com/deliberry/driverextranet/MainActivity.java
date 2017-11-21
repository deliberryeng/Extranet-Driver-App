package com.deliberry.driverextranet;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.deliberry.driverextranet.utils.Consts;
import com.deliberry.driverextranet.utils.ExtranetWebViewClient;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.web_view) WebView web_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        Bundle args = getIntent().getExtras();

        if (args != null) {
            WebSettings webSettings = web_view.getSettings();

            webSettings.setJavaScriptEnabled(true);

            if(Build.VERSION.SDK_INT >= 21){
                webSettings.setMixedContentMode(0);
                web_view.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            }else if(Build.VERSION.SDK_INT >= 19){
                web_view.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            }else if(Build.VERSION.SDK_INT < 19){
                web_view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }

            web_view.setWebViewClient(new ExtranetWebViewClient());

            web_view.loadUrl(
                Consts.EXTRANET_WEBVIEW_URL
                + args.getInt("shopperUserId", 0)
                + Consts.EXTRANET_WEBVIEW_URL_SUFIX
            );
        }
    }
}
