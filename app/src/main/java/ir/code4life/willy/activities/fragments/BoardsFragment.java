package ir.code4life.willy.activities.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.code4life.willy.R;
import ir.code4life.willy.adapters.BoardRecyclerAdapter;
import ir.code4life.willy.database.AppDatabase;
import ir.code4life.willy.database.dao.BoardDao;
import ir.code4life.willy.database.models.BoardWithPins;
import ir.code4life.willy.http.ServiceHelper;
import ir.code4life.willy.services.SyncService;

public class BoardsFragment extends Fragment {

    private BoardRecyclerAdapter adapter;
    private BoardDao boardDao;

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
        ServiceHelper helper = new ServiceHelper(getContext());

        AppDatabase database = AppDatabase.getInstance(getContext());
        boardDao = database.boardDao();

        RecyclerView recyclerView = view.findViewById(R.id.boards_recycler);
        
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        
        adapter = new BoardRecyclerAdapter(getActivity());
        
        recyclerView.setAdapter(adapter);
        
        refreshList();
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(SyncService.SYNCED)){
                    Log.d("DEBUG", "synced");
                    refreshList();
                }
            }
        };
        IntentFilter filter = new IntentFilter(SyncService.SYNCED);
        requireContext().registerReceiver(receiver,filter);
        return view;
    }

    public void refreshList() {
        List<BoardWithPins> boards = boardDao.getAllWithCount();
        if(boards.isEmpty()){
            Intent intent = new Intent();
            intent.setAction(SyncService.SYNC_ALL);
            requireContext().sendBroadcast(intent);
        }
        adapter.updateList(boards);
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
        return super.onOptionsItemSelected(item);
    }
}