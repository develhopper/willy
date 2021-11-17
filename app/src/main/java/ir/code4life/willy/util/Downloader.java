package ir.code4life.willy.util;

import android.webkit.URLUtil;

import ir.code4life.willy.database.models.Download;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Downloader {

    private static Downloader INSTANCE;
    private OkHttpClient client;
    private Downloader(){
        client = new OkHttpClient();
    }

    public static Downloader newInstance(){
        if(INSTANCE == null)
            INSTANCE = new Downloader();
        return INSTANCE;
    }

    public Boolean download(Download download){
        try{
            if(FileSystem.Exists(download.path)){
                return true;
            }
            Request request = new Request.Builder().url(download.link).build();
            Response response = client.newCall(request).execute();
            assert response.body() != null;
            FileSystem.saveImage(response.body().source(),download.path);
            response.close();
        }catch (Exception e){
            return false;
        }
        return true;
    }

}
