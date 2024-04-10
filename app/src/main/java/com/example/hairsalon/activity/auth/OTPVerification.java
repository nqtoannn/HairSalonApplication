package com.example.hairsalon.activity.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hairsalon.R;
import com.example.hairsalon.activity.home.Home;
import com.example.hairsalon.api.ApiService;
import com.example.hairsalon.model.AuthenticationRequest;
import com.example.hairsalon.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import org.checkerframework.checker.units.qual.A;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPVerification extends AppCompatActivity {

    EditText otp1, otp2, otp3, otp4, otp5, otp6;
    TextView txtResend, txtPhoneNo;
    AppCompatButton btnConfirm;
    private boolean resendEnabled = false;
    private int resendTime = 60;
    private int seclectedPosition =0;
    private FirebaseAuth mAuth;
    private String verifiID, phoneNumber;
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        phoneNumber = phoneNumber.substring(1);
        setControl();
        txtPhoneNo.setText("+84 "+ phoneNumber);
        setEvent();
        sendOTP(phoneNumber);
    }

    private void setControl() {
        otp1 = findViewById(R.id.otp1);
        otp2 = findViewById(R.id.otp2);
        otp3 = findViewById(R.id.otp3);
        otp4 = findViewById(R.id.otp4);
        otp5 = findViewById(R.id.otp5);
        otp6 = findViewById(R.id.otp6);
        txtResend = findViewById(R.id.txtResend);
        txtPhoneNo = findViewById(R.id.txtPhoneNo);
        btnConfirm = findViewById(R.id.btnConfirm);
        scrollView =findViewById(R.id.scrollView);
    }

    private void setEvent(){
        otp1.addTextChangedListener(textWatcher);
        otp2.addTextChangedListener(textWatcher);
        otp3.addTextChangedListener(textWatcher);
        otp4.addTextChangedListener(textWatcher);
        otp5.addTextChangedListener(textWatcher);
        otp6.addTextChangedListener(textWatcher);
        showKeyboard(otp1);
        otp1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.scrollTo(0, btnConfirm.getBottom());
                        }
                    });
                }
            }
        });
        startCountDownTimer();
        txtResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(resendEnabled){
                    //Resend code here
                    sendOTP(phoneNumber);
                    startCountDownTimer();
                }
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String otp = otp1.getText().toString() +otp2.getText().toString() +otp3.getText().toString() +otp4.getText().toString()+ otp5.getText().toString()+ otp6.getText().toString();
                if(otp.length()==6){
                    PhoneAuthCredential credential =PhoneAuthProvider.getCredential(verifiID,otp);
                    singInByCredentiel(credential);
                }
            }
        });

    }

    private void singInByCredentiel(PhoneAuthCredential credential) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(OTPVerification.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    AuthenticationRequest authenticationRequest = new AuthenticationRequest(mAuth.getCurrentUser().getPhoneNumber(),mAuth.getCurrentUser().getUid());
                    ApiService.apiService.authenticateUser(authenticationRequest).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                startActivity(new Intent(OTPVerification.this, Home.class));
                                Log.e("Error", response.body().toString());
                            } else {
                                User user = new User(mAuth.getCurrentUser().getPhoneNumber(),mAuth.getCurrentUser().getUid(),"CUSTOMER",mAuth.getCurrentUser().getPhoneNumber());
                                ApiService.apiService.registerUser(user).enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        AuthenticationRequest authenticationRequest = new AuthenticationRequest(mAuth.getCurrentUser().getPhoneNumber(),mAuth.getCurrentUser().getUid());
                                        ApiService.apiService.authenticateUser(authenticationRequest).enqueue(new Callback<Void>() {
                                            @Override
                                            public void onResponse(Call<Void> call, Response<Void> response) {
                                                Log.e("Error", "Login done");
                                                startActivity(new Intent(OTPVerification.this, Home.class));
                                            }

                                            @Override
                                            public void onFailure(Call<Void> call, Throwable t) {
                                                Log.e("Error", t.getMessage().toString());
                                            }
                                        });
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {

                                    }
                                });
                            }
                        }
                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.e("Error", "API call failed: " + t.getMessage());
                        }
                    });
                    startActivity(new Intent(OTPVerification.this, Home.class));
                }else {
                    Toast.makeText(OTPVerification.this, "Mã OTP không hợp lệ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showKeyboard(EditText otpET){
        otpET.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(otpET, InputMethodManager.SHOW_IMPLICIT);
    }

    private  void startCountDownTimer(){
        resendEnabled = false;
        txtResend.setTextColor(Color.parseColor("#99000000"));
        new CountDownTimer(resendTime * 1000,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                txtResend.setText("Gửi lại trong ("+(millisUntilFinished/1000)+")");
            }

            @Override
            public void onFinish() {
                resendEnabled = true;
                txtResend.setText("Gửi lại mã!");
                txtResend.setTextColor(getResources().getColor(R.color.primary));
            }
        }.start();
    }
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(s.length()>0){
                if(seclectedPosition == 0 ){
                    seclectedPosition =1;
                    showKeyboard(otp2);
                }else if(seclectedPosition == 1 ){
                    seclectedPosition =2;
                    showKeyboard(otp3);
                }else if(seclectedPosition == 2 ){
                    seclectedPosition =3;
                    showKeyboard(otp4);
                }else if(seclectedPosition == 3 ){
                    seclectedPosition =4;
                    showKeyboard(otp5);
                }else if(seclectedPosition == 4 ){
                    seclectedPosition =5;
                    showKeyboard(otp6);
                }
            }
        }
    };

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event){
        if (keyCode ==KeyEvent.KEYCODE_DEL){

            if (seclectedPosition == 5 && otp6.getText().toString().equals("")){
                seclectedPosition = 4;
                showKeyboard(otp5);
            } else if (seclectedPosition == 4 && otp5.getText().toString().equals("")) {
                seclectedPosition = 3;
                showKeyboard(otp4);
            } else if (seclectedPosition == 3 && otp4.getText().toString().equals("")) {
                seclectedPosition=2;
                showKeyboard(otp3);
            } else if (seclectedPosition == 2 && otp3.getText().toString().equals("")) {
                seclectedPosition=1;
                showKeyboard(otp2);
            } else if (seclectedPosition == 1 && otp2.getText().toString().equals("")) {
                seclectedPosition=0;
                showKeyboard(otp1);
            }
            return true;
        }
        else {
        return super.onKeyUp(keyCode,event);
        }
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(OTPVerification.this,e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            super.onCodeSent(verificationId,token);
            verifiID = verificationId;
        }
    };

    private void sendOTP(String phone){
        mAuth = FirebaseAuth.getInstance();
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+84"+phone)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // (optional) Activity for callback binding
                        // If no activity is passed, reCAPTCHA verification can not be used.
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }


}