package com.BatteryLife;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class BatteryLifeActivity extends Activity {

    public final static String BROADCAST_ACTION = "com.BatteryLife";
    private TextView text;
    private TextView text1;
    private BroadcastReceiver br;
    private String batinfo;
    private int chargePct;
    private IntentFilter intFilt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        text = (TextView) findViewById(R.id.textView);
        text1 = (TextView) findViewById(R.id.textView2);
        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                batinfo = intent.getStringExtra("BatteryInfo");
                chargePct = intent.getIntExtra("ChargedPct",0);
                text.setText(batinfo);
                //text1.setText(chargePct + " %");
            }
        };
        intFilt = new IntentFilter(BROADCAST_ACTION);
        registerReceiver(br, intFilt);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("batteryInfo", batinfo);
        //outState.putInt("ChargerPct", chargePct);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        registerReceiver(br, intFilt);
        batinfo = savedInstanceState.getString("batteryInfo");
        chargePct = savedInstanceState.getInt("ChargerPct");
        text.setText(batinfo);
        //text1.setText(chargePct);
    }

    public void startClick(View view) {
        startService(new Intent(this, BatteryService.class));
    }
}
