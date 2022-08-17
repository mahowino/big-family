package com.example.bigfamilyv20.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bigfamilyv20.Entities.User;
import com.example.bigfamilyv20.Entities.bulk_array;
import com.example.bigfamilyv20.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class liquidate_bulk extends AppCompatActivity {

    private static Button next;
    private static EditText wholesaleId,wholesaleSecret;
    private static FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
    private static ArrayList<Long> productIds;
    private static List<String> BdocumentIds,Bnames,Bdescription,Bprice;
    private static List<Long>BprodId;
    private static List<Integer>Bamount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liquidate_bulk);
        wholesaleId=(EditText)findViewById(R.id.wholesale_id);
        wholesaleSecret=(EditText)findViewById(R.id.wholesale_Secret);


        BdocumentIds=new ArrayList<>();
        Bnames=new ArrayList<>();
        Bdescription=new ArrayList<>();
        Bprice=new ArrayList<>();
        BprodId=new ArrayList<>();
        Bamount=new ArrayList<>();


        next=(Button)findViewById(R.id.confirmLiquidate_now);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name=wholesaleId.getText().toString().trim();
                String secret=wholesaleSecret.getText().toString().trim();

                if(name==null || secret==null)
                {
                    Toast.makeText(liquidate_bulk.this,"Fill in both fields",Toast.LENGTH_LONG).show();
                }
                else
                {
                    firebaseFirestore
                            .collection("Users")
                            .document(User.getId())
                            .collection("Wholesale_details")
                            .whereEqualTo("wholesale_id",name)
                            .whereEqualTo("wholesale_secret",secret)
                            .get()

                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                    if(task.isSuccessful()){

                                        if(!task.getResult().isEmpty()){
                                            productIds=new ArrayList<>();
                                            for (DocumentSnapshot snapshot:task.getResult()){
                                                productIds=(ArrayList<Long>) snapshot.get("items_allowed_to_bulk");
                                                //ProductId=Long.parseLong(snapshot.get("productId").toString());
                                            }

                                            firebaseFirestore
                                                    .collection("Users")
                                                    .document(User.getId())
                                                    .collection("BulkStore")
                                                    .get()

                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                                            boolean isInBulkStore=false;
                                                            bulk_array bulkArray=new bulk_array();

                                                            if(task.isSuccessful()){

                                                                for (DocumentSnapshot snapshot:task.getResult()){

                                                                    Long prods=snapshot.getLong("b_productId");
                                                                    for (int x=0;x<productIds.size();x++){

                                                                        if(productIds.get(x).equals(prods)){
                                                                            isInBulkStore=true;

                                                                            BdocumentIds.add(snapshot.getId());
                                                                            Bnames.add(snapshot.getString("b_name"));
                                                                            Bdescription.add(snapshot.getString("b_description"));
                                                                            Bprice.add(snapshot.getString("b_price"));
                                                                            BprodId.add(prods);
                                                                            Bamount.add(snapshot.getLong("b_amount").intValue());



                                                                        }


                                                                    }

                                                                }

                                                                if(isInBulkStore){

                                                                    bulkArray.setBdocumentIds(BdocumentIds);
                                                                    bulkArray.setBnames(Bnames);
                                                                    bulkArray.setBdescription(Bdescription);
                                                                    bulkArray.setBprice(Bprice);
                                                                    bulkArray.setBprodId(BprodId);
                                                                    bulkArray.setBamount( Bamount);

                                                                    Intent intent=new Intent(getApplicationContext(), liquidate_confirmation_text.class);
                                                                    startActivity(intent);
                                                                    finish();

                                                                }
                                                                else{
                                                                    Toast.makeText(liquidate_bulk.this,"No Liquidatable item in basket",Toast.LENGTH_LONG).show();
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
                                        else {
                                            Toast.makeText(liquidate_bulk.this,"No such combination",Toast.LENGTH_LONG).show();
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







            }
        });
    }
}
