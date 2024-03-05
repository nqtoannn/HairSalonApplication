package com.example.hairsalon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginWithOtp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_with_otp);
        setControl();
        setEvent();
    }

    EditText edtPhoneNo;
    Button btnConfirm;
    TextView txtLogin;
    ScrollView scrollView;
    private void setControl(){
        edtPhoneNo =findViewById(R.id.edtPhoneNo);
        btnConfirm =findViewById(R.id.btnSendOtp);
        txtLogin =findViewById(R.id.txtLogin);
        scrollView =findViewById(R.id.scrollView);
    }

    private void setEvent() {
        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginWithOtp.this, Login.class);
                startActivity(intent);
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidPhoneNumber(edtPhoneNo.getText().toString())){
                    final String phoneNumber = edtPhoneNo.getText().toString();
                    Intent intent = new Intent(LoginWithOtp.this, OTPVerification.class);
                    intent.putExtra("phoneNumber", phoneNumber);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginWithOtp.this, "Vui lòng nhập số điện thoại hợp lệ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        edtPhoneNo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            int y= edtPhoneNo.getBottom() - 100;
                            scrollView.scrollTo(0, btnConfirm.getTop());
                        }
                    });
                }
            }
        });
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "^0\\d{9}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

}