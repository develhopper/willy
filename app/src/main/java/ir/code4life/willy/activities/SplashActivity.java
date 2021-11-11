package ir.code4life.willy.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

import ir.code4life.willy.BuildConfig;
import ir.code4life.willy.R;
import ir.code4life.willy.http.ServiceHelper;
import ir.code4life.willy.util.G;
import ir.code4life.willy.http.HttpClient;
import ir.code4life.willy.http.PinterestService;
import ir.code4life.willy.ui.LoginButton;
import ir.code4life.willy.util.SecurePreference;
import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener {

    private LoginButton login_btn;
    private TextView status_text;
    private  ServiceHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        login_btn = findViewById(R.id.login_button);
        status_text = findViewById(R.id.splash_status);
        login_btn.setOnClickListener(this);
        helper = new ServiceHelper(this);

        handleIntent();

        checkLogin();
    }

    private void handleIntent() {
        Intent intent = getIntent();
        if(intent.getAction().equals(Intent.ACTION_VIEW)){
            Uri uri = Uri.parse(intent.getDataString());
            String code = uri.getQueryParameter("code");

            helper.login(code, new ServiceHelper.ServiceListener() {
                @Override
                public void success(Map<String, String> data) {
                    checkLogin();
                }

                @Override
                public void fail() {

                }
            });
        }
    }


    private void checkLogin() {
        updateStatus("Getting profile info ...");
        helper.profileInfo(new ServiceHelper.ServiceListener() {
            @Override
            public void success(Map<String, String> data) {
                Toast.makeText(getApplicationContext(), "Logged in successfully", Toast.LENGTH_SHORT).show();
                login_btn.setVisibility(View.INVISIBLE);
                updateStatus("Logged in");
            }

            @Override
            public void fail() {
                updateStatus("Please Login");
                login_btn.setVisibility(View.VISIBLE);
            }
        });
    }

    private void updateStatus(String status) {
        status_text.setText(status);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.login_button){
            String uri = "https://www.pinterest.com/oauth/?client_id=%s&redirect_uri=%s&response_type=code&scope=boards:read,pins:read,user_accounts:read";
            uri = String.format(uri,BuildConfig.APP_ID, G.getCallbackURI());
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
        }
    }
}