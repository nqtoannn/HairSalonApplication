package com.example.hairsalon.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.hairsalon.R;
import com.example.hairsalon.activity.auth.Login;
import com.google.firebase.auth.FirebaseAuth;

public class HomeFragment extends Fragment {
    private Button btnLogOut;
    private TextView txtUsername;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setControl(view);
        setEvent();
        return view;
    }

    private void setEvent() {

    }

    private void setControl(View view) {

    }
}
