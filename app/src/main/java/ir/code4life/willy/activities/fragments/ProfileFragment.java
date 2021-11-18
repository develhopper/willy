package ir.code4life.willy.activities.fragments;

import android.os.Bundle;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ir.code4life.willy.R;
import ir.code4life.willy.util.SecurePreference;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProfileFragment.
     */
    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        SecurePreference preference = new SecurePreference(requireContext(),"SharedPref");

        ImageView profile = view.findViewById(R.id.profile_image);
        TextView username = view.findViewById(R.id.username);

        username.setText(preference.getString("username",false));
        Picasso.get().load(preference.getString("avatar",false)).into(profile);

        profile.setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.profile_circle));
        profile.setClipToOutline(true);
        return view;
    }
}