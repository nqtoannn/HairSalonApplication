package com.example.hairsalon.activity.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hairsalon.R;
import com.example.hairsalon.activity.employee.HomeEmployee;
import com.example.hairsalon.activity.home.HomeCustomer;
import com.example.hairsalon.activity.home.HomeManage;
import com.example.hairsalon.api.ApiService;
import com.example.hairsalon.model.AuthenticationRequest;
import com.example.hairsalon.model.ResponseAuthData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminLogin extends AppCompatActivity {

    EditText edtEmail, edtPassword;
    AppCompatButton btnLogin;
    TextView txtCusLogin;
    ImageView passwordIcon;

    private boolean passwordShow = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        setControl();
        setEvent();
    }
    private void setControl(){
        edtEmail = findViewById(R.id.edtEmailAd);
        edtPassword = findViewById(R.id.edtPasswordAd);
        btnLogin = findViewById(R.id.btnLoginAd);
        txtCusLogin = findViewById(R.id.txtCusLogin);
        passwordIcon = findViewById(R.id.passwordIcon);
    }


    private  void setEvent(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        txtCusLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminLogin.this, Login.class);
                startActivity(intent);
            }
        });
        passwordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(passwordShow){
                    passwordShow = false;
                    edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordIcon.setImageResource(R.drawable.show_password);
                }
                else {
                    passwordShow = true;
                    edtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordIcon.setImageResource(R.drawable.hide_password);
                }
                edtPassword.setSelection(edtPassword.length());
            }
        });
    }

    private void login() {
        String email, pass;
        email = edtEmail.getText().toString();
        pass = edtPassword.getText().toString();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Vui lòng nhập email!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!isValidEmail(email)){
            Toast.makeText(this, "Vui lòng nhập email hợp lệ!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(pass)){
            Toast.makeText(this, "Vui lòng nhập mật khẩu!",Toast.LENGTH_SHORT).show();
            return;
        }
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(email,pass);
        ApiService.apiService.authenticateUser(authenticationRequest).enqueue(new Callback<ResponseAuthData>() {
            @Override
            public void onResponse(Call<ResponseAuthData> call, Response<ResponseAuthData> response) {
                if (response.isSuccessful()) {
                    ResponseAuthData responseAuthData = response.body();
                    SharedPreferences prefs = getSharedPreferences("User", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("userId", responseAuthData.getAccountId());
                    editor.apply();
                    if(responseAuthData.getRole().equals("ADMIN")) {
                        Toast.makeText( AdminLogin.this, "Đăng nhập thành công!",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AdminLogin.this, HomeManage.class);
                        startActivity(intent);
                    } else if (responseAuthData.getRole().equals("CUSTOMER")){
                        Toast.makeText( AdminLogin.this, "Vui lòng đăng nhập khách hàng!",Toast.LENGTH_SHORT).show();
                    }
                    else if (responseAuthData.getRole().equals("EMPLOYEE")){
                        Toast.makeText( AdminLogin.this, "Đăng nhập thành công!",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AdminLogin.this, HomeEmployee.class);
                        startActivity(intent);
                    }
                    Log.e("Error", "login complete");

                } else {
                    // Log thông báo "login failed"
                    Log.e("Error", "login failed: ");
                }
            }
            @Override
            public void onFailure(Call<ResponseAuthData> call, Throwable t) {
                Log.e("Error", "API call failed: " + t.getMessage());
            }
        });
    }

    private boolean isValidEmail(CharSequence target) {
        return (target != null && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

}