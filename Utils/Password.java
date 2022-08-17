package com.example.bigfamilyv20.Utils;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class Password {

    //variables declarations
    public static ArrayList<String> encryptionDetails;
    public static ArrayList<QueryDocumentSnapshot> userLogins;
    private static FirebaseFirestore db=FirebaseFirestore.getInstance();
    private static final String DB_PASSWORD_NAME="USERS_ENCRYPTOR";
    private static final String TAG="PASSWORD_CLASS";
    private static CollectionReference reference;


    public Password(){
        reference=db.collection(DB_PASSWORD_NAME);
    }


    public static ArrayList<String> encrypt(String password)
    {
        encryptionDetails=new ArrayList<>();
        encryptionDetails.clear();

        // Generate Salt. The generated value can be stored in DB.
        String salt = PasswordUtils.getSalt(30);

        // Protect user's password. The generated value can be stored in DB.
        String mySecurePassword = PasswordUtils.generateSecurePassword(password, salt);
       encryptionDetails.add(0,salt);
       encryptionDetails.add(1,mySecurePassword);
        // Print out protected password
       return encryptionDetails;
    }



    public static boolean authenticatePassword(String givenPassword,String u_password,String u_salt)
    {
      Boolean authencated=false;

     if(u_password!=null && u_salt!=null){
         boolean passwordMatch = PasswordUtils.verifyUserPassword(givenPassword, u_password, u_salt);
         if(passwordMatch)
         {
             authencated=true;
         } else {
             authencated=false;
         }
     }
     else {
         Log.i(TAG, "authenticatePassword: u_password and u_salt are null ");
            authencated=false;

     }

        return authencated;
    }
}
