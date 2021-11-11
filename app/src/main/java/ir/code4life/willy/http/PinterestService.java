package ir.code4life.willy.http;

import com.google.gson.JsonObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PinterestService {

    @POST("v5/oauth/token")
    @FormUrlEncoded
    Call<JsonObject> authenticate(@Header("Authorization") String authorization, @FieldMap() Map<String,String> fields);

    @GET("v5/user_account")
    Call<JsonObject> user_account(@Header("Authorization") String authorization);
}
