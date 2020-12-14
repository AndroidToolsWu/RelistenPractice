package com.alan.listen.app;

import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * @author Created by Alan
 * @date 2020/12/14
 */
class SoundRecordManager {

    private static SoundRecordManager instance = new SoundRecordManager();

    private String mSavePath = Environment.getExternalStorageDirectory() + File.separator + "ReListenPractice" + File.separator;
    private MediaRecorder mMediaRecorder = new MediaRecorder();
    private String mLastSoundPath = "";

    public static SoundRecordManager getInstance() {
        return instance;
    }

    SoundRecordManager() {
        initSoundSavePath();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void initSoundSavePath() {
        Log.d("haha", "path:" + mSavePath);
        File file = new File(mSavePath);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    public void startSoundRecord() {
        Log.d("haha", "startSoundRecord");
        try {
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mLastSoundPath = generateSoundPath();
            mMediaRecorder.setOutputFile(mLastSoundPath);
            mMediaRecorder.prepare();
            mMediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private String generateSoundPath() {
        String filePath = mSavePath + System.currentTimeMillis();
        File file = new File(filePath);
        try {
            if (!file.exists()) file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePath;
    }

    public void endSoundRecord() {
        Log.d("haha", "endSoundRecord");
        mMediaRecorder.stop();
        mMediaRecorder.reset();
    }

    /**
     * 在不再使用此功能是释放掉
     */
    public void releaseSoundRecord() {
        mMediaRecorder.release();
    }

    public String getLastSoundPath() {
        return mLastSoundPath;
    }
}
