package com.example.rajawali3d_map;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.rajawali3d_map.fragments.Map3DFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container,new Map3DFragment())
                .commit();
    }
}