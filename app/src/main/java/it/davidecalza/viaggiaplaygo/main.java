package it.davidecalza.viaggiaplaygo;

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
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

public class main extends WearableActivity {
    Handler handler;
    private GoogleApiClient api;
    private boolean logged = false;
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

        // Enables Always-on
        setAmbientEnabled();

        handler = new Handler();
        handler.postDelayed(new CheckLoginStatus(), 3000);

        // Broadcast Receiver
        IntentFilter filter = new IntentFilter(Intent.ACTION_SEND);
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String msg = intent.getStringExtra("WearMessage");
                Log.i("WearService", "tracking_on: " + msg);
                String sender = msg.split("_")[0];
                String activity = msg.split("_")[1];
                String status = msg.split("_")[2];
                String value = msg.split("_")[3];

                if(sender.equals("smartphone") && activity.equals("logstatus") && !logged){
                    if(status.equals("ok")){
                        logged = true;
                        Intent start = new Intent(main.this, tracking_start.class);
                        main.this.startActivity(start);
                    }
                    else if (value.equals("notlogged")){
                        Toast.makeText(getApplicationContext(), "You must login on your phone first", Toast.LENGTH_LONG).show();
                    }
                }
            }
        };
        LocalBroadcastManager.getInstance(this.getApplicationContext()).registerReceiver(receiver, filter);
    }

    private class CheckLoginStatus implements Runnable{
        @Override
        public void run(){
            (new Thread(new MessageRunnable("wearable_logstatus_null"))).start();
            handler.postDelayed(this, 3000);
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
