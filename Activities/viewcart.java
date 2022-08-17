package com.example.bigfamilyv20.Activities;


import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
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
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bigfamilyv20.Entities.ProductCard;
import com.example.bigfamilyv20.R;
import com.example.bigfamilyv20.Utils.Database_helper;

import java.util.ArrayList;
import java.util.List;

public class viewcart extends AppCompatActivity {

    private static int x,total;
    private static List<String> pname,pdesc,pprice;
    private static List<Long>pProdId;
    private static TextView totalPrice;
    private static Button checkOut;

    private static ListView view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewcart);
        view=(ListView)findViewById(R.id.listcart);
        totalPrice=(TextView)findViewById(R.id.price_cart_total);
        checkOut=(Button)findViewById(R.id.button_checkout);
        pname=new ArrayList<>();
        pdesc=new ArrayList<>();
        pprice=new ArrayList<>();
        pProdId=new ArrayList<>();
        total=0;
        Database_helper dbhelp=new Database_helper(this);
        SQLiteDatabase db= dbhelp.getReadableDatabase();
        Cursor cursor =dbhelp.getdata(db);
        if (cursor.moveToFirst()){
            x=0;
            while(!cursor.isAfterLast()){

                pname.add(x,cursor.getString(cursor.getColumnIndex("NAME")));
                pdesc.add(x,cursor.getString(cursor.getColumnIndex("DESCRIPTION")));
                pprice.add(x,cursor.getString(cursor.getColumnIndex("PRICE")));
                pProdId.add(x,cursor.getLong(cursor.getColumnIndex("PRODUCTNO")));
                Log.d("data in", "Cursor:  "+pname.get(x));
                x++;
                // do what ever you want here
                cursor.moveToNext();
            }
        }
        cursor.close();
        //list= ProductCard.CardPopulater(pname,pdesc,pprice);
        for(int x=0;x<pprice.size();x++){
            total=total+Integer.parseInt(pprice.get(x));
        }
        totalPrice.setText(String.valueOf(total));
        ListAdapter adapter=new adapter();
        ((adapter) adapter).notifyDataSetChanged();
        view.setAdapter(adapter);
        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(total==0){
                    Toast.makeText(viewcart.this,"cart is currently empty",Toast.LENGTH_SHORT).show();
                }
                else {
                Intent intent=new Intent(getApplicationContext(),Cart_Activity.class);
                intent.putExtra("price",String.valueOf(total));
                intent.putExtra("Service",String.valueOf(0));
                intent.putExtra("reason","Purchase of goods");
                startActivity(intent);
            }}
        });
    }
    class ProductCarder {

        String Pname, Pdescription, Pprice;
        int Pamount;
        Long productid;

        public ProductCarder(String pname, String pdescription, String pprice, int pamount) {
            this.Pname = pname;
            this.Pdescription = pdescription;
            this.Pprice = pprice;
            this.Pamount = pamount;
        }

        public ProductCarder(String pname, String pdescription, String pprice,Long productid) {
            this.Pname = pname;
            this.Pdescription = pdescription;
            this.Pprice = pprice;
            this.productid=productid;

        }
    }
    class adapter extends BaseAdapter {
        int number;
        ArrayList<ProductCarder> list=new ArrayList<>();
        public adapter() {
            for (int i=0;i<pname.size();i++){
                list.add(i,new ProductCarder(pname.get(i),pdesc.get(i),pprice.get(i),pProdId.get(i)));
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
            ImageButton add=(ImageButton)CustomInflater.findViewById(R.id.buttonAdditem);
            final ImageButton Subtract=(ImageButton)CustomInflater.findViewById(R.id.buttonRemoveitem);
            final ProductCarder card=list.get(position);
            price.setText(card.Pprice);
            namer.setText(card.Pname);
            description.setText(card.Pdescription);
            Subtract.setVisibility(View.INVISIBLE);
            final Database_helper helper=new Database_helper(viewcart.this);

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int number;
                        number = Integer.parseInt(amt.getText().toString());

                        number = number + 1;
                        amt.setText(String.valueOf(number));
                        total = total + (Integer.parseInt(card.Pprice));
                        totalPrice.setText("KSH"+total);
                        if(number>1){
                            Subtract.setVisibility(View.VISIBLE);
                        }

                   helper.EditRecord("AMOUNT",number,card.productid);

                }

            });

            Subtract.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int number;
                    number=Integer.parseInt(amt.getText().toString());

                        number=number-1;
                        amt.setText(String.valueOf(number));
                        total=total-(Integer.parseInt(card.Pprice));
                        totalPrice.setText("KSH"+total);
                    if (number==1){
                        Subtract.setVisibility(View.INVISIBLE);
                    }
                    helper.EditRecord("AMOUNT",number,card.productid);
                }
            });
            return CustomInflater;
        }
    }
    }


