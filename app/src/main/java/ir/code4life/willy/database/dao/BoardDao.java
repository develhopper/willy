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

}
