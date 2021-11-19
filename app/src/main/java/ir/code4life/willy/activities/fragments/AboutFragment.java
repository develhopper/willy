package ir.code4life.willy.activities.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import ir.code4life.willy.R;

public class AboutFragment extends Fragment implements View.OnClickListener {

    public AboutFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_about, container, false);

        initViews(view);
        return view;
    }

    private void initViews(View view) {
        ImageButton github,myket,mail,pinterest;
        github = view.findViewById(R.id.about_github);
        myket = view.findViewById(R.id.about_myket);
        mail = view.findViewById(R.id.about_mail);
        pinterest = view.findViewById(R.id.about_pinterest);

        github.setOnClickListener(this);
        myket.setOnClickListener(this);
        mail.setOnClickListener(this);
        pinterest.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.about_github){
            open_browser("https://github.com/develhopper/willy");
        }
        if(view.getId() == R.id.about_mail){
            Uri uri = Uri.parse("mailto:" + "alireza.tjd77@gmail.com")
                    .buildUpon()
                    .appendQueryParameter("subject", "subject")
                    .appendQueryParameter("body", "body")
                    .build();

            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, uri);
            startActivity(Intent.createChooser(emailIntent, "Send email"));
        }

        if(view.getId() == R.id.about_myket){
            open_browser("myket://details?id="+requireContext().getPackageName());
        }

        if(view.getId() == R.id.about_pinterest){
            open_browser("https://pinterest.com/develhoper");
        }
    }

    public void open_browser(String url){
        try{
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        }catch (Exception ignore){
            Toast.makeText(requireContext(), "Not found", Toast.LENGTH_SHORT).show();
        }
    }
}