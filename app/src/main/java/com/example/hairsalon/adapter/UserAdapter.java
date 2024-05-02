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
import com.example.hairsalon.model.User;


import java.util.ArrayList;

public class UserAdapter extends ArrayAdapter<User> {
    public UserAdapter(@NonNull Context context, ArrayList<User> dataArrayList) {
        super(context, R.layout.list_customer, dataArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        User userData = getItem(position);
        if (view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_customer, parent, false);
        }
        int userId = userData.getId();
        ImageView listImage = view.findViewById(R.id.listImage);
        TextView listName = view.findViewById(R.id.listName);
        listName.setText(userData.getUserName());
        return view;
    }
}
