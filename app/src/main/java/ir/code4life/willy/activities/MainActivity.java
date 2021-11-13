package ir.code4life.willy.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import ir.code4life.willy.R;
import ir.code4life.willy.activities.fragments.BoardsFragment;
import ir.code4life.willy.activities.fragments.ProfileFragment;
import ir.code4life.willy.activities.fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navigationView;
    private Fragment selectedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        selectedFragment = BoardsFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_fragment, selectedFragment);
        transaction.commit();

        navigationView = findViewById(R.id.navigation);
        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.nav_boards)
                    selectedFragment = BoardsFragment.newInstance();
                if(item.getItemId() == R.id.nav_profile)
                    selectedFragment = new ProfileFragment();
                if(item.getItemId() == R.id.nav_settings)
                    selectedFragment = SettingsFragment.newInstance();

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_fragment,selectedFragment);
                transaction.addToBackStack(null);
                transaction.commit();

                return true;
            }
        });
    }
}