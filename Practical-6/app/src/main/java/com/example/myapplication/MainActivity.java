package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private MyReceiver myReceiver;
    private BroadcastReceiver customReceiver;
    private TextView tvStatus;
    private TextView tvCustomStatus;

    private static final String CUSTOM_ACTION = "com.example.myapplication.CUSTOM_BROADCAST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvStatus = findViewById(R.id.tvStatus);
        tvCustomStatus = findViewById(R.id.tvCustomStatus);
        Button btnSendCustom = findViewById(R.id.btnSendCustomBroadcast);

        // Register system broadcast receiver for power events
        myReceiver = new MyReceiver();
        IntentFilter powerFilter = new IntentFilter();
        powerFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        powerFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        registerReceiver(myReceiver, powerFilter);

        // Register custom broadcast receiver
        customReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                tvCustomStatus.setText("Custom Broadcast: Received!");
                Toast.makeText(context, "Custom Broadcast Received!", Toast.LENGTH_SHORT).show();
            }
        };
        registerReceiver(customReceiver, new IntentFilter(CUSTOM_ACTION),
                Context.RECEIVER_NOT_EXPORTED);

        // Button to send custom broadcast
        btnSendCustom.setOnClickListener(v -> {
            Intent intent = new Intent(CUSTOM_ACTION);
            intent.setPackage(getPackageName());
            sendBroadcast(intent);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myReceiver != null) {
            unregisterReceiver(myReceiver);
        }
        if (customReceiver != null) {
            unregisterReceiver(customReceiver);
        }
    }
}