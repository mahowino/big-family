package com.example.bigfamilyv20.Utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class firebasefirestore extends AsyncTask<String,Void,Void >{

    private static boolean isdone;
    private static String TAG="FirebaseDatabase";
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static ArrayList<QueryDocumentSnapshot> snapshots;

    public static boolean addRecord(CollectionReference reference, HashMap<String ,Object> map){
        isdone=false;
        reference
                .add(map)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        isdone=true;
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        isdone=false;
                        Log.w(TAG, "Error adding document", e);
                    }
                });
        return isdone;
    }

    public static ArrayList<QueryDocumentSnapshot> getRecords(CollectionReference reference, final Context context){
        snapshots=new ArrayList<>();
        snapshots.clear();
        reference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int x=0;
                    for (QueryDocumentSnapshot snapshot:task.getResult()) {
                        snapshots.add(x,snapshot);
                        x++;
                    }

                }}
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                snapshots.clear();
                Toast.makeText(context,"error getting data",Toast.LENGTH_LONG).show();
            }
        });


        return snapshots;
    }

    @Override
    protected Void doInBackground(String... strings) {
        return null;
    }
}
