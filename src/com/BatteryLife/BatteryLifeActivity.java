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
    private TextView health;
    private TextView status;
    private TextView charged;
    private TextView voltage;
    private TextView temperature;
    private TextView technology;
    private TextView plugged;
    private BroadcastReceiver br;
    private IntentFilter intFilt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        health = (TextView) findViewById(R.id.textView10);
        status = (TextView) findViewById(R.id.textView11);
        charged = (TextView) findViewById(R.id.textView12);
        voltage = (TextView) findViewById(R.id.textView13);
        temperature = (TextView) findViewById(R.id.textView14);
        technology = (TextView) findViewById(R.id.textView15);
        plugged = (TextView) findViewById(R.id.textView16);

        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                health.setText(intent.getStringExtra("Health"));
                status.setText(intent.getStringExtra("Status"));
                charged.setText(Integer.toString(intent.getIntExtra("Charged", 0)) + " %");
                voltage.setText(Float.toString(intent.getFloatExtra("Voltage", 0)) + " V");
                temperature.setText(Float.toString(intent.getFloatExtra("Temperature", 0) / 10) + " C");
                technology.setText(intent.getStringExtra("Technology"));
                plugged.setText(intent.getStringExtra("Plagged"));
            }
        };
        intFilt = new IntentFilter(BROADCAST_ACTION);
        registerReceiver(br, intFilt);
        startService(new Intent(this, BatteryService.class));
    }

    public void statisticClick(View view){
        Intent powerIntent = new Intent(Intent.ACTION_POWER_USAGE_SUMMARY);
        startActivity(powerIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putString("batteryInfo", batinfo);
        //outState.putInt("ChargerPct", chargePct);
    }

    /*protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        registerReceiver(br, intFilt);
        batinfo = savedInstanceState.getString("batteryInfo");
        chargePct = savedInstanceState.getInt("ChargerPct");
        text1.setText(batinfo);
        //text1.setText(chargePct);
    }*/
}
