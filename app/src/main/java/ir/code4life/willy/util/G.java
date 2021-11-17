package ir.code4life.willy.util;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.webkit.URLUtil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ir.code4life.willy.BuildConfig;
import ir.code4life.willy.R;
import ir.code4life.willy.activities.MainActivity;
import ir.code4life.willy.database.models.Media;
import ir.code4life.willy.http.models.Pin;
import ir.code4life.willy.services.DownloadService;

public class G {

    public static final Integer PERMISSION_REQUEST_CODE=1000;

    public static String getCallbackURI(){
        return "pin"+BuildConfig.APP_ID + "://oauth/";
    }

    public static List<Media> getPinMedia(List<Pin> pins){
        List<Media> list = new ArrayList<>();
        for(Pin pin:pins){
            ir.code4life.willy.http.models.Media media = pin.media;
            list.add(new Media(Size._150x150, media.getImage(Size._150x150), pin.id, pin.board_id));
            list.add(new Media(Size._originals, media.getImage(Size._originals),pin.id, pin.board_id));
        }
        return list;
    }

    public static void log(String message){
        Log.d("DEBUG", message);
    }

    public static Boolean checkStoragePermission(Context context){
        if (context.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }else{
            ActivityCompat.requestPermissions((AppCompatActivity)context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE);
        }
        return false;
    }

    public static String getFileNameFromUrl(String url){
        return URLUtil.guessFileName(url,null,null);
    }

    public static void sendDownloadBroadcast(Context context){
        Intent intent = new Intent(DownloadService.START_DOWNLOAD);
        context.sendBroadcast(intent);
    }

    public static void setWallpaper(Context context,String path){
        File file = new File(path);
        if(file.exists()){
            Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID+".provider", file);
            Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(uri,"image/*");
            intent.putExtra("mimeType", "image/*");
            context.startActivity(Intent.createChooser(intent,"Set as:"));
        }
    }

    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(context.getPackageName(), name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
