package com.example.task71p;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {
    private TextView tvType, tvName, tvPhone, tvDesc, tvDate, tvLocation;
    private Button btnRemove;
    private DBHelper db;
    private int itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(android.R.style.Theme_Material_Light_NoActionBar);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvType     = findViewById(R.id.tvType);
        tvName     = findViewById(R.id.tvName);
        tvPhone    = findViewById(R.id.tvPhone);
        tvDesc     = findViewById(R.id.tvDesc);
        tvDate     = findViewById(R.id.tvDate);
        tvLocation = findViewById(R.id.tvLocation);
        btnRemove  = findViewById(R.id.btnRemove);
        db         = new DBHelper(this);

        itemId = getIntent().getIntExtra("itemId", -1);
        Item item = db.getItem(itemId);
        if (item != null) {
            tvType.setText(item.getPostType());
            tvName.setText(item.getPersonName());
            tvPhone.setText(item.getPhone());
            tvDesc.setText(item.getDescription());
            tvDate.setText(item.getDate());
            tvLocation.setText(item.getLocation());
        }

        btnRemove.setOnClickListener(v -> {
            db.deleteItem(itemId);
            Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
