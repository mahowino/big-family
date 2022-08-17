package com.example.bigfamilyv20.Activities;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.androidstudy.daraja.Daraja;
import com.androidstudy.daraja.DarajaListener;
import com.androidstudy.daraja.model.AccessToken;
import com.androidstudy.daraja.model.LNMExpress;
import com.androidstudy.daraja.model.LNMResult;
import com.androidstudy.daraja.util.Env;
import com.androidstudy.daraja.util.TransactionType;
import com.bdhobare.mpesa.Mode;
import com.bdhobare.mpesa.interfaces.AuthListener;
import com.bdhobare.mpesa.interfaces.MpesaListener;
import com.bdhobare.mpesa.models.STKPush;
import com.bdhobare.mpesa.utils.Pair;
import com.example.bigfamilyv20.Entities.ProductCard;
import com.example.bigfamilyv20.Entities.User;
import com.example.bigfamilyv20.R;
import com.example.bigfamilyv20.Utils.Database_helper;
import com.example.bigfamilyv20.Utils.InternetChecker;
import com.example.bigfamilyv20.Utils.Mpesa;
import com.example.bigfamilyv20.Utils.is_paymentProcess_done;
import com.example.bigfamilyv20.Utils.smsReciever;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.zip.Inflater;

public class Cart_Activity extends AppCompatActivity  {

private static TextView cartPrice,serviceCharge;
private static Button pay;
private static Boolean is_shopkeeper_account;
private static String PAYBILL_SANDBOX="174379",reason,number,docs,pass,docId;
private static String PASSKEY="eba2c28ef6c07b569925fc87ae64258c3dba28b76e02caa8a11811cc5e27cc85";
private static String PASSKEY_SANDBOX="bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919";
private static String CONS_KEY="C91yWYLAoL7ii9FFLzLyclNCcFLWSKIl";
private static String CONSUMER_SECRET="CwE4E2JFQzugnMdQ";
private static String CONS_KEY_SANDBOX="vlNnSf7ywwY6pPmS8Al6JzUVvwuwLrvK";
private static String CONSUMER_SECRET_SANDBOX="a1YyGz3UfXDHcf3S";
               String callbackurl="https://us-central1-big-family-auth-system.cloudfunctions.net/callbackUrl?usid="+User.getId();

               @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_);

        final String price=getIntent().getExtras().getString("price");
        String service=getIntent().getExtras().getString("Service");
        number=getIntent().getExtras().getString("number");
        is_shopkeeper_account=getIntent().getExtras().getBoolean("is_shopkeeper");

        cartPrice=(TextView)findViewById(R.id.total_amountdue);
        serviceCharge=(TextView)findViewById(R.id.service_charge);
        serviceCharge.setText(service);
        pay=(Button)findViewById(R.id.paybutton);

        cartPrice.setText(price);
        reason=getIntent().getExtras().getString("reason");
        docs=getIntent().getExtras().getString("docs") ;
        pass=getIntent().getExtras().getString("pass");
        docId=getIntent().getExtras().getString("userDocId");

        final Daraja daraja = Daraja.with(CONS_KEY, CONSUMER_SECRET, Env.PRODUCTION, new DarajaListener<AccessToken>() {
            @Override
            public void onResult(@NonNull AccessToken accessToken) {
                Log.i(  Cart_Activity.this.getClass().getSimpleName(), accessToken.getAccess_token());

            }

            @Override
            public void onError(String error) {
                Log.e(Cart_Activity.this.getClass().getSimpleName(), error);
            }
        });

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final LNMExpress lnmExpress = new LNMExpress(
                        "7483499",
                        PASSKEY,  //https://developer.safaricom.co.ke/test_credentials
                        TransactionType.CustomerBuyGoodsOnline,
                        String.valueOf(1),
                       // number,
                        User.getPhoneNumber().replace("+",""),
                        "5485689",
                        //number,
                        User.getPhoneNumber().replace("+",""),
                        callbackurl,
                        "001ABC",
                        "Goods Payment"
                );


                daraja.requestMPESAExpress(lnmExpress,
                        new DarajaListener<LNMResult>() {
                            @Override
                            public void onResult(@NonNull LNMResult lnmResult) {
                                try {
                                    SimpleDateFormat dateFormat=new SimpleDateFormat("yyyyMMddHHmmss");
                                    String date=dateFormat.format(new Date());



                                        Intent intent=new Intent(getApplicationContext(),wait_for_payment.class);
                                        intent.putExtra("charge",price);
                                        intent.putExtra("reason",reason);
                                        intent.putExtra("number",number);
                                        intent.putExtra("docs",docs);
                                        intent.putExtra("pass",pass);
                                        intent.putExtra("userDocId",docId);
                                        intent.putExtra("TransactionTime",date);
                                        intent.putExtra("is_shopkeeper",is_shopkeeper_account);


                                        startActivity(intent);
                                        finish();



                                }catch (Exception e){

                                    e.printStackTrace();
                                }

                            }
                            @Override
                            public void onError(String error) {
                                Log.i("the error unatafuta", error);
                                Toast.makeText(getApplicationContext(), "Error processing payment", Toast.LENGTH_SHORT).show();

                            }
                        }
                );
            }

        });


    }
    /*
    private void sendStk(String BUSINESS_SHORT_CODE,String PASSKEY,int amount,String phone ){

        STKPush.Builder builder = new STKPush.Builder(BUSINESS_SHORT_CODE, PASSKEY, amount,BUSINESS_SHORT_CODE, phone);
        STKPush push = builder.build();
        Mpesa.getInstance().pay(Cart_Activity.this, push);



    }
    */
}
