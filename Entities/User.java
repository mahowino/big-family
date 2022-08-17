package com.example.bigfamilyv20.Entities;

import com.example.bigfamilyv20.Utils.PasswordUtils;

import java.util.ArrayList;

public class User {
    private static String firstName,LastName,Email,id;
    private static String phoneNumber;
    private static ArrayList<Integer> TokenAccountsPhoneNumbers;
    public User(String phoneNumber){
        this.phoneNumber=phoneNumber;

    }
    public User(){

    }

    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        User.id = id;
    }

    public static String getFirstName() {
        return firstName;
    }

    public static void setFirstName(String firstName) {
        User.firstName = firstName;
    }

    public static String getLastName() {
        return LastName;
    }

    public static void setLastName(String lastName) {
        LastName = lastName;
    }

    public static String getEmail() {
        return Email;
    }

    public static void setEmail(String email) {
        Email = email;
    }

    public static String getPhoneNumber() {
        return phoneNumber;
    }

    public static void setPhoneNumber(String phoneNumber) {
        User.phoneNumber = phoneNumber;
    }

    public static ArrayList<Integer> getTokenAccountsPhoneNumbers() {
        return TokenAccountsPhoneNumbers;
    }

    public static void setTokenAccountsPhoneNumbers(ArrayList<Integer> tokenAccountsPhoneNumbers) {
        TokenAccountsPhoneNumbers = tokenAccountsPhoneNumbers;
    }
    public static void AuthenticateWithPhone(String phoneNumber,String password){
    //check database if phoneNumber /password matches

//updateUi
    }
    public static boolean deleteUser(String PhoneNumber){
        //check the database for the specific user
        //remove his name from login credentials

        return true;
    }

}
