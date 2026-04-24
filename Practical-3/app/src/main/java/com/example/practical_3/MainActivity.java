package com.example.practical_3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private void showLifecycleEvent(String methodName) {
        Log.d(TAG, methodName);
        Toast.makeText(this, TAG + " " + methodName, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showLifecycleEvent("onCreate");

        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        showLifecycleEvent("onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        showLifecycleEvent("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        showLifecycleEvent("onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        showLifecycleEvent("onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        showLifecycleEvent("onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        showLifecycleEvent("onDestroy");
    }
}