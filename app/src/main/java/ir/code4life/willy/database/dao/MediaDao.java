package ir.code4life.willy.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ir.code4life.willy.database.models.Media;

@Dao
public interface MediaDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<Media> list);

    @Query("SELECT * FROM Media WHERE size=:size AND board_id=:board_id  ORDER BY mediaId ASC LIMIT 3")
    List<Media> getPreviews(Integer size,Long board_id);
}
