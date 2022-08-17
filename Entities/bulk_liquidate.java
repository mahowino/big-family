package com.example.bigfamilyv20.Entities;

import java.util.ArrayList;
import java.util.List;

public class bulk_liquidate {
    private static List<String>SdocumentIds,Snames,Sdescription,Sprice;
    private static List<Long>SprodId;
    private static List<Integer>Samount,Samounter;

    public static List<String> getSdocumentIds() {
        return SdocumentIds;
    }

    public static List<Integer> getSamounter() {
        return Samounter;
    }

    public static void setSamounter(List<Integer> samounter) {
        Samounter = samounter;
    }

    public static void setSdocumentIds(List<String> sdocumentIds) {
        SdocumentIds = sdocumentIds;
    }

    public static List<String> getSnames() {
        return Snames;
    }

    public static void setSnames(List<String> snames) {
        Snames = snames;
    }

    public static List<String> getSdescription() {
        return Sdescription;
    }

    public static void setSdescription(List<String> sdescription) {
        Sdescription = sdescription;
    }

    public static List<String> getSprice() {
        return Sprice;
    }

    public static void setSprice(List<String> sprice) {
        Sprice = sprice;
    }

    public static List<Long> getSprodId() {
        return SprodId;
    }

    public static void setSprodId(List<Long> sprodId) {
        SprodId = sprodId;
    }

    public static List<Integer> getSamount() {
        return Samount;
    }

    public static void setSamount(List<Integer> samount) {
        Samount = samount;
    }
}
