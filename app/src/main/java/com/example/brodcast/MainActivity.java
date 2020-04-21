package com.example.brodcast;


import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private BroadcastReceiver receiver;
    private TextView batteryInfo;
    private TextView networkInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        batteryInfo = findViewById(R.id.tv_battery);
        networkInfo = findViewById(R.id.tv_network_stat);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent == null){
                    return;
                }

                if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())){

                    networkInfo.setText(String.format("WIFI state : %s ", isNetworkAvailable()));

                } else if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())){
                    int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                    int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                    boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                            status == BatteryManager.BATTERY_STATUS_FULL;

                    if (batteryInfo != null) {
                        batteryInfo.setText(String.format("Battery life : %s . is Charging : %s", level, isCharging));
                    }
                }

            }
        };

        IntentFilter filter = new IntentFilter();

        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);

        registerReceiver(receiver, filter);
    }


    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
