package com.example.bigfamilyv20.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bigfamilyv20.R;
import com.example.bigfamilyv20.Utils.FirebaseDatabase;
import com.example.bigfamilyv20.Utils.InputErrors;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class confirmation_message extends AppCompatActivity {
   private static EditText confirmationCode;
   private static Button next;
   private static String phoneNumber;
   private static String PhoneCodeSent;
    private static final String TAG="CONFIRMATION SMS";
    private static FirebaseAuth auth;
    private static boolean coderight;
    public static boolean resendActive=false;
    public static TextView resend;
    public static TextView other;
    private static int x=60;
    private static Thread resender;
    private static String login_path;
    private static ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_message);
        resend=(TextView)findViewById(R.id.resendtext);
        progressBar=(ProgressBar)findViewById(R.id.confirmation_sms_progressbar);

        progressBar.setVisibility(View.INVISIBLE);

         resend.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(x==0){
                     sendVerificationSms(phoneNumber);
                     Toast.makeText(confirmation_message.this,"Code resent",Toast.LENGTH_SHORT).show();
                 }
                 else{
                     Toast.makeText(confirmation_message.this,"wait for code timeout",Toast.LENGTH_SHORT).show();
                 }

             }
         });

        auth=FirebaseAuth.getInstance();
        next=(Button)findViewById(R.id.confirmCode);
        login_path=getIntent().getExtras().get("login_path").toString();
        other=(TextView)findViewById(R.id.textView46);
        //resend.setVisibility(View.INVISIBLE);
        other.setVisibility(View.VISIBLE);
        confirmationCode=(EditText)findViewById(R.id.confirmCodeEditText);
        confirmationCode.setText("");
        phoneNumber=getIntent().getStringExtra("UserPhone");
        sendVerificationSms(phoneNumber);

       // phonecode=getIntent().getStringExtra("verificationCode");
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String confirmcode=confirmationCode.getText().toString();
                String Error=null;
                Error=new InputErrors().emptyEditTextError(confirmcode);

                if(Error!=null){
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(),"write a code to continue",Toast.LENGTH_LONG).show();
                }
                else {

                    verifyPhoneUser(confirmcode,PhoneCodeSent);

                }     }
        });
    }
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            Log.d(TAG, "onVerificationCompleted:" + credential);

            //signInWithPhoneAuthCredential(credential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.w(TAG, "onVerificationFailed", e);

            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                // ...
            } else if (e instanceof FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                // ...
            }

            // Show a message and update the UI
            // ...
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            super.onCodeSent(verificationId, token);
            PhoneCodeSent=verificationId;

            //resender.start();

            //Toast.makeText(getApplicationContext(),"it is done",Toast.LENGTH_SHORT).show();
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d(TAG, "onCodeSent:" + verificationId);

            // Save verification ID and resending token so we can use them later
            // mVerificationId = verificationId;
            //mResendToken = token;

            // ...
        }
    };
    public void sendVerificationSms(String phoneNumber){
         x=60;
         other.setText("Time out in");
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        new CountDownTimer(60000, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {
                resend.setText(""+String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                 x=0;
                 other.setText("Didnt get code ?");
                resend.setText("resend text");
            }
        }.start();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(confirmation_message.this)
                .setTitle("Cancel?")
                .setMessage("Going back will cancel your log in ")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setNegativeButton("go back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            Intent intent=new Intent(confirmation_message.this,Login_Start_Activity.class);
            startActivity(intent);
            finish();
            }
        }).show();
    }

    public  boolean verifyPhoneUser(String CodeEntered, String actualCode){
        PhoneAuthCredential credential = null;
        try {
            credential = PhoneAuthProvider.getCredential(actualCode, CodeEntered);
        }
        catch (Exception e){
            coderight=false;
        }
        signInWithPhoneAuthCredential(credential);

        return coderight;

    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:success");
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            if(login_path.equalsIgnoreCase("logInExistingUser")){
                                progressBar.setVisibility(View.INVISIBLE);
                                Intent intent = new Intent(getApplicationContext(), MainPage.class);
                                startActivity(intent);
                                finish();


                            }
                            else {
                                progressBar.setVisibility(View.INVISIBLE);
                                Intent intent=new Intent(getApplicationContext(),SignUp_SecondScreen.class);
                                intent.putExtra("UserPhone",phoneNumber);
                                startActivity(intent);
                                finish();

                            }



                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            progressBar.setVisibility(View.INVISIBLE);


                            new AlertDialog.Builder(confirmation_message.this)
                                        .setTitle("Wrong Code")
                                        .setMessage("you have entered a wrong code")
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Toast.makeText(getApplicationContext(), "write correct code", Toast.LENGTH_SHORT).show();
                                            }
                                        }).setNegativeButton("resend", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        if(x==0){
                                        sendVerificationSms(phoneNumber);
                                            Toast.makeText(confirmation_message.this,"Code resent",Toast.LENGTH_SHORT).show();
                                    }
                                        else {
                                            Toast.makeText(confirmation_message.this,"wait for timeout",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).show();


                        }
                    }
                });
    }


}
