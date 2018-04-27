package it.davidecalza.viaggiaplaygo;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;

public class tracking_on extends WearableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_on);

        // Enables Always-on
        setAmbientEnabled();
    }
}
