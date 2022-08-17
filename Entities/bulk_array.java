package com.example.bigfamilyv20.Entities;

import java.util.ArrayList;
import java.util.List;

public class bulk_array {
    private static List<String>BdocumentIds,Bnames,Bdescription,Bprice,BpackName;
    private static List<Long>BprodId;
    private static List<Integer>Bamount,retailEquivalent;

    public bulk_array() {
        BdocumentIds=new ArrayList<>();
        Bnames=new ArrayList<>();
        Bdescription=new ArrayList<>();
        Bprice=new ArrayList<>();
        BprodId=new ArrayList<>();
        Bamount=new ArrayList<>();
    }

    public static List<Integer> getretailEquivalent() {
        return retailEquivalent;
    }

    public static void setretailEquivalent(List<Integer> retailEquivalent) {
        bulk_array.retailEquivalent = retailEquivalent;
    }

    public static List<String> getBpackName() {
        return BpackName;
    }

    public static void setBpackName(List<String> bpackName) {
        BpackName = bpackName;
    }

    public static List<String> getBdocumentIds() {
        return BdocumentIds;
    }

    public static void setBdocumentIds(List<String> bdocumentIds) {
        BdocumentIds = bdocumentIds;
    }

    public static List<String> getBnames() {
        return Bnames;
    }

    public static void setBnames(List<String> bnames) {
        Bnames = bnames;
    }

    public static List<String> getBdescription() {
        return Bdescription;
    }

    public static void setBdescription(List<String> bdescription) {
        Bdescription = bdescription;
    }

    public static List<String> getBprice() {
        return Bprice;
    }

    public static void setBprice(List<String> bprice) {
        Bprice = bprice;
    }

    public static List<Long> getBprodId() {
        return BprodId;
    }

    public static void setBprodId(List<Long> bprodId) {
        BprodId = bprodId;
    }

    public static List<Integer> getBamount() {
        return Bamount;
    }

    public static void setBamount(List<Integer> bamount) {
        Bamount = bamount;
    }
}
