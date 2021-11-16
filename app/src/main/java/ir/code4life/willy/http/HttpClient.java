package ir.code4life.willy.http;

import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpClient{
    private static Retrofit retorfit = null;

    public static Retrofit getInstance(){
        if(retorfit == null){
            OkHttpClient.Builder okhttp = new OkHttpClient.Builder();
            Dispatcher dispatcher = new Dispatcher();
            dispatcher.setMaxRequests(1);
            okhttp.dispatcher(dispatcher);

            retorfit = new Retrofit.Builder()
                    .client(okhttp.build())
                    .baseUrl("https://api.pinterest.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retorfit;
    }

}
