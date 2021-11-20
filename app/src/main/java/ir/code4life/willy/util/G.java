package ir.code4life.willy.util;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.util.Locale;
import java.util.UUID;

import ir.code4life.willy.BuildConfig;
import ir.code4life.willy.R;
import ir.code4life.willy.database.dao.DownloadDao;
import ir.code4life.willy.database.models.Download;
import ir.code4life.willy.http.models.Pin;
import ir.code4life.willy.services.DownloadService;

public class G {

    public static final Integer PERMISSION_REQUEST_CODE = 1000;

    public static String getCallbackURI() {
        return "pin" + BuildConfig.APP_ID + "://oauth/";
    }

    public static void log(String message) {
        Log.d("DEBUG", message);
    }

    public static void checkStoragePermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager();
            ActivityCompat.requestPermissions((AppCompatActivity) context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    5000);
            return;
        }
        if (context.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((AppCompatActivity) context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE);
        }
    }

    public static String getFileNameFromUrl(String url) {
        return URLUtil.guessFileName(url, null, null);
    }

    public static String randomString(){
        return UUID.randomUUID().toString();
    }

    public static void sendDownloadBroadcast(Context context) {
        Intent intent = new Intent(DownloadService.START_DOWNLOAD);
        context.sendBroadcast(intent);
    }

    public static void setWallpaper(Context context, Pin pin, String board_name, DownloadDao downloadDao) {
        if (FileSystem.Exists(pin.local_path)) {
            File file = new File(pin.local_path);
            Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
            Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(uri, "image/*");
            intent.putExtra("mimeType", "image/*");
            context.startActivity(Intent.createChooser(intent, "Set as:"));
        }else{
            download(context,pin,board_name,downloadDao);
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

    public static void download(Context context, Pin pin, String board_name, DownloadDao downloadDao){
        if(FileSystem.Exists(pin.local_path)){
            Toast.makeText(context, "already downloaded", Toast.LENGTH_SHORT).show();
        }else{
            String link = pin.getImage_url();
            String path = FileSystem.getPinPath(board_name,link);
            downloadDao.insertOne(new Download(path,link,pin.id));
            G.sendDownloadBroadcast(context);
        }
    }

    public static void setLanguage(Context context, String lang){
        Configuration cfg = new Configuration();
        if (!TextUtils.isEmpty(lang))
            cfg.locale = new Locale(lang);
        else
            cfg.locale = Locale.getDefault();

        context.getResources().updateConfiguration(cfg, null);
    }
}
