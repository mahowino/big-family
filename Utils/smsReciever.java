package com.example.bigfamilyv20.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.bigfamilyv20.Activities.Cart_Activity;
import com.example.bigfamilyv20.Activities.OrderConfirmed;

import org.apache.http.cookie.SM;

import java.security.Permission;
import java.security.Permissions;

public class smsReciever extends BroadcastReceiver {
private boolean is_sms_initiated_by_payment=false;
private static  String   SMSmessage;

    public boolean isIs_sms_initiated_by_payment() {
        return is_sms_initiated_by_payment;
    }

    public void setIs_sms_initiated_by_payment(boolean is_sms_initiated_by_payment) {
        this.is_sms_initiated_by_payment = is_sms_initiated_by_payment;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"hi ive reveived",Toast.LENGTH_LONG).show();
            String permission=Manifest.permission.READ_SMS;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(context,permission)!= PackageManager.PERMISSION_GRANTED){

            String[] permissionsList=new String[1];
           permissionsList[0]=permission;
            ActivityCompat.requestPermissions((Activity) context,permissionsList,1);

        }

        SmsMessage[] message= Telephony.Sms.Intents.getMessagesFromIntent(intent);


        for (SmsMessage sms:message){

            SMSmessage  =sms.getMessageBody();
        }
        if(SMSmessage.contains("001ABC") && is_sms_initiated_by_payment==true){
            is_paymentProcess_done.setIs_processDone(true);
        }

    }

}
