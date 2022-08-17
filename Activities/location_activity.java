package com.example.bigfamilyv20.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.TimeAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bigfamilyv20.Entities.shopkeeper_status;
import com.example.bigfamilyv20.Entities.subcounties_details;
import com.example.bigfamilyv20.Entities.userDocuments;
import com.example.bigfamilyv20.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class location_activity extends AppCompatActivity {

    private List<String> county;
    private List<String[]> subCounty;
    private static FirebaseFirestore db=FirebaseFirestore.getInstance();
    private static Spinner county_spinner,sub_countySpinner;
    private Button next;
    private static TextView no_location;
    private static final String STARTMESSAGE="Select county";
    private static final String[] STARTSUBCOUNTYMESSAGE={"Select sub county"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_activity);

        county=new ArrayList<>();
        subCounty=new ArrayList<>();

        no_location=findViewById(R.id.no_visible_locaation);
        county_spinner=findViewById(R.id.county);
        sub_countySpinner=findViewById(R.id.sub_county);
        next=findViewById(R.id.location_next_btn);

        no_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), unavailable_location.class);
                String phone=getIntent().getExtras().getString("UserPhone");
                intent.putExtra("UserPhone", phone);
                intent.putExtra("login_path","SignUpNewUser");
                //intent.putExtra("verificationCode",PhoneCodeSent);
                startActivity(intent);
                finish();

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //validation to confirm status of locaton

                if(county_spinner.getSelectedItem().toString().equalsIgnoreCase(STARTMESSAGE)
                        ||
                        sub_countySpinner.getSelectedItem().toString().equalsIgnoreCase(String.valueOf(STARTSUBCOUNTYMESSAGE))) {

                    Toast.makeText(getApplicationContext(),"choose a location to proceed", Toast.LENGTH_SHORT).show();
                }

                else {


                    Intent intent = new Intent(getApplicationContext(), SignUp_SecondScreen.class);
                    String phone=getIntent().getExtras().getString("UserPhone");
                    intent.putExtra("UserPhone", phone);
                    intent.putExtra("login_path","SignUpNewUser");
                    //intent.putExtra("verificationCode",PhoneCodeSent);
                    shopkeeper_status status=new shopkeeper_status();
                    status.setIs_shopkeeper(true);
                    status.setCounty(county_spinner.getSelectedItem().toString());
                    status.setSubcounty(sub_countySpinner.getSelectedItem().toString());
                    startActivity(intent);
                    finish();
                }

            }
        });

        db.collection("cities")
                .get()


                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        //populate default values
                        county.add(STARTMESSAGE);
                        subCounty.add(STARTSUBCOUNTYMESSAGE);

                        for (DocumentSnapshot snapshot:task.getResult()){

                            //get the county
                            county.add(snapshot.getId());

                            db
                                    .collection("cities")
                                    .document(snapshot.getId())
                                    .collection("sub_county")
                                    .get()


                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if(task.isSuccessful()) {

                                                List<String> temp=new ArrayList<>();
                                                for (DocumentSnapshot snapshot1 : task.getResult()) {
                                                    temp.add(snapshot1.getId());
                                                }

                                                String[] Holder=new String[temp.size()];

                                                for (int x=0;x<temp.size();x++){
                                                    Holder[x]=temp.get(x);
                                                }

                                                subCounty.add(Holder);
                                            }
                                        }
                                    })

                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
/*
                            //getting the subcounty as a map
                            Map<String, Object> map=snapshot.getData();
                            List<String> temp=new ArrayList<>();
                           // temp=(List<String>) snapshot.getData();
                            List<subcounties_details> users = snapshot.toObject(userDocuments.class).users;

                            ///get in string format

                            for (Map.Entry<String, Object> entry : map.entrySet()){

                             temp.add(entry.getValue().toString());
                            }

                            String[] sub_county=new String[temp.size()];

                            for (int x=0;x<temp.size();x++){
                                sub_county[x]=users.get(x).getSubcountyName();
                            }

                            subCounty.add(sub_county);
                            */
                        }


                        //populate the first spinner
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(location_activity.this, android.R.layout.simple_spinner_item, county);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        county_spinner.setAdapter(adapter);


                        //on item select of the spinner
                        county_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                //populate second view depending on the first spinner's choice

                                List<String> choice=new ArrayList<>();
                                choice= Arrays.asList(subCounty.get(position));
                                List<String> subcounties=new ArrayList<>();

                                for (String item:choice){
                                    subcounties.add(item);
                                }
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(location_activity.this, android.R.layout.simple_spinner_item, subcounties);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                sub_countySpinner.setAdapter(adapter);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                Toast.makeText(location_activity.this,"You have to select something",Toast.LENGTH_SHORT).show();

                            }
                        });

                        /*
                        county_spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                            }
                        });
                        */

                    }
                })


                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(location_activity.this,"Error getting information from database",Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
