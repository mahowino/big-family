//issues
// cannot reverse the order of puting items in the cart when bulking up

package com.example.bigfamilyv20.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.bigfamilyv20.Activities.MainPage;
import com.example.bigfamilyv20.Activities.bulk_up_calculations;
import com.example.bigfamilyv20.Activities.sendTo;
import com.example.bigfamilyv20.Entities.Basket_quantities_container;
import com.example.bigfamilyv20.Entities.namesArray;
import com.example.bigfamilyv20.Entities.sending_data_structure;
import com.example.bigfamilyv20.Entities.wholesale_bulk;
import com.example.bigfamilyv20.R;
import com.example.bigfamilyv20.Utils.LoadingUtils;
import com.example.bigfamilyv20.Utils.shoppingCartUtilis;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class basket extends Fragment {

    private static List<Double> price,bulk_prices;
    private static List<Integer>bulk_amounts;
    private static int x,total;
    private static List<String> pname,pdesc,pprice,DOCUMENTIDz;
    private static List<Integer>amount,wholesale_Amounts,amountz,bulkamou;
    private static List<sending_data_structure>  items_quant;
    private static List<Long>pProdId,productz;
    private static ListView Lview;
    private static TextView totalPrice;
    private static Button send_basket,bulk_up;
    private static FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
    private static ListAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_my_store, container, false);

        pname=new ArrayList<>();
        pdesc=new ArrayList<>();
        pprice=new ArrayList<>();
        amount=new ArrayList<>();
        bulkamou=new ArrayList<>();
        items_quant=new ArrayList<>();
        DOCUMENTIDz=new ArrayList<>();
        items_quant= Basket_quantities_container.getBasket_items();
        bulk_up=(Button)view.findViewById(R.id.bulk_up_btn);
        send_basket=(Button)view.findViewById(R.id.send_basket_btn);
        Lview=(ListView)view.findViewById(R.id.list_storeitems);
        pProdId=new ArrayList<>();
        total=0;
        shoppingCartUtilis dbhelp=new shoppingCartUtilis(getContext());
        SQLiteDatabase db= dbhelp.getReadableDatabase();
        Cursor cursor =dbhelp.getdata(db);
        if (cursor.moveToFirst()){
            x=0;
            while(!cursor.isAfterLast()){

                pname.add(x,cursor.getString(cursor.getColumnIndex("NAME")));
                pdesc.add(x,cursor.getString(cursor.getColumnIndex("DESCRIPTION")));
                pprice.add(x,cursor.getString(cursor.getColumnIndex("PRICE")));
                pProdId.add(x,cursor.getLong(cursor.getColumnIndex("PRODUCTNO")));
                amount.add(x,cursor.getInt(cursor.getColumnIndex("AMOUNT")));
                DOCUMENTIDz.add(x,cursor.getString(cursor.getColumnIndex("DOCUMENTID")));

                Log.d("data in", "Cursor:  "+pname.get(x));
                x++;
                // do what ever you want here
                cursor.moveToNext();
            }
        }
        cursor.close();
        adapter=new adapter();
        Lview.setAdapter(adapter);


        send_basket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pname.size()<=0){
                    Toast.makeText(getContext(),"No items exist in basket",Toast.LENGTH_SHORT).show();
                }
                else {

                    Intent intent=new Intent(getContext(), sendTo.class);
                    namesArray array=new namesArray();
                    array.setName(pname);
                    array.setDocIds(DOCUMENTIDz);
                    array.setPrice(pprice);
                    array.setProductId(pProdId);
                    startActivity(intent);

                }

            }
        });

        bulk_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //initializations

                if(pname.size()<=0){
                    Toast.makeText(getContext(),"No items exist in basket",Toast.LENGTH_SHORT).show();
                }
                else {
                    wholesale_Amounts=new ArrayList<>();
                    price=new ArrayList<>();
                    productz=new ArrayList<>();


                    amountz=new ArrayList<>();
                    bulk_prices=new ArrayList<>();



                    //getting the products ,to find their wholesale values
                    firebaseFirestore.collection("Products").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            for (DocumentSnapshot snapshot:task.getResult()){
                                if (task.isSuccessful()){

                                    productz.add(snapshot.getLong("productCode"));
                                    amountz.add(Integer.parseInt(snapshot.get("p_WholesaleQuantity").toString().trim()));
                                    bulk_prices.add(Double.parseDouble(snapshot.get("p_WholesalePrice").toString().trim()));

                                }
                            }

                            //get the exact wholesale details for every product currently in the store
                            for (int x=0;x<pProdId.size();x++){
                                for (int y=0;y<amountz.size();y++){
                                    if (pProdId.get(x).equals(productz.get(y))){
                                        wholesale_Amounts.add(amountz.get(y));
                                        price.add(bulk_prices.get(y));
                                    }

                                }
                            }

                            //get the amount ,chosen to bulk up with
                            shoppingCartUtilis dbhelp=new shoppingCartUtilis(getContext());
                            SQLiteDatabase db= dbhelp.getReadableDatabase();
                            Cursor cursor =dbhelp.getdata(db);

                            if (cursor.moveToFirst()){
                                x=0;
                                while(!cursor.isAfterLast()){

                                    //contains amount that is chosen in basket, of the goods allowed to be bulked up by user
                                    bulkamou.add(x,cursor.getInt(cursor.getColumnIndex("AMOUNT")));

                                    x++;
                                    // do what ever you want here
                                    cursor.moveToNext();
                                }
                            }
                            cursor.close();

                            wholesale_bulk bulk = new wholesale_bulk();
                            bulk.setBnames(pname);

                            //total amount of retail goods available
                            bulk.setBamount(amount);
                            bulk.setBdescription(pdesc);
                            bulk.setBdocumentIds(DOCUMENTIDz);
                            bulk.setBprice(pprice);
                            bulk.setBprodId(pProdId);
                            bulk.setWprice(price);
                            bulk.setWbulk_amounts(wholesale_Amounts);

                            //amount put/chosen in the basket
                            bulk.setBsmakk_amount(bulkamou);
                            Intent intent = new Intent(getContext(), bulk_up_calculations.class);
                            startActivity(intent);



                        }
                    });

                }




            }
        });

        return view;
    }
    class ProductCarder {

        String Pname, Pdescription, Pprice;
        int Pamount;
        Long productid;

        public ProductCarder(String pname, String pdescription,  int pamount) {
            this.Pname = pname;
            this.Pdescription = pdescription;

            this.Pamount = pamount;
        }

        public ProductCarder(String pname, String pdescription, String pprice,Long productid,int pamount) {
            this.Pname = pname;
            this.Pdescription = pdescription;
            this.Pprice = pprice;
            this.productid=productid;
            this.Pamount=pamount;

        }
    }
    class adapter extends BaseAdapter {
        int number;
        ArrayList<ProductCarder> list=new ArrayList<>();
        public adapter() {
            for (int i=0;i<pname.size();i++){
                list.add(i,new ProductCarder(pname.get(i),pdesc.get(i),pprice.get(i),pProdId.get(i),amount.get(i)));
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
           // TextView description=(TextView)CustomInflater.findViewById(R.id.desc_cart);
            final TextView amt=(TextView)CustomInflater.findViewById(R.id.basket_item_name_amount);
            final ImageButton add=(ImageButton)CustomInflater.findViewById(R.id.basket_item_name_buttonRemoveitem_buttonAdditem);
            final ImageButton Subtract=(ImageButton)CustomInflater.findViewById(R.id.basket_item_name_buttonRemoveitem);
            final ProductCarder card=list.get(position);
            Button remove=(Button)CustomInflater.findViewById(R.id.item_remove_from_db);
            price.setText(card.Pname);
            namer.setText(card.Pdescription);
            amt.setText(String.valueOf(1));

            int full_quant = 0;

            for (int x=0;x<items_quant.size();x++){
                if(items_quant.get(x).getProductId().equals(card.productid)){
                    full_quant=items_quant.get(x).getFinalAmount();
                }
            }

            final shoppingCartUtilis shop=new shoppingCartUtilis(getContext());
            shop.EditRecord("AMOUNT",1,card.productid);

            // description.setText(card.Pdescription);
            Subtract.setVisibility(View.INVISIBLE);
              if(full_quant==1) {
                  add.setVisibility(View.INVISIBLE);
              }


            final int finalFull_quant = full_quant;
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Confirm")
                            .setMessage("Remove Item from Basket??")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("no", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).setNegativeButton("Yes", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            shop.deleteRecord(card.productid);
                            pname.clear();
                            pdesc.clear();
                            pprice.clear();
                            pProdId.clear();
                            DOCUMENTIDz.clear();
                            DOCUMENTIDz.clear();
                            shoppingCartUtilis dbhelp=new shoppingCartUtilis(getContext());
                            SQLiteDatabase db= dbhelp.getReadableDatabase();
                            Cursor cursor =dbhelp.getdata(db);
                            getData(cursor);
                            adapter=new adapter();
                            Lview.setAdapter(adapter);

                        }
                    }).show();

                }
            });

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
                    if(number>= finalFull_quant){
                        add.setVisibility(View.INVISIBLE);
                    }
                    shop.EditRecord("AMOUNT",number,card.productid);

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
                    if(number<finalFull_quant){
                        add.setVisibility(View.VISIBLE);

                    }
                    shop.EditRecord("AMOUNT",number,card.productid);

                }
            });
            return CustomInflater;
        }
    }
    private static void getData(Cursor cursor){
        if (cursor.moveToFirst()){
            x=0;
            while(!cursor.isAfterLast()){

                pname.add(x,cursor.getString(cursor.getColumnIndex("NAME")));
                pdesc.add(x,cursor.getString(cursor.getColumnIndex("DESCRIPTION")));
                pprice.add(x,cursor.getString(cursor.getColumnIndex("PRICE")));
                pProdId.add(x,cursor.getLong(cursor.getColumnIndex("PRODUCTNO")));
                amount.add(x,cursor.getInt(cursor.getColumnIndex("AMOUNT")));
                DOCUMENTIDz.add(x,cursor.getString(cursor.getColumnIndex("DOCUMENTID")));

                Log.d("data in", "Cursor:  "+pname.get(x));
                x++;
                // do what ever you want here
                cursor.moveToNext();
            }
        }
        cursor.close();
    }
}
