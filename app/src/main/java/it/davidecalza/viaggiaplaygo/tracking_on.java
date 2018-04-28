package it.davidecalza.viaggiaplaygo;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import static java.lang.Math.toIntExact;

public class tracking_on extends WearableActivity {

    private long start_time;
    private TextView timer_txt, distance_txt;
    int minutes;
    Handler handler;
    private GoogleApiClient api;
    private String mode;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_on);
        ImageView im = findViewById(R.id.tracking_on_image);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            im.setImageResource(bundle.getInt("tracking_bg"));
            mode = bundle.getString("tracking_mode");
        }
        if(api == null){
            api = new GoogleApiClient.Builder(this.getApplicationContext()).addApi(Wearable.API).build();
        }
        if(!api.isConnected()){
            api.connect();
        }

        timer_txt = findViewById(R.id.timer);
        timer_txt.setText("Timer 00:00");

        distance_txt = findViewById(R.id.distance);
        distance_txt.setText("Km 0");

        // Enables Always-on
        setAmbientEnabled();

        start_time = System.currentTimeMillis();

        handler = new Handler();
        handler.postDelayed(new Stopwatch(), 500);
        handler.postDelayed(new GetDistance(), 5000);

        // Broadcast Receiver
        IntentFilter filter = new IntentFilter(Intent.ACTION_SEND);
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String msg = intent.getStringExtra("WearMessage");
                Log.i("WearService", "tracking_on: " + msg);
                String sender = msg.split("_")[0];
                String activity = msg.split("_")[1];
                String mode = msg.split("_")[2];
                String value = msg.split("_")[3];

                if(sender.equals("smartphone") && activity.equals("getdistance") && !value.equals("err")){
                    DecimalFormat df = new DecimalFormat("###.#");
                    df.setRoundingMode(RoundingMode.CEILING);
                    Double km = Double.parseDouble(value);
                    distance_txt.setText("Km " + df.format(km));
                }
            }
        };
        LocalBroadcastManager.getInstance(this.getApplicationContext()).registerReceiver(receiver, filter);
    }

    private class Stopwatch implements Runnable {
        @SuppressLint("SetTextI18n")
        @Override
        public void run() {
            long seconds = (System.currentTimeMillis() - start_time) / 1000;
            if(seconds >= 60){
                minutes = toIntExact(seconds /60);
                seconds = seconds -(minutes*60);
            }
            String txt_sec = ""+ seconds;
            String txt_min = ""+minutes;
            if(seconds <10){ txt_sec = "0"+ seconds; }
            if(minutes<10){ txt_min = "0"+minutes; }

            timer_txt.setText("Timer " + txt_min + ":" + txt_sec);
            handler.postDelayed(this,500);
        }
    }

    private class GetDistance implements Runnable {
        @Override
        public void run() {
            (new Thread(new MessageRunnable("wearable_getdistance_"+mode))).start();
            handler.postDelayed(this,5000);
        }
    }

    private class MessageRunnable implements Runnable{
        private String message;
        MessageRunnable(String msg){
            message = msg;
        }

        @Override
        public void run(){
            NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(api).await();

            for(Node node : nodes.getNodes()) {
                Wearable.MessageApi.sendMessage(api, node.getId(), message, message.getBytes()).setResultCallback(new ResultCallback<MessageApi.SendMessageResult>() {
                    @Override
                    public void onResult(@NonNull MessageApi.SendMessageResult sendMessageResult) {
                        if(sendMessageResult.getStatus().isSuccess()){
                            Log.d("WearService", "Sent: " + message);
                        }
                    }
                });
            }
        }
    }
}
