package com.example.finagotechcase1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.AdapterView;

import com.example.finagotechcase1.fragments.AssetListFragment;
import com.example.finagotechcase1.fragments.HomeFragment;
import com.example.finagotechcase1.fragments.MarketListFragment;
import com.example.finagotechcase1.models.Market;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment = new HomeFragment();
    AssetListFragment assetListFragment = new AssetListFragment();
    MarketListFragment marketListFragment = new MarketListFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavView);

        getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.home) {
                    // Replace fragment with the desired fragment for menu item 1
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, homeFragment)
                            .commit();
                } else if (itemId == R.id.assets) {
                    // Replace fragment with the desired fragment for menu item 2
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, assetListFragment)
                            .commit();
                } else if (itemId == R.id.markets) {
                    // Replace fragment with the desired fragment for menu item 3
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, marketListFragment)
                            .commit();
                }
                return false;
            }
        });
    }
}