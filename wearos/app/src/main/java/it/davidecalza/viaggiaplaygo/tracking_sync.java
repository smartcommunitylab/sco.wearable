package it.davidecalza.viaggiaplaygo;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class tracking_sync extends WearableActivity {
    private TextView sync_txt;
    private String mode;
    private GoogleApiClient api;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_sync);
        sync_txt = findViewById(R.id.tracking_sync_txt);
        ImageView im = findViewById(R.id.tracking_sync_image);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            im.setImageResource(bundle.getInt("tracking_bg"));
            mode = bundle.getString("tracking_mode");
        }
        sync_txt.setText("Attendi,\nsincronizzazione\nin corso");

        // Enables Always-on
        setAmbientEnabled();

        // Broadcast Receiver
        IntentFilter filter = new IntentFilter(Intent.ACTION_SEND);
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String msg = intent.getStringExtra("WearMessage");
                Log.i("WearService", "tracking_on: " + msg);
                String sender = msg.split("_")[0];
                String activity = msg.split("_")[1];
                String value = msg.split("_")[2];
                String err_msg = msg.split("_")[3];
                if(sender.equals("smartphone") && activity.equals("sync")){
                    if(value.equals("ok")){
                        Intent sync = new Intent(tracking_sync.this, tracking_start.class);
                        tracking_sync.this.startActivity(sync);
                    }
                    else{
                        sync_txt.setText("Errore:\n"+err_msg);
                    }
                }
            }
        };
        LocalBroadcastManager.getInstance(this.getApplicationContext()).registerReceiver(receiver, filter);
    }
}
