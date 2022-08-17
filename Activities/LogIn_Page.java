package com.example.bigfamilyv20.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bigfamilyv20.Entities.User;
import com.example.bigfamilyv20.R;
import com.example.bigfamilyv20.Utils.Password;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class LogIn_Page extends AppCompatActivity {
    private static EditText phone,pass;
    private static Button login;
    private static ProgressBar bar;
    private static FirebaseFirestore firebaseFirestore;
    private static CheckBox box;
    private static String docId,phoner,password;
    private static TextView signuP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in__page);
        login=(Button)findViewById(R.id.login_btn);
        phone=(EditText)findViewById(R.id.login_phone);
        pass=(EditText)findViewById(R.id.login_pass);
        bar=(ProgressBar)findViewById(R.id.bar_login);
        box=(CheckBox)findViewById(R.id.checkBox_SHOW_PASSWORRD);
        signuP=(TextView)findViewById(R.id.text_view_to_signUp);
        signuP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),SignUp_First_Screen.class);
                startActivity(intent);
                finish();
            }
        });
        box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(box.isChecked()){
                    pass.setTransformationMethod(null);
                }
                else{
                    pass.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bar.setVisibility(View.VISIBLE);
                 phoner=phone.getText().toString().trim();
                 password=pass.getText().toString().trim();

                if(!phoner.isEmpty() && !password.isEmpty()) {

                    if(phoner.length()<=10){
                    if(phoner.startsWith("07")){
                        int length=phoner.length();
                        phoner=phoner.substring(2,length);
                        phoner="+2547"+phoner;
                    }

                    if(phoner.startsWith("7")){
                      phoner="+254"+phoner;

                    }

                    firebaseFirestore = FirebaseFirestore.getInstance();
                    final CollectionReference reference1 = firebaseFirestore.collection("Users");
                    reference1
                            .whereEqualTo("phoneNumber", phoner)
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if(!task.getResult().isEmpty()){


                                    final User newUser=new User();
                                    for (DocumentSnapshot snapshot : task.getResult()) {
                                        docId=snapshot.getId();
                                        newUser.setId(docId);
                                        newUser.setFirstName(snapshot.get("FirstName").toString());
                                        newUser.setLastName(snapshot.get("LastName").toString());
                                        newUser.setPhoneNumber(snapshot.get("phoneNumber").toString());

                                    }

                                    reference1.document(docId).collection("securityDetail").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if(task.isSuccessful()){
                                                for (DocumentSnapshot snapshot1:task.getResult()){
                                                    String u_pass = snapshot1.get("U_PASS").toString();
                                                    String u_salt = snapshot1.get("U_SALT").toString();
                                                    if (Password.authenticatePassword(password, u_pass, u_salt) == true) {
                                                        bar.setVisibility(View.INVISIBLE);
                                                        Intent confirmation=new Intent(getApplicationContext(),confirmation_message.class);
                                                        confirmation.putExtra("UserPhone",phoner);
                                                        confirmation.putExtra("login_path","logInExistingUser");
                                                        startActivity(confirmation);

                                                    } else {
                                                        bar.setVisibility(View.INVISIBLE);
                                                        Toast.makeText(LogIn_Page.this, "Password mismatch", Toast.LENGTH_SHORT).show();
                                                    }

                                                }
                                            }}
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            bar.setVisibility(View.INVISIBLE);
                                            Toast.makeText(LogIn_Page.this, "error getting user cred", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                else
                                {
                                    bar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(LogIn_Page.this, "User does not exist", Toast.LENGTH_SHORT).show();
                                }

                        }}
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            bar.setVisibility(View.INVISIBLE);
                            e.printStackTrace();
                            Toast.makeText(LogIn_Page.this, "error getting user", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            else {
                        bar.setVisibility(View.INVISIBLE);
                    Toast.makeText(LogIn_Page.this, "Write a valid Phone Number", Toast.LENGTH_SHORT).show();
                }}
                else {
                    bar.setVisibility(View.INVISIBLE);
                    Toast.makeText(LogIn_Page.this, "Fill In All Details", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
