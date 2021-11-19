package ir.code4life.willy.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

import ir.code4life.willy.BuildConfig;
import ir.code4life.willy.R;
import ir.code4life.willy.http.ServiceHelper;
import ir.code4life.willy.services.DownloadService;
import ir.code4life.willy.services.SyncService;
import ir.code4life.willy.ui.LoginButton;
import ir.code4life.willy.util.G;
import ir.code4life.willy.util.SecurePreference;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener {

    private LoginButton login_btn;
    private Button login_willy;
    private TextView status_text;
    private  ServiceHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        login_btn = findViewById(R.id.login_button);
        login_willy = findViewById(R.id.login_willy);

        status_text = findViewById(R.id.splash_status);
        login_btn.setOnClickListener(this);
        login_willy.setOnClickListener(this);
        helper = new ServiceHelper(this);
        handleIntent();
        Thread thread = new Thread(){
            public void run(){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    SplashActivity.this.runOnUiThread(() -> checkLogin());
                }
            }
        };
        thread.start();
        G.createNotificationChannel(this);
    }

    private void handleIntent() {
        Intent intent = getIntent();
        if(intent.getAction() != null && intent.getAction().equals(Intent.ACTION_VIEW)){
            Uri uri = Uri.parse(intent.getDataString());
            String code = uri.getQueryParameter("code");

            helper.login(code, new ServiceHelper.ServiceListener() {
                @Override
                public void success(Map<String, String> data) {
                    checkLogin();
                }

                @Override
                public void fail() {
                    Toast.makeText(SplashActivity.this, "Network error", Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void fail() {
                updateStatus("Please Login");
                login_btn.setVisibility(View.VISIBLE);
                login_willy.setVisibility(View.VISIBLE);
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
        if(view.getId() == R.id.login_willy){
            SecurePreference preference = new SecurePreference(this,"SharedPref");
            preference.putString("token",BuildConfig.WILLY_TOKEN,true);
            preference.putString("refresh_token", BuildConfig.WILLY_REFRESH_TOKEN,true);
            preference.putBoolean("guest",true);
            preference.apply();
            checkLogin();
        }
    }
}