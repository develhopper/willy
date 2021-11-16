package ir.code4life.willy.database.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = "link", unique = true)})
public class Download {
    @PrimaryKey
    public Integer id;

    public String path;
    public String link;
    public boolean status=false;
    public Long pin_id;

    public Integer total;
    public Integer completed;

    @Ignore
    public Download(String path,String link,Long pin_id){
        this.path = path;
        this.link = link;
        this.pin_id = pin_id;
    }

    public Download(){}
}
