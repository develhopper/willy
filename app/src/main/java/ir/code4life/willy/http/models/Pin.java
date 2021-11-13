package ir.code4life.willy.http.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.JsonObject;

import java.util.Date;

import ir.code4life.willy.util.Size;

@Entity
public class Pin {
    @PrimaryKey
    public Long id;
    public Long board_id;
    @Ignore
    public Media media;
    public String title;
}

