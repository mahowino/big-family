package com.example.bigfamilyv20.Activities;


import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bigfamilyv20.Entities.User;
import com.example.bigfamilyv20.R;

public class ViewProfile extends AppCompatActivity {
    private static TextView fname,lname,number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        fname=(TextView)findViewById(R.id.fnameprof);
        lname=(TextView)findViewById(R.id.lnameprof);
        number=(TextView)findViewById(R.id.numbProf);

        fname.setText(User.getFirstName());
        lname.setText(User.getLastName());
        number.setText(User.getPhoneNumber());

    }

}
