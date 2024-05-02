package com.example.hairsalon.activity.servicehair;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.hairsalon.R;
import com.example.hairsalon.activity.appointment.AppointmentActivity;
import com.example.hairsalon.activity.home.BookingFragment;
import com.example.hairsalon.databinding.ActivityDetailServiceHairBinding;
import com.example.hairsalon.utils.Utils;

public class DetailServiceHairActivity extends AppCompatActivity {

    ActivityDetailServiceHairBinding binding;

    private Integer serviceHairId;
    private String name, imageUrl;
    private Double price;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailServiceHairBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        if (intent != null) {
            serviceHairId = intent.getIntExtra("serviceHairId", 0);
            name = intent.getStringExtra("detailName");
            price = intent.getDoubleExtra("detailPrice", 0.0);
            String description = intent.getStringExtra("detailDescription");
            imageUrl = intent.getStringExtra("imageUrl");
            serviceHairId = intent.getIntExtra("serviceHairId", 0);
            binding.textProductName.setText(name);
            binding.textProductDescription.setText(description);;
            binding.textProductPrice.setText(Utils.formatPrice(price));
            if (requestQueue == null) {
                requestQueue = Volley.newRequestQueue(this);
            }
            ImageRequest imageRequest = new ImageRequest(
                    imageUrl,
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            binding.imageServiceHair.setImageBitmap(response);
                        }
                    },
                    0,
                    0,
                    ImageView.ScaleType.CENTER_CROP,
                    Bitmap.Config.RGB_565,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Handle error if necessary
                        }
                    }
            );
            requestQueue.add(imageRequest);
        }

        binding.btnMakeApm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(DetailServiceHairActivity.this, AppointmentActivity.class);
                intent2.putExtra("serviceHairId", serviceHairId);
                intent2.putExtra("serviceName", name);
                startActivity(intent2);
            }
        });

    }
}