package ir.code4life.willy.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import ir.code4life.willy.R;
import ir.code4life.willy.adapters.PinRecyclerAdapter;
import ir.code4life.willy.database.AppDatabase;
import ir.code4life.willy.database.dao.BoardDao;
import ir.code4life.willy.database.dao.DownloadDao;
import ir.code4life.willy.database.dao.PinDao;
import ir.code4life.willy.database.models.Download;
import ir.code4life.willy.http.models.Board;
import ir.code4life.willy.http.models.Pin;
import ir.code4life.willy.services.DownloadService;
import ir.code4life.willy.util.FileSystem;
import ir.code4life.willy.util.G;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PinsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PinsFragment extends Fragment {

    private static final String ARG_BOARD_ID = "board_id";

    private Long board_id;
    private PinRecyclerAdapter adapter;
    private AppDatabase database;
    private PinDao pinDao;
    private DownloadDao downloadDao;
    private Boolean hit_the_end=false;
    private Board board;

    public PinsFragment() {
        // Required empty public constructor
    }

    public static PinsFragment newInstance(Long board_id) {
        PinsFragment fragment = new PinsFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_BOARD_ID, board_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            database = AppDatabase.getInstance(null);
            BoardDao boardDao = database.boardDao();
            board_id = getArguments().getLong(ARG_BOARD_ID);
            board = boardDao.getBoard(board_id);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_pins, container, false);
        setHasOptionsMenu(true);

        pinDao = database.pinDao();
        downloadDao = database.downloadDao();

        RecyclerView recyclerView = view.findViewById(R.id.pins_recycler);
        adapter = new PinRecyclerAdapter(getActivity());
        adapter.setBoard(board);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL));
        recyclerView.setAdapter(adapter);

        adapter.updateList(pinDao.getAll(board_id,0));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!hit_the_end && !recyclerView.canScrollVertically(1) && newState==RecyclerView.SCROLL_STATE_IDLE) {
                    adapter.page += 1;
                    List<Pin> list = pinDao.getAll(board_id, adapter.page*20);
                    if(!list.isEmpty()){
                        adapter.updateList(list);
                    }else{
                        hit_the_end = true;
                    }
                }
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.pin_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.download_all){
            List<Download> downloads = new ArrayList<>();
            int page = 0;
            List<Pin> pins = pinDao.getAll(board_id,page);
            while(!pins.isEmpty()){
                for(Pin pin:pins){
                    if(!FileSystem.Exists(pin.local_path) || DownloadService.DEBUG){
                        String link = pin.getImage_url();
                        String path = FileSystem.getPinPath(board.name,link);
                        downloads.add(new Download(path, link, pin.id));
                        downloadDao.insertAll(downloads);
                    }
                }
                page++;
                pins.clear();
                pins.addAll(pinDao.getAll(board_id,page*20));
            }
            G.sendDownloadBroadcast(requireContext());
        }
        return super.onOptionsItemSelected(item);
    }
}