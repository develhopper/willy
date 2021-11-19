package ir.code4life.willy.activities.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.code4life.willy.R;
import ir.code4life.willy.adapters.BoardRecyclerAdapter;
import ir.code4life.willy.database.AppDatabase;
import ir.code4life.willy.database.dao.BoardDao;
import ir.code4life.willy.database.models.BoardWithPins;
import ir.code4life.willy.services.DownloadService;
import ir.code4life.willy.services.SyncService;
import ir.code4life.willy.util.G;

public class BoardsFragment extends Fragment {

    private BoardRecyclerAdapter adapter;
    private BoardDao boardDao;
    private BroadcastReceiver receiver;
    private AppDatabase database;

    public BoardsFragment() { }

    public static BoardsFragment newInstance() {
        return new BoardsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_boards, container, false);
        setHasOptionsMenu(true);

        database = AppDatabase.getInstance(getContext());
        boardDao = database.boardDao();

        RecyclerView recyclerView = view.findViewById(R.id.boards_recycler);
        
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        
        adapter = new BoardRecyclerAdapter(getActivity());
        
        recyclerView.setAdapter(adapter);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(SyncService.SYNCED)){
                    refreshList();
                }
                if(intent.getAction().equals(SyncService.SERVICE_STARTED)){
                    G.log("Service started");
                    refreshList();
                }
            }
        };
        IntentFilter filter = new IntentFilter(SyncService.SYNCED);
        filter.addAction(SyncService.SERVICE_STARTED);
        requireContext().registerReceiver(receiver,filter);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requireContext().unregisterReceiver(receiver);
    }

    public void refreshList() {
        List<BoardWithPins> boards = database.boardDao().getAllWithCount();
        adapter.updateList(boards);
        if(boards.isEmpty()){
            Intent intent = new Intent(SyncService.SYNC_ALL);
            requireContext().sendBroadcast(intent);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.board_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.nav_refresh){
                Intent intent = new Intent();
                intent.setAction(SyncService.SYNC_ALL);
                requireContext().sendBroadcast(intent);
        }
        if(item.getItemId() == R.id.nav_about){
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.addToBackStack(null);
            transaction.replace(R.id.main_fragment,new AboutFragment());
            transaction.commit();
        }
        return super.onOptionsItemSelected(item);
    }
}