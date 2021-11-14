package ir.code4life.willy.http.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.JsonObject;

import java.util.Date;

import ir.code4life.willy.util.Size;

@Entity(foreignKeys = {
        @ForeignKey(entity = Board.class, parentColumns = "id",
                childColumns = "board_id",
                onDelete = ForeignKey.CASCADE)},indices = {@Index("board_id")})
public class Pin {
    @PrimaryKey
    @ColumnInfo(name = "pinId")
    public Long id;
    public Long board_id;
    @Ignore
    public Media media;
    public String title;
    public Long sync_id;
}

