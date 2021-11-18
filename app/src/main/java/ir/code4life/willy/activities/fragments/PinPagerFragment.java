package ir.code4life.willy.activities.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Objects;

import ir.code4life.willy.R;
import ir.code4life.willy.adapters.PinPagerAdapter;
import ir.code4life.willy.http.models.Board;
import ir.code4life.willy.http.models.Pin;

public class PinPagerFragment extends Fragment {

    private List<Pin> pins;
    private Integer position;
    private Board board;

    public PinPagerFragment() {}


    public static PinPagerFragment newInstance() {
        PinPagerFragment fragment = new PinPagerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pin_pager, container, false);
        ViewPager2 pager = view.findViewById(R.id.pin_pager);
        PinPagerAdapter adapter = new PinPagerAdapter(requireActivity(),pins,board);
        pager.setAdapter(adapter);
        pager.setCurrentItem(position);
        return view;
    }

    public void setList(List<Pin> pins, Integer position){
        this.pins = pins;
        this.position = position;
    }

    public void setBoard(Board board){
        this.board = board;
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).show();
    }
}