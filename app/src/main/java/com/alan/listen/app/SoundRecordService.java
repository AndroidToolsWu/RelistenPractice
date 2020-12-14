package com.alan.listen.app;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * @author Created by Alan
 * @date 2020/12/14
 */
public class SoundRecordService extends Service {

    private static int mRecordLoop = 8000;
    private ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        loopRecord();
        return START_STICKY;
    }

    private void loopRecord() {
        SoundRecordManager.getInstance().startSoundRecord();
        Disposable disposable = Observable.interval(mRecordLoop, mRecordLoop, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.from(mExecutor))
                .observeOn(Schedulers.from(mExecutor))
                .subscribe(count -> {
                    Log.d("haha", "loopRecord -> count" + count);
                    SoundRecordManager.getInstance().endSoundRecord();
                    String soundPath = SoundRecordManager.getInstance().getLastSoundPath();
                    Thread.sleep(100);
                    SoundPlayManager.getInstance().playSound(soundPath);
                    Thread.sleep(100);
                    SoundRecordManager.getInstance().startSoundRecord();
                }, error -> {
                    Log.d("haha", "error:" + error.getMessage());
                    SoundRecordManager.getInstance().endSoundRecord();
                    Thread.sleep(100);
                    SoundRecordManager.getInstance().startSoundRecord();
                });
        mCompositeDisposable.add(disposable);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static int getRecordLoop() {
        return mRecordLoop;
    }

    public static void setRecordLoop(int mRecordLoop) {
        SoundRecordService.mRecordLoop = mRecordLoop;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
        SoundRecordManager.getInstance().endSoundRecord();
    }
}
