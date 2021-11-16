package ir.code4life.willy.util;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ir.code4life.willy.BuildConfig;
import ir.code4life.willy.database.models.Media;
import ir.code4life.willy.http.models.Pin;

public class G {

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
}
