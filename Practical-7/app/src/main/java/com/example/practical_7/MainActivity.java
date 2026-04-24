package com.example.practical_7;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editName;
    private CheckBox checkJava, checkAndroid;
    private RadioGroup radioGroup;
    private Button btnSubmit;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        editName = findViewById(R.id.editName);
        checkJava = findViewById(R.id.checkJava);
        checkAndroid = findViewById(R.id.checkAndroid);
        radioGroup = findViewById(R.id.radioGroup);
        btnSubmit = findViewById(R.id.btnSubmit);
        tvResult = findViewById(R.id.tvResults);

        // Set submit button click listener
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });
    }

    private void submitForm() {
        // Get name from EditText
        String name = editName.getText().toString().trim();

        // Validate name
        if (name.isEmpty()) {
            Toast.makeText(MainActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get selected skills from CheckBoxes
        StringBuilder skills = new StringBuilder();
        if (checkJava.isChecked()) {
            skills.append("Java ");
        }
        if (checkAndroid.isChecked()) {
            skills.append("Android");
        }

        // Validate skills selection
        if (skills.toString().trim().isEmpty()) {
            Toast.makeText(MainActivity.this, "Please select at least one skill", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get selected gender from RadioGroup
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(MainActivity.this, "Please select a gender", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton radioButton = findViewById(selectedId);
        String gender = radioButton.getText().toString();

        // Build result string
        String result = "Name: " + name +
                "\nSkills: " + skills.toString().trim() +
                "\nGender: " + gender;

        // Display result
        tvResult.setText(result);
        Toast.makeText(MainActivity.this, "Form submitted successfully!", Toast.LENGTH_SHORT).show();
    }
}