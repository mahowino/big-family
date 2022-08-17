package com.example.bigfamilyv20.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bigfamilyv20.Activities.ShoppingCart;
import com.example.bigfamilyv20.Activities.viewcart;
import com.example.bigfamilyv20.R;
import com.example.bigfamilyv20.Utils.Database_helper;
import com.example.bigfamilyv20.Utils.LoadingUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class shop_fragment extends Fragment {
    private static ListView view;
    private static List<String> p_names=new ArrayList<>();
    private static List<String> p_description=new ArrayList<>();
    private static List<String> p_price=new ArrayList<>();
    private static List<Long> p_prodId=new ArrayList<Long>();
    private static Database_helper database_helper, itemslist;
    private static final String collectionref="Products";
    private static int x=0;
    private static String name,desc,price;
    private static Long id;
    private static String[] sugg;
    private static FloatingActionButton button;
    private static ImageButton imageButton;
    private static AutoCompleteTextView Text;
    private static List<Bitmap> images=new ArrayList<>();
    private static List<String> urls=new ArrayList<>();

    public shop_fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myShopInflater= inflater.inflate(R.layout.activity_shopping_cart, container, false);
        view=(ListView)myShopInflater.findViewById(R.id.shopping_items_listview);
        button=(FloatingActionButton) myShopInflater.findViewById(R.id.view_cart);
        Text=(AutoCompleteTextView)myShopInflater.findViewById(R.id.autoCompletesearch);
       // imageButton=(ImageButton)myShopInflater.findViewById(R.id.imageButton2);
        /*imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchtext=Text.getText().toString();
                if(searchtext.isEmpty()){
                    Toast.makeText(getContext(),"enter an item to search",Toast.LENGTH_SHORT).show();

                }
                else {


                }
            }
        });*/

        final LoadingUtils utils=new LoadingUtils(
                getActivity());

        utils.startLoadingDialog();
        database_helper=new Database_helper(getContext());
        database_helper.clearTable();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection(collectionref);
        reference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){

                    x=0;
                    p_names.clear();
                    p_description.clear();
                    p_price.clear();

                    for (DocumentSnapshot snapshot: task.getResult()){

                        String NAME=snapshot.get("p_Name").toString();
                        String DESC=snapshot.get("p_Description").toString();
                        String PRICE=snapshot.get("p_Price").toString();
                        Long PROD_ID=snapshot.getLong("productCode");

                        p_names.add(x,NAME);
                        p_description.add(x,DESC);
                        p_price.add(x,PRICE);
                        p_prodId.add(x,PROD_ID);
                        urls.add(snapshot.get("image_url").toString());
                        //  images.add(getBitmapFromURL(snapshot.get("image_url").toString()));

                        //list.add(x,new ProductCard(NAME,DESC,PRICE));
                        // Log.d("size of list",String.valueOf(list.size()));

                        x++;
                    }
                    sugg=new String[p_names.size()];
                    for (int x=0;x<p_names.size();x++)
                    {
                        sugg[x]=p_names.get(x);
                    }

                   // ArrayAdapter<String> suggestionAdapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,sugg);
                    //Text.setAdapter(suggestionAdapter);
                    ListAdapter adapter=new shop_fragment.adapter();
                    view.setAdapter(adapter);
                    utils.dismissDialog();
                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //database_helper.getdata(db);
                Intent intent=new Intent(getContext(), viewcart.class);

                startActivity(intent);
            }
        });


        // Inflate the layout for this fragment
        return myShopInflater;
    }

    class ProductCarder {

        String Pname, Pdescription, Pprice;
        int Pamount;
        Long prodId;

        public ProductCarder(String pname, String pdescription, String pprice, int pamount) {
            this.Pname = pname;
            this.Pdescription = pdescription;
            this.Pprice = pprice;
            this.Pamount = pamount;
        }

        public ProductCarder(String pname, String pdescription, String pprice,Long prodId) {
            this.Pname = pname;
            this.Pdescription = pdescription;
            this.Pprice = pprice;
            this.prodId=prodId;

        }
    }
    class adapter extends BaseAdapter {
        ArrayList<shop_fragment.ProductCarder> list=new ArrayList<>();

        public adapter() {

            for (int i=0;i<p_names.size();i++){
                list.add(i,new shop_fragment.ProductCarder(p_names.get(i),p_description.get(i),p_price.get(i),p_prodId.get(i)));
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
            View custominflater=convertView;



            if (custominflater == null) {
                LayoutInflater layoutInflator = (LayoutInflater) getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                custominflater = layoutInflator.inflate(R.layout.shopping_card, parent, false);

            }
            TextView p_name = custominflater.findViewById(R.id.s_prodName);
            TextView p_desc = custominflater.findViewById(R.id.S_prodDesc);
            TextView p_price = custominflater.findViewById(R.id.s_prodPrice);

            final Button add_to_cart = custominflater.findViewById(R.id.add_to_cart_btn);

            final shop_fragment.ProductCarder card = list.get(position);
            // custominflater = getLayoutInflater().inflate(R.layout.shopping_card, null);
            Log.d("first",card.Pname);

             /*   ProductCard card = list.get(position);
                name=card.Pname;
                desc=card.Pdescription;
                price=card.Pprice;
                id=card.prodId;*/
            p_name.setText(card.Pname);
            p_desc.setText(card.Pdescription);
            p_price.setText("KSH "+card.Pprice);
            // final Database_helper dbhelp=new Database_helper(ShoppingCart.this);
            final Button finalAdd_to_cart = add_to_cart;
            add_to_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    String text=add_to_cart.getText().toString();
                    if(text.equalsIgnoreCase("Remove"))
                    {
                        new AlertDialog.Builder(getContext())
                                .setTitle("Remove?")
                                .setMessage("This will remove item from cart")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        database_helper.deleteRecord(card.prodId);
                                        Toast.makeText(getContext(), "Removed from cart", Toast.LENGTH_SHORT).show();
                                        finalAdd_to_cart.setText("Add to cart");
                                    }
                                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                            }
                        }).show();

                    }
                    else
                    {
                        boolean cart=database_helper.adddata(card.prodId,card.Pname, card.Pdescription, card.Pprice,1);
                        if(cart){
                            Toast.makeText(getContext(), "added to cart", Toast.LENGTH_SHORT).show();
                            finalAdd_to_cart.setText("Remove");
                        }
                        else {
                            Toast.makeText(getContext(), "Error adding", Toast.LENGTH_SHORT).show();
                            // finalAdd_to_cart.setText("Remove");
                        }


                    }



                }
            });



            return custominflater;
        }}
}