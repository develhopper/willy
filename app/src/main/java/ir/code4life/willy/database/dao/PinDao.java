package ir.code4life.willy.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import java.util.List;

import ir.code4life.willy.http.models.Pin;


@Dao
public interface PinDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<Pin> pin);
}
