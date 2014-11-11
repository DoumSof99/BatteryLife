package com.BatteryLife;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.IBinder;

/**
 * Created by Администратор on 31.10.2014.
 */
public class BatteryService extends Service {

    private SoundBox box;
    private BroadcastReceiver receiver;
    private int status;
    private String batteryInfo;

    @Override
    public void onCreate() {
        super.onCreate();
        box = new SoundBox(this);
        initReceiver();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        registerReceiver(receiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    public void initReceiver() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                int healt = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);
                float voltage = (float) intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
                float temperature = (float) intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
                boolean present = intent.getBooleanExtra(BatteryManager.EXTRA_PRESENT, false);
                status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                String technology = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);

                String health = "Not reported";
                switch (healt) {
                    case BatteryManager.BATTERY_HEALTH_DEAD:
                        health = "Dead";
                        break;
                    case BatteryManager.BATTERY_HEALTH_GOOD:
                        health = "Good";
                        break;
                    case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                        health = "Over voltage";
                        break;
                    case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                        health = "Unknown";
                        break;
                }
                String batteryStatus = "Not reported";
                switch (status) {
                    case BatteryManager.BATTERY_STATUS_CHARGING:
                        batteryStatus = "Charging";
                        break;
                    case BatteryManager.BATTERY_STATUS_DISCHARGING:
                        batteryStatus = "Discharging";
                        break;
                    case BatteryManager.BATTERY_STATUS_FULL:
                        batteryStatus = "Full";
                        break;
                    case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                        batteryStatus = "Not charging";
                        break;
                    case BatteryManager.BATTERY_STATUS_UNKNOWN:
                        batteryStatus = "Unknown";
                        break;
                }

                int chargedPct = (level * 100) / scale;

                batteryInfo = "Battery Info:" + "\nHealth: " + health +
                        "\nStatus: " + batteryStatus +
                        "\nCharged: " + chargedPct + "%" +
                        "\nVoltage: " + Float.toString(voltage) +
                        "\nTemperature: " + Float.toString(temperature / 10) + "C" +
                        "\nTechnology: " + technology +
                        "\nBattery present: " + Boolean.toString(present) + "\n";

                Intent infoIntent = new Intent(BatteryLifeActivity.BROADCAST_ACTION);
                infoIntent.putExtra("BatteryInfo", batteryInfo);
                infoIntent.putExtra("ChargedPct", chargedPct);
                sendBroadcast(infoIntent);
                box.playStatusMode(status, chargedPct);
            }
        };
    }
}
