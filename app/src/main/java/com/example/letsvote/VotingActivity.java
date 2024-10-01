package com.example.letsvote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VotingActivity extends AppCompatActivity {

    private RadioGroup rdgrp;
    private DatabaseReference databaseReference, reference, vIDReference;
    private Button voteSubmit;
    private RadioButton radioButton;
    private String name, vID;
    MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting);

        Intent i = getIntent();
        vID = i.getStringExtra("vID");

        voteSubmit = findViewById(R.id.voteButton);
        rdgrp = (RadioGroup) findViewById(R.id.rdgrp1);

        createButtons();

        voteSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selected = rdgrp.getCheckedRadioButtonId();
                if (selected == -1)
                {
                    mp= MediaPlayer.create(VotingActivity.this,R.raw.error2);
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
                    Toast.makeText(VotingActivity.this, "Please select a candidate to vote for!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    radioButton = (RadioButton) findViewById(selected);
                    name = radioButton.getText().toString();
                    reference = FirebaseDatabase.getInstance().getReference("Candidates");
                    reference.child(name).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().exists()) {
                                    DataSnapshot snapshot = task.getResult();
                                    int cCount = Integer.parseInt(String.valueOf(snapshot.child("count").getValue()));
                                    reference.child(name).child("count").setValue(String.valueOf(cCount + 1));

                                    vIDReference = FirebaseDatabase.getInstance().getReference("Voters");
                                    vIDReference.child(vID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                                            DataSnapshot dataSnapshot =  task.getResult();
                                            vIDReference.child(vID).child("voted").setValue("true");
                                        }
                                    });
                                    mp= MediaPlayer.create(VotingActivity.this,R.raw.bc4);
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
                                    startActivity(new Intent(VotingActivity.this, AfterVotingActivity.class));
                                    finish();
                                } else {
                                    mp= MediaPlayer.create(VotingActivity.this,R.raw.error2);
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
                                    Toast.makeText(VotingActivity.this, "Please try again later", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                mp= MediaPlayer.create(VotingActivity.this,R.raw.error2);
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
                                Toast.makeText(VotingActivity.this, "Please try again later", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void createButtons() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Candidates");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot1 : snapshot.getChildren())
                {
                    String name = String.valueOf(dataSnapshot1.child("Candidate Name").getValue());
                    RadioButton button = new RadioButton(VotingActivity.this);
                    button.setId(View.generateViewId());
                    button.setText(name);
                    button.setTextSize(0x18);
                    button.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    button.setPadding(20, 24, 20, 24);
                    Drawable img = button.getContext().getResources().getDrawable(R.drawable.shivsena);
                    Bitmap bitmap = ((BitmapDrawable) img).getBitmap();
                    Drawable fimg = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 50, 50, true));
                    button.setCompoundDrawablesWithIntrinsicBounds(null, null, fimg, null);
                    button.setCompoundDrawablePadding(40);
                    rdgrp.addView(button);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}