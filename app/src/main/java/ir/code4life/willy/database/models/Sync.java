package ir.code4life.willy.database.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import ir.code4life.willy.http.models.Board;

@Entity(indices = {@Index(value = "board_id", unique = true)}, foreignKeys = {
        @ForeignKey(entity = Board.class,parentColumns = "id", childColumns = "board_id",onDelete = ForeignKey.CASCADE)
})
public class Sync {

    @PrimaryKey(autoGenerate = true)
    public Long id;
    public Long board_id;
    public Long sync_id;

    @Ignore
    public Sync(Long board_id,Long sync_id){
        this.board_id = board_id;
        this.sync_id = sync_id;
    }

    public Sync(){}
}
