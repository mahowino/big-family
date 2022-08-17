package com.example.bigfamilyv20.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bigfamilyv20.Entities.namesArray;
import com.example.bigfamilyv20.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class sendTo extends AppCompatActivity {
private ListView view;
private String[] names;
private static Button next;
private List<String> nameslist;
private List<String> Document_ids;
private List<String> priceList;
private List<Long> productIds;
private static EditText numbers;
private static FirebaseFirestore db;
private static String number_final_string;
private static Boolean is_shopkeeper;
private static int amount_for_final__charge;
private static int valueOfGoods;
private static int transactionCost;
private static String Locationshop;
private static List<DocumentReference> documentReferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_to);


        //declarations and assignments
        view=(ListView)findViewById(R.id.list_before_sending);
        nameslist=new ArrayList<>();
        productIds=new ArrayList<>();
        nameslist= namesArray.getName();
        priceList=new ArrayList<>();
        priceList=namesArray.getPrice();
        productIds=namesArray.getProductId();

        next=(Button)findViewById(R.id.next_to_send);
        numbers=(EditText)findViewById(R.id.number_to_send_to_edittext);
        db=FirebaseFirestore.getInstance();
        amount_for_final__charge=0;
        valueOfGoods=0;



        // Create the array adapter to bind the array to the listView
        names=new String[nameslist.size()];
        for (int x=0;x<nameslist.size();x++)

        {
            names[x]=nameslist.get(x);
        }

        final ArrayAdapter<String> aa;
        aa = new ArrayAdapter<String>( this,
                android.R.layout.simple_list_item_1,
                names
        );



        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String NUMBER=numbers.getText().toString().trim();

                //number validation
                if(!NUMBER.isEmpty()) {

                    if (NUMBER.length() == 13 || NUMBER.length()==10) {

                        if (NUMBER.startsWith("07")) {

                            int length = NUMBER.length();
                            NUMBER = NUMBER.substring(2, length);
                            NUMBER = "+2547" + NUMBER;

                        }

                        if (NUMBER.startsWith("7")) {

                            NUMBER = "+254" + NUMBER;

                        }

                        //get the total price worth of the goods
                        valueOfGoods=0;
                        for (int x=0;x<priceList.size();x++){

                            valueOfGoods=valueOfGoods+Integer.parseInt(priceList.get(x));
                        }

                        db
                                .collection("PricingList")
                                .get()


                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful()){

                                            Map<Integer,DocumentSnapshot> pricing=new TreeMap<>();

                                            for (DocumentSnapshot snapshot:task.getResult()){
                                                int highest_number=Integer.parseInt(snapshot.get("HighestPrice").toString());
                                                int difference=highest_number-valueOfGoods;

                                                if(difference>=0){
                                                    pricing.put(difference,snapshot);
                                                }

                                                else{

                                                }
                                            }
                                            Map.Entry<Integer,DocumentSnapshot> entry = pricing.entrySet().iterator().next();
                                            DocumentSnapshot pricingStandard=entry.getValue();
                                            transactionCost=Integer.parseInt(pricingStandard.get("CostOfTransaction").toString());

                                        }
                                    }
                                })


                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });

                        //get user to check if they are a shopkeeper
                        number_final_string=NUMBER;

                        db
                                .collection("Users")
                                .whereEqualTo("phoneNumber",NUMBER)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                                    @Override

                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                        if(task.isSuccessful()){

                                            if(task.getResult().isEmpty()){

                                               // Toast.makeText(getApplicationContext(),"mno",Toast.LENGTH_SHORT).show();
                                                Intent intent=new Intent(getApplicationContext(),Cart_Activity.class);
                                                intent.putExtra("price",String.valueOf(30));
                                                intent.putExtra("Service",String.valueOf(transactionCost+amount_for_final__charge));
                                                intent.putExtra("reason","sending of goods");
                                                intent.putExtra("number",number_final_string);
                                                intent.putExtra("is_shopkeeper",false);
                                                startActivity(intent);

                                            }
                                            else{

                                                for(DocumentSnapshot user_to_send_to: task.getResult()){
                                                    String id=user_to_send_to.getId();

                                                    db
                                                            .collection("Users")
                                                            .document(id)
                                                            .collection("shopkeeper_details")
                                                            .get()



                                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                                                    if(task.isSuccessful()){

                                                                        if(task.getResult().isEmpty()){

                                                                            for (DocumentSnapshot documentSnapshot:task.getResult()){
                                                                                if(documentSnapshot.exists()){

                                                                                    // user is a shopkeeper
                                                                                    is_shopkeeper=true;
                                                                                    Locationshop= documentSnapshot.get("Location").toString();


                                                                                }
                                                                            }
                                                                           // is_shopkeeper=true;
                                                                        }

                                                                        else{
                                                                            is_shopkeeper=false;
                                                                        }

                                                                        //   for (DocumentSnapshot documentSnapshot:task.getResult()){

                                                                        //is_shopkeeper=false;

                                                                        if(is_shopkeeper==true){

                                                                            //get the ids of all the products
                                                                            db.collection("Products")
                                                                                    .get()


                                                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                                                                            if (task.isSuccessful()){
                                                                                                Document_ids=new ArrayList<>();
                                                                                                for (DocumentSnapshot snapshot:task.getResult()){

                                                                                                    for (int x=0;x<nameslist.size();x++){

                                                                                                        Long productIdFromDatabase= snapshot.getLong("productCode");
                                                                                                        Toast.makeText(getApplicationContext(),"productcode is "+productIdFromDatabase,Toast.LENGTH_SHORT).show();

                                                                                                        if(productIds.get(x).equals(productIdFromDatabase)){
                                                                                                            Document_ids.add(snapshot.getId());
                                                                                                        }

                                                                                                    }

                                                                                                }


                                                                                                Toast.makeText(getApplicationContext(),"Document size is "+Document_ids.size(),Toast.LENGTH_SHORT).show();

                                                                                                db.runTransaction(new Transaction.Function<Object>() {
                                                                                                    @Nullable
                                                                                                    @Override
                                                                                                    public Object apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {

                                                                                                        //declarations
                                                                                                        documentReferences=new ArrayList<>();
                                                                                                        amount_for_final__charge=0;


                                                                                                        //getting the document references
                                                                                                        for(int x=0; x<Document_ids.size();x++){

                                                                                                            //get the extra charge for the goods in basket depending on the location
                                                                                                            DocumentReference reference=  db
                                                                                                                    .collection("Products")
                                                                                                                    .document(Document_ids.get(x))
                                                                                                                    .collection("location_prices")
                                                                                                                    .document(Locationshop);

                                                                                                            documentReferences.add(reference);
                                                                                                        }

                                                                                                        //getting the charge
                                                                                                        for (int x=0;x<Document_ids.size();x++){
                                                                                                            Log.i("Number1",String.valueOf(Integer.parseInt(transaction.get(documentReferences.get(x)).get("price").toString())));
                                                                                                            Log.i("Number2",String.valueOf(Integer.parseInt(priceList.get(x))));
                                                                                                            Log.i("Number3",String.valueOf(amount_for_final__charge));

                                                                                                            amount_for_final__charge = amount_for_final__charge+(Integer.parseInt(transaction.get(documentReferences.get(x)).get("price").toString())-Integer.parseInt(priceList.get(x)));
                                                                                                        }


                                                                                                        return null;
                                                                                                    }
                                                                                                })
                                                                                                        .addOnCompleteListener(new OnCompleteListener<Object>() {

                                                                                                            @Override

                                                                                                            public void onComplete(@NonNull Task<Object> task) {

                                                                                                                //extra charge computed successfully

                                                                                                                Intent intent=new Intent(getApplicationContext(),Cart_Activity.class);
                                                                                                                intent.putExtra("price",String.valueOf(30));
                                                                                                                intent.putExtra("Service",String.valueOf(transactionCost+amount_for_final__charge));
                                                                                                                intent.putExtra("reason","sending of goods");
                                                                                                                intent.putExtra("number",number_final_string);
                                                                                                                intent.putExtra("is_shopkeeper",is_shopkeeper);
                                                                                                                startActivity(intent);

                                                                                                            }
                                                                                                        })
                                                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                                                            @Override
                                                                                                            public void onFailure(@NonNull Exception e) {

                                                                                                                //failure in getting the data in the database
                                                                                                                Toast.makeText(getApplicationContext(),"Error communicating with the database",Toast.LENGTH_SHORT).show();
                                                                                                                e.printStackTrace();

                                                                                                            }


                                                                                                        });
                                                                                            }
                                                                                        }
                                                                                    })


                                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                                        @Override
                                                                                        public void onFailure(@NonNull Exception e) {

                                                                                            Toast.makeText(getApplicationContext(),"error is "+e.getMessage(),Toast.LENGTH_SHORT).show();

                                                                                        }
                                                                                    });
                                                                        }

                                                                        else {


                                                                            Toast.makeText(getApplicationContext(),"mno",Toast.LENGTH_SHORT).show();
                                                                            Intent intent=new Intent(getApplicationContext(),Cart_Activity.class);
                                                                            intent.putExtra("price",String.valueOf(30));
                                                                            intent.putExtra("Service",String.valueOf(transactionCost+amount_for_final__charge));
                                                                            intent.putExtra("reason","sending of goods");
                                                                            intent.putExtra("number",number_final_string);
                                                                            intent.putExtra("is_shopkeeper",is_shopkeeper);
                                                                            startActivity(intent);

                                                                        }



                                                                    }
                                                                    // Toast.makeText(getApplicationContext(),"its there",Toast.LENGTH_SHORT).show();

                                                                    //}


                                                                    else{

                                                                    }

                                                                }
                                                            })



                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(getApplicationContext(),"error is "+e.getMessage(),Toast.LENGTH_SHORT).show();

                                                                }
                                                            });


                                                }
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
                            Toast.makeText(sendTo.this,"write a valid number",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(sendTo.this,"Fill in the field",Toast.LENGTH_SHORT).show();
                }

            }
        });
        view.setAdapter(aa);

    }
}
