package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private TextView txtResult;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editTextName);
        Button btnInsert = findViewById(R.id.btnInsert);
        Button btnShow = findViewById(R.id.btnShow);
        Button btnClear = findViewById(R.id.btnClear);
        txtResult = findViewById(R.id.txtResult);

        uri = MyProvider.CONTENT_URI;

        btnInsert.setOnClickListener(v -> insertName());
        btnShow.setOnClickListener(v -> showData());
        btnClear.setOnClickListener(v -> clearData());
    }

    private void insertName() {
        String input = editText.getText().toString().trim();
        if (input.isEmpty()) {
            Toast.makeText(this, "Enter a name first", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put("name", input);

        getContentResolver().insert(uri, values);
        editText.setText("");
        Toast.makeText(this, "Data inserted", Toast.LENGTH_SHORT).show();
    }

    private void showData() {
        try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
            if (cursor == null || cursor.getCount() == 0) {
                txtResult.setText("No data found");
                return;
            }

            StringBuilder builder = new StringBuilder();
            int nameIndex = cursor.getColumnIndex("name");
            while (cursor.moveToNext()) {
                if (nameIndex >= 0) {
                    builder.append(cursor.getString(nameIndex)).append("\n");
                }
            }
            txtResult.setText(builder.toString().trim());
        }
    }

    private void clearData() {
        int deleted = getContentResolver().delete(uri, null, null);
        txtResult.setText("Data cleared");
        Toast.makeText(this, "Deleted " + deleted + " items", Toast.LENGTH_SHORT).show();
    }
}
