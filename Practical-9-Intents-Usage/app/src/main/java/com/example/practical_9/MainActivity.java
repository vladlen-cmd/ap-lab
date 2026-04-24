package com.example.practical_9;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnExplicit = findViewById(R.id.btnExplicit);
        Button btnBrowser = findViewById(R.id.btnBrowser);
        Button btnDial = findViewById(R.id.btnDial);

        btnExplicit.setOnClickListener(v -> {
            Intent explicitIntent = new Intent(MainActivity.this, SecondActivity.class);
            startActivity(explicitIntent);
        });

        btnBrowser.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"));
            startActivity(browserIntent);
        });

        btnDial.setOnClickListener(v -> {
            Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:1234567890"));
            startActivity(dialIntent);
        });
    }
}