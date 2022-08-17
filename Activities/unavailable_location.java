package com.example.bigfamilyv20.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bigfamilyv20.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class unavailable_location extends AppCompatActivity {
    private static EditText location;
    private static Button advance;
    private static FirebaseFirestore db;
    private static String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unavailable_location);

        location=findViewById(R.id.new_location);
        advance=findViewById(R.id.advance_no_new_location);
        db=FirebaseFirestore.getInstance();
        number=getIntent().getExtras().getString("UserPhone");

        advance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //validate input
                String input=location.getText().toString().trim().toLowerCase();

                if (input.isEmpty()){
                    Toast.makeText(getApplicationContext(),"You have to input text",Toast.LENGTH_SHORT).show();
                }

                else
                {
                    Map<String, Object> location=new HashMap<>();
                    location.put("Location",input);
                    location.put("Number",number);
                    //send to database

                    db
                            .collection("location_requests")
                            .add(location)

                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {


                                    Toast.makeText(getApplicationContext(),"We will notify you when we are available in your location",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), unavailable_location.class);
                                    intent.putExtra("UserPhone", number);
                                    intent.putExtra("login_path","SignUpNewUser");
                                    //intent.putExtra("verificationCode",PhoneCodeSent);
                                    startActivity(intent);
                                    finish();
                                }
                            })

                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });

                }



                //intent to sign up second screen

            }
        });
    }
}
