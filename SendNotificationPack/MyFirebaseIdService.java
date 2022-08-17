package com.example.bigfamilyv20.SendNotificationPack;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bigfamilyv20.Entities.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;


public class MyFirebaseIdService extends FirebaseMessagingService {
    private Token token1;
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken= FirebaseInstanceId.getInstance().getToken();
        if(user!=null){
            updateToken(refreshToken);
        }
    }
    private void updateToken(final String refreshToken){
        //FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        token1=new Token(refreshToken);

        //update in firestore ,the value of token
        FirebaseFirestore.getInstance().runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentReference reference=FirebaseFirestore.getInstance().collection("Users").document(User.getId());
                transaction.update(reference,"U_Token",token1);
                return null;
            }
        });

    }
}
