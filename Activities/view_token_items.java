package com.example.bigfamilyv20.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bigfamilyv20.Entities.namesArray;
import com.example.bigfamilyv20.R;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class view_token_items extends AppCompatActivity {
    private static List<String> p_names=new ArrayList<>(),p_documentsIds=new ArrayList<>();
    private static List<String> p_description=new ArrayList<>();
    private static List<String> p_price=new ArrayList<>();
    private static List<Long> p_prodId=new ArrayList<Long>();
    private static List<Integer> p_amount=new ArrayList<>();
    private static String Docs,pass;
    private static Button next;
    private static ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_token_items);
        p_names= namesArray.getName();
        p_description= namesArray.getDescription();
        p_documentsIds= namesArray.getDocIds();
        p_price= namesArray.getPrice();
        p_prodId= namesArray.getProductId();
        p_amount= namesArray.getAmount();
        listView=(ListView)findViewById(R.id.list_of_items_token);
        ListAdapter adapter=new adapter();
        listView.setAdapter(adapter);
        next=(Button)findViewById(R.id.token_button);
        Docs=getIntent().getExtras().getString("docs");
        pass=getIntent().getExtras().getString("pass");
       next.setOnClickListener(new View.OnClickListener() {
        @Override
    public void onClick(View v) {
            Intent intent=new Intent(getApplicationContext(),Cart_Activity.class);

            intent.putExtra("charge","goods withdrawal");
            intent.putExtra("price",String.valueOf(30));
            intent.putExtra("reason","token_withdrawal");
            intent.putExtra("docs",Docs);
            intent.putExtra("pass",pass);
            startActivity(intent);
            finish();
     }
   });

    }
    class itemsToAdd{
        String name,description,documentIds,price;
        Long prodId;
        int amount;

        public itemsToAdd(String name, String description, String documentIds, String price,Long prodId,int amount) {
            this.name = name;
            this.description = description;
            this.documentIds = documentIds;
            this.price = price;
            this.prodId=prodId;
            this.amount=amount;
        }
    }

    class adapter extends BaseAdapter{
       ArrayList<itemsToAdd> card=new ArrayList();
        public adapter() {
            for (int x=0;x<p_names.size();x++){
            card.add(new itemsToAdd(p_names.get(x),p_description.get(x),p_documentsIds.get(x),p_price.get(x),p_prodId.get(x),p_amount.get(x)));

            }
        }

        @Override
        public int getCount() {
            return card.size();
        }

        @Override
        public Object getItem(int position) {
            return card.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View custominflater=convertView;


            if (custominflater == null) {
                LayoutInflater layoutInflator = (LayoutInflater) view_token_items.this
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                custominflater = layoutInflator.inflate(R.layout.withdraw_card, parent, false);
            }
            itemsToAdd add=card.get(position);
            TextView name=(TextView)custominflater.findViewById(R.id.text_name_withdraw);
            TextView desc=(TextView)custominflater.findViewById(R.id.text_desc_withdraw);
            TextView price=(TextView)custominflater.findViewById(R.id.text_price_withdraw);
            TextView amou=(TextView)custominflater.findViewById(R.id.text_amount_withdraw);
            name.setText(add.name);
            desc.setText(add.description);
            price.setText(add.price);
            amou.setText(String.valueOf(add.amount));


            return custominflater;
        }
    }
}
