package ir.code4life.willy.database.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Pin {
    @PrimaryKey
    public Long id;
    public String title;
}
