package com.example.bigfamilyv20.Activities;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.bigfamilyv20.R;
import com.example.bigfamilyv20.Utils.FirebaseDatabase;
import com.example.bigfamilyv20.Utils.InputErrors;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SignUp_First_Screen extends Activity {
    private static EditText phoneNumber;
    private static Button nextStep;
    private static ConstraintLayout errorLayout;
    private static TextView ErrorMessage;
    private static String Error;
    private static String TAG="FirebaseDatabase";
   private static Switch is_shopkeeper;
    private static String collectionref="/Users";
    private static ArrayList<QueryDocumentSnapshot> numbers;
    private static ProgressBar bar;
    private static String PhoneCodeSent;
    private static String phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up__first__screen);

        //initialization
        is_shopkeeper=findViewById(R.id.sign_up_shopkeeper);
        phoneNumber=(EditText)findViewById(R.id.PhoneNumberSignUp);
        nextStep=(Button)findViewById(R.id.SignupNextStep);
       //errorLayout=(ConstraintLayout)findViewById(R.id.ErrorTabLogIn);
        //ErrorMessage=(TextView)findViewById(R.id.ErrorMessageSignUp);
        bar=(ProgressBar)findViewById(R.id.SignUpProgressBar);
        bar.setVisibility(View.INVISIBLE);
        phoneNumber.setText("");

          nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bar.setVisibility(View.VISIBLE);
                phone=phoneNumber.getText().toString().trim().toLowerCase();
                Error=null;

                Error=new InputErrors().emptyEditTextError(phone);

                if(Error==null) {
                    numbers=new ArrayList<>();
                    Error=new InputErrors().phoneNumberValid(phone);
                    if(Error==null){
                        phone="+254"+phone;
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    CollectionReference reference = db.collection(collectionref);
                    reference
                            .whereEqualTo("phoneNumber", phone)
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                boolean error = false;
                                //int y=0;
                                    for (QueryDocumentSnapshot snapshot : task.getResult()) {

                                        if (snapshot.get("phoneNumber").toString().equalsIgnoreCase(phone)) {
                                            Toast.makeText(getApplicationContext(),"Phone number already exists",Toast.LENGTH_SHORT).show();
                                            bar.setVisibility(View.INVISIBLE);
                                            //showError("Phone number already exists");
                                            error=true;
                                            break;

                                        }

                                }


                                if (error == false) {

                                    bar.setVisibility(View.INVISIBLE);
                                    bar.setVisibility(View.GONE);

                                    if (is_shopkeeper.isChecked()){

                                        Intent intent = new Intent(getApplicationContext(), location_activity.class);
                                        intent.putExtra("UserPhone", phone);
                                        intent.putExtra("login_path","SignUpNewUser");
                                        //intent.putExtra("verificationCode",PhoneCodeSent);
                                        startActivity(intent);
                                        finish();

                                    }


                                    else {

                                        Intent intent = new Intent(getApplicationContext(), confirmation_message.class);
                                        intent.putExtra("UserPhone", phone);
                                        intent.putExtra("login_path", "SignUpNewUser");
                                        //intent.putExtra("verificationCode",PhoneCodeSent);
                                        startActivity(intent);
                                        finish();

                                    }
                                }



                            }
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            bar.setVisibility(View.INVISIBLE);
                            numbers.clear();
                            bar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(),"error getting data",Toast.LENGTH_LONG).show();

                        }
                    });







                }
                else {
                        Toast.makeText(getApplicationContext(),Error,Toast.LENGTH_SHORT).show();
                        bar.setVisibility(View.INVISIBLE);
                       // showError(Error);

                    }  }
                else {
                    Toast.makeText(getApplicationContext(),Error,Toast.LENGTH_SHORT).show();
                    bar.setVisibility(View.INVISIBLE);
                    //showError(Error);
                }
            }
        });

    }

    private void verify(String phone) {

    }



    private static void showError(String Error){

        bar.setVisibility(View.INVISIBLE);
        errorLayout.setVisibility(View.VISIBLE);
        ErrorMessage.setText(Error);
        ErrorMessage.setVisibility(View.VISIBLE);
        Thread errortime = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                    errorLayout.setVisibility(View.INVISIBLE);
                    //ErrorMessage.setText(Error);
                    ErrorMessage.setVisibility(View.INVISIBLE);
                } catch (Exception e) {
                    //Toast.makeText(SignUp_First_Screen.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        };
        errortime.start();
    }

}
