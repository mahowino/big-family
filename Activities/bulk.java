package com.example.bigfamilyv20.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bigfamilyv20.Entities.bulk_array;
import com.example.bigfamilyv20.Entities.bulk_liquidate;
import com.example.bigfamilyv20.R;
import com.example.bigfamilyv20.Utils.LoadingUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class bulk extends AppCompatActivity {
    private static List<String> BdocumentIds,Bnames,Bdescription,Bprice;
    private static List<Long>BprodId;
    private static List<Integer>Bamount,retail_equivalent;
    private static List<String> SdocumentIds,Snames,Sdescription,Sprice,PpackName;
    private static List<Long>SprodId;
    private static List<Integer>Samount,Samounter;
    private static ListView listView;
    private static FloatingActionButton basket;
    private static FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
    private List<Integer> amountz;
    private List<Integer> amo;
    private List<Long> productz;
    private List<Double> bulk_prices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bulk);
        listView=(ListView)findViewById(R.id.bulk_items_view);
        Toolbar toolbar = findViewById(R.id.include4);
        basket=(FloatingActionButton)findViewById(R.id.floating_button_bulk_basket);

        setSupportActionBar(toolbar);
        BprodId=new ArrayList<>( );
        Bnames=new ArrayList<>( );
        Bdescription=new ArrayList<>( );
        Bprice=new ArrayList<>( );
        Bamount=new ArrayList<>( );
        BdocumentIds=new ArrayList<>( );
        productz=new ArrayList<>();
        amountz=new ArrayList<>();
        bulk_prices=new ArrayList<>();
        amo=new ArrayList<>();
        retail_equivalent=new ArrayList<>();

        SprodId=new ArrayList<>( );
        Snames=new ArrayList<>( );
        Sdescription=new ArrayList<>( );
        Sprice=new ArrayList<>( );
        Samount=new ArrayList<>( );
        SdocumentIds=new ArrayList<>( );
        Samounter=new ArrayList<>();
        PpackName=new ArrayList<>();

        Bnames=bulk_array.getBnames( );
        BprodId=bulk_array.getBprodId( );
        Bprice=bulk_array.getBprice( );
        Bdescription=bulk_array.getBdescription( );
        Bamount=bulk_array.getBamount( );
        BdocumentIds=bulk_array.getBdocumentIds( );
        PpackName=bulk_array.getBpackName();
        retail_equivalent=bulk_array.getretailEquivalent();
        ListAdapter adapter=new adapter( );
        listView.setAdapter(adapter);

        basket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Snames.size()<=0){
                    Toast.makeText(bulk.this,"no item in the bulk basket",Toast.LENGTH_LONG).show();
                }
                else {
                    bulk_liquidate bulker=new bulk_liquidate();
                    Log.i("bulk next", "onClick: "+Snames.get(0));
                    Log.i("bulk next", "onClick: ");
                    Log.i("bulk next", "onClick: "+Samount.get(0));
                    Log.i("bulk next", "onClick: ");
                    Log.i("bulk next", "onClick: "+Sdescription.get(0));
                    Log.i("bulk next", "onClick: ");
                    Log.i("bulk next", "onClick: "+SdocumentIds.get(0));
                    Log.i("bulk next", "onClick: ");
                    Log.i("bulk next", "onClick: "+Sprice.get(0));
                    Log.i("bulk next", "onClick: ");
                    Log.i("bulk next", "onClick: "+SprodId.get(0));
                    Log.i("bulk next", "onClick: ");

                    bulker.setSnames(Snames);
                    bulker.setSamount(Samount);
                    bulker.setSdescription(Sdescription);
                    bulker.setSdocumentIds(SdocumentIds);
                    bulker.setSprice(Sprice);
                    bulker.setSprodId(SprodId);
                    bulker.setSamounter(Samounter);

                    Intent intent=new Intent(getApplicationContext(),next_step_send_bulk.class);
                    startActivity(intent);
                    finish();

                }



            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.liquid, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case  R.id.liquidate:
                Intent intent=new Intent(getApplicationContext(), liquidate_bulk.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    class BulkItems{
        String name,description,price,documentid,packName;
        int quantity,retail_productz;
        long productId;

        public BulkItems(String name, String description, String price, String documentid, int quantity, long productId,int retail_productz,String packName) {
            this.name = name;
            this.description = description;
            this.price = price;
            this.documentid = documentid;
            this.quantity = quantity;
            this.productId = productId;
            this.retail_productz=retail_productz;
            this.packName=packName;
        }
    }
    class adapter extends BaseAdapter{
       ArrayList<BulkItems> list=new ArrayList<>();

         public adapter() {

             for (int x=0;x<Bnames.size();x++){
             list.add(new BulkItems(Bnames.get(x),Bdescription.get(x),Bprice.get(x),BdocumentIds.get(x),Bamount.get(x),BprodId.get(x),retail_equivalent.get(x),PpackName.get(x)));
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
            View view=convertView;

            if(view==null){
                LayoutInflater layoutInflator = (LayoutInflater) bulk.this
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                view = layoutInflator.inflate(R.layout.bulk_item, parent, false);

            }

            //initialisation of the views
            TextView name=(TextView)view.findViewById(R.id.bulk_card_name);
            TextView description=(TextView)view.findViewById(R.id.bulk_card_desc);
            TextView retail_prodz=(TextView)view.findViewById(R.id.retail_packs_for_bulk);

            final TextView amount=(TextView)view.findViewById(R.id.bulk_card_amount);
            final Button sendGoods=(Button)view.findViewById(R.id.button_Send_bulk);
            final BulkItems items=list.get(position);

            name.setText(items.name);
            description.setText(items.description);
            if(items.quantity==1){
                amount.setText(items.quantity+" "+items.packName);
            }
            else{
                amount.setText(items.quantity+" "+items.packName+"s");
            }

            retail_prodz.setText(items.retail_productz+" items per "+items.packName);

            Button bulkDown=(Button)view.findViewById(R.id.button_bulk_down);

            sendGoods.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   //sendGoods.setText("assdbulk");
                    String isSendGoodTextActive=sendGoods.getText().toString();

                    if(isSendGoodTextActive.equalsIgnoreCase("Send Bulk")){

                        sendGoods.setText("added");

                        Snames.add(items.name);
                        SprodId.add(items.productId);
                        SdocumentIds.add(items.documentid);
                        Sprice.add(items.price);
                        Sdescription.add(items.description);
                        Samount.add(items.quantity);
                        Samounter.add(1);
                        Log.i("bulk next", "onClick: "+Snames.get(0));

                    }

                    else {
                        //display the send goods text for item
                     //   sendGoods.setText("assdbulk");
                       boolean isItemInBasket=false;
                        //checks for the specific item which has been removed
                        for (int x=0;x<SdocumentIds.size();x++){

                          if(SprodId.get(x).equals(items.productId)){
                              //remove it from the list
                              Snames.remove(x);
                              SprodId.remove(x);
                              SdocumentIds.remove(x);
                              Sprice.remove(x);
                              Sdescription.remove(x);
                              Samount.remove(x);
                              Samounter.remove(x);
                              sendGoods.setText("Send Bulk");

                              isItemInBasket=true;
                          }
                          if(isItemInBasket==false){
                              Toast.makeText(bulk.this,"item already in basket",Toast.LENGTH_LONG).show();

                          }
                        }


                    }
                }
            });

            bulkDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(bulk.this)
                            .setTitle("Confirm")
                            .setMessage("Do You Want to bulk down?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setNegativeButton("yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    final LoadingUtils utils=new LoadingUtils(bulk.this);
                                    utils.startLoadingDialog();
                                    firebaseFirestore
                                            .collection("Products")
                                            .whereEqualTo("productCode",items.productId)
                                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                            for (DocumentSnapshot snapshot : task.getResult()) {

                                                if (task.isSuccessful()) {

                                                    productz.add(snapshot.getLong("productCode"));
                                                    amountz.add(Integer.parseInt(snapshot.get("p_Price").toString().trim()));
                                                    bulk_prices.add(Double.parseDouble(snapshot.get("p_WholesalePrice").toString().trim()));
                                                    amo.add(Integer.parseInt(snapshot.get("p_WholesaleQuantity").toString().trim()));
                                                }


                                            }
                                            //declarations
                                            Double Wprice=0.0;
                                            Double finalAmount=0.0;
                                            int total_retail_packs=0;

                                            //get the amount of a single wholesale bulk
                                            int w_amount=amo.get(0);
                                            //get the total retail packs of a single wholesale bulk
                                             total_retail_packs=w_amount*items.quantity;
                                            //get the price worth in retail of the wholesale packs
                                            int retailPrices=total_retail_packs*amountz.get(0);
                                            //get the price worth in wholesale of the wholesale packs
                                            Wprice=bulk_prices.get(0)*items.quantity;

                                            //get the difference to find out how much money to add on
                                            finalAmount=retailPrices-Wprice;

                                            List<Integer>no=new ArrayList<>();
                                            no.add(total_retail_packs);
                                            Snames.clear();
                                            Samount.clear();
                                            SprodId.clear();
                                            SdocumentIds.clear();
                                            Sprice.clear();
                                            Sdescription.clear();

                                            Intent intent=new Intent(getApplicationContext(),Cart_Activity.class);
                                            bulk_array array=new bulk_array();

                                            Snames.add(items.name);
                                            SprodId.add(items.productId);
                                            SdocumentIds.add(items.documentid);
                                            Sprice.add(items.price);
                                            Sdescription.add(items.description);
                                            Samount.add(total_retail_packs);


                                            array.setBamount(no);
                                            array.setBnames(Snames);
                                            array.setBdescription(Sdescription);
                                            array.setBdocumentIds(SdocumentIds);
                                            array.setBprice(Sprice);
                                            array.setBprodId(SprodId);

                                            intent.putExtra("userDocId","");
                                            intent.putExtra("price",String.valueOf(finalAmount));
                                            intent.putExtra("Service","bulk_goods_down");
                                            intent.putExtra("number","");
                                            intent.putExtra("reason","bulk_goods_down");
                                            intent.putExtra("docs","0");
                                            intent.putExtra("pass","0");
                                            startActivity(intent);
                                            finish();

                                            utils.dismissDialog();


                                        }});}
                            }).setPositiveButton("no", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
                }
            });

             return view;

        }
    }
}
