package com.example.bigfamilyv20.Utils;

import android.widget.EditText;

import java.util.ArrayList;

public class InputErrors {
    private static String error=null;

    public InputErrors(){
      error=null;
    }

    public static String emptyEditTextError(ArrayList<String> text){
        for(int x=0;x>text.size();x++){
        if(text.get(x).isEmpty()){
            error="Fill In All Details";
        }}
        return error;
    }


    public static String emptyEditTextError(String text){

            if(text.isEmpty()){
                error="Fill In All Details";
            }
        return error;
    }


    public static String phoneNumberValid(String text){
        if(text.length()!=9 )
            error="number is invalid";

        try {
            int no = Integer.parseInt(text);

        }
        catch (Exception E){
            error="number is invalid";
            return error;

        }
      return error;

    }


    public static String emailValidity(EditText text){
        String email=text.getText().toString().trim();
        if(!email.contains("@")){
            error="enter vaild email";

        }
        return error;


    }


}
