package com.example.feedforward_association.controllers;

import com.example.feedforward_association.interfaces.CallbackOrders;
import com.example.feedforward_association.interfaces.OrderApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OrdersController {
    private static final String BASE_URL = "";
    private CallbackOrders callbackOrders;
   private OrdersController setCallBackOrders(CallbackOrders callbackOrders) {
        this.callbackOrders = callbackOrders;
        return this;
    }
    private OrderApi getOrderApi() {
        Gson gson = new GsonBuilder()
                .setLenient() // TODO: check if this is needed
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        OrderApi orderApi = retrofit.create(OrderApi.class);
        return orderApi;
    }


}
