package com.example.bigfamilyv20.Activities;


import android.content.Intent;
import android.media.tv.TvContract;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bigfamilyv20.R;
import com.example.bigfamilyv20.Utils.InternetChecker;

public class noInternet extends AppCompatActivity {
    private static Button reload;
    private static ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);


    }
}
