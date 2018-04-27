package it.davidecalza.viaggiaplaygo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.SystemClock;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

public class wear_listener_service extends WearableListenerService {

    private GoogleApiClient _client;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartService = new Intent(getApplicationContext(), this.getClass());
        restartService.setPackage(getPackageName());
        PendingIntent restartServicePI = PendingIntent.getService(getApplicationContext(), 1, restartService,
                PendingIntent.FLAG_ONE_SHOT);

        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        assert alarmService != null;
        alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 100, restartServicePI);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (_client == null) {
            _client = new GoogleApiClient.Builder(this.getApplicationContext()).addApi(Wearable.API).build();
        }
        if (!_client.isConnected()) {
            _client.connect();
        }
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
        byte[] b = messageEvent.getData();
        String s = new String(b);

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra("WearMessage", s);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
