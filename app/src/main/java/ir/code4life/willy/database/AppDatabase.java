package ir.code4life.willy.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import ir.code4life.willy.database.dao.BoardDao;
import ir.code4life.willy.database.dao.DownloadDao;
import ir.code4life.willy.database.dao.PinDao;
import ir.code4life.willy.database.dao.SyncDoa;
import ir.code4life.willy.database.models.Download;
import ir.code4life.willy.database.models.Sync;
import ir.code4life.willy.http.models.Board;
import ir.code4life.willy.http.models.Pin;

@Database(entities = {Board.class, Pin.class, Sync.class, Download.class}, version = 8, exportSchema = false)
@TypeConverters(Converter.class)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;
    protected AppDatabase(){}

    public static AppDatabase getInstance(Context context){
        if(instance == null){
            instance = create(context);
        }
        return instance;
    }

    private static AppDatabase create(Context context){
        return Room.databaseBuilder(context,AppDatabase.class,"app_database.db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }

    public abstract BoardDao boardDao();
    public abstract PinDao pinDao();
    public abstract SyncDoa syncDoa();
    public abstract DownloadDao downloadDao();
}
