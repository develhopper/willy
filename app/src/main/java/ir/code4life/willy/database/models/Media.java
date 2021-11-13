package ir.code4life.willy.database.models;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.List;

import ir.code4life.willy.util.Size;

@Entity(indices = {@Index(value = {"url", "pin_id"},
        unique = true)})
public class Media {

    @PrimaryKey
    public Long id;
    public Size size;
    public String url;
    public Long pin_id;
    public Long board_id;

    public Media(Size size,String url,Long pin_id,Long board_id){
        this.size = size;
        this.url = url;
        this.pin_id = pin_id;
        this.board_id = board_id;
    }

    public Media(){

    }
}
