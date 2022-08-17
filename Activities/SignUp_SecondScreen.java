package com.example.bigfamilyv20.Activities;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.bigfamilyv20.Entities.User;
import com.example.bigfamilyv20.R;

public class SignUp_SecondScreen extends AppCompatActivity {
private static Button signup;
private static EditText fName,lName,Email;
private static ConstraintLayout errorPanel;
private static TextView error;
private static ProgressBar bar;
private static String number;
private static final String SPECIAL_CHARACTERS="!@#$%^&*()_+|:<>?|~";
    public User LogedInUser;

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up__second_screen);
        signup=(Button)findViewById(R.id.SignUpFinal);
        fName=(EditText)findViewById(R.id.firstName);
        lName=(EditText)findViewById(R.id.LastName);
        Email=(EditText)findViewById(R.id.Email);
        errorPanel=(ConstraintLayout)findViewById(R.id.ErrorPanelSignUp2);
        error=(TextView)findViewById(R.id.errorMessageSignUp_2);
        bar=(ProgressBar)findViewById(R.id.progressBarSignUp2);
        number=getIntent().getExtras().getString("UserPhone");
        signup.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            bar.setVisibility(View.VISIBLE);
            String firstName=fName.getText().toString().trim().toLowerCase();
            String lastName=lName.getText().toString().trim().toLowerCase();
            String EmailName=Email.getText().toString().trim().toLowerCase();
            String Error;
             if(firstName.isEmpty() || lastName.isEmpty()){
                 Error="Fill in All Details";
             }

             else if(EmailName.isEmpty()==false && (!EmailName.contains("@")|| !EmailName.contains(".com"))){
                 Error="Write a valid email";

             }
             else {
                 Error=null;
             }
             for(int x=0;x<SPECIAL_CHARACTERS.length();x++){
                 if (firstName.contains(String.valueOf(SPECIAL_CHARACTERS.charAt(x))) || lastName.contains(String.valueOf(SPECIAL_CHARACTERS.charAt(x)))){
                     Error="Invalid names used";
                     break;
                 }
            }
            if(Error!=null){
                showError(Error);

            }
            else{

                Intent intent=new Intent(getApplicationContext(), Password.class);
                intent.putExtra("FirstName",firstName);
                intent.putExtra("LastName",lastName);
                intent.putExtra("UserPhone",number);
                if(EmailName.isEmpty()){
                    intent.putExtra("Email","none");

                }
                else {
                    intent.putExtra("Email", EmailName);


                }


                startActivity(intent);
                finish();
            }

        }
    });
    }
    private static void showError(String Error){

        bar.setVisibility(View.INVISIBLE);
        error.setText(Error);
        error.setVisibility(View.VISIBLE);
        errorPanel.setVisibility(View.VISIBLE);
        Thread errortime = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                    errorPanel.setVisibility(View.INVISIBLE);
                    //ErrorMessage.setText(Error);
                    error.setVisibility(View.INVISIBLE);
                } catch (Exception e) {
                    //Toast.makeText(SignUp_First_Screen.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        };
        errortime.start();
    }

}
