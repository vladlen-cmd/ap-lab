package com.example.practical_12;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private DBHelper db;
    private EditText editName;
    private Button btnInsert;
    private Button btnView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DBHelper(this);
        editName = findViewById(R.id.editName);
        btnInsert = findViewById(R.id.btnInsert);
        btnView = findViewById(R.id.btnView);

        // Insert Data
        btnInsert.setOnClickListener(v -> {
            String name = editName.getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean isInserted = db.insertData(name);
            if (isInserted) {
                Toast.makeText(this, "Data Inserted", Toast.LENGTH_SHORT).show();
                editName.setText("");
            } else {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

        // View Data
        btnView.setOnClickListener(v -> {
            Cursor cursor = db.getData();
            if (cursor.getCount() == 0) {
                Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
                cursor.close();
                return;
            }

            StringBuilder buffer = new StringBuilder();
            while (cursor.moveToNext()) {
                buffer.append("ID: ").append(cursor.getInt(0)).append("\n");
                buffer.append("Name: ").append(cursor.getString(1)).append("\n\n");
            }
            cursor.close();

            Toast.makeText(this, buffer.toString(), Toast.LENGTH_LONG).show();
        });
    }
}