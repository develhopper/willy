package ir.code4life.willy.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import ir.code4life.willy.database.models.Sync;

@Dao
public interface SyncDoa {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void Insert(Sync sync);

    @Query("SELECT sync_id FROM Sync WHERE board_id = :board_id")
    Long getSyncId(Long board_id);
}
