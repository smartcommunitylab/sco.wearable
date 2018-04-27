package it.davidecalza.viaggiaplaygo;

import android.annotation.SuppressLint;
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
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

public class tracking_start extends WearableActivity {

    private GoogleApiClient api;
    private BroadcastReceiver receiver;
    private String _syncPath = "/CordovaCommunication/Data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(api == null){
            api = new GoogleApiClient.Builder(this.getApplicationContext()).addApi(Wearable.API).build();
        }
        if(!api.isConnected()){
            api.connect();
        }

        ImageButton btn_bike = (ImageButton) findViewById(R.id.btn_bike);
        ImageButton btn_walk = (ImageButton) findViewById(R.id.btn_walk);
        ImageButton btn_bus = (ImageButton) findViewById(R.id.btn_bus);
        ImageButton btn_train = (ImageButton) findViewById(R.id.btn_train);

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

        // Enables Always-on
        setAmbientEnabled();

        // Broadcast Receiver
        IntentFilter filter = new IntentFilter(Intent.ACTION_SEND);
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @SuppressLint("ShowToast")
            @Override
            public void onReceive(Context context, Intent intent) {
                String msg = intent.getStringExtra("WearMessage");
                Log.i("WearService", "Received: " + msg);
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
