package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    Button forward_btn,back_btn,play_btn,stop_btn;
    TextView song_title, header_title,time_txt;
    SeekBar seekBar;

    //Media PLayer
    MediaPlayer mediaPlayer;

    //Handlers
    Handler handler = new Handler();


    //variables
    double startTime = 0,finishTime = 0;
    int forwardTime = 10000, backwardTime = 10000;
    static  int oneTimeOnly = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play_btn = findViewById(R.id.play);
        stop_btn = findViewById(R.id.pause);
        forward_btn = findViewById(R.id.forward);
        back_btn = findViewById(R.id.back);
        song_title = findViewById(R.id.song_title);
        time_txt = findViewById(R.id.time_left_text);
        header_title = findViewById(R.id.header_title);
        seekBar = findViewById(R.id.seekBar);

        //media player
        mediaPlayer = MediaPlayer.create(this,R.raw.taylor);
        song_title.setText(getResources().getIdentifier(
                "taylor",
                "raw",
                getPackageName()
        ));

        seekBar.setClickable(false);

        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayMusic();
            }
        });
        stop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.pause();

            }
        });

        forward_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temp = (int) startTime;
                if((temp + forwardTime) <= finishTime ){
                    startTime = startTime + forwardTime;
                    mediaPlayer.seekTo((int) startTime);
                }else{
                    Toast.makeText(MainActivity.this, "Cant Jump Forward", Toast.LENGTH_SHORT).show();
                }
            }
        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temp = (int) startTime;
                if((temp = backwardTime) > 0){
                    startTime = startTime - backwardTime;
                    mediaPlayer.seekTo((int) startTime);
                }else{
                    Toast.makeText(MainActivity.this, "Cant Go Back", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void PlayMusic(){
        mediaPlayer.start();
        finishTime = mediaPlayer.getDuration();
        startTime = mediaPlayer.getCurrentPosition();

        if(oneTimeOnly == 0){
            seekBar.setMax((int) finishTime);
            oneTimeOnly = 1;
        }

        time_txt.setText(String.format(
                "%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) finishTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finishTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finishTime))
        ));
        seekBar.setProgress((int) startTime);
        handler.postDelayed(UpdateSongTime, 100);

    }
    //creating runnable
    private Runnable UpdateSongTime = new Runnable() {
        @Override
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            time_txt.setText(String.format(
                    "%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.NANOSECONDS.toMinutes((long) startTime))
                    ));
            seekBar.setProgress((int) startTime);
            handler.postDelayed(this,100);
        }
    };
}