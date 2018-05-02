package it.davidecalza.viaggiaplaygo;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.wearable.activity.WearableActivity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

public class tracking_start extends WearableActivity {

    private GoogleApiClient api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_start);

        if(api == null){
            api = new GoogleApiClient.Builder(this.getApplicationContext()).addApi(Wearable.API).build();
        }
        if(!api.isConnected()){
            api.connect();
        }

        ImageButton btn_bike = findViewById(R.id.btn_bike);
        ImageButton btn_walk = findViewById(R.id.btn_walk);
        ImageButton btn_bus = findViewById(R.id.btn_bus);
        ImageButton btn_train = findViewById(R.id.btn_train);

        btn_bike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new Thread(new MessageRunnable("wearable_starttracking_bike"))).start();
            }
        });
        btn_bus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new Thread(new MessageRunnable("wearable_starttracking_bus"))).start();
            }
        });
        btn_train.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new Thread(new MessageRunnable("wearable_starttracking_train"))).start();
            }
        });
        btn_walk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new Thread(new MessageRunnable("wearable_starttracking_walk"))).start();
            }
        });

        final TextView username = findViewById(R.id.txt_username);
        final TextView points = findViewById(R.id.txt_points);
        final TextView ranking = findViewById(R.id.txt_ranking);

        (new Thread(new MessageRunnable("wearable_getinfo_username"))).start();
        (new Thread(new MessageRunnable("wearable_getinfo_points"))).start();
        (new Thread(new MessageRunnable("wearable_getinfo_ranking"))).start();

        // Enables Always-on
        setAmbientEnabled();

        // Broadcast Receiver
        IntentFilter filter = new IntentFilter(Intent.ACTION_SEND);
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String msg = intent.getStringExtra("WearMessage");
                Log.i("WearService", "tracking_start: " + msg);
                String sender = msg.split("_")[0];
                String activity = msg.split("_")[1];
                String mode = msg.split("_")[2];
                String value = msg.split("_")[3];

                if(sender.equals("smartphone") && activity.equals("starttracking") && value.equals("ok")){
                    Intent tracking = new Intent(tracking_start.this, tracking_on.class);
                    switch (mode){
                        case "bike":
                            tracking.putExtra("tracking_bg", R.drawable.bike_bg);
                            tracking.putExtra("tracking_mode", "bike");
                            break;
                        case "walk":
                            tracking.putExtra("tracking_bg", R.drawable.walk_bg);
                            tracking.putExtra("tracking_mode", "walk");
                            break;
                        case "bus":
                            tracking.putExtra("tracking_bg", R.drawable.bus_bg);
                            tracking.putExtra("tracking_mode", "bus");
                            break;
                        case "train":
                            tracking.putExtra("tracking_bg", R.drawable.train_bg);
                            tracking.putExtra("tracking_mode", "train");
                            break;
                        default: return;
                    }
                    tracking_start.this.startActivity(tracking);
                }
                if(sender.equals("smartphone") && activity.equals("getinfo") && !value.equals("err")){
                    switch (mode){
                        case "username": username.setText(value); break;
                        case "points": points.setText(value);     break;
                        case "ranking": ranking.setText(value);   break;
                        default: return;
                    }
                }
            }
        };
        LocalBroadcastManager.getInstance(this.getApplicationContext()).registerReceiver(receiver, filter);
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
