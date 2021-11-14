package ir.code4life.willy.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import ir.code4life.willy.database.AppDatabase;
import ir.code4life.willy.database.dao.BoardDao;
import ir.code4life.willy.database.dao.MediaDao;
import ir.code4life.willy.database.dao.PinDao;
import ir.code4life.willy.database.dao.SyncDoa;
import ir.code4life.willy.http.ServiceHelper;
import ir.code4life.willy.http.models.Board;
import ir.code4life.willy.http.models.Pin;
import ir.code4life.willy.util.G;

public class SyncService extends Service {
    public static String SYNC_ALL="pinterest_sync_all";
    public static String SYNC_BOARD="pinterest_sync_board";
    public static String SYNCED="pinterest_synced";

    private ServiceHelper helper;
    private AppDatabase database;
    private BoardDao boardDao;
    private PinDao pinDao;
    private MediaDao mediaDao;
    private BroadcastReceiver receiver;
    private SyncDoa syncDao;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        helper= new ServiceHelper(getApplicationContext());
        database = AppDatabase.getInstance(getApplicationContext());
        boardDao = database.boardDao();
        pinDao = database.pinDao();
        mediaDao = database.mediaDao();
        syncDao = database.syncDoa();

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (SYNC_ALL.equals(action)) {
                    sync_all();
                }else if(SYNC_BOARD.equals(action)){
                    Long board_id = intent.getLongExtra("board_id",0);
                    sync_board(board_id);
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(SYNC_ALL);
        filter.addAction(SYNC_BOARD);
        registerReceiver(receiver,filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    public void sync_board(Long board_id){

    }

    public void sync_all(){
        helper.getBoards(null,boards -> {
            boardDao.insertAll(boards);
            List<Long> ids = new ArrayList<>();
            for (Board board :boards) {
                Long sync_id = board.create_syncId();
                ids.add(board.id);
                helper.getBoardPreviews(board.id, list -> {
                    pinDao.insertAll(list);
                    mediaDao.insertAll(G.getPinMedia(list.toArray(new Pin[0])));
                });
            }
            boardDao.sync_deleted_boards(ids);
        });
    }

    public void sync_boards(){

    }
}
