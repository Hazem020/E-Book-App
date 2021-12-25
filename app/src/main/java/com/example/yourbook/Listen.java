package com.example.yourbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.concurrent.TimeUnit;

public class Listen extends AppCompatActivity {
    /**
     * This activity to allow us to open the audio from URL
     **/

    // declare variables
    TextView playerPosition, playerDuration;
    SeekBar seekBar;
    ImageView btRew, btPlay, btPause, btFf;
    MediaPlayer mediaPlayer;
    RoundedImageView Limg;
    // A Handler is a threading class defined in the android.os package
    // through which we can send and process Message and Runnable objects associated with a thread's
    Handler handler = new Handler();
    // Runnable is a concurrent unit of execution in Android
    Runnable runnable;
    // get audio url from firebase
    String audioURL = BookDetails.audioURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen);
        // attach cover to it's id
        Limg = findViewById(R.id.Limg);
        //change Image with url
        Glide.with(Listen.this).load(BookDetails.imgUrl).placeholder(R.mipmap.ic_launcher).into(Limg);
        // attach each component to it's id
        playerPosition = findViewById(R.id.player_position);
        playerDuration = findViewById(R.id.player_duration);
        seekBar = findViewById(R.id.seek_bar);
        btRew = findViewById(R.id.bt_rew);
        btPlay = findViewById(R.id.bt_play);
        btPause = findViewById(R.id.bt_pause);
        btFf = findViewById(R.id.bt_ff);

        // create a media player
        mediaPlayer = new MediaPlayer();
        try {
            // try & catch to handle errors
            // streaming and opening the audio from url
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(Listen.this, Uri.parse(audioURL));
            // used for playing the live data over stream.
            // It allows to play without blocking the main thread
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    // get the total duration of the audio
                    int duration = mediaPlayer.getDuration();
                    // convert the duration to string by a function we have created
                    // to be shown besides the seek bar
                    String sDuration = convertFormat(duration);
                    playerDuration.setText(sDuration);
                    // loading progress icon
                    ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar2);
                    // when complete, icon gone
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
        // handle errors, to prevent crashes
        catch (Exception e) {
            Toast.makeText(Listen.this, "Error", Toast.LENGTH_SHORT).show();
        }

        // create a runnable to control with the seek bar
        runnable = new Runnable() {
            @Override
            public void run() {
                // make the seekBar get the current time of audio
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this, 500);
            }
        };

        // play button to play audio
        btPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // when we click
                // play button disappears, pause button appears
                btPlay.setVisibility(View.GONE);
                btPause.setVisibility(View.VISIBLE);
                // audio starts
                mediaPlayer.start();
                // get current duration to seek bar
                seekBar.setMax(mediaPlayer.getDuration());
                // allows seekbar pointer to point the current duration
                handler.postDelayed(runnable, 0);

            }
        });

        // pause button to pause audio
        btPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // when we click
                // pause button disappears, play button appears
                btPlay.setVisibility(View.VISIBLE);
                btPause.setVisibility(View.GONE);
                // audio pauses
                mediaPlayer.pause();
                // stop running operation
                handler.removeCallbacks(runnable);

            }
        });

        // button to move forward 5 seconds
        btFf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get current duration of audio and compares it with the duration of audio
                int currentPosition = mediaPlayer.getCurrentPosition();
                int duration = mediaPlayer.getDuration();
                // if they are not the same, we cant move forward 5 second
                if (duration != currentPosition) {
                    // change position of audio
                    currentPosition = currentPosition + 5000;
                    // change text
                    playerPosition.setText(convertFormat(currentPosition));
                    mediaPlayer.seekTo(currentPosition);
                }

            }
        });

        // button to back  5 seconds
        btRew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = mediaPlayer.getCurrentPosition();
                // if current duration less than 5 seconds we can't back
                if (currentPosition > 5000) {
                    // change position of audio
                    currentPosition = currentPosition - 5000;
                    // change text
                    playerPosition.setText(convertFormat(currentPosition));
                    mediaPlayer.seekTo(currentPosition);
                }

            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                /**
                 * make seek bar accept all changes happen to it
                 * it takes seekBar as argument to control it
                 * progress which are INT refers to current duration we seek To
                 * fromUser which mean this action is happened by yser
                 **/

                // if there any change in duration make seek bar change it's current duration
                // to the new one, and change the showed text
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
                playerPosition.setText(convertFormat(mediaPlayer.getCurrentPosition()));
            }

            // Notification that the user has started a touch gesture
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            // Notification that the user has finished a touch gesture
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // when audio is finished
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // pause button disappears, play button appears again
                btPause.setVisibility(View.GONE);
                btPlay.setVisibility(View.VISIBLE);
                // change current duration to zero
                mediaPlayer.seekTo(0);

            }
        });




    }

    @Override
    public void onBackPressed() {
        mediaPlayer.stop();
        finish();
    }

    // we created function to set values of duration and showing it
    @SuppressLint("DefaultLocale")
    private String convertFormat(int duration) {
        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(duration), TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
        );
    }


}