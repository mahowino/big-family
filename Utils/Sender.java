package com.example.bigfamilyv20.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Sender {

    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String NUMBER = "0123456789";
    private static final String DATA_FOR_RANDOM_STRING = CHAR_LOWER + CHAR_UPPER + NUMBER;

    public static int generateRandomcode(int max,int min){
        int random_int = (int)(Math.random() * (max - min + 1) + min);

        return random_int;
    }
    public static String generateRandomString(int length, List<String> IDS) {
        if (length < 1) throw new IllegalArgumentException();


        Random random=new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {

            // 0-62 (exclusive), random returns 0-61
            int rndCharAt = random.nextInt(DATA_FOR_RANDOM_STRING.length());
            char rndChar = DATA_FOR_RANDOM_STRING.charAt(rndCharAt);

            // debug
            System.out.format("%d\t:\t%c%n", rndCharAt, rndChar);

            sb.append(rndChar);

        }
        for (int x=0;x<IDS.size();x++){
            if(sb.toString().equals(IDS.get(x))){
                generateRandomString(length,IDS);
                break;

            }        }

        return sb.toString();

    }
    private static boolean checkIfRandomExists(int random){
        //if random number exists or is null
        //return true
        //else
        return false;
    }

}
