package com.example.letsvote;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import android.widget.Spinner;
import android.widget.Toast;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;

public class AddCandidate extends AppCompatActivity {

    private EditText candidateName, candidateMobile, candidateAddress, candidateEmail;
    private Button selectBirthdate, candidateSubmit;
    private FirebaseDatabase db;
    private DatabaseReference reference;
    private int year, month, dayOfMonth;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private String birthDate;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_candidate);

        db = FirebaseDatabase.getInstance();
        reference = db.getReference().child("Candidates");
        candidateName = findViewById(R.id.candidateName);
        candidateMobile = findViewById(R.id.candidateMobile);
        candidateAddress = findViewById(R.id.candidateAddress);
        candidateEmail = findViewById(R.id.candidateEmail);
        candidateSubmit = findViewById(R.id.candidateSubmit);
        selectBirthdate = findViewById(R.id.candidateBirthdate);


        selectBirthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(AddCandidate.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        birthDate = day+"/"+(month+1)+"/"+year;
                    }
                }, year, month, dayOfMonth);
                datePickerDialog.show();
            }
        });

        candidateSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cName = candidateName.getText().toString();
                String cMobile = candidateMobile.getText().toString();
                String cAddress = candidateAddress.getText().toString();
                String cEmail = candidateEmail.getText().toString();

                if(!cName.isEmpty())
                {
                    if(cMobile.length()==10)
                    {
                        if(!cAddress.isEmpty())
                        {
                            if(!cEmail.isEmpty() && cEmail.contains("@"))
                            {
                                if(!birthDate.isEmpty())
                                {
                                    HashMap<String, String> cMap = new HashMap<>();
                                    cMap.put("Candidate Name", cName);
                                    cMap.put("Candidate Mobile", cMobile);
                                    cMap.put("Candidate Address", cAddress);
                                    cMap.put("Candidate Email", cEmail);
                                    cMap.put("Candidate Birthdate", birthDate);
                                    cMap.put("count", String.valueOf(0));
                                    reference.child(cName).setValue(cMap);
                                    candidateName.setText("");
                                    candidateAddress.setText("");
                                    candidateEmail.setText("");
                                    candidateMobile.setText("");
                                    mp= MediaPlayer.create(AddCandidate.this,R.raw.bc4);
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
                                    Toast.makeText(AddCandidate.this, "Candidate added Successfully", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    mp=MediaPlayer.create(AddCandidate.this,R.raw.error2);
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
                                    Toast.makeText(AddCandidate.this, "Please select your birthdate", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                mp=MediaPlayer.create(AddCandidate.this,R.raw.error2);
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
                                candidateEmail.setError("This field cannot be empty");
                            }
                        }
                        else{
                            mp=MediaPlayer.create(AddCandidate.this,R.raw.error2);
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
                            candidateAddress.setError("This field cannot be empty");
                        }
                    }
                    else{
                        mp=MediaPlayer.create(AddCandidate.this,R.raw.error2);
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
                        candidateMobile.setError("This field cannot be empty");
                    }
                }
                else{
                    mp=MediaPlayer.create(AddCandidate.this,R.raw.error2);
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
                    candidateName.setError("This field cannot be empty");
                }
            }
        });
    }
}