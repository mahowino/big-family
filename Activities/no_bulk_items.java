package com.example.bigfamilyv20.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.bigfamilyv20.R;

public class no_bulk_items extends AppCompatActivity {
    private static Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_bulk_items);
        back=(Button)findViewById(R.id.backButtonBulkUp);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),MainPage.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
