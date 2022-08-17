package com.example.bigfamilyv20.Activities;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import com.example.bigfamilyv20.Entities.User;
import com.example.bigfamilyv20.R;
import com.example.bigfamilyv20.SendNotificationPack.Token;
import com.example.bigfamilyv20.Utils.InternetChecker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class MainActivity extends AppCompatActivity {
private static ConstraintLayout constraintLayout;
private static FirebaseUser user;
private static FirebaseAuth mAuth=FirebaseAuth.getInstance();
private static FirebaseFirestore firebaseFirestore;
/*
    @Override
    protected void onStart() {
        user=mAuth.getCurrentUser();
        if (user!=null){
            Intent home=new Intent(MainActivity.this, MainPage.class);
            startActivity(home);
            finish();
        }
        super.onStart();
    }
    */

    @Override
    protected void onStart() {
        //get the existing active user;

        user=mAuth.getCurrentUser();

        if(user!=null){
            //user already logged in
        String phone=user.getPhoneNumber();
            firebaseFirestore = FirebaseFirestore.getInstance();
            final CollectionReference reference1 = firebaseFirestore.collection("Users");
           //get user info

            reference1
                    .whereEqualTo("phoneNumber",phone)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {

                        User newUser=new User();
                        for (DocumentSnapshot snapshot : task.getResult()) {
                            //populate User Data Onto User Object

                            String docId=snapshot.getId();
                            newUser.setId(docId);
                            newUser.setFirstName(snapshot.get("FirstName").toString());
                            newUser.setLastName(snapshot.get("LastName").toString());
                            newUser.setPhoneNumber(snapshot.get("phoneNumber").toString());


                        }
                        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                            @Override
                            public void onComplete(@NonNull Task<InstanceIdResult> task) {

                                if (task.isSuccessful()){
                                    final String refreshToken=task.getResult().getToken();
                                    FirebaseFirestore.getInstance().runTransaction(new Transaction.Function<Void>() {
                                        @Nullable
                                        @Override
                                        public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                                            DocumentReference reference=FirebaseFirestore.getInstance().collection("Users").document(User.getId());
                                            transaction.update(reference,"U_Token",refreshToken);
                                            //Toast.makeText(MainActivity.this,"TOKEN is "+refreshToken,Toast.LENGTH_LONG).show();
                                            Log.i("U_Token", "apply: "+refreshToken);

                                            return null;
                                        }
                                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            //open MainPage
                                            Intent intent=new Intent(getApplicationContext(), MainPage.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            e.printStackTrace();
                                        }
                                    });
                                }

                            }
                        });

                }
            }});
            }
        else {
            //test this out
            //logic is if user !=null then
            Intent intent=new Intent(MainActivity.this,Login_Start_Activity.class);
            startActivity(intent);
            finish();
        }
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user=mAuth.getCurrentUser();


        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.INTERNET,Manifest.permission.SEND_SMS,Manifest.permission.READ_SMS,Manifest.permission.ACCESS_NETWORK_STATE,Manifest.permission.BROADCAST_SMS}, PackageManager.PERMISSION_GRANTED);
        constraintLayout = (ConstraintLayout) findViewById(R.id.SplashscreenBackground);
            // FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

             //Token token=new Token(refreshToken);

            createNotificationChannel("Receive Notifications","channel to get notifications","receiveNotifications");


        Thread thread =new Thread(){
           @Override
           public void run() {
               try {

                       sleep(3000);





               }
               catch (Exception e){

               }
           }
        };

            thread.start();


        Thread checkInternetConnection =new Thread(){
            @Override
            public void run() {
                super.run();
             boolean internetchecker= (boolean) InternetChecker.internetIsConnected();
             while (internetchecker==true){

             }
             if(internetchecker==false){
                 Intent intent=new Intent(MainActivity.this,noInternet.class);
                 startActivity(intent);
                 finish();
             }
            }
        };
        checkInternetConnection.start();
        }
    private void createNotificationChannel(String nameChannel,String desc,String ID) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = nameChannel;
            String description = desc;
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    }

