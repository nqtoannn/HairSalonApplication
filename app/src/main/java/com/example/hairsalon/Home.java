package com.example.hairsalon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity {
    Button btnLogOut;
    TextView txtUsername;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setControl();
        setEvent();
    }

    private void setEvent() {
        mAuth = FirebaseAuth.getInstance();
        mAuth.getCurrentUser().getUid();
        txtUsername.setText("User UID: "+mAuth.getCurrentUser().getUid().toString());
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Home.this, Login.class);
                startActivity(intent);
            }
        });
    }

    private void setControl() {
        btnLogOut = findViewById(R.id.btnLogOut);
        txtUsername =findViewById(R.id.username);
    }


}