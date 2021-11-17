package ir.code4life.willy.http;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ir.code4life.willy.BuildConfig;
import ir.code4life.willy.http.models.Board;
import ir.code4life.willy.http.models.DataResponse;
import ir.code4life.willy.http.models.Pin;
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
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if(response.code() == 200){
                    JsonObject json = response.body();
                    if(json != null){
                        SecurePreference securePreference = new SecurePreference(context,"SharedPref");
                        securePreference.putString("token",json.get("access_token").getAsString(),true);
                        securePreference.putString("refresh_token",json.get("refresh_token").getAsString(),true);
                        securePreference.apply();
                        listener.success(null);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
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
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if(response.code() == 200){
                    JsonObject json = response.body();
                    if(json != null){
                        securePreference.putString("avatar",json.get("profile_image").getAsString(),false);
                        securePreference.putString("username",json.get("username").getAsString(),false);
                        securePreference.apply();
                        listener.success(null);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {

            }
        });
    }

    public void getBoards(String bookmark, DataListener<Board> listener){
        String token = "Bearer "+securePreference.getString("token",true);
        Call<DataResponse<Board>> call = service.boards(token,bookmark);

        call.enqueue(new Callback<DataResponse<Board>>() {
            @Override
            public void onResponse(@NonNull Call<DataResponse<Board>> call, @NonNull Response<DataResponse<Board>> response) {
                DataResponse<Board> boards = response.body();
                if (boards != null) {
                    listener.success(boards.items);
                }
                else{
                    Toast.makeText(context, "No boards", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<DataResponse<Board>> call, @NonNull Throwable t) {

            }
        });
    }

    public void getBoardPreviews(Long board_id, DataListener<Pin> listener){
        String token = "Bearer "+securePreference.getString("token",true);
        Call<DataResponse<Pin>> call = service.previewPins(token,board_id.toString());

        call.enqueue(new Callback<DataResponse<Pin>>() {
            @Override
            public void onResponse(@NonNull Call<DataResponse<Pin>> call, @NonNull Response<DataResponse<Pin>> response) {
                if(response.body()!=null && response.code()==200){
                    List<Pin> list = response.body().items;
                    listener.success(list);
                }

            }

            @Override
            public void onFailure(@NonNull Call<DataResponse<Pin>> call, @NonNull Throwable t) {

            }
        });
    }

    public void getPins(String bookmark, Long board_id, DataListener<Pin> listener){
        String token = "Bearer "+securePreference.getString("token",true);

        Call<DataResponse<Pin>> call = service.pins(token,board_id.toString(),bookmark);

        call.enqueue(new Callback<DataResponse<Pin>>() {
            @Override
            public void onResponse(@NonNull Call<DataResponse<Pin>> call, @NonNull Response<DataResponse<Pin>> response) {
                G.log(response.raw().toString());
                if (response.body() != null && response.code() == 200) {
                    listener.success(response.body().items);
                    if(response.body().bookmark!=null){
                        getPins(response.body().bookmark,board_id,listener);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<DataResponse<Pin>> call, @NonNull Throwable t) {

            }
        });
    }
    public interface ServiceListener{
        void success(Map<String,String> data);
        void fail();
    }

    public interface DataListener<T>{
        void success(List<T> list);
    }
}
