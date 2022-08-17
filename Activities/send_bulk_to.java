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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class send_bulk_to extends AppCompatActivity {

    private static EditText number;
    private static Button finish;
    private static FirebaseFirestore store=FirebaseFirestore.getInstance();
    private static String number_To_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_bulk_to);
        number=(EditText)findViewById(R.id.bulk_number_to_Send);
        finish=(Button)findViewById(R.id.send_bulk_confirm);

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number_To_send=number.getText().toString().trim();
                if(!number_To_send.isEmpty()) {

                    if(number_To_send.length() == 13 || number_To_send.length()==10) {
                        if (number_To_send.startsWith("07")) {
                            int length = number_To_send.length();
                            number_To_send = number_To_send.substring(2, length);
                            number_To_send = "+2547" + number_To_send;
                        }

                        if (number_To_send.startsWith("7")) {
                            number_To_send = "+254" + number_To_send;

                        }

                        store
                                .collection("Users")
                                .whereEqualTo("phoneNumber", number_To_send)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                        if (task.isSuccessful()) {

                                            if (!task.getResult().isEmpty()) {

                                                String docId = "";
                                                for (DocumentSnapshot snapshot : task.getResult()) {

                                                    if (snapshot.get("phoneNumber").toString().equalsIgnoreCase(number_To_send)) {
                                                        docId = snapshot.getId();
                                                    }

                                                }

                                                //final String phoneNumber=getIntent().getExtras().getString("phoneNumber");
                                                // final String senderDocumentId=getIntent().getExtras().getString("userDocId");
                                                Intent intent = new Intent(getApplicationContext(), Cart_Activity.class);
                                                intent.putExtra("userDocId", docId);
                                                intent.putExtra("price", "30");
                                                intent.putExtra("Service", "Sending_bulk_goods");
                                                intent.putExtra("number", number_To_send);
                                                intent.putExtra("reason", "Sending_bulk_goods");
                                                intent.putExtra("docs", "0");
                                                intent.putExtra("pass", "0");
                                                startActivity(intent);

                                            } else {
                                                Toast.makeText(send_bulk_to.this, "No such number exists", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });

                    }
                    else
                    {
                        Toast.makeText(send_bulk_to.this, "write a valid Number", Toast.LENGTH_LONG).show();
                    }

                    }
                else
                {
                    Toast.makeText(send_bulk_to.this, "Input field is empty", Toast.LENGTH_LONG).show();
                }

            }
        });


    }
}
