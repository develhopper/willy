package ir.code4life.willy.http.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

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
    public String local_path;
    private String image_url;

    public String getImage_url() {
        if(this.image_url!=null)
            return image_url;
        return media.getImage(Size._originals);
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getImage_url(Size size){
        if(this.image_url!=null){
            String result = image_url.replace("/originals/","/"+size.name().substring(1)+"/");
            if(result.endsWith(".png") && size != Size._originals){
                result = result.replace(".png",".jpg");
            }
            return result;
        }
        return null;
    }
}

