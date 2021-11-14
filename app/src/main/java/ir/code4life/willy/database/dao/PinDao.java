package ir.code4life.willy.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import ir.code4life.willy.database.models.PinWithMedia;
import ir.code4life.willy.http.models.Pin;
import ir.code4life.willy.util.Size;


@Dao
public interface PinDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<Pin> pin);

    @Transaction
    @Query("SELECT * FROM Pin JOIN Media ON Media.pin_id=pinId"
            +" WHERE Pin.board_id=:board_id AND size=:size")
    List<PinWithMedia> getPinsWithMedia(Long board_id,Integer size);
}
