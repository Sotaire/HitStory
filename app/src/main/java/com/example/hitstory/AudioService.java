package com.example.hitstory;

import android.app.Service;
import android.content.Intent;
import android.media.MediaMetadata;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.hitstory.data.models.MediaMetaData;
import com.example.hitstory.ui.detail.DetailActivity;
import com.example.hitstory.ui.songs.SongsListFragment;

import java.io.IOException;
import java.util.ArrayList;

public class AudioService extends Service implements MediaPlayer.OnCompletionListener {

    IBinder binder = new MyBinder();
    MediaPlayer mediaPlayer;
    ArrayList<MediaMetaData> musics = new ArrayList<>();
    ActionPlaying actionPlaying;
    int position;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("Bind", "Method");
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String actionName = null;
        if (intent != null) {
            int myPosition = intent.getIntExtra(DetailActivity.POSITION, -1);
            actionName = intent.getStringExtra("ActionName");
            if (myPosition != -1) {
                playMedia(myPosition);
            }
        }
        if (actionName != null){
            switch (actionName){
                case "playPause":
                    if (actionPlaying != null){
                        actionPlaying.playPauseBtnClicked();
                    }
                    break;
                case "next":
                    if (actionPlaying != null){
                        actionPlaying.nextBtnClicked();
                    }
                    break;
                case "previous":
                    if (actionPlaying != null){
                        actionPlaying.previousBtnClicked();
                    }
                    break;
            }
        }
        return START_STICKY;
    }

    private void playMedia(int startPosition) {
        musics = SongsListFragment.data;
        position = startPosition;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            if (musics != null) {
                createMediaPlayer(position);
                try {
                    mediaPlayer.setDataSource(musics.get(position).getMediaUrl());
                    mediaPlayer.prepareAsync();
                    mediaPlayer.setOnPreparedListener(mediaPlayer -> {
                        mediaPlayer.start();
                        onCompleted();
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            createMediaPlayer(position);
            try {
                mediaPlayer.setDataSource(musics.get(position).getMediaUrl());
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(mediaPlayer -> {
                    mediaPlayer.start();
                    onCompleted();
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setOnPreparedListener(MediaPlayer.OnPreparedListener onPreparedListener) {
        mediaPlayer.setOnPreparedListener(onPreparedListener);
    }

    public void setAudioStreamType(int streamMusic) {
        mediaPlayer.setAudioStreamType(streamMusic);
    }

    public void prepareAsync() {
        mediaPlayer.prepareAsync();
    }


    public class MyBinder extends Binder {
        public AudioService getService() {
            return AudioService.this;
        }
    }

    public void start() {
        mediaPlayer.start();
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public void stop() {
        mediaPlayer.stop();
    }

    public void release() {
        mediaPlayer.release();
    }

    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    public void seekTo(int pos) {
        mediaPlayer.seekTo(pos);
    }

    public void createMediaPlayer(int positionInner) {
        this.position = positionInner;
        mediaPlayer = new MediaPlayer();
    }

    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public void setDataSource(String url, int position) {
        this.position = position;
        try {
            mediaPlayer.setDataSource(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onCompleted() {
        mediaPlayer.setOnCompletionListener(this);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if (actionPlaying != null) {
            actionPlaying.nextBtnClicked();
            if (mediaPlayer != null){
                createMediaPlayer(position);
                try {
                    mediaPlayer.setDataSource(musics.get(position).getMediaUrl());
                    mediaPlayer.setOnPreparedListener(mediaPlayer1 -> {
                        mediaPlayer1.start();
                        onCompleted();
                    });
                    mediaPlayer.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setCallback(ActionPlaying callback){
        actionPlaying = callback;
    }
}
