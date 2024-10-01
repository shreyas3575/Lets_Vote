package com.example.letsvote;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;

public class AddVoter extends AppCompatActivity {

    private EditText voterID, voterMobile;
    private Button selectBirthdate, voterSubmit;
    private FirebaseDatabase db;
    private DatabaseReference reference;
    private int year, month, dayOfMonth;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private String birthDate;
    MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_voter);

        db = FirebaseDatabase.getInstance();
        reference = db.getReference().child("Voters");
        voterID = findViewById(R.id.voterID);
        voterMobile = findViewById(R.id.voterMobile);
        selectBirthdate = findViewById(R.id.selectBirthdate);
        voterSubmit = findViewById(R.id.voterSubmit);

        selectBirthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(AddVoter.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        birthDate = day+"/"+(month+1)+"/"+year;
                    }
                }, year, month, dayOfMonth);
                datePickerDialog.show();
            }
        });

        voterSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String vID = voterID.getText().toString();
                String mobile = voterMobile.getText().toString();
                if(vID.length()==10)
                {
                    if(mobile.length()==10)
                    {
                        if(!birthDate.isEmpty())
                        {
                            HashMap<String, String> voterMap = new HashMap<>();
                            voterMap.put("Voter ID", vID);
                            voterMap.put("Mobile", mobile);
                            voterMap.put("Birthdate", birthDate);
                            voterMap.put("voted", "false");
                            reference.child(vID).setValue(voterMap);
                            voterID.setText("");
                            voterMobile.setText("");
                            mp=MediaPlayer.create(AddVoter.this,R.raw.bc4);
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
                            Toast.makeText(AddVoter.this, "Voter added Successfully", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(AddVoter.this, "Please select your Birthdate", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        mp=MediaPlayer.create(AddVoter.this,R.raw.error2);
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

                        voterMobile.setError("Enter a valid number");
                    }
                }
                else {
                    mp=MediaPlayer.create(AddVoter.this,R.raw.error2);
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
                    voterID.setError("Enter a valid Voter ID");
                }
            }
        });
    }
}