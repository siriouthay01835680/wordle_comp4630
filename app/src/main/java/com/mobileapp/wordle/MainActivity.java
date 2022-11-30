package com.mobileapp.wordle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.Menu;

import com.google.android.material.appbar.MaterialToolbar;

public class MainActivity extends AppCompatActivity {

    MaterialToolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavHostFragment navHostFragment = (NavHostFragment)
                getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment)
                ;
        NavController navController = navHostFragment.getNavController();
        AppBarConfiguration.Builder builder = new
                AppBarConfiguration.Builder(navController.getGraph());
        AppBarConfiguration appBarConfiguration = builder.build();
        NavigationUI.setupWithNavController(toolbar, navController,
                appBarConfiguration);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }
}