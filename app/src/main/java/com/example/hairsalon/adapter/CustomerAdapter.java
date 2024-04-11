package com.example.hairsalon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.hairsalon.R;
import com.example.hairsalon.model.Customer;
import com.example.hairsalon.model.ProductItem;

import java.util.ArrayList;

public class CustomerAdapter extends ArrayAdapter<Customer> {
    public CustomerAdapter(@NonNull Context context, ArrayList<Customer> dataArrayList) {
        super(context, R.layout.list_customer, dataArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        Customer listData = getItem(position);

        if (view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_customer, parent, false);
        }

        ImageView listImage = view.findViewById(R.id.listImage);
        TextView listName = view.findViewById(R.id.listName);
        //TextView listQuantity = view.findViewById(R.id.listQuantity);

//        listImage.setImageResource(listData.image);z
        listName.setText(listData.getName());

        return view;
    }
}
