package com.example.bigfamilyv20.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bigfamilyv20.Entities.User;
import com.example.bigfamilyv20.Entities.shopkeeper_status;
import com.example.bigfamilyv20.R;
import com.example.bigfamilyv20.Utils.PasswordUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Password extends AppCompatActivity {
private static  String email,firstname,lastname,number;
private static EditText confirm,password;
private static ProgressBar bar;
private static Button lastStep;
private static final String SHOPKEEPER_DETAILS="shopkeeper_details";
private static Map<String,Object> shopkeeperDetails;
    FirebaseFirestore store=FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        //initializations
        email=getIntent().getExtras().getString("Email");
        firstname=getIntent().getExtras().getString("FirstName");
        lastname=getIntent().getExtras().getString("LastName");
        number=getIntent().getExtras().getString("UserPhone");
        confirm=(EditText)findViewById(R.id.ConfirmPassword);
        password=(EditText)findViewById(R.id.password);
        lastStep=(Button)findViewById(R.id.FinishSignUp);
        bar=(ProgressBar)findViewById(R.id.progressBarFinishSignUp);


        //onclick
        lastStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bar.setVisibility(View.VISIBLE);
                String password1=password.getText().toString().trim();
                String password2=confirm.getText().toString().trim();

                //validation
                if(!password.getText().toString().isEmpty() && !confirm.getText().toString().isEmpty()){
                    if(password1.length()>8){
                        if(checkPasswordMatch(password1,password2) ) {


                            //PUT INFO IN DATABASE
                            final CollectionReference reference1 = store.collection("Users");

                            //encrypt password
                            ArrayList<String> encryptedPass = com.example.bigfamilyv20.Utils.Password.encrypt(password1);
                            String salt = encryptedPass.get(0);
                            String encrypted = encryptedPass.get(1);

                            final Map<String, Object> UsersEncryptor = new HashMap<>();

                            //encrypted passwords hashmap
                            UsersEncryptor.put("U_PASS", encrypted);
                            UsersEncryptor.put("U_SALT", salt);

                            boolean is_shopkeeper= shopkeeper_status.isIs_shopkeeper();

                            //if shopkeeper create hashmap for shopkeeper
                            if (is_shopkeeper){
                                 shopkeeperDetails=new HashMap<>();
                                shopkeeperDetails.put("Location",shopkeeper_status.getCounty());
                                shopkeeperDetails.put("Sub_County",shopkeeper_status.getSubcounty());

                            }



                            //hashmap user details
                            final Map<String, Object> users = new HashMap<>();

                            users.put("FirstName", firstname.toLowerCase());
                            users.put("LastName", lastname.toLowerCase());
                            users.put("Email", email.toLowerCase());
                            users.put("phoneNumber", number.toLowerCase());

                            //get token for specific phone
                            String refreshToken= FirebaseInstanceId.getInstance().getToken();
                            users.put("U_Token",refreshToken);



                            //start adding user details first
                            reference1.add(users).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    String docId=documentReference.getId();
                                    User newUser=new User();
                                    newUser.setId(docId);
                                    newUser.setFirstName(firstname.toLowerCase());
                                    newUser.setLastName(lastname.toLowerCase());
                                    newUser.setPhoneNumber(number.toLowerCase());

                                    //add security details next
                                    reference1.document(docId).collection("securityDetail").add(UsersEncryptor).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {


                                            //add shopkeeper location if user is a shopkeeper
                                            if(is_shopkeeper){

                                                reference1.document(docId).collection(SHOPKEEPER_DETAILS).add(shopkeeperDetails)
                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                            @Override
                                                            public void onSuccess(DocumentReference documentReference) {

                                                                Intent mainpage=new Intent(getApplicationContext(),MainPage.class);

                                                                startActivity(mainpage);
                                                                finish();

                                                            }
                                                        })

                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {

                                                            }
                                                        });

                                            }
                                            else{

                                                Intent mainpage=new Intent(getApplicationContext(),MainPage.class);

                                                startActivity(mainpage);
                                                finish();

                                            }

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            //DELETE ALL THE DOCUMENTS CREATED EARLIER
                                            //SHOW USER A MESSAGE
                                            //clear User
                                            User newuser=new User();
                                            newuser.setId(null);
                                            newuser.setFirstName(null);
                                            newuser.setLastName(null);
                                            newuser.setPhoneNumber(null);

                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //SHOW USER A MESSAGE
                                    //clear User
                                    User newuser=new User();
                                    newuser.setId(null);
                                    newuser.setFirstName(null);
                                    newuser.setLastName(null);
                                    newuser.setPhoneNumber(null);
                                }
                            });

                        }
                        else
                        {
                            Toast.makeText(Password.this,"Passwords does not match",Toast.LENGTH_SHORT).show();

                        }
                    }
                    else {

                        Toast.makeText(Password.this,"write a longer password",Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {
                    Toast.makeText(Password.this,"write a longer password",Toast.LENGTH_SHORT).show();
                }

                }


        });

    }

    private boolean checkPasswordMatch(String password,String Confirm) {
    if(password.equals(Confirm)){
        return true;
    }
    else
        {
            return false;
        }
    }
}
