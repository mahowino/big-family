package com.example.bigfamilyv20.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.androidstudy.daraja.Daraja;
import com.androidstudy.daraja.DarajaListener;
import com.androidstudy.daraja.model.AccessToken;
import com.androidstudy.daraja.model.LNMExpress;
import com.androidstudy.daraja.model.LNMResult;
import com.androidstudy.daraja.util.TransactionType;
import com.example.bigfamilyv20.Entities.User;
import com.example.bigfamilyv20.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.annotation.Nullable;

public class wait_for_payment extends AppCompatActivity {
private FirebaseFirestore db= FirebaseFirestore.getInstance();
private static final int TimeOutTime=2;
private static  String reason,number,docs,pass,docId,price;
//time when i did the transactions
private static Long TransactionTime;
    private static Button push_Stk;
    private static Boolean is_shopkeeper_account;
    private static String PASSKEY="bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919";
    private static String CONS_KEY="vlNnSf7ywwY6pPmS8Al6JzUVvwuwLrvK";
    private static String CONSUMER_SECRET="a1YyGz3UfXDHcf3S";
    private static boolean loopRuns;
    private static ListenerRegistration listenerRegistration;
    String callbackurl="https://us-central1-big-family-auth-system.cloudfunctions.net/callbackUrl?usid="+User.getId();

    @Override
    protected void onStart() {
        //check how you store transaction time in your database
        //if current time< transaction time, difference <0
        //int = Integer.parseInt(new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date()));
        // loopRuns=true;
        listenerRegistration = db.collection("Users").document(User.getId()).collection("MpesaTransactions").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    Toast.makeText(wait_for_payment.this, "error in realtime", Toast.LENGTH_SHORT).show();
                    Log.i("tag", e.getMessage());
                }

                loopRuns = false;
                for (DocumentChange snapshot : queryDocumentSnapshots.getDocumentChanges()) {
                    if(snapshot.getType()== DocumentChange.Type.ADDED){

                        //check how you store transaction time in your database
                        Log.i("tag", TransactionTime + "is this");
                        Long currentTime = Long.parseLong(snapshot.getDocument().get("TransactionDate").toString());
                        Log.i("tag", currentTime + "is this");

                        int difference = currentTime.compareTo(TransactionTime);
                        //if current time< transaction time, difference <0
                        //int = Integer.parseInt(new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date()));
                        if (difference > 0 && loopRuns == false) {

                            Intent Confirmation = new Intent(getApplicationContext(), OrderConfirmed.class);
                            Confirmation.putExtra("charge", price);
                            Confirmation.putExtra("reason", reason);
                            Confirmation.putExtra("number", number);
                            Confirmation.putExtra("docs", docs);
                            Confirmation.putExtra("pass", pass);
                            Confirmation.putExtra("userDocId", docId);
                            Confirmation.putExtra("is_shopkeeper", is_shopkeeper_account);


                            startActivity(Confirmation);
                            finish();
                            listenerRegistration.remove();

                            loopRuns=true;

                        }
                    }


                }

            }
        });

        super.onStart();
    }

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_for_payment);

        push_Stk=findViewById(R.id.button_push_Stk);
        reason=getIntent().getExtras().getString("reason");
        docs=getIntent().getExtras().getString("docs") ;
        pass=getIntent().getExtras().getString("pass");
        docId=getIntent().getExtras().getString("userDocId");
        price=getIntent().getExtras().getString("charge");
        is_shopkeeper_account=getIntent().getExtras().getBoolean("is_shopkeeper");
       // String service=getIntent().getExtras().getString("Service");
        number=getIntent().getExtras().getString("number");
        TransactionTime= Long.valueOf(getIntent().getExtras().getString("TransactionTime"));
        push_Stk.setVisibility(View.INVISIBLE);
        push_Stk.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }

        });


    }
}
