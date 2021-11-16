package ir.code4life.willy.http.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import ir.code4life.willy.database.AppDatabase;
import ir.code4life.willy.database.models.Sync;

@Entity
public class Board {

    @PrimaryKey
    public Long id;
    public String name;
    private Long sync_id = null;


    public Long getSync_id() {
        if(sync_id!=null)
            return this.sync_id;
        AppDatabase database = AppDatabase.getInstance(null);
        this.sync_id = database.syncDoa().getSyncId(this.id);
        return (sync_id==null)?0L:sync_id;
    }

    public void setSync_id(Long sync_id) {
        this.sync_id = sync_id;
    }

    public Long create_syncId(){
        AppDatabase database = AppDatabase.getInstance(null);
        Long sync_id = database.syncDoa().getSyncId(this.id);

        if(sync_id == null){
            sync_id = 0L;
        }

        sync_id+=1;
        database.syncDoa().Insert(new Sync(this.id,sync_id));
        database.boardDao().update_sync_id(this.id,sync_id);
        this.sync_id = sync_id;
        return sync_id;
    }
}
