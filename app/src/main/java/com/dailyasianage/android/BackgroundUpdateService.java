package com.dailyasianage.android;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by ripon on 3/1/2017.
 */

public class BackgroundUpdateService extends Service {

Handler handler1;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("BS", "service working good");
                Toast.makeText(BackgroundUpdateService.this, "service working good", Toast.LENGTH_SHORT).show();
                handler1.postDelayed(this, 60000);
            }
        }, 60000);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent intent = new Intent(getApplicationContext(), this.getClass());
        intent.setPackage(getPackageName());
        startService(intent);
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
