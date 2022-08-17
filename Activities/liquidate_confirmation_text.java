package com.example.bigfamilyv20.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bigfamilyv20.Entities.User;
import com.example.bigfamilyv20.Entities.bulk_array;
import com.example.bigfamilyv20.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class liquidate_confirmation_text extends AppCompatActivity {

    private static FirebaseFirestore store=FirebaseFirestore.getInstance();
    private static List<String> names=new ArrayList<>();
    private static List<String> documentIds;
    private static ListView listView;
    private static Button finish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liquidate_confirmation_text);
        documentIds=new ArrayList<>();
        documentIds= bulk_array.getBdocumentIds();
        listView=(ListView)findViewById(R.id.list_liquidate);
        finish=(Button)findViewById(R.id.finish_liquidate);


        names=bulk_array.getBnames();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                names );

        listView.setAdapter(arrayAdapter);

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(liquidate_confirmation_text.this)
                        .setTitle("liquidate?")
                        .setMessage("are you sure you want to liquidate? ")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                  liquidateItems();
                                  sendNotification();
                                  Intent intent=new Intent(getApplicationContext(),MainPage.class);
                                  startActivity(intent);
                                  finish();
                            }
                        }).setNegativeButton("no",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();               ;


            }
        });

    }

    private  void liquidateItems(){

        WriteBatch batch=store.batch();
        for (int x=0;x<names.size();x++){

            DocumentReference reference=store
                    .collection("Users")
                    .document(User.getId())
                    .collection("BulkStore")
                    .document(documentIds.get(x));
            batch.delete(reference);

        }
        batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                sendNotification();
                createTransaction();
                liquidateMoney();
                Toast.makeText(liquidate_confirmation_text.this,"items lquidated, wait for cash ",Toast.LENGTH_LONG).show();


            }


        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


    }

    private void createTransaction() {

        Map<String,Object> transactingMap;
        transactingMap=createTransactionMap();
        store
                .collection("Users")
                .document(User.getId())
                .collection("Transactions")
                .document()
                .set(transactingMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private Map<String, Object> createTransactionMap() {

        Map<String,Object> transactingMap=new HashMap<>();
        transactingMap.put("initiator_FirstName", User.getFirstName());
        transactingMap.put("initiator_PhoneNumber", User.getPhoneNumber());
        transactingMap.put("initiator_title", "wholesalerTitle");
        transactingMap.put("WholeSalerName", "wholesalerName");
        transactingMap.put("b_productId", bulk_array.getBprodId());
        transactingMap.put("b_name", bulk_array.getBnames());
        transactingMap.put("b_amount",bulk_array.getBamount());
        transactingMap.put("b_description", bulk_array.getBdescription());
        transactingMap.put("b_price", bulk_array.getBprice());

        return transactingMap;
    }

    private void liquidateMoney() {

    }

    private void sendNotification() {
    }
}
