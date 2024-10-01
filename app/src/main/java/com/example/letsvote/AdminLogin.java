package com.example.letsvote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AdminLogin extends AppCompatActivity {
    MediaPlayer mp;
    private EditText adminID, adminPassword;
    private Button adminSubmit;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminlogin);

        auth = FirebaseAuth.getInstance();
        adminID = findViewById(R.id.adminID);
        adminPassword = findViewById(R.id.adminPassword);
        adminSubmit = findViewById(R.id.adminSubmit);

        adminSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = adminID.getText().toString();
                String pass  = adminPassword.getText().toString();

                if(!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    if(!pass.isEmpty())
                    {
                        auth.signInWithEmailAndPassword(email, pass)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        mp=MediaPlayer.create(AdminLogin.this,R.raw.bc4);

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
                                        Toast.makeText(AdminLogin.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(AdminLogin.this, AdminMenu.class));
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        mp=MediaPlayer.create(AdminLogin.this,R.raw.error2);

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
                                        Toast.makeText(AdminLogin.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                    else
                    {
                        mp=MediaPlayer.create(AdminLogin.this,R.raw.error2);

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
                        adminPassword.setError("Password cannot be empty!");
                    }
                }
                else if(email.isEmpty())
                {    mp=MediaPlayer.create(AdminLogin.this,R.raw.error2);

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
                    adminID.setError("Email cannot be empty!");
                }
                else
                {
                    mp=MediaPlayer.create(AdminLogin.this,R.raw.error2);

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
                    adminID.setError("Please enter a valid email");
                }
            }
        });
    }
}