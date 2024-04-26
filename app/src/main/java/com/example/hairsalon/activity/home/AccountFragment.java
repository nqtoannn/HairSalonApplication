package com.example.hairsalon.activity.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.hairsalon.R;
import com.example.hairsalon.activity.auth.Login;
import com.google.firebase.auth.FirebaseAuth;


public class AccountFragment extends Fragment {


    private Button btnLogOut;
    private TextView txtUsername;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        setControl(view);
        setEvent();
        return view;
    }

    private void setEvent() {
        mAuth = FirebaseAuth.getInstance();
        mAuth.getCurrentUser().getUid();
        txtUsername.setText("User UID: " + mAuth.getCurrentUser().getUid());
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                // Chuyển sang màn hình đăng nhập
                getActivity().startActivity(new Intent(getActivity(), Login.class));
                getActivity().finish(); // Kết thúc Fragment hiện tại nếu cần
            }
        });
    }

    private void setControl(View view) {
        btnLogOut = view.findViewById(R.id.btnLogOut);
        txtUsername = view.findViewById(R.id.username);
    }
}