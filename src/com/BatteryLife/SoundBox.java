package com.BatteryLife;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.BatteryManager;

import java.io.IOException;

/**
 * Created by Администратор on 21.10.2014.
 */
public class SoundBox {

    private AssetManager mng;
    private boolean charging;
    private boolean soundFlag;

    public SoundBox(Context context) {
        mng = context.getAssets();
        playSound("start.mp3");
    }

    private void playSound(final String file) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                SoundPool soundPool;
                final int voice;
                AssetFileDescriptor afd = null;
                try {
                    afd = mng.openFd(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
                voice = soundPool.load(afd, 1);
                soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                        soundPool.play(voice, 1, 1, 1, 0, 1);
                    }
                });
            }
        });
        thread.start();
    }

    protected void playStatusMode(int status, int chargePct) {
        switch (status) {
            case BatteryManager.BATTERY_STATUS_CHARGING:
                if (charging == false) {
                    playSound("connect.mp3");
                    charging = true;
                }
                break;
            case BatteryManager.BATTERY_STATUS_DISCHARGING:
                if (charging == true) {
                    playSound("disconnect.mp3");
                    charging = false;
                }
                break;
        }

        switch (chargePct) {
            case 100:
                if (soundFlag == false) {
                    playSound("full.mp3");
                    soundFlag = true;
                }
                break;
            case 30:
                if (soundFlag == false) {
                    playSound("30%.mp3");
                    soundFlag = true;
                }
                break;
            case 15:
                if (soundFlag == false) {
                    playSound("15%.mp3");
                    soundFlag = true;
                }
                break;
            default:
                soundFlag = false;
        }
    }
}
