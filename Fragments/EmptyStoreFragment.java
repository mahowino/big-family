package com.example.bigfamilyv20.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.bigfamilyv20.Activities.ShoppingCart;
import com.example.bigfamilyv20.R;



public class EmptyStoreFragment extends Fragment {
private static Button newItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View m= inflater.inflate(R.layout.fragment_empty_basket, container, false);
        // Inflate the layout for this fragment
        newItems=(Button) m.findViewById(R.id.btn_add_itens);
        newItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), ShoppingCart.class);
                startActivity(intent);
            }
        });

        return m;
    }


}
