//issues

// amount to pay not calculating well


package com.example.bigfamilyv20.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bdhobare.mpesa.Mpesa;
import com.bdhobare.mpesa.interfaces.AuthListener;
import com.bdhobare.mpesa.interfaces.MpesaListener;
import com.bdhobare.mpesa.models.STKPush;
import com.bdhobare.mpesa.utils.Pair;
import com.example.bigfamilyv20.Entities.Bulk_item;
import com.example.bigfamilyv20.Entities.wholesale_bulk;
import com.example.bigfamilyv20.R;

import java.util.ArrayList;
import java.util.List;

public class bulk_up_calculations extends AppCompatActivity  {
    private static List<Double> price,Money_to_bulk;
    private static List<Integer>bulk_amounts,nextBulk,PreviousBulk;
    private static List<String> pname,pdesc,pprice;
    private static List<Integer>amount,smallAmount;
    private static List<Long>pProdId;
    private static double total=0.0;
    private static TextView totalPrice;
    private static ListView view;
    private static Button payCart;
    private static  ListAdapter Listadapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        price=new ArrayList<>();


        //retail quantity to actually bulk up to
        smallAmount=new ArrayList<>();
        bulk_amounts=new ArrayList<>();
        pname=new ArrayList<>();
        pdesc=new ArrayList<>();
        pprice=new ArrayList<>();

        //total retail packs available
        amount=new ArrayList<>();

        pProdId=new ArrayList<>();
        Money_to_bulk=new ArrayList<>();
        nextBulk=new ArrayList<>();
        PreviousBulk=new ArrayList<>();
        total=0.0;
        view=(ListView)findViewById(R.id.list_bulk_amounts);
        totalPrice=(TextView)findViewById(R.id.price_for_bulk_up);
        payCart=(Button) findViewById(R.id.bulk_up_buttn);

        price= wholesale_bulk.getWprice();
        bulk_amounts=wholesale_bulk.getWbulk_amounts();
        pname=wholesale_bulk.getBnames();
        pprice=wholesale_bulk.getBprice();
        pdesc=wholesale_bulk.getBdescription();
        pProdId=wholesale_bulk.getBprodId();
        amount=wholesale_bulk.getBamount();


        //amount that has been chosen from basket, of goods that have been allowed to be bulked
        smallAmount=wholesale_bulk.getBsmakk_amount();

        //getBulkDetails
        Bulk_item item;
            for (int x=0;x<bulk_amounts.size();x++){

                Log.i("bulk", "onCreate: "+bulk_amounts.get(x));
                item=new Bulk_item(bulk_amounts.get(x),price.get(x),smallAmount.get(x));
                total=total+item.getPrice_to_pay();
                Money_to_bulk.add(x,item.getPrice_to_pay());
                nextBulk.add(x,item.getNext_bulk());
                PreviousBulk.add(x,item.getPrevious_bulk());

            }

        Log.i("bulk next", "onClick: "+pname.get(0));
        Log.i("bulk next", "onClick: size "+pname.size());
        Log.i("bulk next", "onClick: "+bulk_amounts.get(0));
        Log.i("bulk next", "onClick:size "+bulk_amounts.size());
        Log.i("bulk next", "onClick: "+price.get(0));
        Log.i("bulk next", "onClick: size "+price.size());
        Log.i("bulk next", "onClick: "+Money_to_bulk.get(0));
        Log.i("bulk next", "onClick: size "+Money_to_bulk.size());
        Log.i("bulk next", "onClick: "+smallAmount.get(0));
        Log.i("bulk next", "onClick: size "+smallAmount.size());
        Log.i("bulk next", "onClick: "+nextBulk.get(0));
        Log.i("bulk next", "onClick: size "+nextBulk.size());
        Log.i("bulk next", "onClick: "+PreviousBulk.get(0));
        Log.i("bulk next", "onClick: size "+PreviousBulk.size());

       Listadapter=new adapter();
        view.setAdapter(Listadapter);
        totalPrice.setText("KSH "+total+" /=");

        payCart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getApplicationContext(),Cart_Activity.class);
                intent.putExtra("userDocId"," ");
                intent.putExtra("price",String.valueOf(total));
                intent.putExtra("Service","bulk_goods_up");
                intent.putExtra("number","0");
                intent.putExtra("reason","bulk_goods_up");
                intent.putExtra("docs","0");
                intent.putExtra("pass","0");

                //sets up the next bulk , without neccesarily overwriting the information on how much is in the store, and how much is the amount set to be bulked up
                wholesale_bulk.setNextBulk(nextBulk);
                startActivity(intent);


            }

        });

    }



    class Bulk_items{

        String Pname, Pdescription ;
        int next_bulk,previous_bulk,wholesale_amount;
        Long productid;
        double Pprice;

        public Bulk_items(String pname, String pdescription, Double pprice, int next_bulk,int previous_bulk,int wholesale_amount) {

            this.Pname = pname;
            this.Pdescription = pdescription;
            this.Pprice = pprice;
            this.next_bulk = next_bulk;
            this.previous_bulk = previous_bulk;
            this.wholesale_amount=wholesale_amount;

        }

    }

    class adapter extends BaseAdapter{
        ArrayList<Bulk_items> list=new ArrayList<>();
        public adapter() {
            for (int i=0;i<pname.size();i++){
                list.add(i,new Bulk_items(pname.get(i),pdesc.get(i),Money_to_bulk.get(i),nextBulk.get(i),PreviousBulk.get(i),bulk_amounts.get(i)));
            }
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View CustomInflater= getLayoutInflater().inflate(R.layout.product_list_store,null);

            TextView price=(TextView)CustomInflater.findViewById(R.id.price_cart);
            TextView namer=(TextView)CustomInflater.findViewById(R.id.name_cart);
            TextView description=(TextView)CustomInflater.findViewById(R.id.desc_cart);
            final TextView amt=(TextView)CustomInflater.findViewById(R.id.amt_cart);
            final ImageButton add=(ImageButton)CustomInflater.findViewById(R.id.buttonAdditem);
            final ImageButton Subtract=(ImageButton)CustomInflater.findViewById(R.id.buttonRemoveitem);
            final TextView retail_quantities=(TextView)CustomInflater.findViewById(R.id.retail_amounts);


            retail_quantities.setVisibility(View.VISIBLE);
            final Bulk_items card=list.get(position);

            price.setText(Math.ceil(card.Pprice)+" /=");
            namer.setText(card.Pname);
            description.setText(card.Pdescription);
            add.setVisibility(View.INVISIBLE);
            Subtract.setVisibility(View.INVISIBLE);
            amt.setText(String.valueOf(card.next_bulk));
            int retailquant=(card.wholesale_amount)*(card.next_bulk);
            retail_quantities.setText(retailquant+" Retail quantities");
            // final Database_helper helper=new Database_helper(viewcart.this);


           // total=Math.ceil(total+card.Pprice);


            return CustomInflater;
        }
    }
    }

