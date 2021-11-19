package ir.code4life.willy.activities.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ir.code4life.willy.R;
import ir.code4life.willy.activities.MainActivity;
import ir.code4life.willy.activities.SplashActivity;
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
        Button logout = view.findViewById(R.id.logout_btn);

        logout.setOnClickListener(view1 -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(requireContext());

            dialog.setTitle("Logout?");
            dialog.setMessage("Are you sure you want to logout?");
            dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    logout(preference);
                }
            });
            dialog.setNegativeButton("Cancel",null);
            dialog.show();
        });

        username.setText(preference.getString("username",false));
        Picasso.get().load(preference.getString("avatar",false)).into(profile);

        profile.setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.profile_circle));
        profile.setClipToOutline(true);
        return view;
    }

    private void logout(SecurePreference preference){
        preference.remove("token");
        preference.remove("refresh_token");
        preference.remove("username");
        preference.remove("avatar");
        preference.apply();
        Intent intent = new Intent(requireContext(), SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        requireActivity().finish();
    }
}