package it.davidecalza.viaggiaplaygo;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class tracking_on extends WearableActivity {

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

        // Enables Always-on
        setAmbientEnabled();
    }
}
