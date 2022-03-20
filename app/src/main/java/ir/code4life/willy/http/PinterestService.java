package ir.code4life.willy.http;

import com.google.gson.JsonObject;

import java.util.Map;

import ir.code4life.willy.http.models.Board;
import ir.code4life.willy.http.models.DataResponse;
import ir.code4life.willy.http.models.Pin;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PinterestService {

    @POST("v5/oauth/token")
    @FormUrlEncoded
    Call<JsonObject> authenticate(@Header("Authorization") String authorization, @FieldMap() Map<String,String> fields);

    @POST("v5/oauth/token")
    @FormUrlEncoded
    Call<JsonObject> refreshToken(@Header("Authorization") String authorization, @FieldMap Map<String,String> fields);

    @GET("v5/user_account")
    Call<JsonObject> user_account(@Header("Authorization") String authorization);

    @GET("v5/boards?page_size=100")
    Call<DataResponse<Board>> boards(@Header("Authorization") String authorization, @Query("bookmark") String bookmark);

    @GET("v5/boards/{Id}")
    Call<Board> getBoard(@Header("Authorization") String authorization,@Path("Id") String board_id);

    @GET("v5/boards/{Id}/pins?page_size=3")
    Call<DataResponse<Pin>> previewPins(@Header("Authorization") String authorization, @Path("Id") String id);

    @GET("v5/boards/{Id}/pins?page_size=100")
    Call<DataResponse<Pin>> pins(@Header("Authorization") String authorization, @Path("Id") String id, @Query("bookmark") String bookmark);
}
