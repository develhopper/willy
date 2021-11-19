package ir.code4life.willy.activities.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ir.code4life.willy.R;
import ir.code4life.willy.adapters.DownloadRecyclerAdapter;
import ir.code4life.willy.database.AppDatabase;
import ir.code4life.willy.database.dao.DownloadDao;
import ir.code4life.willy.database.models.Download;
import ir.code4life.willy.util.G;


public class DownloadsFragment extends Fragment {

    AppDatabase database;
    DownloadDao downloadDao;
    boolean hit_the_end = false;
    private DownloadRecyclerAdapter adapter;

    TextView total_txt,completed_txt,pending_txt;

    public DownloadsFragment() {
        // Required empty public constructor
    }

    public static DownloadsFragment newInstance() {
        return new DownloadsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_downloads, container, false);
        setHasOptionsMenu(true);
        total_txt = view.findViewById(R.id.total_download);
        completed_txt = view.findViewById(R.id.completed);
        pending_txt = view.findViewById(R.id.pending);

        database = AppDatabase.getInstance(null);
        downloadDao = database.downloadDao();
        setupRecycler(view);
        return view;
    }

    @SuppressLint("SetTextI18n")
    private void setupRecycler(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.downloads_recycler);
        List<Download> list = downloadDao.AllDownloads(0);
        Download extra = downloadDao.downloadExtra();

        total_txt.setText("Total:  "+extra.total);
        completed_txt.setText("Completed: "+extra.completed);
        pending_txt.setText("Pending: "+(extra.total-extra.completed));

        adapter = new DownloadRecyclerAdapter(getContext(),list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!hit_the_end && !recyclerView.canScrollVertically(1) && newState==RecyclerView.SCROLL_STATE_IDLE) {
                    adapter.page+=1;
                    List<Download> list = downloadDao.AllDownloads(adapter.page*50);
                    if(!list.isEmpty()){
                        adapter.updateList(list);
                    }else{
                        hit_the_end = true;
                    }
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.download_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.delete_all){
            downloadDao.clearAll();
            Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
            adapter.setList(new ArrayList<>());

        }else if(item.getItemId() == R.id.delete_completed){
            downloadDao.clearCompleted();
            adapter.setList(downloadDao.AllDownloads(0));
            Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
        }else if(item.getItemId() == R.id.resume){
            G.sendDownloadBroadcast(requireContext());
        }
        return super.onOptionsItemSelected(item);
    }
}