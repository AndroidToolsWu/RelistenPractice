package com.alan.listen.app;

import android.bluetooth.BluetoothHeadset;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.alan.listen.R;

public class MainActivity extends AppCompatActivity {

    private Button mStartBtn, mEndBtn;
    private HeadsetReceiver mHeadsetReceiver = new HeadsetReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        registerHeadsetReceiver();
    }

    private void initView() {
        mStartBtn = findViewById(R.id.btn_start_record);
        mEndBtn = findViewById(R.id.btn_end_record);
        mStartBtn.setOnClickListener(v -> requestPermission());
        mEndBtn.setOnClickListener(v -> endSoundRecord());
    }

    private void registerHeadsetReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_HEADSET_PLUG);
        filter.addAction(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED);
        registerReceiver(mHeadsetReceiver, filter);
    }

    private void requestPermission() {
        boolean allGrant = true;
        String[] permission = new String[] {"android.permission.RECORD_AUDIO"
                , "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};
        for (String s : permission) {
            if (ContextCompat.checkSelfPermission(this, s) != PackageManager.PERMISSION_GRANTED) {
                allGrant = false;
                ActivityCompat.requestPermissions(this, permission, 100);
            }
        }
        if (allGrant) startSoundRecord();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean allGrant = true;
        for (int grantResult : grantResults) {
            Log.d("haha", "grantResult:" + grantResult);
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                allGrant = false;
                break;
            }
        }
        if (allGrant) startSoundRecord();
    }

    private void startSoundRecord() {
        startService(new Intent(this, SoundRecordService.class));
    }

    private void endSoundRecord() {
        stopService(new Intent(this, SoundRecordService.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mHeadsetReceiver);
        SoundRecordManager.getInstance().releaseSoundRecord();
    }
}