package com.deliberry.driverextranet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.deliberry.driverextranet.utils.Consts;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    @BindView(R.id.btn_login)
    Button btn_login;

    @BindView(R.id.input_email)
    EditText input_email;

    @BindView(R.id.input_password)
    EditText input_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        int id = retrieveCredentials();

        if (id != 0) {
            startMainActivity(id);
        }

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(input_email.getText().toString(), input_password.getText().toString());
            }
        });
    }

    private void login(String email, String password) {
        if (!validate(email, password)) {
            onLoginFailed();
        } else {
            loginRequest(this, email, password);
        }
    }

    private boolean validate(String email, String password) {
        boolean valid = true;

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            input_email.setError("Introduce una dirección de correo válida");
            valid = false;
        } else {
            input_email.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            input_password.setError("Entre 4 y 10 carácteres alfanuméricos");
            valid = false;
        } else {
            input_password.setError(null);
        }

        return valid;
    }

    private void onLoginFailed() {
        Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show();
    }

    private void onLoginSuccess(int id) {
        storeCredentials(id);
        startMainActivity(id);
        finish();
    }

    private void loginRequest(Context context, final String email, final String password) {
        RequestQueue queue = Volley.newRequestQueue(context);

        JSONObject parameters = new JSONObject(getShopperParams(email, password));

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Consts.LOGIN_REQUEST_URL, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("status").equals("OK")) {
                        onLoginSuccess(response.getInt("id"));
                    } else {
                        onLoginFailed();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    onLoginFailed();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "VolleyError", error);
                onLoginFailed();
            }
        });
        queue.add(jsonObjectRequest);
    }

    private Map<String, String> getShopperParams(String email, String password) {
        Map<String,String> params = new HashMap<String, String>();
        params.put("email", email);
        params.put("password", password);

        return params;
    }

    private void storeCredentials(int id) {
        SharedPreferences sharedPreferences = getSharedPreferences(Consts.SHARED_PREFERENCES_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(Consts.SHARED_PREFERENCE_ID_NAME, id);
        editor.apply();
    }

    private int retrieveCredentials() {
        SharedPreferences sharedPreferences = this.getSharedPreferences(Consts.SHARED_PREFERENCES_NAME, 0);

        return sharedPreferences.getInt(Consts.SHARED_PREFERENCE_ID_NAME, 0);
    }

    private void startMainActivity(int id) {
        Intent mainIntent = new Intent(this, MainActivity.class);
        Bundle args = new Bundle();
        args.putInt("shopperUserId", id);
        startActivity(mainIntent);
    }
}