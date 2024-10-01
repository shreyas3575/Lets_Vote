package com.example.letsvote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AlreadyVotedActivity extends AppCompatActivity {

    private Button homeB;
    MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_already_voted);
    }

    public void pass(View view) {
        mp= MediaPlayer.create(AlreadyVotedActivity.this,R.raw.bc4);
        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mp.start();
            }
        });
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mp.release();
            }
        });
        startActivity(new Intent(AlreadyVotedActivity.this, IntroActivity.class));
        finish();
    }
}