package it.davidecalza.viaggiaplaygo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.wearable.activity.WearableActivity;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Math.toIntExact;
import static java.lang.Thread.sleep;

public class tracking_on extends WearableActivity {

    private long start_time;
    private TextView timer_txt;
    private long seconds;
    int minutes;
    Handler handler;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_on);
        ImageView im = findViewById(R.id.tracking_on_image);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            im.setImageResource(bundle.getInt("tracking_mode"));
        }

        timer_txt = findViewById(R.id.timer);
        timer_txt.setText("Timer 00:00");

        // Enables Always-on
        setAmbientEnabled();

        start_time = System.currentTimeMillis();

        handler = new Handler();
        handler.postDelayed(new Stopwatch(), 500);
    }
    private class Stopwatch implements Runnable {
        @SuppressLint("SetTextI18n")
        @Override
        public void run() {
            seconds = (System.currentTimeMillis() - start_time)/1000;
            if(seconds >= 60){
                minutes = toIntExact(seconds/60);
                seconds = seconds-(minutes*60);
            }
            String txt_sec = ""+seconds;
            String txt_min = ""+minutes;
            if(seconds<10){ txt_sec = "0"+seconds; }
            if(minutes<10){ txt_min = "0"+minutes; }

            timer_txt.setText("Timer " + txt_min + ":" + txt_sec);
            handler.postDelayed(this,500);
        }
    }
}
