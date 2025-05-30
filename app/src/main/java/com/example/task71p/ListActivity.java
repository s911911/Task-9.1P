package com.example.task71p;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    private ListView lvItems;
    private DBHelper db;
    private ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(android.R.style.Theme_Material_Light_NoActionBar);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        lvItems = findViewById(R.id.lvItems);
        db      = new DBHelper(this);
        loadItems();

        lvItems.setOnItemClickListener((parent, view, position, id) -> {
            Item item = adapter.getItem(position);
            Intent intent = new Intent(ListActivity.this, RemoveItemActivity.class);
            intent.putExtra("itemId", item.getId());
            startActivity(intent);
        });
    }

    private void loadItems() {
        List<Item> items = db.getAllItems();
        adapter = new ItemAdapter(this, items);
        lvItems.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadItems();
    }
}
