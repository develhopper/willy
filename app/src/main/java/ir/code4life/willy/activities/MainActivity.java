package ir.code4life.willy.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import ir.code4life.willy.R;
import ir.code4life.willy.activities.fragments.BoardsFragment;
import ir.code4life.willy.activities.fragments.ProfileFragment;
import ir.code4life.willy.activities.fragments.DownloadsFragment;
import ir.code4life.willy.services.DownloadService;
import ir.code4life.willy.services.SyncService;
import ir.code4life.willy.util.G;

public class MainActivity extends AppCompatActivity {

    private Fragment selectedFragment;
    private Intent syncService,downloadService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        G.checkStoragePermission(this);
        initViews();
        initServices();
    }

    @Override
    protected void onDestroy() {
        stopService(syncService);
        stopService(downloadService);
        super.onDestroy();
    }

    private void initServices() {
        syncService = new Intent(this, SyncService.class);
        startService(syncService);
        downloadService = new Intent(this, DownloadService.class);
        startService(downloadService);
    }

    private void initViews() {
        selectedFragment = BoardsFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_fragment, selectedFragment);
        transaction.commit();

        setSupportActionBar(findViewById(R.id.toolbar));

        BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.nav_boards)
                    selectedFragment = BoardsFragment.newInstance();
                if(item.getItemId() == R.id.nav_profile)
                    selectedFragment = new ProfileFragment();
                if(item.getItemId() == R.id.downloads)
                    selectedFragment = DownloadsFragment.newInstance();

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.addToBackStack(null);
                transaction.replace(R.id.main_fragment,selectedFragment);
                transaction.commit();

                return true;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == G.PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Application needs Storage permission for downloading wallpapers");
            builder.setCancelable(true);

            builder.setPositiveButton("OK", (dialogInterface, i) -> {
                dialogInterface.cancel();
            });

            AlertDialog alert11 = builder.create();
            alert11.show();
        }
    }
}