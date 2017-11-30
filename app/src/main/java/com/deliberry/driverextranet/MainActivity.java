package com.deliberry.driverextranet;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Estás seguro de que quieres salir?")
                .setCancelable(false)
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences settings = getSharedPreferences(Consts.SHARED_PREFERENCES_NAME, 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.remove(Consts.SHARED_PREFERENCE_ID_NAME);
                        editor.apply();
                        finish();
                        Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(loginIntent);                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
