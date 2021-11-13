package ir.code4life.willy.http.models;

import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import ir.code4life.willy.util.G;
import ir.code4life.willy.util.Size;

public class Media {

    @PrimaryKey
    public Long id;

    @Ignore
    public JsonObject images;


    public String getImage(Size size) {
        String key = size.name().substring(1);
        if(images.has(key))
            return images.getAsJsonObject(key).get("url").getAsString();
        return null;
    }
}
