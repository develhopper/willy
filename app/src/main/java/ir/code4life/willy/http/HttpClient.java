package ir.code4life.willy.http;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpClient{
    private static Retrofit retorfit = null;

    public static Retrofit getInstance(){
        if(retorfit == null){
            retorfit = new Retrofit.Builder()
                    .baseUrl("https://api.pinterest.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retorfit;
    }

}
