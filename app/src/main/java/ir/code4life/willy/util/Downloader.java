package ir.code4life.willy.util;

import ir.code4life.willy.database.models.Download;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Downloader {

    private static Downloader INSTANCE;
    private final OkHttpClient client;
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
            return FileSystem.saveImage(response.body().source(),download.path);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

}
