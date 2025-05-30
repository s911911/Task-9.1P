package com.example.task71p;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button btnCreate, btnShow, btnShowMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(android.R.style.Theme_Material_Light_NoActionBar);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCreate = findViewById(R.id.btnCreateAdvert);
        btnShow   = findViewById(R.id.btnShowItems);
        btnShowMap  = findViewById(R.id.btnShowMap);

        btnCreate.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, CreateAdvertActivity.class));
        });

        btnShow.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ListActivity.class));
        });

        btnShowMap.setOnClickListener(v ->
                startActivity(new Intent(this, MapActivity.class))
        );
    }
}

