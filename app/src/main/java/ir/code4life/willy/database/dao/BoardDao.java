package ir.code4life.willy.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ir.code4life.willy.http.models.Board;

@Dao
public interface BoardDao {

    @Query("SELECT * FROM board LIMIT :limit OFFSET :offset")
    List<Board> getAll(Integer limit,Integer offset);

    @Query("SELECT * FROM board")
    List<Board> getAll();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<Board> boards);

    @Query("UPDATE board SET sync_id=:sync_id where id=:board_id")
    void update_sync_id(Long board_id, Long sync_id);

    @Query("DELETE FROM board WHERE id NOT IN (:ids)")
    void sync_deleted_boards(List<Long> ids);
}
