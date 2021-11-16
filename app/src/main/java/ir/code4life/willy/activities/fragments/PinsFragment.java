package ir.code4life.willy.activities.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import ir.code4life.willy.R;
import ir.code4life.willy.adapters.PinRecyclerAdapter;
import ir.code4life.willy.database.AppDatabase;
import ir.code4life.willy.database.dao.PinDao;
import ir.code4life.willy.database.models.PinWithMedia;
import ir.code4life.willy.http.models.Pin;
import ir.code4life.willy.util.G;
import ir.code4life.willy.util.Size;

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
    private Boolean hit_the_end=false;

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
            board_id = getArguments().getLong(ARG_BOARD_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_pins, container, false);
        setHasOptionsMenu(true);

        database = AppDatabase.getInstance(getContext());
        pinDao = database.pinDao();

        RecyclerView recyclerView = view.findViewById(R.id.pins_recycler);
        adapter = new PinRecyclerAdapter(getActivity());
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL));
        recyclerView.setAdapter(adapter);

        adapter.updateList(pinDao.getPinsWithMedia(board_id,0));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!hit_the_end && !recyclerView.canScrollVertically(1) && newState==RecyclerView.SCROLL_STATE_IDLE) {
                    adapter.page+=1;
                    List<Pin> list = pinDao.getPinsWithMedia(board_id, adapter.page*20);
                    if(!list.isEmpty()){
                        G.log(adapter.page*20+"");
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
}