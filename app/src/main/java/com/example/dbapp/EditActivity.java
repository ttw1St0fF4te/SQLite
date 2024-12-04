package com.example.dbapp;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class EditActivity extends AppCompatActivity {

    private Spinner spinnerBookId;
    private EditText editTextBookName, editTextBookAuthor;
    private Button buttonUpdate;

    private DatabaseHelper dbHelper;
    private ArrayList<Integer> bookIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        spinnerBookId = findViewById(R.id.spinner_book_id);
        editTextBookName = findViewById(R.id.edit_text_book_name);
        editTextBookAuthor = findViewById(R.id.edit_text_book_author);
        buttonUpdate = findViewById(R.id.button_update);

        dbHelper = new DatabaseHelper(this);
        bookIds = new ArrayList<>();

        loadBookIds();

        spinnerBookId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int selectedBookId = bookIds.get(position);
                loadBookDetails(selectedBookId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        buttonUpdate.setOnClickListener(v -> updateBook());
    }

    private void loadBookIds() {
        Cursor cursor = dbHelper.getAllBooks();
        bookIds.clear();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
            bookIds.add(id);
        }
        cursor.close();

        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, bookIds);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBookId.setAdapter(adapter);
    }

    private void loadBookDetails(int bookId) {
        Cursor cursor = dbHelper.getAllBooks();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
            if (id == bookId) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME));
                String author = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AUTHOR));
                editTextBookName.setText(name);
                editTextBookAuthor.setText(author);
                break;
            }
        }
        cursor.close();
    }

    private void updateBook() {
        int selectedBookId = bookIds.get(spinnerBookId.getSelectedItemPosition());
        String updatedName = editTextBookName.getText().toString().trim();
        String updatedAuthor = editTextBookAuthor.getText().toString().trim();

        if (updatedName.isEmpty() || updatedAuthor.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        int result = dbHelper.updateBook(selectedBookId, updatedName, updatedAuthor);

        if (result > 0) {
            Toast.makeText(this, "Книга успешно обновлена", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Ошибка обновления", Toast.LENGTH_SHORT).show();
        }
    }
}
