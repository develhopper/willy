package ir.code4life.willy.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ir.code4life.willy.database.AppDatabase;
import ir.code4life.willy.database.dao.BoardDao;
import ir.code4life.willy.database.dao.PinDao;
import ir.code4life.willy.http.ServiceHelper;
import ir.code4life.willy.http.models.Board;
import ir.code4life.willy.http.models.Pin;

public class SyncService extends Service {
    public static String SYNC_ALL="pinterest_sync_all";
    public static String SYNC_BOARD="pinterest_sync_board";
    public static String SYNCED="pinterest_synced";

    private ServiceHelper helper;
    private BoardDao boardDao;
    private PinDao pinDao;
    private BroadcastReceiver receiver;
    private Boolean pending = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        helper= new ServiceHelper(getApplicationContext());
        AppDatabase database = AppDatabase.getInstance(getApplicationContext());
        boardDao = database.boardDao();
        pinDao = database.pinDao();

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (SYNC_ALL.equals(action)) {
                    sync_all();
                    pending = true;
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


    public void sync_all(){
        if(pending){
            Toast.makeText(getApplicationContext(), "Sync pending", Toast.LENGTH_SHORT).show();
            return;
        }
        helper.getBoards(null,boards -> {
            boardDao.insertAll(boards);
            List<Long> ids = new ArrayList<>();
            Iterator<Board> iterator = boards.iterator();
            while (iterator.hasNext()){
                Board board = iterator.next();
                ids.add(board.id);
                board.create_syncId();
                helper.getBoardPreviews(board.id, list -> pinDao.insertAll(list));
                if(!iterator.hasNext()){
                    Intent broadcast = new Intent(SYNCED);
                    sendBroadcast(broadcast);
                    sync_boards(boards);
                }
            }
            boardDao.sync_deleted_boards(ids);
        });
    }

    public void sync_boards(List<Board> boards){
        for(Board board: boards){
            helper.getPins(null, board.id, list -> {
                List<Long> ids = new ArrayList<>();
                Long sync_id = board.getSync_id();

                for(Pin pin : list){
                    ids.add(pin.id);
                }
                pinDao.insertAll(list);

                pinDao.set_sync_id(ids,sync_id);
                pinDao.sync_removed_items(board.id,sync_id);
            });
        }
        pending = false;
    }
}
