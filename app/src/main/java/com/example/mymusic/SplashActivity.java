package com.example.mymusic;

import androidx.appcompat.app.AppCompatActivity;


import android.media.MediaPlayer;
import android.os.Bundle;



import android.content.Intent;
import android.net.Uri;
import android.view.MotionEvent;
import android.widget.VideoView;


import androidx.core.splashscreen.SplashScreen;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerView;

 public class SplashActivity extends AppCompatActivity {
    private ExoPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PlayerView playerView = findViewById(R.id.video_view);
        try {
            VideoView videoHolder = new VideoView(this);
            setContentView(videoHolder);
            player = new ExoPlayer.Builder(this).build();
            playerView.setPlayer(player);
            Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.videosplash);
            videoHolder.setVideoURI(video);

            videoHolder.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    jump();
                }
            });
            videoHolder.start();
        } catch (Exception ex) {
            jump();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        jump();
        return true;
    }

    private void jump() {
        if (isFinishing())
            return;
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (player != null) {
            player.release();
        }
    }
}

