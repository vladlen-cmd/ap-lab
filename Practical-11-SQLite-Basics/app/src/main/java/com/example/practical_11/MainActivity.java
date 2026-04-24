package com.example.practical_11;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper myDb;
    private EditText editName;
    private EditText editMarks;
    private EditText editId;
    private Button btnAdd;
    private Button btnView;
    private Button btnUpdate;
    private Button btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        myDb = new DatabaseHelper(this);

        editName = findViewById(R.id.editName);
        editMarks = findViewById(R.id.editMarks);
        editId = findViewById(R.id.editId);

        btnAdd = findViewById(R.id.btnAdd);
        btnView = findViewById(R.id.btnView);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        setupAddHandler();
        setupViewHandler();
        setupUpdateHandler();
        setupDeleteHandler();
    }

    private void setupAddHandler() {
        btnAdd.setOnClickListener(v -> {
            boolean isInserted = myDb.insertData(
                    editName.getText().toString().trim(),
                    editMarks.getText().toString().trim()
            );
            Toast.makeText(this, isInserted ? "Data Inserted" : "Insert Failed", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupViewHandler() {
        btnView.setOnClickListener(v -> {
            Cursor res = myDb.getAllData();
            if (res.getCount() == 0) {
                showMessage("Error", "No Data Found");
                res.close();
                return;
            }

            StringBuilder buffer = new StringBuilder();
            while (res.moveToNext()) {
                buffer.append("ID: ").append(res.getString(0)).append("\n");
                buffer.append("Name: ").append(res.getString(1)).append("\n");
                buffer.append("Marks: ").append(res.getString(2)).append("\n\n");
            }
            res.close();
            showMessage("Student Data", buffer.toString());
        });
    }

    private void setupUpdateHandler() {
        btnUpdate.setOnClickListener(v -> {
            boolean isUpdated = myDb.updateData(
                    editId.getText().toString().trim(),
                    editName.getText().toString().trim(),
                    editMarks.getText().toString().trim()
            );
            Toast.makeText(this, isUpdated ? "Data Updated" : "Update Failed", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupDeleteHandler() {
        btnDelete.setOnClickListener(v -> {
            int deletedRows = myDb.deleteData(editId.getText().toString().trim());
            Toast.makeText(this, deletedRows > 0 ? "Data Deleted" : "ID Not Found", Toast.LENGTH_SHORT).show();
        });
    }

    private void showMessage(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
}