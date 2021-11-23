package ir.code4life.willy.util;

import android.os.Environment;

import java.io.File;

import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

public class FileSystem {
    public static final String ROOT_DIR = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()+"/Willy/";

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

    public static Boolean saveImage(BufferedSource source, String path){
        File file = new File(path);
        try {
            BufferedSink sink = Okio.buffer(Okio.sink(file));
            sink.writeAll(source);
            sink.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getPinPath(String board_name,String pin_url) {
        String ext = pin_url.substring(pin_url.lastIndexOf('.'));
        return getBoardDir(board_name)+G.randomString()+ext;
    }

    public static void removeDir(String dir){
        File parent;

        if(dir == null)
            parent = new File(ROOT_DIR);
        else
            parent = new File(dir);

        if(parent.exists() && parent.isDirectory()){
            String[] children = parent.list();
            if(children == null)
                return;
            for(String child: children){
                G.log(parent.getAbsolutePath());
                File f = new File(parent,child);
                if(f.isDirectory())
                    removeDir(f.getAbsolutePath());
                else
                    f.delete();
            }
        }

        parent.delete();
    }

    public static Boolean Exists(String path){
        if(path == null)
            return false;
        return new File(path).exists();
    }
}
