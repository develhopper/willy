package ir.code4life.willy.activities.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.List;

import ir.code4life.willy.R;
import ir.code4life.willy.adapters.PinPagerAdapter;
import ir.code4life.willy.database.models.PinWithMedia;
import ir.code4life.willy.http.models.Pin;
import ir.code4life.willy.util.G;

public class PinPagerFragment extends Fragment {

    private static final String ARG_PINS = "pins";
    private static final String ARG_POS = "position";

    private List<Pin> pins;
    private Integer position;

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
        PinPagerAdapter adapter = new PinPagerAdapter(getActivity(),pins);
        pager.setAdapter(adapter);
        pager.setCurrentItem(position);
        return view;
    }

    public void setList(List<Pin> pins, Integer position){
        this.pins = pins;
        this.position = position;
    }
}