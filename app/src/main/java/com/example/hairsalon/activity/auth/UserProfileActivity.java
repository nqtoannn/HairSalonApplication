//package com.example.hairsalon.activity.auth;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.hairsalon.R;
//import com.example.hairsalon.model.UserDetails;
//import com.google.firebase.Firebase;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.Objects;
//
//public class UserProfileActivity extends AppCompatActivity
//{
//    private TextView textViewWelcome, textViewName, textViewEmail, textViewDob, textViewPhone;
//    private ProgressBar progressBar;
//    private String fullName,email, doB,phone;
//    private ImageView imgView;
//
//    @Override
//    protected void onCreate(Bundle saveInstanceState){
//        super.onCreate(saveInstanceState);
//        setContentView(R.layout.activiti_user_information);
//        Objects.requireNonNull(getSupportActionBar()).setTitle("Home");
//
//        textViewWelcome = findViewById(R.id.textView_show_welcome);
//        textViewName =findViewById(R.id.textView_show_full_name);
//        textViewEmail =findViewById(R.id.textView_show_email);
//        textViewDob =findViewById(R.id.textView_show_dob);
//        textViewPhone =findViewById(R.id.textView_show_phone);
//
//        FirebaseAuth authProfile = FirebaseAuth.getInstance();
//        FirebaseUser firebaseUser = authProfile.getCurrentUser();
//
//        if(firebaseUser == null)
//        {
//            Toast.makeText(UserProfileActivity.this,
//                    "Something went wrong! User's detail are not available at the moment",
//                    Toast.LENGTH_LONG).show();
//        }else {
//            progressBar.setVisibility(View.VISIBLE);
//            showUserProfile(firebaseUser);
//        }
//
//
//    }
//
//    private void showUserProfile(FirebaseUser firebaseUser) {
//        String userID = firebaseUser.getUid();
//
//        //Extracting User Ref from DB for 'Registered Users'
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Registered Users");
//        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                UserDetails readUserDetails = snapshot.getValue(UserDetails.class);
//                if(readUserDetails!=null)
//                {
//                fullName = firebaseUser.getDisplayName();
//                email = firebaseUser.getEmail();
//                doB = readUserDetails.doB;
//                phone = readUserDetails.phone;
//
//                textViewWelcome.setText("Welcom, " + fullName +"!");
//                textViewName.setText(fullName);
//                textViewEmail.setText(email);
//                textViewDob.setText(doB);
//                textViewPhone.setText(phone);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(UserProfileActivity.this,"Something went wrong!",Toast.LENGTH_LONG).show();
//                progressBar.setVisibility(View.GONE);
//            }
//        });
//    }
//
//}
