package ir.code4life.willy.activities.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Objects;

import ir.code4life.willy.R;
import ir.code4life.willy.adapters.BoardRecyclerAdapter;
import ir.code4life.willy.database.AppDatabase;
import ir.code4life.willy.database.dao.BoardDao;
import ir.code4life.willy.database.dao.MediaDao;
import ir.code4life.willy.database.dao.PinDao;
import ir.code4life.willy.http.ServiceHelper;
import ir.code4life.willy.http.models.Board;
import ir.code4life.willy.http.models.Media;
import ir.code4life.willy.http.models.Pin;
import ir.code4life.willy.services.SyncService;
import ir.code4life.willy.util.G;

public class BoardsFragment extends Fragment {

    private ServiceHelper helper;
    private RecyclerView recyclerView;
    private AppDatabase database;
    private BoardRecyclerAdapter adapter;
    private BoardDao boardDao;
    private PinDao pinDao;
    private MediaDao mediaDao;

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
        helper = new ServiceHelper(getContext());

        database = AppDatabase.getInstance(getContext());
        boardDao = database.boardDao();

        recyclerView = view.findViewById(R.id.boards_recycler);
        
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        
        adapter = new BoardRecyclerAdapter(getActivity());
        
        recyclerView.setAdapter(adapter);
        
        refreshList();
        return view;
    }

    public void refreshList() {
        List<Board> boards = boardDao.getAll();
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