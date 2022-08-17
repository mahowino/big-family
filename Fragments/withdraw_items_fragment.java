package com.example.bigfamilyv20.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bigfamilyv20.Activities.view_token_items;
import com.example.bigfamilyv20.Activities.withdraw_items;
import com.example.bigfamilyv20.Entities.namesArray;
import com.example.bigfamilyv20.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class withdraw_items_fragment extends Fragment {

    private static EditText doc_name,doc_pass;
    private static Button withdraw;
    private static FirebaseFirestore firebaseFirestore;
    private static List<String> p_names=new ArrayList<>(),p_documentsIds=new ArrayList<>();
    private static List<String> p_description=new ArrayList<>();
    private static List<String> p_price=new ArrayList<>();
    private static List<Long> p_prodId=new ArrayList<Long>();
    private static List<Integer> p_amount=new ArrayList<>();
    private static int x;


    public withdraw_items_fragment() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View Withdraw_items=inflater.inflate(R.layout.activity_withdraw_items, container, false);
        // Inflate the layout for this fragment
        doc_name=(EditText)Withdraw_items.findViewById(R.id.document_name);
        doc_pass=(EditText)Withdraw_items.findViewById(R.id.document_password);
        withdraw=(Button)Withdraw_items.findViewById(R.id.withdraw_items_btn);
        firebaseFirestore=FirebaseFirestore.getInstance();

        withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String DocumentName=doc_name.getText().toString().trim();
                final String DocumentPass=doc_pass.getText().toString().trim();
                if(!DocumentName.isEmpty() && !DocumentPass.isEmpty()){
                    firebaseFirestore
                            .collection("TokenAccounts")
                            .document(DocumentName)
                            .collection(DocumentPass)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    x=0;
                                    p_prodId.clear();
                                    p_names.clear();
                                    p_price.clear();
                                    p_description.clear();
                                    p_amount.clear();
                                    p_documentsIds.clear();
                                    for (DocumentSnapshot snapshot:task.getResult()){
                                        if(snapshot.exists()){

                                            p_documentsIds.add(x,snapshot.getId());
                                            String NAME = snapshot.get("p_name").toString();
                                            String DESC = snapshot.get("p_description").toString();
                                            String PRICE = snapshot.get("P_price").toString();
                                            int amount=snapshot.getLong("p_amount").intValue();
                                            Long PROD_ID = snapshot.getLong("P_ID");
                                            p_prodId.add(x,PROD_ID);
                                            p_names.add(x,NAME);
                                            p_price.add(x,PRICE);
                                            p_description.add(x,DESC);
                                            p_amount.add(x,amount);
                                            x++;
                                        }

                                    }

                                    if(p_documentsIds.isEmpty() || p_documentsIds.equals(null)){


                                        Toast.makeText(getContext(),"no such combination existts",Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        namesArray namesarray=new namesArray();
                                        namesarray.setName(p_names);
                                        namesarray.setProductId(p_prodId);
                                        namesarray.setPrice(p_price);
                                        namesarray.setDescription(p_description);
                                        namesarray.setAmount(p_amount);
                                        namesarray.setDocIds(p_documentsIds);
                                        Intent intent=new Intent(getContext(), view_token_items.class);
                                        intent.putExtra("docs",DocumentName);
                                        intent.putExtra("pass",DocumentPass);
                                        startActivity(intent);
                                       // finish();

                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {



                        }
                    })  ;
                }
                else
                {
                    Toast.makeText(getContext(),"Fill in all the details",Toast.LENGTH_LONG).show();
                }

            }
        });
        return Withdraw_items;
    }
}