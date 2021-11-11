package ir.code4life.willy.http;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

import ir.code4life.willy.BuildConfig;
import ir.code4life.willy.util.G;
import ir.code4life.willy.util.SecurePreference;
import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ServiceHelper {
    Retrofit client;
    SecurePreference securePreference;
    Context context;
    PinterestService service;

    public ServiceHelper(Context context){
        this.context = context;
        client = HttpClient.getInstance();
        securePreference = new SecurePreference(context,"SharedPref");
        service = client.create(PinterestService.class);
    }

    public void login(String code,ServiceListener listener){
        String authorization = Credentials.basic(BuildConfig.APP_ID,BuildConfig.APP_SECRET);

        Map<String, String> fields = new HashMap<>();

        fields.put("code", code);
        fields.put("grant_type","authorization_code");
        fields.put("redirect_uri", G.getCallbackURI());

        Call<JsonObject> call = service.authenticate(authorization,fields);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.code() == 200){
                    JsonObject json = response.body();
                    SecurePreference securePreference = new SecurePreference(context,"SharedPref");
                    securePreference.putString("token",json.get("access_token").getAsString(),true);
                    securePreference.putString("refresh_token",json.get("refresh_token").getAsString(),true);
                    securePreference.apply();
                    listener.success(null);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("DEBUG",t.getMessage());
            }
        });
    }

    public void profileInfo(ServiceListener listener){
        String token = securePreference.getString("token",true);
        if(token==null){
            listener.fail();
            return;
        }

        Call<JsonObject> call = service.user_account("Bearer "+token);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.code() == 200){
                    JsonObject json = response.body();
                    securePreference.putString("avatar",json.get("profile_image").getAsString(),false);
                    securePreference.putString("username",json.get("username").getAsString(),false);
                    listener.success(null);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }
    public interface ServiceListener{
        void success(Map<String,String> data);
        void fail();
    }
}
