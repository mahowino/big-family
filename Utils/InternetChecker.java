package com.example.bigfamilyv20.Utils;

public class InternetChecker {

    public static boolean internetIsConnected() {
        try {
            String command = "ping -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {

            return false;

        }
    }

}
