package ir.code4life.willy.activities.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

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
        
        helper = new ServiceHelper(getContext());
        database = AppDatabase.getInstance(getContext());
        boardDao = database.boardDao();
        pinDao = database.pinDao();
        mediaDao = database.mediaDao();
        
        recyclerView = view.findViewById(R.id.boards_recycler);
        
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        
        adapter = new BoardRecyclerAdapter(getContext());
        
        recyclerView.setAdapter(adapter);
        
        refreshList();
        return view;
    }

    private void refreshList() {
        List<Board> boards = boardDao.getAll();
        if(boards.isEmpty()){
            sync();
        }
        adapter.updateList(boards);
    }

    private void sync(){
        helper.getBoards(new ServiceHelper.DataListener<Board>() {
            @Override
            public void success(List<Board> boardList) {
                boardDao.insertAll(boardList);
                for (Board board:boardList) {
                    helper.getBoardPreviews(board.id, new ServiceHelper.DataListener<Pin>() {
                        @Override
                        public void success(List<Pin> list) {
                            pinDao.insertAll(list);
                            mediaDao.insertAll(G.getPinMedia(list.toArray(new Pin[0])));
                            adapter.updateList(boardList);
                        }

                        @Override
                        public void fail() {

                        }
                    });
                }
            }

            @Override
            public void fail() {

            }
        });
    }
}