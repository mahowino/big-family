package com.example.bigfamilyv20.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bigfamilyv20.Entities.bulk_array;
import com.example.bigfamilyv20.Entities.bulk_liquidate;
import com.example.bigfamilyv20.R;

import java.util.ArrayList;
import java.util.List;

public class next_step_send_bulk extends AppCompatActivity {


    private static List<String> SdocumentIds,Snames,Sdescription,Sprice;
    private static List<Long>SprodId,SprodInit;
    private static List<Integer>Samount,Samount_big,SamountFinal;
    private static Button next;
    private static ListView listView;
    private static  ListAdapter adapter_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_step_send_bulk);
        next=(Button)findViewById(R.id.button_initiate_sending_bulk);
        listView=(ListView)findViewById(R.id.list_send_bulk_items);

        SprodId=new ArrayList<>( );
        Snames=new ArrayList<>( );
        Sdescription=new ArrayList<>( );
        Sprice=new ArrayList<>( );
        Samount=new ArrayList<>( );
        SdocumentIds=new ArrayList<>( );
        SamountFinal=new ArrayList<>();
        Samount_big=new ArrayList<>();
        SprodInit=new ArrayList<>();

        Snames= bulk_liquidate.getSnames( );
        SprodId=bulk_liquidate.getSprodId( );
        Sprice=bulk_liquidate.getSprice( );
        Sdescription=bulk_liquidate.getSdescription( );
        Samount=bulk_liquidate.getSamount( );
        SdocumentIds=bulk_liquidate.getSdocumentIds( );

        Samount_big= bulk_liquidate.getSamounter();
       // SprodInit= bulk_array.getBprodId();

        adapter_list=new adapter();
        listView.setAdapter(adapter_list);

        Log.i("bulk next", "onClick: "+Snames.get(0));
        Log.i("bulk next", "onClick: size "+Snames.size());
        Log.i("bulk next", "onClick: "+Samount.get(0));
        Log.i("bulk next", "onClick:size "+Samount.size());
        Log.i("bulk next", "onClick: "+Sdescription.get(0));
        Log.i("bulk next", "onClick: size "+Sdescription.size());
        Log.i("bulk next", "onClick: "+SdocumentIds.get(0));
        Log.i("bulk next", "onClick: size "+SdocumentIds.size());
        Log.i("bulk next", "onClick: "+Sprice.get(0));
        Log.i("bulk next", "onClick: size "+Sprice.size());
        Log.i("bulk next", "onClick: "+SprodId.get(0));
        Log.i("bulk next", "onClick: size "+SprodId.size());

        Log.i("bulk next", "onClick: snames "+Snames.size());
        Log.i("bulk next", "onClick: samountbig"+Samount_big.size());


        Log.i("bulk next", "onClick:samountfinal "+SamountFinal.size());
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //bulk_liquidate bulker=new bulk_liquidate();
                bulk_liquidate.setSamounter(Samount_big);
                Intent intent=new Intent(getApplicationContext(),send_bulk_to.class);
                startActivity(intent);
                finish();

            }
        });

    }
    class ProductCarder {

        String Pname, Pdescription, Pprice;
        int Pamount,bigAmount;
        Long productid;


        public ProductCarder(String pname, String pdescription,  int pamount) {
            this.Pname = pname;
            this.Pdescription = pdescription;

            this.Pamount = pamount;
        }

        public ProductCarder(String pname, String pdescription, String pprice,Long productid,int pamount,int bigAmount) {
            this.Pname = pname;
            this.Pdescription = pdescription;
            this.Pprice = pprice;
            this.productid=productid;
            this.Pamount=pamount;
            this.bigAmount=bigAmount;

        }
    }
    class adapter extends BaseAdapter {
        int number;
        ArrayList<ProductCarder> list=new ArrayList<>();
        public adapter() {
            for (int i=0;i<Sdescription.size();i++){
                list.add(i,new ProductCarder(Snames.get(i),Sdescription.get(i),Sprice.get(i),SprodId.get(i),Samount.get(i),Samount_big.get(i)));
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

            View CustomInflater= getLayoutInflater().inflate(R.layout.basket_item,null);
            TextView price=(TextView)CustomInflater.findViewById(R.id.basket_item_name);
            TextView namer=(TextView)CustomInflater.findViewById(R.id.basket_item_desc);
            Button remove=(Button)CustomInflater.findViewById(R.id.item_remove_from_db);
            remove.setVisibility(View.INVISIBLE);
            // TextView description=(TextView)CustomInflater.findViewById(R.id.desc_cart);
            final TextView amt=(TextView)CustomInflater.findViewById(R.id.basket_item_name_amount);
            final ImageButton add=(ImageButton)CustomInflater.findViewById(R.id.basket_item_name_buttonRemoveitem_buttonAdditem);
            final ImageButton Subtract=(ImageButton)CustomInflater.findViewById(R.id.basket_item_name_buttonRemoveitem);
            final ProductCarder card=list.get(position);
            price.setText(card.Pname);
            namer.setText(card.Pdescription);
            amt.setText(String.valueOf(1));
            // description.setText(card.Pdescription);
            Subtract.setVisibility(View.INVISIBLE);

            if(card.Pamount==1) {
                add.setVisibility(View.INVISIBLE);
            }


            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int number;
                    number = Integer.parseInt(amt.getText().toString());

                    number = number + 1;
                    amt.setText(String.valueOf(number));

                    if(number>1){
                        Subtract.setVisibility(View.VISIBLE);
                    }
                    if(number>=card.Pamount){
                        add.setVisibility(View.INVISIBLE);
                    }

                    for (int x=0;x<SprodId.size();x++){
                         if (SprodId.get(x)==card.productid){
                             Samount_big.set(x,number);
                         }

                    }

                }

            });

            Subtract.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int number;
                    number=Integer.parseInt(amt.getText().toString());

                    number=number-1;
                    amt.setText(String.valueOf(number));

                    if (number==1){
                        Subtract.setVisibility(View.INVISIBLE);
                    }
                    if(number<card.Pamount){
                        add.setVisibility(View.VISIBLE);

                    }

                    for (int x=0;x<SprodId.size();x++){
                        if (SprodId.get(x)==card.productid){
                            Samount_big.set(x,number);
                        }

                    }


                }
            });
            return CustomInflater;
        }
    }
}

