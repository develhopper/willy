package ir.code4life.willy.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import ir.code4life.willy.BuildConfig;

public class SecurePreference {
    private final SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor = null;

    public SecurePreference(Context context, String name){
        this.sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public void putString(String key,String value,boolean encrypt){
        initEditor();
        if(encrypt)
            editor.putString(key,encrypt(value));
        else
            editor.putString(key,value);
    }

    public void putBoolean(String key,Boolean value){
        initEditor();
        editor.putBoolean(key,value);
    }

    public Boolean getBoolean(String key){
        return sharedPreferences.getBoolean(key,false);
    }

    public String getString(String key,boolean decrypt){
        if(decrypt)
            return decrypt(sharedPreferences.getString(key,null));
        return sharedPreferences.getString(key,null);
    }

    public void remove(String key){
        initEditor();
        editor.remove(key);
    }

    private void initEditor() {
        if(editor==null)
            editor = sharedPreferences.edit();
    }

    public void apply(){
        editor.apply();
        editor = null;
    }

    private String encrypt(String value){
        try {
            if(value==null)
                return null;
            SecretKeySpec secret = new SecretKeySpec(BuildConfig.HASH_SECRET.getBytes(), "AES");
            @SuppressLint("GetInstance")
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE,secret);
            byte [] encrypted = cipher.doFinal(value.getBytes());
            return Base64.encodeToString(encrypted,Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String decrypt(String value){
        try {
            SecretKeySpec secret = new SecretKeySpec(BuildConfig.HASH_SECRET.getBytes(), "AES");
            @SuppressLint("GetInstance")
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secret);
            byte [] encrypted = Base64.decode(value,Base64.DEFAULT);
            return new String(cipher.doFinal(encrypted));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }
}
