package ir.code4life.willy.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ir.code4life.willy.database.models.Download;

@Dao
public interface DownloadDao {

    @Query("SELECT * FROM Download LIMIT 50 OFFSET :offset")
    List<Download> AllDownloads(Integer offset);

    @Query("SELECT *,count(id) as total , (SELECT count(id) FROM Download WHERE status=1) as completed FROM Download LIMIT 1")
    Download downloadExtra();

    @Query("SELECT * FROM Download WHERE status=0 LIMIT 10 OFFSET :offset")
    List<Download> getPendingDownloads(Integer offset);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<Download> list);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertOne(Download download);

    @Query("DELETE FROM Download WHERE status=1")
    void clearCompleted();

    @Query("DELETE FROM Download")
    void clearAll();

    @Update
    void update(Download download);

    @Query("UPDATE Pin SET local_path=:path WHERE pinId=:pin_id")
    void updatePin(Long pin_id,String path);
}
