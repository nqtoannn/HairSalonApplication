package com.example.hairsalon;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgetPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        setControl();
        setEvent();
    }

    EditText edtEmail;
    Button bthSendEmail;
    TextView txtLogin;
    ScrollView scrollView;
    private void setControl(){
        edtEmail =findViewById(R.id.edtEmail);
        bthSendEmail =findViewById(R.id.btnSendEmail);
        txtLogin =findViewById(R.id.txtLogin);
        scrollView =findViewById(R.id.scrollView);
    }

    private void setEvent() {
        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgetPassword.this, Login.class);
                startActivity(intent);
            }
        });

        bthSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtEmail.getText().toString().isEmpty()){
                    Toast.makeText(ForgetPassword.this,"Vui lòng nhập email!",Toast.LENGTH_SHORT).show();
                }
                if(isValidEmail(edtEmail.getText().toString())){
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    String emailAddress = edtEmail.getText().toString();
                    auth.sendPasswordResetEmail(emailAddress)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "Email sent.");
                                        Toast.makeText(ForgetPassword.this,"Vui lòng nhập kiểm tra email!",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(ForgetPassword.this, Login.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(ForgetPassword.this,"Có lỗi khi gửi email. Vui lòng thử lại sau!",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(ForgetPassword.this,"Vui lòng nhập email hợp lệ!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        edtEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            int y= edtEmail.getBottom() - 100;
                            scrollView.scrollTo(0, bthSendEmail.getTop());
                        }
                    });
                }
            }
        });
    }
    private boolean isValidEmail(CharSequence target) {
        return (target != null && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

}