package com.deliberry.driverextranet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }
}