package com.example.letsvote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class VoterLogin extends AppCompatActivity {

    private DatabaseReference reference;
    private Button getOtp, voterSubmit, selectBirthdate;
    private EditText voterID, enterOTP;
    private String mobile, birthDate, vID, backendotp, selectedBirthDate, voted;
    private ProgressBar progressBar;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private int year, month, dayOfMonth;
    MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voter_login);

         voterSubmit= findViewById(R.id.voterSubmit);
         getOtp = findViewById(R.id.getOtp);
         selectBirthdate = findViewById(R.id.selectBirthdate);
         voterID = findViewById(R.id.voterid);
         enterOTP = findViewById(R.id.otp);
         progressBar = findViewById(R.id.progressBar);

        selectBirthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(VoterLogin.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        selectedBirthDate = day+"/"+(month+1)+"/"+year;
                    }
                }, year, month, dayOfMonth);
                datePickerDialog.show();
            }
        });

         getOtp.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 vID = voterID.getText().toString().trim();
                 if(vID.length()==10)
                 {
                     progressBar.setVisibility(View.VISIBLE);
                     getOtp.setVisibility(View.INVISIBLE);
                     reference = FirebaseDatabase.getInstance().getReference("Voters");
                     reference.child(vID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                         @Override
                         public void onComplete(@NonNull Task<DataSnapshot> task) {
                             if(task.isSuccessful())
                             {
                                 if(task.getResult().exists())
                                 {
                                     DataSnapshot snapshot = task.getResult();
                                     mobile = String.valueOf(snapshot.child("Mobile").getValue());
                                     birthDate = String.valueOf(snapshot.child("Birthdate").getValue());
                                     voted = String.valueOf(snapshot.child("voted").getValue());

                                     PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                             "+91" + mobile,
                                             0,
                                             TimeUnit.SECONDS,
                                             VoterLogin.this,
                                             new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                                 @Override
                                                 public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                                     progressBar.setVisibility(View.GONE);
                                                     getOtp.setVisibility(View.VISIBLE);
                                                 }

                                                 @Override
                                                 public void onVerificationFailed(@NonNull FirebaseException e) {
                                                     progressBar.setVisibility(View.GONE);
                                                     getOtp.setVisibility(View.VISIBLE);
                                                     mp= MediaPlayer.create(VoterLogin.this,R.raw.error2);
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
                                                     Toast.makeText(VoterLogin.this, "Error please check your internet connection", Toast.LENGTH_SHORT).show();
                                                 }

                                                 @Override
                                                 public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                                     progressBar.setVisibility(View.GONE);
                                                     getOtp.setVisibility(View.VISIBLE);
                                                     backendotp=s;
                                                     mp= MediaPlayer.create(VoterLogin.this,R.raw.pop);
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
                                                     Toast.makeText(VoterLogin.this, "OTP sent", Toast.LENGTH_SHORT).show();
                                                 }
                                             }
                                     );
                                 }
                             }
                             else{
                                 mp= MediaPlayer.create(VoterLogin.this,R.raw.error2);
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
                                 Toast.makeText(VoterLogin.this, "This voter ID may need to be registered by Admin", Toast.LENGTH_SHORT).show();
                             }
                         }
                     });
                 }
                 else{
                     mp= MediaPlayer.create(VoterLogin.this,R.raw.error2);
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
                     voterID.setError("Please enter a valid voter ID");
                 }
             }
         });

         voterSubmit.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if (backendotp != null)
                 {
                     String enteredOtp = enterOTP.getText().toString();
                     if (vID!=null && vID.equals(voterID.getText().toString())) {
                         if (selectedBirthDate!=null && selectedBirthDate.equals(birthDate))
                         {
                             PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                                     backendotp, enteredOtp
                             );
                             FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                                     .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                         @Override
                                         public void onComplete(@NonNull Task<AuthResult> task) {
                                             if (task.isSuccessful()) {
                                                 mp= MediaPlayer.create(VoterLogin.this,R.raw.bc4);
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
                                                 Toast.makeText(VoterLogin.this, "Verification Successful", Toast.LENGTH_SHORT).show();

                                                 if(voted.equals("true"))
                                                 {   mp= MediaPlayer.create(VoterLogin.this,R.raw.bc4);
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
                                                     startActivity(new Intent(VoterLogin.this, AlreadyVotedActivity.class));
                                                     voterID.setText("");
                                                     enterOTP.setText("");
                                                     FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                     user.delete();
                                                 }
                                                 else
                                                 {
                                                     Intent i = new Intent(VoterLogin.this, VotingActivity.class);
                                                     i.putExtra("vID", vID);
                                                     startActivity(i);
                                                     voterID.setText("");
                                                     enterOTP.setText("");
                                                     FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                     user.delete();
                                                 }
                                             } else {
                                                 mp= MediaPlayer.create(VoterLogin.this,R.raw.error2);
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
                                                 Toast.makeText(VoterLogin.this, "Enter the correct OTP", Toast.LENGTH_SHORT).show();
                                             }
                                         }
                                     });
                         }
                         else {
                             mp= MediaPlayer.create(VoterLogin.this,R.raw.error2);
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
                             Toast.makeText(VoterLogin.this, "Please select the correct birthdate", Toast.LENGTH_SHORT).show();
                         }
                     } else {
                         mp= MediaPlayer.create(VoterLogin.this,R.raw.error2);
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
                         voterID.setError("Enter the correct Voter ID");
                     }
                 }
                 else {
                     mp= MediaPlayer.create(VoterLogin.this,R.raw.error2);
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
                     Toast.makeText(VoterLogin.this, "Please generate an OTP first", Toast.LENGTH_SHORT).show();
                 }
             }
         });
    }
}