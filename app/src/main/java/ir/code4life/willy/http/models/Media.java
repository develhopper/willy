package ir.code4life.willy.http.models;

import com.google.gson.JsonObject;

import ir.code4life.willy.util.Size;

public class Media {

    public Long id;

    public JsonObject images;


    public String getImage(Size size) {
        String key = size.name().substring(1);
        if(images.has(key))
            return images.getAsJsonObject(key).get("url").getAsString();
        return null;
    }
}
