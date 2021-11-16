package ir.code4life.willy.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import ir.code4life.willy.database.models.Media;
import ir.code4life.willy.database.models.PinWithMedia;
import ir.code4life.willy.http.models.Pin;
import ir.code4life.willy.util.Size;


@Dao
public interface PinDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<Pin> pin);

    @Query("SELECT * FROM Pin "
            +" WHERE Pin.board_id=:board_id ORDER BY pinId DESC LIMIT 20 OFFSET :offset")
    List<Pin> getPinsWithMedia(Long board_id, Integer offset);

    @Query("SELECT * FROM Pin WHERE board_id=:board_id  ORDER BY pinId DESC LIMIT 3")
    List<Pin> getPreviews(Long board_id);

    @Query("UPDATE Pin SET sync_id=:sync_id WHERE pinId IN (:ids)")
    void set_sync_id(List<Long> ids,Long sync_id);

    @Query("DELETE FROM Pin WHERE board_id=:board_id AND sync_id != :sync_id")
    void sync_removed_items(Long board_id,Long sync_id);
}
