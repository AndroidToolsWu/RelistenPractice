package com.alan.listen.app;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

/**
 * @author Created by Alan
 * @date 2020/12/14
 */
public class HeadsetReceiver extends BroadcastReceiver {

    private static volatile int headsetState = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        String wiredCommand = Intent.ACTION_HEADSET_PLUG;
        String bluetoothCommand = BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED;

        if (TextUtils.equals(wiredCommand, intent.getAction())) {
            headsetState = intent.getIntExtra("state", 0);
        } else if (TextUtils.equals(bluetoothCommand, intent.getAction())) {
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
            headsetState = adapter.getProfileConnectionState(BluetoothProfile.HEADSET);
        }

        if (headsetState == 0) {
            Log.d("haha", "耳机未插入");
            Toast.makeText(context, "耳机未插入", Toast.LENGTH_SHORT).show();
        }
        if (headsetState == 1) {
            Log.d("haha", "检测到耳机插入");
            Toast.makeText(context, "检测到耳机插入", Toast.LENGTH_SHORT).show();
        }
    }

    public static int getHeadsetState() {
        return headsetState;
    }
}
