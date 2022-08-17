package com.example.bigfamilyv20.Entities;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Bulk_item {
    //private static String item_name,item_description;
    private static double price_to_pay,item_wholesale_price,wholesale_pack_price;
    private static long item_product_code;
    private static int next_bulk,previous_bulk,bulk_amount_count,item_count;

    public Bulk_item(long item_product_code,int number_of_retail_products){
        this.item_product_code=item_product_code;
        //details for the specific item bulk


    }

    public static double getPrice_to_pay() {
        return price_to_pay;
    }

    public static int getNext_bulk() {
        return next_bulk;
    }

    public static int getPrevious_bulk() {
        return previous_bulk;
    }

    public Bulk_item(int bulk_amount_count, double item_wholesale_price, int item_count){
        this.item_wholesale_price=item_wholesale_price;
        this.item_count=item_count;
        this.bulk_amount_count=bulk_amount_count;
        price_to_pay=0;
        //details for the specific item bulk
        //details for next and previous bulk
        getNextAndPreviousBulk();



    }

    public static double getliquidateAmount(int bulkitemsno){
        double moneyToSend;
        moneyToSend=bulkitemsno*wholesale_pack_price;
        return moneyToSend;

    }


    private static void getNextAndPreviousBulk(){
        if(item_wholesale_price!=0 ){
            //get price for one single unit of a wholesale pack
            Double price=item_wholesale_price/bulk_amount_count;

            //get the number of full wholesale packs
            int result_one=item_count/bulk_amount_count;

            //get the remaining packs to a full pack
            int remainder=item_count%bulk_amount_count;
            int no_to_full_bulk=bulk_amount_count-remainder;

            if(no_to_full_bulk!=0){
                next_bulk=result_one+1;
                previous_bulk=result_one;
            }
            else {
                next_bulk=result_one;
                previous_bulk=result_one;
            }

            price_to_pay=no_to_full_bulk*price;

        }

        else
            {

            }

    }
}
