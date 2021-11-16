package ir.code4life.willy.util;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

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

    public static Boolean saveImage(InputStream inputStream,String path){
        File file = new File(path);
        try {
            OutputStream outputStream = new FileOutputStream(file);
            byte [] buffer = new byte[4 * 1024];
            int read;

            while((read = inputStream.read(buffer)) != -1){
                outputStream.write(buffer,0,read);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        }catch (Exception ignore){
            return false;
        }
        return true;
    }

    public static String getPinPath(String board_name,String pin_url) {
        return getBoardDir(board_name)+G.getFileNameFromUrl(pin_url);
    }

    public static Boolean Exists(String path){
        File file = new File(path);
        return file.exists();
    }
}
