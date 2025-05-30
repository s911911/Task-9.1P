package com.example.task71p;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RemoveItemActivity extends AppCompatActivity {
    private TextView tvTitle, tvDate, tvLocation;
    private Button btnRemove;
    private DBHelper db;
    private int itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_item);

        tvTitle    = findViewById(R.id.tvRemoveTitle);
        tvDate     = findViewById(R.id.tvRemoveDate);
        tvLocation = findViewById(R.id.tvRemoveLocation);
        btnRemove  = findViewById(R.id.btnRemoveItem);
        db         = new DBHelper(this);


        itemId = getIntent().getIntExtra("itemId", -1);
        Item item = db.getItem(itemId);
        if (item != null) {
            tvTitle.setText(item.getPostType() + " " + item.getDescription());
            tvDate.setText(item.getDate());
            tvLocation.setText(item.getLocation());
        }

        btnRemove.setOnClickListener(v -> {
            db.deleteItem(itemId);
            Toast.makeText(this, getString(R.string.toast_delete), Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
