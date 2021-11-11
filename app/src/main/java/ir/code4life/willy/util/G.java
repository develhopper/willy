package ir.code4life.willy.util;

import android.content.Context;
import android.content.SharedPreferences;

import ir.code4life.willy.BuildConfig;

public class G {

    public static String getCallbackURI(){
        return "pin"+BuildConfig.APP_ID + "://oauth/";
    }

    public static SharedPreferences getSharedPreference(Context context){
        return context.getSharedPreferences("SharedPref",Context.MODE_PRIVATE);
    }
}
