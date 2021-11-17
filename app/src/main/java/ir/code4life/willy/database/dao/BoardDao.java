package ir.code4life.willy.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import ir.code4life.willy.database.models.BoardWithPins;
import ir.code4life.willy.http.models.Board;

@Dao
public interface BoardDao {

    @Transaction
    @Query("SELECT Board.*,count(pinId) AS count FROM Board JOIN Pin ON Board.id=Pin.board_id GROUP BY Board.id")
    List<BoardWithPins> getAllWithCount();

    @Query("SELECT * FROM Board WHERE id=:board_id")
    Board getBoard(Long board_id);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<Board> boards);

    @Query("UPDATE board SET sync_id=:sync_id where id=:board_id")
    void update_sync_id(Long board_id, Long sync_id);

    @Query("DELETE FROM board WHERE id NOT IN (:ids)")
    void sync_deleted_boards(List<Long> ids);
}
