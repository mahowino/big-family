package com.example.bigfamilyv20.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidstudy.daraja.network.ApiClient;
import com.example.bigfamilyv20.Entities.User;
import com.example.bigfamilyv20.Entities.bulk_array;
import com.example.bigfamilyv20.Entities.bulk_liquidate;
import com.example.bigfamilyv20.Entities.namesArray;
import com.example.bigfamilyv20.Entities.wholesale_bulk;
import com.example.bigfamilyv20.R;
import com.example.bigfamilyv20.SendNotificationPack.APIService;
import com.example.bigfamilyv20.SendNotificationPack.Client;
import com.example.bigfamilyv20.SendNotificationPack.Data;
import com.example.bigfamilyv20.SendNotificationPack.DataModel;
import com.example.bigfamilyv20.SendNotificationPack.Jsoner;
import com.example.bigfamilyv20.SendNotificationPack.MyResponse;
import com.example.bigfamilyv20.SendNotificationPack.NotificationSender;
import com.example.bigfamilyv20.SendNotificationPack.Token;
import com.example.bigfamilyv20.Utils.Database_helper;
import com.example.bigfamilyv20.Utils.LoadingUtils;
import com.example.bigfamilyv20.Utils.Sender;
import com.example.bigfamilyv20.Utils.shoppingCartUtilis;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;
import com.kusu.loadingbutton.LoadingButton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;


public class OrderConfirmed extends AppCompatActivity {
private static TextView charges,reasons;
private static Button confirmation;
private static Database_helper helper;
private static shoppingCartUtilis utilis;
private static int x,g,h,p,d;
private static List<String> pname,pDesc,P_DOCUMENTIDS,FROM_STORE_IDS,StringIds,p_prices;
private static List<Integer> price,amount,FROM_STORE_AMOUNT,amount_bulked_up;
private static List<Long> ids,IDS_SENDER_STORE,choosing_ids;
private static Boolean isfinished,iscomplete;
private static LoadingUtils loadingUtils;
private static FirebaseFirestore store=FirebaseFirestore.getInstance();
private static LoadingButton loadingButton;
private APIService apiService;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmed);

        //initializations
        apiService= Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        charges=(TextView) findViewById(R.id.charges);
        confirmation=(Button)findViewById(R.id.button_final_confirm_activities);
        reasons=(TextView)findViewById(R.id.reason_of);

        confirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(confirmation.getText().toString().trim().equalsIgnoreCase("Wait ..do not leave page")){

                    new AlertDialog.Builder(OrderConfirmed.this)
                            .setTitle("Confirm")
                            .setMessage("Do You Want to cancel? this may cause errors!")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("no", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    //logout dismissed
                                }
                            }).setNegativeButton("yes", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent=new Intent(getApplicationContext(),MainPage.class);
                            startActivity(intent);
                            finish();

                        }
                    }).show();


                }
                else if (confirmation.getText().toString().trim().equalsIgnoreCase("finish transaction")){

                    Intent intent=new Intent(getApplicationContext(),MainPage.class);
                    startActivity(intent);
                    finish();

                }
                else{
                    Toast.makeText(OrderConfirmed.this,"Error in completing",Toast.LENGTH_LONG).show();
                }
            }
        });

        String charge=getIntent()
                .getExtras()
                .getString("charge");

        String reason=getIntent()
                .getExtras()
                .getString("reason");

        pname=new ArrayList<>();
        pDesc=new ArrayList<>();
        price=new ArrayList<>();
        amount=new ArrayList<>();
        ids=new ArrayList<>();
        choosing_ids=new ArrayList<>();
        p_prices=new ArrayList<>();
        StringIds=new ArrayList<>();
        IDS_SENDER_STORE=new ArrayList<>();
        P_DOCUMENTIDS=new ArrayList<>();
        FROM_STORE_IDS=new ArrayList<>();
        FROM_STORE_AMOUNT=new ArrayList<>();
        amount_bulked_up=new ArrayList<>();

        utilis=new shoppingCartUtilis(this);
        helper=new Database_helper(this);

        charges.setText(charge);
        reasons.setText(reason);


        //recode this area
        if(reason.equalsIgnoreCase("sending of goods")){


            //get the goods in the basket
            SQLiteDatabase db=utilis.getReadableDatabase();
            Cursor cursor =utilis.getdata(db);
            getDatbaseData(cursor,0);

            final String PhoneNumber=getIntent()
                    .getExtras()
                    .getString("number");

            final CollectionReference reference1 = store.collection("Users");
            reference1
                    .whereEqualTo("phoneNumber",PhoneNumber)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    if(task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {

                        for (DocumentSnapshot snapshot : task.getResult()) {

                            if (snapshot.exists()) {

                                final String documentid = snapshot.getId();
                                reference1.document(documentid)
                                        .collection("Store")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                            g = 0;
                                        for (DocumentSnapshot snapshot1 : task.getResult()) {

                                            IDS_SENDER_STORE.add(g, snapshot1.getLong("P_ID"));
                                            String id = snapshot1.getId();
                                            FROM_STORE_IDS.add(g, id);
                                            FROM_STORE_AMOUNT.add(g, Integer.parseInt(snapshot1.get("p_amount").toString()));
                                            g++;

                                        }
                                        updateStoresAfterSending(PhoneNumber, documentid);

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                    }
                            else{
                               Toast.makeText(OrderConfirmed.this,"Error in getting Sending data",Toast.LENGTH_LONG).show();
                            }
                }
            } else {

                store
                        .collection("TokenAccounts")
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            p = 0;
                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                if (documentSnapshot.exists()) {
                                    StringIds.add(p, documentSnapshot.getId());
                                }
                                p++;
                                Log.i("send goods", "onComplete: documents " + StringIds.get(p));
                            }

                            Sender sender = new Sender();
                            String DocumentName = sender.generateRandomString(6, StringIds);
                            Log.i("send goods", "onComplete: documentName " + DocumentName);
                            String password = String.valueOf(sender.generateRandomcode(99999, 00000));
                            Log.i("send goods", "onComplete: password " + password);
                            updateStoresAfterTokenSending(DocumentName, password,PhoneNumber);
                        }

                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {


                            }
                        });
            }

        }    }})


                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

        }



        //purchase of goods
        else if(reason.equalsIgnoreCase("Purchase of goods")){

            //get the items from the database
            SQLiteDatabase db=helper.getReadableDatabase();
            Cursor cursor =helper.getdata(db);
            getDatbaseData(cursor,1);


            final CollectionReference reference1 = store.collection("Users");
            reference1.document(User.getId())
                    .collection("Store")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                         int g=0;
                         if(task.isSuccessful()) {
                            for (DocumentSnapshot snapshot1 : task.getResult()) {
                                //populate the arraylists
                                IDS_SENDER_STORE.add(g, snapshot1.getLong("P_ID"));
                                String id = snapshot1.getId();
                                FROM_STORE_IDS.add(g, id);
                                FROM_STORE_AMOUNT.add(g, Integer.parseInt(snapshot1.get("p_amount").toString()));
                            //fix issue here

                                g++;
                            //here
                        }
                                updateStoreFromBuying();
                    }
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
         }

        //withdrawal of goods
        //change the code here

        else if(reason.equalsIgnoreCase("token_withdrawal")) {

            //get the document and password details of token item
            final String docs=getIntent()
                    .getExtras()
                    .getString("docs");

            final String pass=getIntent().getExtras().get("pass").toString();

            //get reference to te user store
            final CollectionReference reference1 = store.collection("Users");
            reference1
                    .document(User.getId())
                    .collection("Store")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        int g=0;
                            if(task.isSuccessful()) {
                                for (DocumentSnapshot snapshot1 : task.getResult()) {

                                    IDS_SENDER_STORE.add(g, snapshot1.getLong("P_ID"));
                                    String id = snapshot1.getId();
                                    FROM_STORE_IDS.add(g, id);
                                    FROM_STORE_AMOUNT.add(g, Integer.parseInt(snapshot1.get("p_amount").toString()));
                                    g++;

                                }

                                    pname= namesArray.getName();
                                    pDesc= namesArray.getDescription();
                                    P_DOCUMENTIDS= namesArray.getDocIds();
                                    //confirm how price is stored in the database first
                                    p_prices= namesArray.getPrice();//price is stores as a string,may be the cause of error
                                    ids= namesArray.getProductId();
                                    amount= namesArray.getAmount();
                                Log.i("bulk next", "onClick: "+pname.get(0));
                                Log.i("bulk next", "onClick: size "+pname.size());
                                Log.i("bulk next", "onClick: "+pDesc.get(0));
                                Log.i("bulk next", "onClick:size "+pDesc.size());
                                Log.i("bulk next", "onClick: "+p_prices.get(0));
                                Log.i("bulk next", "onClick: size "+p_prices.size());
                                Log.i("bulk next", "onClick: "+ids.get(0));
                                Log.i("bulk next", "onClick: size "+ids.size());
                                Log.i("bulk next", "onClick: "+amount.get(0));
                                Log.i("bulk next", "onClick: size "+amount.size());
                                Log.i("bulk next", "onClick: "+P_DOCUMENTIDS.get(0));
                                Log.i("bulk next", "onClick: size "+P_DOCUMENTIDS.size());
                                Log.i("bulk next", "onClick: "+IDS_SENDER_STORE.get(0));
                                Log.i("bulk next", "onClick: size "+IDS_SENDER_STORE.size());
                                Log.i("bulk next", "onClick: "+FROM_STORE_IDS.get(0));
                                Log.i("bulk next", "onClick: size "+FROM_STORE_IDS.size());

                                withdrawToken(docs,pass);


                    }
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });


        }
        //bulk goods up

        else if(reason.equalsIgnoreCase("bulk_goods_up")){
            //getting the items to be bulked up

            pname= wholesale_bulk.getBnames();
            pDesc=wholesale_bulk.getBdescription();
            p_prices=wholesale_bulk.getBprice();
            ids=wholesale_bulk.getBprodId();
            amount=wholesale_bulk.getNextBulk();
            P_DOCUMENTIDS=wholesale_bulk.getBdocumentIds();
            amount_bulked_up=wholesale_bulk.getBsmakk_amount();



            Log.i("bulk next", "onClick: "+pname.get(0));
            Log.i("bulk next", "onClick: size "+pname.size());
            Log.i("bulk next", "onClick: "+pDesc.get(0));
            Log.i("bulk next", "onClick:size "+pDesc.size());
            Log.i("bulk next", "onClick: "+p_prices.get(0));
            Log.i("bulk next", "onClick: size "+p_prices.size());
            Log.i("bulk next", "onClick: "+ids.get(0));
            Log.i("bulk next", "onClick: size "+ids.size());
            Log.i("bulk next", "onClick: "+amount.get(0));
            Log.i("bulk next", "onClick: size "+amount.size());
            Log.i("bulk next", "onClick: "+P_DOCUMENTIDS.get(0));
            Log.i("bulk next", "onClick: size "+P_DOCUMENTIDS.size());


            //getting the existing items in the bulk store of the user
            final CollectionReference reference1 = store.collection("Users");
            reference1.document(User.getId()).collection("BulkStore").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    g=0;
                    for(DocumentSnapshot snapshot:task.getResult()){

                        //populating the variables from the bulk store of user
                        IDS_SENDER_STORE.add(g, snapshot.getLong("b_productId"));
                        String id = snapshot.getId();
                        FROM_STORE_IDS.add(g, id);
                        FROM_STORE_AMOUNT.add(g, Integer.parseInt(snapshot.get("b_amount").toString()));
                        g++;

                    }

                    //method to do the actual bulk up
                    bulkGoods();
                }


            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    //if getting bulk data fails
                }

            });


        }

        else if(reason.equalsIgnoreCase("Sending_bulk_goods")){

            //initialise and populate the values to be used
            pname= bulk_liquidate.getSnames( );
            pDesc=bulk_liquidate.getSdescription( );
            p_prices=bulk_liquidate.getSprice( );
            ids=bulk_liquidate.getSprodId( );
            amount=bulk_liquidate.getSamounter();
            P_DOCUMENTIDS=bulk_liquidate.getSdocumentIds( );
            Log.i("bulk next", "onClick: "+pname.get(0));
            Log.i("bulk next", "onClick: size "+pname.size());
            Log.i("bulk next", "onClick: "+pDesc.get(0));
            Log.i("bulk next", "onClick:size "+pDesc.size());
            Log.i("bulk next", "onClick: "+p_prices.get(0));
            Log.i("bulk next", "onClick: size "+p_prices.size());
            Log.i("bulk next", "onClick: "+ids.get(0));
            Log.i("bulk next", "onClick: size "+ids.size());
            Log.i("bulk next", "onClick: "+amount.get(0));
            Log.i("bulk next", "onClick: size "+amount.size());
            Log.i("bulk next", "onClick: "+P_DOCUMENTIDS.get(0));
            Log.i("bulk next", "onClick: size "+P_DOCUMENTIDS.size());


            //get the document information about the person you are to send goods to
            final String phoneNumber=getIntent().getExtras().getString("number");
            final String senderDocumentId=getIntent().getExtras().getString("userDocId");
            final CollectionReference reference1 = store.collection("Users");
            Log.i("bulk next", "onClick: number "+phoneNumber);
            Log.i("bulk next", "onClick: docIs"+senderDocumentId);
           // Log.i("bulk next", "onClick: size "+P_DOCUMENTIDS.size());
           // Log.i("bulk next", "onClick: size "+P_DOCUMENTIDS.size());
            //getting the information from the bulk store of the person we are sending the goods to
            reference1
                    .document(senderDocumentId)
                    .collection("BulkStore")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    if(task.isSuccessful()){
                        g = 0;
                       for (DocumentSnapshot snapshot:task.getResult()){

                               //populate the lists
                               IDS_SENDER_STORE.add(g, snapshot.getLong("b_productId"));
                               String id = snapshot.getId();
                               FROM_STORE_IDS.add(g, id);
                               FROM_STORE_AMOUNT.add(g, Integer.parseInt(snapshot.get("b_amount").toString()));
                               g++;
                           Log.i("bulk next", "onClick: IDS_SENDER_STORE"+IDS_SENDER_STORE.get(0));
                           Log.i("bulk next", "onClick: size "+amount.size());
                           Log.i("bulk next", "onClick: "+FROM_STORE_IDS.get(0));
                           Log.i("bulk next", "onClick: size "+FROM_STORE_AMOUNT.get(0));



                       }
                       //actual action of sending the goods

                       sendBulkGoods(phoneNumber,senderDocumentId);


                    }

                }


            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(OrderConfirmed.this,"failed,",Toast.LENGTH_LONG).show();
                }
            });

        }



        else if(reason.equalsIgnoreCase("bulk_goods_down")) {

            store
                    .collection("Users")
                    .document(User.getId())
                    .collection("Store")
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    if (task.isSuccessful()){
                        g=0;
                        for (DocumentSnapshot snapshot:task.getResult()) {
                            {
                                //populate the lists
                                IDS_SENDER_STORE.add(g, snapshot.getLong("P_ID"));
                                String id = snapshot.getId();
                                FROM_STORE_IDS.add(g, id);
                                FROM_STORE_AMOUNT.add(g, Integer.parseInt(snapshot.get("p_amount").toString()));
                                g++;

                            }
                        }
                            //initialise and populate the values to be used

                            pname= bulk_array.getBnames();
                            pDesc=bulk_array.getBdescription();
                            p_prices=bulk_array.getBprice();
                            ids=bulk_array.getBprodId();
                            amount=bulk_array.getBamount();
                            P_DOCUMENTIDS=bulk_array.getBdocumentIds();

                            for (int x=0;x<p_prices.size();x++){

                                price.add(Integer.parseInt(p_prices.get(x)));
                            }


                            Log.i("bulk next", "onClick: "+pname.get(0));
                            Log.i("bulk next", "onClick: size "+pname.size());
                            Log.i("bulk next", "onClick: "+pDesc.get(0));
                            Log.i("bulk next", "onClick:size "+pDesc.size());
                            Log.i("bulk next", "onClick: "+price.get(0));
                            Log.i("bulk next", "onClick: size "+price.size());
                            Log.i("bulk next", "onClick: "+ids.get(0));
                            Log.i("bulk next", "onClick: size "+ids.size());
                            Log.i("bulk next", "onClick: "+amount.get(0));
                            Log.i("bulk next", "onClick: size "+amount.size());
                            Log.i("bulk next", "onClick: "+P_DOCUMENTIDS.get(0));
                            Log.i("bulk next", "onClick: size "+P_DOCUMENTIDS.size());



                            bulkDown();



                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

        }
        else
            {

        }


    }
    private  void Notify (String Title,String Mes,String LongMes){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(OrderConfirmed.this, "send_goods")
                .setSmallIcon(R.drawable.iconliv)
                .setContentTitle(Title)
                .setContentText(Mes)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(LongMes))
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(0, builder.build());




    }
    private void sendNotificationToClient(String phoneNumberDocument, final String title, final String message){
        FirebaseFirestore.getInstance().collection("Users").document(phoneNumberDocument).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot=task.getResult();
                    String UserToken=documentSnapshot.get("U_Token").toString();
                    ActualSendingNotification(UserToken,title,message);
                    Log.i("tagz","Token Gotten "+UserToken);
                }

            }
        });
    }

    public void ActualSendingNotification(String usertoken,String title,String message){
        //Data data=new Data(title,message);
        //NotificationSender sender=new NotificationSender(data,usertoken);
       // apiService= Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
       // NotificationSender rootModel = new NotificationSender(usertoken, new NotificationSender(title, message), new DataModel("Name", "30"));
        //retrofit2.Call<ResponseBody> responseBodyCall = apiService.getClient("");

       // NotificationSender rootModel = new NotificationSender(usertoken, new Data(title, message), new DataModel("Name", "30"));
        JsonObject payload = Jsoner.buildNotificationPayload(usertoken,title,message);
         apiService =Client.getClient("https://fcm.googleapis.com/").create(APIService.class);;
        retrofit2.Call<JSONObject> responseBodyCall = apiService.sendNotification(payload);

        responseBodyCall.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(retrofit2.Call<JSONObject> call, retrofit2.Response<JSONObject> response) {


                Log.i("tagz","Successfully notification send by using retrofit.");
            }

            @Override
            public void onFailure(retrofit2.Call<JSONObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"failure to do "+ t.getMessage(),Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });






/*
        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if(response.code()==200){

                    if(response.body().success!=1){
                        Toast.makeText(getApplicationContext(),"failure",Toast.LENGTH_SHORT).show();
                    }

                }

            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

                Toast.makeText(getApplicationContext(),"failure to do "+ t.getMessage(),Toast.LENGTH_SHORT).show();
               t.printStackTrace();
            }
        });
*/
    }

    private void bulkDown() {
        store.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                //variables initialisations
                ArrayList<DocumentSnapshot> list_existing=new ArrayList<>();
                ArrayList<DocumentReference> references_existing=new ArrayList<>();
                ArrayList<DocumentSnapshot> list_sender_store=new ArrayList<>();
                ArrayList<DocumentReference> references_sender_store=new ArrayList<>();
                ArrayList<Integer> Existingindexes =new ArrayList<>();
                ArrayList<Integer> newIndexes=new ArrayList<>();
                WriteBatch batch=store.batch();

                //ouur reads are done here
                for(int x=0;x<ids.size();x++){

                    if(checkIfStoreItemExistsInStore(ids.get(x))){

                        //item exists in the bulk store
                        //get index in our array list where the item exists
                        int y=getSpecificIndex(ids.get(x));
                        //read the document from the database and store the documentsnapshot in a localised arraylist
                        DocumentReference reference=store.collection("Users").document(User.getId()).collection("Store").document(FROM_STORE_IDS.get(y));
                        DocumentSnapshot snapshot=transaction.get(reference);
                        list_existing.add(snapshot);
                        references_existing.add(reference);
                        Existingindexes.add(x);

                    }

                    else{
                        //write the index of the goods to be retrieved later
                        Log.i("send goods", "apply: entered un-existing ");
                        newIndexes.add(x);


                    }

                    //check the specific item in his store (store meaning unbulked up goods)
                    //basically read the document snapshot of the store item to be bulked up
                    DocumentReference reference=store
                            .collection("Users")
                            .document(User.getId())
                            .collection("BulkStore")
                            .document(P_DOCUMENTIDS.get(x));

                    DocumentSnapshot documentSnapshot=transaction.get(reference);
                    //Log.i("send goods", "apply: gotten an existing "+documentSnapshot.get("p_name").toString());
                    list_sender_store.add(documentSnapshot);
                    references_sender_store.add(reference);

                }


                for(int x=0;x<list_existing.size();x++){

                    //for the items which are already existing,just increment the amount
                    DocumentSnapshot snapshot=list_existing.get(x);
                    int old_amount=Integer.parseInt(snapshot.get("p_amount").toString());
                    int reference_snapshot= Existingindexes.get(x);
                    int new_amount=old_amount+ amount.get(reference_snapshot);
                    transaction.update(references_existing.get(x),"p_amount",new_amount);

                }

                for(int x=0;x<newIndexes.size();x++){

                    //for items which are not existing,just add create a new item and add it to the bulkStore
                    Map<String,Object> sender;
                    int bulk_item_no=newIndexes.get(x);
                    sender=createMap(bulk_item_no);
                    DocumentReference reference=store.collection("Users").document(User.getId()).collection("Store").document();
                    batch.set(reference,sender);

                }

                for (int x=0;x<list_sender_store.size();x++){
                    //delete the items from the store which have already been bulked up,since they are now already in bulk statr
                    DocumentReference snapshot=references_sender_store.get(x);
                    transaction.delete(snapshot);

                }



                if(newIndexes.size()>0){

                    //if there is any instance of a new item write ,commit the batch and listen for any failure that may occur
                    batch.commit().addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }




                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                createNotificationChannel("Send Goods","goodsSent","send");
                Notify("Bulk Down","Goods Succesfully Bulked down","Check your store to confirm good Bulk down");
                createTransaction(1,"Bulk down of goods");
                confirmation.setBackgroundColor(Color.BLACK);

                confirmation.setTextColor(Color.WHITE);
                confirmation.setText("finish transaction");


            }
        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        e.printStackTrace();
                    }
                });
    }

    private void withdrawToken(final String docs, final String pass) {
      store.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                //declarations
                ArrayList<DocumentSnapshot> list_existing=new ArrayList<>();
                ArrayList<DocumentReference> references_existing=new ArrayList<>();
                ArrayList<DocumentSnapshot> list_sender_store=new ArrayList<>();
                ArrayList<DocumentReference> references_sender_store=new ArrayList<>();
                ArrayList<Integer> Existingindexes =new ArrayList<>();
                ArrayList<Integer> newIndexes=new ArrayList<>();
                WriteBatch batch=store.batch();
                //Doing our reads here

                for (int x=0;x<ids.size();x++){

                    if(checkIfStoreItemExistsInStore(ids.get(x))){
                        Log.i("send goods", "apply: entered existing ");
                        int y=getSpecificIndex(ids.get(x));
                        DocumentReference reference=store.collection("Users").document(User.getId()).collection("Store").document(FROM_STORE_IDS.get(y));
                        DocumentSnapshot snapshot=transaction.get(reference);
                        list_existing.add(snapshot);
                        references_existing.add(reference);
                        Existingindexes.add(x);
                        //indexes.add(x);
                    }
                    else {
                        Log.i("send goods", "apply: entered un-existing ");
                        newIndexes.add(x);
                    }
                    //possibly where error is getting generated
                    DocumentReference reference=store.collection("TokenAccounts").document(docs).collection(pass).document(P_DOCUMENTIDS.get(x));
                    DocumentSnapshot documentSnapshot=transaction.get(reference);
                    Log.i("send goods", "apply: gotten an existing "+documentSnapshot.get("p_name").toString());
                    list_sender_store.add(documentSnapshot);
                    references_sender_store.add(reference);
                }

                //works well
                for ( int x=0;x<list_existing.size();x++){
                    Log.i("send goods", "apply: put existing ");
                    int amountbefore;
                    DocumentSnapshot snapshot=list_existing.get(x);
                    Log.i("send goods", "apply: getting existing index "+Existingindexes.get(x));
                    int y=Existingindexes.get(x);
                    Log.i("send goods", "apply: value of y "+y);
                    amountbefore=Integer.parseInt(snapshot.get("p_amount").toString());
                    Log.i("send goods", "apply: amount from store "+amountbefore);
                    Log.i("send goods", "apply: amount to be added"+amount.get(y));
                    int amount_to_put=amount.get(y)+amountbefore;
                    Log.i("send goods", "apply:final amount to be added"+amount_to_put);
                    transaction.update(references_existing.get(x),"p_amount",amount_to_put);
                }

                for (int x=0;x<newIndexes.size();x++){
                    Log.i("send goods", "apply: put un-existing ");
                    int nexIndex=newIndexes.get(x);
                    Map<String,Object> add;
                    add=createMap(nexIndex);
                    DocumentReference reference=store.collection("Users").document(User.getId()).collection("Store").document();
                    batch.set(reference,add);
                    //transaction.set(reference,add);
                }
                for (int z=0;z<ids.size();z++){
                        Log.i("send goods", "apply:  updated store ");

                        transaction.delete(references_sender_store.get(z));


                }

                if(newIndexes.size()>0){
                    Log.i("send goods", "apply: batchCommited");
                    batch.commit();
                }

                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Notify("Withdrawal of goods","Goods Succesfully withdrawn","Check your store to confirm withdrawal");
                createTransaction(0,"Withdrawal of goods");
                confirmation.setBackgroundColor(Color.BLACK);
                confirmation.setTextColor(Color.WHITE);
                confirmation.setText("finish transaction");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(OrderConfirmed.this,"failure occured",Toast.LENGTH_SHORT).show();
                Intent main=new Intent(getApplicationContext(),MainPage.class);
                startActivity(main);
                finish();
                e.printStackTrace();
            }
        });



    }


    private void getDatbaseData(Cursor cursor,int y){
        if (cursor.moveToFirst()){
            x=0;
            while(!cursor.isAfterLast()){

                pname.add(x,cursor.getString(cursor.getColumnIndex("NAME")));
                pDesc.add(x,cursor.getString(cursor.getColumnIndex("DESCRIPTION")));
                price.add(x,cursor.getInt(cursor.getColumnIndex("PRICE")));
                amount.add(x,cursor.getInt(cursor.getColumnIndex("AMOUNT")));
                ids.add(x,cursor.getLong(cursor.getColumnIndex("PRODUCTNO")));
                Log.i("data in", "Cursor:  "+pname.get(x));
                Log.i("data in", "Cursor: amount put in "+amount.get(x));
                if(y==0){
                    //sending to someone hence need for update of store
                // do what ever you want here
                P_DOCUMENTIDS.add(x,cursor.getString(cursor.getColumnIndex("DOCUMENTID")));
            }
                x++;
                cursor.moveToNext();
            }
        }
        cursor.close();
    }


    public static Boolean checkIfStoreItemExistsInStore(Long id){

        //Boolean exists=false;

        for (int c=0;c<IDS_SENDER_STORE.size();c++){

             if(IDS_SENDER_STORE.get(c).equals(id)){
                 return true;
             }
     }
        return false;
    }


    public static int getSpecificIndex(Long id){

        for (int c=0;c<IDS_SENDER_STORE.size();c++){

            if(IDS_SENDER_STORE.get(c).equals(id)){
                return c;

            }
        }
        return 0;
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



       private void updateStoreFromBuying(){
        store.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                ArrayList<DocumentSnapshot> list_existing=new ArrayList<>();
                ArrayList<DocumentReference> references_existing=new ArrayList<>();
                ArrayList<Integer> Existingindexes =new ArrayList<>();
                ArrayList<Integer> newIndexes=new ArrayList<>();
                WriteBatch batch=store.batch();
                //Doing our reads here
d=0;
                for (int x=0;x<ids.size();x++){

                    if(checkIfStoreItemExistsInStore(ids.get(x))){
                        Log.i("send goods", "apply: entered existing ");
                        int y=getSpecificIndex(ids.get(x));
                        Log.i("send goods", "apply: specific long "+ids.get(x));
                        Log.i("send goods", "apply: specific index "+y);
                        DocumentReference reference=store
                                .collection("Users")
                                .document(User.getId())
                                .collection("Store")
                                .document(FROM_STORE_IDS.get(y));
                        DocumentSnapshot snapshot=transaction.get(reference);
                        list_existing.add(snapshot);
                        references_existing.add(reference);
                        Existingindexes.add(d,x);
                        Log.i("send goods", "apply: index to be added "+Existingindexes.get(d));
                        d++;
                        //indexes.add(x);
                    }
                    else {
                        Log.i("send goods", "apply: entered un-existing ");
                        newIndexes.add(x);
                    }

                }

                //works well
                for ( int x=0;x<list_existing.size();x++){
                    Log.i("send goods", "apply: put existing ");
                    int amountbefore;
                    DocumentSnapshot snapshot=list_existing.get(x);
                    Log.i("send goods", "apply: getting existing index "+Existingindexes.get(x));
                    int y=Existingindexes.get(x);
                    Log.i("send goods", "apply: value of y "+y);
                    amountbefore=Integer.parseInt(snapshot.get("p_amount").toString());
                    Log.i("send goods", "apply: amount from store "+amountbefore);
                    Log.i("send goods", "apply: amount to be added"+amount.get(y));
                    int amount_to_put=amount.get(y)+amountbefore;
                    Log.i("send goods", "apply:final amount to be added"+amount_to_put);
                    transaction.update(references_existing.get(x),"p_amount",amount_to_put);
                }




                for (int x=0;x<newIndexes.size();x++){
                    Log.i("send goods", "apply: put un-existing ");
                    int nexIndex=newIndexes.get(x);
                    Map<String,Object> add;
                    add=createMap(nexIndex);
                    DocumentReference reference=store.collection("Users").document(User.getId()).collection("Store").document();
                    batch.set(reference,add);
                    //transaction.set(reference,add);
                }
                if(newIndexes.size()>0){
                    Log.i("send goods", "apply: batchCommited");
                    batch.commit();
                }



                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                createNotificationChannel("Buy Goods","notification for Buying Goods","send_goods");
                Notify("Buy Goods","Goods Succesfully Purchased","Check your store to confirm good Purchases");
                createTransaction(0,"Buying Goods");
                confirmation.setBackgroundColor(Color.BLACK);

                confirmation.setTextColor(Color.WHITE);
                confirmation.setText("finish transaction");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


       }






    private  void updateStoresAfterSending(final String phoneNumber,final String documentID){

        store.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                //declarations
               // Toast.makeText(OrderConfirmed.this,"items succesfully started",Toast.LENGTH_SHORT);
                ArrayList<DocumentSnapshot> list_existing=new ArrayList<>();
                ArrayList<DocumentReference> references_existing=new ArrayList<>();
                ArrayList<DocumentSnapshot> list_sender_store=new ArrayList<>();
                ArrayList<DocumentReference> references_sender_store=new ArrayList<>();
                ArrayList<Integer> Existingindexes =new ArrayList<>();
                ArrayList<Integer> newIndexes=new ArrayList<>();
                WriteBatch batch=store.batch();
                //Doing our reads here

                for (int x=0;x<ids.size();x++){

                    if(checkIfStoreItemExistsInStore(ids.get(x))){

                        Log.i("send goods", "apply: entered existing ");

                        int y=getSpecificIndex(ids.get(x));

                        DocumentReference reference=store.collection("Users").document(documentID).collection("Store").document(FROM_STORE_IDS.get(y));
                        DocumentSnapshot snapshot=transaction.get(reference);
                        list_existing.add(snapshot);
                        references_existing.add(reference);
                        Existingindexes.add(x);
                       //indexes.add(x);
                    }
                    else {
                        Log.i("send goods", "apply: entered un-existing ");
                        newIndexes.add(x);
                    }
                    DocumentReference reference=store.collection("Users").document(User.getId()).collection("Store").document(P_DOCUMENTIDS.get(x));
                    DocumentSnapshot documentSnapshot=transaction.get(reference);
                    Log.i("send goods", "apply: gotten an existing "+documentSnapshot.get("p_name").toString());
                    list_sender_store.add(documentSnapshot);
                    references_sender_store.add(reference);
                }

                //works well
                 for ( int x=0;x<list_existing.size();x++){

                     Log.i("send goods", "apply: put existing ");
                     int amountbefore;
                     DocumentSnapshot snapshot=list_existing.get(x);
                     int y=Existingindexes.get(x);
                     amountbefore=Integer.parseInt(snapshot.get("p_amount").toString());
                     int amount_to_put=amount.get(y)+amountbefore;
                     transaction.update(references_existing.get(x),"p_amount",amount_to_put);

                 }


                 for (int x=0;x<newIndexes.size();x++){
                     Log.i("send goods", "apply: put un-existing ");
                     int nexIndex=newIndexes.get(x);
                      Map<String,Object> add;
                      add=createMap(nexIndex);
                      DocumentReference reference=store.collection("Users").document(documentID).collection("Store").document();
                      batch.set(reference,add);
                      //transaction.set(reference,add);
                    }


                 for (int z=0;z<ids.size();z++){
                     Log.i("send goods", "apply:  updated store ");
                     DocumentSnapshot snapshot=list_sender_store.get(z);
                     //DocumentReference reference=references_sender_store.get(z);
                     int amountthere=Integer.parseInt(snapshot.get("p_amount").toString());
                     int newAmount=amountthere-amount.get(z);
                     Log.i("send goods", "apply:  new amount "+newAmount);
                     if (newAmount<=0){
                         transaction.delete(references_sender_store.get(z));
                     }
                     else {
                         transaction.update(references_sender_store.get(z),"p_amount",newAmount);

                     }
                 //    Toast.makeText(OrderConfirmed.this,"items succesfully finished",Toast.LENGTH_SHORT);

                }
                 if(newIndexes.size()>0){
                     Log.i("send goods", "apply: batchCommited");
                     batch.commit();
                 }

                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                createNotificationChannel("send Goods","notification for sending Goods","send_goods");
                Notify("Send Good","Goods Succesfully Sent","Check your store to confirm sent goods");
                sendNotificationToClient(documentID, "Goods Received","Check your store to confirm recieving goods");
                createTransaction(0,"sending Goods");
                Toast.makeText(OrderConfirmed.this,"items succesfully updated",Toast.LENGTH_SHORT);
                confirmation.setBackgroundColor(Color.BLACK);
                confirmation.setTextColor(Color.WHITE);
                confirmation.setText("finish transaction");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(OrderConfirmed.this,"error :"+e.getMessage(),Toast.LENGTH_SHORT);
                e.printStackTrace();
            }
        });
    }






   private void bulkGoods(){
       store.runTransaction(new Transaction.Function<Void>() {
           @Nullable
           @Override
           public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {

               //variables initialisations
               ArrayList<DocumentSnapshot> list_existing=new ArrayList<>();
               ArrayList<DocumentReference> references_existing=new ArrayList<>();
               ArrayList<DocumentSnapshot> list_sender_store=new ArrayList<>();
               ArrayList<DocumentReference> references_sender_store=new ArrayList<>();
               ArrayList<Integer> Existingindexes =new ArrayList<>();
               ArrayList<Integer> newIndexes=new ArrayList<>();
               WriteBatch batch=store.batch();

               //ouur reads are done here
               for(int x=0;x<ids.size();x++){

                   if(checkIfStoreItemExistsInStore(ids.get(x))){

                       //item exists in the bulk store
                       //get index in our array list where the item exists
                       int y=getSpecificIndex(ids.get(x));
                       //read the document from the database and store the documentsnapshot in a localised arraylist
                       DocumentReference reference=store
                               .collection("Users")
                               .document(User.getId())
                               .collection("BulkStore")
                               .document(FROM_STORE_IDS.get(y));

                       DocumentSnapshot snapshot=transaction.get(reference);
                       list_existing.add(snapshot);
                       references_existing.add(reference);
                       Existingindexes.add(x);

                   }

                   else{
                        //write the index of the goods to be retrieved later
                       Log.i("send goods", "apply: entered un-existing ");
                       newIndexes.add(x);


                   }

                   //check the specific item in his store (store meaning unbulked up goods)
                   //basically read the document snapshot of the store item to be bulked up
                   DocumentReference reference=store
                           .collection("Users")
                           .document(User.getId())
                           .collection("Store")
                           .document(P_DOCUMENTIDS.get(x));

                   DocumentSnapshot documentSnapshot=transaction.get(reference);
                   Log.i("send goods", "apply: gotten an existing "+documentSnapshot.get("p_name").toString());
                   list_sender_store.add(documentSnapshot);
                   references_sender_store.add(reference);

               }


               for(int x=0;x<list_existing.size();x++){

                   //for the items which are already existing,just increment the amount
                   DocumentSnapshot snapshot=list_existing.get(x);
                   int old_amount=Integer.parseInt(snapshot.get("b_amount").toString());
                   int reference_snapshot= Existingindexes.get(x);
                   int new_amount=old_amount+ amount.get(reference_snapshot);
                   transaction.update(references_existing.get(x),"b_amount",new_amount);

               }

               for(int x=0;x<newIndexes.size();x++){

                   //for items which are not existing,just add create a new item and add it to the bulkStore
                   Map<String,Object> sender;
                   int bulk_item_no=newIndexes.get(x);
                   sender=createBulkMap(bulk_item_no);
                   DocumentReference reference=store.collection("Users").document(User.getId()).collection("BulkStore").document();
                   batch.set(reference,sender);

               }

               for (int x=0;x<list_sender_store.size();x++){
                   //get the data of the amount from the mainstore, for all of the goods to be bulked up, and decrement it to get the remaining items after bulking up

                   int old_store_amount=Integer.parseInt(list_sender_store.get(x).get("p_amount").toString());
                   int new_no=old_store_amount-amount_bulked_up.get(x);

                   if(new_no<=0){
                       transaction.delete(references_sender_store.get(x));
                   }
                   else {
                       transaction.update(references_sender_store.get(x),"p_amount",new_no);


                   }



                   //old comment
                   //delete the items from the store which have already been bulked up,since they are now already in bulk statr
                  //end of old comment
               }



               if(newIndexes.size()>0){

                   //if there is any instance of a new item write ,commit the batch and listen for any failure that may occur
                   batch.commit().addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {

                       }
                   });
               }



               return null;
           }

       }).addOnCompleteListener(new OnCompleteListener<Void>() {
           @Override
           public void onComplete(@NonNull Task<Void> task) {
               createNotificationChannel("Bulk Up","notification for Bulk Up","Bulk_up");
               Notify("Bulked up","Goods Succesfully Bulked up","Check your store to confirm Bulk Up");
               createTransaction(1,"Bulking Up");
               confirmation.setBackgroundColor(Color.BLACK);
               confirmation.setTextColor(Color.WHITE);
               confirmation.setText("finish transaction");

           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {

               e.printStackTrace();
               //failure occured

           }
       });

   }

    private  void updateStoresAfterTokenSending(final String collection, final String documentID, final String phoneNumber){

        store.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                //declarations
                // Toast.makeText(OrderConfirmed.this,"items succesfully started",Toast.LENGTH_SHORT);

                ArrayList<DocumentSnapshot> list_sender_store = new ArrayList<>();
                ArrayList<DocumentReference> references_sender_store = new ArrayList<>();
                ArrayList<Integer> newIndexes = new ArrayList<>();
                WriteBatch batch = store.batch();

                //Doing our reads here

                for (int x = 0; x < ids.size(); x++) {

                DocumentReference reference = store.collection("Users").document(User.getId()).collection("Store").document(P_DOCUMENTIDS.get(x));
                DocumentSnapshot documentSnapshot = transaction.get(reference);
                Log.i("send goods", "apply: gotten an existing " + documentSnapshot.get("p_name").toString());
                list_sender_store.add(documentSnapshot);
                references_sender_store.add(reference);

                }

                //works well

                for (int x=0;x<ids.size();x++){

                    Log.i("send goods", "apply: put un-existing ");
                    Map <String,Object> add=createMap(x);

                    DocumentReference reference=store
                            .collection("TokenAccounts")
                            .document(collection)
                            .collection(documentID).document();

                    batch.set(reference,add);
                    //transaction.set(reference,add);
                }


                for (int z=0;z<ids.size();z++){

                    Log.i("send goods", "apply:  updated store ");
                    DocumentSnapshot snapshot=list_sender_store.get(z);
                    //DocumentReference reference=references_sender_store.get(z);
                    int amountthere=Integer.parseInt(snapshot.get("p_amount").toString());

                    int newAmount=amountthere-amount.get(z);
                    Log.i("send goods", "apply:  new amount "+newAmount);

                    if (newAmount<=0){
                        transaction.delete(references_sender_store.get(z));
                    }
                    else {
                        transaction.update(references_sender_store.get(z),"p_amount",newAmount);

                    }
                    //    Toast.makeText(OrderConfirmed.this,"items succesfully finished",Toast.LENGTH_SHORT);

                }
                    Log.i("send goods", "apply: batchCommited");
                    batch.commit();

                return null;

            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                createNotificationChannel("Token sending","notification for Token_sending","Token_sending");
                Notify("Send goods","Goods Succesfully Sent","Check your store to confirm Sending goods, User will get an sms");
                createTransaction(0,"sending Token retail Goods ");
                String Message="I have sent ";

                for (int x=0;x<ids.size();x++){
                    Message=Message+pname.get(x)+ ","+amount.get(x)+" items ";
                }

                Message=Message+"withdraw with "+collection+" as document name and "+documentID+" as password";
                SmsManager sms = SmsManager.getDefault();
                try
                {
                    sms.sendTextMessage(phoneNumber, null,Message, null, null);

                }
                catch(IllegalArgumentException e)
                {

                }
                confirmation.setBackgroundColor(Color.BLACK);

                confirmation.setTextColor(Color.WHITE);
                confirmation.setText("finish transaction");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(OrderConfirmed.this,"error :"+e.getMessage(),Toast.LENGTH_SHORT);
                e.printStackTrace();
            }
        });
    }



    private void sendBulkGoods(String phoneNumber, final String senderDocumentId) {
        store.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {

                //declarations

                ArrayList<DocumentSnapshot> list_existing=new ArrayList<>();
                ArrayList<DocumentReference> references_existing=new ArrayList<>();
                ArrayList<DocumentSnapshot> list_sender_store=new ArrayList<>();
                ArrayList<DocumentReference> references_sender_store=new ArrayList<>();
                ArrayList<Integer> Existingindexes =new ArrayList<>();
                ArrayList<Integer> newIndexes=new ArrayList<>();
                WriteBatch batch=store.batch();

                //Doing our reads here
                for (int x=0;x<ids.size();x++){

                    if(checkIfStoreItemExistsInStore(ids.get(x))){
                        //specific item exists in his store
                        Log.i("send goods", "apply: entered existing ");
                        int y=getSpecificIndex(ids.get(x));

                        //getting the documents snapshot and storing in arraylist,with all other relevant data
                        DocumentReference reference=store.collection("Users").document(senderDocumentId).collection("BulkStore").document(FROM_STORE_IDS.get(y));
                        DocumentSnapshot snapshot=transaction.get(reference);
                        list_existing.add(snapshot);
                        references_existing.add(reference);
                        Existingindexes.add(x);

                        //indexes.add(x);
                    }
                    else {

                        // add the index of the specific item which does not exist in the sent to person's store
                        Log.i("send goods", "apply: entered un-existing ");
                        newIndexes.add(x);
                    }


                    //get the data of the specific item in your own bulk store too which maps to the goods to be sent
                    DocumentReference reference=store.collection("Users").document(User.getId()).collection("BulkStore").document(P_DOCUMENTIDS.get(x));
                    DocumentSnapshot documentSnapshot=transaction.get(reference);

                    Log.i("send goods", "apply: gotten an existing "+documentSnapshot.get("b_name").toString());

                    list_sender_store.add(documentSnapshot);
                    references_sender_store.add(reference);
                }

                //for the existing items
                for ( int x=0;x<list_existing.size();x++){

                    //for all of them ,increment their numbers in the send to store
                    Log.i("send goods", "apply: put existing ");
                    int amountbefore;
                    DocumentSnapshot snapshot=list_existing.get(x);
                    int y=Existingindexes.get(x);
                    amountbefore=Integer.parseInt(snapshot.get("b_amount").toString());
                    int amount_to_put=amount.get(y)+amountbefore;
                    transaction.update(references_existing.get(x),"b_amount",amount_to_put);

                }


                //for the new items
                for (int x=0;x<newIndexes.size();x++){

                    //create new map and put the new items in their speciifc bulkstore
                    Log.i("send goods", "apply: put un-existing ");
                    int nexIndex=newIndexes.get(x);
                    Map<String,Object> add;
                    add=createBulkMap(nexIndex);

                    DocumentReference reference=store.collection("Users")
                            .document(senderDocumentId)
                            .collection("BulkStore")
                            .document();

                    batch.set(reference,add);
                    //transaction.set(reference,add);
                }


                //updating your own bulkstore after sending
                for (int z=0;z<ids.size();z++){

                    Log.i("send goods", "apply:  updated store ");
                    DocumentSnapshot snapshot=list_sender_store.get(z);
                    //DocumentReference reference=references_sender_store.get(z);

                    int amountthere=Integer.parseInt(snapshot.get("b_amount").toString());
                    int newAmount=amountthere-amount.get(z);

                    Log.i("send goods", "apply:  new amount "+newAmount);

                    if (newAmount<=0){
                        //good no longer exists hence delete it

                        transaction.delete(references_sender_store.get(z));
                    }
                    else {
                        //reduce the amount in your bulkstore
                        transaction.update(references_sender_store.get(z),"b_amount",newAmount);

                    }
                    //    Toast.makeText(OrderConfirmed.this,"items succesfully finished",Toast.LENGTH_SHORT);

                }
                if(newIndexes.size()>0){
                    //if there is a new item to be added into the send to store..comit the batch

                    Log.i("send goods", "apply: batchCommited");
                    batch.commit();
                }


                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                createNotificationChannel("Bulk sending","notification for Bulk_sending","Bulk_sending");
                Notify("Send Bulk goods","Bulk Goods Succesfully Sent","Check your store to confirm Sending Bulk goods");
                sendNotificationToClient(senderDocumentId, "Bulk Goods Recieved","Check your store to confirm recieving Bulk goods");
                createTransaction(1,"sending Bulk Goods");
                //succesfull action
                Toast.makeText(OrderConfirmed.this,"items succesfully updated",Toast.LENGTH_SHORT);
                confirmation.setBackgroundColor(Color.BLACK);
                confirmation.setTextColor(Color.WHITE);
                confirmation.setText("finish transaction");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                //failure

                Toast.makeText(OrderConfirmed.this,"error :"+e.getMessage(),Toast.LENGTH_SHORT);
                e.printStackTrace();
            }
        });


    }



    private static Map<String,Object> createMap(int t){

        Map<String, Object> addedItem = new HashMap<>();
            addedItem.put("P_ID", ids.get(t));
            addedItem.put("p_name", pname.get(t));
            addedItem.put("p_amount", amount.get(t));
            addedItem.put("p_description", pDesc.get(t));
            addedItem.put("P_price", price.get(t));

            return addedItem;

    }


    private static Map<String,Object> createBulkMap(int t){

        Map<String, Object> addedItem = new HashMap<>();
        addedItem.put("b_productId", ids.get(t));
        addedItem.put("b_name", pname.get(t));
        addedItem.put("b_amount", amount.get(t));
        addedItem.put("b_description", pDesc.get(t));
        addedItem.put("b_price",p_prices.get(t));

        return addedItem;

    }
    private void createTransaction(int type,String reason) {

        Map<String,Object> transactingMap;
        transactingMap=createTransactionMap(type,reason);
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

    private Map<String, Object> createTransactionMap(int type,String reason) {

        Map<String,Object> transactingMap=new HashMap<>();
        transactingMap.put("initiator_FirstName", User.getFirstName());
        transactingMap.put("initiator_PhoneNumber", User.getPhoneNumber());
        transactingMap.put("reason",reason);

        if(type==0){
        transactingMap.put("P_ID", ids);
        transactingMap.put("p_name", pname);
        transactingMap.put("p_amount", amount);
        transactingMap.put("p_description", pDesc);
        transactingMap.put("P_price", price);
    }

        else if(type==1){
            transactingMap.put("bulk_ID", ids);
            transactingMap.put("bulk_name", pname);
            transactingMap.put("bulk_amount", amount);
            transactingMap.put("bulk_description", pDesc);
            transactingMap.put("bulk_price", price);

        }
        else {
            return null;
        }
        return transactingMap;


    }



}
