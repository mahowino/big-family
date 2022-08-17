package com.example.bigfamilyv20.Fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bigfamilyv20.Activities.ShoppingCart;
import com.example.bigfamilyv20.Entities.ProductCard;
import com.example.bigfamilyv20.R;
import com.example.bigfamilyv20.Utils.Database_helper;
import com.example.bigfamilyv20.Utils.LoadingUtils;
import com.example.bigfamilyv20.Utils.shoppingCartUtilis;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;


public class mainStore extends Fragment {


    private static List<Long> productId=new ArrayList<>();
    private static List<Integer> amount=new ArrayList<>();
    private static List<String> name=new ArrayList<>(),price=new ArrayList<>(),description=new ArrayList<>(),documentids=new ArrayList<>();
    private static ListView listview;
    private static FloatingActionButton add;


    public mainStore(List<Long> productId, List<String>name, List<String>price, List<String>description, List<Integer> amount,List<String> documentIds)
    {
        this.productId=productId;
        this.name=name;
        this.price=price;
        this.description=description;
        this.amount=amount;
        this.documentids=documentIds;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_basket, container, false);
        listview=(ListView)view.findViewById(R.id.store_items_store);
        add=(FloatingActionButton)view.findViewById(R.id.floatingActionButton2);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), ShoppingCart.class);
                startActivity(intent);
            }
        });
        add.setVisibility(View.INVISIBLE);
        DisplayList();
        return view;
    }

    private void DisplayList() {
        ListAdapter adapter=new adapter();
        listview.setAdapter(adapter);
    }
    class ProductCarder {

        String Pname, Pdescription, Pprice,DocumentIds;
        int Pamount;
        Long prodId;

        public ProductCarder(Long prodId,String pname, String pdescription, String pprice, int pamount,String DocumentIds) {
            this.prodId=prodId;
            this.Pname = pname;
            this.Pdescription = pdescription;
            this.Pprice = pprice;
            this.Pamount = pamount;
            this.DocumentIds=DocumentIds;
        }

        public ProductCarder(String pname, String pdescription, String pprice,Long prodId) {
            this.Pname = pname;
            this.Pdescription = pdescription;
            this.Pprice = pprice;
            this.prodId=prodId;

        }
    }
    class adapter extends BaseAdapter {
        ArrayList<ProductCarder> list = new ArrayList<>();

        public adapter() {

            for (int i = 0; i < name.size(); i++) {
                list.add(i, new ProductCarder(productId.get(i),name.get(i), description.get(i), price.get(i), amount.get(i),documentids.get(i)));
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

            View customView=convertView;
           if(convertView==null) {
               LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
               customView = inflater.inflate(R.layout.store_item, parent, false);
           }
           TextView g_name,g_desc,g_amount;
           final Button add_to_basket;
           g_name=(TextView) customView.findViewById(R.id.g_name);
           g_desc=(TextView)customView.findViewById(R.id.g_des);
           g_amount=(TextView)customView.findViewById(R.id.g_amount);
           add_to_basket=(Button)customView.findViewById(R.id.add_to_basket);
            final ProductCarder card=list.get(position);
            g_name.setText(card.Pname);
            g_desc.setText(card.Pdescription);
            g_amount.setText(card.Pamount +" items");
            shoppingCartUtilis util1=new shoppingCartUtilis(getContext());
            SQLiteDatabase db= util1.getReadableDatabase();
            String query="SELECT * FROM shoppingCart WHERE PRODUCTNO ="+card.prodId;
           Cursor cursor= util1.getdata(db,query);
           cursor.moveToFirst();
            if(!cursor.isAfterLast()){
              add_to_basket.setText("Remove");
            }
            else{
                add_to_basket.setText("send item");
            }

                add_to_basket.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   final shoppingCartUtilis utilis=new shoppingCartUtilis(getContext());
                   if (!add_to_basket.getText().toString().equalsIgnoreCase("Remove")){

                       boolean cart=utilis.adddata(card.prodId,card.Pname, card.Pdescription, card.Pprice,card.Pamount,card.DocumentIds);
                       if(cart){
                           Toast.makeText(getContext(), "added to package", Toast.LENGTH_SHORT).show();
                           add_to_basket.setText("Remove");
                       }
                       else {
                           Toast.makeText(getContext(), "Error removing", Toast.LENGTH_SHORT).show();
                           // finalAdd_to_cart.setText("Remove");
                       }

                   }
                   else {

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

                               utilis.deleteRecord(card.prodId);
                               Toast.makeText(getContext(), "Removed from basket", Toast.LENGTH_SHORT).show();
                               add_to_basket.setText("send item");
                           }
                       }).show();

                   }


               }
           });


           return customView;
        }
    }



}
