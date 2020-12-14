package com.alan.listen.app;

import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

import static com.alan.listen.app.HeadsetReceiver.getHeadsetState;

/**
 * @author Created by Alan
 * @date 2020/12/14
 */
class SoundPlayManager {
    private static SoundPlayManager instance = new SoundPlayManager();
    private MediaPlayer mMediaPlayer = new MediaPlayer();
    private boolean isStart = false;

    static SoundPlayManager getInstance() {
        return instance;
    }

    SoundPlayManager() { }

    public void playSound(String playSource) {
        if (getHeadsetState() == 0 || isStart) {
            Log.d("haha", "耳机未插入");
            return;
        }
        try {
            mMediaPlayer.setDataSource(playSource);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
            mMediaPlayer.setOnCompletionListener(mListener);
            isStart = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    MediaPlayer.OnCompletionListener mListener = mp -> {
        if (!isStart) return;
        mMediaPlayer.stop();
        mMediaPlayer.reset();
        isStart = false;
    };

    public void releaseSoundPlay() {
        mMediaPlayer.release();
    }
}
