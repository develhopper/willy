package ir.code4life.willy.database.models;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.List;

import ir.code4life.willy.http.models.Board;
import ir.code4life.willy.http.models.Pin;
import ir.code4life.willy.util.Size;

@Entity(indices = {@Index(value = {"image_url", "pin_id"},
        unique = true), @Index("board_id"),@Index("pin_id") },foreignKeys = {
        @ForeignKey(entity = Board.class, parentColumns = "id", childColumns = "board_id", onDelete = CASCADE),
        @ForeignKey(entity = Pin.class, parentColumns = "pinId", childColumns = "pin_id", onDelete = CASCADE)})
public class Media {

    @PrimaryKey
    @ColumnInfo(name = "mediaId")
    public Long id;
    public Size size;
    @ColumnInfo(name = "image_url")
    public String url;
    public Long pin_id;
    public Long board_id;


    @Ignore
    public Media(Size size,String url,Long pin_id,Long board_id){
        this.size = size;
        this.url = url;
        this.pin_id = pin_id;
        this.board_id = board_id;
    }

    public Media(){

    }
}
