package com.example.media;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity{

    private MediaController mediaController;
    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    private Timer timer;
    // ui component
    private Button pauseMusic,startMusic;
    private SeekBar chanageVolume,timeline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // video and audio player
        mediaController = new MediaController(MainActivity.this);
        mediaPlayer     = MediaPlayer.create(MainActivity.this,R.raw.shotclock);
        audioManager =(AudioManager)getSystemService(AUDIO_SERVICE);
        // create ui instances
        startMusic    = findViewById(R.id.playmusic);
        pauseMusic    = findViewById(R.id.pusemusic);
        chanageVolume = findViewById(R.id.VolumeBar);
        timeline      = findViewById(R.id.MusicBar);
       // ui listener
        // start button listener
        startMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.start();
                timer         = new Timer();
                timeline.setMax(mediaPlayer.getDuration());

                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        timeline.setProgress(mediaPlayer.getCurrentPosition());
                    }
                },0,1000);
            }
        });
        // start button listener
        pauseMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
                timer.cancel();
            }
        });
        //get the max and in progress volume for this device
        int max    = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int curent = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        chanageVolume.setMax(max);
        chanageVolume.setProgress(curent);
        chanageVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // if user make any chanage in seek bar
                if(fromUser){
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);

                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        timeline.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mediaPlayer.seekTo(progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mediaPlayer.pause();

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.start();

            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                timer.cancel();
                Toast.makeText(MainActivity.this,"Muisc End",Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void PlayVideo(View v){
        Uri VideoUri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.video);
        VideoView video = (VideoView)findViewById(R.id.video);
        video.setVideoURI(VideoUri);
        video.setMediaController(mediaController);
        mediaController.setAnchorView(video);
        video.start();



    }


}
