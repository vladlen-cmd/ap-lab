package com.example.practical_8;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private Button btnDialog;
    private Button btnDate;
    private Button btnTime;
    private TextView txtResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDialog = findViewById(R.id.btnDialog);
        btnDate = findViewById(R.id.btnDate);
        btnTime = findViewById(R.id.btnTime);
        txtResult = findViewById(R.id.txtResult);

        btnDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Confirmation");
                builder.setMessage("Do you want to continue?");
                builder.setPositiveButton("Yes", (dialog, which) ->
                        Toast.makeText(MainActivity.this, "You clicked YES", Toast.LENGTH_SHORT).show());
                builder.setNegativeButton("No", (dialog, which) ->
                        Toast.makeText(MainActivity.this, "You clicked NO", Toast.LENGTH_SHORT).show());
                builder.show();
            }
        });

        btnDate.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    MainActivity.this,
                    (datePicker, selectedYear, selectedMonth, selectedDay) ->
                            txtResult.setText("Selected Date: " + selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear),
                    year,
                    month,
                    day
            );
            datePickerDialog.show();
        });

        btnTime.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    MainActivity.this,
                    (timePicker, selectedHour, selectedMinute) ->
                            txtResult.setText("Selected Time: " + selectedHour + ":" + selectedMinute),
                    hour,
                    minute,
                    true
            );
            timePickerDialog.show();
        });
    }
}