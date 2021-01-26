package com.example.hitstory.ui.detail;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.rtp.AudioStream;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.media.app.NotificationCompat;

import com.example.hitstory.ActionPlaying;
import com.example.hitstory.AudioService;
import com.example.hitstory.MainActivity;
import com.example.hitstory.NotificationReceiver;
import com.example.hitstory.R;
import com.example.hitstory.data.models.MediaMetaData;
import com.example.hitstory.databinding.ActivityDetailBinding;
import com.example.hitstory.ui.songs.SongsListFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.xml.transform.stream.StreamSource;

import static com.example.hitstory.App.ACTION_NEXT;
import static com.example.hitstory.App.ACTION_PLAY;
import static com.example.hitstory.App.ACTION_PREVIOUS;
import static com.example.hitstory.App.CHANNEL_ID_2;

public class DetailActivity extends AppCompatActivity implements ActionPlaying, ServiceConnection {

    public static final String POSITION = "position";
    ActivityDetailBinding binding;
    public static final String SONG = "song";
    static ArrayList<MediaMetaData> metaData;
    private Handler handler = new Handler();
    private Thread playThread, prevThread, nextThread;
    private int position;
    AudioService audioService;
    MediaSessionCompat mediaSessionCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        mediaSessionCompat = new MediaSessionCompat(getBaseContext(), "My Audio");
        getData();
        seekBarListener();
        DetailActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (audioService != null) {
                    int currentPosition = audioService.getCurrentPosition() / 1000;
                    binding.seekBar.setProgress(currentPosition);
                    binding.timer.setText(formattedText(currentPosition));
                }
                handler.postDelayed(this, 1000);
            }
        });
        binding.shuffle.setOnClickListener(view -> {
            if (MainActivity.shuffleBoolean) {
                MainActivity.shuffleBoolean = false;
                binding.shuffle.setImageResource(R.drawable.ic_shuffle_off);
            } else {
                MainActivity.shuffleBoolean = true;
                binding.shuffle.setImageResource(R.drawable.ic_baseline_shuffle_24);
            }
        });
        binding.repeat.setOnClickListener(view -> {
            if (MainActivity.repeatBoolean) {
                MainActivity.repeatBoolean = false;
                binding.repeat.setImageResource(R.drawable.ic_repeat_off);
            } else {
                MainActivity.repeatBoolean = true;
                binding.repeat.setImageResource(R.drawable.ic_repeat_on);
            }
        });
    }

    private String formattedText(int currentPosition) {
        String totalOut = "";
        String totalNew = "";
        String seconds = String.valueOf(currentPosition % 60);
        String minutes = String.valueOf(currentPosition / 60);
        totalOut = minutes + ":" + seconds;
        totalNew = minutes + ":" + "0" + seconds;
        if (seconds.length() == 1) {
            return totalNew;
        } else {
            return totalOut;
        }
    }

    private void seekBarListener() {
        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (audioService != null && b) {
                    audioService.seekTo(i * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    @Override
    protected void onResume() {
        Intent intent = new Intent(this, AudioService.class);
        bindService(intent, this, BIND_AUTO_CREATE);
        playThreadBtn();
        prevThreadBtn();
        nextThreadBtn();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(this);
    }

    private void nextThreadBtn() {
        nextThread = new Thread(() -> {
            binding.next.setOnClickListener(view -> {
                nextBtnClicked();
            });
        });
        nextThread.start();
    }

    public void nextBtnClicked() {
        if (audioService.isPlaying()) {
            audioService.stop();
            audioService.release();
            if (MainActivity.shuffleBoolean && !MainActivity.repeatBoolean) {
                position = getRandomNumber(metaData.size());
            } else if (!MainActivity.shuffleBoolean && !MainActivity.repeatBoolean) {
                position = ((position + 1) == metaData.size() - 1 ? 0 : (position + 1));
            }
            audioService.createMediaPlayer(position);
            audioService.setDataSource(metaData.get(position).getMediaUrl(),position);
            audioService.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    audioService.start();
                    audioService.onCompleted();
                    binding.mediaTitle.setText(metaData.get(position).getMediaArtist());
                    binding.mediaBody.setText(metaData.get(position).getMediaTitle());
                    binding.seekBar.setMax(audioService.getDuration() / 1000);
                }
            });
            audioService.prepareAsync();
            DetailActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (audioService != null) {
                        int currentPosition = audioService.getDuration() / 1000;
                        binding.seekBar.setProgress(currentPosition);
                        binding.timer.setText(formattedText(currentPosition));
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            showNotification(R.drawable.ic_pause);
            binding.playPause.setImageResource(R.drawable.ic_pause);
        } else {
            audioService.stop();
            audioService.release();
            if (MainActivity.shuffleBoolean && !MainActivity.repeatBoolean) {
                position = getRandomNumber(metaData.size());
            } else if (!MainActivity.shuffleBoolean && !MainActivity.repeatBoolean) {
                position = ((position + 1) == metaData.size() - 1 ? 0 : (position + 1));
            }
            audioService.createMediaPlayer(position);
            audioService.setDataSource(metaData.get(position).getMediaUrl(),position);
            audioService.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    binding.mediaTitle.setText(metaData.get(position).getMediaArtist());
                    binding.mediaBody.setText(metaData.get(position).getMediaTitle());
                    binding.seekBar.setMax(audioService.getDuration() / 1000);
                }
            });
            audioService.prepareAsync();
            DetailActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (audioService != null) {
                        int currentPosition = audioService.getCurrentPosition() / 1000;
                        binding.seekBar.setProgress(currentPosition);
                        binding.timer.setText(formattedText(currentPosition));
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            showNotification(R.drawable.ic_pause);
            binding.playPause.setImageResource(R.drawable.ic_pause);
        }
    }

    private int getRandomNumber(int i) {
        Random r = new Random(0);
        return r.nextInt(i);
    }

    private void prevThreadBtn() {
        prevThread = new Thread(() -> {
            binding.previous.setOnClickListener(view -> {
                previousBtnClicked();
            });
        });
        prevThread.start();
    }

    public void previousBtnClicked() {
        if (audioService.isPlaying()) {
            audioService.stop();
            audioService.release();
            if (MainActivity.shuffleBoolean && !MainActivity.repeatBoolean) {
                position = getRandomNumber(metaData.size());
            } else if (!MainActivity.shuffleBoolean && !MainActivity.repeatBoolean) {
                position = ((position - 1) < 0 ? metaData.size() - 1 : (position - 1));
            }
            audioService.createMediaPlayer(position);
            audioService.setDataSource(metaData.get(position).getMediaUrl(),position);
            audioService.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    binding.mediaTitle.setText(metaData.get(position).getMediaArtist());
                    binding.mediaBody.setText(metaData.get(position).getMediaTitle());
                    binding.seekBar.setMax(audioService.getDuration() / 1000);
                }
            });
            audioService.prepareAsync();
            DetailActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (audioService != null) {
                        int currentPosition = audioService.getCurrentPosition() / 1000;
                        binding.seekBar.setProgress(currentPosition);
                        binding.timer.setText(formattedText(currentPosition));
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            showNotification(R.drawable.ic_pause);
            binding.playPause.setImageResource(R.drawable.ic_pause);
        } else {
            audioService.stop();
            audioService.release();
            if (MainActivity.shuffleBoolean && !MainActivity.repeatBoolean) {
                position = getRandomNumber(metaData.size());
            } else if (!MainActivity.shuffleBoolean && !MainActivity.repeatBoolean) {
                position = ((position - 1) < 0 ? metaData.size() - 1 : (position - 1));
            }
            audioService.createMediaPlayer(position);
            audioService.setDataSource(metaData.get(position).getMediaUrl(),position);
            audioService.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    binding.mediaTitle.setText(metaData.get(position).getMediaArtist());
                    binding.mediaBody.setText(metaData.get(position).getMediaTitle());
                    binding.seekBar.setMax(audioService.getDuration() / 1000);
                }
            });
            audioService.prepareAsync();
            DetailActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (audioService != null) {
                        int currentPosition = audioService.getCurrentPosition() / 1000;
                        binding.seekBar.setProgress(currentPosition);
                        binding.timer.setText(formattedText(currentPosition));
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            showNotification(R.drawable.ic_pause);
            binding.playPause.setImageResource(R.drawable.ic_pause);
        }
    }

    private void playThreadBtn() {
        playThread = new Thread(() -> {
            binding.playPause.setOnClickListener(view -> {
                playPauseBtnClicked();
            });
        });
        playThread.start();
    }

    public void playPauseBtnClicked() {
        if (audioService.isPlaying()) {
            binding.playPause.setImageResource(R.drawable.ic_play);
            showNotification(R.drawable.ic_play);
            audioService.pause();
            binding.seekBar.setMax(audioService.getDuration() / 1000);
            DetailActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (audioService != null) {
                        int currentPosition = audioService.getCurrentPosition() / 1000;
                        binding.seekBar.setProgress(currentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
        } else {
            binding.playPause.setImageResource(R.drawable.ic_pause);
            showNotification(R.drawable.ic_pause);
            audioService.start();
            binding.seekBar.setMax(audioService.getDuration() / 1000);
            DetailActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (audioService != null) {
                        int currentPosition = audioService.getCurrentPosition() / 1000;
                        binding.seekBar.setProgress(currentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
        }
    }

    private void getData() {
        Intent intent = getIntent();
        metaData = SongsListFragment.data;
        position = intent.getIntExtra(SONG, 0);
        if (metaData != null) {
            binding.playPause.setImageResource(R.drawable.ic_pause);
            showNotification(R.drawable.ic_pause);
        }
        Intent intent1 = new Intent(this, AudioService.class);
        intent1.putExtra(POSITION, position);
        startService(intent1);
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        AudioService.MyBinder myBinder = (AudioService.MyBinder) iBinder;
        audioService = myBinder.getService();
        audioService.setCallback(this);
        binding.mediaTitle.setText(metaData.get(position).getMediaArtist());
        binding.mediaBody.setText(metaData.get(position).getMediaTitle());
        binding.seekBar.setMax(audioService.getDuration() / 1000);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        audioService = null;
    }

    public void showNotification(int playPauseBtn) {
        Intent contentIntent = new Intent(this, DetailActivity.class);
        PendingIntent contentPending = PendingIntent.getActivity(this, 0, contentIntent, 0);
        Intent prevIntent = new Intent(this, NotificationReceiver.class)
                .setAction(ACTION_PREVIOUS);
        PendingIntent prevPending = PendingIntent
                .getBroadcast(this, 0, prevIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        Intent pauseIntent = new Intent(this, NotificationReceiver.class)
                .setAction(ACTION_PLAY);
        PendingIntent pausePending = PendingIntent
                .getBroadcast(this, 0, pauseIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        Intent nextIntent = new Intent(this, NotificationReceiver.class)
                .setAction(ACTION_NEXT);
        PendingIntent nextPending = PendingIntent
                .getBroadcast(this, 0, nextIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap largeIcon = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.music_background);

        Notification notification = new androidx.core.app.NotificationCompat.Builder(this, CHANNEL_ID_2)
                .setSmallIcon(playPauseBtn)
                .setLargeIcon(largeIcon)
                .setContentTitle(metaData.get(position).getMediaArtist())
                .setContentText(metaData.get(position).getMediaTitle())
                .addAction(R.drawable.ic_skip_previous, "Previous", prevPending)
                .addAction(playPauseBtn, "Pause", pausePending)
                .addAction(R.drawable.ic_skip_next, "Next", nextPending)
                .setStyle(new NotificationCompat.MediaStyle().setMediaSession(mediaSessionCompat.getSessionToken()))
                .setPriority(androidx.core.app.NotificationCompat.PRIORITY_HIGH)
                .setOnlyAlertOnce(true)
                .build();

        NotificationManager manager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0, notification);
    }

}