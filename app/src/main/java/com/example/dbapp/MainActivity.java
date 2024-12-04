package com.example.dbapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ArrayList<Book> bookArrayList;
    private RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        bookArrayList = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.list_book);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapter(this, bookArrayList);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab1 = findViewById(R.id.fab_add_book);
        FloatingActionButton fab2 = findViewById(R.id.fab_edit_book);
        FloatingActionButton fab3 = findViewById(R.id.fab_delete_book);

        fab1.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AddActivity.class)));
        fab2.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, EditActivity.class)));
        fab3.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, DeleteActivity.class)));

        loadBooks();
    }


    private void loadBooks() {
        bookArrayList.clear();
        Cursor cursor = dbHelper.getAllBooks();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME));
                String author = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AUTHOR));
                bookArrayList.add(new Book(id, name, author));
                Log.d("DB_LOG", "Загружена книга: ID=" + id + ", Name=" + name + ", Author=" + author);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        adapter.notifyDataSetChanged();
    }


    @Override
    protected void onResume()
    {
        super.onResume();
        loadBooks();
    }

}