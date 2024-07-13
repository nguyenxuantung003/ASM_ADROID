package com.example.myapplication.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {
    private CardView cardView;
    private BottomNavigationView menuHome;
    @SuppressLint({"MissingInflatedId", "WrongConstant"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        menuHome = findViewById(R.id.bottom_navigation_home);
        cardView = findViewById(R.id.carview1);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this,"Chon",Toast.LENGTH_SHORT).show();
            }
        });



        menuHome.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);


        menuHome.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menu_home_btn1) {
                    // Handle home click
                    Toast.makeText(HomeActivity.this, "Home", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (item.getItemId() == R.id.menu_home_btn2) {
                    // Handle search click
                    Toast.makeText(HomeActivity.this, "Search", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (item.getItemId() == R.id.menu_home_btn3) {
                    // Handle health click
                    Toast.makeText(HomeActivity.this, "Health", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (item.getItemId() == R.id.menu_home_btn4) {
                    // Handle profile click
                    Toast.makeText(HomeActivity.this, "Profile", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });
    }
}