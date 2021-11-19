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

import ir.code4life.willy.BuildConfig;
import ir.code4life.willy.database.AppDatabase;
import ir.code4life.willy.database.dao.BoardDao;
import ir.code4life.willy.database.dao.PinDao;
import ir.code4life.willy.http.ServiceHelper;
import ir.code4life.willy.http.models.Board;
import ir.code4life.willy.http.models.Pin;
import ir.code4life.willy.util.G;
import ir.code4life.willy.util.SecurePreference;

public class SyncService extends Service {
    public static String SYNC_ALL = "pinterest_sync_all";
    public static String SYNC_BOARD = "pinterest_sync_board";
    public static String SYNCED = "pinterest_synced";

    private ServiceHelper helper;
    private BoardDao boardDao;
    private PinDao pinDao;
    private BroadcastReceiver receiver;
    private Boolean pending = false;
    private SecurePreference preference;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        helper = new ServiceHelper(getApplicationContext());
        AppDatabase database = AppDatabase.getInstance(getApplicationContext());
        boardDao = database.boardDao();
        pinDao = database.pinDao();

        preference = new SecurePreference(getApplicationContext(),"SharedPref");

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                G.log("Syncing: "+pending);
                if (SYNC_ALL.equals(action)) {
                    sync_all();
                    pending = true;
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(SYNC_ALL);
        filter.addAction(SYNC_BOARD);
        registerReceiver(receiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }


    public void sync_all() {
        if (pending) {
            Toast.makeText(getApplicationContext(), "Sync pending", Toast.LENGTH_SHORT).show();
            return;
        }
        if(preference.getBoolean("guest")){
            helper.getBoard(BuildConfig.BOARD_ID, this::sync_boards);
            G.log("GUEST");
            return;
        }
        helper.getBoards(null, this::sync_boards);
    }

    public void sync_boards(List<Board> boards) {
        boardDao.insertAll(boards);
        List<Long> board_ids = new ArrayList<>();
        Iterator<Board> iterator = boards.iterator();
        while (iterator.hasNext()) {
            Board board = iterator.next();
            board_ids.add(board.id);
            board.create_syncId();

            helper.getPins(null, board.id, list -> {
                List<Long> pin_ids = new ArrayList<>();
                Long sync_id = board.getSync_id();

                for (Pin pin : list) {
                    pin_ids.add(pin.id);
                }
                pinDao.insertAll(list);

                pinDao.set_sync_id(pin_ids, sync_id);
                pinDao.sync_removed_items(board.id, sync_id);

                if (!iterator.hasNext()) {
                    Intent broadcast = new Intent(SYNCED);
                    sendBroadcast(broadcast);
                }
            });
        }
        boardDao.sync_deleted_boards(board_ids);
        pending = false;
    }
}
