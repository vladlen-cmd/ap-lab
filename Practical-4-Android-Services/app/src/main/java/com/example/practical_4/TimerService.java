package com.example.practical_4;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

public class TimerService extends Service {

    private int seconds = 0;
    private boolean running = false;
    private Thread timerThread;
    private Handler mainHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        mainHandler = new Handler(Looper.getMainLooper());
        Toast.makeText(this, "Service Created", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (running) {
            return START_STICKY;
        }

        running = true;
        seconds = 0;

        timerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (running && !Thread.currentThread().isInterrupted()) {
                    try {
                        Thread.sleep(1000);
                        seconds++;

                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                        "Timer: " + seconds + " seconds",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        });

        timerThread.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        running = false;

        if (timerThread != null) {
            timerThread.interrupt();
            timerThread = null;
        }

        Toast.makeText(this, "Service Stopped", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
