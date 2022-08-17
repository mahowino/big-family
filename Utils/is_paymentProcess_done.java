package com.example.bigfamilyv20.Utils;

public class is_paymentProcess_done {
    private static boolean is_processDone;

    public is_paymentProcess_done() {
        is_processDone=false;
    }

    public static boolean isIs_processDone() {
        return is_processDone;
    }

    public static void setIs_processDone(boolean is_processDone) {
        is_paymentProcess_done.is_processDone = is_processDone;
    }
}
