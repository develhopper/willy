package ir.code4life.willy.util;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import ir.code4life.willy.database.dao.DownloadDao;
import ir.code4life.willy.http.models.Pin;
import okio.BufferedSource;
import okio.Okio;
import okio.Sink;

public class FileSystem {
    public static final String ROOT_DIR = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/Willy/";

    public static Boolean mkdir(String name){
        String path = ROOT_DIR+name;
        File dir = new File(path);
        if(dir.exists())
            return true;

        return dir.mkdirs();
    }

    public static String getBoardDir(String name){
        if(mkdir(name)){
            return ROOT_DIR+name+"/";
        }
        return null;
    }

    public static void saveImage(BufferedSource source, String path){
        try {
            File file = new File(path);
            Sink sink = Okio.sink(file);
            source.readAll(sink);
            source.close();
            sink.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getPinPath(String board_name,String pin_url) {
        return getBoardDir(board_name)+G.getFileNameFromUrl(pin_url);
    }

    public static Boolean Exists(String path){
        if(path == null)
            return false;
        return new File(path).exists();
    }
}
