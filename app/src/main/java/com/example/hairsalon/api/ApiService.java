package com.example.hairsalon.api;

import com.example.hairsalon.constants.Constant;
import com.example.hairsalon.model.ResponseData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {

    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    ApiService apiService = new Retrofit.Builder()
            .baseUrl(Constant.baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);

    @GET("productItem/findAll")
    Call<ResponseData> getProductItem();

    @GET("customer/findAllCartItems/{cartId}")
    Call<ResponseData> getAllCartItemsByCartId(@Path("cartId") Integer cartId);

    @GET("appointments/{customerId}")
    Call<ResponseData> getAllAppointmentByCustomerId(@Path("customerId") Integer customerId);


    @DELETE("customer/deleteAllCartItemByCartId/{cartId}")
    Call<Void> deleteAllCartItemsByCartId(@Path("cartId") Integer cartId);



}
