package com.example.mymusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MusicPlayerActivity extends AppCompatActivity {
    TextView titleTv,currentTimeTv,totalTimeTv;
    SeekBar seekBar;
    ImageView pausePlay,nextBtn,previousBtn,musicIcon;
    ArrayList<AudioModel> songsList;
    AudioModel currentSong;
    MediaPlayer mediaPlayer=MyMediaPlayer.getInstance();
    Context context;
    int x=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        titleTv=findViewById(R.id.song_title);
        currentTimeTv=findViewById(R.id.current_time);
        totalTimeTv=findViewById(R.id.total_time);
        seekBar=findViewById(R.id.seek_bar);
        pausePlay=findViewById(R.id.pause_play);
        nextBtn=findViewById(R.id.next);
        previousBtn=findViewById(R.id.previous);
        musicIcon=findViewById(R.id.music_icon_big);
        titleTv.setSelected(true);
        songsList=(ArrayList<AudioModel>) getIntent().getSerializableExtra("SONGS");
        setResourcesWithMusic();


    }
    void setResourcesWithMusic(){
        currentSong=songsList.get(MyMediaPlayer.currentIndex);
        titleTv.setText(currentSong.title);
        totalTimeTv.setText(convertToMMSS(currentSong.getDuration()));


        pausePlay.setOnClickListener(v->pausePlay());
        nextBtn.setOnClickListener(v->playNextSong());
        previousBtn.setOnClickListener(v->playPreviousSong());
        playMusic();
    }
    private void playMusic(){
      try {
          mediaPlayer.reset();
          mediaPlayer.setDataSource(currentSong.getPath());
          mediaPlayer.prepare();
          mediaPlayer.start();
          seekBar.setProgress(0);
          seekBar.setMax(mediaPlayer.getDuration());
      } catch (IOException e){
          e.printStackTrace();
      }
    }
    private void playNextSong(){
        if(MyMediaPlayer.currentIndex==songsList.size()-1)
            return;
        MyMediaPlayer.currentIndex+=1;
        mediaPlayer.reset();
        setResourcesWithMusic();
        MusicPlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer!=null){
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    currentTimeTv.setText(convertToMMSS(mediaPlayer.getCurrentPosition()+""));

                    if(mediaPlayer.isPlaying()){
                        pausePlay.setImageResource(R.drawable.baseline_pause_circle_outline_24);
                        musicIcon.setRotation(x++);
                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                if(MyMediaPlayer.currentIndex==songsList.size()-1)
                                    return;


                                MyMediaPlayer.currentIndex+=1;
                                mediaPlayer.reset();

                                setResourcesWithMusic();
                                MyMediaPlayer.currentIndex-=1;



                            }

                        });
                    }else{
                        pausePlay.setImageResource(R.drawable.play);
                        musicIcon.setRotation(0);





                    }
                }
                new Handler().postDelayed(this,100);
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer!=null && fromUser){
                    mediaPlayer.seekTo(progress);
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
    private void playPreviousSong(){
        if(MyMediaPlayer.currentIndex==0)
            return;
        MyMediaPlayer.currentIndex-=1;
        mediaPlayer.reset();
        setResourcesWithMusic();
    }
    private void pausePlay(){
        if(mediaPlayer.isPlaying())
            mediaPlayer.pause();
        else
            mediaPlayer.start();
    }

    public static String convertToMMSS(String duration){
        Long millis=Long.parseLong(duration);
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis)%TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis)%TimeUnit.MINUTES.toSeconds(1));
    }
}