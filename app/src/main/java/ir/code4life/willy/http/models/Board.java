package ir.code4life.willy.http.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Board {

    @PrimaryKey
    public Long id;
    public String name;
}
